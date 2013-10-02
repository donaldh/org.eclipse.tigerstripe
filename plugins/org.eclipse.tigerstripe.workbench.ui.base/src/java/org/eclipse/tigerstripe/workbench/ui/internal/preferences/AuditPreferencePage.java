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

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.preferences.PreferenceConstants;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

/**
 * Preference Page for all general parameters for Tigerstripe Workbench
 * 
 * @author Eric Dillon
 */
public class AuditPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public final static String PAGE_ID = "org.eclipse.tigerstripe.ui.eclipse.preferences.AuditPreferencePage";


	public AuditPreferencePage() {
		super(GRID);
		setPreferenceStore(new ScopedPreferenceStore(
		        new ConfigurationScope(),BasePlugin.PLUGIN_ID));
		setDescription("Preferences for Tigerstripe Project Auditors");
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
		group.setText("Audit Preferences");
		addField(new StringFieldEditor(PreferenceConstants.AUDIT_IGNORE_FOLDERS,
				"Ignore Folders :", group));


	}

	@Override
	protected Control createContents(Composite parent) {
		// TODO Auto-generated method stub
		return super.createContents(parent);
	}

	public void init(IWorkbench workbench) {
	}
}