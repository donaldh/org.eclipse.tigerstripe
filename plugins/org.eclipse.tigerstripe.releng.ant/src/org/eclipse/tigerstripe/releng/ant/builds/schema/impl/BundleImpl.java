/**
 * <copyright>
 * </copyright>
 *
 * $Id: BundleImpl.java,v 1.1 2008/04/03 21:45:32 edillon Exp $
 */
package org.eclipse.tigerstripe.releng.ant.builds.schema.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.tigerstripe.releng.ant.builds.schema.BuildSchemaPackage;
import org.eclipse.tigerstripe.releng.ant.builds.schema.Bundle;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Bundle</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BundleImpl#getDownloadLink <em>Download Link</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BundleImpl extends BuildElementImpl implements Bundle {
	/**
	 * The default value of the '{@link #getDownloadLink() <em>Download Link</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDownloadLink()
	 * @generated
	 * @ordered
	 */
	protected static final String DOWNLOAD_LINK_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDownloadLink() <em>Download Link</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDownloadLink()
	 * @generated
	 * @ordered
	 */
	protected String downloadLink = DOWNLOAD_LINK_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BundleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BuildSchemaPackage.Literals.BUNDLE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDownloadLink() {
		return downloadLink;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDownloadLink(String newDownloadLink) {
		String oldDownloadLink = downloadLink;
		downloadLink = newDownloadLink;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BuildSchemaPackage.BUNDLE__DOWNLOAD_LINK, oldDownloadLink, downloadLink));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case BuildSchemaPackage.BUNDLE__DOWNLOAD_LINK:
				return getDownloadLink();
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
			case BuildSchemaPackage.BUNDLE__DOWNLOAD_LINK:
				setDownloadLink((String)newValue);
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
			case BuildSchemaPackage.BUNDLE__DOWNLOAD_LINK:
				setDownloadLink(DOWNLOAD_LINK_EDEFAULT);
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
			case BuildSchemaPackage.BUNDLE__DOWNLOAD_LINK:
				return DOWNLOAD_LINK_EDEFAULT == null ? downloadLink != null : !DOWNLOAD_LINK_EDEFAULT.equals(downloadLink);
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
		result.append(" (downloadLink: ");
		result.append(downloadLink);
		result.append(')');
		return result.toString();
	}

} //BundleImpl
