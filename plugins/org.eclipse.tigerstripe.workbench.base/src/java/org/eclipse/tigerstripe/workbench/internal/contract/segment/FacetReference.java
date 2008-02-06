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

import java.io.File;
import java.net.URI;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.InternalTigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetPredicate;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactChangeListener;
import org.eclipse.tigerstripe.workbench.internal.api.utils.IProjectLocator;
import org.eclipse.tigerstripe.workbench.internal.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.workbench.internal.contract.predicate.FacetPredicate;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.workbench.model.IRelationship;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

public class FacetReference implements IFacetReference, IArtifactChangeListener {

	private URI facetURI;

	private String generationDir = "";

	private TigerstripeProject project;

	private ITigerstripeProject tsProject;

	private ITigerstripeProject contextProject;

	private String projectLabel;

	private String projectRelativePath;

	private FacetPredicate primaryPredicate;

	private IContractSegment resolvedSegment = null;

	private long resolvedTStamp = -1;

	// Bug 921: we keep track of the "state" of the underlying Contract Segment,
	// so
	// we can determine when we need to re-compute the predicate.
	private long computedTStamp;

	// The active mgr when this ref is active or null otherwise
	private ArtifactManager activeMgr;

	public FacetReference(URI facetURI, ITigerstripeProject tsProject) {
		this.facetURI = facetURI;
		this.project = null;
		this.projectRelativePath = null;
		this.tsProject = tsProject;
	}

	public FacetReference(String projectRelativePath, String projectLabel,
			ITigerstripeProject contextProject) {
		this.projectLabel = projectLabel;
		this.projectRelativePath = projectRelativePath;
		this.contextProject = contextProject;
	}

	public FacetReference(String projectRelativePath, TigerstripeProject project) {
		this.project = project;
		this.projectRelativePath = projectRelativePath;
	}

	public void startListeningToManager(ArtifactManager mgr) {
		this.activeMgr = mgr;
		mgr.addArtifactManagerListener(this);
	}

	public void stopListeningToManager() {
		if (activeMgr != null) {
			activeMgr.removeArtifactManagerListener(this);
			this.activeMgr = null;
		}
	}

	public boolean isAbsolute() {
		return project == null && projectLabel == null;
	}

	public URI getURI() throws TigerstripeException {
		if (isAbsolute())
			return facetURI;
		else {
			File baseDir = null;
			if (project == null && projectLabel != null) {
				IProjectLocator locator = (IProjectLocator) InternalTigerstripeCore
						.getFacility(InternalTigerstripeCore.PROJECT_LOCATOR_FACILITY);
				URI uri = locator.locate(contextProject, projectLabel);
				baseDir = new File(uri);
			} else {
				baseDir = project.getBaseDir();
			}

			if (baseDir != null) {
				String path = baseDir.getAbsolutePath() + File.separator
						+ projectRelativePath;
				File file = new File(path);
				return file.toURI();
			}
			throw new TigerstripeException("Can't get URI for facet "
					+ projectRelativePath);
		}
	}

	public IContractSegment resolve() throws TigerstripeException {
		if (getURI() != null) {
			File target = new File(getURI());
			if (target.exists() && target.canRead()) {
				if (target.lastModified() != resolvedTStamp) {
					resolvedSegment = InternalTigerstripeCore
							.getIContractSession().makeIContractSegment(
									getURI());
					resolvedTStamp = target.lastModified();
				}
				return resolvedSegment;
			} else
				return null;
		}
		throw new TigerstripeException("Invalid Facet: " + facetURI);
	}

	public boolean canResolve() {
		try {
			return resolve() != null;
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage(
					"Error while trying to resolve a FacetReference: "
							+ e.getMessage(), e);
		}
		return false;
	}

	public String getGenerationDir() {
		return generationDir;
	}

	public void setGenerationDir(String outputRelativeDir) {
		this.generationDir = outputRelativeDir;
	}

	public String getProjectRelativePath() {
		return projectRelativePath;
	}

	@Override
	public String toString() {
		try {
			return "Facet Reference(" + canResolve() + "): "
					+ getURI().toASCIIString();
		} catch (TigerstripeException e) {
			return "Facet Reference(" + canResolve() + "): <unknown>";
		}
	}

	public ITigerstripeProject getContainingProject() {
		return getTSProject();
	}

	protected ITigerstripeProject getTSProject() {

		if (tsProject != null)
			return tsProject;

		try {
			IAbstractTigerstripeProject aProject = null;
			if (project == null && projectLabel != null) {
				IWorkspace workspace = ResourcesPlugin.getWorkspace();
				IResource res = workspace.getRoot().findMember(projectLabel);
				TigerstripeCore.findProject(res.getFullPath());
			} else {
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				File file = project.getBaseDir();
				IPath path = new Path(file.getAbsolutePath());
				IContainer container = root.getContainerForLocation(path);
				aProject = TigerstripeCore.findProject(container.getFullPath());
			}
			if (aProject instanceof ITigerstripeProject)
				return (ITigerstripeProject) aProject;
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}
		return null;
	}

	protected IFacetPredicate facetPredicate;

	public IFacetPredicate computeFacetPredicate(
			ITigerstripeProgressMonitor monitor) {
		facetPredicate = new FacetPredicate(this, getTSProject());

		// Bug 921: this is a bit of a hack for now to keep track of the state
		// of the underlying contract segment. This is used later to determine
		// whether we need to re-compute the facet predicate or not
		try {
			facetPredicate.resolve(monitor);
			URI csURI = getURI();
			File file = new File(csURI);
			computedTStamp = file.lastModified();
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage(
					"Couldn't determine computedTStamp for FacetPredicate: "
							+ e.getMessage(), e);
		}
		return facetPredicate;
	}

	// Bug 921: we need to re-evaluate the facet predicate if the underlying
	// facet scope changes!
	private boolean hasFacetChanged() {
		if (canResolve()) {
			try {
				URI csURI = getURI();
				File file = new File(csURI);
				long tStamp = file.lastModified();
				return computedTStamp != tStamp;
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage(
						"Couldn't determine whether facet has changed or not: "
								+ e.getMessage(), e);
			}
		}
		return false;
	}

	public IFacetPredicate getFacetPredicate() {
		if (facetPredicate == null || hasFacetChanged())
			return computeFacetPredicate(new TigerstripeNullProgressMonitor());

		return facetPredicate;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IFacetReference) {
			IFacetReference other = (IFacetReference) obj;
			try {
				return other.getURI().equals(getURI());
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage(
						"TigerstripeException detected", e);
				return false;
			}
		}
		return false;
	}

	public void artifactAdded(IAbstractArtifact artifact) {
		// need to determine if this artifact should be in or out of the scope
	}

	public void artifactChanged(IAbstractArtifact artifact) {

	}

	public void artifactRemoved(IAbstractArtifact artifact) {
		// need to determine if
		// - Other artifacts need to be removed from the facet as a result of
		// this removal
		if (artifact instanceof IRelationship) {
			handleRelationshipRemoved(artifact);
		}
	}

	public void artifactRenamed(IAbstractArtifact artifact, String fromFQN) {
		// TODO Auto-generated method stub

	}

	public void managerReloaded() {
		// TODO Auto-generated method stub

	}

	// Artifact Mgr listener

	private void handleRelationshipRemoved(IAbstractArtifact artifact) {

		if (getContainingProject() == null || activeMgr == null)
			return; // should never happen

		if (((IRelationship) artifact).getRelationshipAEnd() == null
				|| ((IRelationship) artifact).getRelationshipZEnd() == null)
			return;

		if (((IRelationship) artifact).getRelationshipAEnd().getType() == null
				|| ((IRelationship) artifact).getRelationshipZEnd().getType() == null)
			return;

		FacetPredicate pred = getPrimaryPredicate();
		// if rel is explicitly in primary predicate, the facet is now in error
		// all bets are off
		if (pred.evaluate(artifact)) {

		} else {
			// if not, check that the aEnd was in, in which case the
			// zEnd should be removed from the facet
			IRelationship rel = (IRelationship) artifact;
			try {
				IAbstractArtifact aEnd = getContainingProject()
						.getArtifactManagerSession()
						.getArtifactByFullyQualifiedName(
								rel.getRelationshipAEnd().getType()
										.getFullyQualifiedName());
				if (pred.evaluate(aEnd)) {
					// remove zEnd
					FacetPredicate resolvedPred = (FacetPredicate) getFacetPredicate();
					resolvedPred.addTempExclude(rel.getRelationshipZEnd()
							.getType().getFullyQualifiedName());
					activeMgr.setActiveFacet(this,
							new TigerstripeNullProgressMonitor());
				}
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage(
						"TigerstripeException detected", e);
			}
		}

	}

	/**
	 * Returns the un-resolved primary predicate for this facet
	 * 
	 * @return
	 */
	private FacetPredicate getPrimaryPredicate() {
		if (primaryPredicate == null) {
			primaryPredicate = new FacetPredicate(this, getContainingProject());
		}
		return primaryPredicate;
	}
}
