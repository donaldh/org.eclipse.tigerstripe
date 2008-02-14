/**
 * <copyright>
 * </copyright>
 *
 * $Id: IArgument.java,v 1.1 2008/02/14 23:58:00 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IArgument</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IArgument#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IArgument#getDefaultValue <em>Default Value</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IArgument#isOrdered <em>Ordered</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IArgument#isUnique <em>Unique</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IArgument#getRefBy <em>Ref By</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIArgument()
 * @model
 * @generated
 */
public interface IArgument extends IModelComponent {
	/**
	 * Returns the value of the '<em><b>Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' containment reference.
	 * @see #setType(IType)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIArgument_Type()
	 * @model containment="true" required="true"
	 * @generated
	 */
	IType getType();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IArgument#getType <em>Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' containment reference.
	 * @see #getType()
	 * @generated
	 */
	void setType(IType value);

	/**
	 * Returns the value of the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Value</em>' attribute.
	 * @see #isSetDefaultValue()
	 * @see #unsetDefaultValue()
	 * @see #setDefaultValue(String)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIArgument_DefaultValue()
	 * @model unsettable="true"
	 * @generated
	 */
	String getDefaultValue();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IArgument#getDefaultValue <em>Default Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Value</em>' attribute.
	 * @see #isSetDefaultValue()
	 * @see #unsetDefaultValue()
	 * @see #getDefaultValue()
	 * @generated
	 */
	void setDefaultValue(String value);

	/**
	 * Unsets the value of the '{@link org.eclipse.tigerstripe.metamodel.IArgument#getDefaultValue <em>Default Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetDefaultValue()
	 * @see #getDefaultValue()
	 * @see #setDefaultValue(String)
	 * @generated
	 */
	void unsetDefaultValue();

	/**
	 * Returns whether the value of the '{@link org.eclipse.tigerstripe.metamodel.IArgument#getDefaultValue <em>Default Value</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Default Value</em>' attribute is set.
	 * @see #unsetDefaultValue()
	 * @see #getDefaultValue()
	 * @see #setDefaultValue(String)
	 * @generated
	 */
	boolean isSetDefaultValue();

	/**
	 * Returns the value of the '<em><b>Ordered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ordered</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ordered</em>' attribute.
	 * @see #setOrdered(boolean)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIArgument_Ordered()
	 * @model
	 * @generated
	 */
	boolean isOrdered();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IArgument#isOrdered <em>Ordered</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ordered</em>' attribute.
	 * @see #isOrdered()
	 * @generated
	 */
	void setOrdered(boolean value);

	/**
	 * Returns the value of the '<em><b>Unique</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Unique</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Unique</em>' attribute.
	 * @see #setUnique(boolean)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIArgument_Unique()
	 * @model
	 * @generated
	 */
	boolean isUnique();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IArgument#isUnique <em>Unique</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Unique</em>' attribute.
	 * @see #isUnique()
	 * @generated
	 */
	void setUnique(boolean value);

	/**
	 * Returns the value of the '<em><b>Ref By</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.tigerstripe.metamodel.ERefByEnum}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ref By</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ref By</em>' attribute.
	 * @see org.eclipse.tigerstripe.metamodel.ERefByEnum
	 * @see #setRefBy(ERefByEnum)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIArgument_RefBy()
	 * @model
	 * @generated
	 */
	ERefByEnum getRefBy();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IArgument#getRefBy <em>Ref By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ref By</em>' attribute.
	 * @see org.eclipse.tigerstripe.metamodel.ERefByEnum
	 * @see #getRefBy()
	 * @generated
	 */
	void setRefBy(ERefByEnum value);

} // IArgument
