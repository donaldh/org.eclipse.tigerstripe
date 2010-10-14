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

import org.eclipse.jdt.ui.IContextMenuConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions.BaseActionProvider;
import org.eclipse.ui.IWorkbenchPartSite;

public class DiagramActionGroup extends BaseActionProvider {

	private CustomDiagramPreferencesAction preferencesAction;

	@Override
	protected void doInit(IWorkbenchPartSite site) {
		preferencesAction = new CustomDiagramPreferencesAction(site);
		preferencesAction.setText("Diagram Settings...");
	}

	@Override
	public void fillContextMenu(IMenuManager menu) {
		appendToGroup(menu, preferencesAction);
	}

	@Override
	public void dispose() {
		ISelectionProvider provider = getSelectionProvider();
		provider.removeSelectionChangedListener(preferencesAction);
		super.dispose();
	}

	private void appendToGroup(IMenuManager menu, IAction action) {
		if (action.isEnabled())
			menu.appendToGroup(IContextMenuConstants.GROUP_PROPERTIES, action);
	}
}
