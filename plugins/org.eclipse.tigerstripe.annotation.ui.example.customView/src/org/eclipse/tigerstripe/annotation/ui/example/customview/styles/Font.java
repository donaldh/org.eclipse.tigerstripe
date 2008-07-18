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
package org.eclipse.tigerstripe.annotation.ui.example.customview.styles;

import org.eclipse.emf.ecore.EObject;

/**
 * @author Yuri Strot
 * @model
 */
public interface Font extends EObject {
	
	/**
	 * @model
	 */
	public int getHeight();
	
	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.ui.example.customview.styles.Font#getHeight <em>Height</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Height</em>' attribute.
	 * @see #getHeight()
	 * @generated
	 */
	void setHeight(int value);

	/**
	 * @model
	 */
	public String getName();
	
	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.ui.example.customview.styles.Font#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * @model
	 */
	public int getStyle();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.ui.example.customview.styles.Font#getStyle <em>Style</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Style</em>' attribute.
	 * @see #getStyle()
	 * @generated
	 */
	void setStyle(int value);

}
