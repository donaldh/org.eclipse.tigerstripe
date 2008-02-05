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

import org.eclipse.tigerstripe.workbench.internal.TigerstripeRuntimeDetails;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ProjectSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.api.impl.WorkbenchProfileSession;
import org.eclipse.tigerstripe.workbench.internal.api.project.IProjectSession;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfileSession;

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

	private static IProjectSession projectSession;

	protected TigerstripeCore() {
	}

	/**
	 * Returns the default Project Session for this API. There is only 1 project
	 * session per instance of Tigerstripe Workbench. (Singleton)
	 * 
	 * This is the main entry point in the API to access project level
	 * information.
	 * 
	 * @return IProjectSession - the default project session.
	 */
	public static IProjectSession getDefaultProjectSession()
			 {

		if (projectSession == null) {
			ProjectSessionImpl session = new ProjectSessionImpl();
			projectSession = session;
		}

		return projectSession;
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
	 * 	Returns the runtime details for this install
	 * 
	 * @return
	 */
	public final static IRuntimeDetails getRuntimeDetails() {
		return TigerstripeRuntimeDetails.INSTANCE;
	}
}
