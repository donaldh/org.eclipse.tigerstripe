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
import java.util.List;

import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * A IModelManager is the holder of the model for a
 * {@link ITigerstripeModelProject}. There is one single IModelManager per
 * {@link ITigerstripeModelProject}.
 * 
 * A IModelManager provides an logical abstraction and some indexing support for
 * a number of {@link IModelRepository} which are used in a "classpath fashion"
 * to resolve any Artifact in the model.
 * 
 * Each IModelManager may have multiple local repositories, but will always have
 * a "defaultRepository" which must be a writeable repository (this corresponds
 * to the old src/ directory by default). It is the first repository on the list
 * of repositories for an IModelManager.
 * 
 * As IModelRepositories are added/removed to a IModelManager, internal indexes
 * are updated. These indexes are only there to speed up lookups and queries
 * that can be run against a IModelManager.
 * 
 * @author erdillon
 * 
 */
public interface IModelManager {

	public IModelRepository getDefaultRepository();

	/**
	 * Returns an unmodifiable list of all the repository in the order they were
	 * registered
	 * 
	 * @return
	 */
	public List<IModelRepository> getRepositories();

	/**
	 * Sets the repositories for this IModelManager
	 * 
	 * NOTE: the first repository will be considered the default repository and
	 * must be a valid writeable IModelRepository
	 * 
	 * @param repositories
	 * @throws TigerstripeException
	 */
	public void setRepositories(IModelRepository[] repositories)
			throws TigerstripeException;

	public IAbstractArtifact[] findAllArtifacts(String fullyQualifiedName)
			throws TigerstripeException;

	public IAbstractArtifact[] getAllArtifacts(boolean includeDependencies)
			throws TigerstripeException;
}
