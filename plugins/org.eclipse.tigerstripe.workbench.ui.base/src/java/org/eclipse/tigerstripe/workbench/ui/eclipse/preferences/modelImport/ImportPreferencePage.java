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
package org.eclipse.tigerstripe.workbench.ui.eclipse.preferences.modelImport;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Group;
import org.eclipse.tigerstripe.api.project.IAdvancedProperties;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Preference Page for all general parameters for Tigerstripe Workbench
 * 
 * @author Eric Dillon
 */
public class ImportPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public final static String PAGE_ID = "org.eclipse.tigerstripe.workbench.ui.eclipse.preferences.ModelImportPreferencePage";

	public ImportPreferencePage() {
		super(GRID);
		setPreferenceStore(EclipsePlugin.getDefault().getPreferenceStore());
		setDescription("Model Import Preferences for Tigerstripe");
		initializeDefaults();
	}

	/**
	 * Sets the default values of the preferences.
	 */
	public static void initializeDefaults() {
		IPreferenceStore store = EclipsePlugin.getDefault()
				.getPreferenceStore();
		store
				.setDefault(
						IAdvancedProperties.PROP_IMPORT_USETARGETPROJECTASGUIDE,
						"true");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */

	@Override
	public void createFieldEditors() {
		Group group = new Group(getFieldEditorParent(), SWT.TITLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		group.setLayoutData(gd);
		group.setText("General");
		addField(new BooleanFieldEditor(
				IAdvancedProperties.PROP_IMPORT_USETARGETPROJECTASGUIDE,
				"&Use target project as guide", group));
	}

	public void init(IWorkbench workbench) {
	}
}