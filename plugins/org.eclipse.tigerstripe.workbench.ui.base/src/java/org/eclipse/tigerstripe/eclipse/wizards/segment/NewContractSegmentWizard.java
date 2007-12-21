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
package org.eclipse.tigerstripe.eclipse.wizards.segment;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.api.contract.segment.IContractSegment;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;

public class NewContractSegmentWizard extends
		org.eclipse.tigerstripe.eclipse.wizards.BasicNewResourceWizard
		implements INewWizard {

	private NewContractSegmentWizardPage mainPage;

	/*
	 * (non-Javadoc) Method declared on IWizard.
	 */
	@Override
	public void addPages() {
		super.addPages();
		// if (LicensedAccess.getLicenseProfile().getWorkbenchProfileRole() !=
		// TSWorkbenchProfileRole.CREATE_EDIT) {
		// NewContractSegmentErrorPage errorPage = new
		// NewContractSegmentErrorPage("errorPage1");
		// errorPage.setTitle("New Contract Segment Error");
		// addPage(errorPage);
		// } else {
		mainPage = new NewContractSegmentWizardPage(
				"newFilePage1", getSelection());//$NON-NLS-1$
		mainPage.setTitle("New Tigerstripe Workbench Contract Segment");
		mainPage
				.setDescription("Create a new Tigerstripe Workbench Contract Segment.");
		mainPage.setFileName(IContractSegment.DEFAULT_SEGMENT_FILE);
		addPage(mainPage);
		// }
	}

	/*
	 * (non-Javadoc) Method declared on IWorkbenchWizard.
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		super.init(workbench, currentSelection);
		setNeedsProgressMonitor(true);
	}

	/*
	 * (non-Javadoc) Method declared on BasicNewResourceWizard.
	 */
	@Override
	protected void initializeDefaultPageImageDescriptor() {
		ImageDescriptor desc = IDEWorkbenchPlugin
				.getIDEImageDescriptor("wizban/newfile_wiz.gif");//$NON-NLS-1$
		setDefaultPageImageDescriptor(desc);
	}

	/*
	 * (non-Javadoc) Method declared on IWizard.
	 */
	@Override
	public boolean performFinish() {
		IFile file = mainPage.createNewFile();
		if (file == null)
			return false;

		selectAndReveal(file);

		// Open editor on new file.
		IWorkbenchWindow dw = getWorkbench().getActiveWorkbenchWindow();
		try {
			if (dw != null) {
				IWorkbenchPage page = dw.getActivePage();
				if (page != null) {
					IDE.openEditor(page, file, true);
				}
			}
		} catch (PartInitException e) {
			// DialogUtil.openError(dw.getShell(),
			// ResourceMessages.FileResource_errorMessage, e.getMessage(),
			// e);
		}

		return true;
	}

	/**
	 * Constructor for NewProjectWizard.
	 */
	public NewContractSegmentWizard() {
		super();

		setNeedsProgressMonitor(true);
		setWindowTitle("New Tigerstripe Workbench Contract Segment");
	}

}
