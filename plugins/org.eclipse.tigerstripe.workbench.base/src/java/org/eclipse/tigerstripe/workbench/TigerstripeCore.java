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
import java.util.Collection;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.tigerstripe.workbench.internal.TigerstripeRuntimeDetails;
import org.eclipse.tigerstripe.workbench.internal.api.impl.WorkbenchProfileSession;
import org.eclipse.tigerstripe.workbench.internal.builder.BuilderConstants;
import org.eclipse.tigerstripe.workbench.internal.core.project.ProjectDetails;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProjectFactory;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfileSession;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;

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
 * <li>The <b>Project Session</b>: which provides a session facade to access
 * all project-related details about a workspace. It allows to access, create,
 * change Tigerstripe projects within the Eclipse workspace</li>
 * <li>The <b>Profile Session</b>: which provides a session facade to access
 * the active Tigerstripe profile but also to create/edit profiles.
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
	public static IAbstractTigerstripeProject findProject(IPath path)
			throws TigerstripeException {
		return TigerstripeProjectFactory.INSTANCE.findProject(path);
	}

	public static IAbstractTigerstripeProject findProject(URI uri)
			throws TigerstripeException {
		File file = new File(uri);
		IPath path = new Path(file.getAbsolutePath());
		return TigerstripeProjectFactory.INSTANCE.findProject(path);
	}

	/**
	 * Creates a project of the given type at the given folder, and returns a
	 * handle on that project
	 * 
	 * @param projectDetails
	 * @param location -
	 *            location for the project to create, if null the default
	 *            location is used
	 * @param projectType -
	 *            one of the types as returned by
	 * @param monitor -
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static IAbstractTigerstripeProject createProject(
			IProjectDetails projectDetails, IPath location, Class projectType,
			Map<String, Object> properties, IProgressMonitor monitor)
			throws TigerstripeException {
		return TigerstripeProjectFactory.INSTANCE.createProject(projectDetails,
				location, projectType, properties, monitor);
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

}
