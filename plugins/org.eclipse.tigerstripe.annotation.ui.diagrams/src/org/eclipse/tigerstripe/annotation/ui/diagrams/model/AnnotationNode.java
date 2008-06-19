/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.ui.diagrams.model;

import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.tigerstripe.annotation.core.Annotation;

/**
 * @author Yuri Strot
 * @model
 */
public interface AnnotationNode extends Node {
	
	/**
	 * @model
	 */
	public String getAnnotationId();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.AnnotationNode#getAnnotationId <em>Annotation Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Annotation Id</em>' attribute.
	 * @see #getAnnotationId()
	 * @generated
	 */
	void setAnnotationId(String value);
	
	/**
	 * @return annotation
	 */
	public Annotation getAnnotation();

}
