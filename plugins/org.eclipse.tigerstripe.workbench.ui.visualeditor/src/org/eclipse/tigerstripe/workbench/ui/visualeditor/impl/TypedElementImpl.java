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

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IOssjLegacySettigsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.OssjLegacySettingsProperty;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.profile.primitiveType.IPrimitiveTypeDef;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.TypeMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.TypedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Visibility;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Typed Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.TypedElementImpl#getType <em>Type</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.TypedElementImpl#getMultiplicity <em>Multiplicity</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.TypedElementImpl#getVisibility <em>Visibility</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.TypedElementImpl#isIsOrdered <em>Is Ordered</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.TypedElementImpl#isIsUnique <em>Is Unique</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.TypedElementImpl#getTypeMultiplicity <em>Type Multiplicity</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.TypedElementImpl#getDefaultValue <em>Default Value</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class TypedElementImpl extends NamedElementImpl implements TypedElement {
	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final String TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected String type = TYPE_EDEFAULT;

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
	 * The default value of the '{@link #getVisibility() <em>Visibility</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getVisibility()
	 * @generated
	 * @ordered
	 */
	protected static final Visibility VISIBILITY_EDEFAULT = Visibility.PUBLIC_LITERAL;

	/**
	 * The cached value of the '{@link #getVisibility() <em>Visibility</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getVisibility()
	 * @generated
	 * @ordered
	 */
	protected Visibility visibility = VISIBILITY_EDEFAULT;

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
	 * The default value of the '{@link #getDefaultValue() <em>Default Value</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDefaultValue()
	 * @generated
	 * @ordered
	 */
	protected static final String DEFAULT_VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDefaultValue() <em>Default Value</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDefaultValue()
	 * @generated
	 * @ordered
	 */
	protected String defaultValue = DEFAULT_VALUE_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected TypedElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return VisualeditorPackage.Literals.TYPED_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getType() {
		return type;
	}

	private String getPackagePart(String fullyQualifiedName) {
		int idx = fullyQualifiedName.lastIndexOf(".");
		if (idx > 0)
			return fullyQualifiedName.substring(0, idx);
		return "";
	}

	private String getClassNamePart(String fullyQualifiedName) {
		int idx = fullyQualifiedName.lastIndexOf(".");
		if (idx > 0 && idx < (fullyQualifiedName.length() - 1))
			return fullyQualifiedName.substring(idx + 1);
		return fullyQualifiedName;
	}

	/**
	 * Checks in the current active profile whether References are allowed or
	 * not
	 * 
	 * @return
	 */
	private static boolean shouldDisplayReference() {
		OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);
		boolean displayReference = prop
				.getPropertyValue(IOssjLegacySettigsProperty.USEATTRIBUTES_ASREFERENCE);
		return displayReference;
	}

	protected boolean isXMIRead() {
		Exception e = new Exception();
		StackTraceElement[] stackTrace = e.getStackTrace();
		for (StackTraceElement stackTraceElement : stackTrace) {
			String className = stackTraceElement.getClassName();
			int lastDotPos = className.lastIndexOf(".");
			if (lastDotPos >= 0
					&& lastDotPos < className.length() - 2
					&& className.substring(lastDotPos + 1).startsWith(
							"XMLHelper")
					&& stackTraceElement.getMethodName().equals("setValue"))
				return true;
		}
		return false;
	}

	protected boolean isEMFsetCommand() {
		Exception e = new Exception();
		StackTraceElement[] stackTrace = e.getStackTrace();
		for (StackTraceElement stackTraceElement : stackTrace) {
			String className = stackTraceElement.getClassName();
			int lastDotPos = className.lastIndexOf(".");
			if (lastDotPos >= 0
					&& lastDotPos < className.length() - 2
					&& className.substring(lastDotPos + 1).startsWith("EMF")
					&& stackTraceElement.getMethodName().equals(
							"setPropertyValue"))
				return true;
		}
		return false;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setType(String newType) {
		if (newType == null)
			newType = "";
		/*
		 * if the newType value ends in [], then set a flag to use later in
		 * setting the multiplicity for this type (assuming the rest of the
		 * value works out)
		 */
		boolean isArrayType = false;
		if (!isEMFsetCommand() && newType.endsWith("[]")) {
			newType = newType.substring(0, newType.lastIndexOf("[]"));
			isArrayType = true;
		}
		/*
		 * if the new type is void and this is a method return type or if the
		 * newType is a not a keyword and is a valid element or classname and
		 * this is a method return type or if the newType is "String", or if the
		 * new type is one of the primitive types available in this session,
		 * then accept the changes, otherwise ignore the changes (reset to the
		 * old value)
		 */
		boolean acceptChanges = false;
		boolean isMethodReturnType = (this instanceof MethodImpl);
		boolean isParameterType = (this instanceof ParameterImpl);
		boolean isEnumeration = isEnumeration(newType);
		String classNamePart = getClassNamePart(newType);
		String packageNamePart = getPackagePart(newType);
		// if it's a method return type, then void is allowed as a value
		if (isMethodReturnType && newType.equals("void"))
			acceptChanges = true;
		/*
		 * else, if it's a method return type then we need to make sure that
		 * it's (a) the class name part is not a reserved keyword (b) the class
		 * name part is a valid element name or class name and (c) the package
		 * name part is a valid package name
		 */
		else if ((isMethodReturnType || isParameterType
				|| !shouldDisplayReference() || isEnumeration)
				&& !NamedElementImpl.keywordList.contains(classNamePart)
				&& (NamedElementImpl.elementNamePattern.matcher(classNamePart)
						.matches() || NamedElementImpl.classNamePattern
						.matcher(classNamePart).matches())
				&& NamedElementImpl.packageNamePattern.matcher(packageNamePart)
						.matches())
			acceptChanges = true;
		// else if it's a type of String, accept the change
		else if (newType.equals("String"))
			acceptChanges = true;
		// else if it's a primitive type but not "void", accept the change
		else if (!newType.equals("void")) {
			Collection<IPrimitiveTypeDef> primitiveTypes = TigerstripeCore
					.getWorkbenchProfileSession().getActiveProfile()
					.getPrimitiveTypeDefs(true);
			for (IPrimitiveTypeDef primitive : primitiveTypes) {
				String name = "";
				if (primitive.getPackageName().equals("<reserved>"))
					name = primitive.getName();
				else
					name = primitive.getPackageName() + "."
							+ primitive.getName();
				if (newType.equals(name)) {
					acceptChanges = true;
					break;
				} else if (newType.equals(primitive.getName())) {
					// Allowing the users to omit the "primitive." prefix on a
					// primitive type
					// So they can type "datetime" or "primitive.datetime" and
					// both will
					// be committed as "primitive.datetime"
					newType = name;
					acceptChanges = true;
					break;
				}
			}
		}

		if (isXMIRead()) {
			acceptChanges = true;
		}

		/*
		 * if get to here and the changes are not accepted, roll back to the
		 * previous value for the type
		 */
		if (!acceptChanges) {
			String oldType = type;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.UNSET,
						VisualeditorPackage.TYPED_ELEMENT__TYPE, oldType, type));
			return;
		}
		/*
		 * otherwise, replace the old name with a new name and notify listeners
		 * that the value has changed (if notification is required)
		 */
		String oldType = type;
		type = newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.TYPED_ELEMENT__TYPE, oldType, type));
		/*
		 * check the multiplicity for this TypedElement to ensure it is
		 * consistent with the value entered as part of the input "newType"
		 * string (a string ending in a "[]" is considered to be an array, a
		 * string ending in any other character sequence is not)
		 */
		if (isArrayType && getMultiplicity() != TypeMultiplicity.ARRAY_LITERAL)
			setMultiplicity(TypeMultiplicity.ARRAY_LITERAL);
		else if (!isArrayType
				&& getMultiplicity() != TypeMultiplicity.NONE_LITERAL)
			setMultiplicity(TypeMultiplicity.NONE_LITERAL);
	}

	private boolean isEnumeration(String fqn) {
		if (this.eContainer instanceof QualifiedNamedElementImpl) {
			Map map = (Map) this.eContainer().eContainer();
			try {
				IArtifactManagerSession session = map
						.getCorrespondingITigerstripeProject()
						.getArtifactManagerSession();
				IAbstractArtifact art = session
						.getArtifactByFullyQualifiedName(fqn);
				return art instanceof IEnumArtifact;
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage(
						"TigerstripeException detected", e);
			}
		}
		return false;
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
					VisualeditorPackage.TYPED_ELEMENT__MULTIPLICITY,
					oldMultiplicity, multiplicity));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Visibility getVisibility() {
		return visibility;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setVisibility(Visibility newVisibility) {
		Visibility oldVisibility = visibility;
		visibility = newVisibility == null ? VISIBILITY_EDEFAULT
				: newVisibility;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.TYPED_ELEMENT__VISIBILITY,
					oldVisibility, visibility));
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
					VisualeditorPackage.TYPED_ELEMENT__IS_ORDERED,
					oldIsOrdered, isOrdered));
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
					VisualeditorPackage.TYPED_ELEMENT__IS_UNIQUE, oldIsUnique,
					isUnique));
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
					VisualeditorPackage.TYPED_ELEMENT__TYPE_MULTIPLICITY,
					oldTypeMultiplicity, typeMultiplicity));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDefaultValue(String newDefaultValue) {
		String oldDefaultValue = defaultValue;
		defaultValue = newDefaultValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.TYPED_ELEMENT__DEFAULT_VALUE,
					oldDefaultValue, defaultValue));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case VisualeditorPackage.TYPED_ELEMENT__TYPE:
			return getType();
		case VisualeditorPackage.TYPED_ELEMENT__MULTIPLICITY:
			return getMultiplicity();
		case VisualeditorPackage.TYPED_ELEMENT__VISIBILITY:
			return getVisibility();
		case VisualeditorPackage.TYPED_ELEMENT__IS_ORDERED:
			return isIsOrdered() ? Boolean.TRUE : Boolean.FALSE;
		case VisualeditorPackage.TYPED_ELEMENT__IS_UNIQUE:
			return isIsUnique() ? Boolean.TRUE : Boolean.FALSE;
		case VisualeditorPackage.TYPED_ELEMENT__TYPE_MULTIPLICITY:
			return getTypeMultiplicity();
		case VisualeditorPackage.TYPED_ELEMENT__DEFAULT_VALUE:
			return getDefaultValue();
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
		switch (featureID) {
		case VisualeditorPackage.TYPED_ELEMENT__TYPE:
			setType((String) newValue);
			return;
		case VisualeditorPackage.TYPED_ELEMENT__MULTIPLICITY:
			setMultiplicity((TypeMultiplicity) newValue);
			return;
		case VisualeditorPackage.TYPED_ELEMENT__VISIBILITY:
			setVisibility((Visibility) newValue);
			return;
		case VisualeditorPackage.TYPED_ELEMENT__DEFAULT_VALUE:
			setDefaultValue((String) newValue);
			return;
		case VisualeditorPackage.TYPED_ELEMENT__IS_ORDERED:
			setIsOrdered(((Boolean) newValue).booleanValue());
			return;
		case VisualeditorPackage.TYPED_ELEMENT__IS_UNIQUE:
			setIsUnique(((Boolean) newValue).booleanValue());
			return;
		case VisualeditorPackage.TYPED_ELEMENT__TYPE_MULTIPLICITY:
			setTypeMultiplicity((AssocMultiplicity) newValue);
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
		case VisualeditorPackage.TYPED_ELEMENT__TYPE:
			setType(TYPE_EDEFAULT);
			return;
		case VisualeditorPackage.TYPED_ELEMENT__MULTIPLICITY:
			setMultiplicity(MULTIPLICITY_EDEFAULT);
			return;
		case VisualeditorPackage.TYPED_ELEMENT__VISIBILITY:
			setVisibility(VISIBILITY_EDEFAULT);
			return;
		case VisualeditorPackage.TYPED_ELEMENT__IS_ORDERED:
			setIsOrdered(IS_ORDERED_EDEFAULT);
			return;
		case VisualeditorPackage.TYPED_ELEMENT__IS_UNIQUE:
			setIsUnique(IS_UNIQUE_EDEFAULT);
			return;
		case VisualeditorPackage.TYPED_ELEMENT__TYPE_MULTIPLICITY:
			setTypeMultiplicity(TYPE_MULTIPLICITY_EDEFAULT);
			return;
		case VisualeditorPackage.TYPED_ELEMENT__DEFAULT_VALUE:
			setDefaultValue(DEFAULT_VALUE_EDEFAULT);
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
		case VisualeditorPackage.TYPED_ELEMENT__TYPE:
			return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT
					.equals(type);
		case VisualeditorPackage.TYPED_ELEMENT__MULTIPLICITY:
			return multiplicity != MULTIPLICITY_EDEFAULT;
		case VisualeditorPackage.TYPED_ELEMENT__VISIBILITY:
			return visibility != VISIBILITY_EDEFAULT;
		case VisualeditorPackage.TYPED_ELEMENT__IS_ORDERED:
			return isOrdered != IS_ORDERED_EDEFAULT;
		case VisualeditorPackage.TYPED_ELEMENT__IS_UNIQUE:
			return isUnique != IS_UNIQUE_EDEFAULT;
		case VisualeditorPackage.TYPED_ELEMENT__TYPE_MULTIPLICITY:
			return typeMultiplicity != TYPE_MULTIPLICITY_EDEFAULT;
		case VisualeditorPackage.TYPED_ELEMENT__DEFAULT_VALUE:
			return DEFAULT_VALUE_EDEFAULT == null ? defaultValue != null
					: !DEFAULT_VALUE_EDEFAULT.equals(defaultValue);
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
		result.append(" (type: ");
		result.append(type);
		result.append(", multiplicity: ");
		result.append(multiplicity);
		result.append(", visibility: ");
		result.append(visibility);
		result.append(", isOrdered: ");
		result.append(isOrdered);
		result.append(", isUnique: ");
		result.append(isUnique);
		result.append(", typeMultiplicity: ");
		result.append(typeMultiplicity);
		result.append(", defaultValue: ");
		result.append(defaultValue);
		result.append(')');
		return result.toString();
	}

} // TypedElementImpl
