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
/**
 * 
 */
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.model;

public class ArtifactAttributeModel {

	public static String[] refByLabels = { "value", "key", "keyResult" };

	public static final int NON_APPLICABLE = -1;

	public static final int REFBY_VALUE = 0;

	public static final int REFBY_KEY = 1;

	public static final int REFBY_KEYRESULT = 2;

	String name;

	String description;

	String attributeClass;

	int modifier;

	int dimensions;

	int refBy = NON_APPLICABLE;

	boolean optional = false;

	boolean readOnly = false;

	boolean unique = false;

	boolean ordered = false;

	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	public boolean isOptional() {
		return this.optional;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public boolean isUnique() {
		return this.unique;
	}

	public void setOrdered(boolean ordered) {
		this.ordered = ordered;
	}

	public boolean isOrdered() {
		return this.ordered;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean isReadOnly() {
		return this.readOnly;
	}

	public void setRefBy(int refBy) {
		this.refBy = refBy;
	}

	public int getRefBy() {
		return this.refBy;
	}

	@Override
	public Object clone() {
		ArtifactAttributeModel result = new ArtifactAttributeModel();
		result.applyValues(this);
		return result;
	}

	public void applyValues(ArtifactAttributeModel ref) {
		this.name = ref.getName();
		this.attributeClass = ref.getAttributeClass();
		this.dimensions = ref.getDimensions();
		this.modifier = ref.getModifier();
		this.refBy = ref.getRefBy();
		this.optional = ref.isOptional();
		this.readOnly = ref.isReadOnly();
		this.unique = ref.isUnique();
		this.ordered = ref.isOrdered();
	}

	public int getModifier() {
		return modifier;
	}

	public void setModifier(int modifier) {
		this.modifier = modifier;
	}

	public String getAttributeClass() {
		return attributeClass;
	}

	public void setAttributeClass(String attributeClass) {
		this.attributeClass = attributeClass;
	}

	public int getDimensions() {
		return dimensions;
	}

	public void setDimensions(int dimensions) {
		this.dimensions = dimensions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}