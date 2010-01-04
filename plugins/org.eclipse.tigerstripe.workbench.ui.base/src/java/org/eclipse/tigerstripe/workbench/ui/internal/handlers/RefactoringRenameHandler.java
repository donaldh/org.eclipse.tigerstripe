package org.eclipse.tigerstripe.workbench.ui.internal.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.TigerstripeRefactorWizardDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring.AbstractModelRefactorWizard;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring.ModelRenameRefactorWizard;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class RefactoringRenameHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		AbstractModelRefactorWizard wizard = new ModelRenameRefactorWizard();
		wizard.init(window.getWorkbench(), selection instanceof IStructuredSelection ? (IStructuredSelection) selection : StructuredSelection.EMPTY);
		TigerstripeRefactorWizardDialog dialog = new TigerstripeRefactorWizardDialog(window.getShell(), wizard);
		dialog.create();
		dialog.open();
		return null;
	}

}
