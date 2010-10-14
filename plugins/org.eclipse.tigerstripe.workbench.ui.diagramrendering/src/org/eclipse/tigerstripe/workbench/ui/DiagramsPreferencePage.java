/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui;

import static org.eclipse.tigerstripe.workbench.ui.internal.gmf.DiagramsPreferences.EXTENDS_RELATIONSHIP_VALUE_HIDE;
import static org.eclipse.tigerstripe.workbench.ui.internal.gmf.DiagramsPreferences.EXTENDS_RELATIONSHIP_VALUE_SHOW;
import static org.eclipse.tigerstripe.workbench.ui.internal.gmf.DiagramsPreferences.P_EXTENDS_RELATIONSHIP;
import static org.eclipse.tigerstripe.workbench.ui.internal.gmf.DiagramsPreferences.P_ROUTING_AVOID_OBSTRUCTIONS;
import static org.eclipse.tigerstripe.workbench.ui.internal.gmf.DiagramsPreferences.P_ROUTING_CLOSEST_DISTANCE;
import static org.eclipse.tigerstripe.workbench.ui.internal.gmf.DiagramsPreferences.P_ROUTING_STYLE;
import static org.eclipse.tigerstripe.workbench.ui.internal.gmf.DiagramsPreferences.P_SHOW_COMPARTMENTS;
import static org.eclipse.tigerstripe.workbench.ui.internal.gmf.DiagramsPreferences.ROUTING_STYLE_VALUE_OBLIQUE;
import static org.eclipse.tigerstripe.workbench.ui.internal.gmf.DiagramsPreferences.ROUTING_STYLE_VALUE_RECTILINEAR;
import static org.eclipse.tigerstripe.workbench.ui.internal.gmf.DiagramsPreferences.SHOW_COMPARTMENTS_VALUE_ALL;
import static org.eclipse.tigerstripe.workbench.ui.internal.gmf.DiagramsPreferences.SHOW_COMPARTMENTS_VALUE_NAME_ONLY;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.DiagramsPreferences;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class DiagramsPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public DiagramsPreferencePage() {
		this(DiagramsPreferences.getGlobalStore());
	}

	public DiagramsPreferencePage(IPreferenceStore store) {
		super(GRID);
		setPreferenceStore(store);
		setDescription("Preferences for Tigerstripe Diagrams");
	}

	@Override
	public void createFieldEditors() {

		Group routingGroup = new Group(getFieldEditorParent(), SWT.TITLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		routingGroup.setLayoutData(gd);
		GridLayout gl = new GridLayout();
		gl.numColumns = 3;
		routingGroup.setLayout(gl);
		routingGroup.setText("Line Routing");

		Group generalGroup = new Group(getFieldEditorParent(), SWT.TITLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		generalGroup.setLayoutData(gd);
		generalGroup.setText("General");

		addField(new RadioGroupFieldEditor(P_ROUTING_STYLE, "Style", 2,
				new String[][] { { "Oblique", ROUTING_STYLE_VALUE_OBLIQUE },
						{ "Rectilinear", ROUTING_STYLE_VALUE_RECTILINEAR } },
				routingGroup));

		addField(new BooleanFieldEditor(P_ROUTING_AVOID_OBSTRUCTIONS,
				"Avoid Obstructions", routingGroup));

		addField(new BooleanFieldEditor(P_ROUTING_CLOSEST_DISTANCE,
				"Closest Distance", routingGroup));

		addField(new RadioGroupFieldEditor(P_SHOW_COMPARTMENTS,
				"Show Compartments", 2, new String[][] {
						{ "All", SHOW_COMPARTMENTS_VALUE_ALL },
						{ "Name Only", SHOW_COMPARTMENTS_VALUE_NAME_ONLY } },
				generalGroup));

		addField(new RadioGroupFieldEditor(P_EXTENDS_RELATIONSHIP,
				"Extends Relationship", 2, new String[][] {
						{ "Show", EXTENDS_RELATIONSHIP_VALUE_SHOW },
						{ "Hide", EXTENDS_RELATIONSHIP_VALUE_HIDE } },
				generalGroup));
	}

	public void init(IWorkbench workbench) {
	}
	
	@Override
	public Button getApplyButton() {
		return super.getApplyButton();
	}
	
	@Override
	public Button getDefaultsButton() {
		return super.getDefaultsButton();
	}
}