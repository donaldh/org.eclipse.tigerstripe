/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.visualeditor;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Association Class</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass#getAssociatedClass <em>Associated Class</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAssociationClass()
 * @model
 * @generated
 */
public interface AssociationClass extends Association {
	/**
	 * Returns the value of the '<em><b>Associated Class</b></em>'
	 * reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Associated Class</em>' containment
	 * reference isn't clear, there really should be more of a description
	 * here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Associated Class</em>' reference.
	 * @see #setAssociatedClass(AssociationClassClass)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAssociationClass_AssociatedClass()
	 * @model
	 * @generated
	 */
	AssociationClassClass getAssociatedClass();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass#getAssociatedClass <em>Associated Class</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Associated Class</em>'
	 *            reference.
	 * @see #getAssociatedClass()
	 * @generated
	 */
	void setAssociatedClass(AssociationClassClass value);

} // AssociationClass
