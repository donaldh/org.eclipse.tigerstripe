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
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginHousing;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginManager;
import org.eclipse.tigerstripe.workbench.plugins.EPluggablePluginNature;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class M0GenerationUtils {

	/**
	 * Returns an array containing all the {@link IPluginConfig} defined for the
	 * given Model project
	 * 
	 * @param project
	 * @param enabledOnly
	 * @return
	 */
	public static IPluginConfig[] m0PluginConfigs(
			ITigerstripeModelProject project, boolean enabledOnly,
			boolean cloneObjects) throws TigerstripeException {
		List<IPluginConfig> result = new ArrayList<IPluginConfig>();

		for (IPluginConfig config : project.getPluginConfigs()) {
			IPluginConfig theConfig = config;
			if (cloneObjects) {
				theConfig.setProjectHandle(project);
				theConfig = config.clone();
			}
			if (config.getPluginNature() == EPluggablePluginNature.M0) {
				if (enabledOnly && theConfig.isEnabled()) {
					result.add(theConfig);
				} else {
					result.add(theConfig);
				}
			}
		}
		return result.toArray(new IPluginConfig[result.size()]);
	}

	public static PluginHousing[] m0PluginHousings() {
		PluginManager manager = PluginManager.getManager();
		List<PluginHousing> result = new ArrayList<PluginHousing>();

		for (PluginHousing housing : manager.getRegisteredHousings()) {
			if (housing.getPluginNature() == EPluggablePluginNature.M0) {
				result.add(housing);
			}
		}

		return result.toArray(new PluginHousing[result.size()]);
	}

}
