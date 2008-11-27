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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeGeneratorProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM1GeneratorProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.GeneratorDeploymentUIHelper;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class UnDeployPluginActionDelegate extends BasePluginActionDelegate
		implements IObjectActionDelegate {

	public void run(IAction action) {
		if (selection == null || targetPart == null)
			return;
		IRunnableWithProgress op = null;
		try {
			final ITigerstripeGeneratorProject projectHandle = getPPProject();
			final GeneratorDeploymentUIHelper helper = new GeneratorDeploymentUIHelper();
			op = new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) {
					try {
						deploymentPath = helper
								.unDeploy(projectHandle, monitor).toOSString();
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
				ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
				dialog.run(true, false, op);
			} catch (InterruptedException ee) {
				EclipsePlugin.log(ee);
			} catch (InvocationTargetException ee) {
				EclipsePlugin.log(ee);
			}

			if (operationSucceeded)
				MessageDialog.openInformation(getShell(), projectHandle
						.getName()
						+ " Plugin", "Plugin '" + projectHandle.getName() + "("
						+ projectHandle.getProjectDetails().getVersion()
						+ ") was successfully un-deployed.\n ("
						+ deploymentPath + ")");
			else {
				MessageDialog.openError(getShell(), projectHandle.getName()
						+ " Plugin",
						"An Error occured while trying to un-deploy plugin '"
								+ projectHandle.getName()
								+ "("
								+ projectHandle.getProjectDetails()
										.getVersion() + ")\n from ("
								+ deploymentPath
								+ ").\nSee Error log for more details.");
			}

		} catch (TigerstripeException ee) {
			EclipsePlugin.log(ee);
		}
	}

	@Override
	protected void checkEnabled(IAction action, IStructuredSelection selection) {
		super.checkEnabled(action, selection);
		if (action.isEnabled()) {
			final ITigerstripeGeneratorProject ppProject = getPPProject();
			if (ppProject != null) {
				try {
					GeneratorDeploymentUIHelper helper = new GeneratorDeploymentUIHelper();
					action.setEnabled(helper.canUndeploy(ppProject));
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}
		}
	}
}
