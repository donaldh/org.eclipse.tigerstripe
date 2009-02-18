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

package org.eclipse.tigerstripe.workbench.ui.internal.wizards.export.facetmodel;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

public class FacetModelExportWizard extends Wizard implements IExportWizard {

	FacetModelExportWizardMainPage wizardPage;

	public FacetModelExportWizard() {

	}

	@Override
	public void addPages() {

		wizardPage = new FacetModelExportWizardMainPage();
		addPage(wizardPage);
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {

		if (selection == null || selection.size() > 1) {
			return;
		}
		
		if(selection.getFirstElement() instanceof IProject) {
			System.out.println("IProject");
		}
		
		if(selection.getFirstElement() instanceof IJavaProject) {
			System.out.println("IJavaProject");
		}

	}

	@Override
	public boolean performFinish() {

		System.out.println("Source: " + wizardPage.getSourceProject().getFullPath());
		System.out.println("Desintation: " + wizardPage.getDestinationProject().getFullPath());
		System.out.println("Facet: " + wizardPage.getFacet());
		System.out.println("References: " + wizardPage.isIncludeReferences());

		return true;
	}

}
