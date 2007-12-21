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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.plugins.pluggable.IPluggablePluginProject;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.header.PluggablePluginProjectPackager;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class PacakgePluginActionDelegate extends BasePluginActionDelegate
		implements IObjectActionDelegate {

	public void run(IAction action) {
		FileDialog dialog = new FileDialog(getShell());
		dialog.setFilterExtensions(new String[] { "*.zip" });
		final String path = dialog.open();
		if (path != null) {
			final IPluggablePluginProject projectHandle = getPPProject();
			IRunnableWithProgress op = new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) {
					try {
						monitor.beginTask("Packaging plugin", 10);
						String lPath = path;
						if (!path.endsWith(".zip")) {
							lPath += ".zip";
						}

						PluggablePluginProjectPackager packager = new PluggablePluginProjectPackager(
								projectHandle.getPPProject());
						packager.packageUpProject(monitor, lPath);

						monitor.done();
						operationSucceeded = true;
					} catch (TigerstripeException ee) {
						EclipsePlugin.log(ee);
						operationSucceeded = false;
					}
				}
			};

			IWorkbench wb = PlatformUI.getWorkbench();
			IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
			Shell shell = win != null ? win.getShell() : null;

			try {
				ProgressMonitorDialog pDialog = new ProgressMonitorDialog(shell);
				pDialog.run(true, false, op);
			} catch (InterruptedException ee) {
				EclipsePlugin.log(ee);
			} catch (InvocationTargetException ee) {
				EclipsePlugin.log(ee);
			}

			try {
				if (operationSucceeded) {
					MessageDialog.openInformation(getShell(), projectHandle
							.getProjectLabel()
							+ " Plugin", "Plugin '"
							+ projectHandle.getProjectDetails().getName() + "("
							+ projectHandle.getProjectDetails().getVersion()
							+ ") was successfully packaged up as \n '" + path
							+ "'.");
				} else {
					MessageDialog
							.openError(
									getShell(),
									projectHandle.getProjectLabel() + " Plugin",
									"Plugin '"
											+ projectHandle.getProjectDetails()
													.getName()
											+ "("
											+ projectHandle.getProjectDetails()
													.getVersion()
											+ ") could not be packaged up. See Error log for more details.\n");
				}
			} catch (TigerstripeException ee) {
				EclipsePlugin.log(ee);
			}
		}
	}
}
