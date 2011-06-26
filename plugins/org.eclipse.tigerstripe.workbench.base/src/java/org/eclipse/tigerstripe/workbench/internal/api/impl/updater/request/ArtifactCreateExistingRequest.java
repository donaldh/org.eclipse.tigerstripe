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

import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactCreateExistingRequest;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;

public class ArtifactCreateExistingRequest extends AbstractArtifactCreateRequest implements
		IArtifactCreateExistingRequest {

	private IAbstractArtifact artifact;

	public void setArtifact(IAbstractArtifact artifact) {
		this.artifact = artifact;
	}

	public IAbstractArtifact getArtifact() {
		return artifact;
	}

	@Override
	protected IAbstractArtifact provideArtifact(
			IArtifactManagerSession mgrSession) {
		return artifact;
	}

	@Override
	protected String getFullyQualifiedName() {
		return artifact.getFullyQualifiedName();
	}
}
