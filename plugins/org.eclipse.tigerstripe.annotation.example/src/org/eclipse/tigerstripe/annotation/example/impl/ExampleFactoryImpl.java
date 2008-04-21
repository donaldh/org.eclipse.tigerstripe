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
 * $Id: ExampleFactoryImpl.java,v 1.1 2008/04/21 23:21:10 edillon Exp $
 */
package org.eclipse.tigerstripe.annotation.example.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.tigerstripe.annotation.example.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ExampleFactoryImpl extends EFactoryImpl implements ExampleFactory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static ExampleFactory init() {
        try {
            ExampleFactory theExampleFactory = (ExampleFactory)EPackage.Registry.INSTANCE.getEFactory("http:///org/eclipse/tigerstripe/annotation/example.ecore"); 
            if (theExampleFactory != null) {
                return theExampleFactory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new ExampleFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExampleFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case ExamplePackage.CHECK_ANNOTATION: return createCheckAnnotation();
            case ExamplePackage.INTEGER_ANNOTATION: return createIntegerAnnotation();
            case ExamplePackage.TEXT_ANNOTATION: return createTextAnnotation();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CheckAnnotation createCheckAnnotation() {
        CheckAnnotationImpl checkAnnotation = new CheckAnnotationImpl();
        return checkAnnotation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IntegerAnnotation createIntegerAnnotation() {
        IntegerAnnotationImpl integerAnnotation = new IntegerAnnotationImpl();
        return integerAnnotation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TextAnnotation createTextAnnotation() {
        TextAnnotationImpl textAnnotation = new TextAnnotationImpl();
        return textAnnotation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExamplePackage getExamplePackage() {
        return (ExamplePackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static ExamplePackage getPackage() {
        return ExamplePackage.eINSTANCE;
    }

} //ExampleFactoryImpl
