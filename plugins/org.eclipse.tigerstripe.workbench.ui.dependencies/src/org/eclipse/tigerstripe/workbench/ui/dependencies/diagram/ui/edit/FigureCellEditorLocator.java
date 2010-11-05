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
package org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.edit;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Text;

public class FigureCellEditorLocator implements CellEditorLocator {

	private static final int MIN_CHAR_COUNT = 5;

	private final IFigure figure;

	public FigureCellEditorLocator(IFigure figure) {
		this.figure = figure;
	}

	public void relocate(CellEditor celleditor) {
		final Text text = (Text) celleditor.getControl();

		final GC gc = new GC(text);
		gc.setFont(text.getFont());
		final FontMetrics metrics = gc.getFontMetrics();
		gc.dispose();

		final Rectangle rect = figure.getClientArea();
		figure.translateToAbsolute(rect);

		rect.width = Math.max(rect.width,
				MIN_CHAR_COUNT * metrics.getAverageCharWidth());

		org.eclipse.swt.graphics.Rectangle trim = text.computeTrim(0, 0, 0, 0);
		rect.translate(trim.x, trim.y);
		rect.width += trim.width;
		rect.height += trim.height;
		text.setBounds(rect.x, rect.y, rect.width, rect.height);
	}
};
