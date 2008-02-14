/**
 * <copyright>
 * </copyright>
 *
 * $Id: IStereotypeInstance.java,v 1.2 2008/05/22 18:26:30 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IStereotype Instance</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IStereotypeInstance#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IStereotypeInstance#getAttributeValues <em>Attribute Values</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIStereotypeInstance()
 * @model
 * @generated
 */
public interface IStereotypeInstance extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIStereotypeInstance_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IStereotypeInstance#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Attribute Values</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.metamodel.IStereotypeAttributeValue}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Attribute Values</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attribute Values</em>' containment reference list.
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIStereotypeInstance_AttributeValues()
	 * @model containment="true"
	 * @generated
	 */
	EList<IStereotypeAttributeValue> getAttributeValues();

} // IStereotypeInstance
