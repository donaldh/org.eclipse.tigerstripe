/**
 * <copyright>
 * </copyright>
 *
 * $Id: IQualifiedNamedComponent.java,v 1.2 2008/05/22 18:26:30 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IQualified Named Component</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IQualifiedNamedComponent#getPackage <em>Package</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIQualifiedNamedComponent()
 * @model abstract="true"
 * @generated
 */
public interface IQualifiedNamedComponent extends IModelComponent {
	/**
	 * Returns the value of the '<em><b>Package</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Package</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Package</em>' attribute.
	 * @see #setPackage(String)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIQualifiedNamedComponent_Package()
	 * @model
	 * @generated
	 */
	String getPackage();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IQualifiedNamedComponent#getPackage <em>Package</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Package</em>' attribute.
	 * @see #getPackage()
	 * @generated
	 */
	void setPackage(String value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	String getFullyQualifiedName();

} // IQualifiedNamedComponent
