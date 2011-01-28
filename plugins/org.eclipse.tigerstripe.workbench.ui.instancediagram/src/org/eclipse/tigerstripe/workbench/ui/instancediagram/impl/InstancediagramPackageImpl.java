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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AggregationEnum;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ChangeableEnum;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.DiagramProperty;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramFactory;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.NamedElement;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.TypeMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.TypedElement;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Variable;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!--
 * end-user-doc -->
 * @generated
 */
public class InstancediagramPackageImpl extends EPackageImpl implements
		InstancediagramPackage {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass namedElementEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass variableEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass instanceEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass classInstanceEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass associationInstanceEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass typedElementEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass instanceMapEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EClass diagramPropertyEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum aggregationEnumEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum changeableEnumEEnum = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum typeMultiplicityEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the
	 * package package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static factory
	 * method {@link #init init()}, which also performs initialization of the
	 * package, or returns the registered package, if one already exists. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private InstancediagramPackageImpl() {
		super(eNS_URI, InstancediagramFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link InstancediagramPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static InstancediagramPackage init() {
		if (isInited) return (InstancediagramPackage)EPackage.Registry.INSTANCE.getEPackage(InstancediagramPackage.eNS_URI);

		// Obtain or create and register package
		InstancediagramPackageImpl theInstancediagramPackage = (InstancediagramPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof InstancediagramPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new InstancediagramPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theInstancediagramPackage.createPackageContents();

		// Initialize created meta-data
		theInstancediagramPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theInstancediagramPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(InstancediagramPackage.eNS_URI, theInstancediagramPackage);
		return theInstancediagramPackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNamedElement() {
		return namedElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNamedElement_Name() {
		return (EAttribute)namedElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getVariable() {
		return variableEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVariable_Value() {
		return (EAttribute)variableEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getInstance() {
		return instanceEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInstance_Package() {
		return (EAttribute)instanceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInstance_ArtifactName() {
		return (EAttribute)instanceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getClassInstance() {
		return classInstanceEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getClassInstance_Variables() {
		return (EReference)classInstanceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getClassInstance_Associations() {
		return (EReference)classInstanceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getClassInstance_AssociationClassInstance() {
		return (EAttribute)classInstanceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAssociationInstance() {
		return associationInstanceEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAssociationInstance_AEnd() {
		return (EReference)associationInstanceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssociationInstance_AEndName() {
		return (EAttribute)associationInstanceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssociationInstance_AEndMultiplicityLowerBound() {
		return (EAttribute)associationInstanceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssociationInstance_AEndMultiplicityUpperBound() {
		return (EAttribute)associationInstanceEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssociationInstance_AEndIsNavigable() {
		return (EAttribute)associationInstanceEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssociationInstance_AEndIsOrdered() {
		return (EAttribute)associationInstanceEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssociationInstance_AEndIsChangeable() {
		return (EAttribute)associationInstanceEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssociationInstance_AEndAggregation() {
		return (EAttribute)associationInstanceEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAssociationInstance_ZEnd() {
		return (EReference)associationInstanceEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssociationInstance_ZEndName() {
		return (EAttribute)associationInstanceEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssociationInstance_ZEndMultiplicityLowerBound() {
		return (EAttribute)associationInstanceEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssociationInstance_ZEndMultiplicityUpperBound() {
		return (EAttribute)associationInstanceEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssociationInstance_ZEndIsNavigable() {
		return (EAttribute)associationInstanceEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssociationInstance_ZEndIsOrdered() {
		return (EAttribute)associationInstanceEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssociationInstance_ZEndIsChangeable() {
		return (EAttribute)associationInstanceEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssociationInstance_ZEndAggregation() {
		return (EAttribute)associationInstanceEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssociationInstance_ReferenceName() {
		return (EAttribute)associationInstanceEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssociationInstance_AEndOrder() {
		return (EAttribute)associationInstanceEClass.getEStructuralFeatures().get(17);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAssociationInstance_ZEndOrder() {
		return (EAttribute)associationInstanceEClass.getEStructuralFeatures().get(18);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTypedElement() {
		return typedElementEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTypedElement_Type() {
		return (EAttribute)typedElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTypedElement_Multiplicity() {
		return (EAttribute)typedElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getInstanceMap() {
		return instanceMapEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getInstanceMap_ClassInstances() {
		return (EReference)instanceMapEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getInstanceMap_AssociationInstances() {
		return (EReference)instanceMapEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInstanceMap_BasePackage() {
		return (EAttribute)instanceMapEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getInstanceMap_Properties() {
		return (EReference)instanceMapEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDiagramProperty() {
		return diagramPropertyEClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDiagramProperty_Name() {
		return (EAttribute)diagramPropertyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDiagramProperty_Value() {
		return (EAttribute)diagramPropertyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getAggregationEnum() {
		return aggregationEnumEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getChangeableEnum() {
		return changeableEnumEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getTypeMultiplicity() {
		return typeMultiplicityEEnum;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public InstancediagramFactory getInstancediagramFactory() {
		return (InstancediagramFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		namedElementEClass = createEClass(NAMED_ELEMENT);
		createEAttribute(namedElementEClass, NAMED_ELEMENT__NAME);

		variableEClass = createEClass(VARIABLE);
		createEAttribute(variableEClass, VARIABLE__VALUE);

		instanceEClass = createEClass(INSTANCE);
		createEAttribute(instanceEClass, INSTANCE__PACKAGE);
		createEAttribute(instanceEClass, INSTANCE__ARTIFACT_NAME);

		classInstanceEClass = createEClass(CLASS_INSTANCE);
		createEReference(classInstanceEClass, CLASS_INSTANCE__VARIABLES);
		createEReference(classInstanceEClass, CLASS_INSTANCE__ASSOCIATIONS);
		createEAttribute(classInstanceEClass, CLASS_INSTANCE__ASSOCIATION_CLASS_INSTANCE);

		associationInstanceEClass = createEClass(ASSOCIATION_INSTANCE);
		createEReference(associationInstanceEClass, ASSOCIATION_INSTANCE__AEND);
		createEAttribute(associationInstanceEClass, ASSOCIATION_INSTANCE__AEND_NAME);
		createEAttribute(associationInstanceEClass, ASSOCIATION_INSTANCE__AEND_MULTIPLICITY_LOWER_BOUND);
		createEAttribute(associationInstanceEClass, ASSOCIATION_INSTANCE__AEND_MULTIPLICITY_UPPER_BOUND);
		createEAttribute(associationInstanceEClass, ASSOCIATION_INSTANCE__AEND_IS_NAVIGABLE);
		createEAttribute(associationInstanceEClass, ASSOCIATION_INSTANCE__AEND_IS_ORDERED);
		createEAttribute(associationInstanceEClass, ASSOCIATION_INSTANCE__AEND_IS_CHANGEABLE);
		createEAttribute(associationInstanceEClass, ASSOCIATION_INSTANCE__AEND_AGGREGATION);
		createEReference(associationInstanceEClass, ASSOCIATION_INSTANCE__ZEND);
		createEAttribute(associationInstanceEClass, ASSOCIATION_INSTANCE__ZEND_NAME);
		createEAttribute(associationInstanceEClass, ASSOCIATION_INSTANCE__ZEND_MULTIPLICITY_LOWER_BOUND);
		createEAttribute(associationInstanceEClass, ASSOCIATION_INSTANCE__ZEND_MULTIPLICITY_UPPER_BOUND);
		createEAttribute(associationInstanceEClass, ASSOCIATION_INSTANCE__ZEND_IS_NAVIGABLE);
		createEAttribute(associationInstanceEClass, ASSOCIATION_INSTANCE__ZEND_IS_ORDERED);
		createEAttribute(associationInstanceEClass, ASSOCIATION_INSTANCE__ZEND_IS_CHANGEABLE);
		createEAttribute(associationInstanceEClass, ASSOCIATION_INSTANCE__ZEND_AGGREGATION);
		createEAttribute(associationInstanceEClass, ASSOCIATION_INSTANCE__REFERENCE_NAME);
		createEAttribute(associationInstanceEClass, ASSOCIATION_INSTANCE__AEND_ORDER);
		createEAttribute(associationInstanceEClass, ASSOCIATION_INSTANCE__ZEND_ORDER);

		typedElementEClass = createEClass(TYPED_ELEMENT);
		createEAttribute(typedElementEClass, TYPED_ELEMENT__TYPE);
		createEAttribute(typedElementEClass, TYPED_ELEMENT__MULTIPLICITY);

		instanceMapEClass = createEClass(INSTANCE_MAP);
		createEReference(instanceMapEClass, INSTANCE_MAP__CLASS_INSTANCES);
		createEReference(instanceMapEClass, INSTANCE_MAP__ASSOCIATION_INSTANCES);
		createEAttribute(instanceMapEClass, INSTANCE_MAP__BASE_PACKAGE);
		createEReference(instanceMapEClass, INSTANCE_MAP__PROPERTIES);

		diagramPropertyEClass = createEClass(DIAGRAM_PROPERTY);
		createEAttribute(diagramPropertyEClass, DIAGRAM_PROPERTY__NAME);
		createEAttribute(diagramPropertyEClass, DIAGRAM_PROPERTY__VALUE);

		// Create enums
		aggregationEnumEEnum = createEEnum(AGGREGATION_ENUM);
		changeableEnumEEnum = createEEnum(CHANGEABLE_ENUM);
		typeMultiplicityEEnum = createEEnum(TYPE_MULTIPLICITY);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model. This
	 * method is guarded to have no affect on any invocation but its first. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Add supertypes to classes
		variableEClass.getESuperTypes().add(this.getTypedElement());
		instanceEClass.getESuperTypes().add(this.getNamedElement());
		classInstanceEClass.getESuperTypes().add(this.getInstance());
		associationInstanceEClass.getESuperTypes().add(this.getInstance());
		typedElementEClass.getESuperTypes().add(this.getNamedElement());

		// Initialize classes and features; add operations and parameters
		initEClass(namedElementEClass, NamedElement.class, "NamedElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNamedElement_Name(), ecorePackage.getEString(), "name", null, 0, 1, NamedElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(variableEClass, Variable.class, "Variable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getVariable_Value(), ecorePackage.getEString(), "value", null, 0, 1, Variable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(instanceEClass, Instance.class, "Instance", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getInstance_Package(), ecorePackage.getEString(), "package", null, 0, 1, Instance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInstance_ArtifactName(), ecorePackage.getEString(), "artifactName", null, 0, 1, Instance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(instanceEClass, ecorePackage.getEString(), "getFullyQualifiedName", 0, 1);

		initEClass(classInstanceEClass, ClassInstance.class, "ClassInstance", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getClassInstance_Variables(), this.getVariable(), null, "variables", null, 0, -1, ClassInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getClassInstance_Associations(), this.getAssociationInstance(), null, "associations", null, 0, -1, ClassInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getClassInstance_AssociationClassInstance(), ecorePackage.getEBoolean(), "associationClassInstance", null, 0, 1, ClassInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(associationInstanceEClass, AssociationInstance.class, "AssociationInstance", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAssociationInstance_AEnd(), this.getInstance(), null, "aEnd", null, 0, 1, AssociationInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociationInstance_AEndName(), ecorePackage.getEString(), "aEndName", null, 0, 1, AssociationInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociationInstance_AEndMultiplicityLowerBound(), ecorePackage.getEString(), "aEndMultiplicityLowerBound", null, 0, 1, AssociationInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociationInstance_AEndMultiplicityUpperBound(), ecorePackage.getEString(), "aEndMultiplicityUpperBound", null, 0, 1, AssociationInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociationInstance_AEndIsNavigable(), ecorePackage.getEBoolean(), "aEndIsNavigable", null, 0, 1, AssociationInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociationInstance_AEndIsOrdered(), ecorePackage.getEBoolean(), "aEndIsOrdered", null, 0, 1, AssociationInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociationInstance_AEndIsChangeable(), this.getChangeableEnum(), "aEndIsChangeable", null, 0, 1, AssociationInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociationInstance_AEndAggregation(), this.getAggregationEnum(), "aEndAggregation", null, 0, 1, AssociationInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getAssociationInstance_ZEnd(), this.getInstance(), null, "zEnd", null, 0, 1, AssociationInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociationInstance_ZEndName(), ecorePackage.getEString(), "zEndName", null, 0, 1, AssociationInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociationInstance_ZEndMultiplicityLowerBound(), ecorePackage.getEString(), "zEndMultiplicityLowerBound", null, 0, 1, AssociationInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociationInstance_ZEndMultiplicityUpperBound(), ecorePackage.getEString(), "zEndMultiplicityUpperBound", null, 0, 1, AssociationInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociationInstance_ZEndIsNavigable(), ecorePackage.getEBoolean(), "zEndIsNavigable", null, 0, 1, AssociationInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociationInstance_ZEndIsOrdered(), ecorePackage.getEBoolean(), "zEndIsOrdered", null, 0, 1, AssociationInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociationInstance_ZEndIsChangeable(), this.getChangeableEnum(), "zEndIsChangeable", null, 0, 1, AssociationInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociationInstance_ZEndAggregation(), this.getAggregationEnum(), "zEndAggregation", null, 0, 1, AssociationInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociationInstance_ReferenceName(), ecorePackage.getEString(), "referenceName", null, 0, 1, AssociationInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociationInstance_AEndOrder(), ecorePackage.getEString(), "aEndOrder", null, 0, 1, AssociationInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAssociationInstance_ZEndOrder(), ecorePackage.getEString(), "zEndOrder", null, 0, 1, AssociationInstance.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(typedElementEClass, TypedElement.class, "TypedElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTypedElement_Type(), ecorePackage.getEString(), "type", null, 0, 1, TypedElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTypedElement_Multiplicity(), this.getTypeMultiplicity(), "multiplicity", null, 0, 1, TypedElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(instanceMapEClass, InstanceMap.class, "InstanceMap", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getInstanceMap_ClassInstances(), this.getClassInstance(), null, "classInstances", null, 0, -1, InstanceMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getInstanceMap_AssociationInstances(), this.getAssociationInstance(), null, "associationInstances", null, 0, -1, InstanceMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInstanceMap_BasePackage(), ecorePackage.getEString(), "basePackage", null, 0, 1, InstanceMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getInstanceMap_Properties(), this.getDiagramProperty(), null, "properties", null, 0, -1, InstanceMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(diagramPropertyEClass, DiagramProperty.class, "DiagramProperty", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDiagramProperty_Name(), ecorePackage.getEString(), "name", null, 0, 1, DiagramProperty.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDiagramProperty_Value(), ecorePackage.getEString(), "value", null, 0, 1, DiagramProperty.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(aggregationEnumEEnum, AggregationEnum.class, "AggregationEnum");
		addEEnumLiteral(aggregationEnumEEnum, AggregationEnum.NONE_LITERAL);
		addEEnumLiteral(aggregationEnumEEnum, AggregationEnum.SHARED_LITERAL);
		addEEnumLiteral(aggregationEnumEEnum, AggregationEnum.COMPOSITE_LITERAL);

		initEEnum(changeableEnumEEnum, ChangeableEnum.class, "ChangeableEnum");
		addEEnumLiteral(changeableEnumEEnum, ChangeableEnum.NONE_LITERAL);
		addEEnumLiteral(changeableEnumEEnum, ChangeableEnum.FROZEN_LITERAL);
		addEEnumLiteral(changeableEnumEEnum, ChangeableEnum.ADD_ONLY_LITERAL);

		initEEnum(typeMultiplicityEEnum, TypeMultiplicity.class, "TypeMultiplicity");
		addEEnumLiteral(typeMultiplicityEEnum, TypeMultiplicity.NONE_LITERAL);
		addEEnumLiteral(typeMultiplicityEEnum, TypeMultiplicity.ARRAY_LITERAL);
		addEEnumLiteral(typeMultiplicityEEnum, TypeMultiplicity.ARRAYOFARRAY_LITERAL);

		// Create resource
		createResource(eNS_URI);
	}

} // InstancediagramPackageImpl
