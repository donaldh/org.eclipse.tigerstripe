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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.tigerstripe.workbench.API;
import org.eclipse.tigerstripe.workbench.internal.api.utils.TigerstripeError;
import org.eclipse.tigerstripe.workbench.internal.api.utils.TigerstripeErrorLevel;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeValidationUtils;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.model.IType;
import org.eclipse.tigerstripe.workbench.model.IAssociationEnd.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.profile.primitiveType.IPrimitiveTypeDef;

/**
 * @author Eric Dillon
 * 
 * This represents a java type.
 */
public class Type implements IType {

	private ArtifactManager artifactManager;

	private String fullyQualifiedName;

	private String dimensions;

	private EMultiplicity multiplicity;

	public String getFullyQualifiedName() {
		return this.fullyQualifiedName;
	}

	public String getPackage() {
		return Util.packageOf(this.fullyQualifiedName);
	}

	public EMultiplicity getTypeMultiplicity() {
		return this.multiplicity;
	}

	public void setTypeMultiplicity(EMultiplicity multiplicity) {
		this.multiplicity = multiplicity;

		// for compatibility reason, we need to set the dimension attribute to
		// the closest match
		if (multiplicity == EMultiplicity.ONE
				|| multiplicity == EMultiplicity.ZERO_ONE
				|| multiplicity == EMultiplicity.ZERO) {
			dimensions = dimensionsAsString(MULTIPLICITY_SINGLE);
		} else {
			dimensions = dimensionsAsString(MULTIPLICITY_MULTI);
		}
	}

	public String getName() {
		return Util.nameOf(getFullyQualifiedName());
	}

	public void setFullyQualifiedName(String name) {
		this.fullyQualifiedName = name;
	}

	public String getDimensions() {
		return this.dimensions;
	}

	public ArtifactManager getArtifactManager() {
		return this.artifactManager;
	}

	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;

		// For compatibility reasons, we need to update the type multiplicity
		if (dimensionsAsString(MULTIPLICITY_MULTI).equals(dimensions)) {
			multiplicity = EMultiplicity.ZERO_STAR;
		} else {
			multiplicity = EMultiplicity.ZERO_ONE;
		}
	}

	public Type(ArtifactManager artifactMgr) {
		this("", EMultiplicity.ZERO_ONE, artifactMgr);
	}

	public Type(String fullyQualifiedName, EMultiplicity typeMultiplicity,
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
	@Deprecated
	public Type(String fullyQualifiedName, String dimensions,
			ArtifactManager artifactMgr) {
		setFullyQualifiedName(fullyQualifiedName);
		setDimensions(dimensions);
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
	@Deprecated
	public Type(String fullyQualifiedName, int dimensions,
			ArtifactManager artifactMgr) {
		this(fullyQualifiedName, dimensionsAsString(dimensions), artifactMgr);
	}

	/**
	 * 
	 * @param dimensions
	 * @return
	 * @deprecated since 2.2-rc, no use of dimensions anymore, multiplicity
	 *             instead
	 */
	@Deprecated
	private static String dimensionsAsString(int dimensions) {
		String result = "";
		for (int i = 0; i < dimensions; i++) {
			result = result + "[]";
		}

		return result;
	}

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
		AbstractArtifact artifact = getArtifactManager()
				.getArtifactByFullyQualifiedName(getFullyQualifiedName(), true,
						new TigerstripeNullProgressMonitor());
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
		AbstractArtifact artifact = getArtifactManager()
				.getArtifactByFullyQualifiedName(getFullyQualifiedName(), true,
						new TigerstripeNullProgressMonitor());
		if ((artifact != null) && (artifact instanceof DatatypeArtifact))
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
		AbstractArtifact artifact = getArtifactManager()
				.getArtifactByFullyQualifiedName(getFullyQualifiedName(), true,
						new TigerstripeNullProgressMonitor());
		if ((artifact != null) && (artifact instanceof QueryArtifact))
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
		AbstractArtifact artifact = getArtifactManager()
				.getArtifactByFullyQualifiedName(getFullyQualifiedName(), true,
						new TigerstripeNullProgressMonitor());
		if ((artifact != null) && (artifact instanceof UpdateProcedureArtifact))
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
		AbstractArtifact artifact = getArtifactManager()
				.getArtifactByFullyQualifiedName(getFullyQualifiedName(), true,
						new TigerstripeNullProgressMonitor());
		if ((artifact != null) && (artifact instanceof ManagedEntityArtifact))
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
		AbstractArtifact artifact = getArtifactManager()
				.getArtifactByFullyQualifiedName(getFullyQualifiedName(), true,
						new TigerstripeNullProgressMonitor());
		if ((artifact != null) && (artifact instanceof ExceptionArtifact))
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
		AbstractArtifact artifact = getArtifactManager()
				.getArtifactByFullyQualifiedName(getFullyQualifiedName(), true,
						new TigerstripeNullProgressMonitor());
		if ((artifact != null) && (artifact instanceof EnumArtifact))
			return true;
		else
			return false;
	}

	public boolean isExtensibleEnum() {
		AbstractArtifact artifact = getArtifactManager()
				.getArtifactByFullyQualifiedName(getFullyQualifiedName(), true,
						new TigerstripeNullProgressMonitor());
		if ((artifact != null) && (artifact instanceof EnumArtifact)) {
			EnumArtifact en = (EnumArtifact) artifact;
			return en.getExtensible();
		} else
			return false;
	}

	public Type getBaseType() {
		AbstractArtifact artifact = getArtifactManager()
				.getArtifactByFullyQualifiedName(getFullyQualifiedName(), true,
						new TigerstripeNullProgressMonitor());
		if (artifact instanceof EnumArtifact) {
			EnumArtifact en = (EnumArtifact) artifact;
			return en.getBaseType();
		} else
			return new Type(artifact.getFullyQualifiedName(), 0,
					getArtifactManager());
	}

	/**
	 * Returns true if this type is an array
	 * 
	 */
	public boolean isArray() {
		return !"".equals(getDimensions());
	}

	// ================================================================
	// Methods to satisfy the IType interface

	public int getMultiplicity() {
		if ("".equals(this.dimensions))
			return IType.MULTIPLICITY_SINGLE;
		else
			return IType.MULTIPLICITY_MULTI;
	}

	public void setMultiplicity(int multiplicity) {
		switch (multiplicity) {
		case IType.MULTIPLICITY_SINGLE:
			this.dimensions = "";
			break;
		default:
			this.dimensions = "[]";
			break;
		}
	}

	public String defaultValue() {
		String result = "";
		switch (getMultiplicity()) {
		case IType.MULTIPLICITY_SINGLE:
			String type = getFullyQualifiedName();
			if ("java.lang.String".equals(type)) {
				result = "\"Value\"";
			} else if ("String".equals(type)) {
				result = "\"Value\"";
			} else if ("int".equals(type)) {
				result = "0";
			}
			break;
		}

		return result;
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof Type) {
			Type other = (Type) arg0;
			return getFullyQualifiedName()
					.equals(other.getFullyQualifiedName());
		} else
			return false;
	}

	public AbstractArtifact getArtifact() {
		AbstractArtifact artifact = getArtifactManager()
				.getArtifactByFullyQualifiedName(getFullyQualifiedName(), true,
						new TigerstripeNullProgressMonitor());
		return artifact;
	}

	public IAbstractArtifact getIArtifact() {
		return getArtifact();
	}

	/*
	 * Used to test the type name to ensure that it is a legal Java type. In
	 * this case, use the list of primitive types defined in the active profile
	 * as the list of legal primitive types (excluding the reserved types).
	 */
	private boolean isPrimitiveProfileType() {
		IPrimitiveTypeDef[] primitiveTypeDefs = API
				.getIWorkbenchProfileSession().getActiveProfile()
				.getPrimitiveTypeDefs(true);
		for (int i = 0; i < primitiveTypeDefs.length; i++) {
			String typeDefPackageName = primitiveTypeDefs[i].getPackageName();
			String typeDefName = primitiveTypeDefs[i].getName();
			if (typeDefPackageName.equals("<reserved>")
					&& this.fullyQualifiedName.equals(typeDefName))
				return true;
			else if (this.fullyQualifiedName.equals(typeDefPackageName + "."
					+ typeDefName))
				return true;
		}
		return false;
	}

	public List<TigerstripeError> validate() {
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
	 * @see org.eclipse.tigerstripe.api.artifacts.model.ILabel#validate()
	 */
	public List<TigerstripeError> validate(boolean isMethodReturnCheck) {

		List<TigerstripeError> errors = new ArrayList();

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
			errors.add(new TigerstripeError(TigerstripeErrorLevel.ERROR, "'"
					+ typeName + "' is not a legal type name"));
		}
		// check label name to ensure it is not a reserved keyword
		else if (TigerstripeValidationUtils.keywordList.contains(typeName)) {
			errors
					.add(new TigerstripeError(
							TigerstripeErrorLevel.ERROR,
							"'"
									+ typeName
									+ "' is a reserved keyword and cannot be used as type name"));
		}
		// check the type package to ensure that it is a legal package name
		String packageName = Util.packageOf(this.getFullyQualifiedName());
		if (!packageName.equals("")
				&& !TigerstripeValidationUtils.packageNamePattern.matcher(
						packageName).matches())
			errors.add(new TigerstripeError(TigerstripeErrorLevel.ERROR, "'"
					+ packageName + "' is not a legal package name"));
		else if (TigerstripeValidationUtils.keywordList.contains(packageName)) {
			errors
					.add(new TigerstripeError(
							TigerstripeErrorLevel.ERROR,
							"'"
									+ packageName
									+ "' is a reserved keyword and cannot be used as package name"));
		}

		return errors;

	}

	@Override
	public IType clone() {
		IType result = new Type(getArtifactManager());
		result.setFullyQualifiedName(getFullyQualifiedName());
		result.setTypeMultiplicity(getTypeMultiplicity());

		return result;
	}
}