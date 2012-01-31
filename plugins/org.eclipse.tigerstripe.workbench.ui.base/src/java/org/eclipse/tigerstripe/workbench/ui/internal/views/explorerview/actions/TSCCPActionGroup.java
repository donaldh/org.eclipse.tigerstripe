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

import org.eclipse.jdt.internal.ui.refactoring.reorg.CutAction;
import org.eclipse.jdt.ui.IContextMenuConstants;
import org.eclipse.jdt.ui.actions.SelectionDispatchAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.texteditor.IWorkbenchActionDefinitionIds;

/**
 * Action group that adds the copy, cut, paste actions to a view part's context
 * menu and installs handlers for the corresponding global menu actions.
 * 
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 * 
 * @since 2.0
 */
public class TSCCPActionGroup extends BaseActionProvider {

	private Clipboard fClipboard;

	private SelectionDispatchAction[] fActions;

	private SelectionDispatchAction fDeleteAction;
	private SelectionDispatchAction fCopyAction;
	private SelectionDispatchAction fPasteAction;
	private SelectionDispatchAction fCutAction;

	@Override
	protected void doInit(IWorkbenchPartSite site) {
		fClipboard = new Clipboard(site.getShell().getDisplay());

		fPasteAction = new TSPasteAction(site, fClipboard);
		fPasteAction.setActionDefinitionId(IWorkbenchActionDefinitionIds.PASTE);

		fCopyAction = new TSCopyToClipboadAction(site, fClipboard, fPasteAction);
		fCopyAction.setActionDefinitionId(IWorkbenchActionDefinitionIds.COPY);

		fCutAction = getCutActionInstance();
		fCutAction.setActionDefinitionId(IWorkbenchActionDefinitionIds.CUT);

		fDeleteAction = new TSDeleteAction(site);
		fDeleteAction
				.setActionDefinitionId(IWorkbenchActionDefinitionIds.DELETE);

		fActions = new SelectionDispatchAction[] { fCutAction, fCopyAction,
				fPasteAction, fDeleteAction };
		registerActionsAsSelectionChangeListeners();
	}

	private SelectionDispatchAction getCutActionInstance() {
		CutAction action = new CutAction(getSite());
		return action;
	}

	private void registerActionsAsSelectionChangeListeners() {
		ISelectionProvider provider = getSelectionProvider();
		ISelection selection = provider.getSelection();
		for (int i = 0; i < fActions.length; i++) {
			SelectionDispatchAction action = fActions[i];
			action.update(selection);
			provider.addSelectionChangedListener(action);
		}
	}

	private void deregisterActionsAsSelectionChangeListeners() {
		ISelectionProvider provider = getSelectionProvider();
		for (int i = 0; i < fActions.length; i++) {
			provider.removeSelectionChangedListener(fActions[i]);
		}
	}

	/**
	 * Returns the delete action managed by this action group.
	 * 
	 * @return the delete action. Returns <code>null</code> if the group doesn't
	 *         provide any delete action
	 */
	public IAction getDeleteAction() {
		return fDeleteAction;
	}

	/*
	 * (non-Javadoc) Method declared in ActionGroup
	 */
	@Override
	public void fillActionBars(IActionBars actionBars) {
		super.fillActionBars(actionBars);
		actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(),
				fDeleteAction);
		actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(),
				fCopyAction);
		actionBars
				.setGlobalActionHandler(ActionFactory.CUT.getId(), fCutAction);
		actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(),
				fPasteAction);
	}

	/*
	 * (non-Javadoc) Method declared in ActionGroup
	 */
	@Override
	public void fillContextMenu(IMenuManager menu) {
		super.fillContextMenu(menu);
		for (int i = 0; i < fActions.length; i++) {
			SelectionDispatchAction action = fActions[i];
			if (action == fCutAction && !fCutAction.isEnabled())
				continue;
			menu.appendToGroup(IContextMenuConstants.GROUP_REORGANIZE, action);
		}
	}

	/*
	 * @see ActionGroup#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		if (fClipboard != null) {
			fClipboard.dispose();
			fClipboard = null;
		}
		deregisterActionsAsSelectionChangeListeners();
	}

}
