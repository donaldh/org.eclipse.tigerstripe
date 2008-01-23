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
package org.eclipse.tigerstripe.eclipse.wizards.imports.xmi;

import java.util.Properties;

import org.eclipse.tigerstripe.api.model.IAssociationEnd.EAggregationEnum;
import org.eclipse.tigerstripe.api.model.IAssociationEnd.EChangeableEnum;
import org.eclipse.tigerstripe.api.model.IAssociationEnd.EMultiplicity;
import org.eclipse.tigerstripe.core.model.importing.AnnotableAssociationEnd;

public class AnnotatedAssociation extends AnnotatedElement {

	private String aEndType;
	private String zEndType;

	private String aEndName;
	private String zEndName;

	private EMultiplicity aEndMultiplicity = EMultiplicity.ONE;
	private EMultiplicity zEndMultiplicity = EMultiplicity.ONE;

	private EChangeableEnum aEndChangeable = EChangeableEnum.NONE;
	private EChangeableEnum zEndChangeable = EChangeableEnum.NONE;

	private EAggregationEnum aEndAggregation = EAggregationEnum.NONE;
	private EAggregationEnum zEndAggregation = EAggregationEnum.NONE;

	private boolean aEndIsNavigable;
	private boolean zEndIsNavigable;

	private boolean aEndisOrdered;
	private boolean zEndisOrdered;

	public AnnotatedAssociation(String packageName, String name) {
		super(packageName, name);
	}

	public void setAEndDetails(AnnotableAssociationEnd aEnd) {
		setAEndType(aEnd.getType().getFullyQualifiedName());
		setAEndName(aEnd.getName());
		setAEndAggregation(aEnd.getAggregation());
		setAEndMultiplicity(aEnd.getEndMultiplicity());
		setAEndChangeable(aEnd.getChangeable());
		setAEndIsNavigable(aEnd.isNavigable());
		setAEndisOrdered(aEnd.isOrdered());
	}

	public void setZEndDetails(AnnotableAssociationEnd zEnd) {
		setZEndType(zEnd.getType().getFullyQualifiedName());
		setZEndName(zEnd.getName());
		setZEndAggregation(zEnd.getAggregation());
		setZEndMultiplicity(zEnd.getEndMultiplicity());
		setZEndChangeable(zEnd.getChangeable());
		setZEndIsNavigable(zEnd.isNavigable());
		setZEndisOrdered(zEnd.isOrdered());
	}

	public String getAEndName() {
		return aEndName;
	}

	public void setAEndName(String endName) {
		aEndName = endName;
	}

	public String getAEndType() {
		return aEndType;
	}

	public void setAEndType(String endType) {
		aEndType = endType;
	}

	public String getZEndName() {
		return zEndName;
	}

	public void setZEndName(String endName) {
		zEndName = endName;
	}

	public String getZEndType() {
		return zEndType;
	}

	public void setZEndType(String endType) {
		zEndType = endType;
	}

	public EAggregationEnum getAEndAggregation() {
		return aEndAggregation;
	}

	public void setAEndAggregation(EAggregationEnum endAggregation) {
		aEndAggregation = endAggregation;
	}

	public EChangeableEnum getAEndChangeable() {
		return aEndChangeable;
	}

	public void setAEndChangeable(EChangeableEnum endChangeable) {
		aEndChangeable = endChangeable;
	}

	public EMultiplicity getAEndMultiplicity() {
		return aEndMultiplicity;
	}

	public void setAEndMultiplicity(EMultiplicity endMultiplicity) {
		aEndMultiplicity = endMultiplicity;
	}

	public EAggregationEnum getZEndAggregation() {
		return zEndAggregation;
	}

	public void setZEndAggregation(EAggregationEnum endAggregation) {
		zEndAggregation = endAggregation;
	}

	public EChangeableEnum getZEndChangeable() {
		return zEndChangeable;
	}

	public void setZEndChangeable(EChangeableEnum endChangeable) {
		zEndChangeable = endChangeable;
	}

	public EMultiplicity getZEndMultiplicity() {
		return zEndMultiplicity;
	}

	public void setZEndMultiplicity(EMultiplicity endMultiplicity) {
		zEndMultiplicity = endMultiplicity;
	}

	public boolean isAEndIsNavigable() {
		return aEndIsNavigable;
	}

	public void setAEndIsNavigable(boolean endIsNavigable) {
		aEndIsNavigable = endIsNavigable;
	}

	public boolean isAEndisOrdered() {
		return aEndisOrdered;
	}

	public void setAEndisOrdered(boolean endisOrdered) {
		aEndisOrdered = endisOrdered;
	}

	public boolean isZEndIsNavigable() {
		return zEndIsNavigable;
	}

	public void setZEndIsNavigable(boolean endIsNavigable) {
		zEndIsNavigable = endIsNavigable;
	}

	public boolean isZEndisOrdered() {
		return zEndisOrdered;
	}

	public void setZEndisOrdered(boolean endisOrdered) {
		zEndisOrdered = endisOrdered;
	}

	@Override
	public Properties mergeProperties(Properties prop) {
		Properties result = super.mergeProperties(prop);

		result.put("aEndType", getAEndType());
		result.put("aEndName", getAEndName());
		result.put("aEndIsNavigable", isAEndIsNavigable());
		result.put("aEndIsOrdered", isAEndisOrdered());
		result.put("aEndAggregation", getAEndAggregation().getLabel());
		result.put("aEndChangeable", getAEndChangeable().getLabel());
		result.put("aEndMultiplicity", getAEndMultiplicity().getLabel());

		result.put("zEndType", getZEndType());
		result.put("zEndName", getZEndName());
		result.put("zEndIsNavigable", isZEndIsNavigable());
		result.put("zEndIsOrdered", isZEndisOrdered());
		result.put("zEndAggregation", getZEndAggregation().getLabel());
		result.put("zEndChangeable", getZEndChangeable().getLabel());
		result.put("zEndMultiplicity", getZEndMultiplicity().getLabel());

		return result;
	}
}
