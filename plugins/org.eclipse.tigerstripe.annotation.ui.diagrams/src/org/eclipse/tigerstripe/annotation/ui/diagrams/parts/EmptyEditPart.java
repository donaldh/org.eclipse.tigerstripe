/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.ui.diagrams.parts;

import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ViewComponentEditPolicy;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.ViewLocationNode;
import org.eclipse.tigerstripe.annotation.ui.internal.diagrams.DiagramRebuildUtils;

/**
 * @author Yuri Strot
 *
 */
public class EmptyEditPart extends ShapeNodeEditPart {

	/**
	 * constructor
	 * @param view the view controlled by this edit part
	 */
	public EmptyEditPart(View view) {
		super(view);
	}
	
	protected ViewLocationNode getViewLocation() {
		return (ViewLocationNode)getModel();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart#refreshVisuals()
	 */
	@Override
	protected void refreshVisuals() {
		ViewLocationNode location = getViewLocation();
		View view = DiagramRebuildUtils.getView(location);
		if (getRoot() != null) {
			EditPartViewer viewer = getViewer();
			if (viewer != null) {
				Object value = viewer.getEditPartRegistry().get(view);
				if (value instanceof GraphicalEditPart) {
					GraphicalEditPart gep = (GraphicalEditPart)value;
					getFigure().setBounds(new Rectangle(gep.getFigure().getBounds()));
				}
			}
		}
		super.refreshVisuals();
	}

	/**
	 * Creates a note figure.
	 */
	protected NodeFigure createNodeFigure() {
		EmptyNodeFigure figure = new EmptyNodeFigure();
		figure.setBorder(new LineBorder(1));
		return figure;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#isSelectable()
	 */
	@Override
	public boolean isSelectable() {
		return false;
	}

	/** Adds support for diagram links. */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();

		// This View doesn't have semantic elements so use a component edit
		// policy that only gets a command to delete the view
		installEditPolicy(
			EditPolicy.COMPONENT_ROLE,
			new ViewComponentEditPolicy());
	}
}
