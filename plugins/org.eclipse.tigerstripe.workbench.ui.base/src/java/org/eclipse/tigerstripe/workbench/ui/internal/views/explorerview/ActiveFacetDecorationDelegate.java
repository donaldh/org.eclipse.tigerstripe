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

import java.util.HashMap;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tigerstripe.workbench.IModelAnnotationChangeDelta;
import org.eclipse.tigerstripe.workbench.IModelChangeDelta;
import org.eclipse.tigerstripe.workbench.ITigerstripeChangeListener;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;
import org.eclipse.tigerstripe.workbench.internal.api.model.IActiveFacetChangeListener;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeWorkspaceNotifier;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;

/**
 * For each ITigerstripeModelProject in the workspace, a listener is set up to
 * trigger a refresh of the labels on the Tigerstripe Explorer.
 * 
 * This is a singleton that registers itself with all Tigerstripe model
 * projects.
 * 
 * @author erdillon
 * 
 */
public class ActiveFacetDecorationDelegate implements
		ITigerstripeChangeListener {

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

	private HashMap<String, ActiveFacetListener> listeners = new HashMap<String, ActiveFacetListener>();

	private TigerstripeExplorerPart explorer;

	public ActiveFacetDecorationDelegate(TigerstripeExplorerPart explorer) {
		this.explorer = explorer;
	}

	public void start() {
		// Discover all Tigerstripe Model Projects in the workspace and register
		// self for facet updates.
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		try {
			for (IResource proj : root.members()) {
				ITigerstripeModelProject project = (ITigerstripeModelProject) proj
						.getAdapter(ITigerstripeModelProject.class);
				if (project != null)
					watchProject(project, (IJavaProject) JavaCore.create(proj));
			}
		} catch (CoreException e) {
			EclipsePlugin.log(e);
		}

		TigerstripeWorkspaceNotifier.INSTANCE.addTigerstripeChangeListener(
				this, ITigerstripeChangeListener.PROJECT);
	}

	private void watchProject(ITigerstripeModelProject project,
			IJavaProject jProject) {
		try {
			ActiveFacetListener listener = new ActiveFacetListener(jProject,
					explorer);
			listeners.put(project.getName(), listener);
			project.getArtifactManagerSession()
					.addActiveFacetListener(listener);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	public void annotationChanged(IModelAnnotationChangeDelta[] delta) {
		// never called
	}

	public void modelChanged(IModelChangeDelta[] delta) {
		// never called
	}

	public void descriptorChanged(IResource changedDescriptor) {
		// NOT USED HERE
	}
	
	public void projectAdded(IAbstractTigerstripeProject project) {
		if (project instanceof ITigerstripeModelProject)
			watchProject((ITigerstripeModelProject) project,
					(IJavaProject) project.getAdapter(IJavaProject.class));
	}

	public void projectDeleted(String projectName) {
		listeners.remove(projectName);
	}

	public void artifactResourceChanged(IResource changedArtifactResource) {
		// TODO Auto-generated method stub
		
	}
}
