/**
 * <copyright>
 * </copyright>
 *
 * $Id: IOssjEnumSpecifics.java,v 1.1 2008/02/14 23:58:00 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.extensions.ossj;

import org.eclipse.tigerstripe.metamodel.IType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IOssj Enum Specifics</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEnumSpecifics#isExtensible <em>Extensible</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEnumSpecifics#getBaseIType <em>Base IType</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage#getIOssjEnumSpecifics()
 * @model
 * @generated
 */
public interface IOssjEnumSpecifics extends IOssjArtifactSpecifics {
	/**
	 * Returns the value of the '<em><b>Extensible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Extensible</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extensible</em>' attribute.
	 * @see #setExtensible(boolean)
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage#getIOssjEnumSpecifics_Extensible()
	 * @model
	 * @generated
	 */
	boolean isExtensible();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEnumSpecifics#isExtensible <em>Extensible</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Extensible</em>' attribute.
	 * @see #isExtensible()
	 * @generated
	 */
	void setExtensible(boolean value);

	/**
	 * Returns the value of the '<em><b>Base IType</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base IType</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base IType</em>' reference.
	 * @see #setBaseIType(IType)
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage#getIOssjEnumSpecifics_BaseIType()
	 * @model
	 * @generated
	 */
	IType getBaseIType();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEnumSpecifics#getBaseIType <em>Base IType</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base IType</em>' reference.
	 * @see #getBaseIType()
	 * @generated
	 */
	void setBaseIType(IType value);

} // IOssjEnumSpecifics
