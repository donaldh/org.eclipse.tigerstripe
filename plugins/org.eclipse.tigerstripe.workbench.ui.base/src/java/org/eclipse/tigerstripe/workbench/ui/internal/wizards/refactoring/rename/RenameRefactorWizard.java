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

package org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring.rename;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.tigerstripe.workbench.refactor.IRefactorCommand;
import org.eclipse.tigerstripe.workbench.refactor.ModelRefactorRequest;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring.RefactorPreviewWizardPage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

public class RenameRefactorWizard extends Wizard implements IWorkbenchWizard {

	private IStructuredSelection selection;

	private RenameInputWizardPage inputPage;
	private RefactorPreviewWizardPage previewPage;

	public RenameRefactorWizard() {

		setNeedsProgressMonitor(true);
	}
	
	@Override
	public void addPages() {

		inputPage = new RenameInputWizardPage();
		addPage(inputPage);
		previewPage = new RefactorPreviewWizardPage();
		addPage(previewPage);
		inputPage.init(selection);
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
	
	@Override
	public boolean performFinish() {

		try {
			getContainer().run(true, true, new IRunnableWithProgress() {

				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

					try {
						
						ModelRefactorRequest req = new ModelRefactorRequest();
						req.setOriginal(inputPage.getArtifact().getProject(), inputPage.getArtifact().getFullyQualifiedName());
						req.setDestination(inputPage.getArtifact().getProject(), inputPage.getNewFullyQualifiedName());
						
						IRefactorCommand cmd = req.getCommand(monitor);
						cmd.execute(monitor);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
				
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

}
