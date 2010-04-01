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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.providers;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IGlobalSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.GlobalSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Method;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Parameter;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.TypeMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.utils.ClassDiagramPartsUtils;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.MethodImpl;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.impl.NamedElementImpl;

public class MethodParser extends TigerstripeStructuralFeaturesParser {

	public MethodParser(List features) {
		super(features);
	}

	@Override
	public String getPrintString(IAdaptable adapter, int flags) {
		setCurrentMap(adapter);

		GlobalSettingsProperty propG = (GlobalSettingsProperty) TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.GLOBAL_SETTINGS);
		boolean displayDirection = propG
				.getPropertyValue(IGlobalSettingsProperty.ARGUMENTDIRECTION);

		// first get the print string from the super-class
		String printString = super.getPrintString(adapter, flags);
		// then get the list of method parameters from the wrapped adapter
		Method method = (Method) adapter.getAdapter(MethodImpl.class);
		if (!"void".equals(method.getType())
				&& method.getTypeMultiplicity() != AssocMultiplicity.ONE_LITERAL)
			printString = printString + "[" + method.getTypeMultiplicity()
					+ "]";
		// from that method, get the prefix to use to indicate the method's
		// visibility
		String visibilityPrefix = ClassDiagramPartsUtils
				.visibilityPrefix(method.getVisibility());

		String defValue = "";
		if (!hideDefaultValues() && !"void".equals(method.getType())
				&& method.getDefaultValue() != null) {
			if (method.getDefaultValue().length() == 0) {
				defValue = "=\"\"";
			} else {
				defValue = "=" + method.getDefaultValue();
			}
		}

		// if the method has any stereotypes, then put together a stereotype
		// prefix string for the method
		String stereotypePref = "";
		if (!hideStereotypes()) {
			IWorkbenchProfile profile = TigerstripeCore.getWorkbenchProfileSession().getActiveProfile();
			StringBuffer stereoPrefBuf = new StringBuffer();
			EList stereotypes = method.getStereotypes();
			for (Object obj : stereotypes) {
				String val = (String) obj;
				IStereotype stereo = profile.getStereotypeByName(val);
				if (stereo != null){
					if (stereoPrefBuf.length() == 0)
						stereoPrefBuf.append("<<");
					else 
						stereoPrefBuf.append(", ");
					stereoPrefBuf.append(val);
				}
			}
			if (stereoPrefBuf.length() > 0)
				stereoPrefBuf.append(">>");

			stereotypePref = stereoPrefBuf.toString();
		}

		// and put together a list of method parameters
		List<Parameter> parameterList = method.getParameters();
		int numParams = parameterList.size();
		// if the number of parameters is zero, just return the print string
		// from
		// the superclass (prefixed by the visibility string)
		if (numParams == 0 && stereotypePref.length() == 0)
			return visibilityPrefix + printString + defValue;
		else if (numParams == 0)
			return stereotypePref + " " + visibilityPrefix + printString
					+ defValue;
		// else, append the parameter types to form a list of types for the
		// method signature
		StringBuffer paramBuffer = new StringBuffer();
		int paramCount = 0;
		IMethod iMethod = method.getMethod();
		for (Parameter param : parameterList) {

			if (displayDirection && iMethod != null) {
				IArgument arg = getCorrespondingArg(iMethod, param);
				if (arg != null)
					paramBuffer.append(arg.getDirection().getLabel() + " ");
			}

			if (!hideStereotypes()) {
				StringBuffer argStereoPrefBuf = new StringBuffer();
				EList stereotypes = param.getStereotypes();
				int stereotypeCount = 0;
				for (Object obj : stereotypes) {
					String val = (String) obj;
					if (stereotypeCount == 0)
						argStereoPrefBuf.append("<<");
					argStereoPrefBuf.append(val);
					if (++stereotypeCount < stereotypes.size())
						argStereoPrefBuf.append(", ");
					else
						argStereoPrefBuf.append(">>");
				}
				paramBuffer.append(argStereoPrefBuf.toString());
			}

			if (hidePackage()) {
				paramBuffer.append(Util.nameOf(param.getType()));
			} else {
				paramBuffer.append(param.getType());
			}
			if (param.getTypeMultiplicity() != AssocMultiplicity.ONE_LITERAL) {
				paramBuffer.append("["
						+ param.getTypeMultiplicity().getLiteral() + "]");
			}

			if (!hideDefaultValues() && param.getDefaultValue() != null) {
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
		// and add that string between the parentheses in the print string we
		// got
		// back from the super-class
		String newPrintString = "";
		int rightParenPos = printString.indexOf(")");
		if (rightParenPos >= 0) {
			// should always end up here...
			if (stereotypePref.length() == 0) {
				newPrintString = visibilityPrefix
						+ printString.substring(0, rightParenPos) + paramString
						+ printString.substring(rightParenPos);
			} else {
				newPrintString = stereotypePref + " " + visibilityPrefix
						+ printString.substring(0, rightParenPos) + paramString
						+ printString.substring(rightParenPos);
			}
			return newPrintString + defValue;
		}

		// just in case...
		if (stereotypePref.length() == 0)
			return visibilityPrefix + printString + defValue;
		return stereotypePref + " " + visibilityPrefix + printString + defValue;
	}

	@Override
	public String getEditString(IAdaptable adapter, int flags) {
		String editString = super.getEditString(adapter, flags);
		// then get the underlying attribute from the wrapped adapter
		Method method = (Method) adapter.getAdapter(MethodImpl.class);
		if (method.getMultiplicity() == TypeMultiplicity.ARRAY_LITERAL)
			editString = editString + "[]";
		return editString;
	}

	@Override
	protected Object getValidNewValue(EStructuralFeature feature, Object value) {
		Object newValue = super.getValidNewValue(feature, value);
		if (!(newValue instanceof InvalidValue) && (value instanceof String)) {
			// if the value is for a feature named "type", then check to see if
			// the
			// value ends in "[]", if so, then remove this suffix before
			// checking
			// validity of the value (a suffix of "[]" in the type is used to
			// indicate an array type, only want to check validity of the type
			// name here, not of the type name plus the suffix)
			String strVal = new String((String) value);
			if (feature.getName().equals("type") && strVal.endsWith("[]"))
				strVal = strVal.substring(0, strVal.lastIndexOf("[]"));
			if (!NamedElementImpl.packageNamePattern.matcher(strVal).matches()) {
				value = new InvalidValue(
						"Value that can be converted to a element name is expected");
			}
		}
		return value;
	}

	protected IArgument getCorrespondingArg(IMethod iMethod, Parameter param) {
		for (IArgument arg : iMethod.getArguments()) {
			if (arg.getName().equals(param.getName())) {
				return arg;
			}
		}
		return null;
	}
}
