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
package org.eclipse.tigerstripe.workbench.internal.core.plugin;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.tigerstripe.workbench.plugins.IPluginReport;

/**
 * 
 * 
 * @author Richard Craddock
 * 
 * a PluginReport is returned by a plugin once a Plugin has run to say what it
 * did
 * 
 */
public class PluginReport implements IPluginReport {

	private PluginConfig pluginConfig;

	private String template;

	private Collection<String> generatedFiles = new ArrayList<String>();

	private Collection<String> copiedFiles = new ArrayList<String>();

	public void setCopiedFiles(Collection<String> inFiles) {
		this.copiedFiles = inFiles;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.plugin.IPluginReport#getCopiedFiles()
	 */
	public Collection<String> getCopiedFiles() {
		return this.copiedFiles;
	}

	public void setGeneratedFiles(Collection<String> inFiles) {
		this.generatedFiles = inFiles;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.workbench.internal.core.plugin.IPluginReport#getGeneratedFiles()
	 */
	public Collection<String> getGeneratedFiles() {
		return this.generatedFiles;
	}

	public String getTemplate() {
		return this.template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public PluginConfig getPluginConfig() {
		return this.pluginConfig;
	}

	public PluginReport(PluginConfig pluginConfig) {
		this.pluginConfig = pluginConfig;
	}

}
