/**
 * <copyright>
 * </copyright>
 *
 * $Id: OssjPackageImpl.java,v 1.1 2008/02/14 23:58:01 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.extensions.ossj.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.tigerstripe.metamodel.MetamodelPackage;

import org.eclipse.tigerstripe.metamodel.extensions.ExtensionsPackage;

import org.eclipse.tigerstripe.metamodel.extensions.impl.ExtensionsPackageImpl;

import org.eclipse.tigerstripe.metamodel.extensions.ossj.EEntityMethodFlavorFlag;
import org.eclipse.tigerstripe.metamodel.extensions.ossj.EMethodType;
import org.eclipse.tigerstripe.metamodel.extensions.ossj.IEventDescriptorEntry;
import org.eclipse.tigerstripe.metamodel.extensions.ossj.IManagedEntityDetails;
import org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjArtifactSpecifics;
import org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjDatatypeSpecifics;
import org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEntitySpecifics;
import org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEnumSpecifics;
import org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEventSpecifics;
import org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjQuerySpecifics;
import org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjUpdateProcedureSpecifics;
import org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjFactory;
import org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage;

import org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class OssjPackageImpl extends EPackageImpl implements OssjPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iOssjArtifactSpecificsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iOssjDatatypeSpecificsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iOssjEntitySpecificsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iOssjEnumSpecificsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iOssjEventSpecificsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iEventDescriptorEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iOssjQuerySpecificsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iOssjUpdateProcedureSpecificsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iManagedEntityDetailsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum eMethodTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum eEntityMethodFlavorFlagEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private OssjPackageImpl() {
		super(eNS_URI, OssjFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this
	 * model, and for any others upon which it depends.  Simple
	 * dependencies are satisfied by calling this method on all
	 * dependent packages before doing anything else.  This method drives
	 * initialization for interdependent packages directly, in parallel
	 * with this package, itself.
	 * <p>Of this package and its interdependencies, all packages which
	 * have not yet been registered by their URI values are first created
	 * and registered.  The packages are then initialized in two steps:
	 * meta-model objects for all of the packages are created before any
	 * are initialized, since one package's meta-model objects may refer to
	 * those of another.
	 * <p>Invocation of this method will not affect any packages that have
	 * already been initialized.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static OssjPackage init() {
		if (isInited) return (OssjPackage)EPackage.Registry.INSTANCE.getEPackage(OssjPackage.eNS_URI);

		// Obtain or create and register package
		OssjPackageImpl theOssjPackage = (OssjPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof OssjPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new OssjPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		MetamodelPackageImpl theMetamodelPackage = (MetamodelPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(MetamodelPackage.eNS_URI) instanceof MetamodelPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(MetamodelPackage.eNS_URI) : MetamodelPackage.eINSTANCE);
		ExtensionsPackageImpl theExtensionsPackage = (ExtensionsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(ExtensionsPackage.eNS_URI) instanceof ExtensionsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ExtensionsPackage.eNS_URI) : ExtensionsPackage.eINSTANCE);

		// Create package meta-data objects
		theOssjPackage.createPackageContents();
		theMetamodelPackage.createPackageContents();
		theExtensionsPackage.createPackageContents();

		// Initialize created meta-data
		theOssjPackage.initializePackageContents();
		theMetamodelPackage.initializePackageContents();
		theExtensionsPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theOssjPackage.freeze();

		return theOssjPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIOssjArtifactSpecifics() {
		return iOssjArtifactSpecificsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIOssjArtifactSpecifics_InterfaceProperties() {
		return (EReference)iOssjArtifactSpecificsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIOssjDatatypeSpecifics() {
		return iOssjDatatypeSpecificsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIOssjDatatypeSpecifics_SingleExtensionType() {
		return (EAttribute)iOssjDatatypeSpecificsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIOssjDatatypeSpecifics_SessionFactoryMethods() {
		return (EAttribute)iOssjDatatypeSpecificsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIOssjEntitySpecifics() {
		return iOssjEntitySpecificsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIOssjEntitySpecifics_FlavorDetails() {
		return (EReference)iOssjEntitySpecificsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIOssjEntitySpecifics_PrimaryKey() {
		return (EAttribute)iOssjEntitySpecificsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIOssjEntitySpecifics_ExtensibilityType() {
		return (EAttribute)iOssjEntitySpecificsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIOssjEntitySpecifics_SessionFactoryMethods() {
		return (EAttribute)iOssjEntitySpecificsEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIOssjEntitySpecifics_InterfaceKeyProperties() {
		return (EReference)iOssjEntitySpecificsEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIOssjEnumSpecifics() {
		return iOssjEnumSpecificsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIOssjEnumSpecifics_Extensible() {
		return (EAttribute)iOssjEnumSpecificsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIOssjEnumSpecifics_BaseIType() {
		return (EReference)iOssjEnumSpecificsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIOssjEventSpecifics() {
		return iOssjEventSpecificsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIOssjEventSpecifics_SingleExtensionType() {
		return (EAttribute)iOssjEventSpecificsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIOssjEventSpecifics_EventDescriptorEntries() {
		return (EReference)iOssjEventSpecificsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIOssjEventSpecifics_CustomEventDescriptorEntries() {
		return (EReference)iOssjEventSpecificsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIEventDescriptorEntry() {
		return iEventDescriptorEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIEventDescriptorEntry_Label() {
		return (EAttribute)iEventDescriptorEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIEventDescriptorEntry_PrimitiveType() {
		return (EAttribute)iEventDescriptorEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIOssjQuerySpecifics() {
		return iOssjQuerySpecificsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIOssjQuerySpecifics_ReturnedEntityIType() {
		return (EReference)iOssjQuerySpecificsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIOssjQuerySpecifics_SingleExtensionType() {
		return (EAttribute)iOssjQuerySpecificsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIOssjQuerySpecifics_SessionFactoryMethods() {
		return (EAttribute)iOssjQuerySpecificsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIOssjUpdateProcedureSpecifics() {
		return iOssjUpdateProcedureSpecificsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIOssjUpdateProcedureSpecifics_SingleExtensionType() {
		return (EAttribute)iOssjUpdateProcedureSpecificsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIOssjUpdateProcedureSpecifics_SessionFactoryMethods() {
		return (EAttribute)iOssjUpdateProcedureSpecificsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIManagedEntityDetails() {
		return iManagedEntityDetailsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIManagedEntityDetails_ManagedEntity() {
		return (EReference)iManagedEntityDetailsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIManagedEntityDetails_CrudFlavorDetails() {
		return (EReference)iManagedEntityDetailsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIManagedEntityDetails_CustomMethodFlavorDetails() {
		return (EReference)iManagedEntityDetailsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getEMethodType() {
		return eMethodTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getEEntityMethodFlavorFlag() {
		return eEntityMethodFlavorFlagEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OssjFactory getOssjFactory() {
		return (OssjFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
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
		iOssjArtifactSpecificsEClass = createEClass(IOSSJ_ARTIFACT_SPECIFICS);
		createEReference(iOssjArtifactSpecificsEClass, IOSSJ_ARTIFACT_SPECIFICS__INTERFACE_PROPERTIES);

		iOssjDatatypeSpecificsEClass = createEClass(IOSSJ_DATATYPE_SPECIFICS);
		createEAttribute(iOssjDatatypeSpecificsEClass, IOSSJ_DATATYPE_SPECIFICS__SINGLE_EXTENSION_TYPE);
		createEAttribute(iOssjDatatypeSpecificsEClass, IOSSJ_DATATYPE_SPECIFICS__SESSION_FACTORY_METHODS);

		iOssjEntitySpecificsEClass = createEClass(IOSSJ_ENTITY_SPECIFICS);
		createEReference(iOssjEntitySpecificsEClass, IOSSJ_ENTITY_SPECIFICS__FLAVOR_DETAILS);
		createEAttribute(iOssjEntitySpecificsEClass, IOSSJ_ENTITY_SPECIFICS__PRIMARY_KEY);
		createEAttribute(iOssjEntitySpecificsEClass, IOSSJ_ENTITY_SPECIFICS__EXTENSIBILITY_TYPE);
		createEAttribute(iOssjEntitySpecificsEClass, IOSSJ_ENTITY_SPECIFICS__SESSION_FACTORY_METHODS);
		createEReference(iOssjEntitySpecificsEClass, IOSSJ_ENTITY_SPECIFICS__INTERFACE_KEY_PROPERTIES);

		iOssjEnumSpecificsEClass = createEClass(IOSSJ_ENUM_SPECIFICS);
		createEAttribute(iOssjEnumSpecificsEClass, IOSSJ_ENUM_SPECIFICS__EXTENSIBLE);
		createEReference(iOssjEnumSpecificsEClass, IOSSJ_ENUM_SPECIFICS__BASE_ITYPE);

		iOssjEventSpecificsEClass = createEClass(IOSSJ_EVENT_SPECIFICS);
		createEAttribute(iOssjEventSpecificsEClass, IOSSJ_EVENT_SPECIFICS__SINGLE_EXTENSION_TYPE);
		createEReference(iOssjEventSpecificsEClass, IOSSJ_EVENT_SPECIFICS__EVENT_DESCRIPTOR_ENTRIES);
		createEReference(iOssjEventSpecificsEClass, IOSSJ_EVENT_SPECIFICS__CUSTOM_EVENT_DESCRIPTOR_ENTRIES);

		iEventDescriptorEntryEClass = createEClass(IEVENT_DESCRIPTOR_ENTRY);
		createEAttribute(iEventDescriptorEntryEClass, IEVENT_DESCRIPTOR_ENTRY__LABEL);
		createEAttribute(iEventDescriptorEntryEClass, IEVENT_DESCRIPTOR_ENTRY__PRIMITIVE_TYPE);

		iOssjQuerySpecificsEClass = createEClass(IOSSJ_QUERY_SPECIFICS);
		createEReference(iOssjQuerySpecificsEClass, IOSSJ_QUERY_SPECIFICS__RETURNED_ENTITY_ITYPE);
		createEAttribute(iOssjQuerySpecificsEClass, IOSSJ_QUERY_SPECIFICS__SINGLE_EXTENSION_TYPE);
		createEAttribute(iOssjQuerySpecificsEClass, IOSSJ_QUERY_SPECIFICS__SESSION_FACTORY_METHODS);

		iOssjUpdateProcedureSpecificsEClass = createEClass(IOSSJ_UPDATE_PROCEDURE_SPECIFICS);
		createEAttribute(iOssjUpdateProcedureSpecificsEClass, IOSSJ_UPDATE_PROCEDURE_SPECIFICS__SINGLE_EXTENSION_TYPE);
		createEAttribute(iOssjUpdateProcedureSpecificsEClass, IOSSJ_UPDATE_PROCEDURE_SPECIFICS__SESSION_FACTORY_METHODS);

		iManagedEntityDetailsEClass = createEClass(IMANAGED_ENTITY_DETAILS);
		createEReference(iManagedEntityDetailsEClass, IMANAGED_ENTITY_DETAILS__MANAGED_ENTITY);
		createEReference(iManagedEntityDetailsEClass, IMANAGED_ENTITY_DETAILS__CRUD_FLAVOR_DETAILS);
		createEReference(iManagedEntityDetailsEClass, IMANAGED_ENTITY_DETAILS__CUSTOM_METHOD_FLAVOR_DETAILS);

		// Create enums
		eMethodTypeEEnum = createEEnum(EMETHOD_TYPE);
		eEntityMethodFlavorFlagEEnum = createEEnum(EENTITY_METHOD_FLAVOR_FLAG);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		ExtensionsPackage theExtensionsPackage = (ExtensionsPackage)EPackage.Registry.INSTANCE.getEPackage(ExtensionsPackage.eNS_URI);
		MetamodelPackage theMetamodelPackage = (MetamodelPackage)EPackage.Registry.INSTANCE.getEPackage(MetamodelPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		iOssjArtifactSpecificsEClass.getESuperTypes().add(theExtensionsPackage.getIStandardSpecifics());
		iOssjDatatypeSpecificsEClass.getESuperTypes().add(this.getIOssjArtifactSpecifics());
		iOssjEntitySpecificsEClass.getESuperTypes().add(this.getIOssjArtifactSpecifics());
		iOssjEnumSpecificsEClass.getESuperTypes().add(this.getIOssjArtifactSpecifics());
		iOssjEventSpecificsEClass.getESuperTypes().add(this.getIOssjArtifactSpecifics());
		iOssjQuerySpecificsEClass.getESuperTypes().add(this.getIOssjArtifactSpecifics());
		iOssjUpdateProcedureSpecificsEClass.getESuperTypes().add(this.getIOssjArtifactSpecifics());

		// Initialize classes and features; add operations and parameters
		initEClass(iOssjArtifactSpecificsEClass, IOssjArtifactSpecifics.class, "IOssjArtifactSpecifics", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIOssjArtifactSpecifics_InterfaceProperties(), theExtensionsPackage.getIProperties(), null, "interfaceProperties", null, 0, 1, IOssjArtifactSpecifics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = addEOperation(iOssjArtifactSpecificsEClass, null, "mergeInterfaceProperties", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theExtensionsPackage.getIProperties(), "interfaceProperties", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(iOssjDatatypeSpecificsEClass, IOssjDatatypeSpecifics.class, "IOssjDatatypeSpecifics", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIOssjDatatypeSpecifics_SingleExtensionType(), ecorePackage.getEBoolean(), "singleExtensionType", null, 0, 1, IOssjDatatypeSpecifics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIOssjDatatypeSpecifics_SessionFactoryMethods(), ecorePackage.getEBoolean(), "sessionFactoryMethods", null, 0, 1, IOssjDatatypeSpecifics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iOssjEntitySpecificsEClass, IOssjEntitySpecifics.class, "IOssjEntitySpecifics", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIOssjEntitySpecifics_FlavorDetails(), theMetamodelPackage.getIEntityMethodFlavorDetails(), null, "flavorDetails", null, 0, -1, IOssjEntitySpecifics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIOssjEntitySpecifics_PrimaryKey(), ecorePackage.getEString(), "primaryKey", "String", 0, 1, IOssjEntitySpecifics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIOssjEntitySpecifics_ExtensibilityType(), ecorePackage.getEString(), "extensibilityType", null, 0, 1, IOssjEntitySpecifics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIOssjEntitySpecifics_SessionFactoryMethods(), ecorePackage.getEBoolean(), "sessionFactoryMethods", null, 0, 1, IOssjEntitySpecifics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIOssjEntitySpecifics_InterfaceKeyProperties(), theExtensionsPackage.getIProperties(), null, "interfaceKeyProperties", null, 0, 1, IOssjEntitySpecifics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = addEOperation(iOssjEntitySpecificsEClass, theMetamodelPackage.getIEntityMethodFlavorDetails(), "getCRUDFlavorDetails", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEInt(), "crudID", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theMetamodelPackage.getOssjEntityMethodFlavor(), "flavor", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(iOssjEntitySpecificsEClass, null, "setCRUDFlavorDetails", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEInt(), "crudID", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theMetamodelPackage.getOssjEntityMethodFlavor(), "flavor", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theMetamodelPackage.getIEntityMethodFlavorDetails(), "details", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(iOssjEntitySpecificsEClass, theMetamodelPackage.getOssjEntityMethodFlavor(), "getSupportedFlavors", 0, -1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEInt(), "crudID", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(iOssjEnumSpecificsEClass, IOssjEnumSpecifics.class, "IOssjEnumSpecifics", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIOssjEnumSpecifics_Extensible(), ecorePackage.getEBoolean(), "extensible", null, 0, 1, IOssjEnumSpecifics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIOssjEnumSpecifics_BaseIType(), theMetamodelPackage.getIType(), null, "baseIType", null, 0, 1, IOssjEnumSpecifics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iOssjEventSpecificsEClass, IOssjEventSpecifics.class, "IOssjEventSpecifics", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIOssjEventSpecifics_SingleExtensionType(), ecorePackage.getEBoolean(), "singleExtensionType", null, 0, 1, IOssjEventSpecifics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIOssjEventSpecifics_EventDescriptorEntries(), this.getIEventDescriptorEntry(), null, "eventDescriptorEntries", null, 0, -1, IOssjEventSpecifics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIOssjEventSpecifics_CustomEventDescriptorEntries(), this.getIEventDescriptorEntry(), null, "customEventDescriptorEntries", null, 0, -1, IOssjEventSpecifics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iEventDescriptorEntryEClass, IEventDescriptorEntry.class, "IEventDescriptorEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIEventDescriptorEntry_Label(), ecorePackage.getEString(), "label", null, 0, 1, IEventDescriptorEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIEventDescriptorEntry_PrimitiveType(), ecorePackage.getEString(), "primitiveType", null, 0, 1, IEventDescriptorEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iOssjQuerySpecificsEClass, IOssjQuerySpecifics.class, "IOssjQuerySpecifics", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIOssjQuerySpecifics_ReturnedEntityIType(), theMetamodelPackage.getIType(), null, "returnedEntityIType", null, 0, 1, IOssjQuerySpecifics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIOssjQuerySpecifics_SingleExtensionType(), ecorePackage.getEBoolean(), "singleExtensionType", null, 0, 1, IOssjQuerySpecifics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIOssjQuerySpecifics_SessionFactoryMethods(), ecorePackage.getEBoolean(), "sessionFactoryMethods", null, 0, 1, IOssjQuerySpecifics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iOssjUpdateProcedureSpecificsEClass, IOssjUpdateProcedureSpecifics.class, "IOssjUpdateProcedureSpecifics", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIOssjUpdateProcedureSpecifics_SingleExtensionType(), ecorePackage.getEBoolean(), "singleExtensionType", null, 0, 1, IOssjUpdateProcedureSpecifics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIOssjUpdateProcedureSpecifics_SessionFactoryMethods(), ecorePackage.getEBoolean(), "sessionFactoryMethods", null, 0, 1, IOssjUpdateProcedureSpecifics.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iManagedEntityDetailsEClass, IManagedEntityDetails.class, "IManagedEntityDetails", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIManagedEntityDetails_ManagedEntity(), theMetamodelPackage.getIManagedEntityArtifact(), null, "managedEntity", null, 0, 1, IManagedEntityDetails.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIManagedEntityDetails_CrudFlavorDetails(), theMetamodelPackage.getIEntityMethodFlavorDetails(), null, "crudFlavorDetails", null, 0, -1, IManagedEntityDetails.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIManagedEntityDetails_CustomMethodFlavorDetails(), theMetamodelPackage.getIEntityMethodFlavorDetails(), null, "customMethodFlavorDetails", null, 0, -1, IManagedEntityDetails.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = addEOperation(iManagedEntityDetailsEClass, theMetamodelPackage.getIEntityMethodFlavorDetails(), "getCRUDFlavorDetails", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getEMethodType(), "methodType", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theMetamodelPackage.getOssjEntityMethodFlavor(), "flavor", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(iManagedEntityDetailsEClass, theMetamodelPackage.getIEntityMethodFlavorDetails(), "getCustomMethodFlavorDetails", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theMetamodelPackage.getOssjEntityMethodFlavor(), "flavor", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "methodID", 0, 1, IS_UNIQUE, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(eMethodTypeEEnum, EMethodType.class, "EMethodType");
		addEEnumLiteral(eMethodTypeEEnum, EMethodType.CRUD_CREATE);
		addEEnumLiteral(eMethodTypeEEnum, EMethodType.CRUD_GET);
		addEEnumLiteral(eMethodTypeEEnum, EMethodType.CRUD_SET);
		addEEnumLiteral(eMethodTypeEEnum, EMethodType.CRUD_REMOVE);
		addEEnumLiteral(eMethodTypeEEnum, EMethodType.CUSTOM);

		initEEnum(eEntityMethodFlavorFlagEEnum, EEntityMethodFlavorFlag.class, "EEntityMethodFlavorFlag");
		addEEnumLiteral(eEntityMethodFlavorFlagEEnum, EEntityMethodFlavorFlag.TRUE);
		addEEnumLiteral(eEntityMethodFlavorFlagEEnum, EEntityMethodFlavorFlag.FALSE);
		addEEnumLiteral(eEntityMethodFlavorFlagEEnum, EEntityMethodFlavorFlag.OPTIONAL);
	}

} //OssjPackageImpl
