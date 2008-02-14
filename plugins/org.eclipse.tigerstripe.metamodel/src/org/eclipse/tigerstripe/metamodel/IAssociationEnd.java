/**
 * <copyright>
 * </copyright>
 *
 * $Id: IAssociationEnd.java,v 1.2 2008/05/22 18:26:30 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IAssociation End</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IAssociationEnd#getAggregation <em>Aggregation</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IAssociationEnd#getChangeable <em>Changeable</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IAssociationEnd#isNavigable <em>Navigable</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IAssociationEnd#isOrdered <em>Ordered</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IAssociationEnd#isUnique <em>Unique</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IAssociationEnd#getMultiplicity <em>Multiplicity</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IAssociationEnd#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIAssociationEnd()
 * @model
 * @generated
 */
public interface IAssociationEnd extends IModelComponent {
	/**
	 * Returns the value of the '<em><b>Aggregation</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.tigerstripe.metamodel.EAggregationEnum}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Aggregation</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Aggregation</em>' attribute.
	 * @see org.eclipse.tigerstripe.metamodel.EAggregationEnum
	 * @see #setAggregation(EAggregationEnum)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIAssociationEnd_Aggregation()
	 * @model required="true"
	 * @generated
	 */
	EAggregationEnum getAggregation();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IAssociationEnd#getAggregation <em>Aggregation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Aggregation</em>' attribute.
	 * @see org.eclipse.tigerstripe.metamodel.EAggregationEnum
	 * @see #getAggregation()
	 * @generated
	 */
	void setAggregation(EAggregationEnum value);

	/**
	 * Returns the value of the '<em><b>Changeable</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.tigerstripe.metamodel.EChangeableEnum}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Changeable</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Changeable</em>' attribute.
	 * @see org.eclipse.tigerstripe.metamodel.EChangeableEnum
	 * @see #setChangeable(EChangeableEnum)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIAssociationEnd_Changeable()
	 * @model
	 * @generated
	 */
	EChangeableEnum getChangeable();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IAssociationEnd#getChangeable <em>Changeable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Changeable</em>' attribute.
	 * @see org.eclipse.tigerstripe.metamodel.EChangeableEnum
	 * @see #getChangeable()
	 * @generated
	 */
	void setChangeable(EChangeableEnum value);

	/**
	 * Returns the value of the '<em><b>Navigable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Navigable</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Navigable</em>' attribute.
	 * @see #setNavigable(boolean)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIAssociationEnd_Navigable()
	 * @model required="true"
	 * @generated
	 */
	boolean isNavigable();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IAssociationEnd#isNavigable <em>Navigable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Navigable</em>' attribute.
	 * @see #isNavigable()
	 * @generated
	 */
	void setNavigable(boolean value);

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
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIAssociationEnd_Ordered()
	 * @model required="true"
	 * @generated
	 */
	boolean isOrdered();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IAssociationEnd#isOrdered <em>Ordered</em>}' attribute.
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
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIAssociationEnd_Unique()
	 * @model required="true"
	 * @generated
	 */
	boolean isUnique();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IAssociationEnd#isUnique <em>Unique</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Unique</em>' attribute.
	 * @see #isUnique()
	 * @generated
	 */
	void setUnique(boolean value);

	/**
	 * Returns the value of the '<em><b>Multiplicity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Multiplicity</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Multiplicity</em>' reference.
	 * @see #setMultiplicity(IMultiplicity)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIAssociationEnd_Multiplicity()
	 * @model
	 * @generated
	 */
	IMultiplicity getMultiplicity();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IAssociationEnd#getMultiplicity <em>Multiplicity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Multiplicity</em>' reference.
	 * @see #getMultiplicity()
	 * @generated
	 */
	void setMultiplicity(IMultiplicity value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' reference.
	 * @see #setType(IAbstractArtifact)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIAssociationEnd_Type()
	 * @model
	 * @generated
	 */
	IAbstractArtifact getType();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IAssociationEnd#getType <em>Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' reference.
	 * @see #getType()
	 * @generated
	 */
	void setType(IAbstractArtifact value);

} // IAssociationEnd
