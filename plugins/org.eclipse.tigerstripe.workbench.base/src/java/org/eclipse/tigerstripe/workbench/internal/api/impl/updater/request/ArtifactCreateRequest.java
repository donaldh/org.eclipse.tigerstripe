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

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.impl.updater.BaseModelChangeRequest;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactCreateRequest;
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjArtifactSpecifics;

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
		super.canExecute(mgrSession);
		IAbstractArtifact art = mgrSession.getArtifactByFullyQualifiedName(
				getFullyQualifiedName(), false);
		return art == null;
	}

	@Override
	public void execute(IArtifactManagerSession mgrSession)
			throws TigerstripeException {
		super.execute(mgrSession);
		IAbstractArtifact artifact = mgrSession.makeArtifact(getArtifactType());
		artifact.setFullyQualifiedName(getFullyQualifiedName());

		applyDefaults(artifact, mgrSession);
		mgrSession.addArtifact(artifact);
		artifact.doSave(new NullProgressMonitor());
	}

	// TODO: this should really live in the mgrSession itself
	protected void applyDefaults(IAbstractArtifact artifact,
			IArtifactManagerSession mgrSession) {
		if (artifact.getIStandardSpecifics() instanceof IOssjArtifactSpecifics) {
			((IOssjArtifactSpecifics) artifact.getIStandardSpecifics())
					.applyDefaults();
		}
	}

	@Override
	public IModelChangeDelta getCorrespondingDelta() {
		ModelChangeDelta delta = new ModelChangeDelta(IModelChangeDelta.ADD);
		try {
			IModelComponent comp = getMgrSession()
					.getArtifactByFullyQualifiedName(getFullyQualifiedName());
			if (comp != null) {
				delta.setNewValue(comp.getAdapter(URI.class));
				delta.setFeature(comp.getClass().getSimpleName());
				delta.setProject(comp.getProject());
				delta.setSource(this);
			}
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
			return ModelChangeDelta.UNKNOWNDELTA;
		}
		return delta;
	}

}
