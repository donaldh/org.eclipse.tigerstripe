/**
 * <copyright>
 * </copyright>
 *
 * $Id: ModelPackage.java,v 1.5 2008/07/11 06:34:47 ystrot Exp $
 */
package org.eclipse.tigerstripe.annotation.ui.diagrams.model;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
	 * The number of structural features of the '<em>Meta Annotation Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_ANNOTATION_NODE_FEATURE_COUNT = NotationPackage.NODE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.MetaViewAnnotationsImpl <em>Meta View Annotations</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.MetaViewAnnotationsImpl
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.ModelPackageImpl#getMetaViewAnnotations()
	 * @generated
	 */
	int META_VIEW_ANNOTATIONS = 2;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_VIEW_ANNOTATIONS__EANNOTATIONS = NotationPackage.NODE__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_VIEW_ANNOTATIONS__VISIBLE = NotationPackage.NODE__VISIBLE;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_VIEW_ANNOTATIONS__TYPE = NotationPackage.NODE__TYPE;

	/**
	 * The feature id for the '<em><b>Mutable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_VIEW_ANNOTATIONS__MUTABLE = NotationPackage.NODE__MUTABLE;

	/**
	 * The feature id for the '<em><b>Source Edges</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_VIEW_ANNOTATIONS__SOURCE_EDGES = NotationPackage.NODE__SOURCE_EDGES;

	/**
	 * The feature id for the '<em><b>Target Edges</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_VIEW_ANNOTATIONS__TARGET_EDGES = NotationPackage.NODE__TARGET_EDGES;

	/**
	 * The feature id for the '<em><b>Persisted Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_VIEW_ANNOTATIONS__PERSISTED_CHILDREN = NotationPackage.NODE__PERSISTED_CHILDREN;

	/**
	 * The feature id for the '<em><b>Styles</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_VIEW_ANNOTATIONS__STYLES = NotationPackage.NODE__STYLES;

	/**
	 * The feature id for the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_VIEW_ANNOTATIONS__ELEMENT = NotationPackage.NODE__ELEMENT;

	/**
	 * The feature id for the '<em><b>Diagram</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_VIEW_ANNOTATIONS__DIAGRAM = NotationPackage.NODE__DIAGRAM;

	/**
	 * The feature id for the '<em><b>Transient Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_VIEW_ANNOTATIONS__TRANSIENT_CHILDREN = NotationPackage.NODE__TRANSIENT_CHILDREN;

	/**
	 * The feature id for the '<em><b>Layout Constraint</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_VIEW_ANNOTATIONS__LAYOUT_CONSTRAINT = NotationPackage.NODE__LAYOUT_CONSTRAINT;

	/**
	 * The feature id for the '<em><b>View</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_VIEW_ANNOTATIONS__VIEW = NotationPackage.NODE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Types</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_VIEW_ANNOTATIONS__TYPES = NotationPackage.NODE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Exclusion Annotations</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_VIEW_ANNOTATIONS__EXCLUSION_ANNOTATIONS = NotationPackage.NODE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Meta View Annotations</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int META_VIEW_ANNOTATIONS_FEATURE_COUNT = NotationPackage.NODE_FEATURE_COUNT + 3;


	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.ViewLocationNodeImpl <em>View Location Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.ViewLocationNodeImpl
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.ModelPackageImpl#getViewLocationNode()
	 * @generated
	 */
	int VIEW_LOCATION_NODE = 3;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW_LOCATION_NODE__EANNOTATIONS = NotationPackage.NODE__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW_LOCATION_NODE__VISIBLE = NotationPackage.NODE__VISIBLE;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW_LOCATION_NODE__TYPE = NotationPackage.NODE__TYPE;

	/**
	 * The feature id for the '<em><b>Mutable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW_LOCATION_NODE__MUTABLE = NotationPackage.NODE__MUTABLE;

	/**
	 * The feature id for the '<em><b>Source Edges</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW_LOCATION_NODE__SOURCE_EDGES = NotationPackage.NODE__SOURCE_EDGES;

	/**
	 * The feature id for the '<em><b>Target Edges</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW_LOCATION_NODE__TARGET_EDGES = NotationPackage.NODE__TARGET_EDGES;

	/**
	 * The feature id for the '<em><b>Persisted Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW_LOCATION_NODE__PERSISTED_CHILDREN = NotationPackage.NODE__PERSISTED_CHILDREN;

	/**
	 * The feature id for the '<em><b>Styles</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW_LOCATION_NODE__STYLES = NotationPackage.NODE__STYLES;

	/**
	 * The feature id for the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW_LOCATION_NODE__ELEMENT = NotationPackage.NODE__ELEMENT;

	/**
	 * The feature id for the '<em><b>Diagram</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW_LOCATION_NODE__DIAGRAM = NotationPackage.NODE__DIAGRAM;

	/**
	 * The feature id for the '<em><b>Transient Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW_LOCATION_NODE__TRANSIENT_CHILDREN = NotationPackage.NODE__TRANSIENT_CHILDREN;

	/**
	 * The feature id for the '<em><b>Layout Constraint</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW_LOCATION_NODE__LAYOUT_CONSTRAINT = NotationPackage.NODE__LAYOUT_CONSTRAINT;

	/**
	 * The feature id for the '<em><b>View</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW_LOCATION_NODE__VIEW = NotationPackage.NODE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>View Location Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIEW_LOCATION_NODE_FEATURE_COUNT = NotationPackage.NODE_FEATURE_COUNT + 1;


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
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.MetaViewAnnotations <em>Meta View Annotations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Meta View Annotations</em>'.
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.MetaViewAnnotations
	 * @generated
	 */
	EClass getMetaViewAnnotations();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.MetaViewAnnotations#getView <em>View</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>View</em>'.
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.MetaViewAnnotations#getView()
	 * @see #getMetaViewAnnotations()
	 * @generated
	 */
	EReference getMetaViewAnnotations_View();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.MetaViewAnnotations#getTypes <em>Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Types</em>'.
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.MetaViewAnnotations#getTypes()
	 * @see #getMetaViewAnnotations()
	 * @generated
	 */
	EAttribute getMetaViewAnnotations_Types();

	/**
	 * Returns the meta object for the attribute list '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.MetaViewAnnotations#getExclusionAnnotations <em>Exclusion Annotations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Exclusion Annotations</em>'.
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.MetaViewAnnotations#getExclusionAnnotations()
	 * @see #getMetaViewAnnotations()
	 * @generated
	 */
	EAttribute getMetaViewAnnotations_ExclusionAnnotations();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.ViewLocationNode <em>View Location Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>View Location Node</em>'.
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.ViewLocationNode
	 * @generated
	 */
	EClass getViewLocationNode();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.ViewLocationNode#getView <em>View</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>View</em>'.
	 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.ViewLocationNode#getView()
	 * @see #getViewLocationNode()
	 * @generated
	 */
	EReference getViewLocationNode_View();

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
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.MetaViewAnnotationsImpl <em>Meta View Annotations</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.MetaViewAnnotationsImpl
		 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.ModelPackageImpl#getMetaViewAnnotations()
		 * @generated
		 */
		EClass META_VIEW_ANNOTATIONS = eINSTANCE.getMetaViewAnnotations();

		/**
		 * The meta object literal for the '<em><b>View</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference META_VIEW_ANNOTATIONS__VIEW = eINSTANCE.getMetaViewAnnotations_View();

		/**
		 * The meta object literal for the '<em><b>Types</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute META_VIEW_ANNOTATIONS__TYPES = eINSTANCE.getMetaViewAnnotations_Types();

		/**
		 * The meta object literal for the '<em><b>Exclusion Annotations</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute META_VIEW_ANNOTATIONS__EXCLUSION_ANNOTATIONS = eINSTANCE.getMetaViewAnnotations_ExclusionAnnotations();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.ViewLocationNodeImpl <em>View Location Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.ViewLocationNodeImpl
		 * @see org.eclipse.tigerstripe.annotation.ui.diagrams.model.impl.ModelPackageImpl#getViewLocationNode()
		 * @generated
		 */
		EClass VIEW_LOCATION_NODE = eINSTANCE.getViewLocationNode();

		/**
		 * The meta object literal for the '<em><b>View</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VIEW_LOCATION_NODE__VIEW = eINSTANCE.getViewLocationNode_View();

	}

} //ModelPackage
