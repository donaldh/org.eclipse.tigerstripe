/*******************************************************************************
 * Copyright (c) 2011 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     xored software, Inc. - initial API and implementation (Valentin Erastov)
 ******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.preferences;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;

@SuppressWarnings("deprecation")
public class TSPreferences {

	public static String defaultArtifactPackage() {
		Preferences store = EclipsePlugin.getDefault().getPluginPreferences();
		String result = store.getString(GeneralPreferencePage.P_DEFAULTPACKAGE);
		if (result == null || result.isEmpty()) {
			return "com.mycompany";
		} else {
			return result;
		}
	}

}
