/**
 * <copyright>
 * </copyright>
 *
 * $Id: ConnectionStyle.java,v 1.4 2010/11/18 17:00:38 ystrot Exp $
 */
package org.eclipse.tigerstripe.workbench.ui.model.dependencies;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Connection Style</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.ConnectionStyle#getStrokeColor <em>Stroke Color</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage#getConnectionStyle()
 * @model
 * @generated
 */
public interface ConnectionStyle extends EObject {
	/**
	 * Returns the value of the '<em><b>Stroke Color</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Stroke Color</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Stroke Color</em>' attribute.
	 * @see #setStrokeColor(int)
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage#getConnectionStyle_StrokeColor()
	 * @model
	 * @generated
	 */
	int getStrokeColor();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.ConnectionStyle#getStrokeColor <em>Stroke Color</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Stroke Color</em>' attribute.
	 * @see #getStrokeColor()
	 * @generated
	 */
	void setStrokeColor(int value);

} // ConnectionStyle
