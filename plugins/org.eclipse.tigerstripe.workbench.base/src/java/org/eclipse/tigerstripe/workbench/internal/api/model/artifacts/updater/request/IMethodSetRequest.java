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
public interface IMethodSetRequest extends IMethodChangeRequest {

	public final static String COMMENT_FEATURE = "comment";
	public final static String NAME_FEATURE = "name";
	public final static String TYPE_FEATURE = "type";
	public final static String MULTIPLICITY_FEATURE = "multiplicity";
	public final static String VISIBILITY_FEATURE = "visibility";
	public final static String ISABSTRACT_FEATURE = "isAbstract";
	public final static String ISUNIQUE_FEATURE = "isUnique";
	public final static String ISORDERED_FEATURE = "isOrdered";
	public final static String DEFAULTRETURNVALUE_FEATURE = "defaultReturnValue";
	public final static String ISVOID_FEATURE = "isVoid";
	public final static String RETURNNAME_FEATURE = "returnName";

	public void setArtifactFQN(String artifactFQN);

	public String getArtifactFQN();

	public void setNewValue(String newValue);

	public void setOldValue(String oldValue);

	public void setFeatureId(String featureId);
}
