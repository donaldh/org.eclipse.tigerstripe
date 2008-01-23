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
package org.eclipse.tigerstripe.workbench.profile.stereotype;


/**
 * The details of applicability scope for an Stereotype definition.
 * 
 * A Stereotype can apply to any (and/or) of the following scope levels :
 * <ul>
 * <li><b>Artifact Level</b>: when the stereotype can be applied on an
 * artifact. Please note that if getArtifactTypes() returns null, the stereotype
 * can be applied to any Artifact Type. Otherwise, getArtifactTypes() returns an
 * array of artifact types that this is applicable to only.</li>
 * <li><b>Artifact Attribute Level</b>: when the stereotype can be applied on
 * an artifact attribute.</li>
 * <li><b>Artifact Method Level</b>: when the stereotype can be applied on an
 * artifact methods</li>
 * <li><b>Artifact Label Level</b>: when the stereotype can be applied on an
 * artifact label</li>
 * <li><b>Method Argument Level</b>: when the stereotype can be applied on a
 * method argument</li>
 * </ul>
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IStereotypeScopeDetails {

	public void setAttributeLevel(boolean isAttributeLevel);

	public void setMethodLevel(boolean isMethodLevel);

	public void setLabelLevel(boolean isLabelLevel);

	public void setArgumentLevel(boolean isArgumentLevel);

	public void setArtifactLevelTypes(String[] types);

	/**
	 * Returns an array of artifact types for which this stereotype can be
	 * applied at the artifact level. The elements of the array are class names
	 * of artifacttypes.
	 * 
	 * An empty array is returned if this sterteotype is not applicable for any
	 * artifacts.
	 * 
	 * @return
	 */
	public String[] getArtifactLevelTypes();

	/**
	 * Return true if the stereotype can be applied at argument level.
	 * 
	 * @return true if applicable at argument level
	 */
	public boolean isArgumentLevel();

	/**
	 * Return true if the stereotype can be applied at attribute (field) level.
	 * 
	 * @return true if applicable at attribute level
	 */
	public boolean isAttributeLevel();

	/**
	 * Return true if the stereotype can be applied at label level.
	 * 
	 * @return true if applicable at lebel level
	 */
	public boolean isLabelLevel();

	/**
	 * Return true if the stereotype can be applied at method level.
	 * 
	 * @return true if applicable at method level
	 */
	public boolean isMethodLevel();
}
