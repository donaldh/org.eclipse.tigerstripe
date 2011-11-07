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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.metamodel.impl.IAssociationArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IPrimitiveTypeImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IOssjLegacySettigsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.OssjLegacySettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.util.QDoxUtils;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeValidationUtils;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship.IRelationshipEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaField;

public class AssociationEnd extends ArtifactComponent implements
		IAssociationEnd {

	private static IAbstractArtifact[] suitableTypes;
	
	public static boolean isSuitableType(IType type){
		if (type.isArtifact()){
			IAbstractArtifact typeArtifact = type.getArtifact();
			if (typeArtifact instanceof IDatatypeArtifact
					|| typeArtifact instanceof IAssociationClassArtifact
					|| typeArtifact instanceof IManagedEntityArtifact
					|| typeArtifact instanceof IExceptionArtifact
					|| typeArtifact instanceof ISessionArtifact) {
				return true;
			} else if (isDisplayReference()) {
				if (typeArtifact instanceof IQueryArtifact
						|| typeArtifact instanceof IEventArtifact
						|| typeArtifact instanceof IUpdateProcedureArtifact
						|| typeArtifact instanceof IEnumArtifact) {
					return true;
				}
			}
		}
		return false; 
	}
	
	public static IAbstractArtifact[] getSuitableTypes(){
		if (suitableTypes == null)
			loadSuitableTypes();
		return suitableTypes;
	}
		
	private static void loadSuitableTypes(){
		List<IAbstractArtifact> suitableModelsList = new ArrayList<IAbstractArtifact>();
		
		//suitableModelsList.add(PrimitiveTypeArtifact.MODEL);
		
		suitableModelsList.add(DatatypeArtifact.MODEL);
		suitableModelsList.add(AssociationClassArtifact.MODEL);
		suitableModelsList.add(ManagedEntityArtifact.MODEL);
		suitableModelsList.add(ExceptionArtifact.MODEL);
		suitableModelsList.add(SessionFacadeArtifact.MODEL);
		
		if (isDisplayReference()) {
			//suitableModelsList.add(AssociationArtifact.MODEL);
			//suitableModelsList.add(DependencyArtifact.MODEL);
			suitableModelsList.add(QueryArtifact.MODEL);
			suitableModelsList.add(EventArtifact.MODEL);
			suitableModelsList.add(UpdateProcedureArtifact.MODEL);
			suitableModelsList.add(EnumArtifact.MODEL);
		}
		suitableTypes = suitableModelsList.toArray( new IAbstractArtifact[0] );
	}
	
	private static boolean isDisplayReference() {
		OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile()
				.getProperty(IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);
		return prop
				.getPropertyValue(IOssjLegacySettigsProperty.USEATTRIBUTES_ASREFERENCE);
	}
	
	
	public String getLabel() {
		return "Association End";
	}

	public final static String AEND_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.ASSOCIATION + "-aEnd";

	public final static String ZEND_TAG = AbstractArtifactTag.PREFIX
			+ AbstractArtifactTag.ASSOCIATION + "-zEnd";

	private IModelComponent containingModelComponent;

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

		
		this.type = new Type(QDoxUtils.getTypeName(field.getType()), EMultiplicity.ONE,
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
		multiplicity = IModelComponent.EMultiplicity.parse(props.getProperty(
				"multiplicity", IModelComponent.EMultiplicity.ONE.getLabel()));

		// Extract all the stereotypes
		extractStereotypes();
	}

	@ProvideModelComponents
	public IType getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setContainingAssociation(IAssociationArtifact artifact) {
		this.containingModelComponent = artifact;
	}

	@ProvideModelComponents
	public IAssociationArtifact getContainingAssociation() {
		return (IAssociationArtifact) this.containingModelComponent;
	}

	public void setContainingArtifact(IAbstractArtifactInternal artifact) {
		this.containingModelComponent = artifact;
	}

	@ProvideModelComponents
	public IAbstractArtifact getContainingArtifact() {
		if (this.containingModelComponent instanceof IAssociationArtifact)
			return (IAbstractArtifact) this.containingModelComponent;
		else
			return null;
	}

	public Collection<IModelComponent> getContainedModelComponents() {
		// Ends don't contain anything
		return Collections
				.unmodifiableCollection(new ArrayList<IModelComponent>());
	}

	public void addContainedModelComponent(IModelComponent component)
			throws TigerstripeException {
		throw new TigerstripeException(
				"Association Ends  cannot contain any Components");
	}

	public void addContainedModelComponents(
			Collection<IModelComponent> components) throws TigerstripeException {
		throw new TigerstripeException(
				"Association Ends  cannot contain any Components");
	}

	public void removeContainedModelComponent(IModelComponent component) {
		return;
	}

	@ProvideModelComponents
	public IModelComponent getContainingModelComponent() {
		if (this.containingModelComponent instanceof IAssociationArtifact)
			return this.containingModelComponent;
		else
			return null;
	}

	public void setContainingModelComponent(IModelComponent containingComponent)
			throws TigerstripeException {
		if (containingComponent instanceof IAssociationArtifact)
			this.containingModelComponent = containingComponent;
		else
			throw new TigerstripeException(
					"Association Ends can only be contained by Association Artifacts");
	}

	public IType makeType() {
		return new Type(getArtifactManager());
	}

	@ProvideModelComponents
	public IRelationship getContainingRelationship() {
		return getContainingAssociation();
	}

	@ProvideModelComponents
	public IRelationshipEnd getOtherEnd() {
		if (this == getContainingRelationship().getRelationshipAEnd())
			return getContainingRelationship().getRelationshipZEnd();
		else
			return getContainingRelationship().getRelationshipAEnd();
	}

	public IStatus validate() {

		MultiStatus result = new MultiStatus(BasePlugin.getPluginId(), 222,
				"AssociationEnd Validation", null);

		// check association end's name to ensure it is not a reserved keyword
		if (TigerstripeValidationUtils.keywordList.contains(getName())) {
			result.add(new Status(IStatus.ERROR, BasePlugin.getPluginId(), "'"
					+ getName()
					+ "' is a reserved keyword and cannot be used an "
					+ ArtifactMetadataFactory.INSTANCE.getMetadata(
							IAssociationArtifactImpl.class.getName()).getLabel(
							getContainingAssociation()) + " end name"));
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
					ArtifactMetadataFactory.INSTANCE.getMetadata(
							IAssociationArtifactImpl.class.getName()).getLabel(
							getContainingAssociation())
							+ " End cannot be a "
							+ ArtifactMetadataFactory.INSTANCE.getMetadata(
									IPrimitiveTypeImpl.class.getName())
									.getLabel(null) + "."));
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

	@Override
	public ITigerstripeModelProject getProject() throws TigerstripeException {
		if (getContainingArtifact() != null)
			return getContainingArtifact().getProject();

		return null;
	}

	public String getLabelString() {
		String result = getName() + ":" + getType().getName();
		if (getMultiplicity() != IModelComponent.EMultiplicity.ONE) {
			result = result + "[" + getMultiplicity().getLabel() + "]";
		}

		return result;
	}

}
