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
package org.eclipse.tigerstripe.workbench.ui.internal.perspective;

import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TigerstripeExplorerPart;
import org.eclipse.tigerstripe.workbench.ui.internal.views.hierarchy.HierarchyView;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class TigerstripePerspectiveFactory implements IPerspectiveFactory {

	public final static String ID = "org.eclipse.tigerstripe.community.tigerstripePerspective";

	private static final String WIZARDS = "org.eclipse.tigerstripe.workbench.ui.wizards.";
	private static final String WIZARDS_PROJECT = WIZARDS + "newProjectWizard";
	private static final String WIZARDS_PLUGIN = WIZARDS + "newPluginWizard";
	private static final String WIZARDS_GENERATOR = WIZARDS
			+ "newM0GeneratorWizard";

	public TigerstripePerspectiveFactory() {
		super();

	}

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();

		IFolderLayout left = layout.createFolder("left",
				IPageLayout.LEFT, 0.25f, editorArea);

		left.addView(TigerstripeExplorerPart.AEXPLORER_ID);
		left.addPlaceholder(HierarchyView.ID);
		
		IFolderLayout bottom = layout.createFolder("bottom",
				IPageLayout.BOTTOM, 0.66f, editorArea);
		bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
		bottom.addView(IPageLayout.ID_TASK_LIST);
		bottom.addView("org.eclipse.tigerstripe.annotation.view.property");

		layout.addPerspectiveShortcut(ID);
		layout.addShowViewShortcut("org.eclipse.pde.runtime.LogView"); //$NON-NLS-1$
		layout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
		layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);

		layout.addShowViewShortcut(TigerstripeExplorerPart.AEXPLORER_ID);

		layout.addNewWizardShortcut(WIZARDS_PROJECT);
		layout.addNewWizardShortcut(WIZARDS_PLUGIN);
		layout.addNewWizardShortcut(WIZARDS_GENERATOR);
	}
}
