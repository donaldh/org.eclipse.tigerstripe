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
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.tigerstripe.workbench.internal.core.util.Misc;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Method;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Parameter;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Method</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.MethodImpl#getParameters <em>Parameters</em>}</li>
 * <li>{@link org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.MethodImpl#isIsAbstract <em>Is Abstract</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class MethodImpl extends TypedElementImpl implements Method {
	/**
	 * The cached value of the '{@link #getParameters() <em>Parameters</em>}'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getParameters()
	 * @generated
	 * @ordered
	 */
	protected EList parameters = null;

	/**
	 * The default value of the '{@link #isIsAbstract() <em>Is Abstract</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isIsAbstract()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_ABSTRACT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsAbstract() <em>Is Abstract</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isIsAbstract()
	 * @generated
	 * @ordered
	 */
	protected boolean isAbstract = IS_ABSTRACT_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected MethodImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return VisualeditorPackage.Literals.METHOD;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList getParameters() {
		if (parameters == null) {
			parameters = new EObjectContainmentEList(Parameter.class, this,
					VisualeditorPackage.METHOD__PARAMETERS);
		}
		return parameters;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isIsAbstract() {
		return isAbstract;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setIsAbstract(boolean newIsAbstract) {
		boolean oldIsAbstract = isAbstract;
		isAbstract = newIsAbstract;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					VisualeditorPackage.METHOD__IS_ABSTRACT, oldIsAbstract,
					isAbstract));
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
		case VisualeditorPackage.METHOD__PARAMETERS:
			return ((InternalEList) getParameters())
					.basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case VisualeditorPackage.METHOD__PARAMETERS:
			return getParameters();
		case VisualeditorPackage.METHOD__IS_ABSTRACT:
			return isIsAbstract() ? Boolean.TRUE : Boolean.FALSE;
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
		case VisualeditorPackage.METHOD__PARAMETERS:
			getParameters().clear();
			getParameters().addAll((Collection) newValue);
			return;
		case VisualeditorPackage.METHOD__IS_ABSTRACT:
			setIsAbstract(((Boolean) newValue).booleanValue());
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
		case VisualeditorPackage.METHOD__PARAMETERS:
			getParameters().clear();
			return;
		case VisualeditorPackage.METHOD__IS_ABSTRACT:
			setIsAbstract(IS_ABSTRACT_EDEFAULT);
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
		case VisualeditorPackage.METHOD__PARAMETERS:
			return parameters != null && !parameters.isEmpty();
		case VisualeditorPackage.METHOD__IS_ABSTRACT:
			return isAbstract != IS_ABSTRACT_EDEFAULT;
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
		result.append(" (isAbstract: ");
		result.append(isAbstract);
		result.append(')');
		return result.toString();
	}

	private String getParamsString() {
		// get the list of method parameters from method
		List<Parameter> params = getParameters();
		int numParams = params.size();
		// if the number of arguments is zero, just return an empty string
		if (numParams == 0)
			return "";
		// else, append the parameter types to form a list of types for the
		// method signature
		StringBuffer paramBuffer = new StringBuffer();
		int paramCount = 0;
		for (Parameter param : params) {
			String paramString = Util.nameOf(param.getType());
			paramBuffer.append(Misc.removeJavaLangString(paramString));

			if (param.getTypeMultiplicity() != AssocMultiplicity.ONE_LITERAL) {
				paramBuffer.append("["
						+ param.getTypeMultiplicity().getLiteral() + "]");
			}

			if (param.getDefaultValue() != null) {
				if (param.getDefaultValue().length() == 0) {
					paramBuffer.append("=\"\"");
				} else {
					paramBuffer.append("=" + param.getDefaultValue());
				}
			}

			if (++paramCount < numParams)
				paramBuffer.append(", ");
		}
		String paramString = paramBuffer.toString();
		return paramString;
	}

	public String getLabelString() {
		String result = getName() + "(" + getParamsString() + ")::";
		String retType = getType();
		result = result + retType;

		if (!"void".equals(retType)
				&& getTypeMultiplicity() != AssocMultiplicity.ONE_LITERAL) {
			result = result + "[" + getTypeMultiplicity().getLiteral() + "]";
		}

		if (!"void".equals(retType) && getDefaultValue() != null) {
			if (getDefaultValue().length() == 0) {
				result = result + "=\"\"";
			} else {
				result = result + "=" + getDefaultValue();
			}
		}
		return result;
	}

	public boolean sameSignature(Method other) {
		List<Parameter> otherParameters = other.getParameters();
		List<Parameter> myParameters = getParameters();

		if (getParameters().size() != otherParameters.size())
			return false; // different number of parameters.

		for (int i = 0; i < myParameters.size(); i++) {
			Parameter myParam = myParameters.get(i);
			Parameter otherParam = otherParameters.get(i);
			if (!myParam.getType().equals(otherParam.getType())
					|| myParam.getTypeMultiplicity() != otherParam
							.getTypeMultiplicity())
				return false;
		}
		return true;
	}
	
	private IMethod method;

	public IMethod getMethod() {
		return method;
	}

	public void setMethod(IMethod method) {
		this.method = method;
	}
	
	

} // MethodImpl
