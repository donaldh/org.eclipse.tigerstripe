/**
 * <copyright>
 * </copyright>
 *
 * $Id: DownloadSitePackage.java,v 1.3 2008/04/30 21:37:10 edillon Exp $
 */
package org.eclipse.tigerstripe.releng.downloadsite.schema;

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
 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSiteFactory
 * @model kind="package"
 * @generated
 */
public interface DownloadSitePackage extends EPackage {
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
	DownloadSitePackage eINSTANCE = org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSitePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSiteElementImpl <em>Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSiteElementImpl
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSitePackageImpl#getDownloadSiteElement()
	 * @generated
	 */
	int DOWNLOAD_SITE_ELEMENT = 6;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOWNLOAD_SITE_ELEMENT__DESCRIPTION = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOWNLOAD_SITE_ELEMENT__NAME = 1;

	/**
	 * The feature id for the '<em><b>Summary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOWNLOAD_SITE_ELEMENT__SUMMARY = 2;

	/**
	 * The number of structural features of the '<em>Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOWNLOAD_SITE_ELEMENT_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.impl.BuildImpl <em>Build</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.BuildImpl
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSitePackageImpl#getBuild()
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
	int BUILD__DESCRIPTION = DOWNLOAD_SITE_ELEMENT__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUILD__NAME = DOWNLOAD_SITE_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Summary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUILD__SUMMARY = DOWNLOAD_SITE_ELEMENT__SUMMARY;

	/**
	 * The feature id for the '<em><b>Component</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUILD__COMPONENT = DOWNLOAD_SITE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Dependency</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUILD__DEPENDENCY = DOWNLOAD_SITE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Detail</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUILD__DETAIL = DOWNLOAD_SITE_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Stream</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUILD__STREAM = DOWNLOAD_SITE_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Tstamp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUILD__TSTAMP = DOWNLOAD_SITE_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUILD__TYPE = DOWNLOAD_SITE_ELEMENT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Build</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUILD_FEATURE_COUNT = DOWNLOAD_SITE_ELEMENT_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.impl.BundleImpl <em>Bundle</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.BundleImpl
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSitePackageImpl#getBundle()
	 * @generated
	 */
	int BUNDLE = 1;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUNDLE__DESCRIPTION = DOWNLOAD_SITE_ELEMENT__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUNDLE__NAME = DOWNLOAD_SITE_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Summary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUNDLE__SUMMARY = DOWNLOAD_SITE_ELEMENT__SUMMARY;

	/**
	 * The feature id for the '<em><b>Link</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUNDLE__LINK = DOWNLOAD_SITE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUNDLE__SIZE = DOWNLOAD_SITE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Bundle</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUNDLE_FEATURE_COUNT = DOWNLOAD_SITE_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.impl.ComponentImpl <em>Component</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.ComponentImpl
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSitePackageImpl#getComponent()
	 * @generated
	 */
	int COMPONENT = 2;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT__DESCRIPTION = DOWNLOAD_SITE_ELEMENT__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT__NAME = DOWNLOAD_SITE_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Summary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT__SUMMARY = DOWNLOAD_SITE_ELEMENT__SUMMARY;

	/**
	 * The feature id for the '<em><b>Bundle</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT__BUNDLE = DOWNLOAD_SITE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Junit Results URL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT__JUNIT_RESULTS_URL = DOWNLOAD_SITE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Component</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_FEATURE_COUNT = DOWNLOAD_SITE_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DependencyImpl <em>Dependency</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DependencyImpl
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSitePackageImpl#getDependency()
	 * @generated
	 */
	int DEPENDENCY = 3;

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
	 * The feature id for the '<em><b>Link</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY__LINK = BUNDLE__LINK;

	/**
	 * The feature id for the '<em><b>Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY__SIZE = BUNDLE__SIZE;

	/**
	 * The number of structural features of the '<em>Dependency</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY_FEATURE_COUNT = BUNDLE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DetailImpl <em>Detail</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DetailImpl
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSitePackageImpl#getDetail()
	 * @generated
	 */
	int DETAIL = 4;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DETAIL__DESCRIPTION = BUNDLE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DETAIL__NAME = BUNDLE__NAME;

	/**
	 * The feature id for the '<em><b>Summary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DETAIL__SUMMARY = BUNDLE__SUMMARY;

	/**
	 * The feature id for the '<em><b>Link</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DETAIL__LINK = BUNDLE__LINK;

	/**
	 * The feature id for the '<em><b>Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DETAIL__SIZE = BUNDLE__SIZE;

	/**
	 * The number of structural features of the '<em>Detail</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DETAIL_FEATURE_COUNT = BUNDLE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSiteImpl <em>Download Site</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSiteImpl
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSitePackageImpl#getDownloadSite()
	 * @generated
	 */
	int DOWNLOAD_SITE = 5;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOWNLOAD_SITE__DESCRIPTION = DOWNLOAD_SITE_ELEMENT__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOWNLOAD_SITE__NAME = DOWNLOAD_SITE_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Summary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOWNLOAD_SITE__SUMMARY = DOWNLOAD_SITE_ELEMENT__SUMMARY;

	/**
	 * The feature id for the '<em><b>Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOWNLOAD_SITE__GROUP = DOWNLOAD_SITE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Build</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOWNLOAD_SITE__BUILD = DOWNLOAD_SITE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Download Site</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOWNLOAD_SITE_FEATURE_COUNT = DOWNLOAD_SITE_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.BuildType <em>Build Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.BuildType
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSitePackageImpl#getBuildType()
	 * @generated
	 */
	int BUILD_TYPE = 7;

	/**
	 * The meta object id for the '<em>Build Type Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.BuildType
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSitePackageImpl#getBuildTypeObject()
	 * @generated
	 */
	int BUILD_TYPE_OBJECT = 8;

	/**
	 * The meta object id for the '<em>TStamp</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.String
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSitePackageImpl#getTStamp()
	 * @generated
	 */
	int TSTAMP = 9;


	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Build <em>Build</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Build</em>'.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.Build
	 * @generated
	 */
	EClass getBuild();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Build#getComponent <em>Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Component</em>'.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.Build#getComponent()
	 * @see #getBuild()
	 * @generated
	 */
	EReference getBuild_Component();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Build#getDependency <em>Dependency</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Dependency</em>'.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.Build#getDependency()
	 * @see #getBuild()
	 * @generated
	 */
	EReference getBuild_Dependency();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Build#getDetail <em>Detail</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Detail</em>'.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.Build#getDetail()
	 * @see #getBuild()
	 * @generated
	 */
	EReference getBuild_Detail();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Build#getStream <em>Stream</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Stream</em>'.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.Build#getStream()
	 * @see #getBuild()
	 * @generated
	 */
	EAttribute getBuild_Stream();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Build#getTstamp <em>Tstamp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Tstamp</em>'.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.Build#getTstamp()
	 * @see #getBuild()
	 * @generated
	 */
	EAttribute getBuild_Tstamp();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Build#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.Build#getType()
	 * @see #getBuild()
	 * @generated
	 */
	EAttribute getBuild_Type();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Bundle <em>Bundle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Bundle</em>'.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.Bundle
	 * @generated
	 */
	EClass getBundle();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Bundle#getLink <em>Link</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Link</em>'.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.Bundle#getLink()
	 * @see #getBundle()
	 * @generated
	 */
	EAttribute getBundle_Link();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Bundle#getSize <em>Size</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Size</em>'.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.Bundle#getSize()
	 * @see #getBundle()
	 * @generated
	 */
	EAttribute getBundle_Size();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Component <em>Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Component</em>'.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.Component
	 * @generated
	 */
	EClass getComponent();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Component#getBundle <em>Bundle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Bundle</em>'.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.Component#getBundle()
	 * @see #getComponent()
	 * @generated
	 */
	EReference getComponent_Bundle();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Component#getJunitResultsURL <em>Junit Results URL</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Junit Results URL</em>'.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.Component#getJunitResultsURL()
	 * @see #getComponent()
	 * @generated
	 */
	EAttribute getComponent_JunitResultsURL();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Dependency <em>Dependency</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Dependency</em>'.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.Dependency
	 * @generated
	 */
	EClass getDependency();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.Detail <em>Detail</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Detail</em>'.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.Detail
	 * @generated
	 */
	EClass getDetail();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSite <em>Download Site</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Download Site</em>'.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSite
	 * @generated
	 */
	EClass getDownloadSite();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSite#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Group</em>'.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSite#getGroup()
	 * @see #getDownloadSite()
	 * @generated
	 */
	EAttribute getDownloadSite_Group();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSite#getBuild <em>Build</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Build</em>'.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSite#getBuild()
	 * @see #getDownloadSite()
	 * @generated
	 */
	EReference getDownloadSite_Build();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSiteElement <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Element</em>'.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSiteElement
	 * @generated
	 */
	EClass getDownloadSiteElement();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSiteElement#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSiteElement#getDescription()
	 * @see #getDownloadSiteElement()
	 * @generated
	 */
	EAttribute getDownloadSiteElement_Description();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSiteElement#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSiteElement#getName()
	 * @see #getDownloadSiteElement()
	 * @generated
	 */
	EAttribute getDownloadSiteElement_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSiteElement#getSummary <em>Summary</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Summary</em>'.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSiteElement#getSummary()
	 * @see #getDownloadSiteElement()
	 * @generated
	 */
	EAttribute getDownloadSiteElement_Summary();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.BuildType <em>Build Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Build Type</em>'.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.BuildType
	 * @generated
	 */
	EEnum getBuildType();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.BuildType <em>Build Type Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Build Type Object</em>'.
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.BuildType
	 * @model instanceClass="org.eclipse.tigerstripe.releng.downloadsite.schema.BuildType"
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
	DownloadSiteFactory getDownloadSiteFactory();

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
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.impl.BuildImpl <em>Build</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.BuildImpl
		 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSitePackageImpl#getBuild()
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
		 * The meta object literal for the '<em><b>Detail</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BUILD__DETAIL = eINSTANCE.getBuild_Detail();

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
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.impl.BundleImpl <em>Bundle</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.BundleImpl
		 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSitePackageImpl#getBundle()
		 * @generated
		 */
		EClass BUNDLE = eINSTANCE.getBundle();

		/**
		 * The meta object literal for the '<em><b>Link</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUNDLE__LINK = eINSTANCE.getBundle_Link();

		/**
		 * The meta object literal for the '<em><b>Size</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUNDLE__SIZE = eINSTANCE.getBundle_Size();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.impl.ComponentImpl <em>Component</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.ComponentImpl
		 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSitePackageImpl#getComponent()
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
		 * The meta object literal for the '<em><b>Junit Results URL</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPONENT__JUNIT_RESULTS_URL = eINSTANCE.getComponent_JunitResultsURL();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DependencyImpl <em>Dependency</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DependencyImpl
		 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSitePackageImpl#getDependency()
		 * @generated
		 */
		EClass DEPENDENCY = eINSTANCE.getDependency();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DetailImpl <em>Detail</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DetailImpl
		 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSitePackageImpl#getDetail()
		 * @generated
		 */
		EClass DETAIL = eINSTANCE.getDetail();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSiteImpl <em>Download Site</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSiteImpl
		 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSitePackageImpl#getDownloadSite()
		 * @generated
		 */
		EClass DOWNLOAD_SITE = eINSTANCE.getDownloadSite();

		/**
		 * The meta object literal for the '<em><b>Group</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOWNLOAD_SITE__GROUP = eINSTANCE.getDownloadSite_Group();

		/**
		 * The meta object literal for the '<em><b>Build</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOWNLOAD_SITE__BUILD = eINSTANCE.getDownloadSite_Build();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSiteElementImpl <em>Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSiteElementImpl
		 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSitePackageImpl#getDownloadSiteElement()
		 * @generated
		 */
		EClass DOWNLOAD_SITE_ELEMENT = eINSTANCE.getDownloadSiteElement();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOWNLOAD_SITE_ELEMENT__DESCRIPTION = eINSTANCE.getDownloadSiteElement_Description();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOWNLOAD_SITE_ELEMENT__NAME = eINSTANCE.getDownloadSiteElement_Name();

		/**
		 * The meta object literal for the '<em><b>Summary</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOWNLOAD_SITE_ELEMENT__SUMMARY = eINSTANCE.getDownloadSiteElement_Summary();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.releng.downloadsite.schema.BuildType <em>Build Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.BuildType
		 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSitePackageImpl#getBuildType()
		 * @generated
		 */
		EEnum BUILD_TYPE = eINSTANCE.getBuildType();

		/**
		 * The meta object literal for the '<em>Build Type Object</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.BuildType
		 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSitePackageImpl#getBuildTypeObject()
		 * @generated
		 */
		EDataType BUILD_TYPE_OBJECT = eINSTANCE.getBuildTypeObject();

		/**
		 * The meta object literal for the '<em>TStamp</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.String
		 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSitePackageImpl#getTStamp()
		 * @generated
		 */
		EDataType TSTAMP = eINSTANCE.getTStamp();

	}

} //DownloadSitePackage
