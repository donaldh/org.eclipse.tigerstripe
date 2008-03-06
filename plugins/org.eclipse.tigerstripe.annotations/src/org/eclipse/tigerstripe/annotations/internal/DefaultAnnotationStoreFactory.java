/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    erdillon (Cisco Systems, Inc.) - Initial version
 *******************************************************************************/
package org.eclipse.tigerstripe.annotations.internal;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;
import org.eclipse.tigerstripe.annotations.AnnotationCoreException;
import org.eclipse.tigerstripe.annotations.AnnotationStore;
import org.eclipse.tigerstripe.annotations.IAnnotationScheme;
import org.eclipse.tigerstripe.annotations.IAnnotationStoreFactory;
import org.eclipse.tigerstripe.annotations.internal.context.AnnotationContext;
import org.eclipse.tigerstripe.annotations.internal.context.ContextFactory;
import org.eclipse.tigerstripe.annotations.internal.context.ContextPackage;

/**
 * 
 * 	This is a default Annotation Store factory using an EMF based persistence mechanism.
 * The storeKey is used to determine the location of the file used for storing the annotations.
 * 
 *  The store file will end-up in the FOLDER of the project corresponding to the given
 *  project.
 *  
 *  A storeCache is implemented to ensure multiple calls to the factory returns the same 
 *  instance of a store. In case, the corresponding file is removed, the cache entry is invalidated 
 *  and the store is marked as dirty.
 * 
 * @author erdillon
 * 
 */
public class DefaultAnnotationStoreFactory implements IResourceChangeListener,
		IAnnotationStoreFactory {

	private final static String FOLDER = ".annotations";

	private Map<IFile, EMFAnnotationStore> storeCache = new HashMap<IFile, EMFAnnotationStore>();

	public DefaultAnnotationStoreFactory() {
		registerForResourceChanges();
	}

	public AnnotationStore getAnnotationStore(Object storeKey,
			IAnnotationScheme scheme) throws AnnotationCoreException {

		XMLResourceImpl res = null;
		EMFAnnotationStore store = null;
		IFile storeFile = resolveFile(storeKey, scheme);

		// is it already in the cache?
		if (storeCache.containsKey(storeFile)) {
			return storeCache.get(storeFile);
		}

		res = new XMLResourceImpl(URI.createPlatformResourceURI(storeFile
				.getFullPath().toOSString(), false));

		if (storeFile.exists()) {
			try {
				res.load(null);

				store = new EMFAnnotationStore(res, scheme);
			} catch (IOException e) {
				throw new AnnotationCoreException(
						"Error while trying to load existing annotation store file: "
								+ e.getMessage(), e);
			}
		} else {
			try {
				EList<EObject> contents = res.getContents();
				AnnotationContext context = ContextFactory.eINSTANCE
						.createAnnotationContext();
				context.setNamespaceID(scheme.getNamespaceID());
				contents.add(context);
				res.save(null);
				store = new EMFAnnotationStore(res, scheme);
			} catch (IOException e) {
				throw new AnnotationCoreException(e);
			}
		}

		storeCache.put(storeFile, store);
		return store;
	}

	public AnnotationStore[] getAnnotationStores(Object storeKey)
			throws AnnotationCoreException {
		// TODO not implemented
		return new AnnotationStore[0];
	}

	/**
	 * Returns the file corresponding to the store for the given key/scheme
	 * combo
	 * 
	 * @param storeKey
	 * @param scheme
	 * @return
	 * @throws AnnotationCoreException
	 */
	private IFile resolveFile(Object storeKey, IAnnotationScheme scheme)
			throws AnnotationCoreException {
		if (storeKey instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable) storeKey;
			IProject project = (IProject) adaptable.getAdapter(IProject.class);
			if (project != null) {
				IFolder folder = project.getFolder(FOLDER);
				if (!folder.exists()) {
					try {
						folder.create(true, true, new NullProgressMonitor());
					} catch (CoreException e) {
						throw new AnnotationCoreException("Can't create "
								+ FOLDER + " directory: " + e.getMessage(), e);
					}
				}

				IFile file = folder.getFile(scheme.getNamespaceID() + "."
						+ ContextPackage.eNS_PREFIX);
				return file;
			}
		}
		throw new AnnotationCoreException("Can't resolve store file for "
				+ storeKey + " / " + scheme.getNamespaceID());
	}

	// ========= Resource Change Listener

	public void resourceChanged(IResourceChangeEvent event) {
		// TODO
	}

	/**
	 * Registers this for changes on resources. The idea is that since each
	 * AnnotationStore is actually a file, we want to be notified when the store
	 * disappears or gets modified outside of this context.
	 * 
	 */
	private void registerForResourceChanges() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(
				this,
				IResourceChangeEvent.PRE_DELETE
						| IResourceChangeEvent.POST_CHANGE
						| IResourceChangeEvent.PRE_CLOSE);
	}
}
