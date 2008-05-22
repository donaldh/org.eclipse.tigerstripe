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

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.tigerstripe.repository.manager.internal.LazyXMLResourceImpl;
import org.eclipse.tigerstripe.repository.manager.internal.SafeExecutableAbstractCommand;

/**
 * Default repository implementation, where all EObject stored in this
 * repository are stored in a single resource
 * 
 * @author erdillon
 * 
 */
public class DefaultXMLModelRepository extends BaseModelRepository implements
		IModelRepository {

	private LazyXMLResourceImpl resource;

	public DefaultXMLModelRepository(URI uri) {
		super(uri);
		resource = new LazyXMLResourceImpl(uri);
		getResources().add(resource);
	}

	public EObject store(final EObject obj, final boolean forceReplace)
			throws ModelCoreException {
		final Object key = KeyService.INSTANCE.getKey(obj);
		if (key == null) {
			throw new ModelCoreException("Invalid key 'null' for " + obj);
		}

		SafeExecutableAbstractCommand cmd = new SafeExecutableAbstractCommand() {
			public void run() throws Exception {
				if (forceReplace) {
					internalRemoveEObjectByKey(key);
				}
				resource.getContents().add(obj);
				resource.save(null);
			}
		};

		getEditingDomain().getCommandStack().execute(cmd);
		if (!cmd.successful()) {
			throw new ModelCoreException("Error while storing EObject '" + obj
					+ "' :" + cmd.getException());
		}

		return obj;
	}

	public void removeEObjectByKey(final Object key) throws ModelCoreException {

		SafeExecutableAbstractCommand cmd = new SafeExecutableAbstractCommand() {
			public void run() throws Exception {
				internalRemoveEObjectByKey(key);

				if (resource.getContents().size() != 0)
					resource.save(null);
				else {
					String pString = getURI().toPlatformString(true);
					IResource res = ResourcesPlugin.getWorkspace().getRoot()
							.findMember(pString);
					res.delete(true, null); // TODO pass a monitor?
				}
			}
		};

		getEditingDomain().getCommandStack().execute(cmd);

		if (!cmd.successful()) {
			throw new ModelCoreException("Error while removing EObject for '"
					+ key + "' :" + cmd.getException());
		}
	}

	public void internalRemoveEObjectByKey(Object key)
			throws ModelCoreException {
		EObject result = getEObjectByKey(key);
		if (result != null) {
			boolean removed = false;
			for (Iterator<EObject> iter = resource.getAllContents(); iter
					.hasNext();) {
				EObject obj = iter.next();
				Object thatKey = KeyService.INSTANCE.getKey(obj);
				if (thatKey.equals(key)) {
					iter.remove();
					removed = true;
				}
			}
			if (!removed) {
				// Panic here!
				throw new ModelCoreException(
						"Panic: couldn't remove EObject for key: " + key);
			}
		}
	}

	public EObject getEObjectByKey(Object key) throws ModelCoreException {

		if (!resource.isLoaded()) {
			try {
				// If the resource doesn't exist on disk
				String pString = getURI().toPlatformString(true);
				IResource res = ResourcesPlugin.getWorkspace().getRoot()
						.findMember(pString);
				if (res != null && res.exists())
					resource.load(null);
			} catch (IOException e) {
				throw new ModelCoreException(
						"Couldn't load repository: " + getURI(), e);
			}
		}
		for (Iterator<EObject> iter = resource.getAllContents(); iter.hasNext();) {
			EObject obj = iter.next();
			Object objKey = KeyService.INSTANCE.getKey(obj);
			if (objKey.equals(key)) {
				return obj;
			}
		}
		return null;
	}

	public boolean contains(EObject obj) {
		Resource res = obj.eResource();
		return resource.equals(res);
	}
}
