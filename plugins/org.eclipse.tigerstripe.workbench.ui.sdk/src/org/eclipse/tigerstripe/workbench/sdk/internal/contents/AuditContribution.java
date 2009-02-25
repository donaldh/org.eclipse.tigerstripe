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

import org.eclipse.pde.core.plugin.IPluginModelBase;


public class AuditContribution {
	public AuditContribution(IPluginModelBase contributor,String name, String auditorClass, boolean readOnly) {
		super();
		this.name = name;					// optional
		this.auditorClass = auditorClass;
		this.contributor = contributor;
		this.readOnly = readOnly;
	}
	
	private String name;
	private String auditorClass;
	private IPluginModelBase contributor;
	private boolean readOnly;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuditorClass() {
		return auditorClass;
	}
	public void setAuditorClass(String auditorClass) {
		this.auditorClass = auditorClass;
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
