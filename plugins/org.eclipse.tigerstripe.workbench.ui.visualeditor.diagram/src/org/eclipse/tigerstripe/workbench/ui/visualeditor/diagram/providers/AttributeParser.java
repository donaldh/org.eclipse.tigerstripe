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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.providers;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.TypeMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.utils.ClassDiagramPartsUtils;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AttributeImpl;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.NamedElementImpl;

public class AttributeParser extends TigerstripeStructuralFeaturesParser {

	public AttributeParser(List features) {
		super(features);
	}

	@Override
	public String getPrintString(IAdaptable adapter, int flags) {

		setCurrentMap(adapter);
		// first get the print string from the super-class
		String printString = super.getPrintString(adapter, flags);
		// then get the underlying attribute from the wrapped adapter
		Attribute attribute = (Attribute) adapter
				.getAdapter(AttributeImpl.class);
		// from that attribute, get the prefix to use to indicate the method's
		// visibility
		String visibilityPrefix = ClassDiagramPartsUtils
				.visibilityPrefix(attribute.getVisibility());
		// if the attribute has any stereotypes, then put together a stereotype
		// prefix string for the attribute
		String stereotypePref = "";
		if (!hideStereotypes()) {
			StringBuffer stereoPrefBuf = new StringBuffer();
			EList stereotypes = attribute.getStereotypes();
			int stereotypeCount = 0;
			for (Object obj : stereotypes) {
				String val = (String) obj;
				if (stereotypeCount == 0)
					stereoPrefBuf.append("<<");
				stereoPrefBuf.append(val);
				if (++stereotypeCount < stereotypes.size())
					stereoPrefBuf.append(", ");
				else
					stereoPrefBuf.append(">>");
			}
			stereotypePref = stereoPrefBuf.toString();
		}

		if (attribute.getTypeMultiplicity() != AssocMultiplicity.ONE_LITERAL)
			printString = printString + "["
					+ attribute.getTypeMultiplicity().getLiteral() + "]";

		if (!hideDefaultValues() && attribute.getDefaultValue() != null) {
			if (attribute.getDefaultValue().length() == 0) {
				printString = printString + "=\"\"";
			} else {
				printString = printString + "=" + attribute.getDefaultValue();
			}
		}

		if (stereotypePref.length() == 0)
			return visibilityPrefix + printString;
		// else, return with the stereotype prefix...

		return stereotypePref + " " + visibilityPrefix + printString;
	}

	@Override
	public String getEditString(IAdaptable adapter, int flags) {
		String editString = super.getEditString(adapter, flags);
		// then get the underlying attribute from the wrapped adapter
		Attribute attribute = (Attribute) adapter
				.getAdapter(AttributeImpl.class);
		if (attribute.getMultiplicity() == TypeMultiplicity.ARRAY_LITERAL)
			editString = editString + "[]";
		return editString;
	}

	@Override
	protected Object getValidNewValue(EStructuralFeature feature, Object value) {
		Object newValue = super.getValidNewValue(feature, value);
		if (!(newValue instanceof InvalidValue) && (value instanceof String)) {
			// if the value is for a feature named "type", then check to see if
			// the
			// value ends in "[]", if so, then remove this suffix before
			// checking
			// validity of the value (a suffix of "[]" in the type is used to
			// indicate an array type, only want to check validity of the type
			// name here, not of the type name plus the suffix)
			String strVal = new String((String) value);
			if (feature.getName().equals("type") && strVal.endsWith("[]"))
				strVal = strVal.substring(0, strVal.lastIndexOf("[]"));
			if (!NamedElementImpl.packageNamePattern.matcher(strVal).matches()) {
				value = new InvalidValue(
						"Value that can be converted to a element name is expected");
			}
		}
		return value;
	}

}
