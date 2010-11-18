/**
 * <copyright>
 * </copyright>
 *
 * $Id: Diagram.java,v 1.4 2010/11/18 17:00:38 ystrot Exp $
 */
package org.eclipse.tigerstripe.workbench.ui.model.dependencies;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diagram</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Diagram#getCurrentLayer <em>Current Layer</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Diagram#getKinds <em>Kinds</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Diagram#getLayers <em>Layers</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Diagram#getLayersHistory <em>Layers History</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Diagram#getRouter <em>Router</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage#getDiagram()
 * @model
 * @generated
 */
public interface Diagram extends EObject {
	/**
	 * Returns the value of the '<em><b>Current Layer</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Current Layer</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Current Layer</em>' reference.
	 * @see #setCurrentLayer(Layer)
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage#getDiagram_CurrentLayer()
	 * @model
	 * @generated
	 */
	Layer getCurrentLayer();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Diagram#getCurrentLayer <em>Current Layer</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Current Layer</em>' reference.
	 * @see #getCurrentLayer()
	 * @generated
	 */
	void setCurrentLayer(Layer value);

	/**
	 * Returns the value of the '<em><b>Kinds</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Kind}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Kinds</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Kinds</em>' containment reference list.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage#getDiagram_Kinds()
	 * @model containment="true"
	 * @generated
	 */
	EList<Kind> getKinds();

	/**
	 * Returns the value of the '<em><b>Layers</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Layer}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Layers</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Layers</em>' containment reference list.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage#getDiagram_Layers()
	 * @model containment="true"
	 * @generated
	 */
	EList<Layer> getLayers();

	/**
	 * Returns the value of the '<em><b>Layers History</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Layer}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Layers History</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Layers History</em>' reference list.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage#getDiagram_LayersHistory()
	 * @model
	 * @generated
	 */
	EList<Layer> getLayersHistory();

	/**
	 * Returns the value of the '<em><b>Router</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Router}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Router</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Router</em>' attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Router
	 * @see #setRouter(Router)
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage#getDiagram_Router()
	 * @model
	 * @generated
	 */
	Router getRouter();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.Diagram#getRouter <em>Router</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Router</em>' attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.model.dependencies.Router
	 * @see #getRouter()
	 * @generated
	 */
	void setRouter(Router value);

} // Diagram
