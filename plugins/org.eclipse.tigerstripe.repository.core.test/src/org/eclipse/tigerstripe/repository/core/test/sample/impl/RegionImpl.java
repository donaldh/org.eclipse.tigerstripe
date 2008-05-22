/**
 * <copyright>
 * </copyright>
 *
 * $Id: RegionImpl.java,v 1.1 2008/05/22 17:46:35 edillon Exp $
 */
package org.eclipse.tigerstripe.repository.core.test.sample.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.tigerstripe.repository.core.test.sample.Region;
import org.eclipse.tigerstripe.repository.core.test.sample.SamplePackage;
import org.eclipse.tigerstripe.repository.core.test.sample.Village;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Region</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.repository.core.test.sample.impl.RegionImpl#getVillages <em>Villages</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.repository.core.test.sample.impl.RegionImpl#getNextTo <em>Next To</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.repository.core.test.sample.impl.RegionImpl#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RegionImpl extends TopImpl implements Region {
	/**
	 * The cached value of the '{@link #getVillages() <em>Villages</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVillages()
	 * @generated
	 * @ordered
	 */
	protected EList<Village> villages;

	/**
	 * The cached value of the '{@link #getNextTo() <em>Next To</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNextTo()
	 * @generated
	 * @ordered
	 */
	protected Region nextTo;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RegionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SamplePackage.Literals.REGION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Village> getVillages() {
		if (villages == null) {
			villages = new EObjectContainmentEList<Village>(Village.class, this, SamplePackage.REGION__VILLAGES);
		}
		return villages;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Region getNextTo() {
		if (nextTo != null && nextTo.eIsProxy()) {
			InternalEObject oldNextTo = (InternalEObject)nextTo;
			nextTo = (Region)eResolveProxy(oldNextTo);
			if (nextTo != oldNextTo) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SamplePackage.REGION__NEXT_TO, oldNextTo, nextTo));
			}
		}
		return nextTo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Region basicGetNextTo() {
		return nextTo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNextTo(Region newNextTo) {
		Region oldNextTo = nextTo;
		nextTo = newNextTo;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SamplePackage.REGION__NEXT_TO, oldNextTo, nextTo));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SamplePackage.REGION__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SamplePackage.REGION__VILLAGES:
				return ((InternalEList<?>)getVillages()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SamplePackage.REGION__VILLAGES:
				return getVillages();
			case SamplePackage.REGION__NEXT_TO:
				if (resolve) return getNextTo();
				return basicGetNextTo();
			case SamplePackage.REGION__NAME:
				return getName();
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
			case SamplePackage.REGION__VILLAGES:
				getVillages().clear();
				getVillages().addAll((Collection<? extends Village>)newValue);
				return;
			case SamplePackage.REGION__NEXT_TO:
				setNextTo((Region)newValue);
				return;
			case SamplePackage.REGION__NAME:
				setName((String)newValue);
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
			case SamplePackage.REGION__VILLAGES:
				getVillages().clear();
				return;
			case SamplePackage.REGION__NEXT_TO:
				setNextTo((Region)null);
				return;
			case SamplePackage.REGION__NAME:
				setName(NAME_EDEFAULT);
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
			case SamplePackage.REGION__VILLAGES:
				return villages != null && !villages.isEmpty();
			case SamplePackage.REGION__NEXT_TO:
				return nextTo != null;
			case SamplePackage.REGION__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //RegionImpl
