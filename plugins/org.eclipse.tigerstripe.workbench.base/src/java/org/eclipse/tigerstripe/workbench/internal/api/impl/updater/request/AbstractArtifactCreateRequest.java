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
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjArtifactSpecifics;

public abstract class AbstractArtifactCreateRequest extends BaseModelChangeRequest {

	protected abstract IAbstractArtifact provideArtifact(IArtifactManagerSession mgrSession);
	protected abstract String getFullyQualifiedName();
	
	@Override
	public void execute(IArtifactManagerSession mgrSession)
			throws TigerstripeException {
		super.execute(mgrSession);
		IAbstractArtifact artifact = provideArtifact(mgrSession);

		applyDefaults(artifact, mgrSession);
		artifact.doSave(new NullProgressMonitor());
		mgrSession.addArtifact(artifact);
		
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
