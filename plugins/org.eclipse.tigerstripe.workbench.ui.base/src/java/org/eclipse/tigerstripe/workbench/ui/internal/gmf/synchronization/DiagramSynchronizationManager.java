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

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.IModelAnnotationChangeDelta;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.ITigerstripeChangeListener;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequest;
import org.eclipse.tigerstripe.workbench.internal.builder.WorkspaceHelper;
import org.eclipse.tigerstripe.workbench.internal.builder.WorkspaceListener;
import org.eclipse.tigerstripe.workbench.internal.builder.WorkspaceHelper.IResourceFilter;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeWorkspaceNotifier;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.refactor.diagrams.DiagramSynchronizerController;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;


/**
 * The Diagram Synchronization Manager is the heart of diagram synchronization.
 * It contains the logic that ensures diagrams are refreshed properly upon model
 * changes.
 * 
 * This is a singleton.
 * 
 * Because the actual diagram synchronizers themselves live in separate plugins,
 * this relies on an extension mechanism to figure out what synchronizer to use
 * for what diagram.
 * 
 * The manager is notified of model changes thru 2 mechanisms: - as a result of
 * a {@link IModelChangeRequest} being fired, for example coming from a class
 * diagram being edited. - as a result of a 'refactor' operation performed by
 * the user on the TS Explorer. .
 * 
 * The manager is set to watch ITigerstripeProjects. As Eclipse starts up and
 * the auditors are being instantiated per project, this is notified with each
 * project so it can be added to the list to watch. When a project is removed it
 * is taken off the list of projects to watch.
 * 
 * @Since Bug 936
 * 
 */
public class DiagramSynchronizationManager extends
		DiagramSynchronizerController implements ITigerstripeChangeListener {

	private static DiagramSynchronizationManager instance;

	private HashMap<String, ProjectDiagramsSynchronizer> projectWatchHash = new HashMap<String, ProjectDiagramsSynchronizer>();

	private boolean hold = false;

	private DiagramSynchronizationManager() {
		// making sure this is a singleton.
	}

	public static DiagramSynchronizationManager getInstance() {
		if (instance == null) {
			instance = new DiagramSynchronizationManager();
			instance.initialize();
		}

		return instance;
	}

	/**
	 * Upon start make sure we discover all existing projects
	 */
	private void initialize() {
		// register with the base.
		DiagramSynchronizerController.registerController(this);

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		try {
			addProjects(Arrays.asList(root.members()));
		} catch (CoreException e) {
			EclipsePlugin.log(e);
		}
	}

//	public void resourceChanged(IResourceChangeEvent event) {
//		// Get the list of removed resources
//		Collection<IResource> removedResources = new HashSet<IResource>();
//		Collection<IResource> changedResources = new HashSet<IResource>();
//		Collection<IResource> addedResources = new HashSet<IResource>();
//		IResourceFilter projectOnly = new IResourceFilter() {
//
//			public boolean select(IResource resource) {
//				if (!(resource instanceof IProject))
//					return false;
//				return true;
//			}
//
//		};
//
//		WorkspaceHelper.buildResourcesLists(event.getDelta(), removedResources,
//				changedResources, addedResources, projectOnly);
//
//		checkProjectAdded(addedResources);
//		checkProjectRemoved(removedResources);
//	}

	private void addProjects(Collection<IResource> addedResources) {
		for (IResource res : addedResources) {
			if (res instanceof IProject) {
				IAbstractTigerstripeProject tsProject = (IAbstractTigerstripeProject) res
						.getProject().getAdapter(
								IAbstractTigerstripeProject.class);
				if (tsProject instanceof ITigerstripeModelProject
						&& tsProject.exists()) {
					projectAdded((ITigerstripeModelProject) tsProject);
				}
			}
		}
	}

//	private void checkProjectRemoved(Collection<IResource> removedResources) {
//		for (IResource res : removedResources) {
//			if (res instanceof IProject) {
//				IAbstractTigerstripeProject tsProject = (IAbstractTigerstripeProject) res
//						.getAdapter(IAbstractTigerstripeProject.class);
//
//				// Bug 936: remove from watch list of
//				// DiagramSynchronizationManager
//				if (tsProject instanceof ITigerstripeModelProject)
//					DiagramSynchronizationManager.getInstance()
//							.removeTSProjectToWatch(
//									(ITigerstripeModelProject) tsProject);
//			}
//		}
//	}

	
	
	/**
	 * Adding a project to the list of projects to watch. This effectively means
	 * we are setting this up as a listener for various changes on the model.
	 * 
	 * @param project
	 */
	public void projectAdded(IAbstractTigerstripeProject project) {
		if (project instanceof ITigerstripeModelProject){
			if (!projectWatchHash.containsKey(project.getLocation().toFile())) {
				TigerstripeRuntime.logDebugMessage("Adding project to watch: "
						+ project.getLocation().toFile());
				final ProjectDiagramsSynchronizer synchronizer = new ProjectDiagramsSynchronizer(
						(ITigerstripeModelProject)project);
				projectWatchHash.put(project.getName(), synchronizer);
				synchronizer.initialize(); // this will run in its own thread.
			}
		}
	}

	/**
	 * Removing a project from the watchlist, ie. de-registering as listener...
	 * 
	 */
	public void projectDeleted(String projectName) {
		if (projectWatchHash.containsKey(projectName)) {
			TigerstripeRuntime.logDebugMessage("Removing project to watch: "
					+ projectName);
			ProjectDiagramsSynchronizer synchronizer = projectWatchHash
					.remove(projectName);
			try {
				synchronizer.dispose();
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
	}

	// Diagram Synchronization control
	@Override
	public void flushSynchronizationRequests(boolean applyRequests,
			IProgressMonitor monitor) {
		if (monitor == null)
			monitor = new NullProgressMonitor();
		monitor.beginTask("Flushing Diagram updates", projectWatchHash.values()
				.size());

		for (ProjectDiagramsSynchronizer syncer : projectWatchHash.values()) {
			monitor.setTaskName(syncer.getProject().getName());
			try {
				syncer.flushRequestQueue(true, monitor);
			} catch (Exception e) {
				BasePlugin.log(e);
			}
			monitor.worked(1);
		}
		monitor.done();
	}

	@Override
	public void holdSynchronization() {
		setHoldSynchronization(true);
	}

	@Override
	public void restartSynchronization() {
		setHoldSynchronization(false);
	}

	protected void setHoldSynchronization(boolean hold) {
		this.hold = hold;
	}

	protected boolean isSynchronizationHeld() {
		return hold;
	}

	public void annotationChanged(IModelAnnotationChangeDelta[] delta) {
		// not registered for these
		
	}

	public void modelChanged(IModelChangeDelta[] delta) {
		// not registered for these		
	}

	

}
