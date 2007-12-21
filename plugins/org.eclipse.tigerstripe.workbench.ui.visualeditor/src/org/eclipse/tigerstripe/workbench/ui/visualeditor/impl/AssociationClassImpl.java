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
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClassClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Association Class</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationClassImpl#getAssociatedClass <em>Associated Class</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class AssociationClassImpl extends AssociationImpl implements
		AssociationClass {
	/**
	 * The cached value of the '{@link #getAssociatedClass() <em>Associated Class</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAssociatedClass()
	 * @generated
	 * @ordered
	 */
	protected AssociationClassClass associatedClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected AssociationClassImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return VisualeditorPackage.Literals.ASSOCIATION_CLASS;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AssociationClassClass getAssociatedClass() {
		if (associatedClass != null && associatedClass.eIsProxy()) {
			InternalEObject oldAssociatedClass = (InternalEObject) associatedClass;
			associatedClass = (AssociationClassClass) eResolveProxy(oldAssociatedClass);
			if (associatedClass != oldAssociatedClass) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(
							this,
							Notification.RESOLVE,
							VisualeditorPackage.ASSOCIATION_CLASS__ASSOCIATED_CLASS,
							oldAssociatedClass, associatedClass));
			}
		}
		return associatedClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AssociationClassClass basicGetAssociatedClass() {
		return associatedClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setAssociatedClass(AssociationClassClass newAssociatedClass) {
		AssociationClassClass oldAssociatedClass = associatedClass;
		associatedClass = newAssociatedClass;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.ASSOCIATION_CLASS__ASSOCIATED_CLASS,
					oldAssociatedClass, associatedClass));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case VisualeditorPackage.ASSOCIATION_CLASS__ASSOCIATED_CLASS:
			if (resolve)
				return getAssociatedClass();
			return basicGetAssociatedClass();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		if (!isReadonly) {
			switch (featureID) {
			case VisualeditorPackage.ASSOCIATION_CLASS__ASSOCIATED_CLASS:
				setAssociatedClass((AssociationClassClass) newValue);
				return;
			}
			super.eSet(featureID, newValue);
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public void eUnset(int featureID) {
		if (!isReadonly) {
			switch (featureID) {
			case VisualeditorPackage.ASSOCIATION_CLASS__ASSOCIATED_CLASS:
				setAssociatedClass((AssociationClassClass) null);
				return;
			}
			super.eUnset(featureID);
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case VisualeditorPackage.ASSOCIATION_CLASS__ASSOCIATED_CLASS:
			return associatedClass != null;
		}
		return super.eIsSet(featureID);
	}

} // AssociationClassImpl
