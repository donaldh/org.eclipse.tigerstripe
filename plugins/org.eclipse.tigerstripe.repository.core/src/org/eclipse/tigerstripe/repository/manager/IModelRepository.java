/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.repository.manager;

import java.util.Collection;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

/**
 * A Model Repository is an abstraction to store Tigerstripe Artifacts.
 * 
 * @author erdillon
 * 
 */
public interface IModelRepository {

	/**
	 * Returns true if this {@link IModelRepository} is readonly
	 * 
	 * @return
	 */
	public boolean isReadonly();

	/**
	 * Stores the given EObject into this Repository
	 * 
	 * Note that once an EObject is stored into a Repository
	 * 
	 * @param obj,
	 *            the EObject to store.
	 * @param forceReplace,
	 *            replace object if already exists.
	 * @throws {@link ModelCoreException}
	 *             if object can't be stored or a duplicate exists (and
	 *             forceReplace set to false) or if this
	 *             {@link IModelRepository} is read-only
	 */
	public EObject store(EObject obj, boolean forceReplace)
			throws ModelCoreException;

	/**
	 * Returns the {@link TransactionalEditingDomain} to use for this
	 * {@link IModelRepository}, or throws an exception if this
	 * {@link IModelRepository} is read-only
	 * 
	 * @return
	 * @throws ModelCoreException
	 */
	public TransactionalEditingDomain getEditingDomain()
			throws ModelCoreException;

	public EObject getEObjectByKey(Object key) throws ModelCoreException;

	public void removeEObjectByKey(Object key) throws ModelCoreException;

	/**
	 * This method is called by the Manager as this {@link IModelRepository}
	 * gets added to a Manager
	 * 
	 * @param manager
	 */
	public void setManager(ModelManager manager);

	/**
	 * Returns true if this {@link IModelRepository} contains the given
	 * {@link EObject}.
	 * 
	 * @param obj
	 * @return
	 */
	public boolean contains(EObject obj);

	/**
	 * Returns the URI for this {@link IModelRepository}
	 * 
	 * @return
	 */
	public URI getURI();

	/**
	 * Returns all EObjects stored in this repository
	 * 
	 * @return
	 * @throws ModelCoreException
	 */
	public Collection<EObject> getAllEObjects() throws ModelCoreException;
}
