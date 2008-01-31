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
package org.eclipse.tigerstripe.workbench.internal.core.plugin.ossj.wsExample;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginBody;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfigFactory;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginReport;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.WsdlPluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.base.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.workbench.internal.tools.example.Wsdl2example;
import org.eclipse.tigerstripe.workbench.project.IAdvancedProperties;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class OssjWsdlExamplePlugin extends BasePlugin {

	// These are the defined properties that can be set for this ref

	public final static String[] PROPERTIES = {};

	private final static String LABEL = "OSS/J Web Services Sample Payload";

	private final static String DESCRIPTION = "Generates SOAP sample payload for the OSS/J WS Integration Profile.";

	public final static String PLUGIN_ID = "ossj-wsdl-example-spec";

	private final static String GROUP_ID = PluginConfigFactory.GROUPID_TS;

	private final static String VERSION = PluginConfigFactory.VERSION_1_3;

	private final static String REPORTTEMPLATE = "org/eclipse/tigerstripe/workbench/internal/core/plugin/ossj/wsExample/resources/OSSJ_WSDL_EXAMPLE_REPORT.vm";

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
			ITigerstripeProject handle = pluginConfig.getProjectHandle();
			// (ITigerstripeProject) API.getDefaultProjectSession()
			// .makeTigerstripeProject(
			// pluginConfig.getProject().getBaseDir().toURI(), null);
			ArtifactManagerSessionImpl session = (ArtifactManagerSessionImpl) handle
					.getArtifactManagerSession();

			// Check if the WSDLPugin is enabled - otherwise we'll have no WSDL!
			WsdlPluginConfig wsdlRef = (WsdlPluginConfig) pluginConfig.getProject()
					.findPluginConfig(WsdlPluginConfig.MODEL);

			if (wsdlRef.isEnabled()) {
				ArtifactManager artifactMgr = session.getArtifactManager();
				this.report = new PluginReport(pluginConfig);
				this.report.setTemplate(REPORTTEMPLATE);

				Collection facades = artifactMgr.getArtifactsByModel(
						SessionFacadeArtifact.MODEL, false,
						new TigerstripeNullProgressMonitor());
				// for (Iterator iter = facades.iterator(); iter.hasNext();) {
				// SessionFacadeArtifact facade = (SessionFacadeArtifact)
				// iter.next();
				Collection generatedSchemas = wsdlRef.getReport()
						.getGeneratedFiles();
				for (Iterator iter = generatedSchemas.iterator(); iter
						.hasNext();) {
					String wsdlName = (String) iter.next();
					// String filename =
					// pluginConfig.getProject().getProjectDetails().getOutputDirectory()
					// + File.separator
					// + facade.getName() + ".wsdl";

					// File wsdl = new File(filename);
					// File wsdlFullPath = new File(
					// pluginConfig.getProject().getBaseDir()
					// + File.separator + wsdl.getPath());

					Wsdl2example g = new Wsdl2example();
					g.setTargetDirSuffix("SOAPexamples");
					g
							.setAllowNetworkSchemas("true"
									.equalsIgnoreCase(pluginConfig
											.getProject()
											.getAdvancedProperty(
													IAdvancedProperties.PROP_GENERATION_MessagePayloadSampleAllowNetwork)));
					String defaultLocn = pluginConfig
							.getProject()
							.getAdvancedProperty(
									IAdvancedProperties.PROP_GENERATION_MessagePayloadSampleDefaultlocation);
					File projBaseDir = pluginConfig.getProject().getBaseDir();

					g.setSchemaDefaultPath(projBaseDir + "\\" + defaultLocn);
					// g.generateExample(wsdlFullPath,pluginConfig);
					g.generateExample(wsdlName, pluginConfig, config);

				}
			} else {
				this.report = null;
			}
			// } catch (TigerstripeLicenseException e) {
			// throw new TigerstripeException("License issue", e);
		} catch (TigerstripeException e) {
			throw e;
		} catch (Exception e) {
			throw new TigerstripeException(
					"Unexpected error while generating WSDL examples", e);
		}
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

		// Check if the WSDLPugin is enabled - otherwise we'll have no XML!
		WsdlPluginConfig wsdlRef = (WsdlPluginConfig) ((TigerstripeProject) tsProject)
				.findPluginConfig(WsdlPluginConfig.MODEL);

		for (int i = 0; i < refs.length; i++) {
			if (PLUGIN_ID.equals(refs[i].getPluginId())
					&& GROUP_ID.equals(refs[i].getGroupId())
					&& refs[i].isEnabled() && wsdlRef.isEnabled())
				return true;
		}

		return false;
	}

	public int getCategory() {
		return IPluginConfig.GENERATE_CATEGORY;
	}

}