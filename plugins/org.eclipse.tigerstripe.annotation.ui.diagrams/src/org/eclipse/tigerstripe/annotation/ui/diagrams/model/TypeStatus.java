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

import org.eclipse.emf.ecore.EObject;

/**
 * @author Yuri Strot
 * @model
 */
public interface TypeStatus extends EObject {
	
	/**
	 * @model
	 */
	public String getType();
	
	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.TypeStatus#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(String value);

	/**
	 * @model
	 */
	public boolean isShowAll();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.TypeStatus#isShowAll <em>Show All</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Show All</em>' attribute.
	 * @see #isShowAll()
	 * @generated
	 */
	void setShowAll(boolean value);

}
