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
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.eclipse.actions.AbstractOpenWizardAction;
import org.eclipse.tigerstripe.workbench.ui.eclipse.export.PublishWizard;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.abstraction.AbstractLogicalExplorerNode;

public class OpenPublishWizardAction extends AbstractOpenWizardAction {

	private boolean locallyDisabled = false;

	@Override
	protected Wizard createWizard() throws CoreException {
		return new PublishWizard();
	}

	public OpenPublishWizardAction() {
		super();
		setText("Publish Project...");
		setToolTipText("Publish Tigerstripe Project to a Website.");
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
			if (!(EclipsePlugin.getITigerstripeProjectFor(res.getProject()) instanceof ITigerstripeProject))
				locallyDisabled = true;
		} else if (ssel != null
				&& ssel.getFirstElement() instanceof IJavaElement) {
			IJavaElement elm = (IJavaElement) ssel.getFirstElement();
			if (elm.getResource() != null
					&& !(EclipsePlugin.getITigerstripeProjectFor(elm
							.getResource().getProject()) instanceof ITigerstripeProject))
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
			dialog.setText("Publish Tigerstripe Project");
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
								dialog.setText("Publish Tigerstripe Project");
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
