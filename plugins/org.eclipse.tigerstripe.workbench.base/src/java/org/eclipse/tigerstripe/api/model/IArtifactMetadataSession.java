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
package org.eclipse.tigerstripe.api.model;

import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.model.artifacts.IAbstractArtifact;

/**
 * This interface provides access to all Artifact metadata
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IArtifactMetadataSession {

	public String[] getSupportedArtifactTypeLabels();

	public String[] getSupportedArtifactTypes();

	public void registerArtifactType(IAbstractArtifact artifactModel)
			throws TigerstripeException;

	public void unRegisterArtifactType(IAbstractArtifact artifactModel)
			throws TigerstripeException;
}
