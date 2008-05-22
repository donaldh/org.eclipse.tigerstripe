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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

/**
 * Manages Tigerstripe Model as a union of {@link IArtifactContainer} within
 * which name resolution is provided as well as numerous indexing options and
 * facet support
 * 
 * @author erdillon
 * 
 */
public class ModelManager {

	private List<IModelRepository> repositories = new ArrayList<IModelRepository>();

	private String name;

	public ModelManager() {
		this(null);
	}

	public ModelManager(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public EObject getEObjectByKey(Object key) throws ModelCoreException {
		EObject result = null;
		for (IModelRepository repository : repositories) {
			result = repository.getEObjectByKey(key);
			if (result != null)
				break;
		}
		return result;
	}

	public EObject[] getAllEObjectByKey(Object key) throws ModelCoreException {
		List<EObject> result = new ArrayList<EObject>();
		for (IModelRepository repository : repositories) {
			EObject obj = repository.getEObjectByKey(key);
			if (obj != null)
				result.add(obj);
		}
		return result.toArray(new EObject[result.size()]);
	}

	/**
	 * Returns a TransactionalEditingDomain for the given EObject, or throws an
	 * exception if the corresponding repository is a read-only repository, or
	 * returns null if the given object couldn't be found
	 * 
	 * @param obj
	 * @return
	 * @throws ModelCoreException
	 */
	public TransactionalEditingDomain getEditingDomainForEObject(EObject obj)
			throws ModelCoreException {
		for (IModelRepository repository : repositories) {
			if (repository.contains(obj)) {
				return repository.getEditingDomain();
			}
		}

		return null;
	}

	/**
	 * Returns the list of repositories indexed in this manager.
	 * 
	 * Note that they are in the order
	 * 
	 * @return
	 */
	public List<IModelRepository> getRepositories() {
		return Collections.unmodifiableList(this.repositories);
	}

	public void appendRepository(IModelRepository repository) {
		internalAddRepository(repository, this.repositories.size());
	}

	public void addRepository(IModelRepository repository, int at) {
		internalAddRepository(repository, at);
	}

	public void removeRepository(IModelRepository repository) {
		internalRemoveRepository(repository);
	}

	protected void internalRemoveRepository(IModelRepository repository) {
		this.repositories.remove(repository);
		repository.setManager(null);
	}

	protected void internalAddRepository(IModelRepository repository, int at) {
		this.repositories.add(at, repository);
		repository.setManager(this);
	}
}
