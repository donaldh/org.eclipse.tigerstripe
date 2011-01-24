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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AggregationEnum;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ChangeableEnum;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.util.InstanceDiagramUtils;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Association Instance</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.AssociationInstanceImpl#getAEnd <em>AEnd</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.AssociationInstanceImpl#getAEndName <em>AEnd Name</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.AssociationInstanceImpl#getAEndMultiplicityLowerBound <em>AEnd Multiplicity Lower Bound</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.AssociationInstanceImpl#getAEndMultiplicityUpperBound <em>AEnd Multiplicity Upper Bound</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.AssociationInstanceImpl#isAEndIsNavigable <em>AEnd Is Navigable</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.AssociationInstanceImpl#isAEndIsOrdered <em>AEnd Is Ordered</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.AssociationInstanceImpl#getAEndIsChangeable <em>AEnd Is Changeable</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.AssociationInstanceImpl#getAEndAggregation <em>AEnd Aggregation</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.AssociationInstanceImpl#getZEnd <em>ZEnd</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.AssociationInstanceImpl#getZEndName <em>ZEnd Name</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.AssociationInstanceImpl#getZEndMultiplicityLowerBound <em>ZEnd Multiplicity Lower Bound</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.AssociationInstanceImpl#getZEndMultiplicityUpperBound <em>ZEnd Multiplicity Upper Bound</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.AssociationInstanceImpl#isZEndIsNavigable <em>ZEnd Is Navigable</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.AssociationInstanceImpl#isZEndIsOrdered <em>ZEnd Is Ordered</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.AssociationInstanceImpl#getZEndIsChangeable <em>ZEnd Is Changeable</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.AssociationInstanceImpl#getZEndAggregation <em>ZEnd Aggregation</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.AssociationInstanceImpl#getReferenceName <em>Reference Name</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.AssociationInstanceImpl#getAEndOrderNumber <em>AEnd Order Number</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.impl.AssociationInstanceImpl#getZEndOrderNumber <em>ZEnd Order Number</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AssociationInstanceImpl extends InstanceImpl implements
		AssociationInstance {
	/**
	 * The cached value of the '{@link #getAEnd() <em>AEnd</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getAEnd()
	 * @generated
	 * @ordered
	 */
	protected Instance aEnd;

	/**
	 * The default value of the '{@link #getAEndName() <em>AEnd Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getAEndName()
	 * @generated
	 * @ordered
	 */
	protected static final String AEND_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAEndName() <em>AEnd Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getAEndName()
	 * @generated
	 * @ordered
	 */
	protected String aEndName = AEND_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getAEndMultiplicityLowerBound() <em>AEnd Multiplicity Lower Bound</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getAEndMultiplicityLowerBound()
	 * @generated
	 * @ordered
	 */
	protected static final String AEND_MULTIPLICITY_LOWER_BOUND_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAEndMultiplicityLowerBound() <em>AEnd Multiplicity Lower Bound</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getAEndMultiplicityLowerBound()
	 * @generated
	 * @ordered
	 */
	protected String aEndMultiplicityLowerBound = AEND_MULTIPLICITY_LOWER_BOUND_EDEFAULT;

	/**
	 * The default value of the '{@link #getAEndMultiplicityUpperBound() <em>AEnd Multiplicity Upper Bound</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getAEndMultiplicityUpperBound()
	 * @generated
	 * @ordered
	 */
	protected static final String AEND_MULTIPLICITY_UPPER_BOUND_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAEndMultiplicityUpperBound() <em>AEnd Multiplicity Upper Bound</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getAEndMultiplicityUpperBound()
	 * @generated
	 * @ordered
	 */
	protected String aEndMultiplicityUpperBound = AEND_MULTIPLICITY_UPPER_BOUND_EDEFAULT;

	/**
	 * The default value of the '{@link #isAEndIsNavigable() <em>AEnd Is Navigable</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isAEndIsNavigable()
	 * @generated
	 * @ordered
	 */
	protected static final boolean AEND_IS_NAVIGABLE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isAEndIsNavigable() <em>AEnd Is Navigable</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isAEndIsNavigable()
	 * @generated
	 * @ordered
	 */
	protected boolean aEndIsNavigable = AEND_IS_NAVIGABLE_EDEFAULT;

	/**
	 * The default value of the '{@link #isAEndIsOrdered() <em>AEnd Is Ordered</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isAEndIsOrdered()
	 * @generated
	 * @ordered
	 */
	protected static final boolean AEND_IS_ORDERED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isAEndIsOrdered() <em>AEnd Is Ordered</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isAEndIsOrdered()
	 * @generated
	 * @ordered
	 */
	protected boolean aEndIsOrdered = AEND_IS_ORDERED_EDEFAULT;

	/**
	 * The default value of the '{@link #getAEndIsChangeable() <em>AEnd Is Changeable</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getAEndIsChangeable()
	 * @generated
	 * @ordered
	 */
	protected static final ChangeableEnum AEND_IS_CHANGEABLE_EDEFAULT = ChangeableEnum.NONE_LITERAL;

	/**
	 * The cached value of the '{@link #getAEndIsChangeable() <em>AEnd Is Changeable</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getAEndIsChangeable()
	 * @generated
	 * @ordered
	 */
	protected ChangeableEnum aEndIsChangeable = AEND_IS_CHANGEABLE_EDEFAULT;

	/**
	 * The default value of the '{@link #getAEndAggregation() <em>AEnd Aggregation</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getAEndAggregation()
	 * @generated
	 * @ordered
	 */
	protected static final AggregationEnum AEND_AGGREGATION_EDEFAULT = AggregationEnum.NONE_LITERAL;

	/**
	 * The cached value of the '{@link #getAEndAggregation() <em>AEnd Aggregation</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getAEndAggregation()
	 * @generated
	 * @ordered
	 */
	protected AggregationEnum aEndAggregation = AEND_AGGREGATION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getZEnd() <em>ZEnd</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getZEnd()
	 * @generated
	 * @ordered
	 */
	protected Instance zEnd;

	/**
	 * The default value of the '{@link #getZEndName() <em>ZEnd Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getZEndName()
	 * @generated
	 * @ordered
	 */
	protected static final String ZEND_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getZEndName() <em>ZEnd Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getZEndName()
	 * @generated
	 * @ordered
	 */
	protected String zEndName = ZEND_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getZEndMultiplicityLowerBound() <em>ZEnd Multiplicity Lower Bound</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getZEndMultiplicityLowerBound()
	 * @generated
	 * @ordered
	 */
	protected static final String ZEND_MULTIPLICITY_LOWER_BOUND_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getZEndMultiplicityLowerBound() <em>ZEnd Multiplicity Lower Bound</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getZEndMultiplicityLowerBound()
	 * @generated
	 * @ordered
	 */
	protected String zEndMultiplicityLowerBound = ZEND_MULTIPLICITY_LOWER_BOUND_EDEFAULT;

	/**
	 * The default value of the '{@link #getZEndMultiplicityUpperBound() <em>ZEnd Multiplicity Upper Bound</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getZEndMultiplicityUpperBound()
	 * @generated
	 * @ordered
	 */
	protected static final String ZEND_MULTIPLICITY_UPPER_BOUND_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getZEndMultiplicityUpperBound() <em>ZEnd Multiplicity Upper Bound</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getZEndMultiplicityUpperBound()
	 * @generated
	 * @ordered
	 */
	protected String zEndMultiplicityUpperBound = ZEND_MULTIPLICITY_UPPER_BOUND_EDEFAULT;

	/**
	 * The default value of the '{@link #isZEndIsNavigable() <em>ZEnd Is Navigable</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isZEndIsNavigable()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ZEND_IS_NAVIGABLE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isZEndIsNavigable() <em>ZEnd Is Navigable</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isZEndIsNavigable()
	 * @generated
	 * @ordered
	 */
	protected boolean zEndIsNavigable = ZEND_IS_NAVIGABLE_EDEFAULT;

	/**
	 * The default value of the '{@link #isZEndIsOrdered() <em>ZEnd Is Ordered</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isZEndIsOrdered()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ZEND_IS_ORDERED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isZEndIsOrdered() <em>ZEnd Is Ordered</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isZEndIsOrdered()
	 * @generated
	 * @ordered
	 */
	protected boolean zEndIsOrdered = ZEND_IS_ORDERED_EDEFAULT;

	/**
	 * The default value of the '{@link #getZEndIsChangeable() <em>ZEnd Is Changeable</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getZEndIsChangeable()
	 * @generated
	 * @ordered
	 */
	protected static final ChangeableEnum ZEND_IS_CHANGEABLE_EDEFAULT = ChangeableEnum.NONE_LITERAL;

	/**
	 * The cached value of the '{@link #getZEndIsChangeable() <em>ZEnd Is Changeable</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getZEndIsChangeable()
	 * @generated
	 * @ordered
	 */
	protected ChangeableEnum zEndIsChangeable = ZEND_IS_CHANGEABLE_EDEFAULT;

	/**
	 * The default value of the '{@link #getZEndAggregation() <em>ZEnd Aggregation</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getZEndAggregation()
	 * @generated
	 * @ordered
	 */
	protected static final AggregationEnum ZEND_AGGREGATION_EDEFAULT = AggregationEnum.NONE_LITERAL;

	/**
	 * The cached value of the '{@link #getZEndAggregation() <em>ZEnd Aggregation</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getZEndAggregation()
	 * @generated
	 * @ordered
	 */
	protected AggregationEnum zEndAggregation = ZEND_AGGREGATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getReferenceName() <em>Reference Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getReferenceName()
	 * @generated
	 * @ordered
	 */
	protected static final String REFERENCE_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getReferenceName() <em>Reference Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getReferenceName()
	 * @generated
	 * @ordered
	 */
	protected String referenceName = REFERENCE_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getAEndOrderNumber() <em>AEnd Order Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAEndOrderNumber()
	 * @generated
	 * @ordered
	 */
	protected static final Integer AEND_ORDER_NUMBER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAEndOrderNumber() <em>AEnd Order Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAEndOrderNumber()
	 * @generated
	 * @ordered
	 */
	protected Integer aEndOrderNumber = AEND_ORDER_NUMBER_EDEFAULT;

	/**
	 * The default value of the '{@link #getZEndOrderNumber() <em>ZEnd Order Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getZEndOrderNumber()
	 * @generated
	 * @ordered
	 */
	protected static final Integer ZEND_ORDER_NUMBER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getZEndOrderNumber() <em>ZEnd Order Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getZEndOrderNumber()
	 * @generated
	 * @ordered
	 */
	protected Integer zEndOrderNumber = ZEND_ORDER_NUMBER_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected AssociationInstanceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return InstancediagramPackage.Literals.ASSOCIATION_INSTANCE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Instance getAEnd() {
		if (aEnd != null && aEnd.eIsProxy()) {
			InternalEObject oldAEnd = (InternalEObject)aEnd;
			aEnd = (Instance)eResolveProxy(oldAEnd);
			if (aEnd != oldAEnd) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, InstancediagramPackage.ASSOCIATION_INSTANCE__AEND, oldAEnd, aEnd));
			}
		}
		return aEnd;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Instance basicGetAEnd() {
		return aEnd;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setAEnd(Instance newAEnd) {
		Instance oldAEnd = aEnd;
		if (!InstanceDiagramUtils.isInstanceEMFsetCommand()) {
			aEnd = newAEnd;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.SET,
						InstancediagramPackage.ASSOCIATION_INSTANCE__AEND,
						oldAEnd, aEnd));
			return;
		}
		eNotify(new ENotificationImpl(this, Notification.UNSET,
				InstancediagramPackage.ASSOCIATION_INSTANCE__AEND, oldAEnd,
				aEnd));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getAEndName() {
		return aEndName;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setAEndName(String newAEndName) {
		String oldAEndName = aEndName;
		if (!InstanceDiagramUtils.isInstanceEMFsetCommand()) {
			aEndName = newAEndName;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.SET,
						InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_NAME,
						oldAEndName, aEndName));
			return;
		}
		eNotify(new ENotificationImpl(this, Notification.UNSET,
				InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_NAME,
				oldAEndName, aEndName));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getAEndMultiplicityLowerBound() {
		return aEndMultiplicityLowerBound;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setAEndMultiplicityLowerBound(
			String newAEndMultiplicityLowerBound) {
		String oldAEndMultiplicityLowerBound = aEndMultiplicityLowerBound;
		if (!InstanceDiagramUtils.isInstanceEMFsetCommand()) {
			aEndMultiplicityLowerBound = newAEndMultiplicityLowerBound;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(
						this,
						Notification.SET,
						InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_MULTIPLICITY_LOWER_BOUND,
						oldAEndMultiplicityLowerBound,
						aEndMultiplicityLowerBound));
			return;
		}
		eNotify(new ENotificationImpl(
				this,
				Notification.UNSET,
				InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_MULTIPLICITY_LOWER_BOUND,
				oldAEndMultiplicityLowerBound, aEndMultiplicityLowerBound));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getAEndMultiplicityUpperBound() {
		return aEndMultiplicityUpperBound;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setAEndMultiplicityUpperBound(
			String newAEndMultiplicityUpperBound) {
		String oldAEndMultiplicityUpperBound = aEndMultiplicityUpperBound;
		if (!InstanceDiagramUtils.isInstanceEMFsetCommand()) {
			aEndMultiplicityUpperBound = newAEndMultiplicityUpperBound;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(
						this,
						Notification.SET,
						InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_MULTIPLICITY_UPPER_BOUND,
						oldAEndMultiplicityUpperBound,
						aEndMultiplicityUpperBound));
			return;
		}
		eNotify(new ENotificationImpl(
				this,
				Notification.UNSET,
				InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_MULTIPLICITY_UPPER_BOUND,
				oldAEndMultiplicityUpperBound, aEndMultiplicityUpperBound));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isAEndIsNavigable() {
		return aEndIsNavigable;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setAEndIsNavigable(boolean newAEndIsNavigable) {
		boolean oldAEndIsNavigable = aEndIsNavigable;
		if (!InstanceDiagramUtils.isInstanceEMFsetCommand()) {
			aEndIsNavigable = newAEndIsNavigable;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(
						this,
						Notification.SET,
						InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_IS_NAVIGABLE,
						oldAEndIsNavigable, aEndIsNavigable));
			return;
		}
		eNotify(new ENotificationImpl(this, Notification.UNSET,
				InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_IS_NAVIGABLE,
				oldAEndIsNavigable, aEndIsNavigable));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isAEndIsOrdered() {
		return aEndIsOrdered;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setAEndIsOrdered(boolean newAEndIsOrdered) {
		boolean oldAEndIsOrdered = aEndIsOrdered;
		if (!InstanceDiagramUtils.isInstanceEMFsetCommand()) {
			aEndIsOrdered = newAEndIsOrdered;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(
						this,
						Notification.SET,
						InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_IS_ORDERED,
						oldAEndIsOrdered, aEndIsOrdered));
			return;
		}
		eNotify(new ENotificationImpl(this, Notification.UNSET,
				InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_IS_ORDERED,
				oldAEndIsOrdered, aEndIsOrdered));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeableEnum getAEndIsChangeable() {
		return aEndIsChangeable;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setAEndIsChangeable(ChangeableEnum newAEndIsChangeable) {
		ChangeableEnum oldAEndIsChangeable = aEndIsChangeable;
		if (!InstanceDiagramUtils.isInstanceEMFsetCommand()) {
			aEndIsChangeable = newAEndIsChangeable == null ? AEND_IS_CHANGEABLE_EDEFAULT
					: newAEndIsChangeable;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(
						this,
						Notification.SET,
						InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_IS_CHANGEABLE,
						oldAEndIsChangeable, aEndIsChangeable));
			return;
		}
		eNotify(new ENotificationImpl(
				this,
				Notification.UNSET,
				InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_IS_CHANGEABLE,
				oldAEndIsChangeable, aEndIsChangeable));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public AggregationEnum getAEndAggregation() {
		return aEndAggregation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setAEndAggregation(AggregationEnum newAEndAggregation) {
		AggregationEnum oldAEndAggregation = aEndAggregation;
		if (!InstanceDiagramUtils.isInstanceEMFsetCommand()) {
			aEndAggregation = newAEndAggregation == null ? AEND_AGGREGATION_EDEFAULT
					: newAEndAggregation;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(
						this,
						Notification.SET,
						InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_AGGREGATION,
						oldAEndAggregation, aEndAggregation));
			return;
		}
		eNotify(new ENotificationImpl(this, Notification.UNSET,
				InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_AGGREGATION,
				oldAEndAggregation, aEndAggregation));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Instance getZEnd() {
		if (zEnd != null && zEnd.eIsProxy()) {
			InternalEObject oldZEnd = (InternalEObject)zEnd;
			zEnd = (Instance)eResolveProxy(oldZEnd);
			if (zEnd != oldZEnd) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND, oldZEnd, zEnd));
			}
		}
		return zEnd;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Instance basicGetZEnd() {
		return zEnd;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setZEnd(Instance newZEnd) {
		Instance oldZEnd = zEnd;
		if (!InstanceDiagramUtils.isInstanceEMFsetCommand()) {
			zEnd = newZEnd;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.SET,
						InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND,
						oldZEnd, zEnd));
			return;
		}
		eNotify(new ENotificationImpl(this, Notification.UNSET,
				InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND, oldZEnd,
				zEnd));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getZEndName() {
		return zEndName;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setZEndName(String newZEndName) {
		String oldZEndName = zEndName;
		if (!InstanceDiagramUtils.isInstanceEMFsetCommand()) {
			zEndName = newZEndName;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.SET,
						InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_NAME,
						oldZEndName, zEndName));
			return;
		}
		eNotify(new ENotificationImpl(this, Notification.UNSET,
				InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_NAME,
				oldZEndName, zEndName));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getZEndMultiplicityLowerBound() {
		return zEndMultiplicityLowerBound;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setZEndMultiplicityLowerBound(
			String newZEndMultiplicityLowerBound) {
		String oldZEndMultiplicityLowerBound = zEndMultiplicityLowerBound;
		if (!InstanceDiagramUtils.isInstanceEMFsetCommand()) {
			zEndMultiplicityLowerBound = newZEndMultiplicityLowerBound;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(
						this,
						Notification.SET,
						InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_MULTIPLICITY_LOWER_BOUND,
						oldZEndMultiplicityLowerBound,
						zEndMultiplicityLowerBound));
			return;
		}
		eNotify(new ENotificationImpl(
				this,
				Notification.UNSET,
				InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_MULTIPLICITY_LOWER_BOUND,
				oldZEndMultiplicityLowerBound, zEndMultiplicityLowerBound));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getZEndMultiplicityUpperBound() {
		return zEndMultiplicityUpperBound;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setZEndMultiplicityUpperBound(
			String newZEndMultiplicityUpperBound) {
		String oldZEndMultiplicityUpperBound = zEndMultiplicityUpperBound;
		if (!InstanceDiagramUtils.isInstanceEMFsetCommand()) {
			zEndMultiplicityUpperBound = newZEndMultiplicityUpperBound;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(
						this,
						Notification.SET,
						InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_MULTIPLICITY_UPPER_BOUND,
						oldZEndMultiplicityUpperBound,
						zEndMultiplicityUpperBound));
			return;
		}
		eNotify(new ENotificationImpl(
				this,
				Notification.UNSET,
				InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_MULTIPLICITY_UPPER_BOUND,
				oldZEndMultiplicityUpperBound, zEndMultiplicityUpperBound));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isZEndIsNavigable() {
		return zEndIsNavigable;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setZEndIsNavigable(boolean newZEndIsNavigable) {
		boolean oldZEndIsNavigable = zEndIsNavigable;
		if (!InstanceDiagramUtils.isInstanceEMFsetCommand()) {
			zEndIsNavigable = newZEndIsNavigable;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(
						this,
						Notification.SET,
						InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_IS_NAVIGABLE,
						oldZEndIsNavigable, zEndIsNavigable));
			return;
		}
		eNotify(new ENotificationImpl(this, Notification.UNSET,
				InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_IS_NAVIGABLE,
				oldZEndIsNavigable, zEndIsNavigable));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isZEndIsOrdered() {
		return zEndIsOrdered;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setZEndIsOrdered(boolean newZEndIsOrdered) {
		boolean oldZEndIsOrdered = zEndIsOrdered;
		if (!InstanceDiagramUtils.isInstanceEMFsetCommand()) {
			zEndIsOrdered = newZEndIsOrdered;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(
						this,
						Notification.SET,
						InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_IS_ORDERED,
						oldZEndIsOrdered, zEndIsOrdered));
			return;
		}
		eNotify(new ENotificationImpl(this, Notification.UNSET,
				InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_IS_ORDERED,
				oldZEndIsOrdered, zEndIsOrdered));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeableEnum getZEndIsChangeable() {
		return zEndIsChangeable;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setZEndIsChangeable(ChangeableEnum newZEndIsChangeable) {
		ChangeableEnum oldZEndIsChangeable = zEndIsChangeable;
		if (!InstanceDiagramUtils.isInstanceEMFsetCommand()) {
			zEndIsChangeable = newZEndIsChangeable == null ? ZEND_IS_CHANGEABLE_EDEFAULT
					: newZEndIsChangeable;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(
						this,
						Notification.SET,
						InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_IS_CHANGEABLE,
						oldZEndIsChangeable, zEndIsChangeable));
			return;
		}
		eNotify(new ENotificationImpl(
				this,
				Notification.UNSET,
				InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_IS_CHANGEABLE,
				oldZEndIsChangeable, zEndIsChangeable));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public AggregationEnum getZEndAggregation() {
		return zEndAggregation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setZEndAggregation(AggregationEnum newZEndAggregation) {
		AggregationEnum oldZEndAggregation = zEndAggregation;
		if (!InstanceDiagramUtils.isInstanceEMFsetCommand()) {
			zEndAggregation = newZEndAggregation == null ? ZEND_AGGREGATION_EDEFAULT
					: newZEndAggregation;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(
						this,
						Notification.SET,
						InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_AGGREGATION,
						oldZEndAggregation, zEndAggregation));
			return;
		}
		eNotify(new ENotificationImpl(this, Notification.UNSET,
				InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_AGGREGATION,
				oldZEndAggregation, zEndAggregation));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public String getReferenceName() {
		return referenceName;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setReferenceName(String newReferenceName) {
		String oldReferenceName = referenceName;
		if (!InstanceDiagramUtils.isInstanceEMFsetCommand()) {
			referenceName = newReferenceName;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(
						this,
						Notification.SET,
						InstancediagramPackage.ASSOCIATION_INSTANCE__REFERENCE_NAME,
						oldReferenceName, referenceName));
			return;
		}
		eNotify(new ENotificationImpl(this, Notification.UNSET,
				InstancediagramPackage.ASSOCIATION_INSTANCE__REFERENCE_NAME,
				oldReferenceName, referenceName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Integer getAEndOrderNumber() {
		return aEndOrderNumber;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAEndOrderNumber(Integer newAEndOrderNumber) {
		Integer oldAEndOrderNumber = aEndOrderNumber;
		aEndOrderNumber = newAEndOrderNumber;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_ORDER_NUMBER, oldAEndOrderNumber, aEndOrderNumber));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Integer getZEndOrderNumber() {
		return zEndOrderNumber;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setZEndOrderNumber(Integer newZEndOrderNumber) {
		Integer oldZEndOrderNumber = zEndOrderNumber;
		zEndOrderNumber = newZEndOrderNumber;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_ORDER_NUMBER, oldZEndOrderNumber, zEndOrderNumber));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND:
				if (resolve) return getAEnd();
				return basicGetAEnd();
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_NAME:
				return getAEndName();
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_MULTIPLICITY_LOWER_BOUND:
				return getAEndMultiplicityLowerBound();
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_MULTIPLICITY_UPPER_BOUND:
				return getAEndMultiplicityUpperBound();
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_IS_NAVIGABLE:
				return isAEndIsNavigable() ? Boolean.TRUE : Boolean.FALSE;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_IS_ORDERED:
				return isAEndIsOrdered() ? Boolean.TRUE : Boolean.FALSE;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_IS_CHANGEABLE:
				return getAEndIsChangeable();
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_AGGREGATION:
				return getAEndAggregation();
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND:
				if (resolve) return getZEnd();
				return basicGetZEnd();
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_NAME:
				return getZEndName();
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_MULTIPLICITY_LOWER_BOUND:
				return getZEndMultiplicityLowerBound();
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_MULTIPLICITY_UPPER_BOUND:
				return getZEndMultiplicityUpperBound();
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_IS_NAVIGABLE:
				return isZEndIsNavigable() ? Boolean.TRUE : Boolean.FALSE;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_IS_ORDERED:
				return isZEndIsOrdered() ? Boolean.TRUE : Boolean.FALSE;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_IS_CHANGEABLE:
				return getZEndIsChangeable();
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_AGGREGATION:
				return getZEndAggregation();
			case InstancediagramPackage.ASSOCIATION_INSTANCE__REFERENCE_NAME:
				return getReferenceName();
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_ORDER_NUMBER:
				return getAEndOrderNumber();
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_ORDER_NUMBER:
				return getZEndOrderNumber();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND:
				setAEnd((Instance)newValue);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_NAME:
				setAEndName((String)newValue);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_MULTIPLICITY_LOWER_BOUND:
				setAEndMultiplicityLowerBound((String)newValue);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_MULTIPLICITY_UPPER_BOUND:
				setAEndMultiplicityUpperBound((String)newValue);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_IS_NAVIGABLE:
				setAEndIsNavigable(((Boolean)newValue).booleanValue());
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_IS_ORDERED:
				setAEndIsOrdered(((Boolean)newValue).booleanValue());
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_IS_CHANGEABLE:
				setAEndIsChangeable((ChangeableEnum)newValue);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_AGGREGATION:
				setAEndAggregation((AggregationEnum)newValue);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND:
				setZEnd((Instance)newValue);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_NAME:
				setZEndName((String)newValue);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_MULTIPLICITY_LOWER_BOUND:
				setZEndMultiplicityLowerBound((String)newValue);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_MULTIPLICITY_UPPER_BOUND:
				setZEndMultiplicityUpperBound((String)newValue);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_IS_NAVIGABLE:
				setZEndIsNavigable(((Boolean)newValue).booleanValue());
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_IS_ORDERED:
				setZEndIsOrdered(((Boolean)newValue).booleanValue());
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_IS_CHANGEABLE:
				setZEndIsChangeable((ChangeableEnum)newValue);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_AGGREGATION:
				setZEndAggregation((AggregationEnum)newValue);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__REFERENCE_NAME:
				setReferenceName((String)newValue);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_ORDER_NUMBER:
				setAEndOrderNumber((Integer)newValue);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_ORDER_NUMBER:
				setZEndOrderNumber((Integer)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND:
				setAEnd((Instance)null);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_NAME:
				setAEndName(AEND_NAME_EDEFAULT);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_MULTIPLICITY_LOWER_BOUND:
				setAEndMultiplicityLowerBound(AEND_MULTIPLICITY_LOWER_BOUND_EDEFAULT);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_MULTIPLICITY_UPPER_BOUND:
				setAEndMultiplicityUpperBound(AEND_MULTIPLICITY_UPPER_BOUND_EDEFAULT);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_IS_NAVIGABLE:
				setAEndIsNavigable(AEND_IS_NAVIGABLE_EDEFAULT);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_IS_ORDERED:
				setAEndIsOrdered(AEND_IS_ORDERED_EDEFAULT);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_IS_CHANGEABLE:
				setAEndIsChangeable(AEND_IS_CHANGEABLE_EDEFAULT);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_AGGREGATION:
				setAEndAggregation(AEND_AGGREGATION_EDEFAULT);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND:
				setZEnd((Instance)null);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_NAME:
				setZEndName(ZEND_NAME_EDEFAULT);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_MULTIPLICITY_LOWER_BOUND:
				setZEndMultiplicityLowerBound(ZEND_MULTIPLICITY_LOWER_BOUND_EDEFAULT);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_MULTIPLICITY_UPPER_BOUND:
				setZEndMultiplicityUpperBound(ZEND_MULTIPLICITY_UPPER_BOUND_EDEFAULT);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_IS_NAVIGABLE:
				setZEndIsNavigable(ZEND_IS_NAVIGABLE_EDEFAULT);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_IS_ORDERED:
				setZEndIsOrdered(ZEND_IS_ORDERED_EDEFAULT);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_IS_CHANGEABLE:
				setZEndIsChangeable(ZEND_IS_CHANGEABLE_EDEFAULT);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_AGGREGATION:
				setZEndAggregation(ZEND_AGGREGATION_EDEFAULT);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__REFERENCE_NAME:
				setReferenceName(REFERENCE_NAME_EDEFAULT);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_ORDER_NUMBER:
				setAEndOrderNumber(AEND_ORDER_NUMBER_EDEFAULT);
				return;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_ORDER_NUMBER:
				setZEndOrderNumber(ZEND_ORDER_NUMBER_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND:
				return aEnd != null;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_NAME:
				return AEND_NAME_EDEFAULT == null ? aEndName != null : !AEND_NAME_EDEFAULT.equals(aEndName);
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_MULTIPLICITY_LOWER_BOUND:
				return AEND_MULTIPLICITY_LOWER_BOUND_EDEFAULT == null ? aEndMultiplicityLowerBound != null : !AEND_MULTIPLICITY_LOWER_BOUND_EDEFAULT.equals(aEndMultiplicityLowerBound);
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_MULTIPLICITY_UPPER_BOUND:
				return AEND_MULTIPLICITY_UPPER_BOUND_EDEFAULT == null ? aEndMultiplicityUpperBound != null : !AEND_MULTIPLICITY_UPPER_BOUND_EDEFAULT.equals(aEndMultiplicityUpperBound);
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_IS_NAVIGABLE:
				return aEndIsNavigable != AEND_IS_NAVIGABLE_EDEFAULT;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_IS_ORDERED:
				return aEndIsOrdered != AEND_IS_ORDERED_EDEFAULT;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_IS_CHANGEABLE:
				return aEndIsChangeable != AEND_IS_CHANGEABLE_EDEFAULT;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_AGGREGATION:
				return aEndAggregation != AEND_AGGREGATION_EDEFAULT;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND:
				return zEnd != null;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_NAME:
				return ZEND_NAME_EDEFAULT == null ? zEndName != null : !ZEND_NAME_EDEFAULT.equals(zEndName);
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_MULTIPLICITY_LOWER_BOUND:
				return ZEND_MULTIPLICITY_LOWER_BOUND_EDEFAULT == null ? zEndMultiplicityLowerBound != null : !ZEND_MULTIPLICITY_LOWER_BOUND_EDEFAULT.equals(zEndMultiplicityLowerBound);
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_MULTIPLICITY_UPPER_BOUND:
				return ZEND_MULTIPLICITY_UPPER_BOUND_EDEFAULT == null ? zEndMultiplicityUpperBound != null : !ZEND_MULTIPLICITY_UPPER_BOUND_EDEFAULT.equals(zEndMultiplicityUpperBound);
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_IS_NAVIGABLE:
				return zEndIsNavigable != ZEND_IS_NAVIGABLE_EDEFAULT;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_IS_ORDERED:
				return zEndIsOrdered != ZEND_IS_ORDERED_EDEFAULT;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_IS_CHANGEABLE:
				return zEndIsChangeable != ZEND_IS_CHANGEABLE_EDEFAULT;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_AGGREGATION:
				return zEndAggregation != ZEND_AGGREGATION_EDEFAULT;
			case InstancediagramPackage.ASSOCIATION_INSTANCE__REFERENCE_NAME:
				return REFERENCE_NAME_EDEFAULT == null ? referenceName != null : !REFERENCE_NAME_EDEFAULT.equals(referenceName);
			case InstancediagramPackage.ASSOCIATION_INSTANCE__AEND_ORDER_NUMBER:
				return AEND_ORDER_NUMBER_EDEFAULT == null ? aEndOrderNumber != null : !AEND_ORDER_NUMBER_EDEFAULT.equals(aEndOrderNumber);
			case InstancediagramPackage.ASSOCIATION_INSTANCE__ZEND_ORDER_NUMBER:
				return ZEND_ORDER_NUMBER_EDEFAULT == null ? zEndOrderNumber != null : !ZEND_ORDER_NUMBER_EDEFAULT.equals(zEndOrderNumber);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (aEndName: ");
		result.append(aEndName);
		result.append(", aEndMultiplicityLowerBound: ");
		result.append(aEndMultiplicityLowerBound);
		result.append(", aEndMultiplicityUpperBound: ");
		result.append(aEndMultiplicityUpperBound);
		result.append(", aEndIsNavigable: ");
		result.append(aEndIsNavigable);
		result.append(", aEndIsOrdered: ");
		result.append(aEndIsOrdered);
		result.append(", aEndIsChangeable: ");
		result.append(aEndIsChangeable);
		result.append(", aEndAggregation: ");
		result.append(aEndAggregation);
		result.append(", zEndName: ");
		result.append(zEndName);
		result.append(", zEndMultiplicityLowerBound: ");
		result.append(zEndMultiplicityLowerBound);
		result.append(", zEndMultiplicityUpperBound: ");
		result.append(zEndMultiplicityUpperBound);
		result.append(", zEndIsNavigable: ");
		result.append(zEndIsNavigable);
		result.append(", zEndIsOrdered: ");
		result.append(zEndIsOrdered);
		result.append(", zEndIsChangeable: ");
		result.append(zEndIsChangeable);
		result.append(", zEndAggregation: ");
		result.append(zEndAggregation);
		result.append(", referenceName: ");
		result.append(referenceName);
		result.append(", aEndOrderNumber: ");
		result.append(aEndOrderNumber);
		result.append(", zEndOrderNumber: ");
		result.append(zEndOrderNumber);
		result.append(')');
		return result.toString();
	}

	/**
	 * used internally to determine which reference (if any) in a class instance
	 * that this association instance represents
	 */
	public boolean isReferenceType() {
		if (getReferenceName() == null)
			return false;
		return getReferenceName().length() > 0;
	}

} // AssociationInstanceImpl
