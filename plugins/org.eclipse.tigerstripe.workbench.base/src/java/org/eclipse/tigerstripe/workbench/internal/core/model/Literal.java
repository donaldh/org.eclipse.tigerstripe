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
import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeValidationUtils;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaField;

/**
 * @author Eric Dillon
 * 
 */
public class Literal extends ArtifactComponent implements ILiteral {

	public String getLabel() {
		return "Literal";
	}
	
	private IModelComponent containingModelComponent;

	private Type type;

	/**
	 * The value of the literal
	 */
	private String value;

	public Literal(ArtifactManager artifactMgr) {
		super(artifactMgr);
	}

	public Literal(JavaField field, ArtifactManager artifactMgr) {
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
		// Create a type with a basic Multiplicity that can be reset below.
		this.type = new Type(type.getValue(), EMultiplicity.ONE, getArtifactManager()); 
		setName(field.getName());

		// Extract value of Literal
		Tag tag = getFirstTagByName(AbstractArtifactTag.PREFIX
				+ AbstractArtifactTag.LITERAL);
		if (tag != null) {
			Properties props = tag.getProperties();
			this.value = props.getProperty("value", field.getName()
					.toUpperCase());
		}

		// Extract all the stereotypes
		extractStereotypes();
	}


	public void setType(IType type) {
		// Check for valid types
		if (type.getName().equals("int") || type.getName().equals("String")){
			this.type = (Type) type;
		} else {
//			throw new TigerstripeException("Invalid type set for Literal");
		}
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
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
		// Literals don't contain anything
		return Collections.unmodifiableCollection( new ArrayList<IModelComponent>());
	}

	
	public void addContainedModelComponent(IModelComponent component) throws TigerstripeException {
		throw new TigerstripeException("Literals cannot contain any Components");
	}

	public void removeContainedModelComponent(IModelComponent component) {
		return ;	
	}
	
	public void addContainedModelComponents(
			Collection<IModelComponent> components) throws TigerstripeException{
		throw new TigerstripeException("Literals cannot contain any Components");
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
	// ===============================================================
	// To satisfy the ILiteral interface

	public String getLabelString() {
		return getName() + "=" + getValue();
	}


	public IType makeType() {
		return new Type(getArtifactManager());
	}

	public IType getType() {
		return this.type;
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
	 * @see org.eclipse.tigerstripe.api.artifacts.model.ILiteral#validate()
	 */
	public IStatus validate() {

		MultiStatus result = new MultiStatus(BasePlugin.getPluginId(), 222,
				"Literal validation", null);

		// check literal name to ensure that it is a valid fieldname in Java
		if (!TigerstripeValidationUtils.literalNamePattern.matcher(getName())
				.matches()) {
			result.add(new Status(IStatus.ERROR, BasePlugin.getPluginId(), "'"
					+ getName() + "' is not a valid literal name"));
		}
		// check literal name to ensure it is not a reserved keyword
		else if (TigerstripeValidationUtils.keywordList.contains(getName())) {
			result
					.add(new Status(
							IStatus.ERROR,
							BasePlugin.getPluginId(),
							"'"
									+ getName()
									+ "' is a reserved keyword and cannot be used as literal name"));
		}

		// check the validity of the type for this literal
		IStatus typeStatus = getType().validate();
		if (!typeStatus.isOK())
			result.add(typeStatus);

		if (result.isOK())
			return Status.OK_STATUS;
		else
			return result;

	}

	@Override
	public ILiteral clone() {
		ILiteral result = new Literal(getArtifactManager());

		result.setComment(getComment());
		result.setType(getType().clone());
		result.setName(getName());
		result.setValue(getValue());
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
	public static Collection<ILiteral> filterFacetExcludedLiterals(
			Collection<ILiteral> components) {
		ArrayList<ILiteral> result = new ArrayList<ILiteral>();
		for (Iterator<ILiteral> iter = components.iterator(); iter.hasNext();) {
			ILiteral component = iter.next();
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
			return getContainingArtifact().getProject();

		return null;
	}

}
