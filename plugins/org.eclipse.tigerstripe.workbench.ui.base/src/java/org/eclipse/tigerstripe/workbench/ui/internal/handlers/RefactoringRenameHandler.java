package org.eclipse.tigerstripe.workbench.ui.internal.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring.RenameModelArtifactWizard;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class RefactoringRenameHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		RenameModelArtifactWizard wizard = new RenameModelArtifactWizard();
		wizard.init(selection instanceof IStructuredSelection ? (IStructuredSelection) selection : StructuredSelection.EMPTY);
		WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
		dialog.open();
		return null;
	}

}
