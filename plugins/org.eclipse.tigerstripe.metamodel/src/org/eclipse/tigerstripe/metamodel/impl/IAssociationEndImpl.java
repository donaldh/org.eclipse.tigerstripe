/**
 * <copyright>
 * </copyright>
 *
 * $Id: IAssociationEndImpl.java,v 1.2 2008/05/22 18:26:28 edillon Exp $
 */
package org.eclipse.tigerstripe.metamodel.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.tigerstripe.metamodel.EAggregationEnum;
import org.eclipse.tigerstripe.metamodel.EChangeableEnum;
import org.eclipse.tigerstripe.metamodel.IAbstractArtifact;
import org.eclipse.tigerstripe.metamodel.IAssociationEnd;
import org.eclipse.tigerstripe.metamodel.IMultiplicity;
import org.eclipse.tigerstripe.metamodel.MetamodelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IAssociation End</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IAssociationEndImpl#getAggregation <em>Aggregation</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IAssociationEndImpl#getChangeable <em>Changeable</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IAssociationEndImpl#isNavigable <em>Navigable</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IAssociationEndImpl#isOrdered <em>Ordered</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IAssociationEndImpl#isUnique <em>Unique</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IAssociationEndImpl#getMultiplicity <em>Multiplicity</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.metamodel.impl.IAssociationEndImpl#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IAssociationEndImpl extends IModelComponentImpl implements IAssociationEnd {
	/**
	 * The default value of the '{@link #getAggregation() <em>Aggregation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAggregation()
	 * @generated
	 * @ordered
	 */
	protected static final EAggregationEnum AGGREGATION_EDEFAULT = EAggregationEnum.NONE;

	/**
	 * The cached value of the '{@link #getAggregation() <em>Aggregation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAggregation()
	 * @generated
	 * @ordered
	 */
	protected EAggregationEnum aggregation = AGGREGATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getChangeable() <em>Changeable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChangeable()
	 * @generated
	 * @ordered
	 */
	protected static final EChangeableEnum CHANGEABLE_EDEFAULT = EChangeableEnum.NONE;

	/**
	 * The cached value of the '{@link #getChangeable() <em>Changeable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChangeable()
	 * @generated
	 * @ordered
	 */
	protected EChangeableEnum changeable = CHANGEABLE_EDEFAULT;

	/**
	 * The default value of the '{@link #isNavigable() <em>Navigable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isNavigable()
	 * @generated
	 * @ordered
	 */
	protected static final boolean NAVIGABLE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isNavigable() <em>Navigable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isNavigable()
	 * @generated
	 * @ordered
	 */
	protected boolean navigable = NAVIGABLE_EDEFAULT;

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
	 * The cached value of the '{@link #getMultiplicity() <em>Multiplicity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMultiplicity()
	 * @generated
	 * @ordered
	 */
	protected IMultiplicity multiplicity;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected IAbstractArtifact type;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IAssociationEndImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MetamodelPackage.Literals.IASSOCIATION_END;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAggregationEnum getAggregation() {
		return aggregation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAggregation(EAggregationEnum newAggregation) {
		EAggregationEnum oldAggregation = aggregation;
		aggregation = newAggregation == null ? AGGREGATION_EDEFAULT : newAggregation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IASSOCIATION_END__AGGREGATION, oldAggregation, aggregation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EChangeableEnum getChangeable() {
		return changeable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setChangeable(EChangeableEnum newChangeable) {
		EChangeableEnum oldChangeable = changeable;
		changeable = newChangeable == null ? CHANGEABLE_EDEFAULT : newChangeable;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IASSOCIATION_END__CHANGEABLE, oldChangeable, changeable));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isNavigable() {
		return navigable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNavigable(boolean newNavigable) {
		boolean oldNavigable = navigable;
		navigable = newNavigable;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IASSOCIATION_END__NAVIGABLE, oldNavigable, navigable));
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
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IASSOCIATION_END__ORDERED, oldOrdered, ordered));
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
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IASSOCIATION_END__UNIQUE, oldUnique, unique));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IMultiplicity getMultiplicity() {
		if (multiplicity != null && multiplicity.eIsProxy()) {
			InternalEObject oldMultiplicity = (InternalEObject)multiplicity;
			multiplicity = (IMultiplicity)eResolveProxy(oldMultiplicity);
			if (multiplicity != oldMultiplicity) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MetamodelPackage.IASSOCIATION_END__MULTIPLICITY, oldMultiplicity, multiplicity));
			}
		}
		return multiplicity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IMultiplicity basicGetMultiplicity() {
		return multiplicity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMultiplicity(IMultiplicity newMultiplicity) {
		IMultiplicity oldMultiplicity = multiplicity;
		multiplicity = newMultiplicity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IASSOCIATION_END__MULTIPLICITY, oldMultiplicity, multiplicity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IAbstractArtifact getType() {
		if (type != null && type.eIsProxy()) {
			InternalEObject oldType = (InternalEObject)type;
			type = (IAbstractArtifact)eResolveProxy(oldType);
			if (type != oldType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MetamodelPackage.IASSOCIATION_END__TYPE, oldType, type));
			}
		}
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IAbstractArtifact basicGetType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(IAbstractArtifact newType) {
		IAbstractArtifact oldType = type;
		type = newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MetamodelPackage.IASSOCIATION_END__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MetamodelPackage.IASSOCIATION_END__AGGREGATION:
				return getAggregation();
			case MetamodelPackage.IASSOCIATION_END__CHANGEABLE:
				return getChangeable();
			case MetamodelPackage.IASSOCIATION_END__NAVIGABLE:
				return isNavigable() ? Boolean.TRUE : Boolean.FALSE;
			case MetamodelPackage.IASSOCIATION_END__ORDERED:
				return isOrdered() ? Boolean.TRUE : Boolean.FALSE;
			case MetamodelPackage.IASSOCIATION_END__UNIQUE:
				return isUnique() ? Boolean.TRUE : Boolean.FALSE;
			case MetamodelPackage.IASSOCIATION_END__MULTIPLICITY:
				if (resolve) return getMultiplicity();
				return basicGetMultiplicity();
			case MetamodelPackage.IASSOCIATION_END__TYPE:
				if (resolve) return getType();
				return basicGetType();
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
			case MetamodelPackage.IASSOCIATION_END__AGGREGATION:
				setAggregation((EAggregationEnum)newValue);
				return;
			case MetamodelPackage.IASSOCIATION_END__CHANGEABLE:
				setChangeable((EChangeableEnum)newValue);
				return;
			case MetamodelPackage.IASSOCIATION_END__NAVIGABLE:
				setNavigable(((Boolean)newValue).booleanValue());
				return;
			case MetamodelPackage.IASSOCIATION_END__ORDERED:
				setOrdered(((Boolean)newValue).booleanValue());
				return;
			case MetamodelPackage.IASSOCIATION_END__UNIQUE:
				setUnique(((Boolean)newValue).booleanValue());
				return;
			case MetamodelPackage.IASSOCIATION_END__MULTIPLICITY:
				setMultiplicity((IMultiplicity)newValue);
				return;
			case MetamodelPackage.IASSOCIATION_END__TYPE:
				setType((IAbstractArtifact)newValue);
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
			case MetamodelPackage.IASSOCIATION_END__AGGREGATION:
				setAggregation(AGGREGATION_EDEFAULT);
				return;
			case MetamodelPackage.IASSOCIATION_END__CHANGEABLE:
				setChangeable(CHANGEABLE_EDEFAULT);
				return;
			case MetamodelPackage.IASSOCIATION_END__NAVIGABLE:
				setNavigable(NAVIGABLE_EDEFAULT);
				return;
			case MetamodelPackage.IASSOCIATION_END__ORDERED:
				setOrdered(ORDERED_EDEFAULT);
				return;
			case MetamodelPackage.IASSOCIATION_END__UNIQUE:
				setUnique(UNIQUE_EDEFAULT);
				return;
			case MetamodelPackage.IASSOCIATION_END__MULTIPLICITY:
				setMultiplicity((IMultiplicity)null);
				return;
			case MetamodelPackage.IASSOCIATION_END__TYPE:
				setType((IAbstractArtifact)null);
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
			case MetamodelPackage.IASSOCIATION_END__AGGREGATION:
				return aggregation != AGGREGATION_EDEFAULT;
			case MetamodelPackage.IASSOCIATION_END__CHANGEABLE:
				return changeable != CHANGEABLE_EDEFAULT;
			case MetamodelPackage.IASSOCIATION_END__NAVIGABLE:
				return navigable != NAVIGABLE_EDEFAULT;
			case MetamodelPackage.IASSOCIATION_END__ORDERED:
				return ordered != ORDERED_EDEFAULT;
			case MetamodelPackage.IASSOCIATION_END__UNIQUE:
				return unique != UNIQUE_EDEFAULT;
			case MetamodelPackage.IASSOCIATION_END__MULTIPLICITY:
				return multiplicity != null;
			case MetamodelPackage.IASSOCIATION_END__TYPE:
				return type != null;
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
		result.append(" (aggregation: ");
		result.append(aggregation);
		result.append(", changeable: ");
		result.append(changeable);
		result.append(", navigable: ");
		result.append(navigable);
		result.append(", ordered: ");
		result.append(ordered);
		result.append(", unique: ");
		result.append(unique);
		result.append(')');
		return result.toString();
	}

} //IAssociationEndImpl
