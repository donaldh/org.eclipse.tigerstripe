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

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Preference Page for all general parameters for Tigerstripe Workbench
 * 
 * @author Eric Dillon
 */
public class ExplorerPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public final static String PAGE_ID = "org.eclipse.tigerstripe.workbench.ui.eclipse.preferences.ExplorerPreferencePage";

	// The target generation path
	public static final String P_LABEL_STEREO_ARTIFACT = "p.explorer.label.stereo.artifact";
	public static final String P_LABEL_STEREO_ATTR = "p.explorer.label.stereo.attr";
	public static final String P_LABEL_STEREO_METH = "p.explorer.label.stereo.meth";
	public static final String P_LABEL_STEREO_METHARGS = "p.explorer.label.stereo.methargs";
	public static final String P_LABEL_STEREO_LIT = "p.explorer.label.stereo.lit";
	public static final String P_LABEL_STEREO_END = "p.explorer.label.stereo.end";

	public static final String P_LABEL_SHOW_RELATIONSHIP_ANCHORS = "p.explorer.label.show.relationship.anchors";

	public ExplorerPreferencePage() {
		super(GRID);
		setPreferenceStore(EclipsePlugin.getDefault().getPreferenceStore());
		setDescription("Preferences for Tigerstripe Explorer");
		initializeDefaults();
	}

	/**
	 * Sets the default values of the preferences.
	 */
	public static void initializeDefaults() {
		IPreferenceStore store = EclipsePlugin.getDefault()
				.getPreferenceStore();
		store.setDefault(P_LABEL_STEREO_ARTIFACT, "false");
		store.setDefault(P_LABEL_STEREO_ATTR, "false");
		store.setDefault(P_LABEL_STEREO_METH, "false");
		store.setDefault(P_LABEL_STEREO_METHARGS, "false");
		store.setDefault(P_LABEL_STEREO_LIT, "false");
		store.setDefault(P_LABEL_STEREO_END, "false");
		store.setDefault(P_LABEL_SHOW_RELATIONSHIP_ANCHORS, "false");
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
		GridLayout gl = new GridLayout();
		gl.numColumns = 3;
		group.setLayout(gl);
		group.setText("Display Stereotypes");
		addField(new BooleanFieldEditor(P_LABEL_STEREO_ARTIFACT,
				"on Artifacts", group));
		addField(new BooleanFieldEditor(P_LABEL_STEREO_ATTR, "on Attributes",
				group));
		addField(new BooleanFieldEditor(P_LABEL_STEREO_METH, "on Methods",
				group));
		addField(new BooleanFieldEditor(P_LABEL_STEREO_METHARGS,
				"on Method Arguments", group));
		addField(new BooleanFieldEditor(P_LABEL_STEREO_LIT, "on Literals",
				group));
		addField(new BooleanFieldEditor(P_LABEL_STEREO_END,
				"on Relationship ends", group));

		Group anchorGroup = new Group(getFieldEditorParent(), SWT.TITLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		anchorGroup.setLayoutData(gd);
		anchorGroup.setText("Relationship Anchors");
		addField(new BooleanFieldEditor(P_LABEL_SHOW_RELATIONSHIP_ANCHORS,
				"Show Relationship Anchors", anchorGroup));

	}

	public void init(IWorkbench workbench) {
	}
}