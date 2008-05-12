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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.tigerstripe.annotation.example.person.PersonFactory
 * @model kind="package"
 * @generated
 */
public interface PersonPackage extends EPackage {
    /**
	 * The package name.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    String eNAME = "person";

    /**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_URI = "http:///org/eclipse/tigerstripe/annotation/example/person.ecore";

    /**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_PREFIX = "org.eclipse.tigerstripe.annotation.example.person";

    /**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    PersonPackage eINSTANCE = org.eclipse.tigerstripe.annotation.example.person.impl.PersonPackageImpl.init();

    /**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.annotation.example.person.impl.NameImpl <em>Name</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.annotation.example.person.impl.NameImpl
	 * @see org.eclipse.tigerstripe.annotation.example.person.impl.PersonPackageImpl#getName_()
	 * @generated
	 */
    int NAME = 0;

    /**
	 * The feature id for the '<em><b>First Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int NAME__FIRST_NAME = 0;

    /**
	 * The feature id for the '<em><b>Last Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int NAME__LAST_NAME = 1;

    /**
	 * The number of structural features of the '<em>Name</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int NAME_FEATURE_COUNT = 2;

    /**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.annotation.example.person.impl.PersonImpl <em>Person</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.annotation.example.person.impl.PersonImpl
	 * @see org.eclipse.tigerstripe.annotation.example.person.impl.PersonPackageImpl#getPerson()
	 * @generated
	 */
    int PERSON = 1;

    /**
	 * The feature id for the '<em><b>Name</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PERSON__NAME = 0;

    /**
	 * The feature id for the '<em><b>Age</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PERSON__AGE = 1;

    /**
	 * The feature id for the '<em><b>Emails</b></em>' attribute list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PERSON__EMAILS = 2;

    /**
	 * The feature id for the '<em><b>Phones</b></em>' attribute list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PERSON__PHONES = 3;

    /**
	 * The feature id for the '<em><b>Married</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PERSON__MARRIED = 4;

    /**
	 * The feature id for the '<em><b>Sex</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PERSON__SEX = 5;

    /**
	 * The number of structural features of the '<em>Person</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int PERSON_FEATURE_COUNT = 6;

    /**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.annotation.example.person.Sex <em>Sex</em>}' enum.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.annotation.example.person.Sex
	 * @see org.eclipse.tigerstripe.annotation.example.person.impl.PersonPackageImpl#getSex()
	 * @generated
	 */
    int SEX = 2;


    /**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.annotation.example.person.Name <em>Name</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Name</em>'.
	 * @see org.eclipse.tigerstripe.annotation.example.person.Name
	 * @generated
	 */
    EClass getName_();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotation.example.person.Name#getFirstName <em>First Name</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>First Name</em>'.
	 * @see org.eclipse.tigerstripe.annotation.example.person.Name#getFirstName()
	 * @see #getName_()
	 * @generated
	 */
    EAttribute getName_FirstName();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotation.example.person.Name#getLastName <em>Last Name</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Last Name</em>'.
	 * @see org.eclipse.tigerstripe.annotation.example.person.Name#getLastName()
	 * @see #getName_()
	 * @generated
	 */
    EAttribute getName_LastName();

    /**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.annotation.example.person.Person <em>Person</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Person</em>'.
	 * @see org.eclipse.tigerstripe.annotation.example.person.Person
	 * @generated
	 */
    EClass getPerson();

    /**
	 * Returns the meta object for the containment reference '{@link org.eclipse.tigerstripe.annotation.example.person.Person#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Name</em>'.
	 * @see org.eclipse.tigerstripe.annotation.example.person.Person#getName()
	 * @see #getPerson()
	 * @generated
	 */
    EReference getPerson_Name();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotation.example.person.Person#getAge <em>Age</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Age</em>'.
	 * @see org.eclipse.tigerstripe.annotation.example.person.Person#getAge()
	 * @see #getPerson()
	 * @generated
	 */
    EAttribute getPerson_Age();

    /**
	 * Returns the meta object for the attribute list '{@link org.eclipse.tigerstripe.annotation.example.person.Person#getEmails <em>Emails</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Emails</em>'.
	 * @see org.eclipse.tigerstripe.annotation.example.person.Person#getEmails()
	 * @see #getPerson()
	 * @generated
	 */
    EAttribute getPerson_Emails();

    /**
	 * Returns the meta object for the attribute list '{@link org.eclipse.tigerstripe.annotation.example.person.Person#getPhones <em>Phones</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Phones</em>'.
	 * @see org.eclipse.tigerstripe.annotation.example.person.Person#getPhones()
	 * @see #getPerson()
	 * @generated
	 */
    EAttribute getPerson_Phones();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotation.example.person.Person#isMarried <em>Married</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Married</em>'.
	 * @see org.eclipse.tigerstripe.annotation.example.person.Person#isMarried()
	 * @see #getPerson()
	 * @generated
	 */
    EAttribute getPerson_Married();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotation.example.person.Person#getSex <em>Sex</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sex</em>'.
	 * @see org.eclipse.tigerstripe.annotation.example.person.Person#getSex()
	 * @see #getPerson()
	 * @generated
	 */
    EAttribute getPerson_Sex();

    /**
	 * Returns the meta object for enum '{@link org.eclipse.tigerstripe.annotation.example.person.Sex <em>Sex</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Sex</em>'.
	 * @see org.eclipse.tigerstripe.annotation.example.person.Sex
	 * @generated
	 */
    EEnum getSex();

    /**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
    PersonFactory getPersonFactory();

    /**
	 * <!-- begin-user-doc -->
     * Defines literals for the meta objects that represent
     * <ul>
     *   <li>each class,</li>
     *   <li>each feature of each class,</li>
     *   <li>each enum,</li>
     *   <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
	 * @generated
	 */
    interface Literals {
        /**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.annotation.example.person.impl.NameImpl <em>Name</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.annotation.example.person.impl.NameImpl
		 * @see org.eclipse.tigerstripe.annotation.example.person.impl.PersonPackageImpl#getName_()
		 * @generated
		 */
        EClass NAME = eINSTANCE.getName_();

        /**
		 * The meta object literal for the '<em><b>First Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute NAME__FIRST_NAME = eINSTANCE.getName_FirstName();

        /**
		 * The meta object literal for the '<em><b>Last Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute NAME__LAST_NAME = eINSTANCE.getName_LastName();

        /**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.annotation.example.person.impl.PersonImpl <em>Person</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.annotation.example.person.impl.PersonImpl
		 * @see org.eclipse.tigerstripe.annotation.example.person.impl.PersonPackageImpl#getPerson()
		 * @generated
		 */
        EClass PERSON = eINSTANCE.getPerson();

        /**
		 * The meta object literal for the '<em><b>Name</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EReference PERSON__NAME = eINSTANCE.getPerson_Name();

        /**
		 * The meta object literal for the '<em><b>Age</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute PERSON__AGE = eINSTANCE.getPerson_Age();

        /**
		 * The meta object literal for the '<em><b>Emails</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute PERSON__EMAILS = eINSTANCE.getPerson_Emails();

        /**
		 * The meta object literal for the '<em><b>Phones</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute PERSON__PHONES = eINSTANCE.getPerson_Phones();

        /**
		 * The meta object literal for the '<em><b>Married</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute PERSON__MARRIED = eINSTANCE.getPerson_Married();

        /**
		 * The meta object literal for the '<em><b>Sex</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute PERSON__SEX = eINSTANCE.getPerson_Sex();

        /**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.annotation.example.person.Sex <em>Sex</em>}' enum.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.annotation.example.person.Sex
		 * @see org.eclipse.tigerstripe.annotation.example.person.impl.PersonPackageImpl#getSex()
		 * @generated
		 */
        EEnum SEX = eINSTANCE.getSex();

    }

} //PersonPackage
