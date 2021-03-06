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
package org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2.annotables;

import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableAssociationEnd;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableDatatype;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd.EAggregationEnum;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd.EChangeableEnum;

public class UML2AnnotableAssociationEnd extends UML2AnnotableBase implements
		AnnotableAssociationEnd {

	private AnnotableDatatype type;
	private boolean isNavigable;
	private boolean isOrdered;
	private EAggregationEnum aggregation = EAggregationEnum.NONE;
	private IModelComponent.EMultiplicity multiplicity = IModelComponent.EMultiplicity.ONE;
	private EChangeableEnum changeable = EChangeableEnum.NONE;
	private int visibility;

	public UML2AnnotableAssociationEnd(String name) {
		super(name);
	}

	public AnnotableDatatype getType() {
		return type;
	}

	public boolean isNavigable() {
		return this.isNavigable;
	}

	public void setNavigable(boolean isNavigable) {
		this.isNavigable = isNavigable;
	}

	public void setType(AnnotableDatatype type) {
		this.type = type;
	}

	public EAggregationEnum getAggregation() {
		return this.aggregation;
	}

	public EChangeableEnum getChangeable() {
		return this.changeable;
	}

	public IModelComponent.EMultiplicity getEndMultiplicity() {
		return this.multiplicity;
	}

	public boolean isOrdered() {
		return isOrdered;
	}

	public void setAggregation(EAggregationEnum aggregationEnum) {
		this.aggregation = aggregationEnum;
	}

	public void setChangeable(EChangeableEnum changeableEnum) {
		this.changeable = changeableEnum;
	}

	public void setEndMultiplicity(IModelComponent.EMultiplicity multiplicity) {
		if (multiplicity == null) {
			TigerstripeRuntime.logInfoMessage("multiplicity is null");
		}
		this.multiplicity = multiplicity;
	}

	public void setOrdered(boolean isOrdered) {
		this.isOrdered = isOrdered;
	}

	public int getVisibility() {
		return this.visibility;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}

}
