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
public interface IField extends IModelComponent, IMember {

	/**
	 * An empty list this is used as a return for Artifact types that do not support Fields.
	 */
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
	 * Returns the IArtifact that is the "container" for the Field.
	 * 
	 * @return the containing artifact.
	 */
	public IAbstractArtifact getContainingArtifact();

	/**
	 * Returns a String that describes the Field.
	 * 
	 * This is the presentation used in the Explorer view.
	 * 
	 * The format is : 
	 * 		name::typeName[] = defaultValue
	 * 		name::typeName = defaultValue
	 * 		name::typeName[]
	 * 		name::typeName
	 * 
	 * 
	 * @return formatted string
	 */
	public String getLabelString();

	/**
	 * Returns an String value indicating the reference type of the field.
	 * Possible values are defined in the refByLabels field of this class.
	 * 
	 * @return String - the refBy type
	 */
	public String getRefByString();

	/**
	 * Returns an integer value indicating the reference type of the field.
	 * Possible values are defined in the static fields of this class.
	 * 
	 * @return int - the integer value corresponding to the refBy
	 */
	public int getRefBy();

	/**
	 * Sets the reference type of the field.
	 * Possible values are defined in the static fields of this class.
	 * 
	 */
	public void setRefBy(int refBy);

	/**
	 * Returns the type of this field.
	 * 
	 * @return IType - the type of this field
	 */
	public IType getType();

	/** 
	 * Sets the type of the field. 
	 * 
	 * The type could be a primitive type, or represent a model Artifact.
	 * 
	 * @param type
	 */
	public void setType(IType type);

	/**
	 * Returns the default value for this field if it exists, null otherwise.
	 * 
	 * @return a simple String - this is not checked for its ability to be parsed into a valid instance of the Type. 
	 */
	public String getDefaultValue();

	/**
	 * Sets the default value for this field if it exists, null otherwise.
	 * 
	 * This is not checked for its ability to be parsed into a valid instance of the Type.
	 * 
	 */
	public void setDefaultValue(String value);

	/**
	 * Make a blank type object.
	 * 
	 * @return - a new IType.
	 */
	public IType makeType();
	
	/**
	 * Returns a boolean indicating if this field is optional or mandatory.
	 * 
	 * @return boolean - true if optional
	 */
	public boolean isOptional();

	/**
	 * Sets the optional attribute of the Field.
	 * 
	 * @param optional
	 */
	public void setOptional(boolean optional);

	/**
	 * Returns a boolean indicating if this field is read-only.
	 * 
	 * @return boolean - true if read-only
	 */
	public boolean isReadOnly();

	/**
	 * Sets the readonly attribute of the Field.
	 * 
	 * @param readonly
	 */
	public void setReadOnly(boolean readonly);

	/**
	 * Returns a boolean indicating if this field is ordered (multiplicity > 1).
	 * 
	 * @return boolean - true if field is ordered.
	 */
	public boolean isOrdered();

	/**
	 * Sets the ordered attribute of the Field.
	 * @param isOrdered
	 */
	public void setOrdered(boolean isOrdered);

	/**
	 * Returns a boolean indicating if this field contains unique values
	 * (multiplicity > 1).
	 * 
	 * @return boolean - true if field contains unique values (multiplicity >
	 *         1).
	 */
	public boolean isUnique();

	/**
	 * Sets the unique attribute of the Field.
	 * @param isUnique
	 */
	public void setUnique(boolean isUnique);

	/**
	 * Clones this Field.
	 * 
	 * @return
	 */
	public IField clone();
}
