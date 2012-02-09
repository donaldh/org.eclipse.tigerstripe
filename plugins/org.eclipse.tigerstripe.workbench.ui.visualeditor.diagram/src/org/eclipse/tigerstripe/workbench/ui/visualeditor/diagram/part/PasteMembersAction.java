package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part;

import static org.eclipse.tigerstripe.workbench.ui.EclipsePlugin.getClipboard;
import static org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions.CopyPasteUtils.containsMembers;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramEditDomain;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions.CopyPasteUtils;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.ClassDiagramSynchronizerUtils;
import org.eclipse.ui.IObjectActionDelegate;

public class PasteMembersAction extends BaseDiagramPartAction implements
		IObjectActionDelegate {

	@Override
	protected boolean isEnabled() {
		try {
			return containsMembers(getClipboard()) && selectionIsAllArtifacts();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
			return false;
		}
	}
	
	public void run(IAction action) {
		TigerstripeDiagramEditor tsd = (TigerstripeDiagramEditor) myTargetWorkbenchPart;
		IDiagramEditDomain diagramEditDomain = tsd.getDiagramEditDomain();
		TransactionalEditingDomain editingDomain = tsd.getEditingDomain();
		Diagram diagram = tsd.getDiagram();
		DiagramEditPart diagramEditPart = tsd.getDiagramEditPart();
		Map map = (Map) diagram.getElement();

		for (IAbstractArtifact artifact : getCorrespondingArtifacts()) {
			CopyPasteUtils.doPaste(artifact, getClipboard(), true);
			try {
				ClassDiagramSynchronizerUtils
						.handleQualifiedNamedElementChanged(diagram,
								diagramEditPart, map, artifact, editingDomain,
								diagramEditDomain);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
	}
}
