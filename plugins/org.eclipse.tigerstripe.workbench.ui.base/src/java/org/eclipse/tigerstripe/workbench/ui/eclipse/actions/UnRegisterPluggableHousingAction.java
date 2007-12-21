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
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.core.plugin.PluginManager;
import org.eclipse.tigerstripe.core.plugin.pluggable.PluggableHousing;
import org.eclipse.tigerstripe.core.plugin.pluggable.PluggablePlugin;
import org.eclipse.tigerstripe.core.util.license.LicensedAccess;
import org.eclipse.tigerstripe.core.util.license.TSWorkbenchPluggablePluginRole;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs.PluginsControlDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class UnRegisterPluggableHousingAction extends Action {

	private PluginsControlDialog dialog;

	private PluggableHousing targetHousing;

	public UnRegisterPluggableHousingAction(int index,
			PluginsControlDialog dialog) {
		this.dialog = dialog;
		PluginManager mgr = PluginManager.getManager();
		List<PluggableHousing> housings = mgr.getRegisteredPluggableHousings();

		targetHousing = housings.get(index);
	}

	public UnRegisterPluggableHousingAction(PluggableHousing targetHousing) {
		this.targetHousing = targetHousing;
	}

	@Override
	public String getText() {
		return "Un-deploy";
	}

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
			boolean confirm = MessageDialog.openConfirm(null,
					"Un-Deploy Tigerstripe Plugin?",
					"Do you really want to undeploy the '"
							+ targetHousing.getLabel() + "' plugin?");

			if (confirm) {
				IRunnableWithProgress op = new IRunnableWithProgress() {
					public void run(IProgressMonitor monitor) {
						try {
							monitor.beginTask("Un-deploying plugin:"
									+ targetHousing.getLabel(), 5);

							monitor.subTask("Closing all editors");
							EclipsePlugin.closeAllEditors(true, true, false,
									false, false);
							monitor.worked(2);

							monitor.subTask("Removing plugin");
							mgr.unRegisterHousing(targetHousing);
							monitor.worked(1);
							PluggablePlugin pPlugin = targetHousing.getBody();
							pPlugin.dispose();

							monitor.done();
						} catch (TigerstripeException e) {
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
					EclipsePlugin.log(ee);
				}

				dialog.populateTable();
			}
		}
	}

}
