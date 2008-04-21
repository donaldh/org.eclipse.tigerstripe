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
 * $Id: PersonImpl.java,v 1.1 2008/04/21 23:22:31 edillon Exp $
 */
package org.eclipse.tigerstripe.annotation.example.person.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

import org.eclipse.tigerstripe.annotation.example.person.Name;
import org.eclipse.tigerstripe.annotation.example.person.Person;
import org.eclipse.tigerstripe.annotation.example.person.PersonPackage;
import org.eclipse.tigerstripe.annotation.example.person.Sex;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Person</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.annotation.example.person.impl.PersonImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.annotation.example.person.impl.PersonImpl#getAge <em>Age</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.annotation.example.person.impl.PersonImpl#getEmails <em>Emails</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.annotation.example.person.impl.PersonImpl#getPhones <em>Phones</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.annotation.example.person.impl.PersonImpl#isMarried <em>Married</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.annotation.example.person.impl.PersonImpl#getSex <em>Sex</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PersonImpl extends EObjectImpl implements Person {
    /**
     * The cached value of the '{@link #getName() <em>Name</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected Name name;

    /**
     * The default value of the '{@link #getAge() <em>Age</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAge()
     * @generated
     * @ordered
     */
    protected static final int AGE_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getAge() <em>Age</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAge()
     * @generated
     * @ordered
     */
    protected int age = AGE_EDEFAULT;

    /**
     * The cached value of the '{@link #getEmails() <em>Emails</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEmails()
     * @generated
     * @ordered
     */
    protected EList<String> emails;

    /**
     * The cached value of the '{@link #getPhones() <em>Phones</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPhones()
     * @generated
     * @ordered
     */
    protected EList<String> phones;

    /**
     * The default value of the '{@link #isMarried() <em>Married</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isMarried()
     * @generated
     * @ordered
     */
    protected static final boolean MARRIED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isMarried() <em>Married</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isMarried()
     * @generated
     * @ordered
     */
    protected boolean married = MARRIED_EDEFAULT;

    /**
     * The default value of the '{@link #getSex() <em>Sex</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSex()
     * @generated
     * @ordered
     */
    protected static final Sex SEX_EDEFAULT = Sex.MALE;

    /**
     * The cached value of the '{@link #getSex() <em>Sex</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSex()
     * @generated
     * @ordered
     */
    protected Sex sex = SEX_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected PersonImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return PersonPackage.Literals.PERSON;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Name getName() {
        if (name != null && name.eIsProxy()) {
            InternalEObject oldName = (InternalEObject)name;
            name = (Name)eResolveProxy(oldName);
            if (name != oldName) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, PersonPackage.PERSON__NAME, oldName, name));
            }
        }
        return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Name basicGetName() {
        return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setName(Name newName) {
        Name oldName = name;
        name = newName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PersonPackage.PERSON__NAME, oldName, name));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public int getAge() {
        return age;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAge(int newAge) {
        int oldAge = age;
        age = newAge;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PersonPackage.PERSON__AGE, oldAge, age));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<String> getEmails() {
        if (emails == null) {
            emails = new EDataTypeUniqueEList<String>(String.class, this, PersonPackage.PERSON__EMAILS);
        }
        return emails;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<String> getPhones() {
        if (phones == null) {
            phones = new EDataTypeUniqueEList<String>(String.class, this, PersonPackage.PERSON__PHONES);
        }
        return phones;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isMarried() {
        return married;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMarried(boolean newMarried) {
        boolean oldMarried = married;
        married = newMarried;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PersonPackage.PERSON__MARRIED, oldMarried, married));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Sex getSex() {
        return sex;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSex(Sex newSex) {
        Sex oldSex = sex;
        sex = newSex == null ? SEX_EDEFAULT : newSex;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PersonPackage.PERSON__SEX, oldSex, sex));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case PersonPackage.PERSON__NAME:
                if (resolve) return getName();
                return basicGetName();
            case PersonPackage.PERSON__AGE:
                return new Integer(getAge());
            case PersonPackage.PERSON__EMAILS:
                return getEmails();
            case PersonPackage.PERSON__PHONES:
                return getPhones();
            case PersonPackage.PERSON__MARRIED:
                return isMarried() ? Boolean.TRUE : Boolean.FALSE;
            case PersonPackage.PERSON__SEX:
                return getSex();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case PersonPackage.PERSON__NAME:
                setName((Name)newValue);
                return;
            case PersonPackage.PERSON__AGE:
                setAge(((Integer)newValue).intValue());
                return;
            case PersonPackage.PERSON__EMAILS:
                getEmails().clear();
                getEmails().addAll((Collection<? extends String>)newValue);
                return;
            case PersonPackage.PERSON__PHONES:
                getPhones().clear();
                getPhones().addAll((Collection<? extends String>)newValue);
                return;
            case PersonPackage.PERSON__MARRIED:
                setMarried(((Boolean)newValue).booleanValue());
                return;
            case PersonPackage.PERSON__SEX:
                setSex((Sex)newValue);
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
            case PersonPackage.PERSON__NAME:
                setName((Name)null);
                return;
            case PersonPackage.PERSON__AGE:
                setAge(AGE_EDEFAULT);
                return;
            case PersonPackage.PERSON__EMAILS:
                getEmails().clear();
                return;
            case PersonPackage.PERSON__PHONES:
                getPhones().clear();
                return;
            case PersonPackage.PERSON__MARRIED:
                setMarried(MARRIED_EDEFAULT);
                return;
            case PersonPackage.PERSON__SEX:
                setSex(SEX_EDEFAULT);
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
            case PersonPackage.PERSON__NAME:
                return name != null;
            case PersonPackage.PERSON__AGE:
                return age != AGE_EDEFAULT;
            case PersonPackage.PERSON__EMAILS:
                return emails != null && !emails.isEmpty();
            case PersonPackage.PERSON__PHONES:
                return phones != null && !phones.isEmpty();
            case PersonPackage.PERSON__MARRIED:
                return married != MARRIED_EDEFAULT;
            case PersonPackage.PERSON__SEX:
                return sex != SEX_EDEFAULT;
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
        result.append(" (age: ");
        result.append(age);
        result.append(", emails: ");
        result.append(emails);
        result.append(", phones: ");
        result.append(phones);
        result.append(", married: ");
        result.append(married);
        result.append(", sex: ");
        result.append(sex);
        result.append(')');
        return result.toString();
    }

} //PersonImpl
