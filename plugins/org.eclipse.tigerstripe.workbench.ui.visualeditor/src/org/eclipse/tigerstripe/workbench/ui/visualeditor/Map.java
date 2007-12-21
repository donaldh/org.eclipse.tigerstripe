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
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Map</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Map#getArtifacts <em>Artifacts</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Map#getAssociations <em>Associations</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Map#getDependencies <em>Dependencies</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Map#getBasePackage <em>Base Package</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Map#getProperties <em>Properties</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getMap()
 * @model
 * @generated
 */
public interface Map extends EObject {
	/**
	 * Returns the value of the '<em><b>Artifacts</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Artifacts</em>' containment reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Artifacts</em>' containment reference
	 *         list.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getMap_Artifacts()
	 * @model type="org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact"
	 *        containment="true"
	 * @generated
	 */
	EList getArtifacts();

	/**
	 * Returns the value of the '<em><b>Associations</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Associations</em>' containment reference
	 * list isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Associations</em>' containment
	 *         reference list.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getMap_Associations()
	 * @model type="org.eclipse.tigerstripe.workbench.ui.visualeditor.Association"
	 *        containment="true"
	 * @generated
	 */
	EList getAssociations();

	/**
	 * Returns the value of the '<em><b>Dependencies</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dependencies</em>' containment reference
	 * list isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Dependencies</em>' containment
	 *         reference list.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getMap_Dependencies()
	 * @model type="org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency"
	 *        containment="true"
	 * @generated
	 */
	EList getDependencies();

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
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getMap_BasePackage()
	 * @model
	 * @generated
	 */
	String getBasePackage();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Map#getBasePackage <em>Base Package</em>}'
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
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getMap_Properties()
	 * @model type="org.eclipse.tigerstripe.workbench.ui.visualeditor.DiagramProperty"
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
			ITigerstripeProject tsProject);

	public ITigerstripeProject getCorrespondingITigerstripeProject();

} // Map
