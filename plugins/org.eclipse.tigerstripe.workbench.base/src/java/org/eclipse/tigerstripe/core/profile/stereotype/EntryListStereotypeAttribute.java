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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;
import org.eclipse.tigerstripe.api.external.profile.stereotype.IextStereotypeAttribute;
import org.eclipse.tigerstripe.api.profile.stereotype.IEntryListStereotypeAttribute;

/**
 * A checkable attribute is rendered as a simple checkbox for the end-user
 * 
 * @author Eric Dillon
 * 
 */
public class EntryListStereotypeAttribute extends BaseStereotypeAttribute
		implements IEntryListStereotypeAttribute {

	private List<String> entries = new ArrayList<String>();

	public EntryListStereotypeAttribute() {
		super(IextStereotypeAttribute.ENTRY_LIST_KIND);
	}

	public String[] getSelectableValues() {
		return entries.toArray(new String[entries.size()]);
	}

	public void setSelectableValues(String[] entries) {
		this.entries.clear();
		this.entries.addAll(Arrays.asList(entries));
	}

	public void addSelectableValue(String entry) {
		entries.add(entry);
	}

	public void removeSelectableValue(String entry) {
		entries.remove(entry);
	}

	@Override
	public Element asElement() {
		Element result = super.asElement();

		Element valuesElm = result.addElement("values");
		for (String entry : entries) {
			valuesElm.addElement("value").addAttribute("label", entry);
		}

		return result;
	}

	@Override
	public void parse(Element element) {
		super.parse(element);
		entries.clear();
		Element valuesElm = element.element("values");
		if (valuesElm != null) {
			for (Iterator iter = valuesElm.elementIterator("value"); iter
					.hasNext();) {
				Element valElm = (Element) iter.next();
				String label = valElm.attributeValue("label");
				entries.add(label);
			}
		}
	}

}
