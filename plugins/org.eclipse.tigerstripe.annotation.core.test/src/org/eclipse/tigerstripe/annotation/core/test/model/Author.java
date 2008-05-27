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
package org.eclipse.tigerstripe.annotation.core.test.model;

import java.util.Date;
import org.eclipse.emf.ecore.EObject;

/**
 * @author Yuri Strot
 * @model
 */
public interface Author extends EObject {
	
	/**
	 * @model
	 */
	public String getFirstName();
	
	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.core.test.model.Author#getFirstName <em>First Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>First Name</em>' attribute.
	 * @see #getFirstName()
	 * @generated
	 */
	void setFirstName(String value);

	/**
	 * @model
	 */
	public String getLastName();
	
	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.core.test.model.Author#getLastName <em>Last Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Last Name</em>' attribute.
	 * @see #getLastName()
	 * @generated
	 */
	void setLastName(String value);

	/**
	 * @model
	 */
	public Date getDate();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.core.test.model.Author#getDate <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Date</em>' attribute.
	 * @see #getDate()
	 * @generated
	 */
	void setDate(Date value);

}
