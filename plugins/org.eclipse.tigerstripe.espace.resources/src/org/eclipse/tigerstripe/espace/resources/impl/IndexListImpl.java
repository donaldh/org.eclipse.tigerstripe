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
 */
package org.eclipse.tigerstripe.espace.resources.impl;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.tigerstripe.espace.resources.IndexKey;
import org.eclipse.tigerstripe.espace.resources.IndexList;
import org.eclipse.tigerstripe.espace.resources.ResourcesPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Index List</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.espace.resources.impl.IndexListImpl#getIndexPaths <em>Index Paths</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IndexListImpl extends EObjectImpl implements IndexList {
    /**
     * The cached value of the '{@link #getIndexPaths() <em>Index Paths</em>}' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIndexPaths()
     * @generated
     * @ordered
     */
    protected EMap<IndexKey, String> indexPaths;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected IndexListImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ResourcesPackage.Literals.INDEX_LIST;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EMap<IndexKey, String> getIndexPaths() {
        if (indexPaths == null) {
            indexPaths = new EcoreEMap<IndexKey,String>(ResourcesPackage.Literals.EINDEX_KEY_TO_STRING_MAP_ENTRY, EIndexKeyToStringMapEntryImpl.class, this, ResourcesPackage.INDEX_LIST__INDEX_PATHS);
        }
        return indexPaths;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case ResourcesPackage.INDEX_LIST__INDEX_PATHS:
                return ((InternalEList<?>)getIndexPaths()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case ResourcesPackage.INDEX_LIST__INDEX_PATHS:
                if (coreType) return getIndexPaths();
                else return getIndexPaths().map();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case ResourcesPackage.INDEX_LIST__INDEX_PATHS:
                ((EStructuralFeature.Setting)getIndexPaths()).set(newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case ResourcesPackage.INDEX_LIST__INDEX_PATHS:
                getIndexPaths().clear();
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case ResourcesPackage.INDEX_LIST__INDEX_PATHS:
                return indexPaths != null && !indexPaths.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //IndexListImpl
