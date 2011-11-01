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
package org.eclipse.tigerstripe.annotation.example;

import org.eclipse.emf.ecore.EObject;

/**
 * @author Yuri Strot
 * @model extendedMetaData="name='text_._annotation' kind='elementOnly'"
 */
public interface TextAnnotation extends EObject {
	
	/**
	 * @model extendedMetaData="kind='element' name='text' namespace='##targetNamespace'"
	 */
	String getText();

    /**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.example.TextAnnotation#getText <em>Text</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Text</em>' attribute.
	 * @see #getText()
	 * @generated
	 */
    void setText(String value);

}
