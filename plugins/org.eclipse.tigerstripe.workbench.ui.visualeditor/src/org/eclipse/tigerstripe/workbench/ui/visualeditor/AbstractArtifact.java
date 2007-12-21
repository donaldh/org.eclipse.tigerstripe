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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Abstract Artifact</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact#getExtends <em>Extends</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact#getAttributes <em>Attributes</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact#getLiterals <em>Literals</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact#getMethods <em>Methods</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact#getReferences <em>References</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact#getImplements <em>Implements</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAbstractArtifact()
 * @model
 * @generated
 */
public interface AbstractArtifact extends QualifiedNamedElement {
	/**
	 * Returns the value of the '<em><b>Extends</b></em>' reference. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Extends</em>' reference isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Extends</em>' reference.
	 * @see #setExtends(AbstractArtifact)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAbstractArtifact_Extends()
	 * @model
	 * @generated
	 */
	AbstractArtifact getExtends();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact#getExtends <em>Extends</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Extends</em>' reference.
	 * @see #getExtends()
	 * @generated
	 */
	void setExtends(AbstractArtifact value);

	/**
	 * Returns the value of the '<em><b>Attributes</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Attributes</em>' containment reference
	 * list isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Attributes</em>' containment reference
	 *         list.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAbstractArtifact_Attributes()
	 * @model type="org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute"
	 *        containment="true"
	 * @generated
	 */
	EList getAttributes();

	/**
	 * Returns the value of the '<em><b>Literals</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal}. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Literals</em>' containment reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Literals</em>' containment reference
	 *         list.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAbstractArtifact_Literals()
	 * @model type="org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal"
	 *        containment="true"
	 * @generated
	 */
	EList getLiterals();

	/**
	 * Returns the value of the '<em><b>Methods</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Method}. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Methods</em>' containment reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Methods</em>' containment reference
	 *         list.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAbstractArtifact_Methods()
	 * @model type="org.eclipse.tigerstripe.workbench.ui.visualeditor.Method"
	 *        containment="true"
	 * @generated
	 */
	EList getMethods();

	/**
	 * Returns the value of the '<em><b>References</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>References</em>' reference list isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>References</em>' containment reference
	 *         list.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAbstractArtifact_References()
	 * @model type="org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference"
	 *        containment="true"
	 * @generated
	 */
	EList getReferences();

	/**
	 * Returns the value of the '<em><b>Implements</b></em>' reference
	 * list. The list contents are of type
	 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Implements</em>' reference list isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Implements</em>' reference list.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAbstractArtifact_Implements()
	 * @model type="org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact"
	 * @generated
	 */
	EList getImplements();

} // AbstractArtifact
