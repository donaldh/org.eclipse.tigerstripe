/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     J. Strawn (Cisco Systems, Inc.) - Initial implementation
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.optional.buckminster;

import org.eclipse.buckminster.runtime.LogAwarePlugin;
import org.eclipse.buckminster.runtime.Logger;
import org.osgi.framework.BundleContext;

public class TigerstripePlugin extends LogAwarePlugin {

	public static final String PLUGIN_ID = "org.eclipse.tigerstripe.workbench.optional.buckminster"; //$NON-NLS-1$

	private static TigerstripePlugin plugin;

	public static TigerstripePlugin getDefault() {
		return plugin;
	}

	public static Logger getLogger() {
		return plugin.getBundleLogger();
	}

	public TigerstripePlugin() {
		plugin = this;
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}
}
