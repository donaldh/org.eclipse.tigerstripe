/**
 * <copyright>
 * </copyright>
 *
 * $Id: Car.java,v 1.1 2008/05/22 17:46:37 edillon Exp $
 */
package org.eclipse.tigerstripe.repository.core.test.sample;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Car</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.repository.core.test.sample.Car#getLicensePlate <em>License Plate</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.repository.core.test.sample.SamplePackage#getCar()
 * @model
 * @generated
 */
public interface Car extends EObject {
	/**
	 * Returns the value of the '<em><b>License Plate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>License Plate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>License Plate</em>' attribute.
	 * @see #setLicensePlate(String)
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.SamplePackage#getCar_LicensePlate()
	 * @model
	 * @generated
	 */
	String getLicensePlate();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.repository.core.test.sample.Car#getLicensePlate <em>License Plate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>License Plate</em>' attribute.
	 * @see #getLicensePlate()
	 * @generated
	 */
	void setLicensePlate(String value);

} // Car
