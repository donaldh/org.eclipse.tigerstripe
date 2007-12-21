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

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.notation.Bendpoints;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.impl.EdgeImpl;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClassClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.policies.MapCanonicalEditPolicy;

public class AssociationClassConnectionEditPart extends ConnectionNodeEditPart {

	private PolylineConnectionEx assocClassConnection = null;
	private AssociationClassEditPart assocClassEditPart = null;
	private View assocClassView;

	public AssociationClassConnectionEditPart(View view,
			AssociationClassEditPart assocClassEditPart) {
		super(view);
		assocClassView = view;
		this.assocClassEditPart = assocClassEditPart;
	}

	@Override
	protected Connection createConnectionFigure() {
		if (assocClassConnection != null)
			return assocClassConnection;
		// get the mapEditPart, and from there the map itself
		EditPart editPart = assocClassEditPart.getSource();
		if (editPart == null)
			return null;
		MapEditPart mapEditPart = (MapEditPart) editPart.getParent();
		EditPart ep = mapEditPart.getParent();

		// get the map's Canonical Edit policy
		MapCanonicalEditPolicy mapCanonicalEditPolicy = (MapCanonicalEditPolicy) mapEditPart
				.getEditPolicy(EditPolicyRoles.CANONICAL_ROLE);

		// get the asociationClassClass object
		EdgeImpl edgeImpl = (EdgeImpl) assocClassEditPart.getModel();
		AssociationClass assocClass = (AssociationClass) edgeImpl.getElement();

		// find the corresponding associationClass object in the map
		AssociationClassClass assocClassClass = assocClass.getAssociatedClass();
		IGraphicalEditPart host = (IGraphicalEditPart) mapCanonicalEditPolicy
				.getHost();
		AssociationClassClassEditPart assocClassClassEditPart = (AssociationClassClassEditPart) host
				.findEditPart(mapEditPart, assocClassClass);
		if (assocClassClassEditPart == null)
			return null;
		// now create the connection from the assocClass to the assocClassClass
		assocClassConnection = new PolylineConnectionEx();
		Bendpoints bendpoints = edgeImpl.getBendpoints();
		EdgeAnchor sourceAnchor = new EdgeAnchor(assocClassEditPart.getFigure());
		ChopboxAnchor targetAnchor = new ChopboxAnchor(assocClassClassEditPart
				.getFigure());
		assocClassConnection.setSourceAnchor(sourceAnchor);
		assocClassConnection.setTargetAnchor(targetAnchor);
		assocClassConnection
				.setLineStyle(org.eclipse.draw2d.Graphics.LINE_DASH);
		assocClassConnection
				.setBackgroundColor(org.eclipse.draw2d.ColorConstants.black);
		// add some bendpoints to the Edge (i.e. to the view the corresponds to
		// this edit part)
		((Edge) assocClassView)
				.createBendpoints(NotationPackage.Literals.RELATIVE_BENDPOINTS);
		// need to add this routing style to avoid a null pointer exception from
		// calling
		// setEnabled(true) so that the connection can be selected...
		((Edge) assocClassView).createStyle(NotationPackage.eINSTANCE
				.getRoutingStyle());
		// set the source and target for this edit part
		this.setSource(assocClassEditPart);
		this.setTarget(assocClassClassEditPart);
		// and add it to the map
		mapEditPart.addAssocClassConnection(this);
		return assocClassConnection;
	}

	private class EdgeAnchor extends AbstractConnectionAnchor {

		public EdgeAnchor(IFigure owner) {
			super(owner);
		}

		public Point getLocation(Point reference) {
			PolylineConnection connection = (PolylineConnection) getOwner();
			PointList pointList = connection.getPoints();
			Point p = pointList.getMidpoint();
			getOwner().translateToAbsolute(p);
			return p;
		}

	}

	public PolylineConnectionEx getAssocClassConnection() {
		return assocClassConnection;
	}

}
