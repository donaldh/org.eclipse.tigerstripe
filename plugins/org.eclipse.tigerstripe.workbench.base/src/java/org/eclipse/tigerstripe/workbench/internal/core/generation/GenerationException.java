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
package org.eclipse.tigerstripe.workbench.internal.core.generation;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;

public class GenerationException extends TigerstripeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -597020633833963651L;
	private IPluginConfig pluginConfig;

	public GenerationException(String message, IPluginConfig pluginConfig) {
		super(message);
		this.pluginConfig = pluginConfig;
	}

	public GenerationException(String message, IPluginConfig pluginConfig,
			Exception e) {
		super(message, e);
		this.pluginConfig = pluginConfig;
	}

	public IPluginConfig getPluginConfig() {
		return this.pluginConfig;
	}
}
