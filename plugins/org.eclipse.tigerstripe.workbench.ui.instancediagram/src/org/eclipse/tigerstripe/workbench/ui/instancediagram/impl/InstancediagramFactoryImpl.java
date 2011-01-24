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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.*;
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
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!--
 * end-user-doc -->
 * @generated
 */
public class InstancediagramFactoryImpl extends EFactoryImpl implements
		InstancediagramFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 */
	public static InstancediagramFactory init() {
		try {
			InstancediagramFactory theInstancediagramFactory = (InstancediagramFactory)EPackage.Registry.INSTANCE.getEFactory("org.eclipse.tigerstripe.workbench.ui.instancediagram"); 
			if (theInstancediagramFactory != null) {
				return theInstancediagramFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new InstancediagramFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 */
	public InstancediagramFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case InstancediagramPackage.NAMED_ELEMENT: return createNamedElement();
			case InstancediagramPackage.VARIABLE: return createVariable();
			case InstancediagramPackage.INSTANCE: return createInstance();
			case InstancediagramPackage.CLASS_INSTANCE: return createClassInstance();
			case InstancediagramPackage.ASSOCIATION_INSTANCE: return createAssociationInstance();
			case InstancediagramPackage.TYPED_ELEMENT: return createTypedElement();
			case InstancediagramPackage.INSTANCE_MAP: return createInstanceMap();
			case InstancediagramPackage.DIAGRAM_PROPERTY: return createDiagramProperty();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case InstancediagramPackage.AGGREGATION_ENUM:
				return createAggregationEnumFromString(eDataType, initialValue);
			case InstancediagramPackage.CHANGEABLE_ENUM:
				return createChangeableEnumFromString(eDataType, initialValue);
			case InstancediagramPackage.TYPE_MULTIPLICITY:
				return createTypeMultiplicityFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case InstancediagramPackage.AGGREGATION_ENUM:
				return convertAggregationEnumToString(eDataType, instanceValue);
			case InstancediagramPackage.CHANGEABLE_ENUM:
				return convertChangeableEnumToString(eDataType, instanceValue);
			case InstancediagramPackage.TYPE_MULTIPLICITY:
				return convertTypeMultiplicityToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NamedElement createNamedElement() {
		NamedElementImpl namedElement = new NamedElementImpl();
		return namedElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Variable createVariable() {
		VariableImpl variable = new VariableImpl();
		return variable;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Instance createInstance() {
		InstanceImpl instance = new InstanceImpl();
		return instance;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ClassInstance createClassInstance() {
		ClassInstanceImpl classInstance = new ClassInstanceImpl();
		return classInstance;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public AssociationInstance createAssociationInstance() {
		AssociationInstanceImpl associationInstance = new AssociationInstanceImpl();
		return associationInstance;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public TypedElement createTypedElement() {
		TypedElementImpl typedElement = new TypedElementImpl();
		return typedElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public InstanceMap createInstanceMap() {
		InstanceMapImpl instanceMap = new InstanceMapImpl();
		return instanceMap;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramProperty createDiagramProperty() {
		DiagramPropertyImpl diagramProperty = new DiagramPropertyImpl();
		return diagramProperty;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public AggregationEnum createAggregationEnumFromString(EDataType eDataType,
			String initialValue) {
		AggregationEnum result = AggregationEnum.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertAggregationEnumToString(EDataType eDataType,
			Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeableEnum createChangeableEnumFromString(EDataType eDataType,
			String initialValue) {
		ChangeableEnum result = ChangeableEnum.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertChangeableEnumToString(EDataType eDataType,
			Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public TypeMultiplicity createTypeMultiplicityFromString(
			EDataType eDataType, String initialValue) {
		TypeMultiplicity result = TypeMultiplicity.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String convertTypeMultiplicityToString(EDataType eDataType,
			Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public InstancediagramPackage getInstancediagramPackage() {
		return (InstancediagramPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static InstancediagramPackage getPackage() {
		return InstancediagramPackage.eINSTANCE;
	}

} // InstancediagramFactoryImpl
