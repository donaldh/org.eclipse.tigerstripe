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
package org.eclipse.tigerstripe.workbench.internal.core.plugin.csv;

import java.io.File;
import java.util.Collection;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.TigerstripeLicenseException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DependencyArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EnumArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EventArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.QueryArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginBody;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfigFactory;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginReport;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.base.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.base.ReportUtils;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class CSVPlugin extends BasePlugin {

	public final static String CSV_DIRECTORY = "csvDirectory";

	public final static String INCLUDE_INHERITED = "includeInherited";

	public final static String LEVEL_OF_DETAIL = "levelOfDetail";

	public final static String[] PROPERTIES = { CSV_DIRECTORY,
			INCLUDE_INHERITED, LEVEL_OF_DETAIL };

	private final static String LABEL = "CSV of Project Artifacts";

	private final static String DESCRIPTION = "Creates a CSV of the Project Artifacts.";

	public final static String PLUGIN_ID = "csv-spec";

	private final static String GROUP_ID = PluginConfigFactory.GROUPID_TS;

	private final static String VERSION = PluginConfigFactory.VERSION_1_3;

	private final static String REPORTTEMPLATE = "org/eclipse/tigerstripe/workbench/internal/core/plugin/csv/resources/CSV_REPORT.vm";

	public final static String DETAIL_MAX = "max";

	public final static String DETAIL_SPECIFICS = "specifics";

	public final static String DETAIL_BASIC = "basic";

	public final static String DETAIL_MIN = "min";

	public final static String[] DETAIL_OPTIONS = { DETAIL_MIN, DETAIL_BASIC,
			DETAIL_SPECIFICS, DETAIL_MAX };

	private final static String[] supportedNatures = { PluginBody.OSSJ_NATURE };

	private PluginReport report;

	public PluginReport getReport() {
		return this.report;
	}

	public String[] getSupportedNatures() {
		return supportedNatures;
	}

	public String getLabel() {
		return LABEL;
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getPluginId() {
		return PLUGIN_ID;
	}

	public String getGroupId() {
		return GROUP_ID;
	}

	public String getVersion() {
		return VERSION;
	}

	public void trigger(PluginConfig pluginConfig, RunConfig config)
			throws TigerstripeException {
		try {
			ITigerstripeProject handle = (ITigerstripeProject) TigerstripeCore
					.getDefaultProjectSession().makeTigerstripeProject(
							pluginConfig.getProject().getBaseDir().toURI(), null);
			ArtifactManagerSessionImpl session = (ArtifactManagerSessionImpl) handle
					.getArtifactManagerSession();

			ArtifactManager artifactMgr = session.getArtifactManager();
			this.report = new PluginReport(pluginConfig);
			this.report.setTemplate(REPORTTEMPLATE);

			CSVModel model = new CSVModel(pluginConfig);
			generateWithTemplate(model, pluginConfig, artifactMgr, config);

		} catch (TigerstripeLicenseException e) {
			throw new TigerstripeException("License issue", e);
		}
	}

	private void generateWithTemplate(CSVModel model, PluginConfig pluginConfig,
			ArtifactManager artifactMgr, RunConfig config)
			throws TigerstripeException {

		// Let's put what we'll need in the context and get going
		Collection entities = artifactMgr.getArtifactsByModel(
				ManagedEntityArtifact.MODEL, false,
				new TigerstripeNullProgressMonitor());
		Collection datatypes = artifactMgr.getArtifactsByModel(
				DatatypeArtifact.MODEL, false,
				new TigerstripeNullProgressMonitor());
		Collection events = artifactMgr.getArtifactsByModel(
				EventArtifact.MODEL, false,
				new TigerstripeNullProgressMonitor());
		Collection enums = artifactMgr.getArtifactsByModel(EnumArtifact.MODEL,
				false, new TigerstripeNullProgressMonitor());
		Collection exceptions = artifactMgr.getArtifactsByModel(
				ExceptionArtifact.MODEL, false,
				new TigerstripeNullProgressMonitor());
		Collection queries = artifactMgr.getArtifactsByModel(
				QueryArtifact.MODEL, false,
				new TigerstripeNullProgressMonitor());
		Collection updates = artifactMgr.getArtifactsByModel(
				UpdateProcedureArtifact.MODEL, false,
				new TigerstripeNullProgressMonitor());
		Collection sessions = artifactMgr.getArtifactsByModel(
				SessionFacadeArtifact.MODEL, false,
				new TigerstripeNullProgressMonitor());
		Collection associations = artifactMgr.getArtifactsByModel(
				AssociationArtifact.MODEL, false,
				new TigerstripeNullProgressMonitor());
		Collection associationClasses = artifactMgr.getArtifactsByModel(
				AssociationClassArtifact.MODEL, false,
				new TigerstripeNullProgressMonitor());
		Collection dependencies = artifactMgr.getArtifactsByModel(
				DependencyArtifact.MODEL, false,
				new TigerstripeNullProgressMonitor());

		TigerstripeProject tsProject = pluginConfig.getProject();

		VelocityContext localContext = new VelocityContext(getDefaultContext());
		localContext.put("entities", entities); // TODO scope?
		localContext.put("datatypes", datatypes); // TODO scope?
		localContext.put("events", events); // TODO scope?
		localContext.put("enumerations", enums); // TODO scope?
		localContext.put("exceptions", exceptions); // TODO scope?
		localContext.put("queries", queries); // TODO scope?
		localContext.put("updateProcedures", updates); // TODO scope?
		localContext.put("sessions", sessions); // TODO scope?

		localContext.put("associations", associations);
		localContext.put("associationClasses", associationClasses);
		localContext.put("dependencies", dependencies);

		localContext.put("pluginConfig", pluginConfig);
		localContext.put("tsProject", pluginConfig.getProject());
		localContext.put("runtime", TigerstripeRuntime.getInstance());
		localContext.put("reportUtils", new ReportUtils());

		localContext.put("detail", getCardinal(DETAIL_OPTIONS, pluginConfig
				.getProperties().getProperty(LEVEL_OF_DETAIL)));

		try {
			setClasspathLoaderForVelocity();

			Template template = Velocity.getTemplate(model.getTemplate());

			// String filename = model.getDestinationDir() + File.separator
			// +"project.csv";
			String filename = pluginConfig.getProperties().getProperty(
					CSV_DIRECTORY)
					+ File.separator
					+ tsProject.getProjectDetails().getName()
					+ ".csv";
			setDefaultDestination(pluginConfig, new File(filename), config);

			// create the output
			template.merge(localContext, getDefaultWriter());

			getDefaultWriter().close();

			Collection<String> files = this.report.getGeneratedFiles();
			files.add(tsProject.getProjectDetails().getName() + ".csv");

		} catch (MethodInvocationException e) {
			Throwable th = e.getWrappedThrowable();
			String errorMsg = null;
			if (th instanceof TigerstripeException) {
				TigerstripeException ee = (TigerstripeException) th;
				errorMsg = ee.getLocalizedMessage() + " while calling "
						+ e.getMethodName() + " within '" + model.getTemplate()
						+ "' template." + "(in "
						+ model.getArtifact().getFullyQualifiedName() + ").";
			} else
				errorMsg = "Unknown Error in "
						+ model.getArtifact().getFullyQualifiedName()
						+ "(while calling " + e.getMethodName() + " within '"
						+ model.getTemplate() + "' template).";
			throw new TigerstripeException(errorMsg, e);
		} catch (Exception e) {
			throw new TigerstripeException("Unexpected error while merging '"
					+ model.getTemplate() + "' template for "
					+ model.getArtifact().getFullyQualifiedName() + ".", e);
		}
	}

	public String[] getDefinedProperties() {
		return PROPERTIES;
	}

	public int getCardinal(String[] prop, String value) {
		for (int i = 0; i < prop.length; i++) {
			if (value.equals(prop[i]))
				return i;
		}
		return 0;
	}

	/**
	 * Returns true if this plugin is enabled in the given ITigerstripeProject
	 * 
	 * @param pluginConfig
	 * @return
	 */
	public static boolean isEnabled(ITigerstripeProject tsProject)
			throws TigerstripeException {
		IPluginConfig[] refs = tsProject.getPluginConfigs();

		for (int i = 0; i < refs.length; i++) {
			if (PLUGIN_ID.equals(refs[i].getPluginId())
					&& GROUP_ID.equals(refs[i].getGroupId())
					&& refs[i].isEnabled())
				return true;
		}

		return false;
	}

	public int getCategory() {
		return IPluginConfig.PUBLISH_CATEGORY;
	}

}