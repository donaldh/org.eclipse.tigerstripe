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

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Group;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Preference Page for all general parameters for Tigerstripe Workbench
 * 
 * @author Eric Dillon
 */
public class AdvancedPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	// The target generation path
	public static final String P_IGNORE_ELEMENTS_WITHOUT_TS_TAGS = "p.ignore.elements.without.tags";

	public AdvancedPreferencePage() {
		super(GRID);
		setPreferenceStore(EclipsePlugin.getDefault().getPreferenceStore());
		setDescription("Advanced Preferences for Tigerstripe");
		initializeDefaults();
	}

	/**
	 * Sets the default values of the preferences.
	 */
	public static void initializeDefaults() {
		IPreferenceStore store = EclipsePlugin.getDefault()
				.getPreferenceStore();
		store.setDefault(P_IGNORE_ELEMENTS_WITHOUT_TS_TAGS, "true");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */

	@Override
	public void createFieldEditors() {
		Group group = new Group(getFieldEditorParent(), SWT.TITLE);
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setText("Artifact Parsing");
		addField(new BooleanFieldEditor(P_IGNORE_ELEMENTS_WITHOUT_TS_TAGS,
				"&Ignore attributes/methods/constants without Tags", group));
	}

	public void init(IWorkbench workbench) {
	}
}