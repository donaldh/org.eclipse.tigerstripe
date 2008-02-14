/**
 * <copyright>
 * </copyright>
 *
 * $Id: OssjPackage.java,v 1.1 2008/02/14 23:58:00 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.extensions.ossj;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.tigerstripe.metamodel.extensions.ExtensionsPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjFactory
 * @model kind="package"
 * @generated
 */
public interface OssjPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "ossj";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "tigerstripe-ossj";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "ts-ossj";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	OssjPackage eINSTANCE = org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.OssjPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjArtifactSpecificsImpl <em>IOssj Artifact Specifics</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjArtifactSpecificsImpl
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.OssjPackageImpl#getIOssjArtifactSpecifics()
	 * @generated
	 */
	int IOSSJ_ARTIFACT_SPECIFICS = 0;

	/**
	 * The feature id for the '<em><b>Interface Properties</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_ARTIFACT_SPECIFICS__INTERFACE_PROPERTIES = ExtensionsPackage.ISTANDARD_SPECIFICS_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>IOssj Artifact Specifics</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_ARTIFACT_SPECIFICS_FEATURE_COUNT = ExtensionsPackage.ISTANDARD_SPECIFICS_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjDatatypeSpecificsImpl <em>IOssj Datatype Specifics</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjDatatypeSpecificsImpl
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.OssjPackageImpl#getIOssjDatatypeSpecifics()
	 * @generated
	 */
	int IOSSJ_DATATYPE_SPECIFICS = 1;

	/**
	 * The feature id for the '<em><b>Interface Properties</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_DATATYPE_SPECIFICS__INTERFACE_PROPERTIES = IOSSJ_ARTIFACT_SPECIFICS__INTERFACE_PROPERTIES;

	/**
	 * The feature id for the '<em><b>Single Extension Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_DATATYPE_SPECIFICS__SINGLE_EXTENSION_TYPE = IOSSJ_ARTIFACT_SPECIFICS_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Session Factory Methods</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_DATATYPE_SPECIFICS__SESSION_FACTORY_METHODS = IOSSJ_ARTIFACT_SPECIFICS_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>IOssj Datatype Specifics</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_DATATYPE_SPECIFICS_FEATURE_COUNT = IOSSJ_ARTIFACT_SPECIFICS_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjEntitySpecificsImpl <em>IOssj Entity Specifics</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjEntitySpecificsImpl
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.OssjPackageImpl#getIOssjEntitySpecifics()
	 * @generated
	 */
	int IOSSJ_ENTITY_SPECIFICS = 2;

	/**
	 * The feature id for the '<em><b>Interface Properties</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_ENTITY_SPECIFICS__INTERFACE_PROPERTIES = IOSSJ_ARTIFACT_SPECIFICS__INTERFACE_PROPERTIES;

	/**
	 * The feature id for the '<em><b>Flavor Details</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_ENTITY_SPECIFICS__FLAVOR_DETAILS = IOSSJ_ARTIFACT_SPECIFICS_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Primary Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_ENTITY_SPECIFICS__PRIMARY_KEY = IOSSJ_ARTIFACT_SPECIFICS_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Extensibility Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_ENTITY_SPECIFICS__EXTENSIBILITY_TYPE = IOSSJ_ARTIFACT_SPECIFICS_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Session Factory Methods</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_ENTITY_SPECIFICS__SESSION_FACTORY_METHODS = IOSSJ_ARTIFACT_SPECIFICS_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Interface Key Properties</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_ENTITY_SPECIFICS__INTERFACE_KEY_PROPERTIES = IOSSJ_ARTIFACT_SPECIFICS_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>IOssj Entity Specifics</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_ENTITY_SPECIFICS_FEATURE_COUNT = IOSSJ_ARTIFACT_SPECIFICS_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjEnumSpecificsImpl <em>IOssj Enum Specifics</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjEnumSpecificsImpl
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.OssjPackageImpl#getIOssjEnumSpecifics()
	 * @generated
	 */
	int IOSSJ_ENUM_SPECIFICS = 3;

	/**
	 * The feature id for the '<em><b>Interface Properties</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_ENUM_SPECIFICS__INTERFACE_PROPERTIES = IOSSJ_ARTIFACT_SPECIFICS__INTERFACE_PROPERTIES;

	/**
	 * The feature id for the '<em><b>Extensible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_ENUM_SPECIFICS__EXTENSIBLE = IOSSJ_ARTIFACT_SPECIFICS_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Base IType</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_ENUM_SPECIFICS__BASE_ITYPE = IOSSJ_ARTIFACT_SPECIFICS_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>IOssj Enum Specifics</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_ENUM_SPECIFICS_FEATURE_COUNT = IOSSJ_ARTIFACT_SPECIFICS_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjEventSpecificsImpl <em>IOssj Event Specifics</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjEventSpecificsImpl
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.OssjPackageImpl#getIOssjEventSpecifics()
	 * @generated
	 */
	int IOSSJ_EVENT_SPECIFICS = 4;

	/**
	 * The feature id for the '<em><b>Interface Properties</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_EVENT_SPECIFICS__INTERFACE_PROPERTIES = IOSSJ_ARTIFACT_SPECIFICS__INTERFACE_PROPERTIES;

	/**
	 * The feature id for the '<em><b>Single Extension Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_EVENT_SPECIFICS__SINGLE_EXTENSION_TYPE = IOSSJ_ARTIFACT_SPECIFICS_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Event Descriptor Entries</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_EVENT_SPECIFICS__EVENT_DESCRIPTOR_ENTRIES = IOSSJ_ARTIFACT_SPECIFICS_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Custom Event Descriptor Entries</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_EVENT_SPECIFICS__CUSTOM_EVENT_DESCRIPTOR_ENTRIES = IOSSJ_ARTIFACT_SPECIFICS_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>IOssj Event Specifics</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_EVENT_SPECIFICS_FEATURE_COUNT = IOSSJ_ARTIFACT_SPECIFICS_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IEventDescriptorEntryImpl <em>IEvent Descriptor Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IEventDescriptorEntryImpl
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.OssjPackageImpl#getIEventDescriptorEntry()
	 * @generated
	 */
	int IEVENT_DESCRIPTOR_ENTRY = 5;

	/**
	 * The feature id for the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEVENT_DESCRIPTOR_ENTRY__LABEL = 0;

	/**
	 * The feature id for the '<em><b>Primitive Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEVENT_DESCRIPTOR_ENTRY__PRIMITIVE_TYPE = 1;

	/**
	 * The number of structural features of the '<em>IEvent Descriptor Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEVENT_DESCRIPTOR_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjQuerySpecificsImpl <em>IOssj Query Specifics</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjQuerySpecificsImpl
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.OssjPackageImpl#getIOssjQuerySpecifics()
	 * @generated
	 */
	int IOSSJ_QUERY_SPECIFICS = 6;

	/**
	 * The feature id for the '<em><b>Interface Properties</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_QUERY_SPECIFICS__INTERFACE_PROPERTIES = IOSSJ_ARTIFACT_SPECIFICS__INTERFACE_PROPERTIES;

	/**
	 * The feature id for the '<em><b>Returned Entity IType</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_QUERY_SPECIFICS__RETURNED_ENTITY_ITYPE = IOSSJ_ARTIFACT_SPECIFICS_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Single Extension Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_QUERY_SPECIFICS__SINGLE_EXTENSION_TYPE = IOSSJ_ARTIFACT_SPECIFICS_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Session Factory Methods</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_QUERY_SPECIFICS__SESSION_FACTORY_METHODS = IOSSJ_ARTIFACT_SPECIFICS_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>IOssj Query Specifics</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_QUERY_SPECIFICS_FEATURE_COUNT = IOSSJ_ARTIFACT_SPECIFICS_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjUpdateProcedureSpecificsImpl <em>IOssj Update Procedure Specifics</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjUpdateProcedureSpecificsImpl
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.OssjPackageImpl#getIOssjUpdateProcedureSpecifics()
	 * @generated
	 */
	int IOSSJ_UPDATE_PROCEDURE_SPECIFICS = 7;

	/**
	 * The feature id for the '<em><b>Interface Properties</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_UPDATE_PROCEDURE_SPECIFICS__INTERFACE_PROPERTIES = IOSSJ_ARTIFACT_SPECIFICS__INTERFACE_PROPERTIES;

	/**
	 * The feature id for the '<em><b>Single Extension Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_UPDATE_PROCEDURE_SPECIFICS__SINGLE_EXTENSION_TYPE = IOSSJ_ARTIFACT_SPECIFICS_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Session Factory Methods</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_UPDATE_PROCEDURE_SPECIFICS__SESSION_FACTORY_METHODS = IOSSJ_ARTIFACT_SPECIFICS_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>IOssj Update Procedure Specifics</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IOSSJ_UPDATE_PROCEDURE_SPECIFICS_FEATURE_COUNT = IOSSJ_ARTIFACT_SPECIFICS_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IManagedEntityDetailsImpl <em>IManaged Entity Details</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IManagedEntityDetailsImpl
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.OssjPackageImpl#getIManagedEntityDetails()
	 * @generated
	 */
	int IMANAGED_ENTITY_DETAILS = 8;

	/**
	 * The feature id for the '<em><b>Managed Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMANAGED_ENTITY_DETAILS__MANAGED_ENTITY = 0;

	/**
	 * The feature id for the '<em><b>Crud Flavor Details</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMANAGED_ENTITY_DETAILS__CRUD_FLAVOR_DETAILS = 1;

	/**
	 * The feature id for the '<em><b>Custom Method Flavor Details</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMANAGED_ENTITY_DETAILS__CUSTOM_METHOD_FLAVOR_DETAILS = 2;

	/**
	 * The number of structural features of the '<em>IManaged Entity Details</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMANAGED_ENTITY_DETAILS_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.EMethodType <em>EMethod Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.EMethodType
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.OssjPackageImpl#getEMethodType()
	 * @generated
	 */
	int EMETHOD_TYPE = 9;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.EEntityMethodFlavorFlag <em>EEntity Method Flavor Flag</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.EEntityMethodFlavorFlag
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.OssjPackageImpl#getEEntityMethodFlavorFlag()
	 * @generated
	 */
	int EENTITY_METHOD_FLAVOR_FLAG = 10;


	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjArtifactSpecifics <em>IOssj Artifact Specifics</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IOssj Artifact Specifics</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjArtifactSpecifics
	 * @generated
	 */
	EClass getIOssjArtifactSpecifics();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjArtifactSpecifics#getInterfaceProperties <em>Interface Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Interface Properties</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjArtifactSpecifics#getInterfaceProperties()
	 * @see #getIOssjArtifactSpecifics()
	 * @generated
	 */
	EReference getIOssjArtifactSpecifics_InterfaceProperties();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjDatatypeSpecifics <em>IOssj Datatype Specifics</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IOssj Datatype Specifics</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjDatatypeSpecifics
	 * @generated
	 */
	EClass getIOssjDatatypeSpecifics();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjDatatypeSpecifics#isSingleExtensionType <em>Single Extension Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Single Extension Type</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjDatatypeSpecifics#isSingleExtensionType()
	 * @see #getIOssjDatatypeSpecifics()
	 * @generated
	 */
	EAttribute getIOssjDatatypeSpecifics_SingleExtensionType();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjDatatypeSpecifics#isSessionFactoryMethods <em>Session Factory Methods</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Session Factory Methods</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjDatatypeSpecifics#isSessionFactoryMethods()
	 * @see #getIOssjDatatypeSpecifics()
	 * @generated
	 */
	EAttribute getIOssjDatatypeSpecifics_SessionFactoryMethods();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEntitySpecifics <em>IOssj Entity Specifics</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IOssj Entity Specifics</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEntitySpecifics
	 * @generated
	 */
	EClass getIOssjEntitySpecifics();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEntitySpecifics#getFlavorDetails <em>Flavor Details</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Flavor Details</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEntitySpecifics#getFlavorDetails()
	 * @see #getIOssjEntitySpecifics()
	 * @generated
	 */
	EReference getIOssjEntitySpecifics_FlavorDetails();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEntitySpecifics#getPrimaryKey <em>Primary Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Primary Key</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEntitySpecifics#getPrimaryKey()
	 * @see #getIOssjEntitySpecifics()
	 * @generated
	 */
	EAttribute getIOssjEntitySpecifics_PrimaryKey();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEntitySpecifics#getExtensibilityType <em>Extensibility Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Extensibility Type</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEntitySpecifics#getExtensibilityType()
	 * @see #getIOssjEntitySpecifics()
	 * @generated
	 */
	EAttribute getIOssjEntitySpecifics_ExtensibilityType();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEntitySpecifics#isSessionFactoryMethods <em>Session Factory Methods</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Session Factory Methods</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEntitySpecifics#isSessionFactoryMethods()
	 * @see #getIOssjEntitySpecifics()
	 * @generated
	 */
	EAttribute getIOssjEntitySpecifics_SessionFactoryMethods();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEntitySpecifics#getInterfaceKeyProperties <em>Interface Key Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Interface Key Properties</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEntitySpecifics#getInterfaceKeyProperties()
	 * @see #getIOssjEntitySpecifics()
	 * @generated
	 */
	EReference getIOssjEntitySpecifics_InterfaceKeyProperties();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEnumSpecifics <em>IOssj Enum Specifics</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IOssj Enum Specifics</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEnumSpecifics
	 * @generated
	 */
	EClass getIOssjEnumSpecifics();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEnumSpecifics#isExtensible <em>Extensible</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Extensible</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEnumSpecifics#isExtensible()
	 * @see #getIOssjEnumSpecifics()
	 * @generated
	 */
	EAttribute getIOssjEnumSpecifics_Extensible();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEnumSpecifics#getBaseIType <em>Base IType</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Base IType</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEnumSpecifics#getBaseIType()
	 * @see #getIOssjEnumSpecifics()
	 * @generated
	 */
	EReference getIOssjEnumSpecifics_BaseIType();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEventSpecifics <em>IOssj Event Specifics</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IOssj Event Specifics</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEventSpecifics
	 * @generated
	 */
	EClass getIOssjEventSpecifics();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEventSpecifics#isSingleExtensionType <em>Single Extension Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Single Extension Type</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEventSpecifics#isSingleExtensionType()
	 * @see #getIOssjEventSpecifics()
	 * @generated
	 */
	EAttribute getIOssjEventSpecifics_SingleExtensionType();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEventSpecifics#getEventDescriptorEntries <em>Event Descriptor Entries</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Event Descriptor Entries</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEventSpecifics#getEventDescriptorEntries()
	 * @see #getIOssjEventSpecifics()
	 * @generated
	 */
	EReference getIOssjEventSpecifics_EventDescriptorEntries();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEventSpecifics#getCustomEventDescriptorEntries <em>Custom Event Descriptor Entries</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Custom Event Descriptor Entries</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEventSpecifics#getCustomEventDescriptorEntries()
	 * @see #getIOssjEventSpecifics()
	 * @generated
	 */
	EReference getIOssjEventSpecifics_CustomEventDescriptorEntries();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IEventDescriptorEntry <em>IEvent Descriptor Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IEvent Descriptor Entry</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IEventDescriptorEntry
	 * @generated
	 */
	EClass getIEventDescriptorEntry();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IEventDescriptorEntry#getLabel <em>Label</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Label</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IEventDescriptorEntry#getLabel()
	 * @see #getIEventDescriptorEntry()
	 * @generated
	 */
	EAttribute getIEventDescriptorEntry_Label();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IEventDescriptorEntry#getPrimitiveType <em>Primitive Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Primitive Type</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IEventDescriptorEntry#getPrimitiveType()
	 * @see #getIEventDescriptorEntry()
	 * @generated
	 */
	EAttribute getIEventDescriptorEntry_PrimitiveType();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjQuerySpecifics <em>IOssj Query Specifics</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IOssj Query Specifics</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjQuerySpecifics
	 * @generated
	 */
	EClass getIOssjQuerySpecifics();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjQuerySpecifics#getReturnedEntityIType <em>Returned Entity IType</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Returned Entity IType</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjQuerySpecifics#getReturnedEntityIType()
	 * @see #getIOssjQuerySpecifics()
	 * @generated
	 */
	EReference getIOssjQuerySpecifics_ReturnedEntityIType();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjQuerySpecifics#isSingleExtensionType <em>Single Extension Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Single Extension Type</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjQuerySpecifics#isSingleExtensionType()
	 * @see #getIOssjQuerySpecifics()
	 * @generated
	 */
	EAttribute getIOssjQuerySpecifics_SingleExtensionType();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjQuerySpecifics#isSessionFactoryMethods <em>Session Factory Methods</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Session Factory Methods</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjQuerySpecifics#isSessionFactoryMethods()
	 * @see #getIOssjQuerySpecifics()
	 * @generated
	 */
	EAttribute getIOssjQuerySpecifics_SessionFactoryMethods();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjUpdateProcedureSpecifics <em>IOssj Update Procedure Specifics</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IOssj Update Procedure Specifics</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjUpdateProcedureSpecifics
	 * @generated
	 */
	EClass getIOssjUpdateProcedureSpecifics();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjUpdateProcedureSpecifics#isSingleExtensionType <em>Single Extension Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Single Extension Type</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjUpdateProcedureSpecifics#isSingleExtensionType()
	 * @see #getIOssjUpdateProcedureSpecifics()
	 * @generated
	 */
	EAttribute getIOssjUpdateProcedureSpecifics_SingleExtensionType();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjUpdateProcedureSpecifics#isSessionFactoryMethods <em>Session Factory Methods</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Session Factory Methods</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjUpdateProcedureSpecifics#isSessionFactoryMethods()
	 * @see #getIOssjUpdateProcedureSpecifics()
	 * @generated
	 */
	EAttribute getIOssjUpdateProcedureSpecifics_SessionFactoryMethods();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IManagedEntityDetails <em>IManaged Entity Details</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IManaged Entity Details</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IManagedEntityDetails
	 * @generated
	 */
	EClass getIManagedEntityDetails();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IManagedEntityDetails#getManagedEntity <em>Managed Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Managed Entity</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IManagedEntityDetails#getManagedEntity()
	 * @see #getIManagedEntityDetails()
	 * @generated
	 */
	EReference getIManagedEntityDetails_ManagedEntity();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IManagedEntityDetails#getCrudFlavorDetails <em>Crud Flavor Details</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Crud Flavor Details</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IManagedEntityDetails#getCrudFlavorDetails()
	 * @see #getIManagedEntityDetails()
	 * @generated
	 */
	EReference getIManagedEntityDetails_CrudFlavorDetails();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IManagedEntityDetails#getCustomMethodFlavorDetails <em>Custom Method Flavor Details</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Custom Method Flavor Details</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.IManagedEntityDetails#getCustomMethodFlavorDetails()
	 * @see #getIManagedEntityDetails()
	 * @generated
	 */
	EReference getIManagedEntityDetails_CustomMethodFlavorDetails();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.EMethodType <em>EMethod Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>EMethod Type</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.EMethodType
	 * @generated
	 */
	EEnum getEMethodType();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.EEntityMethodFlavorFlag <em>EEntity Method Flavor Flag</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>EEntity Method Flavor Flag</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.EEntityMethodFlavorFlag
	 * @generated
	 */
	EEnum getEEntityMethodFlavorFlag();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	OssjFactory getOssjFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjArtifactSpecificsImpl <em>IOssj Artifact Specifics</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjArtifactSpecificsImpl
		 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.OssjPackageImpl#getIOssjArtifactSpecifics()
		 * @generated
		 */
		EClass IOSSJ_ARTIFACT_SPECIFICS = eINSTANCE.getIOssjArtifactSpecifics();

		/**
		 * The meta object literal for the '<em><b>Interface Properties</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IOSSJ_ARTIFACT_SPECIFICS__INTERFACE_PROPERTIES = eINSTANCE.getIOssjArtifactSpecifics_InterfaceProperties();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjDatatypeSpecificsImpl <em>IOssj Datatype Specifics</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjDatatypeSpecificsImpl
		 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.OssjPackageImpl#getIOssjDatatypeSpecifics()
		 * @generated
		 */
		EClass IOSSJ_DATATYPE_SPECIFICS = eINSTANCE.getIOssjDatatypeSpecifics();

		/**
		 * The meta object literal for the '<em><b>Single Extension Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IOSSJ_DATATYPE_SPECIFICS__SINGLE_EXTENSION_TYPE = eINSTANCE.getIOssjDatatypeSpecifics_SingleExtensionType();

		/**
		 * The meta object literal for the '<em><b>Session Factory Methods</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IOSSJ_DATATYPE_SPECIFICS__SESSION_FACTORY_METHODS = eINSTANCE.getIOssjDatatypeSpecifics_SessionFactoryMethods();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjEntitySpecificsImpl <em>IOssj Entity Specifics</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjEntitySpecificsImpl
		 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.OssjPackageImpl#getIOssjEntitySpecifics()
		 * @generated
		 */
		EClass IOSSJ_ENTITY_SPECIFICS = eINSTANCE.getIOssjEntitySpecifics();

		/**
		 * The meta object literal for the '<em><b>Flavor Details</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IOSSJ_ENTITY_SPECIFICS__FLAVOR_DETAILS = eINSTANCE.getIOssjEntitySpecifics_FlavorDetails();

		/**
		 * The meta object literal for the '<em><b>Primary Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IOSSJ_ENTITY_SPECIFICS__PRIMARY_KEY = eINSTANCE.getIOssjEntitySpecifics_PrimaryKey();

		/**
		 * The meta object literal for the '<em><b>Extensibility Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IOSSJ_ENTITY_SPECIFICS__EXTENSIBILITY_TYPE = eINSTANCE.getIOssjEntitySpecifics_ExtensibilityType();

		/**
		 * The meta object literal for the '<em><b>Session Factory Methods</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IOSSJ_ENTITY_SPECIFICS__SESSION_FACTORY_METHODS = eINSTANCE.getIOssjEntitySpecifics_SessionFactoryMethods();

		/**
		 * The meta object literal for the '<em><b>Interface Key Properties</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IOSSJ_ENTITY_SPECIFICS__INTERFACE_KEY_PROPERTIES = eINSTANCE.getIOssjEntitySpecifics_InterfaceKeyProperties();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjEnumSpecificsImpl <em>IOssj Enum Specifics</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjEnumSpecificsImpl
		 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.OssjPackageImpl#getIOssjEnumSpecifics()
		 * @generated
		 */
		EClass IOSSJ_ENUM_SPECIFICS = eINSTANCE.getIOssjEnumSpecifics();

		/**
		 * The meta object literal for the '<em><b>Extensible</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IOSSJ_ENUM_SPECIFICS__EXTENSIBLE = eINSTANCE.getIOssjEnumSpecifics_Extensible();

		/**
		 * The meta object literal for the '<em><b>Base IType</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IOSSJ_ENUM_SPECIFICS__BASE_ITYPE = eINSTANCE.getIOssjEnumSpecifics_BaseIType();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjEventSpecificsImpl <em>IOssj Event Specifics</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjEventSpecificsImpl
		 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.OssjPackageImpl#getIOssjEventSpecifics()
		 * @generated
		 */
		EClass IOSSJ_EVENT_SPECIFICS = eINSTANCE.getIOssjEventSpecifics();

		/**
		 * The meta object literal for the '<em><b>Single Extension Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IOSSJ_EVENT_SPECIFICS__SINGLE_EXTENSION_TYPE = eINSTANCE.getIOssjEventSpecifics_SingleExtensionType();

		/**
		 * The meta object literal for the '<em><b>Event Descriptor Entries</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IOSSJ_EVENT_SPECIFICS__EVENT_DESCRIPTOR_ENTRIES = eINSTANCE.getIOssjEventSpecifics_EventDescriptorEntries();

		/**
		 * The meta object literal for the '<em><b>Custom Event Descriptor Entries</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IOSSJ_EVENT_SPECIFICS__CUSTOM_EVENT_DESCRIPTOR_ENTRIES = eINSTANCE.getIOssjEventSpecifics_CustomEventDescriptorEntries();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IEventDescriptorEntryImpl <em>IEvent Descriptor Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IEventDescriptorEntryImpl
		 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.OssjPackageImpl#getIEventDescriptorEntry()
		 * @generated
		 */
		EClass IEVENT_DESCRIPTOR_ENTRY = eINSTANCE.getIEventDescriptorEntry();

		/**
		 * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IEVENT_DESCRIPTOR_ENTRY__LABEL = eINSTANCE.getIEventDescriptorEntry_Label();

		/**
		 * The meta object literal for the '<em><b>Primitive Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IEVENT_DESCRIPTOR_ENTRY__PRIMITIVE_TYPE = eINSTANCE.getIEventDescriptorEntry_PrimitiveType();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjQuerySpecificsImpl <em>IOssj Query Specifics</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjQuerySpecificsImpl
		 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.OssjPackageImpl#getIOssjQuerySpecifics()
		 * @generated
		 */
		EClass IOSSJ_QUERY_SPECIFICS = eINSTANCE.getIOssjQuerySpecifics();

		/**
		 * The meta object literal for the '<em><b>Returned Entity IType</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IOSSJ_QUERY_SPECIFICS__RETURNED_ENTITY_ITYPE = eINSTANCE.getIOssjQuerySpecifics_ReturnedEntityIType();

		/**
		 * The meta object literal for the '<em><b>Single Extension Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IOSSJ_QUERY_SPECIFICS__SINGLE_EXTENSION_TYPE = eINSTANCE.getIOssjQuerySpecifics_SingleExtensionType();

		/**
		 * The meta object literal for the '<em><b>Session Factory Methods</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IOSSJ_QUERY_SPECIFICS__SESSION_FACTORY_METHODS = eINSTANCE.getIOssjQuerySpecifics_SessionFactoryMethods();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjUpdateProcedureSpecificsImpl <em>IOssj Update Procedure Specifics</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjUpdateProcedureSpecificsImpl
		 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.OssjPackageImpl#getIOssjUpdateProcedureSpecifics()
		 * @generated
		 */
		EClass IOSSJ_UPDATE_PROCEDURE_SPECIFICS = eINSTANCE.getIOssjUpdateProcedureSpecifics();

		/**
		 * The meta object literal for the '<em><b>Single Extension Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IOSSJ_UPDATE_PROCEDURE_SPECIFICS__SINGLE_EXTENSION_TYPE = eINSTANCE.getIOssjUpdateProcedureSpecifics_SingleExtensionType();

		/**
		 * The meta object literal for the '<em><b>Session Factory Methods</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IOSSJ_UPDATE_PROCEDURE_SPECIFICS__SESSION_FACTORY_METHODS = eINSTANCE.getIOssjUpdateProcedureSpecifics_SessionFactoryMethods();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IManagedEntityDetailsImpl <em>IManaged Entity Details</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IManagedEntityDetailsImpl
		 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.OssjPackageImpl#getIManagedEntityDetails()
		 * @generated
		 */
		EClass IMANAGED_ENTITY_DETAILS = eINSTANCE.getIManagedEntityDetails();

		/**
		 * The meta object literal for the '<em><b>Managed Entity</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IMANAGED_ENTITY_DETAILS__MANAGED_ENTITY = eINSTANCE.getIManagedEntityDetails_ManagedEntity();

		/**
		 * The meta object literal for the '<em><b>Crud Flavor Details</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IMANAGED_ENTITY_DETAILS__CRUD_FLAVOR_DETAILS = eINSTANCE.getIManagedEntityDetails_CrudFlavorDetails();

		/**
		 * The meta object literal for the '<em><b>Custom Method Flavor Details</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IMANAGED_ENTITY_DETAILS__CUSTOM_METHOD_FLAVOR_DETAILS = eINSTANCE.getIManagedEntityDetails_CustomMethodFlavorDetails();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.EMethodType <em>EMethod Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.EMethodType
		 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.OssjPackageImpl#getEMethodType()
		 * @generated
		 */
		EEnum EMETHOD_TYPE = eINSTANCE.getEMethodType();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.EEntityMethodFlavorFlag <em>EEntity Method Flavor Flag</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.EEntityMethodFlavorFlag
		 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.OssjPackageImpl#getEEntityMethodFlavorFlag()
		 * @generated
		 */
		EEnum EENTITY_METHOD_FLAVOR_FLAG = eINSTANCE.getEEntityMethodFlavorFlag();

	}

} //OssjPackage
