/**
 * <copyright>
 * </copyright>
 *
 * $Id: IStereotypeCapable.java,v 1.2 2008/05/22 18:26:30 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IStereotype Capable</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IStereotypeCapable#getStereotypeInstances <em>Stereotype Instances</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIStereotypeCapable()
 * @model
 * @generated
 */
public interface IStereotypeCapable extends EObject {
	/**
	 * Returns the value of the '<em><b>Stereotype Instances</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.metamodel.IStereotypeInstance}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Stereotype Instances</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Stereotype Instances</em>' reference list.
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIStereotypeCapable_StereotypeInstances()
	 * @model
	 * @generated
	 */
	EList<IStereotypeInstance> getStereotypeInstances();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	IStereotypeInstance getStereotypeInstanceByName(String name);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	boolean hasStereotypeInstance(String name);

} // IStereotypeCapable
