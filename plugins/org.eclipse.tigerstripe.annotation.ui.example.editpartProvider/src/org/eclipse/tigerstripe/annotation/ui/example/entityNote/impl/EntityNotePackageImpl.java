/**
 * <copyright>
 * </copyright>
 *
 * $Id: EntityNotePackageImpl.java,v 1.1 2008/07/01 08:49:23 ystrot Exp $
 */
package org.eclipse.tigerstripe.annotation.ui.example.entityNote.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.tigerstripe.annotation.ui.example.entityNote.EntityNote;
import org.eclipse.tigerstripe.annotation.ui.example.entityNote.EntityNoteFactory;
import org.eclipse.tigerstripe.annotation.ui.example.entityNote.EntityNotePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EntityNotePackageImpl extends EPackageImpl implements EntityNotePackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass entityNoteEClass = null;

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
	 * @see org.eclipse.tigerstripe.annotation.ui.example.entityNote.EntityNotePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private EntityNotePackageImpl() {
		super(eNS_URI, EntityNoteFactory.eINSTANCE);
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
	public static EntityNotePackage init() {
		if (isInited) return (EntityNotePackage)EPackage.Registry.INSTANCE.getEPackage(EntityNotePackage.eNS_URI);

		// Obtain or create and register package
		EntityNotePackageImpl theEntityNotePackage = (EntityNotePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof EntityNotePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new EntityNotePackageImpl());

		isInited = true;

		// Create package meta-data objects
		theEntityNotePackage.createPackageContents();

		// Initialize created meta-data
		theEntityNotePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theEntityNotePackage.freeze();

		return theEntityNotePackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEntityNote() {
		return entityNoteEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEntityNote_Description() {
		return (EAttribute)entityNoteEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntityNoteFactory getEntityNoteFactory() {
		return (EntityNoteFactory)getEFactoryInstance();
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
		entityNoteEClass = createEClass(ENTITY_NOTE);
		createEAttribute(entityNoteEClass, ENTITY_NOTE__DESCRIPTION);
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
		initEClass(entityNoteEClass, EntityNote.class, "EntityNote", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEntityNote_Description(), ecorePackage.getEString(), "description", null, 0, 1, EntityNote.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //EntityNotePackageImpl
