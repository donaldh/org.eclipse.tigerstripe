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
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeValidationUtils;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.DiagramProperty;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Instance Map</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstanceMapImpl#getClassInstances <em>Class Instances</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstanceMapImpl#getAssociationInstances <em>Association Instances</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstanceMapImpl#getBasePackage <em>Base Package</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.InstanceMapImpl#getProperties <em>Properties</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class InstanceMapImpl extends EObjectImpl implements InstanceMap {
	/**
	 * The cached value of the '{@link #getClassInstances() <em>Class Instances</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getClassInstances()
	 * @generated
	 * @ordered
	 */
	protected EList classInstances = null;

	/**
	 * The cached value of the '{@link #getAssociationInstances() <em>Association Instances</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAssociationInstances()
	 * @generated
	 * @ordered
	 */
	protected EList associationInstances = null;

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
	protected InstanceMapImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return InstancediagramPackage.Literals.INSTANCE_MAP;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList getClassInstances() {
		if (classInstances == null) {
			classInstances = new EObjectContainmentEList(ClassInstance.class,
					this, InstancediagramPackage.INSTANCE_MAP__CLASS_INSTANCES);
		}
		return classInstances;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList getAssociationInstances() {
		if (associationInstances == null) {
			associationInstances = new EObjectContainmentEList(
					AssociationInstance.class, this,
					InstancediagramPackage.INSTANCE_MAP__ASSOCIATION_INSTANCES);
		}
		return associationInstances;
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
		if (!TigerstripeValidationUtils.packageNamePattern.matcher(
				newBasePackage).matches()) {
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.UNSET,
						InstancediagramPackage.INSTANCE_MAP__BASE_PACKAGE,
						oldBasePackage, basePackage));
			return;
		}
		basePackage = newBasePackage;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					InstancediagramPackage.INSTANCE_MAP__BASE_PACKAGE,
					oldBasePackage, basePackage));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList getProperties() {
		if (properties == null) {
			properties = new EObjectContainmentEList(DiagramProperty.class,
					this, InstancediagramPackage.INSTANCE_MAP__PROPERTIES);
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
		case InstancediagramPackage.INSTANCE_MAP__CLASS_INSTANCES:
			return ((InternalEList) getClassInstances()).basicRemove(otherEnd,
					msgs);
		case InstancediagramPackage.INSTANCE_MAP__ASSOCIATION_INSTANCES:
			return ((InternalEList) getAssociationInstances()).basicRemove(
					otherEnd, msgs);
		case InstancediagramPackage.INSTANCE_MAP__PROPERTIES:
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
		case InstancediagramPackage.INSTANCE_MAP__CLASS_INSTANCES:
			return getClassInstances();
		case InstancediagramPackage.INSTANCE_MAP__ASSOCIATION_INSTANCES:
			return getAssociationInstances();
		case InstancediagramPackage.INSTANCE_MAP__BASE_PACKAGE:
			return getBasePackage();
		case InstancediagramPackage.INSTANCE_MAP__PROPERTIES:
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
		case InstancediagramPackage.INSTANCE_MAP__CLASS_INSTANCES:
			getClassInstances().clear();
			getClassInstances().addAll((Collection) newValue);
			return;
		case InstancediagramPackage.INSTANCE_MAP__ASSOCIATION_INSTANCES:
			getAssociationInstances().clear();
			getAssociationInstances().addAll((Collection) newValue);
			return;
		case InstancediagramPackage.INSTANCE_MAP__BASE_PACKAGE:
			setBasePackage((String) newValue);
			return;
		case InstancediagramPackage.INSTANCE_MAP__PROPERTIES:
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
		case InstancediagramPackage.INSTANCE_MAP__CLASS_INSTANCES:
			getClassInstances().clear();
			return;
		case InstancediagramPackage.INSTANCE_MAP__ASSOCIATION_INSTANCES:
			getAssociationInstances().clear();
			return;
		case InstancediagramPackage.INSTANCE_MAP__BASE_PACKAGE:
			setBasePackage(BASE_PACKAGE_EDEFAULT);
			return;
		case InstancediagramPackage.INSTANCE_MAP__PROPERTIES:
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
		case InstancediagramPackage.INSTANCE_MAP__CLASS_INSTANCES:
			return classInstances != null && !classInstances.isEmpty();
		case InstancediagramPackage.INSTANCE_MAP__ASSOCIATION_INSTANCES:
			return associationInstances != null
					&& !associationInstances.isEmpty();
		case InstancediagramPackage.INSTANCE_MAP__BASE_PACKAGE:
			return BASE_PACKAGE_EDEFAULT == null ? basePackage != null
					: !BASE_PACKAGE_EDEFAULT.equals(basePackage);
		case InstancediagramPackage.INSTANCE_MAP__PROPERTIES:
			return properties != null && !properties.isEmpty();
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
		result.append(" (basePackage: ");
		result.append(basePackage);
		result.append(')');
		return result.toString();
	}

	private ITigerstripeProject correspondingTSProject;

	public ITigerstripeProject getCorrespondingITigerstripeProject() {
		return correspondingTSProject;
	}

	public void setCorrespondingITigerstripeProject(
			ITigerstripeProject tsProject) {
		correspondingTSProject = tsProject;
	}

} // InstanceMapImpl
