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
package org.eclipse.tigerstripe.workbench.internal.api.impl;

import java.io.File;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.publish.IProjectCSVCreator;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.csv.CSVPlugin;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

/**
 * Wrapper class around publisher plugin to publish a project
 * 
 * @author Eric Dillon
 * 
 */
public class ProjectCSVCreator implements IProjectCSVCreator {

	private ITigerstripeProject project;

	public ProjectCSVCreator(ITigerstripeProject project) {
		this.project = project;
	}

	protected ITigerstripeProject getProject() {
		return this.project;
	}

	public boolean isCSVCreateable() {
		IPluginConfig cSVPluginConfig;
		try {
			cSVPluginConfig = getCSVPluginConfig();
			if (cSVPluginConfig == null)
				return false;
		} catch (TigerstripeException e) {
			return false;
		}

		if (!cSVPluginConfig.isEnabled())
			return false;
		return true;
	}

	protected IPluginConfig getCSVPluginConfig() throws TigerstripeException {
		// extract the corresponding publisher plugin ref in the descriptor.
		// and trigger it
		IPluginConfig cSVPluginConfig = null;
		IPluginConfig[] plugins = getProject().getPluginConfigs();
		for (int i = 0; i < plugins.length; i++) {
			if (plugins[i].getPluginId().equals(CSVPlugin.PLUGIN_ID)) {
				cSVPluginConfig = plugins[i];
			}
		}
		return cSVPluginConfig;
	}

	public void createCSV() throws TigerstripeException {

		if (isCSVCreateable()) {
			IPluginConfig csvPluginConfig = getCSVPluginConfig();

			// Ok, let's trigger it now
			csvPluginConfig.trigger();
		} else
			throw new TigerstripeException(
					"Project is not csv-able. Please make sure required details for publish are populated.");
	}

	public String getCSVDirectory() {
		IPluginConfig csvPluginConfig;
		try {
			csvPluginConfig = getCSVPluginConfig();
			if (csvPluginConfig == null)
				return "unknown";
			String relDir;
			if (csvPluginConfig.getProperty(CSVPlugin.CSV_DIRECTORY) == null){
				relDir = (String) csvPluginConfig.getProperty(CSVPlugin.CSV_DIRECTORY);
			}
				else 
					relDir = "unknown";
			return getProject().getURI().getPath() + File.separator + relDir;
		} catch (TigerstripeException e) {
			return "unknown";
		}
	}

}
