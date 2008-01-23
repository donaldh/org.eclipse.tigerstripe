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
package org.eclipse.tigerstripe.api.model.artifacts.ossj;


public interface IOssjEventSpecifics extends IOssjArtifactSpecifics {

	public void setEventDescriptorEntries(IEventDescriptorEntry[] entries);

	public void setCustomEventDescriptorEntries(IEventDescriptorEntry[] entries);

	public void setSingleExtensionType(boolean single);

	/**
	 * Returns an array of IEventDescriptorEntry that are those specified
	 * directly by the user.
	 * 
	 * @return
	 */
	public IEventDescriptorEntry[] getCustomEventDescriptorEntries();

	/**
	 * Returns an array of IEventDescriptorEntry that are those specified in
	 * the tree view of the editor - ie based on the attributes of the event.
	 * 
	 * @return
	 */
	public IEventDescriptorEntry[] getEventDescriptorEntries();

	/**
	 * Whether this datatype can only be extended with a single stream of
	 * datatypes.
	 * 
	 * @return
	 */
	public boolean isSingleExtensionType();

}
