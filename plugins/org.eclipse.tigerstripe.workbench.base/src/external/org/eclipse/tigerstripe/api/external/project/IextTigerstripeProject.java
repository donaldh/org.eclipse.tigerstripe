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
package org.eclipse.tigerstripe.api.external.project;

import org.eclipse.tigerstripe.api.external.IextArtifactManagerSession;
import org.eclipse.tigerstripe.api.external.IextPluginReference;
import org.eclipse.tigerstripe.api.external.TigerstripeException;

/**
 * Handle on a Tigerstripe Project
 * 
 * @author Eric Dillon
 * @since 0.3
 */
public interface IextTigerstripeProject extends IextAbstractTigerstripeProject {

	/**
	 * Returns the artifact manager session for this Tigerstripe project.
	 * 
	 * @return
	 */
	public IextArtifactManagerSession getIextArtifactManagerSession()
			throws TigerstripeException;

	/**
	 * Returns the Plugin references defined in this project
	 * 
	 * @return
	 */
	public IextPluginReference[] getIextPluginReferences()
			throws TigerstripeException;

	// ==========================================
	// Project references are direct references to other projects that are
	// required
	// for the local project to build properly.
	public IextTigerstripeProject[] getIextReferencedProjects()
			throws TigerstripeException;

	// ==========================================
	// Facility for Advanced Properties access/configuration
	public String getAdvancedProperty(String property)
			throws TigerstripeException;

	public String getAdvancedProperty(String property, String defaultValue)
			throws TigerstripeException;
}
