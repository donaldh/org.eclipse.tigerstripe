/**
 * <copyright>
 * </copyright>
 *
 * $Id: ModelPackageImpl.java,v 1.7 2008/08/29 08:49:54 ystrot Exp $
 */
package org.eclipse.tigerstripe.annotation.core.test.model.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.tigerstripe.annotation.core.test.model.Author;
import org.eclipse.tigerstripe.annotation.core.test.model.CustomMonth;
import org.eclipse.tigerstripe.annotation.core.test.model.Day;
import org.eclipse.tigerstripe.annotation.core.test.model.DayList;
import org.eclipse.tigerstripe.annotation.core.test.model.Hibernate;
import org.eclipse.tigerstripe.annotation.core.test.model.MimeType;
import org.eclipse.tigerstripe.annotation.core.test.model.ModelFactory;
import org.eclipse.tigerstripe.annotation.core.test.model.ModelPackage;
import org.eclipse.tigerstripe.annotation.core.test.model.ProjectDescription;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ModelPackageImpl extends EPackageImpl implements ModelPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass authorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass hibernateEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mimeTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass projectDescriptionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dayListEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass customMonthEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum dayEEnum = null;

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
	 * @see org.eclipse.tigerstripe.annotation.core.test.model.ModelPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ModelPackageImpl() {
		super(eNS_URI, ModelFactory.eINSTANCE);
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
	public static ModelPackage init() {
		if (isInited) return (ModelPackage)EPackage.Registry.INSTANCE.getEPackage(ModelPackage.eNS_URI);

		// Obtain or create and register package
		ModelPackageImpl theModelPackage = (ModelPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof ModelPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new ModelPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theModelPackage.createPackageContents();

		// Initialize created meta-data
		theModelPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theModelPackage.freeze();

		return theModelPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAuthor() {
		return authorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAuthor_FirstName() {
		return (EAttribute)authorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAuthor_LastName() {
		return (EAttribute)authorEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAuthor_Date() {
		return (EAttribute)authorEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getHibernate() {
		return hibernateEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHibernate_Persistance() {
		return (EAttribute)hibernateEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMimeType() {
		return mimeTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMimeType_MimeType() {
		return (EAttribute)mimeTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getProjectDescription() {
		return projectDescriptionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProjectDescription_Description() {
		return (EAttribute)projectDescriptionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDayList() {
		return dayListEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDayList_Days() {
		return (EAttribute)dayListEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCustomMonth() {
		return customMonthEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCustomMonth_Weeks() {
		return (EReference)customMonthEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getDay() {
		return dayEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelFactory getModelFactory() {
		return (ModelFactory)getEFactoryInstance();
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
		authorEClass = createEClass(AUTHOR);
		createEAttribute(authorEClass, AUTHOR__FIRST_NAME);
		createEAttribute(authorEClass, AUTHOR__LAST_NAME);
		createEAttribute(authorEClass, AUTHOR__DATE);

		hibernateEClass = createEClass(HIBERNATE);
		createEAttribute(hibernateEClass, HIBERNATE__PERSISTANCE);

		mimeTypeEClass = createEClass(MIME_TYPE);
		createEAttribute(mimeTypeEClass, MIME_TYPE__MIME_TYPE);

		projectDescriptionEClass = createEClass(PROJECT_DESCRIPTION);
		createEAttribute(projectDescriptionEClass, PROJECT_DESCRIPTION__DESCRIPTION);

		dayListEClass = createEClass(DAY_LIST);
		createEAttribute(dayListEClass, DAY_LIST__DAYS);

		customMonthEClass = createEClass(CUSTOM_MONTH);
		createEReference(customMonthEClass, CUSTOM_MONTH__WEEKS);

		// Create enums
		dayEEnum = createEEnum(DAY);
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
		initEClass(authorEClass, Author.class, "Author", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAuthor_FirstName(), ecorePackage.getEString(), "firstName", null, 0, 1, Author.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAuthor_LastName(), ecorePackage.getEString(), "lastName", null, 0, 1, Author.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAuthor_Date(), ecorePackage.getEDate(), "date", null, 0, 1, Author.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(hibernateEClass, Hibernate.class, "Hibernate", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getHibernate_Persistance(), ecorePackage.getEBoolean(), "persistance", null, 0, 1, Hibernate.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(mimeTypeEClass, MimeType.class, "MimeType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMimeType_MimeType(), ecorePackage.getEString(), "mimeType", null, 0, 1, MimeType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(projectDescriptionEClass, ProjectDescription.class, "ProjectDescription", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getProjectDescription_Description(), ecorePackage.getEString(), "description", null, 0, 1, ProjectDescription.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dayListEClass, DayList.class, "DayList", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDayList_Days(), this.getDay(), "days", null, 0, -1, DayList.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(customMonthEClass, CustomMonth.class, "CustomMonth", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCustomMonth_Weeks(), this.getDayList(), null, "weeks", null, 0, -1, CustomMonth.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(dayEEnum, Day.class, "Day");
		addEEnumLiteral(dayEEnum, Day.SUNDAY);
		addEEnumLiteral(dayEEnum, Day.MONDAY);
		addEEnumLiteral(dayEEnum, Day.TUESDAY);
		addEEnumLiteral(dayEEnum, Day.WEDNESDAY);
		addEEnumLiteral(dayEEnum, Day.THURSDAY);
		addEEnumLiteral(dayEEnum, Day.FRIDAY);
		addEEnumLiteral(dayEEnum, Day.SATURDAY);

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
		  (getProjectDescription_Description(), 
		   source, 
		   new String[] {
			 "editor", "org.eclipse.ui.DefaultTextEditor"
		   });
	}

} //ModelPackageImpl
