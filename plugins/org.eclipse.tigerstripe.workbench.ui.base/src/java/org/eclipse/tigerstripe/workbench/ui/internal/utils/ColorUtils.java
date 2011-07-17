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
package org.eclipse.tigerstripe.workbench.ui.internal.utils;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class ColorUtils {

	public final static Color BLACK = new Color(null, 0, 0, 0);

	public final static Color WHITE = new Color(null, 255, 255, 255);

	public final static Color LIGHT_GREY = new Color(null, 128, 128, 128);
	
	public final static Color DARK_GREY = new Color(null, 100, 100, 200);

	public final static Color TS_ORANGE = new Color(null, 232, 123, 20);

	public static RGB sate(RGB color, int value) {
		int r = ensureColor(color.red + value);
		int g = ensureColor(color.green + value);
		int b = ensureColor(color.blue + value);
		return new RGB(r, g, b);
	}

	public static int ensureColor(int value) {
		value = value > 255 ? 255 : value;
		return value < 0 ? 0 : value;
	}
}
