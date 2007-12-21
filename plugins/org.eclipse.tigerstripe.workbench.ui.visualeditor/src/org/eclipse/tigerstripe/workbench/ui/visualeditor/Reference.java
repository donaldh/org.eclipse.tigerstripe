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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Reference</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference#getMultiplicity <em>Multiplicity</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference#getZEnd <em>ZEnd</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference#getTypeMultiplicity <em>Type Multiplicity</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference#isIsOrdered <em>Is Ordered</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference#isIsUnique <em>Is Unique</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getReference()
 * @model
 * @generated
 */
public interface Reference extends NamedElement {
	/**
	 * Returns the value of the '<em><b>Multiplicity</b></em>' attribute.
	 * The literals are from the enumeration
	 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.TypeMultiplicity}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Multiplicity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Multiplicity</em>' attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.TypeMultiplicity
	 * @see #setMultiplicity(TypeMultiplicity)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getReference_Multiplicity()
	 * @model
	 * @generated
	 */
	TypeMultiplicity getMultiplicity();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference#getMultiplicity <em>Multiplicity</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Multiplicity</em>' attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.TypeMultiplicity
	 * @see #getMultiplicity()
	 * @generated
	 */
	void setMultiplicity(TypeMultiplicity value);

	/**
	 * Returns the value of the '<em><b>ZEnd</b></em>' reference. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ZEnd</em>' reference isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>ZEnd</em>' reference.
	 * @see #setZEnd(AbstractArtifact)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getReference_ZEnd()
	 * @model
	 * @generated
	 */
	AbstractArtifact getZEnd();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference#getZEnd <em>ZEnd</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>ZEnd</em>' reference.
	 * @see #getZEnd()
	 * @generated
	 */
	void setZEnd(AbstractArtifact value);

	/**
	 * Returns the value of the '<em><b>Type Multiplicity</b></em>'
	 * attribute. The literals are from the enumeration
	 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type Multiplicity</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Type Multiplicity</em>' attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity
	 * @see #setTypeMultiplicity(AssocMultiplicity)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getReference_TypeMultiplicity()
	 * @model
	 * @generated
	 */
	AssocMultiplicity getTypeMultiplicity();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference#getTypeMultiplicity <em>Type Multiplicity</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Type Multiplicity</em>'
	 *            attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity
	 * @see #getTypeMultiplicity()
	 * @generated
	 */
	void setTypeMultiplicity(AssocMultiplicity value);

	/**
	 * Returns the value of the '<em><b>Is Ordered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Ordered</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Is Ordered</em>' attribute.
	 * @see #setIsOrdered(boolean)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getReference_IsOrdered()
	 * @model
	 * @generated
	 */
	boolean isIsOrdered();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference#isIsOrdered <em>Is Ordered</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Is Ordered</em>' attribute.
	 * @see #isIsOrdered()
	 * @generated
	 */
	void setIsOrdered(boolean value);

	/**
	 * Returns the value of the '<em><b>Is Unique</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Unique</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Is Unique</em>' attribute.
	 * @see #setIsUnique(boolean)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getReference_IsUnique()
	 * @model
	 * @generated
	 */
	boolean isIsUnique();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference#isIsUnique <em>Is Unique</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Is Unique</em>' attribute.
	 * @see #isIsUnique()
	 * @generated
	 */
	void setIsUnique(boolean value);

} // Reference
