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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.util;

import java.util.Arrays;
import java.util.Iterator;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.util.EList;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.DiagramProperty;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorFactory;

public class NamedElementPropertiesHelper {

	public final static String UNDEFINED = "UNDEF";

	// ==============================
	// Association properties / values
	public final static String ASSOC_DETAILS = "assoc.details";

	public final static String ASSOC_SHOW_NONE = "assoc.show.none";

	public final static String ASSOC_SHOW_NAME = "assoc.show.name";

	public final static String ASSOC_SHOW_AENDNAME = "assoc.show.aEnd-name";

	public final static String ASSOC_SHOW_NAMEZEND = "assoc.show.name-zEnd";

	public final static String ASSOC_SHOW_ALL = "assoc.show.all";

	// ==============================
	// Bug 933
	public final static String ARTIFACT_HIDE_EXTENDS = "artifact.hide.extends";

	private NamedElement namedElement;

	public final static String[] properties = { ASSOC_DETAILS,
			ARTIFACT_HIDE_EXTENDS };

	public final static String[] propertiesDefaults = { ASSOC_SHOW_ALL, "false" };

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
		EList propertyList = namedElement.getProperties();
		for (Iterator iter = propertyList.iterator(); iter.hasNext();) {
			DiagramProperty property = (DiagramProperty) iter.next();
			if (propertyKey.equals(property.getName()))
				return property.getValue();
		}
		return defaultValue(propertyKey);
	}

	public DiagramProperty setProperty(String propertyKey, String value) {
		Assert.isNotNull(propertyKey);
		Assert.isNotNull(value);
		EList propertyList = namedElement.getProperties();
		for (Iterator iter = propertyList.iterator(); iter.hasNext();) {
			DiagramProperty property = (DiagramProperty) iter.next();
			if (propertyKey.equals(property.getName())) {
				// we've found the existing key, simply update the value
				property.setValue(value);
				return property;
			}
		}

		// There was no property for this key, creating it now
		DiagramProperty newProp = VisualeditorFactory.eINSTANCE
				.createDiagramProperty();
		newProp.setName(propertyKey);
		newProp.setValue(value);
		propertyList.add(newProp);
		return newProp;
	}
}
