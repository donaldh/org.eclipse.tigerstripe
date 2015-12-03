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

		List<IPluginConfig> result = new ArrayList<IPluginConfig>();

		for (IPluginConfig plugin : project.getPluginConfigs()) {
			if (plugin.getPluginNature() == EPluggablePluginNature.Validation
					|| plugin.getPluginNature() == EPluggablePluginNature.Generic) {
				if ((enabledOnly && plugin.isEnabled()) || !enabledOnly) {
					MatchedConfigHousing mch = PluginManager.getManager().resolve(plugin);
					IPluginConfig config = mch.getConfig();
					if (cloneObjects) {
						config = config.clone();
						config.setProjectHandle(project);
					}
					result.add(config);
				} else {
					BasePlugin.logInfo("Ignoring Plugin as it is not enabled " + plugin.getPluginName());
				}
			} else {
				BasePlugin.logInfo("Ignoring Plugin as it is not Validation or Generic " + plugin.getPluginName()
						+ " type=" + plugin.getPluginNature());
			}
		}

		BasePlugin.logInfo("Resolved the following plugins: " + result);

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
