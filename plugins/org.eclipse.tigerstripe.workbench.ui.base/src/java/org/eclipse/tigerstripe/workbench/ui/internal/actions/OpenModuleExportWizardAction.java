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

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.export.ModuleExportWizard;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.AbstractLogicalExplorerNode;

public class OpenModuleExportWizardAction extends AbstractOpenWizardAction {

	private boolean locallyDisabled = false;

	@Override
	protected Wizard createWizard() throws CoreException {
		return new ModuleExportWizard();
	}

	public OpenModuleExportWizardAction() {
		super();
		setText("Tigerstripe Module...");
		setToolTipText("Export project into a Tigerstripe Module");
	}

	@Override
	public void run() {
		if (buildProjectAndCheck()) {
			super.run();
		}
	}

	@Override
	/**
	 * This action is disabled on non tigerstripe model projects, and on
	 * abstractlogicalexplorernodes
	 * 
	 */
	public boolean isEnabled() {
		boolean superEnabled = super.isEnabled();
		IStructuredSelection ssel = getCurrentSelection();
		// making sure that this action is disabled for
		// AbstractLogicalExplorerNodes
		locallyDisabled = false;
		if (ssel != null
				&& ssel.getFirstElement() instanceof AbstractLogicalExplorerNode) {
			locallyDisabled = true;
		} else if (ssel != null && ssel.getFirstElement() instanceof IResource) {
			IResource res = (IResource) ssel.getFirstElement();
			if (!(res.getProject().getAdapter(ITigerstripeModelProject.class) instanceof ITigerstripeModelProject))
				locallyDisabled = true;
		} else if (ssel != null
				&& ssel.getFirstElement() instanceof IJavaElement) {
			IJavaElement elm = (IJavaElement) ssel.getFirstElement();
			if (elm.getResource() != null
					&& !(elm.getResource().getProject().getAdapter(
							ITigerstripeModelProject.class) instanceof ITigerstripeModelProject))
				locallyDisabled = true;
		}
		if (locallyDisabled)
			return false;
		return superEnabled;
	}

	private boolean buildProjectAndCheck() {
		IStructuredSelection ssel = getCurrentSelection();
		Shell shell = EclipsePlugin.getActiveWorkbenchShell();

		if (ssel == null) {
			MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR);
			dialog.setText("Package Tigerstripe Project");
			dialog.setMessage("Please select a Tigerstripe Project first.");
			dialog.open();
			return false;
		} else {
			Object selection = ssel.getFirstElement();
			if (selection instanceof IAdaptable) {
				IAdaptable adapted = (IAdaptable) selection;
				IResource res = (IResource) adapted.getAdapter(IResource.class);

				if (res == null)
					// let the user deal with it!
					return true;
				else {
					IProject project = res.getProject();

					// FIXME: trigger a build here

					try {
						IMarker[] markers = project
								.findMarkers(IMarker.PROBLEM, true,
										IResource.DEPTH_INFINITE);
						for (int i = 0; i < markers.length; i++) {
							if (IMarker.SEVERITY_ERROR == markers[i]
									.getAttribute(IMarker.SEVERITY,
											IMarker.SEVERITY_INFO)) {
								MessageBox dialog = new MessageBox(shell,
										SWT.ICON_QUESTION | SWT.YES | SWT.NO);
								dialog
										.setMessage("This project contains errors. Do you want to continue?");
								dialog.setText("Package Tigerstripe Project");
								if (dialog.open() == SWT.YES)
									return true;
								return false;
							}
						}
						return true;
					} catch (CoreException e) {
						EclipsePlugin.log(e);
						return true;
					}
				}
			}
		}
		return false;
	}

}
