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

public interface IAnnotationStoreFactory {

	/**
	 * Returns all AnnotationStores for the given key
	 * 
	 * @param storeKey
	 * @return
	 * @throws AnnotationCoreException
	 */
	public AnnotationStore[] getAnnotationStores(Object storeKey)
			throws AnnotationCoreException;

	/**
	 * Returns the AnnotationStore for the given key/scheme combo.
	 * 
	 * if this AnnotationStore had been used and contains information it will be loaded. 
	 * Otherwise a new empty store is created.
	 * 
	 * @param storeKey
	 * @param scheme
	 * @return
	 * @throws AnnotationCoreException
	 */
	public AnnotationStore getAnnotationStore(Object storeKey,
			IAnnotationScheme scheme) throws AnnotationCoreException;
}
