/**
 * <copyright>
 * </copyright>
 *
 * $Id: MetamodelSwitch.java,v 1.2 2008/05/22 18:26:36 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.util;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.tigerstripe.metamodel.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage
 * @generated
 */
public class MetamodelSwitch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static MetamodelPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MetamodelSwitch() {
		if (modelPackage == null) {
			modelPackage = MetamodelPackage.eINSTANCE;
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	public T doSwitch(EObject theEObject) {
		return doSwitch(theEObject.eClass(), theEObject);
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected T doSwitch(EClass theEClass, EObject theEObject) {
		if (theEClass.eContainer() == modelPackage) {
			return doSwitch(theEClass.getClassifierID(), theEObject);
		}
		else {
			List<EClass> eSuperTypes = theEClass.getESuperTypes();
			return
				eSuperTypes.isEmpty() ?
					defaultCase(theEObject) :
					doSwitch(eSuperTypes.get(0), theEObject);
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case MetamodelPackage.IABSTRACT_ARTIFACT: {
				IAbstractArtifact iAbstractArtifact = (IAbstractArtifact)theEObject;
				T result = caseIAbstractArtifact(iAbstractArtifact);
				if (result == null) result = caseIQualifiedNamedComponent(iAbstractArtifact);
				if (result == null) result = caseIModelComponent(iAbstractArtifact);
				if (result == null) result = caseIStereotypeCapable(iAbstractArtifact);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MetamodelPackage.IPRIMITIVE_TYPE: {
				IPrimitiveType iPrimitiveType = (IPrimitiveType)theEObject;
				T result = caseIPrimitiveType(iPrimitiveType);
				if (result == null) result = caseIAbstractArtifact(iPrimitiveType);
				if (result == null) result = caseIQualifiedNamedComponent(iPrimitiveType);
				if (result == null) result = caseIModelComponent(iPrimitiveType);
				if (result == null) result = caseIStereotypeCapable(iPrimitiveType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MetamodelPackage.IMANAGED_ENTITY_ARTIFACT: {
				IManagedEntityArtifact iManagedEntityArtifact = (IManagedEntityArtifact)theEObject;
				T result = caseIManagedEntityArtifact(iManagedEntityArtifact);
				if (result == null) result = caseIAbstractArtifact(iManagedEntityArtifact);
				if (result == null) result = caseIQualifiedNamedComponent(iManagedEntityArtifact);
				if (result == null) result = caseIModelComponent(iManagedEntityArtifact);
				if (result == null) result = caseIStereotypeCapable(iManagedEntityArtifact);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MetamodelPackage.IDATATYPE_ARTIFACT: {
				IDatatypeArtifact iDatatypeArtifact = (IDatatypeArtifact)theEObject;
				T result = caseIDatatypeArtifact(iDatatypeArtifact);
				if (result == null) result = caseIAbstractArtifact(iDatatypeArtifact);
				if (result == null) result = caseIQualifiedNamedComponent(iDatatypeArtifact);
				if (result == null) result = caseIModelComponent(iDatatypeArtifact);
				if (result == null) result = caseIStereotypeCapable(iDatatypeArtifact);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MetamodelPackage.IEXCEPTION_ARTIFACT: {
				IExceptionArtifact iExceptionArtifact = (IExceptionArtifact)theEObject;
				T result = caseIExceptionArtifact(iExceptionArtifact);
				if (result == null) result = caseIAbstractArtifact(iExceptionArtifact);
				if (result == null) result = caseIQualifiedNamedComponent(iExceptionArtifact);
				if (result == null) result = caseIModelComponent(iExceptionArtifact);
				if (result == null) result = caseIStereotypeCapable(iExceptionArtifact);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MetamodelPackage.ISESSION_ARTIFACT: {
				ISessionArtifact iSessionArtifact = (ISessionArtifact)theEObject;
				T result = caseISessionArtifact(iSessionArtifact);
				if (result == null) result = caseIAbstractArtifact(iSessionArtifact);
				if (result == null) result = caseIQualifiedNamedComponent(iSessionArtifact);
				if (result == null) result = caseIModelComponent(iSessionArtifact);
				if (result == null) result = caseIStereotypeCapable(iSessionArtifact);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MetamodelPackage.IQUERY_ARTIFACT: {
				IQueryArtifact iQueryArtifact = (IQueryArtifact)theEObject;
				T result = caseIQueryArtifact(iQueryArtifact);
				if (result == null) result = caseIAbstractArtifact(iQueryArtifact);
				if (result == null) result = caseIQualifiedNamedComponent(iQueryArtifact);
				if (result == null) result = caseIModelComponent(iQueryArtifact);
				if (result == null) result = caseIStereotypeCapable(iQueryArtifact);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MetamodelPackage.IUPDATE_PROCEDURE_ARTIFACT: {
				IUpdateProcedureArtifact iUpdateProcedureArtifact = (IUpdateProcedureArtifact)theEObject;
				T result = caseIUpdateProcedureArtifact(iUpdateProcedureArtifact);
				if (result == null) result = caseIAbstractArtifact(iUpdateProcedureArtifact);
				if (result == null) result = caseIQualifiedNamedComponent(iUpdateProcedureArtifact);
				if (result == null) result = caseIModelComponent(iUpdateProcedureArtifact);
				if (result == null) result = caseIStereotypeCapable(iUpdateProcedureArtifact);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MetamodelPackage.IEVENT_ARTIFACT: {
				IEventArtifact iEventArtifact = (IEventArtifact)theEObject;
				T result = caseIEventArtifact(iEventArtifact);
				if (result == null) result = caseIAbstractArtifact(iEventArtifact);
				if (result == null) result = caseIQualifiedNamedComponent(iEventArtifact);
				if (result == null) result = caseIModelComponent(iEventArtifact);
				if (result == null) result = caseIStereotypeCapable(iEventArtifact);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MetamodelPackage.IASSOCIATION_ARTIFACT: {
				IAssociationArtifact iAssociationArtifact = (IAssociationArtifact)theEObject;
				T result = caseIAssociationArtifact(iAssociationArtifact);
				if (result == null) result = caseIAbstractArtifact(iAssociationArtifact);
				if (result == null) result = caseIQualifiedNamedComponent(iAssociationArtifact);
				if (result == null) result = caseIModelComponent(iAssociationArtifact);
				if (result == null) result = caseIStereotypeCapable(iAssociationArtifact);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MetamodelPackage.IASSOCIATION_CLASS_ARTIFACT: {
				IAssociationClassArtifact iAssociationClassArtifact = (IAssociationClassArtifact)theEObject;
				T result = caseIAssociationClassArtifact(iAssociationClassArtifact);
				if (result == null) result = caseIAssociationArtifact(iAssociationClassArtifact);
				if (result == null) result = caseIManagedEntityArtifact(iAssociationClassArtifact);
				if (result == null) result = caseIAbstractArtifact(iAssociationClassArtifact);
				if (result == null) result = caseIQualifiedNamedComponent(iAssociationClassArtifact);
				if (result == null) result = caseIModelComponent(iAssociationClassArtifact);
				if (result == null) result = caseIStereotypeCapable(iAssociationClassArtifact);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MetamodelPackage.IDEPENDENCY_ARTIFACT: {
				IDependencyArtifact iDependencyArtifact = (IDependencyArtifact)theEObject;
				T result = caseIDependencyArtifact(iDependencyArtifact);
				if (result == null) result = caseIAbstractArtifact(iDependencyArtifact);
				if (result == null) result = caseIQualifiedNamedComponent(iDependencyArtifact);
				if (result == null) result = caseIModelComponent(iDependencyArtifact);
				if (result == null) result = caseIStereotypeCapable(iDependencyArtifact);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MetamodelPackage.IENUM_ARTIFACT: {
				IEnumArtifact iEnumArtifact = (IEnumArtifact)theEObject;
				T result = caseIEnumArtifact(iEnumArtifact);
				if (result == null) result = caseIAbstractArtifact(iEnumArtifact);
				if (result == null) result = caseIQualifiedNamedComponent(iEnumArtifact);
				if (result == null) result = caseIModelComponent(iEnumArtifact);
				if (result == null) result = caseIStereotypeCapable(iEnumArtifact);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MetamodelPackage.IFIELD: {
				IField iField = (IField)theEObject;
				T result = caseIField(iField);
				if (result == null) result = caseIModelComponent(iField);
				if (result == null) result = caseIStereotypeCapable(iField);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MetamodelPackage.IMETHOD: {
				IMethod iMethod = (IMethod)theEObject;
				T result = caseIMethod(iMethod);
				if (result == null) result = caseIModelComponent(iMethod);
				if (result == null) result = caseIStereotypeCapable(iMethod);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MetamodelPackage.ILITERAL: {
				ILiteral iLiteral = (ILiteral)theEObject;
				T result = caseILiteral(iLiteral);
				if (result == null) result = caseIModelComponent(iLiteral);
				if (result == null) result = caseIStereotypeCapable(iLiteral);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MetamodelPackage.IMODEL_COMPONENT: {
				IModelComponent iModelComponent = (IModelComponent)theEObject;
				T result = caseIModelComponent(iModelComponent);
				if (result == null) result = caseIStereotypeCapable(iModelComponent);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MetamodelPackage.IQUALIFIED_NAMED_COMPONENT: {
				IQualifiedNamedComponent iQualifiedNamedComponent = (IQualifiedNamedComponent)theEObject;
				T result = caseIQualifiedNamedComponent(iQualifiedNamedComponent);
				if (result == null) result = caseIModelComponent(iQualifiedNamedComponent);
				if (result == null) result = caseIStereotypeCapable(iQualifiedNamedComponent);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MetamodelPackage.ITYPE: {
				IType iType = (IType)theEObject;
				T result = caseIType(iType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MetamodelPackage.IASSOCIATION_END: {
				IAssociationEnd iAssociationEnd = (IAssociationEnd)theEObject;
				T result = caseIAssociationEnd(iAssociationEnd);
				if (result == null) result = caseIModelComponent(iAssociationEnd);
				if (result == null) result = caseIStereotypeCapable(iAssociationEnd);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MetamodelPackage.IARGUMENT: {
				IArgument iArgument = (IArgument)theEObject;
				T result = caseIArgument(iArgument);
				if (result == null) result = caseIModelComponent(iArgument);
				if (result == null) result = caseIStereotypeCapable(iArgument);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MetamodelPackage.IMODEL: {
				IModel iModel = (IModel)theEObject;
				T result = caseIModel(iModel);
				if (result == null) result = caseIModelComponent(iModel);
				if (result == null) result = caseIStereotypeCapable(iModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MetamodelPackage.IPACKAGE: {
				IPackage iPackage = (IPackage)theEObject;
				T result = caseIPackage(iPackage);
				if (result == null) result = caseIModelComponent(iPackage);
				if (result == null) result = caseIStereotypeCapable(iPackage);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MetamodelPackage.ISTEREOTYPE_CAPABLE: {
				IStereotypeCapable iStereotypeCapable = (IStereotypeCapable)theEObject;
				T result = caseIStereotypeCapable(iStereotypeCapable);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MetamodelPackage.ISTEREOTYPE_INSTANCE: {
				IStereotypeInstance iStereotypeInstance = (IStereotypeInstance)theEObject;
				T result = caseIStereotypeInstance(iStereotypeInstance);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MetamodelPackage.ISTEREOTYPE_ATTRIBUTE_VALUE: {
				IStereotypeAttributeValue iStereotypeAttributeValue = (IStereotypeAttributeValue)theEObject;
				T result = caseIStereotypeAttributeValue(iStereotypeAttributeValue);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MetamodelPackage.IMULTIPLICITY: {
				IMultiplicity iMultiplicity = (IMultiplicity)theEObject;
				T result = caseIMultiplicity(iMultiplicity);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IAbstract Artifact</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IAbstract Artifact</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIAbstractArtifact(IAbstractArtifact object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IPrimitive Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IPrimitive Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIPrimitiveType(IPrimitiveType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IManaged Entity Artifact</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IManaged Entity Artifact</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIManagedEntityArtifact(IManagedEntityArtifact object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IDatatype Artifact</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IDatatype Artifact</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIDatatypeArtifact(IDatatypeArtifact object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IException Artifact</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IException Artifact</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIExceptionArtifact(IExceptionArtifact object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>ISession Artifact</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ISession Artifact</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseISessionArtifact(ISessionArtifact object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IQuery Artifact</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IQuery Artifact</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIQueryArtifact(IQueryArtifact object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IUpdate Procedure Artifact</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IUpdate Procedure Artifact</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIUpdateProcedureArtifact(IUpdateProcedureArtifact object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IEvent Artifact</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IEvent Artifact</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIEventArtifact(IEventArtifact object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IAssociation Artifact</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IAssociation Artifact</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIAssociationArtifact(IAssociationArtifact object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IAssociation Class Artifact</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IAssociation Class Artifact</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIAssociationClassArtifact(IAssociationClassArtifact object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IDependency Artifact</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IDependency Artifact</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIDependencyArtifact(IDependencyArtifact object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IEnum Artifact</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IEnum Artifact</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIEnumArtifact(IEnumArtifact object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IField</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IField</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIField(IField object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IMethod</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IMethod</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIMethod(IMethod object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>ILiteral</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ILiteral</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseILiteral(ILiteral object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IModel Component</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IModel Component</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIModelComponent(IModelComponent object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IQualified Named Component</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IQualified Named Component</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIQualifiedNamedComponent(IQualifiedNamedComponent object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IType</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IType</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIType(IType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IAssociation End</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IAssociation End</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIAssociationEnd(IAssociationEnd object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IArgument</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IArgument</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIArgument(IArgument object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IModel</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IModel</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIModel(IModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IPackage</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IPackage</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIPackage(IPackage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IStereotype Capable</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IStereotype Capable</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIStereotypeCapable(IStereotypeCapable object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IStereotype Instance</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IStereotype Instance</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIStereotypeInstance(IStereotypeInstance object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IStereotype Attribute Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IStereotype Attribute Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIStereotypeAttributeValue(IStereotypeAttributeValue object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IMultiplicity</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IMultiplicity</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIMultiplicity(IMultiplicity object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	public T defaultCase(EObject object) {
		return null;
	}

} //MetamodelSwitch
