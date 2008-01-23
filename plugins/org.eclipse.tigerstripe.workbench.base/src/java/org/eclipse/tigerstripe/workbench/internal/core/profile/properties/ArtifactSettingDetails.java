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
package org.eclipse.tigerstripe.workbench.internal.core.profile.properties;

import org.eclipse.tigerstripe.workbench.TigerstripeException;

/**
 * The details for the settings of Artifact as defined in a Profile
 * 
 * @author Eric Dillon
 * @since 1.2
 * @see CoreArtifactSettingsProperty
 */
public class ArtifactSettingDetails {

	private String artifactType; // not persisted
	private boolean isEnabled = true;

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getArtifactType() {
		return artifactType;
	}

	public void setArtifactType(String artifactType) {
		this.artifactType = artifactType;
	}

	public String serializeToString() {
		return String.valueOf(isEnabled);
	}

	public void parseFromSerializedString(String serializedString)
			throws TigerstripeException {
		isEnabled = "true".equalsIgnoreCase(serializedString);
	}
}
