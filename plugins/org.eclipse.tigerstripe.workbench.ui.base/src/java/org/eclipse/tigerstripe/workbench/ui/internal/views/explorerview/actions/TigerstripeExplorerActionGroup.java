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

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.ui.IContextMenuConstants;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jdt.ui.actions.CustomFiltersActionGroup;
import org.eclipse.jdt.ui.actions.ImportActionGroup;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.util.OpenStrategy;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.TSProjectActionGroup;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TigerstripeExplorerPart;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TigerstripeFrameSource;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.actions.OpenInNewWindowAction;
import org.eclipse.ui.views.framelist.BackAction;
import org.eclipse.ui.views.framelist.ForwardAction;
import org.eclipse.ui.views.framelist.FrameAction;
import org.eclipse.ui.views.framelist.FrameList;
import org.eclipse.ui.views.framelist.GoIntoAction;
import org.eclipse.ui.views.framelist.UpAction;

public class TigerstripeExplorerActionGroup extends CompositeActionGroup {

	private TigerstripeExplorerPart fPart;

	private FrameList fFrameList;

	private GoIntoAction fZoomInAction;

	private BackAction fBackAction;

	private ForwardAction fForwardAction;

	private UpAction fUpAction;

	private TSRefactorActionGroup fRefactorActionGroup;

//	private TSPublishActionGroup fPublishActionGroup;

	private TSNavigateActionGroup fNavigateActionGroup;

	private CustomFiltersActionGroup fCustomFiltersActionGroup;

	public TigerstripeExplorerActionGroup(TigerstripeExplorerPart part) {
		super();
		fPart = part;
		setGroups(new ActionGroup[] {
				new PackageActionGroup(fPart.getSite()),
				new NewWizardsActionGroup(fPart.getSite()),
				fNavigateActionGroup = new TSNavigateActionGroup(fPart),
				new TSCCPActionGroup(fPart),
				// new GenerateBuildPathActionGroup(fPart),
				// new GenerateActionGroup(fPart),
				fRefactorActionGroup = new TSRefactorActionGroup(fPart),
				new ImportActionGroup(fPart), 
				new AuditActionGroup(fPart),
				new TSProjectActionGroup(fPart),
				new LayoutActionGroup(fPart),

		});

		// fViewActionGroup.fillFilters(viewer);

		TigerstripeFrameSource frameSource = new TigerstripeFrameSource(fPart);
		fFrameList = new FrameList(frameSource);
		frameSource.connectTo(fFrameList);

		fZoomInAction = new GoIntoAction(fFrameList);
		fBackAction = new BackAction(fFrameList);
		fForwardAction = new ForwardAction(fFrameList);
		fUpAction = new UpAction(fFrameList);

	}

	@Override
	public void dispose() {
		super.dispose();
	}

	// ---- Persistent state
	// -----------------------------------------------------------------------

	/* package */void restoreFilterAndSorterState(IMemento memento) {
		fCustomFiltersActionGroup.restoreState(memento);
	}

	/* package */void saveFilterAndSorterState(IMemento memento) {
		fCustomFiltersActionGroup.saveState(memento);
	}

	// ---- Action Bars
	// ----------------------------------------------------------------------------

	@Override
	public void fillActionBars(IActionBars actionBars) {
		super.fillActionBars(actionBars);
		setGlobalActionHandlers(actionBars);
		fillToolBar(actionBars.getToolBarManager());
		fillViewMenu(actionBars.getMenuManager());
	}

	/* package */void updateActionBars(IActionBars actionBars) {
		actionBars.getToolBarManager().removeAll();
		actionBars.getMenuManager().removeAll();
		fillActionBars(actionBars);
		actionBars.updateActionBars();
		fZoomInAction.setEnabled(true);
	}

	private void setGlobalActionHandlers(IActionBars actionBars) {
		// Navigate Go Into and Go To actions.
		actionBars.setGlobalActionHandler(IWorkbenchActionConstants.GO_INTO,
				fZoomInAction);
		actionBars.setGlobalActionHandler(ActionFactory.BACK.getId(),
				fBackAction);
		actionBars.setGlobalActionHandler(ActionFactory.FORWARD.getId(),
				fForwardAction);
		actionBars.setGlobalActionHandler(IWorkbenchActionConstants.UP,
				fUpAction);
		// actionBars.setGlobalActionHandler(
		// IWorkbenchActionConstants.GO_TO_RESOURCE, fGotoResourceAction);
		// actionBars.setGlobalActionHandler(JdtActionConstants.GOTO_TYPE,
		// fGotoTypeAction);
		// actionBars.setGlobalActionHandler(JdtActionConstants.GOTO_PACKAGE,
		// fGotoPackageAction);

		fRefactorActionGroup.retargetFileMenuActions(actionBars);
	}

	/* package */void fillToolBar(IToolBarManager toolBar) {
		toolBar.add(fBackAction);
		toolBar.add(fForwardAction);
		toolBar.add(fUpAction);

		toolBar.add(new Separator());
		// toolBar.add(fCollapseAllAction);
		// toolBar.add(fToggleLinkingAction);

	}

	/* package */void fillViewMenu(IMenuManager menu) {
		// menu.add(fToggleLinkingAction);

		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		menu
				.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS
						+ "-end"));//$NON-NLS-1$		
	}

	// ---- Context menu
	// -------------------------------------------------------------------------

	@Override
	public void fillContextMenu(IMenuManager menu) {
		IStructuredSelection selection = (IStructuredSelection) getContext()
				.getSelection();
		int size = selection.size();
		Object element = selection.getFirstElement();

		addGotoMenu(menu, element, size);

		addOpenNewWindowAction(menu, element);

		super.fillContextMenu(menu);
	}

	
	
	
	private void addGotoMenu(IMenuManager menu, Object element, int size) {
		boolean enabled = size == 1
				&& fPart.getTreeViewer().isExpandable(element)
				&& (isGoIntoTarget(element) || element instanceof IContainer);
		fZoomInAction.setEnabled(enabled);
		if (enabled)
			menu.appendToGroup(IContextMenuConstants.GROUP_GOTO, fZoomInAction);
	}

	private boolean isGoIntoTarget(Object element) {
		if (element == null)
			return false;
		if (element instanceof IJavaElement) {
			int type = ((IJavaElement) element).getElementType();
			return type == IJavaElement.JAVA_PROJECT
					|| type == IJavaElement.PACKAGE_FRAGMENT_ROOT
					|| type == IJavaElement.PACKAGE_FRAGMENT;
		}
		if (element instanceof IWorkingSet)
			return true;
		return false;
	}

	private void addOpenNewWindowAction(IMenuManager menu, Object element) {
		if (element instanceof IJavaElement) {
			element = ((IJavaElement) element).getResource();

		}
		// fix for 64890 Package explorer out of sync when open/closing projects
		// [package explorer] 64890
		if (element instanceof IProject && !((IProject) element).isOpen())
			return;

		if (!(element instanceof IContainer))
			return;
		menu.appendToGroup(IContextMenuConstants.GROUP_OPEN,
				new OpenInNewWindowAction(fPart.getSite().getWorkbenchWindow(),
						(IContainer) element));
	}

	// ---- Key board and mouse handling
	// ------------------------------------------------------------

	public void handleDoubleClick(DoubleClickEvent event) {
		TreeViewer viewer = fPart.getTreeViewer();
		Object element = ((IStructuredSelection) event.getSelection())
				.getFirstElement();
		if (viewer.isExpandable(element)) {
			if (doubleClickGoesInto()) {
				// don't zoom into compilation units and class files
				if (element instanceof ICompilationUnit
						|| element instanceof IClassFile)
					return;
				if (element instanceof IOpenable
						|| element instanceof IContainer) {
					fZoomInAction.run();
				}
			} else {
				IAction openAction = fNavigateActionGroup.getOpenAction();
				if (openAction != null
						&& openAction.isEnabled()
						&& OpenStrategy.getOpenMethod() == OpenStrategy.DOUBLE_CLICK)
					return;
				viewer.setExpandedState(element, !viewer
						.getExpandedState(element));
			}
		}
	}

	public void handleOpen(OpenEvent event) {
		IAction openAction = fNavigateActionGroup.getOpenAction();
		if (openAction != null && openAction.isEnabled()) {
			openAction.run();
			return;
		}
	}

	public void handleKeyEvent(KeyEvent event) {
		if (event.stateMask != 0)
			return;

		if (event.keyCode == SWT.BS) {
			if (fUpAction != null && fUpAction.isEnabled()) {
				fUpAction.run();
				event.doit = false;
			}
		}
	}

	private boolean doubleClickGoesInto() {
		return PreferenceConstants.DOUBLE_CLICK_GOES_INTO
				.equals(PreferenceConstants.getPreferenceStore().getString(
						PreferenceConstants.DOUBLE_CLICK));
	}

	public FrameAction getUpAction() {
		return fUpAction;
	}

	public FrameAction getBackAction() {
		return fBackAction;
	}

	public FrameAction getForwardAction() {
		return fForwardAction;
	}

	public CustomFiltersActionGroup getCustomFilterActionGroup() {
		return fCustomFiltersActionGroup;
	}
}
