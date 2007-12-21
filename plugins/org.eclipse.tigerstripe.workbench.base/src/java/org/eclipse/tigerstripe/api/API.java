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
package org.eclipse.tigerstripe.api;

import java.util.HashMap;

import org.eclipse.tigerstripe.api.artifacts.IArtifactMetadataSession;
import org.eclipse.tigerstripe.api.contract.IContractSession;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.TigerstripeLicenseException;
import org.eclipse.tigerstripe.api.impl.ArtifactMetadataSessionImpl;
import org.eclipse.tigerstripe.api.impl.ContractSession;
import org.eclipse.tigerstripe.api.impl.DefaultProjectLocator;
import org.eclipse.tigerstripe.api.impl.DiagramRenderingSession;
import org.eclipse.tigerstripe.api.impl.ProjectSessionImpl;
import org.eclipse.tigerstripe.api.impl.WorkbenchProfileSession;
import org.eclipse.tigerstripe.api.profile.IWorkbenchProfileSession;
import org.eclipse.tigerstripe.api.project.IProjectSession;
import org.eclipse.tigerstripe.api.rendering.IDiagramRenderingSession;
import org.eclipse.tigerstripe.api.utils.IAPIFacility;

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
public class API {

	private static IWorkbenchProfileSession workbenchProfileSession;

	private static IDiagramRenderingSession diagramRenderingSession;

	/**
	 * This is a property that can be set on the API when looking up project
	 * references. If no locator is set, a default locator implementation is
	 * provided instead
	 */
	public final static String PROJECT_LOCATOR_FACILITY = "ide.projectLocator";

	private final static String[] supportedFacilities = { PROJECT_LOCATOR_FACILITY };

	private static HashMap<String, IAPIFacility> propertiesMap = new HashMap<String, IAPIFacility>();

	static {
		registerDefaultProperties();
		diagramRenderingSession = new DiagramRenderingSession();
	}

	private static IProjectSession instance;

	private static IContractSession contractSession;

	private API() {
		super();
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
			throws TigerstripeLicenseException {

		if (instance == null) {
			ProjectSessionImpl session = new ProjectSessionImpl();
			instance = session;
		}

		return instance;
	}

	/**
	 * Returns the default IArtifactMetadataSession.
	 * 
	 * This is not fully supported in 1.2 yet, but is intended to provide access
	 * to the Tigerstripe Metamodel that's deployed.
	 * 
	 * This part of the API should be consider un-stable as of Tigerstripe
	 * Workbench 1.2
	 * 
	 * @return IArtifactMetadataSession - the default IArtifactMetadataSession
	 *         (Singleton)
	 */
	public static IArtifactMetadataSession getDefaultArtifactMetadataSession() {
		return ArtifactMetadataSessionImpl.getInstance();
	}

	/**
	 * To allow for better interaction between the IDE used on top of this API,
	 * the client of the API can register a set of utility objects that will be
	 * used if present by the API.
	 * 
	 * These basically implement a call-back mechanism so the API has
	 * effectively no dependency on any given IDE
	 * 
	 * This part of the API should be consider un-stable as of Tigerstripe
	 * Workbench 1.2
	 */
	public static IAPIFacility getFacility(String facilityID)
			throws TigerstripeException {
		if (propertiesMap.containsKey(facilityID))
			return propertiesMap.get(facilityID);

		throw new TigerstripeException("No registered API Facility for '"
				+ facilityID + "'.");
	}

	/**
	 * Allows a client of this API to register its own facilities
	 * 
	 * @param facilityID
	 * @param facility
	 * @throws TigerstripeException
	 */
	public static void registerFacility(String facilityID, IAPIFacility facility)
			throws TigerstripeException {
		// check it's supported facility
		boolean facilityIsSupported = false;
		for (String facID : supportedFacilities) {
			if (facID.equals(facilityID)) {
				facilityIsSupported = true;
				continue;
			}
		}

		if (!facilityIsSupported)
			throw new TigerstripeException("'" + facilityID
					+ "' is not a supported facility.");

		propertiesMap.put(facilityID, facility);
	}

	/**
	 * 
	 */
	private static void registerDefaultProperties() {
		propertiesMap
				.put(PROJECT_LOCATOR_FACILITY, new DefaultProjectLocator());
	}

	/**
	 * Returns the default IWorkbenchProfileSession (Singleton) which provides
	 * access to the Active Profile and to the create/edit additional profiles
	 * programmatically.
	 * 
	 * @return IWorkbenchProfileSession - the default IWorkbenchProfileSession
	 *         (Singleton)
	 */
	public static IWorkbenchProfileSession getIWorkbenchProfileSession() {
		if (workbenchProfileSession == null) {
			workbenchProfileSession = new WorkbenchProfileSession();
			workbenchProfileSession.reloadActiveProfile();
		}

		return workbenchProfileSession;
	}

	public static IContractSession getIContractSession() {
		if (contractSession == null) {
			contractSession = new ContractSession();
		}
		return contractSession;
	}

	public static IDiagramRenderingSession getIDiagramRenderingSession() {
		if (diagramRenderingSession == null) {
			diagramRenderingSession = new DiagramRenderingSession();
		}
		return diagramRenderingSession;
	}
}
