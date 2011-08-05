package org.eclipse.tigerstripe.workbench.ui.internal.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.ui.refactoring.RefactoringSaveHelper;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.TigerstripeRefactorWizardDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring.AbstractModelRefactorWizard;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring.ModelMoveRefactorWizard;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class RefactoringMoveHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		if (new RefactoringSaveHelper(RefactoringSaveHelper.SAVE_ALL_ALWAYS_ASK)
				.saveEditors(window.getShell())) {
			ISelection selection = HandlerUtil.getCurrentSelection(event);
			AbstractModelRefactorWizard wizard = new ModelMoveRefactorWizard();
			wizard.init(
					window.getWorkbench(),
					selection instanceof IStructuredSelection ? (IStructuredSelection) selection
							: StructuredSelection.EMPTY);
			TigerstripeRefactorWizardDialog dialog = new TigerstripeRefactorWizardDialog(
					window.getShell(), wizard);
			dialog.create();
			dialog.open();
		}
		return null;
	}
}
