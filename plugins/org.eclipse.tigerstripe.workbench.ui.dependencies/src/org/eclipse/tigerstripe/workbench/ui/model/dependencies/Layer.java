/**
 * <copyright>
 * </copyright>
 *
 * $Id: Layer.java,v 1.4 2010/11/18 17:00:38 ystrot Exp $
 */
package org.eclipse.tigerstripe.workbench.ui.model.dependencies;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Layer</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Layer#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Layer#getShapes <em>Shapes</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Layer#isWasLayouting <em>Was Layouting</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage#getLayer()
 * @model
 * @generated
 */
public interface Layer extends EObject {
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage#getLayer_Id()
	 * @model
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Layer#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Shapes</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape#getParentLayer <em>Parent Layer</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Shapes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shapes</em>' containment reference list.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage#getLayer_Shapes()
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape#getParentLayer
	 * @model opposite="parentLayer" containment="true"
	 * @generated
	 */
	EList<Shape> getShapes();

	/**
	 * Returns the value of the '<em><b>Was Layouting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Was Layouting</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Was Layouting</em>' attribute.
	 * @see #setWasLayouting(boolean)
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage#getLayer_WasLayouting()
	 * @model
	 * @generated
	 */
	boolean isWasLayouting();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Layer#isWasLayouting <em>Was Layouting</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Was Layouting</em>' attribute.
	 * @see #isWasLayouting()
	 * @generated
	 */
	void setWasLayouting(boolean value);

} // Layer
