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
package org.eclipse.tigerstripe.api.project;

import java.net.URI;

import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.TigerstripeLicenseException;
import org.eclipse.tigerstripe.api.external.project.IextProjectSession;
import org.eclipse.tigerstripe.api.utils.ITigerstripeProgressMonitor;

/**
 * Session facade to manage Tigerstripe projects.
 * 
 * @author Eric Dillon
 * @since 0.3
 */
public interface IProjectSession extends IextProjectSession {

	/**
	 * Returns a list of supported project types
	 */
	public String[] getSupportedTigerstripeProjects();

	/**
	 * Creates a handle on a new Tigerstripe Project for the specified Project
	 * Type.
	 * 
	 * @param projectURI
	 * @param projectType -
	 *            ISimplePluggablePluginProject.class.getName() to create a
	 *            Plugin Project. otherwise a Genering TigerstripeOssjProject is
	 *            created by default
	 * @throws TigerstripeLicenseException
	 *             if the current license doesn't allow this operation
	 * @throws UnsupportedOperationException
	 *             if the projectType is not supported
	 * @throws org.eclipse.tigerstripe.api.external.TigerstripeException
	 *             if any other error occured.
	 * @return
	 */
	public IAbstractTigerstripeProject makeTigerstripeProject(URI projectURI,
			String projectType) throws TigerstripeLicenseException,
			UnsupportedOperationException, TigerstripeException;

	public IDependency makeIDependency(String absolutePath)
			throws TigerstripeException;

	/**
	 * Returns a handle on the Phantom project
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	public IPhantomTigerstripeProject getPhantomProject()
			throws TigerstripeException;

	public void refreshCacheFor(URI uri,
			IAbstractTigerstripeProject workingHandle,
			ITigerstripeProgressMonitor monitor) throws TigerstripeException;

	public void removeFromCache(IAbstractTigerstripeProject project);
}