/**
 * <copyright>
 * </copyright>
 *
 * $Id: DependenciesFactoryImpl.java,v 1.3 2010/11/17 18:57:49 ystrot Exp $
 */
package org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.tigerstripe.workbench.ui.model.dependencies.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DependenciesFactoryImpl extends EFactoryImpl implements DependenciesFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static DependenciesFactory init() {
		try {
			DependenciesFactory theDependenciesFactory = (DependenciesFactory)EPackage.Registry.INSTANCE.getEFactory("http:///org/eclipse/tigerstripe/dependencies.ecore"); 
			if (theDependenciesFactory != null) {
				return theDependenciesFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new DependenciesFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DependenciesFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case DependenciesPackage.SHAPE: return createShape();
			case DependenciesPackage.POINT: return createPoint();
			case DependenciesPackage.DIMENSION: return createDimension();
			case DependenciesPackage.SUBJECT: return createSubject();
			case DependenciesPackage.NOTE: return createNote();
			case DependenciesPackage.SHAPE_STYLE: return createShapeStyle();
			case DependenciesPackage.CONNECTION: return createConnection();
			case DependenciesPackage.CONNECTION_STYLE: return createConnectionStyle();
			case DependenciesPackage.DIAGRAM: return createDiagram();
			case DependenciesPackage.LAYER: return createLayer();
			case DependenciesPackage.KIND: return createKind();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Shape createShape() {
		ShapeImpl shape = new ShapeImpl();
		return shape;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Point createPoint() {
		PointImpl point = new PointImpl();
		return point;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Dimension createDimension() {
		DimensionImpl dimension = new DimensionImpl();
		return dimension;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Subject createSubject() {
		SubjectImpl subject = new SubjectImpl();
		return subject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Note createNote() {
		NoteImpl note = new NoteImpl();
		return note;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ShapeStyle createShapeStyle() {
		ShapeStyleImpl shapeStyle = new ShapeStyleImpl();
		return shapeStyle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Connection createConnection() {
		ConnectionImpl connection = new ConnectionImpl();
		return connection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConnectionStyle createConnectionStyle() {
		ConnectionStyleImpl connectionStyle = new ConnectionStyleImpl();
		return connectionStyle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Diagram createDiagram() {
		DiagramImpl diagram = new DiagramImpl();
		return diagram;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Layer createLayer() {
		LayerImpl layer = new LayerImpl();
		return layer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Kind createKind() {
		KindImpl kind = new KindImpl();
		return kind;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DependenciesPackage getDependenciesPackage() {
		return (DependenciesPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static DependenciesPackage getPackage() {
		return DependenciesPackage.eINSTANCE;
	}

} //DependenciesFactoryImpl
