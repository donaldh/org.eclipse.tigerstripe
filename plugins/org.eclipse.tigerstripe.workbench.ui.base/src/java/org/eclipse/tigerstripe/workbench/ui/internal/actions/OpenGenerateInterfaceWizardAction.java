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

import static org.eclipse.jface.dialogs.MessageDialog.openError;
import static org.eclipse.jface.dialogs.MessageDialog.openQuestion;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.generate.NewTigerstripeRunWizard;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class OpenGenerateInterfaceWizardAction extends AbstractOpenWizardAction
		implements ISelectionChangedListener {

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
		action.setEnabled(aProject instanceof ITigerstripeModelProject
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
			IAbstractTigerstripeProject tsProject = (IAbstractTigerstripeProject) project
					.getAdapter(IAbstractTigerstripeProject.class);
			if (!(tsProject instanceof ITigerstripeModelProject)) {
				String msg = "No valid Tigerstripe Project is in Focus.\nPlease select a project in the Tigerstripe Explorer or set focus on the editor.";
				openError(shell, "Generate Tigerstripe Project", msg);
				return false;
			}

			if (!project.isAccessible()) {
				String msg = "Project "
						+ project.getName()
						+ " is closed.\nPlease open this project first to generate.";
				openError(shell, "Generate Tigerstripe Project", msg);
				return false;
			}

			try {
				IMarker[] markers = project.findMarkers(IMarker.PROBLEM, true,
						IResource.DEPTH_INFINITE);
				for (int i = 0; i < markers.length; i++) {
					if (IMarker.SEVERITY_ERROR == markers[i].getAttribute(
							IMarker.SEVERITY, IMarker.SEVERITY_INFO)) {
						return openQuestion(
								shell,
								"Generate Tigerstripe Project - Errors",
								"This project contains errors: generation may only be partial. Do you want to continue?");
					}
				}
			} catch (CoreException e) {
				EclipsePlugin.log(e);
			}
			return true;
		}
		return false;
	}

	public void selectionChanged(SelectionChangedEvent event) {
		IAbstractTigerstripeProject aProject = EclipsePlugin
				.getTSProjectInFocus();
		setEnabled(aProject instanceof ITigerstripeModelProject
				&& aProject.exists()
				&& EclipsePlugin.getProjectInFocus().isOpen());
	}

}