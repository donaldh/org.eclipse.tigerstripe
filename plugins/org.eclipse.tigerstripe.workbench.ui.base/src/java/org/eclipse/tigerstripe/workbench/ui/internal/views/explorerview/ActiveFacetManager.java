/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.model.IActiveFacetChangeListener;
import org.eclipse.tigerstripe.workbench.internal.builder.WorkspaceHelper;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;

/**
 * The Active Facet Manager forces actions upon active Facet Changes in the
 * workspace
 * 
 * This is a singleton that registers itself with all Tigerstripe model
 * projects.
 * 
 * @author erdillon
 * 
 */
public class ActiveFacetManager implements IResourceChangeListener {

	public class ActiveFacetListener implements IActiveFacetChangeListener {

		private IJavaProject project;

		private TigerstripeExplorerPart explorer;

		public ActiveFacetListener(IJavaProject project,
				TigerstripeExplorerPart explorer) {
			this.project = project;
			this.explorer = explorer;
		}

		public void facetChanged(IFacetReference oldFacet,
				IFacetReference newFacet) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					explorer.projectStateChanged(project);
				}
			});
		}
	}

	private TigerstripeExplorerPart explorer;

	public ActiveFacetManager(TigerstripeExplorerPart explorer) {
		this.explorer = explorer;
		initialize();
	}

	private void initialize() {
		// Discover all Tigerstripe Model Projects in the workspace and register
		// self for facet updates.
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		try {
			checkProjectAdded(Arrays.asList(root.members()));
		} catch (CoreException e) {
			EclipsePlugin.log(e);
		}

		// register self for IResourceChanges so we can add/remove self as
		// Tigerstripe model projects are created/deleted
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	public void resourceChanged(IResourceChangeEvent event) {
		// Get the list of removed resources
		Collection<IResource> removedResources = new HashSet<IResource>();
		Collection<IResource> changedResources = new HashSet<IResource>();
		Collection<IResource> addedResources = new HashSet<IResource>();
		WorkspaceHelper.buildResourcesLists(event.getDelta(), removedResources,
				changedResources, addedResources);

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
					try {
						IJavaProject project = JavaCore.create((IProject) res);
						((ITigerstripeModelProject) tsProject)
								.getArtifactManagerSession()
								.addActiveFacetListener(
										new ActiveFacetListener(project,
												explorer));
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			}
		}
	}

	private void checkProjectRemoved(Collection<IResource> removedResources) {
	}

}
