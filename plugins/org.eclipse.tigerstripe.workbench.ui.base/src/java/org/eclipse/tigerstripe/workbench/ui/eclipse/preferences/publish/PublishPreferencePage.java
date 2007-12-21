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
package org.eclipse.tigerstripe.workbench.ui.eclipse.preferences.publish;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Preference Page for all general parameters for Tigerstripe Workbench
 * 
 * @author Eric Dillon
 */
public class PublishPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public static final String P_PUBLISH_USEDEFAULT_LOGO = "p.publish.useDefaultLogo";
	public static final String P_PUBLISH_LOGO = "p.publish.logo";
	public static final String P_PUBLISH_COPYRIGHT = "p.publish.useDefaultLogo";

	private BooleanFieldEditor useDefaultLogoField;
	private FileFieldEditor publishLogoFile;
	private StringFieldEditor copyrightFooter;

	public PublishPreferencePage() {
		super(GRID);
		setPreferenceStore(EclipsePlugin.getDefault().getPreferenceStore());
		setDescription("Publish Preferences for Tigerstripe");
		initializeDefaults();
	}

	/**
	 * Sets the default values of the preferences.
	 */
	public static void initializeDefaults() {
		IPreferenceStore store = EclipsePlugin.getDefault()
				.getPreferenceStore();
		store.setDefault(P_PUBLISH_USEDEFAULT_LOGO, "true");
		store.setDefault(P_PUBLISH_LOGO, "");
		store.setDefault(P_PUBLISH_COPYRIGHT, "2006, no copyright");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */

	@Override
	public void createFieldEditors() {
		int numColumns = 4;
		Group group = new Group(getFieldEditorParent(), SWT.TITLE);
		GridLayout gl = new GridLayout();
		gl.numColumns = numColumns;
		group.setLayout(gl);
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL));
		group.setText("Branding Options");

		useDefaultLogoField = new BooleanFieldEditor(P_PUBLISH_USEDEFAULT_LOGO,
				"&Use default Tigerstripe logo", group);
		useDefaultLogoField
				.setPropertyChangeListener(new IPropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent event) {
						publishLogoFile.setEnabled(!useDefaultLogoField
								.getBooleanValue(), null);
					}
				});
		useDefaultLogoField.fillIntoGrid(group, numColumns);

		publishLogoFile = new FileFieldEditor(P_PUBLISH_LOGO, "Image:", false,
				group);
		publishLogoFile.fillIntoGrid(group, numColumns);
		copyrightFooter = new StringFieldEditor(P_PUBLISH_COPYRIGHT,
				"Copyright footer:", group);
		copyrightFooter.fillIntoGrid(group, numColumns);
		publishLogoFile.setEnabled(!useDefaultLogoField.getBooleanValue(),
				group);

		addField(useDefaultLogoField);
		addField(publishLogoFile);
		addField(copyrightFooter);
	}

	public void init(IWorkbench workbench) {
	}
}