/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.visualeditor.impl;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedQueryArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NotificationArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Session Facade Artifact</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.SessionFacadeArtifactImpl#getManagedEntities <em>Managed Entities</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.SessionFacadeArtifactImpl#getEmittedNotifications <em>Emitted Notifications</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.SessionFacadeArtifactImpl#getNamedQueries <em>Named Queries</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.SessionFacadeArtifactImpl#getExposedProcedures <em>Exposed Procedures</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class SessionFacadeArtifactImpl extends AbstractArtifactImpl implements
		SessionFacadeArtifact {
	/**
	 * The cached value of the '{@link #getManagedEntities() <em>Managed Entities</em>}'
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getManagedEntities()
	 * @generated
	 * @ordered
	 */
	protected EList managedEntities = null;

	/**
	 * The cached value of the '{@link #getEmittedNotifications() <em>Emitted Notifications</em>}'
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getEmittedNotifications()
	 * @generated
	 * @ordered
	 */
	protected EList emittedNotifications = null;

	/**
	 * The cached value of the '{@link #getNamedQueries() <em>Named Queries</em>}'
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getNamedQueries()
	 * @generated
	 * @ordered
	 */
	protected EList namedQueries = null;

	/**
	 * The cached value of the '{@link #getExposedProcedures() <em>Exposed Procedures</em>}'
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getExposedProcedures()
	 * @generated
	 * @ordered
	 */
	protected EList exposedProcedures = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected SessionFacadeArtifactImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return VisualeditorPackage.Literals.SESSION_FACADE_ARTIFACT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList getManagedEntities() {
		if (managedEntities == null) {
			managedEntities = new EObjectResolvingEList(
					ManagedEntityArtifact.class,
					this,
					VisualeditorPackage.SESSION_FACADE_ARTIFACT__MANAGED_ENTITIES);
		}
		return managedEntities;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList getEmittedNotifications() {
		if (emittedNotifications == null) {
			emittedNotifications = new EObjectResolvingEList(
					NotificationArtifact.class,
					this,
					VisualeditorPackage.SESSION_FACADE_ARTIFACT__EMITTED_NOTIFICATIONS);
		}
		return emittedNotifications;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList getNamedQueries() {
		if (namedQueries == null) {
			namedQueries = new EObjectResolvingEList(NamedQueryArtifact.class,
					this,
					VisualeditorPackage.SESSION_FACADE_ARTIFACT__NAMED_QUERIES);
		}
		return namedQueries;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList getExposedProcedures() {
		if (exposedProcedures == null) {
			exposedProcedures = new EObjectResolvingEList(
					UpdateProcedureArtifact.class,
					this,
					VisualeditorPackage.SESSION_FACADE_ARTIFACT__EXPOSED_PROCEDURES);
		}
		return exposedProcedures;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case VisualeditorPackage.SESSION_FACADE_ARTIFACT__MANAGED_ENTITIES:
			return getManagedEntities();
		case VisualeditorPackage.SESSION_FACADE_ARTIFACT__EMITTED_NOTIFICATIONS:
			return getEmittedNotifications();
		case VisualeditorPackage.SESSION_FACADE_ARTIFACT__NAMED_QUERIES:
			return getNamedQueries();
		case VisualeditorPackage.SESSION_FACADE_ARTIFACT__EXPOSED_PROCEDURES:
			return getExposedProcedures();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		if (!isReadonly) {
			switch (featureID) {
			case VisualeditorPackage.SESSION_FACADE_ARTIFACT__MANAGED_ENTITIES:
				getManagedEntities().clear();
				getManagedEntities().addAll((Collection) newValue);
				return;
			case VisualeditorPackage.SESSION_FACADE_ARTIFACT__EMITTED_NOTIFICATIONS:
				getEmittedNotifications().clear();
				getEmittedNotifications().addAll((Collection) newValue);
				return;
			case VisualeditorPackage.SESSION_FACADE_ARTIFACT__NAMED_QUERIES:
				getNamedQueries().clear();
				getNamedQueries().addAll((Collection) newValue);
				return;
			case VisualeditorPackage.SESSION_FACADE_ARTIFACT__EXPOSED_PROCEDURES:
				getExposedProcedures().clear();
				getExposedProcedures().addAll((Collection) newValue);
				return;
			}
			super.eSet(featureID, newValue);
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public void eUnset(int featureID) {
		if (!isReadonly) {
			switch (featureID) {
			case VisualeditorPackage.SESSION_FACADE_ARTIFACT__MANAGED_ENTITIES:
				getManagedEntities().clear();
				return;
			case VisualeditorPackage.SESSION_FACADE_ARTIFACT__EMITTED_NOTIFICATIONS:
				getEmittedNotifications().clear();
				return;
			case VisualeditorPackage.SESSION_FACADE_ARTIFACT__NAMED_QUERIES:
				getNamedQueries().clear();
				return;
			case VisualeditorPackage.SESSION_FACADE_ARTIFACT__EXPOSED_PROCEDURES:
				getExposedProcedures().clear();
				return;
			}
			super.eUnset(featureID);
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case VisualeditorPackage.SESSION_FACADE_ARTIFACT__MANAGED_ENTITIES:
			return managedEntities != null && !managedEntities.isEmpty();
		case VisualeditorPackage.SESSION_FACADE_ARTIFACT__EMITTED_NOTIFICATIONS:
			return emittedNotifications != null
					&& !emittedNotifications.isEmpty();
		case VisualeditorPackage.SESSION_FACADE_ARTIFACT__NAMED_QUERIES:
			return namedQueries != null && !namedQueries.isEmpty();
		case VisualeditorPackage.SESSION_FACADE_ARTIFACT__EXPOSED_PROCEDURES:
			return exposedProcedures != null && !exposedProcedures.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // SessionFacadeArtifactImpl
