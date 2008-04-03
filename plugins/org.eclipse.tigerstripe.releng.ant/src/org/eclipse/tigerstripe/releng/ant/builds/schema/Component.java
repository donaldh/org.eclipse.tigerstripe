/**
 * <copyright>
 * </copyright>
 *
 * $Id: Component.java,v 1.1 2008/04/03 21:45:31 edillon Exp $
 */
package org.eclipse.tigerstripe.releng.ant.builds.schema;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Component</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.releng.ant.builds.schema.Component#getBundle <em>Bundle</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.BuildSchemaPackage#getComponent()
 * @model extendedMetaData="name='component' kind='elementOnly'"
 * @generated
 */
public interface Component extends BuildElement {
	/**
	 * Returns the value of the '<em><b>Bundle</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.releng.ant.builds.schema.Bundle}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bundle</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bundle</em>' containment reference list.
	 * @see org.eclipse.tigerstripe.releng.ant.builds.schema.BuildSchemaPackage#getComponent_Bundle()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='bundle' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<Bundle> getBundle();

} // Component
