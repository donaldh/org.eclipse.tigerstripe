/**
 * <copyright>
 * </copyright>
 *
 * $Id: IAssociationArtifactImpl.java,v 1.2 2008/05/22 18:26:28 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.tigerstripe.metamodel.IAssociationArtifact;
import org.eclipse.tigerstripe.metamodel.IAssociationEnd;
import org.eclipse.tigerstripe.metamodel.MetamodelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IAssociation Artifact</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IAssociationArtifactImpl#getAEnd <em>AEnd</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IAssociationArtifactImpl#getZEnd <em>ZEnd</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IAssociationArtifactImpl extends IAbstractArtifactImpl implements IAssociationArtifact {
	/**
	 * The cached value of the '{@link #getAEnd() <em>AEnd</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAEnd()
	 * @generated
	 * @ordered
	 */
	protected IAssociationEnd aEnd;

	/**
	 * The cached value of the '{@link #getZEnd() <em>ZEnd</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getZEnd()
	 * @generated
	 * @ordered
	 */
	protected IAssociationEnd zEnd;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IAssociationArtifactImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetamodelPackage.Literals.IASSOCIATION_ARTIFACT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IAssociationEnd getAEnd() {
		return aEnd;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAEnd(IAssociationEnd newAEnd, NotificationChain msgs) {
		IAssociationEnd oldAEnd = aEnd;
		aEnd = newAEnd;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MetamodelPackage.IASSOCIATION_ARTIFACT__AEND, oldAEnd, newAEnd);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAEnd(IAssociationEnd newAEnd) {
		if (newAEnd != aEnd) {
			NotificationChain msgs = null;
			if (aEnd != null)
				msgs = ((InternalEObject)aEnd).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MetamodelPackage.IASSOCIATION_ARTIFACT__AEND, null, msgs);
			if (newAEnd != null)
				msgs = ((InternalEObject)newAEnd).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MetamodelPackage.IASSOCIATION_ARTIFACT__AEND, null, msgs);
			msgs = basicSetAEnd(newAEnd, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IASSOCIATION_ARTIFACT__AEND, newAEnd, newAEnd));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IAssociationEnd getZEnd() {
		if (zEnd != null && zEnd.eIsProxy()) {
			InternalEObject oldZEnd = (InternalEObject)zEnd;
			zEnd = (IAssociationEnd)eResolveProxy(oldZEnd);
			if (zEnd != oldZEnd) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MetamodelPackage.IASSOCIATION_ARTIFACT__ZEND, oldZEnd, zEnd));
			}
		}
		return zEnd;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IAssociationEnd basicGetZEnd() {
		return zEnd;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setZEnd(IAssociationEnd newZEnd) {
		IAssociationEnd oldZEnd = zEnd;
		zEnd = newZEnd;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IASSOCIATION_ARTIFACT__ZEND, oldZEnd, zEnd));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IAssociationEnd> getAssociationEnds() {
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
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MetamodelPackage.IASSOCIATION_ARTIFACT__AEND:
				return basicSetAEnd(null, msgs);
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
			case MetamodelPackage.IASSOCIATION_ARTIFACT__AEND:
				return getAEnd();
			case MetamodelPackage.IASSOCIATION_ARTIFACT__ZEND:
				if (resolve) return getZEnd();
				return basicGetZEnd();
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
			case MetamodelPackage.IASSOCIATION_ARTIFACT__AEND:
				setAEnd((IAssociationEnd)newValue);
				return;
			case MetamodelPackage.IASSOCIATION_ARTIFACT__ZEND:
				setZEnd((IAssociationEnd)newValue);
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
			case MetamodelPackage.IASSOCIATION_ARTIFACT__AEND:
				setAEnd((IAssociationEnd)null);
				return;
			case MetamodelPackage.IASSOCIATION_ARTIFACT__ZEND:
				setZEnd((IAssociationEnd)null);
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
			case MetamodelPackage.IASSOCIATION_ARTIFACT__AEND:
				return aEnd != null;
			case MetamodelPackage.IASSOCIATION_ARTIFACT__ZEND:
				return zEnd != null;
		}
		return super.eIsSet(featureID);
	}

} //IAssociationArtifactImpl
