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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.API;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfileSession;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class DeployProfileActionDelegate extends BaseProfileActionDelegate
		implements IObjectActionDelegate {

	private boolean rollbackCreated = false;

	public void run(IAction action) {
		final IWorkbenchProfile handle = getProfile();

		if (MessageDialog
				.openConfirm(
						getShell(),
						"Save as Active Profile",
						"You are about to set this profile ('"
								+ handle.getName()
								+ "') as the active profile. All open editors will be closed.\n\nDo you want to continue?\n\n(You will be able to rollback to the current active profile).  ")) {

			IRunnableWithProgress op = new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) {
					try {
						monitor.beginTask("Deploying new Active Profile", 10);

						monitor.subTask("Closing all editors");
						EclipsePlugin.closeAllEditors(true, true, false, false,
								true);
						monitor.worked(2);

						IWorkbenchProfileSession session = API
								.getIWorkbenchProfileSession();
						monitor.subTask("Creating Profile");

						rollbackCreated = session.saveAsActiveProfile(handle);
						monitor.worked(2);

						monitor.subTask("Reloading workbench");
						session.reloadActiveProfile();
						monitor.done();
						operationSucceeded = true;
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
						operationSucceeded = false;
					}
				}
			};

			IWorkbench wb = PlatformUI.getWorkbench();
			IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
			Shell shell = win != null ? win.getShell() : null;

			try {
				ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
				dialog.run(true, false, op);
			} catch (InterruptedException e) {
				EclipsePlugin.log(e);
			} catch (InvocationTargetException e) {
				EclipsePlugin.log(e);
			}

			String rollbackStr = "";
			if (rollbackCreated) {
				rollbackStr = "\n\n(A rollback file was successfully created)";
			}

			if (operationSucceeded) {
				MessageDialog
						.openInformation(
								getShell(),
								"Success",
								"Profile '"
										+ handle.getName()
										+ "' is now the active profile for this instance of Tigerstripe Workbench.\n\nWorkbench is now ready to be used with this new active profile."
										+ rollbackStr);

			} else {
				MessageDialog
						.openError(
								getShell(),
								"Error while setting active Profile",
								"An error occured while trying to set the active profile:\n"
										+ "Please check the Error Log for more details");
			}
		}

	}
}
