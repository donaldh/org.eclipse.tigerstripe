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

import org.eclipse.tigerstripe.api.artifacts.model.IAssociationEnd;
import org.eclipse.tigerstripe.api.artifacts.model.IType;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.model.IextType;
import org.eclipse.tigerstripe.api.external.model.artifacts.IArtifact;
import org.eclipse.tigerstripe.api.external.model.artifacts.IRelationship;
import org.eclipse.tigerstripe.api.external.model.artifacts.IRelationship.IRelationshipEnd;
import org.eclipse.tigerstripe.api.utils.TigerstripeError;
import org.eclipse.tigerstripe.api.utils.TigerstripeErrorLevel;
import org.eclipse.tigerstripe.core.util.TigerstripeValidationUtils;

import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaField;

public class AssociationEnd extends ArtifactComponent implements
		IAssociationEnd {

	public final static String AEND_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.ASSOCIATION + "-aEnd";

	public final static String ZEND_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.ASSOCIATION + "-zEnd";

	private AssociationArtifact containingAssociation;

	private Type type;

	private EChangeableEnum changeable = EChangeableEnum.NONE;

	private EAggregationEnum aggregation = EAggregationEnum.NONE;

	private EMultiplicity multiplicity;

	private boolean isNavigable;

	private boolean isOrdered;

	private boolean isUnique;

	public AssociationEnd(ArtifactManager artifactMgr) {
		super(artifactMgr);
	}

	public AssociationEnd(JavaField field, ArtifactManager artifactMgr) {
		this(artifactMgr);
		buildModel(field);
	}

	public void setAggregation(EAggregationEnum aggregation) {
		this.aggregation = aggregation;
	}

	public void setChangeable(EChangeableEnum changeable) {
		this.changeable = changeable;
	}

	public void setMultiplicity(EMultiplicity multiplicity) {
		this.multiplicity = multiplicity;
	}

	public void setNavigable(boolean isNavigable) {
		this.isNavigable = isNavigable;
	}

	public void setOrdered(boolean isOrdered) {
		this.isOrdered = isOrdered;
	}

	public void setUnique(boolean isUnique) {
		this.isUnique = isUnique;
	}

	public boolean isUnique() {
		return isUnique;
	}

	public void setType(IType type) {
		this.type = (Type) type;
	}

	public EAggregationEnum getAggregation() {
		return aggregation;
	}

	public EChangeableEnum getChangeable() {
		return changeable;
	}

	public EMultiplicity getMultiplicity() {
		return multiplicity;
	}

	public boolean isNavigable() {
		return isNavigable;
	}

	public boolean isOrdered() {
		return isOrdered;
	}

	private void buildModel(JavaField field) {
		// Get the comment first
		setComment(field.getComment());
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
		Properties props = null;
		Tag tag = getFirstTagByName(AEND_TAG);
		if (tag != null) {
			// Extracting a aEnd
			props = tag.getProperties();
		} else {
			tag = getFirstTagByName(ZEND_TAG);
			props = tag.getProperties();
		}

		isOrdered = "true".equalsIgnoreCase(props.getProperty("isOrdered",
				"false"));
		isNavigable = "true".equalsIgnoreCase(props.getProperty("isNavigable",
				"false"));
		isUnique = "true".equalsIgnoreCase(props.getProperty("isUnique",
				"false"));
		aggregation = EAggregationEnum.parse(props.getProperty("aggregation",
				EAggregationEnum.NONE.getLabel()));
		changeable = EChangeableEnum.parse(props.getProperty("changeable",
				EChangeableEnum.NONE.getLabel()));
		multiplicity = EMultiplicity.parse(props.getProperty("multiplicity",
				EMultiplicity.ONE.getLabel()));

		// Extract all the stereotypes
		extractStereotypes();
	}

	public Type getType() {
		return this.type;
	}

	public IextType getIextType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setContainingAssociation(AssociationArtifact artifact) {
		this.containingAssociation = artifact;
	}

	public AssociationArtifact getContainingAssociation() {
		return this.containingAssociation;
	}

	public IArtifact getContainingArtifact() {
		return this.containingAssociation;
	}

	public IType makeIType() {
		return new Type(getArtifactManager());
	}

	public IRelationship getContainingRelationship() {
		return getContainingAssociation();
	}

	public IRelationshipEnd getOtherEnd() {
		if (this == getContainingRelationship().getRelationshipAEnd())
			return getContainingRelationship().getRelationshipZEnd();
		else
			return getContainingRelationship().getRelationshipAEnd();
	}

	public List<TigerstripeError> validate() {

		List<TigerstripeError> errors = new ArrayList();

		// check association end's name to ensure that it is a valid fieldname
		// in Java
		if (!TigerstripeValidationUtils.elementNamePattern.matcher(getName())
				.matches()) {
			errors.add(new TigerstripeError(TigerstripeErrorLevel.ERROR, "'"
					+ getName() + "' is not a valid association end name"));
		}
		// check association end's name to ensure it is not a reserved keyword
		else if (TigerstripeValidationUtils.keywordList.contains(getName())) {
			errors
					.add(new TigerstripeError(
							TigerstripeErrorLevel.ERROR,
							"'"
									+ getName()
									+ "' is a reserved keyword and cannot be used an association end name"));
		}

		// check the validity of the type for this association end
		List<TigerstripeError> errorList = getType().validate();
		if (!errorList.isEmpty())
			errors.addAll(errorList);

		// making sure association ends are not scalars
		if (getType() != null
				&& (getType().isPrimitive() || getType()
						.getFullyQualifiedName().equals("String"))) {
			errors.add(new TigerstripeError(TigerstripeErrorLevel.ERROR,
					"Association End cannot be a primitive type."));
		}

		return errors;

	}

	public String getNameForType(String typeName) {
		if (typeName.equals(getType().getFullyQualifiedName()))
			return this.getName();
		return "";
	}

	// Association ends are always in facet as long as the association they
	// belong to is in
	@Override
	public boolean isInActiveFacet() throws TigerstripeException {
		if (getContainingAssociation() != null)
			return getContainingAssociation().isInActiveFacet();
		else
			return true;
	}
}
