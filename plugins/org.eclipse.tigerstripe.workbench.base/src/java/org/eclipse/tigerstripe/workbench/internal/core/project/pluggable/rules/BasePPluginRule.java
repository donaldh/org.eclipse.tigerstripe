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
package org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules;

import org.eclipse.tigerstripe.workbench.internal.api.plugins.pluggable.IPluggablePluginProject;
import org.eclipse.tigerstripe.workbench.internal.api.plugins.pluggable.IRunRule;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public abstract class BasePPluginRule implements IRunRule {

	private boolean enabled = true;

	private String name = "";

	private String description = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public abstract String getLabel();

	public abstract Node getBodyAsNode(Document document);

	public abstract void buildBodyFromNode(Node node);

	private IPluggablePluginProject project;

	public void setProject(IPluggablePluginProject project) {
		this.project = project;
	}

	public IPluggablePluginProject getProject() {
		return this.project;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String isEnabledStr() {
		return Boolean.toString(enabled);
	}

	public void setEnabledStr(String enabledStr) {
		this.enabled = Boolean.parseBoolean(enabledStr);
	}
}
