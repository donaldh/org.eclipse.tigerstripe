/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.project;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.InternalTigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.plugins.pluggable.IPluggablePluginProject;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

/**
 * A Factory for all Project related stuff
 * 
 * @author erdillon
 * 
 */
public class TigerstripeProjectFactory {

	public final static TigerstripeProjectFactory INSTANCE = new TigerstripeProjectFactory();

	private TigerstripeProjectFactory() {

	}

	@SuppressWarnings("unchecked")
	private final static Class[] SUPPORTED_PROJECT_TYPES = {
			ITigerstripeProject.class, IPluggablePluginProject.class };

	private final static IProjectCreator[] PROJECT_CREATORS = {
			new ModelProjectCreator(), new PluginProjectCreator() };

	@SuppressWarnings("unchecked")
	public Collection<Class> getSupportedProjectTypes() {
		return Arrays.asList(SUPPORTED_PROJECT_TYPES);
	}

	/**
	 * Creates a project at the location provided thru the URI.
	 * 
	 * @param folder
	 * @param projectType
	 * @param properties -
	 *            properties to use for project creation
	 * @param monitor
	 * @return
	 * @throws TigerstripeException
	 */
	@SuppressWarnings("unchecked")
	public IAbstractTigerstripeProject createProject(
			IProjectDetails projectDetails, IPath path, Class projectType,
			Map<String, Object> properties, IProgressMonitor monitor)
			throws TigerstripeException {

		IProjectCreator creator = assertKnownProjectType(projectType);
		creator.assertValidProperties(properties);

		try {
			IWorkspaceRunnable runnable = creator.getRunnable(projectDetails,
					path, properties);

			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			workspace.run(runnable, monitor);

			return findProject(workspace.getRoot().findMember(
					projectDetails.getName()).getLocation());
		} catch (CoreException e) {
			throw new TigerstripeException(
					"An error occured while trying to create project: "
							+ e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Asserts that the given project type is known.
	 * 
	 * @param projectType
	 * @throws TigerstripeException
	 */
	@SuppressWarnings("unchecked")
	private IProjectCreator assertKnownProjectType(Class projectType)
			throws TigerstripeException {
		for (int index = 0; index < SUPPORTED_PROJECT_TYPES.length; index++) {
			Class knownType = SUPPORTED_PROJECT_TYPES[index];
			if (knownType == projectType) {
				return PROJECT_CREATORS[index];
			}
		}
		throw new TigerstripeException("Unknown Project Type: "
				+ projectType.getCanonicalName());
	}

	/**
	 * Returns a handle on the project as found at the given path.
	 * 
	 * Note that a caching mechanism is used here to speed the process.
	 * 
	 * @param path
	 * @return
	 * @throws TigerstripeException
	 */
	public IAbstractTigerstripeProject findProject(IPath path)
			throws TigerstripeException {
		URI uri = path.toFile().toURI();
		return InternalTigerstripeCore.getDefaultProjectSession()
				.makeTigerstripeProject(uri);
	}
}
