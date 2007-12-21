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
package org.eclipse.tigerstripe.eclipse.actions;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.api.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.eclipse.wizards.generate.NewTigerstripeRunWizard;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class OpenGenerateInterfaceWizardAction extends AbstractOpenWizardAction {

	/**
	 * Generic default constructor
	 */
	public OpenGenerateInterfaceWizardAction() {
		// TODO: Implement Help context here
		// WorkbenchHelp.setHelp(this,
		// IJavaHelpContextIds.OPEN_PACKAGE_WIZARD_ACTION);
	}

	public OpenGenerateInterfaceWizardAction(String label, Class[] acceptedTypes) {
		super(label, acceptedTypes, false);

		// TODO set help context here
		// WorkbenchHelp.setHelp(this,
		// IJavaHelpContextIds.OPEN_PACKAGE_WIZARD_ACTION);
	}

	@Override
	protected Wizard createWizard() {
		return new NewTigerstripeRunWizard();
	}

	@Override
	protected boolean shouldAcceptElement(Object obj) {
		return isOnBuildPath(obj) && !isInArchive(obj);
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		super.selectionChanged(action, selection);
		IAbstractTigerstripeProject aProject = EclipsePlugin
				.getTSProjectInFocus();
		action.setEnabled(aProject instanceof ITigerstripeProject
				&& aProject.exists()
				&& EclipsePlugin.getProjectInFocus().isOpen());
	}

	@Override
	public void run() {
		if (buildProjectAndCheck()) {
			super.run();
		}
	}

	private boolean buildProjectAndCheck() {

		IProject project = EclipsePlugin.getProjectInFocus();
		Shell shell = EclipsePlugin.getActiveWorkbenchShell();

		if (project != null) {
			IAbstractTigerstripeProject tsProject = EclipsePlugin
					.getITigerstripeProjectFor(project);
			if (!(tsProject instanceof ITigerstripeProject)) {
				MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR);
				dialog.setText("Generate Tigerstripe Project");
				dialog
						.setMessage("No valid Tigerstripe Project is in Focus.\nPlease select a project in the Tigerstripe Explorer or set focus on the editor.");
				dialog.open();
				return false;
			}

			if (!project.isAccessible()) {
				MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR);
				dialog.setText("Generate Tigerstripe Project");
				dialog
						.setMessage("Project "
								+ project.getName()
								+ " is closed.\nPlease open this project first to generate.");
				dialog.open();
				return false;
			}

			try {
				IMarker[] markers = project.findMarkers(IMarker.PROBLEM, true,
						IResource.DEPTH_INFINITE);
				for (int i = 0; i < markers.length; i++) {
					if (IMarker.SEVERITY_ERROR == markers[i].getAttribute(
							IMarker.SEVERITY, IMarker.SEVERITY_INFO)) {
						MessageBox dialog = new MessageBox(shell,
								SWT.ICON_QUESTION | SWT.YES | SWT.NO);
						dialog
								.setMessage("This project contains errors: generation may only be partial. Do you want to continue?");
						dialog.setText("Generate Tigerstripe Project");
						if (dialog.open() == SWT.YES)
							return true;
						return false;
					}
				}
			} catch (CoreException e) {
				EclipsePlugin.log(e);
			}
			return true;
		}
		return false;
	}
}