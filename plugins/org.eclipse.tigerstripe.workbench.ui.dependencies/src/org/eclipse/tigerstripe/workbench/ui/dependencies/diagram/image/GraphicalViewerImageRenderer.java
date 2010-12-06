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
package org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.image;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class GraphicalViewerImageRenderer {
	private static String getSaveFilePath(Shell shell, GraphicalViewer viewer) {
		final FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		dialog.setFilterExtensions(new String[] { "*.png", "*.jpeg", "*.gif",
				"*.bmp", "*.ico" });
		dialog.setFilterIndex(0);
		dialog.setFileName("diagram");
		return dialog.open();
	}

	public static boolean save(Shell shell, GraphicalViewer viewer) {
		final String path = getSaveFilePath(shell, viewer);
		if (path == null) {
			return false;
		}

		int format = SWT.IMAGE_PNG;

		if (path.endsWith(".jpeg")) {
			format = SWT.IMAGE_JPEG;
		} else if (path.endsWith(".bmp")) {
			format = SWT.IMAGE_BMP;
		} else if (path.endsWith(".ico")) {
			format = SWT.IMAGE_ICO;
		} else if (path.endsWith(".png")) {
			format = SWT.IMAGE_PNG;
		} else if (path.endsWith(".gif")) {
			format = SWT.IMAGE_GIF;
		}

		saveEditorContentsAsImage(viewer, path, format);

		return true;
	}

	public static void saveEditorContentsAsImage(GraphicalViewer viewer,
			String saveFilePath, int format) {
		// 1. First get the figure whose visuals we want to save as image. So we
		// would like to save the rooteditpart which actually hosts
		// all the printable layers.
		//
		// NOTE: ScalableRootEditPart manages layers and is registered
		// graphicalviewer's editpartregistry with the key LayerManager.ID ...
		// well that is because ScalableRootEditPart manages all layers that are
		// hosted on a FigureCanvas. Many layers exist for doing
		// different things
		final LayerManager manager = (LayerManager) viewer
				.getEditPartRegistry().get(LayerManager.ID);
		final IFigure rootFigure = manager
				.getLayer(LayerConstants.PRINTABLE_LAYERS);// rootEditPart.getFigure();
		final Rectangle rootFigureBounds = rootFigure.getBounds();

		// 2. Now we want to get the GC associated with the control on which all
		// figures are painted by SWTGraphics. For that first get the
		// SWT Control associated with the viewer on which the rooteditpart is
		// set as contents
		final Control figureCanvas = viewer.getControl();
		final GC figureCanvasGC = new GC(figureCanvas);

		// 3. Create a new Graphics for an Image onto which we want to paint
		// rootFigure
		final Image img = new Image(null, rootFigureBounds.width,
				rootFigureBounds.height);
		final GC imageGC = new GC(img);
		imageGC.setBackground(figureCanvasGC.getBackground());
		imageGC.setForeground(figureCanvasGC.getForeground());
		imageGC.setFont(figureCanvasGC.getFont());
		imageGC.setLineStyle(figureCanvasGC.getLineStyle());
		imageGC.setLineWidth(figureCanvasGC.getLineWidth());
		final Graphics imgGraphics = new SWTGraphics(imageGC);

		// 4. Draw rootFigure onto image. After that image will be ready for
		// save
		rootFigure.paint(imgGraphics);

		final ImageLoader imgLoader = new ImageLoader();
		imgLoader.data = new ImageData[] { img.getImageData() };
		imgLoader.save(saveFilePath, format);

		// release OS resources
		figureCanvasGC.dispose();
		imageGC.dispose();
		img.dispose();
	}
}