package org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring.rename;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.refactor.IRefactorCommand;
import org.eclipse.tigerstripe.workbench.refactor.ModelRefactorRequest;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring.RefactorPreviewWizardPage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

public class RenameRefactorWizard extends Wizard implements IWorkbenchWizard {

	private IStructuredSelection selection;

	private RenameInputWizardPage inputPage;
	private RefactorPreviewWizardPage previewPage;

	@Override
	public boolean performFinish() {

		try {
			System.out.println("Performing finish (rename)...");
			System.out.println("Original Project: " + inputPage.getArtifact().getProject().getName());
			System.out.println("Original Name: " + inputPage.getArtifact().getName());
			System.out.println("New Name: " + inputPage.getNewFullyQualifiedName());

		} catch (TigerstripeException e) {

			e.printStackTrace();
		}

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

}
