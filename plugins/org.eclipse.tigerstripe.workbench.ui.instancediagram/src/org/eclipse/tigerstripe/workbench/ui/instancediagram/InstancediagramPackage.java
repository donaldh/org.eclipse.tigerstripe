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
 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramFactory
 * @model kind="package"
 * @generated
 */
public interface InstancediagramPackage extends EPackage {
	/**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "instancediagram";

	/**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "org.eclipse.tigerstripe.workbench.ui.instancediagram";

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "instancediagram";

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	InstancediagramPackage eINSTANCE = org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstancediagramPackageImpl
			.init();

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.NamedElementImpl <em>Named Element</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.NamedElementImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstancediagramPackageImpl#getNamedElement()
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
	 * The number of structural features of the '<em>Named Element</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.TypedElementImpl <em>Typed Element</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.TypedElementImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstancediagramPackageImpl#getTypedElement()
	 * @generated
	 */
	int TYPED_ELEMENT = 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPED_ELEMENT__NAME = NAMED_ELEMENT__NAME;

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
	 * The number of structural features of the '<em>Typed Element</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TYPED_ELEMENT_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.VariableImpl <em>Variable</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.VariableImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstancediagramPackageImpl#getVariable()
	 * @generated
	 */
	int VARIABLE = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VARIABLE__NAME = TYPED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VARIABLE__TYPE = TYPED_ELEMENT__TYPE;

	/**
	 * The feature id for the '<em><b>Multiplicity</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VARIABLE__MULTIPLICITY = TYPED_ELEMENT__MULTIPLICITY;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VARIABLE__VALUE = TYPED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Variable</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VARIABLE_FEATURE_COUNT = TYPED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstanceImpl <em>Instance</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstanceImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstancediagramPackageImpl#getInstance()
	 * @generated
	 */
	int INSTANCE = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int INSTANCE__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int INSTANCE__PACKAGE = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Artifact Name</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int INSTANCE__ARTIFACT_NAME = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Instance</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int INSTANCE_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.ClassInstanceImpl <em>Class Instance</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.ClassInstanceImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstancediagramPackageImpl#getClassInstance()
	 * @generated
	 */
	int CLASS_INSTANCE = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CLASS_INSTANCE__NAME = INSTANCE__NAME;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CLASS_INSTANCE__PACKAGE = INSTANCE__PACKAGE;

	/**
	 * The feature id for the '<em><b>Artifact Name</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CLASS_INSTANCE__ARTIFACT_NAME = INSTANCE__ARTIFACT_NAME;

	/**
	 * The feature id for the '<em><b>Variables</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CLASS_INSTANCE__VARIABLES = INSTANCE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Associations</b></em>' reference
	 * list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CLASS_INSTANCE__ASSOCIATIONS = INSTANCE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Association Class Instance</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CLASS_INSTANCE__ASSOCIATION_CLASS_INSTANCE = INSTANCE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Class Instance</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int CLASS_INSTANCE_FEATURE_COUNT = INSTANCE_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.AssociationInstanceImpl <em>Association Instance</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.AssociationInstanceImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstancediagramPackageImpl#getAssociationInstance()
	 * @generated
	 */
	int ASSOCIATION_INSTANCE = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_INSTANCE__NAME = INSTANCE__NAME;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_INSTANCE__PACKAGE = INSTANCE__PACKAGE;

	/**
	 * The feature id for the '<em><b>Artifact Name</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_INSTANCE__ARTIFACT_NAME = INSTANCE__ARTIFACT_NAME;

	/**
	 * The feature id for the '<em><b>AEnd</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_INSTANCE__AEND = INSTANCE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>AEnd Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_INSTANCE__AEND_NAME = INSTANCE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>AEnd Multiplicity Lower Bound</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_INSTANCE__AEND_MULTIPLICITY_LOWER_BOUND = INSTANCE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>AEnd Multiplicity Upper Bound</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_INSTANCE__AEND_MULTIPLICITY_UPPER_BOUND = INSTANCE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>AEnd Is Navigable</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_INSTANCE__AEND_IS_NAVIGABLE = INSTANCE_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>AEnd Is Ordered</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_INSTANCE__AEND_IS_ORDERED = INSTANCE_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>AEnd Is Changeable</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_INSTANCE__AEND_IS_CHANGEABLE = INSTANCE_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>AEnd Aggregation</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_INSTANCE__AEND_AGGREGATION = INSTANCE_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>ZEnd</b></em>' reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_INSTANCE__ZEND = INSTANCE_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>ZEnd Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_INSTANCE__ZEND_NAME = INSTANCE_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>ZEnd Multiplicity Lower Bound</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_INSTANCE__ZEND_MULTIPLICITY_LOWER_BOUND = INSTANCE_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>ZEnd Multiplicity Upper Bound</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_INSTANCE__ZEND_MULTIPLICITY_UPPER_BOUND = INSTANCE_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>ZEnd Is Navigable</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_INSTANCE__ZEND_IS_NAVIGABLE = INSTANCE_FEATURE_COUNT + 12;

	/**
	 * The feature id for the '<em><b>ZEnd Is Ordered</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_INSTANCE__ZEND_IS_ORDERED = INSTANCE_FEATURE_COUNT + 13;

	/**
	 * The feature id for the '<em><b>ZEnd Is Changeable</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_INSTANCE__ZEND_IS_CHANGEABLE = INSTANCE_FEATURE_COUNT + 14;

	/**
	 * The feature id for the '<em><b>ZEnd Aggregation</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_INSTANCE__ZEND_AGGREGATION = INSTANCE_FEATURE_COUNT + 15;

	/**
	 * The feature id for the '<em><b>Reference Name</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_INSTANCE__REFERENCE_NAME = INSTANCE_FEATURE_COUNT + 16;

	/**
	 * The number of structural features of the '<em>Association Instance</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ASSOCIATION_INSTANCE_FEATURE_COUNT = INSTANCE_FEATURE_COUNT + 17;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstanceMapImpl <em>Instance Map</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstanceMapImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstancediagramPackageImpl#getInstanceMap()
	 * @generated
	 */
	int INSTANCE_MAP = 6;

	/**
	 * The feature id for the '<em><b>Class Instances</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int INSTANCE_MAP__CLASS_INSTANCES = 0;

	/**
	 * The feature id for the '<em><b>Association Instances</b></em>'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int INSTANCE_MAP__ASSOCIATION_INSTANCES = 1;

	/**
	 * The feature id for the '<em><b>Base Package</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int INSTANCE_MAP__BASE_PACKAGE = 2;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int INSTANCE_MAP__PROPERTIES = 3;

	/**
	 * The number of structural features of the '<em>Instance Map</em>'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int INSTANCE_MAP_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.DiagramPropertyImpl <em>Diagram Property</em>}'
	 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.DiagramPropertyImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstancediagramPackageImpl#getDiagramProperty()
	 * @generated
	 */
	int DIAGRAM_PROPERTY = 10;

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
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AggregationEnum <em>Aggregation Enum</em>}'
	 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.AggregationEnum
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstancediagramPackageImpl#getAggregationEnum()
	 * @generated
	 */
	int AGGREGATION_ENUM = 7;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.ChangeableEnum <em>Changeable Enum</em>}'
	 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.ChangeableEnum
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstancediagramPackageImpl#getChangeableEnum()
	 * @generated
	 */
	int CHANGEABLE_ENUM = 8;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.TypeMultiplicity <em>Type Multiplicity</em>}'
	 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.TypeMultiplicity
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstancediagramPackageImpl#getTypeMultiplicity()
	 * @generated
	 */
	int TYPE_MULTIPLICITY = 9;

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.NamedElement <em>Named Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Named Element</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.NamedElement
	 * @generated
	 */
	EClass getNamedElement();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.NamedElement#getName <em>Name</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.NamedElement#getName()
	 * @see #getNamedElement()
	 * @generated
	 */
	EAttribute getNamedElement_Name();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.Variable <em>Variable</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Variable</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.Variable
	 * @generated
	 */
	EClass getVariable();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.Variable#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.Variable#getValue()
	 * @see #getVariable()
	 * @generated
	 */
	EAttribute getVariable_Value();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance <em>Instance</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Instance</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance
	 * @generated
	 */
	EClass getInstance();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance#getPackage <em>Package</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Package</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance#getPackage()
	 * @see #getInstance()
	 * @generated
	 */
	EAttribute getInstance_Package();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance#getArtifactName <em>Artifact Name</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Artifact Name</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance#getArtifactName()
	 * @see #getInstance()
	 * @generated
	 */
	EAttribute getInstance_ArtifactName();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance <em>Class Instance</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Class Instance</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance
	 * @generated
	 */
	EClass getClassInstance();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance#getVariables <em>Variables</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Variables</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance#getVariables()
	 * @see #getClassInstance()
	 * @generated
	 */
	EReference getClassInstance_Variables();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance#getAssociations <em>Associations</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference list '<em>Associations</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance#getAssociations()
	 * @see #getClassInstance()
	 * @generated
	 */
	EReference getClassInstance_Associations();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance#isAssociationClassInstance <em>Association Class Instance</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Association Class Instance</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance#isAssociationClassInstance()
	 * @see #getClassInstance()
	 * @generated
	 */
	EAttribute getClassInstance_AssociationClassInstance();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance <em>Association Instance</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Association Instance</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance
	 * @generated
	 */
	EClass getAssociationInstance();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getAEnd <em>AEnd</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>AEnd</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getAEnd()
	 * @see #getAssociationInstance()
	 * @generated
	 */
	EReference getAssociationInstance_AEnd();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getAEndName <em>AEnd Name</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>AEnd Name</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getAEndName()
	 * @see #getAssociationInstance()
	 * @generated
	 */
	EAttribute getAssociationInstance_AEndName();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getAEndMultiplicityLowerBound <em>AEnd Multiplicity Lower Bound</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>AEnd Multiplicity Lower Bound</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getAEndMultiplicityLowerBound()
	 * @see #getAssociationInstance()
	 * @generated
	 */
	EAttribute getAssociationInstance_AEndMultiplicityLowerBound();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getAEndMultiplicityUpperBound <em>AEnd Multiplicity Upper Bound</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>AEnd Multiplicity Upper Bound</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getAEndMultiplicityUpperBound()
	 * @see #getAssociationInstance()
	 * @generated
	 */
	EAttribute getAssociationInstance_AEndMultiplicityUpperBound();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#isAEndIsNavigable <em>AEnd Is Navigable</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>AEnd Is Navigable</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#isAEndIsNavigable()
	 * @see #getAssociationInstance()
	 * @generated
	 */
	EAttribute getAssociationInstance_AEndIsNavigable();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#isAEndIsOrdered <em>AEnd Is Ordered</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>AEnd Is Ordered</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#isAEndIsOrdered()
	 * @see #getAssociationInstance()
	 * @generated
	 */
	EAttribute getAssociationInstance_AEndIsOrdered();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getAEndIsChangeable <em>AEnd Is Changeable</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>AEnd Is Changeable</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getAEndIsChangeable()
	 * @see #getAssociationInstance()
	 * @generated
	 */
	EAttribute getAssociationInstance_AEndIsChangeable();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getAEndAggregation <em>AEnd Aggregation</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>AEnd Aggregation</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getAEndAggregation()
	 * @see #getAssociationInstance()
	 * @generated
	 */
	EAttribute getAssociationInstance_AEndAggregation();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getZEnd <em>ZEnd</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference '<em>ZEnd</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getZEnd()
	 * @see #getAssociationInstance()
	 * @generated
	 */
	EReference getAssociationInstance_ZEnd();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getZEndName <em>ZEnd Name</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>ZEnd Name</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getZEndName()
	 * @see #getAssociationInstance()
	 * @generated
	 */
	EAttribute getAssociationInstance_ZEndName();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getZEndMultiplicityLowerBound <em>ZEnd Multiplicity Lower Bound</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>ZEnd Multiplicity Lower Bound</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getZEndMultiplicityLowerBound()
	 * @see #getAssociationInstance()
	 * @generated
	 */
	EAttribute getAssociationInstance_ZEndMultiplicityLowerBound();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getZEndMultiplicityUpperBound <em>ZEnd Multiplicity Upper Bound</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>ZEnd Multiplicity Upper Bound</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getZEndMultiplicityUpperBound()
	 * @see #getAssociationInstance()
	 * @generated
	 */
	EAttribute getAssociationInstance_ZEndMultiplicityUpperBound();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#isZEndIsNavigable <em>ZEnd Is Navigable</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>ZEnd Is Navigable</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#isZEndIsNavigable()
	 * @see #getAssociationInstance()
	 * @generated
	 */
	EAttribute getAssociationInstance_ZEndIsNavigable();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#isZEndIsOrdered <em>ZEnd Is Ordered</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>ZEnd Is Ordered</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#isZEndIsOrdered()
	 * @see #getAssociationInstance()
	 * @generated
	 */
	EAttribute getAssociationInstance_ZEndIsOrdered();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getZEndIsChangeable <em>ZEnd Is Changeable</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>ZEnd Is Changeable</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getZEndIsChangeable()
	 * @see #getAssociationInstance()
	 * @generated
	 */
	EAttribute getAssociationInstance_ZEndIsChangeable();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getZEndAggregation <em>ZEnd Aggregation</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>ZEnd Aggregation</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getZEndAggregation()
	 * @see #getAssociationInstance()
	 * @generated
	 */
	EAttribute getAssociationInstance_ZEndAggregation();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getReferenceName <em>Reference Name</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Reference Name</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getReferenceName()
	 * @see #getAssociationInstance()
	 * @generated
	 */
	EAttribute getAssociationInstance_ReferenceName();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.TypedElement <em>Typed Element</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Typed Element</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.TypedElement
	 * @generated
	 */
	EClass getTypedElement();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.TypedElement#getType <em>Type</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.TypedElement#getType()
	 * @see #getTypedElement()
	 * @generated
	 */
	EAttribute getTypedElement_Type();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.TypedElement#getMultiplicity <em>Multiplicity</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Multiplicity</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.TypedElement#getMultiplicity()
	 * @see #getTypedElement()
	 * @generated
	 */
	EAttribute getTypedElement_Multiplicity();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap <em>Instance Map</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Instance Map</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap
	 * @generated
	 */
	EClass getInstanceMap();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap#getClassInstances <em>Class Instances</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Class Instances</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap#getClassInstances()
	 * @see #getInstanceMap()
	 * @generated
	 */
	EReference getInstanceMap_ClassInstances();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap#getAssociationInstances <em>Association Instances</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Association Instances</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap#getAssociationInstances()
	 * @see #getInstanceMap()
	 * @generated
	 */
	EReference getInstanceMap_AssociationInstances();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap#getBasePackage <em>Base Package</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Base Package</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap#getBasePackage()
	 * @see #getInstanceMap()
	 * @generated
	 */
	EAttribute getInstanceMap_BasePackage();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap#getProperties <em>Properties</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '<em>Properties</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap#getProperties()
	 * @see #getInstanceMap()
	 * @generated
	 */
	EReference getInstanceMap_Properties();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.DiagramProperty <em>Diagram Property</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Diagram Property</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.DiagramProperty
	 * @generated
	 */
	EClass getDiagramProperty();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.DiagramProperty#getName <em>Name</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.DiagramProperty#getName()
	 * @see #getDiagramProperty()
	 * @generated
	 */
	EAttribute getDiagramProperty_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.DiagramProperty#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.DiagramProperty#getValue()
	 * @see #getDiagramProperty()
	 * @generated
	 */
	EAttribute getDiagramProperty_Value();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AggregationEnum <em>Aggregation Enum</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Aggregation Enum</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.AggregationEnum
	 * @generated
	 */
	EEnum getAggregationEnum();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.ChangeableEnum <em>Changeable Enum</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Changeable Enum</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.ChangeableEnum
	 * @generated
	 */
	EEnum getChangeableEnum();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.TypeMultiplicity <em>Type Multiplicity</em>}'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Type Multiplicity</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.TypeMultiplicity
	 * @generated
	 */
	EEnum getTypeMultiplicity();

	/**
	 * Returns the factory that creates the instances of the model. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	InstancediagramFactory getInstancediagramFactory();

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
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.NamedElementImpl <em>Named Element</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.NamedElementImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstancediagramPackageImpl#getNamedElement()
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
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.VariableImpl <em>Variable</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.VariableImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstancediagramPackageImpl#getVariable()
		 * @generated
		 */
		EClass VARIABLE = eINSTANCE.getVariable();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VARIABLE__VALUE = eINSTANCE.getVariable_Value();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstanceImpl <em>Instance</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstanceImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstancediagramPackageImpl#getInstance()
		 * @generated
		 */
		EClass INSTANCE = eINSTANCE.getInstance();

		/**
		 * The meta object literal for the '<em><b>Package</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute INSTANCE__PACKAGE = eINSTANCE.getInstance_Package();

		/**
		 * The meta object literal for the '<em><b>Artifact Name</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute INSTANCE__ARTIFACT_NAME = eINSTANCE
				.getInstance_ArtifactName();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.ClassInstanceImpl <em>Class Instance</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.ClassInstanceImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstancediagramPackageImpl#getClassInstance()
		 * @generated
		 */
		EClass CLASS_INSTANCE = eINSTANCE.getClassInstance();

		/**
		 * The meta object literal for the '<em><b>Variables</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference CLASS_INSTANCE__VARIABLES = eINSTANCE
				.getClassInstance_Variables();

		/**
		 * The meta object literal for the '<em><b>Associations</b></em>'
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference CLASS_INSTANCE__ASSOCIATIONS = eINSTANCE
				.getClassInstance_Associations();

		/**
		 * The meta object literal for the '<em><b>Association Class Instance</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute CLASS_INSTANCE__ASSOCIATION_CLASS_INSTANCE = eINSTANCE
				.getClassInstance_AssociationClassInstance();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.AssociationInstanceImpl <em>Association Instance</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.AssociationInstanceImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstancediagramPackageImpl#getAssociationInstance()
		 * @generated
		 */
		EClass ASSOCIATION_INSTANCE = eINSTANCE.getAssociationInstance();

		/**
		 * The meta object literal for the '<em><b>AEnd</b></em>' reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ASSOCIATION_INSTANCE__AEND = eINSTANCE
				.getAssociationInstance_AEnd();

		/**
		 * The meta object literal for the '<em><b>AEnd Name</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION_INSTANCE__AEND_NAME = eINSTANCE
				.getAssociationInstance_AEndName();

		/**
		 * The meta object literal for the '<em><b>AEnd Multiplicity Lower Bound</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION_INSTANCE__AEND_MULTIPLICITY_LOWER_BOUND = eINSTANCE
				.getAssociationInstance_AEndMultiplicityLowerBound();

		/**
		 * The meta object literal for the '<em><b>AEnd Multiplicity Upper Bound</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION_INSTANCE__AEND_MULTIPLICITY_UPPER_BOUND = eINSTANCE
				.getAssociationInstance_AEndMultiplicityUpperBound();

		/**
		 * The meta object literal for the '<em><b>AEnd Is Navigable</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION_INSTANCE__AEND_IS_NAVIGABLE = eINSTANCE
				.getAssociationInstance_AEndIsNavigable();

		/**
		 * The meta object literal for the '<em><b>AEnd Is Ordered</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION_INSTANCE__AEND_IS_ORDERED = eINSTANCE
				.getAssociationInstance_AEndIsOrdered();

		/**
		 * The meta object literal for the '<em><b>AEnd Is Changeable</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION_INSTANCE__AEND_IS_CHANGEABLE = eINSTANCE
				.getAssociationInstance_AEndIsChangeable();

		/**
		 * The meta object literal for the '<em><b>AEnd Aggregation</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION_INSTANCE__AEND_AGGREGATION = eINSTANCE
				.getAssociationInstance_AEndAggregation();

		/**
		 * The meta object literal for the '<em><b>ZEnd</b></em>' reference
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference ASSOCIATION_INSTANCE__ZEND = eINSTANCE
				.getAssociationInstance_ZEnd();

		/**
		 * The meta object literal for the '<em><b>ZEnd Name</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION_INSTANCE__ZEND_NAME = eINSTANCE
				.getAssociationInstance_ZEndName();

		/**
		 * The meta object literal for the '<em><b>ZEnd Multiplicity Lower Bound</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION_INSTANCE__ZEND_MULTIPLICITY_LOWER_BOUND = eINSTANCE
				.getAssociationInstance_ZEndMultiplicityLowerBound();

		/**
		 * The meta object literal for the '<em><b>ZEnd Multiplicity Upper Bound</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION_INSTANCE__ZEND_MULTIPLICITY_UPPER_BOUND = eINSTANCE
				.getAssociationInstance_ZEndMultiplicityUpperBound();

		/**
		 * The meta object literal for the '<em><b>ZEnd Is Navigable</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION_INSTANCE__ZEND_IS_NAVIGABLE = eINSTANCE
				.getAssociationInstance_ZEndIsNavigable();

		/**
		 * The meta object literal for the '<em><b>ZEnd Is Ordered</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION_INSTANCE__ZEND_IS_ORDERED = eINSTANCE
				.getAssociationInstance_ZEndIsOrdered();

		/**
		 * The meta object literal for the '<em><b>ZEnd Is Changeable</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION_INSTANCE__ZEND_IS_CHANGEABLE = eINSTANCE
				.getAssociationInstance_ZEndIsChangeable();

		/**
		 * The meta object literal for the '<em><b>ZEnd Aggregation</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION_INSTANCE__ZEND_AGGREGATION = eINSTANCE
				.getAssociationInstance_ZEndAggregation();

		/**
		 * The meta object literal for the '<em><b>Reference Name</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute ASSOCIATION_INSTANCE__REFERENCE_NAME = eINSTANCE
				.getAssociationInstance_ReferenceName();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.TypedElementImpl <em>Typed Element</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.TypedElementImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstancediagramPackageImpl#getTypedElement()
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
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstanceMapImpl <em>Instance Map</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstanceMapImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstancediagramPackageImpl#getInstanceMap()
		 * @generated
		 */
		EClass INSTANCE_MAP = eINSTANCE.getInstanceMap();

		/**
		 * The meta object literal for the '<em><b>Class Instances</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference INSTANCE_MAP__CLASS_INSTANCES = eINSTANCE
				.getInstanceMap_ClassInstances();

		/**
		 * The meta object literal for the '<em><b>Association Instances</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference INSTANCE_MAP__ASSOCIATION_INSTANCES = eINSTANCE
				.getInstanceMap_AssociationInstances();

		/**
		 * The meta object literal for the '<em><b>Base Package</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute INSTANCE_MAP__BASE_PACKAGE = eINSTANCE
				.getInstanceMap_BasePackage();

		/**
		 * The meta object literal for the '<em><b>Properties</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		EReference INSTANCE_MAP__PROPERTIES = eINSTANCE
				.getInstanceMap_Properties();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.DiagramPropertyImpl <em>Diagram Property</em>}'
		 * class. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.DiagramPropertyImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstancediagramPackageImpl#getDiagramProperty()
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
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AggregationEnum <em>Aggregation Enum</em>}'
		 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.AggregationEnum
		 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstancediagramPackageImpl#getAggregationEnum()
		 * @generated
		 */
		EEnum AGGREGATION_ENUM = eINSTANCE.getAggregationEnum();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.ChangeableEnum <em>Changeable Enum</em>}'
		 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.ChangeableEnum
		 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstancediagramPackageImpl#getChangeableEnum()
		 * @generated
		 */
		EEnum CHANGEABLE_ENUM = eINSTANCE.getChangeableEnum();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.TypeMultiplicity <em>Type Multiplicity</em>}'
		 * enum. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.TypeMultiplicity
		 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstancediagramPackageImpl#getTypeMultiplicity()
		 * @generated
		 */
		EEnum TYPE_MULTIPLICITY = eINSTANCE.getTypeMultiplicity();

	}

} // InstancediagramPackage
