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
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Qualified Named Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.QualifiedNamedElementImpl#getPackage <em>Package</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.QualifiedNamedElementImpl#isIsReadonly <em>Is Readonly</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.QualifiedNamedElementImpl#isIsAbstract <em>Is Abstract</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class QualifiedNamedElementImpl extends NamedElementImpl implements
		QualifiedNamedElement {
	/**
	 * The default value of the '{@link #getPackage() <em>Package</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getPackage()
	 * @generated
	 * @ordered
	 */
	protected static final String PACKAGE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPackage() <em>Package</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getPackage()
	 * @generated
	 * @ordered
	 */
	protected String package_ = PACKAGE_EDEFAULT;

	/**
	 * The default value of the '{@link #isIsReadonly() <em>Is Readonly</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isIsReadonly()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_READONLY_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsReadonly() <em>Is Readonly</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isIsReadonly()
	 * @generated
	 * @ordered
	 */
	protected boolean isReadonly = IS_READONLY_EDEFAULT;

	/**
	 * The default value of the '{@link #isIsAbstract() <em>Is Abstract</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isIsAbstract()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_ABSTRACT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsAbstract() <em>Is Abstract</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isIsAbstract()
	 * @generated
	 * @ordered
	 */
	protected boolean isAbstract = IS_ABSTRACT_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected QualifiedNamedElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return VisualeditorPackage.Literals.QUALIFIED_NAMED_ELEMENT;
	}

	public void setIsReadonly(boolean isReadonly) {
		this.isReadonly = isReadonly;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getPackage() {
		return package_;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setPackage(String newPackage) {
		String oldPackage = package_;
		if (!isEMFsetCommand()) {
			package_ = newPackage;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.SET,
						VisualeditorPackage.QUALIFIED_NAMED_ELEMENT__PACKAGE,
						oldPackage, package_));
			return;
		}
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET,
					VisualeditorPackage.QUALIFIED_NAMED_ELEMENT__PACKAGE,
					oldPackage, package_));
	}

	protected boolean isEMFsetCommand() {
		Exception e = new Exception();
		StackTraceElement[] stackTrace = e.getStackTrace();
		for (StackTraceElement stackTraceElement : stackTrace) {
			String className = stackTraceElement.getClassName();
			int lastDotPos = className.lastIndexOf(".");
			if (lastDotPos >= 0
					&& lastDotPos < className.length() - 2
					&& className.substring(lastDotPos + 1).startsWith("EMF")
					&& stackTraceElement.getMethodName().equals(
							"setPropertyValue"))
				return true;
		}
		return false;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isIsReadonly() {
		return isReadonly;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isIsAbstract() {
		return isAbstract;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setIsAbstract(boolean newIsAbstract) {
		boolean oldIsAbstract = isAbstract;
		isAbstract = newIsAbstract;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.QUALIFIED_NAMED_ELEMENT__IS_ABSTRACT,
					oldIsAbstract, isAbstract));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 */
	public String getFullyQualifiedName() {
		String packageName = getPackage();
		if (packageName == null || packageName.length() == 0)
			return getName();

		return packageName + "." + getName();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case VisualeditorPackage.QUALIFIED_NAMED_ELEMENT__PACKAGE:
			return getPackage();
		case VisualeditorPackage.QUALIFIED_NAMED_ELEMENT__IS_READONLY:
			return isIsReadonly() ? Boolean.TRUE : Boolean.FALSE;
		case VisualeditorPackage.QUALIFIED_NAMED_ELEMENT__IS_ABSTRACT:
			return isIsAbstract() ? Boolean.TRUE : Boolean.FALSE;
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
			case VisualeditorPackage.QUALIFIED_NAMED_ELEMENT__PACKAGE:
				setPackage((String) newValue);
				return;
			case VisualeditorPackage.QUALIFIED_NAMED_ELEMENT__IS_ABSTRACT:
				setIsAbstract((Boolean) newValue);
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
			case VisualeditorPackage.QUALIFIED_NAMED_ELEMENT__PACKAGE:
				setPackage(PACKAGE_EDEFAULT);
				return;
			case VisualeditorPackage.QUALIFIED_NAMED_ELEMENT__IS_ABSTRACT:
				setIsAbstract(IS_ABSTRACT_EDEFAULT);
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
		case VisualeditorPackage.QUALIFIED_NAMED_ELEMENT__PACKAGE:
			return PACKAGE_EDEFAULT == null ? package_ != null
					: !PACKAGE_EDEFAULT.equals(package_);
		case VisualeditorPackage.QUALIFIED_NAMED_ELEMENT__IS_READONLY:
			return isReadonly != IS_READONLY_EDEFAULT;
		case VisualeditorPackage.QUALIFIED_NAMED_ELEMENT__IS_ABSTRACT:
			return isAbstract != IS_ABSTRACT_EDEFAULT;
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
		result.append(" (package: ");
		result.append(package_);
		result.append(", isReadonly: ");
		result.append(isReadonly);
		result.append(", isAbstract: ");
		result.append(isAbstract);
		result.append(')');
		return result.toString();
	}

	// ======================================================
	// Custom method (not generated)
	public IAbstractArtifact getCorrespondingIArtifact()
			throws TigerstripeException {
		if (eContainer() instanceof Map) {
			Map map = (Map) eContainer();
			ITigerstripeProject tsProject = map
					.getCorrespondingITigerstripeProject();
			IArtifactManagerSession session = tsProject
					.getArtifactManagerSession();
			IAbstractArtifact artifact = session
					.getArtifactByFullyQualifiedName(getFullyQualifiedName());
			return artifact;
		}
		return null;
	}

} // QualifiedNamedElementImpl
