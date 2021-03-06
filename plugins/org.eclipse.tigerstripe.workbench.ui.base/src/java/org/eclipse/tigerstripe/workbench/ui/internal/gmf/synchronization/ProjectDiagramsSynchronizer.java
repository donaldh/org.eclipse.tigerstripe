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
package org.eclipse.tigerstripe.workbench.ui.internal.gmf.synchronization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.internal.jobs.Queue;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceRuleFactory;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.MultiRule;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactChangeListener;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeListener;
import org.eclipse.tigerstripe.workbench.internal.builder.TigerstripeProjectAuditor;
import org.eclipse.tigerstripe.workbench.internal.builder.WorkspaceHelper;
import org.eclipse.tigerstripe.workbench.internal.builder.WorkspaceHelper.IResourceFilter;
import org.eclipse.tigerstripe.workbench.internal.tools.compare.Comparer;
import org.eclipse.tigerstripe.workbench.internal.tools.compare.Difference;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.ClassDiagramLogicalNode;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.InstanceDiagramLogicalNode;
import org.eclipse.ui.IEditorPart;

/**
 * 
 * A Project Diagram Synchronizer is attached to each ITigerstripeProject by the
 * DiagramSynchronizationManager and is delegated the synchronization of all
 * diagrams in the project.
 * 
 * @author erdillon
 * @since Bug 936
 */
public class ProjectDiagramsSynchronizer implements IArtifactChangeListener,
		IResourceChangeListener {

	protected Queue requestQueue = new Queue();
	private Comparer comp = new Comparer();

	// Each time a change occurs, this synchronizer gets notified and queues
	// queues up the corresponding synchronization request to be executed.
	// The requests are being picked up in a single separate job for this
	// synchronizer.
	abstract class SynchronizationRequest {
		protected DiagramHandle[] affectedDiagrams;

		protected SynchronizationRequest(DiagramHandle[] affectedDiagrams) {
			this.affectedDiagrams = affectedDiagrams;
		}

		public abstract IStatus run(IProgressMonitor monitor)
				throws TigerstripeException;
	}

	class SynchronizationForArtifactChangedRequest extends
			SynchronizationRequest {
		private IAbstractArtifact artifact;

		public SynchronizationForArtifactChangedRequest(
				IAbstractArtifact artifact, DiagramHandle[] affectedDiagrams) {
			super(affectedDiagrams);
			this.artifact = artifact;
		}

		public IStatus run(IProgressMonitor monitor)
				throws TigerstripeException {
			List<IStatus> statuses = new ArrayList<IStatus>();
			for (DiagramHandle handle : affectedDiagrams) {
				try {
					handle.updateForChange(artifact);
				} catch (TigerstripeException e) {
					statuses.add(new Status(IStatus.ERROR, EclipsePlugin
							.getPluginId(), 222, e.getMessage(), e));
				}
			}
			if (statuses.size() == 0)
				return Status.OK_STATUS;
			else {
				MultiStatus mStatus = new MultiStatus(
						EclipsePlugin.getPluginId(), 222,
						statuses.toArray(new IStatus[statuses.size()]),
						"Error occurred while synchronizing diagrams", null);
				return mStatus;
			}
		}
	}

	class SynchronizationForArtifactRemovedRequest extends
			SynchronizationRequest {
		protected String removedFQN;

		public SynchronizationForArtifactRemovedRequest(String removedFQN,
				DiagramHandle[] affectedDiagrams) {
			super(affectedDiagrams);
			this.removedFQN = removedFQN;
		}

		public IStatus run(IProgressMonitor monitor) {
			List<IStatus> statuses = new ArrayList<IStatus>();
			for (DiagramHandle handle : affectedDiagrams) {
				try {
					handle.updateForRemove(removedFQN);
				} catch (TigerstripeException e) {
					statuses.add(new Status(IStatus.ERROR, EclipsePlugin
							.getPluginId(), 222, e.getMessage(), e));
				}
			}
			if (statuses.size() == 0)
				return Status.OK_STATUS;
			else {
				MultiStatus mStatus = new MultiStatus(
						EclipsePlugin.getPluginId(), 222,
						statuses.toArray(new IStatus[statuses.size()]),
						"Error occurred while synchronizing diagrams", null);
				return mStatus;
			}
		}

	}

	class SynchronizationForArtifactRenamedRequest extends
			SynchronizationRequest {
		protected String oldFQN;
		protected String newFQN;

		public SynchronizationForArtifactRenamedRequest(String oldFQN,
				String newFQN, DiagramHandle[] affectedDiagrams) {
			super(affectedDiagrams);
			this.oldFQN = oldFQN;
			this.newFQN = newFQN;
		}

		public IStatus run(IProgressMonitor monitor) {
			List<IStatus> statuses = new ArrayList<IStatus>();
			for (DiagramHandle handle : affectedDiagrams) {
				try {
					handle.updateForRename(oldFQN, newFQN);
				} catch (TigerstripeException e) {
					statuses.add(new Status(IStatus.ERROR, EclipsePlugin
							.getPluginId(), 222, e.getMessage(), e));
				}
			}
			if (statuses.size() == 0)
				return Status.OK_STATUS;
			else {
				MultiStatus mStatus = new MultiStatus(
						EclipsePlugin.getPluginId(), 222,
						statuses.toArray(new IStatus[statuses.size()]),
						"Error occurred while synchronizing diagrams", null);
				return mStatus;
			}
		}

	}

	private ITigerstripeModelProject project;

	private DiagramHandleIndex diagramIndex = new DiagramHandleIndex(this);

	private HashMap<IResource, DiagramHandle> handlesByDiagram = new HashMap<IResource, DiagramHandle>();

	private final String[] diagramFileExtensions = {
			ClassDiagramLogicalNode.DIAG_EXT,
			InstanceDiagramLogicalNode.DIAG_EXT }; // FIXME: use extension

	private final String[] modelFileExtensions = {
			ClassDiagramLogicalNode.MODEL_EXT,
			InstanceDiagramLogicalNode.MODEL_EXT }; // FIXME: use extension

	private Job batchSyncJob;
	private boolean enabled = true;

	ProjectDiagramsSynchronizer(ITigerstripeModelProject project) {
		this.project = project;
	}

	public void initialize() {
		// Create batch synchronization job
		batchSyncJob = new Job("Diagram Synchronization (" + project.getName()
				+ ")") {
			protected IStatus run(IProgressMonitor monitor) {
				if (monitor.isCanceled()) {
					return Status.CANCEL_STATUS;
				}
				try {
					if (!DiagramSynchronizationManager.getInstance()
							.isSynchronizationHeld())
						return flushRequestQueue(true, monitor);
					return Status.OK_STATUS;
				} finally {
					schedule(1000); // start again...
				}
			}
		};
		batchSyncJob.setSystem(true);
		batchSyncJob.schedule(); // start as soon as possible

		registerSelfForChanges();
		try {
			Job initialIndexing = new Job("Indexing diagrams in "
					+ getProject().getName()) {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					initialize(monitor);
					return Status.OK_STATUS;
				}
			};

			initialIndexing.setRule(createInitialRule());
			initialIndexing.schedule();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	public synchronized IStatus flushRequestQueue(boolean applyRequests,
			IProgressMonitor monitor) {

		Set<DiagramHandle> affectedDiagrams = new HashSet<DiagramHandle>();
		while (!requestQueue.isEmpty()) {
			try {
				SynchronizationRequest request = (SynchronizationRequest) requestQueue
						.dequeue();
				request.run(new NullProgressMonitor());
				if (request.affectedDiagrams.length != 0) {
					affectedDiagrams.addAll(Arrays
							.asList(request.affectedDiagrams));
				}
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}
		}

		if (affectedDiagrams.size() != 0) {
			for (DiagramHandle handle : affectedDiagrams)
				try {
					diagramIndex.diagramSaved(handle);
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
		}
		return Status.OK_STATUS;
	}

	private ISchedulingRule createInitialRule() throws TigerstripeException {
		List<IResource> modelFiles = getAllModelFiles();
		ISchedulingRule combinedRule = null;
		IResourceRuleFactory ruleFactory = ResourcesPlugin.getWorkspace()
				.getRuleFactory();
		for (IResource modelFile : modelFiles) {
			ISchedulingRule rule = ruleFactory.createRule(modelFile);
			combinedRule = MultiRule.combine(rule, combinedRule);
		}
		return combinedRule;
	}

	/**
	 * Go thru all diagrams and create the DiagramHandles and synchronizer as
	 * requried
	 * 
	 */
	private void initialize(IProgressMonitor monitor) {
		try {
			createAllDiagramHandles(monitor);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	/**
	 * A hook to allow for any clean up needed.
	 * 
	 */
	public void dispose() throws TigerstripeException {
		deregisterSelfForChanges();
		batchSyncJob.cancel();
	}

	public ITigerstripeModelProject getProject() {
		return this.project;
	}

	// ==============================

	/**
	 * Ideally we should only register if we have any diagrams. ie If my project
	 * has no diagrams then I don't really care about changes!
	 * 
	 * Not clear how this would ever get updated for changes in
	 * ProjectReferences. Should we be implement
	 * ProjectDependencyChangeListener?
	 * 
	 * Register self as a listener for model changes both as a
	 * {@link IModelChangeListener} and an {@link IArtifactChangeListener}.
	 * 
	 */
	private void registerSelfForChanges() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);

		try {
			IArtifactManagerSession session = getProject()
					.getArtifactManagerSession();
			session.addArtifactChangeListener(this);

			// register for changes in all parent projects
			for (ITigerstripeModelProject proj : getProject()
					.getReferencedProjects()) {
				proj.getArtifactManagerSession()
						.addArtifactChangeListener(this);
			}

		} catch (TigerstripeException e) {
			IStatus status = new Status(
					IStatus.ERROR,
					EclipsePlugin.getPluginId(),
					222,
					"An error occured while trying to register ProjectDiagramSynchronizer. Diagrams may not be synchronized properly for project: "
							+ getProject().getName(), e);
			EclipsePlugin.log(status);
		}
	}

	private void deregisterSelfForChanges() throws TigerstripeException {
		IArtifactManagerSession session = getProject()
				.getArtifactManagerSession();
		session.removeArtifactChangeListener(this);
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
	}

	protected void queueUpSynchronizationRequest(SynchronizationRequest request) {
		requestQueue.enqueue(request);
	}

	// Artifact Change listener
	public void artifactAdded(IAbstractArtifact artifact) {
		// Artifacts can't be on any diagrams if they never existed before!

	}

	public void artifactChanged(IAbstractArtifact artifact,
			IAbstractArtifact oldArtifact) {
		if (!enabled) {
			return;
		}
		// compare the old and new first
		// This moight be better done in the client, so that they will only
		// bother with
		// changes that interest them
		if (handlesByDiagram.size() == 0) {
			return;
		}
		try {
			DiagramHandle[] affectedDiagrams = getAffectedDiagrams(artifact
					.getFullyQualifiedName());
			if (affectedDiagrams.length > 0) {
				ArrayList<Difference> diffs = comp.compareArtifacts(
						oldArtifact, artifact, true);
				if (diffs.size() > 0) {
					SynchronizationForArtifactChangedRequest request = new SynchronizationForArtifactChangedRequest(
							artifact, affectedDiagrams);
					queueUpSynchronizationRequest(request);
				}
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	public void artifactRemoved(IAbstractArtifact artifact) {
		if (!enabled) {
			return;
		}
		try {
			DiagramHandle[] affectedDiagrams = getAffectedDiagrams(artifact
					.getFullyQualifiedName());
			if (affectedDiagrams.length > 0) {
				SynchronizationForArtifactRemovedRequest request = new SynchronizationForArtifactRemovedRequest(
						artifact.getFullyQualifiedName(), affectedDiagrams);
				queueUpSynchronizationRequest(request);
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	public void artifactRenamed(IAbstractArtifact artifact, String fromFQN) {
		if (!enabled) {
			return;
		}
		try {
			DiagramHandle[] affectedDiagrams = getAffectedDiagrams(fromFQN);
			if (affectedDiagrams.length > 0) {
				SynchronizationForArtifactRenamedRequest request = new SynchronizationForArtifactRenamedRequest(
						fromFQN, artifact.getFullyQualifiedName(),
						affectedDiagrams);
				queueUpSynchronizationRequest(request);
				// Bug 327698 - Need to make sure any internal references in the
				// artifact to itself gets updated
				SynchronizationForArtifactChangedRequest changeRequest = new SynchronizationForArtifactChangedRequest(
						artifact, affectedDiagrams);
				queueUpSynchronizationRequest(changeRequest);
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	public void managerReloaded() {
		// TODO Auto-generated method stub

	}

	/**
	 * Returns an array of DiagramHandle that are affected by a change on
	 * targetFQN
	 * 
	 * @param targetFQN
	 * @return
	 */
	public DiagramHandle[] getAffectedDiagrams(String targetFQN)
			throws TigerstripeException {
		List<DiagramHandle> result = new ArrayList<DiagramHandle>();
		Set<DiagramHandle> candidates = diagramIndex.get(targetFQN);

		for (DiagramHandle handle : candidates) {
			IResource resource = handle.getDiagramResource();
			if (isBinResource(resource)) {
				// ignore whatever is in "bin"
				continue;
			} else {
				IEditorPart[] parts = EclipsePlugin
						.getEditorPartsForResource(resource);
				if (parts.length != 0) {
					continue;
				}
			}
			result.add(handle);
		}

		return result.toArray(new DiagramHandle[result.size()]);
	}

	public Set<IEditorPart> getOpenedDiagrams(String targetFQN) {
		Set<IEditorPart> result = new HashSet<IEditorPart>();

		for (DiagramHandle handle : diagramIndex.get(targetFQN)) {
			IResource resource = handle.getDiagramResource();
			if (isBinResource(resource)) {
				continue;
			}
			IEditorPart[] parts = EclipsePlugin
					.getEditorPartsForResource(resource);
			for (IEditorPart part : parts) {
				result.add(part);
			}
		}
		return result;
	}

	private boolean isBinResource(IResource resource) {
		return resource.getProjectRelativePath().matchingFirstSegments(
				new Path("bin")) == 1;
	}

	private List<IResource> getAllModelFiles() throws TigerstripeException {
		List<IResource> allResources = new ArrayList<IResource>();
		IProject iProject = (IProject) getProject().getAdapter(IProject.class);
		for (String extension : modelFileExtensions) {
			allResources.addAll(TigerstripeProjectAuditor.findAll(iProject,
					extension));
		}
		return allResources;
	}

	private void createAllDiagramHandles(IProgressMonitor monitor)
			throws TigerstripeException {
		// For now, find all diagrams and remove those that are open
		List<IResource> allResources = new ArrayList<IResource>();
		IProject iProject = (IProject) getProject().getAdapter(IProject.class);
		for (String extension : diagramFileExtensions) {
			allResources.addAll(TigerstripeProjectAuditor.findAll(iProject,
					extension));
		}

		monitor.beginTask("Indexing", allResources.size());
		for (IResource diagramResource : allResources) {
			monitor.subTask(diagramResource.getName());
			IResource modelResource = getModelResource(diagramResource);
			DiagramHandle handle = new DiagramHandle(diagramResource,
					modelResource, project);
			handlesByDiagram.put(diagramResource, handle);
			try {
				diagramIndex.diagramSaved(handle);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);// log and keep going
			}
			monitor.worked(1);
		}
		monitor.done();
	}

	private IResource getModelResource(IResource diagramResource)
			throws TigerstripeException {
		for (int i = 0; i < diagramFileExtensions.length; i++) {
			if (diagramFileExtensions[i].equals(diagramResource
					.getFileExtension())) {
				IPath path = diagramResource.getProjectRelativePath();
				path = path.removeFileExtension();
				path = path.addFileExtension(modelFileExtensions[i]);
				IResource res = diagramResource.getProject().findMember(path);
				if (res != null)
					return res;
			}
		}
		throw new TigerstripeException("Can't find model resource for "
				+ diagramResource.getFullPath());
	}

	public void resourceChanged(IResourceChangeEvent event) {
		if (!enabled) {
			return;
		}
		final IProject project = (IProject) getProject().getAdapter(
				IProject.class);
		if (project == null) {
			return;
			// this means the project has been deleted and the hash is being
			// updated. This avoids problem with the race condition between
			// the time this is notified and this has removed itself from
			// the list of ResourceChange listeners.
		}

		// Get the list of removed resources
		Collection<IResource> removedResources = new HashSet<IResource>();
		Collection<IResource> changedResources = new HashSet<IResource>();
		Collection<IResource> addedResources = new HashSet<IResource>();
		// We only care about diagram files
		IResourceFilter diagramFilesFilter = new IResourceFilter() {
			public boolean select(IResource resource) {
				if (!project.equals(resource.getProject()) || !(resource instanceof IFile)) {
					return false;
				}

				try {
					IJavaProject javaProject = (IJavaProject) getProject().getAdapter(IJavaProject.class);
					IPath output = javaProject.getOutputLocation();
					IPath resPath = resource.getFullPath();
					if(resPath.toString().startsWith(output.toString())) {
						return false;
					}
				} catch (JavaModelException ex) {
					// Ignore
				}

				return Arrays.asList(diagramFileExtensions).contains(
						resource.getFileExtension());
			}
		};

		WorkspaceHelper.buildResourcesLists(event.getDelta(), removedResources,
				changedResources, addedResources, diagramFilesFilter);

		try {
			checkForAddedDiagrams(addedResources);
			checkForRemovedDiagrams(removedResources);
			checkForChangedDiagrams(changedResources);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	private void checkForAddedDiagrams(Collection<IResource> addedResources)
			throws TigerstripeException {
		IProject proj = (IProject) getProject().getAdapter(IProject.class);
		for (IResource resource : addedResources) {
			// System.out.println("Ad "+resource.getName());
			for (String ext : diagramFileExtensions) {
				if (ext.equals(resource.getFileExtension())
						&& resource.getProject().equals(proj)
						&& !(isBinResource(resource))) {
					IResource modelResource = getModelResource(resource);
					DiagramHandle handle = new DiagramHandle(resource,
							modelResource, project);
					handlesByDiagram.put(resource, handle);
					diagramIndex.diagramSaved(handle);
				}
			}
		}
	}

	private void checkForRemovedDiagrams(Collection<IResource> removedResources)
			throws TigerstripeException {
		for (IResource resource : removedResources) {
			if (handlesByDiagram.get(resource) != null) {
				DiagramHandle handle = handlesByDiagram.remove(resource);
				diagramIndex.diagramDeleted(handle);
			}
		}
	}

	private void checkForChangedDiagrams(Collection<IResource> changedResources)
			throws TigerstripeException {
		for (IResource resource : changedResources) {
			if (handlesByDiagram.get(resource) != null) {
				diagramIndex.diagramSaved(handlesByDiagram.get(resource));
			}
		}
	}

	public void setEnabled(boolean value) {
		this.enabled = value;

	}
}