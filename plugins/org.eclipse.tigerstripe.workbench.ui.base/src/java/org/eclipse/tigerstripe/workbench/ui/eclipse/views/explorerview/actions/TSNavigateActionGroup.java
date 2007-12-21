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
package org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.internal.ui.actions.ActionMessages;
import org.eclipse.jdt.ui.IContextMenuConstants;
import org.eclipse.jdt.ui.actions.IJavaEditorActionDefinitionIds;
import org.eclipse.jdt.ui.actions.NavigateActionGroup;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.actions.OpenWithMenu;

public class TSNavigateActionGroup extends NavigateActionGroup {

	private TSOpenAction fOpenAction;

	private IWorkbenchPartSite fSite;

	public TSNavigateActionGroup(IViewPart part) {
		super(part);
		fSite = part.getSite();
		fOpenAction = new TSOpenAction(fSite);
		fOpenAction
				.setActionDefinitionId(IJavaEditorActionDefinitionIds.OPEN_EDITOR);
		initialize(fSite.getSelectionProvider());
	}

	@Override
	public IAction getOpenAction() {
		return fOpenAction;
	}

	@Override
	public void fillContextMenu(IMenuManager menu) {
		appendToGroup(menu, fOpenAction);
		addOpenWithMenu(menu);
	}

	private void initialize(ISelectionProvider provider) {
		ISelection selection = provider.getSelection();
		fOpenAction.update(selection);
		provider.addSelectionChangedListener(fOpenAction);
	}

	private void appendToGroup(IMenuManager menu, IAction action) {
		if (action.isEnabled())
			menu.appendToGroup(IContextMenuConstants.GROUP_OPEN, action);
	}

	private void addOpenWithMenu(IMenuManager menu) {
		ISelection selection = getContext().getSelection();
		if (selection.isEmpty() || !(selection instanceof IStructuredSelection))
			return;
		IStructuredSelection ss = (IStructuredSelection) selection;
		if (ss.size() != 1)
			return;

		Object o = ss.getFirstElement();
		if (!(o instanceof IAdaptable))
			return;

		IAdaptable element = (IAdaptable) o;
		Object resource = element.getAdapter(IResource.class);
		if (!(resource instanceof IFile))
			return;

		// Create a menu.
		IMenuManager submenu = new MenuManager(
				ActionMessages.OpenWithMenu_label);
		submenu.add(new OpenWithMenu(fSite.getPage(), (IFile) resource));

		// Add the submenu.
		menu.appendToGroup(IContextMenuConstants.GROUP_OPEN, submenu);
	}

}
