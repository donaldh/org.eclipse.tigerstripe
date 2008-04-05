/**
 * <copyright>
 * </copyright>
 *
 * $Id: DownloadSitePackageImpl.java,v 1.1 2008/04/05 14:00:36 edillon Exp $
 */
package org.eclipse.tigerstripe.releng.downloadsite.schema.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.eclipse.tigerstripe.releng.downloadsite.schema.Build;
import org.eclipse.tigerstripe.releng.downloadsite.schema.BuildType;
import org.eclipse.tigerstripe.releng.downloadsite.schema.Bundle;
import org.eclipse.tigerstripe.releng.downloadsite.schema.Component;
import org.eclipse.tigerstripe.releng.downloadsite.schema.Dependency;
import org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSite;
import org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSiteElement;
import org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSiteFactory;
import org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSitePackage;

import org.eclipse.tigerstripe.releng.downloadsite.schema.util.DownloadSiteValidator;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DownloadSitePackageImpl extends EPackageImpl implements DownloadSitePackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass buildEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass bundleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass componentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dependencyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass downloadSiteEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass downloadSiteElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum buildTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType buildTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType tStampEDataType = null;

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
	 * @see org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSitePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private DownloadSitePackageImpl() {
		super(eNS_URI, DownloadSiteFactory.eINSTANCE);
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
	public static DownloadSitePackage init() {
		if (isInited) return (DownloadSitePackage)EPackage.Registry.INSTANCE.getEPackage(DownloadSitePackage.eNS_URI);

		// Obtain or create and register package
		DownloadSitePackageImpl theDownloadSitePackage = (DownloadSitePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof DownloadSitePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new DownloadSitePackageImpl());

		isInited = true;

		// Initialize simple dependencies
		XMLTypePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theDownloadSitePackage.createPackageContents();

		// Initialize created meta-data
		theDownloadSitePackage.initializePackageContents();

		// Register package validator
		EValidator.Registry.INSTANCE.put
			(theDownloadSitePackage, 
			 new EValidator.Descriptor() {
				 public EValidator getEValidator() {
					 return DownloadSiteValidator.INSTANCE;
				 }
			 });

		// Mark meta-data to indicate it can't be changed
		theDownloadSitePackage.freeze();

		return theDownloadSitePackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBuild() {
		return buildEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBuild_Component() {
		return (EReference)buildEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBuild_Dependency() {
		return (EReference)buildEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBuild_Stream() {
		return (EAttribute)buildEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBuild_Tstamp() {
		return (EAttribute)buildEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBuild_Type() {
		return (EAttribute)buildEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBundle() {
		return bundleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBundle_Link() {
		return (EAttribute)bundleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getComponent() {
		return componentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getComponent_Bundle() {
		return (EReference)componentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDependency() {
		return dependencyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDownloadSite() {
		return downloadSiteEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDownloadSite_Group() {
		return (EAttribute)downloadSiteEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDownloadSite_Build() {
		return (EReference)downloadSiteEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDownloadSiteElement() {
		return downloadSiteElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDownloadSiteElement_Description() {
		return (EAttribute)downloadSiteElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDownloadSiteElement_Name() {
		return (EAttribute)downloadSiteElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDownloadSiteElement_Summary() {
		return (EAttribute)downloadSiteElementEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getBuildType() {
		return buildTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getBuildTypeObject() {
		return buildTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getTStamp() {
		return tStampEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DownloadSiteFactory getDownloadSiteFactory() {
		return (DownloadSiteFactory)getEFactoryInstance();
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
		buildEClass = createEClass(BUILD);
		createEReference(buildEClass, BUILD__COMPONENT);
		createEReference(buildEClass, BUILD__DEPENDENCY);
		createEAttribute(buildEClass, BUILD__STREAM);
		createEAttribute(buildEClass, BUILD__TSTAMP);
		createEAttribute(buildEClass, BUILD__TYPE);

		bundleEClass = createEClass(BUNDLE);
		createEAttribute(bundleEClass, BUNDLE__LINK);

		componentEClass = createEClass(COMPONENT);
		createEReference(componentEClass, COMPONENT__BUNDLE);

		dependencyEClass = createEClass(DEPENDENCY);

		downloadSiteEClass = createEClass(DOWNLOAD_SITE);
		createEAttribute(downloadSiteEClass, DOWNLOAD_SITE__GROUP);
		createEReference(downloadSiteEClass, DOWNLOAD_SITE__BUILD);

		downloadSiteElementEClass = createEClass(DOWNLOAD_SITE_ELEMENT);
		createEAttribute(downloadSiteElementEClass, DOWNLOAD_SITE_ELEMENT__DESCRIPTION);
		createEAttribute(downloadSiteElementEClass, DOWNLOAD_SITE_ELEMENT__NAME);
		createEAttribute(downloadSiteElementEClass, DOWNLOAD_SITE_ELEMENT__SUMMARY);

		// Create enums
		buildTypeEEnum = createEEnum(BUILD_TYPE);

		// Create data types
		buildTypeObjectEDataType = createEDataType(BUILD_TYPE_OBJECT);
		tStampEDataType = createEDataType(TSTAMP);
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
		XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		buildEClass.getESuperTypes().add(this.getDownloadSiteElement());
		bundleEClass.getESuperTypes().add(this.getDownloadSiteElement());
		componentEClass.getESuperTypes().add(this.getDownloadSiteElement());
		dependencyEClass.getESuperTypes().add(this.getBundle());
		downloadSiteEClass.getESuperTypes().add(this.getDownloadSiteElement());

		// Initialize classes and features; add operations and parameters
		initEClass(buildEClass, Build.class, "Build", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBuild_Component(), this.getComponent(), null, "component", null, 0, -1, Build.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBuild_Dependency(), this.getDependency(), null, "dependency", null, 0, -1, Build.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBuild_Stream(), theXMLTypePackage.getString(), "stream", null, 0, 1, Build.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBuild_Tstamp(), this.getTStamp(), "tstamp", null, 0, 1, Build.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBuild_Type(), this.getBuildType(), "type", "R", 0, 1, Build.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(bundleEClass, Bundle.class, "Bundle", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getBundle_Link(), theXMLTypePackage.getString(), "link", null, 0, 1, Bundle.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(componentEClass, Component.class, "Component", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getComponent_Bundle(), this.getBundle(), null, "bundle", null, 0, -1, Component.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dependencyEClass, Dependency.class, "Dependency", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(downloadSiteEClass, DownloadSite.class, "DownloadSite", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDownloadSite_Group(), ecorePackage.getEFeatureMapEntry(), "group", null, 0, -1, DownloadSite.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDownloadSite_Build(), this.getBuild(), null, "build", null, 0, -1, DownloadSite.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(downloadSiteElementEClass, DownloadSiteElement.class, "DownloadSiteElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDownloadSiteElement_Description(), theXMLTypePackage.getString(), "description", null, 1, 1, DownloadSiteElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDownloadSiteElement_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, DownloadSiteElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDownloadSiteElement_Summary(), theXMLTypePackage.getString(), "summary", null, 0, 1, DownloadSiteElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(buildTypeEEnum, BuildType.class, "BuildType");
		addEEnumLiteral(buildTypeEEnum, BuildType.R);
		addEEnumLiteral(buildTypeEEnum, BuildType.M);
		addEEnumLiteral(buildTypeEEnum, BuildType.I);
		addEEnumLiteral(buildTypeEEnum, BuildType.N);

		// Initialize data types
		initEDataType(buildTypeObjectEDataType, BuildType.class, "BuildTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(tStampEDataType, String.class, "TStamp", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createExtendedMetaDataAnnotations() {
		String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";				
		addAnnotation
		  (buildEClass, 
		   source, 
		   new String[] {
			 "name", "build",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getBuild_Component(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "component",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getBuild_Dependency(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "dependency",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getBuild_Stream(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "stream"
		   });		
		addAnnotation
		  (getBuild_Tstamp(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "tstamp"
		   });		
		addAnnotation
		  (getBuild_Type(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "type"
		   });			
		addAnnotation
		  (buildTypeEEnum, 
		   source, 
		   new String[] {
			 "name", "buildType"
		   });		
		addAnnotation
		  (buildTypeObjectEDataType, 
		   source, 
		   new String[] {
			 "name", "buildType:Object",
			 "baseType", "buildType"
		   });		
		addAnnotation
		  (bundleEClass, 
		   source, 
		   new String[] {
			 "name", "bundle",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getBundle_Link(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "link"
		   });		
		addAnnotation
		  (componentEClass, 
		   source, 
		   new String[] {
			 "name", "component",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getComponent_Bundle(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "bundle",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (dependencyEClass, 
		   source, 
		   new String[] {
			 "name", "dependency",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (downloadSiteEClass, 
		   source, 
		   new String[] {
			 "name", "downloadSite",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getDownloadSite_Group(), 
		   source, 
		   new String[] {
			 "kind", "group",
			 "name", "group:3"
		   });		
		addAnnotation
		  (getDownloadSite_Build(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "build",
			 "namespace", "##targetNamespace",
			 "group", "#group:3"
		   });			
		addAnnotation
		  (downloadSiteElementEClass, 
		   source, 
		   new String[] {
			 "name", "downloadSiteElement",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getDownloadSiteElement_Description(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "description",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDownloadSiteElement_Name(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "name"
		   });		
		addAnnotation
		  (getDownloadSiteElement_Summary(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "summary"
		   });		
		addAnnotation
		  (tStampEDataType, 
		   source, 
		   new String[] {
			 "name", "tStamp",
			 "baseType", "http://www.eclipse.org/emf/2003/XMLType#string",
			 "pattern", "\\d{12}"
		   });
	}

} //DownloadSitePackageImpl
