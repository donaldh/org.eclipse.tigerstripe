/**
 * <copyright>
 * </copyright>
 *
 * $Id: IQueryArtifactImpl.java,v 1.2 2008/05/22 18:26:28 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.impl;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.eclipse.tigerstripe.metamodel.IQueryArtifact;
import org.eclipse.tigerstripe.metamodel.IType;
import org.eclipse.tigerstripe.metamodel.MetamodelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IQuery Artifact</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IQueryArtifactImpl#getReturnedType <em>Returned Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IQueryArtifactImpl extends IAbstractArtifactImpl implements IQueryArtifact {
	/**
	 * The cached value of the '{@link #getReturnedType() <em>Returned Type</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReturnedType()
	 * @generated
	 * @ordered
	 */
	protected EList<IType> returnedType;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IQueryArtifactImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetamodelPackage.Literals.IQUERY_ARTIFACT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IType> getReturnedType() {
		if (returnedType == null) {
			returnedType = new EObjectResolvingEList<IType>(IType.class, this, MetamodelPackage.IQUERY_ARTIFACT__RETURNED_TYPE);
		}
		return returnedType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MetamodelPackage.IQUERY_ARTIFACT__RETURNED_TYPE:
				return getReturnedType();
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
			case MetamodelPackage.IQUERY_ARTIFACT__RETURNED_TYPE:
				getReturnedType().clear();
				getReturnedType().addAll((Collection<? extends IType>)newValue);
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
			case MetamodelPackage.IQUERY_ARTIFACT__RETURNED_TYPE:
				getReturnedType().clear();
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
			case MetamodelPackage.IQUERY_ARTIFACT__RETURNED_TYPE:
				return returnedType != null && !returnedType.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //IQueryArtifactImpl
