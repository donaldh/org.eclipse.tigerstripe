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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.dnd;

import java.util.Iterator;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.AbstractArtifactAdapter;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers.DiagramEditorHelper;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers.MapHelper;
import org.eclipse.ui.IEditorPart;

/**
 * This class implements the decision process to enable or not a drag and drop
 * onto a class diagram from the Tigerstripe Explorer
 * 
 * Basically, it needs to be a valid IAbstractArtifact that is not already on
 * the diagram
 * 
 * This is a singleton class
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class ClassDiagramDragDropEnablePolicy {

	private static ClassDiagramDragDropEnablePolicy instance = null;

	private ClassDiagramDragDropEnablePolicy() {
	}

	public static ClassDiagramDragDropEnablePolicy getInstance() {
		if (instance == null) {
			instance = new ClassDiagramDragDropEnablePolicy();
		}
		return instance;
	}

	public boolean isEnabled(IStructuredSelection selection,
			EditPart targetEditPart) {

		boolean result = true;

		if (selection.isEmpty())
			return false;

		if (!(targetEditPart instanceof DiagramEditPart))
			return false;

		DiagramEditPart diagramEditPart = (DiagramEditPart) targetEditPart;
		Diagram diagram = diagramEditPart.getDiagramView();

		IEditorPart editorPart = ((DiagramEditDomain) diagramEditPart
				.getParent().getViewer().getEditDomain()).getEditorPart();

		DiagramEditorHelper dHelper = new DiagramEditorHelper(editorPart);
		ITigerstripeModelProject diagramProject = dHelper
				.getCorrespondingTigerstripeProject();

		if (diagramProject == null)
			// The diagram is not in a Tigerstripe project so we won't be able
			// to resolve
			// the artifact. not allowed
			return false;

		for (Iterator iter = selection.iterator(); iter.hasNext() && result;) {
			Object obj = iter.next();
			IAbstractArtifact artifact = AbstractArtifactAdapter.adaptWithin(
					obj, diagramProject);

			if (artifact != null) {
				// make sure we don't add the same artifact twice on a diagram
				Map map = (Map) diagram.getElement();
				MapHelper helper = new MapHelper(map);

				QualifiedNamedElement element = helper.findElementFor(artifact
						.getFullyQualifiedName());

				if (artifact instanceof IRelationship) {
					// In the case of IRelationShip we need to check that both
					// ends are on
					// the diagram
					IRelationship rel = (IRelationship) artifact;
					if (rel.getRelationshipAEnd() != null
							&& rel.getRelationshipZEnd() != null) {
						String aEndFQN = rel.getRelationshipAEnd().getType()
								.getFullyQualifiedName();
						String zEndFQN = rel.getRelationshipZEnd().getType()
								.getFullyQualifiedName();

						result = (element == null)
								&& (helper.findElementFor(aEndFQN) != null)
								&& (helper.findElementFor(zEndFQN) != null);
					} else {
						result = false;
					}
				} else {
					result = element == null;
				}
			} else {
				result = false;
			}
		}

		return result;
	}
}
