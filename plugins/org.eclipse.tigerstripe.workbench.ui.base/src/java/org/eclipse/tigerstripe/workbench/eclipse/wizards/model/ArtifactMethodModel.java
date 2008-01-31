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
package org.eclipse.tigerstripe.workbench.eclipse.wizards.model;

import java.util.Iterator;
import java.util.List;

public class ArtifactMethodModel {

	public class ParameterRef {

		private String name;

		private String parameterClass;

		private int dimensions;

		@Override
		public Object clone() {
			ParameterRef result = new ParameterRef();
			result.applyValues(this);
			return result;
		}

		public boolean matches(ParameterRef other) {
			boolean result = false;

			if (other.getParameterClass().equals(getParameterClass())
					&& other.getDimensions() == getDimensions()) {
				result = true;
			}
			return result;
		}

		public String getSignature() {
			String signature = getParameterClass();

			for (int i = 0; i < dimensions; i++) {
				signature = signature + "[]";
			}

			signature = signature + " " + getName();

			return signature;
		}

		public void applyValues(ParameterRef ref) {
			this.name = ref.getName();
			this.parameterClass = ref.getParameterClass();
			this.dimensions = ref.getDimensions();
		}

		public String getParameterClass() {
			return parameterClass;
		}

		public void setParameterClass(String attributeClass) {
			this.parameterClass = attributeClass;
		}

		public int getDimensions() {
			return dimensions;
		}

		public void setDimensions(int dimensions) {
			this.dimensions = dimensions;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	private String description;

	private String methodName;

	private List exceptionList;

	private List parameterList;

	private boolean isVoid;

	private boolean isAbstract;

	private String returnClass;

	private int returnDimension;

	private int methodModifier;

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isAbstract() {
		return this.isAbstract;
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public String getSignature() {
		String signature = modifierLabels[getMethodModifier()];
		signature = signature + " ";

		if (isVoid()) {
			signature = signature + "void";
		} else {
			signature = signature + getReturnClass();
			for (int i = 0; i < getReturnDimension(); i++) {
				signature = signature + "[]";
			}
		}

		signature = signature + " " + getMethodName() + "(";

		if (getParameterList() != null) {
			Iterator iter = getParameterList().iterator();
			boolean firstPass = true;
			while (iter.hasNext()) {

				if (!firstPass) {
					signature = signature + ", ";
				}
				ParameterRef ref = (ParameterRef) iter.next();
				signature = signature + ref.getSignature();

				firstPass = false;
			}
		}
		signature = signature + ")";

		return signature;
	}

	public final int PUBLIC = 0;

	public final int PROTECTED = 1;

	public final int PRIVATE = 2;

	private final String[] modifierLabels = { "public", "protected", "private" };

	public String getProperty(String key) {
		return this.getProperty(key);
	}

	public List getExceptionList() {
		return exceptionList;
	}

	public void setExceptionList(List exceptionList) {
		this.exceptionList = exceptionList;
	}

	public List getParameterList() {
		return parameterList;
	}

	public void setParameterList(List parameterList) {
		this.parameterList = parameterList;
	}

	public boolean isVoid() {
		return isVoid;
	}

	public void setVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	public int getMethodModifier() {
		return methodModifier;
	}

	public void setMethodModifier(int methodModifier) {
		this.methodModifier = methodModifier;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getReturnClass() {
		return returnClass;
	}

	public void setReturnClass(String returnClass) {
		this.returnClass = returnClass;
	}

	public int getReturnDimension() {
		return returnDimension;
	}

	public void setReturnDimension(int returnDimension) {
		this.returnDimension = returnDimension;
	}

	public String getFinalReturnClass() {
		if (isVoid())
			return "void";
		else {
			String result = getReturnClass();
			for (int i = 0; i < getReturnDimension(); i++) {
				result = result + "[]";
			}
			return result;
		}
	}
}
