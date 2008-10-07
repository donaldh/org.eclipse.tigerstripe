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
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequest;
import org.eclipse.tigerstripe.workbench.internal.builder.WorkspaceHelper;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
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
public class DiagramSynchronizationManager implements IResourceChangeListener {

	private static DiagramSynchronizationManager instance;

	private HashMap<File, ProjectDiagramsSynchronizer> projectWatchHash = new HashMap<File, ProjectDiagramsSynchronizer>();

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
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		try {
			checkProjectAdded(Arrays.asList(root.members()));
		} catch (CoreException e) {
			EclipsePlugin.log(e);
		}
	}

	public void resourceChanged(IResourceChangeEvent event) {
		// Get the list of removed resources
		Collection<IResource> removedResources = new HashSet<IResource>();
		Collection<IResource> changedResources = new HashSet<IResource>();
		Collection<IResource> addedResources = new HashSet<IResource>();
		WorkspaceHelper.buildResourcesLists(event.getDelta(), removedResources,
				changedResources, addedResources, null);

		checkProjectAdded(addedResources);
		checkProjectRemoved(removedResources);
	}

	private void checkProjectAdded(Collection<IResource> addedResources) {
		for (IResource res : addedResources) {
			if (res instanceof IProject) {
				IAbstractTigerstripeProject tsProject = (IAbstractTigerstripeProject) res
						.getProject().getAdapter(
								IAbstractTigerstripeProject.class);
				if (tsProject instanceof ITigerstripeModelProject
						&& tsProject.exists()) {
					addTSProjectToWatch((ITigerstripeModelProject) tsProject);
				}
			}
		}
	}

	private void checkProjectRemoved(Collection<IResource> removedResources) {
		for (IResource res : removedResources) {
			if (res instanceof IProject) {
				IAbstractTigerstripeProject tsProject = (IAbstractTigerstripeProject) res
						.getAdapter(IAbstractTigerstripeProject.class);

				// Bug 936: remove from watch list of
				// DiagramSynchronizationManager
				if (tsProject instanceof ITigerstripeModelProject)
					DiagramSynchronizationManager.getInstance()
							.removeTSProjectToWatch(
									(ITigerstripeModelProject) tsProject);
			}
		}
	}

	/**
	 * Adding a project to the list of projects to watch. This effectively means
	 * we are setting this up as a listener for various changes on the model.
	 * 
	 * @param project
	 */
	public void addTSProjectToWatch(ITigerstripeModelProject project) {
		if (!projectWatchHash.containsKey(project.getLocation().toFile())) {
			TigerstripeRuntime.logDebugMessage("Adding project to watch: "
					+ project.getLocation().toFile());
			final ProjectDiagramsSynchronizer synchronizer = new ProjectDiagramsSynchronizer(
					project);
			projectWatchHash.put(project.getLocation().toFile(), synchronizer);
			synchronizer.initialize(); // this will run in its own thread.
		}
	}

	/**
	 * Removing a project from the watchlist, ie. de-registering as listener...
	 * 
	 */
	public void removeTSProjectToWatch(ITigerstripeModelProject project) {
		if (projectWatchHash.containsKey(project.getLocation().toFile())) {
			TigerstripeRuntime.logDebugMessage("Removing project to watch: "
					+ project.getLocation().toFile());
			ProjectDiagramsSynchronizer synchronizer = projectWatchHash
					.remove(project.getLocation().toFile());
			try {
				synchronizer.dispose();
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
	}
}
