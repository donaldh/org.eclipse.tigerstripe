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
package org.eclipse.tigerstripe.workbench.ui.eclipse.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.api.plugins.pluggable.IPluggablePluginProject;
import org.eclipse.tigerstripe.api.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.core.util.license.LicensedAccess;
import org.eclipse.tigerstripe.core.util.license.TSWorkbenchPluggablePluginRole;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.ui.IWorkbenchPart;

public class BasePluginActionDelegate {

	protected IWorkbenchPart targetPart;

	protected IStructuredSelection selection;

	protected boolean operationSucceeded = false;

	protected String deploymentPath = null;

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		this.targetPart = targetPart;
	}

	protected Shell getShell() {
		if (targetPart == null)
			return null;
		return targetPart.getSite().getShell();
	}

	protected IPluggablePluginProject getPPProject() {
		if (selection == null)
			return null;
		IAbstractTigerstripeProject tsProject = null;
		Object obj = selection.getFirstElement();
		if (obj instanceof IResource) {
			IResource res = (IResource) obj;
			IProject proj = res.getProject();
			tsProject = EclipsePlugin.getITigerstripeProjectFor(proj);
			if (tsProject instanceof IPluggablePluginProject)
				return (IPluggablePluginProject) tsProject;
		}
		return null;
	}

	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			this.selection = (IStructuredSelection) selection;
			checkEnabled(action, (IStructuredSelection) selection);
		} else {
			action.setEnabled(false);
			selection = null;
		}
	}

	protected void checkEnabled(IAction action, IStructuredSelection selection) {
		if (LicensedAccess.getWorkbenchPluggablePluginRole() != TSWorkbenchPluggablePluginRole.CREATE_EDIT
				&& LicensedAccess.getWorkbenchPluggablePluginRole() != TSWorkbenchPluggablePluginRole.DEPLOY_UNDEPLOY) {
			action.setEnabled(false);
		} else {
			IPluggablePluginProject ppProject = getPPProject();
			action.setEnabled(ppProject != null);
		}
	}

}
