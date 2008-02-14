/**
 * <copyright>
 * </copyright>
 *
 * $Id: IEventDescriptorEntry.java,v 1.1 2008/02/14 23:58:00 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.extensions.ossj;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IEvent Descriptor Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IEventDescriptorEntry#getLabel <em>Label</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IEventDescriptorEntry#getPrimitiveType <em>Primitive Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage#getIEventDescriptorEntry()
 * @model
 * @generated
 */
public interface IEventDescriptorEntry extends EObject {
	/**
	 * Returns the value of the '<em><b>Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Label</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Label</em>' attribute.
	 * @see #setLabel(String)
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage#getIEventDescriptorEntry_Label()
	 * @model
	 * @generated
	 */
	String getLabel();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IEventDescriptorEntry#getLabel <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Label</em>' attribute.
	 * @see #getLabel()
	 * @generated
	 */
	void setLabel(String value);

	/**
	 * Returns the value of the '<em><b>Primitive Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Primitive Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Primitive Type</em>' attribute.
	 * @see #setPrimitiveType(String)
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage#getIEventDescriptorEntry_PrimitiveType()
	 * @model
	 * @generated
	 */
	String getPrimitiveType();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IEventDescriptorEntry#getPrimitiveType <em>Primitive Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Primitive Type</em>' attribute.
	 * @see #getPrimitiveType()
	 * @generated
	 */
	void setPrimitiveType(String value);

} // IEventDescriptorEntry
