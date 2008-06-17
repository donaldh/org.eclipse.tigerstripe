/**
 * 
 */
package org.eclipse.tigerstripe.annotation.ui.internal.diagrams;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest.ConnectionViewDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest.ViewDescriptor;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.tigerstripe.annotation.ui.diagrams.DiagramAnnotationType;
import org.eclipse.tigerstripe.annotation.ui.diagrams.IAnnotationType;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationsCreator extends AbstractHandler {
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow(
			).getActivePage().getActiveEditor();
		
		if (editor instanceof IDiagramWorkbenchPart) {
			IDiagramWorkbenchPart part = (IDiagramWorkbenchPart)editor;
			CompoundCommand command = new CompoundCommand();
			command.add(getAnnotationCommand(part));
			command.add(getConnectionCommand(part));
			if (command != null && command.canExecute()) {
				command.execute();
			}
		}
        return null;
	}
	
	protected Command getAnnotationCommand(IDiagramWorkbenchPart editor) {
		EditPart part = editor.getDiagramEditPart();
		IAnnotationType type = DiagramAnnotationType.ANNOTATION;
		String hint = type.getSemanticHint();
		ViewDescriptor viewDescriptor = new ViewDescriptor(null,
			Node.class, hint, PreferencesHint.USE_DEFAULTS);
		CreateViewRequest request = new CreateViewRequest(viewDescriptor);
		return part.getCommand(request);
	}
	
	protected Command getConnectionCommand(IDiagramWorkbenchPart editor) {
		EditPart part = editor.getDiagramEditPart();
		IAnnotationType type = DiagramAnnotationType.CONNECTION;
		String hint = type.getSemanticHint();
		
		ConnectionViewDescriptor viewDescriptor = new ConnectionViewDescriptor(
				type, hint, PreferencesHint.USE_DEFAULTS);
		CreateConnectionViewRequest request = new CreateConnectionViewRequest(viewDescriptor);
		return part.getCommand(request);
	}

}
