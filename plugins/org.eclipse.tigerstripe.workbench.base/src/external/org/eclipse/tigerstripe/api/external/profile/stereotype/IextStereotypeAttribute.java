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
 * Top-level definition for user-defined attributes on a stereotype.
 * 
 * For each stereotype, optional attributes can be defined so that the end-user
 * can further specify the details of that stereotype. For example, string-entry
 * attributes, checkable attributes, list-entry attributes, etc... Attributes
 * can be marked as mandatory or optional, and should have a default value
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IextStereotypeAttribute {

	/**
	 * Static integer value for a String kind of attribute.
	 */
	public final static int STRING_ENTRY_KIND = 0;

	/**
	 * Static integer value for a Checkable kind of attribute. Checkable
	 * attributes can have only "true" or "false" values
	 */
	public final static int CHECKABLE_KIND = 1;

	/**
	 * Static integer value for a Entry List kind of attribute. Entry List
	 * attributes can take one value from a defined list.
	 */
	public final static int ENTRY_LIST_KIND = 2;

	/**
	 * Get the name of the attribute.
	 * 
	 * @return the attribute name
	 */
	public String getName();

	/**
	 * Get the default value for this attribute. This will be a String
	 * irrespective of the kind of attribute.
	 * 
	 * @return
	 */
	public String getDefaultValue();

	/**
	 * The 'kind' of attribute. Possible values are defined in teh static fields
	 * of this class.
	 * 
	 * @return int representing the kind of attribute
	 */
	public int getKind();

	/**
	 * Returns true if this attribute is an array type. Arrays are currently
	 * only supported for String Kind attributes.
	 * 
	 * @return true if this attribute is an array
	 */
	public boolean isArray();

	/**
	 * Returns the human readable description of this attribute.
	 * 
	 * @return description string.
	 */
	public String getDescription();
}
