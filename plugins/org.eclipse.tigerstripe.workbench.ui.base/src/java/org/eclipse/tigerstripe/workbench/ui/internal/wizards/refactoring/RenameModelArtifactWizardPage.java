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

package org.eclipse.tigerstripe.workbench.ui.internal.wizards.refactoring;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.dialogs.StatusUtil;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.refactor.ModelRefactorRequest;
import org.eclipse.tigerstripe.workbench.refactor.PackageModelRefactorRefactorRequest;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.ArtifactNameValidator;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.WizardUtils;

@SuppressWarnings("restriction")
public class RenameModelArtifactWizardPage extends WizardPage {

	private static final String NAME = "modelRename"; //$NON-NLS-1$

	private Text nameText;

	private Button renameSubpackages;

	IJavaElement javaElement;

	ITigerstripeModelProject modelProject;

	private IAbstractArtifact modelArtifact;

	protected RenameModelArtifactWizardPage() {
		super(NAME);
	}

	public void init(IStructuredSelection selection) {

		javaElement = getSelectionJavaElement(selection);
		if (javaElement != null) {
			modelProject = (ITigerstripeModelProject) javaElement
					.getJavaProject().getProject()
					.getAdapter(ITigerstripeModelProject.class);
			modelArtifact = (IAbstractArtifact) javaElement
					.getAdapter(IAbstractArtifact.class);
		}

	}

	private IJavaElement getSelectionJavaElement(IStructuredSelection selection) {
		return WizardUtils.getInitialJavaElement(selection);
	}

	public void createControl(Composite parent) {

		GridData gridData;
		Composite container = new Composite(parent, SWT.NULL);

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);
		setControl(container);

		Label nameLabel = new Label(container, SWT.LEFT);
		nameLabel.setText("New name:");
		nameLabel.setData("name", "nameLabel");

		nameText = new Text(container, SWT.SINGLE | SWT.BORDER);
		nameText.setData("name", "nameText");
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		nameText.setLayoutData(gridData);
		if (javaElement.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
			nameText.setText(modelArtifact.getFullyQualifiedName());
		} else {
			nameText.setText(modelArtifact.getName());
		}
		nameText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				updatePageComplete();
				if (isPageComplete()) {
					addRefactorRequest();
				}
			}
		});
		if (modelArtifact instanceof IPackageArtifact) {
			new Label(container, SWT.LEFT);
			renameSubpackages = new Button(container, SWT.CHECK);
			renameSubpackages.setText("Rename subpackages");
			renameSubpackages.setData(new GridData(
					GridData.HORIZONTAL_ALIGN_BEGINNING));

			renameSubpackages.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					if (isPageComplete()) {
						addRefactorRequest();
					}
				}
			});
		}

		updatePageComplete();
	}

	private void updatePageComplete() {

		setTitle("Refactor \"" + modelArtifact.getFullyQualifiedName() + "\"");
		setPageComplete(false);

		StatusInfo defaultStatus = new StatusInfo(IStatus.INFO,
				"Specify new name for " + modelArtifact.getFullyQualifiedName()
						+ '.');
		StatusUtil.applyToStatusLine(this, defaultStatus);

		String newName = nameText.getText().trim();
		if (modelArtifact instanceof IPackageArtifact) {
			if (modelArtifact.getFullyQualifiedName().equals(newName)) {
				return;
			}

			IStatus status = ArtifactNameValidator
					.validatePackageArtifactName(newName);
			if (!status.isOK()) {
				StatusUtil.applyToStatusLine(this, status);
				if (status.getSeverity() == IStatus.ERROR)
					return;
			}
		} else {
			if (modelArtifact.getName().equals(newName)) {
				return;
			}

			IStatus status = ArtifactNameValidator
					.validateArtifactName(newName);
			if (!status.isOK()) {
				StatusUtil.applyToStatusLine(this, status);
				if (status.getSeverity() == IStatus.ERROR)
					return;
			}
		}

		try {
			if (!(modelArtifact instanceof IPackageArtifact)) {
				IStatus status = ArtifactNameValidator
						.validateArtifactDoesNotExist(modelProject, newName);
				if (!status.isOK()) {
					StatusUtil.applyToStatusLine(this, status);
					return;
				}

			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
			StatusUtil.applyToStatusLine(this, Status.CANCEL_STATUS);
		}

		setPageComplete(true);
	}

	private void addRefactorRequest() {

		RenameModelArtifactWizard wizard = (RenameModelArtifactWizard) this
				.getWizard();
		wizard.clearRequests();

		try {

			ModelRefactorRequest request;
			if (modelArtifact instanceof IPackageArtifact) {
				request = new PackageModelRefactorRefactorRequest();
				((PackageModelRefactorRefactorRequest)request).setRenameSubpackages(renameSubpackages.getSelection());
			} else {
				request = new ModelRefactorRequest();
			}
			request.setOriginal(modelArtifact.getProject(),
					modelArtifact.getFullyQualifiedName());

			if (javaElement.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
				request.setDestination(modelArtifact.getProject(), nameText
						.getText().trim());
			} else {
				request.setDestination(modelArtifact.getProject(),
						modelArtifact.getPackage() + '.'
								+ nameText.getText().trim());
			}

			if (validateRequest(request)) {
				wizard.addRequest(request);
			}

		} catch (TigerstripeException e) {

			// No need to log this...
			// EclipsePlugin.log(e);
			setErrorMessage(e.getMessage());
			setPageComplete(false);
		}
	}

	private boolean validateRequest(ModelRefactorRequest request)
			throws TigerstripeException {
		IStatus result = request.isValid();
		if (result.isOK()) {
			return true;
		} else {
			throw new TigerstripeException(result.getMessage());
		}
	}
}
