/**
 * <copyright>
 * </copyright>
 *
 * $Id: ISessionArtifact.java,v 1.2 2008/05/22 18:26:30 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>ISession Artifact</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.ISessionArtifact#getManagedEntities <em>Managed Entities</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.ISessionArtifact#getEmittedNotifications <em>Emitted Notifications</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.ISessionArtifact#getSupportedNamedQueries <em>Supported Named Queries</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.ISessionArtifact#getExposedUpdateProcedures <em>Exposed Update Procedures</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getISessionArtifact()
 * @model
 * @generated
 */
public interface ISessionArtifact extends IAbstractArtifact {
	/**
	 * Returns the value of the '<em><b>Managed Entities</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Managed Entities</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Managed Entities</em>' reference list.
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getISessionArtifact_ManagedEntities()
	 * @model
	 * @generated
	 */
	EList<IManagedEntityArtifact> getManagedEntities();

	/**
	 * Returns the value of the '<em><b>Emitted Notifications</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.metamodel.IEventArtifact}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Emitted Notifications</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Emitted Notifications</em>' reference list.
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getISessionArtifact_EmittedNotifications()
	 * @model
	 * @generated
	 */
	EList<IEventArtifact> getEmittedNotifications();

	/**
	 * Returns the value of the '<em><b>Supported Named Queries</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.metamodel.IQueryArtifact}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Supported Named Queries</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Supported Named Queries</em>' reference list.
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getISessionArtifact_SupportedNamedQueries()
	 * @model
	 * @generated
	 */
	EList<IQueryArtifact> getSupportedNamedQueries();

	/**
	 * Returns the value of the '<em><b>Exposed Update Procedures</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.tigerstripe.metamodel.IUpdateProcedureArtifact}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Exposed Update Procedures</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Exposed Update Procedures</em>' reference list.
	 * @see org.eclipse.tigerstripe.metamodel.MetamodelPackage#getISessionArtifact_ExposedUpdateProcedures()
	 * @model
	 * @generated
	 */
	EList<IUpdateProcedureArtifact> getExposedUpdateProcedures();

} // ISessionArtifact
