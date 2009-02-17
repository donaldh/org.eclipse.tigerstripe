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


public class ArtifactIconContribution {

	public ArtifactIconContribution(String contributor, String icon,
			String icon_new, String icon_gs, String artifactType, boolean readOnly) {
		super();
		this.contributor = contributor;
		this.icon = icon;
		this.icon_new = icon_new;
		this.icon_gs = icon_gs;
		this.artifactType = artifactType;
		this.readOnly = readOnly;
	}
	public String getContributor() {
		return contributor;
	}
	public void setContributor(String contributor) {
		this.contributor = contributor;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getIcon_new() {
		return icon_new;
	}
	public void setIcon_new(String icon_new) {
		this.icon_new = icon_new;
	}
	public String getIcon_gs() {
		return icon_gs;
	}
	public void setIcon_gs(String icon_gs) {
		this.icon_gs = icon_gs;
	}
	public String getArtifactType() {
		return artifactType;
	}
	public void setArtifactType(String artifactType) {
		this.artifactType = artifactType;
	}
	private String contributor;
	private String icon;
	private String icon_new;
	private String icon_gs;
	private String artifactType;
	private boolean readOnly;
	
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	
	
}
