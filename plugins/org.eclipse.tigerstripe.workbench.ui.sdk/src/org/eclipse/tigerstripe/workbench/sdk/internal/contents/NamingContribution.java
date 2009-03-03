/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.)
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.sdk.internal.contents;

import org.eclipse.pde.core.plugin.IPluginElement;
import org.eclipse.pde.core.plugin.IPluginModelBase;


public class NamingContribution implements IContribution{
	public NamingContribution(IPluginModelBase contributor,String name, String namingClass, boolean readOnly, IPluginElement pluginElement) {
		super();
		this.name = name;					// optional
		this.namingClass = namingClass;
		this.contributor = contributor;
		this.readOnly = readOnly;
		this.pluginElement = pluginElement;
	}
	
	private String name;
	private String namingClass;
	private IPluginModelBase contributor;
	private boolean readOnly;
	private IPluginElement pluginElement;
	
	public IPluginElement getPluginElement() {
		return pluginElement;
	}
	
	public void setPluginElement(IPluginElement pluginElement) {
		this.pluginElement = pluginElement;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNamingClass() {
		return namingClass;
	}
	public void setNamingClass(String namingClass) {
		this.namingClass = namingClass;
	}
	public IPluginModelBase getContributor() {
		return contributor;
	}
	public void setContributor(IPluginModelBase contributor) {
		this.contributor = contributor;
	}
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}


}
