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
package org.eclipse.tigerstripe.api.impl;

import java.io.File;

import org.eclipse.tigerstripe.api.IPluginReference;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.api.publish.IProjectCSVCreator;
import org.eclipse.tigerstripe.core.plugin.csv.CSVPlugin;

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
		IPluginReference cSVPluginRef;
		try {
			cSVPluginRef = getCSVPluginRef();
			if (cSVPluginRef == null)
				return false;
		} catch (TigerstripeException e) {
			return false;
		}

		if (!cSVPluginRef.isEnabled())
			return false;
		return true;
	}

	protected IPluginReference getCSVPluginRef() throws TigerstripeException {
		// extract the corresponding publisher plugin ref in the descriptor.
		// and trigger it
		IPluginReference cSVPluginRef = null;
		IPluginReference[] plugins = getProject().getPluginReferences();
		for (int i = 0; i < plugins.length; i++) {
			if (plugins[i].getPluginId().equals(CSVPlugin.PLUGIN_ID)) {
				cSVPluginRef = plugins[i];
			}
		}
		return cSVPluginRef;
	}

	public void createCSV() throws TigerstripeException {

		if (isCSVCreateable()) {
			IPluginReference csvPluginRef = getCSVPluginRef();

			// Ok, let's trigger it now
			csvPluginRef.trigger();
		} else
			throw new TigerstripeException(
					"Project is not csv-able. Please make sure required details for publish are populated.");
	}

	public String getCSVDirectory() {
		IPluginReference csvPluginRef;
		try {
			csvPluginRef = getCSVPluginRef();
			if (csvPluginRef == null)
				return "unknown";
			String relDir = csvPluginRef.getProperties().getProperty(
					CSVPlugin.CSV_DIRECTORY, "unknown");
			return getProject().getURI().getPath() + File.separator + relDir;
		} catch (TigerstripeException e) {
			return "unknown";
		}
	}

}
