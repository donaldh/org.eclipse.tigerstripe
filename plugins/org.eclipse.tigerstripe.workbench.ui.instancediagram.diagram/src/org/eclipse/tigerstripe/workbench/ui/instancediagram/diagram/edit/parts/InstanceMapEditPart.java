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
		} else if (DiagramPropertiesHelper.HIDEORDERQUALIFIERS.equals(name)) {
			refreshAssociationEndLabels();
		}
	}

	private void refreshCompartmentLabels() {
		InstanceMap map = getInstanceMap();

		List<ClassInstance> classInstances = map.getClassInstances();
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

		refreshAssociations(map, new Class[] { NamePackageInterface.class });
	}

	public void refreshAssociationEndLabels() {
		InstanceMap map = getInstanceMap();
		refreshAssociations(map, new Class[] {
				AssociationInstanceAEndNameEditPart.class,
				AssociationInstanceZEndNameEditPart.class });
	}

	private void refreshAssociations(InstanceMap map, Class<?>[] clazzes) {
		List<?> assocInstances = map.getAssociationInstances();
		Map<String, EditPart> connectionMap = getConnectionsMap();

		for (Object object : assocInstances) {
			AssociationInstance instance = (AssociationInstance) object;
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
					for (Class<?> clazz : clazzes) {
						if (clazz.isAssignableFrom(childEditPart.getClass())) {
							childEditPart.refresh();
						}
					}
				}
			}
		}
	}

	private Map<String, EditPart> getConnectionsMap() {
		List<?> connectionList = getConnections();
		Map<String, EditPart> connectionMap = new HashMap<String, EditPart>();
		for (Object obj : connectionList) {
			if (obj instanceof AssociationInstanceEditPart) {
				AssociationInstanceEditPart assocEditPart = (AssociationInstanceEditPart) obj;
				EObject eObject = ((EdgeImpl) assocEditPart.getModel())
						.getElement();
				if (eObject instanceof AssociationInstance) {
					AssociationInstance assoc = (AssociationInstance) eObject;
					String key = assoc.toString();
					connectionMap.put(key, assocEditPart);
				}
			}
		}
		return connectionMap;
	}

	private InstanceMap getInstanceMap() {
		return (InstanceMap) ((Diagram) getModel()).getElement();
	}
}
