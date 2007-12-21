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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.PerformanceStats;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.actions.ActionMessages;
import org.eclipse.jdt.internal.ui.actions.ActionUtil;
import org.eclipse.jdt.internal.ui.actions.JDTQuickMenuAction;
import org.eclipse.jdt.internal.ui.actions.SelectionConverter;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.internal.ui.javaeditor.JavaTextSelection;
import org.eclipse.jdt.internal.ui.refactoring.RefactoringMessages;
import org.eclipse.jdt.ui.IContextMenuConstants;
import org.eclipse.jdt.ui.actions.ConvertAnonymousToNestedAction;
import org.eclipse.jdt.ui.actions.ConvertNestedToTopAction;
import org.eclipse.jdt.ui.actions.IJavaEditorActionDefinitionIds;
import org.eclipse.jdt.ui.actions.JdtActionConstants;
import org.eclipse.jdt.ui.actions.ModifyParametersAction;
import org.eclipse.jdt.ui.actions.MoveAction;
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
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IKeyBindingService;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.actions.ActionFactory;
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
public class TSRefactorActionGroup extends ActionGroup {

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

	private IWorkbenchSite fSite;
	private CompilationUnitEditor fEditor;
	private String fGroupName = IContextMenuConstants.GROUP_REORGANIZE;

	private SelectionDispatchAction fMoveAction;
	private SelectionDispatchAction fRenameAction;
	private SelectionDispatchAction fModifyParametersAction;
	private SelectionDispatchAction fConvertAnonymousToNestedAction;
	private SelectionDispatchAction fConvertNestedToTopAction;

	// private SelectionDispatchAction fConvertArtifactAction;

	// private SelectionDispatchAction fPullUpAction;
	// private SelectionDispatchAction fPushDownAction;
	// private SelectionDispatchAction fExtractInterfaceAction;
	// private SelectionDispatchAction fChangeTypeAction;
	// private SelectionDispatchAction fUseSupertypeAction;
	// private SelectionDispatchAction fInferTypeArgumentsAction;
	//	
	// private SelectionDispatchAction fInlineAction;
	// private SelectionDispatchAction fExtractMethodAction;
	// private SelectionDispatchAction fExtractTempAction;
	// private SelectionDispatchAction fExtractConstantAction;
	// private SelectionDispatchAction fIntroduceParameterAction;
	// private SelectionDispatchAction fIntroduceFactoryAction;
	// private SelectionDispatchAction fConvertLocalToFieldAction;
	// private SelectionDispatchAction fSelfEncapsulateField;

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
	public TSRefactorActionGroup(IViewPart part) {
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
	public TSRefactorActionGroup(Page page) {
		this(page.getSite(), null);
	}

	/**
	 * Note: This constructor is for internal use only. Clients should not call
	 * this constructor.
	 * 
	 * @param editor
	 *            the compilation unit editor
	 * @param groupName
	 *            the group name to add the actions to
	 */
	public TSRefactorActionGroup(CompilationUnitEditor editor, String groupName) {

		final PerformanceStats stats = PerformanceStats.getStats(
				PERF_REFACTOR_ACTION_GROUP, this);
		stats.startRun();

		fSite = editor.getEditorSite();
		fEditor = editor;
		fGroupName = groupName;
		ISelectionProvider provider = editor.getSelectionProvider();
		ISelection selection = provider.getSelection();
		fEditorActions = new ArrayList();

		fRenameAction = new TSRenameAction(editor);
		fRenameAction
				.setActionDefinitionId(IJavaEditorActionDefinitionIds.RENAME_ELEMENT);
		fRenameAction.update(selection);
		editor.setAction("RenameElement", fRenameAction); //$NON-NLS-1$
		fEditorActions.add(fRenameAction);

		fMoveAction = new MoveAction(editor);
		fMoveAction
				.setActionDefinitionId(IJavaEditorActionDefinitionIds.MOVE_ELEMENT);
		fMoveAction.update(selection);
		editor.setAction("MoveElement", fMoveAction); //$NON-NLS-1$
		fEditorActions.add(fMoveAction);

		fModifyParametersAction = new ModifyParametersAction(editor);
		fModifyParametersAction
				.setActionDefinitionId(IJavaEditorActionDefinitionIds.MODIFY_METHOD_PARAMETERS);
		fModifyParametersAction.update(selection);
		editor.setAction("ModifyParameters", fModifyParametersAction); //$NON-NLS-1$
		fEditorActions.add(fModifyParametersAction);

		fConvertAnonymousToNestedAction = new ConvertAnonymousToNestedAction(
				editor);
		fConvertAnonymousToNestedAction
				.setActionDefinitionId(IJavaEditorActionDefinitionIds.CONVERT_ANONYMOUS_TO_NESTED);
		initAction(fConvertAnonymousToNestedAction, provider, selection);
		editor.setAction(
				"ConvertAnonymousToNested", fConvertAnonymousToNestedAction); //$NON-NLS-1$
		fEditorActions.add(fConvertAnonymousToNestedAction);

		fConvertNestedToTopAction = new ConvertNestedToTopAction(editor);
		fConvertNestedToTopAction
				.setActionDefinitionId(IJavaEditorActionDefinitionIds.MOVE_INNER_TO_TOP);
		fConvertNestedToTopAction.update(selection);
		editor.setAction("MoveInnerToTop", fConvertNestedToTopAction); //$NON-NLS-1$
		fEditorActions.add(fConvertNestedToTopAction);

		// fConvertArtifactAction = new ConvertArtifactAction(editor);
		fConvertNestedToTopAction.update(selection);
		// editor.setAction("ConvertArtifact", fConvertArtifactAction);
		// //$NON-NLS-1$
		// fEditorActions.add(fConvertArtifactAction);

		// fPullUpAction= new PullUpAction(editor);
		// fPullUpAction.setActionDefinitionId(IJavaEditorActionDefinitionIds.PULL_UP);
		// fPullUpAction.update(selection);
		// editor.setAction("PullUp", fPullUpAction); //$NON-NLS-1$
		// fEditorActions.add(fPullUpAction);

		// fPushDownAction= new PushDownAction(editor);
		// fPushDownAction.setActionDefinitionId(IJavaEditorActionDefinitionIds.PUSH_DOWN);
		// fPushDownAction.update(selection);
		// editor.setAction("PushDown", fPushDownAction); //$NON-NLS-1$
		// fEditorActions.add(fPushDownAction);

		// fExtractInterfaceAction= new ExtractInterfaceAction(editor);
		// fExtractInterfaceAction.setActionDefinitionId(IJavaEditorActionDefinitionIds.EXTRACT_INTERFACE);
		// fExtractInterfaceAction.update(selection);
		// editor.setAction("ExtractInterface", fExtractInterfaceAction);
		// //$NON-NLS-1$
		// fEditorActions.add(fExtractInterfaceAction);

		// fChangeTypeAction= new ChangeTypeAction(editor);
		// fChangeTypeAction.setActionDefinitionId(IJavaEditorActionDefinitionIds.CHANGE_TYPE);
		// initAction(fChangeTypeAction, provider, selection);
		// editor.setAction("ChangeType", fChangeTypeAction); //$NON-NLS-1$
		// fEditorActions.add(fChangeTypeAction);

		// fUseSupertypeAction= new UseSupertypeAction(editor);
		// fUseSupertypeAction.setActionDefinitionId(IJavaEditorActionDefinitionIds.USE_SUPERTYPE);
		// fUseSupertypeAction.update(selection);
		// editor.setAction("UseSupertype", fUseSupertypeAction); //$NON-NLS-1$
		// fEditorActions.add(fUseSupertypeAction);

		// fInferTypeArgumentsAction= new InferTypeArgumentsAction(editor);
		// fInferTypeArgumentsAction.setActionDefinitionId(IJavaEditorActionDefinitionIds.INFER_TYPE_ARGUMENTS_ACTION);
		// fInferTypeArgumentsAction.update(selection);
		// editor.setAction("InferTypeArguments", fInferTypeArgumentsAction);
		// //$NON-NLS-1$
		// fEditorActions.add(fInferTypeArgumentsAction);

		// fInlineAction= new InlineAction(editor);
		// fInlineAction.setActionDefinitionId(IJavaEditorActionDefinitionIds.INLINE);
		// fInlineAction.update(selection);
		// editor.setAction("Inline", fInlineAction); //$NON-NLS-1$
		// fEditorActions.add(fInlineAction);

		// fExtractMethodAction= new ExtractMethodAction(editor);
		// fExtractMethodAction.setActionDefinitionId(IJavaEditorActionDefinitionIds.EXTRACT_METHOD);
		// initAction(fExtractMethodAction, provider, selection);
		// editor.setAction("ExtractMethod", fExtractMethodAction);
		// //$NON-NLS-1$
		// fEditorActions.add(fExtractMethodAction);

		// fExtractTempAction= new ExtractTempAction(editor);
		// fExtractTempAction.setActionDefinitionId(IJavaEditorActionDefinitionIds.EXTRACT_LOCAL_VARIABLE);
		// initAction(fExtractTempAction, provider, selection);
		// editor.setAction("ExtractLocalVariable", fExtractTempAction);
		// //$NON-NLS-1$
		// fEditorActions.add(fExtractTempAction);

		// fExtractConstantAction= new ExtractConstantAction(editor);
		// fExtractConstantAction.setActionDefinitionId(IJavaEditorActionDefinitionIds.EXTRACT_CONSTANT);
		// initAction(fExtractConstantAction, provider, selection);
		// editor.setAction("ExtractConstant", fExtractConstantAction);
		// //$NON-NLS-1$
		// fEditorActions.add(fExtractConstantAction);

		// fIntroduceParameterAction= new IntroduceParameterAction(editor);
		// fIntroduceParameterAction.setActionDefinitionId(IJavaEditorActionDefinitionIds.INTRODUCE_PARAMETER);
		// initAction(fIntroduceParameterAction, provider, selection);
		// editor.setAction("IntroduceParameter", fIntroduceParameterAction);
		// //$NON-NLS-1$
		// fEditorActions.add(fIntroduceParameterAction);

		// fIntroduceFactoryAction= new IntroduceFactoryAction(editor);
		// fIntroduceFactoryAction.setActionDefinitionId(IJavaEditorActionDefinitionIds.INTRODUCE_FACTORY);
		// initAction(fIntroduceFactoryAction, provider, selection);
		// editor.setAction("IntroduceFactory", fIntroduceFactoryAction);
		// //$NON-NLS-1$
		// fEditorActions.add(fIntroduceFactoryAction);

		// fConvertLocalToFieldAction= new ConvertLocalToFieldAction(editor);
		// fConvertLocalToFieldAction.setActionDefinitionId(IJavaEditorActionDefinitionIds.PROMOTE_LOCAL_VARIABLE);
		// initAction(fConvertLocalToFieldAction, provider, selection);
		// editor.setAction("PromoteTemp", fConvertLocalToFieldAction);
		// //$NON-NLS-1$
		// fEditorActions.add(fConvertLocalToFieldAction);

		// fSelfEncapsulateField= new SelfEncapsulateFieldAction(editor);
		// fSelfEncapsulateField.setActionDefinitionId(IJavaEditorActionDefinitionIds.SELF_ENCAPSULATE_FIELD);
		// fSelfEncapsulateField.update(selection);
		// editor.setAction("SelfEncapsulateField", fSelfEncapsulateField);
		// //$NON-NLS-1$
		// fEditorActions.add(fSelfEncapsulateField);

		fQuickAccessAction = new RefactorQuickAccessAction(editor);
		fKeyBindingService = editor.getEditorSite().getKeyBindingService();
		fKeyBindingService.registerAction(fQuickAccessAction);

		stats.endRun();
	}

	private TSRefactorActionGroup(IWorkbenchSite site,
			IKeyBindingService keyBindingService) {

		final PerformanceStats stats = PerformanceStats.getStats(
				PERF_REFACTOR_ACTION_GROUP, this);
		stats.startRun();

		fSite = site;
		ISelectionProvider provider = fSite.getSelectionProvider();
		ISelection selection = provider.getSelection();

		fMoveAction = new TSMoveAction(site);
		fMoveAction
				.setActionDefinitionId(IJavaEditorActionDefinitionIds.MOVE_ELEMENT);
		initAction(fMoveAction, provider, selection);

		fRenameAction = new TSRenameAction(site);
		fRenameAction
				.setActionDefinitionId(IJavaEditorActionDefinitionIds.RENAME_ELEMENT);
		initAction(fRenameAction, provider, selection);

		fModifyParametersAction = new ModifyParametersAction(fSite);
		fModifyParametersAction
				.setActionDefinitionId(IJavaEditorActionDefinitionIds.MODIFY_METHOD_PARAMETERS);
		initAction(fModifyParametersAction, provider, selection);

		// fConvertArtifactAction = new ConvertArtifactAction(site);
		// fConvertArtifactAction.setActionDefinitionId("org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.actions.ConvertArtifactAction");
		// initAction(fConvertArtifactAction, provider, selection);

		// fPullUpAction= new PullUpAction(fSite);
		// fPullUpAction.setActionDefinitionId(IJavaEditorActionDefinitionIds.PULL_UP);
		// initAction(fPullUpAction, provider, selection);

		// fPushDownAction= new PushDownAction(fSite);
		// fPushDownAction.setActionDefinitionId(IJavaEditorActionDefinitionIds.PUSH_DOWN);
		// initAction(fPushDownAction, provider, selection);

		// fSelfEncapsulateField= new SelfEncapsulateFieldAction(fSite);
		// fSelfEncapsulateField.setActionDefinitionId(IJavaEditorActionDefinitionIds.SELF_ENCAPSULATE_FIELD);
		// initAction(fSelfEncapsulateField, provider, selection);

		// fExtractInterfaceAction= new ExtractInterfaceAction(fSite);
		// fExtractInterfaceAction.setActionDefinitionId(IJavaEditorActionDefinitionIds.EXTRACT_INTERFACE);
		// initAction(fExtractInterfaceAction, provider, selection);

		// fChangeTypeAction= new ChangeTypeAction(fSite);
		// fChangeTypeAction.setActionDefinitionId(IJavaEditorActionDefinitionIds.CHANGE_TYPE);
		// initAction(fChangeTypeAction, provider, selection);

		fConvertNestedToTopAction = new ConvertNestedToTopAction(fSite);
		fConvertNestedToTopAction
				.setActionDefinitionId(IJavaEditorActionDefinitionIds.MOVE_INNER_TO_TOP);
		initAction(fConvertNestedToTopAction, provider, selection);

		// fUseSupertypeAction= new UseSupertypeAction(fSite);
		// fUseSupertypeAction.setActionDefinitionId(IJavaEditorActionDefinitionIds.USE_SUPERTYPE);
		// initAction(fUseSupertypeAction, provider, selection);

		// fInferTypeArgumentsAction= new InferTypeArgumentsAction(fSite);
		// fInferTypeArgumentsAction.setActionDefinitionId(IJavaEditorActionDefinitionIds.INFER_TYPE_ARGUMENTS_ACTION);
		// initAction(fInferTypeArgumentsAction, provider, selection);

		// fInlineAction= new InlineAction(fSite);
		// fInlineAction.setActionDefinitionId(IJavaEditorActionDefinitionIds.INLINE);
		// initAction(fInlineAction, provider, selection);

		// fIntroduceFactoryAction= new IntroduceFactoryAction(fSite);
		// fIntroduceFactoryAction.setActionDefinitionId(IJavaEditorActionDefinitionIds.INTRODUCE_FACTORY);
		// initAction(fIntroduceFactoryAction, provider, selection);

		fConvertAnonymousToNestedAction = new ConvertAnonymousToNestedAction(
				fSite);
		fConvertAnonymousToNestedAction
				.setActionDefinitionId(IJavaEditorActionDefinitionIds.CONVERT_ANONYMOUS_TO_NESTED);
		initAction(fConvertAnonymousToNestedAction, provider, selection);

		fKeyBindingService = keyBindingService;
		if (fKeyBindingService != null) {
			fQuickAccessAction = new RefactorQuickAccessAction(null);
			fKeyBindingService.registerAction(fQuickAccessAction);
		}

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
		// actionBars.setGlobalActionHandler(JdtActionConstants.SELF_ENCAPSULATE_FIELD,
		// fSelfEncapsulateField);
		actionBars.setGlobalActionHandler(JdtActionConstants.MOVE, fMoveAction);
		actionBars.setGlobalActionHandler(JdtActionConstants.RENAME,
				fRenameAction);
		actionBars.setGlobalActionHandler(JdtActionConstants.MODIFY_PARAMETERS,
				fModifyParametersAction);
		// actionBars.setGlobalActionHandler("org.eclipse.tigerstripe.eclise.ui.actions.Rename",
		// fConvertArtifactAction);
		// actionBars.setGlobalActionHandler(JdtActionConstants.PULL_UP,
		// fPullUpAction);
		// actionBars.setGlobalActionHandler(JdtActionConstants.PUSH_DOWN,
		// fPushDownAction);
		// actionBars.setGlobalActionHandler(JdtActionConstants.EXTRACT_TEMP,
		// fExtractTempAction);
		// actionBars.setGlobalActionHandler(JdtActionConstants.EXTRACT_CONSTANT,
		// fExtractConstantAction);
		// actionBars.setGlobalActionHandler(JdtActionConstants.INTRODUCE_PARAMETER,
		// fIntroduceParameterAction);
		// actionBars.setGlobalActionHandler(JdtActionConstants.INTRODUCE_FACTORY,
		// fIntroduceFactoryAction);
		// actionBars.setGlobalActionHandler(JdtActionConstants.EXTRACT_METHOD,
		// fExtractMethodAction);
		// actionBars.setGlobalActionHandler(JdtActionConstants.INLINE,
		// fInlineAction);
		// actionBars.setGlobalActionHandler(JdtActionConstants.EXTRACT_INTERFACE,
		// fExtractInterfaceAction);
		// actionBars.setGlobalActionHandler(JdtActionConstants.CHANGE_TYPE,
		// fChangeTypeAction);
		// actionBars.setGlobalActionHandler(JdtActionConstants.CONVERT_NESTED_TO_TOP,
		// fConvertNestedToTopAction);
		// actionBars.setGlobalActionHandler(JdtActionConstants.USE_SUPERTYPE,
		// fUseSupertypeAction);
		// actionBars.setGlobalActionHandler(JdtActionConstants.INFER_TYPE_ARGUMENTS,
		// fInferTypeArgumentsAction);
		// actionBars.setGlobalActionHandler(JdtActionConstants.CONVERT_LOCAL_TO_FIELD,
		// fConvertLocalToFieldAction);
		actionBars.setGlobalActionHandler(
				JdtActionConstants.CONVERT_ANONYMOUS_TO_NESTED,
				fConvertAnonymousToNestedAction);
		if (fUndoRedoActionGroup != null) {
			fUndoRedoActionGroup.fillActionBars(actionBars);
		}
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
		ISelectionProvider provider = fSite.getSelectionProvider();
		// disposeAction(fSelfEncapsulateField, provider);
		disposeAction(fMoveAction, provider);
		disposeAction(fRenameAction, provider);
		// disposeAction(fConvertArtifactAction, provider);
		disposeAction(fModifyParametersAction, provider);
		// disposeAction(fPullUpAction, provider);
		// disposeAction(fPushDownAction, provider);
		// disposeAction(fExtractTempAction, provider);
		// disposeAction(fExtractConstantAction, provider);
		// disposeAction(fIntroduceParameterAction, provider);
		// disposeAction(fIntroduceFactoryAction, provider);
		// disposeAction(fExtractMethodAction, provider);
		// disposeAction(fInlineAction, provider);
		// disposeAction(fExtractInterfaceAction, provider);
		// disposeAction(fChangeTypeAction, provider);
		disposeAction(fConvertNestedToTopAction, provider);
		// disposeAction(fUseSupertypeAction, provider);
		// disposeAction(fInferTypeArgumentsAction, provider);
		// disposeAction(fConvertLocalToFieldAction, provider);
		disposeAction(fConvertAnonymousToNestedAction, provider);
		if (fQuickAccessAction != null && fKeyBindingService != null) {
			fKeyBindingService.unregisterAction(fQuickAccessAction);
		}
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
		String menuText = ActionMessages.RefactorMenu_label;
		if (fQuickAccessAction != null) {
			menuText = fQuickAccessAction.addShortcut(menuText);
		}
		IMenuManager refactorSubmenu = new MenuManager(menuText, MENU_ID);
		if (fEditor != null) {
			IJavaElement element = SelectionConverter.getInput(fEditor);
			if (element != null && ActionUtil.isOnBuildPath(element)) {
				refactorSubmenu.addMenuListener(new IMenuListener() {
					public void menuAboutToShow(IMenuManager manager) {
						refactorMenuShown(manager);
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

	private int fillRefactorMenu(IMenuManager refactorSubmenu) {
		int added = 0;
		refactorSubmenu.add(new Separator(GROUP_REORG));
		added += addAction(refactorSubmenu, fRenameAction);
		added += addAction(refactorSubmenu, fMoveAction);
		// added+= addAction(refactorSubmenu, fConvertArtifactAction);
		added += addAction(refactorSubmenu, fModifyParametersAction);
		added += addAction(refactorSubmenu, fConvertAnonymousToNestedAction);
		added += addAction(refactorSubmenu, fConvertNestedToTopAction);
		refactorSubmenu.add(new Separator(GROUP_TYPE));
		// added+= addAction(refactorSubmenu, fPullUpAction);
		// added+= addAction(refactorSubmenu, fPushDownAction);
		// added+= addAction(refactorSubmenu, fExtractInterfaceAction);
		// added+= addAction(refactorSubmenu, fChangeTypeAction);
		// added+= addAction(refactorSubmenu, fUseSupertypeAction);
		// added+= addAction(refactorSubmenu, fInferTypeArgumentsAction);
		refactorSubmenu.add(new Separator(GROUP_CODING));
		// added+= addAction(refactorSubmenu, fInlineAction);
		// added+= addAction(refactorSubmenu, fExtractMethodAction);
		// added+= addAction(refactorSubmenu, fExtractTempAction);
		// added+= addAction(refactorSubmenu, fExtractConstantAction);
		// added+= addAction(refactorSubmenu, fIntroduceParameterAction);
		// added+= addAction(refactorSubmenu, fIntroduceFactoryAction);
		// added+= addAction(refactorSubmenu, fConvertLocalToFieldAction);
		// added+= addAction(refactorSubmenu, fSelfEncapsulateField);
		return added;
	}

	private int addAction(IMenuManager menu, IAction action) {
		if (action != null && action.isEnabled()) {
			menu.add(action);
			return 1;
		}
		return 0;
	}

	private void refactorMenuShown(final IMenuManager refactorSubmenu) {
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
