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
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions;

import org.eclipse.jdt.internal.ui.IJavaHelpContextIds;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TSExplorerMessages;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TigerstripeExplorerPart;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;

/**
 * Adds view menus to switch between flat and hierarchical layout.
 * 
 * @since 2.1
 */
class LayoutActionGroup extends MultiActionGroup {

	LayoutActionGroup(TigerstripeExplorerPart tsExplorer) {
		super(createActions(tsExplorer), getSelectedState(tsExplorer));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ActionGroup#fillActionBars(IActionBars)
	 */
	@Override
	public void fillActionBars(IActionBars actionBars) {
		super.fillActionBars(actionBars);
		contributeToViewMenu(actionBars.getMenuManager());
	}

	private void contributeToViewMenu(IMenuManager viewMenu) {
		viewMenu.add(new Separator());

		// Create layout sub menu

		IMenuManager layoutSubMenu = new MenuManager(
				TSExplorerMessages.LayoutActionGroup_label);
		final String layoutGroupName = "layout"; //$NON-NLS-1$
		Separator marker = new Separator(layoutGroupName);

		viewMenu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		viewMenu.add(marker);
		viewMenu.appendToGroup(layoutGroupName, layoutSubMenu);
		viewMenu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS
				+ "-end"));//$NON-NLS-1$		
		addActions(layoutSubMenu);
	}

	static int getSelectedState(TigerstripeExplorerPart packageExplorer) {
		if (packageExplorer.isFlatLayout())
			return 0;
		else
			return 1;
	}

	static IAction[] createActions(TigerstripeExplorerPart packageExplorer) {
		IAction flatLayoutAction = new LayoutAction(packageExplorer, true);
		flatLayoutAction
				.setText(TSExplorerMessages.LayoutActionGroup_flatLayoutAction_label);
		JavaPluginImages.setLocalImageDescriptors(flatLayoutAction,
				"flatLayout.gif"); //$NON-NLS-1$
		IAction hierarchicalLayout = new LayoutAction(packageExplorer, false);
		hierarchicalLayout
				.setText(TSExplorerMessages.LayoutActionGroup_hierarchicalLayoutAction_label);
		JavaPluginImages.setLocalImageDescriptors(hierarchicalLayout,
				"hierarchicalLayout.gif"); //$NON-NLS-1$

		return new IAction[] { flatLayoutAction, hierarchicalLayout };
	}
}

class LayoutAction extends Action implements IAction {

	private boolean fIsFlatLayout;

	private TigerstripeExplorerPart tsExplorer;

	public LayoutAction(TigerstripeExplorerPart packageExplorer, boolean flat) {
		super("", AS_RADIO_BUTTON); //$NON-NLS-1$

		fIsFlatLayout = flat;
		tsExplorer = packageExplorer;
		if (fIsFlatLayout)
			PlatformUI.getWorkbench().getHelpSystem().setHelp(this,
					IJavaHelpContextIds.LAYOUT_FLAT_ACTION);
		else
			PlatformUI.getWorkbench().getHelpSystem().setHelp(this,
					IJavaHelpContextIds.LAYOUT_HIERARCHICAL_ACTION);
	}

	/*
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	@Override
	public void run() {
		if (tsExplorer.isFlatLayout() != fIsFlatLayout)
			tsExplorer.toggleLayout();
	}
}
