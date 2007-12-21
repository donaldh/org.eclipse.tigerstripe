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
package org.eclipse.tigerstripe.core.plugin;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 
 * 
 * @author Richard Craddock
 * 
 * a PluginReport is returned by a plugin once a Plugin has run to say what it
 * did
 * 
 */
public class PluginReport {

	private PluginRef pluginRef;

	private String template;

	private Collection<String> generatedFiles = new ArrayList<String>();

	private Collection<String> copiedFiles = new ArrayList<String>();

	public void setCopiedFiles(Collection<String> inFiles) {
		this.copiedFiles = inFiles;
	}

	public Collection<String> getCopiedFiles() {
		return this.copiedFiles;
	}

	public void setGeneratedFiles(Collection<String> inFiles) {
		this.generatedFiles = inFiles;
	}

	public Collection<String> getGeneratedFiles() {
		return this.generatedFiles;
	}

	public String getTemplate() {
		return this.template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public PluginRef getPluginRef() {
		return this.pluginRef;
	}

	public PluginReport(PluginRef pluginRef) {
		this.pluginRef = pluginRef;
	}

}
