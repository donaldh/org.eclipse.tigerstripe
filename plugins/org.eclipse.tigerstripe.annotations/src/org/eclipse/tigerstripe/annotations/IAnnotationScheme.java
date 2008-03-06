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

/**
 * All details about an Annotation Scheme
 * 
 * 
 * 
 * @author erdillon
 * 
 */
public interface IAnnotationScheme {

	public IAnnotationScheme[] EMPTY_ARRAY = new IAnnotationScheme[0];

	/**
	 * Returns the NamespaceID defined by this scheme
	 * 
	 * @return
	 */
	public String getNamespaceID();

	/**
	 * Returns the user label to use for this AnnotationScheme
	 * 
	 * @return
	 */
	public String getNamespaceUserLabel();

	/**
	 * Returns the form that applies to the given URI in this scheme, or null
	 * otherwise.
	 * 
	 * Note that if multiple forms could be applicable the first matching form
	 * will be returned.
	 * 
	 * @param URI
	 * @return
	 */
	public IAnnotationForm selectForm(String URI);

	/**
	 * Returns an array that contains all the defined forms for this scheme
	 * 
	 * @return
	 */
	public IAnnotationForm[] getDefinedForms();

	/**
	 * Returns the selector defined for this Scheme. If no selector was defined
	 * through the corresponding extension point, ISelector.DEFAULT is returned.
	 * 
	 * @return
	 */
	public ISelector getSelector();

	public IAnnotationSpecification findAnnotationSpecification(
			String annotationSpecificationID) throws AnnotationCoreException;
}
