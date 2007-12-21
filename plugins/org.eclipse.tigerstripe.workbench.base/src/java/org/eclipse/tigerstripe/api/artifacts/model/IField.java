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
package org.eclipse.tigerstripe.api.artifacts.model;

import java.util.List;

import org.eclipse.tigerstripe.api.external.model.IextField;
import org.eclipse.tigerstripe.api.utils.TigerstripeError;

/**
 * A Field for an AbstractArtifact
 * 
 * @author Eric Dillon
 */
public interface IField extends IextField, IModelComponent {

	public void setRefBy(int refBy);

	public void setIType(IType type);

	public void setDefaultValue(String value);

	public IType makeIType();

	public IType getIType();

	public String getLabelString();

	public void setOptional(boolean optional);

	public void setReadOnly(boolean readonly);

	public void setOrdered(boolean isOrdered);

	public void setUnique(boolean isUnique);

	public List<TigerstripeError> validate();

	/**
	 * Clones this Field.
	 * 
	 * @return
	 */
	public IField clone();
}
