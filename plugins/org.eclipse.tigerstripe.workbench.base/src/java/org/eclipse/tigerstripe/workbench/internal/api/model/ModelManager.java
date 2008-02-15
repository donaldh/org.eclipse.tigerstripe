/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.api.model;

import java.util.Collection;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequest;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactFactory;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.model.IModelManagerTentative;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;

public class ModelManager implements IModelManagerTentative {

	private ArtifactManager manager;

	public ModelManager(ArtifactManager manager) {
		this.manager = manager;
	}

	public ArtifactManager getArtifactManager() {
		return this.manager;
	}

	@Override
	public void deleteArtifact(String fqn) throws TigerstripeException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteArtifacts(String[] fqns) throws TigerstripeException {
		// TODO Auto-generated method stub

	}

	@Override
	public IAbstractArtifact findArtifact(String fqn,
			boolean includeDependencies, boolean returnWorkingCopy)
			throws TigerstripeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Class> getSupportedArtifactQueryTypes() {
		return ArtifactFactory.INSTANCE.getSupportedArtifactQueryTypes();
	}

	@Override
	public Collection<Class> getSupportedArtifactTypes() {
		return ArtifactFactory.INSTANCE.getSupportedArtifactTypes();
	}

	@Override
	@SuppressWarnings("unchecked")
	public IAbstractArtifact makeArtifact(Class artifactType)
			throws TigerstripeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public IAbstractArtifact makeArtifact(Class artifactType,
			IAbstractArtifact template) throws TigerstripeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IAbstractArtifact> queryArtifacts(IArtifactQuery query,
			boolean returnWorkingCopies) throws TigerstripeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refresh(boolean includeDependencies) {
		// TODO Auto-generated method stub

	}

	@Override
	public void storeArtifact(IAbstractArtifact artifact)
			throws TigerstripeException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateModel(IModelChangeRequest request)
			throws TigerstripeException {
		// TODO Auto-generated method stub

	}

}
