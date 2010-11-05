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
package org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class ColorUtils {

	public static int toColorAsInt(int alpha, int red, int green, int blue) {
		return (alpha << 24) | (red << 16) | (green << 8) | blue;
	}

	public static Color toColor(int color) {
		return new Color(null, (color >> 16) & 255, (color >> 8) & 255,
				color & 255);
	}

	public static RGB toRGB(int color) {
		return new RGB((color >> 16) & 255, (color >> 8) & 255, color & 255);
	}

	public static int fromRGB(RGB rgb) {
		return toColorAsInt(0, rgb.red, rgb.green, rgb.blue);
	}
}
