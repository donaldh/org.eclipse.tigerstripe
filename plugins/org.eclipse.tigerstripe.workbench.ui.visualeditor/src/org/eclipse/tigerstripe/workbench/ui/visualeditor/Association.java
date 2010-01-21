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
package org.eclipse.tigerstripe.workbench.ui.visualeditor;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Association</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getAEnd <em>AEnd</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getAEndName <em>AEnd Name</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getAEndMultiplicity <em>AEnd Multiplicity</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#isAEndIsNavigable <em>AEnd Is Navigable</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#isAEndIsOrdered <em>AEnd Is Ordered</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#isAEndIsUnique <em>AEnd Is Unique</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getAEndIsChangeable <em>AEnd Is Changeable</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getAEndAggregation <em>AEnd Aggregation</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getZEnd <em>ZEnd</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getZEndName <em>ZEnd Name</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getZEndMultiplicity <em>ZEnd Multiplicity</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#isZEndIsNavigable <em>ZEnd Is Navigable</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#isZEndIsOrdered <em>ZEnd Is Ordered</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#isZEndIsUnique <em>ZEnd Is Unique</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getZEndIsChangeable <em>ZEnd Is Changeable</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getZEndAggregation <em>ZEnd Aggregation</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getAEndVisibility <em>AEnd Visibility</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getZEndVisibility <em>ZEnd Visibility</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAssociation()
 * @model
 * @generated
 */
public interface Association extends QualifiedNamedElement {
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
	 * @see #setAEnd(AbstractArtifact)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAssociation_AEnd()
	 * @model
	 * @generated
	 */
	AbstractArtifact getAEnd();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getAEnd <em>AEnd</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>AEnd</em>' reference.
	 * @see #getAEnd()
	 * @generated
	 */
	void setAEnd(AbstractArtifact value);

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
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAssociation_AEndName()
	 * @model
	 * @generated
	 */
	String getAEndName();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getAEndName <em>AEnd Name</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>AEnd Name</em>' attribute.
	 * @see #getAEndName()
	 * @generated
	 */
	void setAEndName(String value);

	/*
	 * @generated NOT
	 */
	String getAEndStereotypeNames();
	
	/*
	 * @generated NOT
	 */
	void setAEndStereotypeNames(String stereotypeNames);
	
	/**
	 * Returns the value of the '<em><b>AEnd Multiplicity</b></em>'
	 * attribute. The literals are from the enumeration
	 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>AEnd Multiplicity</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>AEnd Multiplicity</em>' attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity
	 * @see #setAEndMultiplicity(AssocMultiplicity)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAssociation_AEndMultiplicity()
	 * @model
	 * @generated
	 */
	AssocMultiplicity getAEndMultiplicity();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getAEndMultiplicity <em>AEnd Multiplicity</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>AEnd Multiplicity</em>'
	 *            attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity
	 * @see #getAEndMultiplicity()
	 * @generated
	 */
	void setAEndMultiplicity(AssocMultiplicity value);

	/**
	 * Returns the value of the '<em><b>AEnd Is Navigable</b></em>'
	 * attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>AEnd Is Navigable</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>AEnd Is Navigable</em>' attribute.
	 * @see #setAEndIsNavigable(boolean)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAssociation_AEndIsNavigable()
	 * @model
	 * @generated
	 */
	boolean isAEndIsNavigable();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#isAEndIsNavigable <em>AEnd Is Navigable</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>AEnd Is Navigable</em>'
	 *            attribute.
	 * @see #isAEndIsNavigable()
	 * @generated
	 */
	void setAEndIsNavigable(boolean value);

	/**
	 * Returns the value of the '<em><b>AEnd Is Ordered</b></em>'
	 * attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>AEnd Is Ordered</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>AEnd Is Ordered</em>' attribute.
	 * @see #setAEndIsOrdered(boolean)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAssociation_AEndIsOrdered()
	 * @model
	 * @generated
	 */
	boolean isAEndIsOrdered();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#isAEndIsOrdered <em>AEnd Is Ordered</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>AEnd Is Ordered</em>' attribute.
	 * @see #isAEndIsOrdered()
	 * @generated
	 */
	void setAEndIsOrdered(boolean value);

	/**
	 * Returns the value of the '<em><b>AEnd Is Unique</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>AEnd Is Unique</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>AEnd Is Unique</em>' attribute.
	 * @see #setAEndIsUnique(boolean)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAssociation_AEndIsUnique()
	 * @model
	 * @generated
	 */
	boolean isAEndIsUnique();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#isAEndIsUnique <em>AEnd Is Unique</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>AEnd Is Unique</em>' attribute.
	 * @see #isAEndIsUnique()
	 * @generated
	 */
	void setAEndIsUnique(boolean value);

	/**
	 * Returns the value of the '<em><b>AEnd Is Changeable</b></em>'
	 * attribute. The literals are from the enumeration
	 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.ChangeableEnum}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>AEnd Is Changeable</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>AEnd Is Changeable</em>' attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.ChangeableEnum
	 * @see #setAEndIsChangeable(ChangeableEnum)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAssociation_AEndIsChangeable()
	 * @model
	 * @generated
	 */
	ChangeableEnum getAEndIsChangeable();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getAEndIsChangeable <em>AEnd Is Changeable</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>AEnd Is Changeable</em>'
	 *            attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.ChangeableEnum
	 * @see #getAEndIsChangeable()
	 * @generated
	 */
	void setAEndIsChangeable(ChangeableEnum value);

	/**
	 * Returns the value of the '<em><b>AEnd Aggregation</b></em>'
	 * attribute. The literals are from the enumeration
	 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AggregationEnum}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>AEnd Aggregation</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>AEnd Aggregation</em>' attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AggregationEnum
	 * @see #setAEndAggregation(AggregationEnum)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAssociation_AEndAggregation()
	 * @model
	 * @generated
	 */
	AggregationEnum getAEndAggregation();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getAEndAggregation <em>AEnd Aggregation</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>AEnd Aggregation</em>'
	 *            attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AggregationEnum
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
	 * @see #setZEnd(AbstractArtifact)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAssociation_ZEnd()
	 * @model
	 * @generated
	 */
	AbstractArtifact getZEnd();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getZEnd <em>ZEnd</em>}'
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>ZEnd</em>' reference.
	 * @see #getZEnd()
	 * @generated
	 */
	void setZEnd(AbstractArtifact value);

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
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAssociation_ZEndName()
	 * @model
	 * @generated
	 */
	String getZEndName();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getZEndName <em>ZEnd Name</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>ZEnd Name</em>' attribute.
	 * @see #getZEndName()
	 * @generated
	 */
	void setZEndName(String value);

	/*
	 * @generated NOT
	 */
	String getZEndStereotypeNames();
	
	/*
	 * @generated NOT
	 */
	void setZEndStereotypeNames(String stereotypeNames);
	
	/**
	 * Returns the value of the '<em><b>ZEnd Multiplicity</b></em>'
	 * attribute. The literals are from the enumeration
	 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ZEnd Multiplicity</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>ZEnd Multiplicity</em>' attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity
	 * @see #setZEndMultiplicity(AssocMultiplicity)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAssociation_ZEndMultiplicity()
	 * @model
	 * @generated
	 */
	AssocMultiplicity getZEndMultiplicity();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getZEndMultiplicity <em>ZEnd Multiplicity</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>ZEnd Multiplicity</em>'
	 *            attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity
	 * @see #getZEndMultiplicity()
	 * @generated
	 */
	void setZEndMultiplicity(AssocMultiplicity value);

	/**
	 * Returns the value of the '<em><b>ZEnd Is Navigable</b></em>'
	 * attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ZEnd Is Navigable</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>ZEnd Is Navigable</em>' attribute.
	 * @see #setZEndIsNavigable(boolean)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAssociation_ZEndIsNavigable()
	 * @model
	 * @generated
	 */
	boolean isZEndIsNavigable();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#isZEndIsNavigable <em>ZEnd Is Navigable</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>ZEnd Is Navigable</em>'
	 *            attribute.
	 * @see #isZEndIsNavigable()
	 * @generated
	 */
	void setZEndIsNavigable(boolean value);

	/**
	 * Returns the value of the '<em><b>ZEnd Is Ordered</b></em>'
	 * attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ZEnd Is Ordered</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>ZEnd Is Ordered</em>' attribute.
	 * @see #setZEndIsOrdered(boolean)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAssociation_ZEndIsOrdered()
	 * @model
	 * @generated
	 */
	boolean isZEndIsOrdered();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#isZEndIsOrdered <em>ZEnd Is Ordered</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>ZEnd Is Ordered</em>' attribute.
	 * @see #isZEndIsOrdered()
	 * @generated
	 */
	void setZEndIsOrdered(boolean value);

	/**
	 * Returns the value of the '<em><b>ZEnd Is Unique</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ZEnd Is Unique</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>ZEnd Is Unique</em>' attribute.
	 * @see #setZEndIsUnique(boolean)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAssociation_ZEndIsUnique()
	 * @model
	 * @generated
	 */
	boolean isZEndIsUnique();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#isZEndIsUnique <em>ZEnd Is Unique</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>ZEnd Is Unique</em>' attribute.
	 * @see #isZEndIsUnique()
	 * @generated
	 */
	void setZEndIsUnique(boolean value);

	/**
	 * Returns the value of the '<em><b>ZEnd Is Changeable</b></em>'
	 * attribute. The literals are from the enumeration
	 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.ChangeableEnum}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ZEnd Is Changeable</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>ZEnd Is Changeable</em>' attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.ChangeableEnum
	 * @see #setZEndIsChangeable(ChangeableEnum)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAssociation_ZEndIsChangeable()
	 * @model
	 * @generated
	 */
	ChangeableEnum getZEndIsChangeable();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getZEndIsChangeable <em>ZEnd Is Changeable</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>ZEnd Is Changeable</em>'
	 *            attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.ChangeableEnum
	 * @see #getZEndIsChangeable()
	 * @generated
	 */
	void setZEndIsChangeable(ChangeableEnum value);

	/**
	 * Returns the value of the '<em><b>ZEnd Aggregation</b></em>'
	 * attribute. The literals are from the enumeration
	 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.AggregationEnum}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ZEnd Aggregation</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>ZEnd Aggregation</em>' attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AggregationEnum
	 * @see #setZEndAggregation(AggregationEnum)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAssociation_ZEndAggregation()
	 * @model
	 * @generated
	 */
	AggregationEnum getZEndAggregation();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getZEndAggregation <em>ZEnd Aggregation</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>ZEnd Aggregation</em>'
	 *            attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.AggregationEnum
	 * @see #getZEndAggregation()
	 * @generated
	 */
	void setZEndAggregation(AggregationEnum value);

	/**
	 * Returns the value of the '<em><b>AEnd Visibility</b></em>'
	 * attribute. The literals are from the enumeration
	 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Visibility}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>AEnd Visibility</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>AEnd Visibility</em>' attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Visibility
	 * @see #setAEndVisibility(Visibility)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAssociation_AEndVisibility()
	 * @model
	 * @generated
	 */
	Visibility getAEndVisibility();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getAEndVisibility <em>AEnd Visibility</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>AEnd Visibility</em>' attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Visibility
	 * @see #getAEndVisibility()
	 * @generated
	 */
	void setAEndVisibility(Visibility value);

	/**
	 * Returns the value of the '<em><b>ZEnd Visibility</b></em>'
	 * attribute. The literals are from the enumeration
	 * {@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Visibility}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ZEnd Visibility</em>' attribute isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>ZEnd Visibility</em>' attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Visibility
	 * @see #setZEndVisibility(Visibility)
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage#getAssociation_ZEndVisibility()
	 * @model
	 * @generated
	 */
	Visibility getZEndVisibility();

	/**
	 * Sets the value of the '{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.Association#getZEndVisibility <em>ZEnd Visibility</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>ZEnd Visibility</em>' attribute.
	 * @see org.eclipse.tigerstripe.workbench.ui.visualeditor.Visibility
	 * @see #getZEndVisibility()
	 * @generated
	 */
	void setZEndVisibility(Visibility value);

} // Association
