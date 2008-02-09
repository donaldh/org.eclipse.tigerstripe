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
package org.eclipse.tigerstripe.workbench.ui.eclipse.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
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
		} else if (selection.getFirstElement() instanceof IJavaProject) {
			IJavaProject jProject = (IJavaProject) selection.getFirstElement();
			project = jProject.getProject();
		}
		return project;
	}

	private void checkEnabled(IAction action, IStructuredSelection selection) {

		IProject project = getIProject(selection);
		if (project != null) {
			IAbstractTigerstripeProject tsProject = EclipsePlugin
					.getITigerstripeProjectFor(project);
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
