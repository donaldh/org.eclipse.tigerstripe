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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.DiagramProperty;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Map</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.MapImpl#getArtifacts <em>Artifacts</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.MapImpl#getAssociations <em>Associations</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.MapImpl#getDependencies <em>Dependencies</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.MapImpl#getBasePackage <em>Base Package</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.MapImpl#getProperties <em>Properties</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class MapImpl extends EObjectImpl implements Map {
	/**
	 * The cached value of the '{@link #getArtifacts() <em>Artifacts</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getArtifacts()
	 * @generated
	 * @ordered
	 */
	protected EList artifacts = null;

	/**
	 * The cached value of the '{@link #getAssociations() <em>Associations</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAssociations()
	 * @generated
	 * @ordered
	 */
	protected EList associations = null;

	/**
	 * The cached value of the '{@link #getDependencies() <em>Dependencies</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDependencies()
	 * @generated
	 * @ordered
	 */
	protected EList dependencies = null;

	/**
	 * The default value of the '{@link #getBasePackage() <em>Base Package</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getBasePackage()
	 * @generated
	 * @ordered
	 */
	protected static final String BASE_PACKAGE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBasePackage() <em>Base Package</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getBasePackage()
	 * @generated
	 * @ordered
	 */
	protected String basePackage = BASE_PACKAGE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getProperties() <em>Properties</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getProperties()
	 * @generated
	 * @ordered
	 */
	protected EList properties = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected MapImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return VisualeditorPackage.Literals.MAP;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList getArtifacts() {
		if (artifacts == null) {
			artifacts = new EObjectContainmentEList(AbstractArtifact.class,
					this, VisualeditorPackage.MAP__ARTIFACTS);
		}
		return artifacts;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList getAssociations() {
		if (associations == null) {
			associations = new EObjectContainmentEList(Association.class, this,
					VisualeditorPackage.MAP__ASSOCIATIONS);
		}
		return associations;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList getDependencies() {
		if (dependencies == null) {
			dependencies = new EObjectContainmentEList(Dependency.class, this,
					VisualeditorPackage.MAP__DEPENDENCIES);
		}
		return dependencies;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getBasePackage() {
		return basePackage;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setBasePackage(String newBasePackage) {
		String oldBasePackage = basePackage;
		if (!NamedElementImpl.packageNamePattern.matcher(newBasePackage)
				.matches()) {
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.UNSET,
						VisualeditorPackage.MAP__BASE_PACKAGE, oldBasePackage,
						basePackage));
			return;
		}
		basePackage = newBasePackage;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.MAP__BASE_PACKAGE, oldBasePackage,
					basePackage));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList getProperties() {
		if (properties == null) {
			properties = new EObjectContainmentEList(DiagramProperty.class,
					this, VisualeditorPackage.MAP__PROPERTIES);
		}
		return properties;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd,
			int featureID, NotificationChain msgs) {
		switch (featureID) {
		case VisualeditorPackage.MAP__ARTIFACTS:
			return ((InternalEList) getArtifacts()).basicRemove(otherEnd, msgs);
		case VisualeditorPackage.MAP__ASSOCIATIONS:
			return ((InternalEList) getAssociations()).basicRemove(otherEnd,
					msgs);
		case VisualeditorPackage.MAP__DEPENDENCIES:
			return ((InternalEList) getDependencies()).basicRemove(otherEnd,
					msgs);
		case VisualeditorPackage.MAP__PROPERTIES:
			return ((InternalEList) getProperties())
					.basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case VisualeditorPackage.MAP__ARTIFACTS:
			return getArtifacts();
		case VisualeditorPackage.MAP__ASSOCIATIONS:
			return getAssociations();
		case VisualeditorPackage.MAP__DEPENDENCIES:
			return getDependencies();
		case VisualeditorPackage.MAP__BASE_PACKAGE:
			return getBasePackage();
		case VisualeditorPackage.MAP__PROPERTIES:
			return getProperties();
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
		case VisualeditorPackage.MAP__ARTIFACTS:
			getArtifacts().clear();
			getArtifacts().addAll((Collection) newValue);
			return;
		case VisualeditorPackage.MAP__ASSOCIATIONS:
			getAssociations().clear();
			getAssociations().addAll((Collection) newValue);
			return;
		case VisualeditorPackage.MAP__DEPENDENCIES:
			getDependencies().clear();
			getDependencies().addAll((Collection) newValue);
			return;
		case VisualeditorPackage.MAP__BASE_PACKAGE:
			setBasePackage((String) newValue);
			return;
		case VisualeditorPackage.MAP__PROPERTIES:
			getProperties().clear();
			getProperties().addAll((Collection) newValue);
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
		case VisualeditorPackage.MAP__ARTIFACTS:
			getArtifacts().clear();
			return;
		case VisualeditorPackage.MAP__ASSOCIATIONS:
			getAssociations().clear();
			return;
		case VisualeditorPackage.MAP__DEPENDENCIES:
			getDependencies().clear();
			return;
		case VisualeditorPackage.MAP__BASE_PACKAGE:
			setBasePackage(BASE_PACKAGE_EDEFAULT);
			return;
		case VisualeditorPackage.MAP__PROPERTIES:
			getProperties().clear();
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
		case VisualeditorPackage.MAP__ARTIFACTS:
			return artifacts != null && !artifacts.isEmpty();
		case VisualeditorPackage.MAP__ASSOCIATIONS:
			return associations != null && !associations.isEmpty();
		case VisualeditorPackage.MAP__DEPENDENCIES:
			return dependencies != null && !dependencies.isEmpty();
		case VisualeditorPackage.MAP__BASE_PACKAGE:
			return BASE_PACKAGE_EDEFAULT == null ? basePackage != null
					: !BASE_PACKAGE_EDEFAULT.equals(basePackage);
		case VisualeditorPackage.MAP__PROPERTIES:
			return properties != null && !properties.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (basePackage: ");
		result.append(basePackage);
		result.append(')');
		return result.toString();
	}

	private ITigerstripeModelProject correspondingTSProject;

	public ITigerstripeModelProject getCorrespondingITigerstripeProject() {
		return correspondingTSProject;
	}

	public void setCorrespondingITigerstripeProject(
			ITigerstripeModelProject tsProject) {
		correspondingTSProject = tsProject;
	}

} // MapImpl
