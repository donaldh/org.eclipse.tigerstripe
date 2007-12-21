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
package org.eclipse.tigerstripe.api.external.plugins;

import org.eclipse.tigerstripe.api.external.model.artifacts.IArtifact;

/**
 * This is the interface that needs to be implemented by model classess attached
 * the an ArtifactBased rule run
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IArtifactModel {

	/**
	 * Initializes the artifact for this model.
	 * 
	 * This method is called once right after creation of this object. The
	 * argument corresponds to the current artifact being handled with the
	 * context of an ArtifactBased rule.
	 * 
	 * @param artifact -
	 *            IArtifact the artifact to initialize the model with
	 */
	public void setIArtifact(IArtifact artifact);
}
