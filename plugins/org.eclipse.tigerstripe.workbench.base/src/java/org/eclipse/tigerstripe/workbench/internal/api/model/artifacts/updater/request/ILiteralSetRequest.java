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
 * Literal Set request
 * 
 * @author Eric Dillon
 * 
 */
public interface ILiteralSetRequest extends IModelChangeRequest {

	public final static String COMMENT_FEATURE = "comment";
	public final static String NAME_FEATURE = "name";
	public final static String VALUE_FEATURE = "value";
	public final static String VISIBILITY_FEATURE = "visibility";
	public final static String TYPE_FEATURE = "type";

	public void setArtifactFQN(String artifactFQN);

	public String getArtifactFQN();

	public void setLiteralName(String attributeName);

	public void setNewValue(String value);

	public void setOldValue(String value);

	public void setFeatureId(String featureId);
}
