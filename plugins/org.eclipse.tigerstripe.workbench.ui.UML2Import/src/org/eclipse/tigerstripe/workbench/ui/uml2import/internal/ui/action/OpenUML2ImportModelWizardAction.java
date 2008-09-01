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
package org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ui.action;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.tigerstripe.workbench.ui.internal.actions.AbstractOpenWizardAction;
import org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ui.wizards.UML2ImportWizard;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class OpenUML2ImportModelWizardAction extends AbstractOpenWizardAction {

	/**
	 * Generic default constructor
	 */
	public OpenUML2ImportModelWizardAction() {
		// TODO: Implement Help context here
		// WorkbenchHelp.setHelp(this,
		// IJavaHelpContextIds.OPEN_PACKAGE_WIZARD_ACTION);
	}

	public OpenUML2ImportModelWizardAction(String label, Class[] acceptedTypes) {
		super(label, acceptedTypes, false);

		// TODO set help context here
		// WorkbenchHelp.setHelp(this,
		// IJavaHelpContextIds.OPEN_PACKAGE_WIZARD_ACTION);
	}

	@Override
	protected Wizard createWizard() {
		return new UML2ImportWizard();
	}

	@Override
	protected boolean shouldAcceptElement(Object obj) {
		return isOnBuildPath(obj) && !isInArchive(obj);
	}
}