package org.eclipse.tigerstripe.workbench.ui.internal.gmf;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;

public interface Lifecycle {

	void init(DiagramEditPart editPart, DiagramEditor editorPart,
			IUndoContext undoContext);

	void dispose(DiagramEditPart editPart, DiagramEditor editorPart,
			IUndoContext undoContext);

}
