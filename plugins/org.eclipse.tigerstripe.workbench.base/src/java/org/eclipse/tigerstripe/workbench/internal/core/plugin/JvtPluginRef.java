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
package org.eclipse.tigerstripe.workbench.internal.core.plugin;

import org.eclipse.tigerstripe.workbench.internal.api.plugins.builtin.IOssjJVTProfilePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;

public class JvtPluginRef extends PluginRef {

	public final static JvtPluginRef MODEL = new JvtPluginRef(null);

	private final static String GROUPID = PluginRefFactory.GROUPID_TS;
	private final static String PLUGINID = "ossj-jvt-spec";

	@Override
	public String getActiveVersion() {
		return getProperties().getProperty("activeVersion",
				IOssjJVTProfilePlugin.defaultVersion);
	}

	@Override
	public String getGroupId() {
		return GROUPID;
	}

	@Override
	public String getPluginId() {
		return PLUGINID;
	}

	/* package */JvtPluginRef(TigerstripeProject project) {
		super(project);
	}

}
