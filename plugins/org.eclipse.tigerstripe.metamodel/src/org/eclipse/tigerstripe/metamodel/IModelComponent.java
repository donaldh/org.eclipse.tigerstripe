/**
 * <copyright>
 * </copyright>
 *
 * $Id: IModelComponent.java,v 1.2 2008/02/22 20:01:22 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>IModel Component</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.metamodel.IModelComponent#getName <em>Name</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.metamodel.IModelComponent#getComment <em>Comment</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.metamodel.IModelComponent#getVisibility <em>Visibility</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIModelComponent()
 * @model abstract="true"
 * @generated
 */
public interface IModelComponent extends IStereotypeCapable {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIModelComponent_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IModelComponent#getName <em>Name</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Comment</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Comment</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Comment</em>' attribute.
	 * @see #setComment(String)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIModelComponent_Comment()
	 * @model
	 * @generated
	 */
	String getComment();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IModelComponent#getComment <em>Comment</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Comment</em>' attribute.
	 * @see #getComment()
	 * @generated
	 */
	void setComment(String value);

	/**
	 * Returns the value of the '<em><b>Visibility</b></em>' attribute. The
	 * literals are from the enumeration
	 * {@link org.eclipse.tigerstripe.metamodel.VisibilityEnum}. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Visibility</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Visibility</em>' attribute.
	 * @see org.eclipse.tigerstripe.metamodel.VisibilityEnum
	 * @see #setVisibility(VisibilityEnum)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIModelComponent_Visibility()
	 * @model
	 * @generated
	 */
	VisibilityEnum getVisibility();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IModelComponent#getVisibility <em>Visibility</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Visibility</em>' attribute.
	 * @see org.eclipse.tigerstripe.metamodel.VisibilityEnum
	 * @see #getVisibility()
	 * @generated
	 */
	void setVisibility(VisibilityEnum value);

	/**
	 * Gets the metadata for this artifact type.
	 * 
	 * This is not an EMF method.
	 * 
	 */
	public IModelComponentMetadata getMetadata();

} // IModelComponent
