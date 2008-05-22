/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.test_common;

import java.io.File;

import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * 
 * @author erdillon
 * 
 */
public class BundleUtils {

	private static Location installLocation = Platform.getInstallLocation();

	private String bundleRoot = "";

	private BundleContext context;

	public BundleUtils() {
		// singleton
	}

	public void setBundleRoot(BundleContext context, String pluginID) {
		this.context = context;
		Bundle baseBundle = Platform.getBundle(pluginID);
		bundleRoot = installLocation.getURL().getPath()
				+ File.separator
				+ baseBundle.getLocation().substring(
						baseBundle.getLocation().indexOf("@") + 1,
						baseBundle.getLocation().length());
	}

	public BundleContext getContext() {
		return this.context;
	}

	public String getBundleRoot() {
		return this.bundleRoot;
	}
}
