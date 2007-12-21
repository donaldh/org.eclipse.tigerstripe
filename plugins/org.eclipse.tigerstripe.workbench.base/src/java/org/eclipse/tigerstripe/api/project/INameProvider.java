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
package org.eclipse.tigerstripe.api.project;

import org.eclipse.tigerstripe.api.external.TigerstripeException;

/**
 * Provide unique default names for any artifact type in the context of a given
 * project
 * 
 * @author Eric Dillon
 * 
 */
public interface INameProvider {

	/**
	 * Returns a unique name for the given artifact type within the context of
	 * the project for the given package.
	 * 
	 * Equivalent to forceIncrement = false
	 * 
	 * @param artifactType
	 * @param packageName
	 * @return
	 */
	public String getUniqueName(Class artifactType, String packageName)
			throws TigerstripeException;

	/**
	 * Returns a unique name for the given artifact type within the context of
	 * the project for the given package.
	 * 
	 * @param artifactType
	 * @param packageName
	 * @param forceIncrement
	 * @return
	 */
	public String getUniqueName(Class artifactType, String packageName,
			boolean forceIncrement) throws TigerstripeException;
}
