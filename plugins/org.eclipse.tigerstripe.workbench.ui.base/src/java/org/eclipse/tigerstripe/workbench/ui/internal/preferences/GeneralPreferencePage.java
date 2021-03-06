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

import org.eclipse.gmf.runtime.common.ui.preferences.ComboFieldEditor;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Preference Page for all general parameters for Tigerstripe Workbench
 * 
 * @author Eric Dillon
 */
public class GeneralPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public final static String PAGE_ID = "org.eclipse.tigerstripe.ui.eclipse.preferences.GeneralPreferencePage";

	// The target generation path
	public static final String P_DEFAULTPACKAGE = "p.general.default.artifact.package";

	public static final String P_LOGGINGLEVEL = "p.general.logging.level";
	
	public static final String P_DONT_REMIND_FILEDS_REMOVING = "p.general.ask.removing.fields";
	public static final String P_DONT_REMIND_METHODS_REMOVING = "p.general.ask.removing.methods";
	public static final String P_DONT_REMIND_LITERAL_REMOVING = "p.general.ask.removing.literals";
	public static final String P_DONT_REMIND_STEREOTYPES_REMOVING = "p.general.ask.removing.stereotypes";
	
	public static final String P_CASCADE_DELETE = "p.general.ask.cascade.delete";

	public static final String P_WEAK_RESTART = "p.general.weakRestart";

	
	// See bug 298971
//	public static final String P_CASCADEDELETE_RELATIONSHIPS = "p.general.cascade.delete.relationships";

	public GeneralPreferencePage() {
		super(GRID);
		setPreferenceStore(EclipsePlugin.getDefault().getPreferenceStore());
		setDescription("General Preferences for Tigerstripe");
		initializeDefaults();
	}

	/**
	 * Sets the default values of the preferences.
	 */
	public static void initializeDefaults() {
		IPreferenceStore store = EclipsePlugin.getDefault()
				.getPreferenceStore();
		store.setDefault(P_DEFAULTPACKAGE, "com.mycompany");
		store.setDefault(P_LOGGINGLEVEL, PluginLog.LogLevel.ERROR.toString());
		store.setDefault(P_DONT_REMIND_FILEDS_REMOVING, false);
		store.setDefault(P_DONT_REMIND_METHODS_REMOVING, false);
		store.setDefault(P_DONT_REMIND_LITERAL_REMOVING, false);
		store.setDefault(P_DONT_REMIND_STEREOTYPES_REMOVING, false);
		store.setDefault(P_CASCADE_DELETE, MessageDialogWithToggle.PROMPT);
		store.setDefault(P_WEAK_RESTART, false);
		// See bug 298971
//		store.setDefault(P_CASCADEDELETE_RELATIONSHIPS, "true");
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
		group.setText("Artifact Preferences");
		addField(new StringFieldEditor(P_DEFAULTPACKAGE,
				"&Default Artifact Package:", group));

		// See bug 298971
//		Group modelGroup = new Group(getFieldEditorParent(), SWT.TITLE);
//		gd = new GridData(GridData.FILL_HORIZONTAL);
//		gd.horizontalSpan = 2;
//		modelGroup.setLayoutData(gd);
//		modelGroup.setText("Modelling Preferences");
//		addField(new BooleanFieldEditor(P_CASCADEDELETE_RELATIONSHIPS,
//				"&Cascade Delete Relationships", modelGroup));

		Group loggingGroup = new Group(getFieldEditorParent(), SWT.TITLE);
		GridLayout gl1 = new GridLayout();
		gl1.numColumns = 2;
		group.setLayout(gl1);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		gd.heightHint = 60;
		loggingGroup.setLayoutData(gd);
		loggingGroup.setText("Logging Preferences");

		ComboFieldEditor loggingLevelEditor = new ComboFieldEditor(
				P_LOGGINGLEVEL, "&Log Level:", loggingGroup);
		for (PluginLog.LogLevel level : PluginLog.LogLevel.values()) {
			loggingLevelEditor.addIndexedItemToCombo(level.name(), level
					.toInt());
		}
		addField(loggingLevelEditor);

		Label l = new Label(loggingGroup, SWT.NONE);
		l.setText("");

		Composite subCompo = new Composite(loggingGroup, SWT.NULL);
		GridData gd1 = new GridData(GridData.FILL_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_END);
		subCompo.setLayoutData(gd1);
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		subCompo.setLayout(gl);

		Button openLogButton = new Button(subCompo, SWT.PUSH);

		openLogButton.setText("Open Log...");
		openLogButton
				.setToolTipText("Open the current Tigerstripe.log in a seperate editor");
		openLogButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				String logPath = TigerstripeRuntime.getLogPath();
				org.eclipse.swt.program.Program.launch(logPath);
			}
		});

		Group reminderGroup = new Group(getFieldEditorParent(), SWT.TITLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		reminderGroup.setLayoutData(gd);
		reminderGroup.setText("Don't Remind About Removing");
		
		addField(new BooleanFieldEditor(P_DONT_REMIND_FILEDS_REMOVING,
				"&Attributes", reminderGroup));

		addField(new BooleanFieldEditor(P_DONT_REMIND_METHODS_REMOVING,
				"&Methods", reminderGroup));

		addField(new BooleanFieldEditor(P_DONT_REMIND_LITERAL_REMOVING,
				"&Constants", reminderGroup));

		addField(new BooleanFieldEditor(P_DONT_REMIND_STEREOTYPES_REMOVING,
				"&Stereotypes", reminderGroup));

		addField(new RadioGroupFieldEditor(P_CASCADE_DELETE,
				"Casca&de Delete", 3, new String[][] {
						{ "Always delete", MessageDialogWithToggle.ALWAYS },
						{ "Never delete", MessageDialogWithToggle.NEVER },
						{ "Prompt", MessageDialogWithToggle.PROMPT } },
				getFieldEditorParent(), true));
		
		Group otherGroup = new Group(getFieldEditorParent(), SWT.TITLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		otherGroup.setLayoutData(gd);
		otherGroup.setText("Other");
		
		addField(new BooleanFieldEditor(P_WEAK_RESTART, "Weak &Restart",
				otherGroup));

	}

	@Override
	protected Control createContents(Composite parent) {
		// TODO Auto-generated method stub
		return super.createContents(parent);
	}

	public void init(IWorkbench workbench) {
	}
}