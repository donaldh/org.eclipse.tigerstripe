/**
 * <copyright>
 * </copyright>
 *
 * $Id: TODO.java,v 1.1 2008/11/05 19:53:22 edillon Exp $
 */
package org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>TODO</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.TODO#isHack <em>Hack</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.TODO#getSummary <em>Summary</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.DesignNotesPackage#getTODO()
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
	 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.DesignNotesPackage#getTODO_Hack()
	 * @model
	 * @generated
	 */
	boolean isHack();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.TODO#isHack <em>Hack</em>}' attribute.
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
	 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.DesignNotesPackage#getTODO_Summary()
	 * @model
	 * @generated
	 */
	String getSummary();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.TODO#getSummary <em>Summary</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Summary</em>' attribute.
	 * @see #getSummary()
	 * @generated
	 */
	void setSummary(String value);

} // TODO
