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
package org.eclipse.tigerstripe.workbench.internal.core.util.license;

public final class TSWorkbenchPluggablePluginRole {

	public final static TSWorkbenchPluggablePluginRole NONE = new TSWorkbenchPluggablePluginRole();
	public final static TSWorkbenchPluggablePluginRole DEPLOY_UNDEPLOY = new TSWorkbenchPluggablePluginRole();
	public final static TSWorkbenchPluggablePluginRole CREATE_EDIT = new TSWorkbenchPluggablePluginRole();
	public final static TSWorkbenchPluggablePluginRole UNRECOGNIZED_ROLE = new TSWorkbenchPluggablePluginRole();

	private TSWorkbenchPluggablePluginRole() {
	}

	/*
	 * Returns the pluggable plugin role that corresponds to a string. If an
	 * unrecognized string is found, returns UNRECOGNIZED_ROLE for the pluggable
	 * plugin role
	 */
	public static TSWorkbenchPluggablePluginRole getRole(
			String pluggablePluginRoleStr) {

		if (pluggablePluginRoleStr == null)
			return UNRECOGNIZED_ROLE;

		// check for matching license type string
		if (pluggablePluginRoleStr.equals("None"))
			return NONE;
		else if (pluggablePluginRoleStr.equals("Deploy/Undeploy"))
			return DEPLOY_UNDEPLOY;
		else if (pluggablePluginRoleStr.equals("Create/Edit"))
			return CREATE_EDIT;

		// if no match found, return UNRECOGNIZED_ROLE
		return UNRECOGNIZED_ROLE;

	}

}
