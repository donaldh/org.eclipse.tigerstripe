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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.dnd;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.eclipse.utils.AbstractArtifactAdapter;
import org.eclipse.tigerstripe.workbench.model.IRelationship;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.helpers.InstanceDiagramEditorHelper;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.helpers.InstanceDiagramMapHelper;
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
public class InstanceDiagramDragDropEnablePolicy {

	private static InstanceDiagramDragDropEnablePolicy instance = null;

	private InstanceDiagramDragDropEnablePolicy() {
	}

	public static InstanceDiagramDragDropEnablePolicy getInstance() {
		if (instance == null) {
			instance = new InstanceDiagramDragDropEnablePolicy();
		}
		return instance;
	}

	public boolean isEnabled(IStructuredSelection selection,
			EditPart targetEditPart) {

		boolean result = true;

		if (selection.isEmpty())
			return false;

		// only allow for drag-and-drop of one class at a time onto the
		// instance diagram (to avoid confusion when one of operations in
		// a multi-part drag-and-drop is cancelled)
		if (selection.size() > 1)
			return false;

		if (!(targetEditPart instanceof DiagramEditPart))
			return false;

		DiagramEditPart diagramEditPart = (DiagramEditPart) targetEditPart;
		Diagram diagram = diagramEditPart.getDiagramView();

		IEditorPart editorPart = ((DiagramEditDomain) diagramEditPart
				.getParent().getViewer().getEditDomain()).getEditorPart();

		InstanceDiagramEditorHelper dHelper = new InstanceDiagramEditorHelper(
				editorPart);
		ITigerstripeProject diagramProject = dHelper
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

				// disable drag-and-drop of Associations, AssociationClasses,
				// and
				// Dependencies onto an instance diagram (these are added using
				// the
				// "handles" instead so that the source and target will be
				// known)
				if (artifact instanceof IRelationship)
					return false;

				// disable drag-and-drop of Enumerations
				if (artifact instanceof IEnumArtifact)
					return false;

				// if is an abstract artifact, then return false (cannot add
				// abstract
				// artifacts to an instance diagram (tjmcs, 04-Apr-2007;
				// disabled this
				// check in favor of a dialog box describing the error per
				// Cisco's
				// request)
				// if (artifact.isAbstract()) return false;

				InstanceMap map = (InstanceMap) diagram.getElement();
				InstanceDiagramMapHelper helper = new InstanceDiagramMapHelper(
						map);

				List<Instance> instances = helper.findElementFor(artifact
						.getFullyQualifiedName());

				// if (artifact instanceof IRelationship) {
				// // In the case of IRelationShip we need to check that both
				// // ends are on the diagram
				// IRelationship rel = (IRelationship) artifact;
				// if (rel.getRelationshipAEnd() != null
				// && rel.getRelationshipZEnd() != null) {
				// String aEndFQN = rel.getRelationshipAEnd().getType()
				// .getFullyQualifiedName();
				// String zEndFQN = rel.getRelationshipZEnd().getType()
				// .getFullyQualifiedName();
				//
				// result = (instance == null)
				// && (helper.findElementFor(aEndFQN) != null)
				// && (helper.findElementFor(zEndFQN) != null);
				// } else {
				// result = false;
				// }
				// } else {
				// // make sure we don't add the same artifact twice on a
				// diagram
				// // without having a name for each instance
				// for (Instance instance : instances) {
				// // if there is an instance and it doesn't have a name, return
				// false
				// // (in that case, would have two instances without a name in
				// the diagram)
				// if (instance.getArtifactName() == null)
				// return false;
				// }
				// result = true;
				// }
			} else {
				result = false;
			}
		}

		return result;
	}

}
