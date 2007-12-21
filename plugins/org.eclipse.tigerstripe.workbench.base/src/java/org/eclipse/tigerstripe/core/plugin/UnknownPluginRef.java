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

import org.eclipse.tigerstripe.api.plugins.builtin.IVersionAwarePlugin;
import org.eclipse.tigerstripe.core.project.TigerstripeProject;

public class UnknownPluginRef extends PluginRef {

	private String pluginId;
	private String groupId;

	public UnknownPluginRef(TigerstripeProject project) {
		super(project);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getActiveVersion() {
		return getProperties().getProperty("activeVersion",
				IVersionAwarePlugin.NONE);
	}

	@Override
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Override
	public String getPluginId() {
		return pluginId;
	}

	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}

}
