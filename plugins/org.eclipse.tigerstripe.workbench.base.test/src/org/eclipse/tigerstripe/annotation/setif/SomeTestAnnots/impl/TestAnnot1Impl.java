/**
 * <copyright>
 * </copyright>
 *
 * $Id: TestAnnot1Impl.java,v 1.1 2008/07/18 01:07:18 jworrell Exp $
 */
package org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.SomeTestAnnotsPackage;
import org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.TestAnnot1;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Test Annot1</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.impl.TestAnnot1Impl#getTwine <em>Twine</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TestAnnot1Impl extends EObjectImpl implements TestAnnot1 {
	/**
	 * The default value of the '{@link #getTwine() <em>Twine</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTwine()
	 * @generated
	 * @ordered
	 */
	protected static final String TWINE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTwine() <em>Twine</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTwine()
	 * @generated
	 * @ordered
	 */
	protected String twine = TWINE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TestAnnot1Impl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SomeTestAnnotsPackage.Literals.TEST_ANNOT1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTwine() {
		return twine;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTwine(String newTwine) {
		String oldTwine = twine;
		twine = newTwine;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SomeTestAnnotsPackage.TEST_ANNOT1__TWINE, oldTwine, twine));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SomeTestAnnotsPackage.TEST_ANNOT1__TWINE:
				return getTwine();
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
			case SomeTestAnnotsPackage.TEST_ANNOT1__TWINE:
				setTwine((String)newValue);
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
			case SomeTestAnnotsPackage.TEST_ANNOT1__TWINE:
				setTwine(TWINE_EDEFAULT);
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
			case SomeTestAnnotsPackage.TEST_ANNOT1__TWINE:
				return TWINE_EDEFAULT == null ? twine != null : !TWINE_EDEFAULT.equals(twine);
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
		result.append(" (twine: ");
		result.append(twine);
		result.append(')');
		return result.toString();
	}

} //TestAnnot1Impl
