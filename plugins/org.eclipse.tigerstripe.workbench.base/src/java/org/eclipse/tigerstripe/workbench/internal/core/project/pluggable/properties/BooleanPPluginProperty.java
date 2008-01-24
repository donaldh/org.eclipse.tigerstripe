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

import org.eclipse.tigerstripe.workbench.plugins.IBooleanPluginProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * A simple String Property
 * 
 * @author Eric Dillon
 * 
 */
public class BooleanPPluginProperty extends BasePPluginProperty implements
		IBooleanPluginProperty {

	private final static Boolean DEFAULT_BOOLEAN = false;

	public BooleanPPluginProperty() {
		setDefaultValue(DEFAULT_BOOLEAN);
	}

	public String getType() {
		return IBooleanPluginProperty.class.getCanonicalName();
	}

	public final static String LABEL = "Boolean Property";

	public String getLabel() {
		return LABEL;
	}

	public Boolean getDefaultBoolean() {
		return (Boolean) getDefaultValue();
	}

	public void setDefaultBoolean(Boolean value) {
		setDefaultValue(value);
	}

	@Override
	public void buildBodyFromNode(Node node) {
		if (node.hasChildNodes()) {
			setDefaultBoolean(Boolean.valueOf(node.getFirstChild()
					.getNodeValue()));
		}
	}

	@Override
	public Node getBodyAsNode(Document document) {
		return document.createTextNode(getDefaultBoolean().toString());
	}

	public Object deSerialize(String sValue) {
		return Boolean.valueOf(sValue);
	}

	public String serialize(Object value) {
		return ((Boolean) value).toString();
	}

}
