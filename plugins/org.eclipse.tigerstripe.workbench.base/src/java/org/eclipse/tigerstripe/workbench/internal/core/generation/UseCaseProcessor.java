/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.generation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.contract.useCase.IUseCase;
import org.eclipse.tigerstripe.workbench.internal.api.contract.useCase.IUseCaseReference;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.generation.rendering.DiagramRenderer;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * Processor for Usecases during a generation run.
 * 
 * @author Eric Dillon
 * 
 */
public class UseCaseProcessor {

	private ITigerstripeModelProject project;

	private RunConfig config = null;

	private DiagramRenderer renderer;

	public UseCaseProcessor(ITigerstripeModelProject project) {
		this(project, null);
	}

	public UseCaseProcessor(ITigerstripeModelProject project, RunConfig config) {
		this.project = project;
		this.config = config;
		this.renderer = new DiagramRenderer(project);
	}

	public UseCaseProcessingResult[] run() throws TigerstripeException {

		List<UseCaseProcessingResult> result = new ArrayList<UseCaseProcessingResult>();

		// If there is an active facet at this stage, simply use the use cases
		// for that facet, otherwise use all use cases.
		if (project.getActiveFacet() != null
				&& project.getActiveFacet().canResolve()) {
			IContractSegment facetRef = project.getActiveFacet().resolve();
			result.addAll(Arrays.asList(process(facetRef.getUseCaseRefs(),
					project.getActiveFacet())));
		} else {
			// just look at all the
			result.addAll(Arrays.asList(processAllInProject()));
		}

		// at this stage, the renderer as accumulated a list of all the diags
		// that
		// were referenced from the use case bodies.
		renderer.renderDiagrams();

		return result.toArray(new UseCaseProcessingResult[result.size()]);
	}

	private void getAllUseCases(File baseDir, List<String> relPaths) {
		if (baseDir != null && baseDir.isDirectory()) {
			File[] content = baseDir.listFiles();
			for (File file : content) {
				if (file != null
						&& file.isFile()
						&& file.getName().endsWith(
								"." + IUseCase.FILE_EXTENSION)) {
					// we found one
					try {
						String relPath = Util.getRelativePath(file, project
								.getLocation().toFile());
						relPaths.add(relPath);
					} catch (IOException e) {
						TigerstripeRuntime.logErrorMessage(
								"IOException detected", e);
					}
				} else if (file != null && file.isDirectory()) {
					getAllUseCases(file, relPaths);
				}
			}
		}
	}

	private UseCaseProcessingResult[] processAllInProject()
			throws TigerstripeException {
		List<IUseCaseReference> allUseCases = new ArrayList<IUseCaseReference>();

		// gather all use case files in the project
		List<String> relPaths = new ArrayList<String>();
		File baseDir = project.getLocation().toFile();
		getAllUseCases(baseDir, relPaths);
		for (String relPath : relPaths) {
			IUseCaseReference ref = project.makeUseCaseReference(relPath);
			allUseCases.add(ref);
		}

		return process(allUseCases.toArray(new IUseCaseReference[allUseCases
				.size()]), null);
	}

	private UseCaseProcessingResult[] process(IUseCaseReference[] references,
			IFacetReference facet) throws TigerstripeException {
		List<UseCaseProcessingResult> result = new ArrayList<UseCaseProcessingResult>();

		for (IUseCaseReference ref : references) {
			UseCaseProcessingResult res = process(ref, facet);
			result.add(res);
		}

		return result.toArray(new UseCaseProcessingResult[result.size()]);
	}

	private int getDepth(IUseCaseReference ref) {
		int result = 0;
		if (ref.canResolve()) {
			String path = ref.getProjectRelativePath().replace('\\', '/');
			for (char c : path.toCharArray()) {
				if (c == '/') {
					result++;
				}
			}
		}
		return result;
	}

	private UseCaseProcessingResult process(IUseCaseReference ref,
			IFacetReference facet) throws TigerstripeException {
		UseCaseProcessingResult result = new UseCaseProcessingResult(ref,
				project, config, facet);

		if (ref.canResolve()) {
			// TigerstripeRuntime.logInfoMessage("Processing " +
			// ref.getProjectRelativePath());
		} else {
			result.add(new Status(IStatus.ERROR, BasePlugin.getPluginId(),
					"Can't find use case: " + ref.getProjectRelativePath()));
		}

		String body = ref.resolve().getBody();

		// Processing a use case means:
		// - running the body as a Velocity Template
		// - creating a file with a .txt extension?
		// - optionally running an XSL on the result.

		// First let's create the output dir for use cases

		VelocityEngine engine = new VelocityEngine();
		VelocityContext context = createContext();
		renderer.setFileDepth(getDepth(ref));
		StringWriter w = new StringWriter();
		FileWriter f = null;
		StringReader reader = null;
		File xslFile = null;

		try {
			engine.init();
			engine.evaluate(context, w, ref.getProjectRelativePath(), body);

			String outputDir = config.getAbsoluteOutputDir();
			if (outputDir == null) {
				outputDir = project.getProjectDetails().getOutputDirectory();
			}
			String relOutputPath = project.getLocation().toFile()
					+ File.separator + outputDir + File.separator;
			int i = ref.getProjectRelativePath().lastIndexOf(
					IUseCase.FILE_EXTENSION);
			if (i == -1) {
				relOutputPath += ref.getProjectRelativePath() + "."
						+ config.getProcessedUseCaseExtension();
			} else {
				relOutputPath += ref.getProjectRelativePath().substring(0, i)
						+ config.getProcessedUseCaseExtension();
			}

			File tmp = new File(relOutputPath);
			if (!tmp.getParentFile().exists()) {
				tmp.getParentFile().mkdirs();
			}
			f = new FileWriter(relOutputPath);

			String xsl = null;
			if (ref.resolve().getXslRelPath() != null) {
				xsl = ref.resolve().getXslRelPath();
			} else if (config.getUseCaseXSL() != null) {
				xsl = config.getUseCaseXSL();
			}

			if (xsl != null) {
				File tmpxslFile = new File(xsl);
				if (tmpxslFile.exists() && tmpxslFile.isFile()) {
					xslFile = tmpxslFile;
				}
			}

			String beforeXsl = w.toString();
			if (xslFile != null) {
				TransformerFactory tFactory = TransformerFactory.newInstance();
				Transformer transformer = tFactory
						.newTransformer(new StreamSource(xslFile));
				reader = new StringReader(beforeXsl);
				transformer.transform(new StreamSource(reader),
						new StreamResult(f));
			} else {
				f.write(beforeXsl);
			}
		} catch (IOException e) {
			IStatus error = new Status(
					IStatus.ERROR,
					BasePlugin.getPluginId(),
					"While processing use case:" + ref.getProjectRelativePath(),
					e);
			result.add(error);
		} catch (Exception e) {
			IStatus error = new Status(
					IStatus.ERROR,
					BasePlugin.getPluginId(),
					"While processing use case:" + ref.getProjectRelativePath(),
					e);
			result.add(error);
		} finally {
			if (f != null) {
				try {
					f.close();
				} catch (IOException e) {
					// ignore
				}
			}

			if (reader != null) {
				reader.close();
			}
		}

		return result;
	}

	/**
	 * Creates a default context to use when evaluating the body of a use case
	 * 
	 * @return
	 */
	private VelocityContext createContext() {
		VelocityContext context = new VelocityContext();
		context.put("diagram", renderer);

		return context;
	}
}
