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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginManager;
import org.eclipse.tigerstripe.workbench.internal.core.util.FileUtils;
import org.eclipse.tigerstripe.workbench.internal.core.util.license.LicensedAccess;
import org.eclipse.tigerstripe.workbench.internal.core.util.license.TSWorkbenchPluggablePluginRole;
import org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs.PluginsControlDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class RegisterPluggableHousingAction extends Action {

	private PluginsControlDialog controlDialog;

	public RegisterPluggableHousingAction(PluginsControlDialog controlDialog) {
		this.controlDialog = controlDialog;
	}

	@Override
	public String getText() {
		return "Deploy";
	}

	private boolean operationSucceeded = false;

	@Override
	public void run() {
		if (LicensedAccess.getWorkbenchPluggablePluginRole() != TSWorkbenchPluggablePluginRole.CREATE_EDIT
				&& LicensedAccess.getWorkbenchPluggablePluginRole() != TSWorkbenchPluggablePluginRole.DEPLOY_UNDEPLOY) {
			String errMessage = "You cannot undeploy a Tigerstripe plugin\n\n"
					+ "Your Tigerstripe license has insufficient privileges for this operation, "
					+ "please contact Tigerstripe if you wish to be able to undeploy "
					+ "plugins";
			MessageDialog.openError(null, "Undeploy Plugin Error", errMessage);
		} else {
			final PluginManager mgr = PluginManager.getManager();

			FileDialog dialog = new FileDialog(controlDialog.getShell());
			dialog.setFilterExtensions(new String[] { "*.zip" });
			dialog.setText("Select zipped Tigerstripe plugin");
			String file = dialog.open();
			if (file != null) {

				final File srcFile = new File(file);
				if (!srcFile.exists() || !srcFile.canRead()) {
					MessageDialog.openError(controlDialog.getShell(),
							"Plugin Deploy Error", "Can't deploy plugin "
									+ file + ": unable to read file.");
					return;
				} else {
					IRunnableWithProgress op = new IRunnableWithProgress() {
						public void run(IProgressMonitor monitor) {
							monitor.beginTask("Deploying plugin", 5);

							String runtimeRoot = TigerstripeRuntime
									.getTigerstripeRuntimeRoot();

							String pluginsRoot = runtimeRoot
									+ File.separator
									+ TigerstripeRuntime.TIGERSTRIPE_PLUGINS_DIR;

							String targetFileStr = pluginsRoot + File.separator
									+ srcFile.getName();
							File targetFile = new File(targetFileStr);

							monitor.subTask("Copying plugin into repository");
							// Copy file and reload plugins
							try {
								FileUtils.copy(srcFile.getAbsolutePath(),
										targetFile.getAbsolutePath(), true);
							} catch (IOException e) {
								EclipsePlugin.log(e);
								operationSucceeded = false;
								return;
							}
							monitor.worked(3);

							if (targetFile.exists()) {
								monitor.subTask("Reloading workbench");
								mgr.load();
								monitor.done();
								operationSucceeded = true;
							} else {
								operationSucceeded = false;
							}
						}
					};

					IWorkbench wb = PlatformUI.getWorkbench();
					IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
					Shell shell = win != null ? win.getShell() : null;

					try {
						ProgressMonitorDialog pDialog = new ProgressMonitorDialog(
								shell);
						pDialog.run(true, false, op);
					} catch (InterruptedException ee) {
						EclipsePlugin.log(ee);
					} catch (InvocationTargetException ee) {
						EclipsePlugin.log(ee);
					}

					if (!operationSucceeded)
						MessageDialog.openError(controlDialog.getShell(),
								"Plugin Deploy Error",
								"Couldn't deploy plugin "
										+ srcFile.getAbsolutePath() + ".");

				}
			}
			controlDialog.populateTable();
		}
	}
}
