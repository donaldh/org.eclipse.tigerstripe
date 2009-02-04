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

import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.BaseModelChangeRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactDeleteRequest;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class ArtifactDeleteRequest extends BaseModelChangeRequest implements
		IArtifactDeleteRequest {

	private String artifactName;
	private URI artifactURI;
	private String packageName;
	private ITigerstripeModelProject project;
	private String artifactSimpleType;

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
		if (!super.canExecute(mgrSession)) {
			return false;
		}
		IAbstractArtifact art = mgrSession.getArtifactByFullyQualifiedName(
				getFullyQualifiedName(), false);
		ITigerstripeModelProject project = art.getTigerstripeProject();
		if (project != null){
			// We are NOT in a module so can be updated
			return true;
		}
		return art != null;
	}

	@Override
	public void execute(IArtifactManagerSession mgrSession)
			throws TigerstripeException {
		super.execute(mgrSession);
		String fqn = getArtifactName();
		if (getArtifactPackage() != null && getArtifactPackage().length() != 0) {
			fqn = getArtifactPackage() + "." + getArtifactName();
		}
		IAbstractArtifact artifact = mgrSession
				.getArtifactByFullyQualifiedName(fqn);
		project = artifact.getProject();
		artifactURI = (URI) artifact.getAdapter(URI.class);
		artifactSimpleType = artifact.getClass().getSimpleName();
		mgrSession.removeArtifact(artifact);
	}

	@Override
	public IModelChangeDelta getCorrespondingDelta() {
		// A ModelChangeDelta is pushed by the ArtifactManagerSession so this one 
		// would be redundant...
		return null;
//		ModelChangeDelta delta = new ModelChangeDelta(IModelChangeDelta.REMOVE);
//		delta.setOldValue(artifactURI);
//		delta.setProject(project);
//		delta.setFeature(artifactSimpleType);
//		delta.setSource(this);
//		return delta;
	}

}
