/**
 * <copyright>
 * </copyright>
 *
 * $Id: OssjFactoryImpl.java,v 1.1 2008/02/14 23:58:01 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.extensions.ossj.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.tigerstripe.metamodel.extensions.ossj.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class OssjFactoryImpl extends EFactoryImpl implements OssjFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static OssjFactory init() {
		try {
			OssjFactory theOssjFactory = (OssjFactory)EPackage.Registry.INSTANCE.getEFactory("tigerstripe-ossj"); 
			if (theOssjFactory != null) {
				return theOssjFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new OssjFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OssjFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case OssjPackage.IOSSJ_ARTIFACT_SPECIFICS: return createIOssjArtifactSpecifics();
			case OssjPackage.IOSSJ_DATATYPE_SPECIFICS: return createIOssjDatatypeSpecifics();
			case OssjPackage.IOSSJ_ENTITY_SPECIFICS: return createIOssjEntitySpecifics();
			case OssjPackage.IOSSJ_ENUM_SPECIFICS: return createIOssjEnumSpecifics();
			case OssjPackage.IOSSJ_EVENT_SPECIFICS: return createIOssjEventSpecifics();
			case OssjPackage.IEVENT_DESCRIPTOR_ENTRY: return createIEventDescriptorEntry();
			case OssjPackage.IOSSJ_QUERY_SPECIFICS: return createIOssjQuerySpecifics();
			case OssjPackage.IOSSJ_UPDATE_PROCEDURE_SPECIFICS: return createIOssjUpdateProcedureSpecifics();
			case OssjPackage.IMANAGED_ENTITY_DETAILS: return createIManagedEntityDetails();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case OssjPackage.EMETHOD_TYPE:
				return createEMethodTypeFromString(eDataType, initialValue);
			case OssjPackage.EENTITY_METHOD_FLAVOR_FLAG:
				return createEEntityMethodFlavorFlagFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case OssjPackage.EMETHOD_TYPE:
				return convertEMethodTypeToString(eDataType, instanceValue);
			case OssjPackage.EENTITY_METHOD_FLAVOR_FLAG:
				return convertEEntityMethodFlavorFlagToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IOssjArtifactSpecifics createIOssjArtifactSpecifics() {
		IOssjArtifactSpecificsImpl iOssjArtifactSpecifics = new IOssjArtifactSpecificsImpl();
		return iOssjArtifactSpecifics;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IOssjDatatypeSpecifics createIOssjDatatypeSpecifics() {
		IOssjDatatypeSpecificsImpl iOssjDatatypeSpecifics = new IOssjDatatypeSpecificsImpl();
		return iOssjDatatypeSpecifics;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IOssjEntitySpecifics createIOssjEntitySpecifics() {
		IOssjEntitySpecificsImpl iOssjEntitySpecifics = new IOssjEntitySpecificsImpl();
		return iOssjEntitySpecifics;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IOssjEnumSpecifics createIOssjEnumSpecifics() {
		IOssjEnumSpecificsImpl iOssjEnumSpecifics = new IOssjEnumSpecificsImpl();
		return iOssjEnumSpecifics;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IOssjEventSpecifics createIOssjEventSpecifics() {
		IOssjEventSpecificsImpl iOssjEventSpecifics = new IOssjEventSpecificsImpl();
		return iOssjEventSpecifics;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IEventDescriptorEntry createIEventDescriptorEntry() {
		IEventDescriptorEntryImpl iEventDescriptorEntry = new IEventDescriptorEntryImpl();
		return iEventDescriptorEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IOssjQuerySpecifics createIOssjQuerySpecifics() {
		IOssjQuerySpecificsImpl iOssjQuerySpecifics = new IOssjQuerySpecificsImpl();
		return iOssjQuerySpecifics;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IOssjUpdateProcedureSpecifics createIOssjUpdateProcedureSpecifics() {
		IOssjUpdateProcedureSpecificsImpl iOssjUpdateProcedureSpecifics = new IOssjUpdateProcedureSpecificsImpl();
		return iOssjUpdateProcedureSpecifics;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IManagedEntityDetails createIManagedEntityDetails() {
		IManagedEntityDetailsImpl iManagedEntityDetails = new IManagedEntityDetailsImpl();
		return iManagedEntityDetails;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMethodType createEMethodTypeFromString(EDataType eDataType, String initialValue) {
		EMethodType result = EMethodType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertEMethodTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEntityMethodFlavorFlag createEEntityMethodFlavorFlagFromString(EDataType eDataType, String initialValue) {
		EEntityMethodFlavorFlag result = EEntityMethodFlavorFlag.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertEEntityMethodFlavorFlagToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OssjPackage getOssjPackage() {
		return (OssjPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static OssjPackage getPackage() {
		return OssjPackage.eINSTANCE;
	}

} //OssjFactoryImpl
