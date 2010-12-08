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
package org.eclipse.tigerstripe.workbench.internal.api.model;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;

/**
 * This interface provides access to all Artifact metadata.
 * At this point it holds all artifact types/labels for all registered artifacts.
 * 
 * <b>History of changes</b> (Name: Modification): <br/>
 * Eric Dillon : Initial Creation <br/>
 * Navid Mehregani: Bug 329229 - [Form Editor] In some cases selected artifact type is not persisted for a generator descriptor <br/>
 */
public interface IArtifactMetadataSession {

	public String[] getSupportedArtifactTypeLabels();
	
	/**
	 * @return  Returns artifact type for the given artifact label
	 */
	public String getArtifactType(String artifactLabel);
	
	/**
	 * @return  Returns artifact label for the given artifact type
	 */
	public String getArtifactLabel(String artifactLabel);

	public void registerArtifactType(IAbstractArtifact artifactModel) throws TigerstripeException;

	public void unRegisterArtifactType(IAbstractArtifact artifactModel) throws TigerstripeException;
}
