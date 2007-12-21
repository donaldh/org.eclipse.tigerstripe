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
package org.eclipse.tigerstripe.workbench.ui.gmf.export.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

public class AllDiagramsExportWizard extends Wizard implements IExportWizard {

	private AllDiagramsExportWizardPage firstPage;

	private IStructuredSelection selection;

	public AllDiagramsExportWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		super.addPages();

		this.firstPage = new AllDiagramsExportWizardPage();
		addPage(this.firstPage);
		this.firstPage.init(selection);
	}

	@Override
	public boolean performFinish() {
		IRunnableWithProgress runnable = firstPage.getRunnable();
		try {
			getContainer().run(false, false, runnable);
		} catch (InvocationTargetException e) {
			EclipsePlugin.log(e);
		} catch (InterruptedException e) {
			EclipsePlugin.log(e);
		}
		return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

}
