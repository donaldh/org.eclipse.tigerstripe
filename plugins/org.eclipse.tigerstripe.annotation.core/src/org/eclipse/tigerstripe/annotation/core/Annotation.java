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
package org.eclipse.tigerstripe.annotation.core;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Annotation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.annotation.Annotation#getUri <em>Uri</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.annotation.Annotation#getContent <em>Content</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.annotation.Annotation#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.annotation.AnnotationPackage#getAnnotation()
 * @model
 * @author Yuri Strot
 * @generated
 */
public interface Annotation extends EObject {
	/**
	 * Returns the value of the '<em><b>Uri</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Uri</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Uri</em>' attribute.
	 * @see #setUri(URI)
	 * @see org.eclipse.tigerstripe.annotation.AnnotationPackage#getAnnotation_Uri()
	 * @model dataType="org.eclipse.tigerstripe.annotation.URI"
	 *        annotation="org.eclipse.tigerstripe.annotation indexed='true' id='true'"
	 * @generated
	 */
	URI getUri();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.Annotation#getUri <em>Uri</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Uri</em>' attribute.
	 * @see #getUri()
	 * @generated
	 */
	void setUri(URI value);

	/**
	 * Returns the value of the '<em><b>Content</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Content</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Content</em>' containment reference.
	 * @see #setContent(EObject)
	 * @see org.eclipse.tigerstripe.annotation.AnnotationPackage#getAnnotation_Content()
	 * @model containment="true"
	 * @generated
	 */
	EObject getContent();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.Annotation#getContent <em>Content</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Content</em>' containment reference.
	 * @see #getContent()
	 * @generated
	 */
	void setContent(EObject value);
	
	/**
	 * @model annotation="org.eclipse.tigerstripe.annotation indexed='true'"
	 */
	int getId();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.Annotation#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(int value);

} // Annotation
