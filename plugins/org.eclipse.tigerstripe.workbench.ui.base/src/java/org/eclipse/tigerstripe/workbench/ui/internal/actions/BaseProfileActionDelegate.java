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
package org.eclipse.tigerstripe.workbench.ui.internal.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.util.license.LicensedAccess;
import org.eclipse.tigerstripe.workbench.internal.core.util.license.TSWorkbenchProfileRole;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.ui.IWorkbenchPart;

public class BaseProfileActionDelegate {

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

	protected IWorkbenchProfile getProfile() {
		if (selection == null)
			return null;
		IWorkbenchProfile profile = null;
		Object obj = selection.getFirstElement();
		if (obj instanceof IFile) {
			IFile profileFile = (IFile) obj;
			try {
				profile = TigerstripeCore.getWorkbenchProfileSession()
						.getWorkbenchProfileFor(
								profileFile.getLocation().toOSString());
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
		return profile;
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
		if (LicensedAccess.getWorkbenchProfileRole() != TSWorkbenchProfileRole.CREATE_EDIT
				&& LicensedAccess.getWorkbenchProfileRole() != TSWorkbenchProfileRole.DEPLOY_UNDEPLOY) {
			action.setEnabled(false);
		} else {
			action.setEnabled(getProfile() != null);
		}
	}

}
