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

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.RootEditPart;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.ui.PlatformUI;

public class DiagramUtils {

	public static void copyToImage(RootEditPart root, OutputStream stream,
			int format) {
		ImageLoader loader = new ImageLoader();
		Image image = createImage(root);
		loader.data = new ImageData[] { image.getImageData() };
		loader.save(stream, format);
	}

	public static Image createImage(RootEditPart root) {
		GraphicalEditPart parent = (GraphicalEditPart) root.getContents();
		List<?> children = parent.getChildren();
		List<GraphicalEditPart> connections = new ArrayList<GraphicalEditPart>();
		GraphicalEditPart[] parts = new GraphicalEditPart[children.size()];
		for (int i = 0; i < parts.length; i++) {
			parts[i] = (GraphicalEditPart) children.get(i);
			for (Object connection : parts[i].getSourceConnections()) {
				if (connection instanceof GraphicalEditPart) {
					connections.add((GraphicalEditPart) connection);
				}
			}
		}

		org.eclipse.swt.graphics.Rectangle bounds = getBounds(parts);
		Image image = new Image(PlatformUI.getWorkbench().getDisplay(), bounds);
		GC gc = new GC(image);
		SWTGraphics graphics = new SWTGraphics(gc);

		graphics.translate(-bounds.x, -bounds.y);
		graphics.pushState();

		paint(graphics, parent);
		for (GraphicalEditPart connection : connections) {
			paint(graphics, connection);
		}

		gc.dispose();
		return image;
	}

	private static void paint(Graphics graphics, GraphicalEditPart part) {
		IFigure figure = part.getFigure();
		if (figure.isVisible()) {
			figure.paint(graphics);
		}
	}

	private static org.eclipse.swt.graphics.Rectangle getBounds(
			GraphicalEditPart[] parts) {
		int minX = 0;
		int maxX = 0;
		int minY = 0;
		int maxY = 0;

		for (int i = 0; i < parts.length; i++) {
			IFigure figure = parts[i].getFigure();

			Rectangle bounds = figure.getBounds();
			bounds = bounds.getExpanded(10, 10);

			if (i == 0) {
				minX = bounds.x;
				maxX = bounds.x + bounds.width;
				minY = bounds.y;
				maxY = bounds.y + bounds.height;
			} else {
				minX = Math.min(minX, bounds.x);
				maxX = Math.max(maxX, (bounds.x + bounds.width));
				minY = Math.min(minY, bounds.y);
				maxY = Math.max(maxY, (bounds.y + bounds.height));
			}
		}

		int width = (maxX - minX);
		int height = (maxY - minY);
		// create an empty image if the
		// diagram does not contain child
		if (width <= 0) {
			width = 100;
		}
		if (height <= 0) {
			height = 100;
		}
		org.eclipse.swt.graphics.Rectangle imageRect = new org.eclipse.swt.graphics.Rectangle(
				minX, minY, width, height);
		return imageRect;
	}

}
