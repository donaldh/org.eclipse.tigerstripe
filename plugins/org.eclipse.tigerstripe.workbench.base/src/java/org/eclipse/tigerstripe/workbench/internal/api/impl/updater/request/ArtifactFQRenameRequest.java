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

import java.io.File;
import java.util.Collection;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.adapt.TigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactFQRenameRequest;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ModelChangeDelta;
import org.eclipse.tigerstripe.workbench.internal.core.model.Type;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IEmittedEvent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IExposedUpdateProcedure;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.IManagedEntityDetails;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact.INamedQuery;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;

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
		super.canExecute(mgrSession);
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

		// This is using the fact that this is a file. Will need to be removed
		// to attach to EMF (nokia)
		String artPath = aArt.getArtifactPath();
		String projectDir = aArt.getArtifactManager().getTSProject()
				.getBaseDir().toString();
		String fullArtPath = projectDir + File.separator + artPath;
		File f = new File(fullArtPath);


		mgrSession
				.renameArtifact(origArt, newFQName);
		origArt.doSave(new NullProgressMonitor());

		if (!f.delete()) {
			// TigerstripeRuntime.logInfoMessage("Error deleting: " +
			// f.toURI());
		}
		// Update references
		updateReferences(mgrSession, origArt, oldFQN);
	}

}
