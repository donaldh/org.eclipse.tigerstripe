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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.util.InstanceDiagramUtils;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Instance</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstanceImpl#getPackage <em>Package</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstanceImpl#getArtifactName <em>Artifact Name</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class InstanceImpl extends NamedElementImpl implements Instance {
	/**
	 * The default value of the '{@link #getPackage() <em>Package</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getPackage()
	 * @generated
	 * @ordered
	 */
	protected static final String PACKAGE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPackage() <em>Package</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getPackage()
	 * @generated
	 * @ordered
	 */
	protected String package_ = PACKAGE_EDEFAULT;

	/**
	 * The default value of the '{@link #getArtifactName() <em>Artifact Name</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getArtifactName()
	 * @generated
	 * @ordered
	 */
	protected static final String ARTIFACT_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getArtifactName() <em>Artifact Name</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getArtifactName()
	 * @generated
	 * @ordered
	 */
	protected String artifactName = ARTIFACT_NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected InstanceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return InstancediagramPackage.Literals.INSTANCE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getPackage() {
		return package_;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setPackage(String newPackage) {
		String oldPackage = package_;
		if (!InstanceDiagramUtils.isInstanceEMFsetCommand()) {
			package_ = newPackage;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.SET,
						InstancediagramPackage.INSTANCE__PACKAGE, oldPackage,
						package_));
			return;
		}
		eNotify(new ENotificationImpl(this, Notification.UNSET,
				InstancediagramPackage.INSTANCE__PACKAGE, oldPackage, package_));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getArtifactName() {
		return artifactName;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setArtifactName(String newArtifactName) {
		String oldArtifactName = artifactName;
		if (!InstanceDiagramUtils.isInstanceEMFsetCommand()) {
			artifactName = newArtifactName;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.SET,
						InstancediagramPackage.INSTANCE__ARTIFACT_NAME,
						oldArtifactName, artifactName));
			return;
		}
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET,
					InstancediagramPackage.INSTANCE__ARTIFACT_NAME,
					oldArtifactName, artifactName));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public String getFullyQualifiedName() {
		String packageName = getPackage();
		if (packageName == null || packageName.length() == 0)
			return getName();
		return packageName + "." + getName();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case InstancediagramPackage.INSTANCE__PACKAGE:
			return getPackage();
		case InstancediagramPackage.INSTANCE__ARTIFACT_NAME:
			return getArtifactName();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case InstancediagramPackage.INSTANCE__PACKAGE:
			setPackage((String) newValue);
			return;
		case InstancediagramPackage.INSTANCE__ARTIFACT_NAME:
			setArtifactName((String) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case InstancediagramPackage.INSTANCE__PACKAGE:
			setPackage(PACKAGE_EDEFAULT);
			return;
		case InstancediagramPackage.INSTANCE__ARTIFACT_NAME:
			setArtifactName(ARTIFACT_NAME_EDEFAULT);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case InstancediagramPackage.INSTANCE__PACKAGE:
			return PACKAGE_EDEFAULT == null ? package_ != null
					: !PACKAGE_EDEFAULT.equals(package_);
		case InstancediagramPackage.INSTANCE__ARTIFACT_NAME:
			return ARTIFACT_NAME_EDEFAULT == null ? artifactName != null
					: !ARTIFACT_NAME_EDEFAULT.equals(artifactName);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (package: ");
		result.append(package_);
		result.append(", artifactName: ");
		result.append(artifactName);
		result.append(')');
		return result.toString();
	}

	// ======================================================
	// Custom method (not generated)
	public IAbstractArtifact getCorrespondingIArtifact()
			throws TigerstripeException {
		if (eContainer() instanceof InstanceMap) {
			InstanceMap map = (InstanceMap) eContainer();
			ITigerstripeModelProject tsProject = map
					.getCorrespondingITigerstripeProject();
			IArtifactManagerSession session = tsProject
					.getArtifactManagerSession();
			IAbstractArtifact artifact = session
					.getArtifactByFullyQualifiedName(getFullyQualifiedName());
			return artifact;
		}
		return null;
	}

	/**
	 * This is preliminary until the metamodel is complete
	 * 
	 * @preliminary
	 * @return
	 */
	public String getInstanceName() {
		return this.artifactName;
	}

	/**
	 * This is preliminary until the metamodel is complete
	 * 
	 * @preliminary
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	public IAbstractArtifact getArtifact() throws TigerstripeException {
		return getCorrespondingIArtifact();
	}
} // InstanceImpl
