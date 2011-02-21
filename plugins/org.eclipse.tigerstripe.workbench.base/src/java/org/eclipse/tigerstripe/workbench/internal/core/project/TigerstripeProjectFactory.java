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
import java.net.URISyntaxException;
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
import org.eclipse.tigerstripe.workbench.internal.api.impl.ProjectSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.api.project.IPhantomTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM0GeneratorProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM1GeneratorProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * A Factory for all Project related stuff
 * 
 * This includes a cache for project handles.
 * 
 * This is a singleton that listens to workspace changes to make sure the cache
 * is updated.
 * 
 * @author erdillon
 * 
 */
public class TigerstripeProjectFactory {

	public final static TigerstripeProjectFactory INSTANCE = new TigerstripeProjectFactory();

	private ProjectSessionImpl session = new ProjectSessionImpl();

	@SuppressWarnings("unchecked")
	private final static Class[] SUPPORTED_PROJECT_TYPES = {
			ITigerstripeModelProject.class,
			ITigerstripeM1GeneratorProject.class,
			ITigerstripeM0GeneratorProject.class };

	private final static IProjectCreator[] PROJECT_CREATORS = {
			new ModelProjectCreator(), new PluginProjectCreator(),
			new M0GeneratorProjectCreator() };

	@SuppressWarnings("unchecked")
	public Collection<Class> getSupportedProjectTypes() {
		return Arrays.asList(SUPPORTED_PROJECT_TYPES);
	}

	/**
	 * Creates a project at the location provided thru the URI.
	 * 
	 * @param projectName
	 *            - name for the project to be created. This corresponds to the
	 *            dir where the project will be created.
	 * @param projectDetails
	 *            - additional details to be used to pre-populate the project
	 *            descriptor upon creation.
	 * @param path
	 *            - the location where the project shall be created
	 * @param projectType
	 * @param properties
	 *            - properties to use for project creation
	 * @param monitor
	 * @return
	 * @throws TigerstripeException
	 */
	@SuppressWarnings("unchecked")
	public IAbstractTigerstripeProject createProject(String projectName,
			IProjectDetails projectDetails, IPath path, Class projectType,
			Map<String, Object> properties, IProgressMonitor monitor)
			throws TigerstripeException {

		IProjectCreator creator = assertKnownProjectType(projectType);
		creator.assertValidProperties(properties);

		try {
			IWorkspaceRunnable runnable = creator.getRunnable(projectName,
					projectDetails, path, properties);

			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			workspace.run(runnable, monitor);

			return findProjectOrCreate(workspace.getRoot().findMember(projectName)
					.getLocation());
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
	public IAbstractTigerstripeProject findProjectOrCreate(IPath path)
			throws TigerstripeException {
		return session.makeTigerstripeProject(getProjectURI(path));
	}

	public IAbstractTigerstripeProject findProject(IPath path)
			throws TigerstripeException {
		return session.getProject(getProjectURI(path));
	}
	
	public URI getProjectURI(IPath path) throws TigerstripeException {
		try {
			path = path.addTrailingSeparator();
			return new URI("file", null, path.toString(), null);
		} catch (URISyntaxException e) {
			throw new TigerstripeException(String.format("Unable to determine URI for the project path '%s'", path), e);
		}
	}
	
	
	/**
	 * As a result of a project being deleted we need to remove its entry in the
	 * cache
	 * 
	 * @param projectPath
	 * @throws TigerstripeException 
	 */
	public void deleteProject(IPath projectPath) throws TigerstripeException {
		session.removeProject(getProjectURI(projectPath));
	}

	public IPhantomTigerstripeProject getPhantomProject()
			throws TigerstripeException {
		return session.getPhantomProject();
	}

	// FIXME this is temporary until the project session disappears.
	public ProjectSessionImpl getProjectSession() {
		return this.session;
	}
}
