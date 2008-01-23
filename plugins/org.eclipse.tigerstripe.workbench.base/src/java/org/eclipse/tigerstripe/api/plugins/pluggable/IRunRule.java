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

import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.core.plugin.pluggable.PluggablePluginRef;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Top level run rule for PluggablePlugins.
 * 
 * @author erdillon
 * 
 */
public interface IRunRule {

	public String getName();

	public void setName(String name);

	public String getDescription();

	public void setDescription(String description);

	public String getLabel();

	public String getType();

	public abstract Node getBodyAsNode(Document document);

	public abstract void buildBodyFromNode(Node node);

	public void setProject(IPluggablePluginProject project);

	public IPluggablePluginProject getProject();

	public void trigger(PluggablePluginRef pluginRef, IPluginRuleExecutor exec)
			throws TigerstripeException;

	public boolean isEnabled();

	public void setEnabled(boolean enabled);

	public String isEnabledStr();

	public void setEnabledStr(String enabledStr);

}
