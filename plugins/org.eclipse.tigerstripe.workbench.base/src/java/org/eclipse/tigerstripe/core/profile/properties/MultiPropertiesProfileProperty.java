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
package org.eclipse.tigerstripe.core.profile.properties;

import java.util.HashMap;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.eclipse.tigerstripe.api.TigerstripeException;

public abstract class MultiPropertiesProfileProperty extends
		BaseWorkbenchProfileProperty {

	private HashMap<String, Boolean> values = new HashMap<String, Boolean>();

	public MultiPropertiesProfileProperty() {
		super();
		initDefaultValues(true);
	}

	public abstract String[][] getProperties();

	protected void initDefaultValues(boolean overwrite) {

		if (overwrite)
			values.clear();

		for (String[] entry : getProperties()) {
			String propertyKey = entry[0];
			Boolean defaultValue = new Boolean(entry[2]);
			if (!values.containsKey(propertyKey))
				values.put(propertyKey, defaultValue);
		}
	}

	protected abstract String getPropertyLabel();

	public void parseFromSerializedString(String serializedString)
			throws TigerstripeException {

		initDefaultValues(true);
		Document document = documentFromString(serializedString);
		Element rootElem = document.getRootElement();

		for (Iterator<Element> iter = rootElem.elementIterator("property"); iter
				.hasNext();) {
			Element propElem = iter.next();
			String key = propElem.attributeValue("key");
			String value = propElem.attributeValue("value");
			values.put(key, new Boolean(value));
		}
	}

	public boolean getPropertyValue(String propertyKey) {
		return values.get(propertyKey);
	}

	public void setPropertyValue(String propertyKey, boolean value) {
		if (isValidPropertyKey(propertyKey)) {
			values.put(propertyKey, value);
		}
	}

	private boolean isValidPropertyKey(String propertyKey) {
		boolean result = false;
		for (String[] prop : getProperties()) {
			if (prop[0].equals(propertyKey)) {
				result = true;
				continue;
			}
		}
		return result;
	}

	public String serializeToString() {
		Document document = DocumentFactory.getInstance().createDocument();
		Element rootElem = DocumentFactory.getInstance().createElement(
				getPropertyLabel());
		document.setRootElement(rootElem);

		// initDefaultValues(false);
		for (String[] prop : getProperties()) {
			Element detsElem = rootElem.addElement("property");
			detsElem.addAttribute("key", prop[0]);
			Boolean value = values.get(prop[0]);
			if (value == null) {
				value = new Boolean(prop[2]);
			}
			detsElem.addAttribute("value", value.toString());
		}

		return document.asXML();
	}

}
