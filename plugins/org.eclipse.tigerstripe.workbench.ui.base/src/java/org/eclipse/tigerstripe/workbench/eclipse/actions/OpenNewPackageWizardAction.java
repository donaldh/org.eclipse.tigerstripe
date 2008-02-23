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
import org.eclipse.jdt.internal.ui.wizards.NewPackageCreationWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class OpenNewPackageWizardAction extends AbstractOpenWizardAction {

	/**
	 * Generic default constructor
	 */
	public OpenNewPackageWizardAction() {
		initAction();
	}

	public OpenNewPackageWizardAction(String label, Class[] acceptedTypes) {
		super(label, acceptedTypes, false);
		initAction();
	}

	protected void initAction() {
		setText("Package");
		setImageDescriptor(JavaPluginImages
				.getDescriptor(JavaPluginImages.IMG_OBJS_PACKAGE));
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this,
				IJavaHelpContextIds.OPEN_PACKAGE_WIZARD_ACTION);
	}

	@Override
	protected Wizard createWizard() {
		return new NewPackageCreationWizard() {
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
}