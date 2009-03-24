/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jim Strawn (Cisco Systems, Inc.) - initial implementation
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring.rename;

import org.eclipse.core.runtime.IStatus;
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
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.refactor.ModelRefactorRequest;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;

public class RenameInputWizardPage extends WizardPage {

	public static final String PAGE_NAME = "RenameInputPage";

	private IAbstractArtifact artifact;

	public RenameInputWizardPage() {

		super(PAGE_NAME);
	}

	public void createControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);

		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		composite.setLayout(gridLayout);
		setControl(composite);

		new Label(composite, SWT.NONE).setText("New name:");
		final Text nameField = new Text(composite, SWT.BORDER);
		nameField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		nameField.setText(artifact.getFullyQualifiedName());
		nameField.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {

				try {

					RenameRefactorWizard wizard = (RenameRefactorWizard) getWizard();
					ModelRefactorRequest request = wizard.getRequest();
					request.setOriginal(artifact.getProject(), artifact.getFullyQualifiedName());
					request.setDestination(artifact.getProject(), nameField.getText());
					validatePage(request);

				} catch (TigerstripeException te) {
					EclipsePlugin.log(te);
				}
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

	private void validatePage(ModelRefactorRequest request) {

		if (request.isValid().getSeverity() == IStatus.OK) {
			setPageComplete(true);
		} else {
			setPageComplete(false);
		}
	}

}
