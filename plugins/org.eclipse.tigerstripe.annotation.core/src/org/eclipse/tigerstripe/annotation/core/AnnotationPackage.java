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
 * $Id: AnnotationPackage.java,v 1.3 2008/05/11 12:42:14 ystrot Exp $
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
     * The feature id for the '<em><b>Content</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANNOTATION__CONTENT = 1;

    /**
     * The number of structural features of the '<em>Annotation</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANNOTATION_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link org.eclipse.tigerstripe.annotation.core.impl.ResourceListImpl <em>Resource List</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.tigerstripe.annotation.core.impl.ResourceListImpl
     * @see org.eclipse.tigerstripe.annotation.core.impl.AnnotationPackageImpl#getResourceList()
     * @generated
     */
    int RESOURCE_LIST = 1;

    /**
     * The feature id for the '<em><b>Resource Uris</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE_LIST__RESOURCE_URIS = 0;

    /**
     * The number of structural features of the '<em>Resource List</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE_LIST_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '<em>URI</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.common.util.URI
     * @see org.eclipse.tigerstripe.annotation.core.impl.AnnotationPackageImpl#getURI()
     * @generated
     */
    int URI = 2;


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
     * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.annotation.core.Annotation#getContent <em>Content</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Content</em>'.
     * @see org.eclipse.tigerstripe.annotation.core.Annotation#getContent()
     * @see #getAnnotation()
     * @generated
     */
    EReference getAnnotation_Content();

    /**
     * Returns the meta object for class '{@link org.eclipse.tigerstripe.espace.resources.ResourceList <em>Resource List</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Resource List</em>'.
     * @see org.eclipse.tigerstripe.espace.resources.ResourceList
     * @generated
     */
    EClass getResourceList();

    /**
     * Returns the meta object for the attribute list '{@link org.eclipse.tigerstripe.espace.resources.ResourceList#getResourceUris <em>Resource Uris</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Resource Uris</em>'.
     * @see org.eclipse.tigerstripe.espace.resources.ResourceList#getResourceUris()
     * @see #getResourceList()
     * @generated
     */
    EAttribute getResourceList_ResourceUris();

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
         * The meta object literal for the '<em><b>Content</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ANNOTATION__CONTENT = eINSTANCE.getAnnotation_Content();

        /**
         * The meta object literal for the '{@link org.eclipse.tigerstripe.annotation.core.impl.ResourceListImpl <em>Resource List</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.tigerstripe.annotation.core.impl.ResourceListImpl
         * @see org.eclipse.tigerstripe.annotation.core.impl.AnnotationPackageImpl#getResourceList()
         * @generated
         */
        EClass RESOURCE_LIST = eINSTANCE.getResourceList();

        /**
         * The meta object literal for the '<em><b>Resource Uris</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute RESOURCE_LIST__RESOURCE_URIS = eINSTANCE.getResourceList_ResourceUris();

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
