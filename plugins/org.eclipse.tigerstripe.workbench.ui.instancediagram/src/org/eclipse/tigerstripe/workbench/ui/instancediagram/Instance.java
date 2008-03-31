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

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Instance</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance#getPackage <em>Package</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance#getArtifactName <em>Artifact Name</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getInstance()
 * @model
 * @generated
 */
public interface Instance extends NamedElement {
	/**
	 * Returns the value of the '<em><b>Package</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Package</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Package</em>' attribute.
	 * @see #setPackage(String)
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getInstance_Package()
	 * @model
	 * @generated
	 */
	String getPackage();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance#getPackage <em>Package</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Package</em>' attribute.
	 * @see #getPackage()
	 * @generated
	 */
	void setPackage(String value);

	/**
	 * Returns the value of the '<em><b>Artifact Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Artifact Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Artifact Name</em>' attribute.
	 * @see #setArtifactName(String)
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getInstance_ArtifactName()
	 * @model
	 * @generated
	 */
	String getArtifactName();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance#getArtifactName <em>Artifact Name</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Artifact Name</em>' attribute.
	 * @see #getArtifactName()
	 * @generated
	 */
	void setArtifactName(String value);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model kind="operation"
	 * @generated
	 */
	String getFullyQualifiedName();

	/**
	 * This is preliminary until the metamodel is complete
	 * 
	 * @preliminary
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	public IAbstractArtifact getArtifact() throws TigerstripeException;

	/**
	 * This is preliminary until the metamodel is complete
	 * 
	 * @preliminary
	 * @return
	 */
	public String getInstanceName();
} // Instance
