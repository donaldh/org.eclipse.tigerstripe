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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Instance Map</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap#getClassInstances <em>Class Instances</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap#getAssociationInstances <em>Association Instances</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap#getBasePackage <em>Base Package</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap#getProperties <em>Properties</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getInstanceMap()
 * @model
 * @generated
 */
public interface InstanceMap extends EObject {
	/**
	 * Returns the value of the '<em><b>Class Instances</b></em>'
	 * containment reference list. The list contents are of type
	 * {@link org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Class Instances</em>' containment
	 * reference list isn't clear, there really should be more of a description
	 * here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Class Instances</em>' containment
	 *         reference list.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getInstanceMap_ClassInstances()
	 * @model type="org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance"
	 *        containment="true"
	 * @generated
	 */
	EList getClassInstances();

	/**
	 * Returns the value of the '<em><b>Association Instances</b></em>'
	 * containment reference list. The list contents are of type
	 * {@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Association Instances</em>' containment
	 * reference list isn't clear, there really should be more of a description
	 * here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Association Instances</em>' containment
	 *         reference list.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getInstanceMap_AssociationInstances()
	 * @model type="org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance"
	 *        containment="true"
	 * @generated
	 */
	EList getAssociationInstances();

	/**
	 * Returns the value of the '<em><b>Base Package</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Package</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Base Package</em>' attribute.
	 * @see #setBasePackage(String)
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getInstanceMap_BasePackage()
	 * @model
	 * @generated
	 */
	String getBasePackage();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap#getBasePackage <em>Base Package</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Base Package</em>' attribute.
	 * @see #getBasePackage()
	 * @generated
	 */
	void setBasePackage(String value);

	/**
	 * Returns the value of the '<em><b>Properties</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.tigerstripe.workbench.ui.instancediagram.DiagramProperty}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Properties</em>' containment reference
	 * list isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Properties</em>' containment reference
	 *         list.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getInstanceMap_Properties()
	 * @model type="org.eclipse.tigerstripe.workbench.ui.instancediagram.DiagramProperty"
	 *        containment="true"
	 * @generated
	 */
	EList getProperties();

	// =============================================================
	// Custom variables for lookup purpose (not part of EMF)
	/**
	 * 
	 */
	public void setCorrespondingITigerstripeProject(
			ITigerstripeModelProject tsProject);

	public ITigerstripeModelProject getCorrespondingITigerstripeProject();

} // InstanceMap
