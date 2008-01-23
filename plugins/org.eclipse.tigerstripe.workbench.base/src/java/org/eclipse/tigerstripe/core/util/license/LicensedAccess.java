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
package org.eclipse.tigerstripe.core.util.license;

import org.eclipse.tigerstripe.api.TigerstripeLicenseException;

/**
 * This class is the singleton shell to validate any Licensed functionality
 * 
 * @author Eric Dillon
 * 
 */
public class LicensedAccess implements ILicensedAccess {

	private static LicensedAccess instance;

	/*
	 * make the default constructor private so that it cannot be called from
	 * outside of this class
	 */
	private LicensedAccess() {
	}

	public static LicensedAccess getInstance()
			throws TigerstripeLicenseException {
		if (instance == null) {
			instance = new LicensedAccess();
			instance.initialize();
		}
		return instance;
	}

	/*
	 * Initialise the Licensing Framework by looking up the license and loading
	 * the corresponding properties.
	 */
	private void initialize() throws TigerstripeLicenseException {

	}

	/**
	 * Asserts that the given feature is accessible with the current license
	 * 
	 * @param feature
	 * @throws TigerstripeLicenseException
	 */
	public void assertLicense(String feature)
			throws TigerstripeLicenseException {
	}

	public static TSWorkbenchPluggablePluginRole getWorkbenchPluggablePluginRole() {
		return TSWorkbenchPluggablePluginRole.CREATE_EDIT;
	}

	public static TSWorkbenchProfileRole getWorkbenchProfileRole() {
		return TSWorkbenchProfileRole.CREATE_EDIT;
	}
}
