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
 * $Id: ResourceListImpl.java,v 1.6 2008/06/19 10:46:03 ystrot Exp $
 */
package org.eclipse.tigerstripe.espace.resources.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.tigerstripe.espace.resources.ResourceList;
import org.eclipse.tigerstripe.espace.resources.ResourceLocation;
import org.eclipse.tigerstripe.espace.resources.ResourcesPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Resource List</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.espace.resources.impl.ResourceListImpl#getLocations <em>Locations</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.espace.resources.impl.ResourceListImpl#getCurrentId <em>Current Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ResourceListImpl extends EObjectImpl implements ResourceList {
	/**
	 * The cached value of the '{@link #getLocations() <em>Locations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocations()
	 * @generated
	 * @ordered
	 */
	protected EList<ResourceLocation> locations;
	/**
	 * The default value of the '{@link #getCurrentId() <em>Current Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCurrentId()
	 * @generated
	 * @ordered
	 */
	protected static final int CURRENT_ID_EDEFAULT = 1;
	/**
	 * The cached value of the '{@link #getCurrentId() <em>Current Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCurrentId()
	 * @generated
	 * @ordered
	 */
	protected int currentId = CURRENT_ID_EDEFAULT;
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
	public EList<ResourceLocation> getLocations() {
		if (locations == null) {
			locations = new EObjectContainmentEList<ResourceLocation>(ResourceLocation.class, this, ResourcesPackage.RESOURCE_LIST__LOCATIONS);
		}
		return locations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getCurrentId() {
		return currentId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCurrentId(int newCurrentId) {
		int oldCurrentId = currentId;
		currentId = newCurrentId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ResourcesPackage.RESOURCE_LIST__CURRENT_ID, oldCurrentId, currentId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ResourcesPackage.RESOURCE_LIST__LOCATIONS:
				return ((InternalEList<?>)getLocations()).basicRemove(otherEnd, msgs);
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
			case ResourcesPackage.RESOURCE_LIST__LOCATIONS:
				return getLocations();
			case ResourcesPackage.RESOURCE_LIST__CURRENT_ID:
				return new Integer(getCurrentId());
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
			case ResourcesPackage.RESOURCE_LIST__LOCATIONS:
				getLocations().clear();
				getLocations().addAll((Collection<? extends ResourceLocation>)newValue);
				return;
			case ResourcesPackage.RESOURCE_LIST__CURRENT_ID:
				setCurrentId(((Integer)newValue).intValue());
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
			case ResourcesPackage.RESOURCE_LIST__LOCATIONS:
				getLocations().clear();
				return;
			case ResourcesPackage.RESOURCE_LIST__CURRENT_ID:
				setCurrentId(CURRENT_ID_EDEFAULT);
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
			case ResourcesPackage.RESOURCE_LIST__LOCATIONS:
				return locations != null && !locations.isEmpty();
			case ResourcesPackage.RESOURCE_LIST__CURRENT_ID:
				return currentId != CURRENT_ID_EDEFAULT;
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
		result.append(" (currentId: ");
		result.append(currentId);
		result.append(')');
		return result.toString();
	}

} //ResourceListImpl
