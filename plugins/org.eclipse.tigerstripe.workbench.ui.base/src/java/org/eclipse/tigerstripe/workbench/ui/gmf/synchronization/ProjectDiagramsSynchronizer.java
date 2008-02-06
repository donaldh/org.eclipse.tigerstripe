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
package org.eclipse.tigerstripe.workbench.ui.gmf.synchronization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceRuleFactory;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.MultiRule;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactChangeListener;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeListener;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.eclipse.builder.TigerstripeProjectAuditor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.builder.WorkspaceListener;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.abstraction.ClassDiagramLogicalNode;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.abstraction.InstanceDiagramLogicalNode;
import org.eclipse.ui.IEditorPart;

/**
 * 
 * A Project Diagram Synchronizer is attached to each ITigerstripeProject by the
 * DiagramSynchronizationManager and is delegated the synchronization of all
 * diagrams in the project.
 * 
 * @author eric
 * @since Bug 936
 */
public class ProjectDiagramsSynchronizer implements IArtifactChangeListener,
		IResourceChangeListener {

	private ITigerstripeProject project;

	private DiagramHandleIndex diagramIndex = new DiagramHandleIndex(this);

	private HashMap<IResource, DiagramHandle> handlesByDiagram = new HashMap<IResource, DiagramHandle>();

	private HashMap<IResource, DiagramHandle> handlesByModel = new HashMap<IResource, DiagramHandle>();

	private final String[] diagramFileExtensions = {
			ClassDiagramLogicalNode.DIAG_EXT,
			InstanceDiagramLogicalNode.DIAG_EXT }; // FIXME: use extension

	private final String[] modelFileExtensions = {
			ClassDiagramLogicalNode.MODEL_EXT,
			InstanceDiagramLogicalNode.MODEL_EXT }; // FIXME: use extension

	/* package */ProjectDiagramsSynchronizer(ITigerstripeProject project) {
		this.project = project;
	}

	public void initialize() {
		registerSelfForChanges();
		try {
			Job initialIndexing = new Job("Indexing diagrams in "
					+ getProject().getProjectLabel()) {
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
	}

	public ITigerstripeProject getProject() {
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
			ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
		} catch (TigerstripeException e) {
			IStatus status = new Status(
					IStatus.ERROR,
					TigerstripePluginConstants.PLUGIN_ID,
					222,
					"An error occured while trying to register ProjectDiagramSynchronizer. Diagrams may not be synchronized properly for project: "
							+ getProject().getProjectLabel(), e);
			EclipsePlugin.log(status);
		}
	}

	private void deregisterSelfForChanges() throws TigerstripeException {
		IArtifactManagerSession session = getProject()
				.getArtifactManagerSession();
		session.removeArtifactChangeListener(this);
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
	}

	// Artifact Change listener
	public void artifactAdded(IAbstractArtifact artifact) {
		// TODO Auto-generated method stub

	}

	public void artifactChanged(IAbstractArtifact artifact) {
		handleArtifactChanged(artifact);
	}

	public void artifactRemoved(IAbstractArtifact artifact) {
		handleArtifactRemoved(artifact.getFullyQualifiedName());
	}

	public void artifactRenamed(IAbstractArtifact artifact, String fromFQN) {
		handleArtifactRenamed(fromFQN, artifact.getFullyQualifiedName());
	}

	public void managerReloaded() {
		// TODO Auto-generated method stub

	}

	private void handleArtifactChanged(IAbstractArtifact artifact) {
		try {
			// This needs to run in its own thread to avoid slowness on GUI
			final IAbstractArtifact fArtifact = artifact;
			final DiagramHandle[] affectedDiagrams = getAffectedDiagrams(fArtifact
					.getFullyQualifiedName());
			final Job job = new Job("Diagram synchronization ("
					+ getProject().getProjectLabel() + ")") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						for (DiagramHandle handle : affectedDiagrams) {
							// System.out
							// .println("Updating for change ("
							// + fArtifact.getFullyQualifiedName()
							// + "): "
							// + handle.getDiagramResource()
							// .getFullPath());
							handle.updateForChange(fArtifact);
						}
						return Status.OK_STATUS;
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
						return new Status(IStatus.ERROR,
								TigerstripePluginConstants.PLUGIN_ID, 222, e
										.getMessage(), e);
					}
				}
			};

			job.setRule(createRule(affectedDiagrams));
			job.schedule();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	private void handleArtifactRemoved(String targetFQN) {
		// This needs to run in its own thread to avoid slowness on GUI
		try {
			final String fTargetFQN = targetFQN;
			final DiagramHandle[] affectedDiagrams = getAffectedDiagrams(targetFQN);
			final Job job = new Job("Diagram synchronization ("
					+ getProject().getProjectLabel() + ")") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						for (DiagramHandle handle : affectedDiagrams) {
							// System.out
							// .println("Updating for remove ("
							// + fTargetFQN
							// + "): "
							// + handle.getDiagramResource()
							// .getFullPath());
							handle.updateForRemove(fTargetFQN);
						}
						return Status.OK_STATUS;
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
						return new Status(IStatus.ERROR,
								TigerstripePluginConstants.PLUGIN_ID, 222, e
										.getMessage(), e);
					}
				}
			};

			job.setRule(createRule(affectedDiagrams));
			job.schedule();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	private void handleArtifactRenamed(final String oldFQN, final String newFQN) {
		// This needs to run in its own thread to avoid slowness on GUI
		// yet make sure we use the proper scheduling rule (Bug 940)
		try {
			final DiagramHandle[] affectedDiagrams = getAffectedDiagrams(oldFQN);
			final Job job = new Job("Diagram synchronization ("
					+ getProject().getProjectLabel() + ")") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						for (DiagramHandle handle : affectedDiagrams) {
							// System.out
							// .println("Updating for rename ("
							// + oldFQN
							// + "): "
							// + handle.getDiagramResource()
							// .getFullPath());
							handle.updateForRename(oldFQN, newFQN);
						}
						return Status.OK_STATUS;
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
						return new Status(IStatus.ERROR,
								TigerstripePluginConstants.PLUGIN_ID, 222, e
										.getMessage(), e);
					}
				}
			};

			job.setRule(createRule(affectedDiagrams));
			job.schedule();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	public ISchedulingRule createRule(DiagramHandle[] handles) {
		ISchedulingRule combinedRule = null;
		IResourceRuleFactory ruleFactory = ResourcesPlugin.getWorkspace()
				.getRuleFactory();
		for (DiagramHandle handle : handles) {
			ISchedulingRule rule = ruleFactory.createRule(handle
					.getModelResource());
			combinedRule = MultiRule.combine(rule, combinedRule);
		}
		return combinedRule;
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
		IProject iProject = EclipsePlugin.getIProject(getProject());
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
		IProject iProject = EclipsePlugin.getIProject(getProject());
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
		try {
			EclipsePlugin.getIProject(getProject());
		} catch (TigerstripeException e) {
			// this means the project has been deleted and the hash is being
			// updated. This avoids problem with the race condition between
			// the time this is notified and this has removed itself from
			// the list of ResourceChange listeners.
			return;
		}

		// Get the list of removed resources
		Collection<IResource> removedResources = new HashSet<IResource>();
		Collection<IResource> changedResources = new HashSet<IResource>();
		Collection<IResource> addedResources = new HashSet<IResource>();
		WorkspaceListener.buildResourcesLists(event.getDelta(),
				removedResources, changedResources, addedResources);

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
		IProject proj = EclipsePlugin.getIProject(getProject());
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
