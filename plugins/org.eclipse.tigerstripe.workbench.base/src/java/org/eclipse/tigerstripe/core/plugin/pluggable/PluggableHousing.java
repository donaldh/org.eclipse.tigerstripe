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
package org.eclipse.tigerstripe.core.plugin.pluggable;

import java.util.Properties;

import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.plugins.PluginLog;
import org.eclipse.tigerstripe.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.api.plugins.pluggable.EPluggablePluginNature;
import org.eclipse.tigerstripe.api.plugins.pluggable.IPluggablePluginProperty;
import org.eclipse.tigerstripe.core.plugin.PluginBody;
import org.eclipse.tigerstripe.core.plugin.PluginHousing;

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

	public PluggablePluginRef makeDefaultPluginRef(
			TigerstripeProjectHandle project) throws TigerstripeException {

		PluggablePluginRef result = new PluggablePluginRef(project
				.getTSProject());
		result.setVersion(getVersion());
		result.setEnabled(true);
		result.setGroupId(getGroupId());
		result.setPluginId(getPluginId());

		// Create the global properties with their default values
		IPluggablePluginProperty[] props = getBody().getPProject()
				.getGlobalProperties();
		Properties properties = result.getProperties();
		for (IPluggablePluginProperty prop : props) {
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
