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
package org.eclipse.tigerstripe.core.profile.stereotype;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotypeAttribute;

public abstract class BaseStereotypeAttribute implements IStereotypeAttribute {

	private String name = "";
	private String description = "";
	private String defaultValue = "";
	private int kind;

	private boolean isArray;

	protected BaseStereotypeAttribute(int kind) {
		this.kind = kind;
	}

	public boolean isArray() {
		return isArray;
	}

	public void setArray(boolean isArray) {
		this.isArray = isArray;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDefaultValue() {
		return this.defaultValue;
	}

	public int getKind() {
		return this.kind;
	}

	public Element asElement() {
		DocumentFactory factory = DocumentFactory.getInstance();
		Element result = factory.createElement("attribute");
		result.addAttribute("name", getName());
		result.addAttribute("kind", String.valueOf(kind));
		result.addAttribute("defaultValue", getDefaultValue());
		result.addAttribute("isArray", String.valueOf(isArray()));
		result.addElement("description").setText(getDescription());
		return result;
	}

	public void parse(Element element) {
		setName(element.attributeValue("name"));
		// kind is already set at this point since the factory is used when
		// creating attrs
		setDefaultValue(element.attributeValue("defaultValue"));
		setArray(Boolean.parseBoolean(element.attributeValue("isArray")));
		if (element.element("description") != null)
			setDescription(element.element("description").getText());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BaseStereotypeAttribute) {
			BaseStereotypeAttribute attr = (BaseStereotypeAttribute) obj;
			if (attr.getName() != null)
				return attr.getName().equals(getName());
		}
		return false;
	}
}
