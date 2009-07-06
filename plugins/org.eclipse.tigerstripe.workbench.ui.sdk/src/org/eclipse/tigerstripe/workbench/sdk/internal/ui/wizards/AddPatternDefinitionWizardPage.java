/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.)
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.sdk.internal.ui.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.sdk.internal.ISDKProvider;

public class AddPatternDefinitionWizardPage extends AbstractWizardPage
		implements IWizardPage {

	private Text patternFileText;
	private Button browsePatternFilesButton;
	private Text validatorClassText;
	private Button browseValidatorButton;
	private Button chooseContributionButton;

	protected AddPatternDefinitionWizardPage(String pageName, Shell shell,
			ISDKProvider provider) {
		super(pageName);
		this.shell = shell;
		this.provider = provider;
	}

	protected void init(IStructuredSelection selection) {

	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);

		WizardPageListener adapter = new WizardPageListener();
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		composite.setLayout(gridLayout);

		final Label contributionLabel = new Label(composite, SWT.NONE);
		contributionLabel.setText("Contribution");
		contributerText = new Text(composite, SWT.BORDER);
		final GridData gd_contributionText = new GridData(275, SWT.DEFAULT);
		contributerText.setLayoutData(gd_contributionText);
		contributerText.addModifyListener(adapter);
		// MUST do this via browse
		contributerText.setEditable(false);

		chooseContributionButton = new Button(composite, SWT.NONE);
		chooseContributionButton.addSelectionListener(adapter);
		chooseContributionButton.setText("Browse");
		chooseContributionButton.setData("name", "Choose_Contribution");
		final GridData gd_chooseContributionButton = new GridData(
				GridData.FILL_HORIZONTAL);
		chooseContributionButton.setLayoutData(gd_chooseContributionButton);

		final Label patternLabel = new Label(composite, SWT.NONE);
		patternLabel.setText("Pattern Name:");
		patternFileText = new Text(composite, SWT.BORDER);
		final GridData gd_patternToDisableText = new GridData(275, SWT.DEFAULT);
		patternFileText.setLayoutData(gd_patternToDisableText);
		patternFileText.addModifyListener(adapter);
		// MUST do this via browse
		patternFileText.setEditable(false);

		browsePatternFilesButton = new Button(composite, SWT.NONE);
		browsePatternFilesButton.addSelectionListener(adapter);
		browsePatternFilesButton.setText("Browse");
		browsePatternFilesButton.setData("name", "Browse_Pattern_Files");
		final GridData gd_browsePatternsButton = new GridData(
				GridData.FILL_HORIZONTAL);
		browsePatternFilesButton.setLayoutData(gd_browsePatternsButton);

		final Label validatorLabel = new Label(composite, SWT.NONE);
		validatorLabel.setText("Validator Class:");
		validatorClassText = new Text(composite, SWT.BORDER);
		final GridData gd_validatorClassText = new GridData(275, SWT.DEFAULT);
		validatorClassText.setLayoutData(gd_validatorClassText);
		validatorClassText.addModifyListener(adapter);
		// MUST do this via browse
		validatorClassText.setEditable(false);

		browseValidatorButton = new Button(composite, SWT.NONE);
		browseValidatorButton.addSelectionListener(adapter);
		browseValidatorButton.setText("Browse");
		browseValidatorButton.setData("name", "Browse_Validators");
		final GridData gd_browseValidatorButton = new GridData(
				GridData.FILL_HORIZONTAL);
		browseValidatorButton.setLayoutData(gd_browseValidatorButton);

		setControl(composite);
		new Label(composite, SWT.NONE);

		new TabFolder(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
	}

	public void handleWidgetSelected(SelectionEvent e) {
		if (e.getSource() == chooseContributionButton) {
			chooseContributerButtonPressed();
		} else if (e.getSource() == browseValidatorButton) {
			browseClassButtonPressed(getContributerSelection(),
					validatorClassText, "Select the Validator Class");
		} else if (e.getSource() == browsePatternFilesButton) {
			browseFilesButtonPressed(patternFileText, "Select the Pattern File");

		}
		updatePageComplete();
	}

	public void handleModifyText(ModifyEvent e) {
		// Should not really be called?
		// updatePageComplete();
	}

	protected void updatePageComplete() {

		if (getContributerSelection() == null) {
			// Need to check the contents of the Text for a valid entry
			setErrorMessage("Contributer must be specified");
			browsePatternFilesButton.setEnabled(false);
			browseValidatorButton.setEnabled(false);
			return;
		}
		browsePatternFilesButton.setEnabled(true);
		browseValidatorButton.setEnabled(true);

		// See if we have specified a valid one
		if (getPatternFile().equals("")) {
			// Need to check the contents of the Text for a valid entry
			setErrorMessage("Pattern File must be specified");
			return;
		}
		// And that it exists!
		IResource res = (IResource) getContributerSelection().getAdapter(
				IResource.class);
		IProject contProject = null;
		if (res != null) {
			contProject = (IProject) res.getProject();
			IResource patternResource = contProject
					.findMember(getPatternFile());
			if (!patternResource.exists()) {
				setErrorMessage("Pattern File does not exist");
				return;
			}

		}
		// And contains a pattern?

		// And The validator this must be a class that correctly implements the
		// stated interface?
		String V_CLASS = "IPatternBasedCreationValidator";
		if (getValidatorClass().equals("")) {
			setErrorMessage(null);
			setMessage("Validator Class is not set", 3);
		} else {
			try {
				boolean goodOne = false;
				String[] interfaces = getClassType().getSuperInterfaceNames();
				for (String itf : interfaces) {
					if (itf.equals(V_CLASS)) {
						goodOne = true;
					}
				}
				if (!goodOne) {
					setErrorMessage("Validator class may not implement "
							+ V_CLASS);
					return;
				}
			} catch (Exception j) {
				setErrorMessage("Unable to interpret Validator class");
				return;
			}
		}

		setErrorMessage(null);
		setMessage("Press 'Finish' to add the pattern contribution");
		setPageComplete(true);

	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		setMessage("Specify details of the pattern to be added");
		setPageComplete(false);
	}

	public String getPatternFile() {
		return patternFileText.getText().trim();
	}

	public String getValidatorClass() {
		return validatorClassText.getText().trim();
	}

}
