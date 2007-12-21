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
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Variable;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.util.InstanceDiagramUtils;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Class Instance</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.ClassInstanceImpl#getVariables <em>Variables</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.ClassInstanceImpl#getAssociations <em>Associations</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.ClassInstanceImpl#isAssociationClassInstance <em>Association Class Instance</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ClassInstanceImpl extends InstanceImpl implements ClassInstance {
	/**
	 * The cached value of the '{@link #getVariables() <em>Variables</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getVariables()
	 * @generated
	 * @ordered
	 */
	protected EList variables = null;

	/**
	 * The cached value of the '{@link #getAssociations() <em>Associations</em>}'
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAssociations()
	 * @generated
	 * @ordered
	 */
	protected EList associations = null;

	/**
	 * The default value of the '{@link #isAssociationClassInstance() <em>Association Class Instance</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isAssociationClassInstance()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ASSOCIATION_CLASS_INSTANCE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isAssociationClassInstance() <em>Association Class Instance</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isAssociationClassInstance()
	 * @generated
	 * @ordered
	 */
	protected boolean associationClassInstance = ASSOCIATION_CLASS_INSTANCE_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ClassInstanceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return InstancediagramPackage.Literals.CLASS_INSTANCE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList getVariables() {
		if (variables == null) {
			variables = new EObjectContainmentEList(Variable.class, this,
					InstancediagramPackage.CLASS_INSTANCE__VARIABLES);
		}
		return variables;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList getAssociations() {
		if (associations == null) {
			associations = new EObjectResolvingEList(AssociationInstance.class,
					this, InstancediagramPackage.CLASS_INSTANCE__ASSOCIATIONS);
		}
		return associations;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isAssociationClassInstance() {
		return associationClassInstance;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setAssociationClassInstance(boolean newAssociationClassInstance) {
		boolean oldAssociationClassInstance = associationClassInstance;
		if (!InstanceDiagramUtils.isInstanceEMFsetCommand()) {
			associationClassInstance = newAssociationClassInstance;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(
						this,
						Notification.SET,
						InstancediagramPackage.CLASS_INSTANCE__ASSOCIATION_CLASS_INSTANCE,
						oldAssociationClassInstance, associationClassInstance));
			return;
		}
		eNotify(new ENotificationImpl(
				this,
				Notification.UNSET,
				InstancediagramPackage.CLASS_INSTANCE__ASSOCIATION_CLASS_INSTANCE,
				oldAssociationClassInstance, associationClassInstance));
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
		case InstancediagramPackage.CLASS_INSTANCE__VARIABLES:
			return ((InternalEList) getVariables()).basicRemove(otherEnd, msgs);
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
		case InstancediagramPackage.CLASS_INSTANCE__VARIABLES:
			return getVariables();
		case InstancediagramPackage.CLASS_INSTANCE__ASSOCIATIONS:
			return getAssociations();
		case InstancediagramPackage.CLASS_INSTANCE__ASSOCIATION_CLASS_INSTANCE:
			return isAssociationClassInstance() ? Boolean.TRUE : Boolean.FALSE;
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
		case InstancediagramPackage.CLASS_INSTANCE__VARIABLES:
			getVariables().clear();
			getVariables().addAll((Collection) newValue);
			return;
		case InstancediagramPackage.CLASS_INSTANCE__ASSOCIATIONS:
			getAssociations().clear();
			getAssociations().addAll((Collection) newValue);
			return;
		case InstancediagramPackage.CLASS_INSTANCE__ASSOCIATION_CLASS_INSTANCE:
			setAssociationClassInstance(((Boolean) newValue).booleanValue());
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
		case InstancediagramPackage.CLASS_INSTANCE__VARIABLES:
			getVariables().clear();
			return;
		case InstancediagramPackage.CLASS_INSTANCE__ASSOCIATIONS:
			getAssociations().clear();
			return;
		case InstancediagramPackage.CLASS_INSTANCE__ASSOCIATION_CLASS_INSTANCE:
			setAssociationClassInstance(ASSOCIATION_CLASS_INSTANCE_EDEFAULT);
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
		case InstancediagramPackage.CLASS_INSTANCE__VARIABLES:
			return variables != null && !variables.isEmpty();
		case InstancediagramPackage.CLASS_INSTANCE__ASSOCIATIONS:
			return associations != null && !associations.isEmpty();
		case InstancediagramPackage.CLASS_INSTANCE__ASSOCIATION_CLASS_INSTANCE:
			return associationClassInstance != ASSOCIATION_CLASS_INSTANCE_EDEFAULT;
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
		result.append(" (associationClassInstance: ");
		result.append(associationClassInstance);
		result.append(')');
		return result.toString();
	}

} // ClassInstanceImpl
