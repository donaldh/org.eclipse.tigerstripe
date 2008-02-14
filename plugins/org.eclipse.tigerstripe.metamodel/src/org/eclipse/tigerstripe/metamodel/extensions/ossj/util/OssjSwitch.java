/**
 * <copyright>
 * </copyright>
 *
 * $Id: OssjSwitch.java,v 1.1 2008/02/14 23:58:01 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.extensions.ossj.util;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.tigerstripe.metamodel.extensions.IStandardSpecifics;

import org.eclipse.tigerstripe.metamodel.extensions.ossj.*;

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
 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage
 * @generated
 */
public class OssjSwitch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static OssjPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OssjSwitch() {
		if (modelPackage == null) {
			modelPackage = OssjPackage.eINSTANCE;
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
			case OssjPackage.IOSSJ_ARTIFACT_SPECIFICS: {
				IOssjArtifactSpecifics iOssjArtifactSpecifics = (IOssjArtifactSpecifics)theEObject;
				T result = caseIOssjArtifactSpecifics(iOssjArtifactSpecifics);
				if (result == null) result = caseIStandardSpecifics(iOssjArtifactSpecifics);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case OssjPackage.IOSSJ_DATATYPE_SPECIFICS: {
				IOssjDatatypeSpecifics iOssjDatatypeSpecifics = (IOssjDatatypeSpecifics)theEObject;
				T result = caseIOssjDatatypeSpecifics(iOssjDatatypeSpecifics);
				if (result == null) result = caseIOssjArtifactSpecifics(iOssjDatatypeSpecifics);
				if (result == null) result = caseIStandardSpecifics(iOssjDatatypeSpecifics);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case OssjPackage.IOSSJ_ENTITY_SPECIFICS: {
				IOssjEntitySpecifics iOssjEntitySpecifics = (IOssjEntitySpecifics)theEObject;
				T result = caseIOssjEntitySpecifics(iOssjEntitySpecifics);
				if (result == null) result = caseIOssjArtifactSpecifics(iOssjEntitySpecifics);
				if (result == null) result = caseIStandardSpecifics(iOssjEntitySpecifics);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case OssjPackage.IOSSJ_ENUM_SPECIFICS: {
				IOssjEnumSpecifics iOssjEnumSpecifics = (IOssjEnumSpecifics)theEObject;
				T result = caseIOssjEnumSpecifics(iOssjEnumSpecifics);
				if (result == null) result = caseIOssjArtifactSpecifics(iOssjEnumSpecifics);
				if (result == null) result = caseIStandardSpecifics(iOssjEnumSpecifics);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case OssjPackage.IOSSJ_EVENT_SPECIFICS: {
				IOssjEventSpecifics iOssjEventSpecifics = (IOssjEventSpecifics)theEObject;
				T result = caseIOssjEventSpecifics(iOssjEventSpecifics);
				if (result == null) result = caseIOssjArtifactSpecifics(iOssjEventSpecifics);
				if (result == null) result = caseIStandardSpecifics(iOssjEventSpecifics);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case OssjPackage.IEVENT_DESCRIPTOR_ENTRY: {
				IEventDescriptorEntry iEventDescriptorEntry = (IEventDescriptorEntry)theEObject;
				T result = caseIEventDescriptorEntry(iEventDescriptorEntry);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case OssjPackage.IOSSJ_QUERY_SPECIFICS: {
				IOssjQuerySpecifics iOssjQuerySpecifics = (IOssjQuerySpecifics)theEObject;
				T result = caseIOssjQuerySpecifics(iOssjQuerySpecifics);
				if (result == null) result = caseIOssjArtifactSpecifics(iOssjQuerySpecifics);
				if (result == null) result = caseIStandardSpecifics(iOssjQuerySpecifics);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case OssjPackage.IOSSJ_UPDATE_PROCEDURE_SPECIFICS: {
				IOssjUpdateProcedureSpecifics iOssjUpdateProcedureSpecifics = (IOssjUpdateProcedureSpecifics)theEObject;
				T result = caseIOssjUpdateProcedureSpecifics(iOssjUpdateProcedureSpecifics);
				if (result == null) result = caseIOssjArtifactSpecifics(iOssjUpdateProcedureSpecifics);
				if (result == null) result = caseIStandardSpecifics(iOssjUpdateProcedureSpecifics);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case OssjPackage.IMANAGED_ENTITY_DETAILS: {
				IManagedEntityDetails iManagedEntityDetails = (IManagedEntityDetails)theEObject;
				T result = caseIManagedEntityDetails(iManagedEntityDetails);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IOssj Artifact Specifics</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IOssj Artifact Specifics</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIOssjArtifactSpecifics(IOssjArtifactSpecifics object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IOssj Datatype Specifics</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IOssj Datatype Specifics</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIOssjDatatypeSpecifics(IOssjDatatypeSpecifics object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IOssj Entity Specifics</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IOssj Entity Specifics</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIOssjEntitySpecifics(IOssjEntitySpecifics object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IOssj Enum Specifics</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IOssj Enum Specifics</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIOssjEnumSpecifics(IOssjEnumSpecifics object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IOssj Event Specifics</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IOssj Event Specifics</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIOssjEventSpecifics(IOssjEventSpecifics object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IEvent Descriptor Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IEvent Descriptor Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIEventDescriptorEntry(IEventDescriptorEntry object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IOssj Query Specifics</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IOssj Query Specifics</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIOssjQuerySpecifics(IOssjQuerySpecifics object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IOssj Update Procedure Specifics</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IOssj Update Procedure Specifics</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIOssjUpdateProcedureSpecifics(IOssjUpdateProcedureSpecifics object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IManaged Entity Details</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IManaged Entity Details</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIManagedEntityDetails(IManagedEntityDetails object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IStandard Specifics</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IStandard Specifics</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIStandardSpecifics(IStandardSpecifics object) {
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

} //OssjSwitch
