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
import java.net.URI;

import org.eclipse.tigerstripe.api.external.TigerstripeException;

/**
 * Top level handle for any Tigerstripe project
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IextAbstractTigerstripeProject {

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

	/**
	 * @return true if the project exists (i.e. a valid project descriptor was
	 *         found in the handle)
	 * 
	 */
	public boolean exists();

	public URI getURI();

	public IextProjectDetails getIextProjectDetails()
			throws TigerstripeException;

	/**
	 * Base directory for the project
	 * 
	 * @return
	 */
	public File getBaseDir();

}
