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
package org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.UnknownPluginException;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;

public class PluggablePluginConfig extends PluginConfig {

	private String pluginId;

	private String groupId;

	public PluggablePluginConfig(TigerstripeProject project) {
		super(project);
	}

	@Override
	public String getActiveVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPluginId() {
		return pluginId;
	}

	@Override
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}

	@Override
	public int getCategory() {
		return GENERATE_CATEGORY;
	}

	@Override
	public void trigger() throws TigerstripeException {
		try {
			resolve(); // need to make sure the housing as not been un-plugged
			super.trigger();
		} catch (UnknownPluginException e) {
			// Ignore it just means the pluginConfig is in the tigerstripe.xml
			// descriptor but the housing is not registered.
		}
	}

}
