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
package org.eclipse.tigerstripe.api.profile.stereotype;

import org.eclipse.tigerstripe.api.external.profile.stereotype.IextStereotypeScopeDetails;

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
public interface IStereotypeScopeDetails extends IextStereotypeScopeDetails {

	public void setAttributeLevel(boolean isAttributeLevel);

	public void setMethodLevel(boolean isMethodLevel);

	public void setLabelLevel(boolean isLabelLevel);

	public void setArgumentLevel(boolean isArgumentLevel);

	public void setArtifactLevelTypes(String[] types);
}
