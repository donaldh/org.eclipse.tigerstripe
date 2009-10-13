/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    S Jerman (Cisco Systems, Inc.) - add extra action
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.actions;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfileSession;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeGeneratorProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.EditorHelper;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.GeneratorDeploymentUIHelper;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class DeployZippedPluginActionDelegate implements IObjectActionDelegate {

	protected IWorkbenchPart targetPart;

	IFile zipFile = null;

	IPath deploymentPath = null;
	boolean operationSucceeded = false;

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		this.targetPart = targetPart;

	}

	protected Shell getShell() {
		if (targetPart == null)
			return null;
		return targetPart.getSite().getShell();
	}

	public void run(IAction action) {

		if (zipFile == null || targetPart == null)
			return;
		IRunnableWithProgress op = null;
		if (MessageDialog
				.openConfirm(
						targetPart.getSite().getShell(),
						"Deploy new plugin",
						"You are about to deploy this plugin ('"
								+ zipFile.getName()
								+ "'). All open editors will be closed.\nDo you want to continue?.  ")) {

			final GeneratorDeploymentUIHelper helper = new GeneratorDeploymentUIHelper();
			op = new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) {

					try {
						deploymentPath = helper.deployZipFromWorkspace(zipFile,
								monitor);
						// deploymentPath = helper.deploy(project,
						// monitor)(projectHandle,
						// monitor).toOSString();
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
				EclipsePlugin.log(ee.getTargetException());
			}

			if (operationSucceeded) {
				MessageDialog.openInformation(targetPart.getSite().getShell(),
						zipFile.getName() + " Plugin", "Plugin '"
								+ zipFile.getName()
								+ "' was successfully deployed.\n ("
								+ deploymentPath + ")");
			} else {
				MessageDialog
						.openError(
								targetPart.getSite().getShell(),
								zipFile.getName() + " Plugin",
								"Plugin '"
										+ zipFile.getName()
										+ "') could not be deployed. See Error log for more details");
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action
	 * .IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection sel = (IStructuredSelection) selection;
			if (sel.getFirstElement() instanceof IFile) {
				IFile tmp = (IFile) sel.getFirstElement();
				if (tmp.getFileExtension().equals("zip")) {
					zipFile = tmp;
				}
			}
		} else {
			action.setEnabled(false);
			selection = null;
		}
	}

}
