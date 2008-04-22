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
package org.eclipse.tigerstripe.workbench.ui.internal.preferences.modelImport.xmi;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.preferences.ITigerstripePreferences;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class XMITopLevelPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage, ITigerstripePreferences {

	public XMITopLevelPreferencePage() {
		super(GRID);
		setPreferenceStore(EclipsePlugin.getDefault().getPreferenceStore());
		setDescription("XMI Import Preferences");
	}

	public static void initializeDefaults() {

	}

	@Override
	protected void createFieldEditors() {
		// TODO Auto-generated method stub
	}

	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub
	}

}
