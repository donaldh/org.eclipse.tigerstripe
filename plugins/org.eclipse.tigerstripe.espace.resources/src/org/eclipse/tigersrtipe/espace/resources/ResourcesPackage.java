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
 * $Id: ResourcesPackage.java,v 1.1 2008/04/21 23:30:28 edillon Exp $
 */
package org.eclipse.tigersrtipe.espace.resources;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
 * @see org.eclipse.tigersrtipe.espace.resources.ResourcesFactory
 * @model kind="package"
 * @generated
 */
public interface ResourcesPackage extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "resources";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http:///org/eclipse/espace/resources.ecore";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "org.eclipse.tigersrtipe.espace.resources";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    ResourcesPackage eINSTANCE = org.eclipse.tigersrtipe.espace.resources.impl.ResourcesPackageImpl.init();

    /**
     * The meta object id for the '{@link org.eclipse.tigersrtipe.espace.resources.impl.ResourceListImpl <em>Resource List</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.tigersrtipe.espace.resources.impl.ResourceListImpl
     * @see org.eclipse.tigersrtipe.espace.resources.impl.ResourcesPackageImpl#getResourceList()
     * @generated
     */
    int RESOURCE_LIST = 0;

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
     * @see org.eclipse.tigersrtipe.espace.resources.impl.ResourcesPackageImpl#getURI()
     * @generated
     */
    int URI = 1;


    /**
     * Returns the meta object for class '{@link org.eclipse.tigersrtipe.espace.resources.ResourceList <em>Resource List</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Resource List</em>'.
     * @see org.eclipse.tigersrtipe.espace.resources.ResourceList
     * @generated
     */
    EClass getResourceList();

    /**
     * Returns the meta object for the attribute list '{@link org.eclipse.tigersrtipe.espace.resources.ResourceList#getResourceUris <em>Resource Uris</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Resource Uris</em>'.
     * @see org.eclipse.tigersrtipe.espace.resources.ResourceList#getResourceUris()
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
    ResourcesFactory getResourcesFactory();

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
         * The meta object literal for the '{@link org.eclipse.tigersrtipe.espace.resources.impl.ResourceListImpl <em>Resource List</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.tigersrtipe.espace.resources.impl.ResourceListImpl
         * @see org.eclipse.tigersrtipe.espace.resources.impl.ResourcesPackageImpl#getResourceList()
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
         * @see org.eclipse.tigersrtipe.espace.resources.impl.ResourcesPackageImpl#getURI()
         * @generated
         */
        EDataType URI = eINSTANCE.getURI();

    }

} //ResourcesPackage
