/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial API and Implementation
 *******************************************************************************/
package org.eclipse.tigerstripe.annotations;

/**
 * 	An annotation form contains a set of possible annotations to be presented to the user for
 * a specific URI.
 * 
 * @author erdillon
 *
 */
public interface IAnnotationForm {
	
	/**
	 * 	Returns the scheme this form belongs to
	 * 
	 * @return
	 */
	public IAnnotationScheme getScheme();
	
	/**
	 * 	Returns the selector defined for this form if any
	 * 
	 * @return
	 */
	public ISelector getSelector();
	
	/**
	 * 	Returns the array of Annotation Specifications grouped in this form.
	 * 
	 * @return
	 */
	public IAnnotationSpecification[] getSpecifications();
	
	
	public String getID();
	
	public IValidator getValidator();
}
