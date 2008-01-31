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

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public abstract class OpenNewArtifactWizardAction extends
		AbstractOpenWizardAction {

	/**
	 * Generic default constructor
	 */
	public OpenNewArtifactWizardAction() {
		// TODO: Implement Help context here
		// WorkbenchHelp.setHelp(this,
		// IJavaHelpContextIds.OPEN_PACKAGE_WIZARD_ACTION);
		initAction();
	}

	public OpenNewArtifactWizardAction(String label, Class[] acceptedTypes) {
		super(label, acceptedTypes, false);

		// TODO set help context here
		// WorkbenchHelp.setHelp(this,
		// IJavaHelpContextIds.OPEN_PACKAGE_WIZARD_ACTION);
		initAction();
	}

	/**
	 * Initialize the action (set text, images, etc...)
	 */
	protected abstract void initAction();

	/**
	 * Returns the wizard to be called
	 */
	@Override
	protected abstract Wizard createWizard();

	@Override
	protected boolean shouldAcceptElement(Object obj) {
		return isOnBuildPath(obj) && !isInArchive(obj);
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		super.selectionChanged(action, selection);

		IAbstractTigerstripeProject aProject = EclipsePlugin
				.getTSProjectInFocus();
		action.setEnabled((aProject instanceof ITigerstripeProject && aProject
				.exists())
				&& EclipsePlugin.getProjectInFocus().isOpen());
	}

}