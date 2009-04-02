package org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring;

public class DragAndDropMoveRefactorWizard extends AbstractModelRefactorWizard {

	@Override
	public void addPages() {

		addPage(new PreviewWizardPage());
	}
}
