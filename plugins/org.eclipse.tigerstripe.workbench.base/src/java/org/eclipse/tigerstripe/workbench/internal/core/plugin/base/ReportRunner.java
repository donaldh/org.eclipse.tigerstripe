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
package org.eclipse.tigerstripe.workbench.internal.core.plugin.base;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.generation.M1RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EnumArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EventArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.PackageArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.QueryArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginReport;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.internal.core.util.VelocityContextUtil;
import org.eclipse.tigerstripe.workbench.internal.tools.compare.Comparer;

public class ReportRunner {
	/**
	 * Generate a report for this run
	 * 
	 */
	public void generateReport(ReportModel model, ArtifactManager artifactMgr,
			Collection<PluginReport> reports, M1RunConfig config)
			throws TigerstripeException {

		TigerstripeProject project = model.getProject();

		VelocityContextUtil util = new VelocityContextUtil();
		VelocityContext localContext = new VelocityContext();

		Collection entities = artifactMgr.getArtifactsByModel(
				ManagedEntityArtifact.MODEL, false,
				new NullProgressMonitor());
		Collection datatypes = artifactMgr.getArtifactsByModel(
				DatatypeArtifact.MODEL, false,
				new NullProgressMonitor());
		Collection queries = artifactMgr.getArtifactsByModel(
				QueryArtifact.MODEL, false,
				new NullProgressMonitor());
		Collection enums = artifactMgr.getArtifactsByModel(EnumArtifact.MODEL,
				false, new NullProgressMonitor());
		Collection updateProcedures = artifactMgr.getArtifactsByModel(
				UpdateProcedureArtifact.MODEL, false,
				new NullProgressMonitor());
		Collection notifications = artifactMgr.getArtifactsByModel(
				EventArtifact.MODEL, false,
				new NullProgressMonitor());
		Collection exceptions = artifactMgr.getArtifactsByModel(
				ExceptionArtifact.MODEL, false,
				new NullProgressMonitor());
		Collection sessions = artifactMgr.getArtifactsByModel(
				SessionFacadeArtifact.MODEL, false,
				new NullProgressMonitor());
		Collection packages = artifactMgr.getArtifactsByModel(
				PackageArtifact.MODEL, false,
				new NullProgressMonitor());

		localContext.put("entities", entities);
		localContext.put("datatypes", datatypes);
		localContext.put("queries", queries);
		localContext.put("enums", enums);
		localContext.put("updateProcedures", updateProcedures);
		localContext.put("notifications", notifications);
		localContext.put("exceptions", exceptions);
		localContext.put("sessions", sessions);
		localContext.put("manager", artifactMgr);
		localContext.put("packages", packages);

		localContext.put("pluginConfigs", project.getPluginConfigs());
		localContext.put("tsProject", project);
		localContext.put("runtime", TigerstripeRuntime.getInstance());
		localContext.put("util", util);
		localContext.put("reportUtils", (new ReportUtils()));
		localContext.put("reports", reports);
		localContext.put("comparer", (new Comparer()));
		try {
			VelocityEngine engine = new VelocityEngine();
			Properties properties = new Properties();
			properties.put("resource.loader", "class");
			properties
					.put("class.resource.loader.class",
							"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			
			
			ClassLoader startingLoader = Thread.currentThread().getContextClassLoader();
			try {
				if( engine.getClass().getClassLoader() != Thread.currentThread().getContextClassLoader()) {
					Thread.currentThread().setContextClassLoader(engine.getClass().getClassLoader());
				}
				engine.init(properties);
			} finally {
				Thread.currentThread().setContextClassLoader(startingLoader);
			}

			Template template = engine.getTemplate(model.getTemplate());

			String filename = model.getDestinationDir() + File.separator
					+ model.getName();

			final String targetInCorrectPath;
			if (config != null && config.getAbsoluteOutputDir() != null) {
				targetInCorrectPath = config.getAbsoluteOutputDir() + File.separator + filename;
			} else {
				targetInCorrectPath = project.getBaseDir() + File.separator + filename;
			}
			File defaultDestination = new File(targetInCorrectPath);

			// Do we need to create the target dir ?
			if (!defaultDestination.getParentFile().exists()) {
				defaultDestination.getParentFile().mkdirs();
			}

			Writer defaultWriter = new FileWriter(defaultDestination);
			// create the output
			template.merge(localContext, defaultWriter);
			defaultWriter.close();

		} catch (MethodInvocationException e) {
			Throwable th = e.getWrappedThrowable();
			String errorMsg = null;
			if (th instanceof TigerstripeException) {
				TigerstripeException ee = (TigerstripeException) th;
				errorMsg = ee.getLocalizedMessage() + " while calling "
						+ e.getMethodName() + " within '" + model.getTemplate();
			} else
				errorMsg = "Unknown Error";
			throw new TigerstripeException(errorMsg, e);

		} catch (Exception e) {
			TigerstripeRuntime.logErrorMessage("Exception detected", e);
			throw new TigerstripeException("Unexpected error", e);

		}

	}

}
