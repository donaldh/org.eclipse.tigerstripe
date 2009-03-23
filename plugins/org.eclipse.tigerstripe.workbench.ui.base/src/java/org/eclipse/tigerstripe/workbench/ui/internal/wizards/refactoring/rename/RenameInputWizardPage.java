package org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring.rename;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;

public class RenameInputWizardPage extends WizardPage {

	private Text nameField;

	private IAbstractArtifact artifact;

	protected RenameInputWizardPage() {
		super("RenameInputWizardPage");
	}

	public IAbstractArtifact getArtifact() {
		return artifact;
	}

	public String getNewFullyQualifiedName() {
		
		return artifact.getPackage() + '.' + nameField.getText();
	}
	
	public void createControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);

		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		composite.setLayout(gridLayout);
		setControl(composite);

		new Label(composite, SWT.NONE).setText("New name:");
		nameField = new Text(composite, SWT.BORDER);
		nameField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		nameField.setText(artifact.getName());
		nameField.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				validatePage();
			}

		});
		
		setPageComplete(false);
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

	protected final void validatePage() {
		
		setPageComplete(!nameField.getText().equals(artifact.getName()));
	}

}
