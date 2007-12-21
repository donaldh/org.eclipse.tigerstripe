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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClassClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.DiagramProperty;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Enumeration;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Method;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NotificationArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Parameter;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.TypedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;

/**
 * <!-- begin-user-doc --> The <b>Adapter Factory</b> for the model. It
 * provides an adapter <code>createXXX</code> method for each class of the
 * model. <!-- end-user-doc -->
 * 
 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage
 * @generated
 */
public class VisualeditorAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected static VisualeditorPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public VisualeditorAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = VisualeditorPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc --> This implementation returns <code>true</code>
	 * if the object is either the model's package or is an instance object of
	 * the model. <!-- end-user-doc -->
	 * 
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage)
			return true;
		if (object instanceof EObject)
			return ((EObject) object).eClass().getEPackage() == modelPackage;
		return false;
	}

	/**
	 * The switch the delegates to the <code>createXXX</code> methods. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected VisualeditorSwitch modelSwitch = new VisualeditorSwitch() {
		@Override
		public Object caseNamedElement(NamedElement object) {
			return createNamedElementAdapter();
		}

		@Override
		public Object caseQualifiedNamedElement(QualifiedNamedElement object) {
			return createQualifiedNamedElementAdapter();
		}

		@Override
		public Object caseAbstractArtifact(AbstractArtifact object) {
			return createAbstractArtifactAdapter();
		}

		@Override
		public Object caseManagedEntityArtifact(ManagedEntityArtifact object) {
			return createManagedEntityArtifactAdapter();
		}

		@Override
		public Object caseDatatypeArtifact(DatatypeArtifact object) {
			return createDatatypeArtifactAdapter();
		}

		@Override
		public Object caseNotificationArtifact(NotificationArtifact object) {
			return createNotificationArtifactAdapter();
		}

		@Override
		public Object caseNamedQueryArtifact(NamedQueryArtifact object) {
			return createNamedQueryArtifactAdapter();
		}

		@Override
		public Object caseEnumeration(Enumeration object) {
			return createEnumerationAdapter();
		}

		@Override
		public Object caseUpdateProcedureArtifact(UpdateProcedureArtifact object) {
			return createUpdateProcedureArtifactAdapter();
		}

		@Override
		public Object caseExceptionArtifact(ExceptionArtifact object) {
			return createExceptionArtifactAdapter();
		}

		@Override
		public Object caseSessionFacadeArtifact(SessionFacadeArtifact object) {
			return createSessionFacadeArtifactAdapter();
		}

		@Override
		public Object caseAssociation(Association object) {
			return createAssociationAdapter();
		}

		@Override
		public Object caseAssociationClass(AssociationClass object) {
			return createAssociationClassAdapter();
		}

		@Override
		public Object caseTypedElement(TypedElement object) {
			return createTypedElementAdapter();
		}

		@Override
		public Object caseAttribute(Attribute object) {
			return createAttributeAdapter();
		}

		@Override
		public Object caseMethod(Method object) {
			return createMethodAdapter();
		}

		@Override
		public Object caseLiteral(Literal object) {
			return createLiteralAdapter();
		}

		@Override
		public Object caseParameter(Parameter object) {
			return createParameterAdapter();
		}

		@Override
		public Object caseMap(Map object) {
			return createMapAdapter();
		}

		@Override
		public Object caseReference(Reference object) {
			return createReferenceAdapter();
		}

		@Override
		public Object caseDependency(Dependency object) {
			return createDependencyAdapter();
		}

		@Override
		public Object caseAssociationClassClass(AssociationClassClass object) {
			return createAssociationClassClassAdapter();
		}

		@Override
		public Object caseDiagramProperty(DiagramProperty object) {
			return createDiagramPropertyAdapter();
		}

		@Override
		public Object defaultCase(EObject object) {
			return createEObjectAdapter();
		}
	};

	/**
	 * Creates an adapter for the <code>target</code>. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @param target
	 *            the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return (Adapter) modelSwitch.doSwitch((EObject) target);
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedElement <em>Named Element</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedElement
	 * @generated
	 */
	public Adapter createNamedElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement <em>Qualified Named Element</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement
	 * @generated
	 */
	public Adapter createQualifiedNamedElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact <em>Abstract Artifact</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact
	 * @generated
	 */
	public Adapter createAbstractArtifactAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.ManagedEntityArtifact <em>Managed Entity Artifact</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.ManagedEntityArtifact
	 * @generated
	 */
	public Adapter createManagedEntityArtifactAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.DatatypeArtifact <em>Datatype Artifact</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.DatatypeArtifact
	 * @generated
	 */
	public Adapter createDatatypeArtifactAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.NotificationArtifact <em>Notification Artifact</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.NotificationArtifact
	 * @generated
	 */
	public Adapter createNotificationArtifactAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact <em>Named Query Artifact</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact
	 * @generated
	 */
	public Adapter createNamedQueryArtifactAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Enumeration <em>Enumeration</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Enumeration
	 * @generated
	 */
	public Adapter createEnumerationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.UpdateProcedureArtifact <em>Update Procedure Artifact</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.UpdateProcedureArtifact
	 * @generated
	 */
	public Adapter createUpdateProcedureArtifactAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.ExceptionArtifact <em>Exception Artifact</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.ExceptionArtifact
	 * @generated
	 */
	public Adapter createExceptionArtifactAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact <em>Session Facade Artifact</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact
	 * @generated
	 */
	public Adapter createSessionFacadeArtifactAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association <em>Association</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Association
	 * @generated
	 */
	public Adapter createAssociationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass <em>Association Class</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass
	 * @generated
	 */
	public Adapter createAssociationClassAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.TypedElement <em>Typed Element</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.TypedElement
	 * @generated
	 */
	public Adapter createTypedElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute
	 * @generated
	 */
	public Adapter createAttributeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Method <em>Method</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Method
	 * @generated
	 */
	public Adapter createMethodAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal <em>Literal</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal
	 * @generated
	 */
	public Adapter createLiteralAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Parameter <em>Parameter</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Parameter
	 * @generated
	 */
	public Adapter createParameterAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Map <em>Map</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Map
	 * @generated
	 */
	public Adapter createMapAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference <em>Reference</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference
	 * @generated
	 */
	public Adapter createReferenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency <em>Dependency</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency
	 * @generated
	 */
	public Adapter createDependencyAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClassClass <em>Association Class Class</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClassClass
	 * @generated
	 */
	public Adapter createAssociationClassClassAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.DiagramProperty <em>Diagram Property</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that
	 * we can easily ignore cases; it's useful to ignore a case when inheritance
	 * will catch all the cases anyway. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.DiagramProperty
	 * @generated
	 */
	public Adapter createDiagramPropertyAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case. <!-- begin-user-doc --> This
	 * default implementation returns null. <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} // VisualeditorAdapterFactory
