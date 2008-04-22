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
package org.eclipse.tigerstripe.workbench.ui.internal.gmf;

import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.notation.FillStyle;
import org.eclipse.gmf.runtime.notation.FontStyle;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;

public class InitialDiagramPrefs {

	public final static String DEFAULT_FONTNAME = "Arial";

	public final static int DEFAULT_FONTSIZE = 9;

	public final static int DEFAULT_FONTSTYLE = SWT.NORMAL;

	private final static RGB DEFAULT_FILLCOLOR = new RGB(250, 245, 210);

	private final static FontData DEFAULT_FONTDATA = new FontData(
			DEFAULT_FONTNAME, DEFAULT_FONTSIZE, DEFAULT_FONTSTYLE);

	public static void setDefaultFillColor(View view) {
		FillStyle fillStyle = (FillStyle) view
				.getStyle(NotationPackage.Literals.FILL_STYLE);
		if (fillStyle != null) {
			// fill color
			fillStyle.setFillColor(FigureUtilities.RGBToInteger(
					DEFAULT_FILLCOLOR).intValue());
		}
	}

	public static void setDefaultFontStyle(View view) {
		FontStyle fontStyle = (FontStyle) view
				.getStyle(NotationPackage.Literals.FONT_STYLE);
		if (fontStyle != null) {
			fontStyle.setFontName(DEFAULT_FONTDATA.getName());
			fontStyle.setFontHeight(DEFAULT_FONTDATA.getHeight());
			fontStyle.setBold((DEFAULT_FONTDATA.getStyle() & SWT.BOLD) != 0);
			fontStyle
					.setItalic((DEFAULT_FONTDATA.getStyle() & SWT.ITALIC) != 0);
		}
	}

}
