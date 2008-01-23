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
package org.eclipse.tigerstripe.workbench.internal.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.tigerstripe.workbench.internal.api.utils.TigerstripeError;
import org.eclipse.tigerstripe.workbench.internal.api.utils.TigerstripeErrorLevel;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeValidationUtils;
import org.eclipse.tigerstripe.workbench.model.IField;
import org.eclipse.tigerstripe.workbench.model.IType;
import org.eclipse.tigerstripe.workbench.model.IAssociationEnd.EMultiplicity;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;

import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaField;

/**
 * A model for a field on an AbstractArtifact
 * 
 * @author Eric Dillon
 */
public class Field extends ArtifactComponent implements IField {

	private final static String FIELD_TAG = "tigerstripe.field";

	public static int refByFromLabel(String label) {
		int result = IField.NON_APPLICABLE;
		for (int i = 0; i < refByLabels.length; i++) {
			if (refByLabels[i].equalsIgnoreCase(label)) {
				result = i;
			}
		}

		return result;
	}

	private AbstractArtifact containingArtifact;

	private String defaultValue;

	private Type type;

	private String refBy;

	private boolean optional;

	private boolean readOnly;

	private boolean isUnique;

	private boolean isOrdered;

	public boolean isUnique() {
		return this.isUnique;
	}

	public void setUnique(boolean isUnique) {
		this.isUnique = isUnique;
	}

	public boolean isOrdered() {
		return this.isOrdered;
	}

	public void setOrdered(boolean isOrdered) {
		this.isOrdered = isOrdered;
	}

	public String getDefaultValue() {
		return this.defaultValue;
	}

	public void setDefaultValue(String value) {
		this.defaultValue = value;
	}

	public boolean hasDefaultValue() {
		return defaultValue != null;
	}

	public boolean isOptional() {
		return this.optional;
	}

	public boolean isReadOnly() {
		return this.readOnly;
	}

	public Field(ArtifactManager artifactMgr) {
		super(artifactMgr);
	}

	public Field(JavaField field, ArtifactManager artifactMgr) {
		this(artifactMgr);
		buildModel(field);
	}

	private void buildModel(JavaField field) {
		// Get the comment first
		setComment(xmlEncode.decode(field.getComment()));
		setVisibility(field.getModifiers());

		// extracts the tags
		DocletTag[] tags = field.getTags();
		for (int i = 0; i < tags.length; i++) {
			Tag tag = new Tag(tags[i]);
			addTag(tag);
		}

		com.thoughtworks.qdox.model.Type type = field.getType();
		this.type = new Type(type.getValue(), type.getDimensions(),
				getArtifactManager()); // using old fashion constructor
		setName(field.getName());

		Tag tag = getFirstTagByName(FIELD_TAG);
		if (tag != null) {
			Properties prop = tag.getProperties();
			this.refBy = (prop.getProperty("ref-by", "value")).toLowerCase();

			this.optional = ("true".equals(prop.getProperty("isOptional",
					"false").toLowerCase()));
			this.readOnly = ("true".equals(prop.getProperty("isReadOnly",
					"false").toLowerCase()));
			this.isOrdered = ("true".equals(prop.getProperty("isOrdered",
					"false").toLowerCase()));
			this.isUnique = ("true".equals(prop
					.getProperty("isUnique", "false").toLowerCase()));
			String typeMultiplicity = prop
					.getProperty("typeMultiplicity", null);
			if (typeMultiplicity != null) {
				// setting proper type multiplicity is present (i.e. otherwise
				// model is prior to 2.2rc)
				this.type.setTypeMultiplicity(EMultiplicity
						.parse(typeMultiplicity));
			}

			String defaultValue = prop.getProperty("defaultValue", null);
			if (defaultValue != null) {
				this.setDefaultValue(xmlEncode.decode(defaultValue));
			}
		}

		// Extract all the stereotypes
		extractStereotypes();

	}

	public Type getType() {
		return this.type;
	}

	public boolean isRefByValue() {
		return "value".equalsIgnoreCase(this.refBy);
	}

	public boolean isRefByKey() {
		return "key".equalsIgnoreCase(this.refBy);
	}

	public boolean isRefByKeyResult() {
		return "keyresult".equalsIgnoreCase(this.refBy);
	}

	public void setContainingArtifact(AbstractArtifact artifact) {
		this.containingArtifact = artifact;
	}

	public AbstractArtifact getContainingArtifact() {
		return this.containingArtifact;
	}

	// ==================================================================
	// Methods to satisfy the IType interface

	// ==================================================================
	// Methods to satisfy the IType interface

	public IType getIType() {
		return this.type;
	}

	public IType makeIType() {
		return new Type(getArtifactManager());
	}

	public void setIType(IType type) {
		this.type = (Type) type;
	}

	public void setReadOnly(boolean readonly) {
		this.readOnly = readonly;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	public void setRefBy(int refBy) {
		this.refBy = refByLabels[refBy];
	}

	public int getRefBy() {
		for (int i = 0; i < refByLabels.length; i++) {
			if (refByLabels[i].equalsIgnoreCase(this.refBy))
				return i;
		}
		return NON_APPLICABLE;
	}

	public String getRefByString() {
		if (getRefBy() == IField.NON_APPLICABLE)
			return "";
		return IField.refByLabels[getRefBy()];
	}

	public Properties getAnnotationProperties(String annotation) {
		// Read the tigerstripe tag
		Tag intfTag = getFirstTagByName(annotation);
		if (intfTag != null) {
			Properties prop = intfTag.getProperties();
			return prop;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * used to validate the Field when saving it to the underlying data model
	 * 
	 * @see org.eclipse.tigerstripe.api.artifacts.model.ILabel#validate()
	 */
	public List<TigerstripeError> validate() {

		List<TigerstripeError> errors = new ArrayList();

		// check field name to ensure that it is a valid fieldname in Java
		if (!TigerstripeValidationUtils.elementNamePattern.matcher(getName())
				.matches()
				&& !TigerstripeValidationUtils.allUppercase.matcher(getName())
						.matches()) {
			errors.add(new TigerstripeError(TigerstripeErrorLevel.ERROR, "'"
					+ getName() + "' is not a valid field name"));
		}
		// check field name to ensure it is not a reserved keyword
		else if (TigerstripeValidationUtils.keywordList.contains(getName())) {
			errors
					.add(new TigerstripeError(
							TigerstripeErrorLevel.ERROR,
							"'"
									+ getName()
									+ "' is a reserved keyword and cannot be used as field name"));
		}

		// check the validity of the type for this field
		List<TigerstripeError> errorList = getIType().validate();
		if (!errorList.isEmpty())
			errors.addAll(errorList);

		return errors;

	}

	public String getLabelString() {
		String result = getName() + "::" + getIType().getName();
		if (getType().getTypeMultiplicity() != EMultiplicity.ONE) {
			result = result + "[" + getType().getTypeMultiplicity().getLabel()
					+ "]";
		}

		if (getDefaultValue() != null) {
			if (getDefaultValue().length() == 0) {
				result = result + "=\"\"";
			} else {
				result = result + "=" + getDefaultValue();
			}
		}
		return result;
	}

	@Override
	public IField clone() {
		if (getContainingArtifact() == null)
			throw new IllegalStateException(
					"Cannot clone field that doesn't belong to any artifact.");

		IField result = getContainingArtifact().makeIField();
		result.setName(getName());
		result.setComment(getComment());
		result.setDefaultValue(getDefaultValue());
		result.setIType(getIType().clone());
		result.setOptional(isOptional());
		result.setOrdered(isOrdered());
		result.setReadOnly(isReadOnly());
		result.setRefBy(getRefBy());
		result.setUnique(isUnique());
		result.setVisibility(getVisibility());

		IStereotypeInstance[] stereotypeInstances = getStereotypeInstances();
		for (IStereotypeInstance inst : stereotypeInstances) {
			result.addStereotypeInstance(inst);
		}
		return result;
	}
}
