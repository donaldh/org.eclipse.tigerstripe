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
package org.eclipse.tigerstripe.workbench.model.deprecated_.ossj;


public interface IEventDescriptorEntry  {

	public void setLabel(String label);

	public void setPrimitiveType(String type);

	/**
	 * Returns the label for the Entry.
	 * 
	 * @return String - the Label of the Entry
	 */
	public String getLabel();

	/**
	 * Returns a String of the name of the primitive type of the Entry.
	 * 
	 * 
	 * @return String - the name of teh primitive type
	 */
	public String getPrimitiveType();

}
