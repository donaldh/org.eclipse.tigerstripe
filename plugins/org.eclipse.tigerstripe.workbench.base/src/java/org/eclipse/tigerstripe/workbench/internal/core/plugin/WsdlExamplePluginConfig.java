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

import org.eclipse.tigerstripe.workbench.internal.api.plugins.builtin.IWSDLExampleProfilePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;

public class WsdlExamplePluginConfig extends PluginConfig {

	public final static WsdlExamplePluginConfig MODEL = new WsdlExamplePluginConfig(
			null);

	private final static String GROUPID = PluginConfigFactory.GROUPID_TS;
	private final static String PLUGINID = "ossj-wsdl-example-spec";

	@Override
	public String getGroupId() {
		return GROUPID;
	}

	@Override
	public String getActiveVersion() {
		return getProperties().getProperty("activeVersion",
				IWSDLExampleProfilePlugin.defaultVersion);
	}

	@Override
	public String getPluginId() {
		return PLUGINID;
	}

	/* package */WsdlExamplePluginConfig(TigerstripeProject project) {
		super(project);
	}

}
