/**
 * <copyright>
 * </copyright>
 *
 * $Id: IFieldImpl.java,v 1.1 2008/02/14 23:58:00 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.tigerstripe.metamodel.ERefByEnum;
import org.eclipse.tigerstripe.metamodel.IField;
import org.eclipse.tigerstripe.metamodel.IType;
import org.eclipse.tigerstripe.metamodel.MetamodelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IField</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IFieldImpl#isOptional <em>Optional</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IFieldImpl#isReadOnly <em>Read Only</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IFieldImpl#isOrdered <em>Ordered</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IFieldImpl#isUnique <em>Unique</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IFieldImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IFieldImpl#getDefaultValue <em>Default Value</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IFieldImpl#getRefBy <em>Ref By</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IFieldImpl extends IModelComponentImpl implements IField {
	/**
	 * The default value of the '{@link #isOptional() <em>Optional</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isOptional()
	 * @generated
	 * @ordered
	 */
	protected static final boolean OPTIONAL_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isOptional() <em>Optional</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isOptional()
	 * @generated
	 * @ordered
	 */
	protected boolean optional = OPTIONAL_EDEFAULT;

	/**
	 * The default value of the '{@link #isReadOnly() <em>Read Only</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isReadOnly()
	 * @generated
	 * @ordered
	 */
	protected static final boolean READ_ONLY_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isReadOnly() <em>Read Only</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isReadOnly()
	 * @generated
	 * @ordered
	 */
	protected boolean readOnly = READ_ONLY_EDEFAULT;

	/**
	 * The default value of the '{@link #isOrdered() <em>Ordered</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isOrdered()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ORDERED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isOrdered() <em>Ordered</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isOrdered()
	 * @generated
	 * @ordered
	 */
	protected boolean ordered = ORDERED_EDEFAULT;

	/**
	 * The default value of the '{@link #isUnique() <em>Unique</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUnique()
	 * @generated
	 * @ordered
	 */
	protected static final boolean UNIQUE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isUnique() <em>Unique</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUnique()
	 * @generated
	 * @ordered
	 */
	protected boolean unique = UNIQUE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected IType type;

	/**
	 * The default value of the '{@link #getDefaultValue() <em>Default Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultValue()
	 * @generated
	 * @ordered
	 */
	protected static final String DEFAULT_VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDefaultValue() <em>Default Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultValue()
	 * @generated
	 * @ordered
	 */
	protected String defaultValue = DEFAULT_VALUE_EDEFAULT;

	/**
	 * This is true if the Default Value attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean defaultValueESet;

	/**
	 * The default value of the '{@link #getRefBy() <em>Ref By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRefBy()
	 * @generated
	 * @ordered
	 */
	protected static final ERefByEnum REF_BY_EDEFAULT = ERefByEnum.NON_APPLICABLE;

	/**
	 * The cached value of the '{@link #getRefBy() <em>Ref By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRefBy()
	 * @generated
	 * @ordered
	 */
	protected ERefByEnum refBy = REF_BY_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IFieldImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetamodelPackage.Literals.IFIELD;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isOptional() {
		return optional;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOptional(boolean newOptional) {
		boolean oldOptional = optional;
		optional = newOptional;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IFIELD__OPTIONAL, oldOptional, optional));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReadOnly(boolean newReadOnly) {
		boolean oldReadOnly = readOnly;
		readOnly = newReadOnly;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IFIELD__READ_ONLY, oldReadOnly, readOnly));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isOrdered() {
		return ordered;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOrdered(boolean newOrdered) {
		boolean oldOrdered = ordered;
		ordered = newOrdered;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IFIELD__ORDERED, oldOrdered, ordered));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isUnique() {
		return unique;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUnique(boolean newUnique) {
		boolean oldUnique = unique;
		unique = newUnique;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IFIELD__UNIQUE, oldUnique, unique));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IType getType() {
		if (type != null && type.eIsProxy()) {
			InternalEObject oldType = (InternalEObject)type;
			type = (IType)eResolveProxy(oldType);
			if (type != oldType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MetamodelPackage.IFIELD__TYPE, oldType, type));
			}
		}
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IType basicGetType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(IType newType) {
		IType oldType = type;
		type = newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IFIELD__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefaultValue(String newDefaultValue) {
		String oldDefaultValue = defaultValue;
		defaultValue = newDefaultValue;
		boolean oldDefaultValueESet = defaultValueESet;
		defaultValueESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IFIELD__DEFAULT_VALUE, oldDefaultValue, defaultValue, !oldDefaultValueESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetDefaultValue() {
		String oldDefaultValue = defaultValue;
		boolean oldDefaultValueESet = defaultValueESet;
		defaultValue = DEFAULT_VALUE_EDEFAULT;
		defaultValueESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, MetamodelPackage.IFIELD__DEFAULT_VALUE, oldDefaultValue, DEFAULT_VALUE_EDEFAULT, oldDefaultValueESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetDefaultValue() {
		return defaultValueESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ERefByEnum getRefBy() {
		return refBy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRefBy(ERefByEnum newRefBy) {
		ERefByEnum oldRefBy = refBy;
		refBy = newRefBy == null ? REF_BY_EDEFAULT : newRefBy;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IFIELD__REF_BY, oldRefBy, refBy));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MetamodelPackage.IFIELD__OPTIONAL:
				return isOptional() ? Boolean.TRUE : Boolean.FALSE;
			case MetamodelPackage.IFIELD__READ_ONLY:
				return isReadOnly() ? Boolean.TRUE : Boolean.FALSE;
			case MetamodelPackage.IFIELD__ORDERED:
				return isOrdered() ? Boolean.TRUE : Boolean.FALSE;
			case MetamodelPackage.IFIELD__UNIQUE:
				return isUnique() ? Boolean.TRUE : Boolean.FALSE;
			case MetamodelPackage.IFIELD__TYPE:
				if (resolve) return getType();
				return basicGetType();
			case MetamodelPackage.IFIELD__DEFAULT_VALUE:
				return getDefaultValue();
			case MetamodelPackage.IFIELD__REF_BY:
				return getRefBy();
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
			case MetamodelPackage.IFIELD__OPTIONAL:
				setOptional(((Boolean)newValue).booleanValue());
				return;
			case MetamodelPackage.IFIELD__READ_ONLY:
				setReadOnly(((Boolean)newValue).booleanValue());
				return;
			case MetamodelPackage.IFIELD__ORDERED:
				setOrdered(((Boolean)newValue).booleanValue());
				return;
			case MetamodelPackage.IFIELD__UNIQUE:
				setUnique(((Boolean)newValue).booleanValue());
				return;
			case MetamodelPackage.IFIELD__TYPE:
				setType((IType)newValue);
				return;
			case MetamodelPackage.IFIELD__DEFAULT_VALUE:
				setDefaultValue((String)newValue);
				return;
			case MetamodelPackage.IFIELD__REF_BY:
				setRefBy((ERefByEnum)newValue);
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
			case MetamodelPackage.IFIELD__OPTIONAL:
				setOptional(OPTIONAL_EDEFAULT);
				return;
			case MetamodelPackage.IFIELD__READ_ONLY:
				setReadOnly(READ_ONLY_EDEFAULT);
				return;
			case MetamodelPackage.IFIELD__ORDERED:
				setOrdered(ORDERED_EDEFAULT);
				return;
			case MetamodelPackage.IFIELD__UNIQUE:
				setUnique(UNIQUE_EDEFAULT);
				return;
			case MetamodelPackage.IFIELD__TYPE:
				setType((IType)null);
				return;
			case MetamodelPackage.IFIELD__DEFAULT_VALUE:
				unsetDefaultValue();
				return;
			case MetamodelPackage.IFIELD__REF_BY:
				setRefBy(REF_BY_EDEFAULT);
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
			case MetamodelPackage.IFIELD__OPTIONAL:
				return optional != OPTIONAL_EDEFAULT;
			case MetamodelPackage.IFIELD__READ_ONLY:
				return readOnly != READ_ONLY_EDEFAULT;
			case MetamodelPackage.IFIELD__ORDERED:
				return ordered != ORDERED_EDEFAULT;
			case MetamodelPackage.IFIELD__UNIQUE:
				return unique != UNIQUE_EDEFAULT;
			case MetamodelPackage.IFIELD__TYPE:
				return type != null;
			case MetamodelPackage.IFIELD__DEFAULT_VALUE:
				return isSetDefaultValue();
			case MetamodelPackage.IFIELD__REF_BY:
				return refBy != REF_BY_EDEFAULT;
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
		result.append(" (optional: ");
		result.append(optional);
		result.append(", readOnly: ");
		result.append(readOnly);
		result.append(", ordered: ");
		result.append(ordered);
		result.append(", unique: ");
		result.append(unique);
		result.append(", defaultValue: ");
		if (defaultValueESet) result.append(defaultValue); else result.append("<unset>");
		result.append(", refBy: ");
		result.append(refBy);
		result.append(')');
		return result.toString();
	}

} //IFieldImpl
