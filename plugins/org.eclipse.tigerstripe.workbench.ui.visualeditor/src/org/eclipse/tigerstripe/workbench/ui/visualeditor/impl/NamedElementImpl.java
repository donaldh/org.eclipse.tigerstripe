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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.DiagramProperty;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Named Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.NamedElementImpl#getName <em>Name</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.NamedElementImpl#getStereotypes <em>Stereotypes</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.NamedElementImpl#getProperties <em>Properties</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class NamedElementImpl extends EObjectImpl implements NamedElement {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getStereotypes() <em>Stereotypes</em>}'
	 * attribute list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getStereotypes()
	 * @generated
	 * @ordered
	 */
	protected EList stereotypes = null;

	/**
	 * The cached value of the '{@link #getProperties() <em>Properties</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getProperties()
	 * @generated
	 * @ordered
	 */
	protected EList properties = null;

	public static final Pattern classNamePattern = compilePattern("^[A-Za-z_][a-z0-9A-Z_]*$");
	public static final Pattern elementNamePattern = compilePattern("^[A-Za-z_][a-z0-9A-Z_]*$");
	public static final Pattern literalNamePattern = compilePattern("^[A-Za-z_][A-Za-z0-9_]*$");
	public static final Pattern packageNamePattern = compilePattern("^[A-Za-z_][a-zA-Z0-9_]*(\\.[a-zA-Z_][a-zA-Z0-9_]*)*$");
	public static final List keywordList = buildKeywordList();

	private static Pattern compilePattern(String pattern) {
		return Pattern.compile(pattern);
	}

	private static List buildKeywordList() {
		String[] strArray = new String[] { "abstract", "continue", "for",
				"new", "switch", "assert", "default", "goto", "package",
				"synchronized", "boolean", "do", "if", "private", "this",
				"break", "double", "implements", "protected", "throw", "byte",
				"else", "import", "public", "throws", "case", "enum",
				"instanceof", "return", "transient", "catch", "extends", "int",
				"short", "try", "char", "final", "interface", "static", "void",
				"class", "finally", "long", "strictfp", "volatile", "const",
				"float", "native", "super", "while", "true", "false", "null" };
		// convert to list of keywords
		return Arrays.asList(strArray);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected NamedElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return VisualeditorPackage.Literals.NAMED_ELEMENT;
	}

	protected static boolean isReservedKeyword(String val) {
		return keywordList.contains(val);
	}

	private static boolean isConstrainedToElementName(Class clazz) {
		if (clazz
				.getName()
				.equals(
						"org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AttributeImpl")
				|| clazz
						.getName()
						.equals(
								"org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.MethodImpl")
				|| clazz
						.getName()
						.equals(
								"org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.ReferenceImpl")
				|| clazz
						.getName()
						.equals(
								"org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.ParameterImpl")
				|| clazz
						.getName()
						.equals(
								"org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.LiteralImpl"))
			return true;
		return false;
	}

	private static boolean isConstrainedToClassName(Class clazz) {
		if (clazz
				.getName()
				.equals(
						"org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.ManagedEntityArtifactImpl")
				|| clazz
						.getName()
						.equals(
								"org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.DatatypeArtifactImpl")
				|| clazz
						.getName()
						.equals(
								"org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.EnumerationImpl")
				|| clazz
						.getName()
						.equals(
								"org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.NamedQueryArtifactImpl")
				|| clazz
						.getName()
						.equals(
								"org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.UpdateProcedureArtifactImpl")
				|| clazz
						.getName()
						.equals(
								"org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.NotificationArtifactImpl")
				|| clazz
						.getName()
						.equals(
								"org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.SessionFacadeArtifactImpl")
				|| clazz
						.getName()
						.equals(
								"org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.ExceptionArtifactImpl")
				|| clazz
						.getName()
						.equals(
								"org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationClassImpl")
				|| clazz
						.getName()
						.equals(
								"org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.DependencyImpl")
				|| clazz
						.getName()
						.equals(
								"org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.AssociationImpl"))
			return true;
		return false;
	}

	public static boolean isLegalElementName(Class clazz, String strVal) {
		if (strVal == null)
			return false;
		/*
		 * if fail to match the elementNamePattern regexp, then the value is not
		 * a legal classname and should return false
		 */
		if (isConstrainedToElementName(clazz))
			// if it's a literal value, ensure that the name is not reserved and
			// all caps
			if (clazz
					.getName()
					.equals(
							"org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.LiteralImpl")) {
				if (isReservedKeyword(strVal)
						|| !literalNamePattern.matcher(strVal).matches())
					return false;
			}
			// else ensure that the value isn't a reserved keyword and that it
			// can be mapped to a POJO method or attribute
			else if (isReservedKeyword(strVal)
					|| !elementNamePattern.matcher(strVal).matches())
				return false;
		/*
		 * otherwise, return true (the value is a legal elementName value)
		 */
		return true;
	}

	public static boolean isLegalClassName(Class clazz, String strVal) {
		if (strVal == null)
			return false;
		/*
		 * because the Tigerstripe Workbench allows users to create Artifacts
		 * (classes in the POJO model) with names that start with a lower-case
		 * letter, we need to consider names that start with either an
		 * upper-case or a lower-case letter to be legal class names in the
		 * diagram editor to be consistent; by accepting names that start with a
		 * lower case letter, however, we also have to be careful that the name
		 * of the class is not a reserved keyword
		 */
		if (isConstrainedToClassName(clazz)
				&& (isReservedKeyword(strVal) || (!classNamePattern.matcher(
						strVal).matches() && !elementNamePattern
						.matcher(strVal).matches())))
			return false;
		/*
		 * otherwise, return true (the value is a legal classname value)
		 */
		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setName(String newName) {
		/*
		 * if it's a class type that will be mapped into a POJO and the name
		 * isn't correct for a class name or it's a type that will be mapped
		 * into an attribute/method of a POJO and the name isn't correct for an
		 * attribute/method of a POJO, keep the old name and notify listeners
		 * that you are doing so (if notification is required)... while we are
		 * at it, ensure that reserved keywords aren't used for names of objects
		 * too
		 */
		if ((NamedElementImpl.isConstrainedToClassName(this.getClass()) && !NamedElementImpl
				.isLegalClassName(this.getClass(), newName))
				|| (NamedElementImpl
						.isConstrainedToElementName(this.getClass()) && !NamedElementImpl
						.isLegalElementName(this.getClass(), newName))
				|| NamedElementImpl.keywordList.contains(newName)) {
			// if here, unset value (have an illegal value)
			String oldName = name;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.UNSET,
						VisualeditorPackage.NAMED_ELEMENT__NAME, oldName, name));
			return;
		}
		/*
		 * otherwise, replace the old name with a new name and notify listeners
		 * that the value has changed (if notification is required)
		 */
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.NAMED_ELEMENT__NAME, oldName, name));
		return;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList getStereotypes() {
		if (stereotypes == null) {
			stereotypes = new EDataTypeUniqueEList(String.class, this,
					VisualeditorPackage.NAMED_ELEMENT__STEREOTYPES);
		}
		return stereotypes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList getProperties() {
		if (properties == null) {
			properties = new EObjectContainmentEList(DiagramProperty.class,
					this, VisualeditorPackage.NAMED_ELEMENT__PROPERTIES);
		}
		return properties;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd,
			int featureID, NotificationChain msgs) {
		switch (featureID) {
		case VisualeditorPackage.NAMED_ELEMENT__PROPERTIES:
			return ((InternalEList) getProperties())
					.basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	public EList resetStereotypes() {
		EList oldStereotypes = new EDataTypeUniqueEList(String.class, this,
				VisualeditorPackage.NAMED_ELEMENT__STEREOTYPES);
		oldStereotypes.addAll(stereotypes);
		stereotypes = null;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.REMOVE_MANY,
					VisualeditorPackage.NAMED_ELEMENT__STEREOTYPES,
					oldStereotypes, stereotypes));
		return stereotypes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case VisualeditorPackage.NAMED_ELEMENT__NAME:
			return getName();
		case VisualeditorPackage.NAMED_ELEMENT__STEREOTYPES:
			return getStereotypes();
		case VisualeditorPackage.NAMED_ELEMENT__PROPERTIES:
			return getProperties();
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
		case VisualeditorPackage.NAMED_ELEMENT__NAME:
			setName((String) newValue);
			return;
		case VisualeditorPackage.NAMED_ELEMENT__STEREOTYPES:
			getStereotypes().clear();
			getStereotypes().addAll((Collection) newValue);
			return;
		case VisualeditorPackage.NAMED_ELEMENT__PROPERTIES:
			getProperties().clear();
			getProperties().addAll((Collection) newValue);
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
		case VisualeditorPackage.NAMED_ELEMENT__NAME:
			setName(NAME_EDEFAULT);
			return;
		case VisualeditorPackage.NAMED_ELEMENT__STEREOTYPES:
			getStereotypes().clear();
			return;
		case VisualeditorPackage.NAMED_ELEMENT__PROPERTIES:
			getProperties().clear();
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
		case VisualeditorPackage.NAMED_ELEMENT__NAME:
			return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT
					.equals(name);
		case VisualeditorPackage.NAMED_ELEMENT__STEREOTYPES:
			return stereotypes != null && !stereotypes.isEmpty();
		case VisualeditorPackage.NAMED_ELEMENT__PROPERTIES:
			return properties != null && !properties.isEmpty();
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
		result.append(" (name: ");
		result.append(name);
		result.append(", stereotypes: ");
		result.append(stereotypes);
		result.append(')');
		return result.toString();
	}

} // NamedElementImpl
