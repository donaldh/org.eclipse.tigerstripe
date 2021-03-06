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
package org.eclipse.tigerstripe.workbench.ui.internal.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeWorkspaceNotifier;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * This action delegate resets a project to have no Active Facet
 * 
 * @author Eric Dillon
 * 
 */
public class ResetActiveFacetActionDelegate implements IObjectActionDelegate {

	private ITigerstripeModelProject targetProject;

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub

	}

	public void run(IAction action) {
		try {
			targetProject.resetActiveFacet();
			TigerstripeWorkspaceNotifier.INSTANCE.activeFacetChanged(targetProject);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			checkEnabled(action, (IStructuredSelection) selection);
		} else {
			action.setEnabled(false);
		}
	}

	private IProject getIProject(IStructuredSelection selection) {
		IProject project = null;
		if (selection.getFirstElement() instanceof IResource) {
			IResource res = (IResource) selection.getFirstElement();
			project = res.getProject();
		} else if (selection.getFirstElement() instanceof IProject) {
			project = (IProject) selection.getFirstElement();
		}
		return project;
	}

	private void checkEnabled(IAction action, IStructuredSelection selection) {

		IProject project = getIProject(selection);
		if (project != null) {
			IAbstractTigerstripeProject tsProject = (IAbstractTigerstripeProject) project
					.getAdapter(IAbstractTigerstripeProject.class);
			;
			if (tsProject instanceof ITigerstripeModelProject) {
				targetProject = (ITigerstripeModelProject) tsProject;
				try {
					action.setEnabled(targetProject.getActiveFacet() != null);
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
					action.setEnabled(false);
				}
				return;
			}
		}
		action.setEnabled(false);
	}
}
