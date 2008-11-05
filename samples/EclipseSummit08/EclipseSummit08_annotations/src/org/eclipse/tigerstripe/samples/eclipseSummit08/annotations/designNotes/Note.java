/**
 * <copyright>
 * </copyright>
 *
 * $Id: Note.java,v 1.1 2008/11/05 19:53:22 edillon Exp $
 */
package org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Note</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.Note#getText <em>Text</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.DesignNotesPackage#getNote()
 * @model
 * @generated
 */
public interface Note extends EObject {
	/**
	 * Returns the value of the '<em><b>Text</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Text</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Text</em>' attribute.
	 * @see #setText(String)
	 * @see org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.DesignNotesPackage#getNote_Text()
	 * @model annotation="org.eclipse.tigerstripe.annotation multiline='true'"
	 * @generated
	 */
	String getText();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.Note#getText <em>Text</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Text</em>' attribute.
	 * @see #getText()
	 * @generated
	 */
	void setText(String value);

} // Note
