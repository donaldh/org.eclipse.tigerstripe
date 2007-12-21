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
package org.eclipse.tigerstripe.api.external.model.artifacts.ossjSpecifics;

import org.eclipse.tigerstripe.api.artifacts.model.ossj.IEventDescriptorEntry;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextEventDescriptorEntry;

/**
 * OSSJ Standard specific details for Notifications Artifacts
 * 
 * @author Eric Dillon
 * 
 */
public interface IextOssjEventSpecifics extends IextOssjArtifactSpecifics {

	/**
	 * Returns an array of IextEventDescriptorEntry that are those specified in
	 * the tree view of the editor - ie based on the attributes of the event.
	 * 
	 * @return
	 */
	public IextEventDescriptorEntry[] getEventDescriptorEntries();

	/**
	 * Returns an array of IextEventDescriptorEntry that are those specified
	 * directly by the user.
	 * 
	 * @return
	 */
	public IEventDescriptorEntry[] getCustomEventDescriptorEntries();

	/**
	 * Whether this datatype can only be extended with a single stream of
	 * datatypes.
	 * 
	 * @return
	 */
	public boolean isSingleExtensionType();

}
