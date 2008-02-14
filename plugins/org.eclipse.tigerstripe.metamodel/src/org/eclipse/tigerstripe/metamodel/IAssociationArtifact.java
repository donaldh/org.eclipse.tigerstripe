/**
 * <copyright>
 * </copyright>
 *
 * $Id: IAssociationArtifact.java,v 1.2 2008/05/22 18:26:30 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IAssociation Artifact</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IAssociationArtifact#getAEnd <em>AEnd</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IAssociationArtifact#getZEnd <em>ZEnd</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIAssociationArtifact()
 * @model
 * @generated
 */
public interface IAssociationArtifact extends IAbstractArtifact {
	/**
	 * Returns the value of the '<em><b>AEnd</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>AEnd</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>AEnd</em>' containment reference.
	 * @see #setAEnd(IAssociationEnd)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIAssociationArtifact_AEnd()
	 * @model containment="true" required="true"
	 * @generated
	 */
	IAssociationEnd getAEnd();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IAssociationArtifact#getAEnd <em>AEnd</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>AEnd</em>' containment reference.
	 * @see #getAEnd()
	 * @generated
	 */
	void setAEnd(IAssociationEnd value);

	/**
	 * Returns the value of the '<em><b>ZEnd</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ZEnd</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>ZEnd</em>' reference.
	 * @see #setZEnd(IAssociationEnd)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIAssociationArtifact_ZEnd()
	 * @model required="true"
	 * @generated
	 */
	IAssociationEnd getZEnd();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IAssociationArtifact#getZEnd <em>ZEnd</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>ZEnd</em>' reference.
	 * @see #getZEnd()
	 * @generated
	 */
	void setZEnd(IAssociationEnd value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EList<IAssociationEnd> getAssociationEnds();

} // IAssociationArtifact
