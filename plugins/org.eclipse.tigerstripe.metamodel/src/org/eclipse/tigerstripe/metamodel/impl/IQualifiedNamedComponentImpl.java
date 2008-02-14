/**
 * <copyright>
 * </copyright>
 *
 * $Id: IQualifiedNamedComponentImpl.java,v 1.1 2008/02/14 23:58:00 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.tigerstripe.metamodel.IQualifiedNamedComponent;
import org.eclipse.tigerstripe.metamodel.MetamodelPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>IQualified Named Component</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.metamodel.impl.IQualifiedNamedComponentImpl#getPackage <em>Package</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public abstract class IQualifiedNamedComponentImpl extends IModelComponentImpl
		implements IQualifiedNamedComponent {

	// This is cached for performance
	private String _fullyQualifiedName;

	/**
	 * The default value of the '{@link #getPackage() <em>Package</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getPackage()
	 * @generated
	 * @ordered
	 */
	protected static final String PACKAGE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPackage() <em>Package</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getPackage()
	 * @generated
	 * @ordered
	 */
	protected String package_ = PACKAGE_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected IQualifiedNamedComponentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetamodelPackage.Literals.IQUALIFIED_NAMED_COMPONENT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getPackage() {
		return package_;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setPackage(String newPackage) {
		String oldPackage = package_;
		package_ = newPackage;
		updateFullyQualifiedName();
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					MetamodelPackage.IQUALIFIED_NAMED_COMPONENT__PACKAGE,
					oldPackage, package_));
	}

	@Override
	public void setName(String newName) {
		super.setName(newName);
		updateFullyQualifiedName();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public String getFullyQualifiedName() {
		if (_fullyQualifiedName == null)
			updateFullyQualifiedName();
		return _fullyQualifiedName;
	}

	private void updateFullyQualifiedName() {
		_fullyQualifiedName = name;
		if (package_ != null && package_.length() != 0) {
			_fullyQualifiedName = package_ + "." + name;
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case MetamodelPackage.IQUALIFIED_NAMED_COMPONENT__PACKAGE:
			return getPackage();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case MetamodelPackage.IQUALIFIED_NAMED_COMPONENT__PACKAGE:
			setPackage((String) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case MetamodelPackage.IQUALIFIED_NAMED_COMPONENT__PACKAGE:
			setPackage(PACKAGE_EDEFAULT);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case MetamodelPackage.IQUALIFIED_NAMED_COMPONENT__PACKAGE:
			return PACKAGE_EDEFAULT == null ? package_ != null
					: !PACKAGE_EDEFAULT.equals(package_);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (package: ");
		result.append(package_);
		result.append(')');
		return result.toString();
	}

} // IQualifiedNamedComponentImpl
