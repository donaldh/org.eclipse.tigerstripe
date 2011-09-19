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
package org.eclipse.tigerstripe.workbench.internal.core.model;

import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeValidationUtils;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.profile.primitiveType.IPrimitiveTypeDef;

/**
 * @author Eric Dillon
 * 
 * This represents a java type.
 */
public class Type implements IType {

	private final ArtifactManager artifactManager;

	private String fullyQualifiedName;

	/*private String dimensions;*/

	private IModelComponent.EMultiplicity multiplicity;

	public String getFullyQualifiedName() {
		return this.fullyQualifiedName;
	}

	public String getPackage() {
		return Util.packageOf(this.fullyQualifiedName);
	}

	public IModelComponent.EMultiplicity getTypeMultiplicity() {
		return this.multiplicity;
	}

	public void setTypeMultiplicity(IModelComponent.EMultiplicity multiplicity) {
		this.multiplicity = multiplicity;

		/*// for compatibility reason, we need to set the dimension attribute to
		// the closest match
		if (multiplicity == IModelComponent.EMultiplicity.ONE
				|| multiplicity == IModelComponent.EMultiplicity.ZERO_ONE
				|| multiplicity == IModelComponent.EMultiplicity.ZERO) {
			dimensions = dimensionsAsString(MULTIPLICITY_SINGLE);
		} else {
			dimensions = dimensionsAsString(MULTIPLICITY_MULTI);
		}*/
	}

	public String getName() {
		return Util.nameOf(getFullyQualifiedName());
	}

	public void setFullyQualifiedName(String name) {
		this.fullyQualifiedName = name;
	}

/*	public String getDimensions() {
		return this.dimensions;
	}*/

	public ArtifactManager getArtifactManager() {
		return this.artifactManager;
	}

/*	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;

		// For compatibility reasons, we need to update the type multiplicity
		if (dimensionsAsString(MULTIPLICITY_MULTI).equals(dimensions)) {
			multiplicity = IModelComponent.EMultiplicity.ZERO_STAR;
		} else {
			multiplicity = IModelComponent.EMultiplicity.ZERO_ONE;
		}
	}*/

	public Type(ArtifactManager artifactMgr) {
		this("", IModelComponent.EMultiplicity.ZERO_ONE, artifactMgr);
	}

	public Type(String fullyQualifiedName, IModelComponent.EMultiplicity typeMultiplicity,
			ArtifactManager artifactMgr) {
		setFullyQualifiedName(fullyQualifiedName);
		setTypeMultiplicity(typeMultiplicity);
		this.artifactManager = artifactMgr;
	}

	/**
	 * 
	 * @param fullyQualifiedName
	 * @param dimensions
	 * @param artifactMgr
	 * @deprecated since 2.2-rc, no use of dimensions anymore, multiplicity
	 *             instead
	 */
	/*@Deprecated
	public Type(String fullyQualifiedName, String dimensions,
			ArtifactManager artifactMgr) {
		setFullyQualifiedName(fullyQualifiedName);
		setDimensions(dimensions);
		this.artifactManager = artifactMgr;
	}*/

	/**
	 * 
	 * @param fullyQualifiedName
	 * @param dimensions
	 * @param artifactMgr
	 * @deprecated since 2.2-rc, no use of dimensions anymore, multiplicity
	 *             instead
	 */
	@Deprecated
/*	public Type(String fullyQualifiedName, int dimensions,
			ArtifactManager artifactMgr) {
		this(fullyQualifiedName, dimensionsAsString(dimensions), artifactMgr);
	}*/

	/**
	 * 
	 * @param dimensions
	 * @return
	 * @deprecated since 2.2-rc, no use of dimensions anymore, multiplicity
	 *             instead
	 */
/*	@Deprecated
	private static String dimensionsAsString(int dimensions) {
		String result = "";
		for (int i = 0; i < dimensions; i++) {
			result = result + "[]";
		}

		return result;
	}*/

	/**
	 * Returns true if this type is a primitive type
	 * 
	 * @return
	 */
	public boolean isPrimitive() {
		if ("boolean".equals(this.fullyQualifiedName)
				|| "int".equals(this.fullyQualifiedName)
				|| "char".equals(this.fullyQualifiedName)
				|| "float".equals(this.fullyQualifiedName)
				|| "double".equals(this.fullyQualifiedName)
				|| "long".equals(this.fullyQualifiedName)
				|| "short".equals(this.fullyQualifiedName)
				|| "byte".equals(this.fullyQualifiedName))
			return true;
		return false;
	}

	public boolean isArtifact() {
		IAbstractArtifactInternal artifact = getArtifactManager()
				.getArtifactByFullyQualifiedName(getFullyQualifiedName(), true,
						new NullProgressMonitor());
		if (artifact != null)
			return true;
		return false;
	}

	/**
	 * Returns true if this type corresponds to a DatatypeArtifact
	 * 
	 * @author Eric Dillon
	 * 
	 */
	public boolean isDatatype() {
		IAbstractArtifactInternal artifact = getArtifactManager()
				.getArtifactByFullyQualifiedName(getFullyQualifiedName(), true,
						new NullProgressMonitor());
		if ((artifact != null) && (artifact instanceof IDatatypeArtifact))
			return true;
		return false;
	}

	/**
	 * Returns true if this type corresponds to a Query Artifact
	 * 
	 * @author Eric Dillon
	 * 
	 */
	public boolean isQueryType() {
		IAbstractArtifactInternal artifact = getArtifactManager()
				.getArtifactByFullyQualifiedName(getFullyQualifiedName(), true,
						new NullProgressMonitor());
		if ((artifact != null) && (artifact instanceof IQueryArtifact))
			return true;
		return false;
	}

	/**
	 * Returns true if this type corresponds to a Query Artifact
	 * 
	 * @author Eric Dillon
	 * 
	 */
	public boolean isUpdateProcedureType() {
		IAbstractArtifactInternal artifact = getArtifactManager()
				.getArtifactByFullyQualifiedName(getFullyQualifiedName(), true,
						new NullProgressMonitor());
		if ((artifact != null)
				&& (artifact instanceof IUpdateProcedureArtifact))
			return true;
		return false;
	}

	/**
	 * Returns true if this type corresponds to a ManagedEntityArtifact
	 * 
	 * @author Eric Dillon
	 * 
	 */
	public boolean isEntityType() {
		IAbstractArtifactInternal artifact = getArtifactManager()
				.getArtifactByFullyQualifiedName(getFullyQualifiedName(), true,
						new NullProgressMonitor());
		if ((artifact != null) && (artifact instanceof IManagedEntityArtifact))
			return true;
		return false;
	}

	/**
	 * Returns true if this type corresponds to a TSException
	 * 
	 * @author Richard Craddock
	 * 
	 */
	public boolean isTSException() {
		IAbstractArtifactInternal artifact = getArtifactManager()
				.getArtifactByFullyQualifiedName(getFullyQualifiedName(), true,
						new NullProgressMonitor());
		if ((artifact != null) && (artifact instanceof IExceptionArtifact))
			return true;
		return false;
	}

	/**
	 * Returns true if this type corresponds to a Enumeration
	 * 
	 * @author Richard Craddock
	 * 
	 */
	public boolean isEnum() {
		IAbstractArtifactInternal artifact = getArtifactManager()
				.getArtifactByFullyQualifiedName(getFullyQualifiedName(), true,
						new NullProgressMonitor());
		if ((artifact != null) && (artifact instanceof IEnumArtifact))
			return true;
		else
			return false;
	}

	public boolean isExtensibleEnum() {
		IAbstractArtifactInternal artifact = getArtifactManager()
				.getArtifactByFullyQualifiedName(getFullyQualifiedName(), true,
						new NullProgressMonitor());
		if ((artifact != null) && (artifact instanceof IEnumArtifact)) {
			EnumArtifact en = (EnumArtifact) artifact;
			return en.getExtensible();
		} else
			return false;
	}

	public Type getBaseType() {
		IAbstractArtifactInternal artifact = getArtifactManager()
				.getArtifactByFullyQualifiedName(getFullyQualifiedName(), true,
						new NullProgressMonitor());
		if (artifact instanceof EnumArtifact) {
			EnumArtifact en = (EnumArtifact) artifact;
			return en.getBaseType();
		} else
			return new Type(artifact.getFullyQualifiedName(), EMultiplicity.ZERO_ONE,
					getArtifactManager());
	}

	/**
	 * Returns true if this type is an array
	 * 
	 */
	public boolean isArray() {
		return this.getTypeMultiplicity().isArray();
		//return !"".equals(getDimensions());
	}

	// ================================================================
	// Methods to satisfy the IType interface

	public int getMultiplicity() {
/*		if ("".equals(this.dimensions))
			return IType.MULTIPLICITY_SINGLE;
		else
			return IType.MULTIPLICITY_MULTI;*/
		return 0;
	}

	public void setMultiplicity(int multiplicity) {
/*		switch (multiplicity) {
		case IType.MULTIPLICITY_SINGLE:
			this.dimensions = "";
			break;
		default:
			this.dimensions = "[]";
			break;
		}*/
	}

	public String defaultValue() {
		String result = "";
		if (!getTypeMultiplicity().isArray()){
			String type = getFullyQualifiedName();
			if ("java.lang.String".equals(type)) {
				result = "\"Value\"";
			} else if ("String".equals(type)) {
				result = "\"Value\"";
			} else if ("int".equals(type)) {
				result = "0";
			}
		}

		return result;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((fullyQualifiedName == null) ? 0 : fullyQualifiedName
						.hashCode());
		result = prime * result
				+ ((multiplicity == null) ? 0 : multiplicity.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Type other = (Type) obj;
		if (fullyQualifiedName == null) {
			if (other.fullyQualifiedName != null)
				return false;
		} else if (!fullyQualifiedName.equals(other.fullyQualifiedName))
			return false;
		if (multiplicity != other.multiplicity)
			return false;
		return true;
	}

	public IAbstractArtifactInternal getArtifact() {
		IAbstractArtifactInternal artifact = getArtifactManager()
				.getArtifactByFullyQualifiedName(getFullyQualifiedName(), true,
						new NullProgressMonitor());
		return artifact;
	}

	/*
	 * Used to test the type name to ensure that it is a legal Java type. In
	 * this case, use the list of primitive types defined in the active profile
	 * as the list of legal primitive types (excluding the reserved types).
	 */
	private boolean isPrimitiveProfileType() {
		Collection<IPrimitiveTypeDef> primitiveTypeDefs = TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile()
				.getPrimitiveTypeDefs(true);
		for (IPrimitiveTypeDef primitive: primitiveTypeDefs) {
			String typeDefPackageName = primitive.getPackageName();
			String typeDefName = primitive.getName();
			if (typeDefPackageName.equals("<reserved>")
					&& this.fullyQualifiedName.equals(typeDefName))
				return true;
			else if (this.fullyQualifiedName.equals(typeDefPackageName + "."
					+ typeDefName))
				return true;
		}
		return false;
	}

	public IStatus validate() {
		return this.validate(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * used to validate the Type when saving it to the underlying data model.
	 * The additional argument (isMethodReturnCheck) is used to indicate that
	 * the type being checked is a return type for a method so the additional
	 * valid value of "void" should also be allowed
	 * 
	 * @see org.eclipse.tigerstripe.api.artifacts.model.ILiteral#validate()
	 */
	public IStatus validate(boolean isMethodReturnCheck) {

		MultiStatus result = new MultiStatus(BasePlugin.getPluginId(), 222,
				"Type validation", null);

		// check the type name; it should either be a primitive profile type or
		// a valid
		// class name otherwise the name cannot be used as a legal type
		String typeName = getName();
		if (isPrimitiveProfileType()
				|| (isMethodReturnCheck && typeName.equals("void"))) {
			;
		} else if (!TigerstripeValidationUtils.classNamePattern.matcher(
				typeName).matches()
				&& !TigerstripeValidationUtils.elementNamePattern.matcher(
						typeName).matches()) {
			result.add(new Status(IStatus.ERROR, BasePlugin.getPluginId(), "'"
					+ typeName + "' is not a legal type name"));
		}
		// check label name to ensure it is not a reserved keyword
		else if (TigerstripeValidationUtils.keywordList.contains(typeName)) {
			result
					.add(new Status(
							IStatus.ERROR,
							BasePlugin.getPluginId(),
							"'"
									+ typeName
									+ "' is a reserved keyword and cannot be used as type name"));
		}
		// check the type package to ensure that it is a legal package name
		String packageName = Util.packageOf(this.getFullyQualifiedName());
		if (!packageName.equals("")
				&& !TigerstripeValidationUtils.packageNamePattern.matcher(
						packageName).matches())
			result.add(new Status(IStatus.ERROR, BasePlugin.getPluginId(), "'"
					+ packageName + "' is not a legal package name"));
		else if (TigerstripeValidationUtils.keywordList.contains(packageName)) {
			result
					.add(new Status(
							IStatus.ERROR,
							BasePlugin.getPluginId(),
							"'"
									+ packageName
									+ "' is a reserved keyword and cannot be used as package name"));
		}

		return result;

	}

	@Override
	public IType clone() {
		IType result = new Type(getArtifactManager());
		result.setFullyQualifiedName(getFullyQualifiedName());
		result.setTypeMultiplicity(getTypeMultiplicity());

		return result;
	}
}