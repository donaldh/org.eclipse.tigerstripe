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
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.ui.IContextMenuConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.CoreArtifactSettingsProperty;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.actions.ActionGroup;

/**
 * Action group for all new wizards within the Tigerstripe Explorer.
 * 
 * @author Richard Craddock
 * @since 1.2
 */
public class PackageActionGroup extends ActionGroup {

	private IWorkbenchSite site ;
	
	/**
	 * Creates a new <code>NewWizardsActionGroup</code>. The group requires
	 * that the selection provided by the part's selection provider is of type
	 * <code>
	 * org.eclipse.jface.viewers.IStructuredSelection</code>.
	 * 
	 * @param site
	 *            the view part that owns this action group
	 */
	public PackageActionGroup(IWorkbenchSite site) {
		this.site = site;
	}

	/*
	 * (non-Javadoc) Method declared in ActionGroup
	 */
	@Override
	public void fillContextMenu(IMenuManager menu) {
		super.fillContextMenu(menu);
		IStructuredSelection selection = (IStructuredSelection) getContext()
		.getSelection();
		Object element = selection.getFirstElement();
		if (selection instanceof IStructuredSelection) {
			
			// Check if this is a package, then if packages are enabled..
			if (element instanceof IPackageFragment){
				IWorkbenchProfile profile = TigerstripeCore
					.getWorkbenchProfileSession()
					.getActiveProfile();
				CoreArtifactSettingsProperty prop = (CoreArtifactSettingsProperty) profile
					.getProperty(IWorkbenchPropertyLabels.CORE_ARTIFACTS_SETTINGS);
				if (prop.getDetailsForType(
						IPackageArtifact.class.getName())
						.isEnabled()) {

					addOpenEditorAction(menu, element);
				}
			}
		}
	}

	private void addOpenEditorAction(IMenuManager menu, Object element) {
		if (element instanceof IJavaElement) {
			element = ((IJavaElement) element).getResource();

		}
		// fix for 64890 Package explorer out of sync when open/closing projects
		// [package explorer] 64890
		if (element instanceof IProject && !((IProject) element).isOpen())
			return;

		if (!(element instanceof IContainer))
			return;
		Action newAction = new OpenPackageArtifactEditorAction(site);
		newAction.setText("Open Package Artifact in Editor");
		menu.prependToGroup(IContextMenuConstants.GROUP_NEW,
				newAction);
	}
}
