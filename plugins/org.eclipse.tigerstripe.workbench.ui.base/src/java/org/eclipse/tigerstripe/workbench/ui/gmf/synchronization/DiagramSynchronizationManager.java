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

import java.io.File;
import java.util.HashMap;

import org.eclipse.tigerstripe.api.artifacts.updater.IModelChangeRequest;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.eclipse.builder.WorkspaceListener;

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
 * the user on the TS Explorer. This will be picked up by the
 * {@link WorkspaceListener} and propagated to this.
 * 
 * The manager is set to watch ITigerstripeProjects. As Eclipse starts up and
 * the auditors are being instantiated per project, this is notified with each
 * project so it can be added to the list to watch. When a project is removed it
 * is taken off the list of projects to watch.
 * 
 * @Since Bug 936
 * @author eric
 * 
 */
public class DiagramSynchronizationManager {

	private static DiagramSynchronizationManager instance;

	private HashMap<File, ProjectDiagramsSynchronizer> projectWatchHash = new HashMap<File, ProjectDiagramsSynchronizer>();

	private DiagramSynchronizationManager() {
		// making sure this is a singleton.
	}

	public static DiagramSynchronizationManager getInstance() {
		if (instance == null) {
			instance = new DiagramSynchronizationManager();
		}

		return instance;
	}

	/**
	 * Adding a project to the list of projects to watch. This effectively means
	 * we are setting this up as a listener for various changes on the model.
	 * 
	 * @param project
	 */
	public void addTSProjectToWatch(ITigerstripeProject project) {
		if (!projectWatchHash.containsKey(project.getBaseDir())) {
			TigerstripeRuntime.logDebugMessage("Adding project to watch: "
					+ project.getBaseDir());
			final ProjectDiagramsSynchronizer synchronizer = new ProjectDiagramsSynchronizer(
					project);
			projectWatchHash.put(project.getBaseDir(), synchronizer);
			synchronizer.initialize(); // this will run in its own thread.
		}
	}

	/**
	 * Removing a project from the watchlist, ie. de-registering as listener...
	 * 
	 */
	public void removeTSProjectToWatch(ITigerstripeProject project) {
		if (projectWatchHash.containsKey(project.getBaseDir())) {
			TigerstripeRuntime.logDebugMessage("Removing project to watch: "
					+ project.getBaseDir());
			ProjectDiagramsSynchronizer synchronizer = projectWatchHash
					.remove(project.getBaseDir());
			try {
				synchronizer.dispose();
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
	}
}
