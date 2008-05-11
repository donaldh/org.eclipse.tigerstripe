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
 * $Id: CheckAnnotationImpl.java,v 1.2 2008/05/11 12:42:21 ystrot Exp $
 */
package org.eclipse.tigerstripe.annotation.example.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.tigerstripe.annotation.example.CheckAnnotation;
import org.eclipse.tigerstripe.annotation.example.ExamplePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Check Annotation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.annotation.example.impl.CheckAnnotationImpl#isChecked <em>Checked</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CheckAnnotationImpl extends EObjectImpl implements CheckAnnotation {
    /**
     * The default value of the '{@link #isChecked() <em>Checked</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isChecked()
     * @generated
     * @ordered
     */
    protected static final boolean CHECKED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isChecked() <em>Checked</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isChecked()
     * @generated
     * @ordered
     */
    protected boolean checked = CHECKED_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CheckAnnotationImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ExamplePackage.Literals.CHECK_ANNOTATION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setChecked(boolean newChecked) {
        boolean oldChecked = checked;
        checked = newChecked;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ExamplePackage.CHECK_ANNOTATION__CHECKED, oldChecked, checked));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case ExamplePackage.CHECK_ANNOTATION__CHECKED:
                return isChecked() ? Boolean.TRUE : Boolean.FALSE;
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
            case ExamplePackage.CHECK_ANNOTATION__CHECKED:
                setChecked(((Boolean)newValue).booleanValue());
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
            case ExamplePackage.CHECK_ANNOTATION__CHECKED:
                setChecked(CHECKED_EDEFAULT);
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
            case ExamplePackage.CHECK_ANNOTATION__CHECKED:
                return checked != CHECKED_EDEFAULT;
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
        result.append(" (checked: ");
        result.append(checked);
        result.append(')');
        return result.toString();
    }

} //CheckAnnotationImpl
