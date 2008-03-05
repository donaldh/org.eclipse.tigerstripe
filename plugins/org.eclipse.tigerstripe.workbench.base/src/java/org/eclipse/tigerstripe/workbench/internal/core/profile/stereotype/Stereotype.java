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
package org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.MigrationHelper;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeScopeDetails;

/**
 * Implementation of a Stereotype definition in a Workbench profile
 * 
 * This class serves as the factory for instances of stereotype as they may be
 * found in artifact components
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class Stereotype implements IStereotype {

	private IWorkbenchProfile profile;

	private String name = "";

	private String version = "";

	private String description = "";

	private String parentStereotype = "";

	private IStereotypeScopeDetails details = new StereotypeScopeDetails();

	private List<IStereotypeAttribute> attributes = new ArrayList<IStereotypeAttribute>();

	private boolean isVisible = true;

	private List<String> requiresList = new ArrayList<String>();

	private List<String> excludesList = new ArrayList<String>();

	public IStereotypeScopeDetails getStereotypeScopeDetails() {
		return details;
	}

	public Stereotype(IWorkbenchProfile profile) {
		this.profile = profile;
	}

	public IWorkbenchProfile getProfile() {
		return this.profile;
	}

	public String getParentStereotype() {
		return this.parentStereotype;
	}

	public void setParentStereotype(String parentStereotype) {
		this.parentStereotype = parentStereotype;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isVisible() {
		return this.isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public IStereotypeAttribute[] getIAttributes() {
		return getAttributes();
	}

	public IStereotypeAttribute[] getAttributes() {
		return this.attributes.toArray(new IStereotypeAttribute[this.attributes
				.size()]);
	}

	public IStereotypeAttribute getIAttributeByName(String nameToGet) {
		return getAttributeByName(nameToGet);
	}

	public IStereotypeAttribute getAttributeByName(String nameToGet) {
		for (IStereotypeAttribute attribute : attributes) {
			if (attribute.getName().equals(nameToGet))
				return attribute;
		}
		return null;
	}

	public boolean isValidAttribute(IStereotypeAttribute attribute) {
		return this.attributes.contains(attribute);
	}

	public void setAttributes(IStereotypeAttribute[] attributes)
			throws TigerstripeException {
		this.attributes = new ArrayList<IStereotypeAttribute>();
		for (IStereotypeAttribute attribute : attributes) {
			if (this.attributes.contains(attribute))
				throw new TigerstripeException("Duplicate attribute '"
						+ attribute.getName() + "' in stereotype '" + getName()
						+ "'.");
			else {
				this.attributes.add(attribute);
			}
		}
	}

	public void addAttribute(IStereotypeAttribute attribute)
			throws TigerstripeException {

		/* New check on name only */
		if (getAttributeByName(attribute.getName()) == null) {

			/*
			 * this check only works for "exact" matches : name and type - so
			 * added the above check on name only - so could probabaly loose
			 * this one?
			 */
			// if (this.attributes.contains(attribute)) {
			this.attributes.add(attribute);

		} else
			throw new TigerstripeException("Duplicate attribute '"
					+ attribute.getName() + "' in stereotype '" + getName()
					+ "'.");
	}

	public void removeAttribute(IStereotypeAttribute attribute)
			throws TigerstripeException {
		if (this.attributes.contains(attribute)) {
			TigerstripeRuntime.logInfoMessage("removing attr "
					+ attribute.getName());
			this.attributes.remove(attribute);
		} else
			throw new TigerstripeException("Unknown attribute '"
					+ attribute.getName() + "' in stereotype '" + getName()
					+ "'.");
	}

	public void removeAttributes(IStereotypeAttribute[] attributes)
			throws TigerstripeException {
		for (IStereotypeAttribute attribute : attributes) {
			removeAttribute(attribute);
		}
	}

	public String[] getRequiresList() {
		return this.requiresList.toArray(new String[this.requiresList.size()]);
	}

	public void setRequiresList(String[] requiredStereotypeNames)
			throws TigerstripeException {
		this.requiresList = new ArrayList<String>();
		for (String requiredName : requiredStereotypeNames) {
			if (this.requiresList.contains(requiredName))
				throw new TigerstripeException(
						"Duplicate required stereotype '" + requiredName
								+ "' in stereotype '" + getName() + "'.");
			else {
				this.requiresList.add(requiredName);
			}
		}
	}

	public void addToRequiresList(String requiredStereotypeName)
			throws TigerstripeException {
		if (this.requiresList.contains(requiredStereotypeName))
			throw new TigerstripeException("Duplicate required stereotype '"
					+ requiredStereotypeName + "' in stereotype '" + getName()
					+ "'.");
		else {
			this.requiresList.add(requiredStereotypeName);
		}
	}

	public void removeFromRequiresList(String requiredStereotypeName)
			throws TigerstripeException {
		if (this.requiresList.contains(requiredStereotypeName)) {
			this.requiresList.remove(requiredStereotypeName);
		} else
			throw new TigerstripeException("Unknown required stereotype '"
					+ requiredStereotypeName + "' in stereotype '" + getName()
					+ "'.");
	}

	public void removeFromRequiresList(String[] names)
			throws TigerstripeException {
		for (String name : names) {
			removeFromRequiresList(name);
		}
	}

	public String[] getExcludesList() {
		return this.excludesList.toArray(new String[this.excludesList.size()]);
	}

	public void setExcludesList(String[] excludesStereotypeNames)
			throws TigerstripeException {
		this.excludesList = new ArrayList<String>();
		for (String excludedName : excludesStereotypeNames) {
			if (this.excludesList.contains(excludedName))
				throw new TigerstripeException(
						"Duplicate excluded stereotype '" + excludedName
								+ "' in stereotype '" + getName() + "'.");
			else {
				this.excludesList.add(excludedName);
			}
		}
	}

	public void addToExcludesList(String excludesStereotypeName)
			throws TigerstripeException {
		if (this.excludesList.contains(excludesStereotypeName))
			throw new TigerstripeException("Duplicate excluded stereotype '"
					+ excludesStereotypeName + "' in stereotype '" + getName()
					+ "'.");
		else {
			this.excludesList.add(excludesStereotypeName);
		}
	}

	public void removeFromExcludesList(String excludesStereotypeName)
			throws TigerstripeException {
		if (this.excludesList.contains(excludesStereotypeName)) {
			this.excludesList.remove(excludesStereotypeName);
		} else
			throw new TigerstripeException("Unknown excluded stereotype '"
					+ excludesStereotypeName + "' in stereotype '" + getName()
					+ "'.");
	}

	public void removeFromExcludesList(String[] names)
			throws TigerstripeException {
		for (String name : names) {
			removeFromExcludesList(name);
		}
	}

	public Element asElement() {
		DocumentFactory factory = DocumentFactory.getInstance();
		Element stereotype = factory.createElement("stereotype");
		stereotype.addAttribute("name", getName());
		stereotype.addAttribute("version", getVersion());
		stereotype.addAttribute("isVisible", String.valueOf(isVisible()));
		stereotype.addElement("description").setText(getDescription());

		Element scope = stereotype.addElement("scope");
		scope.addAttribute("argument", String
				.valueOf(details.isArgumentLevel()));
		scope.addAttribute("attribute", String.valueOf(details
				.isAttributeLevel()));
		scope.addAttribute("method", String.valueOf(details.isMethodLevel()));
		scope.addAttribute("label", String.valueOf(details.isLiteralLevel()));
		scope.addAttribute("associationEnd", String.valueOf(details.isAssociationEndLevel()));

		if (details.getArtifactLevelTypes() != null) {
			String[] types = details.getArtifactLevelTypes();
			for (String type : types) {
				scope.addElement("artifactLevelType").setText(type);
			}
		}

		Element attributes = stereotype.addElement("attributes");
		for (IStereotypeAttribute attribute : getAttributes()) {
			attributes.add(attribute.asElement());
		}

		Element requires = stereotype.addElement("requires");
		for (String require : getRequiresList()) {
			requires.addElement("require").addAttribute("name", require);
		}

		Element excludes = stereotype.addElement("excludes");
		for (String exclude : getExcludesList()) {
			excludes.addElement("exclude").addAttribute("name", exclude);
		}

		return stereotype;
	}

	public void parse(Element element) throws TigerstripeException {
		if (!"stereotype".equals(element.getName()))
			throw new TigerstripeException("Not a stereotype element: "
					+ element.getName());
		setName(element.attributeValue("name"));
		setVersion(element.attributeValue("version"));
		setDescription(element.element("description").getText());
		setVisible(Boolean.parseBoolean(element.attributeValue("isVisible")));

		Element scope = element.element("scope");
		if (scope != null) {
			details.setArgumentLevel("true".equals(scope
					.attributeValue("argument")));
			details.setAttributeLevel("true".equals(scope
					.attributeValue("attribute")));
			details.setMethodLevel("true"
					.equals(scope.attributeValue("method")));
			details.setLiteralLevel("true"
					.equals(scope.attributeValue("label")));
			details.setAssociationEndLevel("true"
					.equals(scope.attributeValue("associationEnd")));
			details.setArtifactLevelTypes(null);
			ArrayList<String> incls = new ArrayList<String>();
			for (Iterator<Element> iter = scope
					.elementIterator("artifactLevelType"); iter.hasNext();) {
				incls.add(MigrationHelper
						.profileMigrateAnnotationArtifactLevelType(iter.next()
								.getText()));
			}
			if (incls.size() != 0) {
				details.setArtifactLevelTypes(incls.toArray(new String[incls
						.size()]));
			}
		}

		Element attributes = element.element("attributes");
		this.attributes = new ArrayList<IStereotypeAttribute>();
		for (Iterator attrIter = attributes.elementIterator(); attrIter
				.hasNext();) {
			Element elm = (Element) attrIter.next();
			IStereotypeAttribute attr = StereotypeAttributeFactory
					.makeAttribute(elm);
			if (attr != null)
				this.attributes.add(attr);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Stereotype) {
			Stereotype st = (Stereotype) obj;
			if (st.getName() != null)
				return st.getName().equals(getName());
		}

		return false;
	}

	public IStereotypeInstance makeInstance() {
		StereotypeInstance result = new StereotypeInstance(this);

		// go thru every attribute and setup the default value if it exists
		for (IStereotypeAttribute attr : attributes) {
			try {
				result.setAttributeValue(attr, attr.getDefaultValue());
			} catch (TigerstripeException e) {
				// ignore here
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return this.getName();
	}
}
