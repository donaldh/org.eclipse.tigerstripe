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

import org.eclipse.core.runtime.IStatus;

/**
 * A Type in a Tigerstripe Model
 * 
 * @author Eric Dillon
 */
public interface IType {

	/**
	 * Static integer value for multiple multiplicity - ie "*"
	 * 
	 * @deprecated no use of Multiplicity any more, please use Type Multiplicity
	 */
	@Deprecated
	public final static int MULTIPLICITY_MULTI = 1;
	/**
	 * Static integer value for single multiplicity - ie "0..1"
	 * 
	 * @deprecated no use of Multiplicity any more, please use Type Multiplicity
	 */
	@Deprecated
	public final static int MULTIPLICITY_SINGLE = 0;

	/**
	 * Returns the fully qualified name (ie. package + name) of this type.
	 * 
	 * @return String - the FQN of the type.
	 */
	public String getFullyQualifiedName();

	/**
	 * Sets the fully qualified name (ie. package + name) of this type.
	 * 
	 * The string should be '.' separated.
	 * 
	 * @param fqn
	 */
	public void setFullyQualifiedName(String fqn);

	/**
	 * Returns the multiplicity for this type.
	 * 
	 * @return
	 * @since 2.2-rc
	 */
	public IModelComponent.EMultiplicity getTypeMultiplicity();

	/** 
	 * Sets the multiplicity for this type.
	 * 
	 * @param multiplicity
	 */
	public void setTypeMultiplicity(IModelComponent.EMultiplicity multiplicity);

	public IStatus validate();

	public IStatus validate(boolean isMethodReturnCheck);

	/**
	 * Clone this type.
	 * @return
	 */
	public IType clone();

	/**
	 * Returns a simple default value. For a type with FQN of "java.lang.String"
	 * this will return "Value". For a type with FQN of "int" this will return
	 * "0".
	 * 
	 * @return String representation of a default Value
	 */
	public String defaultValue();

	/**
	 * Get the Tigerstripe artifact (of any type).
	 * 
	 * @return the Artifact if the FQN of this type matches that of a artifact
	 *         in the same project(including dependencies & refereneces), null
	 *         otherwise.
	 */
	public IAbstractArtifact getArtifact();

	/**
	 * Returns the name of this type.
	 * 
	 * @return String - the name of the type.
	 */
	public String getName();

	/**
	 * Returns the package name for this type.
	 * 
	 * @return String - the package where this artifact was defined.
	 */
	public String getPackage();

	/**
	 * Test to see of the type represented here is a Tigerstripe artifact (of
	 * any type).
	 * 
	 * @return true if the FQN of this type matches that of a artifact in the
	 *         same project(including dependencies & refereneces).
	 */
	public boolean isArtifact();

	/**
	 * Test to see of the type represented here is a Tigerstripe Datatype
	 * artifact.
	 * 
	 * @return true if the FQN of this type matches that of a Datatype artifact
	 *         in the same project(including dependencies & refereneces).
	 */
	public boolean isDatatype();

	/**
	 * Test to see of the type represented here is a Tigerstripe ManagedEntity
	 * artifact.
	 * 
	 * @return true if the FQN of this type matches that of a ManagedEntity
	 *         artifact in the same project(including dependencies &
	 *         refereneces).
	 */
	public boolean isEntityType();

	/**
	 * Test to see of the type represented here is a Tigerstripe Enumeration
	 * artifact.
	 * 
	 * @return true if the FQN of this type matches that of a Enumeration
	 *         artifact in the same project(including dependencies &
	 *         refereneces).
	 */
	public boolean isEnum();

	/**
	 * Test to see of the type represented here is a "primitive" type.
	 * 
	 * Note that the primitive test here is *not* for primitive types defined in
	 * a profile, but for java primitive types:
	 * boolean,int,char,float,double,long,short,byte
	 * 
	 * @return true if the FQN of the type patches any of those in this list.
	 */
	public boolean isPrimitive();

	/**
	 * Returns an integer value indicating the multiplicity of this type.
	 * Possible values are defined in the static fields of this class.
	 * 
	 * @return int - the integer value corresponding to the multiplicity.
	 * @deprecated since 2.2-rc, no use of multiplicity anymore,
	 *             TypeMultiplicity instead
	 */
	@Deprecated
	public int getMultiplicity();

	/**
	 * 
	 * @param multiplicity
	 * @deprecated since 2.2-rc, no use of multiplicity anymore,
	 *             TypeMultiplicity instead
	 */
	@Deprecated
	public void setMultiplicity(int multiplicity);
}
