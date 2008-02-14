/**
 * <copyright>
 * </copyright>
 *
 * $Id: IStereotypeCapableImpl.java,v 1.2 2008/05/22 18:26:28 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.impl;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.eclipse.tigerstripe.metamodel.IStereotypeCapable;
import org.eclipse.tigerstripe.metamodel.IStereotypeInstance;
import org.eclipse.tigerstripe.metamodel.MetamodelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IStereotype Capable</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IStereotypeCapableImpl#getStereotypeInstances <em>Stereotype Instances</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IStereotypeCapableImpl extends EObjectImpl implements IStereotypeCapable {
	/**
	 * The cached value of the '{@link #getStereotypeInstances() <em>Stereotype Instances</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStereotypeInstances()
	 * @generated
	 * @ordered
	 */
	protected EList<IStereotypeInstance> stereotypeInstances;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IStereotypeCapableImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetamodelPackage.Literals.ISTEREOTYPE_CAPABLE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IStereotypeInstance> getStereotypeInstances() {
		if (stereotypeInstances == null) {
			stereotypeInstances = new EObjectResolvingEList<IStereotypeInstance>(IStereotypeInstance.class, this, MetamodelPackage.ISTEREOTYPE_CAPABLE__STEREOTYPE_INSTANCES);
		}
		return stereotypeInstances;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IStereotypeInstance getStereotypeInstanceByName(String name) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean hasStereotypeInstance(String name) {
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
			case MetamodelPackage.ISTEREOTYPE_CAPABLE__STEREOTYPE_INSTANCES:
				return getStereotypeInstances();
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
			case MetamodelPackage.ISTEREOTYPE_CAPABLE__STEREOTYPE_INSTANCES:
				getStereotypeInstances().clear();
				getStereotypeInstances().addAll((Collection<? extends IStereotypeInstance>)newValue);
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
			case MetamodelPackage.ISTEREOTYPE_CAPABLE__STEREOTYPE_INSTANCES:
				getStereotypeInstances().clear();
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
			case MetamodelPackage.ISTEREOTYPE_CAPABLE__STEREOTYPE_INSTANCES:
				return stereotypeInstances != null && !stereotypeInstances.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //IStereotypeCapableImpl
