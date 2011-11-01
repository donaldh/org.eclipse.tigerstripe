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
 * $Id: ExamplePackage.java,v 1.3 2011/11/01 11:12:14 asalnik Exp $
 */
package org.eclipse.tigerstripe.annotation.example;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
 * @see org.eclipse.tigerstripe.annotation.example.ExampleFactory
 * @model kind="package"
 * @generated
 */
public interface ExamplePackage extends EPackage {
    /**
	 * The package name.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    String eNAME = "example";

    /**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_URI = "http://eclipse.org/tigerstripe/examples/annotation";

    /**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_PREFIX = "org.eclipse.tigerstripe.annotation.example";

    /**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    ExamplePackage eINSTANCE = org.eclipse.tigerstripe.annotation.example.impl.ExamplePackageImpl.init();

    /**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.annotation.example.impl.CheckAnnotationImpl <em>Check Annotation</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.annotation.example.impl.CheckAnnotationImpl
	 * @see org.eclipse.tigerstripe.annotation.example.impl.ExamplePackageImpl#getCheckAnnotation()
	 * @generated
	 */
    int CHECK_ANNOTATION = 0;

    /**
	 * The feature id for the '<em><b>Checked</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CHECK_ANNOTATION__CHECKED = 0;

    /**
	 * The number of structural features of the '<em>Check Annotation</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CHECK_ANNOTATION_FEATURE_COUNT = 1;

    /**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.annotation.example.impl.IntegerAnnotationImpl <em>Integer Annotation</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.annotation.example.impl.IntegerAnnotationImpl
	 * @see org.eclipse.tigerstripe.annotation.example.impl.ExamplePackageImpl#getIntegerAnnotation()
	 * @generated
	 */
    int INTEGER_ANNOTATION = 1;

    /**
	 * The feature id for the '<em><b>Integer</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int INTEGER_ANNOTATION__INTEGER = 0;

    /**
	 * The number of structural features of the '<em>Integer Annotation</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int INTEGER_ANNOTATION_FEATURE_COUNT = 1;

    /**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.annotation.example.impl.TextAnnotationImpl <em>Text Annotation</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.annotation.example.impl.TextAnnotationImpl
	 * @see org.eclipse.tigerstripe.annotation.example.impl.ExamplePackageImpl#getTextAnnotation()
	 * @generated
	 */
    int TEXT_ANNOTATION = 2;

    /**
	 * The feature id for the '<em><b>Text</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TEXT_ANNOTATION__TEXT = 0;

    /**
	 * The number of structural features of the '<em>Text Annotation</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int TEXT_ANNOTATION_FEATURE_COUNT = 1;


    /**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.annotation.example.impl.ReferencesExampleImpl <em>References Example</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.annotation.example.impl.ReferencesExampleImpl
	 * @see org.eclipse.tigerstripe.annotation.example.impl.ExamplePackageImpl#getReferencesExample()
	 * @generated
	 */
	int REFERENCES_EXAMPLE = 3;

				/**
	 * The feature id for the '<em><b>Entity</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCES_EXAMPLE__ENTITY = 0;

				/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCES_EXAMPLE__ATTRIBUTES = 1;

				/**
	 * The feature id for the '<em><b>String Ref To Attribute</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCES_EXAMPLE__STRING_REF_TO_ATTRIBUTE = 2;

				/**
	 * The feature id for the '<em><b>String Refs To Attributes</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCES_EXAMPLE__STRING_REFS_TO_ATTRIBUTES = 3;

				/**
	 * The number of structural features of the '<em>References Example</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCES_EXAMPLE_FEATURE_COUNT = 4;

				/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.annotation.example.CheckAnnotation <em>Check Annotation</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Check Annotation</em>'.
	 * @see org.eclipse.tigerstripe.annotation.example.CheckAnnotation
	 * @generated
	 */
    EClass getCheckAnnotation();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotation.example.CheckAnnotation#isChecked <em>Checked</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Checked</em>'.
	 * @see org.eclipse.tigerstripe.annotation.example.CheckAnnotation#isChecked()
	 * @see #getCheckAnnotation()
	 * @generated
	 */
    EAttribute getCheckAnnotation_Checked();

    /**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.annotation.example.IntegerAnnotation <em>Integer Annotation</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Integer Annotation</em>'.
	 * @see org.eclipse.tigerstripe.annotation.example.IntegerAnnotation
	 * @generated
	 */
    EClass getIntegerAnnotation();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotation.example.IntegerAnnotation#getInteger <em>Integer</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Integer</em>'.
	 * @see org.eclipse.tigerstripe.annotation.example.IntegerAnnotation#getInteger()
	 * @see #getIntegerAnnotation()
	 * @generated
	 */
    EAttribute getIntegerAnnotation_Integer();

    /**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.annotation.example.TextAnnotation <em>Text Annotation</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Text Annotation</em>'.
	 * @see org.eclipse.tigerstripe.annotation.example.TextAnnotation
	 * @generated
	 */
    EClass getTextAnnotation();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotation.example.TextAnnotation#getText <em>Text</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Text</em>'.
	 * @see org.eclipse.tigerstripe.annotation.example.TextAnnotation#getText()
	 * @see #getTextAnnotation()
	 * @generated
	 */
    EAttribute getTextAnnotation_Text();

    /**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.annotation.example.ReferencesExample <em>References Example</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>References Example</em>'.
	 * @see org.eclipse.tigerstripe.annotation.example.ReferencesExample
	 * @generated
	 */
	EClass getReferencesExample();

				/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.tigerstripe.annotation.example.ReferencesExample#getEntity <em>Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Entity</em>'.
	 * @see org.eclipse.tigerstripe.annotation.example.ReferencesExample#getEntity()
	 * @see #getReferencesExample()
	 * @generated
	 */
	EReference getReferencesExample_Entity();

				/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.annotation.example.ReferencesExample#getAttributes <em>Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Attributes</em>'.
	 * @see org.eclipse.tigerstripe.annotation.example.ReferencesExample#getAttributes()
	 * @see #getReferencesExample()
	 * @generated
	 */
	EReference getReferencesExample_Attributes();

				/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotation.example.ReferencesExample#getStringRefToAttribute <em>String Ref To Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>String Ref To Attribute</em>'.
	 * @see org.eclipse.tigerstripe.annotation.example.ReferencesExample#getStringRefToAttribute()
	 * @see #getReferencesExample()
	 * @generated
	 */
	EAttribute getReferencesExample_StringRefToAttribute();

				/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.tigerstripe.annotation.example.ReferencesExample#getStringRefsToAttributes <em>String Refs To Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>String Refs To Attributes</em>'.
	 * @see org.eclipse.tigerstripe.annotation.example.ReferencesExample#getStringRefsToAttributes()
	 * @see #getReferencesExample()
	 * @generated
	 */
	EAttribute getReferencesExample_StringRefsToAttributes();

				/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
    ExampleFactory getExampleFactory();

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
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.annotation.example.impl.CheckAnnotationImpl <em>Check Annotation</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.annotation.example.impl.CheckAnnotationImpl
		 * @see org.eclipse.tigerstripe.annotation.example.impl.ExamplePackageImpl#getCheckAnnotation()
		 * @generated
		 */
        EClass CHECK_ANNOTATION = eINSTANCE.getCheckAnnotation();

        /**
		 * The meta object literal for the '<em><b>Checked</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CHECK_ANNOTATION__CHECKED = eINSTANCE.getCheckAnnotation_Checked();

        /**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.annotation.example.impl.IntegerAnnotationImpl <em>Integer Annotation</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.annotation.example.impl.IntegerAnnotationImpl
		 * @see org.eclipse.tigerstripe.annotation.example.impl.ExamplePackageImpl#getIntegerAnnotation()
		 * @generated
		 */
        EClass INTEGER_ANNOTATION = eINSTANCE.getIntegerAnnotation();

        /**
		 * The meta object literal for the '<em><b>Integer</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute INTEGER_ANNOTATION__INTEGER = eINSTANCE.getIntegerAnnotation_Integer();

        /**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.annotation.example.impl.TextAnnotationImpl <em>Text Annotation</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.annotation.example.impl.TextAnnotationImpl
		 * @see org.eclipse.tigerstripe.annotation.example.impl.ExamplePackageImpl#getTextAnnotation()
		 * @generated
		 */
        EClass TEXT_ANNOTATION = eINSTANCE.getTextAnnotation();

        /**
		 * The meta object literal for the '<em><b>Text</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute TEXT_ANNOTATION__TEXT = eINSTANCE.getTextAnnotation_Text();

								/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.annotation.example.impl.ReferencesExampleImpl <em>References Example</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.annotation.example.impl.ReferencesExampleImpl
		 * @see org.eclipse.tigerstripe.annotation.example.impl.ExamplePackageImpl#getReferencesExample()
		 * @generated
		 */
		EClass REFERENCES_EXAMPLE = eINSTANCE.getReferencesExample();

								/**
		 * The meta object literal for the '<em><b>Entity</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REFERENCES_EXAMPLE__ENTITY = eINSTANCE.getReferencesExample_Entity();

								/**
		 * The meta object literal for the '<em><b>Attributes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REFERENCES_EXAMPLE__ATTRIBUTES = eINSTANCE.getReferencesExample_Attributes();

								/**
		 * The meta object literal for the '<em><b>String Ref To Attribute</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REFERENCES_EXAMPLE__STRING_REF_TO_ATTRIBUTE = eINSTANCE.getReferencesExample_StringRefToAttribute();

								/**
		 * The meta object literal for the '<em><b>String Refs To Attributes</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REFERENCES_EXAMPLE__STRING_REFS_TO_ATTRIBUTES = eINSTANCE.getReferencesExample_StringRefsToAttributes();

    }

} //ExamplePackage
