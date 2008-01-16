/**
 * <copyright>
 * </copyright>
 *
 * $Id: ContextPackage.java,v 1.1 2008/01/16 18:14:19 edillon Exp $
 */
package org.eclipse.tigerstripe.annotations.internal.context;

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
 * @see org.eclipse.tigerstripe.annotations.internal.context.ContextFactory
 * @model kind="package"
 * @generated
 */
public interface ContextPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "context";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/tigerstripe/schemas/annotations";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "ann";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ContextPackage eINSTANCE = org.eclipse.tigerstripe.annotations.internal.context.impl.ContextPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.annotations.internal.context.impl.AnnotationContextImpl <em>Annotation Context</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.annotations.internal.context.impl.AnnotationContextImpl
	 * @see org.eclipse.tigerstripe.annotations.internal.context.impl.ContextPackageImpl#getAnnotationContext()
	 * @generated
	 */
	int ANNOTATION_CONTEXT = 0;

	/**
	 * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_CONTEXT__ANNOTATIONS = 0;

	/**
	 * The feature id for the '<em><b>Namespace ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_CONTEXT__NAMESPACE_ID = 1;

	/**
	 * The number of structural features of the '<em>Annotation Context</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_CONTEXT_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.annotations.internal.context.impl.AnnotationImpl <em>Annotation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.annotations.internal.context.impl.AnnotationImpl
	 * @see org.eclipse.tigerstripe.annotations.internal.context.impl.ContextPackageImpl#getAnnotation()
	 * @generated
	 */
	int ANNOTATION = 1;

	/**
	 * The feature id for the '<em><b>Annotation Specification ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION__ANNOTATION_SPECIFICATION_ID = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION__VALUE = 1;

	/**
	 * The feature id for the '<em><b>Uri</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION__URI = 2;

	/**
	 * The number of structural features of the '<em>Annotation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_FEATURE_COUNT = 3;


	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.annotations.internal.context.AnnotationContext <em>Annotation Context</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Annotation Context</em>'.
	 * @see org.eclipse.tigerstripe.annotations.internal.context.AnnotationContext
	 * @generated
	 */
	EClass getAnnotationContext();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.annotations.internal.context.AnnotationContext#getAnnotations <em>Annotations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Annotations</em>'.
	 * @see org.eclipse.tigerstripe.annotations.internal.context.AnnotationContext#getAnnotations()
	 * @see #getAnnotationContext()
	 * @generated
	 */
	EReference getAnnotationContext_Annotations();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotations.internal.context.AnnotationContext#getNamespaceID <em>Namespace ID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Namespace ID</em>'.
	 * @see org.eclipse.tigerstripe.annotations.internal.context.AnnotationContext#getNamespaceID()
	 * @see #getAnnotationContext()
	 * @generated
	 */
	EAttribute getAnnotationContext_NamespaceID();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.annotations.internal.context.Annotation <em>Annotation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Annotation</em>'.
	 * @see org.eclipse.tigerstripe.annotations.internal.context.Annotation
	 * @generated
	 */
	EClass getAnnotation();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotations.internal.context.Annotation#getAnnotationSpecificationID <em>Annotation Specification ID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Annotation Specification ID</em>'.
	 * @see org.eclipse.tigerstripe.annotations.internal.context.Annotation#getAnnotationSpecificationID()
	 * @see #getAnnotation()
	 * @generated
	 */
	EAttribute getAnnotation_AnnotationSpecificationID();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotations.internal.context.Annotation#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.tigerstripe.annotations.internal.context.Annotation#getValue()
	 * @see #getAnnotation()
	 * @generated
	 */
	EAttribute getAnnotation_Value();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotations.internal.context.Annotation#getUri <em>Uri</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Uri</em>'.
	 * @see org.eclipse.tigerstripe.annotations.internal.context.Annotation#getUri()
	 * @see #getAnnotation()
	 * @generated
	 */
	EAttribute getAnnotation_Uri();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ContextFactory getContextFactory();

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
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.annotations.internal.context.impl.AnnotationContextImpl <em>Annotation Context</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.annotations.internal.context.impl.AnnotationContextImpl
		 * @see org.eclipse.tigerstripe.annotations.internal.context.impl.ContextPackageImpl#getAnnotationContext()
		 * @generated
		 */
		EClass ANNOTATION_CONTEXT = eINSTANCE.getAnnotationContext();

		/**
		 * The meta object literal for the '<em><b>Annotations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ANNOTATION_CONTEXT__ANNOTATIONS = eINSTANCE.getAnnotationContext_Annotations();

		/**
		 * The meta object literal for the '<em><b>Namespace ID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ANNOTATION_CONTEXT__NAMESPACE_ID = eINSTANCE.getAnnotationContext_NamespaceID();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.annotations.internal.context.impl.AnnotationImpl <em>Annotation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.annotations.internal.context.impl.AnnotationImpl
		 * @see org.eclipse.tigerstripe.annotations.internal.context.impl.ContextPackageImpl#getAnnotation()
		 * @generated
		 */
		EClass ANNOTATION = eINSTANCE.getAnnotation();

		/**
		 * The meta object literal for the '<em><b>Annotation Specification ID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ANNOTATION__ANNOTATION_SPECIFICATION_ID = eINSTANCE.getAnnotation_AnnotationSpecificationID();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ANNOTATION__VALUE = eINSTANCE.getAnnotation_Value();

		/**
		 * The meta object literal for the '<em><b>Uri</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ANNOTATION__URI = eINSTANCE.getAnnotation_Uri();

	}

} //ContextPackage
