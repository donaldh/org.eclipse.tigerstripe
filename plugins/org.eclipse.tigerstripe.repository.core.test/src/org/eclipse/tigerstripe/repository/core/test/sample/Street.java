/**
 * <copyright>
 * </copyright>
 *
 * $Id: Street.java,v 1.1 2008/05/22 17:46:38 edillon Exp $
 */
package org.eclipse.tigerstripe.repository.core.test.sample;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Street</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.repository.core.test.sample.Street#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.repository.core.test.sample.Street#getHouses <em>Houses</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.repository.core.test.sample.Street#getCars <em>Cars</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.repository.core.test.sample.Street#getCrossStreets <em>Cross Streets</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.repository.core.test.sample.SamplePackage#getStreet()
 * @model
 * @generated
 */
public interface Street extends Top {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.SamplePackage#getStreet_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.repository.core.test.sample.Street#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Houses</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.repository.core.test.sample.House}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Houses</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Houses</em>' containment reference list.
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.SamplePackage#getStreet_Houses()
	 * @model containment="true"
	 * @generated
	 */
	EList<House> getHouses();

	/**
	 * Returns the value of the '<em><b>Cars</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.repository.core.test.sample.Car}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cars</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cars</em>' reference list.
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.SamplePackage#getStreet_Cars()
	 * @model
	 * @generated
	 */
	EList<Car> getCars();

	/**
	 * Returns the value of the '<em><b>Cross Streets</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.repository.core.test.sample.Street}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cross Streets</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cross Streets</em>' reference list.
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.SamplePackage#getStreet_CrossStreets()
	 * @model
	 * @generated
	 */
	EList<Street> getCrossStreets();

} // Street
