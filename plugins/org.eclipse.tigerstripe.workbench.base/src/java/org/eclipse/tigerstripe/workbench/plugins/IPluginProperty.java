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
package org.eclipse.tigerstripe.workbench.plugins;


import org.eclipse.tigerstripe.workbench.project.ITigerstripePluginProject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Properties as they can be defined on a Pluggable Plugin
 * 
 * @author Eric Dillon
 * 
 */
public interface IPluginProperty  {

	public void setProject(ITigerstripePluginProject project);

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

	/**
	 * Value of that property
	 * 
	 * @return
	 */
	public Object getDefaultValue();

	/**
	 * Name of that property
	 * 
	 * @return
	 */
	public String getName();

	public ITigerstripePluginProject getProject();

	/**
	 * The tiptool text that will appear in the GUI
	 * 
	 * @return
	 */
	public String getTipToolText();

	public String getType();

}
