/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.releng.updatesite.elements;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class CategoryDef {

	public final static String CATEGORY_DEF = "category-def";

	private String name = "";
	private String label = "";
	private String description = "";

	public CategoryDef() {
		// default constructor
	}

	public CategoryDef(Element element) {
		parseElement(element);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private void parseElement(Element element) {
		setName(element.attributeValue("name"));
		setLabel(element.attributeValue("label"));
		Element descElement = element.element("description");
		if (descElement != null)
			setDescription(descElement.getText());
	}

	public Element toElement() {
		Element result = DocumentHelper.createElement(CATEGORY_DEF);
		result.addAttribute("name", getName());
		result.addAttribute("label", getLabel());
		result.addElement("description").setText(getDescription());

		return result;
	}
}
