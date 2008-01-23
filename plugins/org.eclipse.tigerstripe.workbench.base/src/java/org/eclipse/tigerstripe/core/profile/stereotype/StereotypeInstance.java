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

import java.util.HashMap;

import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotypeInstance;

public class StereotypeInstance implements IStereotypeInstance {

	private IStereotype characterizingStereotype;

	private HashMap<IStereotypeAttribute, String> attributeValueMap = new HashMap<IStereotypeAttribute, String>();

	private HashMap<IStereotypeAttribute, String[]> arrayAttributeValueMap = new HashMap<IStereotypeAttribute, String[]>();

	public StereotypeInstance(IStereotype characterizingStereotype) {
		this.characterizingStereotype = characterizingStereotype;
	}

	public String getName() {
		return characterizingStereotype.getName();
	}


	public IStereotype getCharacterizingIStereotype() {
		return this.characterizingStereotype;
	}

	public String getAttributeValue(IStereotypeAttribute attribute)
			throws TigerstripeException {

		if (!characterizingStereotype.isValidAttribute(attribute))
			throw new TigerstripeException("Attribute '" + attribute.getName()
					+ "' is invalid.");

		return this.attributeValueMap.get(attribute);
	}

	public String getStringifiedAttributeValues(IStereotypeAttribute attribute)
			throws TigerstripeException {
		if (!attribute.isArray())
			return getAttributeValue(attribute);
		else {
			String result = "";
			for (String value : getAttributeValues(attribute)) {
				result = result + value + "\005";
			}
			return result;
		}
	}

	public String[] getAttributeValues(IStereotypeAttribute attribute)
			throws TigerstripeException {

		if (!characterizingStereotype.isValidAttribute(attribute))
			throw new TigerstripeException("Attribute '" + attribute.getName()
					+ "' is invalid.");

		if (!attribute.isArray())
			throw new TigerstripeException("Attribute '" + attribute.getName()
					+ "' is not an array attribute.");

		String[] result = this.arrayAttributeValueMap.get(attribute);
		if (result == null) {
			result = new String[0];
		}
		return result;
	}

	public String getAttributeValue(String attributeName)
			throws TigerstripeException {
		if (attributeName != null) {
			for (IStereotypeAttribute attribute : characterizingStereotype
					.getIAttributes()) {
				if (attributeName.equals(attribute.getName()))
					return getAttributeValue(attribute);
			}
		}
		throw new TigerstripeException("Unknown attribute '" + attributeName
				+ "'.");
	}

	public String[] getAttributeValues(String attributeName)
			throws TigerstripeException {
		if (attributeName != null) {
			for (IStereotypeAttribute attribute : characterizingStereotype
					.getIAttributes()) {
				if (attributeName.equals(attribute.getName()))
					return getAttributeValues(attribute);
			}
		}
		throw new TigerstripeException("Unknown attribute '" + attributeName
				+ "'.");
	}

	public void setAttributeValue(IStereotypeAttribute attribute, String value)
			throws TigerstripeException {
		if (!characterizingStereotype.isValidAttribute(attribute))
			throw new TigerstripeException("Attribute '" + attribute.getName()
					+ "' is invalid.");

		this.attributeValueMap.put(attribute, value);
	}

	public void setAttributeValuesFromStringified(
			IStereotypeAttribute attribute, String stringifiedValue)
			throws TigerstripeException {
		if (!characterizingStereotype.isValidAttribute(attribute))
			throw new TigerstripeException("Attribute '" + attribute.getName()
					+ "' is invalid.");

		if (stringifiedValue != null && stringifiedValue.length() != 0) {
			String[] split = stringifiedValue.split("\005");
			this.arrayAttributeValueMap.put(attribute, split);
		}
	}

	public void setAttributeValues(IStereotypeAttribute attribute,
			String values[]) throws TigerstripeException {
		if (!characterizingStereotype.isValidAttribute(attribute))
			throw new TigerstripeException("Attribute '" + attribute.getName()
					+ "' is invalid.");

		if (!attribute.isArray())
			throw new TigerstripeException("Attribute '" + attribute.getName()
					+ "' is not an array Attribute.");

		this.arrayAttributeValueMap.put(attribute, values);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		IStereotypeInstance copy = new StereotypeInstance(
				getCharacterizingIStereotype());
		for (IStereotypeAttribute attr : getCharacterizingIStereotype()
				.getAttributes()) {
			try {
				if (attr.isArray()) {

					String[] orig = this.getAttributeValues(attr);
					String[] dup = new String[orig.length];
					int i = 0;
					for (String str : orig) {
						dup[i++] = str;
					}
					copy.setAttributeValues(attr, dup);
				} else {
					copy.setAttributeValue(attr, this.getAttributeValue(attr));
				}
			} catch (TigerstripeException e) {
				// ignore that attr
			}
		}
		return copy;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof StereotypeInstance) {
			StereotypeInstance other = (StereotypeInstance) obj;
			if (other.getName() != null && other.getName().equals(getName()))
				return true;
		}
		return false;
	}

}
