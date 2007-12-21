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
package org.eclipse.tigerstripe.api.external.profile.stereotype;

/**
 * The details of applicability scope for an Stereotype definition.
 * 
 * A Stereotype can apply to any (and/or) of the following scope levels :
 * <ul>
 * <li><b>Artifact Level</b>: getArtifactLevelTypes() returns an array of
 * artifact types for which this stereotype can be applied at the artifact
 * level.</li>
 * <li><b>Artifact Attribute Level</b>: when the stereotype can be applied on
 * any artifact attribute, regardless of the type of the containing artifact.</li>
 * <li><b>Artifact Method Level</b>: when the stereotype can be applied on an
 * y artifact methods, regardless of the type of the containing artifact</li>
 * <li><b>Artifact Label Level</b>: when the stereotype can be applied on any
 * artifact label, regardless of the type of the containing artifact</li>
 * <li><b>Method Argument Level</b>: when the stereotype can be applied on any
 * method argument, regardless of the type of the containing artifact</li>
 * </ul>
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IextStereotypeScopeDetails {

	/**
	 * Return true if the stereotype can be applied at attribute (field) level.
	 * 
	 * @return true if applicable at attribute level
	 */
	public boolean isAttributeLevel();

	/**
	 * Return true if the stereotype can be applied at method level.
	 * 
	 * @return true if applicable at method level
	 */
	public boolean isMethodLevel();

	/**
	 * Return true if the stereotype can be applied at label level.
	 * 
	 * @return true if applicable at lebel level
	 */
	public boolean isLabelLevel();

	/**
	 * Return true if the stereotype can be applied at argument level.
	 * 
	 * @return true if applicable at argument level
	 */
	public boolean isArgumentLevel();

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
}
