/*******************************************************************************
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     xored software, Inc. - initial API and implementation (Yuri Strot)
 ******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.parts;

import java.util.List;

import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.commands.SetConstraintCommand;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Diagram;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape;

public class DiagramEditPart extends AbstractGraphicalEditPart {

	private Adapter adapter;

	@Override
	public void activate() {
		super.activate();
		adapter = new AdapterImpl() {
			@Override
			public void notifyChanged(Notification msg) {
				if (msg.getEventType() == Notification.SET) {
					if (DependenciesPackage.Literals.DIAGRAM__CURRENT_LAYER
							.equals(msg.getFeature())) {
						Object newValue = msg.getNewValue();
						Object oldValue = msg.getOldValue();
						if (newValue != oldValue) {
							refresh();
						}
					}
				}
			}
		};
		getDiagram().eAdapters().add(adapter);
	}

	@Override
	public void deactivate() {
		if (adapter != null) {
			getDiagram().eAdapters().remove(adapter);
		}
		super.activate();
	}

	@Override
	protected void createEditPolicies() {
		// disallows the removal of this edit part from its parent
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new RootComponentEditPolicy());
		// handles constraint changes (e.g. moving and/or resizing) of model
		// elements
		// and creation of new model elements
		installEditPolicy(EditPolicy.LAYOUT_ROLE,
				new ShapesXYLayoutEditPolicy());
	}

	@Override
	protected IFigure createFigure() {
		Figure f = new FreeformLayer();
		f.setBorder(new MarginBorder(3));
		f.setLayoutManager(new FreeformLayout());
		f.addLayoutListener(LayoutAnimator.getDefault());

		// Create the static router for the connection layer
		ConnectionLayer connLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
		connLayer.setConnectionRouter(new ShortestPathConnectionRouter(f));

		return f;
	}

	public Diagram getDiagram() {
		return (Diagram) getModel();
	}

	@Override
	protected List<Shape> getModelChildren() {
		return getDiagram().getCurrentLayer().getShapes();
	}

	private static class ShapesXYLayoutEditPolicy extends XYLayoutEditPolicy {

		@Override
		protected Command createChangeConstraintCommand(
				ChangeBoundsRequest request, EditPart child, Object constraint) {
			if (((child instanceof SubjectEditPart) || (child instanceof NoteEditPart))
					&& constraint instanceof Rectangle) {
				// return a command that can move and/or resize a Shape
				return new SetConstraintCommand((Shape) child.getModel(),
						request, (Rectangle) constraint);
			}
			return super.createChangeConstraintCommand(request, child,
					constraint);
		}

		@Override
		protected Command createChangeConstraintCommand(EditPart child,
				Object constraint) {
			return null;
		}

		@Override
		protected Command getCreateCommand(CreateRequest request) {
			return null;
		}

	}

}
