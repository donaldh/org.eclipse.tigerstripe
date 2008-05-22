/**
 * <copyright>
 * </copyright>
 *
 * $Id: CarImpl.java,v 1.1 2008/05/22 17:46:35 edillon Exp $
 */
package org.eclipse.tigerstripe.repository.core.test.sample.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.tigerstripe.repository.core.test.sample.Car;
import org.eclipse.tigerstripe.repository.core.test.sample.SamplePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Car</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.repository.core.test.sample.impl.CarImpl#getLicensePlate <em>License Plate</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CarImpl extends EObjectImpl implements Car {
	/**
	 * The default value of the '{@link #getLicensePlate() <em>License Plate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLicensePlate()
	 * @generated
	 * @ordered
	 */
	protected static final String LICENSE_PLATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLicensePlate() <em>License Plate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLicensePlate()
	 * @generated
	 * @ordered
	 */
	protected String licensePlate = LICENSE_PLATE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CarImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SamplePackage.Literals.CAR;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLicensePlate() {
		return licensePlate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLicensePlate(String newLicensePlate) {
		String oldLicensePlate = licensePlate;
		licensePlate = newLicensePlate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SamplePackage.CAR__LICENSE_PLATE, oldLicensePlate, licensePlate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SamplePackage.CAR__LICENSE_PLATE:
				return getLicensePlate();
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
			case SamplePackage.CAR__LICENSE_PLATE:
				setLicensePlate((String)newValue);
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
			case SamplePackage.CAR__LICENSE_PLATE:
				setLicensePlate(LICENSE_PLATE_EDEFAULT);
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
			case SamplePackage.CAR__LICENSE_PLATE:
				return LICENSE_PLATE_EDEFAULT == null ? licensePlate != null : !LICENSE_PLATE_EDEFAULT.equals(licensePlate);
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
		result.append(" (licensePlate: ");
		result.append(licensePlate);
		result.append(')');
		return result.toString();
	}

} //CarImpl
