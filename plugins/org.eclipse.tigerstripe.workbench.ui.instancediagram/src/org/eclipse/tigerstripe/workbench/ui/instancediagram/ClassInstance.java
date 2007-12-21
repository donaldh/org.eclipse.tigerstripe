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

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Class Instance</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance#getVariables <em>Variables</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance#getAssociations <em>Associations</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance#isAssociationClassInstance <em>Association Class Instance</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getClassInstance()
 * @model
 * @generated
 */
public interface ClassInstance extends Instance {
	/**
	 * Returns the value of the '<em><b>Variables</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.tigerstripe.workbench.ui.instancediagram.Variable}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Variables</em>' containment reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Variables</em>' containment reference
	 *         list.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getClassInstance_Variables()
	 * @model type="org.eclipse.tigerstripe.workbench.ui.instancediagram.Variable"
	 *        containment="true"
	 * @generated
	 */
	EList getVariables();

	/**
	 * Returns the value of the '<em><b>Associations</b></em>' reference
	 * list. The list contents are of type
	 * {@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Associations</em>' reference list isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Associations</em>' reference list.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getClassInstance_Associations()
	 * @model type="org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance"
	 * @generated
	 */
	EList getAssociations();

	/**
	 * Returns the value of the '<em><b>Association Class Instance</b></em>'
	 * attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Association Class Instance</em>' attribute
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Association Class Instance</em>'
	 *         attribute.
	 * @see #setAssociationClassInstance(boolean)
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getClassInstance_AssociationClassInstance()
	 * @model
	 * @generated
	 */
	boolean isAssociationClassInstance();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance#isAssociationClassInstance <em>Association Class Instance</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Association Class Instance</em>'
	 *            attribute.
	 * @see #isAssociationClassInstance()
	 * @generated
	 */
	void setAssociationClassInstance(boolean value);

} // ClassInstance
