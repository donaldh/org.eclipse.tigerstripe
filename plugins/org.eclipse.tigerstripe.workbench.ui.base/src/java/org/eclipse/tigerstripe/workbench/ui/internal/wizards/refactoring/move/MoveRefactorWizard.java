package org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring.move;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring.RefactorPreviewWizardPage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

public class MoveRefactorWizard extends Wizard implements IWorkbenchWizard {

	private IStructuredSelection selection;
	
	private MoveInputWizardPage inputPage;
	private RefactorPreviewWizardPage previewPage;

	@Override
	public boolean performFinish() {

		System.out.println("Performing finish (move)...");
		return true;
	}

	@Override
	public void addPages() {

		inputPage = new MoveInputWizardPage();
		addPage(inputPage);
		previewPage = new RefactorPreviewWizardPage();
		addPage(previewPage);
		inputPage.init(selection);
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

}
