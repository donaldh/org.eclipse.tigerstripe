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
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Enumeration;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Enumeration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.EnumerationImpl#getBaseType <em>Base Type</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class EnumerationImpl extends AbstractArtifactImpl implements
		Enumeration {
	/**
	 * The default value of the '{@link #getBaseType() <em>Base Type</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getBaseType()
	 * @generated
	 * @ordered
	 */
	protected static final String BASE_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBaseType() <em>Base Type</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getBaseType()
	 * @generated
	 * @ordered
	 */
	protected String baseType = BASE_TYPE_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected EnumerationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return VisualeditorPackage.Literals.ENUMERATION;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getBaseType() {
		return baseType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setBaseType(String newBaseType) {
		// ignore changes to base type from EMF editor and diagram editor if not
		// initial setup
		String oldBaseType = baseType;
		if (!isEMFsetCommand()) {
			baseType = newBaseType;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.SET,
						VisualeditorPackage.ENUMERATION__BASE_TYPE,
						oldBaseType, baseType));
			return;
		}
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET,
					VisualeditorPackage.ENUMERATION__BASE_TYPE, oldBaseType,
					baseType));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case VisualeditorPackage.ENUMERATION__BASE_TYPE:
			return getBaseType();
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
		case VisualeditorPackage.ENUMERATION__BASE_TYPE:
			setBaseType((String) newValue);
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
		case VisualeditorPackage.ENUMERATION__BASE_TYPE:
			setBaseType(BASE_TYPE_EDEFAULT);
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
		case VisualeditorPackage.ENUMERATION__BASE_TYPE:
			return BASE_TYPE_EDEFAULT == null ? baseType != null
					: !BASE_TYPE_EDEFAULT.equals(baseType);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (baseType: ");
		result.append(baseType);
		result.append(')');
		return result.toString();
	}

} // EnumerationImpl
