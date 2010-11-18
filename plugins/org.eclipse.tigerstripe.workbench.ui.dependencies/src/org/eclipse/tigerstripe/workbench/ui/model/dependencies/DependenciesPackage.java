/**
 * <copyright>
 * </copyright>
 *
 * $Id: DependenciesPackage.java,v 1.5 2010/11/18 17:00:38 ystrot Exp $
 */
package org.eclipse.tigerstripe.workbench.ui.model.dependencies;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
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
 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesFactory
 * @model kind="package"
 * @generated
 */
public interface DependenciesPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "dependencies";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http:///org/eclipse/tigerstripe/dependencies.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.tigerstripe.dependencies";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DependenciesPackage eINSTANCE = org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DependenciesPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.ShapeImpl <em>Shape</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.ShapeImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DependenciesPackageImpl#getShape()
	 * @generated
	 */
	int SHAPE = 0;

	/**
	 * The feature id for the '<em><b>Location</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHAPE__LOCATION = 0;

	/**
	 * The feature id for the '<em><b>Size</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHAPE__SIZE = 1;

	/**
	 * The feature id for the '<em><b>Style</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHAPE__STYLE = 2;

	/**
	 * The feature id for the '<em><b>Source Connections</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHAPE__SOURCE_CONNECTIONS = 3;

	/**
	 * The feature id for the '<em><b>Target Connections</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHAPE__TARGET_CONNECTIONS = 4;

	/**
	 * The feature id for the '<em><b>Parent Layer</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHAPE__PARENT_LAYER = 5;

	/**
	 * The feature id for the '<em><b>Was Layouting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHAPE__WAS_LAYOUTING = 6;

	/**
	 * The number of structural features of the '<em>Shape</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHAPE_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.PointImpl <em>Point</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.PointImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DependenciesPackageImpl#getPoint()
	 * @generated
	 */
	int POINT = 1;

	/**
	 * The feature id for the '<em><b>X</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POINT__X = 0;

	/**
	 * The feature id for the '<em><b>Y</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POINT__Y = 1;

	/**
	 * The number of structural features of the '<em>Point</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POINT_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DimensionImpl <em>Dimension</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DimensionImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DependenciesPackageImpl#getDimension()
	 * @generated
	 */
	int DIMENSION = 2;

	/**
	 * The feature id for the '<em><b>Width</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIMENSION__WIDTH = 0;

	/**
	 * The feature id for the '<em><b>Height</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIMENSION__HEIGHT = 1;

	/**
	 * The number of structural features of the '<em>Dimension</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIMENSION_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.SubjectImpl <em>Subject</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.SubjectImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DependenciesPackageImpl#getSubject()
	 * @generated
	 */
	int SUBJECT = 3;

	/**
	 * The feature id for the '<em><b>Location</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUBJECT__LOCATION = SHAPE__LOCATION;

	/**
	 * The feature id for the '<em><b>Size</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUBJECT__SIZE = SHAPE__SIZE;

	/**
	 * The feature id for the '<em><b>Style</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUBJECT__STYLE = SHAPE__STYLE;

	/**
	 * The feature id for the '<em><b>Source Connections</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUBJECT__SOURCE_CONNECTIONS = SHAPE__SOURCE_CONNECTIONS;

	/**
	 * The feature id for the '<em><b>Target Connections</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUBJECT__TARGET_CONNECTIONS = SHAPE__TARGET_CONNECTIONS;

	/**
	 * The feature id for the '<em><b>Parent Layer</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUBJECT__PARENT_LAYER = SHAPE__PARENT_LAYER;

	/**
	 * The feature id for the '<em><b>Was Layouting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUBJECT__WAS_LAYOUTING = SHAPE__WAS_LAYOUTING;

	/**
	 * The feature id for the '<em><b>External Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUBJECT__EXTERNAL_ID = SHAPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Opened</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUBJECT__OPENED = SHAPE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Use Custom Style</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUBJECT__USE_CUSTOM_STYLE = SHAPE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUBJECT__KIND = SHAPE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Master</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUBJECT__MASTER = SHAPE_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Loaded</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUBJECT__LOADED = SHAPE_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Subject</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUBJECT_FEATURE_COUNT = SHAPE_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.NoteImpl <em>Note</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.NoteImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DependenciesPackageImpl#getNote()
	 * @generated
	 */
	int NOTE = 4;

	/**
	 * The feature id for the '<em><b>Location</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE__LOCATION = SHAPE__LOCATION;

	/**
	 * The feature id for the '<em><b>Size</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE__SIZE = SHAPE__SIZE;

	/**
	 * The feature id for the '<em><b>Style</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE__STYLE = SHAPE__STYLE;

	/**
	 * The feature id for the '<em><b>Source Connections</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE__SOURCE_CONNECTIONS = SHAPE__SOURCE_CONNECTIONS;

	/**
	 * The feature id for the '<em><b>Target Connections</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE__TARGET_CONNECTIONS = SHAPE__TARGET_CONNECTIONS;

	/**
	 * The feature id for the '<em><b>Parent Layer</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE__PARENT_LAYER = SHAPE__PARENT_LAYER;

	/**
	 * The feature id for the '<em><b>Was Layouting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE__WAS_LAYOUTING = SHAPE__WAS_LAYOUTING;

	/**
	 * The feature id for the '<em><b>Text</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE__TEXT = SHAPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Note</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NOTE_FEATURE_COUNT = SHAPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.ShapeStyleImpl <em>Shape Style</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.ShapeStyleImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DependenciesPackageImpl#getShapeStyle()
	 * @generated
	 */
	int SHAPE_STYLE = 5;

	/**
	 * The feature id for the '<em><b>Background Color</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHAPE_STYLE__BACKGROUND_COLOR = 0;

	/**
	 * The feature id for the '<em><b>Foreground Color</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHAPE_STYLE__FOREGROUND_COLOR = 1;

	/**
	 * The number of structural features of the '<em>Shape Style</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHAPE_STYLE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.ConnectionImpl <em>Connection</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.ConnectionImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DependenciesPackageImpl#getConnection()
	 * @generated
	 */
	int CONNECTION = 6;

	/**
	 * The feature id for the '<em><b>Source</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTION__SOURCE = 0;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTION__TARGET = 1;

	/**
	 * The feature id for the '<em><b>Style</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTION__STYLE = 2;

	/**
	 * The number of structural features of the '<em>Connection</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTION_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.ConnectionStyleImpl <em>Connection Style</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.ConnectionStyleImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DependenciesPackageImpl#getConnectionStyle()
	 * @generated
	 */
	int CONNECTION_STYLE = 7;

	/**
	 * The feature id for the '<em><b>Stroke Color</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTION_STYLE__STROKE_COLOR = 0;

	/**
	 * The number of structural features of the '<em>Connection Style</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTION_STYLE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DiagramImpl <em>Diagram</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DiagramImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DependenciesPackageImpl#getDiagram()
	 * @generated
	 */
	int DIAGRAM = 8;

	/**
	 * The feature id for the '<em><b>Current Layer</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM__CURRENT_LAYER = 0;

	/**
	 * The feature id for the '<em><b>Kinds</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM__KINDS = 1;

	/**
	 * The feature id for the '<em><b>Layers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM__LAYERS = 2;

	/**
	 * The feature id for the '<em><b>Layers History</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM__LAYERS_HISTORY = 3;

	/**
	 * The feature id for the '<em><b>Router</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM__ROUTER = 4;

	/**
	 * The number of structural features of the '<em>Diagram</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.LayerImpl <em>Layer</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.LayerImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DependenciesPackageImpl#getLayer()
	 * @generated
	 */
	int LAYER = 9;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LAYER__ID = 0;

	/**
	 * The feature id for the '<em><b>Shapes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LAYER__SHAPES = 1;

	/**
	 * The feature id for the '<em><b>Was Layouting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LAYER__WAS_LAYOUTING = 2;

	/**
	 * The number of structural features of the '<em>Layer</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LAYER_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.KindImpl <em>Kind</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.KindImpl
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DependenciesPackageImpl#getKind()
	 * @generated
	 */
	int KIND = 10;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KIND__ID = 0;

	/**
	 * The feature id for the '<em><b>Style</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KIND__STYLE = 1;

	/**
	 * The feature id for the '<em><b>Use Custom Style</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KIND__USE_CUSTOM_STYLE = 2;

	/**
	 * The number of structural features of the '<em>Kind</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KIND_FEATURE_COUNT = 3;


	/**
	 * The meta object id for the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Router <em>Router</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Router
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DependenciesPackageImpl#getRouter()
	 * @generated
	 */
	int ROUTER = 11;


	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape <em>Shape</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Shape</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape
	 * @generated
	 */
	EClass getShape();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape#getLocation <em>Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Location</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape#getLocation()
	 * @see #getShape()
	 * @generated
	 */
	EReference getShape_Location();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape#getSize <em>Size</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Size</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape#getSize()
	 * @see #getShape()
	 * @generated
	 */
	EReference getShape_Size();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape#getStyle <em>Style</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Style</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape#getStyle()
	 * @see #getShape()
	 * @generated
	 */
	EReference getShape_Style();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape#getSourceConnections <em>Source Connections</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Source Connections</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape#getSourceConnections()
	 * @see #getShape()
	 * @generated
	 */
	EReference getShape_SourceConnections();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape#getTargetConnections <em>Target Connections</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Target Connections</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape#getTargetConnections()
	 * @see #getShape()
	 * @generated
	 */
	EReference getShape_TargetConnections();

	/**
	 * Returns the meta object for the container reference '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape#getParentLayer <em>Parent Layer</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Parent Layer</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape#getParentLayer()
	 * @see #getShape()
	 * @generated
	 */
	EReference getShape_ParentLayer();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape#isWasLayouting <em>Was Layouting</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Was Layouting</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape#isWasLayouting()
	 * @see #getShape()
	 * @generated
	 */
	EAttribute getShape_WasLayouting();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Point <em>Point</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Point</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Point
	 * @generated
	 */
	EClass getPoint();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Point#getX <em>X</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>X</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Point#getX()
	 * @see #getPoint()
	 * @generated
	 */
	EAttribute getPoint_X();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Point#getY <em>Y</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Y</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Point#getY()
	 * @see #getPoint()
	 * @generated
	 */
	EAttribute getPoint_Y();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Dimension <em>Dimension</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Dimension</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Dimension
	 * @generated
	 */
	EClass getDimension();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Dimension#getWidth <em>Width</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Width</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Dimension#getWidth()
	 * @see #getDimension()
	 * @generated
	 */
	EAttribute getDimension_Width();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Dimension#getHeight <em>Height</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Height</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Dimension#getHeight()
	 * @see #getDimension()
	 * @generated
	 */
	EAttribute getDimension_Height();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject <em>Subject</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Subject</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject
	 * @generated
	 */
	EClass getSubject();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject#getExternalId <em>External Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>External Id</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject#getExternalId()
	 * @see #getSubject()
	 * @generated
	 */
	EAttribute getSubject_ExternalId();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject#isOpened <em>Opened</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Opened</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject#isOpened()
	 * @see #getSubject()
	 * @generated
	 */
	EAttribute getSubject_Opened();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject#isUseCustomStyle <em>Use Custom Style</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Use Custom Style</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject#isUseCustomStyle()
	 * @see #getSubject()
	 * @generated
	 */
	EAttribute getSubject_UseCustomStyle();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject#getKind <em>Kind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Kind</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject#getKind()
	 * @see #getSubject()
	 * @generated
	 */
	EReference getSubject_Kind();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject#isMaster <em>Master</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Master</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject#isMaster()
	 * @see #getSubject()
	 * @generated
	 */
	EAttribute getSubject_Master();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject#isLoaded <em>Loaded</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Loaded</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject#isLoaded()
	 * @see #getSubject()
	 * @generated
	 */
	EAttribute getSubject_Loaded();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Note <em>Note</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Note</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Note
	 * @generated
	 */
	EClass getNote();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Note#getText <em>Text</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Text</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Note#getText()
	 * @see #getNote()
	 * @generated
	 */
	EAttribute getNote_Text();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.ShapeStyle <em>Shape Style</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Shape Style</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.ShapeStyle
	 * @generated
	 */
	EClass getShapeStyle();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.ShapeStyle#getBackgroundColor <em>Background Color</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Background Color</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.ShapeStyle#getBackgroundColor()
	 * @see #getShapeStyle()
	 * @generated
	 */
	EAttribute getShapeStyle_BackgroundColor();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.ShapeStyle#getForegroundColor <em>Foreground Color</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Foreground Color</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.ShapeStyle#getForegroundColor()
	 * @see #getShapeStyle()
	 * @generated
	 */
	EAttribute getShapeStyle_ForegroundColor();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Connection <em>Connection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Connection</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Connection
	 * @generated
	 */
	EClass getConnection();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Connection#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Source</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Connection#getSource()
	 * @see #getConnection()
	 * @generated
	 */
	EReference getConnection_Source();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Connection#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Target</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Connection#getTarget()
	 * @see #getConnection()
	 * @generated
	 */
	EReference getConnection_Target();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Connection#getStyle <em>Style</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Style</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Connection#getStyle()
	 * @see #getConnection()
	 * @generated
	 */
	EReference getConnection_Style();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.ConnectionStyle <em>Connection Style</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Connection Style</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.ConnectionStyle
	 * @generated
	 */
	EClass getConnectionStyle();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.ConnectionStyle#getStrokeColor <em>Stroke Color</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Stroke Color</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.ConnectionStyle#getStrokeColor()
	 * @see #getConnectionStyle()
	 * @generated
	 */
	EAttribute getConnectionStyle_StrokeColor();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Diagram <em>Diagram</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Diagram</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Diagram
	 * @generated
	 */
	EClass getDiagram();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Diagram#getCurrentLayer <em>Current Layer</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Current Layer</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Diagram#getCurrentLayer()
	 * @see #getDiagram()
	 * @generated
	 */
	EReference getDiagram_CurrentLayer();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Diagram#getKinds <em>Kinds</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Kinds</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Diagram#getKinds()
	 * @see #getDiagram()
	 * @generated
	 */
	EReference getDiagram_Kinds();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Diagram#getLayers <em>Layers</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Layers</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Diagram#getLayers()
	 * @see #getDiagram()
	 * @generated
	 */
	EReference getDiagram_Layers();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Diagram#getLayersHistory <em>Layers History</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Layers History</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Diagram#getLayersHistory()
	 * @see #getDiagram()
	 * @generated
	 */
	EReference getDiagram_LayersHistory();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Diagram#getRouter <em>Router</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Router</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Diagram#getRouter()
	 * @see #getDiagram()
	 * @generated
	 */
	EAttribute getDiagram_Router();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Layer <em>Layer</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Layer</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Layer
	 * @generated
	 */
	EClass getLayer();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Layer#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Layer#getId()
	 * @see #getLayer()
	 * @generated
	 */
	EAttribute getLayer_Id();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Layer#getShapes <em>Shapes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Shapes</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Layer#getShapes()
	 * @see #getLayer()
	 * @generated
	 */
	EReference getLayer_Shapes();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Layer#isWasLayouting <em>Was Layouting</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Was Layouting</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Layer#isWasLayouting()
	 * @see #getLayer()
	 * @generated
	 */
	EAttribute getLayer_WasLayouting();

	/**
	 * Returns the meta object for class '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Kind <em>Kind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Kind</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Kind
	 * @generated
	 */
	EClass getKind();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Kind#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Kind#getId()
	 * @see #getKind()
	 * @generated
	 */
	EAttribute getKind_Id();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Kind#getStyle <em>Style</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Style</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Kind#getStyle()
	 * @see #getKind()
	 * @generated
	 */
	EReference getKind_Style();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Kind#isUseCustomStyle <em>Use Custom Style</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Use Custom Style</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Kind#isUseCustomStyle()
	 * @see #getKind()
	 * @generated
	 */
	EAttribute getKind_UseCustomStyle();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Router <em>Router</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Router</em>'.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Router
	 * @generated
	 */
	EEnum getRouter();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DependenciesFactory getDependenciesFactory();

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
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.ShapeImpl <em>Shape</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.ShapeImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DependenciesPackageImpl#getShape()
		 * @generated
		 */
		EClass SHAPE = eINSTANCE.getShape();

		/**
		 * The meta object literal for the '<em><b>Location</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SHAPE__LOCATION = eINSTANCE.getShape_Location();

		/**
		 * The meta object literal for the '<em><b>Size</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SHAPE__SIZE = eINSTANCE.getShape_Size();

		/**
		 * The meta object literal for the '<em><b>Style</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SHAPE__STYLE = eINSTANCE.getShape_Style();

		/**
		 * The meta object literal for the '<em><b>Source Connections</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SHAPE__SOURCE_CONNECTIONS = eINSTANCE.getShape_SourceConnections();

		/**
		 * The meta object literal for the '<em><b>Target Connections</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SHAPE__TARGET_CONNECTIONS = eINSTANCE.getShape_TargetConnections();

		/**
		 * The meta object literal for the '<em><b>Parent Layer</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SHAPE__PARENT_LAYER = eINSTANCE.getShape_ParentLayer();

		/**
		 * The meta object literal for the '<em><b>Was Layouting</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SHAPE__WAS_LAYOUTING = eINSTANCE.getShape_WasLayouting();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.PointImpl <em>Point</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.PointImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DependenciesPackageImpl#getPoint()
		 * @generated
		 */
		EClass POINT = eINSTANCE.getPoint();

		/**
		 * The meta object literal for the '<em><b>X</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute POINT__X = eINSTANCE.getPoint_X();

		/**
		 * The meta object literal for the '<em><b>Y</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute POINT__Y = eINSTANCE.getPoint_Y();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DimensionImpl <em>Dimension</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DimensionImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DependenciesPackageImpl#getDimension()
		 * @generated
		 */
		EClass DIMENSION = eINSTANCE.getDimension();

		/**
		 * The meta object literal for the '<em><b>Width</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIMENSION__WIDTH = eINSTANCE.getDimension_Width();

		/**
		 * The meta object literal for the '<em><b>Height</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIMENSION__HEIGHT = eINSTANCE.getDimension_Height();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.SubjectImpl <em>Subject</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.SubjectImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DependenciesPackageImpl#getSubject()
		 * @generated
		 */
		EClass SUBJECT = eINSTANCE.getSubject();

		/**
		 * The meta object literal for the '<em><b>External Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUBJECT__EXTERNAL_ID = eINSTANCE.getSubject_ExternalId();

		/**
		 * The meta object literal for the '<em><b>Opened</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUBJECT__OPENED = eINSTANCE.getSubject_Opened();

		/**
		 * The meta object literal for the '<em><b>Use Custom Style</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUBJECT__USE_CUSTOM_STYLE = eINSTANCE.getSubject_UseCustomStyle();

		/**
		 * The meta object literal for the '<em><b>Kind</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SUBJECT__KIND = eINSTANCE.getSubject_Kind();

		/**
		 * The meta object literal for the '<em><b>Master</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUBJECT__MASTER = eINSTANCE.getSubject_Master();

		/**
		 * The meta object literal for the '<em><b>Loaded</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUBJECT__LOADED = eINSTANCE.getSubject_Loaded();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.NoteImpl <em>Note</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.NoteImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DependenciesPackageImpl#getNote()
		 * @generated
		 */
		EClass NOTE = eINSTANCE.getNote();

		/**
		 * The meta object literal for the '<em><b>Text</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NOTE__TEXT = eINSTANCE.getNote_Text();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.ShapeStyleImpl <em>Shape Style</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.ShapeStyleImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DependenciesPackageImpl#getShapeStyle()
		 * @generated
		 */
		EClass SHAPE_STYLE = eINSTANCE.getShapeStyle();

		/**
		 * The meta object literal for the '<em><b>Background Color</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SHAPE_STYLE__BACKGROUND_COLOR = eINSTANCE.getShapeStyle_BackgroundColor();

		/**
		 * The meta object literal for the '<em><b>Foreground Color</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SHAPE_STYLE__FOREGROUND_COLOR = eINSTANCE.getShapeStyle_ForegroundColor();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.ConnectionImpl <em>Connection</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.ConnectionImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DependenciesPackageImpl#getConnection()
		 * @generated
		 */
		EClass CONNECTION = eINSTANCE.getConnection();

		/**
		 * The meta object literal for the '<em><b>Source</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONNECTION__SOURCE = eINSTANCE.getConnection_Source();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONNECTION__TARGET = eINSTANCE.getConnection_Target();

		/**
		 * The meta object literal for the '<em><b>Style</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONNECTION__STYLE = eINSTANCE.getConnection_Style();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.ConnectionStyleImpl <em>Connection Style</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.ConnectionStyleImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DependenciesPackageImpl#getConnectionStyle()
		 * @generated
		 */
		EClass CONNECTION_STYLE = eINSTANCE.getConnectionStyle();

		/**
		 * The meta object literal for the '<em><b>Stroke Color</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONNECTION_STYLE__STROKE_COLOR = eINSTANCE.getConnectionStyle_StrokeColor();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DiagramImpl <em>Diagram</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DiagramImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DependenciesPackageImpl#getDiagram()
		 * @generated
		 */
		EClass DIAGRAM = eINSTANCE.getDiagram();

		/**
		 * The meta object literal for the '<em><b>Current Layer</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIAGRAM__CURRENT_LAYER = eINSTANCE.getDiagram_CurrentLayer();

		/**
		 * The meta object literal for the '<em><b>Kinds</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIAGRAM__KINDS = eINSTANCE.getDiagram_Kinds();

		/**
		 * The meta object literal for the '<em><b>Layers</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIAGRAM__LAYERS = eINSTANCE.getDiagram_Layers();

		/**
		 * The meta object literal for the '<em><b>Layers History</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DIAGRAM__LAYERS_HISTORY = eINSTANCE.getDiagram_LayersHistory();

		/**
		 * The meta object literal for the '<em><b>Router</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIAGRAM__ROUTER = eINSTANCE.getDiagram_Router();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.LayerImpl <em>Layer</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.LayerImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DependenciesPackageImpl#getLayer()
		 * @generated
		 */
		EClass LAYER = eINSTANCE.getLayer();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LAYER__ID = eINSTANCE.getLayer_Id();

		/**
		 * The meta object literal for the '<em><b>Shapes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LAYER__SHAPES = eINSTANCE.getLayer_Shapes();

		/**
		 * The meta object literal for the '<em><b>Was Layouting</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LAYER__WAS_LAYOUTING = eINSTANCE.getLayer_WasLayouting();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.KindImpl <em>Kind</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.KindImpl
		 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DependenciesPackageImpl#getKind()
		 * @generated
		 */
		EClass KIND = eINSTANCE.getKind();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KIND__ID = eINSTANCE.getKind_Id();

		/**
		 * The meta object literal for the '<em><b>Style</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference KIND__STYLE = eINSTANCE.getKind_Style();

		/**
		 * The meta object literal for the '<em><b>Use Custom Style</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KIND__USE_CUSTOM_STYLE = eINSTANCE.getKind_UseCustomStyle();

		/**
		 * The meta object literal for the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Router <em>Router</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Router
		 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.DependenciesPackageImpl#getRouter()
		 * @generated
		 */
		EEnum ROUTER = eINSTANCE.getRouter();

	}

} //DependenciesPackage
