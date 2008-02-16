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

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.metamodel.IPackage;
import org.eclipse.tigerstripe.workbench.TigerstripeException;

public interface IModelRepository {

	/**
	 * Returns true if this repository is readonly. If true all calls to
	 * {@link #store(IAbstractArtifact, boolean)} will throw a
	 * {@link TigerstripeException};
	 * 
	 * @return
	 */
	public boolean isReadonly();

	/**
	 * Store the given artifact into this repository.
	 * 
	 * 
	 * @param workingCopy
	 * @param force
	 * @return
	 * @throws TigerstripeException
	 */
	public IAbstractArtifact store(IAbstractArtifact workingCopy, boolean force)
			throws TigerstripeException;

	/**
	 * Returns a un-modifiable collection of all the artifacts stored in this
	 * repository
	 * 
	 * @return
	 */
	public Collection<IAbstractArtifact> getAllArtifacts();

	/**
	 * Returns the artifact identified by its fully qualified name in this
	 * repository
	 * 
	 * @param fullyQualifiedName
	 * @return The artifact for the given fully qualified name, or null if not
	 *         found.
	 */
	public IAbstractArtifact getArtifactByFullyQualifiedName(
			String fullyQualifiedName);

	/**
	 * Performs a refresh of the repository (i.e. re-load all underlying
	 * artifacts from persistent storage) recursively below the given package.
	 * 
	 * If no package is provided (null), performs a refresh from the root.
	 * 
	 * @param fromPackage
	 */
	public void refresh(IPackage fromPackage);

	public TransactionalEditingDomain getEditingDomain();

}
