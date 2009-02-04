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

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactFQRenameRequest;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;

public class ArtifactFQRenameRequest extends ArtifactRenameRequest implements
		IArtifactFQRenameRequest {

	private String newFQName;

	public void setNewFQName(String newFQName) {
		this.newFQName = newFQName;
	}

	public String getNewFQName() {
		return newFQName;
	}

	@Override
	public boolean canExecute(IArtifactManagerSession mgrSession) {
		if (!super.canExecute(mgrSession)) {
			return false;
		}
		try {
			String target = newFQName;

			IAbstractArtifact newArt = mgrSession
				.getArtifactByFullyQualifiedName(target);
			return newArt == null;

		} catch (TigerstripeException t) {
			return false;
		}
	}

	@Override
	public void execute(IArtifactManagerSession mgrSession)
			throws TigerstripeException {
		super.execute(mgrSession);

		IAbstractArtifact origArt = mgrSession
				.getArtifactByFullyQualifiedName(getArtifactFQN());

		AbstractArtifact aArt = (AbstractArtifact) origArt;
		String oldFQN = aArt.getFullyQualifiedName();

		IResource res = (IResource) aArt.getAdapter(IResource.class);
		

		mgrSession
				.renameArtifact(origArt, newFQName);
		origArt.doSave(new NullProgressMonitor());

		try {
			res.delete(true, null);
		} catch (Exception e){
			BasePlugin.log(e);
		}

		// Update references
		updateReferences(mgrSession, origArt, oldFQN);
	}

}
