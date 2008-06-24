/**
 * <copyright>
 * </copyright>
 *
 * $Id: ModelPackage.java,v 1.2 2008/06/24 09:40:14 ystrot Exp $
 */
package org.eclipse.tigerstripe.annotation.ui.diagrams.model;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gmf.runtime.notation.NotationPackage;

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
 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.ModelFactory
 * @model kind="package"
 * @generated
 */
public interface ModelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "model";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http:///org/eclipse/tigerstripe/annotation/ui/diagrams/model.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.tigerstripe.annotation.ui.diagrams.model";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ModelPackage eINSTANCE = org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.ModelPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.AnnotationNodeImpl <em>Annotation Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.AnnotationNodeImpl
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.ModelPackageImpl#getAnnotationNode()
	 * @generated
	 */
	int ANNOTATION_NODE = 0;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_NODE__EANNOTATIONS = NotationPackage.NODE__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_NODE__VISIBLE = NotationPackage.NODE__VISIBLE;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_NODE__TYPE = NotationPackage.NODE__TYPE;

	/**
	 * The feature id for the '<em><b>Mutable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_NODE__MUTABLE = NotationPackage.NODE__MUTABLE;

	/**
	 * The feature id for the '<em><b>Source Edges</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_NODE__SOURCE_EDGES = NotationPackage.NODE__SOURCE_EDGES;

	/**
	 * The feature id for the '<em><b>Target Edges</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_NODE__TARGET_EDGES = NotationPackage.NODE__TARGET_EDGES;

	/**
	 * The feature id for the '<em><b>Persisted Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_NODE__PERSISTED_CHILDREN = NotationPackage.NODE__PERSISTED_CHILDREN;

	/**
	 * The feature id for the '<em><b>Styles</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_NODE__STYLES = NotationPackage.NODE__STYLES;

	/**
	 * The feature id for the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_NODE__ELEMENT = NotationPackage.NODE__ELEMENT;

	/**
	 * The feature id for the '<em><b>Diagram</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_NODE__DIAGRAM = NotationPackage.NODE__DIAGRAM;

	/**
	 * The feature id for the '<em><b>Transient Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_NODE__TRANSIENT_CHILDREN = NotationPackage.NODE__TRANSIENT_CHILDREN;

	/**
	 * The feature id for the '<em><b>Layout Constraint</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_NODE__LAYOUT_CONSTRAINT = NotationPackage.NODE__LAYOUT_CONSTRAINT;

	/**
	 * The feature id for the '<em><b>Annotation Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_NODE__ANNOTATION_ID = NotationPackage.NODE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Annotation Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANNOTATION_NODE_FEATURE_COUNT = NotationPackage.NODE_FEATURE_COUNT + 1;


	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.MetaAnnotationNodeImpl <em>Meta Annotation Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.MetaAnnotationNodeImpl
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.ModelPackageImpl#getMetaAnnotationNode()
	 * @generated
	 */
	int META_ANNOTATION_NODE = 1;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_ANNOTATION_NODE__EANNOTATIONS = NotationPackage.NODE__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_ANNOTATION_NODE__VISIBLE = NotationPackage.NODE__VISIBLE;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_ANNOTATION_NODE__TYPE = NotationPackage.NODE__TYPE;

	/**
	 * The feature id for the '<em><b>Mutable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_ANNOTATION_NODE__MUTABLE = NotationPackage.NODE__MUTABLE;

	/**
	 * The feature id for the '<em><b>Source Edges</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_ANNOTATION_NODE__SOURCE_EDGES = NotationPackage.NODE__SOURCE_EDGES;

	/**
	 * The feature id for the '<em><b>Target Edges</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_ANNOTATION_NODE__TARGET_EDGES = NotationPackage.NODE__TARGET_EDGES;

	/**
	 * The feature id for the '<em><b>Persisted Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_ANNOTATION_NODE__PERSISTED_CHILDREN = NotationPackage.NODE__PERSISTED_CHILDREN;

	/**
	 * The feature id for the '<em><b>Styles</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_ANNOTATION_NODE__STYLES = NotationPackage.NODE__STYLES;

	/**
	 * The feature id for the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_ANNOTATION_NODE__ELEMENT = NotationPackage.NODE__ELEMENT;

	/**
	 * The feature id for the '<em><b>Diagram</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_ANNOTATION_NODE__DIAGRAM = NotationPackage.NODE__DIAGRAM;

	/**
	 * The feature id for the '<em><b>Transient Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_ANNOTATION_NODE__TRANSIENT_CHILDREN = NotationPackage.NODE__TRANSIENT_CHILDREN;

	/**
	 * The feature id for the '<em><b>Layout Constraint</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_ANNOTATION_NODE__LAYOUT_CONSTRAINT = NotationPackage.NODE__LAYOUT_CONSTRAINT;

	/**
	 * The feature id for the '<em><b>Views Type Statuses</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_ANNOTATION_NODE__VIEWS_TYPE_STATUSES = NotationPackage.NODE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Meta Annotation Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_ANNOTATION_NODE_FEATURE_COUNT = NotationPackage.NODE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.TypeStatusImpl <em>Type Status</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.TypeStatusImpl
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.ModelPackageImpl#getTypeStatus()
	 * @generated
	 */
	int TYPE_STATUS = 2;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_STATUS__TYPE = 0;

	/**
	 * The feature id for the '<em><b>Show All</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_STATUS__SHOW_ALL = 1;

	/**
	 * The number of structural features of the '<em>Type Status</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_STATUS_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.ViewTypesStatusImpl <em>View Types Status</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.ViewTypesStatusImpl
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.ModelPackageImpl#getViewTypesStatus()
	 * @generated
	 */
	int VIEW_TYPES_STATUS = 3;

	/**
	 * The feature id for the '<em><b>View</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW_TYPES_STATUS__VIEW = 0;

	/**
	 * The feature id for the '<em><b>Statuses</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW_TYPES_STATUS__STATUSES = 1;

	/**
	 * The number of structural features of the '<em>View Types Status</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW_TYPES_STATUS_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '<em>Annotation</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.annotation.core.Annotation
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.ModelPackageImpl#getAnnotation()
	 * @generated
	 */
	int ANNOTATION = 4;


	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.AnnotationNode <em>Annotation Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Annotation Node</em>'.
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.AnnotationNode
	 * @generated
	 */
	EClass getAnnotationNode();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.AnnotationNode#getAnnotationId <em>Annotation Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Annotation Id</em>'.
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.AnnotationNode#getAnnotationId()
	 * @see #getAnnotationNode()
	 * @generated
	 */
	EAttribute getAnnotationNode_AnnotationId();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.MetaAnnotationNode <em>Meta Annotation Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Meta Annotation Node</em>'.
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.MetaAnnotationNode
	 * @generated
	 */
	EClass getMetaAnnotationNode();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.MetaAnnotationNode#getViewsTypeStatuses <em>Views Type Statuses</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Views Type Statuses</em>'.
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.MetaAnnotationNode#getViewsTypeStatuses()
	 * @see #getMetaAnnotationNode()
	 * @generated
	 */
	EReference getMetaAnnotationNode_ViewsTypeStatuses();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.TypeStatus <em>Type Status</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Type Status</em>'.
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.TypeStatus
	 * @generated
	 */
	EClass getTypeStatus();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.TypeStatus#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.TypeStatus#getType()
	 * @see #getTypeStatus()
	 * @generated
	 */
	EAttribute getTypeStatus_Type();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.TypeStatus#isShowAll <em>Show All</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Show All</em>'.
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.TypeStatus#isShowAll()
	 * @see #getTypeStatus()
	 * @generated
	 */
	EAttribute getTypeStatus_ShowAll();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.ViewTypesStatus <em>View Types Status</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>View Types Status</em>'.
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.ViewTypesStatus
	 * @generated
	 */
	EClass getViewTypesStatus();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.ViewTypesStatus#getView <em>View</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>View</em>'.
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.ViewTypesStatus#getView()
	 * @see #getViewTypesStatus()
	 * @generated
	 */
	EReference getViewTypesStatus_View();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.ViewTypesStatus#getStatuses <em>Statuses</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Statuses</em>'.
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.ViewTypesStatus#getStatuses()
	 * @see #getViewTypesStatus()
	 * @generated
	 */
	EReference getViewTypesStatus_Statuses();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.tigerstripe.annotation.core.Annotation <em>Annotation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Annotation</em>'.
	 * @see org.eclipse.tigerstripe.annotation.core.Annotation
	 * @model instanceClass="org.eclipse.tigerstripe.annotation.core.Annotation"
	 * @generated
	 */
	EDataType getAnnotation();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ModelFactory getModelFactory();

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
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.AnnotationNodeImpl <em>Annotation Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.AnnotationNodeImpl
		 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.ModelPackageImpl#getAnnotationNode()
		 * @generated
		 */
		EClass ANNOTATION_NODE = eINSTANCE.getAnnotationNode();

		/**
		 * The meta object literal for the '<em><b>Annotation Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ANNOTATION_NODE__ANNOTATION_ID = eINSTANCE.getAnnotationNode_AnnotationId();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.MetaAnnotationNodeImpl <em>Meta Annotation Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.MetaAnnotationNodeImpl
		 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.ModelPackageImpl#getMetaAnnotationNode()
		 * @generated
		 */
		EClass META_ANNOTATION_NODE = eINSTANCE.getMetaAnnotationNode();

		/**
		 * The meta object literal for the '<em><b>Views Type Statuses</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference META_ANNOTATION_NODE__VIEWS_TYPE_STATUSES = eINSTANCE.getMetaAnnotationNode_ViewsTypeStatuses();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.TypeStatusImpl <em>Type Status</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.TypeStatusImpl
		 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.ModelPackageImpl#getTypeStatus()
		 * @generated
		 */
		EClass TYPE_STATUS = eINSTANCE.getTypeStatus();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TYPE_STATUS__TYPE = eINSTANCE.getTypeStatus_Type();

		/**
		 * The meta object literal for the '<em><b>Show All</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TYPE_STATUS__SHOW_ALL = eINSTANCE.getTypeStatus_ShowAll();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.ViewTypesStatusImpl <em>View Types Status</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.ViewTypesStatusImpl
		 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.ModelPackageImpl#getViewTypesStatus()
		 * @generated
		 */
		EClass VIEW_TYPES_STATUS = eINSTANCE.getViewTypesStatus();

		/**
		 * The meta object literal for the '<em><b>View</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VIEW_TYPES_STATUS__VIEW = eINSTANCE.getViewTypesStatus_View();

		/**
		 * The meta object literal for the '<em><b>Statuses</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VIEW_TYPES_STATUS__STATUSES = eINSTANCE.getViewTypesStatus_Statuses();

		/**
		 * The meta object literal for the '<em>Annotation</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.annotation.core.Annotation
		 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.ModelPackageImpl#getAnnotation()
		 * @generated
		 */
		EDataType ANNOTATION = eINSTANCE.getAnnotation();

	}

} //ModelPackage
