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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ListCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ResizableCompartmentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.policies.AssociationClassClassMethodCompartmentCanonicalEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.policies.AssociationClassClassMethodCompartmentItemSemanticEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.policies.OpenInEditorEditPolicy;

/**
 * @generated
 */
public class AssociationClassClassMethodCompartmentEditPart extends
		ListCompartmentEditPart implements TigerstripeEditableEntityEditPart {

	/**
	 * @generated
	 */
	public static final int VISUAL_ID = 5012;

	/**
	 * @generated
	 */
	public AssociationClassClassMethodCompartmentEditPart(View view) {
		super(view);
	}

	/**
	 * @generated
	 */
	@Override
	protected boolean hasModelChildrenChanged(Notification evt) {
		return false;
	}

	/**
	 * @generated
	 */
	@Override
	public String getCompartmentName() {
		return "MethodCompartment";
	}

	/**
	 * @generated
	 */
	@Override
	public IFigure createFigure() {
		ResizableCompartmentFigure result = (ResizableCompartmentFigure) super
				.createFigure();
		result.setTitleVisibility(false);
		return result;
	}

	/**
	 * @generated
	 */
	@Override
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE,
				new ResizableCompartmentEditPolicy());
		installEditPolicy(
				EditPolicyRoles.SEMANTIC_ROLE,
				new AssociationClassClassMethodCompartmentItemSemanticEditPolicy());
		installEditPolicy(EditPolicyRoles.CREATION_ROLE,
				new CreationEditPolicy());
		installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE,
				new DragDropEditPolicy());
		installEditPolicy(EditPolicyRoles.CANONICAL_ROLE,
				new AssociationClassClassMethodCompartmentCanonicalEditPolicy());
		installEditPolicy(EditPolicyRoles.OPEN_ROLE,
				new OpenInEditorEditPolicy());
	}

	@Override
	public void refresh() {
		CanonicalEditPolicy canonicalPolicy = (CanonicalEditPolicy) getEditPolicy(EditPolicyRoles.CANONICAL_ROLE);
		if (canonicalPolicy != null) {
			canonicalPolicy.refresh();
		}
		super.refresh();
	}

	/**
	 * @generated
	 */
	@Override
	protected void setRatio(Double ratio) {
		if (getFigure().getParent().getLayoutManager() instanceof ConstrainedToolbarLayout) {
			super.setRatio(ratio);
		}
	}
}
