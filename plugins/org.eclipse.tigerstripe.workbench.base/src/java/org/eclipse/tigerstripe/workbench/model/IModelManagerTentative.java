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

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequest;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.queries.IArtifactQuery;

/**
 * The Model Manager interface provides all the functionalities to create, read,
 * update and query artifacts of a model independently of how they are
 * implemented and persisted.
 * 
 * @author erdillon@cisco.com
 * 
 */
public interface IModelManagerTentative {

	public <T extends IAbstractArtifact> Collection<Class<T>> getSupportedArtifactTypes();

	public <T extends IArtifactQuery> Collection<Class<T>> getSupportedArtifactQueryTypes();

	/**
	 * Create a new instance of the given artifact type. The returned artifact
	 * is a Working Copy that will need to be stored before it gets persisted.
	 * 
	 * @param artifactType
	 * @return
	 * @throws IllegalArtifactException
	 */
	public <T extends IAbstractArtifact> T makeArtifact(Class<T> artifactType)
			throws TigerstripeException;

	/**
	 * Create a new instance of the given artifact type. The returned artifact
	 * is created based on the given template.
	 * 
	 * @param artifactType
	 * @param template
	 * @return
	 * @throws IllegalArtifactException
	 * @throws TigerstripeException
	 */
	public <T extends IAbstractArtifact> T makeArtifact(Class<T> artifactType,
			IAbstractArtifact template) throws TigerstripeException;

	/**
	 * Stores the given artifact as part of this model. This operation results
	 * in the persistence of the given artifact.
	 * 
	 * Note that the given artifact needs to be a Working Copy
	 * 
	 * @param artifact
	 * @throws TigerstripeException
	 */
	public void storeArtifact(IAbstractArtifact artifact)
			throws TigerstripeException;

	public Collection<IAbstractArtifact> queryArtifacts(IArtifactQuery query,
			boolean returnWorkingCopies) throws TigerstripeException;

	public IAbstractArtifact findArtifact(String fqn,
			boolean includeDependencies, boolean returnWorkingCopy)
			throws TigerstripeException;

	public void refresh(boolean includeDependencies);

	public void updateModel(IModelChangeRequest request)
			throws TigerstripeException;

	public void deleteArtifact(String fqn) throws TigerstripeException;

	public void deleteArtifacts(String[] fqns) throws TigerstripeException;
}
