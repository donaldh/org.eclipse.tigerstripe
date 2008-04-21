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
 * $Id: ExamplePackage.java,v 1.1 2008/04/21 23:21:10 edillon Exp $
 */
package org.eclipse.tigerstripe.annotation.example;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

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
 * @see org.eclipse.tigerstripe.annotation.example.ExampleFactory
 * @model kind="package"
 * @generated
 */
public interface ExamplePackage extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "example";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http:///org/eclipse/tigerstripe/annotation/example.ecore";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "org.eclipse.tigerstripe.annotation.example";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    ExamplePackage eINSTANCE = org.eclipse.tigerstripe.annotation.example.impl.ExamplePackageImpl.init();

    /**
     * The meta object id for the '{@link org.eclipse.tigerstripe.annotation.example.impl.CheckAnnotationImpl <em>Check Annotation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.tigerstripe.annotation.example.impl.CheckAnnotationImpl
     * @see org.eclipse.tigerstripe.annotation.example.impl.ExamplePackageImpl#getCheckAnnotation()
     * @generated
     */
    int CHECK_ANNOTATION = 0;

    /**
     * The feature id for the '<em><b>Checked</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHECK_ANNOTATION__CHECKED = 0;

    /**
     * The number of structural features of the '<em>Check Annotation</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHECK_ANNOTATION_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link org.eclipse.tigerstripe.annotation.example.impl.IntegerAnnotationImpl <em>Integer Annotation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.tigerstripe.annotation.example.impl.IntegerAnnotationImpl
     * @see org.eclipse.tigerstripe.annotation.example.impl.ExamplePackageImpl#getIntegerAnnotation()
     * @generated
     */
    int INTEGER_ANNOTATION = 1;

    /**
     * The feature id for the '<em><b>Integer</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INTEGER_ANNOTATION__INTEGER = 0;

    /**
     * The number of structural features of the '<em>Integer Annotation</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INTEGER_ANNOTATION_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link org.eclipse.tigerstripe.annotation.example.impl.TextAnnotationImpl <em>Text Annotation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.tigerstripe.annotation.example.impl.TextAnnotationImpl
     * @see org.eclipse.tigerstripe.annotation.example.impl.ExamplePackageImpl#getTextAnnotation()
     * @generated
     */
    int TEXT_ANNOTATION = 2;

    /**
     * The feature id for the '<em><b>Text</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_ANNOTATION__TEXT = 0;

    /**
     * The number of structural features of the '<em>Text Annotation</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TEXT_ANNOTATION_FEATURE_COUNT = 1;


    /**
     * Returns the meta object for class '{@link org.eclipse.tigerstripe.annotation.example.CheckAnnotation <em>Check Annotation</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Check Annotation</em>'.
     * @see org.eclipse.tigerstripe.annotation.example.CheckAnnotation
     * @generated
     */
    EClass getCheckAnnotation();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotation.example.CheckAnnotation#isChecked <em>Checked</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Checked</em>'.
     * @see org.eclipse.tigerstripe.annotation.example.CheckAnnotation#isChecked()
     * @see #getCheckAnnotation()
     * @generated
     */
    EAttribute getCheckAnnotation_Checked();

    /**
     * Returns the meta object for class '{@link org.eclipse.tigerstripe.annotation.example.IntegerAnnotation <em>Integer Annotation</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Integer Annotation</em>'.
     * @see org.eclipse.tigerstripe.annotation.example.IntegerAnnotation
     * @generated
     */
    EClass getIntegerAnnotation();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotation.example.IntegerAnnotation#getInteger <em>Integer</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Integer</em>'.
     * @see org.eclipse.tigerstripe.annotation.example.IntegerAnnotation#getInteger()
     * @see #getIntegerAnnotation()
     * @generated
     */
    EAttribute getIntegerAnnotation_Integer();

    /**
     * Returns the meta object for class '{@link org.eclipse.tigerstripe.annotation.example.TextAnnotation <em>Text Annotation</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Text Annotation</em>'.
     * @see org.eclipse.tigerstripe.annotation.example.TextAnnotation
     * @generated
     */
    EClass getTextAnnotation();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotation.example.TextAnnotation#getText <em>Text</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Text</em>'.
     * @see org.eclipse.tigerstripe.annotation.example.TextAnnotation#getText()
     * @see #getTextAnnotation()
     * @generated
     */
    EAttribute getTextAnnotation_Text();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    ExampleFactory getExampleFactory();

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
         * The meta object literal for the '{@link org.eclipse.tigerstripe.annotation.example.impl.CheckAnnotationImpl <em>Check Annotation</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.tigerstripe.annotation.example.impl.CheckAnnotationImpl
         * @see org.eclipse.tigerstripe.annotation.example.impl.ExamplePackageImpl#getCheckAnnotation()
         * @generated
         */
        EClass CHECK_ANNOTATION = eINSTANCE.getCheckAnnotation();

        /**
         * The meta object literal for the '<em><b>Checked</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CHECK_ANNOTATION__CHECKED = eINSTANCE.getCheckAnnotation_Checked();

        /**
         * The meta object literal for the '{@link org.eclipse.tigerstripe.annotation.example.impl.IntegerAnnotationImpl <em>Integer Annotation</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.tigerstripe.annotation.example.impl.IntegerAnnotationImpl
         * @see org.eclipse.tigerstripe.annotation.example.impl.ExamplePackageImpl#getIntegerAnnotation()
         * @generated
         */
        EClass INTEGER_ANNOTATION = eINSTANCE.getIntegerAnnotation();

        /**
         * The meta object literal for the '<em><b>Integer</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INTEGER_ANNOTATION__INTEGER = eINSTANCE.getIntegerAnnotation_Integer();

        /**
         * The meta object literal for the '{@link org.eclipse.tigerstripe.annotation.example.impl.TextAnnotationImpl <em>Text Annotation</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.tigerstripe.annotation.example.impl.TextAnnotationImpl
         * @see org.eclipse.tigerstripe.annotation.example.impl.ExamplePackageImpl#getTextAnnotation()
         * @generated
         */
        EClass TEXT_ANNOTATION = eINSTANCE.getTextAnnotation();

        /**
         * The meta object literal for the '<em><b>Text</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute TEXT_ANNOTATION__TEXT = eINSTANCE.getTextAnnotation_Text();

    }

} //ExamplePackage
