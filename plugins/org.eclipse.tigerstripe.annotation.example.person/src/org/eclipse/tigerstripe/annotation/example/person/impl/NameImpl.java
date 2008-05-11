/**
 * <copyright>
 * 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *     
 * </copyright>
 *
 * $Id: NameImpl.java,v 1.2 2008/05/11 12:41:56 ystrot Exp $
 */
package org.eclipse.tigerstripe.annotation.example.person.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.tigerstripe.annotation.example.person.Name;
import org.eclipse.tigerstripe.annotation.example.person.PersonPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Name</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.annotation.example.person.impl.NameImpl#getFirstName <em>First Name</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.annotation.example.person.impl.NameImpl#getLastName <em>Last Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class NameImpl extends EObjectImpl implements Name {
    /**
     * The default value of the '{@link #getFirstName() <em>First Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFirstName()
     * @generated
     * @ordered
     */
    protected static final String FIRST_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFirstName() <em>First Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFirstName()
     * @generated
     * @ordered
     */
    protected String firstName = FIRST_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getLastName() <em>Last Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLastName()
     * @generated
     * @ordered
     */
    protected static final String LAST_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLastName() <em>Last Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLastName()
     * @generated
     * @ordered
     */
    protected String lastName = LAST_NAME_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected NameImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return PersonPackage.Literals.NAME;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFirstName(String newFirstName) {
        String oldFirstName = firstName;
        firstName = newFirstName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PersonPackage.NAME__FIRST_NAME, oldFirstName, firstName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLastName(String newLastName) {
        String oldLastName = lastName;
        lastName = newLastName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PersonPackage.NAME__LAST_NAME, oldLastName, lastName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case PersonPackage.NAME__FIRST_NAME:
                return getFirstName();
            case PersonPackage.NAME__LAST_NAME:
                return getLastName();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case PersonPackage.NAME__FIRST_NAME:
                setFirstName((String)newValue);
                return;
            case PersonPackage.NAME__LAST_NAME:
                setLastName((String)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case PersonPackage.NAME__FIRST_NAME:
                setFirstName(FIRST_NAME_EDEFAULT);
                return;
            case PersonPackage.NAME__LAST_NAME:
                setLastName(LAST_NAME_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case PersonPackage.NAME__FIRST_NAME:
                return FIRST_NAME_EDEFAULT == null ? firstName != null : !FIRST_NAME_EDEFAULT.equals(firstName);
            case PersonPackage.NAME__LAST_NAME:
                return LAST_NAME_EDEFAULT == null ? lastName != null : !LAST_NAME_EDEFAULT.equals(lastName);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (firstName: ");
        result.append(firstName);
        result.append(", lastName: ");
        result.append(lastName);
        result.append(')');
        return result.toString();
    }

} //NameImpl
