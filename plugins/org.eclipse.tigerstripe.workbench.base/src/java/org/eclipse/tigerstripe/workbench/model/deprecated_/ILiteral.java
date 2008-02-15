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


public interface ILiteral extends IModelComponent {

	public final static List<ILiteral> EMPTY_LIST = new ArrayList<ILiteral>();

	public void setType(IType type);

	public IType makeType();

	public void setValue(String value);

	public IType getType();

	public String getLabelString();

	public ILiteral clone();

	/**
	 * Returns the IArtifact that is the "container" for the Literal.
	 * 
	 * @return the containing artifact.
	 */
	public IAbstractArtifact getContainingArtifact();

	/**
	 * Returns the value of the literal. The return will be a String irrespetive
	 * of the "type" of the literal.
	 * 
	 * @return String - the value of the literal
	 */
	public String getValue();
}
