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
public interface Color extends EObject {
	
	/**
	 * @model
	 */
	public int getRed();
	
	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.ui.example.customview.styles.Color#getRed <em>Red</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Red</em>' attribute.
	 * @see #getRed()
	 * @generated
	 */
	void setRed(int value);

	/**
	 * @model
	 */
	public int getGreen();
	
	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.ui.example.customview.styles.Color#getGreen <em>Green</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Green</em>' attribute.
	 * @see #getGreen()
	 * @generated
	 */
	void setGreen(int value);

	/**
	 * @model
	 */
	public int getBlue();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.ui.example.customview.styles.Color#getBlue <em>Blue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Blue</em>' attribute.
	 * @see #getBlue()
	 * @generated
	 */
	void setBlue(int value);

}
