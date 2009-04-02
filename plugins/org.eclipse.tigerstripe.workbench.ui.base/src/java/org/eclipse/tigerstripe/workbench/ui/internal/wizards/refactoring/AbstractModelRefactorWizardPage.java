package org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.refactor.ModelRefactorRequest;

public abstract class AbstractModelRefactorWizardPage extends WizardPage {

	protected IAbstractArtifact artifact;
	
	protected AbstractModelRefactorWizardPage(String pageName) {
		super(pageName);
	}

	public void init(IStructuredSelection selection) {
	
		if (selection == null)
			return;
	
		if (selection.size() > 0) {
	
			Object obj = selection.getFirstElement();
			if (obj instanceof IJavaElement) {
	
				IJavaElement element = (IJavaElement) obj;
				IAbstractArtifact artifact = (IAbstractArtifact) element.getAdapter(IAbstractArtifact.class);
				if (artifact != null) {
					this.artifact = artifact;
				}
			}
		}
	}

	protected boolean validatePage(ModelRefactorRequest request) {
	
		if (request.isValid().getSeverity() == IStatus.OK) {
			setPageComplete(true);
			return true;
		} else {
			setPageComplete(false);
			return false;
		}
	}


}
