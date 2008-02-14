/**
 * <copyright>
 * </copyright>
 *
 * $Id: IOssjUpdateProcedureSpecificsImpl.java,v 1.1 2008/02/14 23:58:01 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.extensions.ossj.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjUpdateProcedureSpecifics;
import org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IOssj Update Procedure Specifics</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjUpdateProcedureSpecificsImpl#isSingleExtensionType <em>Single Extension Type</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjUpdateProcedureSpecificsImpl#isSessionFactoryMethods <em>Session Factory Methods</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IOssjUpdateProcedureSpecificsImpl extends IOssjArtifactSpecificsImpl implements IOssjUpdateProcedureSpecifics {
	/**
	 * The default value of the '{@link #isSingleExtensionType() <em>Single Extension Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSingleExtensionType()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SINGLE_EXTENSION_TYPE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isSingleExtensionType() <em>Single Extension Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSingleExtensionType()
	 * @generated
	 * @ordered
	 */
	protected boolean singleExtensionType = SINGLE_EXTENSION_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #isSessionFactoryMethods() <em>Session Factory Methods</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSessionFactoryMethods()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SESSION_FACTORY_METHODS_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isSessionFactoryMethods() <em>Session Factory Methods</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSessionFactoryMethods()
	 * @generated
	 * @ordered
	 */
	protected boolean sessionFactoryMethods = SESSION_FACTORY_METHODS_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IOssjUpdateProcedureSpecificsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OssjPackage.Literals.IOSSJ_UPDATE_PROCEDURE_SPECIFICS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSingleExtensionType() {
		return singleExtensionType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSingleExtensionType(boolean newSingleExtensionType) {
		boolean oldSingleExtensionType = singleExtensionType;
		singleExtensionType = newSingleExtensionType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OssjPackage.IOSSJ_UPDATE_PROCEDURE_SPECIFICS__SINGLE_EXTENSION_TYPE, oldSingleExtensionType, singleExtensionType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSessionFactoryMethods() {
		return sessionFactoryMethods;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSessionFactoryMethods(boolean newSessionFactoryMethods) {
		boolean oldSessionFactoryMethods = sessionFactoryMethods;
		sessionFactoryMethods = newSessionFactoryMethods;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OssjPackage.IOSSJ_UPDATE_PROCEDURE_SPECIFICS__SESSION_FACTORY_METHODS, oldSessionFactoryMethods, sessionFactoryMethods));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case OssjPackage.IOSSJ_UPDATE_PROCEDURE_SPECIFICS__SINGLE_EXTENSION_TYPE:
				return isSingleExtensionType() ? Boolean.TRUE : Boolean.FALSE;
			case OssjPackage.IOSSJ_UPDATE_PROCEDURE_SPECIFICS__SESSION_FACTORY_METHODS:
				return isSessionFactoryMethods() ? Boolean.TRUE : Boolean.FALSE;
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
			case OssjPackage.IOSSJ_UPDATE_PROCEDURE_SPECIFICS__SINGLE_EXTENSION_TYPE:
				setSingleExtensionType(((Boolean)newValue).booleanValue());
				return;
			case OssjPackage.IOSSJ_UPDATE_PROCEDURE_SPECIFICS__SESSION_FACTORY_METHODS:
				setSessionFactoryMethods(((Boolean)newValue).booleanValue());
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
			case OssjPackage.IOSSJ_UPDATE_PROCEDURE_SPECIFICS__SINGLE_EXTENSION_TYPE:
				setSingleExtensionType(SINGLE_EXTENSION_TYPE_EDEFAULT);
				return;
			case OssjPackage.IOSSJ_UPDATE_PROCEDURE_SPECIFICS__SESSION_FACTORY_METHODS:
				setSessionFactoryMethods(SESSION_FACTORY_METHODS_EDEFAULT);
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
			case OssjPackage.IOSSJ_UPDATE_PROCEDURE_SPECIFICS__SINGLE_EXTENSION_TYPE:
				return singleExtensionType != SINGLE_EXTENSION_TYPE_EDEFAULT;
			case OssjPackage.IOSSJ_UPDATE_PROCEDURE_SPECIFICS__SESSION_FACTORY_METHODS:
				return sessionFactoryMethods != SESSION_FACTORY_METHODS_EDEFAULT;
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
		result.append(" (singleExtensionType: ");
		result.append(singleExtensionType);
		result.append(", sessionFactoryMethods: ");
		result.append(sessionFactoryMethods);
		result.append(')');
		return result.toString();
	}

} //IOssjUpdateProcedureSpecificsImpl
