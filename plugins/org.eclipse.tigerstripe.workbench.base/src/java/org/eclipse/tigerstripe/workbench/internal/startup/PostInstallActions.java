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
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.Status;
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
import org.eclipse.tigerstripe.workbench.internal.core.util.FileUtils;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.internal.core.util.ZipFilePackager;
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

	public void run(BundleContext context) throws TigerstripeException {

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

//				checkForUpgrade(workbenchFeatureVersion);

				setupTigerstripeVariables(context);
				createPropertiesFileForHeadlessRun(context);

				createDirectoriesForHeadlessRun(context);

				createTigerstripeEclipseLibrary(context);

				// Bug 634: we need to make sure the Phantom Project path exists
				// before we create the variable for it.
				forceCreationOfPhantomProject(context);
				hasRun = true;
			}
		}
	}

	protected void forceCreationOfPhantomProject(BundleContext context)
			throws TigerstripeException {
		// Make sure the dir exists on the install dir
		// this will force creation is doesn't exist
		PhantomTigerstripeProjectMgr.getInstance().getPhantomProject();

		IPath path = JavaCore
				.getClasspathVariable(ITigerstripeConstants.PHANTOMLIB);
		if (path == null || !path.toFile().exists()) {
			IStatus status = new Status(
					IStatus.ERROR,
					BasePlugin.getPluginId(),
					222,
					ITigerstripeConstants.PHANTOMLIB + " couldn't be resolved.",
					null);
			BasePlugin.log(status);
		}
	}

	/**
	 * #414 If the version has been upgraded, the lib and bin dirs need to be
	 * deleted so the new versions can be copied over
	 * 
	 */
	private void checkForUpgrade(String currentFeatureVersion) {
		Preferences preferences = BasePlugin.getDefault()
				.getPluginPreferences();
		String oldVersion = preferences.getString("workbenchFeatureVersion");
		if (oldVersion == null || !oldVersion.equals(currentFeatureVersion)) {
			File binDir = new File(tigerstripeRuntimeDir + File.separator
					+ "bin");
			binDir.mkdirs();
			Util.deleteDir(binDir);
			binDir.mkdirs();

			File libDir = new File(tigerstripeRuntimeDir + File.separator
					+ "lib");
			libDir.mkdirs();
			Util.deleteDir(libDir);
			libDir.mkdirs();

			preferences.setValue("workbenchFeatureVersion",
					currentFeatureVersion);
		}
	}

	/**
	 * Copies the bin/ and lib/ directories into the runtime root to be
	 * referenced during headless runs
	 * 
	 * @param context
	 */
	private void createDirectoriesForHeadlessRun(BundleContext context)
			throws TigerstripeException {

		File binDir = new File(tigerstripeRuntimeDir + File.separator + "bin");
		binDir.mkdirs();
		if (!binDir.exists()) {
			BasePlugin.logErrorMessage("Couldn't create "
					+ binDir.getAbsolutePath());
		}

		File libDir = new File(tigerstripeRuntimeDir + File.separator + "lib");
		libDir.mkdirs();
		if (!libDir.exists()) {
			BasePlugin.logErrorMessage("Couldn't create "
					+ libDir.getAbsolutePath());
		}

		// Copy files from install root (bin)
		File srcBin = new File(this.baseBundleRoot + "/src/bin");
		File[] srcBinFiles = srcBin.listFiles();
		if (srcBinFiles == null) {
			srcBinFiles = new File[0];
		}
		for (File srcBinFile : srcBinFiles) {

			String binDirPath = binDir.getAbsolutePath();
			if (!(new File(binDirPath + "/" + srcBinFile.getName())).exists()) {
				try {
					if (srcBinFile.isDirectory())
						FileUtils.copyDir(srcBinFile.getAbsolutePath(), binDir
								.getAbsolutePath(), true);
					else {
						FileUtils.copy(srcBinFile.getAbsolutePath(), binDir
								.getAbsolutePath(), true);
					}
				} catch (IOException e) {
					throw new TigerstripeException(
							"Error while copying bin dir for headless run: "
									+ e.getMessage(), e);
				}
			}
		}

		// Copy files from install root (Lib)
		File srcLib = new File(this.baseBundleRoot + "/lib");
		File[] srcLibFiles = srcLib.listFiles();
		if (srcLibFiles == null)
			srcLibFiles = new File[0];
		for (File srcLibFile : srcLibFiles) {
			String libDirPath = libDir.getAbsolutePath();
			if (!(new File(libDirPath + "/" + srcLibFile.getName())).exists()) {
				try {
					if (srcLibFile.isFile())
						FileUtils.copy(srcLibFile.getAbsolutePath(), libDir
								.getAbsolutePath(), true);
					else {
						FileUtils.copyDir(srcLibFile.getAbsolutePath(), libDir
								.getAbsolutePath(), true);
					}
				} catch (IOException e) {
					throw new TigerstripeException(
							"Error while copying lib dir for headless run: "
									+ e.getMessage(), e);
				}
			}
		}

		// The Core .jar
		File rootDir = new File(this.baseBundleRoot);
		File[] rootFiles = rootDir.listFiles();
		if (rootFiles == null)
			rootFiles = new File[0];
		for (File rootFile : rootFiles) {
			if (rootFile.getName().startsWith("ts-headless")) {
				String libDirPath = libDir.getAbsolutePath();
				if (!(new File(libDirPath + "/" + rootFile.getName())).exists()) {
					try {
						FileUtils.copy(rootFile.getAbsolutePath(), libDir
								.getAbsolutePath(), true);
					} catch (IOException e) {
						throw new TigerstripeException(
								"Error while copying core .jar for headless run: "
										+ e.getMessage(), e);
					}
				}
			}
		}

		// finally the External API jar
		String externalAPIJarPath = JavaCore.getClasspathVariable(
				ITigerstripeConstants.EXTERNALAPI_LIB).toOSString();
		File externalAPIJar = new File(externalAPIJarPath);
		if (!(new File(libDir + "/" + externalAPIJar.getName())).exists()) {
			try {
				FileUtils.copy(externalAPIJar.getAbsolutePath(), libDir
						.getAbsolutePath(), true);
			} catch (IOException e) {
				throw new TigerstripeException(
						"Error while copying external API .jar for headless run: "
								+ e.getMessage(), e);
			}
		}

	}

	/**
	 * Assemble all the jars contained in the /lib dir into an Eclipse Library
	 * to be used when people want to use the Tigerstripe API
	 * 
	 * @param context
	 */
	private void createTigerstripeEclipseLibrary(BundleContext context) {
		File libDir = new File(tigerstripeRuntimeDir + File.separator + "lib");

		if (libDir.exists()) {
			// TODO NOT IMPLEMENTED
		}
	}

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

		// Set up the External API lib to be referenced from Tigerstripe
		// Pluggable Plugin Projects
		// This references directly the org.eclipse.tigerstripe.workbench.api
		// bundle
		try {
			Bundle baseBundle = Platform
					.getBundle(TigerstripeRuntime.BASEBUNDLE_ID);
			File bundleDir = FileLocator.getBundleFile(baseBundle);
			String pathStr = bundleDir.getAbsolutePath();

			ZipFilePackager packager = null;
			try {
				packager = new ZipFilePackager(pathStr + File.separator
						+ "ts-external-api.zip", true);
				// the dir with all compiled artifact
				String binDir = pathStr + File.separator + "bin";
				File binDirFile = new File(binDir);

				// In the built version there is no bin dir, but everything is
				// off the root
				if (!binDirFile.exists()) {
					binDir = pathStr;
					binDirFile = new File(binDir);
				}

				FileFilter fileFilter = new FileFilter() {

					public boolean accept(File pathname) {
						boolean result = pathname.toString().contains(
								"org" + File.separator + "eclipse"
										+ File.separator + "tigerstripe")
								&& !pathname.toString().contains("internal");
						return result;
					}

				};
				File[] binFiles = binDirFile.listFiles();

				if (binFiles != null && binFiles.length != 0)
					packager.write(binFiles, "", fileFilter);

				packager.finished();

			} catch (IOException e) {
				BasePlugin.log(e);
			} catch (TigerstripeException e) {
				BasePlugin.log(e);
			} finally {
				if (packager != null) {
					try {
						packager.finished();
					} catch (IOException e) {
						// just ignore it
					}
				}
			}

			String externalPathStr = pathStr + "/ts-external-api.zip";
			// }

			Path externalPath = new Path(externalPathStr);
			JavaCore.setClasspathVariable(
					ITigerstripeConstants.EXTERNALAPI_LIB, externalPath, null);
			// }

			// Bug 1035
			// Now create a classpathVariable for the core as well.
			String internalPathStr = pathStr + "/ts-headless.jar";
			Path internalPath = new Path(internalPathStr);
			JavaCore.setClasspathVariable(
					ITigerstripeConstants.INTERNALAPI_LIB, internalPath, null);

			// Add org.eclipse.equinox.common as a variable that can be
			// referenced from Tigerstripe Plugin projects.
			IPath equinoxPath = findEquinoxCommonJarPath(context);
			JavaCore.setClasspathVariable(ITigerstripeConstants.EQUINOX_COMMON,
					equinoxPath, null);

			// Create lib entries for each Annotation plugin so we can reference
			// them directly
			for (String pluginId : AnnotationUtils.getAnnotationPluginIds()) {

				IPath pPath = AnnotationUtils.getAnnotationPluginPath(pluginId);
				JavaCore.setClasspathVariable(pluginId, pPath, null);
			}
		} catch (IOException e) {
			BasePlugin.log(e);
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
		installProperties.setProperty(TigerstripeRuntime.EXTERNAL_API_ARCHIVE,
				JavaCore.getClasspathVariable(
						ITigerstripeConstants.EXTERNALAPI_LIB).toOSString());

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

	private String findWorkbenchFeatureVersion(BundleContext context) {
		return TigerstripeCore.getRuntimeDetails().getBaseBundleValue(
				Constants.BUNDLE_VERSION);
	}
}
