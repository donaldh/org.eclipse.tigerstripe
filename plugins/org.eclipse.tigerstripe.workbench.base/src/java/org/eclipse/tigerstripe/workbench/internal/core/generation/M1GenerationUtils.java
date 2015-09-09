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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginHousing;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginManager;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggableHousing;
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
	public static IPluginConfig[] m1PluginConfigs(
			ITigerstripeModelProject project, boolean enabledOnly,
			boolean cloneObjects) throws TigerstripeException {

		List<IPluginConfig> result = new ArrayList<IPluginConfig>();

		PluginManager manager = PluginManager.getManager();
		if (!PluginManager.isOsgiVersioning()) {
			for (IPluginConfig config : project.getPluginConfigs()) {
				IPluginConfig theConfig = config;
				if (cloneObjects) {
					theConfig = config.clone();
					theConfig.setProjectHandle(project);
				}
				if (config.getPluginNature() == EPluggablePluginNature.Validation
						|| config.getPluginNature() == EPluggablePluginNature.Generic) {
					if ((enabledOnly && theConfig.isEnabled()) || !enabledOnly) {
						result.add(theConfig);
					}
				}
			}

		} else {
			Map<String, Collection<PluggableHousing>> housingNameMap = new HashMap<String, Collection<PluggableHousing>>();
			for (PluggableHousing housing : manager
					.getRegisteredPluggableHousings()) {
				String name = housing.getPluginName();
				if (housingNameMap.containsKey(name)) {
					housingNameMap.get(name).add(housing);
				} else {
					Collection<PluggableHousing> phs = new ArrayList<PluggableHousing>();
					phs.add(housing);
					housingNameMap.put(name, phs);
				}
			}
			for (String name : housingNameMap.keySet()) {
				MatchedConfigHousing mch = manager.resolve(
						housingNameMap.get(name), project.getPluginConfigs());
				IPluginConfig config = mch.getConfig();
				if (config != null) {
					IPluginConfig theConfig = config;
					if (cloneObjects) {
						theConfig = config.clone();
						theConfig.setProjectHandle(project);
					}
					if (config.getPluginNature() == EPluggablePluginNature.Validation
							|| config.getPluginNature() == EPluggablePluginNature.Generic) {
						if ((enabledOnly && theConfig.isEnabled())
								|| !enabledOnly) {
							result.add(theConfig);
						}
					}
				}
			}
		}
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
