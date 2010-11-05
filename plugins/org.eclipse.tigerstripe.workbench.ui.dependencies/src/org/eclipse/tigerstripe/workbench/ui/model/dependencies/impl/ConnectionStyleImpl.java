/**
 * <copyright>
 * </copyright>
 *
 * $Id: ConnectionStyleImpl.java,v 1.4 2010/11/18 17:00:38 ystrot Exp $
 */
package org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.tigerstripe.workbench.ui.model.dependencies.ConnectionStyle;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Connection Style</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.model.dependencies.impl.ConnectionStyleImpl#getStrokeColor <em>Stroke Color</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ConnectionStyleImpl extends EObjectImpl implements ConnectionStyle {
	/**
	 * The default value of the '{@link #getStrokeColor() <em>Stroke Color</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStrokeColor()
	 * @generated
	 * @ordered
	 */
	protected static final int STROKE_COLOR_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getStrokeColor() <em>Stroke Color</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStrokeColor()
	 * @generated
	 * @ordered
	 */
	protected int strokeColor = STROKE_COLOR_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ConnectionStyleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DependenciesPackage.Literals.CONNECTION_STYLE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getStrokeColor() {
		return strokeColor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStrokeColor(int newStrokeColor) {
		int oldStrokeColor = strokeColor;
		strokeColor = newStrokeColor;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DependenciesPackage.CONNECTION_STYLE__STROKE_COLOR, oldStrokeColor, strokeColor));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DependenciesPackage.CONNECTION_STYLE__STROKE_COLOR:
				return getStrokeColor();
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
			case DependenciesPackage.CONNECTION_STYLE__STROKE_COLOR:
				setStrokeColor((Integer)newValue);
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
			case DependenciesPackage.CONNECTION_STYLE__STROKE_COLOR:
				setStrokeColor(STROKE_COLOR_EDEFAULT);
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
			case DependenciesPackage.CONNECTION_STYLE__STROKE_COLOR:
				return strokeColor != STROKE_COLOR_EDEFAULT;
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
		result.append(" (strokeColor: ");
		result.append(strokeColor);
		result.append(')');
		return result.toString();
	}

} //ConnectionStyleImpl
