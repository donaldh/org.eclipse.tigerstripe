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
package org.eclipse.tigerstripe.workbench.eclipse.actions;

import org.eclipse.jdt.internal.ui.IJavaHelpContextIds;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.wizards.NewInterfaceCreationWizard;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.ui.PlatformUI;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class OpenNewInterfaceWizardAction extends AbstractOpenWizardAction {

	/**
	 * Generic default constructor
	 */
	public OpenNewInterfaceWizardAction() {
		initAction();
	}

	public OpenNewInterfaceWizardAction(String label, Class[] acceptedTypes) {
		super(label, acceptedTypes, false);
		initAction();
	}

	protected void initAction() {
		setText("Interface");
		setImageDescriptor(JavaPluginImages
				.getDescriptor(JavaPluginImages.IMG_OBJS_INTERFACE));
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this,
				IJavaHelpContextIds.OPEN_INTERFACE_WIZARD_ACTION);
	}

	@Override
	protected Wizard createWizard() {
		return new NewInterfaceCreationWizard() {
			// Bug 219769, we need to run the performFinish in the UI Thread
			@Override
			protected boolean canRunForked() {
				return false;
			}
		};
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
				&& aProject.exists());
	}

}