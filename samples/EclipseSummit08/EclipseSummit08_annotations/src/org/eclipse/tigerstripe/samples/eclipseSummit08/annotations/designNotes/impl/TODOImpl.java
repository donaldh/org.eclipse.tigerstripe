/**
 * <copyright>
 * </copyright>
 *
 * $Id: TODOImpl.java,v 1.1 2008/11/05 19:53:22 edillon Exp $
 */
package org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.DesignNotesPackage;
import org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.TODO;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>TODO</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.impl.TODOImpl#isHack <em>Hack</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.samples.eclipseSummit08.annotations.designNotes.impl.TODOImpl#getSummary <em>Summary</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TODOImpl extends EObjectImpl implements TODO {
	/**
	 * The default value of the '{@link #isHack() <em>Hack</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isHack()
	 * @generated
	 * @ordered
	 */
	protected static final boolean HACK_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isHack() <em>Hack</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isHack()
	 * @generated
	 * @ordered
	 */
	protected boolean hack = HACK_EDEFAULT;

	/**
	 * The default value of the '{@link #getSummary() <em>Summary</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSummary()
	 * @generated
	 * @ordered
	 */
	protected static final String SUMMARY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSummary() <em>Summary</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSummary()
	 * @generated
	 * @ordered
	 */
	protected String summary = SUMMARY_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TODOImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DesignNotesPackage.Literals.TODO;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isHack() {
		return hack;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHack(boolean newHack) {
		boolean oldHack = hack;
		hack = newHack;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DesignNotesPackage.TODO__HACK, oldHack, hack));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSummary(String newSummary) {
		String oldSummary = summary;
		summary = newSummary;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DesignNotesPackage.TODO__SUMMARY, oldSummary, summary));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DesignNotesPackage.TODO__HACK:
				return isHack() ? Boolean.TRUE : Boolean.FALSE;
			case DesignNotesPackage.TODO__SUMMARY:
				return getSummary();
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
			case DesignNotesPackage.TODO__HACK:
				setHack(((Boolean)newValue).booleanValue());
				return;
			case DesignNotesPackage.TODO__SUMMARY:
				setSummary((String)newValue);
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
			case DesignNotesPackage.TODO__HACK:
				setHack(HACK_EDEFAULT);
				return;
			case DesignNotesPackage.TODO__SUMMARY:
				setSummary(SUMMARY_EDEFAULT);
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
			case DesignNotesPackage.TODO__HACK:
				return hack != HACK_EDEFAULT;
			case DesignNotesPackage.TODO__SUMMARY:
				return SUMMARY_EDEFAULT == null ? summary != null : !SUMMARY_EDEFAULT.equals(summary);
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
		result.append(" (hack: ");
		result.append(hack);
		result.append(", summary: ");
		result.append(summary);
		result.append(')');
		return result.toString();
	}

} //TODOImpl
