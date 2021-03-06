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

public class AddDecoratorWizardPage extends AbstractWizardPage implements
		IWizardPage {

	// private Button browsePatternFilesButton;
	private Text classText;
	private Button browseClassesButton;
	private Button chooseContributionButton;

	protected AddDecoratorWizardPage(String pageName, Shell shell,
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

		final Label validatorLabel = new Label(composite, SWT.NONE);
		validatorLabel.setText("Decorator Class:");
		classText = new Text(composite, SWT.BORDER);
		final GridData gd_validatorClassText = new GridData(275, SWT.DEFAULT);
		classText.setLayoutData(gd_validatorClassText);
		classText.addModifyListener(adapter);
		// MUST do this via browse
		classText.setEditable(false);

		browseClassesButton = new Button(composite, SWT.NONE);
		browseClassesButton.addSelectionListener(adapter);
		browseClassesButton.setText("Browse");
		browseClassesButton.setData("name", "Browse_Decorators");
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
					"Select the Audit Class");

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

			browseClassesButton.setEnabled(false);
			return;
		}
		browseClassesButton.setEnabled(true);

		// And The class this must be a class that correctly implements the
		// stated interface?
		String V_CLASS = "Decorator";
		if (getDecoratorClass().equals("")) {
			setErrorMessage("Decorator Class is not set");

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
					setErrorMessage("Decorator class may not implement "
							+ V_CLASS);
					return;
				}
			} catch (Exception j) {
				setErrorMessage("Unable to interpret Decorator class");
				return;
			}
		}

		setErrorMessage(null);
		setMessage("Press 'Finish' to add the Decorator contribution");
		setPageComplete(true);

	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		setMessage("Specify details of the Decorator to be added");
		setPageComplete(false);
	}

	public String getDecoratorClass() {
		return classText.getText().trim();
	}

}
