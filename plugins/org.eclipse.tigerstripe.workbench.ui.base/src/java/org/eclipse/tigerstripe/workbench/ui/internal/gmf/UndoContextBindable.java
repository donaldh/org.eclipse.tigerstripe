package org.eclipse.tigerstripe.workbench.ui.internal.gmf;

import org.eclipse.core.commands.operations.IUndoableOperation;

public interface UndoContextBindable {

	void bindUndoContext(IUndoableOperation op);

}
