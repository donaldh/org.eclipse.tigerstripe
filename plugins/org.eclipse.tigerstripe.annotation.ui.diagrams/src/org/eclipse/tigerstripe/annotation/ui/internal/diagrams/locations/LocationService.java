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
package org.eclipse.tigerstripe.annotation.ui.internal.diagrams.locations;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RoutingListener;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.annotation.ui.internal.diagrams.utils.ViewFigureUtils;

/**
 * @author Yuri Strot
 */
public class LocationService {
	
	private static Map<View, FigureListener> figureListeners = 
		new HashMap<View, FigureListener>();
	
	private static Map<View, RoutingListener> routingListeners = 
		new HashMap<View, RoutingListener>();
	
	private static Map<View, EditPartViewer> viewers = 
		new HashMap<View, EditPartViewer>();
	
	public static void addListener(final View view, EditPartViewer viewer, final ILocationChangedListener listener) {
		viewers.put(view, viewer);
		IFigure figure = ViewFigureUtils.getFigure(view, viewer);
		if (figure != null) {
			addFigureListener(view, figure, listener);
			if (figure instanceof PolylineConnection)
				addRoutingListener(view, (PolylineConnection)figure, listener);
		}
	}
	
	public static void removeListener(View view) {
		EditPartViewer viewer = viewers.remove(view);
		if (viewer != null) {
			IFigure figure = ViewFigureUtils.getFigure(view, viewer);
			if (figure != null) {
				removeFigureListener(view, figure);
				if (figure instanceof PolylineConnection)
					removeRoutingListener(view, (PolylineConnection)figure);
			}
		}
	}
	
	private static void addFigureListener(View view, IFigure figure, final ILocationChangedListener listener) {
		FigureListener figureListener = new FigureListener() {
			public void figureMoved(IFigure source) {
				listener.locationChanged();
			}
		};
		figureListeners.put(view, figureListener);
		figure.addFigureListener(figureListener);
	}
	
	private static void removeFigureListener(View view, IFigure figure) {
		FigureListener figureListener = figureListeners.remove(view);
		if (figureListener != null)
			figure.removeFigureListener(figureListener);
	}
	
	private static void addRoutingListener(View view, PolylineConnection connection, final ILocationChangedListener listener) {
		RoutingListener routingListener = new RoutingListener.Stub() {
			public void postRoute(Connection connection) {
				listener.locationChanged();
			}
		};
		routingListeners.put(view, routingListener);
		connection.addRoutingListener(routingListener);
	}
	
	private static void removeRoutingListener(View view, PolylineConnection connection) {
		RoutingListener routingListener = routingListeners.remove(view);
		if (routingListener != null)
			connection.removeRoutingListener(routingListener);
	}

}
