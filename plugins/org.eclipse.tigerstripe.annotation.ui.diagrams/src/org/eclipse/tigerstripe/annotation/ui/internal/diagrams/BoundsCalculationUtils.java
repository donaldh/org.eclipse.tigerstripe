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
package org.eclipse.tigerstripe.annotation.ui.internal.diagrams;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author Yuri Strot
 *
 */
public class BoundsCalculationUtils {
	
	public static Rectangle getBounds(View view, EditPartViewer viewer) {
		if (view instanceof Node)
			return getBounds((Node)view, viewer);
		if (view instanceof Edge) {
			return getBounds((Edge)view, viewer);
		}
		return null;
	}
	
	public static void update(View view, EditPartViewer viewer) {
		if (view instanceof Node)
			update((Node)view, viewer);
		if (view instanceof Edge) {
			update((Edge)view, viewer);
		}
	}
	
	protected static IFigure getFigure(View view, EditPartViewer viewer) {
		GraphicalEditPart part = (GraphicalEditPart)viewer.getEditPartRegistry().get(view);
		if (part != null)
			return part.getFigure();
		return null;
	}
	
	protected static Rectangle getBounds(Node node, EditPartViewer viewer) {
		IFigure figure = getFigure(node, viewer);
		if (figure != null) {
			return figure.getBounds();
		}
		return null;
	}
	
	protected static void update(Node node, EditPartViewer viewer) {
		IFigure figure = getFigure(node, viewer);
		if (figure != null) {
			IFigure parent = figure.getParent();
			if (parent != null && parent.getLayoutManager() != null)
				parent.getLayoutManager().layout(parent);
		}
	}
	
	protected static Rectangle getBounds(Edge edge, EditPartViewer viewer) {
		IFigure figure = getFigure(edge, viewer);
		if (figure instanceof Connection) {
			Connection connection = (Connection)figure;
			Point center = getCenter(connection);
			if (center != null) {
				return new Rectangle(center.x, center.y, 0, 0);
			}
		}
		return null;
	}
	
	protected static void update(Edge edge, EditPartViewer viewer) {
		IFigure figure = getFigure(edge, viewer);
		if (figure instanceof Connection) {
			Connection connection = (Connection)figure;
			connection.getConnectionRouter().route(connection);
		}
	}
	
	protected static Point getCenter(Connection connection) {
		PointList points = connection.getPoints();
		if (points.size() == 0)
			return null;
		double[] lengths = new double[points.size() - 1];
		double length = 0;
		for (int i = 0; i < lengths.length; i++) {
			Point p1 = points.getPoint(i);
			Point p2 = points.getPoint(i + 1);
			double dx = p2.x - p1.x;
			dx *= dx;
			double dy = p2.y - p1.y;
			dy *= dy;
			lengths[i] = Math.sqrt(dx + dy);
			length += lengths[i];
		}
		length /= 2;
		double curLength = 0;
		for (int i = 0; i < lengths.length; i++) {
			curLength += lengths[i];
			if (curLength >= length) {
				double dif = curLength - length;
				Point p1 = points.getPoint(i + 1);
				Point p2 = points.getPoint(i);
				int nx = (int)(p1.x + dif * (p2.x - p1.x) / lengths[i]);
				int ny = (int)(p1.y + dif * (p2.y - p1.y) / lengths[i]);
				return new Point(nx, ny);
			}
		}
		return null;
	}

}
