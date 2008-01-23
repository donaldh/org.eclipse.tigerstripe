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
package org.eclipse.tigerstripe.workbench.internal.api.plugins.pluggable;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.plugins.IPluggablePluginProperty;

/**
 * This interface is to be implemented by classes that need to persist changes
 * made to IPluggablePluginProperties when they are rendered.
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IPluggablePluginPropertyListener {

	/**
	 * Stores the current value of the IPluggablePluginProperty
	 * 
	 * @param property
	 * @param value
	 * @throws TigerstripeException
	 *             if the operation failed
	 */
	public void storeProperty(IPluggablePluginProperty property, Object value)
			throws TigerstripeException;
}
