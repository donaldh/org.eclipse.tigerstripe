/**
 * <copyright>
 * </copyright>
 *
 * $Id: IEntityMethodFlavorDetails.java,v 1.1 2008/02/14 23:58:00 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.tigerstripe.metamodel.extensions.ossj.EEntityMethodFlavorFlag;
import org.eclipse.tigerstripe.metamodel.extensions.ossj.EMethodType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IEntity Method Flavor Details</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails#getComment <em>Comment</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails#getFlag <em>Flag</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails#getExceptions <em>Exceptions</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails#getMethod <em>Method</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails#getFlavor <em>Flavor</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails#getMethodType <em>Method Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIEntityMethodFlavorDetails()
 * @model
 * @generated
 */
public interface IEntityMethodFlavorDetails extends EObject {
	/**
	 * Returns the value of the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Comment</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Comment</em>' attribute.
	 * @see #setComment(String)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIEntityMethodFlavorDetails_Comment()
	 * @model
	 * @generated
	 */
	String getComment();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails#getComment <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Comment</em>' attribute.
	 * @see #getComment()
	 * @generated
	 */
	void setComment(String value);

	/**
	 * Returns the value of the '<em><b>Flag</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.tigerstripe.metamodel.extensions.ossj.EEntityMethodFlavorFlag}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Flag</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Flag</em>' attribute.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.EEntityMethodFlavorFlag
	 * @see #setFlag(EEntityMethodFlavorFlag)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIEntityMethodFlavorDetails_Flag()
	 * @model
	 * @generated
	 */
	EEntityMethodFlavorFlag getFlag();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails#getFlag <em>Flag</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Flag</em>' attribute.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.EEntityMethodFlavorFlag
	 * @see #getFlag()
	 * @generated
	 */
	void setFlag(EEntityMethodFlavorFlag value);

	/**
	 * Returns the value of the '<em><b>Exceptions</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.metamodel.IType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Exceptions</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Exceptions</em>' reference list.
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIEntityMethodFlavorDetails_Exceptions()
	 * @model
	 * @generated
	 */
	EList<IType> getExceptions();

	/**
	 * Returns the value of the '<em><b>Method</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Method</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Method</em>' reference.
	 * @see #setMethod(IMethod)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIEntityMethodFlavorDetails_Method()
	 * @model
	 * @generated
	 */
	IMethod getMethod();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails#getMethod <em>Method</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Method</em>' reference.
	 * @see #getMethod()
	 * @generated
	 */
	void setMethod(IMethod value);

	/**
	 * Returns the value of the '<em><b>Flavor</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.tigerstripe.metamodel.OssjEntityMethodFlavor}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Flavor</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Flavor</em>' attribute.
	 * @see org.eclipse.tigerstripe.metamodel.OssjEntityMethodFlavor
	 * @see #setFlavor(OssjEntityMethodFlavor)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIEntityMethodFlavorDetails_Flavor()
	 * @model
	 * @generated
	 */
	OssjEntityMethodFlavor getFlavor();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails#getFlavor <em>Flavor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Flavor</em>' attribute.
	 * @see org.eclipse.tigerstripe.metamodel.OssjEntityMethodFlavor
	 * @see #getFlavor()
	 * @generated
	 */
	void setFlavor(OssjEntityMethodFlavor value);

	/**
	 * Returns the value of the '<em><b>Method Type</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.tigerstripe.metamodel.extensions.ossj.EMethodType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Method Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Method Type</em>' attribute.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.EMethodType
	 * @see #setMethodType(EMethodType)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIEntityMethodFlavorDetails_MethodType()
	 * @model
	 * @generated
	 */
	EMethodType getMethodType();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails#getMethodType <em>Method Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Method Type</em>' attribute.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.EMethodType
	 * @see #getMethodType()
	 * @generated
	 */
	void setMethodType(EMethodType value);

} // IEntityMethodFlavorDetails
