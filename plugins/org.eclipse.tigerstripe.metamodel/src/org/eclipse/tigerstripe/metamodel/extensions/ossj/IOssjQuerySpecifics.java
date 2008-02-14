/**
 * <copyright>
 * </copyright>
 *
 * $Id: IOssjQuerySpecifics.java,v 1.1 2008/02/14 23:58:00 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.extensions.ossj;

import org.eclipse.tigerstripe.metamodel.IType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IOssj Query Specifics</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjQuerySpecifics#getReturnedEntityIType <em>Returned Entity IType</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjQuerySpecifics#isSingleExtensionType <em>Single Extension Type</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjQuerySpecifics#isSessionFactoryMethods <em>Session Factory Methods</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage#getIOssjQuerySpecifics()
 * @model
 * @generated
 */
public interface IOssjQuerySpecifics extends IOssjArtifactSpecifics {
	/**
	 * Returns the value of the '<em><b>Returned Entity IType</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Returned Entity IType</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Returned Entity IType</em>' reference.
	 * @see #setReturnedEntityIType(IType)
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage#getIOssjQuerySpecifics_ReturnedEntityIType()
	 * @model
	 * @generated
	 */
	IType getReturnedEntityIType();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjQuerySpecifics#getReturnedEntityIType <em>Returned Entity IType</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Returned Entity IType</em>' reference.
	 * @see #getReturnedEntityIType()
	 * @generated
	 */
	void setReturnedEntityIType(IType value);

	/**
	 * Returns the value of the '<em><b>Single Extension Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Single Extension Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Single Extension Type</em>' attribute.
	 * @see #setSingleExtensionType(boolean)
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage#getIOssjQuerySpecifics_SingleExtensionType()
	 * @model
	 * @generated
	 */
	boolean isSingleExtensionType();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjQuerySpecifics#isSingleExtensionType <em>Single Extension Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Single Extension Type</em>' attribute.
	 * @see #isSingleExtensionType()
	 * @generated
	 */
	void setSingleExtensionType(boolean value);

	/**
	 * Returns the value of the '<em><b>Session Factory Methods</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Session Factory Methods</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Session Factory Methods</em>' attribute.
	 * @see #setSessionFactoryMethods(boolean)
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage#getIOssjQuerySpecifics_SessionFactoryMethods()
	 * @model
	 * @generated
	 */
	boolean isSessionFactoryMethods();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjQuerySpecifics#isSessionFactoryMethods <em>Session Factory Methods</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Session Factory Methods</em>' attribute.
	 * @see #isSessionFactoryMethods()
	 * @generated
	 */
	void setSessionFactoryMethods(boolean value);

} // IOssjQuerySpecifics
