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

import java.util.Properties;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginBody;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginHousing;
import org.eclipse.tigerstripe.workbench.plugins.EPluggablePluginNature;
import org.eclipse.tigerstripe.workbench.plugins.IPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog;

public class PluggableHousing extends PluginHousing {

	public PluggableHousing(Class pluginClass) throws TigerstripeException {
		super(pluginClass);
		// TODO Auto-generated constructor stub
	}

	public PluggableHousing(PluginBody body) {
		super(body);
	}

	public PluggablePlugin getBody() {
		return (PluggablePlugin) this.body;
	}

	/*
	 * Says if the plugin is deployed.
	 * The inverse is that it is Contributed through an plugin.
	 * 
	 */
	public boolean isDeployed(){
		return getBody().getCanDelete();
	}
	
	public PluggablePluginConfig makeDefaultPluginConfig(
			TigerstripeProjectHandle project) throws TigerstripeException {

		PluggablePluginConfig result = new PluggablePluginConfig(project
				.getTSProject(), getPluginId(), getGroupId());
		result.setVersion(getVersion());
		result.setEnabled(true);

		// Create the global properties with their default values
		IPluginProperty[] props = getBody().getPProject().getGlobalProperties();
		Properties properties = result.getProperties();
		for (IPluginProperty prop : props) {
			properties.setProperty(prop.getName(), prop.serialize(prop
					.getDefaultValue()));
		}

		return result;
	}

	@Override
	public String[] getDefinedProperties() {
		return getBody().getDefinedProperties();
	}

	@Override
	public String getLabel() {
		return getBody().getLabel();
	}

	public PluginLog.LogLevel getDefaultLogLevel() {
		return getBody().getDefaultLogLevel();
	}

	public String getLogPath() {
		return getBody().getLogPath();
	}

	public boolean isLogEnabled() {
		return getBody().isLogEnabled();
	}

	public EPluggablePluginNature getPluginNature() {
		return getBody().getPProject().getPluginNature();
	}
}
