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
package org.eclipse.tigerstripe.api.external.profile.stereotype;

/**
 * A specific type of Stereotype attribute that has a list of valid values to be
 * selected from.
 * 
 * This type is displayed has a drop-down menu list to the end-user.
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IextEntryListStereotypeAttribute extends
		IextStereotypeAttribute {

	/**
	 * The list of selectable values for this attribute
	 * 
	 * @return
	 */
	public String[] getSelectableValues();
}
