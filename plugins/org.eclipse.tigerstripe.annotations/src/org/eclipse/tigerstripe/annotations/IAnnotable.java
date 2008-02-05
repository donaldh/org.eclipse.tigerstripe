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
package org.eclipse.tigerstripe.annotations;

/**
 * IAnnotable should be implemented by clients of the Annotations framework for
 * all objects that is potentially annotable with the framework.
 * 
 * The idea is to have clients provide a mechanism to provide a KEY to use for
 * the annotations.
 * 
 * This is used by the annotation framework view to try and adapt the current
 * selection to an IAnnotable. If possible it will use this method to get the
 * Annotation URI.
 * 
 * @author erdillon
 * 
 */
public interface IAnnotable {

	/**
	 * Returns the URI to use as the key for annotations related to this.
	 * 
	 * @return
	 * @throws AnnotationCoreException
	 */
	public String getURI() throws AnnotationCoreException;

	/**
	 * The AnnotationStore to use for this.
	 * 
	 * Note that this may optionally return null, in which case a default store
	 * will be provided by the framework.
	 * 
	 * @return
	 * @param scheme -
	 * @throws AnnotationCoreException
	 */
	public AnnotationStore getStore(IAnnotationScheme scheme)
			throws AnnotationCoreException;
}
