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
package org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeAttribute;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;

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

	/**
	 * This constructor is used when we discover that an instance is not actually valid for 
	 * the model component on which it has been found.
	 * @param instance
	 */
	public UnresolvedStereotypeInstance(IStereotypeInstance instance) {
		this.label = instance.getCharacterizingStereotype().getName();
		if (instance.getCharacterizingStereotype() != null){
			for (IStereotypeAttribute attr : instance.getCharacterizingStereotype().getAttributes()){
				try {
					fakeAttributeMap.put(attr.getName(),instance.getAttributeValue(attr));
				} catch (TigerstripeException e) {
					// TODO Auto-generated catch block
					// Not really a big issue
				}
			}
		}

	}
	
	public IStereotype getCharacterizingStereotype() {
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

	public String getAttributeValue(IStereotypeAttribute attribute)
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

	public String[] getAttributeValues(IStereotypeAttribute attribute)
			throws TigerstripeException {
		return getAttributeValues(attribute.getName());
	}

	public String[] getAttributeValues(String attributeName)
			throws TigerstripeException {
		String stringifiedValue = getAttributeValue(attributeName);
		String[] split = stringifiedValue.split("\005");
		return split;
	}


	@Override
	public Object clone() throws CloneNotSupportedException {
		return new UnresolvedStereotypeInstance(label);
	}

	/**
	 * Use this to get the set of attributes that we found (as we cannot use the CharacterizingStereotype).
	 * Skip over the one that has the name of the stereotype
	 * 
	 * @return
	 */
	public Collection<String> getAttributes(){
		Collection<String>  attrs = new ArrayList<String>();
		for (String key : fakeAttributeMap.keySet()){
			if (!key.equals("st.name")){
				attrs.add(key);
			}
		}
		return attrs;
	}
}
