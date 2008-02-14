/**
 * <copyright>
 * </copyright>
 *
 * $Id: IManagedEntityArtifact.java,v 1.2 2008/05/22 18:26:30 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IManaged Entity Artifact</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact#getPrimaryKey <em>Primary Key</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIManagedEntityArtifact()
 * @model
 * @generated
 */
public interface IManagedEntityArtifact extends IAbstractArtifact {
	/**
	 * Returns the value of the '<em><b>Primary Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Primary Key</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Primary Key</em>' attribute.
	 * @see #setPrimaryKey(String)
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getIManagedEntityArtifact_PrimaryKey()
	 * @model
	 * @generated
	 */
	String getPrimaryKey();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact#getPrimaryKey <em>Primary Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Primary Key</em>' attribute.
	 * @see #getPrimaryKey()
	 * @generated
	 */
	void setPrimaryKey(String value);

} // IManagedEntityArtifact
