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
 * $Id: ExampleFactory.java,v 1.2 2008/05/11 12:42:21 ystrot Exp $
 */
package org.eclipse.tigerstripe.annotation.example;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.tigerstripe.annotation.example.ExamplePackage
 * @generated
 */
public interface ExampleFactory extends EFactory {
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    ExampleFactory eINSTANCE = org.eclipse.tigerstripe.annotation.example.impl.ExampleFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Check Annotation</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Check Annotation</em>'.
     * @generated
     */
    CheckAnnotation createCheckAnnotation();

    /**
     * Returns a new object of class '<em>Integer Annotation</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Integer Annotation</em>'.
     * @generated
     */
    IntegerAnnotation createIntegerAnnotation();

    /**
     * Returns a new object of class '<em>Text Annotation</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Text Annotation</em>'.
     * @generated
     */
    TextAnnotation createTextAnnotation();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    ExamplePackage getExamplePackage();

} //ExampleFactory
