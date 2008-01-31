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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.TigerstripeEditableEntityEditPart;
import org.eclipse.ui.IWorkbenchPart;

public abstract class BaseDiagramPartAction {

	/**
	 * 
	 */
	protected EditPart mySelectedElement;

	/**
	 * 
	 */
	private Shell myShell;

	protected IWorkbenchPart myTargetWorkbenchPart;

	protected Shell getShell() {
		return this.myShell;
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		myShell = targetPart.getSite().getShell();
		myTargetWorkbenchPart = targetPart;
	}

	protected InstanceMap getMap() {
		if (mySelectedElement != null && mySelectedElement.getParent() != null)
			return (InstanceMap) ((View) mySelectedElement.getParent()
					.getModel()).getElement();
		return null;
	}

	protected ITigerstripeProject getCorrespondingTigerstripeProject() {
		DiagramGraphicalViewer viewer = null;
		if (mySelectedElement != null) {
			if (mySelectedElement instanceof ShapeNodeEditPart) {
				viewer = (DiagramGraphicalViewer) mySelectedElement.getParent()
						.getParent().getViewer();
			} else {
				viewer = (DiagramGraphicalViewer) mySelectedElement.getParent()
						.getViewer();
			}
		} else if (myTargetWorkbenchPart instanceof InstanceDiagramEditor) {
			InstanceDiagramEditor tsd = (InstanceDiagramEditor) myTargetWorkbenchPart;
			viewer = (DiagramGraphicalViewer) tsd.getDiagramGraphicalViewer();
		}

		DiagramEditDomain domain = (DiagramEditDomain) viewer.getEditDomain();

		IResource res = (IResource) domain.getEditorPart().getEditorInput()
				.getAdapter(IResource.class);

		IAbstractTigerstripeProject project = EclipsePlugin
				.getITigerstripeProjectFor(res.getProject());

		// domain.getCommandStack().execute(
		// new ICommandProxy(new RefreshEditPartCommand(mySelectedElement,
		// true)));
		if (project instanceof ITigerstripeProject)
			return (ITigerstripeProject) project;
		return null;
	}

	public void selectionChanged(IAction action, ISelection selection) {
		mySelectedElement = null;
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (structuredSelection.size() == 1) {
				if (structuredSelection.getFirstElement() instanceof TigerstripeEditableEntityEditPart) {
					mySelectedElement = (EditPart) structuredSelection
							.getFirstElement();
				}
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

	protected EObject getCorrespondingEObject() {
		EObject element = null;
		if (mySelectedElement != null) {
			if (mySelectedElement.getModel() instanceof Node) {
				Node obj = (Node) mySelectedElement.getModel();
				element = obj.getElement();
			} else if (mySelectedElement.getModel() instanceof Edge) {
				Edge obj = (Edge) mySelectedElement.getModel();
				element = obj.getElement();
			}
		} else if (myTargetWorkbenchPart instanceof InstanceDiagramEditor) {
			InstanceDiagramEditor tsd = (InstanceDiagramEditor) myTargetWorkbenchPart;
			Diagram obj = (Diagram) tsd.getDiagramEditPart().getModel();
			element = obj.getElement();
		}

		return element;
	}

	protected IAbstractArtifact getCorrespondingArtifact() {
		ITigerstripeProject project = getCorrespondingTigerstripeProject();
		String fqn = null;

		EObject element = getCorrespondingEObject();
		if (element instanceof ClassInstance) {
			ClassInstance qne = (ClassInstance) element;
			fqn = qne.getFullyQualifiedName();
		}

		if (fqn != null && fqn.length() != 0) {
			try {
				IArtifactManagerSession session = project
						.getArtifactManagerSession();
				IAbstractArtifact result = session
						.getArtifactByFullyQualifiedName(fqn);
				return result;
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
		return null;
	}

	public IWorkbenchPart getMyTargetWorkbenchPart() {
		return myTargetWorkbenchPart;
	}
}
