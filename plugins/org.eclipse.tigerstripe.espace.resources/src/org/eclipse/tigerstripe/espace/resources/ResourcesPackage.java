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
 * $Id: ResourcesPackage.java,v 1.2 2008/05/11 12:42:38 ystrot Exp $
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
     * The meta object id for the '{@link org.eclipse.tigerstripe.espace.resources.impl.IndexKeyImpl <em>Index Key</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.tigerstripe.espace.resources.impl.IndexKeyImpl
     * @see org.eclipse.tigerstripe.espace.resources.impl.ResourcesPackageImpl#getIndexKey()
     * @generated
     */
    int INDEX_KEY = 0;

    /**
     * The feature id for the '<em><b>Class Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDEX_KEY__CLASS_NAME = 0;

    /**
     * The feature id for the '<em><b>Feature Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDEX_KEY__FEATURE_NAME = 1;

    /**
     * The number of structural features of the '<em>Index Key</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDEX_KEY_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '{@link org.eclipse.tigerstripe.espace.resources.impl.IndexListImpl <em>Index List</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.tigerstripe.espace.resources.impl.IndexListImpl
     * @see org.eclipse.tigerstripe.espace.resources.impl.ResourcesPackageImpl#getIndexList()
     * @generated
     */
    int INDEX_LIST = 1;

    /**
     * The feature id for the '<em><b>Index Paths</b></em>' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDEX_LIST__INDEX_PATHS = 0;

    /**
     * The number of structural features of the '<em>Index List</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INDEX_LIST_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link org.eclipse.tigerstripe.espace.resources.impl.ResourceListImpl <em>Resource List</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.tigerstripe.espace.resources.impl.ResourceListImpl
     * @see org.eclipse.tigerstripe.espace.resources.impl.ResourcesPackageImpl#getResourceList()
     * @generated
     */
    int RESOURCE_LIST = 2;

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
     * The meta object id for the '{@link org.eclipse.emf.common.util.EMap <em>EMap 1</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.common.util.EMap
     * @see org.eclipse.tigerstripe.espace.resources.impl.ResourcesPackageImpl#getEMap_1()
     * @generated
     */
    int EMAP_1 = 3;

    /**
     * The number of structural features of the '<em>EMap 1</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EMAP_1_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link org.eclipse.tigerstripe.espace.resources.impl.EIndexKeyToStringMapEntryImpl <em>EIndex Key To String Map Entry</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.tigerstripe.espace.resources.impl.EIndexKeyToStringMapEntryImpl
     * @see org.eclipse.tigerstripe.espace.resources.impl.ResourcesPackageImpl#getEIndexKeyToStringMapEntry()
     * @generated
     */
    int EINDEX_KEY_TO_STRING_MAP_ENTRY = 4;

    /**
     * The feature id for the '<em><b>Key</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EINDEX_KEY_TO_STRING_MAP_ENTRY__KEY = 0;

    /**
     * The feature id for the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EINDEX_KEY_TO_STRING_MAP_ENTRY__VALUE = 1;

    /**
     * The number of structural features of the '<em>EIndex Key To String Map Entry</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EINDEX_KEY_TO_STRING_MAP_ENTRY_FEATURE_COUNT = 2;

    /**
     * The meta object id for the '<em>Map</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.util.Map
     * @see org.eclipse.tigerstripe.espace.resources.impl.ResourcesPackageImpl#getMap()
     * @generated
     */
    int MAP = 5;

    /**
     * The meta object id for the '<em>URI</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.common.util.URI
     * @see org.eclipse.tigerstripe.espace.resources.impl.ResourcesPackageImpl#getURI()
     * @generated
     */
    int URI = 6;


    /**
     * The meta object id for the '<em>EMap</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.common.util.EMap
     * @see org.eclipse.tigerstripe.espace.resources.impl.ResourcesPackageImpl#getEMap()
     * @generated
     */
    int EMAP = 7;


    /**
     * Returns the meta object for class '{@link org.eclipse.tigerstripe.espace.resources.IndexKey <em>Index Key</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Index Key</em>'.
     * @see org.eclipse.tigerstripe.espace.resources.IndexKey
     * @generated
     */
    EClass getIndexKey();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.espace.resources.IndexKey#getClassName <em>Class Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Class Name</em>'.
     * @see org.eclipse.tigerstripe.espace.resources.IndexKey#getClassName()
     * @see #getIndexKey()
     * @generated
     */
    EAttribute getIndexKey_ClassName();

    /**
     * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.espace.resources.IndexKey#getFeatureName <em>Feature Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Feature Name</em>'.
     * @see org.eclipse.tigerstripe.espace.resources.IndexKey#getFeatureName()
     * @see #getIndexKey()
     * @generated
     */
    EAttribute getIndexKey_FeatureName();

    /**
     * Returns the meta object for class '{@link org.eclipse.tigerstripe.espace.resources.IndexList <em>Index List</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Index List</em>'.
     * @see org.eclipse.tigerstripe.espace.resources.IndexList
     * @generated
     */
    EClass getIndexList();

    /**
     * Returns the meta object for the map '{@link org.eclipse.tigerstripe.espace.resources.IndexList#getIndexPaths <em>Index Paths</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the map '<em>Index Paths</em>'.
     * @see org.eclipse.tigerstripe.espace.resources.IndexList#getIndexPaths()
     * @see #getIndexList()
     * @generated
     */
    EReference getIndexList_IndexPaths();

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
     * Returns the meta object for class '{@link org.eclipse.emf.common.util.EMap <em>EMap 1</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>EMap 1</em>'.
     * @see org.eclipse.emf.common.util.EMap
     * @model instanceClass="org.eclipse.emf.common.util.EMap"
     * @generated
     */
    EClass getEMap_1();

    /**
     * Returns the meta object for class '{@link java.util.Map.Entry <em>EIndex Key To String Map Entry</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>EIndex Key To String Map Entry</em>'.
     * @see java.util.Map.Entry
     * @model keyType="org.eclipse.tigerstripe.espace.resources.IndexKey"
     *        valueDataType="org.eclipse.emf.ecore.EString"
     * @generated
     */
    EClass getEIndexKeyToStringMapEntry();

    /**
     * Returns the meta object for the reference '{@link java.util.Map.Entry <em>Key</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Key</em>'.
     * @see java.util.Map.Entry
     * @see #getEIndexKeyToStringMapEntry()
     * @generated
     */
    EReference getEIndexKeyToStringMapEntry_Key();

    /**
     * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Value</em>'.
     * @see java.util.Map.Entry
     * @see #getEIndexKeyToStringMapEntry()
     * @generated
     */
    EAttribute getEIndexKeyToStringMapEntry_Value();

    /**
     * Returns the meta object for data type '{@link java.util.Map <em>Map</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>Map</em>'.
     * @see java.util.Map
     * @model instanceClass="java.util.Map" typeParameters="T T1"
     * @generated
     */
    EDataType getMap();

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
     * Returns the meta object for data type '{@link org.eclipse.emf.common.util.EMap <em>EMap</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for data type '<em>EMap</em>'.
     * @see org.eclipse.emf.common.util.EMap
     * @model instanceClass="org.eclipse.emf.common.util.EMap" typeParameters="T T1"
     * @generated
     */
    EDataType getEMap();

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
         * The meta object literal for the '{@link org.eclipse.tigerstripe.espace.resources.impl.IndexKeyImpl <em>Index Key</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.tigerstripe.espace.resources.impl.IndexKeyImpl
         * @see org.eclipse.tigerstripe.espace.resources.impl.ResourcesPackageImpl#getIndexKey()
         * @generated
         */
        EClass INDEX_KEY = eINSTANCE.getIndexKey();

        /**
         * The meta object literal for the '<em><b>Class Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INDEX_KEY__CLASS_NAME = eINSTANCE.getIndexKey_ClassName();

        /**
         * The meta object literal for the '<em><b>Feature Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute INDEX_KEY__FEATURE_NAME = eINSTANCE.getIndexKey_FeatureName();

        /**
         * The meta object literal for the '{@link org.eclipse.tigerstripe.espace.resources.impl.IndexListImpl <em>Index List</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.tigerstripe.espace.resources.impl.IndexListImpl
         * @see org.eclipse.tigerstripe.espace.resources.impl.ResourcesPackageImpl#getIndexList()
         * @generated
         */
        EClass INDEX_LIST = eINSTANCE.getIndexList();

        /**
         * The meta object literal for the '<em><b>Index Paths</b></em>' map feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INDEX_LIST__INDEX_PATHS = eINSTANCE.getIndexList_IndexPaths();

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
         * The meta object literal for the '{@link org.eclipse.emf.common.util.EMap <em>EMap 1</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.emf.common.util.EMap
         * @see org.eclipse.tigerstripe.espace.resources.impl.ResourcesPackageImpl#getEMap_1()
         * @generated
         */
        EClass EMAP_1 = eINSTANCE.getEMap_1();

        /**
         * The meta object literal for the '{@link org.eclipse.tigerstripe.espace.resources.impl.EIndexKeyToStringMapEntryImpl <em>EIndex Key To String Map Entry</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.tigerstripe.espace.resources.impl.EIndexKeyToStringMapEntryImpl
         * @see org.eclipse.tigerstripe.espace.resources.impl.ResourcesPackageImpl#getEIndexKeyToStringMapEntry()
         * @generated
         */
        EClass EINDEX_KEY_TO_STRING_MAP_ENTRY = eINSTANCE.getEIndexKeyToStringMapEntry();

        /**
         * The meta object literal for the '<em><b>Key</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EINDEX_KEY_TO_STRING_MAP_ENTRY__KEY = eINSTANCE.getEIndexKeyToStringMapEntry_Key();

        /**
         * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EINDEX_KEY_TO_STRING_MAP_ENTRY__VALUE = eINSTANCE.getEIndexKeyToStringMapEntry_Value();

        /**
         * The meta object literal for the '<em>Map</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see java.util.Map
         * @see org.eclipse.tigerstripe.espace.resources.impl.ResourcesPackageImpl#getMap()
         * @generated
         */
        EDataType MAP = eINSTANCE.getMap();

        /**
         * The meta object literal for the '<em>URI</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.emf.common.util.URI
         * @see org.eclipse.tigerstripe.espace.resources.impl.ResourcesPackageImpl#getURI()
         * @generated
         */
        EDataType URI = eINSTANCE.getURI();

        /**
         * The meta object literal for the '<em>EMap</em>' data type.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.eclipse.emf.common.util.EMap
         * @see org.eclipse.tigerstripe.espace.resources.impl.ResourcesPackageImpl#getEMap()
         * @generated
         */
        EDataType EMAP = eINSTANCE.getEMap();

    }

} //ResourcesPackage
