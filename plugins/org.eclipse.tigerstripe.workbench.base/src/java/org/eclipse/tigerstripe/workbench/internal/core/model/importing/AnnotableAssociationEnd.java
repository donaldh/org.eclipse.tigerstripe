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

import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd.EAggregationEnum;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd.EChangeableEnum;

public interface AnnotableAssociationEnd extends Annotable {

	public void setNavigable(boolean isNavigable);

	public boolean isNavigable();

	public AnnotableDatatype getType();

	public void setType(AnnotableDatatype type);

	public void setAggregation(EAggregationEnum aggregationEnum);

	public EAggregationEnum getAggregation();

	public void setChangeable(EChangeableEnum changeableEnum);

	public EChangeableEnum getChangeable();

	public void setEndMultiplicity(IModelComponent.EMultiplicity multiplicity);

	public IModelComponent.EMultiplicity getEndMultiplicity();

	public void setOrdered(boolean isOrdered);

	public boolean isOrdered();

	public void setVisibility(int visibility);

	public int getVisibility();
}
