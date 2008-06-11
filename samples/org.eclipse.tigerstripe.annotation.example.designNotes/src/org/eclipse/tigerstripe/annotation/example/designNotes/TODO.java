/**
 * <copyright>
 * </copyright>
 *
 * $Id: TODO.java,v 1.1 2008/06/11 23:31:30 edillon Exp $
 */
package org.eclipse.tigerstripe.annotation.example.designNotes;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>TODO</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.annotation.example.designNotes.TODO#isHack <em>Hack</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.annotation.example.designNotes.TODO#getSummary <em>Summary</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.annotation.example.designNotes.DesignNotesPackage#getTODO()
 * @model
 * @generated
 */
public interface TODO extends EObject {
	/**
	 * Returns the value of the '<em><b>Hack</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hack</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hack</em>' attribute.
	 * @see #setHack(boolean)
	 * @see org.eclipse.tigerstripe.annotation.example.designNotes.DesignNotesPackage#getTODO_Hack()
	 * @model
	 * @generated
	 */
	boolean isHack();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.example.designNotes.TODO#isHack <em>Hack</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Hack</em>' attribute.
	 * @see #isHack()
	 * @generated
	 */
	void setHack(boolean value);

	/**
	 * Returns the value of the '<em><b>Summary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Summary</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Summary</em>' attribute.
	 * @see #setSummary(String)
	 * @see org.eclipse.tigerstripe.annotation.example.designNotes.DesignNotesPackage#getTODO_Summary()
	 * @model
	 * @generated
	 */
	String getSummary();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.annotation.example.designNotes.TODO#getSummary <em>Summary</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Summary</em>' attribute.
	 * @see #getSummary()
	 * @generated
	 */
	void setSummary(String value);

} // TODO
