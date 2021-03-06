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
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.IArtifactChangeListener;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.AbstractLogicalExplorerNode;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.BuildAction;

public class AuditAction extends BuildAction {

	public AuditAction(IShellProvider provider, int type) {
		super(provider, type);
	}

	/**
	 * The clean audit action is always available from the Tigerstripe Explorer
	 * except from AbstractLogicalExplorerNodes
	 */
	@Override
	protected boolean updateSelection(IStructuredSelection s) {
		super.updateSelection(s);
		return true;
	}

	@Override
	public boolean isEnabled() {
		// Disable this action for AbstractLogicalExplorerNode
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		if (window != null) {
			ISelection sel = window.getSelectionService().getSelection();
			if (sel instanceof IStructuredSelection) {
				IStructuredSelection ssel = (IStructuredSelection) sel;
				if (ssel.getFirstElement() instanceof AbstractLogicalExplorerNode)
					return false;
				else if (ssel.getFirstElement() instanceof IAdaptable) {
					IResource res = (IResource) ((IAdaptable) ssel
							.getFirstElement()).getAdapter(IResource.class);
					if (res != null) {
						if (!(res.getProject().getAdapter(
								ITigerstripeModelProject.class) instanceof ITigerstripeModelProject))
							return false;
					}
				}
			}

		}

		return super.isEnabled();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void run() {
		for (Iterator<? extends IResource> i = getSelectedResources().iterator(); i
				.hasNext();) {
			IResource resource = i.next();
			IProject project = resource.getProject();
			if (project != null) {
				try {
					final IAbstractTigerstripeProject tsProject = (IAbstractTigerstripeProject) project
							.getAdapter(IAbstractTigerstripeProject.class);
					if (tsProject instanceof ITigerstripeModelProject) {
						IRunnableWithProgress op = new IRunnableWithProgress() {
							public void run(IProgressMonitor monitor) {
								try {
									monitor
											.beginTask(
													"Refreshing project:"
															+ ((ITigerstripeModelProject) tsProject)
																	.getName(),
													5);

									((ITigerstripeModelProject) tsProject)
											.getArtifactManagerSession()
											.setBroadcastMask(
													IArtifactChangeListener.NOTIFY_NONE);
									((ITigerstripeModelProject) tsProject)
											.getArtifactManagerSession()
											.refreshAll(true, monitor);
									monitor.done();
								} catch (TigerstripeException e) {
									EclipsePlugin.log(e);
								} finally {
									try {
										((ITigerstripeModelProject) tsProject)
												.getArtifactManagerSession()
												.setBroadcastMask(
														IArtifactChangeListener.NOTIFY_ALL);
									} catch (TigerstripeException e) {
										// ignore here
									}
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

					} else
						throw new TigerstripeException(
								"Couldn't find reference to project '"
										+ project.getName()
										+ "' for a Clean Audit");
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}
		}
		super.run();
	}

}
