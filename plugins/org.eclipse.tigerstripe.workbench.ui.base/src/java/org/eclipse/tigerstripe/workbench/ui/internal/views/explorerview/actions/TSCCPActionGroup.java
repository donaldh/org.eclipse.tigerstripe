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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.jdt.internal.ui.refactoring.reorg.CutAction;
import org.eclipse.jdt.internal.ui.refactoring.reorg.PasteAction;
import org.eclipse.jdt.ui.IContextMenuConstants;
import org.eclipse.jdt.ui.actions.SelectionDispatchAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.part.Page;
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
public class TSCCPActionGroup extends ActionGroup {

	private IWorkbenchSite fSite;
	private Clipboard fClipboard;

	private SelectionDispatchAction[] fActions;

	private SelectionDispatchAction fDeleteAction;
	private SelectionDispatchAction fCopyAction;
	private SelectionDispatchAction fPasteAction;
	private SelectionDispatchAction fCutAction;

	/**
	 * Creates a new <code>CCPActionGroup</code>. The group requires that the
	 * selection provided by the view part's selection provider is of type
	 * <code>org.eclipse.jface.viewers.IStructuredSelection</code>.
	 * 
	 * @param part
	 *            the view part that owns this action group
	 */
	public TSCCPActionGroup(IViewPart part) {
		this(part.getSite());
	}

	/**
	 * Creates a new <code>CCPActionGroup</code>. The group requires that the
	 * selection provided by the page's selection provider is of type
	 * <code>org.eclipse.jface.viewers.IStructuredSelection</code>.
	 * 
	 * @param page
	 *            the page that owns this action group
	 */
	public TSCCPActionGroup(Page page) {
		this(page.getSite());
	}

	private TSCCPActionGroup(IWorkbenchSite site) {
		fSite = site;
		fClipboard = new Clipboard(site.getShell().getDisplay());

		fPasteAction = new PasteAction(fSite, fClipboard);
		fPasteAction.setActionDefinitionId(IWorkbenchActionDefinitionIds.PASTE);

		fCopyAction = new TSCopyToClipboadAction(fSite, fClipboard,
				fPasteAction);
		fCopyAction.setActionDefinitionId(IWorkbenchActionDefinitionIds.COPY);

		fCutAction = getCutActionInstance();
		fCutAction.setActionDefinitionId(IWorkbenchActionDefinitionIds.CUT);

		fDeleteAction = new TSDeleteAction(fSite);
		fDeleteAction
				.setActionDefinitionId(IWorkbenchActionDefinitionIds.DELETE);

		fActions = new SelectionDispatchAction[] { fCutAction, fCopyAction,
				fPasteAction, fDeleteAction };
		registerActionsAsSelectionChangeListeners();
	}

	private SelectionDispatchAction getCutActionInstance() {
		try {
			Class clazz = CutAction.class;
			Constructor[] constructorArray = clazz.getDeclaredConstructors();
			for (Constructor constructor : constructorArray) {
				Class[] parameterTypes = constructor.getParameterTypes();
				if (parameterTypes.length == 3
						&& parameterTypes[0] == IWorkbenchSite.class
						&& parameterTypes[1] == Clipboard.class
						&& parameterTypes[2] == SelectionDispatchAction.class) {
					Object[] argList = new Object[3];
					argList[0] = fSite;
					argList[1] = fClipboard;
					argList[2] = fPasteAction;
					return ((SelectionDispatchAction) constructor
							.newInstance(argList));
				} else if (parameterTypes.length == 2
						&& parameterTypes[0] == IWorkbenchSite.class
						&& parameterTypes[1] == Clipboard.class) {
					Object[] argList = new Object[2];
					argList[0] = fSite;
					argList[1] = fClipboard;
					return ((SelectionDispatchAction) constructor
							.newInstance(argList));
				}
			}
		} catch (InstantiationException e) {
			EclipsePlugin.log(e);
		} catch (InvocationTargetException e) {
			EclipsePlugin.log(e);
		} catch (IllegalAccessException e) {
			EclipsePlugin.log(e);
		}
		return null;
	}

	private void registerActionsAsSelectionChangeListeners() {
		ISelectionProvider provider = fSite.getSelectionProvider();
		for (int i = 0; i < fActions.length; i++) {
			provider.addSelectionChangedListener(fActions[i]);
		}
	}

	private void deregisterActionsAsSelectionChangeListeners() {
		ISelectionProvider provider = fSite.getSelectionProvider();
		for (int i = 0; i < fActions.length; i++) {
			provider.removeSelectionChangedListener(fActions[i]);
		}
	}

	/**
	 * Returns the delete action managed by this action group.
	 * 
	 * @return the delete action. Returns <code>null</code> if the group
	 *         doesn't provide any delete action
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
