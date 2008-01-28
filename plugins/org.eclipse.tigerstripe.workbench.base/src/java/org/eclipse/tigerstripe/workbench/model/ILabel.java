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
package org.eclipse.tigerstripe.workbench.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;

public interface ILabel extends IModelComponent {

	public final static List<ILabel> EMPTY_LIST = new ArrayList<ILabel>();

	public void setIType(IType type);

	public IType makeIType();

	public void setValue(String value);

	public IType getIType();

	public String getLabelString();

	public ILabel clone();

	/**
	 * Returns the IArtifact that is the "container" for the Label.
	 * 
	 * @return the containing artifact.
	 */
	public IAbstractArtifact getContainingArtifact();

	/**
	 * Returns the value of the label. The return will be a String irrespetive
	 * of the "type" of the Label.
	 * 
	 * @return String - the value of the label
	 */
	public String getValue();
}
