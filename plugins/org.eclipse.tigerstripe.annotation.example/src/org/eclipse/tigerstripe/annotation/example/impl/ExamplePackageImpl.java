/**
 * <copyright>
 * 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *     
 * </copyright>
 *
 * $Id: ExamplePackageImpl.java,v 1.3 2011/11/01 11:12:15 asalnik Exp $
 */
package org.eclipse.tigerstripe.annotation.example.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.tigerstripe.annotation.example.CheckAnnotation;
import org.eclipse.tigerstripe.annotation.example.ExampleFactory;
import org.eclipse.tigerstripe.annotation.example.ExamplePackage;
import org.eclipse.tigerstripe.annotation.example.IntegerAnnotation;
import org.eclipse.tigerstripe.annotation.example.ReferencesExample;
import org.eclipse.tigerstripe.annotation.example.TextAnnotation;
import org.eclipse.tigerstripe.workbench.annotation.modelReference.ModelReferencePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ExamplePackageImpl extends EPackageImpl implements ExamplePackage {
    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass checkAnnotationEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass integerAnnotationEClass = null;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass textAnnotationEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass referencesExampleEClass = null;

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
	 * @see org.eclipse.tigerstripe.annotation.example.ExamplePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
    private ExamplePackageImpl() {
		super(eNS_URI, ExampleFactory.eINSTANCE);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private static boolean isInited = false;

    /**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link ExamplePackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
    public static ExamplePackage init() {
		if (isInited) return (ExamplePackage)EPackage.Registry.INSTANCE.getEPackage(ExamplePackage.eNS_URI);

		// Obtain or create and register package
		ExamplePackageImpl theExamplePackage = (ExamplePackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ExamplePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ExamplePackageImpl());

		isInited = true;

		// Initialize simple dependencies
		ModelReferencePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theExamplePackage.createPackageContents();

		// Initialize created meta-data
		theExamplePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theExamplePackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ExamplePackage.eNS_URI, theExamplePackage);
		return theExamplePackage;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getCheckAnnotation() {
		return checkAnnotationEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getCheckAnnotation_Checked() {
		return (EAttribute)checkAnnotationEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getIntegerAnnotation() {
		return integerAnnotationEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getIntegerAnnotation_Integer() {
		return (EAttribute)integerAnnotationEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getTextAnnotation() {
		return textAnnotationEClass;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getTextAnnotation_Text() {
		return (EAttribute)textAnnotationEClass.getEStructuralFeatures().get(0);
	}

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getReferencesExample() {
		return referencesExampleEClass;
	}

				/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getReferencesExample_Entity() {
		return (EReference)referencesExampleEClass.getEStructuralFeatures().get(0);
	}

				/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getReferencesExample_Attributes() {
		return (EReference)referencesExampleEClass.getEStructuralFeatures().get(1);
	}

				/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReferencesExample_StringRefToAttribute() {
		return (EAttribute)referencesExampleEClass.getEStructuralFeatures().get(2);
	}

				/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReferencesExample_StringRefsToAttributes() {
		return (EAttribute)referencesExampleEClass.getEStructuralFeatures().get(3);
	}

				/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public ExampleFactory getExampleFactory() {
		return (ExampleFactory)getEFactoryInstance();
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
		checkAnnotationEClass = createEClass(CHECK_ANNOTATION);
		createEAttribute(checkAnnotationEClass, CHECK_ANNOTATION__CHECKED);

		integerAnnotationEClass = createEClass(INTEGER_ANNOTATION);
		createEAttribute(integerAnnotationEClass, INTEGER_ANNOTATION__INTEGER);

		textAnnotationEClass = createEClass(TEXT_ANNOTATION);
		createEAttribute(textAnnotationEClass, TEXT_ANNOTATION__TEXT);

		referencesExampleEClass = createEClass(REFERENCES_EXAMPLE);
		createEReference(referencesExampleEClass, REFERENCES_EXAMPLE__ENTITY);
		createEReference(referencesExampleEClass, REFERENCES_EXAMPLE__ATTRIBUTES);
		createEAttribute(referencesExampleEClass, REFERENCES_EXAMPLE__STRING_REF_TO_ATTRIBUTE);
		createEAttribute(referencesExampleEClass, REFERENCES_EXAMPLE__STRING_REFS_TO_ATTRIBUTES);
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
		ModelReferencePackage theModelReferencePackage = (ModelReferencePackage)EPackage.Registry.INSTANCE.getEPackage(ModelReferencePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(checkAnnotationEClass, CheckAnnotation.class, "CheckAnnotation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCheckAnnotation_Checked(), ecorePackage.getEBoolean(), "checked", null, 0, 1, CheckAnnotation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(integerAnnotationEClass, IntegerAnnotation.class, "IntegerAnnotation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIntegerAnnotation_Integer(), ecorePackage.getEInt(), "integer", null, 0, 1, IntegerAnnotation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(textAnnotationEClass, TextAnnotation.class, "TextAnnotation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTextAnnotation_Text(), ecorePackage.getEString(), "text", null, 0, 1, TextAnnotation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(referencesExampleEClass, ReferencesExample.class, "ReferencesExample", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getReferencesExample_Entity(), theModelReferencePackage.getModelReference(), null, "entity", null, 0, 1, ReferencesExample.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getReferencesExample_Attributes(), theModelReferencePackage.getModelReference(), null, "attributes", null, 0, -1, ReferencesExample.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReferencesExample_StringRefToAttribute(), ecorePackage.getEString(), "stringRefToAttribute", null, 0, 1, ReferencesExample.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReferencesExample_StringRefsToAttributes(), ecorePackage.getEString(), "stringRefsToAttributes", null, 0, -1, ReferencesExample.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations();
		// org.eclipse.tigerstripe.annotation
		createOrgAnnotations();
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
		  (textAnnotationEClass, 
		   source, 
		   new String[] {
			 "name", "text_._annotation",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getTextAnnotation_Text(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "text",
			 "namespace", "##targetNamespace"
		   });	
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
		  (getTextAnnotation_Text(), 
		   source, 
		   new String[] {
			 "multiline", "true"
		   });
	}

} //ExamplePackageImpl
