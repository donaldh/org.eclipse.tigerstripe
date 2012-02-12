package org.eclipse.tigerstripe.workbench.convert.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.convert.Converter;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public abstract class ConvertationHandler extends AbstractHandler {

	protected abstract Class<?> toClass();
	
	public Object execute(ExecutionEvent event) throws ExecutionException {

		ISelection selection = HandlerUtil.getCurrentSelection(event);
		IStructuredSelection ss = selection instanceof IStructuredSelection ? (IStructuredSelection) selection
				: StructuredSelection.EMPTY;

		IJavaElement firstElement = (IJavaElement) ss.getFirstElement();

		final IAbstractArtifact entity = (IAbstractArtifact) firstElement
				.getAdapter(IAbstractArtifact.class);

		if (entity == null) {
			return null;
		}
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		final Shell shell = window.getShell();
		
		new Converter(shell, entity, toClass()).convert();
		return null;
	}
}
