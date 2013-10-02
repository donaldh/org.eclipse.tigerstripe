/*******************************************************************************
 * Copyright (c) 2013 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;


public class CorePreferencesInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IEclipsePreferences node = new DefaultScope().getNode(BasePlugin.PLUGIN_ID);
		node.put(PreferenceConstants.AUDIT_IGNORE_FOLDERS, PreferenceConstants.AUDIT_IGNORE_FOLDERS_DEFAULT); //$NON-NLS-1$
	}

}
