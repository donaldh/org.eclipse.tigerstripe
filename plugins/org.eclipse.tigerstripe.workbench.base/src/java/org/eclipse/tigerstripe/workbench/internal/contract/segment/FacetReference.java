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
import java.net.URISyntaxException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.AbstractContainedObject;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.IContainedObject;
import org.eclipse.tigerstripe.workbench.internal.InternalTigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IContractSegment;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetPredicate;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeOssjProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactChangeListener;
import org.eclipse.tigerstripe.workbench.internal.api.utils.IProjectLocator;
import org.eclipse.tigerstripe.workbench.internal.contract.predicate.FacetPredicate;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class FacetReference extends AbstractContainedObject implements
		IFacetReference, IArtifactChangeListener, IContainedObject {

	private URI facetURI;

	private String generationDir = "";

	private TigerstripeProject project;

	private ITigerstripeModelProject tsProject;

	private ITigerstripeModelProject contextProject;

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

	public FacetReference(FacetReference ref) {
		projectLabel = ref.projectLabel;
		projectRelativePath = ref.projectRelativePath;
		facetURI = ref.facetURI;
		contextProject = ref.contextProject;
		tsProject = ref.tsProject;
		generationDir = ref.generationDir;

		// Bug 222275 (one part of)
		project = ref.project;

	}

	public FacetReference(URI facetURI, ITigerstripeModelProject tsProject) {
		this.facetURI = facetURI;
		this.project = null;
		this.projectRelativePath = null;
		this.tsProject = tsProject;
	}

	public FacetReference(String projectRelativePath, String projectLabel,
			ITigerstripeModelProject contextProject) {
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
				if (target.lastModified() > resolvedTStamp) {
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
		markDirty();
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

	public ITigerstripeModelProject getContainingProject() {
		return getTSProject();
	}

	protected ITigerstripeModelProject getTSProject() {

		if (tsProject != null)
			return tsProject;

		try {
			IAbstractTigerstripeProject aProject = null;
			if (project == null && projectLabel != null) {
				IWorkspace workspace = ResourcesPlugin.getWorkspace();
				IResource res = workspace.getRoot().findMember(projectLabel);
				aProject = TigerstripeCore.findProjectOrCreate(res.getLocation());
			} else {
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				File file = project.getBaseDir();
//				IPath path = new Path(file.getAbsolutePath());
//				IContainer container = root.getContainerForLocation(path);
				aProject = TigerstripeCore.findProject(file.toURI());
			}
			if (aProject instanceof ITigerstripeModelProject)
				return (ITigerstripeModelProject) aProject;
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}
		return null;
	}

	protected IFacetPredicate facetPredicate;

	public IFacetPredicate computeFacetPredicate(IProgressMonitor monitor) {
		facetPredicate = new FacetPredicate(this, getTSProject());

		// Bug 921: this is a bit of a hack for now to keep track of the state
		// of the underlying contract segment. This is used later to determine
		// whether we need to re-compute the facet predicate or not
		try {
			facetPredicate.resolve(monitor);
			computedTStamp = System.currentTimeMillis();
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
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
				return resolvedTStamp < tStamp;
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage(
						"Couldn't determine whether facet has changed or not: "
								+ e.getMessage(), e);
			}
		}
		return false;
	}

	public IFacetPredicate getFacetPredicate() {
		if (needsToBeEvaluated())
			return computeFacetPredicate(new NullProgressMonitor());

		return facetPredicate;
	}

	public boolean needsToBeEvaluated() {
		return facetPredicate == null || hasFacetChanged() || modelHasChanged();
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
		handleNeedToReevaluate();
	}

	public void artifactChanged(IAbstractArtifact artifact, IAbstractArtifact oldArtifact) {
		handleNeedToReevaluate();
	}

	public void artifactRemoved(IAbstractArtifact artifact) {
		handleNeedToReevaluate();
		if (artifact instanceof IRelationship) {
			handleRelationshipRemoved(artifact);
		}
	}

	public void artifactRenamed(IAbstractArtifact artifact, String fromFQN) {
		handleNeedToReevaluate();
	}

	public void managerReloaded() {
		handleNeedToReevaluate();
	}

	private void handleNeedToReevaluate() {
		// TODO
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
					activeMgr.setActiveFacet(this, new NullProgressMonitor());
					this.computedTStamp = System.currentTimeMillis();
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

	public static FacetReference decode(Node facetElement,
			TigerstripeProject project) throws TigerstripeException {
		NamedNodeMap namedAttributes = facetElement.getAttributes();
		Node uriNode = namedAttributes.getNamedItem("uri");
		Node relPath = namedAttributes.getNamedItem("relPath");
		Node genDir = namedAttributes.getNamedItem("genDir");
		Node projectLabel = namedAttributes.getNamedItem("project");
		String uriStr = null;
		String genDirStr = null;
		String relPathStr = null;
		String projectLabelStr = null;
		if (uriNode != null)
			uriStr = uriNode.getNodeValue();

		if (genDir != null) {
			genDirStr = Util.fixWindowsPath(genDir.getNodeValue());
		}

		if (relPath != null) {
			relPathStr = Util.fixWindowsPath(relPath.getNodeValue());
		}

		if (projectLabel != null) {
			projectLabelStr = projectLabel.getNodeValue();
		}

		try {
			FacetReference ref = null;
			if (uriStr != null) {
				URI uri = new URI(uriStr);
				ref = new FacetReference(uri, project.getTSProject());
			} else if (relPathStr != null) {

				if (projectLabelStr != null) {
					ref = new FacetReference(relPathStr, projectLabelStr,
							project.getTSProject());
				} else {
					ref = new FacetReference(relPathStr, project);
				}
			}
			if (ref != null) {
				ref.setGenerationDir(genDirStr);
				return ref;
			}
		} catch (URISyntaxException e) {
			throw new TigerstripeException(
					"Error while trying to parse facet reference: "
							+ e.getLocalizedMessage() + " ("
							+ facetElement.toString() + ")", e);
		}
		throw new TigerstripeException("Can't parse facet Reference: "
				+ facetElement.toString());
	}

	public static Element encode(IFacetReference ref, Document document,
			TigerstripeProject project) {
		Element refElm = document.createElement(TigerstripeProject.FACET_TAG);
		if (ref.isAbsolute()) {
			try {
				refElm.setAttribute("uri", ref.getURI().toASCIIString());
			} catch (TigerstripeException e) {
				TigerstripeRuntime.logErrorMessage(
						"TigerstripeException detected", e);
			}
		} else {
			refElm.setAttribute("relPath", Util.fixWindowsPath(ref
					.getProjectRelativePath()));
			if (ref.getContainingProject() != null
					&& !ref.getContainingProject().getName().equals(
							project.getProjectLabel())) {
				refElm.setAttribute("project", ref.getContainingProject()
						.getName());
			}
		}
		refElm.setAttribute("genDir", Util.fixWindowsPath(ref
				.getGenerationDir()));

		return refElm;
	}

	public IFacetReference clone() {
		return new FacetReference(this);
	}

	/**
	 * Returns true if the model was changed since the last time this facet was
	 * evaluated. If it can't be determined the return will be true
	 * 
	 * @return
	 */
	protected boolean modelHasChanged() {
		try {
			if (getTSProject() instanceof TigerstripeOssjProjectHandle) {
				IArtifactManagerSession session = ((TigerstripeOssjProjectHandle) getTSProject())
						.getArtifactManagerSession();
				return session.getLocalTimeStamp() > computedTStamp;
			}
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}
		// couldn't figure out if the model changed, so to be safe let's say
		// it did
		// so the facet will be reevaluated.
		return true;
	}
}
