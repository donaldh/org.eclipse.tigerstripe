/**
 * <copyright>
 * </copyright>
 *
 * $Id: IOssjEntitySpecifics.java,v 1.1 2008/02/14 23:58:00 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.extensions.ossj;

import org.eclipse.emf.common.util.EList;

import org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails;
import org.eclipse.tigerstripe.metamodel.OssjEntityMethodFlavor;

import org.eclipse.tigerstripe.metamodel.extensions.IProperties;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IOssj Entity Specifics</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEntitySpecifics#getFlavorDetails <em>Flavor Details</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEntitySpecifics#getPrimaryKey <em>Primary Key</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEntitySpecifics#getExtensibilityType <em>Extensibility Type</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEntitySpecifics#isSessionFactoryMethods <em>Session Factory Methods</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEntitySpecifics#getInterfaceKeyProperties <em>Interface Key Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage#getIOssjEntitySpecifics()
 * @model
 * @generated
 */
public interface IOssjEntitySpecifics extends IOssjArtifactSpecifics {
	/**
	 * Returns the value of the '<em><b>Flavor Details</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Flavor Details</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Flavor Details</em>' reference list.
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage#getIOssjEntitySpecifics_FlavorDetails()
	 * @model
	 * @generated
	 */
	EList<IEntityMethodFlavorDetails> getFlavorDetails();

	/**
	 * Returns the value of the '<em><b>Primary Key</b></em>' attribute.
	 * The default value is <code>"String"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Primary Key</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Primary Key</em>' attribute.
	 * @see #setPrimaryKey(String)
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage#getIOssjEntitySpecifics_PrimaryKey()
	 * @model default="String"
	 * @generated
	 */
	String getPrimaryKey();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEntitySpecifics#getPrimaryKey <em>Primary Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Primary Key</em>' attribute.
	 * @see #getPrimaryKey()
	 * @generated
	 */
	void setPrimaryKey(String value);

	/**
	 * Returns the value of the '<em><b>Extensibility Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Extensibility Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extensibility Type</em>' attribute.
	 * @see #setExtensibilityType(String)
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage#getIOssjEntitySpecifics_ExtensibilityType()
	 * @model
	 * @generated
	 */
	String getExtensibilityType();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEntitySpecifics#getExtensibilityType <em>Extensibility Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Extensibility Type</em>' attribute.
	 * @see #getExtensibilityType()
	 * @generated
	 */
	void setExtensibilityType(String value);

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
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage#getIOssjEntitySpecifics_SessionFactoryMethods()
	 * @model
	 * @generated
	 */
	boolean isSessionFactoryMethods();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEntitySpecifics#isSessionFactoryMethods <em>Session Factory Methods</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Session Factory Methods</em>' attribute.
	 * @see #isSessionFactoryMethods()
	 * @generated
	 */
	void setSessionFactoryMethods(boolean value);

	/**
	 * Returns the value of the '<em><b>Interface Key Properties</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Interface Key Properties</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Interface Key Properties</em>' reference.
	 * @see #setInterfaceKeyProperties(IProperties)
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage#getIOssjEntitySpecifics_InterfaceKeyProperties()
	 * @model
	 * @generated
	 */
	IProperties getInterfaceKeyProperties();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEntitySpecifics#getInterfaceKeyProperties <em>Interface Key Properties</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Interface Key Properties</em>' reference.
	 * @see #getInterfaceKeyProperties()
	 * @generated
	 */
	void setInterfaceKeyProperties(IProperties value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	IEntityMethodFlavorDetails getCRUDFlavorDetails(int crudID, OssjEntityMethodFlavor flavor);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void setCRUDFlavorDetails(int crudID, OssjEntityMethodFlavor flavor, IEntityMethodFlavorDetails details);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	EList<OssjEntityMethodFlavor> getSupportedFlavors(int crudID);

} // IOssjEntitySpecifics
