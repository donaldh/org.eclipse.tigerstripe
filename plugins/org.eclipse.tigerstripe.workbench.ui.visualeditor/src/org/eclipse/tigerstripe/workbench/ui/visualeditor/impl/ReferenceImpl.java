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
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Reference;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.TypeMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.ReferenceImpl#getMultiplicity <em>Multiplicity</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.ReferenceImpl#getZEnd <em>ZEnd</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.ReferenceImpl#getTypeMultiplicity <em>Type Multiplicity</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.ReferenceImpl#isIsOrdered <em>Is Ordered</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.ReferenceImpl#isIsUnique <em>Is Unique</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ReferenceImpl extends NamedElementImpl implements Reference {
	/**
	 * The default value of the '{@link #getMultiplicity() <em>Multiplicity</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getMultiplicity()
	 * @generated
	 * @ordered
	 */
	protected static final TypeMultiplicity MULTIPLICITY_EDEFAULT = TypeMultiplicity.NONE_LITERAL;

	/**
	 * The cached value of the '{@link #getMultiplicity() <em>Multiplicity</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getMultiplicity()
	 * @generated
	 * @ordered
	 */
	protected TypeMultiplicity multiplicity = MULTIPLICITY_EDEFAULT;

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
	 * The default value of the '{@link #getTypeMultiplicity() <em>Type Multiplicity</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTypeMultiplicity()
	 * @generated
	 * @ordered
	 */
	protected static final AssocMultiplicity TYPE_MULTIPLICITY_EDEFAULT = AssocMultiplicity.ONE_LITERAL;

	/**
	 * The cached value of the '{@link #getTypeMultiplicity() <em>Type Multiplicity</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTypeMultiplicity()
	 * @generated
	 * @ordered
	 */
	protected AssocMultiplicity typeMultiplicity = TYPE_MULTIPLICITY_EDEFAULT;

	/**
	 * The default value of the '{@link #isIsOrdered() <em>Is Ordered</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isIsOrdered()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_ORDERED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsOrdered() <em>Is Ordered</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isIsOrdered()
	 * @generated
	 * @ordered
	 */
	protected boolean isOrdered = IS_ORDERED_EDEFAULT;

	/**
	 * The default value of the '{@link #isIsUnique() <em>Is Unique</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isIsUnique()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_UNIQUE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsUnique() <em>Is Unique</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isIsUnique()
	 * @generated
	 * @ordered
	 */
	protected boolean isUnique = IS_UNIQUE_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ReferenceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return VisualeditorPackage.Literals.REFERENCE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public TypeMultiplicity getMultiplicity() {
		return multiplicity;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setMultiplicity(TypeMultiplicity newMultiplicity) {
		TypeMultiplicity oldMultiplicity = multiplicity;
		multiplicity = newMultiplicity == null ? MULTIPLICITY_EDEFAULT
				: newMultiplicity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.REFERENCE__MULTIPLICITY,
					oldMultiplicity, multiplicity));
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
							VisualeditorPackage.REFERENCE__ZEND, oldZEnd, zEnd));
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
					VisualeditorPackage.REFERENCE__ZEND, oldZEnd, zEnd));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AssocMultiplicity getTypeMultiplicity() {
		return typeMultiplicity;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setTypeMultiplicity(AssocMultiplicity newTypeMultiplicity) {
		AssocMultiplicity oldTypeMultiplicity = typeMultiplicity;
		typeMultiplicity = newTypeMultiplicity == null ? TYPE_MULTIPLICITY_EDEFAULT
				: newTypeMultiplicity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.REFERENCE__TYPE_MULTIPLICITY,
					oldTypeMultiplicity, typeMultiplicity));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isIsOrdered() {
		return isOrdered;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setIsOrdered(boolean newIsOrdered) {
		boolean oldIsOrdered = isOrdered;
		isOrdered = newIsOrdered;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.REFERENCE__IS_ORDERED, oldIsOrdered,
					isOrdered));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isIsUnique() {
		return isUnique;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setIsUnique(boolean newIsUnique) {
		boolean oldIsUnique = isUnique;
		isUnique = newIsUnique;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.REFERENCE__IS_UNIQUE, oldIsUnique,
					isUnique));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case VisualeditorPackage.REFERENCE__MULTIPLICITY:
			return getMultiplicity();
		case VisualeditorPackage.REFERENCE__ZEND:
			if (resolve)
				return getZEnd();
			return basicGetZEnd();
		case VisualeditorPackage.REFERENCE__TYPE_MULTIPLICITY:
			return getTypeMultiplicity();
		case VisualeditorPackage.REFERENCE__IS_ORDERED:
			return isIsOrdered() ? Boolean.TRUE : Boolean.FALSE;
		case VisualeditorPackage.REFERENCE__IS_UNIQUE:
			return isIsUnique() ? Boolean.TRUE : Boolean.FALSE;
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
		case VisualeditorPackage.REFERENCE__MULTIPLICITY:
			setMultiplicity((TypeMultiplicity) newValue);
			return;
		case VisualeditorPackage.REFERENCE__ZEND:
			setZEnd((AbstractArtifact) newValue);
			return;
		case VisualeditorPackage.REFERENCE__TYPE_MULTIPLICITY:
			setTypeMultiplicity((AssocMultiplicity) newValue);
			return;
		case VisualeditorPackage.REFERENCE__IS_ORDERED:
			setIsOrdered(((Boolean) newValue).booleanValue());
			return;
		case VisualeditorPackage.REFERENCE__IS_UNIQUE:
			setIsUnique(((Boolean) newValue).booleanValue());
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
		case VisualeditorPackage.REFERENCE__MULTIPLICITY:
			setMultiplicity(MULTIPLICITY_EDEFAULT);
			return;
		case VisualeditorPackage.REFERENCE__ZEND:
			setZEnd((AbstractArtifact) null);
			return;
		case VisualeditorPackage.REFERENCE__TYPE_MULTIPLICITY:
			setTypeMultiplicity(TYPE_MULTIPLICITY_EDEFAULT);
			return;
		case VisualeditorPackage.REFERENCE__IS_ORDERED:
			setIsOrdered(IS_ORDERED_EDEFAULT);
			return;
		case VisualeditorPackage.REFERENCE__IS_UNIQUE:
			setIsUnique(IS_UNIQUE_EDEFAULT);
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
		case VisualeditorPackage.REFERENCE__MULTIPLICITY:
			return multiplicity != MULTIPLICITY_EDEFAULT;
		case VisualeditorPackage.REFERENCE__ZEND:
			return zEnd != null;
		case VisualeditorPackage.REFERENCE__TYPE_MULTIPLICITY:
			return typeMultiplicity != TYPE_MULTIPLICITY_EDEFAULT;
		case VisualeditorPackage.REFERENCE__IS_ORDERED:
			return isOrdered != IS_ORDERED_EDEFAULT;
		case VisualeditorPackage.REFERENCE__IS_UNIQUE:
			return isUnique != IS_UNIQUE_EDEFAULT;
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
		result.append(" (multiplicity: ");
		result.append(multiplicity);
		result.append(", typeMultiplicity: ");
		result.append(typeMultiplicity);
		result.append(", isOrdered: ");
		result.append(isOrdered);
		result.append(", isUnique: ");
		result.append(isUnique);
		result.append(')');
		return result.toString();
	}

} // ReferenceImpl
