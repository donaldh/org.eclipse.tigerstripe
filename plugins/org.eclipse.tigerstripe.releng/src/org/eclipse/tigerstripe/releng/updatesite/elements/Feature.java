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

public class Feature {

	public final static String FEATURE = "feature";

	private String url = "";
	private String id = "";
	private String version = "";
	private Category category;

	public Feature() {
		// default constructor
	}

	public Feature(Element element) {
		parse(element);
	}

	public Category createCategory() {
		this.category = new Category();
		return this.category;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	private void parse(Element element) {
		setId(element.attributeValue("id"));
		setUrl(element.attributeValue("url"));
		setVersion(element.attributeValue("version"));
		Element catElem = element.element("category");
		if (catElem != null) {
			this.category = new Category(catElem);
		}
	}

	public Element toElement() {
		Element result = DocumentHelper.createElement(FEATURE);
		result.addAttribute("id", getId());
		result.addAttribute("url", getUrl());
		result.addAttribute("version", getVersion());
		if (category != null)
			result.add(category.toElement());
		return result;
	}

	public class Category {
		private String name = "";

		public Category() {
			// default constructor;
		}

		public Category(Element element) {
			parse(element);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		private void parse(Element element) {
			setName(element.attributeValue("name"));
		}

		public Element toElement() {
			Element result = DocumentHelper.createElement("category");
			result.addAttribute("name", getName());
			return result;
		}
	}
}
