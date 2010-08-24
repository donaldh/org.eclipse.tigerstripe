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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.impl.EdgeImpl;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.DiagramProperty;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.dnd.InstanceDiagramDragDropEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.policies.InstanceMapCanonicalEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.policies.InstanceMapItemSemanticEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.utils.DiagramPropertiesHelper;

/**
 * @generated
 */
public class InstanceMapEditPart extends DiagramEditPart implements TigerstripeEditableEntityEditPart{

	/**
	 * @generated
	 */
	public static String MODEL_ID = "Instance"; //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 79;

	/**
	 * @generated
	 */
	public InstanceMapEditPart(View view) {
		super(view);
	}

	/**
	 * @generated NOT
	 */
	@Override
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
				new InstanceMapItemSemanticEditPolicy());
		installEditPolicy(EditPolicyRoles.CANONICAL_ROLE,
				new InstanceMapCanonicalEditPolicy());
		installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE,
				new InstanceDiagramDragDropEditPolicy());
	}

	 @Override
	public void refresh() {
		InstanceMapCanonicalEditPolicy canonicalPolicy = (InstanceMapCanonicalEditPolicy) getEditPolicy(EditPolicyRoles.CANONICAL_ROLE);
		canonicalPolicy.refresh();
		super.refresh();
	}

	public void diagramPropertyChanged(DiagramProperty property) {
		String name = property.getName();

		if (DiagramPropertiesHelper.HIDEARTIFACTPACKAGES.equals(name)) {
			refreshCompartmentLabels();
		}
	}

	private void refreshCompartmentLabels() {
		InstanceMap map = (InstanceMap) ((Diagram) getModel()).getElement();
		List<ClassInstance> classInstances = map.getClassInstances();
		List<AssociationInstance> assocInstances = map
				.getAssociationInstances();
		for (ClassInstance instance : classInstances) {
			// then, for each artifact, get the edit part that goes along with
			// it
			IGraphicalEditPart editPart = (IGraphicalEditPart) findEditPart(
					this, instance);
			if (editPart != null) {
				// if the edit part is not null, get a list of that edit part's
				// children
				List<EditPart> childEditParts = editPart.getChildren();
				for (EditPart childEditPart : childEditParts) {
					// and then refresh each of the children
					if (childEditPart instanceof NamePackageInterface)
						childEditPart.refresh();
				}
			}
		}
		List connectionList = getConnections();
		Map<String, EditPart> connectionMap = new HashMap<String, EditPart>();
		for (Object obj : connectionList) {
			if (obj instanceof AssociationInstanceEditPart) {
				AssociationInstanceEditPart assocEditPart = (AssociationInstanceEditPart) obj;
				EObject eObject = ((EdgeImpl) assocEditPart.getModel())
						.getElement();
				AssociationInstance assoc = (AssociationInstance) eObject;
				String key = assoc.toString();
				connectionMap.put(key, assocEditPart);
			}
		}
		for (AssociationInstance instance : assocInstances) {
			// then, for each artifact, get the edit part that goes along with
			// it
			String key = instance.toString();
			IGraphicalEditPart editPart = (IGraphicalEditPart) connectionMap
					.get(key);
			if (editPart != null) {
				// if the edit part is not null, get a list of that edit part's
				// children
				List<EditPart> childEditParts = editPart.getChildren();
				for (EditPart childEditPart : childEditParts) {
					// and then refresh each of the children
					if (childEditPart instanceof NamePackageInterface)
						childEditPart.refresh();
				}
			}
		}
	}
}
