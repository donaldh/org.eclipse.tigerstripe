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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Site {

	public final static String MIRRORS_URL = "mirrorsURL";

	private String mirrorsURL = "";
	private String description = "";
	private List<CategoryDef> categoryDefs = new ArrayList<CategoryDef>();
	private List<Feature> features = new ArrayList<Feature>();

	public void setMirrorsURL(String mirrorsURL) {
		this.mirrorsURL = mirrorsURL;
	}

	public String getMirrorsURL() {
		return this.mirrorsURL;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public List<CategoryDef> getCategoryDefs() {
		return this.categoryDefs;
	}

	public List<Feature> getFeatures() {
		return this.features;
	}

	public CategoryDef createCategoryDef() {
		CategoryDef result = new CategoryDef();
		categoryDefs.add(result);
		return result;
	}

	public Site() {
		// default constructor
	}

	public Site(Document document) {
		parseDocument(document);
	}

	public final static Site parse(File file) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(file);
		return new Site(document);
	}

	@SuppressWarnings("unchecked")
	private void parseDocument(Document document) {
		Element root = document.getRootElement();

		setMirrorsURL(root.attributeValue(MIRRORS_URL));
		Element desc = root.element("description");
		if (desc != null)
			setDescription(desc.getText());

		for (Iterator<Element> i = root
				.elementIterator(CategoryDef.CATEGORY_DEF); i.hasNext();) {
			Element catDefElement = i.next();
			CategoryDef catDef = new CategoryDef(catDefElement);
			categoryDefs.add(catDef);
		}

		for (Iterator<Element> i = root.elementIterator(Feature.FEATURE); i
				.hasNext();) {
			Element element = i.next();
			Feature feat = new Feature(element);
			features.add(feat);
		}

	}

	public final void saveAs(File file) throws IOException {
		Document document = toDocument();
		String xml = document.asXML();
		FileWriter writer = new FileWriter(file);
		try {
			writer.write(xml);
			writer.close();
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	private Document toDocument() {
		Document result = DocumentHelper.createDocument();

		Element siteElem = result.addElement("site");
		siteElem.addAttribute(MIRRORS_URL, getMirrorsURL());
		siteElem.addElement("description").setText(getDescription());
		for (CategoryDef def : categoryDefs) {
			siteElem.add(def.toElement());
		}
		for (Feature feat : features) {
			siteElem.add(feat.toElement());
		}

		return result;
	}
}
