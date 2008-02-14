/**
 * <copyright>
 * </copyright>
 *
 * $Id: IOssjArtifactSpecificsImpl.java,v 1.1 2008/02/14 23:58:01 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.extensions.ossj.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.tigerstripe.metamodel.extensions.IProperties;

import org.eclipse.tigerstripe.metamodel.extensions.impl.IStandardSpecificsImpl;

import org.eclipse.tigerstripe.metamodel.extensions.ossj.IOssjArtifactSpecifics;
import org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IOssj Artifact Specifics</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IOssjArtifactSpecificsImpl#getInterfaceProperties <em>Interface Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IOssjArtifactSpecificsImpl extends IStandardSpecificsImpl implements IOssjArtifactSpecifics {
	/**
	 * The cached value of the '{@link #getInterfaceProperties() <em>Interface Properties</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInterfaceProperties()
	 * @generated
	 * @ordered
	 */
	protected IProperties interfaceProperties;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IOssjArtifactSpecificsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OssjPackage.Literals.IOSSJ_ARTIFACT_SPECIFICS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IProperties getInterfaceProperties() {
		if (interfaceProperties != null && interfaceProperties.eIsProxy()) {
			InternalEObject oldInterfaceProperties = (InternalEObject)interfaceProperties;
			interfaceProperties = (IProperties)eResolveProxy(oldInterfaceProperties);
			if (interfaceProperties != oldInterfaceProperties) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, OssjPackage.IOSSJ_ARTIFACT_SPECIFICS__INTERFACE_PROPERTIES, oldInterfaceProperties, interfaceProperties));
			}
		}
		return interfaceProperties;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IProperties basicGetInterfaceProperties() {
		return interfaceProperties;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInterfaceProperties(IProperties newInterfaceProperties) {
		IProperties oldInterfaceProperties = interfaceProperties;
		interfaceProperties = newInterfaceProperties;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OssjPackage.IOSSJ_ARTIFACT_SPECIFICS__INTERFACE_PROPERTIES, oldInterfaceProperties, interfaceProperties));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void mergeInterfaceProperties(IProperties interfaceProperties) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case OssjPackage.IOSSJ_ARTIFACT_SPECIFICS__INTERFACE_PROPERTIES:
				if (resolve) return getInterfaceProperties();
				return basicGetInterfaceProperties();
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
			case OssjPackage.IOSSJ_ARTIFACT_SPECIFICS__INTERFACE_PROPERTIES:
				setInterfaceProperties((IProperties)newValue);
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
			case OssjPackage.IOSSJ_ARTIFACT_SPECIFICS__INTERFACE_PROPERTIES:
				setInterfaceProperties((IProperties)null);
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
			case OssjPackage.IOSSJ_ARTIFACT_SPECIFICS__INTERFACE_PROPERTIES:
				return interfaceProperties != null;
		}
		return super.eIsSet(featureID);
	}

} //IOssjArtifactSpecificsImpl
