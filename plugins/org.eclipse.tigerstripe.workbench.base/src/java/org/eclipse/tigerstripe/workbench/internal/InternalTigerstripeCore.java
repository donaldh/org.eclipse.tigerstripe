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
package org.eclipse.tigerstripe.workbench.internal;

import java.util.HashMap;

import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.IContractSession;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactMetadataSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ContractSession;
import org.eclipse.tigerstripe.workbench.internal.api.impl.DefaultProjectLocator;
import org.eclipse.tigerstripe.workbench.internal.api.impl.DiagramRenderingSession;
import org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactMetadataSession;
import org.eclipse.tigerstripe.workbench.internal.api.rendering.IDiagramRenderingSession;
import org.eclipse.tigerstripe.workbench.internal.api.utils.IAPIFacility;

/**
 * The Internal Tigerstripe Core contains additional entry point in the
 * Tigerstripe API that are still provisional and may evolve in the future.
 * 
 * @author erdillon
 * 
 */
public class InternalTigerstripeCore extends TigerstripeCore {

	/**
	 * This is a property that can be set on the API when looking up project
	 * references. If no locator is set, a default locator implementation is
	 * provided instead
	 */
	public final static String PROJECT_LOCATOR_FACILITY = "ide.projectLocator";
	private final static String[] supportedFacilities = { PROJECT_LOCATOR_FACILITY };
	private static HashMap<String, IAPIFacility> propertiesMap = new HashMap<String, IAPIFacility>();
	private static IDiagramRenderingSession diagramRenderingSession;
	private static IContractSession contractSession;

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

	static {
		registerDefaultProperties();
		diagramRenderingSession = new DiagramRenderingSession();
	}

	/**
	 * 
	 */
	private static void registerDefaultProperties() {
		propertiesMap
				.put(PROJECT_LOCATOR_FACILITY, new DefaultProjectLocator());
	}

	public static IDiagramRenderingSession getIDiagramRenderingSession() {
		if (diagramRenderingSession == null) {
			diagramRenderingSession = new DiagramRenderingSession();
		}
		return diagramRenderingSession;
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

	public static IContractSession getIContractSession() {
		if (contractSession == null) {
			contractSession = new ContractSession();
		}
		return contractSession;
	}

}
