/**
 * <copyright>
 * </copyright>
 *
 * $Id: IProperties.java,v 1.1 2008/02/14 23:58:01 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.extensions;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IProperties</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.IProperties#getEntries <em>Entries</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.metamodel.extensions.ExtensionsPackage#getIProperties()
 * @model
 * @generated
 */
public interface IProperties extends EObject {
	/**
	 * Returns the value of the '<em><b>Entries</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entries</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entries</em>' reference.
	 * @see #setEntries(IProperty)
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ExtensionsPackage#getIProperties_Entries()
	 * @model
	 * @generated
	 */
	IProperty getEntries();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.extensions.IProperties#getEntries <em>Entries</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entries</em>' reference.
	 * @see #getEntries()
	 * @generated
	 */
	void setEntries(IProperty value);

} // IProperties
