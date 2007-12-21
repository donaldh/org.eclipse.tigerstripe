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
package org.eclipse.tigerstripe.api.modules;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.tigerstripe.api.API;
import org.eclipse.tigerstripe.api.project.IDependency;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;

/**
 * This is a singleton class loads all external modules and caches them.
 * 
 */
public class ExternalModules {

	private static ExternalModules instance;

	public static boolean modulesExist;

	public static HashMap moduleMapByFile = new HashMap<String, String>();

	public static Map modules = new HashMap<String, String>();

	public static Map baseModules = new HashMap<String, String>();

	protected ExternalModules() {
		reload();
	}

	public void reload() {
		moduleMapByFile.clear();
		modules.clear();
		baseModules.clear();

		modulesExist = loadModules();
	}

	/**
	 * Returns the instance.
	 * 
	 */
	public static ExternalModules getInstance() {

		if (instance == null) {
			instance = new ExternalModules();
		}
		return instance;
	}

	// Shipped base modules don't exist anymore since tsw1.2 @see #299
	// /**
	// * Returns an array of all valid base modules that are shipped with
	// * the current install of TS.
	// *
	// * @return
	// * @since 1.0.3
	// */
	// public IDependency[] getShippedBaseModules() {
	//
	// List<IDependency> resultList = new ArrayList<IDependency>();
	//
	// if (baseModules.containsKey("base")) {
	// String allValues = (String) baseModules.get("base");
	// String[] allModules = allValues.split("~");
	// for (int i = 0; i < allModules.length; i++) {
	// String[] moduleName = allModules[i].split(":");
	// try {
	// IDependency dep = API.getDefaultProjectSession()
	// .makeIDependency( moduleMapByFile.get(moduleName[0]) + File.separator +
	// moduleName[0]);
	// resultList.add(dep);
	// } catch ( TigerstripeException e ) {
	// // invalid module, ignore
	// } catch ( TigerstripeLicenseException e ) {
	// // invalid module, ignore
	// }
	// }
	// }
	// IDependency[] result = new IDependency[ resultList.size() ];
	// return resultList.toArray(result);
	// }
	//
	public static File getModuleFolder() {

		String path = TigerstripeRuntime.getTigerstripeRuntimeRoot()
				+ File.separator + TigerstripeRuntime.TIGERSTRIPE_MODULES_DIR;

		return new File(path);
	}

	/**
	 * Loads all modules from the "modules" folder
	 * 
	 */
	private static boolean loadModules() {
		boolean returnValue = false;

		File moduleFolder = getModuleFolder();

		// The "modules" folder exists
		if (moduleFolder.exists()) {
			returnValue = loadContents(moduleFolder);

		}
		return returnValue;
	}

	/**
	 * Loads all information for the modules using the Dependency class
	 * 
	 * @param fullPath
	 * @return
	 */
	private static String getModuleDetails(String fullPath) {
		String details = "";

		try {
			IDependency dep = API.getDefaultProjectSession().makeIDependency(
					fullPath);
			StringBuffer detBuff = new StringBuffer();
			detBuff.append(dep.getIProjectDetails().getName());
			detBuff.append(":");
			detBuff.append(dep.getIModuleHeader().getModuleID());
			detBuff.append(":");
			detBuff.append(dep.getIProjectDetails().getVersion());
			detBuff.append(":");
			detBuff.append(dep.getIProjectDetails().getDescription());
			details = detBuff.toString();

			return details;
		} catch (Exception e) {
			TigerstripeRuntime.logErrorMessage("Exception detected", e);
		} finally {
			return details;
		}
	}

	private static boolean loadContents(File dir) {
		boolean containsDirectories = false;
		boolean baseFolder = false;
		String[] files;
		String fol = "sb";
		files = dir.list();
		boolean end = false;
		StringBuffer concatFolder = new StringBuffer();
		for (int i = 0; i < files.length; i++) {
			File f = new File(dir, files[i]);
			if (f.isDirectory()) {
				containsDirectories = true;
				loadContents(f);
			} else {
				if (end) {
					concatFolder.append("~");
				}
				end = true;
				concatFolder.append(f.getName());
				if (!dir.getName().equalsIgnoreCase("modules")) {
					String[] pathSplit = f.getAbsoluteFile().toString().split(
							f.getName());
					moduleMapByFile.put(f.getName(), pathSplit[0]);
					String[] sp = f.getAbsolutePath().split(f.getName());
					String[] anSp = sp[0].split("modules");
					fol = anSp[1].substring(1, anSp[1].length() - 1);
					concatFolder.append(":");
					concatFolder.append(getModuleDetails(f.getAbsolutePath()));
				} else {
					baseFolder = true;
					String[] pathSplit = f.getAbsoluteFile().toString().split(
							f.getName());
					moduleMapByFile.put(f.getName(), pathSplit[0]);
					String[] sp = f.getAbsolutePath().split(f.getName());
					concatFolder.append(":");
					concatFolder.append(getModuleDetails(f.getAbsolutePath()));
				}
			}
		}
		if (end) {
			if (!fol.equalsIgnoreCase("sb")) {
				modules.put(fol, concatFolder.toString());
			}
		}
		if (baseFolder) {
			baseModules.put("base", concatFolder.toString());
		}

		return containsDirectories;
	}

}
