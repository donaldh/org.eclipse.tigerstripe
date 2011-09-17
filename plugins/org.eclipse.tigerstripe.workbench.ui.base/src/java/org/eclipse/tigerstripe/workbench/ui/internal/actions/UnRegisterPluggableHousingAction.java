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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginManager;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggableHousing;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.util.license.LicensedAccess;
import org.eclipse.tigerstripe.workbench.internal.core.util.license.TSWorkbenchPluggablePluginRole;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.PluginsControlDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.EditorHelper;
import org.eclipse.ui.PlatformUI;

public class UnRegisterPluggableHousingAction extends Action {
	private static final String ERR_NOT_PERMITTED = "You cannot undeploy a Tigerstripe generator\n\n"
			+ "Your Tigerstripe license has insufficient privileges for this operation, "
			+ "please contact Tigerstripe if you wish to be able to undeploy "
			+ "generators";

	private static final int WORK_CLOSE_EDITORS = 2;
	private static final int WORK_RELOAD = 3;

	private PluginsControlDialog dialog;

	private List<PluggableHousing> targetHousing;
	
	private boolean confirm = true;

	public UnRegisterPluggableHousingAction(List<PluggableHousing> housing,
			PluginsControlDialog dialog) {
		this.dialog = dialog;

		targetHousing = housing;

		// Disable this action is the plugin cannot be deleted!
		setEnabled(true);
		for (final PluggableHousing h : targetHousing) {
			if (!h.isDeployed()) {
				setEnabled(false);
			}
		}
	}

	public UnRegisterPluggableHousingAction(PluggableHousing housing,
			PluginsControlDialog dialog) {
		this(Collections.singletonList(housing), dialog);
	}

	@Override
	public String getText() {
		return "Un-deploy";
	}

	@Override
	public void run() {
		if (LicensedAccess.getWorkbenchPluggablePluginRole() != TSWorkbenchPluggablePluginRole.CREATE_EDIT
				&& LicensedAccess.getWorkbenchPluggablePluginRole() != TSWorkbenchPluggablePluginRole.DEPLOY_UNDEPLOY) {
			MessageDialog.openError(null, "Undeploy Generator Error",
					ERR_NOT_PERMITTED);
			return;
		}

		// double check that we only try to undeploy deployed stuff
		final List<PluggableHousing> deployed = new ArrayList<PluggableHousing>();
		for (final PluggableHousing housing : targetHousing) {
			if (housing.isDeployed()) {
				deployed.add(housing);
			}
		}
		if (deployed.isEmpty()) {
			return;
		}
		
		if(confirm) {
			final String confirmationTitle;
			final String confirmationText;
			if (deployed.size() == 1) {
				confirmationTitle = "Un-Deploy Tigerstripe Generator?";
				confirmationText = "Do you really want to undeploy the '"
						+ deployed.get(0).getLabel() + "' generator?";
			} else {
				confirmationTitle = "Un-Deploy Tigerstripe Generators?";
				confirmationText = "Do you really want to undeploy "
						+ deployed.size() + " generators?";
			}
			if (!MessageDialog.openConfirm(dialog.getShell(), confirmationTitle,
					confirmationText)) {
				return;
			}
		}

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor pm) {
				try {
					final int work = WORK_CLOSE_EDITORS + WORK_RELOAD
							+ deployed.size();
					final SubMonitor monitor = SubMonitor.convert(pm, work);

					final PluginManager mgr = PluginManager.getManager();

					monitor.setTaskName("Closing all editors");
					EditorHelper.closeAllEditors(true, true, false, false,
							false);
					monitor.worked(WORK_CLOSE_EDITORS);

					for (final PluggableHousing housing : deployed) {
						monitor.setTaskName("Removing generator " + housing.getLabel());
						mgr.unRegisterHousing(housing);
						
						PluggablePlugin pPlugin = housing.getBody();
						pPlugin.dispose();
						monitor.worked(1);
					}

					// Need to reload in case there is a "contributed"
					// generator
					// with the same ID as the one we just removed.
					monitor.setTaskName("Reloading workbench");
					mgr.load();
					monitor.worked(WORK_RELOAD);
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				} finally {
					pm.done();
				}
			}
		};

		try {
			PlatformUI.getWorkbench().getProgressService().run(true, false, op);
		} catch (InterruptedException ee) {
			EclipsePlugin.log(ee);
		} catch (InvocationTargetException ee) {
			EclipsePlugin.log(ee);
		}

		if (!dialog.getShell().isDisposed()) {
			dialog.populateTable();
		}
	}
	
	public void setConfirm(final boolean confirm) {
		this.confirm = confirm;
	}
}
