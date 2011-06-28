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
package org.eclipse.tigerstripe.workbench.internal.contract.segment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetPredicate;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.contract.predicate.LogicalFacetPredicate;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.util.LogicalPredicate;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class MultiFacetReference extends FacetReference {

	private final List<IFacetReference> facets = new ArrayList<IFacetReference>();

	private IContractSegment combinedFacet;

	public MultiFacetReference(IFacetReference[] facetsRefs,
			ITigerstripeModelProject project) {
		super(null, project);
		facets.addAll(Arrays.asList(facetsRefs));
	}

	@Override
	public IFacetPredicate computeFacetPredicate(IProgressMonitor monitor) {

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
		facetPredicate = null;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((facets == null) ? 0 : facets.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		MultiFacetReference other = (MultiFacetReference) obj;
		if (this.facets.size() != other.facets.size()) {
			return false;
		}
		for (IFacetReference reference : this.facets) {
			if (!other.facets.contains(reference)) {
				return false;
			}
		}
		return true;
	}
}
