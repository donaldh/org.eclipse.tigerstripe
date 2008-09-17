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
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.profile.IActiveWorkbenchProfileChangeListener;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IOssjLegacySettigsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.OssjLegacySettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeValidationUtils;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaField;

/**
 * A model for a field on an AbstractArtifact
 * 
 * @author Eric Dillon
 */
public class Field extends ArtifactComponent implements IField, IActiveWorkbenchProfileChangeListener {

	private static boolean isRegistered = false;
	
	private static IAbstractArtifact[] suitableTypes;
	
	public static IAbstractArtifact[] getSuitableTypes(){
		if (suitableTypes == null)
			loadSuitableTypes();
		return suitableTypes;
	}
		
	private static void loadSuitableTypes(){
		List<IAbstractArtifact> suitableModelsList = new ArrayList<IAbstractArtifact>();
		suitableModelsList.add(PrimitiveTypeArtifact.MODEL);
		suitableModelsList.add(DatatypeArtifact.MODEL);
		suitableModelsList.add(EnumArtifact.MODEL);
		
		IWorkbenchProfile profile = TigerstripeCore
			.getWorkbenchProfileSession()
			.getActiveProfile();
		OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) TigerstripeCore
			.getWorkbenchProfileSession().getActiveProfile().getProperty(
				IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);
		boolean displayReference = prop
			.getPropertyValue(IOssjLegacySettigsProperty.USEATTRIBUTES_ASREFERENCE);
		if (displayReference){
			suitableModelsList.add(ExceptionArtifact.MODEL);
			suitableModelsList.add(AssociationArtifact.MODEL);
			suitableModelsList.add(DependencyArtifact.MODEL);
			suitableModelsList.add(AssociationClassArtifact.MODEL);
			suitableModelsList.add(QueryArtifact.MODEL);
			suitableModelsList.add(EventArtifact.MODEL);
			suitableModelsList.add(UpdateProcedureArtifact.MODEL);
			suitableModelsList.add(SessionFacadeArtifact.MODEL);
			suitableModelsList.add(ManagedEntityArtifact.MODEL);
		}

		suitableTypes = suitableModelsList.toArray( new IAbstractArtifact[0] );
	}
	
	
	public void profileChanged(IWorkbenchProfile newActiveProfile) {
		suitableTypes = null;
	}

	public String getLabel() {
		return "Field";
	}

	public static int refByFromLabel(String label) {
		int result = IField.NON_APPLICABLE;
		for (int i = 0; i < refByLabels.length; i++) {
			if (refByLabels[i].equalsIgnoreCase(label)) {
				result = i;
			}
		}

		return result;
	}

	private IModelComponent containingModelComponent;

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
		if (! isRegistered){
			TigerstripeCore
				.getWorkbenchProfileSession().addActiveProfileListener(this);
			isRegistered = true;
		}
	}

	public Field(JavaField field, ArtifactManager artifactMgr) {
		this(artifactMgr);
		buildModel(field);
		if (! isRegistered){
			TigerstripeCore
			.getWorkbenchProfileSession().addActiveProfileListener(this);
			isRegistered = true;
		}
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
		// Create a type with a basic Multiplicity that can be reset below.
		this.type = new Type(type.getValue(), EMultiplicity.ONE, getArtifactManager()); 
		setName(field.getName());

		Tag tag = getFirstTagByName(AbstractArtifactTag.PREFIX
				+ AbstractArtifactTag.FIELD);
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
				this.type.setTypeMultiplicity(IModelComponent.EMultiplicity
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
		this.containingModelComponent = artifact;
	}

	
	public IAbstractArtifact getContainingArtifact() {
		if (this.containingModelComponent instanceof IAbstractArtifact)
			return (IAbstractArtifact) this.containingModelComponent;
		else
			return null;
	}

	public Collection<IModelComponent> getContainedModelComponents() {
		// Fields don't contain anything
		return Collections.unmodifiableCollection( new ArrayList<IModelComponent>());
	}

	public void addContainedModelComponent(IModelComponent component) throws TigerstripeException {
		throw new TigerstripeException("Fields cannot contain any Components");
	}

	public void addContainedModelComponents(
			Collection<IModelComponent> components) throws TigerstripeException{
		throw new TigerstripeException("Fields cannot contain any Components");
	}
	
	
	
	public void removeContainedModelComponent(IModelComponent component) {
		return ;	
	}

	public IModelComponent getContainingModelComponent() {
		if (this.containingModelComponent instanceof IAbstractArtifact)
			return (IAbstractArtifact) this.containingModelComponent;
		else
			return null;
	}
	

	public void setContainingModelComponent(IModelComponent containingComponent) throws TigerstripeException {
		if (containingComponent instanceof IAbstractArtifact)
			this.containingModelComponent = containingComponent;
		else 
			throw new TigerstripeException("Fields can only be contained by Artifacts");
	}


	// ==================================================================
	// Methods to satisfy the IType interface

	
	public IType getType() {
		return this.type;
	}

	public IType makeType() {
		return new Type(getArtifactManager());
	}

	public void setType(IType type) {
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
	 * @see org.eclipse.tigerstripe.api.artifacts.model.ILiteral#validate()
	 */
	public IStatus validate() {

		MultiStatus result = new MultiStatus(BasePlugin.getPluginId(), 222,
				"Field Validation", null);

		// check field name to ensure that it is a valid fieldname in Java
		if (!TigerstripeValidationUtils.elementNamePattern.matcher(getName())
				.matches()
				&& !TigerstripeValidationUtils.allUppercase.matcher(getName())
						.matches()) {
			result.add(new Status(IStatus.ERROR, BasePlugin.getPluginId(), "'"
					+ getName() + "' is not a valid field name"));
		}
		// check field name to ensure it is not a reserved keyword
		else if (TigerstripeValidationUtils.keywordList.contains(getName())) {
			result
					.add(new Status(
							IStatus.ERROR,
							BasePlugin.getPluginId(),
							"'"
									+ getName()
									+ "' is a reserved keyword and cannot be used as field name"));
		}

		// check the validity of the type for this field
		IStatus typeStatus = getType().validate();
		if (!typeStatus.isOK())
			result.add(typeStatus);

		if (result.isOK())
			return Status.OK_STATUS;
		else
			return result;

	}

	public String getLabelString() {
		String result = getName() + ":" + getType().getName();
		if (getType().getTypeMultiplicity() != IModelComponent.EMultiplicity.ONE) {
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

		IField result = ((AbstractArtifact) getContainingArtifact()).makeField();
		result.setName(getName());
		result.setComment(getComment());
		result.setDefaultValue(getDefaultValue());
		result.setType(getType().clone());
		result.setOptional(isOptional());
		result.setOrdered(isOrdered());
		result.setReadOnly(isReadOnly());
		result.setRefBy(getRefBy());
		result.setUnique(isUnique());
		result.setVisibility(getVisibility());

		Collection<IStereotypeInstance> stereotypeInstances = getStereotypeInstances();
		for (IStereotypeInstance inst : stereotypeInstances) {
			result.addStereotypeInstance(inst);
		}
		return result;
	}

	/**
	 * Returns a duplicate of the initial list where all components that are not
	 * in the current active facet are filtered out.
	 * 
	 * @param components
	 * @return
	 */
	public static Collection<IField> filterFacetExcludedFields(
			Collection<IField> components) {
		ArrayList<IField> result = new ArrayList<IField>();
		for (Iterator<IField> iter = components.iterator(); iter.hasNext();) {
			IField component = iter.next();
			try {
				if (!component.isInActiveFacet())
					continue;
				else
					result.add(component);
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage(
						"Error while evaluating isInActiveFacet for "
								+ component.getName() + ": " + e.getMessage(),
						e);
			}
		}
		return result;
	}
	
	@Override
	public ITigerstripeModelProject getProject() throws TigerstripeException {
		if (getContainingArtifact() != null)
			return ((AbstractArtifact)getContainingArtifact()) .getProject();

		return null;
	}
}
