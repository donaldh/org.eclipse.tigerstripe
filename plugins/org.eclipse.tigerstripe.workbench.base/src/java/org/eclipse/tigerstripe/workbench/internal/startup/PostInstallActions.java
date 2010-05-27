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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.annotation.AnnotationUtils;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.profile.PhantomTigerstripeProjectMgr;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

/**
 * Contains the post install actions to be executed to create a proper directory
 * structure for Tigerstripe to function after a install through the Tigerstripe
 * Update Site
 * 
 */
public class PostInstallActions {

	// TODO this should create a log file!!

	private static Boolean hasRun = false;

	private Location installLocation;

	private String tigerstripeRuntimeDir;

	private String workbenchFeatureVersion;

	private String baseBundleRoot = null;

	public void run(final BundleContext context) throws TigerstripeException {

		synchronized (hasRun) {
			if (!hasRun) {
				installLocation = Platform.getInstallLocation();

				// We don't want to recreate if it exists as it might contain a
				// key
				if (!tigerstripeRuntimeExists(context)) {
					createTigerstripeRuntimeDir(context);
				}
				TigerstripeRuntime
						.setTigerstripeRuntimeRoot(tigerstripeRuntimeDir);
				TigerstripeRuntime.initLogger();

				if (!tigerstripePluginExists(context)) {
					createTigerstripePluginDir(context);
				}

				if (!tigerstripeModulesExists(context)) {
					createTigerstripeModulesDir(context);
				}

				baseBundleRoot = findBaseBundleRoot(context);
				workbenchFeatureVersion = findWorkbenchFeatureVersion(context);

				// checkForUpgrade(workbenchFeatureVersion);
				
				// create the phatom project
				// references are set up through an Ext pt
				PhantomTigerstripeProjectMgr.getInstance().getPhantomProject();
			
//				Job setUpPhantom = new Job("Set up TS Phantom"){
//
//					public IStatus run(IProgressMonitor monitor){
//						try {
//							System.out.println("setUpV begin: " + new Date());
//							setupTigerstripeVariables(context);
//							System.out.println("createProps begin: " + new Date());
//							createPropertiesFileForHeadlessRun(context);
//							System.out.println("forceCreate begin: " + new Date());
//							// This does nothing
//							//				createTigerstripeEclipseLibrary(context);
//
//							// Bug 634: we need to make sure the Phantom Project path exists
//							// before we create the variable for it.
//							forceCreationOfPhantomProject(context);
//							System.out.println("Alll done: " + new Date());
//						} catch (TigerstripeException e){
//							e.printStackTrace();
//						}
//						return Status.OK_STATUS;
//					}
//				};

//				setUpPhantom.schedule();

				hasRun = true;
			}
		}
	}

//	protected void forceCreationOfPhantomProject(BundleContext context)
//			throws TigerstripeException {
//		// Make sure the dir exists on the install dir
//		// this will force creation is doesn't exist
//		PhantomTigerstripeProjectMgr.getInstance().getPhantomProject();
//
//		IPath path = JavaCore
//				.getClasspathVariable(ITigerstripeConstants.PHANTOMLIB);
//		if (path == null || !path.toFile().exists()) {
//			IStatus status = new Status(
//					IStatus.ERROR,
//					BasePlugin.getPluginId(),
//					222,
//					ITigerstripeConstants.PHANTOMLIB + " couldn't be resolved.",
//					null);
//			BasePlugin.log(status);
//		}
//	}
//
//	/**
//	 * Copies the bin/ and lib/ directories into the runtime root to be
//	 * referenced during headless runs
//	 * 
//	 * @param context
//	 */
//	private void createDirectoriesForHeadlessRun(BundleContext context)
//			throws TigerstripeException {
//
//	}
//
//	/**
//	 * Assemble all the jars contained in the /lib dir into an Eclipse Library
//	 * to be used when people want to use the Tigerstripe API
//	 * 
//	 * @param context
//	 */
//	private void createTigerstripeEclipseLibrary(BundleContext context) {
//		File libDir = new File(tigerstripeRuntimeDir + File.separator + "lib");
//
//		if (libDir.exists()) {
//			// TODO NOT IMPLEMENTED
//		}
//	}

	private boolean tigerstripeRuntimeExists(BundleContext context)
			throws TigerstripeException {

		String installationPath = installLocation.getURL().getPath();

		if (installationPath != null && installationPath.length() != 0) {
			tigerstripeRuntimeDir = installationPath + "/"
					+ TigerstripeRuntime.TIGERSTRIPE_HOME_DIR;
			File tsRuntimeDirFile = new File(tigerstripeRuntimeDir);
			return tsRuntimeDirFile.exists();
		}

		throw new TigerstripeException(
				"Unable to located base installation location of Eclipse");
	}

	private void createTigerstripeRuntimeDir(BundleContext context)
			throws TigerstripeException {
		File tsRuntimeDirFile = new File(tigerstripeRuntimeDir);
		tsRuntimeDirFile.mkdirs();
		if (!tsRuntimeDirFile.exists())
			throw new TigerstripeException(
					"Couldn't create Tigerstripe Runtime Directory at "
							+ tsRuntimeDirFile.toURI().toString());
	}

	private boolean tigerstripePluginExists(BundleContext context)
			throws TigerstripeException {
		String pluginPath = tigerstripeRuntimeDir + File.separator
				+ TigerstripeRuntime.TIGERSTRIPE_PLUGINS_DIR;
		File tsPluginDirFile = new File(pluginPath);
		return tsPluginDirFile.exists();
	}

	private void createTigerstripePluginDir(BundleContext context)
			throws TigerstripeException {
		File pluginsDir = new File(tigerstripeRuntimeDir + File.separator
				+ TigerstripeRuntime.TIGERSTRIPE_PLUGINS_DIR);
		pluginsDir.mkdirs();
		if (!pluginsDir.exists())
			throw new TigerstripeException(
					"Couldn't create Tigerstripe Runtime Directory for plugins at "
							+ pluginsDir.toURI().toString());
	}

	private boolean tigerstripeModulesExists(BundleContext context)
			throws TigerstripeException {
		String modulesPath = tigerstripeRuntimeDir + File.separator
				+ TigerstripeRuntime.TIGERSTRIPE_MODULES_DIR;
		File tsModulesDirFile = new File(modulesPath);
		return tsModulesDirFile.exists();
	}

	private void createTigerstripeModulesDir(BundleContext context)
			throws TigerstripeException {
		File modulesDir = new File(tigerstripeRuntimeDir + File.separator
				+ TigerstripeRuntime.TIGERSTRIPE_MODULES_DIR);
		modulesDir.mkdirs();
		if (!modulesDir.exists())
			throw new TigerstripeException(
					"Couldn't create Tigerstripe Runtime Directory for modules at "
							+ modulesDir.toURI().toString());

	}

	/**
	 * Setup variables that will be used in Tigerstripe projects
	 * 
	 * @param context
	 */
	private void setupTigerstripeVariables(BundleContext context) {

		// Set up the Phantom Lib to be referenced in all Tigerstripe Model
		// Projects
		// NOTE: the actual dir is created later, but the reference is needed
		// before forceCreationOfPhantomProject to be stored in the properties
		// file.
		try {
			String pathStr = PhantomTigerstripeProjectMgr.getInstance()
					.getPhantomURI().getPath()
					+ ITigerstripeConstants.PHANTOMLIB_DEF;
			Path phantomPath = new Path(pathStr);
			JavaCore.setClasspathVariable(ITigerstripeConstants.PHANTOMLIB,
					phantomPath, null);
		} catch (JavaModelException e) {
			BasePlugin.log(e);
		}

		

		try {
			// Add org.eclipse.equinox.common as a variable that can be
			// referenced from Tigerstripe Plugin projects.
			IPath equinoxPath = findEquinoxCommonJarPath(context);
			JavaCore.setClasspathVariable(ITigerstripeConstants.EQUINOX_COMMON,
					equinoxPath, null);

			// Add org.eclipse.tigerstripe.workbench.base as a variable that can
			// be referenced from Tigerstripe plugin projects too
			IPath tigerstripeBasePath = findTigerstripeBaseJarPath(context);
			JavaCore.setClasspathVariable(
					ITigerstripeConstants.EXTERNALAPI_LIB, tigerstripeBasePath,
					null);

			// Create lib entries for each Annotation plugin so we can reference
			// them directly
			for (String pluginId : AnnotationUtils.getAnnotationPluginIds()) {
				IPath pPath = AnnotationUtils.getAnnotationPluginPath(pluginId);
				JavaCore.setClasspathVariable(pluginId, pPath, null);
			}
			// } catch (IOException e) {
			// BasePlugin.log(e);
		} catch (JavaModelException e) {
			BasePlugin.log(e);
		}
	}

	/**
	 * Save in the ${tigerstripe.home} directory a set of properties detailing
	 * the install so it can be read outside of Eclipse in a headless run
	 * 
	 * @param context
	 */
	private void createPropertiesFileForHeadlessRun(BundleContext context)
			throws TigerstripeException {
		// ED-20090109: I believe this should be removed?
		Properties installProperties = new Properties();

		installProperties.setProperty(
				TigerstripeRuntime.TIGERSTRIPE_RUNTIME_ROOT,
				tigerstripeRuntimeDir);
		installProperties.setProperty(TigerstripeRuntime.BASEBUNDLE_ROOT,
				baseBundleRoot);
		installProperties.setProperty(
				TigerstripeRuntime.TIGERSTRIPE_FEATURE_VERSION,
				workbenchFeatureVersion);
		installProperties.setProperty(TigerstripeRuntime.PRODUCT_NAME,
				"Tigerstripe Workbench");
		installProperties.setProperty(TigerstripeRuntime.PHANTOM_ARCHIVE,
				JavaCore.getClasspathVariable(ITigerstripeConstants.PHANTOMLIB)
						.toOSString());
		// installProperties.setProperty(TigerstripeRuntime.EXTERNAL_API_ARCHIVE,
		// JavaCore.getClasspathVariable(
		// ITigerstripeConstants.EXTERNALAPI_LIB).toOSString());

		// And save
		FileOutputStream oStream = null;
		try {
			oStream = new FileOutputStream(tigerstripeRuntimeDir
					+ File.separator + TigerstripeRuntime.PROPERTIES_FILE);
			installProperties.store(oStream,
					"#DO NOT EDIT - THIS FILE WAS AUTOMATICALLY GENERATED.");
		} catch (FileNotFoundException e) {
			throw new TigerstripeException(
					"Couldn't create install.properties in "
							+ tigerstripeRuntimeDir, e);
		} catch (IOException e) {
			throw new TigerstripeException(
					"Exception while trying to write content of install.properties in "
							+ tigerstripeRuntimeDir, e);
		} finally {
			if (oStream != null) {
				try {
					oStream.close();
				} catch (IOException e) {
					// ignore here
				}
			}
		}
	}

	private String findBaseBundleRoot(BundleContext context) {
		Bundle baseBundle = Platform
				.getBundle(TigerstripeRuntime.BASEBUNDLE_ID);
		if (baseBundle != null)
			return installLocation.getURL().getPath()
					+ File.separator
					+ baseBundle.getLocation().substring(
							baseBundle.getLocation().indexOf("@") + 1,
							baseBundle.getLocation().length());
		return "unknown_location_for_org.eclipse.tigerstripe.workbench.base";
	}

	private IPath findEquinoxCommonJarPath(BundleContext context) {
		Bundle b = Platform.getBundle("org.eclipse.equinox.common");
		try {
			File bFile = FileLocator.getBundleFile(b);
			return (new Path(bFile.getAbsolutePath())).makeAbsolute();
		} catch (IOException e) {
			BasePlugin.log(e);
		}
		return new Path("unknown_location_for_org.eclipse.equinox.common");
	}

	private IPath findTigerstripeBaseJarPath(BundleContext context) {
		Bundle b = Platform.getBundle("org.eclipse.tigerstripe.workbench.base");
		try {
			File bFile = FileLocator.getBundleFile(b);
			if (bFile.getName().endsWith(".jar")) {
				return (new Path(bFile.getAbsolutePath())).makeAbsolute();
			} else {
				return (new Path(bFile.getAbsolutePath() + File.separator
						+ "bin")).makeAbsolute();
			}
		} catch (IOException e) {
			BasePlugin.log(e);
		}
		return new Path(
				"unknown_location_for_org.eclipse.tigerstripe.workbench.base");
	}

	private String findWorkbenchFeatureVersion(BundleContext context) {
		return TigerstripeCore.getRuntimeDetails().getBaseBundleValue(
				Constants.BUNDLE_VERSION);
	}
}
