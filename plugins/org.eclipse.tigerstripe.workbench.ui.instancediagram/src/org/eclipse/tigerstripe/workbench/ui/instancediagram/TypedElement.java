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
package org.eclipse.tigerstripe.workbench.ui.instancediagram;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Typed Element</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.TypedElement#getType <em>Type</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.TypedElement#getMultiplicity <em>Multiplicity</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getTypedElement()
 * @model
 * @generated
 */
public interface TypedElement extends NamedElement {
	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getTypedElement_Type()
	 * @model
	 * @generated
	 */
	String getType();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.TypedElement#getType <em>Type</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(String value);

	/**
	 * Returns the value of the '<em><b>Multiplicity</b></em>' attribute.
	 * The literals are from the enumeration
	 * {@link org.eclipse.tigerstripe.workbench.ui.instancediagram.TypeMultiplicity}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Multiplicity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Multiplicity</em>' attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.TypeMultiplicity
	 * @see #setMultiplicity(TypeMultiplicity)
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getTypedElement_Multiplicity()
	 * @model
	 * @generated
	 */
	TypeMultiplicity getMultiplicity();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.TypedElement#getMultiplicity <em>Multiplicity</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Multiplicity</em>' attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.TypeMultiplicity
	 * @see #getMultiplicity()
	 * @generated
	 */
	void setMultiplicity(TypeMultiplicity value);

} // TypedElement
