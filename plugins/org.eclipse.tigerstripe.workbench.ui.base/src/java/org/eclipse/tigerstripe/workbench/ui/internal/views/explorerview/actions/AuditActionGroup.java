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

import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.jdt.ui.IContextMenuConstants;
import org.eclipse.jdt.ui.actions.RefreshAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.OpenGenerateInterfaceWizardAction;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.actions.BuildAction;
import org.eclipse.ui.ide.IDEActionFactory;

/**
 * Contributes all build related actions to the context menu and installs
 * handlers for the corresponding global menu actions.
 * 
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 * 
 * @since 2.0
 */
public class AuditActionGroup extends ActionGroup {

	private IWorkbenchSite fSite;

	private BuildAction fBuildAction;
	private OpenGenerateInterfaceWizardAction fGenerateAction;
	private RefreshAction fRefreshAction;

	/**
	 * Creates a new <code>BuildActionGroup</code>. The group requires that the
	 * selection provided by the view part's selection provider is of type
	 * <code>org.eclipse.jface.viewers.IStructuredSelection</code>.
	 * 
	 * @param part
	 *            the view part that owns this action group
	 */
	public AuditActionGroup(IViewPart part) {
		fSite = part.getSite();
		ISelectionProvider provider = fSite.getSelectionProvider();

		fBuildAction = new AuditAction(fSite,
				IncrementalProjectBuilder.CLEAN_BUILD);
		fBuildAction.setText("Clean Audit Now");
		fBuildAction
				.setActionDefinitionId("org.eclipse.ui.project.buildProject"); //$NON-NLS-1$

		fGenerateAction = new OpenGenerateInterfaceWizardAction();
		fGenerateAction.setText("Generate...");

		fRefreshAction = new RefreshAction(fSite);
		fRefreshAction.setActionDefinitionId("org.eclipse.ui.file.refresh"); //$NON-NLS-1$

		provider.addSelectionChangedListener(fBuildAction);
		provider.addSelectionChangedListener(fRefreshAction);
		provider.addSelectionChangedListener(fGenerateAction);
	}

	/**
	 * Returns the refresh action managed by this group.
	 * 
	 * @return the refresh action. If this group doesn't manage a refresh action
	 *         <code>null</code> is returned
	 */
	public IAction getRefreshAction() {
		return fRefreshAction;
	}

	/*
	 * (non-Javadoc) Method declared in ActionGroup
	 */
	@Override
	public void fillActionBars(IActionBars actionBar) {
		super.fillActionBars(actionBar);
		setGlobalActionHandlers(actionBar);
	}

	/*
	 * (non-Javadoc) Method declared in ActionGroup
	 */
	@Override
	public void fillContextMenu(IMenuManager menu) {
		appendToGroup(menu, fBuildAction);
		appendToGroup(menu, fGenerateAction);
		appendToGroup(menu, fRefreshAction);
		super.fillContextMenu(menu);
	}

	/*
	 * (non-Javadoc) Method declared in ActionGroup
	 */
	@Override
	public void dispose() {
		ISelectionProvider provider = fSite.getSelectionProvider();
		provider.removeSelectionChangedListener(fBuildAction);
		provider.removeSelectionChangedListener(fRefreshAction);
		super.dispose();
	}

	private void setGlobalActionHandlers(IActionBars actionBar) {
		actionBar.setGlobalActionHandler(
				IDEActionFactory.BUILD_PROJECT.getId(), fBuildAction);
		actionBar.setGlobalActionHandler(ActionFactory.REFRESH.getId(),
				fRefreshAction);
	}

	private void appendToGroup(IMenuManager menu, IAction action) {
		if (action.isEnabled())
			menu.appendToGroup(IContextMenuConstants.GROUP_BUILD, action);
	}
}
