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
package org.eclipse.tigerstripe.workbench.internal.core.module;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.project.ProjectDetails;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaSource;

/**
 * A Descriptor model is a reference on the XML descriptor for the module. It
 * can be read/written/signed.
 * 
 * @author Eric Dillon
 * @since 0.4
 */
public class ModuleDescriptorModel {

	// The corresponding project
	private ITigerstripeModelProject tsProject;

	private ProjectDetails details;

	private ModuleHeader header;

	private ModuleArtifactManager artifactMgr;

	private TigerstripeProject embeddedProject;

	// The default name for the XML descriptor
	public final static String DESCRIPTOR = "ts-module.xml";

	public ModuleDescriptorModel(ITigerstripeModelProject project) {
		setTSProject(project);
	}

	public ModuleDescriptorModel(TigerstripeProject embeddedProject,
			Reader reader, boolean parseArtifacts, IProgressMonitor monitor)
			throws InvalidModuleException {
		this.embeddedProject = embeddedProject;
		details = new ProjectDetails(null);
		header = new ModuleHeader();
		readModel(reader, parseArtifacts, monitor);
	}

	public TigerstripeProject getEmbeddedProject() {
		return this.embeddedProject;
	}

	// ============================================================
	public ProjectDetails getProjectDetails() {
		return this.details;
	}

	public ArtifactManager getArtifactManager() {
		return this.artifactMgr;
	}

	private ITigerstripeModelProject getTSProject() {
		return this.tsProject;
	}

	protected void setTSProject(ITigerstripeModelProject project) {
		this.tsProject = project;
	}

	public ModuleHeader getModuleHeader() {
		return this.header;
	}

	public void setModuleHeader(ModuleHeader header) {
		this.header = header;
	}

	/**
	 * Reads the module descriptor model from the given reader
	 * 
	 * @param reader
	 */
	private void readModel(Reader reader, boolean parseArtifacts,
			IProgressMonitor monitor) throws InvalidModuleException {
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(reader);
			details = extractProjectDetails(document);
			header = extractModuleHeader(document);

			// This is for compatibility reasons. If no originalName was set
			if (header.getOriginalName() == null) {
				Element root = document.getRootElement();
				Element details = root.element("details");

				if (details != null) {
					Node node = details;
					String name = ((Element) node).attributeValue("name");
					header.setOriginalName(name);
				}
			}

			if (parseArtifacts)
				extractArtifacts(document, monitor);

		} catch (DocumentException e) {
			TigerstripeRuntime.logErrorMessage("DocumentException detected", e);
			throw new InvalidModuleException("DocumentException detected: "
					+ e.getMessage(), e);
		} catch (InvalidModuleException e) {
			TigerstripeRuntime.logErrorMessage(
					"InvalidModuleException detected", e);
			throw e;
		}
	}

	/**
	 * Writes this module descriptor model to the given writer
	 * 
	 */
	public void writeModel(Writer writer, IProgressMonitor monitor)
			throws TigerstripeException {

		Document document = createXMLDocument(monitor);
		try {
			OutputFormat outformat = new OutputFormat("  ", true);
			XMLWriter xmlWriter = new XMLWriter(writer, outformat);
			xmlWriter.write(document);
			xmlWriter.close();
		} catch (Exception e) {
			throw new TigerstripeException(
					"An error occured while trying to write Module Descriptor.",
					e);
		}
	}

	private Document createXMLDocument(IProgressMonitor monitor)
			throws TigerstripeException {
		Document document = DocumentHelper.createDocument();
		Element module = document.addElement("module");
		createHeader(module, monitor);
		createDetails(module, monitor);
		createArtifacts(module, monitor);

		return document;
	}

	private void createHeader(Element module, IProgressMonitor monitor) {
		Element hElem = module.addElement("header");

		hElem.addAttribute("originalName", getTSProject().getName());
		hElem.addAttribute("moduleID", header.getModuleID());
		hElem.addAttribute("build", TigerstripeRuntime
				.getProperty(TigerstripeRuntime.TIGERSTRIPE_FEATURE_VERSION));
		DateFormat format = new SimpleDateFormat("yyyy.MM.dd-hh.mm");
		String dateStr = format.format(new Date());
		hElem.addAttribute("date", dateStr);
	}

	private void createDetails(Element module, IProgressMonitor monitor)
			throws TigerstripeException {
		Element details = module.addElement("details");

		details.addAttribute("name", getTSProject().getName());
		details.addAttribute("version", getTSProject().getProjectDetails()
				.getVersion());
		Element description = details.addElement("description");
		description
				.addText(getTSProject().getProjectDetails().getDescription());
	}

	private void createArtifacts(Element module, IProgressMonitor monitor)
			throws TigerstripeException {
		Element artifacts = module.addElement("artifacts");

		IArtifactManagerSession session = getTSProject()
				.getArtifactManagerSession();
		IQueryAllArtifacts query = (IQueryAllArtifacts) session
				.makeQuery(IQueryAllArtifacts.class.getName());
		query.setIncludeDependencies(false); // we only package the locally
		// defined artifacts

		Collection allArtifacts = session.queryArtifact(query);
		monitor.beginTask("Exporting Artifacts", allArtifacts.size());
		for (Iterator iter = allArtifacts.iterator(); iter.hasNext();) {

			IAbstractArtifact artifact = (IAbstractArtifact) iter.next();
			monitor.subTask(artifact.getFullyQualifiedName());
			TigerstripeRuntime.logDebugMessage("Creating artifact: "
					+ artifact.getFullyQualifiedName());

			String finalText = null;
			// For performance improvement, let's use what is already on disk if
			// it exists. If not, revert to processing the artifact the old
			// fashion "safe but slow" way.
			String path = getTSProject().getLocation().toFile()
					+ File.separator
					+ ((AbstractArtifact) artifact).getArtifactPath();
			File artFile = new File(path);
			try {
				// Bug 917
				// process Illegal XML Characters
				StringBuffer sb = Util.readAndReplaceInFile(artFile,
						"" + '\05', "<?char x0005?>");
				finalText = sb.toString();
			} catch (IOException e) {
				String artText = artifact.asText();
				// Bug 917
				// process Illegal XML Characters
				String c = "" + '\05';
				finalText = artText.replaceAll(c, "<?char x0005?>");
			}

			Element artifactElem = artifacts.addElement("artifact");
			artifactElem.addAttribute("name", artifact.getFullyQualifiedName());
			artifactElem.addAttribute("type", artifact.getClass().getName());
			artifactElem.addText(finalText);
			if (monitor.isCanceled())
				throw new TigerstripeException(
						"Interupted Packaging. Last processed was "
								+ artifact.getFullyQualifiedName());
			monitor.worked(1);
		}
		monitor.done();
	}

	/**
	 * Extracts a ITigerstripeProject from the given doc (ts-module.xml)
	 * 
	 * @param doc
	 * @return
	 */
	private ProjectDetails extractProjectDetails(Document doc)
			throws InvalidModuleException {
		ProjectDetails result = new ProjectDetails(null);

		Element root = doc.getRootElement();
		Element details = root.element("details");

		Node node = details;
		String name = ((Element) node).attributeValue("name");
		result.setName(name);
		String version = ((Element) node).attributeValue("version");
		result.setVersion(version);
		String description = node.getStringValue();
		result.setDescription(description);

		return result;
	}

	private ModuleHeader extractModuleHeader(Document doc)
			throws InvalidModuleException {
		ModuleHeader header = new ModuleHeader();
		Element root = doc.getRootElement();
		Element headerElm = root.element("header");

		Element node = headerElm;
		String originalName = node.attributeValue("originalName");
		header.setOriginalName(originalName);

		String moduleId = node.attributeValue("moduleID");
		header.setModuleID(moduleId);

		String build = node.attributeValue("build");
		header.setBuild(build);

		String date = node.attributeValue("date");
		DateFormat format = new SimpleDateFormat("yyyy.MM.dd-hh.mm");
		try {
			header.setDate(format.parse(date));
		} catch (ParseException e) {
			header.setDate(null);
		}
		return header;
	}

	private void extractArtifacts(Document doc, IProgressMonitor monitor)
			throws InvalidModuleException {

		artifactMgr = new ModuleArtifactManager(this);

		Element root = doc.getRootElement();
		if (root == null)
			throw new InvalidModuleException("No document root");
		Element artifacts = root.element("artifacts");
		if (artifacts != null) {

			List list = artifacts.elements("artifact");
			monitor.beginTask("Parsing module content", list.size());
			JavaDocBuilder builder = new JavaDocBuilder();
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Node node = (Node) iter.next();
				String text = node.getText();

				// Bug 917
				String finalText = text.replaceAll("<\\?char x0005\\?>",
						"" + '\05');
				StringReader reader = new StringReader(finalText);

				try {
					JavaSource src = builder.addSource(reader);
					IAbstractArtifact art = artifactMgr.extractArtifact(src,
							monitor);
					if (art != null) {
						// note that art maybe null if the current profile
						// ignores
						// an artifact
						// type that was used when packaging the module
						monitor.subTask(art.getFullyQualifiedName()
								+ " (from module)");
						// Don't resolve for the moment
						artifactMgr.addArtifact(art, monitor);
					}
					if (monitor.isCanceled())
						break;
				} catch (TigerstripeException e) {
					new InvalidModuleException(
							"Error while decoding an artifact", e);
				}
				monitor.worked(1);
			}
			monitor.done();
		}
	}
}
