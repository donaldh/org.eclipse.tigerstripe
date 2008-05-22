/**
 * <copyright>
 * </copyright>
 *
 * $Id: House.java,v 1.1 2008/05/22 17:46:37 edillon Exp $
 */
package org.eclipse.tigerstripe.repository.core.test.sample;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>House</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.repository.core.test.sample.House#getNumber <em>Number</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.repository.core.test.sample.House#getWindows <em>Windows</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.repository.core.test.sample.SamplePackage#getHouse()
 * @model
 * @generated
 */
public interface House extends Top {
	/**
	 * Returns the value of the '<em><b>Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Number</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Number</em>' attribute.
	 * @see #setNumber(int)
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.SamplePackage#getHouse_Number()
	 * @model
	 * @generated
	 */
	int getNumber();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.repository.core.test.sample.House#getNumber <em>Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Number</em>' attribute.
	 * @see #getNumber()
	 * @generated
	 */
	void setNumber(int value);

	/**
	 * Returns the value of the '<em><b>Windows</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.repository.core.test.sample.Window}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Windows</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Windows</em>' containment reference list.
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.SamplePackage#getHouse_Windows()
	 * @model containment="true"
	 * @generated
	 */
	EList<Window> getWindows();

} // House
