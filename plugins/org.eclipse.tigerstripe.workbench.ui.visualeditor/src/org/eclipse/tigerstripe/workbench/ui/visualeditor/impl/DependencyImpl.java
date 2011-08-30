/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.visualeditor.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Dependency</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.DependencyImpl#getAEnd <em>AEnd</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.DependencyImpl#getAEndMultiplicity <em>AEnd Multiplicity</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.DependencyImpl#isAEndIsNavigable <em>AEnd Is Navigable</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.DependencyImpl#getZEnd <em>ZEnd</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.DependencyImpl#getZEndMultiplicity <em>ZEnd Multiplicity</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.DependencyImpl#isZEndIsNavigable <em>ZEnd Is Navigable</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class DependencyImpl extends QualifiedNamedElementImpl implements
		Dependency {
	/**
	 * The cached value of the '{@link #getAEnd() <em>AEnd</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAEnd()
	 * @generated
	 * @ordered
	 */
	protected AbstractArtifact aEnd = null;

	/**
	 * The default value of the '{@link #getAEndMultiplicity() <em>AEnd Multiplicity</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAEndMultiplicity()
	 * @generated
	 * @ordered
	 */
	protected static final AssocMultiplicity AEND_MULTIPLICITY_EDEFAULT = AssocMultiplicity.ONE_LITERAL;

	/**
	 * The cached value of the '{@link #getAEndMultiplicity() <em>AEnd Multiplicity</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAEndMultiplicity()
	 * @generated
	 * @ordered
	 */
	protected AssocMultiplicity aEndMultiplicity = AEND_MULTIPLICITY_EDEFAULT;

	/**
	 * The default value of the '{@link #isAEndIsNavigable() <em>AEnd Is Navigable</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isAEndIsNavigable()
	 * @generated
	 * @ordered
	 */
	protected static final boolean AEND_IS_NAVIGABLE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isAEndIsNavigable() <em>AEnd Is Navigable</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isAEndIsNavigable()
	 * @generated
	 * @ordered
	 */
	protected boolean aEndIsNavigable = AEND_IS_NAVIGABLE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getZEnd() <em>ZEnd</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getZEnd()
	 * @generated
	 * @ordered
	 */
	protected AbstractArtifact zEnd = null;

	/**
	 * The default value of the '{@link #getZEndMultiplicity() <em>ZEnd Multiplicity</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getZEndMultiplicity()
	 * @generated
	 * @ordered
	 */
	protected static final AssocMultiplicity ZEND_MULTIPLICITY_EDEFAULT = AssocMultiplicity.ONE_LITERAL;

	/**
	 * The cached value of the '{@link #getZEndMultiplicity() <em>ZEnd Multiplicity</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getZEndMultiplicity()
	 * @generated
	 * @ordered
	 */
	protected AssocMultiplicity zEndMultiplicity = ZEND_MULTIPLICITY_EDEFAULT;

	/**
	 * The default value of the '{@link #isZEndIsNavigable() <em>ZEnd Is Navigable</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isZEndIsNavigable()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ZEND_IS_NAVIGABLE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isZEndIsNavigable() <em>ZEnd Is Navigable</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isZEndIsNavigable()
	 * @generated
	 * @ordered
	 */
	protected boolean zEndIsNavigable = ZEND_IS_NAVIGABLE_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected DependencyImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return VisualeditorPackage.Literals.DEPENDENCY;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AbstractArtifact getAEnd() {
		if (aEnd != null && aEnd.eIsProxy()) {
			InternalEObject oldAEnd = (InternalEObject) aEnd;
			aEnd = (AbstractArtifact) eResolveProxy(oldAEnd);
			if (aEnd != oldAEnd) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							VisualeditorPackage.DEPENDENCY__AEND, oldAEnd, aEnd));
			}
		}
		return aEnd;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AbstractArtifact basicGetAEnd() {
		return aEnd;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setAEnd(AbstractArtifact newAEnd) {
		AbstractArtifact oldAEnd = aEnd;
		aEnd = newAEnd;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.DEPENDENCY__AEND, oldAEnd, aEnd));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AssocMultiplicity getAEndMultiplicity() {
		return aEndMultiplicity;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setAEndMultiplicity(AssocMultiplicity newAEndMultiplicity) {
		AssocMultiplicity oldAEndMultiplicity = aEndMultiplicity;
		aEndMultiplicity = newAEndMultiplicity == null ? AEND_MULTIPLICITY_EDEFAULT
				: newAEndMultiplicity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.DEPENDENCY__AEND_MULTIPLICITY,
					oldAEndMultiplicity, aEndMultiplicity));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isAEndIsNavigable() {
		return aEndIsNavigable;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setAEndIsNavigable(boolean newAEndIsNavigable) {
		boolean oldAEndIsNavigable = aEndIsNavigable;
		aEndIsNavigable = newAEndIsNavigable;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.DEPENDENCY__AEND_IS_NAVIGABLE,
					oldAEndIsNavigable, aEndIsNavigable));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AbstractArtifact getZEnd() {
		if (zEnd != null && zEnd.eIsProxy()) {
			InternalEObject oldZEnd = (InternalEObject) zEnd;
			zEnd = (AbstractArtifact) eResolveProxy(oldZEnd);
			if (zEnd != oldZEnd) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							VisualeditorPackage.DEPENDENCY__ZEND, oldZEnd, zEnd));
			}
		}
		return zEnd;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AbstractArtifact basicGetZEnd() {
		return zEnd;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setZEnd(AbstractArtifact newZEnd) {
		AbstractArtifact oldZEnd = zEnd;
		zEnd = newZEnd;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.DEPENDENCY__ZEND, oldZEnd, zEnd));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AssocMultiplicity getZEndMultiplicity() {
		return zEndMultiplicity;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setZEndMultiplicity(AssocMultiplicity newZEndMultiplicity) {
		AssocMultiplicity oldZEndMultiplicity = zEndMultiplicity;
		zEndMultiplicity = newZEndMultiplicity == null ? ZEND_MULTIPLICITY_EDEFAULT
				: newZEndMultiplicity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.DEPENDENCY__ZEND_MULTIPLICITY,
					oldZEndMultiplicity, zEndMultiplicity));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isZEndIsNavigable() {
		return zEndIsNavigable;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setZEndIsNavigable(boolean newZEndIsNavigable) {
		boolean oldZEndIsNavigable = zEndIsNavigable;
		zEndIsNavigable = newZEndIsNavigable;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.DEPENDENCY__ZEND_IS_NAVIGABLE,
					oldZEndIsNavigable, zEndIsNavigable));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case VisualeditorPackage.DEPENDENCY__AEND:
			if (resolve)
				return getAEnd();
			return basicGetAEnd();
		case VisualeditorPackage.DEPENDENCY__AEND_MULTIPLICITY:
			return getAEndMultiplicity();
		case VisualeditorPackage.DEPENDENCY__AEND_IS_NAVIGABLE:
			return isAEndIsNavigable() ? Boolean.TRUE : Boolean.FALSE;
		case VisualeditorPackage.DEPENDENCY__ZEND:
			if (resolve)
				return getZEnd();
			return basicGetZEnd();
		case VisualeditorPackage.DEPENDENCY__ZEND_MULTIPLICITY:
			return getZEndMultiplicity();
		case VisualeditorPackage.DEPENDENCY__ZEND_IS_NAVIGABLE:
			return isZEndIsNavigable() ? Boolean.TRUE : Boolean.FALSE;
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
		case VisualeditorPackage.DEPENDENCY__AEND:
			setAEnd((AbstractArtifact) newValue);
			return;
		case VisualeditorPackage.DEPENDENCY__AEND_MULTIPLICITY:
			setAEndMultiplicity((AssocMultiplicity) newValue);
			return;
		case VisualeditorPackage.DEPENDENCY__AEND_IS_NAVIGABLE:
			setAEndIsNavigable(((Boolean) newValue).booleanValue());
			return;
		case VisualeditorPackage.DEPENDENCY__ZEND:
			setZEnd((AbstractArtifact) newValue);
			return;
		case VisualeditorPackage.DEPENDENCY__ZEND_MULTIPLICITY:
			setZEndMultiplicity((AssocMultiplicity) newValue);
			return;
		case VisualeditorPackage.DEPENDENCY__ZEND_IS_NAVIGABLE:
			setZEndIsNavigable(((Boolean) newValue).booleanValue());
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
		case VisualeditorPackage.DEPENDENCY__AEND:
			setAEnd((AbstractArtifact) null);
			return;
		case VisualeditorPackage.DEPENDENCY__AEND_MULTIPLICITY:
			setAEndMultiplicity(AEND_MULTIPLICITY_EDEFAULT);
			return;
		case VisualeditorPackage.DEPENDENCY__AEND_IS_NAVIGABLE:
			setAEndIsNavigable(AEND_IS_NAVIGABLE_EDEFAULT);
			return;
		case VisualeditorPackage.DEPENDENCY__ZEND:
			setZEnd((AbstractArtifact) null);
			return;
		case VisualeditorPackage.DEPENDENCY__ZEND_MULTIPLICITY:
			setZEndMultiplicity(ZEND_MULTIPLICITY_EDEFAULT);
			return;
		case VisualeditorPackage.DEPENDENCY__ZEND_IS_NAVIGABLE:
			setZEndIsNavigable(ZEND_IS_NAVIGABLE_EDEFAULT);
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
		case VisualeditorPackage.DEPENDENCY__AEND:
			return aEnd != null;
		case VisualeditorPackage.DEPENDENCY__AEND_MULTIPLICITY:
			return aEndMultiplicity != AEND_MULTIPLICITY_EDEFAULT;
		case VisualeditorPackage.DEPENDENCY__AEND_IS_NAVIGABLE:
			return aEndIsNavigable != AEND_IS_NAVIGABLE_EDEFAULT;
		case VisualeditorPackage.DEPENDENCY__ZEND:
			return zEnd != null;
		case VisualeditorPackage.DEPENDENCY__ZEND_MULTIPLICITY:
			return zEndMultiplicity != ZEND_MULTIPLICITY_EDEFAULT;
		case VisualeditorPackage.DEPENDENCY__ZEND_IS_NAVIGABLE:
			return zEndIsNavigable != ZEND_IS_NAVIGABLE_EDEFAULT;
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
		result.append(" (aEndMultiplicity: ");
		result.append(aEndMultiplicity);
		result.append(", aEndIsNavigable: ");
		result.append(aEndIsNavigable);
		result.append(", zEndMultiplicity: ");
		result.append(zEndMultiplicity);
		result.append(", zEndIsNavigable: ");
		result.append(zEndIsNavigable);
		result.append(')');
		return result.toString();
	}

} // DependencyImpl
