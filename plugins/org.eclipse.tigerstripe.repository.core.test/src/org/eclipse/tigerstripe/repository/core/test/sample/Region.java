/**
 * <copyright>
 * </copyright>
 *
 * $Id: Region.java,v 1.1 2008/05/22 17:46:38 edillon Exp $
 */
package org.eclipse.tigerstripe.repository.core.test.sample;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Region</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.repository.core.test.sample.Region#getVillages <em>Villages</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.repository.core.test.sample.Region#getNextTo <em>Next To</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.repository.core.test.sample.Region#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.repository.core.test.sample.SamplePackage#getRegion()
 * @model
 * @generated
 */
public interface Region extends Top {
	/**
	 * Returns the value of the '<em><b>Villages</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.repository.core.test.sample.Village}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Villages</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Villages</em>' containment reference list.
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.SamplePackage#getRegion_Villages()
	 * @model containment="true"
	 * @generated
	 */
	EList<Village> getVillages();

	/**
	 * Returns the value of the '<em><b>Next To</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Next To</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Next To</em>' reference.
	 * @see #setNextTo(Region)
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.SamplePackage#getRegion_NextTo()
	 * @model
	 * @generated
	 */
	Region getNextTo();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.repository.core.test.sample.Region#getNextTo <em>Next To</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Next To</em>' reference.
	 * @see #getNextTo()
	 * @generated
	 */
	void setNextTo(Region value);

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
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.SamplePackage#getRegion_Name()
	 * @model id="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.repository.core.test.sample.Region#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // Region
