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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part;

import org.eclipse.core.resources.IResource;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.MapEditPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * 
 */
public class TigerstripeRefreshDiagramAction implements IObjectActionDelegate {

	/**
	 * 
	 */
	private MapEditPart mySelectedElement;

	/**
	 * 
	 */
	private Shell myShell;

	/**
	 * 
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		myShell = targetPart.getSite().getShell();
	}

	public ITigerstripeModelProject getCorrespondingTigerstripeProject() {
		DiagramGraphicalViewer viewer = (DiagramGraphicalViewer) mySelectedElement
				.getParent().getViewer();
		DiagramEditDomain domain = (DiagramEditDomain) viewer.getEditDomain();

		IResource res = (IResource) domain.getEditorPart().getEditorInput()
				.getAdapter(IResource.class);

		IAbstractTigerstripeProject project = (IAbstractTigerstripeProject) res
				.getProject().getAdapter(IAbstractTigerstripeProject.class);

		// domain.getCommandStack().execute(
		// new ICommandProxy(new RefreshEditPartCommand(mySelectedElement,
		// true)));
		if (project instanceof ITigerstripeModelProject)
			return (ITigerstripeModelProject) project;
		return null;
	}

	/**
	 * Refreshes the content of the Diagram based on the current state of the
	 * Artifact manager
	 */
	public void run(IAction action) {
		ITigerstripeModelProject tsProject = getCorrespondingTigerstripeProject();
		if (tsProject != null) {
			// MapRefresher refresher = new MapRefresher(mySelectedElement
			// .getDiagramEditDomain(), mySelectedElement
			// .getEditingDomain(), (Map) mySelectedElement
			// .getAdapter(Map.class), tsProject);
			// refresher.run();
		}

	}

	/**
	 * 
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		mySelectedElement = null;
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (structuredSelection.size() == 1
					&& structuredSelection.getFirstElement() instanceof MapEditPart) {
				mySelectedElement = (MapEditPart) structuredSelection
						.getFirstElement();
			}
		}
		action.setEnabled(isEnabled());
	}

	/**
	 * 
	 */
	private boolean isEnabled() {
		return mySelectedElement != null;
	}

}
