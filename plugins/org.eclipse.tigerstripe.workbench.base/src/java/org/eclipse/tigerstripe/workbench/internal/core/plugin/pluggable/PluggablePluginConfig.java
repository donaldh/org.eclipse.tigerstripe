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
import org.eclipse.tigerstripe.workbench.internal.core.util.ContainedProperties;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;

public class PluggablePluginConfig extends PluginConfig {

	private String pluginId;

	private String groupId;
	private String pluginName;

	public PluggablePluginConfig(TigerstripeProject project, String pluginId,
			String groupId, String name) {
		super(project);
		this.pluginId = pluginId;
		this.groupId = groupId;
		this.pluginName = name;
	}

	@Override
	public String getPluginId() {
		return pluginId;
	}

	@Override
	public String getGroupId() {
		return groupId;
	}

	
	public String getPluginName() {
		return pluginName;
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

	public IPluginConfig clone() {
		PluggablePluginConfig config = new PluggablePluginConfig(getProject(),
				pluginId, groupId, pluginName);

		config.setEnabled(isEnabled());
		config.setVersion(getVersion());
		config.setProperties(new ContainedProperties(getProperties()));
		config.setLogLevel(getLogLevel());
		config.setDisableLogging(isLoggingDisabled());
		if (getFacetReference() != null)
			config.setFacetReference(getFacetReference().clone());
		config.setProjectHandle(getProjectHandle());

		return config;
	}
}
