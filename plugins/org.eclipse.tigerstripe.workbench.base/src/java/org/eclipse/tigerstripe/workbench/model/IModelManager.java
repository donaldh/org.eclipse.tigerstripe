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
package org.eclipse.tigerstripe.workbench.model;

import java.util.Collection;

import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.modelManager.IModelRepository;

public interface IModelManager {

	public IModelRepository getDefaultRepository();

	public Collection<IModelRepository> getRepositories();

	public void addRepository(IModelRepository repository)
			throws TigerstripeException;

	public void removeRepository(IModelRepository repository)
			throws TigerstripeException;

	public IAbstractArtifact[] findAllArtifacts(String fullyQualifiedName)
			throws TigerstripeException;

	public IAbstractArtifact[] getAllArtifacts(boolean includeDependencies)
			throws TigerstripeException;
}
