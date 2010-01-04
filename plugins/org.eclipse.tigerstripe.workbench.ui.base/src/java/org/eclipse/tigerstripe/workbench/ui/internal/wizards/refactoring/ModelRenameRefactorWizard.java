/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jim Strawn (Cisco Systems, Inc.) - initial implementation
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring;


public class ModelRenameRefactorWizard extends AbstractModelRefactorWizard {

	private RenameInputWizardPage inputPage;
	
	@Override
	public void addPages() {

		inputPage = new RenameInputWizardPage();
		addPage(inputPage);
		addPage(new PreviewWizardPage());
		inputPage.init(selection);
	}
	
	/**
	 * Returns the default page title used for pages that don't provide their
	 * own page title.
	 *
	 * @return the default page title or <code>null</code> if non has been set
	 *
	 * @see #setDefaultPageTitle(String)
	 */
	public String getDefaultPageTitle() {
		return "Rename Model Artifact";
	}

}
