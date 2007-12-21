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
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Named Element</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedElement#getName <em>Name</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedElement#getStereotypes <em>Stereotypes</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedElement#getProperties <em>Properties</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getNamedElement()
 * @model
 * @generated
 */
public interface NamedElement extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getNamedElement_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedElement#getName <em>Name</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Stereotypes</b></em>' attribute
	 * list. The list contents are of type {@link java.lang.String}. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Stereotypes</em>' attribute list isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Stereotypes</em>' attribute list.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getNamedElement_Stereotypes()
	 * @model type="java.lang.String"
	 * @generated
	 */
	EList getStereotypes();

	/**
	 * Returns the value of the '<em><b>Properties</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.DiagramProperty}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Properties</em>' reference list isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Properties</em>' containment reference
	 *         list.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getNamedElement_Properties()
	 * @model type="org.eclipse.tigerstripe.workbench.ui.visualeditor.DiagramProperty"
	 *        containment="true"
	 * @generated
	 */
	EList getProperties();

	EList resetStereotypes();

} // NamedElement
