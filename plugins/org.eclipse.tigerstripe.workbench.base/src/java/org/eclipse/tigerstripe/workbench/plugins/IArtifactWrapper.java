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
package org.eclipse.tigerstripe.workbench.plugins;

import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;

/**
 * This is the interface that needs to be implemented by model classess attached
 * the an ArtifactBased rule run
 * 
 * 
 * @author Richard Craddock
 * 
 */
public interface IArtifactWrapper {

	/**
	 * Initializes the artifact for this wrapper.
	 * 
	 * This method is called once right after creation of this object. The
	 * argument corresponds to the current artifact being handled with the
	 * context of an ArtifactBased rule.
	 * 
	 * @param artifact -
	 *            IArtifact the artifact to initialize the wrapper with
	 */
	public void setIArtifact(IAbstractArtifact artifact);

	/**
	 * Initializes the plugin ref for this wrapper.
	 * 
	 * This method is called once right after creation of this object. The
	 * argument corresponds to the current plugin reference.
	 * 
	 * @param pluginConfig -
	 *            The current plugin Reference
	 */
	public void setPluginConfig(IPluginConfig pluginConfig);
}
