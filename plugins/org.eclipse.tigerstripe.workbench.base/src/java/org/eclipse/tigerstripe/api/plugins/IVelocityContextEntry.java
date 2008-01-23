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
/**
 * 
 */
package org.eclipse.tigerstripe.api.plugins;

/**
 * This interface captures the minimal requirements for Velocity Context Entries
 * to be instantiated in the Velocity Context of a template.
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IVelocityContextEntry {

	/**
	 * This hook will be called right after instantiation of the Context Entry
	 * to provide an opportunity to access the Plugin Context later during the
	 * lifetime of the Context Entry.
	 * 
	 * @param pluginContext -
	 *            the context of execution of the plugin.
	 */
	public void setIPluginContext(IPluginContext pluginContext);
}
