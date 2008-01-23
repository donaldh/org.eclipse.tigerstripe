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
package org.eclipse.tigerstripe.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.tigerstripe.api.model.ILabel;
import org.eclipse.tigerstripe.api.model.IType;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.api.utils.TigerstripeError;
import org.eclipse.tigerstripe.api.utils.TigerstripeErrorLevel;
import org.eclipse.tigerstripe.core.util.TigerstripeValidationUtils;

import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaField;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Label extends ArtifactComponent implements ILabel {

	private final static String LABEL_TAG = "tigerstripe.label";

	private AbstractArtifact containingArtifact;

	private Type type;

	/**
	 * The value of the Label
	 */
	private String value;

	public Label(ArtifactManager artifactMgr) {
		super(artifactMgr);
	}

	public Label(JavaField field, ArtifactManager artifactMgr) {
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
				getArtifactManager());
		setName(field.getName());

		// Extract value of Label
		Tag tag = getFirstTagByName(LABEL_TAG);
		if (tag != null) {
			Properties props = tag.getProperties();
			this.value = props.getProperty("value", field.getName()
					.toUpperCase());
		}

		// Extract all the stereotypes
		extractStereotypes();
	}

	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setContainingArtifact(AbstractArtifact artifact) {
		this.containingArtifact = artifact;
	}

	public AbstractArtifact getContainingArtifact() {
		return this.containingArtifact;
	}

	// ===============================================================
	// To satisfy the ILabel interface

	public String getLabelString() {
		return getName() + "=" + getValue();
	}

	public void setIType(IType type) {
		setType((Type) type);
	}

	public IType makeIType() {
		return new Type(getArtifactManager());
	}

	public IType getIType() {
		return getType();
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
	 * used to validate the Label when saving it to the underlying data model
	 * 
	 * @see org.eclipse.tigerstripe.api.artifacts.model.ILabel#validate()
	 */
	public List<TigerstripeError> validate() {

		List<TigerstripeError> errors = new ArrayList();

		// check label name to ensure that it is a valid fieldname in Java
		if (!TigerstripeValidationUtils.literalNamePattern.matcher(getName())
				.matches()) {
			errors.add(new TigerstripeError(TigerstripeErrorLevel.ERROR, "'"
					+ getName() + "' is not a valid label name"));
		}
		// check label name to ensure it is not a reserved keyword
		else if (TigerstripeValidationUtils.keywordList.contains(getName())) {
			errors
					.add(new TigerstripeError(
							TigerstripeErrorLevel.ERROR,
							"'"
									+ getName()
									+ "' is a reserved keyword and cannot be used as label name"));
		}

		// check the validity of the type for this label
		List<TigerstripeError> errorList = getIType().validate();
		if (!errorList.isEmpty())
			errors.addAll(errorList);

		return errors;

	}

	@Override
	public ILabel clone() {
		ILabel result = new Label(getArtifactManager());

		result.setComment(getComment());
		result.setIType(getIType().clone());
		result.setName(getName());
		result.setValue(getValue());
		result.setVisibility(getVisibility());
		IStereotypeInstance[] stereotypeInstances = getStereotypeInstances();
		for (IStereotypeInstance inst : stereotypeInstances) {
			result.addStereotypeInstance(inst);
		}

		return result;
	}
}
