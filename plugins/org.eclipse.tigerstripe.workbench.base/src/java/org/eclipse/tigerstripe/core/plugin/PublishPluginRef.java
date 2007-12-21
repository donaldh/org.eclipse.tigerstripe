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

import org.eclipse.tigerstripe.api.plugins.builtin.IPublishPlugin;
import org.eclipse.tigerstripe.core.project.TigerstripeProject;

public class PublishPluginRef extends PluginRef {

	public final static PublishPluginRef MODEL = new PublishPluginRef(null);

	private final static String GROUPID = "ts";
	private final static String PLUGINID = "publisher";

	/* package */PublishPluginRef(TigerstripeProject project) {
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
