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
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.actions.ActionFactory;
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
public class AuditActionGroup extends BaseActionProvider {

	private BuildAction fBuildAction;
	private OpenGenerateInterfaceWizardAction fGenerateAction;
	private RefreshAction fRefreshAction;
	private RemoveFromBuildAction removeFromBuildAction;

	@Override
	protected void doInit(IWorkbenchPartSite site) {
		fBuildAction = new AuditAction(site,
				IncrementalProjectBuilder.CLEAN_BUILD);
		fBuildAction.setText("Clean Audit Now");
		fBuildAction
				.setActionDefinitionId("org.eclipse.ui.project.buildProject"); //$NON-NLS-1$

		removeFromBuildAction = new RemoveFromBuildAction();
		removeFromBuildAction.setExcludeText();

		fGenerateAction = new OpenGenerateInterfaceWizardAction();
		fGenerateAction.setText("Generate...");

		fRefreshAction = new RefreshAction(site);
		fRefreshAction.setActionDefinitionId("org.eclipse.ui.file.refresh"); //$NON-NLS-1$

		ISelectionProvider provider = site.getSelectionProvider();

		provider.addSelectionChangedListener(fBuildAction);
		provider.addSelectionChangedListener(removeFromBuildAction);
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
		appendToGroup(menu, removeFromBuildAction);
		appendToGroup(menu, fGenerateAction);
		appendToGroup(menu, fRefreshAction);
		super.fillContextMenu(menu);
	}

	/*
	 * (non-Javadoc) Method declared in ActionGroup
	 */
	@Override
	public void dispose() {
		ISelectionProvider provider = getSelectionProvider();
		provider.removeSelectionChangedListener(fBuildAction);
		provider.removeSelectionChangedListener(fRefreshAction);
		provider.removeSelectionChangedListener(removeFromBuildAction);
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
