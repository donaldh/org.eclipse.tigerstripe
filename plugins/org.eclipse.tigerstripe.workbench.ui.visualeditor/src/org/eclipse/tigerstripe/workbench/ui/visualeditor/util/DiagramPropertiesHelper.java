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
import java.util.List;

import org.eclipse.tigerstripe.workbench.ui.visualeditor.DiagramProperty;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorFactory;

/**
 * Helper class to manage Class Diagram level properties
 * 
 * @author Eric Dillon
 * 
 */
public class DiagramPropertiesHelper {

	public final static String HIDEPACKAGESINCOMPARTMENTS = "hidePackagesInCompartments";

	public final static String HIDEDEFAULTVALUES = "hideDefaultValues";

	public final static String HIDESTEREOTYPES = "hideStereotypes";

	public final static String HIDEARTIFACTPACKAGES = "hideArtifactPackages";

	public final static String[] properties = { HIDEPACKAGESINCOMPARTMENTS,
			HIDEDEFAULTVALUES, HIDESTEREOTYPES, HIDEARTIFACTPACKAGES };

	public final static String[] propertiesDefaults = { "true", "false",
			"false", "false" };

	private Map map;

	public DiagramPropertiesHelper(Map map) {
		this.map = map;
	}

	public DiagramProperty getDiagramProperty(String name) {
		if (isValidProperty(name)) {
			for (DiagramProperty prop : (List<DiagramProperty>) map
					.getProperties()) {
				if (name.equals(prop.getName()))
					return prop;
			}
			return null;
		}
		throw new IllegalArgumentException("Unknown Diagram property: " + name);
	}

	public String getPropertyValue(String name) {
		if (isValidProperty(name)) {
			DiagramProperty prop = getDiagramProperty(name);
			if (prop != null)
				return prop.getValue();
			else
				return defaultValue(name);
		}
		throw new IllegalArgumentException("Unknown Diagram property: " + name);
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
		throw new IllegalArgumentException("Unknown Diagram property: " + name);
	}

	public void resetProperty(String name) {
		setProperty(name, defaultValue(name));
	}

	public void setProperty(String name, String value) {
		if (isValidProperty(name)) {
			DiagramProperty prop = getDiagramProperty(name);
			if (prop == null) {
				prop = VisualeditorFactory.eINSTANCE.createDiagramProperty();
				map.getProperties().add(prop);
				prop.setName(name);
				prop.setValue(value);
			} else {
				prop.setValue(value);
			}
			return;
		}
		throw new IllegalArgumentException("Unknown Diagram property: " + name);
	}

}
