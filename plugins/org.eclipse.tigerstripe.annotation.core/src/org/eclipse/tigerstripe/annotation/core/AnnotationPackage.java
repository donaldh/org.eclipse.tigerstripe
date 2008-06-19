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
 * $Id: AnnotationPackage.java,v 1.5 2008/06/19 10:45:58 ystrot Exp $
 */
package org.eclipse.tigerstripe.annotation.core;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
 * @see org.eclipse.tigerstripe.annotation.core.AnnotationFactory
 * @model kind="package"
 * @generated
 */
public interface AnnotationPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "annotation";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http:///org/eclipse/tigerstripe/annotation.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.tigerstripe.annotation";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	AnnotationPackage eINSTANCE = org.eclipse.tigerstripe.annotation.core.impl.AnnotationPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.annotation.core.impl.AnnotationImpl <em>Annotation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see org.eclipse.tigerstripe.annotation.core.impl.AnnotationImpl
     * @see org.eclipse.tigerstripe.annotation.core.impl.AnnotationPackageImpl#getAnnotation()
	 * @generated
	 */
	int ANNOTATION = 0;

	/**
	 * The feature id for the '<em><b>Uri</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION__URI = 0;

	/**
	 * The feature id for the '<em><b>Content</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION__CONTENT = 1;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION__ID = 2;

	/**
	 * The number of structural features of the '<em>Annotation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '<em>URI</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.common.util.URI
	 * @see org.eclipse.tigerstripe.annotation.core.impl.AnnotationPackageImpl#getURI()
	 * @generated
	 */
	int URI = 1;


	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.annotation.core.Annotation <em>Annotation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Annotation</em>'.
	 * @see org.eclipse.tigerstripe.annotation.core.Annotation
	 * @generated
	 */
	EClass getAnnotation();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotation.core.Annotation#getUri <em>Uri</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Uri</em>'.
	 * @see org.eclipse.tigerstripe.annotation.core.Annotation#getUri()
	 * @see #getAnnotation()
	 * @generated
	 */
	EAttribute getAnnotation_Uri();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.tigerstripe.annotation.core.Annotation#getContent <em>Content</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Content</em>'.
	 * @see org.eclipse.tigerstripe.annotation.core.Annotation#getContent()
	 * @see #getAnnotation()
	 * @generated
	 */
	EReference getAnnotation_Content();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotation.Annotation#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.tigerstripe.annotation.Annotation#getId()
	 * @see #getAnnotation()
	 * @generated
	 */
	EAttribute getAnnotation_Id();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.emf.common.util.URI <em>URI</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>URI</em>'.
	 * @see org.eclipse.emf.common.util.URI
	 * @model instanceClass="org.eclipse.emf.common.util.URI"
	 * @generated
	 */
	EDataType getURI();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	AnnotationFactory getAnnotationFactory();

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
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.annotation.core.impl.AnnotationImpl <em>Annotation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.annotation.core.impl.AnnotationImpl
		 * @see org.eclipse.tigerstripe.annotation.core.impl.AnnotationPackageImpl#getAnnotation()
		 * @generated
		 */
		EClass ANNOTATION = eINSTANCE.getAnnotation();

		/**
		 * The meta object literal for the '<em><b>Uri</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ANNOTATION__URI = eINSTANCE.getAnnotation_Uri();

		/**
		 * The meta object literal for the '<em><b>Content</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANNOTATION__CONTENT = eINSTANCE.getAnnotation_Content();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ANNOTATION__ID = eINSTANCE.getAnnotation_Id();

		/**
		 * The meta object literal for the '<em>URI</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.common.util.URI
		 * @see org.eclipse.tigerstripe.annotation.core.impl.AnnotationPackageImpl#getURI()
		 * @generated
		 */
		EDataType URI = eINSTANCE.getURI();

	}

} //AnnotationPackage
