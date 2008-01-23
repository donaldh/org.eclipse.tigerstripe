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
package org.eclipse.tigerstripe.workbench.ui.eclipse.utils;

import org.eclipse.core.runtime.Platform;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;

public class Debug {

	public static void println(Object srcObj, String message) {
		println(srcObj, DebugOptions.DEBUG, message);
	}

	public static void println(Object srcObj, String debugOption, String message) {
		String debugFlag = Platform.getDebugOption(debugOption);
		String src = "[null]";

		if (srcObj != null) {
			src = "[" + srcObj.getClass().getCanonicalName() + "]";
		}

		if (EclipsePlugin.getDefault().isDebugging()
				&& "true".equalsIgnoreCase(debugFlag)) {
			TigerstripeRuntime.logInfoMessage(src + message);
		}
	}
}
