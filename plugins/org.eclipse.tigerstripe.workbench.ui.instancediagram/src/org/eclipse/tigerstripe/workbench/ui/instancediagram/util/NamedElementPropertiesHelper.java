/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.instancediagram.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.DiagramProperty;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramFactory;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.NamedElement;

public class NamedElementPropertiesHelper {

	// Association properties / values
	public final static String ASSOC_DETAILS = "assoc.details";

	public final static String ASSOC_SHOW_ALL = "assoc.show.all";

	public final static String ASSOC_SHOW_NAME_ENDSNAMES = "assoc.show.name-endsNames";

	public final static String ASSOC_SHOW_NAME_ORDER = "assoc.show.name-order";

	public final static String ASSOC_SHOW_NONE = "assoc.show.none";

	private final NamedElement namedElement;

	public final static String[] properties = { ASSOC_DETAILS };

	public final static String[] propertiesDefaults = { ASSOC_SHOW_ALL };

	public NamedElementPropertiesHelper(NamedElement namedElement) {
		this.namedElement = namedElement;
	}

	private boolean isValidProperty(String name) {
		return Arrays.asList(properties).contains(name);
	}

	private String defaultValue(String name) {
		if (isValidProperty(name)) {
			int i = 0;
			for (String prop : properties) {
				if (name.equals(prop))
					return propertiesDefaults[i];
				i++;
			}
		}
		throw new IllegalArgumentException("Unknown Artifact property: " + name);
	}

	public String getProperty(String propertyKey) {
		Assert.isNotNull(propertyKey);
		List<?> propertyList = namedElement.getProperties();
		for (Iterator<?> iter = propertyList.iterator(); iter.hasNext();) {
			DiagramProperty property = (DiagramProperty) iter.next();
			if (propertyKey.equals(property.getName()))
				return property.getValue();
		}
		return defaultValue(propertyKey);
	}

	public DiagramProperty setProperty(String propertyKey, String value) {
		Assert.isNotNull(propertyKey);
		Assert.isNotNull(value);
		List propertyList = namedElement.getProperties();
		for (Iterator iter = propertyList.iterator(); iter.hasNext();) {
			DiagramProperty property = (DiagramProperty) iter.next();
			if (propertyKey.equals(property.getName())) {
				// we've found the existing key, simply update the value
				property.setValue(value);
				return property;
			}
		}

		// There was no property for this key, creating it now
		DiagramProperty newProp = InstancediagramFactory.eINSTANCE
				.createDiagramProperty();
		newProp.setName(propertyKey);
		newProp.setValue(value);
		propertyList.add(newProp);
		return newProp;
	}
}
