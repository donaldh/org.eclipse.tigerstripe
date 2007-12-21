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
package org.eclipse.tigerstripe.api.contract.segment;

import java.net.URI;

import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.api.utils.ITigerstripeProgressMonitor;

/**
 * A reference to a facet as it appears in a Tigerstripe project descriptor.
 * 
 * @author Eric Dillon
 * 
 */
public interface IFacetReference {

	/**
	 * Returns the URI for the reference facet
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
	 * Resolves this reference to the corresponding IContractFacet
	 * 
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	public IContractSegment resolve() throws TigerstripeException;

	/**
	 * Sets the dir to be used to save generated file relative to the project's
	 * output dir
	 * 
	 * @param outputRelativeDir
	 */
	public void setGenerationDir(String outputRelativeDir);

	/**
	 * gets the dir to be used to save generated file relative to the project's
	 * output dir
	 * 
	 * @param outputRelativeDir
	 */
	public String getGenerationDir();

	/**
	 * Returns true if the ref is an absolute i.e. (not thru a project relative
	 * path)
	 * 
	 * @return
	 */
	public boolean isAbsolute();

	public String getProjectRelativePath();

	/**
	 * Computes the facet predicate for this reference providing feedback thru
	 * the monitor
	 * 
	 * @param monitor
	 * @return
	 */
	public IFacetPredicate computeFacetPredicate(
			ITigerstripeProgressMonitor monitor);

	/**
	 * Return the FacetPredicate corresponding to this facet ref.
	 * 
	 * Note: if computeFacetPredicate has not been called it will be called with
	 * a null progress monitor.
	 * 
	 * @return
	 */
	public IFacetPredicate getFacetPredicate();

	/**
	 * Returns the project where the facet is defined.
	 * 
	 * @return
	 */
	public ITigerstripeProject getContainingProject();
}
