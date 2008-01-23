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
package org.eclipse.tigerstripe.api.model.artifacts.updater.request;

import org.eclipse.tigerstripe.api.model.artifacts.updater.IModelChangeRequest;

/**
 * Artifact creation request
 * 
 * @author Eric Dillon
 * 
 */
public interface IMethodSetRequest extends IModelChangeRequest {

	public final static String NAME_FEATURE = "name";
	public final static String TYPE_FEATURE = "type";
	public final static String MULTIPLICITY_FEATURE = "multiplicity";
	public final static String VISIBILITY_FEATURE = "visibility";
	public final static String ISABSTRACT_FEATURE = "isAbstract";
	public final static String ISUNIQUE_FEATURE = "isUnique";
	public final static String ISORDERED_FEATURE = "isOrdered";
	public final static String DEFAULTRETURNVALUE_FEATURE = "defaultReturnValue";

	public void setArtifactFQN(String artifactFQN);

	public String getArtifactFQN();

	/**
	 * The method label to uniquely identify the target method BEFORE the change
	 * is applied (so the match can happen!).
	 * 
	 * @param methodLabel
	 */
	public void setMethodLabelBeforeChange(String methodLabel);

	public void setNewValue(String newValue);

	public void setOldValue(String oldValue);

	public void setFeatureId(String featureId);
}
