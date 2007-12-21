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
package org.eclipse.tigerstripe.api.external.plugins;

import org.eclipse.tigerstripe.api.plugins.pluggable.IPluggablePluginProject;

public interface IextPluggablePluginProperty {

	public String getType();

	public IPluggablePluginProject getProject();

	/**
	 * Name of that property
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * The tiptool text that will appear in the GUI
	 * 
	 * @return
	 */
	public String getTipToolText();

	/**
	 * Value of that property
	 * 
	 * @return
	 */
	public Object getDefaultValue();

}
