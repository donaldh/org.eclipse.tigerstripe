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
package org.eclipse.tigerstripe.workbench.internal.api.contract.segment;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.useCase.IUseCaseReference;
import org.eclipse.tigerstripe.workbench.internal.api.versioning.IVersionAwareElement;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;

/**
 * 
 * @author Eric Dillon
 * 
 */
public interface IContractSegment extends IVersionAwareElement {

	public final static String FILE_EXTENSION = "wfc";

	public final static String DEFAULT_SEGMENT_FILE = "default" + "."
			+ FILE_EXTENSION;

	/**
	 * Returns the Segment Scope define for this segment
	 * 
	 * @return
	 */
	public ISegmentScope getISegmentScope();

	/**
	 * returns the segment scope as defined by the local scope combined with the
	 * scopes of all included facets.
	 * 
	 * @return
	 */
	public ISegmentScope getCombinedScope() throws TigerstripeException;

	/**
	 * Return an array containing all the facet References (if any) used to
	 * augment the local scope
	 * 
	 * @since Bug 1016
	 * @return
	 */
	public IFacetReference[] getFacetReferences();

	public void addFacetReference(IFacetReference facetReference)
			throws TigerstripeException;

	public void removeFacetReference(IFacetReference facetReference)
			throws TigerstripeException;

	public IUseCaseReference[] getUseCaseRefs();

	public void addUseCase(IUseCaseReference useCase)
			throws TigerstripeException;

	public void removeUseCase(IUseCaseReference useCase)
			throws TigerstripeException;

	/**
	 * 	Returns an array containing the IDs of all the annotations to be considered as part
	 * of the annotation context for this facet.
	 * 
	 * If no annotation context include or exclude pattern is specified, all known annotations
	 * are included
	 * 
	 * @return
	 */
	public String[] getAnnotationContext();
	
	/**
	 * Returns the project where the facet is stored, if any.
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	public IAbstractTigerstripeProject getContainingProject()
			throws TigerstripeException;
}
