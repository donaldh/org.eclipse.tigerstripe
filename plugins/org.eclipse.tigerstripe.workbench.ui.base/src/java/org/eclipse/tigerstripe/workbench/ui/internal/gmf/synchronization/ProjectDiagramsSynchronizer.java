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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.internal.jobs.Queue;
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
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactChangeListener;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeListener;
import org.eclipse.tigerstripe.workbench.internal.builder.TigerstripeProjectAuditor;
import org.eclipse.tigerstripe.workbench.internal.builder.WorkspaceHelper;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.preferences.ITigerstripePreferences;
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
				MultiStatus mStatus = new MultiStatus(EclipsePlugin
						.getPluginId(), 222, statuses
						.toArray(new IStatus[statuses.size()]),
						"Error occured while synchronizing diagrams", null);
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
				MultiStatus mStatus = new MultiStatus(EclipsePlugin
						.getPluginId(), 222, statuses
						.toArray(new IStatus[statuses.size()]),
						"Error occured while synchronizing diagrams", null);
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
				MultiStatus mStatus = new MultiStatus(EclipsePlugin
						.getPluginId(), 222, statuses
						.toArray(new IStatus[statuses.size()]),
						"Error occured while synchronizing diagrams", null);
				return mStatus;
			}
		}

	}

	private ITigerstripeModelProject project;

	private DiagramHandleIndex diagramIndex = new DiagramHandleIndex(this);

	private HashMap<IResource, DiagramHandle> handlesByDiagram = new HashMap<IResource, DiagramHandle>();

	private HashMap<IResource, DiagramHandle> handlesByModel = new HashMap<IResource, DiagramHandle>();

	private final String[] diagramFileExtensions = {
			ClassDiagramLogicalNode.DIAG_EXT,
			InstanceDiagramLogicalNode.DIAG_EXT }; // FIXME: use extension

	private final String[] modelFileExtensions = {
			ClassDiagramLogicalNode.MODEL_EXT,
			InstanceDiagramLogicalNode.MODEL_EXT }; // FIXME: use extension

	private Job batchSyncJob;

	/* package */ProjectDiagramsSynchronizer(ITigerstripeModelProject project) {
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
					schedule(300); // start again in 300ms
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

	public IStatus flushRequestQueue(boolean applyRequests,
			IProgressMonitor monitor) {
		while (!requestQueue.isEmpty()) {
			try {
				SynchronizationRequest request = (SynchronizationRequest) requestQueue
						.dequeue();
				request.run(new NullProgressMonitor());
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
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
	 * Register self as a listener for model changes both as a
	 * {@link IModelChangeListener} and an {@link IArtifactChangeListener}.
	 * 
	 */
	private void registerSelfForChanges() {
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

			ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
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
		// TODO Auto-generated method stub

	}

	public void artifactChanged(IAbstractArtifact artifact) {
		try {
			DiagramHandle[] affectedDiagrams = getAffectedDiagrams(artifact
					.getFullyQualifiedName());
			SynchronizationForArtifactChangedRequest request = new SynchronizationForArtifactChangedRequest(
					artifact, affectedDiagrams);
			queueUpSynchronizationRequest(request);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	public void artifactRemoved(IAbstractArtifact artifact) {
		try {
			DiagramHandle[] affectedDiagrams = getAffectedDiagrams(artifact
					.getFullyQualifiedName());
			SynchronizationForArtifactRemovedRequest request = new SynchronizationForArtifactRemovedRequest(
					artifact.getFullyQualifiedName(), affectedDiagrams);
			queueUpSynchronizationRequest(request);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	public void artifactRenamed(IAbstractArtifact artifact, String fromFQN) {
		try {
			DiagramHandle[] affectedDiagrams = getAffectedDiagrams(fromFQN);
			SynchronizationForArtifactRenamedRequest request = new SynchronizationForArtifactRenamedRequest(
					fromFQN, artifact.getFullyQualifiedName(), affectedDiagrams);
			queueUpSynchronizationRequest(request);
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
	private DiagramHandle[] getAffectedDiagrams(String targetFQN)
			throws TigerstripeException {
		List<DiagramHandle> result = new ArrayList<DiagramHandle>();
		Set<DiagramHandle> candidates = diagramIndex.get(targetFQN);

		for (DiagramHandle handle : candidates) {
			IResource resource = handle.getDiagramResource();
			if (resource.getProjectRelativePath().matchingFirstSegments(
					new Path("bin")) == 1) {
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
			// Ignore the diagrams that are in the "/bin" dir. These are simply
			// being copied by Eclipse by the Java builder. No need to track
			if (diagramResource.getProjectRelativePath().matchingFirstSegments(
					new Path("bin")) == 1) {
				// ignore whatever is in "bin"
				continue;
			} else {
				monitor.subTask(diagramResource.getName());
				IResource modelResource = getModelResource(diagramResource);
				DiagramHandle handle = new DiagramHandle(diagramResource,
						modelResource, project);
				handlesByDiagram.put(diagramResource, handle);
				handlesByModel.put(modelResource, handle);
				try {
					diagramIndex.diagramSaved(handle);
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);// log and keep going
				}
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
		if (getProject().getAdapter(IProject.class) == null) {
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
		WorkspaceHelper.buildResourcesLists(event.getDelta(), removedResources,
				changedResources, addedResources, null);

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
			for (String ext : diagramFileExtensions) {
				if (ext.equals(resource.getFileExtension())
						&& resource.getProject().equals(proj)
						&& !(resource.getProjectRelativePath()
								.matchingFirstSegments(new Path("bin")) == 1)) {
					IResource modelResource = getModelResource(resource);
					DiagramHandle handle = new DiagramHandle(resource,
							modelResource, project);
					handlesByDiagram.put(resource, handle);
					handlesByModel.put(modelResource, handle);
					diagramIndex.diagramSaved(handle);
				}
			}
		}
	}

	private void checkForRemovedDiagrams(Collection<IResource> removedResources)
			throws TigerstripeException {
		for (IResource resource : removedResources) {
			if (handlesByDiagram.get(resource) != null) {
				handlesByDiagram.remove(resource);
			} else if (handlesByModel.get(resource) != null) {
				DiagramHandle handle = handlesByModel.remove(resource);
				diagramIndex.diagramDeleted(handle);
			}
		}
	}

	private void checkForChangedDiagrams(Collection<IResource> changedResources)
			throws TigerstripeException {
		for (IResource resource : changedResources) {
			if (handlesByModel.get(resource) != null) {
				DiagramHandle handle = handlesByModel.get(resource);
				diagramIndex.diagramSaved(handle);
			}
		}
	}
}
