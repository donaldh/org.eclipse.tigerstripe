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
package org.eclipse.tigerstripe.api.external;

import org.eclipse.tigerstripe.api.external.project.IextProjectSession;

/**
 * The entry point for all interactions with the Tigerstripe API This is a
 * singleton class that acts as the factory class for any Tigerstripe Project.
 * 
 * Also, as the singleton is accessed, the TigerstripeLicense is checked.
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
 * The API contains one entry point:
 * <ul>
 * <li>The <b>Project Session</b>: which provides a session facade to access
 * all project-related details about a workspace. It allows to access, create,
 * change Tigerstripe projects within the Eclipse workspace</li>
 * </ul>
 */
public class API {

	/**
	 * Returns the default Project Session for this API. There is only 1 project
	 * session per instance of Tigerstripe Workbench. (Singleton)
	 * 
	 * This is the main entry point in the API to access project level
	 * information.
	 * 
	 * @return IProjectSession - the default project session.
	 */
	public static IextProjectSession getDefaultProjectSession()
			throws TigerstripeLicenseException {

		return org.eclipse.tigerstripe.api.API.getDefaultProjectSession();
	}

}
