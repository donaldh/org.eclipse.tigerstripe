package org.eclipse.tigerstripe.annotation.java.ui.internal.refactoring;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.NullChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CopyArguments;
import org.eclipse.ltk.core.refactoring.participants.ReorgExecutionLog;

public class CopyChange extends Change {

	private CopyArguments arguments;

	public CopyChange(CopyArguments arguments) {
		this.arguments = arguments;
	}

	@Override
	public Object getModifiedElement() {
		return null;
	}

	@Override
	public String getName() {
		return "Copy Change";
	}

	@Override
	public void initializeValidationData(IProgressMonitor pm) {
	}

	@Override
	public RefactoringStatus isValid(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		return new RefactoringStatus();
	}

	@Override
	public Change perform(IProgressMonitor pm) throws CoreException {
		ReorgExecutionLog executionLog = arguments.getExecutionLog();
		Object[] processedElements = executionLog.getProcessedElements();
		for (int i = 0; i < processedElements.length; i++) {
			Object element = processedElements[i];
			String newName = executionLog.getNewName(element);
			ChangesTracker.setNewName(element, newName);
		}
		return new NullChange();
	}

}
