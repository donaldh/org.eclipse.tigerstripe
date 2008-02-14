/**
 * <copyright>
 * </copyright>
 *
 * $Id: IDependencyArtifactImpl.java,v 1.2 2008/05/22 18:26:28 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.tigerstripe.metamodel.IDependencyArtifact;
import org.eclipse.tigerstripe.metamodel.IType;
import org.eclipse.tigerstripe.metamodel.MetamodelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IDependency Artifact</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IDependencyArtifactImpl#getAEndType <em>AEnd Type</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IDependencyArtifactImpl#getZEndType <em>ZEnd Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IDependencyArtifactImpl extends IAbstractArtifactImpl implements IDependencyArtifact {
	/**
	 * The cached value of the '{@link #getAEndType() <em>AEnd Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAEndType()
	 * @generated
	 * @ordered
	 */
	protected IType aEndType;

	/**
	 * The cached value of the '{@link #getZEndType() <em>ZEnd Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getZEndType()
	 * @generated
	 * @ordered
	 */
	protected IType zEndType;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IDependencyArtifactImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetamodelPackage.Literals.IDEPENDENCY_ARTIFACT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IType getAEndType() {
		if (aEndType != null && aEndType.eIsProxy()) {
			InternalEObject oldAEndType = (InternalEObject)aEndType;
			aEndType = (IType)eResolveProxy(oldAEndType);
			if (aEndType != oldAEndType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MetamodelPackage.IDEPENDENCY_ARTIFACT__AEND_TYPE, oldAEndType, aEndType));
			}
		}
		return aEndType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IType basicGetAEndType() {
		return aEndType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAEndType(IType newAEndType) {
		IType oldAEndType = aEndType;
		aEndType = newAEndType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IDEPENDENCY_ARTIFACT__AEND_TYPE, oldAEndType, aEndType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IType getZEndType() {
		if (zEndType != null && zEndType.eIsProxy()) {
			InternalEObject oldZEndType = (InternalEObject)zEndType;
			zEndType = (IType)eResolveProxy(oldZEndType);
			if (zEndType != oldZEndType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MetamodelPackage.IDEPENDENCY_ARTIFACT__ZEND_TYPE, oldZEndType, zEndType));
			}
		}
		return zEndType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IType basicGetZEndType() {
		return zEndType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setZEndType(IType newZEndType) {
		IType oldZEndType = zEndType;
		zEndType = newZEndType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IDEPENDENCY_ARTIFACT__ZEND_TYPE, oldZEndType, zEndType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MetamodelPackage.IDEPENDENCY_ARTIFACT__AEND_TYPE:
				if (resolve) return getAEndType();
				return basicGetAEndType();
			case MetamodelPackage.IDEPENDENCY_ARTIFACT__ZEND_TYPE:
				if (resolve) return getZEndType();
				return basicGetZEndType();
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
			case MetamodelPackage.IDEPENDENCY_ARTIFACT__AEND_TYPE:
				setAEndType((IType)newValue);
				return;
			case MetamodelPackage.IDEPENDENCY_ARTIFACT__ZEND_TYPE:
				setZEndType((IType)newValue);
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
			case MetamodelPackage.IDEPENDENCY_ARTIFACT__AEND_TYPE:
				setAEndType((IType)null);
				return;
			case MetamodelPackage.IDEPENDENCY_ARTIFACT__ZEND_TYPE:
				setZEndType((IType)null);
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
			case MetamodelPackage.IDEPENDENCY_ARTIFACT__AEND_TYPE:
				return aEndType != null;
			case MetamodelPackage.IDEPENDENCY_ARTIFACT__ZEND_TYPE:
				return zEndType != null;
		}
		return super.eIsSet(featureID);
	}

} //IDependencyArtifactImpl
