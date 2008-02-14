/**
 * <copyright>
 * </copyright>
 *
 * $Id: IManagedEntityDetailsImpl.java,v 1.1 2008/02/14 23:58:01 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.extensions.ossj.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.eclipse.tigerstripe.metamodel.IEntityMethodFlavorDetails;
import org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact;
import org.eclipse.tigerstripe.metamodel.OssjEntityMethodFlavor;

import org.eclipse.tigerstripe.metamodel.extensions.ossj.EMethodType;
import org.eclipse.tigerstripe.metamodel.extensions.ossj.IManagedEntityDetails;
import org.eclipse.tigerstripe.metamodel.extensions.ossj.OssjPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IManaged Entity Details</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IManagedEntityDetailsImpl#getManagedEntity <em>Managed Entity</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IManagedEntityDetailsImpl#getCrudFlavorDetails <em>Crud Flavor Details</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.extensions.ossj.impl.IManagedEntityDetailsImpl#getCustomMethodFlavorDetails <em>Custom Method Flavor Details</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IManagedEntityDetailsImpl extends EObjectImpl implements IManagedEntityDetails {
	/**
	 * The cached value of the '{@link #getManagedEntity() <em>Managed Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getManagedEntity()
	 * @generated
	 * @ordered
	 */
	protected IManagedEntityArtifact managedEntity;

	/**
	 * The cached value of the '{@link #getCrudFlavorDetails() <em>Crud Flavor Details</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCrudFlavorDetails()
	 * @generated
	 * @ordered
	 */
	protected EList<IEntityMethodFlavorDetails> crudFlavorDetails;

	/**
	 * The cached value of the '{@link #getCustomMethodFlavorDetails() <em>Custom Method Flavor Details</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCustomMethodFlavorDetails()
	 * @generated
	 * @ordered
	 */
	protected EList<IEntityMethodFlavorDetails> customMethodFlavorDetails;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IManagedEntityDetailsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OssjPackage.Literals.IMANAGED_ENTITY_DETAILS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IManagedEntityArtifact getManagedEntity() {
		if (managedEntity != null && managedEntity.eIsProxy()) {
			InternalEObject oldManagedEntity = (InternalEObject)managedEntity;
			managedEntity = (IManagedEntityArtifact)eResolveProxy(oldManagedEntity);
			if (managedEntity != oldManagedEntity) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, OssjPackage.IMANAGED_ENTITY_DETAILS__MANAGED_ENTITY, oldManagedEntity, managedEntity));
			}
		}
		return managedEntity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IManagedEntityArtifact basicGetManagedEntity() {
		return managedEntity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setManagedEntity(IManagedEntityArtifact newManagedEntity) {
		IManagedEntityArtifact oldManagedEntity = managedEntity;
		managedEntity = newManagedEntity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OssjPackage.IMANAGED_ENTITY_DETAILS__MANAGED_ENTITY, oldManagedEntity, managedEntity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IEntityMethodFlavorDetails> getCrudFlavorDetails() {
		if (crudFlavorDetails == null) {
			crudFlavorDetails = new EObjectResolvingEList<IEntityMethodFlavorDetails>(IEntityMethodFlavorDetails.class, this, OssjPackage.IMANAGED_ENTITY_DETAILS__CRUD_FLAVOR_DETAILS);
		}
		return crudFlavorDetails;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IEntityMethodFlavorDetails> getCustomMethodFlavorDetails() {
		if (customMethodFlavorDetails == null) {
			customMethodFlavorDetails = new EObjectResolvingEList<IEntityMethodFlavorDetails>(IEntityMethodFlavorDetails.class, this, OssjPackage.IMANAGED_ENTITY_DETAILS__CUSTOM_METHOD_FLAVOR_DETAILS);
		}
		return customMethodFlavorDetails;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IEntityMethodFlavorDetails getCRUDFlavorDetails(EMethodType methodType, OssjEntityMethodFlavor flavor) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IEntityMethodFlavorDetails getCustomMethodFlavorDetails(OssjEntityMethodFlavor flavor, String methodID) {
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
			case OssjPackage.IMANAGED_ENTITY_DETAILS__MANAGED_ENTITY:
				if (resolve) return getManagedEntity();
				return basicGetManagedEntity();
			case OssjPackage.IMANAGED_ENTITY_DETAILS__CRUD_FLAVOR_DETAILS:
				return getCrudFlavorDetails();
			case OssjPackage.IMANAGED_ENTITY_DETAILS__CUSTOM_METHOD_FLAVOR_DETAILS:
				return getCustomMethodFlavorDetails();
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
			case OssjPackage.IMANAGED_ENTITY_DETAILS__MANAGED_ENTITY:
				setManagedEntity((IManagedEntityArtifact)newValue);
				return;
			case OssjPackage.IMANAGED_ENTITY_DETAILS__CRUD_FLAVOR_DETAILS:
				getCrudFlavorDetails().clear();
				getCrudFlavorDetails().addAll((Collection<? extends IEntityMethodFlavorDetails>)newValue);
				return;
			case OssjPackage.IMANAGED_ENTITY_DETAILS__CUSTOM_METHOD_FLAVOR_DETAILS:
				getCustomMethodFlavorDetails().clear();
				getCustomMethodFlavorDetails().addAll((Collection<? extends IEntityMethodFlavorDetails>)newValue);
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
			case OssjPackage.IMANAGED_ENTITY_DETAILS__MANAGED_ENTITY:
				setManagedEntity((IManagedEntityArtifact)null);
				return;
			case OssjPackage.IMANAGED_ENTITY_DETAILS__CRUD_FLAVOR_DETAILS:
				getCrudFlavorDetails().clear();
				return;
			case OssjPackage.IMANAGED_ENTITY_DETAILS__CUSTOM_METHOD_FLAVOR_DETAILS:
				getCustomMethodFlavorDetails().clear();
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
			case OssjPackage.IMANAGED_ENTITY_DETAILS__MANAGED_ENTITY:
				return managedEntity != null;
			case OssjPackage.IMANAGED_ENTITY_DETAILS__CRUD_FLAVOR_DETAILS:
				return crudFlavorDetails != null && !crudFlavorDetails.isEmpty();
			case OssjPackage.IMANAGED_ENTITY_DETAILS__CUSTOM_METHOD_FLAVOR_DETAILS:
				return customMethodFlavorDetails != null && !customMethodFlavorDetails.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //IManagedEntityDetailsImpl
