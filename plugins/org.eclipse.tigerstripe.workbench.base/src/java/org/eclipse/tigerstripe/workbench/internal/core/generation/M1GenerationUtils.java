/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.generation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginHousing;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginManager;
import org.eclipse.tigerstripe.workbench.internal.core.util.MatchedConfigHousing;
import org.eclipse.tigerstripe.workbench.plugins.EPluggablePluginNature;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class M1GenerationUtils {

	/**
	 * Returns an array containing all the {@link IPluginConfig} defined for the
	 * given Model project
	 * 
	 * @param project
	 * @param enabledOnly
	 * @return
	 */
	public static IPluginConfig[] m1PluginConfigs(ITigerstripeModelProject project, boolean enabledOnly,
			boolean cloneObjects) throws TigerstripeException {

		IPluginConfig[] pluginConfig = project.getPluginConfigs();
		if (pluginConfig == null || pluginConfig.length == 0) {
			BasePlugin.logErrorMessage("No Generator Plugins were found for project " + project.getName());
		}

		BasePlugin.logInfo("Project " + project.getName() + " has " + String.valueOf(pluginConfig.length) + " plugins to resolve.");
		List<IPluginConfig> result = new ArrayList<IPluginConfig>();
		for (IPluginConfig plugin : pluginConfig) {
			IPluginConfig config = null;
			if (!PluginManager.isOsgiVersioning()) {
				config = plugin;
			} else {
				MatchedConfigHousing mch = PluginManager.getManager().resolve(plugin);
				config = mch.getConfig();
				if (config == null) {
					BasePlugin.logErrorMessage("Failed to resolve plugin " + plugin.getPluginName());
					continue;
				}
			}
			if (config.getPluginNature() == EPluggablePluginNature.Validation
					|| config.getPluginNature() == EPluggablePluginNature.Generic) {
				if ((enabledOnly && config.isEnabled()) || !enabledOnly) {
					if (cloneObjects) {
						config = config.clone();
						config.setProjectHandle(project);
					}
					result.add(config);
				} else {
					BasePlugin.logInfo("Ignoring Plugin as it is not enabled " + plugin.getPluginName());
				}
			} else {
				BasePlugin.logInfo("Ignoring Plugin as it is not a Validation or Generic Plugin "
						+ plugin.getPluginName() + " type=" + plugin.getPluginNature());
			}
		}

		BasePlugin.logInfo("Project " + project.getName() + " resolved " + String.valueOf(result.size()) + " plugins.");
		return result.toArray(new IPluginConfig[result.size()]);

	}

	public static PluginHousing[] m1PluginHousings() {
		PluginManager manager = PluginManager.getManager();
		List<PluginHousing> result = new ArrayList<PluginHousing>();

		for (PluginHousing housing : manager.getRegisteredHousings()) {
			if (housing.getPluginNature() == EPluggablePluginNature.Validation
					|| housing.getPluginNature() == EPluggablePluginNature.Generic) {
				result.add(housing);
			}
		}

		return result.toArray(new PluginHousing[result.size()]);
	}

}
