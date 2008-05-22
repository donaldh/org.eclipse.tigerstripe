/**
 * <copyright>
 * </copyright>
 *
 * $Id: ISessionArtifactImpl.java,v 1.2 2008/05/22 18:26:28 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.impl;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.eclipse.tigerstripe.metamodel.IEventArtifact;
import org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact;
import org.eclipse.tigerstripe.metamodel.IQueryArtifact;
import org.eclipse.tigerstripe.metamodel.ISessionArtifact;
import org.eclipse.tigerstripe.metamodel.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.metamodel.MetamodelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>ISession Artifact</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.ISessionArtifactImpl#getManagedEntities <em>Managed Entities</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.ISessionArtifactImpl#getEmittedNotifications <em>Emitted Notifications</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.ISessionArtifactImpl#getSupportedNamedQueries <em>Supported Named Queries</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.ISessionArtifactImpl#getExposedUpdateProcedures <em>Exposed Update Procedures</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ISessionArtifactImpl extends IAbstractArtifactImpl implements ISessionArtifact {
	/**
	 * The cached value of the '{@link #getManagedEntities() <em>Managed Entities</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getManagedEntities()
	 * @generated
	 * @ordered
	 */
	protected EList<IManagedEntityArtifact> managedEntities;

	/**
	 * The cached value of the '{@link #getEmittedNotifications() <em>Emitted Notifications</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEmittedNotifications()
	 * @generated
	 * @ordered
	 */
	protected EList<IEventArtifact> emittedNotifications;

	/**
	 * The cached value of the '{@link #getSupportedNamedQueries() <em>Supported Named Queries</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSupportedNamedQueries()
	 * @generated
	 * @ordered
	 */
	protected EList<IQueryArtifact> supportedNamedQueries;

	/**
	 * The cached value of the '{@link #getExposedUpdateProcedures() <em>Exposed Update Procedures</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExposedUpdateProcedures()
	 * @generated
	 * @ordered
	 */
	protected EList<IUpdateProcedureArtifact> exposedUpdateProcedures;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ISessionArtifactImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetamodelPackage.Literals.ISESSION_ARTIFACT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IManagedEntityArtifact> getManagedEntities() {
		if (managedEntities == null) {
			managedEntities = new EObjectResolvingEList<IManagedEntityArtifact>(IManagedEntityArtifact.class, this, MetamodelPackage.ISESSION_ARTIFACT__MANAGED_ENTITIES);
		}
		return managedEntities;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IEventArtifact> getEmittedNotifications() {
		if (emittedNotifications == null) {
			emittedNotifications = new EObjectResolvingEList<IEventArtifact>(IEventArtifact.class, this, MetamodelPackage.ISESSION_ARTIFACT__EMITTED_NOTIFICATIONS);
		}
		return emittedNotifications;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IQueryArtifact> getSupportedNamedQueries() {
		if (supportedNamedQueries == null) {
			supportedNamedQueries = new EObjectResolvingEList<IQueryArtifact>(IQueryArtifact.class, this, MetamodelPackage.ISESSION_ARTIFACT__SUPPORTED_NAMED_QUERIES);
		}
		return supportedNamedQueries;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IUpdateProcedureArtifact> getExposedUpdateProcedures() {
		if (exposedUpdateProcedures == null) {
			exposedUpdateProcedures = new EObjectResolvingEList<IUpdateProcedureArtifact>(IUpdateProcedureArtifact.class, this, MetamodelPackage.ISESSION_ARTIFACT__EXPOSED_UPDATE_PROCEDURES);
		}
		return exposedUpdateProcedures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MetamodelPackage.ISESSION_ARTIFACT__MANAGED_ENTITIES:
				return getManagedEntities();
			case MetamodelPackage.ISESSION_ARTIFACT__EMITTED_NOTIFICATIONS:
				return getEmittedNotifications();
			case MetamodelPackage.ISESSION_ARTIFACT__SUPPORTED_NAMED_QUERIES:
				return getSupportedNamedQueries();
			case MetamodelPackage.ISESSION_ARTIFACT__EXPOSED_UPDATE_PROCEDURES:
				return getExposedUpdateProcedures();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case MetamodelPackage.ISESSION_ARTIFACT__MANAGED_ENTITIES:
				getManagedEntities().clear();
				getManagedEntities().addAll((Collection<? extends IManagedEntityArtifact>)newValue);
				return;
			case MetamodelPackage.ISESSION_ARTIFACT__EMITTED_NOTIFICATIONS:
				getEmittedNotifications().clear();
				getEmittedNotifications().addAll((Collection<? extends IEventArtifact>)newValue);
				return;
			case MetamodelPackage.ISESSION_ARTIFACT__SUPPORTED_NAMED_QUERIES:
				getSupportedNamedQueries().clear();
				getSupportedNamedQueries().addAll((Collection<? extends IQueryArtifact>)newValue);
				return;
			case MetamodelPackage.ISESSION_ARTIFACT__EXPOSED_UPDATE_PROCEDURES:
				getExposedUpdateProcedures().clear();
				getExposedUpdateProcedures().addAll((Collection<? extends IUpdateProcedureArtifact>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case MetamodelPackage.ISESSION_ARTIFACT__MANAGED_ENTITIES:
				getManagedEntities().clear();
				return;
			case MetamodelPackage.ISESSION_ARTIFACT__EMITTED_NOTIFICATIONS:
				getEmittedNotifications().clear();
				return;
			case MetamodelPackage.ISESSION_ARTIFACT__SUPPORTED_NAMED_QUERIES:
				getSupportedNamedQueries().clear();
				return;
			case MetamodelPackage.ISESSION_ARTIFACT__EXPOSED_UPDATE_PROCEDURES:
				getExposedUpdateProcedures().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case MetamodelPackage.ISESSION_ARTIFACT__MANAGED_ENTITIES:
				return managedEntities != null && !managedEntities.isEmpty();
			case MetamodelPackage.ISESSION_ARTIFACT__EMITTED_NOTIFICATIONS:
				return emittedNotifications != null && !emittedNotifications.isEmpty();
			case MetamodelPackage.ISESSION_ARTIFACT__SUPPORTED_NAMED_QUERIES:
				return supportedNamedQueries != null && !supportedNamedQueries.isEmpty();
			case MetamodelPackage.ISESSION_ARTIFACT__EXPOSED_UPDATE_PROCEDURES:
				return exposedUpdateProcedures != null && !exposedUpdateProcedures.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ISessionArtifactImpl
