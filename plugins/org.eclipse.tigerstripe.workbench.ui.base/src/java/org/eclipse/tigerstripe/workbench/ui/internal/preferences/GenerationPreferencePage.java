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
package org.eclipse.tigerstripe.workbench.ui.internal.preferences;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.tigerstripe.workbench.project.IAdvancedProperties;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Preference Page for all general parameters for Tigerstripe Workbench
 * 
 * @author Eric Dillon
 */
public class GenerationPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	public final static String PAGE_ID = "org.eclipse.tigerstripe.ui.eclipse.preferences.GenerationPreferencePage";

	// The target generation path
	public static final String P_TARGETPATH = "p.general.target.path";
	public static final String P_DEFAULTPACKAGE = "p.general.default.artifact.package";
	public static final String P_GENERATERUNASLOCAL = IAdvancedProperties.PROP_GENERATION_allRulesLocal;
	public static final String P_GENERATEREPORT = IAdvancedProperties.PROP_GENERATION_GenerateReport;
	public static final String P_LOGMESSAGES = IAdvancedProperties.PROP_GENERATION_LogMessages;

	// public static final String P_DEFAULTINTERFACEPACKAGE =
	// "p.general.default.interface.package";

	public GenerationPreferencePage() {
		super(GRID);
		setPreferenceStore(EclipsePlugin.getDefault().getPreferenceStore());
		setDescription("Preferences for Code Generation in Tigerstripe");
		initializeDefaults();
	}

	/**
	 * Sets the default values of the preferences.
	 */
	public static void initializeDefaults() {
		IPreferenceStore store = EclipsePlugin.getDefault()
				.getPreferenceStore();
		store.setDefault(P_TARGETPATH, "target/tigerstripe.gen");
		store.setDefault(IAdvancedProperties.PROP_GENERATION_GenerateReport,
				"true");
		
		// NM: Look for appropriate extension point and set default value of 'run all rules as local' 
		String runAllRulesDefaultValue = "false";
		IConfigurationElement[] configElements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.eclipse.tigerstripe.workbench.base.runAllRulesAsLocal");
		if (configElements!=null && configElements.length>0) {
			String attribute = configElements[0].getAttribute("value");
			if (attribute!=null && attribute.equalsIgnoreCase("true"))
				runAllRulesDefaultValue = "true";
		}
		
		store.setDefault(IAdvancedProperties.PROP_GENERATION_allRulesLocal,runAllRulesDefaultValue);
		store.setDefault(IAdvancedProperties.PROP_GENERATION_LogMessages,
				"false");


	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */

	@Override
	public void createFieldEditors() {
		// Group group = new Group(getFieldEditorParent(), SWT.TITLE );
		// GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		// gd.horizontalSpan = 2;
		// group.setLayoutData(gd);
		// group.setText("Artifact Preferences");
		// addField(new StringFieldEditor(P_DEFAULTPACKAGE, "&Default Artifact
		// Package:",
		// group));
		// addField(new StringFieldEditor(P_DEFAULTINTERFACEPACKAGE, "&Default
		// Interface Package:",
		// group));

		// Group group1 = new Group(getFieldEditorParent(), SWT.TITLE );
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		getFieldEditorParent().setLayoutData(gd);
		GridLayout gl = new GridLayout();
		gl.numColumns = 3;
		getFieldEditorParent().setLayout(gl);
		// group1.setText("Generation Preferences");
		StringFieldEditor genPath = new StringFieldEditor(P_TARGETPATH,
				"&Generation Path:", getFieldEditorParent());
		addField(genPath);
		addField(new BooleanFieldEditor(
				IAdvancedProperties.PROP_GENERATION_GenerateReport,
				"&Generate report", getFieldEditorParent()));
		addField(new BooleanFieldEditor(
				IAdvancedProperties.PROP_GENERATION_allRulesLocal,
				"Run All Rules as Local", getFieldEditorParent()));
//		addField(new BooleanFieldEditor(
//				IAdvancedProperties.PROP_GENERATION_LogMessages,
//				"&Log messages", getFieldEditorParent()));

	}

	public void init(IWorkbench workbench) {
	}
}