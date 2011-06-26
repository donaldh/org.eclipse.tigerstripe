package org.eclipse.tigerstripe.workbench.convert.partactions;

import org.eclipse.jface.action.IAction;
import org.eclipse.tigerstripe.workbench.convert.Converter;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part.BaseDiagramPartAction;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;

public abstract class ConvertAction extends BaseDiagramPartAction implements
		IObjectActionDelegate {

	protected abstract Class<?> toClass();

	public void run(IAction action) {
		IAbstractArtifact[] abstractArtifacts = getCorrespondingArtifacts();
		if (abstractArtifacts.length == 0) {
			return;
		}
		Converter.convert(getShell(), abstractArtifacts[0], toClass(),
				(IEditorPart) getMyTargetWorkbenchPart());
	}
}
