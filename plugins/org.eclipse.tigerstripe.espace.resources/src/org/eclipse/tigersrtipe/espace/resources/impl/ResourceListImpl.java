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
 * $Id: ResourceListImpl.java,v 1.1 2008/04/21 23:30:28 edillon Exp $
 */
package org.eclipse.tigersrtipe.espace.resources.impl;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

import org.eclipse.tigersrtipe.espace.resources.ResourceList;
import org.eclipse.tigersrtipe.espace.resources.ResourcesPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Resource List</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigersrtipe.espace.resources.impl.ResourceListImpl#getResourceUris <em>Resource Uris</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ResourceListImpl extends EObjectImpl implements ResourceList {
    /**
     * The cached value of the '{@link #getResourceUris() <em>Resource Uris</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResourceUris()
     * @generated
     * @ordered
     */
    protected EList<URI> resourceUris;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ResourceListImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ResourcesPackage.Literals.RESOURCE_LIST;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<URI> getResourceUris() {
        if (resourceUris == null) {
            resourceUris = new EDataTypeUniqueEList<URI>(URI.class, this, ResourcesPackage.RESOURCE_LIST__RESOURCE_URIS);
        }
        return resourceUris;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case ResourcesPackage.RESOURCE_LIST__RESOURCE_URIS:
                return getResourceUris();
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
            case ResourcesPackage.RESOURCE_LIST__RESOURCE_URIS:
                getResourceUris().clear();
                getResourceUris().addAll((Collection<? extends URI>)newValue);
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
            case ResourcesPackage.RESOURCE_LIST__RESOURCE_URIS:
                getResourceUris().clear();
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
            case ResourcesPackage.RESOURCE_LIST__RESOURCE_URIS:
                return resourceUris != null && !resourceUris.isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (resourceUris: ");
        result.append(resourceUris);
        result.append(')');
        return result.toString();
    }

} //ResourceListImpl
