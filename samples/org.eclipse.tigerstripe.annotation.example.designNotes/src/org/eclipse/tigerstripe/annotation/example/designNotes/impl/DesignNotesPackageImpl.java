/**
 * <copyright>
 * </copyright>
 *
 * $Id: DesignNotesPackageImpl.java,v 1.1 2008/06/11 23:31:30 edillon Exp $
 */
package org.eclipse.tigerstripe.annotation.example.designNotes.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.tigerstripe.annotation.example.designNotes.DesignNotesFactory;
import org.eclipse.tigerstripe.annotation.example.designNotes.DesignNotesPackage;
import org.eclipse.tigerstripe.annotation.example.designNotes.Note;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DesignNotesPackageImpl extends EPackageImpl implements DesignNotesPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass noteEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass todoEClass = null;

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
	 * @see org.eclipse.tigerstripe.annotation.example.designNotes.DesignNotesPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private DesignNotesPackageImpl() {
		super(eNS_URI, DesignNotesFactory.eINSTANCE);
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
	public static DesignNotesPackage init() {
		if (isInited) return (DesignNotesPackage)EPackage.Registry.INSTANCE.getEPackage(DesignNotesPackage.eNS_URI);

		// Obtain or create and register package
		DesignNotesPackageImpl theDesignNotesPackage = (DesignNotesPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof DesignNotesPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new DesignNotesPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theDesignNotesPackage.createPackageContents();

		// Initialize created meta-data
		theDesignNotesPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theDesignNotesPackage.freeze();

		return theDesignNotesPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNote() {
		return noteEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNote_Text() {
		return (EAttribute)noteEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTODO() {
		return todoEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTODO_Hack() {
		return (EAttribute)todoEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTODO_Summary() {
		return (EAttribute)todoEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DesignNotesFactory getDesignNotesFactory() {
		return (DesignNotesFactory)getEFactoryInstance();
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
		noteEClass = createEClass(NOTE);
		createEAttribute(noteEClass, NOTE__TEXT);

		todoEClass = createEClass(TODO);
		createEAttribute(todoEClass, TODO__HACK);
		createEAttribute(todoEClass, TODO__SUMMARY);
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
		initEClass(noteEClass, Note.class, "Note", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNote_Text(), ecorePackage.getEString(), "Text", null, 0, 1, Note.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(todoEClass, org.eclipse.tigerstripe.annotation.example.designNotes.TODO.class, "TODO", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTODO_Hack(), ecorePackage.getEBoolean(), "hack", null, 0, 1, org.eclipse.tigerstripe.annotation.example.designNotes.TODO.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTODO_Summary(), ecorePackage.getEString(), "summary", null, 0, 1, org.eclipse.tigerstripe.annotation.example.designNotes.TODO.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// org.eclipse.tigerstripe.annotation
		createOrgAnnotations();
	}

	/**
	 * Initializes the annotations for <b>org.eclipse.tigerstripe.annotation</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createOrgAnnotations() {
		String source = "org.eclipse.tigerstripe.annotation";		
		addAnnotation
		  (getNote_Text(), 
		   source, 
		   new String[] {
			 "multiline", "true"
		   });
	}

} //DesignNotesPackageImpl
