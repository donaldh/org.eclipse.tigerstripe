/**
 * <copyright>
 * </copyright>
 *
 * $Id: IOssjEnumSpecificsImpl.java,v 1.1 2008/02/14 23:58:01 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.extensions.ossj.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.tigerstripe.metamodel.IType;

import org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEnumSpecifics;
import org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IOssj Enum Specifics</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjEnumSpecificsImpl#isExtensible <em>Extensible</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjEnumSpecificsImpl#getBaseIType <em>Base IType</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IOssjEnumSpecificsImpl extends IOssjArtifactSpecificsImpl implements IOssjEnumSpecifics {
	/**
	 * The default value of the '{@link #isExtensible() <em>Extensible</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isExtensible()
	 * @generated
	 * @ordered
	 */
	protected static final boolean EXTENSIBLE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isExtensible() <em>Extensible</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isExtensible()
	 * @generated
	 * @ordered
	 */
	protected boolean extensible = EXTENSIBLE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getBaseIType() <em>Base IType</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseIType()
	 * @generated
	 * @ordered
	 */
	protected IType baseIType;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IOssjEnumSpecificsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OssjPackage.Literals.IOSSJ_ENUM_SPECIFICS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isExtensible() {
		return extensible;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExtensible(boolean newExtensible) {
		boolean oldExtensible = extensible;
		extensible = newExtensible;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OssjPackage.IOSSJ_ENUM_SPECIFICS__EXTENSIBLE, oldExtensible, extensible));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IType getBaseIType() {
		if (baseIType != null && baseIType.eIsProxy()) {
			InternalEObject oldBaseIType = (InternalEObject)baseIType;
			baseIType = (IType)eResolveProxy(oldBaseIType);
			if (baseIType != oldBaseIType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, OssjPackage.IOSSJ_ENUM_SPECIFICS__BASE_ITYPE, oldBaseIType, baseIType));
			}
		}
		return baseIType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IType basicGetBaseIType() {
		return baseIType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBaseIType(IType newBaseIType) {
		IType oldBaseIType = baseIType;
		baseIType = newBaseIType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OssjPackage.IOSSJ_ENUM_SPECIFICS__BASE_ITYPE, oldBaseIType, baseIType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case OssjPackage.IOSSJ_ENUM_SPECIFICS__EXTENSIBLE:
				return isExtensible() ? Boolean.TRUE : Boolean.FALSE;
			case OssjPackage.IOSSJ_ENUM_SPECIFICS__BASE_ITYPE:
				if (resolve) return getBaseIType();
				return basicGetBaseIType();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case OssjPackage.IOSSJ_ENUM_SPECIFICS__EXTENSIBLE:
				setExtensible(((Boolean)newValue).booleanValue());
				return;
			case OssjPackage.IOSSJ_ENUM_SPECIFICS__BASE_ITYPE:
				setBaseIType((IType)newValue);
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
			case OssjPackage.IOSSJ_ENUM_SPECIFICS__EXTENSIBLE:
				setExtensible(EXTENSIBLE_EDEFAULT);
				return;
			case OssjPackage.IOSSJ_ENUM_SPECIFICS__BASE_ITYPE:
				setBaseIType((IType)null);
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
			case OssjPackage.IOSSJ_ENUM_SPECIFICS__EXTENSIBLE:
				return extensible != EXTENSIBLE_EDEFAULT;
			case OssjPackage.IOSSJ_ENUM_SPECIFICS__BASE_ITYPE:
				return baseIType != null;
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
		result.append(" (extensible: ");
		result.append(extensible);
		result.append(')');
		return result.toString();
	}

} //IOssjEnumSpecificsImpl
