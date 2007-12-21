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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.utils;

import org.eclipse.tigerstripe.workbench.ui.visualeditor.Visibility;

public class ClassDiagramPartsUtils {

	public static String visibilityPrefix(Visibility vis) {

		String prefix = "";

		if (vis != null) {
			if (vis == Visibility.PUBLIC_LITERAL) {
				prefix = "+";
			} else if (vis == Visibility.PROTECTED_LITERAL) {
				prefix = "#";
			} else if (vis == Visibility.PRIVATE_LITERAL) {
				prefix = "-";
			} else if (vis == Visibility.PACKAGE_LITERAL) {
				prefix = "~";
			}
		}

		return prefix;
	}
}
