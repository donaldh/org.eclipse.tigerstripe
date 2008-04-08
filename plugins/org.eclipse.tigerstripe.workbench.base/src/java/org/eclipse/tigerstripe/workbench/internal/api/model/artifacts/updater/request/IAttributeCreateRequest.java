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
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;

/**
 * Artifact creation request
 * 
 * @author Eric Dillon
 * 
 */
public interface IAttributeCreateRequest extends IModelChangeRequest {

	public void setArtifactFQN(String artifactFQN);

	public String getArtifactFQN();

	public void setAttributeName(String name);

	public void setAttributeType(String type);

	public void setAttributeMultiplicity(String multiplicity);
	
	public void setField(IField field);
}
