/**
 * <copyright>
 * </copyright>
 *
 * $Id: VillageImpl.java,v 1.1 2008/05/22 17:46:35 edillon Exp $
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
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.tigerstripe.repository.core.test.sample.SamplePackage;
import org.eclipse.tigerstripe.repository.core.test.sample.Street;
import org.eclipse.tigerstripe.repository.core.test.sample.Village;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Village</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.repository.core.test.sample.impl.VillageImpl#getStreets <em>Streets</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.repository.core.test.sample.impl.VillageImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.repository.core.test.sample.impl.VillageImpl#getNeighboringVillages <em>Neighboring Villages</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VillageImpl extends TopImpl implements Village {
	/**
	 * The cached value of the '{@link #getStreets() <em>Streets</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStreets()
	 * @generated
	 * @ordered
	 */
	protected EList<Street> streets;

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
	 * The cached value of the '{@link #getNeighboringVillages() <em>Neighboring Villages</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNeighboringVillages()
	 * @generated
	 * @ordered
	 */
	protected EList<Village> neighboringVillages;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VillageImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SamplePackage.Literals.VILLAGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Street> getStreets() {
		if (streets == null) {
			streets = new EObjectContainmentEList<Street>(Street.class, this, SamplePackage.VILLAGE__STREETS);
		}
		return streets;
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
			eNotify(new ENotificationImpl(this, Notification.SET, SamplePackage.VILLAGE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Village> getNeighboringVillages() {
		if (neighboringVillages == null) {
			neighboringVillages = new EObjectResolvingEList<Village>(Village.class, this, SamplePackage.VILLAGE__NEIGHBORING_VILLAGES);
		}
		return neighboringVillages;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SamplePackage.VILLAGE__STREETS:
				return ((InternalEList<?>)getStreets()).basicRemove(otherEnd, msgs);
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
			case SamplePackage.VILLAGE__STREETS:
				return getStreets();
			case SamplePackage.VILLAGE__NAME:
				return getName();
			case SamplePackage.VILLAGE__NEIGHBORING_VILLAGES:
				return getNeighboringVillages();
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
			case SamplePackage.VILLAGE__STREETS:
				getStreets().clear();
				getStreets().addAll((Collection<? extends Street>)newValue);
				return;
			case SamplePackage.VILLAGE__NAME:
				setName((String)newValue);
				return;
			case SamplePackage.VILLAGE__NEIGHBORING_VILLAGES:
				getNeighboringVillages().clear();
				getNeighboringVillages().addAll((Collection<? extends Village>)newValue);
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
			case SamplePackage.VILLAGE__STREETS:
				getStreets().clear();
				return;
			case SamplePackage.VILLAGE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case SamplePackage.VILLAGE__NEIGHBORING_VILLAGES:
				getNeighboringVillages().clear();
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
			case SamplePackage.VILLAGE__STREETS:
				return streets != null && !streets.isEmpty();
			case SamplePackage.VILLAGE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case SamplePackage.VILLAGE__NEIGHBORING_VILLAGES:
				return neighboringVillages != null && !neighboringVillages.isEmpty();
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

} //VillageImpl
