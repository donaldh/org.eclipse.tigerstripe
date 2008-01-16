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
 * Top-Level interface for all annotation specifications
 * 
 * @author erdillon
 * 
 */
public interface IAnnotationSpecification {

	/**
	 * Returns the AnnotationSpecification ID
	 * 
	 * @return
	 */
	public String getID();

	/**
	 * Returns the form where this IAnnotationSpecification is defined
	 * 
	 * @return
	 */
	public IAnnotationForm getParentForm();

	/**
	 * The user label for this Annotation Spefication. This labels is the one to
	 * appear in the GUI and be presented to the user in the Annotation View.
	 * 
	 * @return
	 */
	public String getUserLabel();

	/**
	 * The index of the Annotation Specification should be used to specify the
	 * order in which all specifications should be represented in the view.
	 * 
	 * @return
	 */
	public int getIndex();

}
