/**
 * <copyright>
 * </copyright>
 *
 * $Id: IPropertiesImpl.java,v 1.1 2008/02/14 23:58:01 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.extensions.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.tigerstripe.metamodel.extensions.ExtensionsPackage;
import org.eclipse.tigerstripe.metamodel.extensions.IProperties;
import org.eclipse.tigerstripe.metamodel.extensions.IProperty;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IProperties</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.impl.IPropertiesImpl#getEntries <em>Entries</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IPropertiesImpl extends EObjectImpl implements IProperties {
	/**
	 * The cached value of the '{@link #getEntries() <em>Entries</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntries()
	 * @generated
	 * @ordered
	 */
	protected IProperty entries;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IPropertiesImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ExtensionsPackage.Literals.IPROPERTIES;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IProperty getEntries() {
		if (entries != null && entries.eIsProxy()) {
			InternalEObject oldEntries = (InternalEObject)entries;
			entries = (IProperty)eResolveProxy(oldEntries);
			if (entries != oldEntries) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExtensionsPackage.IPROPERTIES__ENTRIES, oldEntries, entries));
			}
		}
		return entries;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IProperty basicGetEntries() {
		return entries;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEntries(IProperty newEntries) {
		IProperty oldEntries = entries;
		entries = newEntries;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExtensionsPackage.IPROPERTIES__ENTRIES, oldEntries, entries));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ExtensionsPackage.IPROPERTIES__ENTRIES:
				if (resolve) return getEntries();
				return basicGetEntries();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ExtensionsPackage.IPROPERTIES__ENTRIES:
				setEntries((IProperty)newValue);
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
			case ExtensionsPackage.IPROPERTIES__ENTRIES:
				setEntries((IProperty)null);
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
			case ExtensionsPackage.IPROPERTIES__ENTRIES:
				return entries != null;
		}
		return super.eIsSet(featureID);
	}

} //IPropertiesImpl
