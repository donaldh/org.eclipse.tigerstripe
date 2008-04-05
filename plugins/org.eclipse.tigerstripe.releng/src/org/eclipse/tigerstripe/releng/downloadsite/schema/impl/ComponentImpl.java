/**
 * <copyright>
 * </copyright>
 *
 * $Id: ComponentImpl.java,v 1.2 2008/04/08 22:22:16 edillon Exp $
 */
package org.eclipse.tigerstripe.releng.downloadsite.schema.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.tigerstripe.releng.downloadsite.schema.Bundle;
import org.eclipse.tigerstripe.releng.downloadsite.schema.Component;
import org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSitePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Component</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.releng.downloadsite.schema.impl.ComponentImpl#getBundle <em>Bundle</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ComponentImpl extends DownloadSiteElementImpl implements Component {
	/**
	 * The cached value of the '{@link #getBundle() <em>Bundle</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBundle()
	 * @generated
	 * @ordered
	 */
	protected EList<Bundle> bundle;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ComponentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DownloadSitePackage.Literals.COMPONENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Bundle> getBundle() {
		if (bundle == null) {
			bundle = new EObjectContainmentEList<Bundle>(Bundle.class, this, DownloadSitePackage.COMPONENT__BUNDLE);
		}
		return bundle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DownloadSitePackage.COMPONENT__BUNDLE:
				return ((InternalEList<?>)getBundle()).basicRemove(otherEnd, msgs);
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
			case DownloadSitePackage.COMPONENT__BUNDLE:
				return getBundle();
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
			case DownloadSitePackage.COMPONENT__BUNDLE:
				getBundle().clear();
				getBundle().addAll((Collection<? extends Bundle>)newValue);
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
			case DownloadSitePackage.COMPONENT__BUNDLE:
				getBundle().clear();
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
			case DownloadSitePackage.COMPONENT__BUNDLE:
				return bundle != null && !bundle.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ComponentImpl
