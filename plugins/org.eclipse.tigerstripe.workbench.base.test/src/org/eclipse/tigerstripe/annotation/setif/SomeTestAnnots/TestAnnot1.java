/**
 * <copyright>
 * </copyright>
 *
 * $Id: TestAnnot1.java,v 1.1 2008/07/18 01:07:18 jworrell Exp $
 */
package org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Test Annot1</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.TestAnnot1#getTwine <em>Twine</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.SomeTestAnnotsPackage#getTestAnnot1()
 * @model
 * @generated
 */
public interface TestAnnot1 extends EObject {
	/**
	 * Returns the value of the '<em><b>Twine</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Twine</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Twine</em>' attribute.
	 * @see #setTwine(String)
	 * @see org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.SomeTestAnnotsPackage#getTestAnnot1_Twine()
	 * @model
	 * @generated
	 */
	String getTwine();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.TestAnnot1#getTwine <em>Twine</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Twine</em>' attribute.
	 * @see #getTwine()
	 * @generated
	 */
	void setTwine(String value);

} // TestAnnot1
