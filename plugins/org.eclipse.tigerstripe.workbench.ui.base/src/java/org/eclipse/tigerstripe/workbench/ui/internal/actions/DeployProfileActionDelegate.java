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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.patterns.PatternFactory;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.profile.PhantomTigerstripeProjectMgr;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfileSession;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.WeakRestart;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.profile.ProfileEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.AbstractDiagramEditor;
import org.eclipse.tigerstripe.workbench.utils.AdaptHelper;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
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
								+ "') as the active profile.\n\nThis will restart the workbench.\n\nDo you want to continue?\n\n(You will be able to rollback to the current active profile).  ")) {
			
			IRunnableWithProgress op = new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) {
					try {
						monitor.beginTask("Deploying new Active Profile", 10);

						IWorkbenchProfileSession session = TigerstripeCore
								.getWorkbenchProfileSession();
						rollbackCreated = session.saveAsActiveProfile(handle);
						monitor.worked(2);
						session.reloadActiveProfile();
						monitor.worked(1);
						WeakRestart.restart(new SubProgressMonitor(monitor, 7));
						monitor.done();
						operationSucceeded = true;
						
					} catch (Exception e) {
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
				// We still need to do this as it replaces the profile that will be used on start up
				dialog.run(true, false, op);
			} catch (InterruptedException e) {
				EclipsePlugin.log(e);
			} catch (InvocationTargetException e) {
				EclipsePlugin.log(e);
			}

		}

	}
}
