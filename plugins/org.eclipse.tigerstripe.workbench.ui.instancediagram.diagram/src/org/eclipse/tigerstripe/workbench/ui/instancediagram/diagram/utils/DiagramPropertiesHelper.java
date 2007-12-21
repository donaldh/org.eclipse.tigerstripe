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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.utils;

import java.util.Arrays;
import java.util.List;

import org.eclipse.tigerstripe.workbench.ui.instancediagram.DiagramProperty;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramFactory;

/**
 * Helper class to manage Class Diagram level properties
 * 
 * @author Eric Dillon
 * 
 */
public class DiagramPropertiesHelper {

	public final static String HIDEARTIFACTPACKAGES = "hideArtifactPackages";

	public final static String[] properties = { HIDEARTIFACTPACKAGES };

	public final static String[] propertiesDefaults = { "false" };

	private InstanceMap map;

	public DiagramPropertiesHelper(InstanceMap map) {
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
				prop = InstancediagramFactory.eINSTANCE.createDiagramProperty();
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
