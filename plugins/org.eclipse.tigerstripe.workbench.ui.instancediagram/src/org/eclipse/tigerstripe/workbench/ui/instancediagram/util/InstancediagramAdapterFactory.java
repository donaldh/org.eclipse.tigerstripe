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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.DiagramProperty;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.NamedElement;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.TypedElement;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Variable;

/**
 * <!-- begin-user-doc --> The <b>Adapter Factory</b> for the model. It
 * provides an adapter <code>createXXX</code> method for each class of the
 * model. <!-- end-user-doc -->
 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage
 * @generated
 */
public class InstancediagramAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected static InstancediagramPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 */
	public InstancediagramAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = InstancediagramPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc --> This implementation returns <code>true</code>
	 * if the object is either the model's package or is an instance object of
	 * the model. <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch the delegates to the <code>createXXX</code> methods. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected InstancediagramSwitch modelSwitch = new InstancediagramSwitch() {
			public Object caseNamedElement(NamedElement object) {
				return createNamedElementAdapter();
			}
			public Object caseVariable(Variable object) {
				return createVariableAdapter();
			}
			public Object caseInstance(Instance object) {
				return createInstanceAdapter();
			}
			public Object caseClassInstance(ClassInstance object) {
				return createClassInstanceAdapter();
			}
			public Object caseAssociationInstance(AssociationInstance object) {
				return createAssociationInstanceAdapter();
			}
			public Object caseTypedElement(TypedElement object) {
				return createTypedElementAdapter();
			}
			public Object caseInstanceMap(InstanceMap object) {
				return createInstanceMapAdapter();
			}
			public Object caseDiagramProperty(DiagramProperty object) {
				return createDiagramPropertyAdapter();
			}
			public Object defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return (Adapter)modelSwitch.doSwitch((EObject)target);
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.NamedElement <em>Named Element</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.NamedElement
	 * @generated
	 */
	public Adapter createNamedElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.Variable <em>Variable</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.Variable
	 * @generated
	 */
	public Adapter createVariableAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance <em>Instance</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance
	 * @generated
	 */
	public Adapter createInstanceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance <em>Class Instance</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance
	 * @generated
	 */
	public Adapter createClassInstanceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance <em>Association Instance</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance
	 * @generated
	 */
	public Adapter createAssociationInstanceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.TypedElement <em>Typed Element</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.TypedElement
	 * @generated
	 */
	public Adapter createTypedElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap <em>Instance Map</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap
	 * @generated
	 */
	public Adapter createInstanceMapAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.DiagramProperty <em>Diagram Property</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.DiagramProperty
	 * @generated
	 */
	public Adapter createDiagramPropertyAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc --> This
	 * default implementation returns null. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} // InstancediagramAdapterFactory
