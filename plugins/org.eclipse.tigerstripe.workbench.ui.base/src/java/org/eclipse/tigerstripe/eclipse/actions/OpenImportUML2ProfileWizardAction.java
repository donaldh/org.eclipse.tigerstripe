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

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.tigerstripe.eclipse.wizards.imports.uml2.ImportUML2ProfileWizard;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class OpenImportUML2ProfileWizardAction extends AbstractOpenWizardAction {

	/**
	 * Generic default constructor
	 */
	public OpenImportUML2ProfileWizardAction() {
		// TODO: Implement Help context here
		// WorkbenchHelp.setHelp(this,
		// IJavaHelpContextIds.OPEN_PACKAGE_WIZARD_ACTION);
	}

	public OpenImportUML2ProfileWizardAction(String label, Class[] acceptedTypes) {
		super(label, acceptedTypes, false);

		// TODO set help context here
		// WorkbenchHelp.setHelp(this,
		// IJavaHelpContextIds.OPEN_PACKAGE_WIZARD_ACTION);
	}

	@Override
	protected Wizard createWizard() {
		return new ImportUML2ProfileWizard();
	}

	@Override
	protected boolean shouldAcceptElement(Object obj) {
		return isOnBuildPath(obj) && !isInArchive(obj);
	}
}