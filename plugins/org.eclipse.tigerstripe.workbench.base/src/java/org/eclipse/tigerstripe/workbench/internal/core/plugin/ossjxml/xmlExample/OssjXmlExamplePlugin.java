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
package org.eclipse.tigerstripe.workbench.internal.core.plugin.ossjxml.xmlExample;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginBody;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginRef;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginRefFactory;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginReport;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.XmlPluginRef;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.base.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.tools.example.Xml2example;
import org.eclipse.tigerstripe.workbench.project.IAdvancedProperties;
import org.eclipse.tigerstripe.workbench.project.IPluginReference;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class OssjXmlExamplePlugin extends BasePlugin {

	// These are the defined properties that can be set for this ref

	public final static String[] PROPERTIES = {};

	private final static String LABEL = "OSS/J XML examples";

	private final static String DESCRIPTION = "Generates XML examples the OSS/J XML Integration Profile.";

	public final static String PLUGIN_ID = "ossj-xml-example-spec";

	private final static String GROUP_ID = PluginRefFactory.GROUPID_TS;

	private final static String VERSION = PluginRefFactory.VERSION_1_3;

	private final static String REPORTTEMPLATE = "org/eclipse/tigerstripe/workbench/internal/core/plugin/ossjxml/xmlExample/resources/OSSJ_XML_EXAMPLE_REPORT.vm";

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

	public void trigger(PluginRef pluginRef, RunConfig config)
			throws TigerstripeException {

		try {
			ITigerstripeProject handle = pluginRef.getProjectHandle();
			// (ITigerstripeProject) API.getDefaultProjectSession()
			// .makeTigerstripeProject(
			// pluginRef.getProject().getBaseDir().toURI(), null);
			ArtifactManagerSessionImpl session = (ArtifactManagerSessionImpl) handle
					.getArtifactManagerSession();

			// Check if the XMLPugin is enabled - otherwise we'll have no XML!
			XmlPluginRef xmlRef = (XmlPluginRef) pluginRef.getProject()
					.findPluginRef(XmlPluginRef.MODEL);

			if (xmlRef.isEnabled()) {
				ArtifactManager artifactMgr = session.getArtifactManager();
				this.report = new PluginReport(pluginRef);
				this.report.setTemplate(REPORTTEMPLATE);

				// Put the meat in here

				// Iterate over each schema that has been produced, and
				// do the necessary
				// Get the iles from the xmlPluginReport !
				Collection generatedSchemas = xmlRef.getReport()
						.getGeneratedFiles();
				for (Iterator iter = generatedSchemas.iterator(); iter
						.hasNext();) {
					String schemaName = (String) iter.next();
					String schemaFilename = pluginRef.getProject()
							.getProjectDetails().getOutputDirectory()
							+ File.separator + schemaName;
					// TigerstripeRuntime.logInfoMessage(schemaFilename);
					// File schema = new File(schemaFilename);
					// File schemaFullPath = new File(
					// pluginRef.getProject().getBaseDir()
					// + File.separator + schema.getPath());

					Xml2example g = new Xml2example();
					g.setTargetDirSuffix("XMLexamples");
					g
							.setAllowNetworkSchemas("true"
									.equalsIgnoreCase(pluginRef
											.getProject()
											.getAdvancedProperty(
													IAdvancedProperties.PROP_GENERATION_MessagePayloadSampleAllowNetwork)));
					String defaultLocn = pluginRef
							.getProject()
							.getAdvancedProperty(
									IAdvancedProperties.PROP_GENERATION_MessagePayloadSampleDefaultlocation);
					File projBaseDir = pluginRef.getProject().getBaseDir();

					g.setSchemaDefaultPath(projBaseDir + "\\" + defaultLocn);
					// g.generateExample(schemaFullPath, pluginRef);

					g.generateExample(schemaName, pluginRef);

				}

				return;

			} else {
				this.report = null;
			}
			// } catch (TigerstripeLicenseException e) {
			// throw new TigerstripeException("License issue", e);
		} catch (TigerstripeException e) {
			throw e;
		} catch (Exception e) {
			throw new TigerstripeException(
					"Unexpected error while generating XML examples", e);
		}
	}

	public String[] getDefinedProperties() {
		return PROPERTIES;
	}

	/**
	 * Returns true if this plugin is enabled in the given ITigerstripeProject
	 * 
	 * @param pluginRef
	 * @return
	 */
	public static boolean isEnabled(ITigerstripeProject tsProject)
			throws TigerstripeException {
		IPluginReference[] refs = tsProject.getPluginReferences();

		for (int i = 0; i < refs.length; i++) {
			if (PLUGIN_ID.equals(refs[i].getPluginId())
					&& GROUP_ID.equals(refs[i].getGroupId())
					&& refs[i].isEnabled())
				return true;
		}

		return false;
	}

	public int getCategory() {
		return IPluginReference.GENERATE_CATEGORY;
	}

}