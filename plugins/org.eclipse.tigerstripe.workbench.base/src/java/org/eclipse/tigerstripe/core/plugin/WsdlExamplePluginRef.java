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

import org.eclipse.tigerstripe.api.plugins.builtin.IWSDLExampleProfilePlugin;
import org.eclipse.tigerstripe.core.project.TigerstripeProject;

public class WsdlExamplePluginRef extends PluginRef {

	public final static WsdlExamplePluginRef MODEL = new WsdlExamplePluginRef(
			null);

	private final static String GROUPID = PluginRefFactory.GROUPID_TS;
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

	/* package */WsdlExamplePluginRef(TigerstripeProject project) {
		super(project);
	}

}
