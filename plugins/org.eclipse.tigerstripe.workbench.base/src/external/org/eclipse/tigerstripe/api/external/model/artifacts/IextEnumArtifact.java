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
package org.eclipse.tigerstripe.api.external.model.artifacts;

/**
 * This class represents a Enumeration Artifact.
 * 
 * All Labels in an enumeration should be of the same base type (ie int or
 * String).
 * 
 */

public interface IextEnumArtifact extends IArtifact {
	/**
	 * Static string for int base type.
	 */
	public final static String BASETYPE_INT = "int";
	/**
	 * Static string for String base type.
	 */
	public final static String BASETYPE_STRING = "String";

	/**
	 * Array of supported base types.
	 */
	public static final String[] baseTypeOptions = new String[] { BASETYPE_INT,
			BASETYPE_STRING };

	/**
	 * Return a string of the base type for this enum.
	 * 
	 * @return String matching one of the defined options.
	 */
	public String getBaseTypeStr();

	/**
	 * Return the NAME of the Enum with the lowest value. If a String based Enum
	 * the return is indeterminate.
	 * 
	 * @return Name of the label with lowest integer value.
	 */
	public String getMinLabel();

	/**
	 * Return the NAME of the Enum with the highest value. If a String based
	 * Enum the return is indeterminate.
	 * 
	 * @return Name of the label with highest integer value.
	 */
	public String getMaxLabel();
}
