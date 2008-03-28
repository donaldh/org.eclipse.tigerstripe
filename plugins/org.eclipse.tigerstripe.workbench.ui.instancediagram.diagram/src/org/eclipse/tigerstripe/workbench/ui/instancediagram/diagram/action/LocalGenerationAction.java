/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.action;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.generate.NewTigerstripeM0RunWizard;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.InstanceMapEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part.BaseDiagramPartAction;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWizard;

public class LocalGenerationAction extends BaseDiagramPartAction implements
		IObjectActionDelegate {

	public void selectionChanged(IAction action, ISelection selection) {
		mySelectedElement = null;
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (structuredSelection.size() == 1) {
				if (structuredSelection.getFirstElement() instanceof InstanceMapEditPart) {
					mySelectedElement = (EditPart) structuredSelection
							.getFirstElement();
				}
			}
		}
		action.setEnabled(isEnabled());
	}

	protected IWorkbench getWorkbench() {
		return EclipsePlugin.getDefault().getWorkbench();
	}

	protected IStructuredSelection getCurrentSelection() {
		IWorkbenchWindow window = EclipsePlugin.getActiveWorkbenchWindow();
		if (window != null) {
			ISelection selection = window.getSelectionService().getSelection();
			if (selection instanceof IStructuredSelection)
				return (IStructuredSelection) selection;

		}
		return null;
	}

	
	public void run(IAction action) {
		Shell shell = EclipsePlugin.getActiveWorkbenchShell();
		Wizard wizard = new NewTigerstripeM0RunWizard();
		if (wizard instanceof IWorkbenchWizard) {
			((IWorkbenchWizard) wizard).init(getWorkbench(),
					getCurrentSelection());
		}

		WizardDialog dialog = new WizardDialog(shell, wizard);

		dialog.create();
		dialog.open();
	}

}
