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

import java.util.Collection;

import org.eclipse.pde.core.plugin.IPluginElement;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.IPluginObject;

public class AnnotationTypeContribution implements IContribution{

	public AnnotationTypeContribution(IPluginModelBase contributor,String name, String eClass,
			String namespace, String uniq, 
			boolean readOnly, IPluginElement pluginElement) {
		super();
		this.contributor = contributor;

		this.name = name;
		this.eClass = eClass;
		this.namespace = namespace;
		this.uniq = uniq;
		this.readOnly = readOnly;
		this.pluginElement = pluginElement;
	}
	
	public class Target{
		public Target(String type, String unique) {
			super();
			this.type = type;
			this.unique = unique;
		}
		
		private String type;
		private String unique;
		
		
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getUnique() {
			return unique;
		}
		public void setUnique(String unique) {
			this.unique = unique;
		}

	}
	private IPluginModelBase contributor;

	private String name;
	private String eClass;
	private String namespace;
	private String uniq;
	private Collection<Target> targets;
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
	public String getEClass() {
		return eClass;
	}
	public void setEClass(String class1) {
		eClass = class1;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public String getUniq() {
		return uniq;
	}
	public void setUniq(String uniq) {
		this.uniq = uniq;
	}
	public Collection<Target> getTargets() {
		return targets;
	}
	public void setTargets(Collection<Target> targets) {
		this.targets = targets;
	}
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	public IPluginModelBase getContributor() {
		return contributor;
	}
	public void setContributor(IPluginModelBase contributor) {
		this.contributor = contributor;
	}
	
}
