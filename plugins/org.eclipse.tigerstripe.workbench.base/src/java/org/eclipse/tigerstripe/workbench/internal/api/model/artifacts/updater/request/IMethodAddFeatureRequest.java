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
package org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request;

import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequest;

/**
 * Artifact creation request
 * 
 * @author Eric Dillon
 * 
 */
public interface IMethodAddFeatureRequest extends IModelChangeRequest {

	public final static String EXCEPTIONS_FEATURE = "exception";
	public final static String ARGUMENTS_FEATURE = "arguments";
	public final static String ARGUMENT_NAME_FEATURE = "argument_name";
	public final static String ARGUMENT_TYPE_FEATURE = "argument_type";
	public final static String ARGUMENT_MULTIPLICITY_FEATURE = "argument_multiplicity";
	public final static String ARGUMENT_COMMENT_FEATURE = "argument_comment";
	public final static String ARGUMENT_DEFAULTVALUE_FEATURE = "argument_defaultValue";
	public final static String ARGUMENT_DIRECTION_FEATURE = "argument_direction";
	public final static String ARGUMENT_ISUNIQUE_FEATURE = "argument_isUnique";
	public final static String ARGUMENT_ISORDERED_FEATURE = "argument_isOrdered";

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
	
	public void setArgumentPosition(int position);
	
}
