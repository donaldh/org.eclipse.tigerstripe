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
package org.eclipse.tigerstripe.workbench.internal.modelManager;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.IModelRepository;

public abstract class ModelRepository implements IModelRepository {

	private URI repositoryURI = null;
	private ModelManager manager = null;
	private TransactionalEditingDomain editingDomain = null;

	public ModelRepository(URI repositoryURI, ModelManager manager) {
		this.repositoryURI = repositoryURI;
		this.manager = manager;
		ResourceSet resourceSet = new ResourceSetImpl();
		this.editingDomain = TigerstripeTxFactory.INSTANCE
				.createEditingDomain(resourceSet);
	}

	public TransactionalEditingDomain getEditingDomain() {
		return this.editingDomain;
	}

	protected ModelManager getManager() {
		return this.manager;
	}

	public URI getRepositoryURI() {
		return this.repositoryURI;
	}

	public ResourceSet getResourceSet() {
		return editingDomain.getResourceSet();
	}

	protected abstract void loadResourceSet() throws TigerstripeException;

	public abstract IAbstractArtifact store(IAbstractArtifact workingCopy,
			boolean force) throws TigerstripeException;

	public boolean isReadonly() {
		return false;
	}
}
