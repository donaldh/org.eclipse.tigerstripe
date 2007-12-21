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

import org.eclipse.tigerstripe.api.external.model.IextType;
import org.eclipse.tigerstripe.api.external.model.artifacts.IextAssociationEnd.EMultiplicity;
import org.eclipse.tigerstripe.api.utils.TigerstripeError;

/**
 * A Type in a Tigerstripe Model
 * 
 * @author Eric Dillon
 */
public interface IType extends IextType {

	public void setFullyQualifiedName(String fqn);

	/**
	 * 
	 * @param multiplicity
	 * @deprecated since 2.2-rc, no use of multiplicity anymore,
	 *             TypeMultiplicity instead
	 */
	@Deprecated
	public void setMultiplicity(int multiplicity);

	public void setTypeMultiplicity(EMultiplicity multiplicity);

	public List<TigerstripeError> validate();

	public List<TigerstripeError> validate(boolean isMethodReturnCheck);

	public IType clone();
}
