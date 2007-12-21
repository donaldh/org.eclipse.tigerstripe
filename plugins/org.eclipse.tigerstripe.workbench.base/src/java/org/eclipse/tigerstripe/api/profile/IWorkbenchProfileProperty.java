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
package org.eclipse.tigerstripe.api.profile;

import org.eclipse.tigerstripe.api.external.TigerstripeException;

/**
 * Property as it may be defined in a Workbench profile.
 * 
 * The idea is that any property can be stored/retrived from a profile. These
 * properties should only be serializable into a String so they can be stored in
 * the underlying profile file.
 * 
 * In the profile file itself they are stored like follows
 * 
 * <workbenchProperty type="org.eclipse.tigerstripe.xxx.yy.Zzz">ferqyiugerog
 * eigqer</workbenchProperty>
 * 
 * In other words, the persistence layer for Workbench profiles will know what
 * to read/write and will put the actual class name of the property in the file.
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IWorkbenchProfileProperty {

	/**
	 * Return a serialized version of this
	 * 
	 * @return
	 */
	public String serializeToString();

	/**
	 * Populate this based on the serialized string
	 * 
	 * @param serializedString
	 * @throws TigerstripeException,
	 *             if the serialized string cannot be decoded successfully.
	 */
	public void parseFromSerializedString(String serializedString)
			throws TigerstripeException;

	public void setPropertyValue(String name, boolean value);

	public boolean getPropertyValue(String name);
}
