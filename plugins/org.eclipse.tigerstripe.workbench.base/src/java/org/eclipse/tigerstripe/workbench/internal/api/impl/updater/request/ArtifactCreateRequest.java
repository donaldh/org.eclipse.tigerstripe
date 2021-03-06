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
package org.eclipse.tigerstripe.workbench.internal.api.impl.updater.request;

import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactCreateRequest;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;

public class ArtifactCreateRequest extends AbstractArtifactCreateRequest implements
		IArtifactCreateRequest {

	private String artifactName;

	private String packageName;

	private String artifactType;
	
	public void setArtifactName(String name) {
		this.artifactName = name;
	}

	public String getArtifactName() {
		return this.artifactName;
	}

	public void setArtifactPackage(String packageName) {
		this.packageName = packageName;
	}

	public String getArtifactPackage() {
		return this.packageName;
	}

	public void setArtifactType(String artifactType) {
		this.artifactType = artifactType;
	}

	public String getArtifactType() {
		return this.artifactType;
	}

	protected String getFullyQualifiedName() {
		if (packageName == null || packageName.length() == 0)
			return artifactName;
		else
			return packageName + "." + artifactName;
	}

	@Override
	public boolean canExecute(IArtifactManagerSession mgrSession) {
		super.canExecute(mgrSession);
		IAbstractArtifact art = mgrSession.getArtifactByFullyQualifiedName(
				getFullyQualifiedName(), false);
		return art == null;
	}

	@Override
	protected IAbstractArtifact provideArtifact(IArtifactManagerSession mgrSession) {
		IAbstractArtifact artifact = mgrSession.makeArtifact(getArtifactType());
		artifact.setFullyQualifiedName(getFullyQualifiedName());
		return artifact;
	}
}
