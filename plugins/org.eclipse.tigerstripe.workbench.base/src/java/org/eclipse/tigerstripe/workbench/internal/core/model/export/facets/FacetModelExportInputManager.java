/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jim Strawn (Cisco Systems, Inc.) - initial implementation
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.internal.core.model.export.facets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class FacetModelExportInputManager {

	private IFile facet;

	private ITigerstripeModelProject source = null;

	private ITigerstripeModelProject destination = null;

	private boolean facetApplied = false;

	private boolean includeReferences = false;

	private boolean overwriteExisting = false;
	
	private List<IAbstractArtifact> overwrites = new ArrayList<IAbstractArtifact>();

	public void setInitialSelection(IStructuredSelection selection) {

		if (selection.size() != 0) {
			if(selection.getFirstElement() instanceof IJavaProject) {
				source = (ITigerstripeModelProject) ((IJavaProject) selection.getFirstElement()).getProject().getAdapter(ITigerstripeModelProject.class);
			}
		}
	}

	public IFile getFacet() {
		return facet;
	}

	public void setFacet(IFile facet) {
		this.facet = facet;
	}

	public ITigerstripeModelProject getSource() {
		return source;
	}

	public void setSource(ITigerstripeModelProject source) {
		this.source = source;
	}

	public ITigerstripeModelProject getDestination() {
		return destination;
	}

	public void setDestination(ITigerstripeModelProject destination) {
		this.destination = destination;
	}

	public void setFacetApplied(boolean facetApplied) {
		this.facetApplied = facetApplied;
	}

	public boolean isFacetApplied() {
		return facetApplied;
	}

	public boolean isIncludeReferences() {
		return includeReferences;
	}

	public void setIncludeReferences(boolean includeReferences) {
		this.includeReferences = includeReferences;
	}

	public boolean isOverwriteExisting() {
		return overwriteExisting;
	}

	public void setOverwriteExisting(boolean overwriteExisting) {
		this.overwriteExisting = overwriteExisting;
	}

	public List<IAbstractArtifact> getOverwrites() {
		return overwrites;
	}
	
	public void setOverwrites(List<IAbstractArtifact> overwrites) {
		this.overwrites = overwrites;
	}
	
	public void validate() {

		if (facet == null)
			throw new IllegalArgumentException("The facet file may not be null.");

		if (!facet.exists())
			throw new IllegalArgumentException("The facet file must exist in the file system: " + facet.getFullPath());

		if (source == null)
			throw new IllegalArgumentException("The source project may not be null.");

		if (!source.exists())
			throw new IllegalArgumentException("The source project must exist in the file system: " + source.getFullPath());

		if (destination == null)
			throw new IllegalArgumentException("The destination project may not be null.");

		if (!destination.exists())
			throw new IllegalArgumentException("The destination project must exist in the file system: " + destination.getFullPath());

	}

	public boolean verifyComplete() {

		if (source != null && destination != null && facet != null) {
			return true;
		} else {
			return false;
		}
	}

}