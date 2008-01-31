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
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.plugins.pluggable.IPluggablePluginProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.utils.PluginDeploymentHelper;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class DeployPluginActionDelegate extends BasePluginActionDelegate
		implements IObjectActionDelegate {

	public void run(IAction action) {

		if (selection == null || targetPart == null)
			return;
		IRunnableWithProgress op = null;
		try {
			IPluggablePluginProject projectHandle = getPPProject();
			if (MessageDialog
					.openConfirm(
							targetPart.getSite().getShell(),
							"Deploy new plugin",
							"You are about to deploy this plugin ('"
									+ projectHandle.getProjectDetails()
											.getName()
									+ "'). All open editors will be closed.\nDo you want to continue?.  ")) {

				final PluginDeploymentHelper helper = new PluginDeploymentHelper(
						projectHandle);
				op = new IRunnableWithProgress() {
					public void run(IProgressMonitor monitor) {
						try {
							deploymentPath = helper.deploy(monitor);
							operationSucceeded = deploymentPath != null;
						} catch (TigerstripeException e) {
							operationSucceeded = false;
							EclipsePlugin.log(e);
						}
					}
				};

				IWorkbench wb = PlatformUI.getWorkbench();
				IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
				Shell shell = win != null ? win.getShell() : null;

				try {
					ProgressMonitorDialog dialog = new ProgressMonitorDialog(
							shell);
					dialog.run(true, false, op);
				} catch (InterruptedException ee) {
					EclipsePlugin.log(ee);
				} catch (InvocationTargetException ee) {
					EclipsePlugin.log(ee.getTargetException());
				}

				if (operationSucceeded) {
					MessageDialog.openInformation(targetPart.getSite()
							.getShell(), projectHandle.getProjectLabel()
							+ " Plugin", "Plugin '"
							+ projectHandle.getProjectDetails().getName() + "("
							+ projectHandle.getProjectDetails().getVersion()
							+ ") was successfully deployed.\n ("
							+ deploymentPath + ")");
				} else {
					MessageDialog
							.openError(
									targetPart.getSite().getShell(),
									projectHandle.getProjectLabel() + " Plugin",
									"Plugin '"
											+ projectHandle.getProjectDetails()
													.getName()
											+ "("
											+ projectHandle.getProjectDetails()
													.getVersion()
											+ ") could not be deployed. See Error log for more details");
				}
			}
		} catch (TigerstripeException ee) {
			EclipsePlugin.log(ee);
		}
	}

}
