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
package org.eclipse.tigerstripe.contract.segment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.tigerstripe.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.api.contract.segment.IFacetPredicate;
import org.eclipse.tigerstripe.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.contract.predicate.LogicalFacetPredicate;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.core.util.LogicalPredicate;

public class MultiFacetReference extends FacetReference {

	private List<IFacetReference> facets = new ArrayList<IFacetReference>();

	private IContractSegment combinedFacet;

	public MultiFacetReference(IFacetReference[] facetsRefs,
			ITigerstripeProject project) {
		super(null, project);
		facets.addAll(Arrays.asList(facetsRefs));
	}

	@Override
	public IFacetPredicate computeFacetPredicate(
			ITigerstripeProgressMonitor monitor) {

		facetPredicate = new LogicalFacetPredicate(LogicalPredicate.OR);
		for (IFacetReference facet : facets) {
			((LogicalFacetPredicate) facetPredicate).add(facet
					.computeFacetPredicate(monitor));
		}

		return facetPredicate;
	}

	@Override
	public boolean canResolve() {
		for (IFacetReference ref : facets) {
			if (!ref.canResolve())
				return false;
		}
		return true;
	}

	@Override
	public IContractSegment resolve() throws TigerstripeException {
		if (combinedFacet == null) {
			internalResolve();
		}
		return combinedFacet;
	}

	protected void internalResolve() {
		// combine all facets into a single one
		combinedFacet = new LogicalContractSegment();

		String name = "<Merged Facets: ";
		String sep = "";
		for (IFacetReference ref : facets) {
			try {
				if (ref.resolve() != null) {
					name = name + sep + ref.resolve().getName();
				}
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage(
						"TigerstripeException detected", e);
			}
			sep = ", ";
		}
		name = name + ">";

		combinedFacet.setName(name);
	}
}
