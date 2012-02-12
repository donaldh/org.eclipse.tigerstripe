package org.eclipse.tigerstripe.workbench.convert.handlers;

import static org.eclipse.tigerstripe.workbench.utils.AdaptHelper.adapt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.convert.Converter;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public abstract class ConvertationHandler extends AbstractHandler {

	protected abstract Class<?> toClass();

	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		List<IAbstractArtifact> artifacts = extractArtifacts(selection);
		if (artifacts.isEmpty()) {
			return null;
		}
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		final Shell shell = window.getShell();
		new Converter(shell, toClass(), artifacts).convert();
		return null;
	}

	private List<IAbstractArtifact> extractArtifacts(ISelection selection) {
		List<IAbstractArtifact> artifacts = new ArrayList<IAbstractArtifact>();
		if (!(selection instanceof IStructuredSelection)) {
			return artifacts;
		}
		IStructuredSelection ss = (IStructuredSelection) selection;
		for (Object element : ss.toList()) {
			if (element instanceof IAdaptable) {
				IAbstractArtifact entity = adapt((IAdaptable) element,
						IAbstractArtifact.class);
				if (entity != null) {
					artifacts.add(entity);
				}
			}
		}
		return artifacts;
	}
}
