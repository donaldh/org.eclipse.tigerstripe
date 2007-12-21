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

import org.eclipse.tigerstripe.api.plugins.builtin.IOssjWSDLProfilePlugin;
import org.eclipse.tigerstripe.core.project.TigerstripeProject;

public class WsdlPluginRef extends PluginRef {

	public final static WsdlPluginRef MODEL = new WsdlPluginRef(null);

	private final static String GROUPID = PluginRefFactory.GROUPID_TS;
	private final static String PLUGINID = "ossj-wsdl-spec";

	@Override
	public String getActiveVersion() {
		return getProperties().getProperty("activeVersion",
				IOssjWSDLProfilePlugin.defaultVersion);
	}

	@Override
	public String getGroupId() {
		return GROUPID;
	}

	@Override
	public String getPluginId() {
		return PLUGINID;
	}

	/* package */WsdlPluginRef(TigerstripeProject project) {
		super(project);
	}

}
