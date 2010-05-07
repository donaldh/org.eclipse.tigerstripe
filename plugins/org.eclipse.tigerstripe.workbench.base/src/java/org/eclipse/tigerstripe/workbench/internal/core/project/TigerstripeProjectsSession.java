/*******************************************************************************
 * Copyright (c) 2010 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.project;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.tigerstripe.workbench.IModelAnnotationChangeDelta;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.ITigerstripeChangeListener;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeWorkspaceNotifier;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * This singleton keeps track of all TS projects in the workspace. This allows
 * to keep a cache of all Tigerstripe projects so there is no need to
 * adapt/compute them over and over as it proved to be very expensive.
 * 
 * This listens to the {@link TigerstripeWorkspaceNotifier} (project creations
 * and deletions) so it stays up to date.
 * 
 * @author erdillon
 * 
 */
public class TigerstripeProjectsSession implements ITigerstripeChangeListener {

	public static TigerstripeProjectsSession INSTANCE = new TigerstripeProjectsSession();

	private Map<String, IAbstractTigerstripeProject> projectsByName = new HashMap<String, IAbstractTigerstripeProject>();
	private Map<String, ITigerstripeModelProject> modelProjectsByName = new HashMap<String, ITigerstripeModelProject>();

	private IAbstractTigerstripeProject[] allProjects = new IAbstractTigerstripeProject[0];
	private ITigerstripeModelProject[] allModelProjects = new ITigerstripeModelProject[0];

	private TigerstripeProjectsSession() {
		buildList();
	}

	private void buildList() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		for (IProject iProject : root.getProjects()) {
			IAbstractTigerstripeProject tProject = (IAbstractTigerstripeProject) iProject
					.getAdapter(IAbstractTigerstripeProject.class);
			if (tProject instanceof IAbstractTigerstripeProject) {
				String name = tProject.getName();
				projectsByName.put(name, tProject);
				if (tProject instanceof ITigerstripeModelProject) {
					modelProjectsByName.put(name,
							(ITigerstripeModelProject) tProject);
				}
			}
		}
		updateAllModelProjects();
		updateAllProjects();
		
		TigerstripeWorkspaceNotifier.INSTANCE.addTigerstripeChangeListener(this, PROJECT);
	}

	public void annotationChanged(IModelAnnotationChangeDelta[] delta) {
		// NOT USED HERE
	}

	public void modelChanged(IModelChangeDelta[] delta) {
		// NOT USED HERE
	}

	public void projectAdded(IAbstractTigerstripeProject project) {
		String name = project.getName();
		projectsByName.put(name, project);
		updateAllProjects();
		if (project instanceof ITigerstripeModelProject) {
			modelProjectsByName.put(name, (ITigerstripeModelProject) project);
			updateAllModelProjects();
		}
	}

	public void projectDeleted(String projectName) {
		if (projectsByName.containsKey(projectName)) {
			projectsByName.remove(projectName);
			updateAllProjects();
		}
		if (modelProjectsByName.containsKey(projectName)) {
			modelProjectsByName.remove(projectName);
			updateAllModelProjects();
		}
	}

	private void updateAllProjects() {
		allProjects = projectsByName.values()
				.toArray(
						new IAbstractTigerstripeProject[projectsByName.values()
								.size()]);
	}

	private void updateAllModelProjects() {
		allModelProjects = modelProjectsByName.values().toArray(
				new ITigerstripeModelProject[modelProjectsByName.values()
						.size()]);
	}

	public IAbstractTigerstripeProject[] allProjects() {
		return allProjects;
	}

	public ITigerstripeModelProject[] allModelProjects() {
		return allModelProjects;
	}

}
