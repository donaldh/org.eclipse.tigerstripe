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

import org.eclipse.tigerstripe.workbench.internal.api.plugins.builtin.IPublishPlugin;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;

public class PublishPluginConfig extends PluginConfig {

	public final static PublishPluginConfig MODEL = new PublishPluginConfig(null);

	private final static String GROUPID = "ts";
	private final static String PLUGINID = "publisher";

	/* package */PublishPluginConfig(TigerstripeProject project) {
		super(project);
	}

	@Override
	public String getActiveVersion() {
		return getProperties().getProperty("activeVersion",
				IPublishPlugin.defaultVersion);
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
