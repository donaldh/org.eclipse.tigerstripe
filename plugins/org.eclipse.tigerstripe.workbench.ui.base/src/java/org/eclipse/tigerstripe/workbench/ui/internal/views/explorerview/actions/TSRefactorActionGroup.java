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

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.PerformanceStats;
import org.eclipse.jdt.ui.IContextMenuConstants;
import org.eclipse.jdt.ui.actions.ConvertAnonymousToNestedAction;
import org.eclipse.jdt.ui.actions.ConvertNestedToTopAction;
import org.eclipse.jdt.ui.actions.IJavaEditorActionDefinitionIds;
import org.eclipse.jdt.ui.actions.JdtActionConstants;
import org.eclipse.jdt.ui.actions.ModifyParametersAction;
import org.eclipse.jdt.ui.actions.SelectionDispatchAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.operations.UndoRedoActionGroup;

/**
 * Action group that adds refactor actions (for example 'Rename', 'Move') to a
 * context menu and the global menu bar.
 * 
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 * 
 * @since 0.3.6
 */
public class TSRefactorActionGroup extends BaseActionProvider {

	private static final String PERF_REFACTOR_ACTION_GROUP = "org.eclipse.jdt.ui/perf/explorer/RefactorActionGroup"; //$NON-NLS-1$

	/**
	 * Pop-up menu: id of the refactor sub menu (value
	 * <code>org.eclipse.jdt.ui.refactoring.menu</code>).
	 * 
	 * @since 2.1
	 */
	public static final String MENU_ID = "org.eclipse.jdt.ui.refactoring.menu"; //$NON-NLS-1$

	/**
	 * Pop-up menu: id of the reorg group of the refactor sub menu (value
	 * <code>reorgGroup</code>).
	 * 
	 * @since 2.1
	 */
	public static final String GROUP_REORG = "reorgGroup"; //$NON-NLS-1$

	/**
	 * Pop-up menu: id of the type group of the refactor sub menu (value
	 * <code>typeGroup</code>).
	 * 
	 * @since 2.1
	 */
	public static final String GROUP_TYPE = "typeGroup"; //$NON-NLS-1$

	/**
	 * Pop-up menu: id of the coding group of the refactor sub menu (value
	 * <code>codingGroup</code>).
	 * 
	 * @since 2.1
	 */
	public static final String GROUP_CODING = "codingGroup"; //$NON-NLS-1$

	private String fGroupName = IContextMenuConstants.GROUP_REORGANIZE;

	private SelectionDispatchAction fMoveAction;
	private SelectionDispatchAction fRenameAction;
	private SelectionDispatchAction fModifyParametersAction;
	private SelectionDispatchAction fConvertAnonymousToNestedAction;
	private SelectionDispatchAction fConvertNestedToTopAction;

	private UndoRedoActionGroup fUndoRedoActionGroup;

	@Override
	protected void doInit(IWorkbenchPartSite site) {
		IUndoContext workspaceContext = (IUndoContext) ResourcesPlugin
				.getWorkspace().getAdapter(IUndoContext.class);
		fUndoRedoActionGroup = new UndoRedoActionGroup(site, workspaceContext,
				true);

		final PerformanceStats stats = PerformanceStats.getStats(
				PERF_REFACTOR_ACTION_GROUP, this);
		stats.startRun();

		ISelectionProvider provider = site.getSelectionProvider();
		ISelection selection = provider.getSelection();

		fMoveAction = new TSMoveAction(site);
		fMoveAction
				.setActionDefinitionId(IJavaEditorActionDefinitionIds.MOVE_ELEMENT);
		initAction(fMoveAction, provider, selection);

		fRenameAction = new TSRenameAction(site);
		fRenameAction
				.setActionDefinitionId(IJavaEditorActionDefinitionIds.RENAME_ELEMENT);
		initAction(fRenameAction, provider, selection);

		fModifyParametersAction = new ModifyParametersAction(site);
		fModifyParametersAction
				.setActionDefinitionId(IJavaEditorActionDefinitionIds.MODIFY_METHOD_PARAMETERS);
		initAction(fModifyParametersAction, provider, selection);

		fConvertNestedToTopAction = new ConvertNestedToTopAction(site);
		fConvertNestedToTopAction
				.setActionDefinitionId(IJavaEditorActionDefinitionIds.MOVE_INNER_TO_TOP);
		initAction(fConvertNestedToTopAction, provider, selection);

		fConvertAnonymousToNestedAction = new ConvertAnonymousToNestedAction(
				site);
		fConvertAnonymousToNestedAction
				.setActionDefinitionId(IJavaEditorActionDefinitionIds.CONVERT_ANONYMOUS_TO_NESTED);
		initAction(fConvertAnonymousToNestedAction, provider, selection);

		stats.endRun();
	}

	private static void initAction(SelectionDispatchAction action,
			ISelectionProvider provider, ISelection selection) {
		action.update(selection);
		provider.addSelectionChangedListener(action);
	}

	/*
	 * (non-Javadoc) Method declared in ActionGroup
	 */
	@Override
	public void fillActionBars(IActionBars actionBars) {
		super.fillActionBars(actionBars);
		actionBars.setGlobalActionHandler(JdtActionConstants.MOVE, fMoveAction);
		actionBars.setGlobalActionHandler(JdtActionConstants.RENAME,
				fRenameAction);
		actionBars.setGlobalActionHandler(JdtActionConstants.MODIFY_PARAMETERS,
				fModifyParametersAction);
		actionBars.setGlobalActionHandler(
				JdtActionConstants.CONVERT_ANONYMOUS_TO_NESTED,
				fConvertAnonymousToNestedAction);
		if (fUndoRedoActionGroup != null) {
			fUndoRedoActionGroup.fillActionBars(actionBars);
		}
		retargetFileMenuActions(actionBars);
	}

	/**
	 * Retargets the File actions with the corresponding refactoring actions.
	 * 
	 * @param actionBars
	 *            the action bar to register the move and rename action with
	 */
	public void retargetFileMenuActions(IActionBars actionBars) {
		actionBars.setGlobalActionHandler(ActionFactory.RENAME.getId(),
				fRenameAction);
		actionBars.setGlobalActionHandler(ActionFactory.MOVE.getId(),
				fMoveAction);
	}

	/*
	 * (non-Javadoc) Method declared in ActionGroup
	 */
	@Override
	public void fillContextMenu(IMenuManager menu) {
		super.fillContextMenu(menu);
		addRefactorSubmenu(menu);
	}

	/*
	 * @see ActionGroup#dispose()
	 */
	@Override
	public void dispose() {
		ISelectionProvider provider = getSelectionProvider();
		disposeAction(fMoveAction, provider);
		disposeAction(fRenameAction, provider);
		disposeAction(fModifyParametersAction, provider);
		disposeAction(fConvertNestedToTopAction, provider);
		disposeAction(fConvertAnonymousToNestedAction, provider);
		if (fUndoRedoActionGroup != null) {
			fUndoRedoActionGroup.dispose();
		}
		super.dispose();
	}

	private void disposeAction(ISelectionChangedListener action,
			ISelectionProvider provider) {
		if (action != null)
			provider.removeSelectionChangedListener(action);
	}

	private void addRefactorSubmenu(IMenuManager menu) {
		IMenuManager refactorSubmenu = new MenuManager("Refac&tor", MENU_ID);
		if (fillRefactorMenu(refactorSubmenu) > 0)
			menu.appendToGroup(fGroupName, refactorSubmenu);
	}

	private int fillRefactorMenu(IMenuManager refactorSubmenu) {
		int added = 0;
		refactorSubmenu.add(new Separator(GROUP_REORG));
		added += addAction(refactorSubmenu, fRenameAction);
		added += addAction(refactorSubmenu, fMoveAction);
		added += addAction(refactorSubmenu, fModifyParametersAction);
		added += addAction(refactorSubmenu, fConvertAnonymousToNestedAction);
		added += addAction(refactorSubmenu, fConvertNestedToTopAction);
		refactorSubmenu.add(new Separator(GROUP_TYPE));
		refactorSubmenu.add(new Separator(GROUP_CODING));
		return added;
	}

	private int addAction(IMenuManager menu, IAction action) {
		if (action != null && action.isEnabled()) {
			menu.add(action);
			return 1;
		}
		return 0;
	}
}
