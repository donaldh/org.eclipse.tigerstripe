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
package org.eclipse.tigerstripe.api.impl.updater.request;

import org.eclipse.tigerstripe.api.artifacts.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.artifacts.updater.request.IArtifactDeleteRequest;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.model.artifacts.IArtifact;
import org.eclipse.tigerstripe.api.impl.updater.BaseModelChangeRequest;

public class ArtifactDeleteRequest extends BaseModelChangeRequest implements
		IArtifactDeleteRequest {

	private String artifactName;

	private String packageName;

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

	public String getFullyQualifiedName() {
		if (packageName == null || packageName.length() == 0)
			return artifactName;
		else
			return packageName + "." + artifactName;
	}

	@Override
	public boolean canExecute(IArtifactManagerSession mgrSession) {
		IArtifact art = mgrSession.getIArtifactByFullyQualifiedName(
				getFullyQualifiedName(), false);
		return art != null;
	}

	@Override
	public void execute(IArtifactManagerSession mgrSession)
			throws TigerstripeException {
		String fqn = getArtifactName();
		if (getArtifactPackage() != null && getArtifactPackage().length() != 0) {
			fqn = getArtifactPackage() + "." + getArtifactName();
		}
		IAbstractArtifact artifact = mgrSession
				.getArtifactByFullyQualifiedName(fqn);
		mgrSession.removeArtifact(artifact);
	}

}
