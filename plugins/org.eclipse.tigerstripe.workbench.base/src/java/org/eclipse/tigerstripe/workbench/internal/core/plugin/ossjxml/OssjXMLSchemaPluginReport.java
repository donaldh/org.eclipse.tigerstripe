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
package org.eclipse.tigerstripe.workbench.internal.core.plugin.ossjxml;

import org.eclipse.tigerstripe.workbench.internal.core.plugin.PackageToSchemaMapper;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginReport;

public class OssjXMLSchemaPluginReport extends PluginReport {

	private PackageToSchemaMapper mapper;

	public OssjXMLSchemaPluginReport(PluginConfig pluginConfig) {
		super(pluginConfig);
	}

	public PackageToSchemaMapper getMapper() {
		return this.mapper;
	}

	public void setMapper(PackageToSchemaMapper mapper) {
		this.mapper = mapper;
	}
}
