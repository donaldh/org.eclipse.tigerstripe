/**
 * <copyright>
 * </copyright>
 *
 * $Id: Shape.java,v 1.3 2010/11/17 18:57:49 ystrot Exp $
 */
package org.eclipse.tigerstripe.workbench.ui.model.dependencies;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Shape</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape#getLocation <em>Location</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape#getSize <em>Size</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape#getStyle <em>Style</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape#getSourceConnections <em>Source Connections</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape#getTargetConnections <em>Target Connections</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape#getParentLayer <em>Parent Layer</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage#getShape()
 * @model
 * @generated
 */
public interface Shape extends EObject {
	/**
	 * Returns the value of the '<em><b>Location</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Location</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Location</em>' containment reference.
	 * @see #setLocation(Point)
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage#getShape_Location()
	 * @model containment="true"
	 * @generated
	 */
	Point getLocation();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape#getLocation <em>Location</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Location</em>' containment reference.
	 * @see #getLocation()
	 * @generated
	 */
	void setLocation(Point value);

	/**
	 * Returns the value of the '<em><b>Size</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Size</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Size</em>' containment reference.
	 * @see #setSize(Dimension)
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage#getShape_Size()
	 * @model containment="true"
	 * @generated
	 */
	Dimension getSize();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape#getSize <em>Size</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Size</em>' containment reference.
	 * @see #getSize()
	 * @generated
	 */
	void setSize(Dimension value);

	/**
	 * Returns the value of the '<em><b>Style</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Style</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Style</em>' containment reference.
	 * @see #setStyle(ShapeStyle)
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage#getShape_Style()
	 * @model containment="true"
	 * @generated
	 */
	ShapeStyle getStyle();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape#getStyle <em>Style</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Style</em>' containment reference.
	 * @see #getStyle()
	 * @generated
	 */
	void setStyle(ShapeStyle value);

	/**
	 * Returns the value of the '<em><b>Source Connections</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Connection}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source Connections</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source Connections</em>' reference list.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage#getShape_SourceConnections()
	 * @model
	 * @generated
	 */
	EList<Connection> getSourceConnections();

	/**
	 * Returns the value of the '<em><b>Target Connections</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Connection}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target Connections</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target Connections</em>' containment reference list.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage#getShape_TargetConnections()
	 * @model containment="true"
	 * @generated
	 */
	EList<Connection> getTargetConnections();

	/**
	 * Returns the value of the '<em><b>Parent Layer</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Layer#getShapes <em>Shapes</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parent Layer</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent Layer</em>' container reference.
	 * @see #setParentLayer(Layer)
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage#getShape_ParentLayer()
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Layer#getShapes
	 * @model opposite="shapes" transient="false"
	 * @generated
	 */
	Layer getParentLayer();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape#getParentLayer <em>Parent Layer</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent Layer</em>' container reference.
	 * @see #getParentLayer()
	 * @generated
	 */
	void setParentLayer(Layer value);

} // Shape
