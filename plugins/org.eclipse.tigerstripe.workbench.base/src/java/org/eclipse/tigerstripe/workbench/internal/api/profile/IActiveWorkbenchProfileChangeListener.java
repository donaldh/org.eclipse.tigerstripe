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
package org.eclipse.tigerstripe.workbench.internal.api.profile;

import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;

/**
 * Listener interface for any object interested in changes in the active profile
 * 
 * 
 * @author Eric Dillon
 * 
 */
public interface IActiveWorkbenchProfileChangeListener {

	/**
	 * This method is called each time the active profile is reloaded
	 * 
	 */
	public void profileChanged(IWorkbenchProfile newActiveProfile);
}
