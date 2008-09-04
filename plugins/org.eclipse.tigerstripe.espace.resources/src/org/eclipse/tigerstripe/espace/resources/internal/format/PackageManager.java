/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.espace.resources.internal.format;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.tigerstripe.espace.resources.ResourcesPlugin;
import org.eclipse.tigerstripe.espace.resources.core.IPackageFinder;

/**
 * @author Yuri Strot
 *
 */
public class PackageManager {
	
	private static PackageManager INSTANCE;

	private static final String PACKAGE_FINDER_EXTPT = ResourcesPlugin.PLUGIN_ID + ".packageFinder";
	
	private PackageManager() {
	}
	
	private IPackageFinder finder;
	
	public static PackageManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PackageManager();
		}
		return INSTANCE;
	}
	
	public IPackageFinder getPackageFinder() {
		if (finder == null) {
			try {
				IConfigurationElement[] configs = Platform
					.getExtensionRegistry().getConfigurationElementsFor(
						PACKAGE_FINDER_EXTPT);
				if (configs.length > 0) {
					finder = (IPackageFinder)configs[0].createExecutableExtension("class");
				}
			}
			catch (Exception e) {
			}
		}
		return finder;
	}

}
