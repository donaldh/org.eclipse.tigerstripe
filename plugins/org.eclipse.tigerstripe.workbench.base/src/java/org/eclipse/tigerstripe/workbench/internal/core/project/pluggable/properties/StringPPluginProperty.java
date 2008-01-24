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
package org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.properties;

import org.eclipse.tigerstripe.workbench.plugins.IStringPluginProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * A simple String Property
 * 
 * @author Eric Dillon
 * 
 */
public class StringPPluginProperty extends BasePPluginProperty implements
		IStringPluginProperty {

	private final static String DEFAULT_STRING = "";

	public StringPPluginProperty() {
		setDefaultValue(DEFAULT_STRING);
	}

	public String getType() {
		return IStringPluginProperty.class.getCanonicalName();
	}

	public final static String LABEL = "String Property";

	public String getLabel() {
		return LABEL;
	}

	public String getDefaultString() {
		return (String) getDefaultValue();
	}

	public void setDefaultString(String value) {
		setDefaultValue(value);
	}

	@Override
	public void buildBodyFromNode(Node node) {
		if (node.hasChildNodes()) {
			setDefaultString(node.getFirstChild().getNodeValue());
		}
	}

	@Override
	public Node getBodyAsNode(Document document) {
		return document.createTextNode(getDefaultString());
	}

	public Object deSerialize(String sValue) {
		return sValue;
	}

	public String serialize(Object value) {
		return (String) value;
	}

}
