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
package org.eclipse.tigerstripe.workbench.optional.bulkImport.ui.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.tigerstripe.core.util.messages.MessageList;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * This is a sample new wizard. Its role is to create a new file resource in the
 * provided container. If the container resource (a folder or a project) is
 * selected in the workspace when the wizard is opened, it will accept it as the
 * target container. The wizard creates one file with the extension "mpe". If a
 * sample multi-page editor (also available as a template) is registered for the
 * same extension, it will be able to open it.
 */

public class BulkImportWizard extends Wizard implements INewWizard {

	private BulkImportWizardPage firstPage;

	private IStructuredSelection fSelection;

	private MessageList messages;

	public IStructuredSelection getSelection() {
		return this.fSelection;
	}

	/**
	 * Constructor for NewProjectWizard.
	 */
	public BulkImportWizard() {
		super();
		setNeedsProgressMonitor(true);

		setWindowTitle("Import/Update from XML File ...");
		messages = new MessageList();
	}

	/**
	 * Adding the page to the wizard.
	 */

	@Override
	public void addPages() {
		super.addPages();

		this.firstPage = new BulkImportWizardPage();

		addPage(this.firstPage);

		this.firstPage.init(getSelection());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 *      org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		fSelection = currentSelection;
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	@Override
	public boolean performFinish() {

		// There is nothing to do....
		return true;

	}

}