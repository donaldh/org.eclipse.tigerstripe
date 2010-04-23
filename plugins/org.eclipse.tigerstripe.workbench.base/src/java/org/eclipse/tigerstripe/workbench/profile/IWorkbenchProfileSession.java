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
package org.eclipse.tigerstripe.workbench.profile;

import org.eclipse.tigerstripe.workbench.TigerstripeException;

/**
 * Session facade to access IWorkbenchProfiles, edit and create them
 * 
 * This session is available from the API (@see API)
 * 
 * There is only one active IWorkbenchProfile per runtime. (@see
 * #getActiveProfile())
 * 
 * It is possible for any object to register interest in changes of the active
 * profile by implementing the {@link IActiveWorkbenchProfileChangeListener}
 * interface
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IWorkbenchProfileSession {

	/**
	 * Returns the active profile for this runtime.
	 * 
	 * @return IWorkbenchProfile - the active IWorkbenchProfile
	 */
	public IWorkbenchProfile getActiveProfile();

	/**
	 * Loads the active profile from the default Workbench profile location for
	 * this install.
	 * 
	 */
	public void reloadActiveProfile();

	/**
	 * Sets the active profile to the factory defaults.
	 * 
	 * If a factory.wbp file is found, it will be used as default. If not a
	 * simple default profile is set
	 * 
	 * @return true if a rollback file was created to enable a rollback
	 */
	public boolean setDefaultActiveProfile() throws TigerstripeException;

	/**
	 * Factory method for IWorkbenchProfiles
	 * 
	 * @return IWorkbenchProfile - returns a new empty IWorkbenchProfile
	 */
	public IWorkbenchProfile makeWorkbenchProfile();

	/**
	 * Get a new instance  of teh "Factory Settings" Profile.
	 * This may have been contributed through an Extension Point. 
	 * 
	 * @return IWorkbenchProfile - 
	 */
	public IWorkbenchProfile makeFactoryWorkbenchProfile();
	
	
	public IWorkbenchProfile getWorkbenchProfileFor(String pathname)
			throws TigerstripeException;

	/**
	 * Sets the given profile as the active profile by saving it as such. A
	 * rollback file is created to rollback to the current active profile.
	 * 
	 * <p>
	 * <b>Note:</b> the profile is NOT reloaded, the action only sets up the
	 * given profile as the active profile. A reloadActiveProfile() is required.
	 * </p>
	 * 
	 * @param profile
	 * @return boolean - true if a rollback file was created.
	 * @throws TigerstripeException
	 */
	public boolean saveAsActiveProfile(IWorkbenchProfile profile)
			throws TigerstripeException;

	/**
	 * Rolls back the active profile to its previous version. If no rollback
	 * file is available, no action is performed.
	 * 
	 * <p>
	 * <b>Note:</b> the profile is NOT reloaded, the rollback action only sets
	 * up the previous profile as the active profile. A reloadActiveProfile() is
	 * required.
	 * </p>
	 * 
	 * @return IWorkbenchProfile - the profile that was rolled back to.
	 */
	public IWorkbenchProfile rollbackActiveProfile()
			throws TigerstripeException;

	/**
	 * Returns true if a rollback file can be found to perform a rollback
	 * 
	 * @return true if it is possible to rollback to the previous active profile
	 */
	public boolean canRollback();

}
