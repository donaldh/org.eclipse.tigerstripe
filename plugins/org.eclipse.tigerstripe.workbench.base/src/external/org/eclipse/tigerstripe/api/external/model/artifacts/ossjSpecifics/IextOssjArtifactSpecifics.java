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
package org.eclipse.tigerstripe.api.external.model.artifacts.ossjSpecifics;

import java.util.Properties;

import org.eclipse.tigerstripe.api.external.model.artifacts.IextStandardSpecifics;

/**
 * Top level interface for OSSJ Standard Specific details for Tigerstripe
 * Artifacts.
 * 
 */
public interface IextOssjArtifactSpecifics extends IextStandardSpecifics {

	/**
	 * Returns the interface properties for an OSSJ Artifact
	 * 
	 * @return
	 */
	public Properties getInterfaceProperties();

}
