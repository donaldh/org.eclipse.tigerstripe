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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Named Query Artifact</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact#getReturnedType <em>Returned Type</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getNamedQueryArtifact()
 * @model
 * @generated
 */
public interface NamedQueryArtifact extends AbstractArtifact {
	/**
	 * Returns the value of the '<em><b>Returned Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Returned Type</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Returned Type</em>' reference.
	 * @see #setReturnedType(AbstractArtifact)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getNamedQueryArtifact_ReturnedType()
	 * @model
	 * @generated
	 */
	AbstractArtifact getReturnedType();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact#getReturnedType <em>Returned Type</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Returned Type</em>' reference.
	 * @see #getReturnedType()
	 * @generated
	 */
	void setReturnedType(AbstractArtifact value);

} // NamedQueryArtifact
