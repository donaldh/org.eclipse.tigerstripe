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

import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;

public class UnknownPluginConfig extends PluginConfig {

	private String pluginId;
	private String groupId;

	public UnknownPluginConfig(TigerstripeProject project) {
		super(project);
	}

	@Override
	public String getGroupId() {
		return groupId;
	}

	@Override
	public String getPluginId() {
		return pluginId;
	}

	public IPluginConfig clone() {
		UnknownPluginConfig result = new UnknownPluginConfig(getProject());
		result.groupId = groupId;
		result.pluginId = pluginId;
		return result;
	}
}
