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
package org.eclipse.tigerstripe.metamodel;

import org.eclipse.core.runtime.Plugin;

public class Activator extends Plugin {
	public final static String PLUGIN_ID = "org.eclipse.tigerstripe.metadata";

	// The shared instance.
	private static Activator plugin;

	public Activator() {
		super();
		plugin = this;
	}

	public static Activator getDefault() {
		return plugin;
	}

	public static String getPluginId() {
		return PLUGIN_ID;
	}

}
