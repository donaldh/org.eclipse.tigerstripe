/**
 * <copyright>
 * </copyright>
 *
 * $Id: MetamodelFactoryImpl.java,v 1.2 2008/02/28 18:05:31 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.tigerstripe.metamodel.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MetamodelFactoryImpl extends EFactoryImpl implements MetamodelFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static MetamodelFactory init() {
		try {
			MetamodelFactory theMetamodelFactory = (MetamodelFactory)EPackage.Registry.INSTANCE.getEFactory("tigerstripe"); 
			if (theMetamodelFactory != null) {
				return theMetamodelFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new MetamodelFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MetamodelFactoryImpl() {
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
			case MetamodelPackage.IPRIMITIVE_TYPE: return createIPrimitiveType();
			case MetamodelPackage.IMANAGED_ENTITY_ARTIFACT: return createIManagedEntityArtifact();
			case MetamodelPackage.IDATATYPE_ARTIFACT: return createIDatatypeArtifact();
			case MetamodelPackage.IEXCEPTION_ARTIFACT: return createIExceptionArtifact();
			case MetamodelPackage.ISESSION_ARTIFACT: return createISessionArtifact();
			case MetamodelPackage.IQUERY_ARTIFACT: return createIQueryArtifact();
			case MetamodelPackage.IUPDATE_PROCEDURE_ARTIFACT: return createIUpdateProcedureArtifact();
			case MetamodelPackage.IEVENT_ARTIFACT: return createIEventArtifact();
			case MetamodelPackage.IASSOCIATION_ARTIFACT: return createIAssociationArtifact();
			case MetamodelPackage.IASSOCIATION_CLASS_ARTIFACT: return createIAssociationClassArtifact();
			case MetamodelPackage.IDEPENDENCY_ARTIFACT: return createIDependencyArtifact();
			case MetamodelPackage.IENUM_ARTIFACT: return createIEnumArtifact();
			case MetamodelPackage.IFIELD: return createIField();
			case MetamodelPackage.IMETHOD: return createIMethod();
			case MetamodelPackage.ILITERAL: return createILiteral();
			case MetamodelPackage.ITYPE: return createIType();
			case MetamodelPackage.IASSOCIATION_END: return createIAssociationEnd();
			case MetamodelPackage.IARGUMENT: return createIArgument();
			case MetamodelPackage.IMODEL: return createIModel();
			case MetamodelPackage.IPACKAGE: return createIPackage();
			case MetamodelPackage.ISTEREOTYPE_CAPABLE: return createIStereotypeCapable();
			case MetamodelPackage.ISTEREOTYPE_INSTANCE: return createIStereotypeInstance();
			case MetamodelPackage.ISTEREOTYPE_ATTRIBUTE_VALUE: return createIStereotypeAttributeValue();
			case MetamodelPackage.IENTITY_METHOD_FLAVOR_DETAILS: return createIEntityMethodFlavorDetails();
			case MetamodelPackage.IMULTIPLICITY: return createIMultiplicity();
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
			case MetamodelPackage.EREF_BY_ENUM:
				return createERefByEnumFromString(eDataType, initialValue);
			case MetamodelPackage.VISIBILITY_ENUM:
				return createVisibilityEnumFromString(eDataType, initialValue);
			case MetamodelPackage.EAGGREGATION_ENUM:
				return createEAggregationEnumFromString(eDataType, initialValue);
			case MetamodelPackage.ECHANGEABLE_ENUM:
				return createEChangeableEnumFromString(eDataType, initialValue);
			case MetamodelPackage.OSSJ_ENTITY_METHOD_FLAVOR:
				return createOssjEntityMethodFlavorFromString(eDataType, initialValue);
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
			case MetamodelPackage.EREF_BY_ENUM:
				return convertERefByEnumToString(eDataType, instanceValue);
			case MetamodelPackage.VISIBILITY_ENUM:
				return convertVisibilityEnumToString(eDataType, instanceValue);
			case MetamodelPackage.EAGGREGATION_ENUM:
				return convertEAggregationEnumToString(eDataType, instanceValue);
			case MetamodelPackage.ECHANGEABLE_ENUM:
				return convertEChangeableEnumToString(eDataType, instanceValue);
			case MetamodelPackage.OSSJ_ENTITY_METHOD_FLAVOR:
				return convertOssjEntityMethodFlavorToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IPrimitiveType createIPrimitiveType() {
		IPrimitiveTypeImpl iPrimitiveType = new IPrimitiveTypeImpl();
		return iPrimitiveType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IManagedEntityArtifact createIManagedEntityArtifact() {
		IManagedEntityArtifactImpl iManagedEntityArtifact = new IManagedEntityArtifactImpl();
		return iManagedEntityArtifact;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IDatatypeArtifact createIDatatypeArtifact() {
		IDatatypeArtifactImpl iDatatypeArtifact = new IDatatypeArtifactImpl();
		return iDatatypeArtifact;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IExceptionArtifact createIExceptionArtifact() {
		IExceptionArtifactImpl iExceptionArtifact = new IExceptionArtifactImpl();
		return iExceptionArtifact;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ISessionArtifact createISessionArtifact() {
		ISessionArtifactImpl iSessionArtifact = new ISessionArtifactImpl();
		return iSessionArtifact;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IQueryArtifact createIQueryArtifact() {
		IQueryArtifactImpl iQueryArtifact = new IQueryArtifactImpl();
		return iQueryArtifact;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IUpdateProcedureArtifact createIUpdateProcedureArtifact() {
		IUpdateProcedureArtifactImpl iUpdateProcedureArtifact = new IUpdateProcedureArtifactImpl();
		return iUpdateProcedureArtifact;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IEventArtifact createIEventArtifact() {
		IEventArtifactImpl iEventArtifact = new IEventArtifactImpl();
		return iEventArtifact;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IAssociationArtifact createIAssociationArtifact() {
		IAssociationArtifactImpl iAssociationArtifact = new IAssociationArtifactImpl();
		return iAssociationArtifact;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IAssociationClassArtifact createIAssociationClassArtifact() {
		IAssociationClassArtifactImpl iAssociationClassArtifact = new IAssociationClassArtifactImpl();
		return iAssociationClassArtifact;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IDependencyArtifact createIDependencyArtifact() {
		IDependencyArtifactImpl iDependencyArtifact = new IDependencyArtifactImpl();
		return iDependencyArtifact;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IEnumArtifact createIEnumArtifact() {
		IEnumArtifactImpl iEnumArtifact = new IEnumArtifactImpl();
		return iEnumArtifact;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IField createIField() {
		IFieldImpl iField = new IFieldImpl();
		return iField;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IMethod createIMethod() {
		IMethodImpl iMethod = new IMethodImpl();
		return iMethod;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ILiteral createILiteral() {
		ILiteralImpl iLiteral = new ILiteralImpl();
		return iLiteral;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IType createIType() {
		ITypeImpl iType = new ITypeImpl();
		return iType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IAssociationEnd createIAssociationEnd() {
		IAssociationEndImpl iAssociationEnd = new IAssociationEndImpl();
		return iAssociationEnd;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IArgument createIArgument() {
		IArgumentImpl iArgument = new IArgumentImpl();
		return iArgument;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IModel createIModel() {
		IModelImpl iModel = new IModelImpl();
		return iModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IPackage createIPackage() {
		IPackageImpl iPackage = new IPackageImpl();
		return iPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IStereotypeCapable createIStereotypeCapable() {
		IStereotypeCapableImpl iStereotypeCapable = new IStereotypeCapableImpl();
		return iStereotypeCapable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IStereotypeInstance createIStereotypeInstance() {
		IStereotypeInstanceImpl iStereotypeInstance = new IStereotypeInstanceImpl();
		return iStereotypeInstance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IStereotypeAttributeValue createIStereotypeAttributeValue() {
		IStereotypeAttributeValueImpl iStereotypeAttributeValue = new IStereotypeAttributeValueImpl();
		return iStereotypeAttributeValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IEntityMethodFlavorDetails createIEntityMethodFlavorDetails() {
		IEntityMethodFlavorDetailsImpl iEntityMethodFlavorDetails = new IEntityMethodFlavorDetailsImpl();
		return iEntityMethodFlavorDetails;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IMultiplicity createIMultiplicity() {
		IMultiplicityImpl iMultiplicity = new IMultiplicityImpl();
		return iMultiplicity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ERefByEnum createERefByEnumFromString(EDataType eDataType, String initialValue) {
		ERefByEnum result = ERefByEnum.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertERefByEnumToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VisibilityEnum createVisibilityEnumFromString(EDataType eDataType, String initialValue) {
		VisibilityEnum result = VisibilityEnum.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertVisibilityEnumToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAggregationEnum createEAggregationEnumFromString(EDataType eDataType, String initialValue) {
		EAggregationEnum result = EAggregationEnum.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertEAggregationEnumToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EChangeableEnum createEChangeableEnumFromString(EDataType eDataType, String initialValue) {
		EChangeableEnum result = EChangeableEnum.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertEChangeableEnumToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OssjEntityMethodFlavor createOssjEntityMethodFlavorFromString(EDataType eDataType, String initialValue) {
		OssjEntityMethodFlavor result = OssjEntityMethodFlavor.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertOssjEntityMethodFlavorToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MetamodelPackage getMetamodelPackage() {
		return (MetamodelPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static MetamodelPackage getPackage() {
		return MetamodelPackage.eINSTANCE;
	}

} //MetamodelFactoryImpl
