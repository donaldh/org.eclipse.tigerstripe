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
package org.eclipse.tigerstripe.core.plugin;

import org.eclipse.tigerstripe.api.plugins.builtin.ICSVCreatePlugin;
import org.eclipse.tigerstripe.core.project.TigerstripeProject;

public class CSVCreatePluginRef extends PluginRef {

	public final static CSVCreatePluginRef MODEL = new CSVCreatePluginRef(null);

	private final static String GROUPID = "ts";
	private final static String PLUGINID = "csv-spec";

	/* package */CSVCreatePluginRef(TigerstripeProject project) {
		super(project);
	}

	@Override
	public String getActiveVersion() {
		return getProperties().getProperty("activeVersion",
				ICSVCreatePlugin.defaultVersion);
	}

	@Override
	public String getPluginId() {
		return PLUGINID;
	}

	@Override
	public String getGroupId() {
		return GROUPID;
	}

}
