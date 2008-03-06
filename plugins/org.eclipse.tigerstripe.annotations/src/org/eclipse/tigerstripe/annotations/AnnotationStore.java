/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    erdillon (Cisco Systems, Inc.) - Initial version
 *******************************************************************************/
package org.eclipse.tigerstripe.annotations;

import org.eclipse.tigerstripe.annotations.internal.DefaultAnnotationStoreFactory;

public abstract class AnnotationStore {

	private static Class defaultAnnotationStoreFactoryClass;
	private static IAnnotationStoreFactory factory;

	/**
	 * Registers the default AnnotationStoreFactory
	 * 
	 * @param factoryClass
	 */
	public static void registerDefaultAnnotationStoreFactory(Class factoryClass) {
		defaultAnnotationStoreFactoryClass = factoryClass;
	}

	public static IAnnotationStoreFactory getDefaultFactory()
			throws AnnotationCoreException {
		if (defaultAnnotationStoreFactoryClass == null) {
			registerDefaultAnnotationStoreFactory(DefaultAnnotationStoreFactory.class);

			try {
				factory = (IAnnotationStoreFactory) defaultAnnotationStoreFactoryClass
						.newInstance();
			} catch (IllegalAccessException e) {
				throw new AnnotationCoreException(
						"Can't instantiate AnnotationStoreFactory: "
								+ e.getMessage(), e);
			} catch (InstantiationException e) {
				throw new AnnotationCoreException(
						"Can't instantiate AnnotationStoreFactory: "
								+ e.getMessage(), e);
			}
		}
		return factory;
	}

	// =============================================================

	/**
	 * Updates the store after a URI change (resource renamed/moved e.g.)
	 */
	public abstract void uriChanged(String oldURI, String newURI)
			throws AnnotationCoreException;

	public abstract void uriRemoved(String URI) throws AnnotationCoreException;

	public abstract void setAnnotation(IAnnotationSpecification specification,
			String URI, Object Value) throws AnnotationCoreException;

	public abstract void unsetAnnotation(
			IAnnotationSpecification specification, String URI)
			throws AnnotationCoreException;

	public abstract Object getAnnotation(
			IAnnotationSpecification specification, String URI)
			throws AnnotationCoreException;

	/**
	 * Return true if this store is dirty, i.e. it was modified locally and
	 * needs to be saved.
	 * 
	 * @return
	 * @throws AnnotationCoreException
	 */
	public abstract boolean isDirty() throws AnnotationCoreException;

	/**
	 * Loads the content of the store from persistent storage
	 * 
	 * As a result, the "dirty" flag is reset.
	 * 
	 * @throws AnnotationCoreException
	 */
	public abstract void load() throws AnnotationCoreException;

	/**
	 * Saves the content of the store to persistent storage.
	 * 
	 * As a result, the "dirty" flag is reset.
	 * 
	 * @throws AnnotationCoreException
	 */
	public abstract void store() throws AnnotationCoreException;
}
