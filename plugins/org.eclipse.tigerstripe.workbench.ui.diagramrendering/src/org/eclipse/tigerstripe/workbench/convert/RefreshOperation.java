package org.eclipse.tigerstripe.workbench.convert;

import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part.InstanceDiagramEditor;

public class RefreshOperation extends AbstractOperation {

	private final InstanceDiagramEditor editor;
	private final Set<String> affected;

	public RefreshOperation(String label, InstanceDiagramEditor editor,
			Set<String> affected) {
		super(label);
		this.editor = editor;
		this.affected = affected;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		ConvertUtils.refreshInstance(editor, affected);
		return Status.OK_STATUS;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		ConvertUtils.refreshInstance(editor, affected);
		return Status.OK_STATUS;
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		ConvertUtils.refreshInstance(editor, affected);
		return Status.OK_STATUS;
	}
}
