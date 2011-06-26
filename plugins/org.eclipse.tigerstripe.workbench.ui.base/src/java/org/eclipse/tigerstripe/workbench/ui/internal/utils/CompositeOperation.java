package org.eclipse.tigerstripe.workbench.ui.internal.utils;

import static org.eclipse.tigerstripe.workbench.ui.EclipsePlugin.PLUGIN_ID;

import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;

public class CompositeOperation extends AbstractOperation {

	private final List<IUndoableOperation> oprations;

	private static final Callback EXECUTE = new Callback() {

		public IStatus apply(IUndoableOperation op, IProgressMonitor monitor,
				IAdaptable info) throws ExecutionException {
			return op.execute(monitor, info);
		}
	};

	private static final Callback UNDO = new Callback() {

		public IStatus apply(IUndoableOperation op, IProgressMonitor monitor,
				IAdaptable info) throws ExecutionException {
			return op.undo(monitor, info);
		}
	};

	private static final Callback REDO = new Callback() {

		public IStatus apply(IUndoableOperation op, IProgressMonitor monitor,
				IAdaptable info) throws ExecutionException {
			return op.redo(monitor, info);
		}
	};

	public CompositeOperation(String label, List<IUndoableOperation> oprations) {
		super(label);
		this.oprations = oprations;
	}

	@Override
	public IStatus execute(final IProgressMonitor monitor, final IAdaptable info)
			throws ExecutionException {
		return runBatch(monitor, info, EXECUTE, false);
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return runBatch(monitor, info, REDO, false);
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		return runBatch(monitor, info, UNDO, true);
	}

	private IStatus runBatch(IProgressMonitor monitor, IAdaptable info,
			Callback callback, boolean reverse) throws ExecutionException {
		int size = oprations.size();
		IStatus[] statuses = new IStatus[size];

		if (reverse) {
			for (int index = size - 1; index > -1; --index) {
				IUndoableOperation op = oprations.get(index);
				statuses[index] = callback.apply(op, monitor, info);
			}
		} else {
			for (int index = 0; index < size; ++index) {
				IUndoableOperation op = oprations.get(index);
				statuses[index] = callback.apply(op, monitor, info);
			}
		}
		return new MultiStatus(PLUGIN_ID, 0, statuses, "", null);
	}

	public List<IUndoableOperation> getOprations() {
		return oprations;
	}

	private static interface Callback {
		IStatus apply(IUndoableOperation op, IProgressMonitor monitor,
				IAdaptable info) throws ExecutionException;
	}

}
