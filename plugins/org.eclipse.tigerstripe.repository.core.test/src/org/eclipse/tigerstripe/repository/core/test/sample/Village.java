/**
 * <copyright>
 * </copyright>
 *
 * $Id: Village.java,v 1.1 2008/05/22 17:46:38 edillon Exp $
 */
package org.eclipse.tigerstripe.repository.core.test.sample;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Village</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.repository.core.test.sample.Village#getStreets <em>Streets</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.repository.core.test.sample.Village#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.repository.core.test.sample.Village#getNeighboringVillages <em>Neighboring Villages</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.repository.core.test.sample.SamplePackage#getVillage()
 * @model
 * @generated
 */
public interface Village extends Top {
	/**
	 * Returns the value of the '<em><b>Streets</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.repository.core.test.sample.Street}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Streets</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Streets</em>' containment reference list.
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.SamplePackage#getVillage_Streets()
	 * @model containment="true"
	 * @generated
	 */
	EList<Street> getStreets();

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
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.SamplePackage#getVillage_Name()
	 * @model id="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.repository.core.test.sample.Village#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Neighboring Villages</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.repository.core.test.sample.Village}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Neighboring Villages</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Neighboring Villages</em>' reference list.
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.SamplePackage#getVillage_NeighboringVillages()
	 * @model
	 * @generated
	 */
	EList<Village> getNeighboringVillages();

} // Village
