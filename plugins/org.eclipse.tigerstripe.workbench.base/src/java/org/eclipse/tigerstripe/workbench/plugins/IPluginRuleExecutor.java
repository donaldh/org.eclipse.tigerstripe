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
package org.eclipse.tigerstripe.workbench.plugins;

import org.eclipse.tigerstripe.workbench.internal.core.generation.M1RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePlugin;

public interface IPluginRuleExecutor {

	public PluggablePlugin getPlugin();

	/**
	 * Get the additional info about the run
	 * 
	 * @return
	 */
	public M1RunConfig getConfig();
}
