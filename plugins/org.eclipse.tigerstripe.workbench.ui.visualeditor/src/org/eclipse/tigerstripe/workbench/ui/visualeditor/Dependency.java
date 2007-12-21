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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Dependency</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency#getAEnd <em>AEnd</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency#getAEndMultiplicity <em>AEnd Multiplicity</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency#isAEndIsNavigable <em>AEnd Is Navigable</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency#getZEnd <em>ZEnd</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency#getZEndMultiplicity <em>ZEnd Multiplicity</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency#isZEndIsNavigable <em>ZEnd Is Navigable</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getDependency()
 * @model
 * @generated
 */
public interface Dependency extends QualifiedNamedElement {
	/**
	 * Returns the value of the '<em><b>AEnd</b></em>' reference. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>AEnd</em>' reference isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>AEnd</em>' reference.
	 * @see #setAEnd(AbstractArtifact)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getDependency_AEnd()
	 * @model
	 * @generated
	 */
	AbstractArtifact getAEnd();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency#getAEnd <em>AEnd</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>AEnd</em>' reference.
	 * @see #getAEnd()
	 * @generated
	 */
	void setAEnd(AbstractArtifact value);

	/**
	 * Returns the value of the '<em><b>AEnd Multiplicity</b></em>'
	 * attribute. The literals are from the enumeration
	 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>AEnd Multiplicity</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>AEnd Multiplicity</em>' attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity
	 * @see #setAEndMultiplicity(AssocMultiplicity)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getDependency_AEndMultiplicity()
	 * @model
	 * @generated
	 */
	AssocMultiplicity getAEndMultiplicity();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency#getAEndMultiplicity <em>AEnd Multiplicity</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>AEnd Multiplicity</em>'
	 *            attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity
	 * @see #getAEndMultiplicity()
	 * @generated
	 */
	void setAEndMultiplicity(AssocMultiplicity value);

	/**
	 * Returns the value of the '<em><b>AEnd Is Navigable</b></em>'
	 * attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>AEnd Is Navigable</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>AEnd Is Navigable</em>' attribute.
	 * @see #setAEndIsNavigable(boolean)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getDependency_AEndIsNavigable()
	 * @model
	 * @generated
	 */
	boolean isAEndIsNavigable();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency#isAEndIsNavigable <em>AEnd Is Navigable</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>AEnd Is Navigable</em>'
	 *            attribute.
	 * @see #isAEndIsNavigable()
	 * @generated
	 */
	void setAEndIsNavigable(boolean value);

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
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getDependency_ZEnd()
	 * @model
	 * @generated
	 */
	AbstractArtifact getZEnd();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency#getZEnd <em>ZEnd</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>ZEnd</em>' reference.
	 * @see #getZEnd()
	 * @generated
	 */
	void setZEnd(AbstractArtifact value);

	/**
	 * Returns the value of the '<em><b>ZEnd Multiplicity</b></em>'
	 * attribute. The literals are from the enumeration
	 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ZEnd Multiplicity</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>ZEnd Multiplicity</em>' attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity
	 * @see #setZEndMultiplicity(AssocMultiplicity)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getDependency_ZEndMultiplicity()
	 * @model
	 * @generated
	 */
	AssocMultiplicity getZEndMultiplicity();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency#getZEndMultiplicity <em>ZEnd Multiplicity</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>ZEnd Multiplicity</em>'
	 *            attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity
	 * @see #getZEndMultiplicity()
	 * @generated
	 */
	void setZEndMultiplicity(AssocMultiplicity value);

	/**
	 * Returns the value of the '<em><b>ZEnd Is Navigable</b></em>'
	 * attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ZEnd Is Navigable</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>ZEnd Is Navigable</em>' attribute.
	 * @see #setZEndIsNavigable(boolean)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getDependency_ZEndIsNavigable()
	 * @model
	 * @generated
	 */
	boolean isZEndIsNavigable();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency#isZEndIsNavigable <em>ZEnd Is Navigable</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>ZEnd Is Navigable</em>'
	 *            attribute.
	 * @see #isZEndIsNavigable()
	 * @generated
	 */
	void setZEndIsNavigable(boolean value);

} // Dependency
