/**
 * <copyright>
 * </copyright>
 *
 * $Id: DownloadSiteImpl.java,v 1.2 2008/04/08 22:22:16 edillon Exp $
 */
package org.eclipse.tigerstripe.releng.downloadsite.schema.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.tigerstripe.releng.downloadsite.schema.Build;
import org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSite;
import org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSitePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Download Site</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSiteImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.releng.downloadsite.schema.impl.DownloadSiteImpl#getBuild <em>Build</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DownloadSiteImpl extends DownloadSiteElementImpl implements DownloadSite {
	/**
	 * The cached value of the '{@link #getGroup() <em>Group</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroup()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap group;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DownloadSiteImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DownloadSitePackage.Literals.DOWNLOAD_SITE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getGroup() {
		if (group == null) {
			group = new BasicFeatureMap(this, DownloadSitePackage.DOWNLOAD_SITE__GROUP);
		}
		return group;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Build> getBuild() {
		return getGroup().list(DownloadSitePackage.Literals.DOWNLOAD_SITE__BUILD);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DownloadSitePackage.DOWNLOAD_SITE__GROUP:
				return ((InternalEList<?>)getGroup()).basicRemove(otherEnd, msgs);
			case DownloadSitePackage.DOWNLOAD_SITE__BUILD:
				return ((InternalEList<?>)getBuild()).basicRemove(otherEnd, msgs);
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
			case DownloadSitePackage.DOWNLOAD_SITE__GROUP:
				if (coreType) return getGroup();
				return ((FeatureMap.Internal)getGroup()).getWrapper();
			case DownloadSitePackage.DOWNLOAD_SITE__BUILD:
				return getBuild();
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
			case DownloadSitePackage.DOWNLOAD_SITE__GROUP:
				((FeatureMap.Internal)getGroup()).set(newValue);
				return;
			case DownloadSitePackage.DOWNLOAD_SITE__BUILD:
				getBuild().clear();
				getBuild().addAll((Collection<? extends Build>)newValue);
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
			case DownloadSitePackage.DOWNLOAD_SITE__GROUP:
				getGroup().clear();
				return;
			case DownloadSitePackage.DOWNLOAD_SITE__BUILD:
				getBuild().clear();
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
			case DownloadSitePackage.DOWNLOAD_SITE__GROUP:
				return group != null && !group.isEmpty();
			case DownloadSitePackage.DOWNLOAD_SITE__BUILD:
				return !getBuild().isEmpty();
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
		result.append(" (group: ");
		result.append(group);
		result.append(')');
		return result.toString();
	}

} //DownloadSiteImpl
