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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.depsdiagram;

import org.eclipse.swt.graphics.RGB;

public class Utils {

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
