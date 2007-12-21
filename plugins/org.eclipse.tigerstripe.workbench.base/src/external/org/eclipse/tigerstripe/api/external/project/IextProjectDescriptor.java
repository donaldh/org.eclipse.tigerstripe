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

import java.io.File;

import org.eclipse.tigerstripe.api.external.IextPluginReference;
import org.eclipse.tigerstripe.api.external.TigerstripeException;

/**
 * Interface representing a tigerstripe.xml project descriptor.
 * 
 * @author Eric Dillon
 * 
 */
public interface IextProjectDescriptor {

	/**
	 * The project label is a contextual name use to identify the project.
	 * Typically, within most IDEs projects have a local name within the
	 * workspace, this is it!
	 * 
	 * Internally, an IProjectLocator facility is used to determine the value
	 * 
	 * @return
	 */
	public String getProjectLabel();

	public IextProjectDetails getIextProjectDetails()
			throws TigerstripeException;

	/**
	 * Base directory for the project
	 * 
	 * @return
	 */
	public File getBaseDir();

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
