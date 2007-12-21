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
package org.eclipse.tigerstripe.api.plugins.pluggable;

import org.eclipse.tigerstripe.api.external.plugins.IextPluggablePluginProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Properties as they can be defined on a Pluggable Plugin
 * 
 * @author Eric Dillon
 * 
 */
public interface IPluggablePluginProperty extends IextPluggablePluginProperty {

	public void setProject(IPluggablePluginProject project);

	/**
	 * Returns the label to appear in the GUI
	 * 
	 * @return
	 */
	public String getLabel();

	public void setName(String name);

	public void setTipToolText(String text);

	public void setDefaultValue(Object value);

	public abstract Node getBodyAsNode(Document document);

	public abstract void buildBodyFromNode(Node node);

	/**
	 * Serializes the value of the property once being used at runtime
	 * 
	 * @param value
	 * @return
	 */
	public String serialize(Object value);

	/**
	 * De-Serialize the string into a value for this property
	 * 
	 * @param sValue
	 * @return
	 */
	public Object deSerialize(String sValue);

}
