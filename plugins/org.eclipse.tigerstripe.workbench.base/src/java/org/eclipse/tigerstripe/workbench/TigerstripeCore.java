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
package org.eclipse.tigerstripe.workbench;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.TigerstripeRuntimeDetails;
import org.eclipse.tigerstripe.workbench.internal.api.impl.WorkbenchProfileSession;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeWorkspaceNotifier;
import org.eclipse.tigerstripe.workbench.internal.core.project.ProjectDetails;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProjectFactory;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfileSession;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * The entry point for all interactions with the Tigerstripe API This is a
 * singleton class that acts as the factory class for any Tigerstripe Project.
 * 
 * 
 * Throughout the Tigerstripe API, a set of patterns are used:
 * <ul>
 * <li>Factory methods for types. The API only provides access to Java
 * Interfaces. Factory methods for these types are provided whenever required.</li>
 * <li>Query-based interfaces. In various places for the API, opaque Query
 * objects are defined to be populated and run through the API to return a
 * resultset. Supported queries, factories are available where required.</li>
 * </ul>
 * 
 * The API contains 2 major entry points:
 * <ul>
 * <li>The <b>Project Session</b>: which provides a session facade to access all
 * project-related details about a workspace. It allows to access, create,
 * change Tigerstripe projects within the Eclipse workspace</li>
 * <li>The <b>Profile Session</b>: which provides a session facade to access the
 * active Tigerstripe profile but also to create/edit profiles.
 * </ul>
 * 
 * @author Eric Dillon
 * @since 0.3
 */
public class TigerstripeCore {

	private static IWorkbenchProfileSession workbenchProfileSession;

	protected TigerstripeCore() {
	}

	/**
	 * Returns the default IWorkbenchProfileSession (Singleton) which provides
	 * access to the Active Profile and to the create/edit additional profiles
	 * programmatically.
	 * 
	 * @return IWorkbenchProfileSession - the default IWorkbenchProfileSession
	 *         (Singleton)
	 */
	public static IWorkbenchProfileSession getWorkbenchProfileSession() {
		if (workbenchProfileSession == null) {
			workbenchProfileSession = new WorkbenchProfileSession();
			workbenchProfileSession.reloadActiveProfile();
		}

		return workbenchProfileSession;
	}

	/**
	 * Returns the runtime details for this install
	 * 
	 * @return
	 */
	public final static IRuntimeDetails getRuntimeDetails() {
		return TigerstripeRuntimeDetails.INSTANCE;
	}

	/**
	 * Returns a IAbstractTigerstripeProject corresponding to the given path.
	 * 
	 * The path is expected to be the folder where the project is stored. if no
	 * tigerstripe-related project descriptor is found there, no project is
	 * returned.
	 * 
	 * @param path
	 * @return
	 * @throws TigerstripeException
	 */
	public static IAbstractTigerstripeProject findProjectOrCreate(IPath path)
			throws TigerstripeException {
		return TigerstripeProjectFactory.INSTANCE.findProjectOrCreate(path);
	}

	public static IAbstractTigerstripeProject findProject(IPath path)
			throws TigerstripeException {
		return TigerstripeProjectFactory.INSTANCE.findProject(path);
	}

	public static List<IAbstractTigerstripeProject> projects() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		List<IAbstractTigerstripeProject> result = new ArrayList<IAbstractTigerstripeProject>();
		try {
			for (IResource res : root.members()) {
				IAbstractTigerstripeProject p = (IAbstractTigerstripeProject) res
						.getAdapter(IAbstractTigerstripeProject.class);
				if (p != null)
					result.add(p);
			}
		} catch (CoreException e) {
			BasePlugin.log(e);
		}
		return Collections.unmodifiableList(result);
	}

	public static IAbstractTigerstripeProject findProject(URI uri)
			throws TigerstripeException {
		File file = new File(uri);
		IPath path = new Path(file.getAbsolutePath());
		return TigerstripeProjectFactory.INSTANCE.findProjectOrCreate(path);
	}

	public static IAbstractTigerstripeProject findProject(String name)
			throws TigerstripeException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource tsContainer = root.findMember(new Path(name));
		if (tsContainer == null) {
			return null;
		}
		IPath path = tsContainer.getLocation();
		return findProjectOrCreate(path);
	}

	/**
	 * Returns an array containing all Tigerstripe Projects present in the
	 * workspace
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	public static IAbstractTigerstripeProject[] allProjects()
			throws TigerstripeException {
		// return TigerstripeProjectsSession.INSTANCE.allProjects();
		List<IAbstractTigerstripeProject> allProjects = new ArrayList<IAbstractTigerstripeProject>();

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		for (IProject iProject : root.getProjects()) {
			IAbstractTigerstripeProject tProject = (IAbstractTigerstripeProject) iProject
					.getAdapter(IAbstractTigerstripeProject.class);
			if (tProject instanceof IAbstractTigerstripeProject)
				allProjects.add(tProject);
		}
		return allProjects.toArray(new IAbstractTigerstripeProject[allProjects
				.size()]);
	}

	/**
	 * Returns an array containing all Tigerstripe Projects present in the
	 * workspace
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	public static ITigerstripeModelProject[] allModelProjects()
			throws TigerstripeException {
		// return TigerstripeProjectsSession.INSTANCE.allModelProjects();
		List<ITigerstripeModelProject> allProjects = new ArrayList<ITigerstripeModelProject>();

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		for (IProject iProject : root.getProjects()) {
			ITigerstripeModelProject tProject = (ITigerstripeModelProject) iProject
					.getAdapter(ITigerstripeModelProject.class);
			if (tProject instanceof ITigerstripeModelProject)
				allProjects.add(tProject);
		}
		return allProjects.toArray(new ITigerstripeModelProject[allProjects
				.size()]);
	}

	/**
	 * Returns Tigerstripe Model Project by model ID
	 * 
	 * @param modelId
	 *            project model ID
	 * @return
	 */
	public static ITigerstripeModelProject findModelProjectByID(String modelId) {
		if (modelId == null)
			return null;

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		for (IProject iProject : root.getProjects()) {
			ITigerstripeModelProject tProject = (ITigerstripeModelProject) iProject
					.getAdapter(ITigerstripeModelProject.class);

			if (tProject != null) {
				try {
					if (modelId.equals(tProject.getModelId())) {
						return tProject;
					}
				} catch (Exception e) {
					// ignore exception
				}
			}
		}
		return null;
	}

	/**
	 * Creates a project of the given type at the given folder, and returns a
	 * handle on that project
	 * 
	 * @param projectName
	 * @param projectDetails
	 *            - if null, default implementation is used.
	 * @param location
	 *            - location for the project to create, if null the default
	 *            location is used
	 * @param projectType
	 *            - one of the types as returned by
	 * @param monitor
	 *            -
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static IAbstractTigerstripeProject createProject(String projectName,
			IProjectDetails projectDetails, IPath location, Class projectType,
			Map<String, Object> properties, IProgressMonitor monitor)
			throws TigerstripeException {

		if (projectDetails == null)
			projectDetails = makeProjectDetails();

		return TigerstripeProjectFactory.INSTANCE.createProject(projectName,
				projectDetails, location, projectType, properties, monitor);
	}

	public static IProjectDetails makeProjectDetails() {
		return new ProjectDetails(null);
	}

	/**
	 * Returns a collection of Tigerstripe project types that are supported
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Collection<Class> getSupportedProjectTypes() {
		return TigerstripeProjectFactory.INSTANCE.getSupportedProjectTypes();
	}

	/**
	 * Register a new {@link ITigerstripeChangeListener}
	 * 
	 * @param listener
	 *            the listener to register
	 * @param changeLevel
	 *            valid values are {@link ITigerstripeChangeListener#MODEL},
	 *            {@link ITigerstripeChangeListener#PROJECT} or
	 *            {@link ITigerstripeChangeListener#ALL}
	 */
	public static void addTigerstripeChangeListener(
			ITigerstripeChangeListener listener, int changeLevel) {
		TigerstripeWorkspaceNotifier.INSTANCE.addTigerstripeChangeListener(
				listener, changeLevel);
	}

	/**
	 * Un-Register a {@link ITigerstripeChangeListener}
	 * 
	 * @param listener
	 */
	public static void removeTigerstripeChangeListener(
			ITigerstripeChangeListener listener) {
		TigerstripeWorkspaceNotifier.INSTANCE
				.removeTigerstripeChangeListener(listener);
	}
}
