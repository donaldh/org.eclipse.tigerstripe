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
package org.eclipse.tigerstripe.workbench.internal.api.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.eclipse.tigerstripe.metamodel.impl.IPrimitiveTypeImpl;
import org.eclipse.tigerstripe.metamodel.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.profile.IActiveWorkbenchProfileChangeListener;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.profile.WorkbenchProfile;
import org.eclipse.tigerstripe.workbench.internal.core.util.FileUtils;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfileSession;
import org.eclipse.tigerstripe.workbench.profile.primitiveType.IPrimitiveTypeDef;

/**
 * Singleton class acting as session facade for all operations on
 * IWorkbenchProfiles
 * 
 * The active IWorkbenchProfile is lazy-loaded upon first access. If the
 * default.wbp file is not found upon load, a built-in minimal profile is
 * provided.
 * 
 * There is only 1 active profile per instance of Tigerstripe workbench. It can
 * be changed and reloaded (from its default file location) at any time.
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class WorkbenchProfileSession implements IWorkbenchProfileSession {

	private ArrayList<IActiveWorkbenchProfileChangeListener> activeProfileListeners = new ArrayList<IActiveWorkbenchProfileChangeListener>();

	private IWorkbenchProfile activeProfile;

	private HashMap<String, IWorkbenchProfile> profileCache = new HashMap<String, IWorkbenchProfile>();

	public IWorkbenchProfile makeWorkbenchProfile() {
		return new WorkbenchProfile();
	}

	/**
	 * Saves the given profile as the active profile and creates a rollback file
	 * 
	 * @param profile
	 * @return boolean - true if rollback file was created
	 */
	public boolean saveAsActiveProfile(IWorkbenchProfile profile)
			throws TigerstripeException {
		return saveAsActiveProfile(profile, true);
	}

	/**
	 * Saves the given profile as the active profile and creates a rollback file
	 * 
	 * @param profile
	 * @return boolean - true if rollback file was created
	 */
	protected boolean saveAsActiveProfile(IWorkbenchProfile profile,
			boolean createRollback) throws TigerstripeException {
		boolean rollbackCreated = false;

		String filename = getActiveProfileFilename();
		File file = new File(filename);

		// create a roll back file if necessary
		if (createRollback) {
			if (file.exists()) {
				File rollbackFile = new File(filename + ".bak");
				try {
					FileUtils.copy(file.getAbsolutePath(), rollbackFile
							.getAbsolutePath(), true);
					rollbackCreated = rollbackFile.exists();
				} catch (IOException e) {
					throw new TigerstripeException(
							"Error while creating rollback file:"
									+ e.getMessage(), e);
				}

			}
		}

		Collection<IPrimitiveTypeDef> primitiveTypes = profile
				.getPrimitiveTypeDefs(false);
		List badTypesList = new ArrayList();
		for (IPrimitiveTypeDef primitiveType : primitiveTypes) {
			if (!primitiveType.isValidName()) {
				badTypesList.add(primitiveType.getName());
			}
		}
		// if error was found with at least one of the primitive type names,
		// roll back and throw an exception
		if (badTypesList.size() > 0) {
			if (rollbackCreated) {
				// copy back the rollback to leave a working profile
				File rollbackFile = new File(filename + ".bak");
				try {
					FileUtils.copy(rollbackFile.getAbsolutePath(), file
							.getAbsolutePath(), true);
					rollbackCreated = false;
				} catch (IOException e) {
					throw new TigerstripeException(
							"Error while trying to roll back: "
									+ e.getMessage(), e);
				}
			}
			throw new TigerstripeException(
					"Error while trying to save active profile '"
							+ profile.getName()
							+ "' -> invalid "
							+ ArtifactMetadataFactory.INSTANCE.getMetadata(
									IPrimitiveTypeImpl.class.getName())
									.getLabel() + " names detected: "
							+ badTypesList.toString());
		}

		FileWriter writer = null;
		try {
			writer = new FileWriter(file);
			writer.write(profile.asText());
			writer.close();
		} catch (IOException e) {
			// Make sure the current profile is intact
			if (rollbackCreated) {
				// copy back the rollback to leave a working profile
				File rollbackFile = new File(filename + ".bak");
				try {
					FileUtils.copy(rollbackFile.getAbsolutePath(), file
							.getAbsolutePath(), true);
					rollbackCreated = false;
				} catch (IOException ee) {
					// ignore
				}
			}

			throw new TigerstripeException(
					"Error while trying to save active profile '"
							+ profile.getName() + "': " + e.getMessage(), e);

		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// ignore
				}
			}

		}
		return rollbackCreated;
	}

	public String getActiveProfileFilename() {
		String rootInstallDir = TigerstripeRuntime.getTigerstripeRuntimeRoot();

		String defaultFile = rootInstallDir + File.separator
				+ IWorkbenchProfile.DEFAULT_PROFILE_FILE;
		return defaultFile;
	}

	public synchronized void reloadActiveProfile() {

		// Get a reader on the default profile file
		String defaultFile = getActiveProfileFilename();

		File dFile = new File(defaultFile);
		if (dFile.exists()) {
			try {
				activeProfile = getWorkbenchProfileFor(defaultFile);
			} catch (TigerstripeException e) {
				// ignore, if default can't be loaded, a simple one is
				// generated.
				// FIXME: will need feedback for the GUI!
			}
		} else {
			activeProfile = makeWorkbenchProfile();
		}

		activeProfileChanged();
	}

	/**
	 * Returns the current active profile for this instance
	 */
	public synchronized IWorkbenchProfile getActiveProfile() {
		if (activeProfile == null)
			reloadActiveProfile();
		return activeProfile;
	}

	/**
	 * Returns a handle on the target pathname. If pathname is known, always
	 * return the same unique handle.
	 * 
	 */
	public IWorkbenchProfile getWorkbenchProfileFor(String pathname)
			throws TigerstripeException {

		IWorkbenchProfile result = makeWorkbenchProfile();

		File file = new File(pathname);
		FileWriter writer = null;
		if (!file.exists()) {
			try {
				writer = new FileWriter(pathname);
				writer.write(result.asText());
				writer.close();
			} catch (IOException e) {
				throw new TigerstripeException("Couldn't create profile for '"
						+ pathname + "': " + e.getMessage(), e);
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						// ignore
					}
				}
			}
		}

		try {

			File profFile = new File(pathname);
			long lastModified = profFile.lastModified();

			if (profileCache.containsKey(pathname)) {
				IWorkbenchProfile profile = profileCache.get(pathname);
				if (lastModified == ((WorkbenchProfile) profile).lastModified())
					return profile;
				else {
					profileCache.remove(pathname);
				}
			}

			FileReader reader = new FileReader(pathname);
			result.parse(reader);
			((WorkbenchProfile) result).setLastModified(lastModified);
			profileCache.put(pathname, result);
			return result;
		} catch (FileNotFoundException e) {
			throw new TigerstripeException(e.getMessage(), e);
		}
	}

	public IWorkbenchProfile rollbackActiveProfile()
			throws TigerstripeException {
		if (!canRollback())
			throw new TigerstripeException(
					"No rollback file was found. Can't rollback");

		String name = getActiveProfileFilename() + ".bak";
		IWorkbenchProfile profile = getWorkbenchProfileFor(name);

		saveAsActiveProfile(profile, false);

		File rFile = new File(name);
		rFile.delete();

		return profile;
	}

	public boolean canRollback() {
		String name = getActiveProfileFilename() + ".bak";
		File rFile = new File(name);
		return rFile.exists();
	}

	/**
	 * Resetting to the default profile, simply means getting rid of any profile
	 * file. Upon reload a brandnew one will be created as if it was the first
	 * run of Workbench.
	 * 
	 */
	public boolean setDefaultActiveProfile() throws TigerstripeException {
		String fName = getActiveProfileFilename();

		File fFile = new File(fName);
		boolean rollbackCreated = false;

		if (fFile.exists()) {

			File rollbackFile = new File(fFile.getAbsolutePath() + ".bak");
			try {
				FileUtils.copy(fFile.getAbsolutePath(), rollbackFile
						.getAbsolutePath(), true);
				rollbackCreated = rollbackFile.exists();
			} catch (IOException e) {
				throw new TigerstripeException(
						"Error while creating rollback file:" + e.getMessage(),
						e);
			}
			boolean deleted = fFile.delete();
		}
		return rollbackCreated;
	}

	private void activeProfileChanged() {
		synchronized (activeProfileListeners) {
			for (IActiveWorkbenchProfileChangeListener listener : activeProfileListeners) {
				listener.profileChanged(getActiveProfile());
			}
		}
	}

	public void addActiveProfileListener(
			IActiveWorkbenchProfileChangeListener listener) {
		synchronized (activeProfileListeners) {
			activeProfileListeners.add(listener);
		}
	}

	public void removeActiveProfileListener(
			IActiveWorkbenchProfileChangeListener listener) {
		synchronized (activeProfileListeners) {
			if (activeProfileListeners.contains(listener)) {
				activeProfileListeners.remove(listener);
			}
		}
	}
}
