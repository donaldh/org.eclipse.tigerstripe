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
package org.eclipse.tigerstripe.workbench.internal.core.profile.primitiveType;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.eclipse.tigerstripe.metamodel.impl.IPrimitiveTypeImpl;
import org.eclipse.tigerstripe.metamodel.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeValidationUtils;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.profile.primitiveType.IPrimitiveTypeDef;

public class PrimitiveTypeDef implements IPrimitiveTypeDef {

	private String description = "";

	private String name = "";

	private String packageName = ITigerstripeConstants.BASEPRIMITIVE_PACKAGE;

	private boolean isReserved = false;

	public void setReserved(boolean isReserved) {
		this.isReserved = isReserved;
	}

	public boolean isReserved() {
		return this.isReserved;
	}

	public String getDescription() {
		return description;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPackageName() {
		return this.packageName;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Element asElement() {
		DocumentFactory factory = DocumentFactory.getInstance();
		Element primitiveType = factory.createElement("primitiveType");
		primitiveType.addAttribute("name", getName());
		primitiveType.addElement("description").setText(getDescription());
		return primitiveType;
	}

	public Element asDefaultElement() {
		Element primitiveType = asElement();
		primitiveType.addElement("packageName").setText(packageName);
		return primitiveType;
	}

	public void parse(Element element) throws TigerstripeException {
		if (!"primitiveType".equals(element.getName()))
			throw new TigerstripeException("Not a " + ArtifactMetadataFactory.INSTANCE.getMetadata(
					IPrimitiveTypeImpl.class.getName())
					.getLabel() + " element: "
					+ element.getName());
		setName(element.attributeValue("name"));
		setDescription(element.element("description").getText());

		if (element.element("packageName") != null) {
			setPackageName(element.element("packageName").getText());
			if (packageName.equals(IPrimitiveTypeArtifact.RESERVED)) {
				isReserved = true;
			}
		}
	}

	public boolean isRecommendedName() {
		// Bug 715, loosening up the conditions here as Cisco is getting a lot
		// of
		// unwanted warnings.
		return TigerstripeValidationUtils.elementNamePattern.matcher(getName())
				.matches();
	}

	public boolean isValidName() {
		return !TigerstripeValidationUtils.keywordList.contains(getName())
				&& TigerstripeValidationUtils.elementNamePattern.matcher(
						getName()).matches();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PrimitiveTypeDef) {
			PrimitiveTypeDef other = (PrimitiveTypeDef) obj;
			return getName() != null && getName().equals(other.getName())
					&& getPackageName() != null
					&& getPackageName().equals(other.getPackageName());
		}
		return false;
	}

	@Override
	public String toString() {
		return getName();
	}

}
