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

import org.eclipse.tigerstripe.api.external.model.artifacts.IextAssociationEnd;
import org.eclipse.tigerstripe.api.utils.TigerstripeError;

public interface IAssociationEnd extends IModelComponent, IextAssociationEnd {

	public void setNavigable(boolean isNavigable);

	public void setAggregation(EAggregationEnum aggregation);

	public void setChangeable(EChangeableEnum changeable);

	public void setMultiplicity(EMultiplicity multiplicity);

	public void setOrdered(boolean isOrdered);

	public void setType(IType type);

	public void setUnique(boolean isUnique);

	public IType makeIType();

	public List<TigerstripeError> validate();

}
