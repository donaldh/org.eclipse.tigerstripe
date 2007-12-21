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

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Method</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Method#getParameters <em>Parameters</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Method#isIsAbstract <em>Is Abstract</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getMethod()
 * @model
 * @generated
 */
public interface Method extends TypedElement {
	/**
	 * Returns the value of the '<em><b>Parameters</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Parameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameters</em>' containment reference
	 * list isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Parameters</em>' containment reference
	 *         list.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getMethod_Parameters()
	 * @model type="org.eclipse.tigerstripe.workbench.ui.visualeditor.Parameter"
	 *        containment="true"
	 * @generated
	 */
	EList getParameters();

	/**
	 * Returns the value of the '<em><b>Is Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Abstract</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Is Abstract</em>' attribute.
	 * @see #setIsAbstract(boolean)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getMethod_IsAbstract()
	 * @model
	 * @generated
	 */
	boolean isIsAbstract();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Method#isIsAbstract <em>Is Abstract</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Is Abstract</em>' attribute.
	 * @see #isIsAbstract()
	 * @generated
	 */
	void setIsAbstract(boolean value);

	/**
	 * Returns a label for the method that contains the methodName + profile &
	 * return type. This is an ad'hoc method that was added.
	 * 
	 * @since Bug 997
	 * @return
	 */
	public String getLabelString();

	/**
	 * Compares the parameters of this method with the params of the given other
	 * method Returns true if the methodProfile is identical.
	 * 
	 * @param other
	 * @return
	 * @since Bug 997
	 */
	public boolean sameSignature(Method other);
} // Method
