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
package org.eclipse.tigerstripe.workbench.internal.api.contract.useCase;

import java.net.URI;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * Reference to a UseCase from a Contract Segment
 * 
 * @author Eric Dillon
 * 
 */
public interface IUseCaseReference {

	/**
	 * Returns the URI for the referenced Use Case
	 * 
	 * @return
	 * @throws TigerstripeException
	 *             if can't be computed.
	 */
	public URI getURI() throws TigerstripeException;

	/**
	 * Returns true if the ref can be resolved
	 * 
	 * @return
	 */
	public boolean canResolve();

	/**
	 * Resolves this reference to the corresponding IUseCase
	 * 
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	public IUseCase resolve() throws TigerstripeException;

	/**
	 * Returns true if the ref is an absolute i.e. (not thru a project relative
	 * path)
	 * 
	 * @return
	 */
	public boolean isAbsolute();

	public String getProjectRelativePath();

	/**
	 * Returns the project where the facet is defined.
	 * 
	 * @return
	 */
	public ITigerstripeModelProject getContainingProject();
}
