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
package org.eclipse.tigerstripe.core.profile.stereotype;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.tigerstripe.api.profile.stereotype.IStereotypeScopeDetails;

public class StereotypeScopeDetails implements IStereotypeScopeDetails {

	private ArrayList<String> artifactLevelTypes = new ArrayList<String>();

	private boolean isArgumentLevel;
	private boolean isAttributeLevel;
	private boolean isMethodLevel;
	private boolean isLabelLevel;

	public void setArgumentLevel(boolean isArgumentLevel) {
		this.isArgumentLevel = isArgumentLevel;
	}

	public void setArtifactLevelTypes(String[] types) {
		artifactLevelTypes.clear();
		if (types != null)
			artifactLevelTypes.addAll(Arrays.asList(types));
	}

	public void setAttributeLevel(boolean isAttributeLevel) {
		this.isAttributeLevel = isAttributeLevel;
	}

	public void setLabelLevel(boolean isLabelLevel) {
		this.isLabelLevel = isLabelLevel;
	}

	public void setMethodLevel(boolean isMethodLevel) {
		this.isMethodLevel = isMethodLevel;
	}

	public String[] getArtifactLevelTypes() {
		return this.artifactLevelTypes.toArray(new String[artifactLevelTypes
				.size()]);
	}

	public void addArtifactLevelType(String type) {
		if (!artifactLevelTypes.contains(type))
			this.artifactLevelTypes.add(type);
	}

	public void removeArtifactLevelType(String type) {
		if (artifactLevelTypes.contains(type))
			this.artifactLevelTypes.remove(type);
	}

	public boolean isArgumentLevel() {
		return isArgumentLevel;
	}

	public boolean isAttributeLevel() {
		return isAttributeLevel;
	}

	public boolean isLabelLevel() {
		return isLabelLevel;
	}

	public boolean isMethodLevel() {
		return isMethodLevel;
	}

}
