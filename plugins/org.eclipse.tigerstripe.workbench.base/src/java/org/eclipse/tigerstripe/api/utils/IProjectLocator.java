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
package org.eclipse.tigerstripe.api.utils;

import java.net.URI;

import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;

/**
 * Provides a location facility for projects references.
 * 
 * Basically, the reference that is stored in the Tigerstripe.xml for referenced
 * projects is a label for the project avoiding any hardcoded path. An
 * IProjectLocator returns the corresponding URI at run-time for the given label
 * 
 * @author Eric Dillon
 * 
 */
public interface IProjectLocator extends IAPIFacility {

	/**
	 * Locates the project identified by the provided label within the context
	 * of the given ITigerstripeProject and returns the absolute URI for it.
	 * 
	 * @param projectContext -
	 *            the project where the label is used
	 * @param projectLabel -
	 *            the project label to lookup
	 * @return the URI for the given label
	 * @throws TigerstripeException
	 */
	public URI locate(ITigerstripeProject projectContext, String projectLabel)
			throws TigerstripeException;

	/**
	 * Maps the given URI into a contextual label that can be used locally
	 * 
	 * @param uri
	 * @return
	 * @throws TigerstripeException
	 */
	public String getLocalLabel(URI uri) throws TigerstripeException;
}
