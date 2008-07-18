/**
 * <copyright>
 * </copyright>
 *
 * $Id: SomeTestAnnotsPackageImpl.java,v 1.1 2008/07/18 01:07:18 jworrell Exp $
 */
package org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.SomeTestAnnotsFactory;
import org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.SomeTestAnnotsPackage;
import org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.TestAnnot1;
import org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.TestAnnot2;
import org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.TestAnnot3;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SomeTestAnnotsPackageImpl extends EPackageImpl implements SomeTestAnnotsPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass testAnnot1EClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass testAnnot2EClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass testAnnot3EClass = null;

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
	 * @see org.eclipse.tigerstripe.annotation.setif.SomeTestAnnots.SomeTestAnnotsPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private SomeTestAnnotsPackageImpl() {
		super(eNS_URI, SomeTestAnnotsFactory.eINSTANCE);
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
	public static SomeTestAnnotsPackage init() {
		if (isInited) return (SomeTestAnnotsPackage)EPackage.Registry.INSTANCE.getEPackage(SomeTestAnnotsPackage.eNS_URI);

		// Obtain or create and register package
		SomeTestAnnotsPackageImpl theSomeTestAnnotsPackage = (SomeTestAnnotsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof SomeTestAnnotsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new SomeTestAnnotsPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theSomeTestAnnotsPackage.createPackageContents();

		// Initialize created meta-data
		theSomeTestAnnotsPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theSomeTestAnnotsPackage.freeze();

		return theSomeTestAnnotsPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTestAnnot1() {
		return testAnnot1EClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTestAnnot1_Twine() {
		return (EAttribute)testAnnot1EClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTestAnnot2() {
		return testAnnot2EClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTestAnnot3() {
		return testAnnot3EClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTestAnnot3_N() {
		return (EAttribute)testAnnot3EClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SomeTestAnnotsFactory getSomeTestAnnotsFactory() {
		return (SomeTestAnnotsFactory)getEFactoryInstance();
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
		testAnnot1EClass = createEClass(TEST_ANNOT1);
		createEAttribute(testAnnot1EClass, TEST_ANNOT1__TWINE);

		testAnnot2EClass = createEClass(TEST_ANNOT2);

		testAnnot3EClass = createEClass(TEST_ANNOT3);
		createEAttribute(testAnnot3EClass, TEST_ANNOT3__N);
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

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(testAnnot1EClass, TestAnnot1.class, "TestAnnot1", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTestAnnot1_Twine(), ecorePackage.getEString(), "twine", null, 0, 1, TestAnnot1.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(testAnnot2EClass, TestAnnot2.class, "TestAnnot2", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(testAnnot3EClass, TestAnnot3.class, "TestAnnot3", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTestAnnot3_N(), ecorePackage.getEInt(), "n", null, 0, 1, TestAnnot3.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //SomeTestAnnotsPackageImpl
