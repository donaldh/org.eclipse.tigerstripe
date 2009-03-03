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


public class ArtifactMetadataContribution implements IContribution{

	public ArtifactMetadataContribution(IPluginModelBase contributor,
			String artifactType, String userLabel, boolean hasFields,
			boolean hasLiterals, boolean hasMethods, String icon,
			String icon_new, String icon_gs, boolean readOnly, IPluginElement pluginElement) {
		super();
		this.contributor = contributor;
		this.artifactType = artifactType;
		this.userLabel = userLabel;
		this.hasFields = hasFields;
		this.hasLiterals = hasLiterals;
		this.hasMethods = hasMethods;
		this.icon = icon;
		this.icon_new = icon_new;
		this.icon_gs = icon_gs;
		this.readOnly = readOnly;
		this.pluginElement = pluginElement;
	}
	private IPluginModelBase contributor;
	private String artifactType;
	private String userLabel;
	private boolean hasFields;
	private boolean hasLiterals;
	private boolean hasMethods;
	private String icon;
	private String icon_new;
	private String icon_gs;
	private boolean readOnly;
	private IPluginElement pluginElement;
	
	public IPluginElement getPluginElement() {
		return pluginElement;
	}
	
	public void setPluginElement(IPluginElement pluginElement) {
		this.pluginElement = pluginElement;
	}

	public IPluginModelBase getContributor() {
		return contributor;
	}
	public void setContributor(IPluginModelBase contributor) {
		this.contributor = contributor;
	}
	public String getArtifactType() {
		return artifactType;
	}
	public void setArtifactType(String artifactType) {
		this.artifactType = artifactType;
	}
	public boolean isHasFields() {
		return hasFields;
	}
	public void setHasFields(boolean hasFields) {
		this.hasFields = hasFields;
	}
	public boolean isHasLiterals() {
		return hasLiterals;
	}
	public void setHasLiterals(boolean hasLiterals) {
		this.hasLiterals = hasLiterals;
	}
	public boolean isHasMethods() {
		return hasMethods;
	}
	public void setHasMethods(boolean hasMethods) {
		this.hasMethods = hasMethods;
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
	public String getUserLabel() {
		return userLabel;
	}
	public void setUserLabel(String userLabel) {
		this.userLabel = userLabel;
	}
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
}
