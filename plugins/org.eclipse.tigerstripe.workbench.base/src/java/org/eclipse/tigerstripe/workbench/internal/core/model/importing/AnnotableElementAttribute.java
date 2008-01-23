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
package org.eclipse.tigerstripe.workbench.internal.core.model.importing;

public interface AnnotableElementAttribute extends Annotable {

	/**
	 * Returns the type for this attribute
	 * 
	 * @return
	 */
	public AnnotableDatatype getType();

	public void setType(AnnotableDatatype datatype);

	/**
	 * Returns the visibility for this attribute.
	 * 
	 * @return
	 * @see AnnotableElementAttribute.PUBLIC
	 * @see AnnotableElementAttribute.PROTECTED
	 * @see AnnotableElementAttribute.PRIVATE
	 */
	public int getVisibility();

	public void setVisibility(int visibility);

	/**
	 * Returns the dimensions for this attribute 0 if single dimension 1 if
	 * array ([])
	 * 
	 * @return
	 */
	public int getDimensions();

	public void setDimensions(int dimensions);

}
