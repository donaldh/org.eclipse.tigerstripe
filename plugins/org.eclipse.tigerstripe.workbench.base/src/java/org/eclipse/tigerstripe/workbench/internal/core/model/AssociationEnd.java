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

import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeValidationUtils;
import org.eclipse.tigerstripe.workbench.model.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.IRelationship;
import org.eclipse.tigerstripe.workbench.model.IType;
import org.eclipse.tigerstripe.workbench.model.IRelationship.IRelationshipEnd;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;

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

	private IModelComponent.EMultiplicity multiplicity;

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

	public void setMultiplicity(IModelComponent.EMultiplicity multiplicity) {
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

	public IModelComponent.EMultiplicity getMultiplicity() {
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
		// the *type* at the end should have a fixed multiplicity of one.
		// Note this is different from the multiplicity of the end.
		
		this.type = new Type(type.getValue(), EMultiplicity.ONE,
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
		multiplicity = IModelComponent.EMultiplicity.parse(props.getProperty("multiplicity",
				IModelComponent.EMultiplicity.ONE.getLabel()));


		
		// Extract all the stereotypes
		extractStereotypes();
	}

	public IType getType() {
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

	public IAbstractArtifact getContainingArtifact() {
		return this.containingAssociation;
	}

	public IType makeType() {
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

	public IStatus validate() {

		MultiStatus result = new MultiStatus(BasePlugin.getPluginId(), 222,
				"AssociationEnd Validation", null);

		// check association end's name to ensure that it is a valid fieldname
		// in Java
		if (!TigerstripeValidationUtils.elementNamePattern.matcher(getName())
				.matches()) {
			result.add(new Status(IStatus.ERROR, BasePlugin.getPluginId(), "'"
					+ getName() + "' is not a valid association end name"));
		}
		// check association end's name to ensure it is not a reserved keyword
		else if (TigerstripeValidationUtils.keywordList.contains(getName())) {
			result
					.add(new Status(
							IStatus.ERROR,
							BasePlugin.getPluginId(),
							"'"
									+ getName()
									+ "' is a reserved keyword and cannot be used an association end name"));
		}

		// check the validity of the type for this association end
		IStatus typeStatus = getType().validate();
		if (!typeStatus.isOK())
			result.add(typeStatus);

		// making sure association ends are not scalars
		if (getType() != null
				&& (getType().isPrimitive() || getType()
						.getFullyQualifiedName().equals("String"))) {
			result.add(new Status(IStatus.ERROR, BasePlugin.getPluginId(),
					"Association End cannot be a primitive type."));
		}

		if (result.isOK())
			return Status.OK_STATUS;
		else
			return result;

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
