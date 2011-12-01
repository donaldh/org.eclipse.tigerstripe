/**
 * <copyright>
 * </copyright>
 *
 * $Id: ModelReference.java,v 1.2 2011/12/01 15:24:06 verastov Exp $
 */
package org.eclipse.tigerstripe.workbench.annotation.modelReference;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.workbench.annotation.modelReference.ModelReference#getUri <em>Uri</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.workbench.annotation.modelReference.ModelReferencePackage#getModelReference()
 * @model
 * @generated
 */
public interface ModelReference extends EObject {
	/**
	 * Returns the value of the '<em><b>Uri</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Uri</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Uri</em>' attribute.
	 * @see #setUri(String)
	 * @see org.eclipse.tigerstripe.workbench.annotation.modelReference.ModelReferencePackage#getModelReference_Uri()
	 * @model
	 * @generated
	 */
	String getUri();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.annotation.modelReference.ModelReference#getUri <em>Uri</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Uri</em>' attribute.
	 * @see #getUri()
	 * @generated
	 */
	void setUri(String value);

	IModelComponent resolve();

} // ModelReference
