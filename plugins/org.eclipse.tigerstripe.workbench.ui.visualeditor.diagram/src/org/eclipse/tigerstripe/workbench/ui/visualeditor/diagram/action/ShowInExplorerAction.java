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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.action;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.TigerstripeExplorerPart;
import org.eclipse.tigerstripe.workbench.ui.gmf.TigerstripeShapeNodeEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.ui.IActionDelegate;

public class ShowInExplorerAction implements IActionDelegate {

	private ISelection selection;

	public void run(IAction action) {

		EObject element = findElement();
		if (element == null)
			return;

		try {
			if (element instanceof QualifiedNamedElement) {
				Map map = (Map) element.eContainer();
				ITigerstripeModelProject project = map
						.getCorrespondingITigerstripeProject();
				revealQNEInExplorer((QualifiedNamedElement) element, project);
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	private EObject findElement() {
		EditPart targetPart = findTargetPart();
		// if (targetPart instanceof MapEditPart) {
		// return ((Diagram) targetPart.getModel()).getElement();
		// } else
		if (targetPart instanceof TigerstripeShapeNodeEditPart)
			return ((Node) targetPart.getModel()).getElement();
		else if (targetPart instanceof ConnectionEditPart)
			return ((Edge) targetPart.getModel()).getElement();

		return null;
	}

	private void revealQNEInExplorer(QualifiedNamedElement qne,
			ITigerstripeModelProject project) throws TigerstripeException {
		TigerstripeExplorerPart part = EclipsePlugin.findTigerstripeExplorer();
		IArtifactManagerSession session = project.getArtifactManagerSession();
		IAbstractArtifact artifact = session
				.getArtifactByFullyQualifiedName(qne.getFullyQualifiedName());
		part.revealArtifact(artifact);
	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			action.setEnabled(ssel.toList().size() == 1
					&& findElement() != null);
			return;
		}
		action.setEnabled(false);
	}

	private EditPart findTargetPart() {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			Object obj = ssel.getFirstElement();
			if (obj instanceof EditPart)
				return (EditPart) obj;
		}
		return null;
	}
}
