/**
 * <copyright>
 * </copyright>
 *
 * $Id: IOssjEventSpecificsImpl.java,v 1.1 2008/02/14 23:58:01 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.extensions.ossj.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.eclipse.tigerstripe.metamodel.extensions.ossj.IEventDescriptorEntry;
import org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjEventSpecifics;
import org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IOssj Event Specifics</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjEventSpecificsImpl#isSingleExtensionType <em>Single Extension Type</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjEventSpecificsImpl#getEventDescriptorEntries <em>Event Descriptor Entries</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjEventSpecificsImpl#getCustomEventDescriptorEntries <em>Custom Event Descriptor Entries</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IOssjEventSpecificsImpl extends IOssjArtifactSpecificsImpl implements IOssjEventSpecifics {
	/**
	 * The default value of the '{@link #isSingleExtensionType() <em>Single Extension Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSingleExtensionType()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SINGLE_EXTENSION_TYPE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isSingleExtensionType() <em>Single Extension Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSingleExtensionType()
	 * @generated
	 * @ordered
	 */
	protected boolean singleExtensionType = SINGLE_EXTENSION_TYPE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getEventDescriptorEntries() <em>Event Descriptor Entries</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEventDescriptorEntries()
	 * @generated
	 * @ordered
	 */
	protected EList<IEventDescriptorEntry> eventDescriptorEntries;

	/**
	 * The cached value of the '{@link #getCustomEventDescriptorEntries() <em>Custom Event Descriptor Entries</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCustomEventDescriptorEntries()
	 * @generated
	 * @ordered
	 */
	protected EList<IEventDescriptorEntry> customEventDescriptorEntries;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IOssjEventSpecificsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OssjPackage.Literals.IOSSJ_EVENT_SPECIFICS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSingleExtensionType() {
		return singleExtensionType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSingleExtensionType(boolean newSingleExtensionType) {
		boolean oldSingleExtensionType = singleExtensionType;
		singleExtensionType = newSingleExtensionType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OssjPackage.IOSSJ_EVENT_SPECIFICS__SINGLE_EXTENSION_TYPE, oldSingleExtensionType, singleExtensionType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IEventDescriptorEntry> getEventDescriptorEntries() {
		if (eventDescriptorEntries == null) {
			eventDescriptorEntries = new EObjectResolvingEList<IEventDescriptorEntry>(IEventDescriptorEntry.class, this, OssjPackage.IOSSJ_EVENT_SPECIFICS__EVENT_DESCRIPTOR_ENTRIES);
		}
		return eventDescriptorEntries;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IEventDescriptorEntry> getCustomEventDescriptorEntries() {
		if (customEventDescriptorEntries == null) {
			customEventDescriptorEntries = new EObjectResolvingEList<IEventDescriptorEntry>(IEventDescriptorEntry.class, this, OssjPackage.IOSSJ_EVENT_SPECIFICS__CUSTOM_EVENT_DESCRIPTOR_ENTRIES);
		}
		return customEventDescriptorEntries;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case OssjPackage.IOSSJ_EVENT_SPECIFICS__SINGLE_EXTENSION_TYPE:
				return isSingleExtensionType() ? Boolean.TRUE : Boolean.FALSE;
			case OssjPackage.IOSSJ_EVENT_SPECIFICS__EVENT_DESCRIPTOR_ENTRIES:
				return getEventDescriptorEntries();
			case OssjPackage.IOSSJ_EVENT_SPECIFICS__CUSTOM_EVENT_DESCRIPTOR_ENTRIES:
				return getCustomEventDescriptorEntries();
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
			case OssjPackage.IOSSJ_EVENT_SPECIFICS__SINGLE_EXTENSION_TYPE:
				setSingleExtensionType(((Boolean)newValue).booleanValue());
				return;
			case OssjPackage.IOSSJ_EVENT_SPECIFICS__EVENT_DESCRIPTOR_ENTRIES:
				getEventDescriptorEntries().clear();
				getEventDescriptorEntries().addAll((Collection<? extends IEventDescriptorEntry>)newValue);
				return;
			case OssjPackage.IOSSJ_EVENT_SPECIFICS__CUSTOM_EVENT_DESCRIPTOR_ENTRIES:
				getCustomEventDescriptorEntries().clear();
				getCustomEventDescriptorEntries().addAll((Collection<? extends IEventDescriptorEntry>)newValue);
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
			case OssjPackage.IOSSJ_EVENT_SPECIFICS__SINGLE_EXTENSION_TYPE:
				setSingleExtensionType(SINGLE_EXTENSION_TYPE_EDEFAULT);
				return;
			case OssjPackage.IOSSJ_EVENT_SPECIFICS__EVENT_DESCRIPTOR_ENTRIES:
				getEventDescriptorEntries().clear();
				return;
			case OssjPackage.IOSSJ_EVENT_SPECIFICS__CUSTOM_EVENT_DESCRIPTOR_ENTRIES:
				getCustomEventDescriptorEntries().clear();
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
			case OssjPackage.IOSSJ_EVENT_SPECIFICS__SINGLE_EXTENSION_TYPE:
				return singleExtensionType != SINGLE_EXTENSION_TYPE_EDEFAULT;
			case OssjPackage.IOSSJ_EVENT_SPECIFICS__EVENT_DESCRIPTOR_ENTRIES:
				return eventDescriptorEntries != null && !eventDescriptorEntries.isEmpty();
			case OssjPackage.IOSSJ_EVENT_SPECIFICS__CUSTOM_EVENT_DESCRIPTOR_ENTRIES:
				return customEventDescriptorEntries != null && !customEventDescriptorEntries.isEmpty();
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
		result.append(" (singleExtensionType: ");
		result.append(singleExtensionType);
		result.append(')');
		return result.toString();
	}

} //IOssjEventSpecificsImpl
