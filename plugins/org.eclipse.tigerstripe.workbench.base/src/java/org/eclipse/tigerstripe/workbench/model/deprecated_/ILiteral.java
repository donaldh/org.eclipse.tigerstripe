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


public interface ILiteral extends IModelComponent, IMember {

	/**
	 * An empty list this is used as a return for Artifact types that do not support Literals.
	 */
	public final static List<ILiteral> EMPTY_LIST = new ArrayList<ILiteral>();

	/**
	 * Returns the IArtifact that is the "container" for the Literal.
	 * 
	 * @return the containing artifact.
	 */
	public IAbstractArtifact getContainingArtifact();

	/**
	 * Returns a String that describes the Literak.
	 * 
	 * This is the presentation used in the Explorer view.
	 * 
	 * The format is : 
	 * 		name = value
	 * 
	 * @return formatted string
	 */
	public String getLabelString();

	/**
	 * Returns the type of this literal.
	 * 
	 * @return IType - the type of this literal
	 */
	public IType getType();

	/** 
	 * Sets the type of the literal. 
	 * 
	 * The type must be one of String or int.
	 * 
	 * @param type
	 */
	public void setType(IType type);

	/**
	 * Make a blank type object.
	 * 
	 * @return - a new IType.
	 */
	public IType makeType();

	/**
	 * Returns the value of the literal. The return will be a String irrespetive
	 * of the "type" of the literal.
	 * 
	 * @return String - the value of the literal
	 */
	public String getValue();

	/**
	 * Sets the value of the literal.
	 * 
	 * 
	 * @param value
	 */
	public void setValue(String value);

	/**
	 * Clone the literal.
	 * 
	 * @return
	 */
	public ILiteral clone();
}
