/**
 * <copyright>
 * </copyright>
 *
 * $Id: MetamodelPackage.java,v 1.2 2008/02/28 18:05:32 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

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
 * @see org.eclipse.tigerstripe.metamodel.MetamodelFactory
 * @model kind="package"
 * @generated
 */
public interface MetamodelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "metamodel";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "tigerstripe";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "ts";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MetamodelPackage eINSTANCE = org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.impl.IStereotypeCapableImpl <em>IStereotype Capable</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.impl.IStereotypeCapableImpl
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIStereotypeCapable()
	 * @generated
	 */
	int ISTEREOTYPE_CAPABLE = 23;

	/**
	 * The feature id for the '<em><b>Stereotype Instances</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISTEREOTYPE_CAPABLE__STEREOTYPE_INSTANCES = 0;

	/**
	 * The number of structural features of the '<em>IStereotype Capable</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISTEREOTYPE_CAPABLE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.impl.IModelComponentImpl <em>IModel Component</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.impl.IModelComponentImpl
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIModelComponent()
	 * @generated
	 */
	int IMODEL_COMPONENT = 16;

	/**
	 * The feature id for the '<em><b>Stereotype Instances</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMODEL_COMPONENT__STEREOTYPE_INSTANCES = ISTEREOTYPE_CAPABLE__STEREOTYPE_INSTANCES;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMODEL_COMPONENT__NAME = ISTEREOTYPE_CAPABLE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMODEL_COMPONENT__COMMENT = ISTEREOTYPE_CAPABLE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMODEL_COMPONENT__VISIBILITY = ISTEREOTYPE_CAPABLE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>IModel Component</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMODEL_COMPONENT_FEATURE_COUNT = ISTEREOTYPE_CAPABLE_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.impl.IQualifiedNamedComponentImpl <em>IQualified Named Component</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.impl.IQualifiedNamedComponentImpl
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIQualifiedNamedComponent()
	 * @generated
	 */
	int IQUALIFIED_NAMED_COMPONENT = 17;

	/**
	 * The feature id for the '<em><b>Stereotype Instances</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IQUALIFIED_NAMED_COMPONENT__STEREOTYPE_INSTANCES = IMODEL_COMPONENT__STEREOTYPE_INSTANCES;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IQUALIFIED_NAMED_COMPONENT__NAME = IMODEL_COMPONENT__NAME;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IQUALIFIED_NAMED_COMPONENT__COMMENT = IMODEL_COMPONENT__COMMENT;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IQUALIFIED_NAMED_COMPONENT__VISIBILITY = IMODEL_COMPONENT__VISIBILITY;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IQUALIFIED_NAMED_COMPONENT__PACKAGE = IMODEL_COMPONENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>IQualified Named Component</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IQUALIFIED_NAMED_COMPONENT_FEATURE_COUNT = IMODEL_COMPONENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.impl.IAbstractArtifactImpl <em>IAbstract Artifact</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.impl.IAbstractArtifactImpl
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIAbstractArtifact()
	 * @generated
	 */
	int IABSTRACT_ARTIFACT = 0;

	/**
	 * The feature id for the '<em><b>Stereotype Instances</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IABSTRACT_ARTIFACT__STEREOTYPE_INSTANCES = IQUALIFIED_NAMED_COMPONENT__STEREOTYPE_INSTANCES;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IABSTRACT_ARTIFACT__NAME = IQUALIFIED_NAMED_COMPONENT__NAME;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IABSTRACT_ARTIFACT__COMMENT = IQUALIFIED_NAMED_COMPONENT__COMMENT;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IABSTRACT_ARTIFACT__VISIBILITY = IQUALIFIED_NAMED_COMPONENT__VISIBILITY;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IABSTRACT_ARTIFACT__PACKAGE = IQUALIFIED_NAMED_COMPONENT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Fields</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IABSTRACT_ARTIFACT__FIELDS = IQUALIFIED_NAMED_COMPONENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Methods</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IABSTRACT_ARTIFACT__METHODS = IQUALIFIED_NAMED_COMPONENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Literals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IABSTRACT_ARTIFACT__LITERALS = IQUALIFIED_NAMED_COMPONENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IABSTRACT_ARTIFACT__ABSTRACT = IQUALIFIED_NAMED_COMPONENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Extended Artifact</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IABSTRACT_ARTIFACT__EXTENDED_ARTIFACT = IQUALIFIED_NAMED_COMPONENT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Implemented Artifacts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IABSTRACT_ARTIFACT__IMPLEMENTED_ARTIFACTS = IQUALIFIED_NAMED_COMPONENT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Standard Specifics</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IABSTRACT_ARTIFACT__STANDARD_SPECIFICS = IQUALIFIED_NAMED_COMPONENT_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>IAbstract Artifact</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IABSTRACT_ARTIFACT_FEATURE_COUNT = IQUALIFIED_NAMED_COMPONENT_FEATURE_COUNT + 7;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.impl.IPrimitiveTypeImpl <em>IPrimitive Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.impl.IPrimitiveTypeImpl
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIPrimitiveType()
	 * @generated
	 */
	int IPRIMITIVE_TYPE = 1;

	/**
	 * The feature id for the '<em><b>Stereotype Instances</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPRIMITIVE_TYPE__STEREOTYPE_INSTANCES = IABSTRACT_ARTIFACT__STEREOTYPE_INSTANCES;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPRIMITIVE_TYPE__NAME = IABSTRACT_ARTIFACT__NAME;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPRIMITIVE_TYPE__COMMENT = IABSTRACT_ARTIFACT__COMMENT;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPRIMITIVE_TYPE__VISIBILITY = IABSTRACT_ARTIFACT__VISIBILITY;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPRIMITIVE_TYPE__PACKAGE = IABSTRACT_ARTIFACT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Fields</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPRIMITIVE_TYPE__FIELDS = IABSTRACT_ARTIFACT__FIELDS;

	/**
	 * The feature id for the '<em><b>Methods</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPRIMITIVE_TYPE__METHODS = IABSTRACT_ARTIFACT__METHODS;

	/**
	 * The feature id for the '<em><b>Literals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPRIMITIVE_TYPE__LITERALS = IABSTRACT_ARTIFACT__LITERALS;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPRIMITIVE_TYPE__ABSTRACT = IABSTRACT_ARTIFACT__ABSTRACT;

	/**
	 * The feature id for the '<em><b>Extended Artifact</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPRIMITIVE_TYPE__EXTENDED_ARTIFACT = IABSTRACT_ARTIFACT__EXTENDED_ARTIFACT;

	/**
	 * The feature id for the '<em><b>Implemented Artifacts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPRIMITIVE_TYPE__IMPLEMENTED_ARTIFACTS = IABSTRACT_ARTIFACT__IMPLEMENTED_ARTIFACTS;

	/**
	 * The feature id for the '<em><b>Standard Specifics</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPRIMITIVE_TYPE__STANDARD_SPECIFICS = IABSTRACT_ARTIFACT__STANDARD_SPECIFICS;

	/**
	 * The number of structural features of the '<em>IPrimitive Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPRIMITIVE_TYPE_FEATURE_COUNT = IABSTRACT_ARTIFACT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.impl.IManagedEntityArtifactImpl <em>IManaged Entity Artifact</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.impl.IManagedEntityArtifactImpl
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIManagedEntityArtifact()
	 * @generated
	 */
	int IMANAGED_ENTITY_ARTIFACT = 2;

	/**
	 * The feature id for the '<em><b>Stereotype Instances</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMANAGED_ENTITY_ARTIFACT__STEREOTYPE_INSTANCES = IABSTRACT_ARTIFACT__STEREOTYPE_INSTANCES;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMANAGED_ENTITY_ARTIFACT__NAME = IABSTRACT_ARTIFACT__NAME;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMANAGED_ENTITY_ARTIFACT__COMMENT = IABSTRACT_ARTIFACT__COMMENT;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMANAGED_ENTITY_ARTIFACT__VISIBILITY = IABSTRACT_ARTIFACT__VISIBILITY;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMANAGED_ENTITY_ARTIFACT__PACKAGE = IABSTRACT_ARTIFACT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Fields</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMANAGED_ENTITY_ARTIFACT__FIELDS = IABSTRACT_ARTIFACT__FIELDS;

	/**
	 * The feature id for the '<em><b>Methods</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMANAGED_ENTITY_ARTIFACT__METHODS = IABSTRACT_ARTIFACT__METHODS;

	/**
	 * The feature id for the '<em><b>Literals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMANAGED_ENTITY_ARTIFACT__LITERALS = IABSTRACT_ARTIFACT__LITERALS;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMANAGED_ENTITY_ARTIFACT__ABSTRACT = IABSTRACT_ARTIFACT__ABSTRACT;

	/**
	 * The feature id for the '<em><b>Extended Artifact</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMANAGED_ENTITY_ARTIFACT__EXTENDED_ARTIFACT = IABSTRACT_ARTIFACT__EXTENDED_ARTIFACT;

	/**
	 * The feature id for the '<em><b>Implemented Artifacts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMANAGED_ENTITY_ARTIFACT__IMPLEMENTED_ARTIFACTS = IABSTRACT_ARTIFACT__IMPLEMENTED_ARTIFACTS;

	/**
	 * The feature id for the '<em><b>Standard Specifics</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMANAGED_ENTITY_ARTIFACT__STANDARD_SPECIFICS = IABSTRACT_ARTIFACT__STANDARD_SPECIFICS;

	/**
	 * The feature id for the '<em><b>Primary Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMANAGED_ENTITY_ARTIFACT__PRIMARY_KEY = IABSTRACT_ARTIFACT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>IManaged Entity Artifact</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMANAGED_ENTITY_ARTIFACT_FEATURE_COUNT = IABSTRACT_ARTIFACT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.impl.IDatatypeArtifactImpl <em>IDatatype Artifact</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.impl.IDatatypeArtifactImpl
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIDatatypeArtifact()
	 * @generated
	 */
	int IDATATYPE_ARTIFACT = 3;

	/**
	 * The feature id for the '<em><b>Stereotype Instances</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDATATYPE_ARTIFACT__STEREOTYPE_INSTANCES = IABSTRACT_ARTIFACT__STEREOTYPE_INSTANCES;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDATATYPE_ARTIFACT__NAME = IABSTRACT_ARTIFACT__NAME;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDATATYPE_ARTIFACT__COMMENT = IABSTRACT_ARTIFACT__COMMENT;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDATATYPE_ARTIFACT__VISIBILITY = IABSTRACT_ARTIFACT__VISIBILITY;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDATATYPE_ARTIFACT__PACKAGE = IABSTRACT_ARTIFACT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Fields</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDATATYPE_ARTIFACT__FIELDS = IABSTRACT_ARTIFACT__FIELDS;

	/**
	 * The feature id for the '<em><b>Methods</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDATATYPE_ARTIFACT__METHODS = IABSTRACT_ARTIFACT__METHODS;

	/**
	 * The feature id for the '<em><b>Literals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDATATYPE_ARTIFACT__LITERALS = IABSTRACT_ARTIFACT__LITERALS;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDATATYPE_ARTIFACT__ABSTRACT = IABSTRACT_ARTIFACT__ABSTRACT;

	/**
	 * The feature id for the '<em><b>Extended Artifact</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDATATYPE_ARTIFACT__EXTENDED_ARTIFACT = IABSTRACT_ARTIFACT__EXTENDED_ARTIFACT;

	/**
	 * The feature id for the '<em><b>Implemented Artifacts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDATATYPE_ARTIFACT__IMPLEMENTED_ARTIFACTS = IABSTRACT_ARTIFACT__IMPLEMENTED_ARTIFACTS;

	/**
	 * The feature id for the '<em><b>Standard Specifics</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDATATYPE_ARTIFACT__STANDARD_SPECIFICS = IABSTRACT_ARTIFACT__STANDARD_SPECIFICS;

	/**
	 * The number of structural features of the '<em>IDatatype Artifact</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDATATYPE_ARTIFACT_FEATURE_COUNT = IABSTRACT_ARTIFACT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.impl.IExceptionArtifactImpl <em>IException Artifact</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.impl.IExceptionArtifactImpl
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIExceptionArtifact()
	 * @generated
	 */
	int IEXCEPTION_ARTIFACT = 4;

	/**
	 * The feature id for the '<em><b>Stereotype Instances</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEXCEPTION_ARTIFACT__STEREOTYPE_INSTANCES = IABSTRACT_ARTIFACT__STEREOTYPE_INSTANCES;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEXCEPTION_ARTIFACT__NAME = IABSTRACT_ARTIFACT__NAME;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEXCEPTION_ARTIFACT__COMMENT = IABSTRACT_ARTIFACT__COMMENT;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEXCEPTION_ARTIFACT__VISIBILITY = IABSTRACT_ARTIFACT__VISIBILITY;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEXCEPTION_ARTIFACT__PACKAGE = IABSTRACT_ARTIFACT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Fields</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEXCEPTION_ARTIFACT__FIELDS = IABSTRACT_ARTIFACT__FIELDS;

	/**
	 * The feature id for the '<em><b>Methods</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEXCEPTION_ARTIFACT__METHODS = IABSTRACT_ARTIFACT__METHODS;

	/**
	 * The feature id for the '<em><b>Literals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEXCEPTION_ARTIFACT__LITERALS = IABSTRACT_ARTIFACT__LITERALS;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEXCEPTION_ARTIFACT__ABSTRACT = IABSTRACT_ARTIFACT__ABSTRACT;

	/**
	 * The feature id for the '<em><b>Extended Artifact</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEXCEPTION_ARTIFACT__EXTENDED_ARTIFACT = IABSTRACT_ARTIFACT__EXTENDED_ARTIFACT;

	/**
	 * The feature id for the '<em><b>Implemented Artifacts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEXCEPTION_ARTIFACT__IMPLEMENTED_ARTIFACTS = IABSTRACT_ARTIFACT__IMPLEMENTED_ARTIFACTS;

	/**
	 * The feature id for the '<em><b>Standard Specifics</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEXCEPTION_ARTIFACT__STANDARD_SPECIFICS = IABSTRACT_ARTIFACT__STANDARD_SPECIFICS;

	/**
	 * The number of structural features of the '<em>IException Artifact</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEXCEPTION_ARTIFACT_FEATURE_COUNT = IABSTRACT_ARTIFACT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.impl.ISessionArtifactImpl <em>ISession Artifact</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.impl.ISessionArtifactImpl
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getISessionArtifact()
	 * @generated
	 */
	int ISESSION_ARTIFACT = 5;

	/**
	 * The feature id for the '<em><b>Stereotype Instances</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISESSION_ARTIFACT__STEREOTYPE_INSTANCES = IABSTRACT_ARTIFACT__STEREOTYPE_INSTANCES;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISESSION_ARTIFACT__NAME = IABSTRACT_ARTIFACT__NAME;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISESSION_ARTIFACT__COMMENT = IABSTRACT_ARTIFACT__COMMENT;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISESSION_ARTIFACT__VISIBILITY = IABSTRACT_ARTIFACT__VISIBILITY;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISESSION_ARTIFACT__PACKAGE = IABSTRACT_ARTIFACT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Fields</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISESSION_ARTIFACT__FIELDS = IABSTRACT_ARTIFACT__FIELDS;

	/**
	 * The feature id for the '<em><b>Methods</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISESSION_ARTIFACT__METHODS = IABSTRACT_ARTIFACT__METHODS;

	/**
	 * The feature id for the '<em><b>Literals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISESSION_ARTIFACT__LITERALS = IABSTRACT_ARTIFACT__LITERALS;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISESSION_ARTIFACT__ABSTRACT = IABSTRACT_ARTIFACT__ABSTRACT;

	/**
	 * The feature id for the '<em><b>Extended Artifact</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISESSION_ARTIFACT__EXTENDED_ARTIFACT = IABSTRACT_ARTIFACT__EXTENDED_ARTIFACT;

	/**
	 * The feature id for the '<em><b>Implemented Artifacts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISESSION_ARTIFACT__IMPLEMENTED_ARTIFACTS = IABSTRACT_ARTIFACT__IMPLEMENTED_ARTIFACTS;

	/**
	 * The feature id for the '<em><b>Standard Specifics</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISESSION_ARTIFACT__STANDARD_SPECIFICS = IABSTRACT_ARTIFACT__STANDARD_SPECIFICS;

	/**
	 * The feature id for the '<em><b>Managed Entities</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISESSION_ARTIFACT__MANAGED_ENTITIES = IABSTRACT_ARTIFACT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Emitted Notifications</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISESSION_ARTIFACT__EMITTED_NOTIFICATIONS = IABSTRACT_ARTIFACT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Supported Named Queries</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISESSION_ARTIFACT__SUPPORTED_NAMED_QUERIES = IABSTRACT_ARTIFACT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Exposed Update Procedures</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISESSION_ARTIFACT__EXPOSED_UPDATE_PROCEDURES = IABSTRACT_ARTIFACT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>ISession Artifact</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISESSION_ARTIFACT_FEATURE_COUNT = IABSTRACT_ARTIFACT_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.impl.IQueryArtifactImpl <em>IQuery Artifact</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.impl.IQueryArtifactImpl
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIQueryArtifact()
	 * @generated
	 */
	int IQUERY_ARTIFACT = 6;

	/**
	 * The feature id for the '<em><b>Stereotype Instances</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IQUERY_ARTIFACT__STEREOTYPE_INSTANCES = IABSTRACT_ARTIFACT__STEREOTYPE_INSTANCES;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IQUERY_ARTIFACT__NAME = IABSTRACT_ARTIFACT__NAME;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IQUERY_ARTIFACT__COMMENT = IABSTRACT_ARTIFACT__COMMENT;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IQUERY_ARTIFACT__VISIBILITY = IABSTRACT_ARTIFACT__VISIBILITY;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IQUERY_ARTIFACT__PACKAGE = IABSTRACT_ARTIFACT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Fields</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IQUERY_ARTIFACT__FIELDS = IABSTRACT_ARTIFACT__FIELDS;

	/**
	 * The feature id for the '<em><b>Methods</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IQUERY_ARTIFACT__METHODS = IABSTRACT_ARTIFACT__METHODS;

	/**
	 * The feature id for the '<em><b>Literals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IQUERY_ARTIFACT__LITERALS = IABSTRACT_ARTIFACT__LITERALS;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IQUERY_ARTIFACT__ABSTRACT = IABSTRACT_ARTIFACT__ABSTRACT;

	/**
	 * The feature id for the '<em><b>Extended Artifact</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IQUERY_ARTIFACT__EXTENDED_ARTIFACT = IABSTRACT_ARTIFACT__EXTENDED_ARTIFACT;

	/**
	 * The feature id for the '<em><b>Implemented Artifacts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IQUERY_ARTIFACT__IMPLEMENTED_ARTIFACTS = IABSTRACT_ARTIFACT__IMPLEMENTED_ARTIFACTS;

	/**
	 * The feature id for the '<em><b>Standard Specifics</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IQUERY_ARTIFACT__STANDARD_SPECIFICS = IABSTRACT_ARTIFACT__STANDARD_SPECIFICS;

	/**
	 * The feature id for the '<em><b>Returned Type</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IQUERY_ARTIFACT__RETURNED_TYPE = IABSTRACT_ARTIFACT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>IQuery Artifact</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IQUERY_ARTIFACT_FEATURE_COUNT = IABSTRACT_ARTIFACT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.impl.IUpdateProcedureArtifactImpl <em>IUpdate Procedure Artifact</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.impl.IUpdateProcedureArtifactImpl
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIUpdateProcedureArtifact()
	 * @generated
	 */
	int IUPDATE_PROCEDURE_ARTIFACT = 7;

	/**
	 * The feature id for the '<em><b>Stereotype Instances</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IUPDATE_PROCEDURE_ARTIFACT__STEREOTYPE_INSTANCES = IABSTRACT_ARTIFACT__STEREOTYPE_INSTANCES;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IUPDATE_PROCEDURE_ARTIFACT__NAME = IABSTRACT_ARTIFACT__NAME;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IUPDATE_PROCEDURE_ARTIFACT__COMMENT = IABSTRACT_ARTIFACT__COMMENT;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IUPDATE_PROCEDURE_ARTIFACT__VISIBILITY = IABSTRACT_ARTIFACT__VISIBILITY;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IUPDATE_PROCEDURE_ARTIFACT__PACKAGE = IABSTRACT_ARTIFACT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Fields</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IUPDATE_PROCEDURE_ARTIFACT__FIELDS = IABSTRACT_ARTIFACT__FIELDS;

	/**
	 * The feature id for the '<em><b>Methods</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IUPDATE_PROCEDURE_ARTIFACT__METHODS = IABSTRACT_ARTIFACT__METHODS;

	/**
	 * The feature id for the '<em><b>Literals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IUPDATE_PROCEDURE_ARTIFACT__LITERALS = IABSTRACT_ARTIFACT__LITERALS;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IUPDATE_PROCEDURE_ARTIFACT__ABSTRACT = IABSTRACT_ARTIFACT__ABSTRACT;

	/**
	 * The feature id for the '<em><b>Extended Artifact</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IUPDATE_PROCEDURE_ARTIFACT__EXTENDED_ARTIFACT = IABSTRACT_ARTIFACT__EXTENDED_ARTIFACT;

	/**
	 * The feature id for the '<em><b>Implemented Artifacts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IUPDATE_PROCEDURE_ARTIFACT__IMPLEMENTED_ARTIFACTS = IABSTRACT_ARTIFACT__IMPLEMENTED_ARTIFACTS;

	/**
	 * The feature id for the '<em><b>Standard Specifics</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IUPDATE_PROCEDURE_ARTIFACT__STANDARD_SPECIFICS = IABSTRACT_ARTIFACT__STANDARD_SPECIFICS;

	/**
	 * The number of structural features of the '<em>IUpdate Procedure Artifact</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IUPDATE_PROCEDURE_ARTIFACT_FEATURE_COUNT = IABSTRACT_ARTIFACT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.impl.IEventArtifactImpl <em>IEvent Artifact</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.impl.IEventArtifactImpl
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIEventArtifact()
	 * @generated
	 */
	int IEVENT_ARTIFACT = 8;

	/**
	 * The feature id for the '<em><b>Stereotype Instances</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEVENT_ARTIFACT__STEREOTYPE_INSTANCES = IABSTRACT_ARTIFACT__STEREOTYPE_INSTANCES;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEVENT_ARTIFACT__NAME = IABSTRACT_ARTIFACT__NAME;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEVENT_ARTIFACT__COMMENT = IABSTRACT_ARTIFACT__COMMENT;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEVENT_ARTIFACT__VISIBILITY = IABSTRACT_ARTIFACT__VISIBILITY;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEVENT_ARTIFACT__PACKAGE = IABSTRACT_ARTIFACT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Fields</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEVENT_ARTIFACT__FIELDS = IABSTRACT_ARTIFACT__FIELDS;

	/**
	 * The feature id for the '<em><b>Methods</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEVENT_ARTIFACT__METHODS = IABSTRACT_ARTIFACT__METHODS;

	/**
	 * The feature id for the '<em><b>Literals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEVENT_ARTIFACT__LITERALS = IABSTRACT_ARTIFACT__LITERALS;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEVENT_ARTIFACT__ABSTRACT = IABSTRACT_ARTIFACT__ABSTRACT;

	/**
	 * The feature id for the '<em><b>Extended Artifact</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEVENT_ARTIFACT__EXTENDED_ARTIFACT = IABSTRACT_ARTIFACT__EXTENDED_ARTIFACT;

	/**
	 * The feature id for the '<em><b>Implemented Artifacts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEVENT_ARTIFACT__IMPLEMENTED_ARTIFACTS = IABSTRACT_ARTIFACT__IMPLEMENTED_ARTIFACTS;

	/**
	 * The feature id for the '<em><b>Standard Specifics</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEVENT_ARTIFACT__STANDARD_SPECIFICS = IABSTRACT_ARTIFACT__STANDARD_SPECIFICS;

	/**
	 * The number of structural features of the '<em>IEvent Artifact</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEVENT_ARTIFACT_FEATURE_COUNT = IABSTRACT_ARTIFACT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.impl.IAssociationArtifactImpl <em>IAssociation Artifact</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.impl.IAssociationArtifactImpl
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIAssociationArtifact()
	 * @generated
	 */
	int IASSOCIATION_ARTIFACT = 9;

	/**
	 * The feature id for the '<em><b>Stereotype Instances</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_ARTIFACT__STEREOTYPE_INSTANCES = IABSTRACT_ARTIFACT__STEREOTYPE_INSTANCES;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_ARTIFACT__NAME = IABSTRACT_ARTIFACT__NAME;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_ARTIFACT__COMMENT = IABSTRACT_ARTIFACT__COMMENT;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_ARTIFACT__VISIBILITY = IABSTRACT_ARTIFACT__VISIBILITY;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_ARTIFACT__PACKAGE = IABSTRACT_ARTIFACT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Fields</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_ARTIFACT__FIELDS = IABSTRACT_ARTIFACT__FIELDS;

	/**
	 * The feature id for the '<em><b>Methods</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_ARTIFACT__METHODS = IABSTRACT_ARTIFACT__METHODS;

	/**
	 * The feature id for the '<em><b>Literals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_ARTIFACT__LITERALS = IABSTRACT_ARTIFACT__LITERALS;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_ARTIFACT__ABSTRACT = IABSTRACT_ARTIFACT__ABSTRACT;

	/**
	 * The feature id for the '<em><b>Extended Artifact</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_ARTIFACT__EXTENDED_ARTIFACT = IABSTRACT_ARTIFACT__EXTENDED_ARTIFACT;

	/**
	 * The feature id for the '<em><b>Implemented Artifacts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_ARTIFACT__IMPLEMENTED_ARTIFACTS = IABSTRACT_ARTIFACT__IMPLEMENTED_ARTIFACTS;

	/**
	 * The feature id for the '<em><b>Standard Specifics</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_ARTIFACT__STANDARD_SPECIFICS = IABSTRACT_ARTIFACT__STANDARD_SPECIFICS;

	/**
	 * The feature id for the '<em><b>AEnd</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_ARTIFACT__AEND = IABSTRACT_ARTIFACT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>ZEnd</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_ARTIFACT__ZEND = IABSTRACT_ARTIFACT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>IAssociation Artifact</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_ARTIFACT_FEATURE_COUNT = IABSTRACT_ARTIFACT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.impl.IAssociationClassArtifactImpl <em>IAssociation Class Artifact</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.impl.IAssociationClassArtifactImpl
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIAssociationClassArtifact()
	 * @generated
	 */
	int IASSOCIATION_CLASS_ARTIFACT = 10;

	/**
	 * The feature id for the '<em><b>Stereotype Instances</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_CLASS_ARTIFACT__STEREOTYPE_INSTANCES = IASSOCIATION_ARTIFACT__STEREOTYPE_INSTANCES;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_CLASS_ARTIFACT__NAME = IASSOCIATION_ARTIFACT__NAME;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_CLASS_ARTIFACT__COMMENT = IASSOCIATION_ARTIFACT__COMMENT;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_CLASS_ARTIFACT__VISIBILITY = IASSOCIATION_ARTIFACT__VISIBILITY;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_CLASS_ARTIFACT__PACKAGE = IASSOCIATION_ARTIFACT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Fields</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_CLASS_ARTIFACT__FIELDS = IASSOCIATION_ARTIFACT__FIELDS;

	/**
	 * The feature id for the '<em><b>Methods</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_CLASS_ARTIFACT__METHODS = IASSOCIATION_ARTIFACT__METHODS;

	/**
	 * The feature id for the '<em><b>Literals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_CLASS_ARTIFACT__LITERALS = IASSOCIATION_ARTIFACT__LITERALS;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_CLASS_ARTIFACT__ABSTRACT = IASSOCIATION_ARTIFACT__ABSTRACT;

	/**
	 * The feature id for the '<em><b>Extended Artifact</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_CLASS_ARTIFACT__EXTENDED_ARTIFACT = IASSOCIATION_ARTIFACT__EXTENDED_ARTIFACT;

	/**
	 * The feature id for the '<em><b>Implemented Artifacts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_CLASS_ARTIFACT__IMPLEMENTED_ARTIFACTS = IASSOCIATION_ARTIFACT__IMPLEMENTED_ARTIFACTS;

	/**
	 * The feature id for the '<em><b>Standard Specifics</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_CLASS_ARTIFACT__STANDARD_SPECIFICS = IASSOCIATION_ARTIFACT__STANDARD_SPECIFICS;

	/**
	 * The feature id for the '<em><b>AEnd</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_CLASS_ARTIFACT__AEND = IASSOCIATION_ARTIFACT__AEND;

	/**
	 * The feature id for the '<em><b>ZEnd</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_CLASS_ARTIFACT__ZEND = IASSOCIATION_ARTIFACT__ZEND;

	/**
	 * The feature id for the '<em><b>Primary Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_CLASS_ARTIFACT__PRIMARY_KEY = IASSOCIATION_ARTIFACT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>IAssociation Class Artifact</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_CLASS_ARTIFACT_FEATURE_COUNT = IASSOCIATION_ARTIFACT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.impl.IDependencyArtifactImpl <em>IDependency Artifact</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.impl.IDependencyArtifactImpl
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIDependencyArtifact()
	 * @generated
	 */
	int IDEPENDENCY_ARTIFACT = 11;

	/**
	 * The feature id for the '<em><b>Stereotype Instances</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDEPENDENCY_ARTIFACT__STEREOTYPE_INSTANCES = IABSTRACT_ARTIFACT__STEREOTYPE_INSTANCES;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDEPENDENCY_ARTIFACT__NAME = IABSTRACT_ARTIFACT__NAME;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDEPENDENCY_ARTIFACT__COMMENT = IABSTRACT_ARTIFACT__COMMENT;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDEPENDENCY_ARTIFACT__VISIBILITY = IABSTRACT_ARTIFACT__VISIBILITY;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDEPENDENCY_ARTIFACT__PACKAGE = IABSTRACT_ARTIFACT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Fields</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDEPENDENCY_ARTIFACT__FIELDS = IABSTRACT_ARTIFACT__FIELDS;

	/**
	 * The feature id for the '<em><b>Methods</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDEPENDENCY_ARTIFACT__METHODS = IABSTRACT_ARTIFACT__METHODS;

	/**
	 * The feature id for the '<em><b>Literals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDEPENDENCY_ARTIFACT__LITERALS = IABSTRACT_ARTIFACT__LITERALS;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDEPENDENCY_ARTIFACT__ABSTRACT = IABSTRACT_ARTIFACT__ABSTRACT;

	/**
	 * The feature id for the '<em><b>Extended Artifact</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDEPENDENCY_ARTIFACT__EXTENDED_ARTIFACT = IABSTRACT_ARTIFACT__EXTENDED_ARTIFACT;

	/**
	 * The feature id for the '<em><b>Implemented Artifacts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDEPENDENCY_ARTIFACT__IMPLEMENTED_ARTIFACTS = IABSTRACT_ARTIFACT__IMPLEMENTED_ARTIFACTS;

	/**
	 * The feature id for the '<em><b>Standard Specifics</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDEPENDENCY_ARTIFACT__STANDARD_SPECIFICS = IABSTRACT_ARTIFACT__STANDARD_SPECIFICS;

	/**
	 * The feature id for the '<em><b>AEnd Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDEPENDENCY_ARTIFACT__AEND_TYPE = IABSTRACT_ARTIFACT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>ZEnd Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDEPENDENCY_ARTIFACT__ZEND_TYPE = IABSTRACT_ARTIFACT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>IDependency Artifact</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDEPENDENCY_ARTIFACT_FEATURE_COUNT = IABSTRACT_ARTIFACT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.impl.IEnumArtifactImpl <em>IEnum Artifact</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.impl.IEnumArtifactImpl
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIEnumArtifact()
	 * @generated
	 */
	int IENUM_ARTIFACT = 12;

	/**
	 * The feature id for the '<em><b>Stereotype Instances</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENUM_ARTIFACT__STEREOTYPE_INSTANCES = IABSTRACT_ARTIFACT__STEREOTYPE_INSTANCES;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENUM_ARTIFACT__NAME = IABSTRACT_ARTIFACT__NAME;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENUM_ARTIFACT__COMMENT = IABSTRACT_ARTIFACT__COMMENT;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENUM_ARTIFACT__VISIBILITY = IABSTRACT_ARTIFACT__VISIBILITY;

	/**
	 * The feature id for the '<em><b>Package</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENUM_ARTIFACT__PACKAGE = IABSTRACT_ARTIFACT__PACKAGE;

	/**
	 * The feature id for the '<em><b>Fields</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENUM_ARTIFACT__FIELDS = IABSTRACT_ARTIFACT__FIELDS;

	/**
	 * The feature id for the '<em><b>Methods</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENUM_ARTIFACT__METHODS = IABSTRACT_ARTIFACT__METHODS;

	/**
	 * The feature id for the '<em><b>Literals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENUM_ARTIFACT__LITERALS = IABSTRACT_ARTIFACT__LITERALS;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENUM_ARTIFACT__ABSTRACT = IABSTRACT_ARTIFACT__ABSTRACT;

	/**
	 * The feature id for the '<em><b>Extended Artifact</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENUM_ARTIFACT__EXTENDED_ARTIFACT = IABSTRACT_ARTIFACT__EXTENDED_ARTIFACT;

	/**
	 * The feature id for the '<em><b>Implemented Artifacts</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENUM_ARTIFACT__IMPLEMENTED_ARTIFACTS = IABSTRACT_ARTIFACT__IMPLEMENTED_ARTIFACTS;

	/**
	 * The feature id for the '<em><b>Standard Specifics</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENUM_ARTIFACT__STANDARD_SPECIFICS = IABSTRACT_ARTIFACT__STANDARD_SPECIFICS;

	/**
	 * The feature id for the '<em><b>Base Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENUM_ARTIFACT__BASE_TYPE = IABSTRACT_ARTIFACT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>IEnum Artifact</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENUM_ARTIFACT_FEATURE_COUNT = IABSTRACT_ARTIFACT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.impl.IFieldImpl <em>IField</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.impl.IFieldImpl
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIField()
	 * @generated
	 */
	int IFIELD = 13;

	/**
	 * The feature id for the '<em><b>Stereotype Instances</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IFIELD__STEREOTYPE_INSTANCES = IMODEL_COMPONENT__STEREOTYPE_INSTANCES;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IFIELD__NAME = IMODEL_COMPONENT__NAME;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IFIELD__COMMENT = IMODEL_COMPONENT__COMMENT;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IFIELD__VISIBILITY = IMODEL_COMPONENT__VISIBILITY;

	/**
	 * The feature id for the '<em><b>Optional</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IFIELD__OPTIONAL = IMODEL_COMPONENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Read Only</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IFIELD__READ_ONLY = IMODEL_COMPONENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Ordered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IFIELD__ORDERED = IMODEL_COMPONENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Unique</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IFIELD__UNIQUE = IMODEL_COMPONENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IFIELD__TYPE = IMODEL_COMPONENT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IFIELD__DEFAULT_VALUE = IMODEL_COMPONENT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Ref By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IFIELD__REF_BY = IMODEL_COMPONENT_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>IField</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IFIELD_FEATURE_COUNT = IMODEL_COMPONENT_FEATURE_COUNT + 7;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.impl.IMethodImpl <em>IMethod</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.impl.IMethodImpl
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIMethod()
	 * @generated
	 */
	int IMETHOD = 14;

	/**
	 * The feature id for the '<em><b>Stereotype Instances</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMETHOD__STEREOTYPE_INSTANCES = IMODEL_COMPONENT__STEREOTYPE_INSTANCES;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMETHOD__NAME = IMODEL_COMPONENT__NAME;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMETHOD__COMMENT = IMODEL_COMPONENT__COMMENT;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMETHOD__VISIBILITY = IMODEL_COMPONENT__VISIBILITY;

	/**
	 * The feature id for the '<em><b>Arguments</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMETHOD__ARGUMENTS = IMODEL_COMPONENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Return Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMETHOD__RETURN_TYPE = IMODEL_COMPONENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMETHOD__ABSTRACT = IMODEL_COMPONENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Ordered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMETHOD__ORDERED = IMODEL_COMPONENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Unique</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMETHOD__UNIQUE = IMODEL_COMPONENT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Optional</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMETHOD__OPTIONAL = IMODEL_COMPONENT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Exceptions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMETHOD__EXCEPTIONS = IMODEL_COMPONENT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Void</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMETHOD__VOID = IMODEL_COMPONENT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Iterator Return</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMETHOD__ITERATOR_RETURN = IMODEL_COMPONENT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Return Ref By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMETHOD__RETURN_REF_BY = IMODEL_COMPONENT_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Instance Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMETHOD__INSTANCE_METHOD = IMODEL_COMPONENT_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>Default Return Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMETHOD__DEFAULT_RETURN_VALUE = IMODEL_COMPONENT_FEATURE_COUNT + 11;

	/**
	 * The feature id for the '<em><b>Method Return Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMETHOD__METHOD_RETURN_NAME = IMODEL_COMPONENT_FEATURE_COUNT + 12;

	/**
	 * The feature id for the '<em><b>Return Stereotype Instances</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMETHOD__RETURN_STEREOTYPE_INSTANCES = IMODEL_COMPONENT_FEATURE_COUNT + 13;

	/**
	 * The feature id for the '<em><b>Entity Method Flavor Details</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMETHOD__ENTITY_METHOD_FLAVOR_DETAILS = IMODEL_COMPONENT_FEATURE_COUNT + 14;

	/**
	 * The number of structural features of the '<em>IMethod</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMETHOD_FEATURE_COUNT = IMODEL_COMPONENT_FEATURE_COUNT + 15;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.impl.ILiteralImpl <em>ILiteral</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.impl.ILiteralImpl
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getILiteral()
	 * @generated
	 */
	int ILITERAL = 15;

	/**
	 * The feature id for the '<em><b>Stereotype Instances</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ILITERAL__STEREOTYPE_INSTANCES = IMODEL_COMPONENT__STEREOTYPE_INSTANCES;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ILITERAL__NAME = IMODEL_COMPONENT__NAME;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ILITERAL__COMMENT = IMODEL_COMPONENT__COMMENT;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ILITERAL__VISIBILITY = IMODEL_COMPONENT__VISIBILITY;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ILITERAL__VALUE = IMODEL_COMPONENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ILITERAL__TYPE = IMODEL_COMPONENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>ILiteral</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ILITERAL_FEATURE_COUNT = IMODEL_COMPONENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.impl.ITypeImpl <em>IType</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.impl.ITypeImpl
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIType()
	 * @generated
	 */
	int ITYPE = 18;

	/**
	 * The feature id for the '<em><b>Fully Qualified Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITYPE__FULLY_QUALIFIED_NAME = 0;

	/**
	 * The feature id for the '<em><b>Multiplicity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITYPE__MULTIPLICITY = 1;

	/**
	 * The number of structural features of the '<em>IType</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITYPE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.impl.IAssociationEndImpl <em>IAssociation End</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.impl.IAssociationEndImpl
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIAssociationEnd()
	 * @generated
	 */
	int IASSOCIATION_END = 19;

	/**
	 * The feature id for the '<em><b>Stereotype Instances</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_END__STEREOTYPE_INSTANCES = IMODEL_COMPONENT__STEREOTYPE_INSTANCES;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_END__NAME = IMODEL_COMPONENT__NAME;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_END__COMMENT = IMODEL_COMPONENT__COMMENT;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_END__VISIBILITY = IMODEL_COMPONENT__VISIBILITY;

	/**
	 * The feature id for the '<em><b>Aggregation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_END__AGGREGATION = IMODEL_COMPONENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Changeable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_END__CHANGEABLE = IMODEL_COMPONENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Navigable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_END__NAVIGABLE = IMODEL_COMPONENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Ordered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_END__ORDERED = IMODEL_COMPONENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Unique</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_END__UNIQUE = IMODEL_COMPONENT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Multiplicity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_END__MULTIPLICITY = IMODEL_COMPONENT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_END__TYPE = IMODEL_COMPONENT_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>IAssociation End</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IASSOCIATION_END_FEATURE_COUNT = IMODEL_COMPONENT_FEATURE_COUNT + 7;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.impl.IArgumentImpl <em>IArgument</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.impl.IArgumentImpl
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIArgument()
	 * @generated
	 */
	int IARGUMENT = 20;

	/**
	 * The feature id for the '<em><b>Stereotype Instances</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IARGUMENT__STEREOTYPE_INSTANCES = IMODEL_COMPONENT__STEREOTYPE_INSTANCES;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IARGUMENT__NAME = IMODEL_COMPONENT__NAME;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IARGUMENT__COMMENT = IMODEL_COMPONENT__COMMENT;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IARGUMENT__VISIBILITY = IMODEL_COMPONENT__VISIBILITY;

	/**
	 * The feature id for the '<em><b>Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IARGUMENT__TYPE = IMODEL_COMPONENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IARGUMENT__DEFAULT_VALUE = IMODEL_COMPONENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Ordered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IARGUMENT__ORDERED = IMODEL_COMPONENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Unique</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IARGUMENT__UNIQUE = IMODEL_COMPONENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Ref By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IARGUMENT__REF_BY = IMODEL_COMPONENT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>IArgument</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IARGUMENT_FEATURE_COUNT = IMODEL_COMPONENT_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.impl.IModelImpl <em>IModel</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.impl.IModelImpl
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIModel()
	 * @generated
	 */
	int IMODEL = 21;

	/**
	 * The feature id for the '<em><b>Stereotype Instances</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMODEL__STEREOTYPE_INSTANCES = IMODEL_COMPONENT__STEREOTYPE_INSTANCES;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMODEL__NAME = IMODEL_COMPONENT__NAME;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMODEL__COMMENT = IMODEL_COMPONENT__COMMENT;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMODEL__VISIBILITY = IMODEL_COMPONENT__VISIBILITY;

	/**
	 * The feature id for the '<em><b>Packages</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMODEL__PACKAGES = IMODEL_COMPONENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>IModel</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMODEL_FEATURE_COUNT = IMODEL_COMPONENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.impl.IPackageImpl <em>IPackage</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.impl.IPackageImpl
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIPackage()
	 * @generated
	 */
	int IPACKAGE = 22;

	/**
	 * The feature id for the '<em><b>Artifacts</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPACKAGE__ARTIFACTS = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPACKAGE__NAME = 1;

	/**
	 * The number of structural features of the '<em>IPackage</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPACKAGE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.impl.IStereotypeInstanceImpl <em>IStereotype Instance</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.impl.IStereotypeInstanceImpl
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIStereotypeInstance()
	 * @generated
	 */
	int ISTEREOTYPE_INSTANCE = 24;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISTEREOTYPE_INSTANCE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Attribute Values</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISTEREOTYPE_INSTANCE__ATTRIBUTE_VALUES = 1;

	/**
	 * The number of structural features of the '<em>IStereotype Instance</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISTEREOTYPE_INSTANCE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.impl.IStereotypeAttributeValueImpl <em>IStereotype Attribute Value</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.impl.IStereotypeAttributeValueImpl
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIStereotypeAttributeValue()
	 * @generated
	 */
	int ISTEREOTYPE_ATTRIBUTE_VALUE = 25;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISTEREOTYPE_ATTRIBUTE_VALUE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISTEREOTYPE_ATTRIBUTE_VALUE__VALUE = 1;

	/**
	 * The number of structural features of the '<em>IStereotype Attribute Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISTEREOTYPE_ATTRIBUTE_VALUE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.impl.IEntityMethodFlavorDetailsImpl <em>IEntity Method Flavor Details</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.impl.IEntityMethodFlavorDetailsImpl
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIEntityMethodFlavorDetails()
	 * @generated
	 */
	int IENTITY_METHOD_FLAVOR_DETAILS = 26;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENTITY_METHOD_FLAVOR_DETAILS__COMMENT = 0;

	/**
	 * The feature id for the '<em><b>Flag</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENTITY_METHOD_FLAVOR_DETAILS__FLAG = 1;

	/**
	 * The feature id for the '<em><b>Exceptions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENTITY_METHOD_FLAVOR_DETAILS__EXCEPTIONS = 2;

	/**
	 * The feature id for the '<em><b>Method</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENTITY_METHOD_FLAVOR_DETAILS__METHOD = 3;

	/**
	 * The feature id for the '<em><b>Flavor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENTITY_METHOD_FLAVOR_DETAILS__FLAVOR = 4;

	/**
	 * The feature id for the '<em><b>Method Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENTITY_METHOD_FLAVOR_DETAILS__METHOD_TYPE = 5;

	/**
	 * The number of structural features of the '<em>IEntity Method Flavor Details</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IENTITY_METHOD_FLAVOR_DETAILS_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.impl.IMultiplicityImpl <em>IMultiplicity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.impl.IMultiplicityImpl
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIMultiplicity()
	 * @generated
	 */
	int IMULTIPLICITY = 27;

	/**
	 * The feature id for the '<em><b>Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMULTIPLICITY__LOWER_BOUND = 0;

	/**
	 * The feature id for the '<em><b>Upper Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMULTIPLICITY__UPPER_BOUND = 1;

	/**
	 * The number of structural features of the '<em>IMultiplicity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMULTIPLICITY_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.ERefByEnum <em>ERef By Enum</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.ERefByEnum
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getERefByEnum()
	 * @generated
	 */
	int EREF_BY_ENUM = 28;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.VisibilityEnum <em>Visibility Enum</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.VisibilityEnum
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getVisibilityEnum()
	 * @generated
	 */
	int VISIBILITY_ENUM = 29;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.EAggregationEnum <em>EAggregation Enum</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.EAggregationEnum
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getEAggregationEnum()
	 * @generated
	 */
	int EAGGREGATION_ENUM = 30;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.EChangeableEnum <em>EChangeable Enum</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.EChangeableEnum
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getEChangeableEnum()
	 * @generated
	 */
	int ECHANGEABLE_ENUM = 31;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.metamodel.OssjEntityMethodFlavor <em>Ossj Entity Method Flavor</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.metamodel.OssjEntityMethodFlavor
	 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getOssjEntityMethodFlavor()
	 * @generated
	 */
	int OSSJ_ENTITY_METHOD_FLAVOR = 32;

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact <em>IAbstract Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IAbstract Artifact</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IAbstractArtifact
	 * @generated
	 */
	EClass getIAbstractArtifact();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getFields <em>Fields</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Fields</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getFields()
	 * @see #getIAbstractArtifact()
	 * @generated
	 */
	EReference getIAbstractArtifact_Fields();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getMethods <em>Methods</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Methods</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getMethods()
	 * @see #getIAbstractArtifact()
	 * @generated
	 */
	EReference getIAbstractArtifact_Methods();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getLiterals <em>Literals</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Literals</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getLiterals()
	 * @see #getIAbstractArtifact()
	 * @generated
	 */
	EReference getIAbstractArtifact_Literals();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#isAbstract <em>Abstract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Abstract</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IAbstractArtifact#isAbstract()
	 * @see #getIAbstractArtifact()
	 * @generated
	 */
	EAttribute getIAbstractArtifact_Abstract();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getExtendedArtifact <em>Extended Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Extended Artifact</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getExtendedArtifact()
	 * @see #getIAbstractArtifact()
	 * @generated
	 */
	EReference getIAbstractArtifact_ExtendedArtifact();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getImplementedArtifacts <em>Implemented Artifacts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Implemented Artifacts</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getImplementedArtifacts()
	 * @see #getIAbstractArtifact()
	 * @generated
	 */
	EReference getIAbstractArtifact_ImplementedArtifacts();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getStandardSpecifics <em>Standard Specifics</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Standard Specifics</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IAbstractArtifact#getStandardSpecifics()
	 * @see #getIAbstractArtifact()
	 * @generated
	 */
	EReference getIAbstractArtifact_StandardSpecifics();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.IPrimitiveType <em>IPrimitive Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IPrimitive Type</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IPrimitiveType
	 * @generated
	 */
	EClass getIPrimitiveType();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact <em>IManaged Entity Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IManaged Entity Artifact</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact
	 * @generated
	 */
	EClass getIManagedEntityArtifact();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact#getPrimaryKey <em>Primary Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Primary Key</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact#getPrimaryKey()
	 * @see #getIManagedEntityArtifact()
	 * @generated
	 */
	EAttribute getIManagedEntityArtifact_PrimaryKey();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.IDatatypeArtifact <em>IDatatype Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IDatatype Artifact</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IDatatypeArtifact
	 * @generated
	 */
	EClass getIDatatypeArtifact();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.IExceptionArtifact <em>IException Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IException Artifact</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IExceptionArtifact
	 * @generated
	 */
	EClass getIExceptionArtifact();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.ISessionArtifact <em>ISession Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ISession Artifact</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.ISessionArtifact
	 * @generated
	 */
	EClass getISessionArtifact();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.metamodel.ISessionArtifact#getManagedEntities <em>Managed Entities</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Managed Entities</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.ISessionArtifact#getManagedEntities()
	 * @see #getISessionArtifact()
	 * @generated
	 */
	EReference getISessionArtifact_ManagedEntities();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.metamodel.ISessionArtifact#getEmittedNotifications <em>Emitted Notifications</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Emitted Notifications</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.ISessionArtifact#getEmittedNotifications()
	 * @see #getISessionArtifact()
	 * @generated
	 */
	EReference getISessionArtifact_EmittedNotifications();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.metamodel.ISessionArtifact#getSupportedNamedQueries <em>Supported Named Queries</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Supported Named Queries</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.ISessionArtifact#getSupportedNamedQueries()
	 * @see #getISessionArtifact()
	 * @generated
	 */
	EReference getISessionArtifact_SupportedNamedQueries();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.metamodel.ISessionArtifact#getExposedUpdateProcedures <em>Exposed Update Procedures</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Exposed Update Procedures</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.ISessionArtifact#getExposedUpdateProcedures()
	 * @see #getISessionArtifact()
	 * @generated
	 */
	EReference getISessionArtifact_ExposedUpdateProcedures();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.IQueryArtifact <em>IQuery Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IQuery Artifact</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IQueryArtifact
	 * @generated
	 */
	EClass getIQueryArtifact();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.metamodel.IQueryArtifact#getReturnedType <em>Returned Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Returned Type</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IQueryArtifact#getReturnedType()
	 * @see #getIQueryArtifact()
	 * @generated
	 */
	EReference getIQueryArtifact_ReturnedType();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.IUpdateProcedureArtifact <em>IUpdate Procedure Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IUpdate Procedure Artifact</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IUpdateProcedureArtifact
	 * @generated
	 */
	EClass getIUpdateProcedureArtifact();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.IEventArtifact <em>IEvent Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IEvent Artifact</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IEventArtifact
	 * @generated
	 */
	EClass getIEventArtifact();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.IAssociationArtifact <em>IAssociation Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IAssociation Artifact</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IAssociationArtifact
	 * @generated
	 */
	EClass getIAssociationArtifact();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.tigerstripe.metamodel.IAssociationArtifact#getAEnd <em>AEnd</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>AEnd</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IAssociationArtifact#getAEnd()
	 * @see #getIAssociationArtifact()
	 * @generated
	 */
	EReference getIAssociationArtifact_AEnd();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.metamodel.IAssociationArtifact#getZEnd <em>ZEnd</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>ZEnd</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IAssociationArtifact#getZEnd()
	 * @see #getIAssociationArtifact()
	 * @generated
	 */
	EReference getIAssociationArtifact_ZEnd();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.IAssociationClassArtifact <em>IAssociation Class Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IAssociation Class Artifact</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IAssociationClassArtifact
	 * @generated
	 */
	EClass getIAssociationClassArtifact();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.IDependencyArtifact <em>IDependency Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IDependency Artifact</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IDependencyArtifact
	 * @generated
	 */
	EClass getIDependencyArtifact();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.metamodel.IDependencyArtifact#getAEndType <em>AEnd Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>AEnd Type</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IDependencyArtifact#getAEndType()
	 * @see #getIDependencyArtifact()
	 * @generated
	 */
	EReference getIDependencyArtifact_AEndType();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.metamodel.IDependencyArtifact#getZEndType <em>ZEnd Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>ZEnd Type</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IDependencyArtifact#getZEndType()
	 * @see #getIDependencyArtifact()
	 * @generated
	 */
	EReference getIDependencyArtifact_ZEndType();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.IEnumArtifact <em>IEnum Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IEnum Artifact</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IEnumArtifact
	 * @generated
	 */
	EClass getIEnumArtifact();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IEnumArtifact#getBaseType <em>Base Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Type</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IEnumArtifact#getBaseType()
	 * @see #getIEnumArtifact()
	 * @generated
	 */
	EAttribute getIEnumArtifact_BaseType();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.IField <em>IField</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IField</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IField
	 * @generated
	 */
	EClass getIField();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IField#isOptional <em>Optional</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Optional</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IField#isOptional()
	 * @see #getIField()
	 * @generated
	 */
	EAttribute getIField_Optional();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IField#isReadOnly <em>Read Only</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Read Only</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IField#isReadOnly()
	 * @see #getIField()
	 * @generated
	 */
	EAttribute getIField_ReadOnly();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IField#isOrdered <em>Ordered</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ordered</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IField#isOrdered()
	 * @see #getIField()
	 * @generated
	 */
	EAttribute getIField_Ordered();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IField#isUnique <em>Unique</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Unique</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IField#isUnique()
	 * @see #getIField()
	 * @generated
	 */
	EAttribute getIField_Unique();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.metamodel.IField#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Type</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IField#getType()
	 * @see #getIField()
	 * @generated
	 */
	EReference getIField_Type();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IField#getDefaultValue <em>Default Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Value</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IField#getDefaultValue()
	 * @see #getIField()
	 * @generated
	 */
	EAttribute getIField_DefaultValue();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IField#getRefBy <em>Ref By</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ref By</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IField#getRefBy()
	 * @see #getIField()
	 * @generated
	 */
	EAttribute getIField_RefBy();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.IMethod <em>IMethod</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IMethod</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IMethod
	 * @generated
	 */
	EClass getIMethod();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.metamodel.IMethod#getArguments <em>Arguments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Arguments</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IMethod#getArguments()
	 * @see #getIMethod()
	 * @generated
	 */
	EReference getIMethod_Arguments();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.metamodel.IMethod#getReturnType <em>Return Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Return Type</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IMethod#getReturnType()
	 * @see #getIMethod()
	 * @generated
	 */
	EReference getIMethod_ReturnType();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IMethod#isAbstract <em>Abstract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Abstract</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IMethod#isAbstract()
	 * @see #getIMethod()
	 * @generated
	 */
	EAttribute getIMethod_Abstract();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IMethod#isOrdered <em>Ordered</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ordered</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IMethod#isOrdered()
	 * @see #getIMethod()
	 * @generated
	 */
	EAttribute getIMethod_Ordered();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IMethod#isUnique <em>Unique</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Unique</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IMethod#isUnique()
	 * @see #getIMethod()
	 * @generated
	 */
	EAttribute getIMethod_Unique();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IMethod#isOptional <em>Optional</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Optional</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IMethod#isOptional()
	 * @see #getIMethod()
	 * @generated
	 */
	EAttribute getIMethod_Optional();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.metamodel.IMethod#getExceptions <em>Exceptions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Exceptions</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IMethod#getExceptions()
	 * @see #getIMethod()
	 * @generated
	 */
	EReference getIMethod_Exceptions();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IMethod#isVoid <em>Void</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Void</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IMethod#isVoid()
	 * @see #getIMethod()
	 * @generated
	 */
	EAttribute getIMethod_Void();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IMethod#isIteratorReturn <em>Iterator Return</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Iterator Return</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IMethod#isIteratorReturn()
	 * @see #getIMethod()
	 * @generated
	 */
	EAttribute getIMethod_IteratorReturn();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IMethod#getReturnRefBy <em>Return Ref By</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Return Ref By</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IMethod#getReturnRefBy()
	 * @see #getIMethod()
	 * @generated
	 */
	EAttribute getIMethod_ReturnRefBy();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IMethod#isInstanceMethod <em>Instance Method</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Instance Method</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IMethod#isInstanceMethod()
	 * @see #getIMethod()
	 * @generated
	 */
	EAttribute getIMethod_InstanceMethod();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IMethod#getDefaultReturnValue <em>Default Return Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Return Value</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IMethod#getDefaultReturnValue()
	 * @see #getIMethod()
	 * @generated
	 */
	EAttribute getIMethod_DefaultReturnValue();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IMethod#getMethodReturnName <em>Method Return Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Method Return Name</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IMethod#getMethodReturnName()
	 * @see #getIMethod()
	 * @generated
	 */
	EAttribute getIMethod_MethodReturnName();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.metamodel.IMethod#getReturnStereotypeInstances <em>Return Stereotype Instances</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Return Stereotype Instances</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IMethod#getReturnStereotypeInstances()
	 * @see #getIMethod()
	 * @generated
	 */
	EReference getIMethod_ReturnStereotypeInstances();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.metamodel.IMethod#getEntityMethodFlavorDetails <em>Entity Method Flavor Details</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Entity Method Flavor Details</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IMethod#getEntityMethodFlavorDetails()
	 * @see #getIMethod()
	 * @generated
	 */
	EReference getIMethod_EntityMethodFlavorDetails();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.ILiteral <em>ILiteral</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ILiteral</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.ILiteral
	 * @generated
	 */
	EClass getILiteral();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.ILiteral#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.ILiteral#getValue()
	 * @see #getILiteral()
	 * @generated
	 */
	EAttribute getILiteral_Value();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.metamodel.ILiteral#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Type</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.ILiteral#getType()
	 * @see #getILiteral()
	 * @generated
	 */
	EReference getILiteral_Type();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.IModelComponent <em>IModel Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IModel Component</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IModelComponent
	 * @generated
	 */
	EClass getIModelComponent();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IModelComponent#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IModelComponent#getName()
	 * @see #getIModelComponent()
	 * @generated
	 */
	EAttribute getIModelComponent_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IModelComponent#getComment <em>Comment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Comment</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IModelComponent#getComment()
	 * @see #getIModelComponent()
	 * @generated
	 */
	EAttribute getIModelComponent_Comment();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IModelComponent#getVisibility <em>Visibility</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Visibility</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IModelComponent#getVisibility()
	 * @see #getIModelComponent()
	 * @generated
	 */
	EAttribute getIModelComponent_Visibility();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.IQualifiedNamedComponent <em>IQualified Named Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IQualified Named Component</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IQualifiedNamedComponent
	 * @generated
	 */
	EClass getIQualifiedNamedComponent();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IQualifiedNamedComponent#getPackage <em>Package</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Package</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IQualifiedNamedComponent#getPackage()
	 * @see #getIQualifiedNamedComponent()
	 * @generated
	 */
	EAttribute getIQualifiedNamedComponent_Package();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.IType <em>IType</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IType</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IType
	 * @generated
	 */
	EClass getIType();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IType#getFullyQualifiedName <em>Fully Qualified Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fully Qualified Name</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IType#getFullyQualifiedName()
	 * @see #getIType()
	 * @generated
	 */
	EAttribute getIType_FullyQualifiedName();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.metamodel.IType#getMultiplicity <em>Multiplicity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Multiplicity</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IType#getMultiplicity()
	 * @see #getIType()
	 * @generated
	 */
	EReference getIType_Multiplicity();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.IAssociationEnd <em>IAssociation End</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IAssociation End</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IAssociationEnd
	 * @generated
	 */
	EClass getIAssociationEnd();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IAssociationEnd#getAggregation <em>Aggregation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Aggregation</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IAssociationEnd#getAggregation()
	 * @see #getIAssociationEnd()
	 * @generated
	 */
	EAttribute getIAssociationEnd_Aggregation();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IAssociationEnd#getChangeable <em>Changeable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Changeable</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IAssociationEnd#getChangeable()
	 * @see #getIAssociationEnd()
	 * @generated
	 */
	EAttribute getIAssociationEnd_Changeable();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IAssociationEnd#isNavigable <em>Navigable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Navigable</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IAssociationEnd#isNavigable()
	 * @see #getIAssociationEnd()
	 * @generated
	 */
	EAttribute getIAssociationEnd_Navigable();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IAssociationEnd#isOrdered <em>Ordered</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ordered</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IAssociationEnd#isOrdered()
	 * @see #getIAssociationEnd()
	 * @generated
	 */
	EAttribute getIAssociationEnd_Ordered();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IAssociationEnd#isUnique <em>Unique</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Unique</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IAssociationEnd#isUnique()
	 * @see #getIAssociationEnd()
	 * @generated
	 */
	EAttribute getIAssociationEnd_Unique();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.metamodel.IAssociationEnd#getMultiplicity <em>Multiplicity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Multiplicity</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IAssociationEnd#getMultiplicity()
	 * @see #getIAssociationEnd()
	 * @generated
	 */
	EReference getIAssociationEnd_Multiplicity();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.metamodel.IAssociationEnd#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Type</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IAssociationEnd#getType()
	 * @see #getIAssociationEnd()
	 * @generated
	 */
	EReference getIAssociationEnd_Type();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.IArgument <em>IArgument</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IArgument</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IArgument
	 * @generated
	 */
	EClass getIArgument();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.tigerstripe.metamodel.IArgument#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Type</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IArgument#getType()
	 * @see #getIArgument()
	 * @generated
	 */
	EReference getIArgument_Type();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IArgument#getDefaultValue <em>Default Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Value</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IArgument#getDefaultValue()
	 * @see #getIArgument()
	 * @generated
	 */
	EAttribute getIArgument_DefaultValue();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IArgument#isOrdered <em>Ordered</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ordered</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IArgument#isOrdered()
	 * @see #getIArgument()
	 * @generated
	 */
	EAttribute getIArgument_Ordered();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IArgument#isUnique <em>Unique</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Unique</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IArgument#isUnique()
	 * @see #getIArgument()
	 * @generated
	 */
	EAttribute getIArgument_Unique();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IArgument#getRefBy <em>Ref By</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ref By</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IArgument#getRefBy()
	 * @see #getIArgument()
	 * @generated
	 */
	EAttribute getIArgument_RefBy();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.IModel <em>IModel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IModel</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IModel
	 * @generated
	 */
	EClass getIModel();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.metamodel.IModel#getPackages <em>Packages</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Packages</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IModel#getPackages()
	 * @see #getIModel()
	 * @generated
	 */
	EReference getIModel_Packages();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.IPackage <em>IPackage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IPackage</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IPackage
	 * @generated
	 */
	EClass getIPackage();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.metamodel.IPackage#getArtifacts <em>Artifacts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Artifacts</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IPackage#getArtifacts()
	 * @see #getIPackage()
	 * @generated
	 */
	EReference getIPackage_Artifacts();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IPackage#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IPackage#getName()
	 * @see #getIPackage()
	 * @generated
	 */
	EAttribute getIPackage_Name();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.IStereotypeCapable <em>IStereotype Capable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IStereotype Capable</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IStereotypeCapable
	 * @generated
	 */
	EClass getIStereotypeCapable();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.metamodel.IStereotypeCapable#getStereotypeInstances <em>Stereotype Instances</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Stereotype Instances</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IStereotypeCapable#getStereotypeInstances()
	 * @see #getIStereotypeCapable()
	 * @generated
	 */
	EReference getIStereotypeCapable_StereotypeInstances();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.IStereotypeInstance <em>IStereotype Instance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IStereotype Instance</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IStereotypeInstance
	 * @generated
	 */
	EClass getIStereotypeInstance();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IStereotypeInstance#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IStereotypeInstance#getName()
	 * @see #getIStereotypeInstance()
	 * @generated
	 */
	EAttribute getIStereotypeInstance_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.metamodel.IStereotypeInstance#getAttributeValues <em>Attribute Values</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Attribute Values</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IStereotypeInstance#getAttributeValues()
	 * @see #getIStereotypeInstance()
	 * @generated
	 */
	EReference getIStereotypeInstance_AttributeValues();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.IStereotypeAttributeValue <em>IStereotype Attribute Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IStereotype Attribute Value</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IStereotypeAttributeValue
	 * @generated
	 */
	EClass getIStereotypeAttributeValue();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IStereotypeAttributeValue#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IStereotypeAttributeValue#getName()
	 * @see #getIStereotypeAttributeValue()
	 * @generated
	 */
	EAttribute getIStereotypeAttributeValue_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IStereotypeAttributeValue#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IStereotypeAttributeValue#getValue()
	 * @see #getIStereotypeAttributeValue()
	 * @generated
	 */
	EAttribute getIStereotypeAttributeValue_Value();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails <em>IEntity Method Flavor Details</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IEntity Method Flavor Details</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails
	 * @generated
	 */
	EClass getIEntityMethodFlavorDetails();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails#getComment <em>Comment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Comment</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails#getComment()
	 * @see #getIEntityMethodFlavorDetails()
	 * @generated
	 */
	EAttribute getIEntityMethodFlavorDetails_Comment();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails#getFlag <em>Flag</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Flag</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails#getFlag()
	 * @see #getIEntityMethodFlavorDetails()
	 * @generated
	 */
	EAttribute getIEntityMethodFlavorDetails_Flag();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails#getExceptions <em>Exceptions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Exceptions</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails#getExceptions()
	 * @see #getIEntityMethodFlavorDetails()
	 * @generated
	 */
	EReference getIEntityMethodFlavorDetails_Exceptions();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails#getMethod <em>Method</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Method</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails#getMethod()
	 * @see #getIEntityMethodFlavorDetails()
	 * @generated
	 */
	EReference getIEntityMethodFlavorDetails_Method();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails#getFlavor <em>Flavor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Flavor</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails#getFlavor()
	 * @see #getIEntityMethodFlavorDetails()
	 * @generated
	 */
	EAttribute getIEntityMethodFlavorDetails_Flavor();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails#getMethodType <em>Method Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Method Type</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails#getMethodType()
	 * @see #getIEntityMethodFlavorDetails()
	 * @generated
	 */
	EAttribute getIEntityMethodFlavorDetails_MethodType();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.metamodel.IMultiplicity <em>IMultiplicity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IMultiplicity</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IMultiplicity
	 * @generated
	 */
	EClass getIMultiplicity();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IMultiplicity#getLowerBound <em>Lower Bound</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lower Bound</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IMultiplicity#getLowerBound()
	 * @see #getIMultiplicity()
	 * @generated
	 */
	EAttribute getIMultiplicity_LowerBound();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.metamodel.IMultiplicity#getUpperBound <em>Upper Bound</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Upper Bound</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.IMultiplicity#getUpperBound()
	 * @see #getIMultiplicity()
	 * @generated
	 */
	EAttribute getIMultiplicity_UpperBound();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.tigerstripe.metamodel.ERefByEnum <em>ERef By Enum</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>ERef By Enum</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.ERefByEnum
	 * @generated
	 */
	EEnum getERefByEnum();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.tigerstripe.metamodel.VisibilityEnum <em>Visibility Enum</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Visibility Enum</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.VisibilityEnum
	 * @generated
	 */
	EEnum getVisibilityEnum();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.tigerstripe.metamodel.EAggregationEnum <em>EAggregation Enum</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>EAggregation Enum</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.EAggregationEnum
	 * @generated
	 */
	EEnum getEAggregationEnum();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.tigerstripe.metamodel.EChangeableEnum <em>EChangeable Enum</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>EChangeable Enum</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.EChangeableEnum
	 * @generated
	 */
	EEnum getEChangeableEnum();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.tigerstripe.metamodel.OssjEntityMethodFlavor <em>Ossj Entity Method Flavor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Ossj Entity Method Flavor</em>'.
	 * @see org.eclipse.tigerstripe.metamodel.OssjEntityMethodFlavor
	 * @generated
	 */
	EEnum getOssjEntityMethodFlavor();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	MetamodelFactory getMetamodelFactory();

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
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.impl.IAbstractArtifactImpl <em>IAbstract Artifact</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.impl.IAbstractArtifactImpl
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIAbstractArtifact()
		 * @generated
		 */
		EClass IABSTRACT_ARTIFACT = eINSTANCE.getIAbstractArtifact();

		/**
		 * The meta object literal for the '<em><b>Fields</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IABSTRACT_ARTIFACT__FIELDS = eINSTANCE.getIAbstractArtifact_Fields();

		/**
		 * The meta object literal for the '<em><b>Methods</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IABSTRACT_ARTIFACT__METHODS = eINSTANCE.getIAbstractArtifact_Methods();

		/**
		 * The meta object literal for the '<em><b>Literals</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IABSTRACT_ARTIFACT__LITERALS = eINSTANCE.getIAbstractArtifact_Literals();

		/**
		 * The meta object literal for the '<em><b>Abstract</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IABSTRACT_ARTIFACT__ABSTRACT = eINSTANCE.getIAbstractArtifact_Abstract();

		/**
		 * The meta object literal for the '<em><b>Extended Artifact</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IABSTRACT_ARTIFACT__EXTENDED_ARTIFACT = eINSTANCE.getIAbstractArtifact_ExtendedArtifact();

		/**
		 * The meta object literal for the '<em><b>Implemented Artifacts</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IABSTRACT_ARTIFACT__IMPLEMENTED_ARTIFACTS = eINSTANCE.getIAbstractArtifact_ImplementedArtifacts();

		/**
		 * The meta object literal for the '<em><b>Standard Specifics</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IABSTRACT_ARTIFACT__STANDARD_SPECIFICS = eINSTANCE.getIAbstractArtifact_StandardSpecifics();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.impl.IPrimitiveTypeImpl <em>IPrimitive Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.impl.IPrimitiveTypeImpl
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIPrimitiveType()
		 * @generated
		 */
		EClass IPRIMITIVE_TYPE = eINSTANCE.getIPrimitiveType();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.impl.IManagedEntityArtifactImpl <em>IManaged Entity Artifact</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.impl.IManagedEntityArtifactImpl
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIManagedEntityArtifact()
		 * @generated
		 */
		EClass IMANAGED_ENTITY_ARTIFACT = eINSTANCE.getIManagedEntityArtifact();

		/**
		 * The meta object literal for the '<em><b>Primary Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IMANAGED_ENTITY_ARTIFACT__PRIMARY_KEY = eINSTANCE.getIManagedEntityArtifact_PrimaryKey();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.impl.IDatatypeArtifactImpl <em>IDatatype Artifact</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.impl.IDatatypeArtifactImpl
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIDatatypeArtifact()
		 * @generated
		 */
		EClass IDATATYPE_ARTIFACT = eINSTANCE.getIDatatypeArtifact();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.impl.IExceptionArtifactImpl <em>IException Artifact</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.impl.IExceptionArtifactImpl
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIExceptionArtifact()
		 * @generated
		 */
		EClass IEXCEPTION_ARTIFACT = eINSTANCE.getIExceptionArtifact();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.impl.ISessionArtifactImpl <em>ISession Artifact</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.impl.ISessionArtifactImpl
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getISessionArtifact()
		 * @generated
		 */
		EClass ISESSION_ARTIFACT = eINSTANCE.getISessionArtifact();

		/**
		 * The meta object literal for the '<em><b>Managed Entities</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ISESSION_ARTIFACT__MANAGED_ENTITIES = eINSTANCE.getISessionArtifact_ManagedEntities();

		/**
		 * The meta object literal for the '<em><b>Emitted Notifications</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ISESSION_ARTIFACT__EMITTED_NOTIFICATIONS = eINSTANCE.getISessionArtifact_EmittedNotifications();

		/**
		 * The meta object literal for the '<em><b>Supported Named Queries</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ISESSION_ARTIFACT__SUPPORTED_NAMED_QUERIES = eINSTANCE.getISessionArtifact_SupportedNamedQueries();

		/**
		 * The meta object literal for the '<em><b>Exposed Update Procedures</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ISESSION_ARTIFACT__EXPOSED_UPDATE_PROCEDURES = eINSTANCE.getISessionArtifact_ExposedUpdateProcedures();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.impl.IQueryArtifactImpl <em>IQuery Artifact</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.impl.IQueryArtifactImpl
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIQueryArtifact()
		 * @generated
		 */
		EClass IQUERY_ARTIFACT = eINSTANCE.getIQueryArtifact();

		/**
		 * The meta object literal for the '<em><b>Returned Type</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IQUERY_ARTIFACT__RETURNED_TYPE = eINSTANCE.getIQueryArtifact_ReturnedType();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.impl.IUpdateProcedureArtifactImpl <em>IUpdate Procedure Artifact</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.impl.IUpdateProcedureArtifactImpl
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIUpdateProcedureArtifact()
		 * @generated
		 */
		EClass IUPDATE_PROCEDURE_ARTIFACT = eINSTANCE.getIUpdateProcedureArtifact();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.impl.IEventArtifactImpl <em>IEvent Artifact</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.impl.IEventArtifactImpl
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIEventArtifact()
		 * @generated
		 */
		EClass IEVENT_ARTIFACT = eINSTANCE.getIEventArtifact();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.impl.IAssociationArtifactImpl <em>IAssociation Artifact</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.impl.IAssociationArtifactImpl
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIAssociationArtifact()
		 * @generated
		 */
		EClass IASSOCIATION_ARTIFACT = eINSTANCE.getIAssociationArtifact();

		/**
		 * The meta object literal for the '<em><b>AEnd</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IASSOCIATION_ARTIFACT__AEND = eINSTANCE.getIAssociationArtifact_AEnd();

		/**
		 * The meta object literal for the '<em><b>ZEnd</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IASSOCIATION_ARTIFACT__ZEND = eINSTANCE.getIAssociationArtifact_ZEnd();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.impl.IAssociationClassArtifactImpl <em>IAssociation Class Artifact</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.impl.IAssociationClassArtifactImpl
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIAssociationClassArtifact()
		 * @generated
		 */
		EClass IASSOCIATION_CLASS_ARTIFACT = eINSTANCE.getIAssociationClassArtifact();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.impl.IDependencyArtifactImpl <em>IDependency Artifact</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.impl.IDependencyArtifactImpl
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIDependencyArtifact()
		 * @generated
		 */
		EClass IDEPENDENCY_ARTIFACT = eINSTANCE.getIDependencyArtifact();

		/**
		 * The meta object literal for the '<em><b>AEnd Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IDEPENDENCY_ARTIFACT__AEND_TYPE = eINSTANCE.getIDependencyArtifact_AEndType();

		/**
		 * The meta object literal for the '<em><b>ZEnd Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IDEPENDENCY_ARTIFACT__ZEND_TYPE = eINSTANCE.getIDependencyArtifact_ZEndType();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.impl.IEnumArtifactImpl <em>IEnum Artifact</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.impl.IEnumArtifactImpl
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIEnumArtifact()
		 * @generated
		 */
		EClass IENUM_ARTIFACT = eINSTANCE.getIEnumArtifact();

		/**
		 * The meta object literal for the '<em><b>Base Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IENUM_ARTIFACT__BASE_TYPE = eINSTANCE.getIEnumArtifact_BaseType();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.impl.IFieldImpl <em>IField</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.impl.IFieldImpl
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIField()
		 * @generated
		 */
		EClass IFIELD = eINSTANCE.getIField();

		/**
		 * The meta object literal for the '<em><b>Optional</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IFIELD__OPTIONAL = eINSTANCE.getIField_Optional();

		/**
		 * The meta object literal for the '<em><b>Read Only</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IFIELD__READ_ONLY = eINSTANCE.getIField_ReadOnly();

		/**
		 * The meta object literal for the '<em><b>Ordered</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IFIELD__ORDERED = eINSTANCE.getIField_Ordered();

		/**
		 * The meta object literal for the '<em><b>Unique</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IFIELD__UNIQUE = eINSTANCE.getIField_Unique();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IFIELD__TYPE = eINSTANCE.getIField_Type();

		/**
		 * The meta object literal for the '<em><b>Default Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IFIELD__DEFAULT_VALUE = eINSTANCE.getIField_DefaultValue();

		/**
		 * The meta object literal for the '<em><b>Ref By</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IFIELD__REF_BY = eINSTANCE.getIField_RefBy();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.impl.IMethodImpl <em>IMethod</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.impl.IMethodImpl
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIMethod()
		 * @generated
		 */
		EClass IMETHOD = eINSTANCE.getIMethod();

		/**
		 * The meta object literal for the '<em><b>Arguments</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IMETHOD__ARGUMENTS = eINSTANCE.getIMethod_Arguments();

		/**
		 * The meta object literal for the '<em><b>Return Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IMETHOD__RETURN_TYPE = eINSTANCE.getIMethod_ReturnType();

		/**
		 * The meta object literal for the '<em><b>Abstract</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IMETHOD__ABSTRACT = eINSTANCE.getIMethod_Abstract();

		/**
		 * The meta object literal for the '<em><b>Ordered</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IMETHOD__ORDERED = eINSTANCE.getIMethod_Ordered();

		/**
		 * The meta object literal for the '<em><b>Unique</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IMETHOD__UNIQUE = eINSTANCE.getIMethod_Unique();

		/**
		 * The meta object literal for the '<em><b>Optional</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IMETHOD__OPTIONAL = eINSTANCE.getIMethod_Optional();

		/**
		 * The meta object literal for the '<em><b>Exceptions</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IMETHOD__EXCEPTIONS = eINSTANCE.getIMethod_Exceptions();

		/**
		 * The meta object literal for the '<em><b>Void</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IMETHOD__VOID = eINSTANCE.getIMethod_Void();

		/**
		 * The meta object literal for the '<em><b>Iterator Return</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IMETHOD__ITERATOR_RETURN = eINSTANCE.getIMethod_IteratorReturn();

		/**
		 * The meta object literal for the '<em><b>Return Ref By</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IMETHOD__RETURN_REF_BY = eINSTANCE.getIMethod_ReturnRefBy();

		/**
		 * The meta object literal for the '<em><b>Instance Method</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IMETHOD__INSTANCE_METHOD = eINSTANCE.getIMethod_InstanceMethod();

		/**
		 * The meta object literal for the '<em><b>Default Return Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IMETHOD__DEFAULT_RETURN_VALUE = eINSTANCE.getIMethod_DefaultReturnValue();

		/**
		 * The meta object literal for the '<em><b>Method Return Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IMETHOD__METHOD_RETURN_NAME = eINSTANCE.getIMethod_MethodReturnName();

		/**
		 * The meta object literal for the '<em><b>Return Stereotype Instances</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IMETHOD__RETURN_STEREOTYPE_INSTANCES = eINSTANCE.getIMethod_ReturnStereotypeInstances();

		/**
		 * The meta object literal for the '<em><b>Entity Method Flavor Details</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IMETHOD__ENTITY_METHOD_FLAVOR_DETAILS = eINSTANCE.getIMethod_EntityMethodFlavorDetails();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.impl.ILiteralImpl <em>ILiteral</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.impl.ILiteralImpl
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getILiteral()
		 * @generated
		 */
		EClass ILITERAL = eINSTANCE.getILiteral();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ILITERAL__VALUE = eINSTANCE.getILiteral_Value();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ILITERAL__TYPE = eINSTANCE.getILiteral_Type();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.impl.IModelComponentImpl <em>IModel Component</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.impl.IModelComponentImpl
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIModelComponent()
		 * @generated
		 */
		EClass IMODEL_COMPONENT = eINSTANCE.getIModelComponent();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IMODEL_COMPONENT__NAME = eINSTANCE.getIModelComponent_Name();

		/**
		 * The meta object literal for the '<em><b>Comment</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IMODEL_COMPONENT__COMMENT = eINSTANCE.getIModelComponent_Comment();

		/**
		 * The meta object literal for the '<em><b>Visibility</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IMODEL_COMPONENT__VISIBILITY = eINSTANCE.getIModelComponent_Visibility();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.impl.IQualifiedNamedComponentImpl <em>IQualified Named Component</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.impl.IQualifiedNamedComponentImpl
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIQualifiedNamedComponent()
		 * @generated
		 */
		EClass IQUALIFIED_NAMED_COMPONENT = eINSTANCE.getIQualifiedNamedComponent();

		/**
		 * The meta object literal for the '<em><b>Package</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IQUALIFIED_NAMED_COMPONENT__PACKAGE = eINSTANCE.getIQualifiedNamedComponent_Package();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.impl.ITypeImpl <em>IType</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.impl.ITypeImpl
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIType()
		 * @generated
		 */
		EClass ITYPE = eINSTANCE.getIType();

		/**
		 * The meta object literal for the '<em><b>Fully Qualified Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ITYPE__FULLY_QUALIFIED_NAME = eINSTANCE.getIType_FullyQualifiedName();

		/**
		 * The meta object literal for the '<em><b>Multiplicity</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ITYPE__MULTIPLICITY = eINSTANCE.getIType_Multiplicity();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.impl.IAssociationEndImpl <em>IAssociation End</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.impl.IAssociationEndImpl
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIAssociationEnd()
		 * @generated
		 */
		EClass IASSOCIATION_END = eINSTANCE.getIAssociationEnd();

		/**
		 * The meta object literal for the '<em><b>Aggregation</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IASSOCIATION_END__AGGREGATION = eINSTANCE.getIAssociationEnd_Aggregation();

		/**
		 * The meta object literal for the '<em><b>Changeable</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IASSOCIATION_END__CHANGEABLE = eINSTANCE.getIAssociationEnd_Changeable();

		/**
		 * The meta object literal for the '<em><b>Navigable</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IASSOCIATION_END__NAVIGABLE = eINSTANCE.getIAssociationEnd_Navigable();

		/**
		 * The meta object literal for the '<em><b>Ordered</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IASSOCIATION_END__ORDERED = eINSTANCE.getIAssociationEnd_Ordered();

		/**
		 * The meta object literal for the '<em><b>Unique</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IASSOCIATION_END__UNIQUE = eINSTANCE.getIAssociationEnd_Unique();

		/**
		 * The meta object literal for the '<em><b>Multiplicity</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IASSOCIATION_END__MULTIPLICITY = eINSTANCE.getIAssociationEnd_Multiplicity();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IASSOCIATION_END__TYPE = eINSTANCE.getIAssociationEnd_Type();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.impl.IArgumentImpl <em>IArgument</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.impl.IArgumentImpl
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIArgument()
		 * @generated
		 */
		EClass IARGUMENT = eINSTANCE.getIArgument();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IARGUMENT__TYPE = eINSTANCE.getIArgument_Type();

		/**
		 * The meta object literal for the '<em><b>Default Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IARGUMENT__DEFAULT_VALUE = eINSTANCE.getIArgument_DefaultValue();

		/**
		 * The meta object literal for the '<em><b>Ordered</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IARGUMENT__ORDERED = eINSTANCE.getIArgument_Ordered();

		/**
		 * The meta object literal for the '<em><b>Unique</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IARGUMENT__UNIQUE = eINSTANCE.getIArgument_Unique();

		/**
		 * The meta object literal for the '<em><b>Ref By</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IARGUMENT__REF_BY = eINSTANCE.getIArgument_RefBy();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.impl.IModelImpl <em>IModel</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.impl.IModelImpl
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIModel()
		 * @generated
		 */
		EClass IMODEL = eINSTANCE.getIModel();

		/**
		 * The meta object literal for the '<em><b>Packages</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IMODEL__PACKAGES = eINSTANCE.getIModel_Packages();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.impl.IPackageImpl <em>IPackage</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.impl.IPackageImpl
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIPackage()
		 * @generated
		 */
		EClass IPACKAGE = eINSTANCE.getIPackage();

		/**
		 * The meta object literal for the '<em><b>Artifacts</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IPACKAGE__ARTIFACTS = eINSTANCE.getIPackage_Artifacts();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IPACKAGE__NAME = eINSTANCE.getIPackage_Name();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.impl.IStereotypeCapableImpl <em>IStereotype Capable</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.impl.IStereotypeCapableImpl
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIStereotypeCapable()
		 * @generated
		 */
		EClass ISTEREOTYPE_CAPABLE = eINSTANCE.getIStereotypeCapable();

		/**
		 * The meta object literal for the '<em><b>Stereotype Instances</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ISTEREOTYPE_CAPABLE__STEREOTYPE_INSTANCES = eINSTANCE.getIStereotypeCapable_StereotypeInstances();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.impl.IStereotypeInstanceImpl <em>IStereotype Instance</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.impl.IStereotypeInstanceImpl
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIStereotypeInstance()
		 * @generated
		 */
		EClass ISTEREOTYPE_INSTANCE = eINSTANCE.getIStereotypeInstance();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ISTEREOTYPE_INSTANCE__NAME = eINSTANCE.getIStereotypeInstance_Name();

		/**
		 * The meta object literal for the '<em><b>Attribute Values</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ISTEREOTYPE_INSTANCE__ATTRIBUTE_VALUES = eINSTANCE.getIStereotypeInstance_AttributeValues();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.impl.IStereotypeAttributeValueImpl <em>IStereotype Attribute Value</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.impl.IStereotypeAttributeValueImpl
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIStereotypeAttributeValue()
		 * @generated
		 */
		EClass ISTEREOTYPE_ATTRIBUTE_VALUE = eINSTANCE.getIStereotypeAttributeValue();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ISTEREOTYPE_ATTRIBUTE_VALUE__NAME = eINSTANCE.getIStereotypeAttributeValue_Name();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ISTEREOTYPE_ATTRIBUTE_VALUE__VALUE = eINSTANCE.getIStereotypeAttributeValue_Value();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.impl.IEntityMethodFlavorDetailsImpl <em>IEntity Method Flavor Details</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.impl.IEntityMethodFlavorDetailsImpl
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIEntityMethodFlavorDetails()
		 * @generated
		 */
		EClass IENTITY_METHOD_FLAVOR_DETAILS = eINSTANCE.getIEntityMethodFlavorDetails();

		/**
		 * The meta object literal for the '<em><b>Comment</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IENTITY_METHOD_FLAVOR_DETAILS__COMMENT = eINSTANCE.getIEntityMethodFlavorDetails_Comment();

		/**
		 * The meta object literal for the '<em><b>Flag</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IENTITY_METHOD_FLAVOR_DETAILS__FLAG = eINSTANCE.getIEntityMethodFlavorDetails_Flag();

		/**
		 * The meta object literal for the '<em><b>Exceptions</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IENTITY_METHOD_FLAVOR_DETAILS__EXCEPTIONS = eINSTANCE.getIEntityMethodFlavorDetails_Exceptions();

		/**
		 * The meta object literal for the '<em><b>Method</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IENTITY_METHOD_FLAVOR_DETAILS__METHOD = eINSTANCE.getIEntityMethodFlavorDetails_Method();

		/**
		 * The meta object literal for the '<em><b>Flavor</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IENTITY_METHOD_FLAVOR_DETAILS__FLAVOR = eINSTANCE.getIEntityMethodFlavorDetails_Flavor();

		/**
		 * The meta object literal for the '<em><b>Method Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IENTITY_METHOD_FLAVOR_DETAILS__METHOD_TYPE = eINSTANCE.getIEntityMethodFlavorDetails_MethodType();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.impl.IMultiplicityImpl <em>IMultiplicity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.impl.IMultiplicityImpl
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getIMultiplicity()
		 * @generated
		 */
		EClass IMULTIPLICITY = eINSTANCE.getIMultiplicity();

		/**
		 * The meta object literal for the '<em><b>Lower Bound</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IMULTIPLICITY__LOWER_BOUND = eINSTANCE.getIMultiplicity_LowerBound();

		/**
		 * The meta object literal for the '<em><b>Upper Bound</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IMULTIPLICITY__UPPER_BOUND = eINSTANCE.getIMultiplicity_UpperBound();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.ERefByEnum <em>ERef By Enum</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.ERefByEnum
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getERefByEnum()
		 * @generated
		 */
		EEnum EREF_BY_ENUM = eINSTANCE.getERefByEnum();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.VisibilityEnum <em>Visibility Enum</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.VisibilityEnum
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getVisibilityEnum()
		 * @generated
		 */
		EEnum VISIBILITY_ENUM = eINSTANCE.getVisibilityEnum();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.EAggregationEnum <em>EAggregation Enum</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.EAggregationEnum
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getEAggregationEnum()
		 * @generated
		 */
		EEnum EAGGREGATION_ENUM = eINSTANCE.getEAggregationEnum();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.EChangeableEnum <em>EChangeable Enum</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.EChangeableEnum
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getEChangeableEnum()
		 * @generated
		 */
		EEnum ECHANGEABLE_ENUM = eINSTANCE.getEChangeableEnum();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.metamodel.OssjEntityMethodFlavor <em>Ossj Entity Method Flavor</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.metamodel.OssjEntityMethodFlavor
		 * @see org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl#getOssjEntityMethodFlavor()
		 * @generated
		 */
		EEnum OSSJ_ENTITY_METHOD_FLAVOR = eINSTANCE.getOssjEntityMethodFlavor();

	}

} //MetamodelPackage
