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
package org.eclipse.tigerstripe.workbench.ui.eclipse.preferences;

import org.eclipse.core.runtime.Preferences.IPropertyChangeListener;
import org.eclipse.core.runtime.Preferences.PropertyChangeEvent;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog;

/**
 * Simply listens to preference changes and acts when needed.
 * 
 * It is registered to listen by the {@link PreferencesInitializer}.
 * 
 * The following preferences are being watched: -
 * {@link GeneralPreferencePage.P_LOGGINGLEVEL}
 * 
 * @author Eric Dillon
 * @since 2.2.1
 */
public class PreferenceListener implements IPropertyChangeListener {

	public void propertyChange(PropertyChangeEvent event) {
		if (GeneralPreferencePage.P_LOGGINGLEVEL.equals(event.getProperty())) {
			String label = (String) event.getNewValue();
			PluginLog.LogLevel newLevel = PluginLog.LogLevel.valueOf(label);
			TigerstripeRuntime.setLogLevel(newLevel);
		}
	}
}
