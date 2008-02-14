/**
 * <copyright>
 * </copyright>
 *
 * $Id: IOssjArtifactSpecifics.java,v 1.1 2008/02/14 23:58:00 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.extensions.ossj;

import org.eclipse.tigerstripe.metamodel.extensions.IProperties;
import org.eclipse.tigerstripe.metamodel.extensions.IStandardSpecifics;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IOssj Artifact Specifics</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjArtifactSpecifics#getInterfaceProperties <em>Interface Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage#getIOssjArtifactSpecifics()
 * @model
 * @generated
 */
public interface IOssjArtifactSpecifics extends IStandardSpecifics {
	/**
	 * Returns the value of the '<em><b>Interface Properties</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Interface Properties</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Interface Properties</em>' reference.
	 * @see #setInterfaceProperties(IProperties)
	 * @see org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage#getIOssjArtifactSpecifics_InterfaceProperties()
	 * @model
	 * @generated
	 */
	IProperties getInterfaceProperties();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjArtifactSpecifics#getInterfaceProperties <em>Interface Properties</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Interface Properties</em>' reference.
	 * @see #getInterfaceProperties()
	 * @generated
	 */
	void setInterfaceProperties(IProperties value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void mergeInterfaceProperties(IProperties interfaceProperties);

} // IOssjArtifactSpecifics
