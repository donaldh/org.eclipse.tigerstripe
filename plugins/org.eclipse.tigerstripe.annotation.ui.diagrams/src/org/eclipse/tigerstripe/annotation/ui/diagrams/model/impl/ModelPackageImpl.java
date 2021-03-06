/**
 * <copyright>
 * </copyright>
 *
 * $Id: ModelPackageImpl.java,v 1.6 2008/07/11 06:34:47 ystrot Exp $
 */
package org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.gmf.runtime.notation.NotationPackage;

import org.eclipse.tigerstripe.annotation.ui.diagrams.model.AnnotationNode;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.MetaAnnotationNode;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.MetaViewAnnotations;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.ModelFactory;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.ModelPackage;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.ViewLocationNode;

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
	private EClass annotationNodeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass metaAnnotationNodeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass metaViewAnnotationsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass viewLocationNodeEClass = null;

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
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.ModelPackage#eNS_URI
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

		// Initialize simple dependencies
		NotationPackage.eINSTANCE.eClass();
		EcorePackage.eINSTANCE.eClass();

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
	public EClass getAnnotationNode() {
		return annotationNodeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAnnotationNode_AnnotationId() {
		return (EAttribute)annotationNodeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMetaAnnotationNode() {
		return metaAnnotationNodeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMetaViewAnnotations() {
		return metaViewAnnotationsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMetaViewAnnotations_View() {
		return (EReference)metaViewAnnotationsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMetaViewAnnotations_Types() {
		return (EAttribute)metaViewAnnotationsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMetaViewAnnotations_ExclusionAnnotations() {
		return (EAttribute)metaViewAnnotationsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getViewLocationNode() {
		return viewLocationNodeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getViewLocationNode_View() {
		return (EReference)viewLocationNodeEClass.getEStructuralFeatures().get(0);
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
		annotationNodeEClass = createEClass(ANNOTATION_NODE);
		createEAttribute(annotationNodeEClass, ANNOTATION_NODE__ANNOTATION_ID);

		metaAnnotationNodeEClass = createEClass(META_ANNOTATION_NODE);

		metaViewAnnotationsEClass = createEClass(META_VIEW_ANNOTATIONS);
		createEReference(metaViewAnnotationsEClass, META_VIEW_ANNOTATIONS__VIEW);
		createEAttribute(metaViewAnnotationsEClass, META_VIEW_ANNOTATIONS__TYPES);
		createEAttribute(metaViewAnnotationsEClass, META_VIEW_ANNOTATIONS__EXCLUSION_ANNOTATIONS);

		viewLocationNodeEClass = createEClass(VIEW_LOCATION_NODE);
		createEReference(viewLocationNodeEClass, VIEW_LOCATION_NODE__VIEW);
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
		NotationPackage theNotationPackage = (NotationPackage)EPackage.Registry.INSTANCE.getEPackage(NotationPackage.eNS_URI);
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		annotationNodeEClass.getESuperTypes().add(theNotationPackage.getNode());
		metaAnnotationNodeEClass.getESuperTypes().add(theNotationPackage.getNode());
		metaViewAnnotationsEClass.getESuperTypes().add(theNotationPackage.getNode());
		viewLocationNodeEClass.getESuperTypes().add(theNotationPackage.getNode());

		// Initialize classes and features; add operations and parameters
		initEClass(annotationNodeEClass, AnnotationNode.class, "AnnotationNode", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAnnotationNode_AnnotationId(), ecorePackage.getEString(), "annotationId", null, 0, 1, AnnotationNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(metaAnnotationNodeEClass, MetaAnnotationNode.class, "MetaAnnotationNode", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(metaViewAnnotationsEClass, MetaViewAnnotations.class, "MetaViewAnnotations", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMetaViewAnnotations_View(), theNotationPackage.getView(), null, "view", null, 0, 1, MetaViewAnnotations.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetaViewAnnotations_Types(), ecorePackage.getEString(), "types", null, 0, -1, MetaViewAnnotations.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMetaViewAnnotations_ExclusionAnnotations(), ecorePackage.getEString(), "exclusionAnnotations", null, 0, -1, MetaViewAnnotations.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(viewLocationNodeEClass, ViewLocationNode.class, "ViewLocationNode", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getViewLocationNode_View(), theEcorePackage.getEObject(), null, "view", null, 0, 1, ViewLocationNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //ModelPackageImpl
