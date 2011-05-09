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
package org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeScopeDetails;

public class StereotypeScopeDetails implements IStereotypeScopeDetails {

	private ArrayList<String> artifactLevelTypes = new ArrayList<String>();

	private boolean isArgumentLevel;
	private boolean isAttributeLevel;
	private boolean isMethodLevel;
	private boolean isLiteralLevel;
	private boolean isAssociationEndLevel;
	private boolean isReturnLevel;

	public void setAssociationEndLevel(boolean isAssociationEndLevel) {
		this.isAssociationEndLevel = isAssociationEndLevel;
	}

	public boolean isAssociationEndLevel() {
		return isAssociationEndLevel;
	}

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

	public void setLiteralLevel(boolean isLiteralLevel) {
		this.isLiteralLevel = isLiteralLevel;
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

	public boolean isLiteralLevel() {
		return isLiteralLevel;
	}

	public boolean isMethodLevel() {
		return isMethodLevel;
	}

	public void setReturnLevel(boolean isReturnLevel) {
		this.isReturnLevel = isReturnLevel;
	}

	public boolean isReturnLevel() {
		return isReturnLevel;
	}
}
