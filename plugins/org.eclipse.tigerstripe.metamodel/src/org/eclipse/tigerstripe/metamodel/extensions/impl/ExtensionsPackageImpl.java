/**
 * <copyright>
 * </copyright>
 *
 * $Id: ExtensionsPackageImpl.java,v 1.1 2008/02/14 23:58:01 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.extensions.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.tigerstripe.metamodel.MetamodelPackage;

import org.eclipse.tigerstripe.metamodel.extensions.ExtensionsFactory;
import org.eclipse.tigerstripe.metamodel.extensions.ExtensionsPackage;
import org.eclipse.tigerstripe.metamodel.extensions.IProperties;
import org.eclipse.tigerstripe.metamodel.extensions.IProperty;
import org.eclipse.tigerstripe.metamodel.extensions.IStandardSpecifics;

import org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage;

import org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.OssjPackageImpl;

import org.eclipse.tigerstripe.metamodel.impl.MetamodelPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ExtensionsPackageImpl extends EPackageImpl implements ExtensionsPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iStandardSpecificsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iPropertiesEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iPropertyEClass = null;

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
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ExtensionsPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ExtensionsPackageImpl() {
		super(eNS_URI, ExtensionsFactory.eINSTANCE);
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
	public static ExtensionsPackage init() {
		if (isInited) return (ExtensionsPackage)EPackage.Registry.INSTANCE.getEPackage(ExtensionsPackage.eNS_URI);

		// Obtain or create and register package
		ExtensionsPackageImpl theExtensionsPackage = (ExtensionsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof ExtensionsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new ExtensionsPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		MetamodelPackageImpl theMetamodelPackage = (MetamodelPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(MetamodelPackage.eNS_URI) instanceof MetamodelPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(MetamodelPackage.eNS_URI) : MetamodelPackage.eINSTANCE);
		OssjPackageImpl theOssjPackage = (OssjPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(OssjPackage.eNS_URI) instanceof OssjPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(OssjPackage.eNS_URI) : OssjPackage.eINSTANCE);

		// Create package meta-data objects
		theExtensionsPackage.createPackageContents();
		theMetamodelPackage.createPackageContents();
		theOssjPackage.createPackageContents();

		// Initialize created meta-data
		theExtensionsPackage.initializePackageContents();
		theMetamodelPackage.initializePackageContents();
		theOssjPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theExtensionsPackage.freeze();

		return theExtensionsPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIStandardSpecifics() {
		return iStandardSpecificsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIProperties() {
		return iPropertiesEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIProperties_Entries() {
		return (EReference)iPropertiesEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIProperty() {
		return iPropertyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIProperty_Name() {
		return (EAttribute)iPropertyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIProperty_Value() {
		return (EAttribute)iPropertyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExtensionsFactory getExtensionsFactory() {
		return (ExtensionsFactory)getEFactoryInstance();
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
		iStandardSpecificsEClass = createEClass(ISTANDARD_SPECIFICS);

		iPropertiesEClass = createEClass(IPROPERTIES);
		createEReference(iPropertiesEClass, IPROPERTIES__ENTRIES);

		iPropertyEClass = createEClass(IPROPERTY);
		createEAttribute(iPropertyEClass, IPROPERTY__NAME);
		createEAttribute(iPropertyEClass, IPROPERTY__VALUE);
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
		OssjPackage theOssjPackage = (OssjPackage)EPackage.Registry.INSTANCE.getEPackage(OssjPackage.eNS_URI);

		// Add subpackages
		getESubpackages().add(theOssjPackage);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(iStandardSpecificsEClass, IStandardSpecifics.class, "IStandardSpecifics", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(iPropertiesEClass, IProperties.class, "IProperties", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getIProperties_Entries(), this.getIProperty(), null, "entries", null, 0, 1, IProperties.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iPropertyEClass, IProperty.class, "IProperty", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIProperty_Name(), ecorePackage.getEString(), "name", null, 0, 1, IProperty.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIProperty_Value(), ecorePackage.getEString(), "value", null, 0, 1, IProperty.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
	}

} //ExtensionsPackageImpl
