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
package org.eclipse.tigerstripe.api.external.project;

import java.net.URI;

import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.TigerstripeLicenseException;

public interface IextProjectSession {

	/**
	 * Creates a handle on a new Tigerstripe Project for the specified Project
	 * Type.
	 * 
	 * @param projectURI
	 * @throws TigerstripeLicenseException
	 *             if the current license doesn't allow this operation
	 * @throws org.eclipse.tigerstripe.api.external.TigerstripeException
	 *             if any other error occured.
	 * @return
	 */
	public IextAbstractTigerstripeProject makeTigerstripeProject(URI projectURI)
			throws TigerstripeLicenseException, TigerstripeException;

}
