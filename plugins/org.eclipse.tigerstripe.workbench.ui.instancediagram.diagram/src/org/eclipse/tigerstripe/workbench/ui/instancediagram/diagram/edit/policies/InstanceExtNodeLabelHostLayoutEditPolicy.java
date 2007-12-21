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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.policies;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class InstanceExtNodeLabelHostLayoutEditPolicy extends
		XYLayoutEditPolicy {

	/**
	 * @generated
	 */
	private LayoutEditPolicy realLayoutEditPolicy;

	/**
	 * @generated
	 */
	public LayoutEditPolicy getRealLayoutEditPolicy() {
		return realLayoutEditPolicy;
	}

	/**
	 * @generated
	 */
	public void setRealLayoutEditPolicy(LayoutEditPolicy realLayoutEditPolicy) {
		this.realLayoutEditPolicy = realLayoutEditPolicy;
	}

	/**
	 * @generated
	 */
	protected boolean isExternalLabel(EditPart editPart) {
		return false;
	}

	/**
	 * @generated
	 */
	protected final List getExternalLabels(GroupRequest request) {
		List editParts = new ArrayList();
		if (request.getEditParts() != null) {
			for (Iterator it = request.getEditParts().iterator(); it.hasNext();) {
				EditPart editPart = (EditPart) it.next();
				if (isExternalLabel(editPart)) {
					editParts.add(editPart);
				}
			}
		}
		return editParts;
	}

	/**
	 * @generated
	 */
	@Override
	public Command getCommand(Request request) {
		if (REQ_MOVE_CHILDREN.equals(request.getType())) {
			ChangeBoundsRequest cbRequest = (ChangeBoundsRequest) request;
			List extLabels = getExternalLabels(cbRequest);
			if (!extLabels.isEmpty()) {
				List editParts = cbRequest.getEditParts();
				Command cmd = null;
				if (realLayoutEditPolicy != null
						&& editParts.size() > extLabels.size()) {
					List other = new ArrayList(editParts);
					other.removeAll(extLabels);
					cbRequest.setEditParts(other);
					cmd = realLayoutEditPolicy.getCommand(request);
				}
				cbRequest.setEditParts(extLabels);
				Command extLabelsCmd = getMoveChildrenCommand(request);
				cbRequest.setEditParts(editParts);
				return cmd == null ? extLabelsCmd : cmd.chain(extLabelsCmd);
			}
		}
		if (request instanceof GroupRequest) {
			List extLabels = getExternalLabels((GroupRequest) request);
			if (!extLabels.isEmpty())
				return null;
		}
		return realLayoutEditPolicy == null ? null : realLayoutEditPolicy
				.getCommand(request);
	}

	/**
	 * @generated
	 */
	@Override
	protected Object getConstraintFor(ChangeBoundsRequest request,
			GraphicalEditPart child) {
		int dx = ((Integer) ViewUtil.getStructuralFeatureValue((View) child
				.getModel(), NotationPackage.eINSTANCE.getLocation_X()))
				.intValue();
		int dy = ((Integer) ViewUtil.getStructuralFeatureValue((View) child
				.getModel(), NotationPackage.eINSTANCE.getLocation_Y()))
				.intValue();
		Rectangle r = new Rectangle(dx, dy, 0, 0);
		child.getFigure().translateToAbsolute(r);
		r.translate(request.getMoveDelta());
		child.getFigure().translateToRelative(r);
		return r;
	}

	/**
	 * @generated
	 */
	@Override
	public boolean understandsRequest(Request req) {
		if (realLayoutEditPolicy != null
				&& realLayoutEditPolicy.understandsRequest(req))
			return true;
		return super.understandsRequest(req);
	}

	/**
	 * @generated
	 */
	@Override
	protected void decorateChild(EditPart child) {
	}

	/**
	 * @generated
	 */
	@Override
	public void setHost(EditPart host) {
		super.setHost(host);
		if (realLayoutEditPolicy != null) {
			realLayoutEditPolicy.setHost(host);
		}
	}

	/**
	 * @generated
	 */
	@Override
	public void activate() {
		super.activate();
		if (realLayoutEditPolicy != null) {
			realLayoutEditPolicy.activate();
		}
	}

	/**
	 * @generated
	 */
	@Override
	public void deactivate() {
		super.deactivate();
		if (realLayoutEditPolicy != null) {
			realLayoutEditPolicy.deactivate();
		}
	}

	/**
	 * @generated
	 */
	@Override
	public EditPart getTargetEditPart(Request request) {
		if (realLayoutEditPolicy != null)
			return realLayoutEditPolicy.getTargetEditPart(request);
		else
			return super.getTargetEditPart(request);
	}

	/**
	 * @generated
	 */
	@Override
	public void showSourceFeedback(Request request) {
		if (realLayoutEditPolicy != null) {
			realLayoutEditPolicy.showSourceFeedback(request);
		} else {
			super.showSourceFeedback(request);
		}
	}

	/**
	 * @generated
	 */
	@Override
	public void eraseSourceFeedback(Request request) {
		if (realLayoutEditPolicy != null) {
			realLayoutEditPolicy.eraseSourceFeedback(request);
		} else {
			super.eraseSourceFeedback(request);
		}
	}

	/**
	 * @generated
	 */
	@Override
	public void showTargetFeedback(Request request) {
		if (realLayoutEditPolicy != null) {
			realLayoutEditPolicy.showTargetFeedback(request);
		} else {
			super.showTargetFeedback(request);
		}
	}

	/**
	 * @generated
	 */
	@Override
	public void eraseTargetFeedback(Request request) {
		if (realLayoutEditPolicy != null) {
			realLayoutEditPolicy.eraseTargetFeedback(request);
		} else {
			super.eraseTargetFeedback(request);
		}
	}
}
