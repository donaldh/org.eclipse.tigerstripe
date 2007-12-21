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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Session Facade Artifact</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact#getManagedEntities <em>Managed Entities</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact#getEmittedNotifications <em>Emitted Notifications</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact#getNamedQueries <em>Named Queries</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact#getExposedProcedures <em>Exposed Procedures</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getSessionFacadeArtifact()
 * @model
 * @generated
 */
public interface SessionFacadeArtifact extends AbstractArtifact {
	/**
	 * Returns the value of the '<em><b>Managed Entities</b></em>'
	 * reference list. The list contents are of type
	 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.ManagedEntityArtifact}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Managed Entities</em>' reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Managed Entities</em>' reference list.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getSessionFacadeArtifact_ManagedEntities()
	 * @model type="org.eclipse.tigerstripe.workbench.ui.visualeditor.ManagedEntityArtifact"
	 * @generated
	 */
	EList getManagedEntities();

	/**
	 * Returns the value of the '<em><b>Emitted Notifications</b></em>'
	 * reference list. The list contents are of type
	 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.NotificationArtifact}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Emitted Notifications</em>' reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Emitted Notifications</em>' reference
	 *         list.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getSessionFacadeArtifact_EmittedNotifications()
	 * @model type="org.eclipse.tigerstripe.workbench.ui.visualeditor.NotificationArtifact"
	 * @generated
	 */
	EList getEmittedNotifications();

	/**
	 * Returns the value of the '<em><b>Named Queries</b></em>' reference
	 * list. The list contents are of type
	 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Named Queries</em>' reference list isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Named Queries</em>' reference list.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getSessionFacadeArtifact_NamedQueries()
	 * @model type="org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact"
	 * @generated
	 */
	EList getNamedQueries();

	/**
	 * Returns the value of the '<em><b>Exposed Procedures</b></em>'
	 * reference list. The list contents are of type
	 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.UpdateProcedureArtifact}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Exposed Procedures</em>' reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Exposed Procedures</em>' reference
	 *         list.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getSessionFacadeArtifact_ExposedProcedures()
	 * @model type="org.eclipse.tigerstripe.workbench.ui.visualeditor.UpdateProcedureArtifact"
	 * @generated
	 */
	EList getExposedProcedures();

} // SessionFacadeArtifact
