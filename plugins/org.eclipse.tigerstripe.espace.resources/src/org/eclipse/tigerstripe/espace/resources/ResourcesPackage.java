/**
 * <copyright>
 * </copyright>
 *
 * $Id: ResourcesPackage.java,v 1.3 2008/05/15 04:53:03 ystrot Exp $
 */
package org.eclipse.tigerstripe.espace.resources;

import org.eclipse.emf.ecore.EClass;
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
	 * The feature id for the '<em><b>Resource Uris</b></em>' containment reference list.
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
	 * The meta object id for the '{@link org.eclipse.emf.common.util.URI <em>URI</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.common.util.URI
	 * @see org.eclipse.tigerstripe.espace.resources.impl.ResourcesPackageImpl#getURI()
	 * @generated
	 */
	int URI = 2;

	/**
	 * The number of structural features of the '<em>URI</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int URI_FEATURE_COUNT = 0;


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
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.espace.resources.ResourceList#getResourceUris <em>Resource Uris</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Resource Uris</em>'.
	 * @see org.eclipse.tigerstripe.espace.resources.ResourceList#getResourceUris()
	 * @see #getResourceList()
	 * @generated
	 */
	EReference getResourceList_ResourceUris();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.common.util.URI <em>URI</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>URI</em>'.
	 * @see org.eclipse.emf.common.util.URI
	 * @model instanceClass="org.eclipse.emf.common.util.URI"
	 * @generated
	 */
	EClass getURI();

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
		 * The meta object literal for the '<em><b>Resource Uris</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESOURCE_LIST__RESOURCE_URIS = eINSTANCE.getResourceList_ResourceUris();

		/**
		 * The meta object literal for the '{@link org.eclipse.emf.common.util.URI <em>URI</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.common.util.URI
		 * @see org.eclipse.tigerstripe.espace.resources.impl.ResourcesPackageImpl#getURI()
		 * @generated
		 */
		EClass URI = eINSTANCE.getURI();

	}

} //ResourcesPackage
