/**
 * <copyright>
 * </copyright>
 *
 * $Id: Window.java,v 1.1 2008/05/22 17:46:38 edillon Exp $
 */
package org.eclipse.tigerstripe.repository.core.test.sample;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Window</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.repository.core.test.sample.Window#getDirection <em>Direction</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.repository.core.test.sample.SamplePackage#getWindow()
 * @model
 * @generated
 */
public interface Window extends Top {
	/**
	 * Returns the value of the '<em><b>Direction</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Direction</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Direction</em>' attribute.
	 * @see #setDirection(String)
	 * @see org.eclipse.tigerstripe.repository.core.test.sample.SamplePackage#getWindow_Direction()
	 * @model
	 * @generated
	 */
	String getDirection();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.repository.core.test.sample.Window#getDirection <em>Direction</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Direction</em>' attribute.
	 * @see #getDirection()
	 * @generated
	 */
	void setDirection(String value);

} // Window
