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

import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.eclipse.preferences.modelImport.ImportPreferencePage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.preferences.modelImport.xmi.XMITopLevelPreferencePage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.preferences.ossj.OssjJVTPreferencePage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.preferences.publish.PublishPreferencePage;

/**
 * A convenience class that initializes the default values of the preference
 * store upon start of the plugin.
 * 
 * 
 * @author Eric Dillon
 * 
 */
public class PreferencesInitializer {

	private final static PreferenceListener prefListener = new PreferenceListener();

	/**
	 * Initializes all the defaults in the preference store
	 * 
	 * This is called once upon Plugin Start up
	 * 
	 */
	public static void initialize() {

		TopLevelPreferencePage.initializeDefaults();

		OssjPreferencePage.initializeDefaults();
		LicensePreferencePage.initializeDefaults();

		GeneralPreferencePage.initializeDefaults();
		GenerationPreferencePage.initializeDefaults();
		AdvancedPreferencePage.initializeDefaults();
		PublishPreferencePage.initializeDefaults();

		OssjJVTPreferencePage.initializeDefaults();
		ImportPreferencePage.initializeDefaults();
		XMITopLevelPreferencePage.initializeDefaults();

		// registering preference listener so the values can be propagated as
		// needed
		// into Tigerstripe.
		EclipsePlugin.getDefault().getPluginPreferences()
				.addPropertyChangeListener(prefListener);
	}

	public static void dispose() {
		EclipsePlugin.getDefault().getPluginPreferences()
				.removePropertyChangeListener(prefListener);
	}
}
