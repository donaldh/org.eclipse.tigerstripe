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

public final class TSWorkbenchProfileRole {

	public final static TSWorkbenchProfileRole NONE = new TSWorkbenchProfileRole();
	public final static TSWorkbenchProfileRole DEPLOY_UNDEPLOY = new TSWorkbenchProfileRole();
	public final static TSWorkbenchProfileRole CREATE_EDIT = new TSWorkbenchProfileRole();
	public final static TSWorkbenchProfileRole UNRECOGNIZED_ROLE = new TSWorkbenchProfileRole();

	private TSWorkbenchProfileRole() {
	}

	/*
	 * Returns the workbench profile role that corresponds to a string. If an
	 * unrecognized string is found, returns UNRECOGNIZED_ROLE for the workbench
	 * profile role
	 */
	public static TSWorkbenchProfileRole getRole(String workbenchProfileRoleStr) {

		if (workbenchProfileRoleStr == null)
			return UNRECOGNIZED_ROLE;

		// check for matching license type string
		if (workbenchProfileRoleStr.equals("None"))
			return NONE;
		else if (workbenchProfileRoleStr.equals("Deploy/Undeploy"))
			return DEPLOY_UNDEPLOY;
		else if (workbenchProfileRoleStr.equals("Create/Edit"))
			return CREATE_EDIT;

		// if no match found, return UNRECOGNIZED_ROLE
		return UNRECOGNIZED_ROLE;

	}

}
