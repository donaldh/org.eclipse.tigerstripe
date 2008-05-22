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
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.tigerstripe.repository.MetamodelCorePlugin;

public abstract class BaseModelRepository extends ResourceSetImpl implements
		IModelRepository {

	private URI uri = null;
	private ModelManager manager = null;
	private TransactionalEditingDomain editingDomain;

	public BaseModelRepository(URI uri) {
		this.uri = uri;
		editingDomain = TransactionalEditingDomain.Factory.INSTANCE
				.createEditingDomain(this);
	}

	public boolean contains(EObject obj) {
		Resource res = obj.eResource();
		if (res == null)
			return false;
		return getResources().contains(res);
	}

	public Collection<EObject> getAllEObjects() throws ModelCoreException {
		List<EObject> result = new ArrayList<EObject>();
		for (Resource obj : getResources()) {
			result.addAll(obj.getContents());
		}

		return result;
	}

	/*
	 * This method is called when trying to resolve a proxy.
	 * 
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceSetImpl#getEObject(org.eclipse.emf.common.util.URI,
	 *      boolean)
	 */
	@Override
	public EObject getEObject(URI uri, boolean loadOnDemand) {
		Object key = uri.fragment(); // FIXME: this is forcing our URI
		// scheme. Should be abstracted in
		// nameEncoder

		EObject result = null;
		// Search locally first
		try {
			result = getEObjectByKey(key);
			if (result == null && manager != null) {
				// if not found delegate to Manager if any
				return manager.getEObjectByKey(key);
			}
		} catch (ModelCoreException e) {
			MetamodelCorePlugin.log(e);
		}
		return result;
	}

	public URI getURI() {
		return this.uri;
	}

	public ModelManager getManager() {
		return this.manager;
	}

	public TransactionalEditingDomain getEditingDomain()
			throws ModelCoreException {
		if (isReadonly())
			throw new ModelCoreException("Repository (" + uri.toString()
					+ ") is read-only.");
		return editingDomain;
	}

	public boolean isReadonly() {
		return false;
	}

	public void removeEObjectByKey(Object key) throws ModelCoreException {
		throw new UnsupportedOperationException();
	}

	public void setManager(ModelManager manager) {
		this.manager = manager;
	}
}
