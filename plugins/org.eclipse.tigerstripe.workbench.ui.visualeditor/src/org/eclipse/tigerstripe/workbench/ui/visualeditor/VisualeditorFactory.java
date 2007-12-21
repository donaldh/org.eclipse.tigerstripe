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

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a
 * create method for each non-abstract class of the model. <!-- end-user-doc -->
 * 
 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage
 * @generated
 */
public interface VisualeditorFactory extends EFactory {
	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	VisualeditorFactory eINSTANCE = org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorFactoryImpl
			.init();

	/**
	 * Returns a new object of class '<em>Named Element</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Named Element</em>'.
	 * @generated
	 */
	NamedElement createNamedElement();

	/**
	 * Returns a new object of class '<em>Qualified Named Element</em>'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Qualified Named Element</em>'.
	 * @generated
	 */
	QualifiedNamedElement createQualifiedNamedElement();

	/**
	 * Returns a new object of class '<em>Abstract Artifact</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Abstract Artifact</em>'.
	 * @generated
	 */
	AbstractArtifact createAbstractArtifact();

	/**
	 * Returns a new object of class '<em>Managed Entity Artifact</em>'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Managed Entity Artifact</em>'.
	 * @generated
	 */
	ManagedEntityArtifact createManagedEntityArtifact();

	/**
	 * Returns a new object of class '<em>Datatype Artifact</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Datatype Artifact</em>'.
	 * @generated
	 */
	DatatypeArtifact createDatatypeArtifact();

	/**
	 * Returns a new object of class '<em>Notification Artifact</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Notification Artifact</em>'.
	 * @generated
	 */
	NotificationArtifact createNotificationArtifact();

	/**
	 * Returns a new object of class '<em>Named Query Artifact</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Named Query Artifact</em>'.
	 * @generated
	 */
	NamedQueryArtifact createNamedQueryArtifact();

	/**
	 * Returns a new object of class '<em>Enumeration</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Enumeration</em>'.
	 * @generated
	 */
	Enumeration createEnumeration();

	/**
	 * Returns a new object of class '<em>Update Procedure Artifact</em>'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Update Procedure Artifact</em>'.
	 * @generated
	 */
	UpdateProcedureArtifact createUpdateProcedureArtifact();

	/**
	 * Returns a new object of class '<em>Exception Artifact</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Exception Artifact</em>'.
	 * @generated
	 */
	ExceptionArtifact createExceptionArtifact();

	/**
	 * Returns a new object of class '<em>Session Facade Artifact</em>'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Session Facade Artifact</em>'.
	 * @generated
	 */
	SessionFacadeArtifact createSessionFacadeArtifact();

	/**
	 * Returns a new object of class '<em>Association</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Association</em>'.
	 * @generated
	 */
	Association createAssociation();

	/**
	 * Returns a new object of class '<em>Association Class</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Association Class</em>'.
	 * @generated
	 */
	AssociationClass createAssociationClass();

	/**
	 * Returns a new object of class '<em>Typed Element</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Typed Element</em>'.
	 * @generated
	 */
	TypedElement createTypedElement();

	/**
	 * Returns a new object of class '<em>Attribute</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Attribute</em>'.
	 * @generated
	 */
	Attribute createAttribute();

	/**
	 * Returns a new object of class '<em>Method</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Method</em>'.
	 * @generated
	 */
	Method createMethod();

	/**
	 * Returns a new object of class '<em>Literal</em>'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Literal</em>'.
	 * @generated
	 */
	Literal createLiteral();

	/**
	 * Returns a new object of class '<em>Parameter</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Parameter</em>'.
	 * @generated
	 */
	Parameter createParameter();

	/**
	 * Returns a new object of class '<em>Map</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Map</em>'.
	 * @generated
	 */
	Map createMap();

	/**
	 * Returns a new object of class '<em>Reference</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Reference</em>'.
	 * @generated
	 */
	Reference createReference();

	/**
	 * Returns a new object of class '<em>Dependency</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Dependency</em>'.
	 * @generated
	 */
	Dependency createDependency();

	/**
	 * Returns a new object of class '<em>Association Class Class</em>'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Association Class Class</em>'.
	 * @generated
	 */
	AssociationClassClass createAssociationClassClass();

	/**
	 * Returns a new object of class '<em>Diagram Property</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Diagram Property</em>'.
	 * @generated
	 */
	DiagramProperty createDiagramProperty();

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	VisualeditorPackage getVisualeditorPackage();

} // VisualeditorFactory
