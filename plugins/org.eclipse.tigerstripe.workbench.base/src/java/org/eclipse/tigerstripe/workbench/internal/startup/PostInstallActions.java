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
package org.eclipse.tigerstripe.workbench.internal.startup;

import java.io.File;

import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;

/**
 * Contains the post install actions to be executed to create a proper directory
 * structure for Tigerstripe to function after a install through the Tigerstripe
 * Update Site
 * 
 */
public class PostInstallActions {

	private static volatile boolean initialized = false;

	public static synchronized void init() {
		if (!initialized) {
			initialized = true;
			try {
				new PostInstallActions().run();
			} catch (TigerstripeException e) {
				BasePlugin.logErrorMessage("Error while PostInstallActions initialization", e);
			}
		}
	}

	private static Boolean hasRun = false;

	private Location installLocation;

	private String tigerstripeRuntimeDir;

	void run() throws TigerstripeException {

		// CSCto45728 [NM]: Commenting out synchronized block as it was causing
		// a deadlock
		// According to API of plugin start method, implementers should avoid
		// synchronized blocks as it may result in deadlocks
		// This method is only called from BasePlugin at the moment so there is
		// no need for a synchronized block.
		// synchronized (hasRun) {
		if (!hasRun) {
			installLocation = Platform.getInstallLocation();

			// We don't want to recreate if it exists as it might contain a
			// key
			if (!tigerstripeRuntimeExists()) {
				createTigerstripeRuntimeDir();
			}
			TigerstripeRuntime.setTigerstripeRuntimeRoot(tigerstripeRuntimeDir);
			TigerstripeRuntime.initLogger();

			if (!tigerstripePluginExists()) {
				createTigerstripePluginDir();
			}

			if (!tigerstripeModulesExists()) {
				createTigerstripeModulesDir();
			}

			// create the phantom project
			// references are set up through an Ext pt
			BasePlugin.getDefault().getPhantomTigerstripeProjectMgr().getPhantomProject();
		}
		hasRun = true;
		// }
	}

	private boolean tigerstripeRuntimeExists() throws TigerstripeException {

		String installationPath = installLocation.getURL().getPath();

		if (installationPath != null && installationPath.length() != 0) {
			tigerstripeRuntimeDir = installationPath + "/" + TigerstripeRuntime.TIGERSTRIPE_HOME_DIR;
			File tsRuntimeDirFile = new File(tigerstripeRuntimeDir);
			return tsRuntimeDirFile.exists();
		}

		throw new TigerstripeException("Unable to located base installation location of Eclipse");
	}

	private void createTigerstripeRuntimeDir() throws TigerstripeException {
		File tsRuntimeDirFile = new File(tigerstripeRuntimeDir);
		tsRuntimeDirFile.mkdirs();
		if (!tsRuntimeDirFile.exists())
			throw new TigerstripeException(
					"Couldn't create Tigerstripe Runtime Directory at " + tsRuntimeDirFile.toURI().toString());
	}

	private boolean tigerstripePluginExists() throws TigerstripeException {
		String pluginPath = tigerstripeRuntimeDir + File.separator + TigerstripeRuntime.TIGERSTRIPE_PLUGINS_DIR;
		File tsPluginDirFile = new File(pluginPath);
		return tsPluginDirFile.exists();
	}

	private void createTigerstripePluginDir() throws TigerstripeException {
		File pluginsDir = new File(tigerstripeRuntimeDir + File.separator + TigerstripeRuntime.TIGERSTRIPE_PLUGINS_DIR);
		pluginsDir.mkdirs();
		if (!pluginsDir.exists())
			throw new TigerstripeException(
					"Couldn't create Tigerstripe Runtime Directory for plugins at " + pluginsDir.toURI().toString());
	}

	private boolean tigerstripeModulesExists() throws TigerstripeException {
		String modulesPath = tigerstripeRuntimeDir + File.separator + TigerstripeRuntime.TIGERSTRIPE_MODULES_DIR;
		File tsModulesDirFile = new File(modulesPath);
		return tsModulesDirFile.exists();
	}

	private void createTigerstripeModulesDir() throws TigerstripeException {
		File modulesDir = new File(tigerstripeRuntimeDir + File.separator + TigerstripeRuntime.TIGERSTRIPE_MODULES_DIR);
		modulesDir.mkdirs();
		if (!modulesDir.exists())
			throw new TigerstripeException(
					"Couldn't create Tigerstripe Runtime Directory for modules at " + modulesDir.toURI().toString());

	}
}
