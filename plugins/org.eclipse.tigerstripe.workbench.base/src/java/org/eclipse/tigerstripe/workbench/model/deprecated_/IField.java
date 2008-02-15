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
package org.eclipse.tigerstripe.workbench.model.deprecated_;

import java.util.ArrayList;
import java.util.List;


/**
 * This class represents a Field for an IArtifact.
 * 
 * An IField is the model representation of an attribute of a Tigerstripe
 * Artifact.
 * 
 * 
 */
public interface IField extends IModelComponent {

	public final static List<IField> EMPTY_LIST = new ArrayList<IField>();
	
	/**
	 * Static integer value for non-applicable reference type.
	 */
	public static final int NON_APPLICABLE = -1;
	/**
	 * Static integer value for Ref By Key type.
	 */
	public static final int REFBY_KEY = 1;
	/**
	 * Static integer value for Ref By Key Result type.
	 */
	public static final int REFBY_KEYRESULT = 2;
	/**
	 * Static integer value for Ref By Value type.
	 */
	public static final int REFBY_VALUE = 0;
	/**
	 * String values corresponding to the refBy types
	 */
	public static String[] refByLabels = { "value", "key", "keyResult" };

	/**
	 * Sets the reference type of the field.
	 * Possible values are defined in the static fields of this class.
	 * 
	 */
	public void setRefBy(int refBy);

	
	public void setType(IType type);

	/**
	 * Sets the default value for this field if it exists, null otherwise.
	 * 
	 * This is not checked for its ability to be parsed into a valid instance of the Type.
	 * 
	 */
	public void setDefaultValue(String value);

	public IType makeType();
	
	/**
	 * Returns the type of this field.
	 * 
	 * @return IType - the type of this field
	 */
	public IType getType();

	public String getLabelString();

	public void setOptional(boolean optional);

	public void setReadOnly(boolean readonly);

	public void setOrdered(boolean isOrdered);

	public void setUnique(boolean isUnique);

	/**
	 * Clones this Field.
	 * 
	 * @return
	 */
	public IField clone();

	/**
	 * Returns the IArtifact that is the "container" for the Field.
	 * 
	 * @return the containing artifact.
	 */
	public IAbstractArtifact getContainingArtifact();

	/**
	 * Returns the default value for this field if it exists, null otherwise.
	 * 
	 * @return a simple String - this is not checked for its ability to be parsed into a valid instance of the Type. 
	 */
	public String getDefaultValue();


	/**
	 * Returns an integer value indicating the reference type of the field.
	 * Possible values are defined in the static fields of this class.
	 * 
	 * @return int - the integer value corresponding to the refBy
	 */
	public int getRefBy();

	/**
	 * Returns an String value indicating the reference type of the field.
	 * Possible values are defined in the refByLabels field of this class.
	 * 
	 * @return String - the refBy type
	 */
	public String getRefByString();

	/**
	 * Returns a boolean indicating if this field is optional or mandatory.
	 * 
	 * @return boolean - true if optional
	 */
	public boolean isOptional();

	/**
	 * Returns a boolean indicating if this field is ordered (multiplicity > 1).
	 * 
	 * @return boolean - true if field is ordered.
	 */
	public boolean isOrdered();

	/**
	 * Returns a boolean indicating if this field is read-only.
	 * 
	 * @return boolean - true if read-only
	 */
	public boolean isReadOnly();

	/**
	 * Returns a boolean indicating if this field contains unique values
	 * (multiplicity > 1).
	 * 
	 * @return boolean - true if field contains unique values (multiplicity >
	 *         1).
	 */
	public boolean isUnique();
}
