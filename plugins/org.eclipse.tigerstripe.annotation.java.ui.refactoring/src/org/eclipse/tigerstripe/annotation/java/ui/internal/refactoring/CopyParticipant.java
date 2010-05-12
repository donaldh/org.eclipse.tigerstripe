package org.eclipse.tigerstripe.annotation.java.ui.internal.refactoring;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;

public class CopyParticipant extends
		org.eclipse.ltk.core.refactoring.participants.CopyParticipant {

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		return new RefactoringStatus();
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		CompositeChange change = new CompositeChange("Group copy change");
		change.add(new CopyChange(getArguments()));
		change.markAsSynthetic();
		return change;
	}

	@Override
	public String getName() {
		return "Copy Partifipant";
	}

	@Override
	protected boolean initialize(Object element) {
		ChangesTracker.initialize();
		return true;
	}

}
