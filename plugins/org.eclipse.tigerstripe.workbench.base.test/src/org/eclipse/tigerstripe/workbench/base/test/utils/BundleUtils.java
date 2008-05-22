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
package org.eclipse.tigerstripe.workbench.base.test.utils;

import java.io.File;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import org.eclipse.osgi.service.datalocation.Location;

/**
 * A Singleton class that is initialized at plugin start to provide some
 * convenience methods to the bundle
 * 
 * @author erdillon
 * 
 */
public class BundleUtils {

	private Location installLocation = Platform.getInstallLocation();

	private String bundleRoot = "";

	private BundleContext context;

	private final static String BASETEST_BUNDLE_ID = "org.eclipse.tigerstripe.workbench.base.test";

	public final static BundleUtils INSTANCE = new BundleUtils();

	private BundleUtils() {
		// singleton
	}

	public void setBundleRoot(BundleContext context) {
		this.context = context;
		Bundle baseBundle = Platform.getBundle(BASETEST_BUNDLE_ID);
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
