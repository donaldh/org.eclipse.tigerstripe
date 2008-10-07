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

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.workbench.internal.TigerstripeRuntimeDetails;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.util.license.LicensedAccess;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>,
 * we can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class TopLevelPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public final static String PAGE_ID = "org.eclipse.tigerstripe.workbench.ui.eclipse.preferences.TopLevelPreferences";

	private Label imageLabel;

	public TopLevelPreferencePage() {
		super(GRID);
		setPreferenceStore(EclipsePlugin.getDefault().getPreferenceStore());
		initializeDefaults();
	}

	/**
	 * Sets the default values of the preferences.
	 */
	public static void initializeDefaults() {
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite fieldEditorParent = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		fieldEditorParent.setLayout(layout);
		fieldEditorParent.setFont(parent.getFont());

		createLocalContent(fieldEditorParent);

		// initialize();
		// checkState();
		return fieldEditorParent;
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createLocalContent(Composite parent) {

		GridData gd = new GridData();
		gd.horizontalSpan = 3;
		gd.grabExcessHorizontalSpace = true;
		gd.minimumWidth = 400;
		Label label = new Label(parent, SWT.NULL);
		
		label.setText(TigerstripeRuntimeDetails.INSTANCE.getBaseBundleValue("Bundle-Name"));
		label.setAlignment(SWT.LEFT);
		label.setLayoutData(gd);

		label = new Label(parent, SWT.NULL);
		label
				.setText("  version "
						+ TigerstripeRuntimeDetails.INSTANCE.getBaseBundleValue("Bundle-Version"));
		label.setLayoutData(gd);

		label = new Label(parent, SWT.NULL);
		label.setLayoutData(gd);

		label = new Label(parent, SWT.NULL);
		label.setText("Installation Root:");
		label.setLayoutData(gd);

		label = new Label(parent, SWT.NULL);
		label.setText("  " + TigerstripeRuntime.getBaseBundleRoot());
		label.setLayoutData(gd);

		label = new Label(parent, SWT.NULL);
		label.setText("Runtime Root:");
		label.setLayoutData(gd);

		label = new Label(parent, SWT.NULL);
		label.setText("  " + TigerstripeRuntime.getTigerstripeRuntimeRoot());
		label.setLayoutData(gd);
		LicensedAccess licensedAccess = null;

		licensedAccess = LicensedAccess.getInstance();

	}

	public void init(IWorkbench workbench) {
	}

	@Override
	protected void createFieldEditors() {
		// make the compiler happy since we overide createContents()
	}
}