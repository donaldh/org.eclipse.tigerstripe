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

public class AnnotationExplicitFileRouterContribution {

	public AnnotationExplicitFileRouterContribution(IPluginModelBase contributor,
			String nsURI, String path, String eClass, String ePackage,
			boolean readOnly) {
		super();
		this.contributor = contributor;
		this.nsURI = nsURI;
		this.path = path;
		this.eClass = eClass;
		this.ePackage = ePackage;
		this.readOnly = readOnly;
	}
	private IPluginModelBase contributor;
	private String nsURI;
	private String path;
	private String eClass;
	private String ePackage;
	private boolean readOnly;
	public IPluginModelBase getContributor() {
		return contributor;
	}
	public void setContributor(IPluginModelBase contributor) {
		this.contributor = contributor;
	}
	public String getNsURI() {
		return nsURI;
	}
	public void setNsURI(String nsURI) {
		this.nsURI = nsURI;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getEClass() {
		return eClass;
	}
	public void setEClass(String class1) {
		eClass = class1;
	}
	public String getEPackage() {
		return ePackage;
	}
	public void setEPackage(String package1) {
		ePackage = package1;
	}
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	
}
