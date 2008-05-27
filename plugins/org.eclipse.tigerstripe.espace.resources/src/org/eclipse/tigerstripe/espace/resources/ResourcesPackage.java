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
 * $Id: ResourcesPackage.java,v 1.4 2008/05/27 09:41:03 ystrot Exp $
 */
package org.eclipse.tigerstripe.espace.resources;

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
 * @see org.eclipse.tigerstripe.espace.resources.ResourcesFactory
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
	String eNS_URI = "http:///org/eclipse/tigerstripe/espace/resources.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.tigerstripe.espace.resources";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ResourcesPackage eINSTANCE = org.eclipse.tigerstripe.espace.resources.impl.ResourcesPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.espace.resources.impl.EObjectListImpl <em>EObject List</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.espace.resources.impl.EObjectListImpl
	 * @see org.eclipse.tigerstripe.espace.resources.impl.ResourcesPackageImpl#getEObjectList()
	 * @generated
	 */
	int EOBJECT_LIST = 0;

	/**
	 * The feature id for the '<em><b>Objects</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EOBJECT_LIST__OBJECTS = 0;

	/**
	 * The number of structural features of the '<em>EObject List</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EOBJECT_LIST_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.espace.resources.impl.ResourceListImpl <em>Resource List</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.espace.resources.impl.ResourceListImpl
	 * @see org.eclipse.tigerstripe.espace.resources.impl.ResourcesPackageImpl#getResourceList()
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
	 * @see org.eclipse.tigerstripe.espace.resources.impl.ResourcesPackageImpl#getURI()
	 * @generated
	 */
	int URI = 2;


	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.espace.resources.EObjectList <em>EObject List</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EObject List</em>'.
	 * @see org.eclipse.tigerstripe.espace.resources.EObjectList
	 * @generated
	 */
	EClass getEObjectList();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.espace.resources.EObjectList#getObjects <em>Objects</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Objects</em>'.
	 * @see org.eclipse.tigerstripe.espace.resources.EObjectList#getObjects()
	 * @see #getEObjectList()
	 * @generated
	 */
	EReference getEObjectList_Objects();

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
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.espace.resources.impl.EObjectListImpl <em>EObject List</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.espace.resources.impl.EObjectListImpl
		 * @see org.eclipse.tigerstripe.espace.resources.impl.ResourcesPackageImpl#getEObjectList()
		 * @generated
		 */
		EClass EOBJECT_LIST = eINSTANCE.getEObjectList();

		/**
		 * The meta object literal for the '<em><b>Objects</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EOBJECT_LIST__OBJECTS = eINSTANCE.getEObjectList_Objects();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.espace.resources.impl.ResourceListImpl <em>Resource List</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.espace.resources.impl.ResourceListImpl
		 * @see org.eclipse.tigerstripe.espace.resources.impl.ResourcesPackageImpl#getResourceList()
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
		 * @see org.eclipse.tigerstripe.espace.resources.impl.ResourcesPackageImpl#getURI()
		 * @generated
		 */
		EDataType URI = eINSTANCE.getURI();

	}

} //ResourcesPackage
