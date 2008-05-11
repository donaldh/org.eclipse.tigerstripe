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
package org.eclipse.tigerstripe.annotation.example.person;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * @author Yuri Strot
 * @model
 */
public interface Person extends EObject {
	
	/**
	 * @model containment="true"
	 */
	Name getName();

    /**
     * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.example.person.Person#getName <em>Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' containment reference.
     * @see #getName()
     * @generated
     */
    void setName(Name value);

    /**
	 * @model
	 */
	int getAge();

    /**
     * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.example.person.Person#getAge <em>Age</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Age</em>' attribute.
     * @see #getAge()
     * @generated
     */
    void setAge(int value);

    /**
	 * @model
	 */
	EList<String> getEmails();

	/**
	 * @model
	 */
	EList<String> getPhones();
	
	/**
	 * @model
	 */
	boolean isMarried();

    /**
     * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.example.person.Person#isMarried <em>Married</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Married</em>' attribute.
     * @see #isMarried()
     * @generated
     */
    void setMarried(boolean value);
    
	/**
	 * @model
	 */
    Sex getSex();

    /**
     * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.example.person.Person#getSex <em>Sex</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Sex</em>' attribute.
     * @see org.eclipse.tigerstripe.annotation.example.person.Sex
     * @see #getSex()
     * @generated
     */
    void setSex(Sex value);

}
