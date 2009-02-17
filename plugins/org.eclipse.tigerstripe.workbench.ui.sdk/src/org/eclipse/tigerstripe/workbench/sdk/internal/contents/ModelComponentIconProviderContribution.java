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


public class ModelComponentIconProviderContribution {

	public ModelComponentIconProviderContribution(String contributor,
			String artifactType, String provider, boolean readOnly) {
		super();
		this.contributor = contributor;
		this.artifactType = artifactType;
		this.provider = provider;
		this.readOnly = readOnly;
	}
	private String contributor;
	private String artifactType;
	private String provider;
	private boolean readOnly;
	
	
	public String getContributor() {
		return contributor;
	}
	public void setContributor(String contributor) {
		this.contributor = contributor;
	}
	public String getArtifactType() {
		return artifactType;
	}
	public void setArtifactType(String artifactType) {
		this.artifactType = artifactType;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	
	
}
