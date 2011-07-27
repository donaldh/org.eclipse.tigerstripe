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
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.ui.ModelElementAnnotationsHelper;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AggregationEnum;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ChangeableEnum;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Visibility;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Association</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationImpl#getAEnd
 * <em>AEnd</em>}</li>
 * <li>
 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationImpl#getAEndName
 * <em>AEnd Name</em>}</li>
 * <li>
 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationImpl#getAEndMultiplicity
 * <em>AEnd Multiplicity</em>}</li>
 * <li>
 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationImpl#isAEndIsNavigable
 * <em>AEnd Is Navigable</em>}</li>
 * <li>
 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationImpl#isAEndIsOrdered
 * <em>AEnd Is Ordered</em>}</li>
 * <li>
 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationImpl#isAEndIsUnique
 * <em>AEnd Is Unique</em>}</li>
 * <li>
 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationImpl#getAEndIsChangeable
 * <em>AEnd Is Changeable</em>}</li>
 * <li>
 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationImpl#getAEndAggregation
 * <em>AEnd Aggregation</em>}</li>
 * <li>
 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationImpl#getZEnd
 * <em>ZEnd</em>}</li>
 * <li>
 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationImpl#getZEndName
 * <em>ZEnd Name</em>}</li>
 * <li>
 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationImpl#getZEndMultiplicity
 * <em>ZEnd Multiplicity</em>}</li>
 * <li>
 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationImpl#isZEndIsNavigable
 * <em>ZEnd Is Navigable</em>}</li>
 * <li>
 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationImpl#isZEndIsOrdered
 * <em>ZEnd Is Ordered</em>}</li>
 * <li>
 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationImpl#isZEndIsUnique
 * <em>ZEnd Is Unique</em>}</li>
 * <li>
 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationImpl#getZEndIsChangeable
 * <em>ZEnd Is Changeable</em>}</li>
 * <li>
 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationImpl#getZEndAggregation
 * <em>ZEnd Aggregation</em>}</li>
 * <li>
 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationImpl#getAEndVisibility
 * <em>AEnd Visibility</em>}</li>
 * <li>
 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationImpl#getZEndVisibility
 * <em>ZEnd Visibility</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class AssociationImpl extends QualifiedNamedElementImpl implements
		Association {
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
	 * The default value of the '{@link #getAEndName() <em>AEnd Name</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAEndName()
	 * @generated
	 * @ordered
	 */
	protected static final String AEND_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAEndName() <em>AEnd Name</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAEndName()
	 * @generated
	 * @ordered
	 */
	protected String aEndName = AEND_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getAEndMultiplicity()
	 * <em>AEnd Multiplicity</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getAEndMultiplicity()
	 * @generated
	 * @ordered
	 */
	protected static final AssocMultiplicity AEND_MULTIPLICITY_EDEFAULT = AssocMultiplicity.ONE_LITERAL;

	/**
	 * The cached value of the '{@link #getAEndMultiplicity()
	 * <em>AEnd Multiplicity</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getAEndMultiplicity()
	 * @generated
	 * @ordered
	 */
	protected AssocMultiplicity aEndMultiplicity = AEND_MULTIPLICITY_EDEFAULT;

	/**
	 * The default value of the '{@link #isAEndIsNavigable()
	 * <em>AEnd Is Navigable</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isAEndIsNavigable()
	 * @generated
	 * @ordered
	 */
	protected static final boolean AEND_IS_NAVIGABLE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isAEndIsNavigable()
	 * <em>AEnd Is Navigable</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isAEndIsNavigable()
	 * @generated
	 * @ordered
	 */
	protected boolean aEndIsNavigable = AEND_IS_NAVIGABLE_EDEFAULT;

	/**
	 * The default value of the '{@link #isAEndIsOrdered()
	 * <em>AEnd Is Ordered</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isAEndIsOrdered()
	 * @generated
	 * @ordered
	 */
	protected static final boolean AEND_IS_ORDERED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isAEndIsOrdered()
	 * <em>AEnd Is Ordered</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isAEndIsOrdered()
	 * @generated
	 * @ordered
	 */
	protected boolean aEndIsOrdered = AEND_IS_ORDERED_EDEFAULT;

	/**
	 * The default value of the '{@link #isAEndIsUnique()
	 * <em>AEnd Is Unique</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isAEndIsUnique()
	 * @generated
	 * @ordered
	 */
	protected static final boolean AEND_IS_UNIQUE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isAEndIsUnique()
	 * <em>AEnd Is Unique</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isAEndIsUnique()
	 * @generated
	 * @ordered
	 */
	protected boolean aEndIsUnique = AEND_IS_UNIQUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getAEndIsChangeable()
	 * <em>AEnd Is Changeable</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getAEndIsChangeable()
	 * @generated
	 * @ordered
	 */
	protected static final ChangeableEnum AEND_IS_CHANGEABLE_EDEFAULT = ChangeableEnum.NONE_LITERAL;

	/**
	 * The cached value of the '{@link #getAEndIsChangeable()
	 * <em>AEnd Is Changeable</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getAEndIsChangeable()
	 * @generated
	 * @ordered
	 */
	protected ChangeableEnum aEndIsChangeable = AEND_IS_CHANGEABLE_EDEFAULT;

	/**
	 * The default value of the '{@link #getAEndAggregation()
	 * <em>AEnd Aggregation</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getAEndAggregation()
	 * @generated
	 * @ordered
	 */
	protected static final AggregationEnum AEND_AGGREGATION_EDEFAULT = AggregationEnum.NONE_LITERAL;

	/**
	 * The cached value of the '{@link #getAEndAggregation()
	 * <em>AEnd Aggregation</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getAEndAggregation()
	 * @generated
	 * @ordered
	 */
	protected AggregationEnum aEndAggregation = AEND_AGGREGATION_EDEFAULT;

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
	 * The default value of the '{@link #getZEndName() <em>ZEnd Name</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getZEndName()
	 * @generated
	 * @ordered
	 */
	protected static final String ZEND_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getZEndName() <em>ZEnd Name</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getZEndName()
	 * @generated
	 * @ordered
	 */
	protected String zEndName = ZEND_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getZEndMultiplicity()
	 * <em>ZEnd Multiplicity</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getZEndMultiplicity()
	 * @generated
	 * @ordered
	 */
	protected static final AssocMultiplicity ZEND_MULTIPLICITY_EDEFAULT = AssocMultiplicity.ONE_LITERAL;

	/**
	 * The cached value of the '{@link #getZEndMultiplicity()
	 * <em>ZEnd Multiplicity</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getZEndMultiplicity()
	 * @generated
	 * @ordered
	 */
	protected AssocMultiplicity zEndMultiplicity = ZEND_MULTIPLICITY_EDEFAULT;

	/**
	 * The default value of the '{@link #isZEndIsNavigable()
	 * <em>ZEnd Is Navigable</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isZEndIsNavigable()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ZEND_IS_NAVIGABLE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isZEndIsNavigable()
	 * <em>ZEnd Is Navigable</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isZEndIsNavigable()
	 * @generated
	 * @ordered
	 */
	protected boolean zEndIsNavigable = ZEND_IS_NAVIGABLE_EDEFAULT;

	/**
	 * The default value of the '{@link #isZEndIsOrdered()
	 * <em>ZEnd Is Ordered</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isZEndIsOrdered()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ZEND_IS_ORDERED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isZEndIsOrdered()
	 * <em>ZEnd Is Ordered</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isZEndIsOrdered()
	 * @generated
	 * @ordered
	 */
	protected boolean zEndIsOrdered = ZEND_IS_ORDERED_EDEFAULT;

	/**
	 * The default value of the '{@link #isZEndIsUnique()
	 * <em>ZEnd Is Unique</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isZEndIsUnique()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ZEND_IS_UNIQUE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isZEndIsUnique()
	 * <em>ZEnd Is Unique</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isZEndIsUnique()
	 * @generated
	 * @ordered
	 */
	protected boolean zEndIsUnique = ZEND_IS_UNIQUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getZEndIsChangeable()
	 * <em>ZEnd Is Changeable</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getZEndIsChangeable()
	 * @generated
	 * @ordered
	 */
	protected static final ChangeableEnum ZEND_IS_CHANGEABLE_EDEFAULT = ChangeableEnum.NONE_LITERAL;

	/**
	 * The cached value of the '{@link #getZEndIsChangeable()
	 * <em>ZEnd Is Changeable</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getZEndIsChangeable()
	 * @generated
	 * @ordered
	 */
	protected ChangeableEnum zEndIsChangeable = ZEND_IS_CHANGEABLE_EDEFAULT;

	/**
	 * The default value of the '{@link #getZEndAggregation()
	 * <em>ZEnd Aggregation</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getZEndAggregation()
	 * @generated
	 * @ordered
	 */
	protected static final AggregationEnum ZEND_AGGREGATION_EDEFAULT = AggregationEnum.NONE_LITERAL;

	/**
	 * The cached value of the '{@link #getZEndAggregation()
	 * <em>ZEnd Aggregation</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getZEndAggregation()
	 * @generated
	 * @ordered
	 */
	protected AggregationEnum zEndAggregation = ZEND_AGGREGATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getAEndVisibility()
	 * <em>AEnd Visibility</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getAEndVisibility()
	 * @generated
	 * @ordered
	 */
	protected static final Visibility AEND_VISIBILITY_EDEFAULT = Visibility.PUBLIC_LITERAL;

	/**
	 * The cached value of the '{@link #getAEndVisibility()
	 * <em>AEnd Visibility</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getAEndVisibility()
	 * @generated
	 * @ordered
	 */
	protected Visibility aEndVisibility = AEND_VISIBILITY_EDEFAULT;

	/**
	 * The default value of the '{@link #getZEndVisibility()
	 * <em>ZEnd Visibility</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getZEndVisibility()
	 * @generated
	 * @ordered
	 */
	protected static final Visibility ZEND_VISIBILITY_EDEFAULT = Visibility.PUBLIC_LITERAL;

	/**
	 * The cached value of the '{@link #getZEndVisibility()
	 * <em>ZEnd Visibility</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getZEndVisibility()
	 * @generated
	 * @ordered
	 */
	protected Visibility zEndVisibility = ZEND_VISIBILITY_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected AssociationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return VisualeditorPackage.Literals.ASSOCIATION;
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
							VisualeditorPackage.ASSOCIATION__AEND, oldAEnd,
							aEnd));
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
					VisualeditorPackage.ASSOCIATION__AEND, oldAEnd, aEnd));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
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
		if (!(newAEndName == null || "".equals(newAEndName))) {
			aEndName = newAEndName;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.SET,
						VisualeditorPackage.ASSOCIATION__AEND_NAME,
						oldAEndName, aEndName));
			return;
		}
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET,
					VisualeditorPackage.ASSOCIATION__AEND_NAME, oldAEndName,
					aEndName));
	}

	private String aEndStereotypeNames = "";

	/**
	 * 
	 * @generated NOT
	 */
	public String getAEndStereotypeNames() {
		return aEndStereotypeNames;
	}

	/**
	 * 
	 * @generated NOT
	 */
	public void setAEndStereotypeNames(String stereotypeNames) {
		aEndStereotypeNames = stereotypeNames;
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
					VisualeditorPackage.ASSOCIATION__AEND_MULTIPLICITY,
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
					VisualeditorPackage.ASSOCIATION__AEND_IS_NAVIGABLE,
					oldAEndIsNavigable, aEndIsNavigable));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isAEndIsOrdered() {
		return aEndIsOrdered;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setAEndIsOrdered(boolean newAEndIsOrdered) {
		boolean oldAEndIsOrdered = aEndIsOrdered;
		aEndIsOrdered = newAEndIsOrdered;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.ASSOCIATION__AEND_IS_ORDERED,
					oldAEndIsOrdered, aEndIsOrdered));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isAEndIsUnique() {
		return aEndIsUnique;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setAEndIsUnique(boolean newAEndIsUnique) {
		boolean oldAEndIsUnique = aEndIsUnique;
		aEndIsUnique = newAEndIsUnique;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.ASSOCIATION__AEND_IS_UNIQUE,
					oldAEndIsUnique, aEndIsUnique));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ChangeableEnum getAEndIsChangeable() {
		return aEndIsChangeable;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setAEndIsChangeable(ChangeableEnum newAEndIsChangeable) {
		ChangeableEnum oldAEndIsChangeable = aEndIsChangeable;
		aEndIsChangeable = newAEndIsChangeable == null ? AEND_IS_CHANGEABLE_EDEFAULT
				: newAEndIsChangeable;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.ASSOCIATION__AEND_IS_CHANGEABLE,
					oldAEndIsChangeable, aEndIsChangeable));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AggregationEnum getAEndAggregation() {
		return aEndAggregation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setAEndAggregation(AggregationEnum newAEndAggregation) {
		AggregationEnum oldAEndAggregation = aEndAggregation;
		aEndAggregation = newAEndAggregation == null ? AEND_AGGREGATION_EDEFAULT
				: newAEndAggregation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.ASSOCIATION__AEND_AGGREGATION,
					oldAEndAggregation, aEndAggregation));
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
							VisualeditorPackage.ASSOCIATION__ZEND, oldZEnd,
							zEnd));
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
					VisualeditorPackage.ASSOCIATION__ZEND, oldZEnd, zEnd));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
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
		if (!(newZEndName == null || "".equals(newZEndName))) {
			zEndName = newZEndName;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.SET,
						VisualeditorPackage.ASSOCIATION__ZEND_NAME,
						oldZEndName, zEndName));
			return;
		}
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET,
					VisualeditorPackage.ASSOCIATION__ZEND_NAME, oldZEndName,
					zEndName));
	}

	private String zEndStereotypeNames = "";

	/**
	 * 
	 * @generated NOT
	 */
	public String getZEndStereotypeNames() {
		return zEndStereotypeNames;
	}

	/**
	 * 
	 * @generated NOT
	 */
	public void setZEndStereotypeNames(String stereotypeNames) {
		zEndStereotypeNames = stereotypeNames;
	}

	/**
	 * Populates the stereotype strings o
	 */
	public void populateEndStereotypeNames() {
		try {
			IAssociationArtifact assoc = (IAssociationArtifact) getCorrespondingIArtifact();
			if (assoc != null) {
				setAEndStereotypeNames(ModelElementAnnotationsHelper
						.getAnnotationsAsString(assoc.getAEnd()));
				setZEndStereotypeNames(ModelElementAnnotationsHelper
						.getAnnotationsAsString(assoc.getZEnd()));
			}
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}
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
					VisualeditorPackage.ASSOCIATION__ZEND_MULTIPLICITY,
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
					VisualeditorPackage.ASSOCIATION__ZEND_IS_NAVIGABLE,
					oldZEndIsNavigable, zEndIsNavigable));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isZEndIsOrdered() {
		return zEndIsOrdered;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setZEndIsOrdered(boolean newZEndIsOrdered) {
		boolean oldZEndIsOrdered = zEndIsOrdered;
		zEndIsOrdered = newZEndIsOrdered;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.ASSOCIATION__ZEND_IS_ORDERED,
					oldZEndIsOrdered, zEndIsOrdered));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isZEndIsUnique() {
		return zEndIsUnique;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setZEndIsUnique(boolean newZEndIsUnique) {
		boolean oldZEndIsUnique = zEndIsUnique;
		zEndIsUnique = newZEndIsUnique;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.ASSOCIATION__ZEND_IS_UNIQUE,
					oldZEndIsUnique, zEndIsUnique));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ChangeableEnum getZEndIsChangeable() {
		return zEndIsChangeable;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setZEndIsChangeable(ChangeableEnum newZEndIsChangeable) {
		ChangeableEnum oldZEndIsChangeable = zEndIsChangeable;
		zEndIsChangeable = newZEndIsChangeable == null ? ZEND_IS_CHANGEABLE_EDEFAULT
				: newZEndIsChangeable;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.ASSOCIATION__ZEND_IS_CHANGEABLE,
					oldZEndIsChangeable, zEndIsChangeable));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AggregationEnum getZEndAggregation() {
		return zEndAggregation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setZEndAggregation(AggregationEnum newZEndAggregation) {
		AggregationEnum oldZEndAggregation = zEndAggregation;
		zEndAggregation = newZEndAggregation == null ? ZEND_AGGREGATION_EDEFAULT
				: newZEndAggregation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.ASSOCIATION__ZEND_AGGREGATION,
					oldZEndAggregation, zEndAggregation));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Visibility getAEndVisibility() {
		return aEndVisibility;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setAEndVisibility(Visibility newAEndVisibility) {
		Visibility oldAEndVisibility = aEndVisibility;
		aEndVisibility = newAEndVisibility == null ? AEND_VISIBILITY_EDEFAULT
				: newAEndVisibility;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.ASSOCIATION__AEND_VISIBILITY,
					oldAEndVisibility, aEndVisibility));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Visibility getZEndVisibility() {
		return zEndVisibility;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setZEndVisibility(Visibility newZEndVisibility) {
		Visibility oldZEndVisibility = zEndVisibility;
		zEndVisibility = newZEndVisibility == null ? ZEND_VISIBILITY_EDEFAULT
				: newZEndVisibility;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.ASSOCIATION__ZEND_VISIBILITY,
					oldZEndVisibility, zEndVisibility));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case VisualeditorPackage.ASSOCIATION__AEND:
			if (resolve)
				return getAEnd();
			return basicGetAEnd();
		case VisualeditorPackage.ASSOCIATION__AEND_NAME:
			return getAEndName();
		case VisualeditorPackage.ASSOCIATION__AEND_MULTIPLICITY:
			return getAEndMultiplicity();
		case VisualeditorPackage.ASSOCIATION__AEND_IS_NAVIGABLE:
			return isAEndIsNavigable() ? Boolean.TRUE : Boolean.FALSE;
		case VisualeditorPackage.ASSOCIATION__AEND_IS_ORDERED:
			return isAEndIsOrdered() ? Boolean.TRUE : Boolean.FALSE;
		case VisualeditorPackage.ASSOCIATION__AEND_IS_UNIQUE:
			return isAEndIsUnique() ? Boolean.TRUE : Boolean.FALSE;
		case VisualeditorPackage.ASSOCIATION__AEND_IS_CHANGEABLE:
			return getAEndIsChangeable();
		case VisualeditorPackage.ASSOCIATION__AEND_AGGREGATION:
			return getAEndAggregation();
		case VisualeditorPackage.ASSOCIATION__ZEND:
			if (resolve)
				return getZEnd();
			return basicGetZEnd();
		case VisualeditorPackage.ASSOCIATION__ZEND_NAME:
			return getZEndName();
		case VisualeditorPackage.ASSOCIATION__ZEND_MULTIPLICITY:
			return getZEndMultiplicity();
		case VisualeditorPackage.ASSOCIATION__ZEND_IS_NAVIGABLE:
			return isZEndIsNavigable() ? Boolean.TRUE : Boolean.FALSE;
		case VisualeditorPackage.ASSOCIATION__ZEND_IS_ORDERED:
			return isZEndIsOrdered() ? Boolean.TRUE : Boolean.FALSE;
		case VisualeditorPackage.ASSOCIATION__ZEND_IS_UNIQUE:
			return isZEndIsUnique() ? Boolean.TRUE : Boolean.FALSE;
		case VisualeditorPackage.ASSOCIATION__ZEND_IS_CHANGEABLE:
			return getZEndIsChangeable();
		case VisualeditorPackage.ASSOCIATION__ZEND_AGGREGATION:
			return getZEndAggregation();
		case VisualeditorPackage.ASSOCIATION__AEND_VISIBILITY:
			return getAEndVisibility();
		case VisualeditorPackage.ASSOCIATION__ZEND_VISIBILITY:
			return getZEndVisibility();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		if (!isReadonly) {
			switch (featureID) {
			case VisualeditorPackage.ASSOCIATION__AEND:
				setAEnd((AbstractArtifact) newValue);
				return;
			case VisualeditorPackage.ASSOCIATION__AEND_NAME:
				setAEndName((String) newValue);
				return;
			case VisualeditorPackage.ASSOCIATION__AEND_MULTIPLICITY:
				setAEndMultiplicity((AssocMultiplicity) newValue);
				return;
			case VisualeditorPackage.ASSOCIATION__AEND_IS_NAVIGABLE:
				setAEndIsNavigable(((Boolean) newValue).booleanValue());
				return;
			case VisualeditorPackage.ASSOCIATION__AEND_IS_ORDERED:
				setAEndIsOrdered(((Boolean) newValue).booleanValue());
				return;
			case VisualeditorPackage.ASSOCIATION__AEND_IS_UNIQUE:
				setAEndIsUnique(((Boolean) newValue).booleanValue());
				return;
			case VisualeditorPackage.ASSOCIATION__AEND_IS_CHANGEABLE:
				setAEndIsChangeable((ChangeableEnum) newValue);
				return;
			case VisualeditorPackage.ASSOCIATION__AEND_AGGREGATION:
				setAEndAggregation((AggregationEnum) newValue);
				return;
			case VisualeditorPackage.ASSOCIATION__AEND_VISIBILITY:
				setAEndVisibility((Visibility) newValue);
				return;
			case VisualeditorPackage.ASSOCIATION__ZEND:
				setZEnd((AbstractArtifact) newValue);
				return;
			case VisualeditorPackage.ASSOCIATION__ZEND_NAME:
				setZEndName((String) newValue);
				return;
			case VisualeditorPackage.ASSOCIATION__ZEND_MULTIPLICITY:
				setZEndMultiplicity((AssocMultiplicity) newValue);
				return;
			case VisualeditorPackage.ASSOCIATION__ZEND_IS_NAVIGABLE:
				setZEndIsNavigable(((Boolean) newValue).booleanValue());
				return;
			case VisualeditorPackage.ASSOCIATION__ZEND_IS_ORDERED:
				setZEndIsOrdered(((Boolean) newValue).booleanValue());
				return;
			case VisualeditorPackage.ASSOCIATION__ZEND_IS_UNIQUE:
				setZEndIsUnique(((Boolean) newValue).booleanValue());
				return;
			case VisualeditorPackage.ASSOCIATION__ZEND_IS_CHANGEABLE:
				setZEndIsChangeable((ChangeableEnum) newValue);
				return;
			case VisualeditorPackage.ASSOCIATION__ZEND_AGGREGATION:
				setZEndAggregation((AggregationEnum) newValue);
				return;
			case VisualeditorPackage.ASSOCIATION__ZEND_VISIBILITY:
				setZEndVisibility((Visibility) newValue);
				return;
			}
			super.eSet(featureID, newValue);
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public void eUnset(int featureID) {
		if (!isReadonly) {
			switch (featureID) {
			case VisualeditorPackage.ASSOCIATION__AEND:
				setAEnd((AbstractArtifact) null);
				return;
			case VisualeditorPackage.ASSOCIATION__AEND_NAME:
				setAEndName(AEND_NAME_EDEFAULT);
				return;
			case VisualeditorPackage.ASSOCIATION__AEND_MULTIPLICITY:
				setAEndMultiplicity(AEND_MULTIPLICITY_EDEFAULT);
				return;
			case VisualeditorPackage.ASSOCIATION__AEND_IS_NAVIGABLE:
				setAEndIsNavigable(AEND_IS_NAVIGABLE_EDEFAULT);
				return;
			case VisualeditorPackage.ASSOCIATION__AEND_IS_ORDERED:
				setAEndIsOrdered(AEND_IS_ORDERED_EDEFAULT);
				return;
			case VisualeditorPackage.ASSOCIATION__AEND_IS_UNIQUE:
				setAEndIsUnique(AEND_IS_UNIQUE_EDEFAULT);
				return;
			case VisualeditorPackage.ASSOCIATION__AEND_IS_CHANGEABLE:
				setAEndIsChangeable(AEND_IS_CHANGEABLE_EDEFAULT);
				return;
			case VisualeditorPackage.ASSOCIATION__AEND_AGGREGATION:
				setAEndAggregation(AEND_AGGREGATION_EDEFAULT);
				return;
			case VisualeditorPackage.ASSOCIATION__AEND_VISIBILITY:
				setAEndVisibility(AEND_VISIBILITY_EDEFAULT);
				return;
			case VisualeditorPackage.ASSOCIATION__ZEND:
				setZEnd((AbstractArtifact) null);
				return;
			case VisualeditorPackage.ASSOCIATION__ZEND_NAME:
				setZEndName(ZEND_NAME_EDEFAULT);
				return;
			case VisualeditorPackage.ASSOCIATION__ZEND_MULTIPLICITY:
				setZEndMultiplicity(ZEND_MULTIPLICITY_EDEFAULT);
				return;
			case VisualeditorPackage.ASSOCIATION__ZEND_IS_NAVIGABLE:
				setZEndIsNavigable(ZEND_IS_NAVIGABLE_EDEFAULT);
				return;
			case VisualeditorPackage.ASSOCIATION__ZEND_IS_ORDERED:
				setZEndIsOrdered(ZEND_IS_ORDERED_EDEFAULT);
				return;
			case VisualeditorPackage.ASSOCIATION__ZEND_IS_UNIQUE:
				setZEndIsUnique(ZEND_IS_UNIQUE_EDEFAULT);
				return;
			case VisualeditorPackage.ASSOCIATION__ZEND_IS_CHANGEABLE:
				setZEndIsChangeable(ZEND_IS_CHANGEABLE_EDEFAULT);
				return;
			case VisualeditorPackage.ASSOCIATION__ZEND_AGGREGATION:
				setZEndAggregation(ZEND_AGGREGATION_EDEFAULT);
				return;
			case VisualeditorPackage.ASSOCIATION__ZEND_VISIBILITY:
				setZEndVisibility(ZEND_VISIBILITY_EDEFAULT);
				return;
			}
			super.eUnset(featureID);
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case VisualeditorPackage.ASSOCIATION__AEND:
			return aEnd != null;
		case VisualeditorPackage.ASSOCIATION__AEND_NAME:
			return AEND_NAME_EDEFAULT == null ? aEndName != null
					: !AEND_NAME_EDEFAULT.equals(aEndName);
		case VisualeditorPackage.ASSOCIATION__AEND_MULTIPLICITY:
			return aEndMultiplicity != AEND_MULTIPLICITY_EDEFAULT;
		case VisualeditorPackage.ASSOCIATION__AEND_IS_NAVIGABLE:
			return aEndIsNavigable != AEND_IS_NAVIGABLE_EDEFAULT;
		case VisualeditorPackage.ASSOCIATION__AEND_IS_ORDERED:
			return aEndIsOrdered != AEND_IS_ORDERED_EDEFAULT;
		case VisualeditorPackage.ASSOCIATION__AEND_IS_UNIQUE:
			return aEndIsUnique != AEND_IS_UNIQUE_EDEFAULT;
		case VisualeditorPackage.ASSOCIATION__AEND_IS_CHANGEABLE:
			return aEndIsChangeable != AEND_IS_CHANGEABLE_EDEFAULT;
		case VisualeditorPackage.ASSOCIATION__AEND_AGGREGATION:
			return aEndAggregation != AEND_AGGREGATION_EDEFAULT;
		case VisualeditorPackage.ASSOCIATION__ZEND:
			return zEnd != null;
		case VisualeditorPackage.ASSOCIATION__ZEND_NAME:
			return ZEND_NAME_EDEFAULT == null ? zEndName != null
					: !ZEND_NAME_EDEFAULT.equals(zEndName);
		case VisualeditorPackage.ASSOCIATION__ZEND_MULTIPLICITY:
			return zEndMultiplicity != ZEND_MULTIPLICITY_EDEFAULT;
		case VisualeditorPackage.ASSOCIATION__ZEND_IS_NAVIGABLE:
			return zEndIsNavigable != ZEND_IS_NAVIGABLE_EDEFAULT;
		case VisualeditorPackage.ASSOCIATION__ZEND_IS_ORDERED:
			return zEndIsOrdered != ZEND_IS_ORDERED_EDEFAULT;
		case VisualeditorPackage.ASSOCIATION__ZEND_IS_UNIQUE:
			return zEndIsUnique != ZEND_IS_UNIQUE_EDEFAULT;
		case VisualeditorPackage.ASSOCIATION__ZEND_IS_CHANGEABLE:
			return zEndIsChangeable != ZEND_IS_CHANGEABLE_EDEFAULT;
		case VisualeditorPackage.ASSOCIATION__ZEND_AGGREGATION:
			return zEndAggregation != ZEND_AGGREGATION_EDEFAULT;
		case VisualeditorPackage.ASSOCIATION__AEND_VISIBILITY:
			return aEndVisibility != AEND_VISIBILITY_EDEFAULT;
		case VisualeditorPackage.ASSOCIATION__ZEND_VISIBILITY:
			return zEndVisibility != ZEND_VISIBILITY_EDEFAULT;
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
		result.append(" (aEndName: ");
		result.append(aEndName);
		result.append(", aEndMultiplicity: ");
		result.append(aEndMultiplicity);
		result.append(", aEndIsNavigable: ");
		result.append(aEndIsNavigable);
		result.append(", aEndIsOrdered: ");
		result.append(aEndIsOrdered);
		result.append(", aEndIsUnique: ");
		result.append(aEndIsUnique);
		result.append(", aEndIsChangeable: ");
		result.append(aEndIsChangeable);
		result.append(", aEndAggregation: ");
		result.append(aEndAggregation);
		result.append(", zEndName: ");
		result.append(zEndName);
		result.append(", zEndMultiplicity: ");
		result.append(zEndMultiplicity);
		result.append(", zEndIsNavigable: ");
		result.append(zEndIsNavigable);
		result.append(", zEndIsOrdered: ");
		result.append(zEndIsOrdered);
		result.append(", zEndIsUnique: ");
		result.append(zEndIsUnique);
		result.append(", zEndIsChangeable: ");
		result.append(zEndIsChangeable);
		result.append(", zEndAggregation: ");
		result.append(zEndAggregation);
		result.append(", aEndVisibility: ");
		result.append(aEndVisibility);
		result.append(", zEndVisibility: ");
		result.append(zEndVisibility);
		result.append(')');
		return result.toString();
	}

} // AssociationImpl
