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
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.sdk.internal.ISDKProvider;

public class AddComponentIconProviderWizardPage extends AbstractWizardPage
		implements IWizardPage {

	private Text nameText;
	// private Button browsePatternFilesButton;
	private Text classText;
	private Button browseClassesButton;
	private Button chooseContributionButton;

	protected AddComponentIconProviderWizardPage(String pageName, Shell shell,
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
		patternLabel.setText("Component Type:");
		nameText = new Text(composite, SWT.BORDER);
		final GridData gd_patternToDisableText = new GridData(275, SWT.DEFAULT);
		nameText.setLayoutData(gd_patternToDisableText);
		nameText.addModifyListener(adapter);
		// MUST do this via browse
		nameText.setEditable(true);

		new Label(composite, SWT.NONE);

		final Label validatorLabel = new Label(composite, SWT.NONE);
		validatorLabel.setText("Provider Class:");
		classText = new Text(composite, SWT.BORDER);
		final GridData gd_validatorClassText = new GridData(275, SWT.DEFAULT);
		classText.setLayoutData(gd_validatorClassText);
		classText.addModifyListener(adapter);
		// MUST do this via browse
		classText.setEditable(false);

		browseClassesButton = new Button(composite, SWT.NONE);
		browseClassesButton.addSelectionListener(adapter);
		browseClassesButton.setText("Browse");
		browseClassesButton.setData("name", "Browse_Validators");
		final GridData gd_browseValidatorButton = new GridData(
				GridData.FILL_HORIZONTAL);
		browseClassesButton.setLayoutData(gd_browseValidatorButton);

		setControl(composite);
	}

	public void handleWidgetSelected(SelectionEvent e) {
		if (e.getSource() == chooseContributionButton) {
			chooseContributerButtonPressed();
		} else if (e.getSource() == browseClassesButton) {
			browseClassButtonPressed(getContributerSelection(), classText,
					"Select the Icon Provider Class");

		}
		updatePageComplete();
	}

	public void handleModifyText(ModifyEvent e) {
		updatePageComplete();
	}

	protected void updatePageComplete() {

		if (getContributerSelection() == null) {
			// Need to check the contents of the Text for a valid entry
			setErrorMessage("Contributer must be specified");

			browseClassesButton.setEnabled(false);
			return;
		}
		browseClassesButton.setEnabled(true);

		if (getArtifactType().equals("")) {
			setErrorMessage("Artifact Type is not set");
			return;
		}

		// And The class this must be a class that correctly implements the
		// stated interface?
		String V_CLASS = "IModelComponentIconProvider";
		if (getProviderClass().equals("")) {
			setErrorMessage("Icon Provider Class is not set");
			return;
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
					setErrorMessage("Icon Provider class may not implement "
							+ V_CLASS);
					return;
				}
			} catch (Exception j) {
				setErrorMessage("Unable to interpret Icon Provider class");
				return;
			}
		}

		setErrorMessage(null);
		setMessage("Press 'Finish' to add the Icon Provider contribution");
		setPageComplete(true);

	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		setMessage("Specify details of the Icon Provider class to be added");
		setPageComplete(false);
	}

	public String getArtifactType() {
		return nameText.getText().trim();
	}

	public String getProviderClass() {
		return classText.getText().trim();
	}

}
