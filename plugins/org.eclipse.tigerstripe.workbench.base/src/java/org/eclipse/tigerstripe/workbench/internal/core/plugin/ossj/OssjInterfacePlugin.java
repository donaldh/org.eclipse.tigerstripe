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
package org.eclipse.tigerstripe.workbench.internal.core.plugin.ossj;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.DatatypeArtifact;
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
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class OssjInterfacePlugin extends BasePlugin {

	// These are the defined properties that can be set for this ref
	public final static String DEFAULT_INTERFACE_PACKAGE = "defaultInterfacePackage";

	public final static String[] PROPERTIES = { DEFAULT_INTERFACE_PACKAGE };

	private final static String LABEL = "J2EE JVT Integration Profile";

	private final static String DESCRIPTION = "Generates the J2EE JVT Integration profile.";

	public final static String PLUGIN_ID = "ossj-jvt-spec";

	private final static String GROUP_ID = PluginConfigFactory.GROUPID_TS;

	private final static String VERSION = PluginConfigFactory.VERSION_1_3;

	private final static String[] supportedNatures = { PluginBody.OSSJ_NATURE };

	private final static String REPORTTEMPLATE = "OSSJ_JVT_REPORT.vm";

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

		// try {
		ITigerstripeProject handle = pluginConfig.getProjectHandle();
		// (ITigerstripeProject) API
		// .getDefaultProjectSession().makeTigerstripeProject(
		// pluginConfig.getProject().getBaseDir().toURI(), null);
		ArtifactManagerSessionImpl session = (ArtifactManagerSessionImpl) handle
				.getArtifactManagerSession();

		ArtifactManager artifactMgr = session.getArtifactManager();
		artifactMgr.lock(true);
		this.report = new PluginReport(pluginConfig);
		this.report.setTemplate(OssjInterfaceModel.TEMPLATE_PREFIX + "/"
				+ pluginConfig.getActiveVersion() + "/" + REPORTTEMPLATE);

		// // Code generation is based on Managed Entity Artifacts, so we
		// iterate
		// // on them.
		Collection entities = artifactMgr.getArtifactsByModel(
				ManagedEntityArtifact.MODEL, false,
				new TigerstripeNullProgressMonitor());

		for (Iterator iter = entities.iterator(); iter.hasNext();) {
			ManagedEntityArtifact entity = (ManagedEntityArtifact) iter.next();
			ValueInterfaceModel valueModel = new ValueInterfaceModel(entity,
					pluginConfig);
			generateWithTemplate(valueModel, pluginConfig, config);

			ValueIteratorInterfaceModel iterModel = new ValueIteratorInterfaceModel(
					entity, pluginConfig);
			generateWithTemplate(iterModel, pluginConfig, config);

			KeyInterfaceModel model = new KeyInterfaceModel(entity, pluginConfig);
			generateWithTemplate(model, pluginConfig, config);

			KeyResultInterfaceModel resultModel = new KeyResultInterfaceModel(
					entity, pluginConfig);
			generateWithTemplate(resultModel, pluginConfig, config);

			KeyResultIteratorInterfaceModel resultIteratorModel = new KeyResultIteratorInterfaceModel(
					entity, pluginConfig);
			generateWithTemplate(resultIteratorModel, pluginConfig, config);
		}

		// Code generation is based on Datatype Artifacts, so we iterate
		// on them.
		Collection datatypes = artifactMgr.getArtifactsByModel(
				DatatypeArtifact.MODEL, false,
				new TigerstripeNullProgressMonitor());
		for (Iterator iter = datatypes.iterator(); iter.hasNext();) {
			DatatypeArtifact datatype = (DatatypeArtifact) iter.next();
			DatatypeInterfaceModel model = new DatatypeInterfaceModel(datatype,
					pluginConfig);
			generateWithTemplate(model, pluginConfig, config);
		}

		// Code generation is based on Enum Artifacts, so we iterate
		// on them.
		Collection enums = artifactMgr.getArtifactsByModel(EnumArtifact.MODEL,
				false, new TigerstripeNullProgressMonitor());
		for (Iterator iter = enums.iterator(); iter.hasNext();) {
			EnumArtifact aEnum = (EnumArtifact) iter.next();
			EnumInterfaceModel model = new EnumInterfaceModel(aEnum, pluginConfig);
			generateWithTemplate(model, pluginConfig, config);
		}

		// Code generation is based on Update Procedure Artifacts, so we
		// iterate
		// on them.
		Collection updateProcedures = artifactMgr.getArtifactsByModel(
				UpdateProcedureArtifact.MODEL, false,
				new TigerstripeNullProgressMonitor());
		for (Iterator iter = updateProcedures.iterator(); iter.hasNext();) {
			UpdateProcedureArtifact aProc = (UpdateProcedureArtifact) iter
					.next();
			UpdateProcedureInterfaceModel model = new UpdateProcedureInterfaceModel(
					aProc, pluginConfig);
			generateWithTemplate(model, pluginConfig, config);
			// Need to add a reponse Model for DG 1.3
			UpdateProcedureResponseInterfaceModel responseModel = new UpdateProcedureResponseInterfaceModel(
					aProc, pluginConfig);
			generateWithTemplate(responseModel, pluginConfig, config);
		}

		// Code generation is based on Exception Artifacts, so we iterate
		// on them.
		Collection exceptions = artifactMgr.getArtifactsByModel(
				ExceptionArtifact.MODEL, false,
				new TigerstripeNullProgressMonitor());
		for (Iterator iter = exceptions.iterator(); iter.hasNext();) {
			ExceptionArtifact aException = (ExceptionArtifact) iter.next();
			ExceptionInterfaceModel model = new ExceptionInterfaceModel(
					aException, pluginConfig);
			generateWithTemplate(model, pluginConfig, config);
		}

		// Code generation is based on SessionFacade Artifacts, so we
		// iterate
		// on them.
		Collection facades = artifactMgr.getArtifactsByModel(
				SessionFacadeArtifact.MODEL, false,
				new TigerstripeNullProgressMonitor());
		for (Iterator iter = facades.iterator(); iter.hasNext();) {
			SessionFacadeArtifact facade = (SessionFacadeArtifact) iter.next();
			SessionFacadeInterfaceModel model = new SessionFacadeInterfaceModel(
					facade, pluginConfig);
			generateWithTemplate(model, pluginConfig, config);
			SessionFacadeHomeInterfaceModel homeModel = new SessionFacadeHomeInterfaceModel(
					facade, pluginConfig);
			generateWithTemplate(homeModel, pluginConfig, config);
			SessionOptionalOpsInterfaceModel optionalOpsModel = new SessionOptionalOpsInterfaceModel(
					facade, pluginConfig);
			generateWithTemplate(optionalOpsModel, pluginConfig, config);
		}

		// // Code generation is based on Event Artifacts, so we iterate
		// // on them.
		Collection events = artifactMgr.getArtifactsByModel(
				EventArtifact.MODEL, false,
				new TigerstripeNullProgressMonitor());
		for (Iterator iter = events.iterator(); iter.hasNext();) {
			EventArtifact event = (EventArtifact) iter.next();
			EventInterfaceModel model = new EventInterfaceModel(event,
					pluginConfig);
			generateWithTemplate(model, pluginConfig, config);

			EventDescriptorInterfaceModel descrModel = new EventDescriptorInterfaceModel(
					event, pluginConfig);
			generateWithTemplate(descrModel, pluginConfig, config);
		}

		// Code generation is based on Query Artifacts, so we iterate
		// on them.
		Collection queries = artifactMgr.getArtifactsByModel(
				QueryArtifact.MODEL, false,
				new TigerstripeNullProgressMonitor());
		for (Iterator iter = queries.iterator(); iter.hasNext();) {
			QueryArtifact query = (QueryArtifact) iter.next();
			QueryInterfaceModel model = new QueryInterfaceModel(query,
					pluginConfig);
			generateWithTemplate(model, pluginConfig, config);
			// Need to add a reponse Model for DG 1.3
			QueryResponseInterfaceModel responseModel = new QueryResponseInterfaceModel(
					query, pluginConfig);
			generateWithTemplate(responseModel, pluginConfig, config);
		}
		artifactMgr.lock(false);
		// } catch (TigerstripeLicenseException e) {
		// throw new TigerstripeException("License Issue", e);
		// }
	}

	private void generateWithTemplate(OssjInterfaceModel model,
			PluginConfig pluginConfig, RunConfig config) throws TigerstripeException {
		// Build up a local context for the template
		VelocityContext localContext = new VelocityContext(getDefaultContext());

		// try {
		ITigerstripeProject handle = pluginConfig.getProjectHandle();
		// (ITigerstripeProject) API
		// .getDefaultProjectSession().makeTigerstripeProject(
		// pluginConfig.getProject().getBaseDir().toURI(), null);
		ArtifactManagerSessionImpl session = (ArtifactManagerSessionImpl) handle
				.getArtifactManagerSession();

		ArtifactManager artifactMgr = session.getArtifactManager();

		if (model.getGenerate()) {
			localContext.put("artifact", model.getArtifact());
			localContext.put("model", model);
			localContext.put("ossjUtil", new OssjUtil(artifactMgr, pluginConfig));
			localContext.put("pluginConfig", pluginConfig);
			localContext.put("runtime", TigerstripeRuntime.getInstance());
			localContext.put("tsProject", pluginConfig.getProject());
			try {
				setClasspathLoaderForVelocity();

				Template template = Velocity.getTemplate(model.getTemplate());

				String filename = model.getInterfacePath() + File.separator
						+ model.getInterfaceFilename();

				setDefaultDestination(pluginConfig, new File(filename), config);

				// create the output
				template.merge(localContext, getDefaultWriter());

				getDefaultWriter().close();
				Collection<String> files = this.report.getGeneratedFiles();
				// files.add(model.getInterfaceFilename());
				files.add(model.getInterfaceExtensionPath() + File.separator
						+ model.getInterfaceFilename());

			} catch (MethodInvocationException e) {
				Throwable th = e.getWrappedThrowable();
				String errorMsg = null;
				if (th instanceof TigerstripeException) {
					TigerstripeException ee = (TigerstripeException) th;
					errorMsg = ee.getLocalizedMessage() + " while calling "
							+ e.getMethodName() + " within '"
							+ model.getTemplate() + "' template." + "(in "
							+ model.getArtifact().getFullyQualifiedName()
							+ ").";
				} else
					errorMsg = "Unknown Error in "
							+ model.getArtifact().getFullyQualifiedName()
							+ "(while calling " + e.getMethodName()
							+ " within '" + model.getTemplate()
							+ "' template).";
				throw new TigerstripeException(errorMsg, e);
			} catch (Exception e) {
				throw new TigerstripeException(
						"Unexpected error while merging '"
								+ model.getTemplate() + "' template for "
								+ model.getArtifact().getFullyQualifiedName()
								+ ".", e);
			}
		}
		// } catch (TigerstripeLicenseException e) {
		// throw new TigerstripeException("License Issue", e);
		// }
	}

	public String[] getDefinedProperties() {
		return PROPERTIES;
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
		return IPluginConfig.GENERATE_CATEGORY;
	}

}