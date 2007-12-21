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

import java.util.List;

import org.eclipse.emf.ecore.EClass;
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
 * <!-- begin-user-doc --> The <b>Switch</b> for the model's inheritance
 * hierarchy. It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object and proceeding up the
 * inheritance hierarchy until a non-null result is returned, which is the
 * result of the switch. <!-- end-user-doc -->
 * 
 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage
 * @generated
 */
public class VisualeditorSwitch {
	/**
	 * The cached model package <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected static VisualeditorPackage modelPackage;

	/**
	 * Creates an instance of the switch. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public VisualeditorSwitch() {
		if (modelPackage == null) {
			modelPackage = VisualeditorPackage.eINSTANCE;
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one
	 * returns a non null result; it yields that result. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the first non-null result returned by a <code>caseXXX</code>
	 *         call.
	 * @generated
	 */
	public Object doSwitch(EObject theEObject) {
		return doSwitch(theEObject.eClass(), theEObject);
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one
	 * returns a non null result; it yields that result. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the first non-null result returned by a <code>caseXXX</code>
	 *         call.
	 * @generated
	 */
	protected Object doSwitch(EClass theEClass, EObject theEObject) {
		if (theEClass.eContainer() == modelPackage)
			return doSwitch(theEClass.getClassifierID(), theEObject);
		else {
			List eSuperTypes = theEClass.getESuperTypes();
			return eSuperTypes.isEmpty() ? defaultCase(theEObject) : doSwitch(
					(EClass) eSuperTypes.get(0), theEObject);
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one
	 * returns a non null result; it yields that result. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the first non-null result returned by a <code>caseXXX</code>
	 *         call.
	 * @generated
	 */
	protected Object doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
		case VisualeditorPackage.NAMED_ELEMENT: {
			NamedElement namedElement = (NamedElement) theEObject;
			Object result = caseNamedElement(namedElement);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case VisualeditorPackage.QUALIFIED_NAMED_ELEMENT: {
			QualifiedNamedElement qualifiedNamedElement = (QualifiedNamedElement) theEObject;
			Object result = caseQualifiedNamedElement(qualifiedNamedElement);
			if (result == null)
				result = caseNamedElement(qualifiedNamedElement);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case VisualeditorPackage.ABSTRACT_ARTIFACT: {
			AbstractArtifact abstractArtifact = (AbstractArtifact) theEObject;
			Object result = caseAbstractArtifact(abstractArtifact);
			if (result == null)
				result = caseQualifiedNamedElement(abstractArtifact);
			if (result == null)
				result = caseNamedElement(abstractArtifact);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case VisualeditorPackage.MANAGED_ENTITY_ARTIFACT: {
			ManagedEntityArtifact managedEntityArtifact = (ManagedEntityArtifact) theEObject;
			Object result = caseManagedEntityArtifact(managedEntityArtifact);
			if (result == null)
				result = caseAbstractArtifact(managedEntityArtifact);
			if (result == null)
				result = caseQualifiedNamedElement(managedEntityArtifact);
			if (result == null)
				result = caseNamedElement(managedEntityArtifact);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case VisualeditorPackage.DATATYPE_ARTIFACT: {
			DatatypeArtifact datatypeArtifact = (DatatypeArtifact) theEObject;
			Object result = caseDatatypeArtifact(datatypeArtifact);
			if (result == null)
				result = caseAbstractArtifact(datatypeArtifact);
			if (result == null)
				result = caseQualifiedNamedElement(datatypeArtifact);
			if (result == null)
				result = caseNamedElement(datatypeArtifact);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case VisualeditorPackage.NOTIFICATION_ARTIFACT: {
			NotificationArtifact notificationArtifact = (NotificationArtifact) theEObject;
			Object result = caseNotificationArtifact(notificationArtifact);
			if (result == null)
				result = caseAbstractArtifact(notificationArtifact);
			if (result == null)
				result = caseQualifiedNamedElement(notificationArtifact);
			if (result == null)
				result = caseNamedElement(notificationArtifact);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case VisualeditorPackage.NAMED_QUERY_ARTIFACT: {
			NamedQueryArtifact namedQueryArtifact = (NamedQueryArtifact) theEObject;
			Object result = caseNamedQueryArtifact(namedQueryArtifact);
			if (result == null)
				result = caseAbstractArtifact(namedQueryArtifact);
			if (result == null)
				result = caseQualifiedNamedElement(namedQueryArtifact);
			if (result == null)
				result = caseNamedElement(namedQueryArtifact);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case VisualeditorPackage.ENUMERATION: {
			Enumeration enumeration = (Enumeration) theEObject;
			Object result = caseEnumeration(enumeration);
			if (result == null)
				result = caseAbstractArtifact(enumeration);
			if (result == null)
				result = caseQualifiedNamedElement(enumeration);
			if (result == null)
				result = caseNamedElement(enumeration);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case VisualeditorPackage.UPDATE_PROCEDURE_ARTIFACT: {
			UpdateProcedureArtifact updateProcedureArtifact = (UpdateProcedureArtifact) theEObject;
			Object result = caseUpdateProcedureArtifact(updateProcedureArtifact);
			if (result == null)
				result = caseAbstractArtifact(updateProcedureArtifact);
			if (result == null)
				result = caseQualifiedNamedElement(updateProcedureArtifact);
			if (result == null)
				result = caseNamedElement(updateProcedureArtifact);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case VisualeditorPackage.EXCEPTION_ARTIFACT: {
			ExceptionArtifact exceptionArtifact = (ExceptionArtifact) theEObject;
			Object result = caseExceptionArtifact(exceptionArtifact);
			if (result == null)
				result = caseAbstractArtifact(exceptionArtifact);
			if (result == null)
				result = caseQualifiedNamedElement(exceptionArtifact);
			if (result == null)
				result = caseNamedElement(exceptionArtifact);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case VisualeditorPackage.SESSION_FACADE_ARTIFACT: {
			SessionFacadeArtifact sessionFacadeArtifact = (SessionFacadeArtifact) theEObject;
			Object result = caseSessionFacadeArtifact(sessionFacadeArtifact);
			if (result == null)
				result = caseAbstractArtifact(sessionFacadeArtifact);
			if (result == null)
				result = caseQualifiedNamedElement(sessionFacadeArtifact);
			if (result == null)
				result = caseNamedElement(sessionFacadeArtifact);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case VisualeditorPackage.ASSOCIATION: {
			Association association = (Association) theEObject;
			Object result = caseAssociation(association);
			if (result == null)
				result = caseQualifiedNamedElement(association);
			if (result == null)
				result = caseNamedElement(association);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case VisualeditorPackage.ASSOCIATION_CLASS: {
			AssociationClass associationClass = (AssociationClass) theEObject;
			Object result = caseAssociationClass(associationClass);
			if (result == null)
				result = caseAssociation(associationClass);
			if (result == null)
				result = caseQualifiedNamedElement(associationClass);
			if (result == null)
				result = caseNamedElement(associationClass);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case VisualeditorPackage.TYPED_ELEMENT: {
			TypedElement typedElement = (TypedElement) theEObject;
			Object result = caseTypedElement(typedElement);
			if (result == null)
				result = caseNamedElement(typedElement);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case VisualeditorPackage.ATTRIBUTE: {
			Attribute attribute = (Attribute) theEObject;
			Object result = caseAttribute(attribute);
			if (result == null)
				result = caseTypedElement(attribute);
			if (result == null)
				result = caseNamedElement(attribute);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case VisualeditorPackage.METHOD: {
			Method method = (Method) theEObject;
			Object result = caseMethod(method);
			if (result == null)
				result = caseTypedElement(method);
			if (result == null)
				result = caseNamedElement(method);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case VisualeditorPackage.LITERAL: {
			Literal literal = (Literal) theEObject;
			Object result = caseLiteral(literal);
			if (result == null)
				result = caseTypedElement(literal);
			if (result == null)
				result = caseNamedElement(literal);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case VisualeditorPackage.PARAMETER: {
			Parameter parameter = (Parameter) theEObject;
			Object result = caseParameter(parameter);
			if (result == null)
				result = caseTypedElement(parameter);
			if (result == null)
				result = caseNamedElement(parameter);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case VisualeditorPackage.MAP: {
			Map map = (Map) theEObject;
			Object result = caseMap(map);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case VisualeditorPackage.REFERENCE: {
			Reference reference = (Reference) theEObject;
			Object result = caseReference(reference);
			if (result == null)
				result = caseNamedElement(reference);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case VisualeditorPackage.DEPENDENCY: {
			Dependency dependency = (Dependency) theEObject;
			Object result = caseDependency(dependency);
			if (result == null)
				result = caseQualifiedNamedElement(dependency);
			if (result == null)
				result = caseNamedElement(dependency);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case VisualeditorPackage.ASSOCIATION_CLASS_CLASS: {
			AssociationClassClass associationClassClass = (AssociationClassClass) theEObject;
			Object result = caseAssociationClassClass(associationClassClass);
			if (result == null)
				result = caseAbstractArtifact(associationClassClass);
			if (result == null)
				result = caseQualifiedNamedElement(associationClassClass);
			if (result == null)
				result = caseNamedElement(associationClassClass);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case VisualeditorPackage.DIAGRAM_PROPERTY: {
			DiagramProperty diagramProperty = (DiagramProperty) theEObject;
			Object result = caseDiagramProperty(diagramProperty);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		default:
			return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Named Element</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Named Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseNamedElement(NamedElement object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Qualified Named Element</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Qualified Named Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseQualifiedNamedElement(QualifiedNamedElement object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Abstract Artifact</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Abstract Artifact</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseAbstractArtifact(AbstractArtifact object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Managed Entity Artifact</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Managed Entity Artifact</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseManagedEntityArtifact(ManagedEntityArtifact object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Datatype Artifact</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Datatype Artifact</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseDatatypeArtifact(DatatypeArtifact object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Notification Artifact</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Notification Artifact</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseNotificationArtifact(NotificationArtifact object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Named Query Artifact</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Named Query Artifact</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseNamedQueryArtifact(NamedQueryArtifact object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Enumeration</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Enumeration</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseEnumeration(Enumeration object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Update Procedure Artifact</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Update Procedure Artifact</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseUpdateProcedureArtifact(UpdateProcedureArtifact object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Exception Artifact</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Exception Artifact</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseExceptionArtifact(ExceptionArtifact object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Session Facade Artifact</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Session Facade Artifact</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseSessionFacadeArtifact(SessionFacadeArtifact object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Association</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Association</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseAssociation(Association object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Association Class</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Association Class</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseAssociationClass(AssociationClass object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Typed Element</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Typed Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseTypedElement(TypedElement object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Attribute</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Attribute</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseAttribute(Attribute object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Method</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Method</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseMethod(Method object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Literal</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Literal</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseLiteral(Literal object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Parameter</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Parameter</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseParameter(Parameter object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Map</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Map</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseMap(Map object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Reference</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseReference(Reference object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Dependency</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Dependency</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseDependency(Dependency object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Association Class Class</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Association Class Class</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseAssociationClassClass(AssociationClassClass object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>Diagram Property</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>Diagram Property</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseDiagramProperty(DiagramProperty object) {
		return null;
	}

	/**
	 * Returns the result of interpretting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch, but this is the last case
	 * anyway. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpretting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	public Object defaultCase(EObject object) {
		return null;
	}

} // VisualeditorSwitch
