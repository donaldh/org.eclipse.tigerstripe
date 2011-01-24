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
package org.eclipse.tigerstripe.workbench.ui.instancediagram;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Association Instance</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getAEnd <em>AEnd</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getAEndName <em>AEnd Name</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getAEndMultiplicityLowerBound <em>AEnd Multiplicity Lower Bound</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getAEndMultiplicityUpperBound <em>AEnd Multiplicity Upper Bound</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#isAEndIsNavigable <em>AEnd Is Navigable</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#isAEndIsOrdered <em>AEnd Is Ordered</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getAEndIsChangeable <em>AEnd Is Changeable</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getAEndAggregation <em>AEnd Aggregation</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getZEnd <em>ZEnd</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getZEndName <em>ZEnd Name</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getZEndMultiplicityLowerBound <em>ZEnd Multiplicity Lower Bound</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getZEndMultiplicityUpperBound <em>ZEnd Multiplicity Upper Bound</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#isZEndIsNavigable <em>ZEnd Is Navigable</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#isZEndIsOrdered <em>ZEnd Is Ordered</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getZEndIsChangeable <em>ZEnd Is Changeable</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getZEndAggregation <em>ZEnd Aggregation</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getReferenceName <em>Reference Name</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getAEndOrderNumber <em>AEnd Order Number</em>}</li>
 *   <li>{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getZEndOrderNumber <em>ZEnd Order Number</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getAssociationInstance()
 * @model
 * @generated
 */
public interface AssociationInstance extends Instance {
	/**
	 * Returns the value of the '<em><b>AEnd</b></em>' reference. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>AEnd</em>' reference isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>AEnd</em>' reference.
	 * @see #setAEnd(Instance)
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getAssociationInstance_AEnd()
	 * @model
	 * @generated
	 */
	Instance getAEnd();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getAEnd <em>AEnd</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>AEnd</em>' reference.
	 * @see #getAEnd()
	 * @generated
	 */
	void setAEnd(Instance value);

	/**
	 * Returns the value of the '<em><b>AEnd Name</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>AEnd Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>AEnd Name</em>' attribute.
	 * @see #setAEndName(String)
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getAssociationInstance_AEndName()
	 * @model
	 * @generated
	 */
	String getAEndName();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getAEndName <em>AEnd Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>AEnd Name</em>' attribute.
	 * @see #getAEndName()
	 * @generated
	 */
	void setAEndName(String value);

	/**
	 * Returns the value of the '<em><b>AEnd Multiplicity Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>AEnd Multiplicity Lower Bound</em>'
	 * attribute isn't clear, there really should be more of a description
	 * here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>AEnd Multiplicity Lower Bound</em>' attribute.
	 * @see #setAEndMultiplicityLowerBound(String)
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getAssociationInstance_AEndMultiplicityLowerBound()
	 * @model
	 * @generated
	 */
	String getAEndMultiplicityLowerBound();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getAEndMultiplicityLowerBound <em>AEnd Multiplicity Lower Bound</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>AEnd Multiplicity Lower Bound</em>' attribute.
	 * @see #getAEndMultiplicityLowerBound()
	 * @generated
	 */
	void setAEndMultiplicityLowerBound(String value);

	/**
	 * Returns the value of the '<em><b>AEnd Multiplicity Upper Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>AEnd Multiplicity Upper Bound</em>'
	 * attribute isn't clear, there really should be more of a description
	 * here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>AEnd Multiplicity Upper Bound</em>' attribute.
	 * @see #setAEndMultiplicityUpperBound(String)
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getAssociationInstance_AEndMultiplicityUpperBound()
	 * @model
	 * @generated
	 */
	String getAEndMultiplicityUpperBound();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getAEndMultiplicityUpperBound <em>AEnd Multiplicity Upper Bound</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>AEnd Multiplicity Upper Bound</em>' attribute.
	 * @see #getAEndMultiplicityUpperBound()
	 * @generated
	 */
	void setAEndMultiplicityUpperBound(String value);

	/**
	 * Returns the value of the '<em><b>AEnd Is Navigable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>AEnd Is Navigable</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>AEnd Is Navigable</em>' attribute.
	 * @see #setAEndIsNavigable(boolean)
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getAssociationInstance_AEndIsNavigable()
	 * @model
	 * @generated
	 */
	boolean isAEndIsNavigable();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#isAEndIsNavigable <em>AEnd Is Navigable</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>AEnd Is Navigable</em>' attribute.
	 * @see #isAEndIsNavigable()
	 * @generated
	 */
	void setAEndIsNavigable(boolean value);

	/**
	 * Returns the value of the '<em><b>AEnd Is Ordered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>AEnd Is Ordered</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>AEnd Is Ordered</em>' attribute.
	 * @see #setAEndIsOrdered(boolean)
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getAssociationInstance_AEndIsOrdered()
	 * @model
	 * @generated
	 */
	boolean isAEndIsOrdered();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#isAEndIsOrdered <em>AEnd Is Ordered</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>AEnd Is Ordered</em>' attribute.
	 * @see #isAEndIsOrdered()
	 * @generated
	 */
	void setAEndIsOrdered(boolean value);

	/**
	 * Returns the value of the '<em><b>AEnd Is Changeable</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.tigerstripe.workbench.ui.instancediagram.ChangeableEnum}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>AEnd Is Changeable</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>AEnd Is Changeable</em>' attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.ChangeableEnum
	 * @see #setAEndIsChangeable(ChangeableEnum)
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getAssociationInstance_AEndIsChangeable()
	 * @model
	 * @generated
	 */
	ChangeableEnum getAEndIsChangeable();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getAEndIsChangeable <em>AEnd Is Changeable</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>AEnd Is Changeable</em>' attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.ChangeableEnum
	 * @see #getAEndIsChangeable()
	 * @generated
	 */
	void setAEndIsChangeable(ChangeableEnum value);

	/**
	 * Returns the value of the '<em><b>AEnd Aggregation</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AggregationEnum}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>AEnd Aggregation</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>AEnd Aggregation</em>' attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.AggregationEnum
	 * @see #setAEndAggregation(AggregationEnum)
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getAssociationInstance_AEndAggregation()
	 * @model
	 * @generated
	 */
	AggregationEnum getAEndAggregation();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getAEndAggregation <em>AEnd Aggregation</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>AEnd Aggregation</em>' attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.AggregationEnum
	 * @see #getAEndAggregation()
	 * @generated
	 */
	void setAEndAggregation(AggregationEnum value);

	/**
	 * Returns the value of the '<em><b>ZEnd</b></em>' reference. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ZEnd</em>' reference isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>ZEnd</em>' reference.
	 * @see #setZEnd(Instance)
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getAssociationInstance_ZEnd()
	 * @model
	 * @generated
	 */
	Instance getZEnd();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getZEnd <em>ZEnd</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>ZEnd</em>' reference.
	 * @see #getZEnd()
	 * @generated
	 */
	void setZEnd(Instance value);

	/**
	 * Returns the value of the '<em><b>ZEnd Name</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ZEnd Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>ZEnd Name</em>' attribute.
	 * @see #setZEndName(String)
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getAssociationInstance_ZEndName()
	 * @model
	 * @generated
	 */
	String getZEndName();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getZEndName <em>ZEnd Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>ZEnd Name</em>' attribute.
	 * @see #getZEndName()
	 * @generated
	 */
	void setZEndName(String value);

	/**
	 * Returns the value of the '<em><b>ZEnd Multiplicity Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ZEnd Multiplicity Lower Bound</em>'
	 * attribute isn't clear, there really should be more of a description
	 * here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>ZEnd Multiplicity Lower Bound</em>' attribute.
	 * @see #setZEndMultiplicityLowerBound(String)
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getAssociationInstance_ZEndMultiplicityLowerBound()
	 * @model
	 * @generated
	 */
	String getZEndMultiplicityLowerBound();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getZEndMultiplicityLowerBound <em>ZEnd Multiplicity Lower Bound</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>ZEnd Multiplicity Lower Bound</em>' attribute.
	 * @see #getZEndMultiplicityLowerBound()
	 * @generated
	 */
	void setZEndMultiplicityLowerBound(String value);

	/**
	 * Returns the value of the '<em><b>ZEnd Multiplicity Upper Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ZEnd Multiplicity Upper Bound</em>'
	 * attribute isn't clear, there really should be more of a description
	 * here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>ZEnd Multiplicity Upper Bound</em>' attribute.
	 * @see #setZEndMultiplicityUpperBound(String)
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getAssociationInstance_ZEndMultiplicityUpperBound()
	 * @model
	 * @generated
	 */
	String getZEndMultiplicityUpperBound();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getZEndMultiplicityUpperBound <em>ZEnd Multiplicity Upper Bound</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>ZEnd Multiplicity Upper Bound</em>' attribute.
	 * @see #getZEndMultiplicityUpperBound()
	 * @generated
	 */
	void setZEndMultiplicityUpperBound(String value);

	/**
	 * Returns the value of the '<em><b>ZEnd Is Navigable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ZEnd Is Navigable</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>ZEnd Is Navigable</em>' attribute.
	 * @see #setZEndIsNavigable(boolean)
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getAssociationInstance_ZEndIsNavigable()
	 * @model
	 * @generated
	 */
	boolean isZEndIsNavigable();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#isZEndIsNavigable <em>ZEnd Is Navigable</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>ZEnd Is Navigable</em>' attribute.
	 * @see #isZEndIsNavigable()
	 * @generated
	 */
	void setZEndIsNavigable(boolean value);

	/**
	 * Returns the value of the '<em><b>ZEnd Is Ordered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ZEnd Is Ordered</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>ZEnd Is Ordered</em>' attribute.
	 * @see #setZEndIsOrdered(boolean)
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getAssociationInstance_ZEndIsOrdered()
	 * @model
	 * @generated
	 */
	boolean isZEndIsOrdered();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#isZEndIsOrdered <em>ZEnd Is Ordered</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>ZEnd Is Ordered</em>' attribute.
	 * @see #isZEndIsOrdered()
	 * @generated
	 */
	void setZEndIsOrdered(boolean value);

	/**
	 * Returns the value of the '<em><b>ZEnd Is Changeable</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.tigerstripe.workbench.ui.instancediagram.ChangeableEnum}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ZEnd Is Changeable</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>ZEnd Is Changeable</em>' attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.ChangeableEnum
	 * @see #setZEndIsChangeable(ChangeableEnum)
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getAssociationInstance_ZEndIsChangeable()
	 * @model
	 * @generated
	 */
	ChangeableEnum getZEndIsChangeable();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getZEndIsChangeable <em>ZEnd Is Changeable</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>ZEnd Is Changeable</em>' attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.ChangeableEnum
	 * @see #getZEndIsChangeable()
	 * @generated
	 */
	void setZEndIsChangeable(ChangeableEnum value);

	/**
	 * Returns the value of the '<em><b>ZEnd Aggregation</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AggregationEnum}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ZEnd Aggregation</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>ZEnd Aggregation</em>' attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.AggregationEnum
	 * @see #setZEndAggregation(AggregationEnum)
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getAssociationInstance_ZEndAggregation()
	 * @model
	 * @generated
	 */
	AggregationEnum getZEndAggregation();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getZEndAggregation <em>ZEnd Aggregation</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>ZEnd Aggregation</em>' attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.AggregationEnum
	 * @see #getZEndAggregation()
	 * @generated
	 */
	void setZEndAggregation(AggregationEnum value);

	/**
	 * Returns the value of the '<em><b>Reference Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Reference Name</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Reference Name</em>' attribute.
	 * @see #setReferenceName(String)
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getAssociationInstance_ReferenceName()
	 * @model
	 * @generated
	 */
	String getReferenceName();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getReferenceName <em>Reference Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Reference Name</em>' attribute.
	 * @see #getReferenceName()
	 * @generated
	 */
	void setReferenceName(String value);

	/**
	 * Returns the value of the '<em><b>AEnd Order Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>AEnd Order Number</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>AEnd Order Number</em>' attribute.
	 * @see #setAEndOrderNumber(Integer)
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getAssociationInstance_AEndOrderNumber()
	 * @model
	 * @generated
	 */
	Integer getAEndOrderNumber();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getAEndOrderNumber <em>AEnd Order Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>AEnd Order Number</em>' attribute.
	 * @see #getAEndOrderNumber()
	 * @generated
	 */
	void setAEndOrderNumber(Integer value);

	/**
	 * Returns the value of the '<em><b>ZEnd Order Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ZEnd Order Number</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>ZEnd Order Number</em>' attribute.
	 * @see #setZEndOrderNumber(Integer)
	 * @see org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramPackage#getAssociationInstance_ZEndOrderNumber()
	 * @model
	 * @generated
	 */
	Integer getZEndOrderNumber();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance#getZEndOrderNumber <em>ZEnd Order Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>ZEnd Order Number</em>' attribute.
	 * @see #getZEndOrderNumber()
	 * @generated
	 */
	void setZEndOrderNumber(Integer value);

	/**
	 * used internally to determine which if this association instance
	 * represents a reference to another class instance (rather than a true
	 * association)
	 */
	public boolean isReferenceType();

} // AssociationInstance
