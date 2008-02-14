/**
 * <copyright>
 * </copyright>
 *
 * $Id: IStereotypeInstanceImpl.java,v 1.2 2008/05/22 18:26:28 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.tigerstripe.metamodel.IStereotypeAttributeValue;
import org.eclipse.tigerstripe.metamodel.IStereotypeInstance;
import org.eclipse.tigerstripe.metamodel.MetamodelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IStereotype Instance</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IStereotypeInstanceImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IStereotypeInstanceImpl#getAttributeValues <em>Attribute Values</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IStereotypeInstanceImpl extends EObjectImpl implements IStereotypeInstance {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getAttributeValues() <em>Attribute Values</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAttributeValues()
	 * @generated
	 * @ordered
	 */
	protected EList<IStereotypeAttributeValue> attributeValues;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IStereotypeInstanceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetamodelPackage.Literals.ISTEREOTYPE_INSTANCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.ISTEREOTYPE_INSTANCE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IStereotypeAttributeValue> getAttributeValues() {
		if (attributeValues == null) {
			attributeValues = new EObjectContainmentEList<IStereotypeAttributeValue>(IStereotypeAttributeValue.class, this, MetamodelPackage.ISTEREOTYPE_INSTANCE__ATTRIBUTE_VALUES);
		}
		return attributeValues;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MetamodelPackage.ISTEREOTYPE_INSTANCE__ATTRIBUTE_VALUES:
				return ((InternalEList<?>)getAttributeValues()).basicRemove(otherEnd, msgs);
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
			case MetamodelPackage.ISTEREOTYPE_INSTANCE__NAME:
				return getName();
			case MetamodelPackage.ISTEREOTYPE_INSTANCE__ATTRIBUTE_VALUES:
				return getAttributeValues();
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
			case MetamodelPackage.ISTEREOTYPE_INSTANCE__NAME:
				setName((String)newValue);
				return;
			case MetamodelPackage.ISTEREOTYPE_INSTANCE__ATTRIBUTE_VALUES:
				getAttributeValues().clear();
				getAttributeValues().addAll((Collection<? extends IStereotypeAttributeValue>)newValue);
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
			case MetamodelPackage.ISTEREOTYPE_INSTANCE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case MetamodelPackage.ISTEREOTYPE_INSTANCE__ATTRIBUTE_VALUES:
				getAttributeValues().clear();
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
			case MetamodelPackage.ISTEREOTYPE_INSTANCE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case MetamodelPackage.ISTEREOTYPE_INSTANCE__ATTRIBUTE_VALUES:
				return attributeValues != null && !attributeValues.isEmpty();
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
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //IStereotypeInstanceImpl
