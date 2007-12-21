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

import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.profile.stereotype.IextStereotype;
import org.eclipse.tigerstripe.api.external.profile.stereotype.IextStereotypeAttribute;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.api.profile.stereotype.IStereotypeInstance;

/**
 * An unresolved stereotype instance is a best-effort attempt to re-construct a
 * stereotype instance that is present in the artifact but has no corresponding
 * definition in the active profile (hinting that the wrong profile is active
 * and that this annotation has been create with another profile active).
 * 
 * From what can be parse, it builds its own list of attributes and populates
 * them with the corresponding values as-parsed. Of course, we can't guarantee
 * that all value are properly handled thru this guess work.
 * 
 * @author Eric Dillon
 * @since 2.2.1
 */
public class UnresolvedStereotypeInstance implements IStereotypeInstance {

	private String label;

	private HashMap<String, String> fakeAttributeMap = new HashMap<String, String>();

	public UnresolvedStereotypeInstance(String stereotypeLabel) {
		this.label = stereotypeLabel;
	}

	public IStereotype getCharacterizingIStereotype() {
		return null;
	}

	public String getName() {
		return label;
	}

	public void addAttributeAndValue(String name, String value) {
		fakeAttributeMap.put(name, value);
	}

	public void setAttributeValue(IStereotypeAttribute attribute, String value)
			throws TigerstripeException {
		throw new TigerstripeException(
				"Can't set value on unresolved annotation instance ("
						+ getName() + ").");
	}

	public void setAttributeValues(IStereotypeAttribute attribute,
			String[] values) throws TigerstripeException {
		throw new TigerstripeException(
				"Can't set values on unresolved annotation instance ("
						+ getName() + ").");
	}

	public String getAttributeValue(IextStereotypeAttribute attribute)
			throws TigerstripeException {
		String name = attribute.getName();
		if (fakeAttributeMap.containsKey(name))
			return fakeAttributeMap.get(name);
		else
			throw new TigerstripeException(
					"Unknown value on unresolved annotation instance ("
							+ getName() + ") for attribute '" + name + "'.");
	}

	public String getAttributeValue(String attributeName)
			throws TigerstripeException {
		if (fakeAttributeMap.containsKey(attributeName))
			return fakeAttributeMap.get(attributeName);
		else
			throw new TigerstripeException(
					"Unknown value on unresolved annotation instance ("
							+ getName() + ") for attribute '" + attributeName
							+ "'.");
	}

	public String[] getAttributeValues(IextStereotypeAttribute attribute)
			throws TigerstripeException {
		return getAttributeValues(attribute.getName());
	}

	public String[] getAttributeValues(String attributeName)
			throws TigerstripeException {
		String stringifiedValue = getAttributeValue(attributeName);
		String[] split = stringifiedValue.split("\005");
		return split;
	}

	public IextStereotype getCharacterizingIextStereotype() {
		return null;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new UnresolvedStereotypeInstance(label);
	}

}
