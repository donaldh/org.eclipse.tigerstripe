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

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.PerformanceStats;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.actions.ActionUtil;
import org.eclipse.jdt.internal.ui.actions.JDTQuickMenuAction;
import org.eclipse.jdt.internal.ui.actions.SelectionConverter;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.internal.ui.javaeditor.JavaTextSelection;
import org.eclipse.jdt.internal.ui.refactoring.RefactoringMessages;
import org.eclipse.jdt.ui.IContextMenuConstants;
import org.eclipse.jdt.ui.actions.SelectionDispatchAction;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.tigerstripe.workbench.ui.eclipse.actions.OpenCSVWizardAction;
import org.eclipse.tigerstripe.workbench.ui.eclipse.actions.OpenModuleExportWizardAction;
import org.eclipse.tigerstripe.workbench.ui.eclipse.actions.OpenPublishWizardAction;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IKeyBindingService;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.operations.UndoRedoActionGroup;
import org.eclipse.ui.part.Page;

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
public class TSPublishActionGroup extends ActionGroup {

	private static final String PERF_REFACTOR_ACTION_GROUP = "org.eclipse.jdt.ui/perf/explorer/RefactorActionGroup"; //$NON-NLS-1$

	/**
	 * Pop-up menu: id of the refactor sub menu (value
	 * <code>org.eclipse.jdt.ui.refactoring.menu</code>).
	 * 
	 * @since 2.1
	 */
	public static final String MENU_ID = "org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.actions.publish.menu"; //$NON-NLS-1$

	/**
	 * Pop-up menu: id of the reorg group of the refactor sub menu (value
	 * <code>reorgGroup</code>).
	 * 
	 * @since 2.1
	 */
	public static final String GROUP_PACKAGE = "packageGroup"; //$NON-NLS-1$

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

	private IWorkbenchSite fSite;
	private CompilationUnitEditor fEditor;
	private String fGroupName = IContextMenuConstants.GROUP_GENERATE;

	private OpenModuleExportWizardAction fModuleExportAction;
	private OpenPublishWizardAction fPublishAction;
	private OpenCSVWizardAction fCSVAction;

	private UndoRedoActionGroup fUndoRedoActionGroup;

	private List fEditorActions;

	private static final String QUICK_MENU_ID = "org.eclipse.jdt.ui.edit.text.java.refactor.quickMenu"; //$NON-NLS-1$

	private class RefactorQuickAccessAction extends JDTQuickMenuAction {
		public RefactorQuickAccessAction(CompilationUnitEditor editor) {
			super(editor, QUICK_MENU_ID);
		}

		@Override
		protected void fillMenu(IMenuManager menu) {
			fillQuickMenu(menu);
		}
	}

	private RefactorQuickAccessAction fQuickAccessAction;
	private IKeyBindingService fKeyBindingService;

	private static class NoActionAvailable extends Action {
		public NoActionAvailable() {
			setEnabled(true);
			setText(RefactoringMessages.RefactorActionGroup_no_refactoring_available);
		}
	}

	private Action fNoActionAvailable = new NoActionAvailable();

	/**
	 * Creates a new <code>RefactorActionGroup</code>. The group requires
	 * that the selection provided by the part's selection provider is of type
	 * <code>
	 * org.eclipse.jface.viewers.IStructuredSelection</code>.
	 * 
	 * @param part
	 *            the view part that owns this action group
	 */
	public TSPublishActionGroup(IViewPart part) {
		this(part.getSite(), part.getSite().getKeyBindingService());

		IUndoContext workspaceContext = (IUndoContext) ResourcesPlugin
				.getWorkspace().getAdapter(IUndoContext.class);
		fUndoRedoActionGroup = new UndoRedoActionGroup(part.getViewSite(),
				workspaceContext, true);
	}

	/**
	 * Creates a new <code>RefactorActionGroup</code>. The action requires
	 * that the selection provided by the page's selection provider is of type
	 * <code>
	 * org.eclipse.jface.viewers.IStructuredSelection</code>.
	 * 
	 * @param page
	 *            the page that owns this action group
	 */
	public TSPublishActionGroup(Page page) {
		this(page.getSite(), null);
	}

	private TSPublishActionGroup(IWorkbenchSite site,
			IKeyBindingService keyBindingService) {

		final PerformanceStats stats = PerformanceStats.getStats(
				PERF_REFACTOR_ACTION_GROUP, this);
		stats.startRun();

		fSite = site;
		ISelectionProvider provider = fSite.getSelectionProvider();
		ISelection selection = provider.getSelection();

		fModuleExportAction = new OpenModuleExportWizardAction();
		fPublishAction = new OpenPublishWizardAction();
		fCSVAction = new OpenCSVWizardAction();
		// fModuleExportAction.setActionDefinitionId("org.eclipse.tigerstripe.workbench.ui.eclipe.packageProject");

		// fKeyBindingService= keyBindingService;
		// if (fKeyBindingService != null) {
		// fQuickAccessAction= new RefactorQuickAccessAction(null);
		// fKeyBindingService.registerAction(fQuickAccessAction);
		// }
		//		
		stats.endRun();
	}

	/*
	 * (non-Javadoc) Method declared in ActionGroup
	 */
	@Override
	public void fillActionBars(IActionBars actionBars) {
		super.fillActionBars(actionBars);

		if (fUndoRedoActionGroup != null) {
			fUndoRedoActionGroup.fillActionBars(actionBars);
		}
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
		ISelectionProvider provider = fSite.getSelectionProvider();

		if (fQuickAccessAction != null && fKeyBindingService != null) {
			fKeyBindingService.unregisterAction(fQuickAccessAction);
		}
		if (fUndoRedoActionGroup != null) {
			fUndoRedoActionGroup.dispose();
		}
		super.dispose();
	}

	private void addRefactorSubmenu(IMenuManager menu) {
		String menuText = "Publish";
		if (fQuickAccessAction != null) {
			menuText = fQuickAccessAction.addShortcut(menuText);
		}

		IMenuManager refactorSubmenu = new MenuManager(menuText, MENU_ID);
		if (fEditor != null) {
			IJavaElement element = SelectionConverter.getInput(fEditor);
			if (element != null && ActionUtil.isOnBuildPath(element)) {
				refactorSubmenu.addMenuListener(new IMenuListener() {
					public void menuAboutToShow(IMenuManager manager) {
						publishMenuShown(manager);
					}
				});
				refactorSubmenu.add(fNoActionAvailable);
				menu.appendToGroup(fGroupName, refactorSubmenu);
			}
		} else {
			if (fillRefactorMenu(refactorSubmenu) > 0)
				menu.appendToGroup(fGroupName, refactorSubmenu);
		}
	}

	private int fillRefactorMenu(IMenuManager publishSubmenu) {
		int added = 0;
		publishSubmenu.add(new Separator(GROUP_PACKAGE));
		added += addAction(publishSubmenu, fModuleExportAction);
		added += addAction(publishSubmenu, fPublishAction);
		added += addAction(publishSubmenu, fCSVAction);
		return added;
	}

	private int addAction(IMenuManager menu, IAction action) {
		if (action != null && action.isEnabled()) {
			menu.add(action);
			return 1;
		}
		return 0;
	}

	private void publishMenuShown(final IMenuManager refactorSubmenu) {
		// we know that we have an MenuManager since we created it in
		// addRefactorSubmenu.
		Menu menu = ((MenuManager) refactorSubmenu).getMenu();
		menu.addMenuListener(new MenuAdapter() {
			@Override
			public void menuHidden(MenuEvent e) {
				refactorMenuHidden(refactorSubmenu);
			}
		});
		ITextSelection textSelection = (ITextSelection) fEditor
				.getSelectionProvider().getSelection();
		JavaTextSelection javaSelection = new JavaTextSelection(
				getEditorInput(), getDocument(), textSelection.getOffset(),
				textSelection.getLength());

		for (Iterator iter = fEditorActions.iterator(); iter.hasNext();) {
			SelectionDispatchAction action = (SelectionDispatchAction) iter
					.next();
			action.update(javaSelection);
		}
		refactorSubmenu.removeAll();
		if (fillRefactorMenu(refactorSubmenu) == 0)
			refactorSubmenu.add(fNoActionAvailable);
	}

	private void refactorMenuHidden(IMenuManager manager) {
		ITextSelection textSelection = (ITextSelection) fEditor
				.getSelectionProvider().getSelection();
		for (Iterator iter = fEditorActions.iterator(); iter.hasNext();) {
			SelectionDispatchAction action = (SelectionDispatchAction) iter
					.next();
			action.update(textSelection);
		}
	}

	private IJavaElement getEditorInput() {
		return JavaPlugin.getDefault().getWorkingCopyManager().getWorkingCopy(
				fEditor.getEditorInput());
	}

	private IDocument getDocument() {
		return JavaPlugin.getDefault().getCompilationUnitDocumentProvider()
				.getDocument(fEditor.getEditorInput());
	}

	private void fillQuickMenu(IMenuManager menu) {
		if (fEditor != null) {
			IJavaElement element = SelectionConverter.getInput(fEditor);
			if (element == null || !ActionUtil.isOnBuildPath(element)) {
				menu.add(fNoActionAvailable);
				return;
			}
			ITextSelection textSelection = (ITextSelection) fEditor
					.getSelectionProvider().getSelection();
			JavaTextSelection javaSelection = new JavaTextSelection(
					getEditorInput(), getDocument(), textSelection.getOffset(),
					textSelection.getLength());

			for (Iterator iter = fEditorActions.iterator(); iter.hasNext();) {
				((SelectionDispatchAction) iter.next()).update(javaSelection);
			}
			fillRefactorMenu(menu);
			for (Iterator iter = fEditorActions.iterator(); iter.hasNext();) {
				((SelectionDispatchAction) iter.next()).update(textSelection);
			}
		} else {
			fillRefactorMenu(menu);
		}
	}
}
