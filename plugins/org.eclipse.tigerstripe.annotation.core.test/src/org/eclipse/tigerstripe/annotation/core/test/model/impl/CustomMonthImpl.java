/**
 * <copyright>
 * </copyright>
 *
 * $Id: CustomMonthImpl.java,v 1.1 2008/08/29 08:49:54 ystrot Exp $
 */
package org.eclipse.tigerstripe.annotation.core.test.model.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.tigerstripe.annotation.core.test.model.CustomMonth;
import org.eclipse.tigerstripe.annotation.core.test.model.DayList;
import org.eclipse.tigerstripe.annotation.core.test.model.ModelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Custom Month</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.annotation.core.test.model.impl.CustomMonthImpl#getWeeks <em>Weeks</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CustomMonthImpl extends EObjectImpl implements CustomMonth {
	/**
	 * The cached value of the '{@link #getWeeks() <em>Weeks</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWeeks()
	 * @generated
	 * @ordered
	 */
	protected EList<DayList> weeks;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CustomMonthImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.CUSTOM_MONTH;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<DayList> getWeeks() {
		if (weeks == null) {
			weeks = new EObjectContainmentEList<DayList>(DayList.class, this, ModelPackage.CUSTOM_MONTH__WEEKS);
		}
		return weeks;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ModelPackage.CUSTOM_MONTH__WEEKS:
				return ((InternalEList<?>)getWeeks()).basicRemove(otherEnd, msgs);
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
			case ModelPackage.CUSTOM_MONTH__WEEKS:
				return getWeeks();
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
			case ModelPackage.CUSTOM_MONTH__WEEKS:
				getWeeks().clear();
				getWeeks().addAll((Collection<? extends DayList>)newValue);
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
			case ModelPackage.CUSTOM_MONTH__WEEKS:
				getWeeks().clear();
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
			case ModelPackage.CUSTOM_MONTH__WEEKS:
				return weeks != null && !weeks.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //CustomMonthImpl
