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


public class NamingContribution {
	public NamingContribution(String contributor,String name, String namingClass, boolean readOnly) {
		super();
		this.name = name;					// optional
		this.namingClass = namingClass;
		this.contributor = contributor;
		this.readOnly = readOnly;
	}
	
	private String name;
	private String namingClass;
	private String contributor;
	private boolean readOnly;
	
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
	public String getContributor() {
		return contributor;
	}
	public void setContributor(String contributor) {
		this.contributor = contributor;
	}
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}


}
