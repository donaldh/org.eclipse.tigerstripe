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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc --> The <b>Package</b> for the model. It contains
 * accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * 
 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorFactory
 * @model kind="package"
 * @generated
 */
public interface VisualeditorPackage extends EPackage {
	/**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "visualeditor";

	/**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "null";

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "null";

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	VisualeditorPackage eINSTANCE = org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl
			.init();

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.NamedElementImpl <em>Named Element</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.NamedElementImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getNamedElement()
	 * @generated
	 */
	int NAMED_ELEMENT = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT__NAME = 0;

	/**
	 * The feature id for the '<em><b>Stereotypes</b></em>' attribute list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT__STEREOTYPES = 1;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT__PROPERTIES = 2;

	/**
	 * The number of structural features of the '<em>Named Element</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.QualifiedNamedElementImpl <em>Qualified Named Element</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.QualifiedNamedElementImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getQualifiedNamedElement()
	 * @generated
	 */
	int QUALIFIED_NAMED_ELEMENT = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int QUALIFIED_NAMED_ELEMENT__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Stereotypes</b></em>' attribute list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int QUALIFIED_NAMED_ELEMENT__STEREOTYPES = NAMED_ELEMENT__STEREOTYPES;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int QUALIFIED_NAMED_ELEMENT__PROPERTIES = NAMED_ELEMENT__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int QUALIFIED_NAMED_ELEMENT__PACKAGE = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Readonly</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int QUALIFIED_NAMED_ELEMENT__IS_READONLY = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Is Abstract</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int QUALIFIED_NAMED_ELEMENT__IS_ABSTRACT = NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Qualified Named Element</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AbstractArtifactImpl <em>Abstract Artifact</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AbstractArtifactImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getAbstractArtifact()
	 * @generated
	 */
	int ABSTRACT_ARTIFACT = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_ARTIFACT__NAME = QUALIFIED_NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Stereotypes</b></em>' attribute list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_ARTIFACT__STEREOTYPES = QUALIFIED_NAMED_ELEMENT__STEREOTYPES;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_ARTIFACT__PROPERTIES = QUALIFIED_NAMED_ELEMENT__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_ARTIFACT__PACKAGE = QUALIFIED_NAMED_ELEMENT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Is Readonly</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_ARTIFACT__IS_READONLY = QUALIFIED_NAMED_ELEMENT__IS_READONLY;

	/**
	 * The feature id for the '<em><b>Is Abstract</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_ARTIFACT__IS_ABSTRACT = QUALIFIED_NAMED_ELEMENT__IS_ABSTRACT;

	/**
	 * The feature id for the '<em><b>Extends</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_ARTIFACT__EXTENDS = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_ARTIFACT__ATTRIBUTES = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Literals</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_ARTIFACT__LITERALS = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Methods</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_ARTIFACT__METHODS = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>References</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_ARTIFACT__REFERENCES = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Implements</b></em>' reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_ARTIFACT__IMPLEMENTS = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Abstract Artifact</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_ARTIFACT_FEATURE_COUNT = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.ManagedEntityArtifactImpl <em>Managed Entity Artifact</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.ManagedEntityArtifactImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getManagedEntityArtifact()
	 * @generated
	 */
	int MANAGED_ENTITY_ARTIFACT = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MANAGED_ENTITY_ARTIFACT__NAME = ABSTRACT_ARTIFACT__NAME;

	/**
	 * The feature id for the '<em><b>Stereotypes</b></em>' attribute list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MANAGED_ENTITY_ARTIFACT__STEREOTYPES = ABSTRACT_ARTIFACT__STEREOTYPES;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MANAGED_ENTITY_ARTIFACT__PROPERTIES = ABSTRACT_ARTIFACT__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MANAGED_ENTITY_ARTIFACT__PACKAGE = ABSTRACT_ARTIFACT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Is Readonly</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MANAGED_ENTITY_ARTIFACT__IS_READONLY = ABSTRACT_ARTIFACT__IS_READONLY;

	/**
	 * The feature id for the '<em><b>Is Abstract</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MANAGED_ENTITY_ARTIFACT__IS_ABSTRACT = ABSTRACT_ARTIFACT__IS_ABSTRACT;

	/**
	 * The feature id for the '<em><b>Extends</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MANAGED_ENTITY_ARTIFACT__EXTENDS = ABSTRACT_ARTIFACT__EXTENDS;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MANAGED_ENTITY_ARTIFACT__ATTRIBUTES = ABSTRACT_ARTIFACT__ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>Literals</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MANAGED_ENTITY_ARTIFACT__LITERALS = ABSTRACT_ARTIFACT__LITERALS;

	/**
	 * The feature id for the '<em><b>Methods</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MANAGED_ENTITY_ARTIFACT__METHODS = ABSTRACT_ARTIFACT__METHODS;

	/**
	 * The feature id for the '<em><b>References</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MANAGED_ENTITY_ARTIFACT__REFERENCES = ABSTRACT_ARTIFACT__REFERENCES;

	/**
	 * The feature id for the '<em><b>Implements</b></em>' reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MANAGED_ENTITY_ARTIFACT__IMPLEMENTS = ABSTRACT_ARTIFACT__IMPLEMENTS;

	/**
	 * The number of structural features of the '<em>Managed Entity Artifact</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MANAGED_ENTITY_ARTIFACT_FEATURE_COUNT = ABSTRACT_ARTIFACT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.DatatypeArtifactImpl <em>Datatype Artifact</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.DatatypeArtifactImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getDatatypeArtifact()
	 * @generated
	 */
	int DATATYPE_ARTIFACT = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATATYPE_ARTIFACT__NAME = ABSTRACT_ARTIFACT__NAME;

	/**
	 * The feature id for the '<em><b>Stereotypes</b></em>' attribute list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATATYPE_ARTIFACT__STEREOTYPES = ABSTRACT_ARTIFACT__STEREOTYPES;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATATYPE_ARTIFACT__PROPERTIES = ABSTRACT_ARTIFACT__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATATYPE_ARTIFACT__PACKAGE = ABSTRACT_ARTIFACT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Is Readonly</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATATYPE_ARTIFACT__IS_READONLY = ABSTRACT_ARTIFACT__IS_READONLY;

	/**
	 * The feature id for the '<em><b>Is Abstract</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATATYPE_ARTIFACT__IS_ABSTRACT = ABSTRACT_ARTIFACT__IS_ABSTRACT;

	/**
	 * The feature id for the '<em><b>Extends</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATATYPE_ARTIFACT__EXTENDS = ABSTRACT_ARTIFACT__EXTENDS;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATATYPE_ARTIFACT__ATTRIBUTES = ABSTRACT_ARTIFACT__ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>Literals</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATATYPE_ARTIFACT__LITERALS = ABSTRACT_ARTIFACT__LITERALS;

	/**
	 * The feature id for the '<em><b>Methods</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATATYPE_ARTIFACT__METHODS = ABSTRACT_ARTIFACT__METHODS;

	/**
	 * The feature id for the '<em><b>References</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATATYPE_ARTIFACT__REFERENCES = ABSTRACT_ARTIFACT__REFERENCES;

	/**
	 * The feature id for the '<em><b>Implements</b></em>' reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATATYPE_ARTIFACT__IMPLEMENTS = ABSTRACT_ARTIFACT__IMPLEMENTS;

	/**
	 * The number of structural features of the '<em>Datatype Artifact</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DATATYPE_ARTIFACT_FEATURE_COUNT = ABSTRACT_ARTIFACT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.NotificationArtifactImpl <em>Notification Artifact</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.NotificationArtifactImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getNotificationArtifact()
	 * @generated
	 */
	int NOTIFICATION_ARTIFACT = 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NOTIFICATION_ARTIFACT__NAME = ABSTRACT_ARTIFACT__NAME;

	/**
	 * The feature id for the '<em><b>Stereotypes</b></em>' attribute list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NOTIFICATION_ARTIFACT__STEREOTYPES = ABSTRACT_ARTIFACT__STEREOTYPES;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NOTIFICATION_ARTIFACT__PROPERTIES = ABSTRACT_ARTIFACT__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NOTIFICATION_ARTIFACT__PACKAGE = ABSTRACT_ARTIFACT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Is Readonly</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NOTIFICATION_ARTIFACT__IS_READONLY = ABSTRACT_ARTIFACT__IS_READONLY;

	/**
	 * The feature id for the '<em><b>Is Abstract</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NOTIFICATION_ARTIFACT__IS_ABSTRACT = ABSTRACT_ARTIFACT__IS_ABSTRACT;

	/**
	 * The feature id for the '<em><b>Extends</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NOTIFICATION_ARTIFACT__EXTENDS = ABSTRACT_ARTIFACT__EXTENDS;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NOTIFICATION_ARTIFACT__ATTRIBUTES = ABSTRACT_ARTIFACT__ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>Literals</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NOTIFICATION_ARTIFACT__LITERALS = ABSTRACT_ARTIFACT__LITERALS;

	/**
	 * The feature id for the '<em><b>Methods</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NOTIFICATION_ARTIFACT__METHODS = ABSTRACT_ARTIFACT__METHODS;

	/**
	 * The feature id for the '<em><b>References</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NOTIFICATION_ARTIFACT__REFERENCES = ABSTRACT_ARTIFACT__REFERENCES;

	/**
	 * The feature id for the '<em><b>Implements</b></em>' reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NOTIFICATION_ARTIFACT__IMPLEMENTS = ABSTRACT_ARTIFACT__IMPLEMENTS;

	/**
	 * The number of structural features of the '<em>Notification Artifact</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NOTIFICATION_ARTIFACT_FEATURE_COUNT = ABSTRACT_ARTIFACT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.NamedQueryArtifactImpl <em>Named Query Artifact</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.NamedQueryArtifactImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getNamedQueryArtifact()
	 * @generated
	 */
	int NAMED_QUERY_ARTIFACT = 6;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMED_QUERY_ARTIFACT__NAME = ABSTRACT_ARTIFACT__NAME;

	/**
	 * The feature id for the '<em><b>Stereotypes</b></em>' attribute list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMED_QUERY_ARTIFACT__STEREOTYPES = ABSTRACT_ARTIFACT__STEREOTYPES;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMED_QUERY_ARTIFACT__PROPERTIES = ABSTRACT_ARTIFACT__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMED_QUERY_ARTIFACT__PACKAGE = ABSTRACT_ARTIFACT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Is Readonly</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMED_QUERY_ARTIFACT__IS_READONLY = ABSTRACT_ARTIFACT__IS_READONLY;

	/**
	 * The feature id for the '<em><b>Is Abstract</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMED_QUERY_ARTIFACT__IS_ABSTRACT = ABSTRACT_ARTIFACT__IS_ABSTRACT;

	/**
	 * The feature id for the '<em><b>Extends</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMED_QUERY_ARTIFACT__EXTENDS = ABSTRACT_ARTIFACT__EXTENDS;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMED_QUERY_ARTIFACT__ATTRIBUTES = ABSTRACT_ARTIFACT__ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>Literals</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMED_QUERY_ARTIFACT__LITERALS = ABSTRACT_ARTIFACT__LITERALS;

	/**
	 * The feature id for the '<em><b>Methods</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMED_QUERY_ARTIFACT__METHODS = ABSTRACT_ARTIFACT__METHODS;

	/**
	 * The feature id for the '<em><b>References</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMED_QUERY_ARTIFACT__REFERENCES = ABSTRACT_ARTIFACT__REFERENCES;

	/**
	 * The feature id for the '<em><b>Implements</b></em>' reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMED_QUERY_ARTIFACT__IMPLEMENTS = ABSTRACT_ARTIFACT__IMPLEMENTS;

	/**
	 * The feature id for the '<em><b>Returned Type</b></em>' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMED_QUERY_ARTIFACT__RETURNED_TYPE = ABSTRACT_ARTIFACT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Named Query Artifact</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMED_QUERY_ARTIFACT_FEATURE_COUNT = ABSTRACT_ARTIFACT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.EnumerationImpl <em>Enumeration</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.EnumerationImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getEnumeration()
	 * @generated
	 */
	int ENUMERATION = 7;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION__NAME = ABSTRACT_ARTIFACT__NAME;

	/**
	 * The feature id for the '<em><b>Stereotypes</b></em>' attribute list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION__STEREOTYPES = ABSTRACT_ARTIFACT__STEREOTYPES;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION__PROPERTIES = ABSTRACT_ARTIFACT__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION__PACKAGE = ABSTRACT_ARTIFACT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Is Readonly</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION__IS_READONLY = ABSTRACT_ARTIFACT__IS_READONLY;

	/**
	 * The feature id for the '<em><b>Is Abstract</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION__IS_ABSTRACT = ABSTRACT_ARTIFACT__IS_ABSTRACT;

	/**
	 * The feature id for the '<em><b>Extends</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION__EXTENDS = ABSTRACT_ARTIFACT__EXTENDS;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION__ATTRIBUTES = ABSTRACT_ARTIFACT__ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>Literals</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION__LITERALS = ABSTRACT_ARTIFACT__LITERALS;

	/**
	 * The feature id for the '<em><b>Methods</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION__METHODS = ABSTRACT_ARTIFACT__METHODS;

	/**
	 * The feature id for the '<em><b>References</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION__REFERENCES = ABSTRACT_ARTIFACT__REFERENCES;

	/**
	 * The feature id for the '<em><b>Implements</b></em>' reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION__IMPLEMENTS = ABSTRACT_ARTIFACT__IMPLEMENTS;

	/**
	 * The feature id for the '<em><b>Base Type</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION__BASE_TYPE = ABSTRACT_ARTIFACT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Enumeration</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ENUMERATION_FEATURE_COUNT = ABSTRACT_ARTIFACT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.UpdateProcedureArtifactImpl <em>Update Procedure Artifact</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.UpdateProcedureArtifactImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getUpdateProcedureArtifact()
	 * @generated
	 */
	int UPDATE_PROCEDURE_ARTIFACT = 8;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_PROCEDURE_ARTIFACT__NAME = ABSTRACT_ARTIFACT__NAME;

	/**
	 * The feature id for the '<em><b>Stereotypes</b></em>' attribute list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_PROCEDURE_ARTIFACT__STEREOTYPES = ABSTRACT_ARTIFACT__STEREOTYPES;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_PROCEDURE_ARTIFACT__PROPERTIES = ABSTRACT_ARTIFACT__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_PROCEDURE_ARTIFACT__PACKAGE = ABSTRACT_ARTIFACT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Is Readonly</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_PROCEDURE_ARTIFACT__IS_READONLY = ABSTRACT_ARTIFACT__IS_READONLY;

	/**
	 * The feature id for the '<em><b>Is Abstract</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_PROCEDURE_ARTIFACT__IS_ABSTRACT = ABSTRACT_ARTIFACT__IS_ABSTRACT;

	/**
	 * The feature id for the '<em><b>Extends</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_PROCEDURE_ARTIFACT__EXTENDS = ABSTRACT_ARTIFACT__EXTENDS;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_PROCEDURE_ARTIFACT__ATTRIBUTES = ABSTRACT_ARTIFACT__ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>Literals</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_PROCEDURE_ARTIFACT__LITERALS = ABSTRACT_ARTIFACT__LITERALS;

	/**
	 * The feature id for the '<em><b>Methods</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_PROCEDURE_ARTIFACT__METHODS = ABSTRACT_ARTIFACT__METHODS;

	/**
	 * The feature id for the '<em><b>References</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_PROCEDURE_ARTIFACT__REFERENCES = ABSTRACT_ARTIFACT__REFERENCES;

	/**
	 * The feature id for the '<em><b>Implements</b></em>' reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_PROCEDURE_ARTIFACT__IMPLEMENTS = ABSTRACT_ARTIFACT__IMPLEMENTS;

	/**
	 * The number of structural features of the '<em>Update Procedure Artifact</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UPDATE_PROCEDURE_ARTIFACT_FEATURE_COUNT = ABSTRACT_ARTIFACT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.ExceptionArtifactImpl <em>Exception Artifact</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.ExceptionArtifactImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getExceptionArtifact()
	 * @generated
	 */
	int EXCEPTION_ARTIFACT = 9;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EXCEPTION_ARTIFACT__NAME = ABSTRACT_ARTIFACT__NAME;

	/**
	 * The feature id for the '<em><b>Stereotypes</b></em>' attribute list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EXCEPTION_ARTIFACT__STEREOTYPES = ABSTRACT_ARTIFACT__STEREOTYPES;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EXCEPTION_ARTIFACT__PROPERTIES = ABSTRACT_ARTIFACT__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EXCEPTION_ARTIFACT__PACKAGE = ABSTRACT_ARTIFACT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Is Readonly</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EXCEPTION_ARTIFACT__IS_READONLY = ABSTRACT_ARTIFACT__IS_READONLY;

	/**
	 * The feature id for the '<em><b>Is Abstract</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EXCEPTION_ARTIFACT__IS_ABSTRACT = ABSTRACT_ARTIFACT__IS_ABSTRACT;

	/**
	 * The feature id for the '<em><b>Extends</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EXCEPTION_ARTIFACT__EXTENDS = ABSTRACT_ARTIFACT__EXTENDS;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EXCEPTION_ARTIFACT__ATTRIBUTES = ABSTRACT_ARTIFACT__ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>Literals</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EXCEPTION_ARTIFACT__LITERALS = ABSTRACT_ARTIFACT__LITERALS;

	/**
	 * The feature id for the '<em><b>Methods</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EXCEPTION_ARTIFACT__METHODS = ABSTRACT_ARTIFACT__METHODS;

	/**
	 * The feature id for the '<em><b>References</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EXCEPTION_ARTIFACT__REFERENCES = ABSTRACT_ARTIFACT__REFERENCES;

	/**
	 * The feature id for the '<em><b>Implements</b></em>' reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EXCEPTION_ARTIFACT__IMPLEMENTS = ABSTRACT_ARTIFACT__IMPLEMENTS;

	/**
	 * The number of structural features of the '<em>Exception Artifact</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int EXCEPTION_ARTIFACT_FEATURE_COUNT = ABSTRACT_ARTIFACT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.SessionFacadeArtifactImpl <em>Session Facade Artifact</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.SessionFacadeArtifactImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getSessionFacadeArtifact()
	 * @generated
	 */
	int SESSION_FACADE_ARTIFACT = 10;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SESSION_FACADE_ARTIFACT__NAME = ABSTRACT_ARTIFACT__NAME;

	/**
	 * The feature id for the '<em><b>Stereotypes</b></em>' attribute list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SESSION_FACADE_ARTIFACT__STEREOTYPES = ABSTRACT_ARTIFACT__STEREOTYPES;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SESSION_FACADE_ARTIFACT__PROPERTIES = ABSTRACT_ARTIFACT__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SESSION_FACADE_ARTIFACT__PACKAGE = ABSTRACT_ARTIFACT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Is Readonly</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SESSION_FACADE_ARTIFACT__IS_READONLY = ABSTRACT_ARTIFACT__IS_READONLY;

	/**
	 * The feature id for the '<em><b>Is Abstract</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SESSION_FACADE_ARTIFACT__IS_ABSTRACT = ABSTRACT_ARTIFACT__IS_ABSTRACT;

	/**
	 * The feature id for the '<em><b>Extends</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SESSION_FACADE_ARTIFACT__EXTENDS = ABSTRACT_ARTIFACT__EXTENDS;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SESSION_FACADE_ARTIFACT__ATTRIBUTES = ABSTRACT_ARTIFACT__ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>Literals</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SESSION_FACADE_ARTIFACT__LITERALS = ABSTRACT_ARTIFACT__LITERALS;

	/**
	 * The feature id for the '<em><b>Methods</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SESSION_FACADE_ARTIFACT__METHODS = ABSTRACT_ARTIFACT__METHODS;

	/**
	 * The feature id for the '<em><b>References</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SESSION_FACADE_ARTIFACT__REFERENCES = ABSTRACT_ARTIFACT__REFERENCES;

	/**
	 * The feature id for the '<em><b>Implements</b></em>' reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SESSION_FACADE_ARTIFACT__IMPLEMENTS = ABSTRACT_ARTIFACT__IMPLEMENTS;

	/**
	 * The feature id for the '<em><b>Managed Entities</b></em>' reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SESSION_FACADE_ARTIFACT__MANAGED_ENTITIES = ABSTRACT_ARTIFACT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Emitted Notifications</b></em>'
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SESSION_FACADE_ARTIFACT__EMITTED_NOTIFICATIONS = ABSTRACT_ARTIFACT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Named Queries</b></em>' reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SESSION_FACADE_ARTIFACT__NAMED_QUERIES = ABSTRACT_ARTIFACT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Exposed Procedures</b></em>'
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SESSION_FACADE_ARTIFACT__EXPOSED_PROCEDURES = ABSTRACT_ARTIFACT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Session Facade Artifact</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int SESSION_FACADE_ARTIFACT_FEATURE_COUNT = ABSTRACT_ARTIFACT_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationImpl <em>Association</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getAssociation()
	 * @generated
	 */
	int ASSOCIATION = 11;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__NAME = QUALIFIED_NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Stereotypes</b></em>' attribute list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__STEREOTYPES = QUALIFIED_NAMED_ELEMENT__STEREOTYPES;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__PROPERTIES = QUALIFIED_NAMED_ELEMENT__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__PACKAGE = QUALIFIED_NAMED_ELEMENT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Is Readonly</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__IS_READONLY = QUALIFIED_NAMED_ELEMENT__IS_READONLY;

	/**
	 * The feature id for the '<em><b>Is Abstract</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__IS_ABSTRACT = QUALIFIED_NAMED_ELEMENT__IS_ABSTRACT;

	/**
	 * The feature id for the '<em><b>AEnd</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__AEND = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>AEnd Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__AEND_NAME = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>AEnd Multiplicity</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__AEND_MULTIPLICITY = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>AEnd Is Navigable</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__AEND_IS_NAVIGABLE = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>AEnd Is Ordered</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__AEND_IS_ORDERED = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>AEnd Is Unique</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__AEND_IS_UNIQUE = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>AEnd Is Changeable</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__AEND_IS_CHANGEABLE = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>AEnd Aggregation</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__AEND_AGGREGATION = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>ZEnd</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__ZEND = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>ZEnd Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__ZEND_NAME = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>ZEnd Multiplicity</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__ZEND_MULTIPLICITY = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>ZEnd Is Navigable</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__ZEND_IS_NAVIGABLE = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>ZEnd Is Ordered</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__ZEND_IS_ORDERED = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 12;

	/**
	 * The feature id for the '<em><b>ZEnd Is Unique</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__ZEND_IS_UNIQUE = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 13;

	/**
	 * The feature id for the '<em><b>ZEnd Is Changeable</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__ZEND_IS_CHANGEABLE = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 14;

	/**
	 * The feature id for the '<em><b>ZEnd Aggregation</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__ZEND_AGGREGATION = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 15;

	/**
	 * The feature id for the '<em><b>AEnd Visibility</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__AEND_VISIBILITY = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 16;

	/**
	 * The feature id for the '<em><b>ZEnd Visibility</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION__ZEND_VISIBILITY = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 17;

	/**
	 * The number of structural features of the '<em>Association</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_FEATURE_COUNT = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 18;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationClassImpl <em>Association Class</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationClassImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getAssociationClass()
	 * @generated
	 */
	int ASSOCIATION_CLASS = 12;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS__NAME = ASSOCIATION__NAME;

	/**
	 * The feature id for the '<em><b>Stereotypes</b></em>' attribute list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS__STEREOTYPES = ASSOCIATION__STEREOTYPES;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS__PROPERTIES = ASSOCIATION__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS__PACKAGE = ASSOCIATION__PACKAGE;

	/**
	 * The feature id for the '<em><b>Is Readonly</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS__IS_READONLY = ASSOCIATION__IS_READONLY;

	/**
	 * The feature id for the '<em><b>Is Abstract</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS__IS_ABSTRACT = ASSOCIATION__IS_ABSTRACT;

	/**
	 * The feature id for the '<em><b>AEnd</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS__AEND = ASSOCIATION__AEND;

	/**
	 * The feature id for the '<em><b>AEnd Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS__AEND_NAME = ASSOCIATION__AEND_NAME;

	/**
	 * The feature id for the '<em><b>AEnd Multiplicity</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS__AEND_MULTIPLICITY = ASSOCIATION__AEND_MULTIPLICITY;

	/**
	 * The feature id for the '<em><b>AEnd Is Navigable</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS__AEND_IS_NAVIGABLE = ASSOCIATION__AEND_IS_NAVIGABLE;

	/**
	 * The feature id for the '<em><b>AEnd Is Ordered</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS__AEND_IS_ORDERED = ASSOCIATION__AEND_IS_ORDERED;

	/**
	 * The feature id for the '<em><b>AEnd Is Unique</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS__AEND_IS_UNIQUE = ASSOCIATION__AEND_IS_UNIQUE;

	/**
	 * The feature id for the '<em><b>AEnd Is Changeable</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS__AEND_IS_CHANGEABLE = ASSOCIATION__AEND_IS_CHANGEABLE;

	/**
	 * The feature id for the '<em><b>AEnd Aggregation</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS__AEND_AGGREGATION = ASSOCIATION__AEND_AGGREGATION;

	/**
	 * The feature id for the '<em><b>ZEnd</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS__ZEND = ASSOCIATION__ZEND;

	/**
	 * The feature id for the '<em><b>ZEnd Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS__ZEND_NAME = ASSOCIATION__ZEND_NAME;

	/**
	 * The feature id for the '<em><b>ZEnd Multiplicity</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS__ZEND_MULTIPLICITY = ASSOCIATION__ZEND_MULTIPLICITY;

	/**
	 * The feature id for the '<em><b>ZEnd Is Navigable</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS__ZEND_IS_NAVIGABLE = ASSOCIATION__ZEND_IS_NAVIGABLE;

	/**
	 * The feature id for the '<em><b>ZEnd Is Ordered</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS__ZEND_IS_ORDERED = ASSOCIATION__ZEND_IS_ORDERED;

	/**
	 * The feature id for the '<em><b>ZEnd Is Unique</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS__ZEND_IS_UNIQUE = ASSOCIATION__ZEND_IS_UNIQUE;

	/**
	 * The feature id for the '<em><b>ZEnd Is Changeable</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS__ZEND_IS_CHANGEABLE = ASSOCIATION__ZEND_IS_CHANGEABLE;

	/**
	 * The feature id for the '<em><b>ZEnd Aggregation</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS__ZEND_AGGREGATION = ASSOCIATION__ZEND_AGGREGATION;

	/**
	 * The feature id for the '<em><b>AEnd Visibility</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS__AEND_VISIBILITY = ASSOCIATION__AEND_VISIBILITY;

	/**
	 * The feature id for the '<em><b>ZEnd Visibility</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS__ZEND_VISIBILITY = ASSOCIATION__ZEND_VISIBILITY;

	/**
	 * The feature id for the '<em><b>Associated Class</b></em>' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS__ASSOCIATED_CLASS = ASSOCIATION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Association Class</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS_FEATURE_COUNT = ASSOCIATION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.TypedElementImpl <em>Typed Element</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.TypedElementImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getTypedElement()
	 * @generated
	 */
	int TYPED_ELEMENT = 13;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPED_ELEMENT__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Stereotypes</b></em>' attribute list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPED_ELEMENT__STEREOTYPES = NAMED_ELEMENT__STEREOTYPES;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPED_ELEMENT__PROPERTIES = NAMED_ELEMENT__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPED_ELEMENT__TYPE = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Multiplicity</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPED_ELEMENT__MULTIPLICITY = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPED_ELEMENT__VISIBILITY = NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Is Ordered</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPED_ELEMENT__IS_ORDERED = NAMED_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Is Unique</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPED_ELEMENT__IS_UNIQUE = NAMED_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Type Multiplicity</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPED_ELEMENT__TYPE_MULTIPLICITY = NAMED_ELEMENT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPED_ELEMENT__DEFAULT_VALUE = NAMED_ELEMENT_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Typed Element</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPED_ELEMENT_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 7;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AttributeImpl <em>Attribute</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AttributeImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getAttribute()
	 * @generated
	 */
	int ATTRIBUTE = 14;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__NAME = TYPED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Stereotypes</b></em>' attribute list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__STEREOTYPES = TYPED_ELEMENT__STEREOTYPES;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__PROPERTIES = TYPED_ELEMENT__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__TYPE = TYPED_ELEMENT__TYPE;

	/**
	 * The feature id for the '<em><b>Multiplicity</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__MULTIPLICITY = TYPED_ELEMENT__MULTIPLICITY;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__VISIBILITY = TYPED_ELEMENT__VISIBILITY;

	/**
	 * The feature id for the '<em><b>Is Ordered</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__IS_ORDERED = TYPED_ELEMENT__IS_ORDERED;

	/**
	 * The feature id for the '<em><b>Is Unique</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__IS_UNIQUE = TYPED_ELEMENT__IS_UNIQUE;

	/**
	 * The feature id for the '<em><b>Type Multiplicity</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__TYPE_MULTIPLICITY = TYPED_ELEMENT__TYPE_MULTIPLICITY;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__DEFAULT_VALUE = TYPED_ELEMENT__DEFAULT_VALUE;

	/**
	 * The number of structural features of the '<em>Attribute</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_FEATURE_COUNT = TYPED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.MethodImpl <em>Method</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.MethodImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getMethod()
	 * @generated
	 */
	int METHOD = 15;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int METHOD__NAME = TYPED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Stereotypes</b></em>' attribute list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int METHOD__STEREOTYPES = TYPED_ELEMENT__STEREOTYPES;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int METHOD__PROPERTIES = TYPED_ELEMENT__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int METHOD__TYPE = TYPED_ELEMENT__TYPE;

	/**
	 * The feature id for the '<em><b>Multiplicity</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int METHOD__MULTIPLICITY = TYPED_ELEMENT__MULTIPLICITY;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int METHOD__VISIBILITY = TYPED_ELEMENT__VISIBILITY;

	/**
	 * The feature id for the '<em><b>Is Ordered</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int METHOD__IS_ORDERED = TYPED_ELEMENT__IS_ORDERED;

	/**
	 * The feature id for the '<em><b>Is Unique</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int METHOD__IS_UNIQUE = TYPED_ELEMENT__IS_UNIQUE;

	/**
	 * The feature id for the '<em><b>Type Multiplicity</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int METHOD__TYPE_MULTIPLICITY = TYPED_ELEMENT__TYPE_MULTIPLICITY;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int METHOD__DEFAULT_VALUE = TYPED_ELEMENT__DEFAULT_VALUE;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int METHOD__PARAMETERS = TYPED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Abstract</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int METHOD__IS_ABSTRACT = TYPED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Method</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int METHOD_FEATURE_COUNT = TYPED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.LiteralImpl <em>Literal</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.LiteralImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getLiteral()
	 * @generated
	 */
	int LITERAL = 16;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LITERAL__NAME = TYPED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Stereotypes</b></em>' attribute list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LITERAL__STEREOTYPES = TYPED_ELEMENT__STEREOTYPES;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LITERAL__PROPERTIES = TYPED_ELEMENT__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LITERAL__TYPE = TYPED_ELEMENT__TYPE;

	/**
	 * The feature id for the '<em><b>Multiplicity</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LITERAL__MULTIPLICITY = TYPED_ELEMENT__MULTIPLICITY;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LITERAL__VISIBILITY = TYPED_ELEMENT__VISIBILITY;

	/**
	 * The feature id for the '<em><b>Is Ordered</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LITERAL__IS_ORDERED = TYPED_ELEMENT__IS_ORDERED;

	/**
	 * The feature id for the '<em><b>Is Unique</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LITERAL__IS_UNIQUE = TYPED_ELEMENT__IS_UNIQUE;

	/**
	 * The feature id for the '<em><b>Type Multiplicity</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LITERAL__TYPE_MULTIPLICITY = TYPED_ELEMENT__TYPE_MULTIPLICITY;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LITERAL__DEFAULT_VALUE = TYPED_ELEMENT__DEFAULT_VALUE;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LITERAL__VALUE = TYPED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Literal</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LITERAL_FEATURE_COUNT = TYPED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.ParameterImpl <em>Parameter</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.ParameterImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getParameter()
	 * @generated
	 */
	int PARAMETER = 17;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PARAMETER__NAME = TYPED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Stereotypes</b></em>' attribute list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PARAMETER__STEREOTYPES = TYPED_ELEMENT__STEREOTYPES;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PARAMETER__PROPERTIES = TYPED_ELEMENT__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PARAMETER__TYPE = TYPED_ELEMENT__TYPE;

	/**
	 * The feature id for the '<em><b>Multiplicity</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PARAMETER__MULTIPLICITY = TYPED_ELEMENT__MULTIPLICITY;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PARAMETER__VISIBILITY = TYPED_ELEMENT__VISIBILITY;

	/**
	 * The feature id for the '<em><b>Is Ordered</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PARAMETER__IS_ORDERED = TYPED_ELEMENT__IS_ORDERED;

	/**
	 * The feature id for the '<em><b>Is Unique</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PARAMETER__IS_UNIQUE = TYPED_ELEMENT__IS_UNIQUE;

	/**
	 * The feature id for the '<em><b>Type Multiplicity</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PARAMETER__TYPE_MULTIPLICITY = TYPED_ELEMENT__TYPE_MULTIPLICITY;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PARAMETER__DEFAULT_VALUE = TYPED_ELEMENT__DEFAULT_VALUE;

	/**
	 * The number of structural features of the '<em>Parameter</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PARAMETER_FEATURE_COUNT = TYPED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.MapImpl <em>Map</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.MapImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getMap()
	 * @generated
	 */
	int MAP = 18;

	/**
	 * The feature id for the '<em><b>Artifacts</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MAP__ARTIFACTS = 0;

	/**
	 * The feature id for the '<em><b>Associations</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MAP__ASSOCIATIONS = 1;

	/**
	 * The feature id for the '<em><b>Dependencies</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MAP__DEPENDENCIES = 2;

	/**
	 * The feature id for the '<em><b>Base Package</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MAP__BASE_PACKAGE = 3;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MAP__PROPERTIES = 4;

	/**
	 * The number of structural features of the '<em>Map</em>' class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int MAP_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.ReferenceImpl <em>Reference</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.ReferenceImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getReference()
	 * @generated
	 */
	int REFERENCE = 19;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Stereotypes</b></em>' attribute list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE__STEREOTYPES = NAMED_ELEMENT__STEREOTYPES;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE__PROPERTIES = NAMED_ELEMENT__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Multiplicity</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE__MULTIPLICITY = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>ZEnd</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE__ZEND = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Type Multiplicity</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE__TYPE_MULTIPLICITY = NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Is Ordered</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE__IS_ORDERED = NAMED_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Is Unique</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE__IS_UNIQUE = NAMED_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Reference</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int REFERENCE_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.DependencyImpl <em>Dependency</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.DependencyImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getDependency()
	 * @generated
	 */
	int DEPENDENCY = 20;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY__NAME = QUALIFIED_NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Stereotypes</b></em>' attribute list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY__STEREOTYPES = QUALIFIED_NAMED_ELEMENT__STEREOTYPES;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY__PROPERTIES = QUALIFIED_NAMED_ELEMENT__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY__PACKAGE = QUALIFIED_NAMED_ELEMENT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Is Readonly</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY__IS_READONLY = QUALIFIED_NAMED_ELEMENT__IS_READONLY;

	/**
	 * The feature id for the '<em><b>Is Abstract</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY__IS_ABSTRACT = QUALIFIED_NAMED_ELEMENT__IS_ABSTRACT;

	/**
	 * The feature id for the '<em><b>AEnd</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY__AEND = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>AEnd Multiplicity</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY__AEND_MULTIPLICITY = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>AEnd Is Navigable</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY__AEND_IS_NAVIGABLE = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>ZEnd</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY__ZEND = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>ZEnd Multiplicity</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY__ZEND_MULTIPLICITY = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>ZEnd Is Navigable</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY__ZEND_IS_NAVIGABLE = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Dependency</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY_FEATURE_COUNT = QUALIFIED_NAMED_ELEMENT_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationClassClassImpl <em>Association Class Class</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationClassClassImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getAssociationClassClass()
	 * @generated
	 */
	int ASSOCIATION_CLASS_CLASS = 21;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS_CLASS__NAME = ABSTRACT_ARTIFACT__NAME;

	/**
	 * The feature id for the '<em><b>Stereotypes</b></em>' attribute list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS_CLASS__STEREOTYPES = ABSTRACT_ARTIFACT__STEREOTYPES;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS_CLASS__PROPERTIES = ABSTRACT_ARTIFACT__PROPERTIES;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS_CLASS__PACKAGE = ABSTRACT_ARTIFACT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Is Readonly</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS_CLASS__IS_READONLY = ABSTRACT_ARTIFACT__IS_READONLY;

	/**
	 * The feature id for the '<em><b>Is Abstract</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS_CLASS__IS_ABSTRACT = ABSTRACT_ARTIFACT__IS_ABSTRACT;

	/**
	 * The feature id for the '<em><b>Extends</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS_CLASS__EXTENDS = ABSTRACT_ARTIFACT__EXTENDS;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS_CLASS__ATTRIBUTES = ABSTRACT_ARTIFACT__ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>Literals</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS_CLASS__LITERALS = ABSTRACT_ARTIFACT__LITERALS;

	/**
	 * The feature id for the '<em><b>Methods</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS_CLASS__METHODS = ABSTRACT_ARTIFACT__METHODS;

	/**
	 * The feature id for the '<em><b>References</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS_CLASS__REFERENCES = ABSTRACT_ARTIFACT__REFERENCES;

	/**
	 * The feature id for the '<em><b>Implements</b></em>' reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS_CLASS__IMPLEMENTS = ABSTRACT_ARTIFACT__IMPLEMENTS;

	/**
	 * The number of structural features of the '<em>Association Class Class</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_CLASS_CLASS_FEATURE_COUNT = ABSTRACT_ARTIFACT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.DiagramPropertyImpl <em>Diagram Property</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.DiagramPropertyImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getDiagramProperty()
	 * @generated
	 */
	int DIAGRAM_PROPERTY = 22;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_PROPERTY__NAME = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_PROPERTY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Diagram Property</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_PROPERTY_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AggregationEnum <em>Aggregation Enum</em>}'
	 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AggregationEnum
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getAggregationEnum()
	 * @generated
	 */
	int AGGREGATION_ENUM = 23;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.ChangeableEnum <em>Changeable Enum</em>}'
	 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.ChangeableEnum
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getChangeableEnum()
	 * @generated
	 */
	int CHANGEABLE_ENUM = 24;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity <em>Assoc Multiplicity</em>}'
	 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getAssocMultiplicity()
	 * @generated
	 */
	int ASSOC_MULTIPLICITY = 25;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.TypeMultiplicity <em>Type Multiplicity</em>}'
	 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.TypeMultiplicity
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getTypeMultiplicity()
	 * @generated
	 */
	int TYPE_MULTIPLICITY = 26;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Visibility <em>Visibility</em>}'
	 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Visibility
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getVisibility()
	 * @generated
	 */
	int VISIBILITY = 27;

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedElement <em>Named Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Named Element</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedElement
	 * @generated
	 */
	EClass getNamedElement();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedElement#getName <em>Name</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedElement#getName()
	 * @see #getNamedElement()
	 * @generated
	 */
	EAttribute getNamedElement_Name();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedElement#getStereotypes <em>Stereotypes</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute list '<em>Stereotypes</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedElement#getStereotypes()
	 * @see #getNamedElement()
	 * @generated
	 */
	EAttribute getNamedElement_Stereotypes();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedElement#getProperties <em>Properties</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Properties</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedElement#getProperties()
	 * @see #getNamedElement()
	 * @generated
	 */
	EReference getNamedElement_Properties();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement <em>Qualified Named Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Qualified Named Element</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement
	 * @generated
	 */
	EClass getQualifiedNamedElement();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement#getPackage <em>Package</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Package</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement#getPackage()
	 * @see #getQualifiedNamedElement()
	 * @generated
	 */
	EAttribute getQualifiedNamedElement_Package();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement#isIsReadonly <em>Is Readonly</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Is Readonly</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement#isIsReadonly()
	 * @see #getQualifiedNamedElement()
	 * @generated
	 */
	EAttribute getQualifiedNamedElement_IsReadonly();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement#isIsAbstract <em>Is Abstract</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Is Abstract</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement#isIsAbstract()
	 * @see #getQualifiedNamedElement()
	 * @generated
	 */
	EAttribute getQualifiedNamedElement_IsAbstract();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact <em>Abstract Artifact</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Abstract Artifact</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact
	 * @generated
	 */
	EClass getAbstractArtifact();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact#getExtends <em>Extends</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Extends</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact#getExtends()
	 * @see #getAbstractArtifact()
	 * @generated
	 */
	EReference getAbstractArtifact_Extends();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact#getAttributes <em>Attributes</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Attributes</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact#getAttributes()
	 * @see #getAbstractArtifact()
	 * @generated
	 */
	EReference getAbstractArtifact_Attributes();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact#getLiterals <em>Literals</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Literals</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact#getLiterals()
	 * @see #getAbstractArtifact()
	 * @generated
	 */
	EReference getAbstractArtifact_Literals();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact#getMethods <em>Methods</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Methods</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact#getMethods()
	 * @see #getAbstractArtifact()
	 * @generated
	 */
	EReference getAbstractArtifact_Methods();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact#getReferences <em>References</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>References</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact#getReferences()
	 * @see #getAbstractArtifact()
	 * @generated
	 */
	EReference getAbstractArtifact_References();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact#getImplements <em>Implements</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference list '<em>Implements</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact#getImplements()
	 * @see #getAbstractArtifact()
	 * @generated
	 */
	EReference getAbstractArtifact_Implements();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.ManagedEntityArtifact <em>Managed Entity Artifact</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Managed Entity Artifact</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.ManagedEntityArtifact
	 * @generated
	 */
	EClass getManagedEntityArtifact();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.DatatypeArtifact <em>Datatype Artifact</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Datatype Artifact</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.DatatypeArtifact
	 * @generated
	 */
	EClass getDatatypeArtifact();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.NotificationArtifact <em>Notification Artifact</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Notification Artifact</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.NotificationArtifact
	 * @generated
	 */
	EClass getNotificationArtifact();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact <em>Named Query Artifact</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Named Query Artifact</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact
	 * @generated
	 */
	EClass getNamedQueryArtifact();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact#getReturnedType <em>Returned Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Returned Type</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact#getReturnedType()
	 * @see #getNamedQueryArtifact()
	 * @generated
	 */
	EReference getNamedQueryArtifact_ReturnedType();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Enumeration <em>Enumeration</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Enumeration</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Enumeration
	 * @generated
	 */
	EClass getEnumeration();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Enumeration#getBaseType <em>Base Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Base Type</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Enumeration#getBaseType()
	 * @see #getEnumeration()
	 * @generated
	 */
	EAttribute getEnumeration_BaseType();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.UpdateProcedureArtifact <em>Update Procedure Artifact</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Update Procedure Artifact</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.UpdateProcedureArtifact
	 * @generated
	 */
	EClass getUpdateProcedureArtifact();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.ExceptionArtifact <em>Exception Artifact</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Exception Artifact</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.ExceptionArtifact
	 * @generated
	 */
	EClass getExceptionArtifact();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact <em>Session Facade Artifact</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Session Facade Artifact</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact
	 * @generated
	 */
	EClass getSessionFacadeArtifact();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact#getManagedEntities <em>Managed Entities</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference list '<em>Managed Entities</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact#getManagedEntities()
	 * @see #getSessionFacadeArtifact()
	 * @generated
	 */
	EReference getSessionFacadeArtifact_ManagedEntities();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact#getEmittedNotifications <em>Emitted Notifications</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference list '<em>Emitted Notifications</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact#getEmittedNotifications()
	 * @see #getSessionFacadeArtifact()
	 * @generated
	 */
	EReference getSessionFacadeArtifact_EmittedNotifications();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact#getNamedQueries <em>Named Queries</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference list '<em>Named Queries</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact#getNamedQueries()
	 * @see #getSessionFacadeArtifact()
	 * @generated
	 */
	EReference getSessionFacadeArtifact_NamedQueries();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact#getExposedProcedures <em>Exposed Procedures</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference list '<em>Exposed Procedures</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact#getExposedProcedures()
	 * @see #getSessionFacadeArtifact()
	 * @generated
	 */
	EReference getSessionFacadeArtifact_ExposedProcedures();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association <em>Association</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Association</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Association
	 * @generated
	 */
	EClass getAssociation();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getAEnd <em>AEnd</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>AEnd</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getAEnd()
	 * @see #getAssociation()
	 * @generated
	 */
	EReference getAssociation_AEnd();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getAEndName <em>AEnd Name</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>AEnd Name</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getAEndName()
	 * @see #getAssociation()
	 * @generated
	 */
	EAttribute getAssociation_AEndName();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getAEndMultiplicity <em>AEnd Multiplicity</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>AEnd Multiplicity</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getAEndMultiplicity()
	 * @see #getAssociation()
	 * @generated
	 */
	EAttribute getAssociation_AEndMultiplicity();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#isAEndIsNavigable <em>AEnd Is Navigable</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>AEnd Is Navigable</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#isAEndIsNavigable()
	 * @see #getAssociation()
	 * @generated
	 */
	EAttribute getAssociation_AEndIsNavigable();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#isAEndIsOrdered <em>AEnd Is Ordered</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>AEnd Is Ordered</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#isAEndIsOrdered()
	 * @see #getAssociation()
	 * @generated
	 */
	EAttribute getAssociation_AEndIsOrdered();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#isAEndIsUnique <em>AEnd Is Unique</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>AEnd Is Unique</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#isAEndIsUnique()
	 * @see #getAssociation()
	 * @generated
	 */
	EAttribute getAssociation_AEndIsUnique();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getAEndIsChangeable <em>AEnd Is Changeable</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>AEnd Is Changeable</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getAEndIsChangeable()
	 * @see #getAssociation()
	 * @generated
	 */
	EAttribute getAssociation_AEndIsChangeable();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getAEndAggregation <em>AEnd Aggregation</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>AEnd Aggregation</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getAEndAggregation()
	 * @see #getAssociation()
	 * @generated
	 */
	EAttribute getAssociation_AEndAggregation();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getZEnd <em>ZEnd</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>ZEnd</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getZEnd()
	 * @see #getAssociation()
	 * @generated
	 */
	EReference getAssociation_ZEnd();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getZEndName <em>ZEnd Name</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>ZEnd Name</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getZEndName()
	 * @see #getAssociation()
	 * @generated
	 */
	EAttribute getAssociation_ZEndName();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getZEndMultiplicity <em>ZEnd Multiplicity</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>ZEnd Multiplicity</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getZEndMultiplicity()
	 * @see #getAssociation()
	 * @generated
	 */
	EAttribute getAssociation_ZEndMultiplicity();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#isZEndIsNavigable <em>ZEnd Is Navigable</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>ZEnd Is Navigable</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#isZEndIsNavigable()
	 * @see #getAssociation()
	 * @generated
	 */
	EAttribute getAssociation_ZEndIsNavigable();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#isZEndIsOrdered <em>ZEnd Is Ordered</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>ZEnd Is Ordered</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#isZEndIsOrdered()
	 * @see #getAssociation()
	 * @generated
	 */
	EAttribute getAssociation_ZEndIsOrdered();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#isZEndIsUnique <em>ZEnd Is Unique</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>ZEnd Is Unique</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#isZEndIsUnique()
	 * @see #getAssociation()
	 * @generated
	 */
	EAttribute getAssociation_ZEndIsUnique();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getZEndIsChangeable <em>ZEnd Is Changeable</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>ZEnd Is Changeable</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getZEndIsChangeable()
	 * @see #getAssociation()
	 * @generated
	 */
	EAttribute getAssociation_ZEndIsChangeable();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getZEndAggregation <em>ZEnd Aggregation</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>ZEnd Aggregation</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getZEndAggregation()
	 * @see #getAssociation()
	 * @generated
	 */
	EAttribute getAssociation_ZEndAggregation();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getAEndVisibility <em>AEnd Visibility</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>AEnd Visibility</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getAEndVisibility()
	 * @see #getAssociation()
	 * @generated
	 */
	EAttribute getAssociation_AEndVisibility();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getZEndVisibility <em>ZEnd Visibility</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>ZEnd Visibility</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getZEndVisibility()
	 * @see #getAssociation()
	 * @generated
	 */
	EAttribute getAssociation_ZEndVisibility();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass <em>Association Class</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Association Class</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass
	 * @generated
	 */
	EClass getAssociationClass();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass#getAssociatedClass <em>Associated Class</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>Associated Class</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass#getAssociatedClass()
	 * @see #getAssociationClass()
	 * @generated
	 */
	EReference getAssociationClass_AssociatedClass();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.TypedElement <em>Typed Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Typed Element</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.TypedElement
	 * @generated
	 */
	EClass getTypedElement();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.TypedElement#getType <em>Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.TypedElement#getType()
	 * @see #getTypedElement()
	 * @generated
	 */
	EAttribute getTypedElement_Type();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.TypedElement#getMultiplicity <em>Multiplicity</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Multiplicity</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.TypedElement#getMultiplicity()
	 * @see #getTypedElement()
	 * @generated
	 */
	EAttribute getTypedElement_Multiplicity();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.TypedElement#getVisibility <em>Visibility</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Visibility</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.TypedElement#getVisibility()
	 * @see #getTypedElement()
	 * @generated
	 */
	EAttribute getTypedElement_Visibility();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.TypedElement#isIsOrdered <em>Is Ordered</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Is Ordered</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.TypedElement#isIsOrdered()
	 * @see #getTypedElement()
	 * @generated
	 */
	EAttribute getTypedElement_IsOrdered();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.TypedElement#isIsUnique <em>Is Unique</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Is Unique</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.TypedElement#isIsUnique()
	 * @see #getTypedElement()
	 * @generated
	 */
	EAttribute getTypedElement_IsUnique();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.TypedElement#getTypeMultiplicity <em>Type Multiplicity</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Type Multiplicity</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.TypedElement#getTypeMultiplicity()
	 * @see #getTypedElement()
	 * @generated
	 */
	EAttribute getTypedElement_TypeMultiplicity();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.TypedElement#getDefaultValue <em>Default Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Default Value</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.TypedElement#getDefaultValue()
	 * @see #getTypedElement()
	 * @generated
	 */
	EAttribute getTypedElement_DefaultValue();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Attribute</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute
	 * @generated
	 */
	EClass getAttribute();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Method <em>Method</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Method</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Method
	 * @generated
	 */
	EClass getMethod();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Method#getParameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Parameters</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Method#getParameters()
	 * @see #getMethod()
	 * @generated
	 */
	EReference getMethod_Parameters();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Method#isIsAbstract <em>Is Abstract</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Is Abstract</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Method#isIsAbstract()
	 * @see #getMethod()
	 * @generated
	 */
	EAttribute getMethod_IsAbstract();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal <em>Literal</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Literal</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal
	 * @generated
	 */
	EClass getLiteral();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal#getValue()
	 * @see #getLiteral()
	 * @generated
	 */
	EAttribute getLiteral_Value();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Parameter <em>Parameter</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Parameter</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Parameter
	 * @generated
	 */
	EClass getParameter();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Map <em>Map</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Map</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Map
	 * @generated
	 */
	EClass getMap();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Map#getArtifacts <em>Artifacts</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Artifacts</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Map#getArtifacts()
	 * @see #getMap()
	 * @generated
	 */
	EReference getMap_Artifacts();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Map#getAssociations <em>Associations</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Associations</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Map#getAssociations()
	 * @see #getMap()
	 * @generated
	 */
	EReference getMap_Associations();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Map#getDependencies <em>Dependencies</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Dependencies</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Map#getDependencies()
	 * @see #getMap()
	 * @generated
	 */
	EReference getMap_Dependencies();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Map#getBasePackage <em>Base Package</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Base Package</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Map#getBasePackage()
	 * @see #getMap()
	 * @generated
	 */
	EAttribute getMap_BasePackage();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Map#getProperties <em>Properties</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Properties</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Map#getProperties()
	 * @see #getMap()
	 * @generated
	 */
	EReference getMap_Properties();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference <em>Reference</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Reference</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference
	 * @generated
	 */
	EClass getReference();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference#getMultiplicity <em>Multiplicity</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Multiplicity</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference#getMultiplicity()
	 * @see #getReference()
	 * @generated
	 */
	EAttribute getReference_Multiplicity();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference#getZEnd <em>ZEnd</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>ZEnd</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference#getZEnd()
	 * @see #getReference()
	 * @generated
	 */
	EReference getReference_ZEnd();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference#getTypeMultiplicity <em>Type Multiplicity</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Type Multiplicity</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference#getTypeMultiplicity()
	 * @see #getReference()
	 * @generated
	 */
	EAttribute getReference_TypeMultiplicity();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference#isIsOrdered <em>Is Ordered</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Is Ordered</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference#isIsOrdered()
	 * @see #getReference()
	 * @generated
	 */
	EAttribute getReference_IsOrdered();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference#isIsUnique <em>Is Unique</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Is Unique</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference#isIsUnique()
	 * @see #getReference()
	 * @generated
	 */
	EAttribute getReference_IsUnique();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency <em>Dependency</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Dependency</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency
	 * @generated
	 */
	EClass getDependency();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency#getAEnd <em>AEnd</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>AEnd</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency#getAEnd()
	 * @see #getDependency()
	 * @generated
	 */
	EReference getDependency_AEnd();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency#getAEndMultiplicity <em>AEnd Multiplicity</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>AEnd Multiplicity</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency#getAEndMultiplicity()
	 * @see #getDependency()
	 * @generated
	 */
	EAttribute getDependency_AEndMultiplicity();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency#isAEndIsNavigable <em>AEnd Is Navigable</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>AEnd Is Navigable</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency#isAEndIsNavigable()
	 * @see #getDependency()
	 * @generated
	 */
	EAttribute getDependency_AEndIsNavigable();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency#getZEnd <em>ZEnd</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>ZEnd</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency#getZEnd()
	 * @see #getDependency()
	 * @generated
	 */
	EReference getDependency_ZEnd();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency#getZEndMultiplicity <em>ZEnd Multiplicity</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>ZEnd Multiplicity</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency#getZEndMultiplicity()
	 * @see #getDependency()
	 * @generated
	 */
	EAttribute getDependency_ZEndMultiplicity();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency#isZEndIsNavigable <em>ZEnd Is Navigable</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>ZEnd Is Navigable</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency#isZEndIsNavigable()
	 * @see #getDependency()
	 * @generated
	 */
	EAttribute getDependency_ZEndIsNavigable();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClassClass <em>Association Class Class</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Association Class Class</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClassClass
	 * @generated
	 */
	EClass getAssociationClassClass();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.DiagramProperty <em>Diagram Property</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Diagram Property</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.DiagramProperty
	 * @generated
	 */
	EClass getDiagramProperty();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.DiagramProperty#getName <em>Name</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.DiagramProperty#getName()
	 * @see #getDiagramProperty()
	 * @generated
	 */
	EAttribute getDiagramProperty_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.DiagramProperty#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.DiagramProperty#getValue()
	 * @see #getDiagramProperty()
	 * @generated
	 */
	EAttribute getDiagramProperty_Value();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AggregationEnum <em>Aggregation Enum</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Aggregation Enum</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AggregationEnum
	 * @generated
	 */
	EEnum getAggregationEnum();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.ChangeableEnum <em>Changeable Enum</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Changeable Enum</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.ChangeableEnum
	 * @generated
	 */
	EEnum getChangeableEnum();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity <em>Assoc Multiplicity</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Assoc Multiplicity</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity
	 * @generated
	 */
	EEnum getAssocMultiplicity();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.TypeMultiplicity <em>Type Multiplicity</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Type Multiplicity</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.TypeMultiplicity
	 * @generated
	 */
	EEnum getTypeMultiplicity();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Visibility <em>Visibility</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Visibility</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Visibility
	 * @generated
	 */
	EEnum getVisibility();

	/**
	 * Returns the factory that creates the instances of the model. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	VisualeditorFactory getVisualeditorFactory();

	/**
	 * <!-- begin-user-doc --> Defines literals for the meta objects that
	 * represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.NamedElementImpl <em>Named Element</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.NamedElementImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getNamedElement()
		 * @generated
		 */
		EClass NAMED_ELEMENT = eINSTANCE.getNamedElement();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute NAMED_ELEMENT__NAME = eINSTANCE.getNamedElement_Name();

		/**
		 * The meta object literal for the '<em><b>Stereotypes</b></em>'
		 * attribute list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute NAMED_ELEMENT__STEREOTYPES = eINSTANCE
				.getNamedElement_Stereotypes();

		/**
		 * The meta object literal for the '<em><b>Properties</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference NAMED_ELEMENT__PROPERTIES = eINSTANCE
				.getNamedElement_Properties();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.QualifiedNamedElementImpl <em>Qualified Named Element</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.QualifiedNamedElementImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getQualifiedNamedElement()
		 * @generated
		 */
		EClass QUALIFIED_NAMED_ELEMENT = eINSTANCE.getQualifiedNamedElement();

		/**
		 * The meta object literal for the '<em><b>Package</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute QUALIFIED_NAMED_ELEMENT__PACKAGE = eINSTANCE
				.getQualifiedNamedElement_Package();

		/**
		 * The meta object literal for the '<em><b>Is Readonly</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute QUALIFIED_NAMED_ELEMENT__IS_READONLY = eINSTANCE
				.getQualifiedNamedElement_IsReadonly();

		/**
		 * The meta object literal for the '<em><b>Is Abstract</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute QUALIFIED_NAMED_ELEMENT__IS_ABSTRACT = eINSTANCE
				.getQualifiedNamedElement_IsAbstract();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AbstractArtifactImpl <em>Abstract Artifact</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AbstractArtifactImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getAbstractArtifact()
		 * @generated
		 */
		EClass ABSTRACT_ARTIFACT = eINSTANCE.getAbstractArtifact();

		/**
		 * The meta object literal for the '<em><b>Extends</b></em>'
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ABSTRACT_ARTIFACT__EXTENDS = eINSTANCE
				.getAbstractArtifact_Extends();

		/**
		 * The meta object literal for the '<em><b>Attributes</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ABSTRACT_ARTIFACT__ATTRIBUTES = eINSTANCE
				.getAbstractArtifact_Attributes();

		/**
		 * The meta object literal for the '<em><b>Literals</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ABSTRACT_ARTIFACT__LITERALS = eINSTANCE
				.getAbstractArtifact_Literals();

		/**
		 * The meta object literal for the '<em><b>Methods</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ABSTRACT_ARTIFACT__METHODS = eINSTANCE
				.getAbstractArtifact_Methods();

		/**
		 * The meta object literal for the '<em><b>References</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ABSTRACT_ARTIFACT__REFERENCES = eINSTANCE
				.getAbstractArtifact_References();

		/**
		 * The meta object literal for the '<em><b>Implements</b></em>'
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ABSTRACT_ARTIFACT__IMPLEMENTS = eINSTANCE
				.getAbstractArtifact_Implements();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.ManagedEntityArtifactImpl <em>Managed Entity Artifact</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.ManagedEntityArtifactImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getManagedEntityArtifact()
		 * @generated
		 */
		EClass MANAGED_ENTITY_ARTIFACT = eINSTANCE.getManagedEntityArtifact();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.DatatypeArtifactImpl <em>Datatype Artifact</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.DatatypeArtifactImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getDatatypeArtifact()
		 * @generated
		 */
		EClass DATATYPE_ARTIFACT = eINSTANCE.getDatatypeArtifact();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.NotificationArtifactImpl <em>Notification Artifact</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.NotificationArtifactImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getNotificationArtifact()
		 * @generated
		 */
		EClass NOTIFICATION_ARTIFACT = eINSTANCE.getNotificationArtifact();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.NamedQueryArtifactImpl <em>Named Query Artifact</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.NamedQueryArtifactImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getNamedQueryArtifact()
		 * @generated
		 */
		EClass NAMED_QUERY_ARTIFACT = eINSTANCE.getNamedQueryArtifact();

		/**
		 * The meta object literal for the '<em><b>Returned Type</b></em>'
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference NAMED_QUERY_ARTIFACT__RETURNED_TYPE = eINSTANCE
				.getNamedQueryArtifact_ReturnedType();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.EnumerationImpl <em>Enumeration</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.EnumerationImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getEnumeration()
		 * @generated
		 */
		EClass ENUMERATION = eINSTANCE.getEnumeration();

		/**
		 * The meta object literal for the '<em><b>Base Type</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ENUMERATION__BASE_TYPE = eINSTANCE.getEnumeration_BaseType();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.UpdateProcedureArtifactImpl <em>Update Procedure Artifact</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.UpdateProcedureArtifactImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getUpdateProcedureArtifact()
		 * @generated
		 */
		EClass UPDATE_PROCEDURE_ARTIFACT = eINSTANCE
				.getUpdateProcedureArtifact();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.ExceptionArtifactImpl <em>Exception Artifact</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.ExceptionArtifactImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getExceptionArtifact()
		 * @generated
		 */
		EClass EXCEPTION_ARTIFACT = eINSTANCE.getExceptionArtifact();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.SessionFacadeArtifactImpl <em>Session Facade Artifact</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.SessionFacadeArtifactImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getSessionFacadeArtifact()
		 * @generated
		 */
		EClass SESSION_FACADE_ARTIFACT = eINSTANCE.getSessionFacadeArtifact();

		/**
		 * The meta object literal for the '<em><b>Managed Entities</b></em>'
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference SESSION_FACADE_ARTIFACT__MANAGED_ENTITIES = eINSTANCE
				.getSessionFacadeArtifact_ManagedEntities();

		/**
		 * The meta object literal for the '<em><b>Emitted Notifications</b></em>'
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference SESSION_FACADE_ARTIFACT__EMITTED_NOTIFICATIONS = eINSTANCE
				.getSessionFacadeArtifact_EmittedNotifications();

		/**
		 * The meta object literal for the '<em><b>Named Queries</b></em>'
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference SESSION_FACADE_ARTIFACT__NAMED_QUERIES = eINSTANCE
				.getSessionFacadeArtifact_NamedQueries();

		/**
		 * The meta object literal for the '<em><b>Exposed Procedures</b></em>'
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference SESSION_FACADE_ARTIFACT__EXPOSED_PROCEDURES = eINSTANCE
				.getSessionFacadeArtifact_ExposedProcedures();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationImpl <em>Association</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getAssociation()
		 * @generated
		 */
		EClass ASSOCIATION = eINSTANCE.getAssociation();

		/**
		 * The meta object literal for the '<em><b>AEnd</b></em>' reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ASSOCIATION__AEND = eINSTANCE.getAssociation_AEnd();

		/**
		 * The meta object literal for the '<em><b>AEnd Name</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION__AEND_NAME = eINSTANCE.getAssociation_AEndName();

		/**
		 * The meta object literal for the '<em><b>AEnd Multiplicity</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION__AEND_MULTIPLICITY = eINSTANCE
				.getAssociation_AEndMultiplicity();

		/**
		 * The meta object literal for the '<em><b>AEnd Is Navigable</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION__AEND_IS_NAVIGABLE = eINSTANCE
				.getAssociation_AEndIsNavigable();

		/**
		 * The meta object literal for the '<em><b>AEnd Is Ordered</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION__AEND_IS_ORDERED = eINSTANCE
				.getAssociation_AEndIsOrdered();

		/**
		 * The meta object literal for the '<em><b>AEnd Is Unique</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION__AEND_IS_UNIQUE = eINSTANCE
				.getAssociation_AEndIsUnique();

		/**
		 * The meta object literal for the '<em><b>AEnd Is Changeable</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION__AEND_IS_CHANGEABLE = eINSTANCE
				.getAssociation_AEndIsChangeable();

		/**
		 * The meta object literal for the '<em><b>AEnd Aggregation</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION__AEND_AGGREGATION = eINSTANCE
				.getAssociation_AEndAggregation();

		/**
		 * The meta object literal for the '<em><b>ZEnd</b></em>' reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ASSOCIATION__ZEND = eINSTANCE.getAssociation_ZEnd();

		/**
		 * The meta object literal for the '<em><b>ZEnd Name</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION__ZEND_NAME = eINSTANCE.getAssociation_ZEndName();

		/**
		 * The meta object literal for the '<em><b>ZEnd Multiplicity</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION__ZEND_MULTIPLICITY = eINSTANCE
				.getAssociation_ZEndMultiplicity();

		/**
		 * The meta object literal for the '<em><b>ZEnd Is Navigable</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION__ZEND_IS_NAVIGABLE = eINSTANCE
				.getAssociation_ZEndIsNavigable();

		/**
		 * The meta object literal for the '<em><b>ZEnd Is Ordered</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION__ZEND_IS_ORDERED = eINSTANCE
				.getAssociation_ZEndIsOrdered();

		/**
		 * The meta object literal for the '<em><b>ZEnd Is Unique</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION__ZEND_IS_UNIQUE = eINSTANCE
				.getAssociation_ZEndIsUnique();

		/**
		 * The meta object literal for the '<em><b>ZEnd Is Changeable</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION__ZEND_IS_CHANGEABLE = eINSTANCE
				.getAssociation_ZEndIsChangeable();

		/**
		 * The meta object literal for the '<em><b>ZEnd Aggregation</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION__ZEND_AGGREGATION = eINSTANCE
				.getAssociation_ZEndAggregation();

		/**
		 * The meta object literal for the '<em><b>AEnd Visibility</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION__AEND_VISIBILITY = eINSTANCE
				.getAssociation_AEndVisibility();

		/**
		 * The meta object literal for the '<em><b>ZEnd Visibility</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION__ZEND_VISIBILITY = eINSTANCE
				.getAssociation_ZEndVisibility();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationClassImpl <em>Association Class</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationClassImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getAssociationClass()
		 * @generated
		 */
		EClass ASSOCIATION_CLASS = eINSTANCE.getAssociationClass();

		/**
		 * The meta object literal for the '<em><b>Associated Class</b></em>'
		 * reference feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ASSOCIATION_CLASS__ASSOCIATED_CLASS = eINSTANCE
				.getAssociationClass_AssociatedClass();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.TypedElementImpl <em>Typed Element</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.TypedElementImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getTypedElement()
		 * @generated
		 */
		EClass TYPED_ELEMENT = eINSTANCE.getTypedElement();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute TYPED_ELEMENT__TYPE = eINSTANCE.getTypedElement_Type();

		/**
		 * The meta object literal for the '<em><b>Multiplicity</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute TYPED_ELEMENT__MULTIPLICITY = eINSTANCE
				.getTypedElement_Multiplicity();

		/**
		 * The meta object literal for the '<em><b>Visibility</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute TYPED_ELEMENT__VISIBILITY = eINSTANCE
				.getTypedElement_Visibility();

		/**
		 * The meta object literal for the '<em><b>Is Ordered</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute TYPED_ELEMENT__IS_ORDERED = eINSTANCE
				.getTypedElement_IsOrdered();

		/**
		 * The meta object literal for the '<em><b>Is Unique</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute TYPED_ELEMENT__IS_UNIQUE = eINSTANCE
				.getTypedElement_IsUnique();

		/**
		 * The meta object literal for the '<em><b>Type Multiplicity</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute TYPED_ELEMENT__TYPE_MULTIPLICITY = eINSTANCE
				.getTypedElement_TypeMultiplicity();

		/**
		 * The meta object literal for the '<em><b>Default Value</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute TYPED_ELEMENT__DEFAULT_VALUE = eINSTANCE
				.getTypedElement_DefaultValue();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AttributeImpl <em>Attribute</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AttributeImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getAttribute()
		 * @generated
		 */
		EClass ATTRIBUTE = eINSTANCE.getAttribute();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.MethodImpl <em>Method</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.MethodImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getMethod()
		 * @generated
		 */
		EClass METHOD = eINSTANCE.getMethod();

		/**
		 * The meta object literal for the '<em><b>Parameters</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference METHOD__PARAMETERS = eINSTANCE.getMethod_Parameters();

		/**
		 * The meta object literal for the '<em><b>Is Abstract</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute METHOD__IS_ABSTRACT = eINSTANCE.getMethod_IsAbstract();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.LiteralImpl <em>Literal</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.LiteralImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getLiteral()
		 * @generated
		 */
		EClass LITERAL = eINSTANCE.getLiteral();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute LITERAL__VALUE = eINSTANCE.getLiteral_Value();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.ParameterImpl <em>Parameter</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.ParameterImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getParameter()
		 * @generated
		 */
		EClass PARAMETER = eINSTANCE.getParameter();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.MapImpl <em>Map</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.MapImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getMap()
		 * @generated
		 */
		EClass MAP = eINSTANCE.getMap();

		/**
		 * The meta object literal for the '<em><b>Artifacts</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MAP__ARTIFACTS = eINSTANCE.getMap_Artifacts();

		/**
		 * The meta object literal for the '<em><b>Associations</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MAP__ASSOCIATIONS = eINSTANCE.getMap_Associations();

		/**
		 * The meta object literal for the '<em><b>Dependencies</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MAP__DEPENDENCIES = eINSTANCE.getMap_Dependencies();

		/**
		 * The meta object literal for the '<em><b>Base Package</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute MAP__BASE_PACKAGE = eINSTANCE.getMap_BasePackage();

		/**
		 * The meta object literal for the '<em><b>Properties</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference MAP__PROPERTIES = eINSTANCE.getMap_Properties();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.ReferenceImpl <em>Reference</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.ReferenceImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getReference()
		 * @generated
		 */
		EClass REFERENCE = eINSTANCE.getReference();

		/**
		 * The meta object literal for the '<em><b>Multiplicity</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute REFERENCE__MULTIPLICITY = eINSTANCE
				.getReference_Multiplicity();

		/**
		 * The meta object literal for the '<em><b>ZEnd</b></em>' reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference REFERENCE__ZEND = eINSTANCE.getReference_ZEnd();

		/**
		 * The meta object literal for the '<em><b>Type Multiplicity</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute REFERENCE__TYPE_MULTIPLICITY = eINSTANCE
				.getReference_TypeMultiplicity();

		/**
		 * The meta object literal for the '<em><b>Is Ordered</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute REFERENCE__IS_ORDERED = eINSTANCE.getReference_IsOrdered();

		/**
		 * The meta object literal for the '<em><b>Is Unique</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute REFERENCE__IS_UNIQUE = eINSTANCE.getReference_IsUnique();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.DependencyImpl <em>Dependency</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.DependencyImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getDependency()
		 * @generated
		 */
		EClass DEPENDENCY = eINSTANCE.getDependency();

		/**
		 * The meta object literal for the '<em><b>AEnd</b></em>' reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DEPENDENCY__AEND = eINSTANCE.getDependency_AEnd();

		/**
		 * The meta object literal for the '<em><b>AEnd Multiplicity</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DEPENDENCY__AEND_MULTIPLICITY = eINSTANCE
				.getDependency_AEndMultiplicity();

		/**
		 * The meta object literal for the '<em><b>AEnd Is Navigable</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DEPENDENCY__AEND_IS_NAVIGABLE = eINSTANCE
				.getDependency_AEndIsNavigable();

		/**
		 * The meta object literal for the '<em><b>ZEnd</b></em>' reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference DEPENDENCY__ZEND = eINSTANCE.getDependency_ZEnd();

		/**
		 * The meta object literal for the '<em><b>ZEnd Multiplicity</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DEPENDENCY__ZEND_MULTIPLICITY = eINSTANCE
				.getDependency_ZEndMultiplicity();

		/**
		 * The meta object literal for the '<em><b>ZEnd Is Navigable</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DEPENDENCY__ZEND_IS_NAVIGABLE = eINSTANCE
				.getDependency_ZEndIsNavigable();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationClassClassImpl <em>Association Class Class</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationClassClassImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getAssociationClassClass()
		 * @generated
		 */
		EClass ASSOCIATION_CLASS_CLASS = eINSTANCE.getAssociationClassClass();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.DiagramPropertyImpl <em>Diagram Property</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.DiagramPropertyImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getDiagramProperty()
		 * @generated
		 */
		EClass DIAGRAM_PROPERTY = eINSTANCE.getDiagramProperty();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DIAGRAM_PROPERTY__NAME = eINSTANCE.getDiagramProperty_Name();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DIAGRAM_PROPERTY__VALUE = eINSTANCE
				.getDiagramProperty_Value();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AggregationEnum <em>Aggregation Enum</em>}'
		 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AggregationEnum
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getAggregationEnum()
		 * @generated
		 */
		EEnum AGGREGATION_ENUM = eINSTANCE.getAggregationEnum();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.ChangeableEnum <em>Changeable Enum</em>}'
		 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.ChangeableEnum
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getChangeableEnum()
		 * @generated
		 */
		EEnum CHANGEABLE_ENUM = eINSTANCE.getChangeableEnum();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity <em>Assoc Multiplicity</em>}'
		 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getAssocMultiplicity()
		 * @generated
		 */
		EEnum ASSOC_MULTIPLICITY = eINSTANCE.getAssocMultiplicity();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.TypeMultiplicity <em>Type Multiplicity</em>}'
		 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.TypeMultiplicity
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getTypeMultiplicity()
		 * @generated
		 */
		EEnum TYPE_MULTIPLICITY = eINSTANCE.getTypeMultiplicity();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Visibility <em>Visibility</em>}'
		 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Visibility
		 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.VisualeditorPackageImpl#getVisibility()
		 * @generated
		 */
		EEnum VISIBILITY = eINSTANCE.getVisibility();

	}

} // VisualeditorPackage
