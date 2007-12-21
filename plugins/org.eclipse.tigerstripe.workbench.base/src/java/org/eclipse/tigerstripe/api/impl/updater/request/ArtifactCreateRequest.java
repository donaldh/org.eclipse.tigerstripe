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
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IOssjArtifactSpecifics;
import org.eclipse.tigerstripe.api.artifacts.updater.request.IArtifactCreateRequest;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.model.artifacts.IArtifact;
import org.eclipse.tigerstripe.api.impl.updater.BaseModelChangeRequest;
import org.eclipse.tigerstripe.core.util.TigerstripeNullProgressMonitor;

public class ArtifactCreateRequest extends BaseModelChangeRequest implements
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
		IArtifact art = mgrSession.getIArtifactByFullyQualifiedName(
				getFullyQualifiedName(), false);
		return art == null;
	}

	@Override
	public void execute(IArtifactManagerSession mgrSession)
			throws TigerstripeException {
		IAbstractArtifact artifact = mgrSession.makeArtifact(getArtifactType());
		artifact.setFullyQualifiedName(getFullyQualifiedName());

		applyDefaults(artifact, mgrSession);
		mgrSession.addArtifact(artifact);
		artifact.doSave(new TigerstripeNullProgressMonitor());
	}

	// TODO: this should really live in the mgrSession itself
	protected void applyDefaults(IAbstractArtifact artifact,
			IArtifactManagerSession mgrSession) {
		if (artifact.getIStandardSpecifics() instanceof IOssjArtifactSpecifics) {
			((IOssjArtifactSpecifics) artifact.getIStandardSpecifics())
					.applyDefaults();
		}
	}
}
