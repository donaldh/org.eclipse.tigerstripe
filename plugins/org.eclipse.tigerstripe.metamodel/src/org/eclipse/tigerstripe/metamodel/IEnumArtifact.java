/**
 * <copyright>
 * </copyright>
 *
 * $Id: IEnumArtifact.java,v 1.2 2008/05/22 18:26:30 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IEnum Artifact</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IEnumArtifact#getBaseType <em>Base Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIEnumArtifact()
 * @model
 * @generated
 */
public interface IEnumArtifact extends IAbstractArtifact {
	/**
	 * Returns the value of the '<em><b>Base Type</b></em>' attribute.
	 * The default value is <code>"int"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Type</em>' attribute.
	 * @see #setBaseType(String)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIEnumArtifact_BaseType()
	 * @model default="int"
	 * @generated
	 */
	String getBaseType();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IEnumArtifact#getBaseType <em>Base Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Type</em>' attribute.
	 * @see #getBaseType()
	 * @generated
	 */
	void setBaseType(String value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	String getMaxLiteral();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	String getMinLiteral();

} // IEnumArtifact
