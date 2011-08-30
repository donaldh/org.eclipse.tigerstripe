/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.visualeditor.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Named Query Artifact</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.NamedQueryArtifactImpl#getReturnedType <em>Returned Type</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class NamedQueryArtifactImpl extends AbstractArtifactImpl implements
		NamedQueryArtifact {
	/**
	 * The cached value of the '{@link #getReturnedType() <em>Returned Type</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getReturnedType()
	 * @generated
	 * @ordered
	 */
	protected AbstractArtifact returnedType = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected NamedQueryArtifactImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return VisualeditorPackage.Literals.NAMED_QUERY_ARTIFACT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AbstractArtifact getReturnedType() {
		if (returnedType != null && returnedType.eIsProxy()) {
			InternalEObject oldReturnedType = (InternalEObject) returnedType;
			returnedType = (AbstractArtifact) eResolveProxy(oldReturnedType);
			if (returnedType != oldReturnedType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(
							this,
							Notification.RESOLVE,
							VisualeditorPackage.NAMED_QUERY_ARTIFACT__RETURNED_TYPE,
							oldReturnedType, returnedType));
			}
		}
		return returnedType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AbstractArtifact basicGetReturnedType() {
		return returnedType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setReturnedType(AbstractArtifact newReturnedType) {
		AbstractArtifact oldReturnedType = returnedType;
		returnedType = newReturnedType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.NAMED_QUERY_ARTIFACT__RETURNED_TYPE,
					oldReturnedType, returnedType));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case VisualeditorPackage.NAMED_QUERY_ARTIFACT__RETURNED_TYPE:
			if (resolve)
				return getReturnedType();
			return basicGetReturnedType();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case VisualeditorPackage.NAMED_QUERY_ARTIFACT__RETURNED_TYPE:
			setReturnedType((AbstractArtifact) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case VisualeditorPackage.NAMED_QUERY_ARTIFACT__RETURNED_TYPE:
			setReturnedType((AbstractArtifact) null);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case VisualeditorPackage.NAMED_QUERY_ARTIFACT__RETURNED_TYPE:
			return returnedType != null;
		}
		return super.eIsSet(featureID);
	}

} // NamedQueryArtifactImpl
