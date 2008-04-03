/**
 * <copyright>
 * </copyright>
 *
 * $Id: BuildSchemaPackage.java,v 1.1 2008/04/03 21:45:31 edillon Exp $
 */
package org.eclipse.tigerstripe.releng.ant.builds.schema;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
 * <!-- begin-model-doc -->
 * 
 * 			Builds descriptor for Tigerstripe builds.
 * 		
 * <!-- end-model-doc -->
 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.BuildSchemaFactory
 * @model kind="package"
 * @generated
 */
public interface BuildSchemaPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "schema";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/tigerstripe/schemas/BuildsSchema";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "schema";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	BuildSchemaPackage eINSTANCE = org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildSchemaPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildElementImpl <em>Build Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildElementImpl
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildSchemaPackageImpl#getBuildElement()
	 * @generated
	 */
	int BUILD_ELEMENT = 1;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUILD_ELEMENT__DESCRIPTION = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUILD_ELEMENT__NAME = 1;

	/**
	 * The feature id for the '<em><b>Summary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUILD_ELEMENT__SUMMARY = 2;

	/**
	 * The number of structural features of the '<em>Build Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUILD_ELEMENT_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildImpl <em>Build</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildImpl
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildSchemaPackageImpl#getBuild()
	 * @generated
	 */
	int BUILD = 0;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUILD__DESCRIPTION = BUILD_ELEMENT__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUILD__NAME = BUILD_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Summary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUILD__SUMMARY = BUILD_ELEMENT__SUMMARY;

	/**
	 * The feature id for the '<em><b>Component</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUILD__COMPONENT = BUILD_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Dependency</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUILD__DEPENDENCY = BUILD_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Stream</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUILD__STREAM = BUILD_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Tstamp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUILD__TSTAMP = BUILD_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUILD__TYPE = BUILD_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Build</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUILD_FEATURE_COUNT = BUILD_ELEMENT_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildsImpl <em>Builds</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildsImpl
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildSchemaPackageImpl#getBuilds()
	 * @generated
	 */
	int BUILDS = 2;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUILDS__DESCRIPTION = BUILD_ELEMENT__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUILDS__NAME = BUILD_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Summary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUILDS__SUMMARY = BUILD_ELEMENT__SUMMARY;

	/**
	 * The feature id for the '<em><b>Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUILDS__GROUP = BUILD_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Build</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUILDS__BUILD = BUILD_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Builds</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUILDS_FEATURE_COUNT = BUILD_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BundleImpl <em>Bundle</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BundleImpl
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildSchemaPackageImpl#getBundle()
	 * @generated
	 */
	int BUNDLE = 3;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUNDLE__DESCRIPTION = BUILD_ELEMENT__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUNDLE__NAME = BUILD_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Summary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUNDLE__SUMMARY = BUILD_ELEMENT__SUMMARY;

	/**
	 * The feature id for the '<em><b>Download Link</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUNDLE__DOWNLOAD_LINK = BUILD_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Bundle</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUNDLE_FEATURE_COUNT = BUILD_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.impl.ComponentImpl <em>Component</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.ComponentImpl
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildSchemaPackageImpl#getComponent()
	 * @generated
	 */
	int COMPONENT = 4;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT__DESCRIPTION = BUILD_ELEMENT__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT__NAME = BUILD_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Summary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT__SUMMARY = BUILD_ELEMENT__SUMMARY;

	/**
	 * The feature id for the '<em><b>Bundle</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT__BUNDLE = BUILD_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Component</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_FEATURE_COUNT = BUILD_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.impl.DependencyImpl <em>Dependency</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.DependencyImpl
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildSchemaPackageImpl#getDependency()
	 * @generated
	 */
	int DEPENDENCY = 5;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY__DESCRIPTION = BUNDLE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY__NAME = BUNDLE__NAME;

	/**
	 * The feature id for the '<em><b>Summary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY__SUMMARY = BUNDLE__SUMMARY;

	/**
	 * The feature id for the '<em><b>Download Link</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY__DOWNLOAD_LINK = BUNDLE__DOWNLOAD_LINK;

	/**
	 * The number of structural features of the '<em>Dependency</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY_FEATURE_COUNT = BUNDLE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.BuildType <em>Build Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.BuildType
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildSchemaPackageImpl#getBuildType()
	 * @generated
	 */
	int BUILD_TYPE = 6;

	/**
	 * The meta object id for the '<em>Build Type Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.BuildType
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildSchemaPackageImpl#getBuildTypeObject()
	 * @generated
	 */
	int BUILD_TYPE_OBJECT = 7;

	/**
	 * The meta object id for the '<em>TStamp</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.String
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildSchemaPackageImpl#getTStamp()
	 * @generated
	 */
	int TSTAMP = 8;


	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.Build <em>Build</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Build</em>'.
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.Build
	 * @generated
	 */
	EClass getBuild();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.Build#getComponent <em>Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Component</em>'.
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.Build#getComponent()
	 * @see #getBuild()
	 * @generated
	 */
	EReference getBuild_Component();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.Build#getDependency <em>Dependency</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Dependency</em>'.
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.Build#getDependency()
	 * @see #getBuild()
	 * @generated
	 */
	EReference getBuild_Dependency();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.Build#getStream <em>Stream</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Stream</em>'.
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.Build#getStream()
	 * @see #getBuild()
	 * @generated
	 */
	EAttribute getBuild_Stream();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.Build#getTstamp <em>Tstamp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Tstamp</em>'.
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.Build#getTstamp()
	 * @see #getBuild()
	 * @generated
	 */
	EAttribute getBuild_Tstamp();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.Build#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.Build#getType()
	 * @see #getBuild()
	 * @generated
	 */
	EAttribute getBuild_Type();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.BuildElement <em>Build Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Build Element</em>'.
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.BuildElement
	 * @generated
	 */
	EClass getBuildElement();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.BuildElement#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.BuildElement#getDescription()
	 * @see #getBuildElement()
	 * @generated
	 */
	EAttribute getBuildElement_Description();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.BuildElement#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.BuildElement#getName()
	 * @see #getBuildElement()
	 * @generated
	 */
	EAttribute getBuildElement_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.BuildElement#getSummary <em>Summary</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Summary</em>'.
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.BuildElement#getSummary()
	 * @see #getBuildElement()
	 * @generated
	 */
	EAttribute getBuildElement_Summary();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.Builds <em>Builds</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Builds</em>'.
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.Builds
	 * @generated
	 */
	EClass getBuilds();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.Builds#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Group</em>'.
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.Builds#getGroup()
	 * @see #getBuilds()
	 * @generated
	 */
	EAttribute getBuilds_Group();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.Builds#getBuild <em>Build</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Build</em>'.
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.Builds#getBuild()
	 * @see #getBuilds()
	 * @generated
	 */
	EReference getBuilds_Build();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.Bundle <em>Bundle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Bundle</em>'.
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.Bundle
	 * @generated
	 */
	EClass getBundle();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.Bundle#getDownloadLink <em>Download Link</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Download Link</em>'.
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.Bundle#getDownloadLink()
	 * @see #getBundle()
	 * @generated
	 */
	EAttribute getBundle_DownloadLink();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.Component <em>Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Component</em>'.
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.Component
	 * @generated
	 */
	EClass getComponent();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.Component#getBundle <em>Bundle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Bundle</em>'.
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.Component#getBundle()
	 * @see #getComponent()
	 * @generated
	 */
	EReference getComponent_Bundle();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.Dependency <em>Dependency</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Dependency</em>'.
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.Dependency
	 * @generated
	 */
	EClass getDependency();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.BuildType <em>Build Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Build Type</em>'.
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.BuildType
	 * @generated
	 */
	EEnum getBuildType();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.BuildType <em>Build Type Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Build Type Object</em>'.
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.BuildType
	 * @model instanceClass="org.eclipse.tigerstripe.releng.ant.builds.schema.BuildType"
	 *        extendedMetaData="name='buildType:Object' baseType='buildType'"
	 * @generated
	 */
	EDataType getBuildTypeObject();

	/**
	 * Returns the meta object for data type '{@link java.lang.String <em>TStamp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>TStamp</em>'.
	 * @see java.lang.String
	 * @model instanceClass="java.lang.String"
	 *        extendedMetaData="name='tStamp' baseType='http://www.eclipse.org/emf/2003/XMLType#string' pattern='\\d{12}'"
	 * @generated
	 */
	EDataType getTStamp();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	BuildSchemaFactory getBuildSchemaFactory();

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
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildImpl <em>Build</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildImpl
		 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildSchemaPackageImpl#getBuild()
		 * @generated
		 */
		EClass BUILD = eINSTANCE.getBuild();

		/**
		 * The meta object literal for the '<em><b>Component</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BUILD__COMPONENT = eINSTANCE.getBuild_Component();

		/**
		 * The meta object literal for the '<em><b>Dependency</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BUILD__DEPENDENCY = eINSTANCE.getBuild_Dependency();

		/**
		 * The meta object literal for the '<em><b>Stream</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUILD__STREAM = eINSTANCE.getBuild_Stream();

		/**
		 * The meta object literal for the '<em><b>Tstamp</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUILD__TSTAMP = eINSTANCE.getBuild_Tstamp();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUILD__TYPE = eINSTANCE.getBuild_Type();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildElementImpl <em>Build Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildElementImpl
		 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildSchemaPackageImpl#getBuildElement()
		 * @generated
		 */
		EClass BUILD_ELEMENT = eINSTANCE.getBuildElement();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUILD_ELEMENT__DESCRIPTION = eINSTANCE.getBuildElement_Description();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUILD_ELEMENT__NAME = eINSTANCE.getBuildElement_Name();

		/**
		 * The meta object literal for the '<em><b>Summary</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUILD_ELEMENT__SUMMARY = eINSTANCE.getBuildElement_Summary();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildsImpl <em>Builds</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildsImpl
		 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildSchemaPackageImpl#getBuilds()
		 * @generated
		 */
		EClass BUILDS = eINSTANCE.getBuilds();

		/**
		 * The meta object literal for the '<em><b>Group</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUILDS__GROUP = eINSTANCE.getBuilds_Group();

		/**
		 * The meta object literal for the '<em><b>Build</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BUILDS__BUILD = eINSTANCE.getBuilds_Build();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BundleImpl <em>Bundle</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BundleImpl
		 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildSchemaPackageImpl#getBundle()
		 * @generated
		 */
		EClass BUNDLE = eINSTANCE.getBundle();

		/**
		 * The meta object literal for the '<em><b>Download Link</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUNDLE__DOWNLOAD_LINK = eINSTANCE.getBundle_DownloadLink();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.impl.ComponentImpl <em>Component</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.ComponentImpl
		 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildSchemaPackageImpl#getComponent()
		 * @generated
		 */
		EClass COMPONENT = eINSTANCE.getComponent();

		/**
		 * The meta object literal for the '<em><b>Bundle</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPONENT__BUNDLE = eINSTANCE.getComponent_Bundle();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.impl.DependencyImpl <em>Dependency</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.DependencyImpl
		 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildSchemaPackageImpl#getDependency()
		 * @generated
		 */
		EClass DEPENDENCY = eINSTANCE.getDependency();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.releng.ant.builds.schema.BuildType <em>Build Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.BuildType
		 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildSchemaPackageImpl#getBuildType()
		 * @generated
		 */
		EEnum BUILD_TYPE = eINSTANCE.getBuildType();

		/**
		 * The meta object literal for the '<em>Build Type Object</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.BuildType
		 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildSchemaPackageImpl#getBuildTypeObject()
		 * @generated
		 */
		EDataType BUILD_TYPE_OBJECT = eINSTANCE.getBuildTypeObject();

		/**
		 * The meta object literal for the '<em>TStamp</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.String
		 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildSchemaPackageImpl#getTStamp()
		 * @generated
		 */
		EDataType TSTAMP = eINSTANCE.getTStamp();

	}

} //BuildSchemaPackage
