package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.policies;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.OpenEditPolicy;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions.TSOpenAction;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.ui.PlatformUI;

public class OpenInEditorEditPolicy extends OpenEditPolicy {

	@Override
	protected Command getOpenCommand(Request request) {

		return new Command() {

			@Override
			public boolean canExecute() {
				return true;
			}

			@Override
			public boolean canUndo() {
				return false;
			}

			@Override
			public void execute() {
				EditPart editPart = getHost();
				while (editPart != null) {
					EObject element = ((View) editPart.getModel()).getElement();
					if (element instanceof AbstractArtifact) {
						AbstractArtifact artifact = (AbstractArtifact) element;
						try {
							IAbstractArtifact iArtifact = artifact
									.getCorrespondingIArtifact();
							TSOpenAction.openEditor(iArtifact, PlatformUI
									.getWorkbench().getActiveWorkbenchWindow()
									.getActivePage());
						} catch (TigerstripeException e) {
							BasePlugin.log(e);
						}
						break;
					}
					editPart = editPart.getParent();
				}
			}
		};
	}

}
