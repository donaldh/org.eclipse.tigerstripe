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
import java.util.Iterator;
import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeValidationUtils;
import org.eclipse.tigerstripe.workbench.model.ILabel;
import org.eclipse.tigerstripe.workbench.model.IType;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;

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
	public IStatus validate() {

		MultiStatus result = new MultiStatus(BasePlugin.getPluginId(), 222,
				"Label validation", null);

		// check label name to ensure that it is a valid fieldname in Java
		if (!TigerstripeValidationUtils.literalNamePattern.matcher(getName())
				.matches()) {
			result.add(new Status(IStatus.ERROR, BasePlugin.getPluginId(), "'"
					+ getName() + "' is not a valid label name"));
		}
		// check label name to ensure it is not a reserved keyword
		else if (TigerstripeValidationUtils.keywordList.contains(getName())) {
			result
					.add(new Status(
							IStatus.ERROR,
							BasePlugin.getPluginId(),
							"'"
									+ getName()
									+ "' is a reserved keyword and cannot be used as label name"));
		}

		// check the validity of the type for this label
		IStatus typeStatus = getIType().validate();
		if (!typeStatus.isOK())
			result.add(typeStatus);

		if (result.isOK())
			return Status.OK_STATUS;
		else
			return result;

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

	/**
	 * Returns a duplicate of the initial list where all components that are not
	 * in the current active facet are filtered out.
	 * 
	 * @param components
	 * @return
	 */
	public static Collection<ILabel> filterFacetExcludedLabels(
			Collection<ILabel> components) {
		ArrayList<ILabel> result = new ArrayList<ILabel>();
		for (Iterator<ILabel> iter = components.iterator(); iter.hasNext();) {
			ILabel component = iter.next();
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
}
