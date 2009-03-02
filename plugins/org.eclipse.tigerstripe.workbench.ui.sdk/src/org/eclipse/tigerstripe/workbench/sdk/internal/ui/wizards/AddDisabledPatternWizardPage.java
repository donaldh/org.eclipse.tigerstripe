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
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.sdk.internal.ISDKProvider;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.PatternFileContribution;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.dialogs.SelectContributionDialog;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.dialogs.SelectPatternDialog;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;

public class AddDisabledPatternWizardPage extends AbstractWizardPage implements IWizardPage{


		
	private Text patternToDisableText;
	private Button browsePatternsButton; 
	private Button chooseContributionButton; 
	private PatternFileContribution selection = null; 
	
	protected AddDisabledPatternWizardPage(String pageName, Shell shell, ISDKProvider provider) {
		super(pageName);
		this.shell = shell;
		this.provider = provider;
	}

	
	protected void init(IStructuredSelection selection){

	}
	
	protected void initContents(){
		patternToDisableText.setText("Pattern Name");
		contributerText.setText("Contributer");
	}
	
	
	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		
		WizardPageListener adapter = new WizardPageListener();
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		composite.setLayout(gridLayout);

		final Label patternLabel = new Label(composite, SWT.NONE);
		patternLabel.setText("Pattern Name:");
		patternToDisableText = new Text(composite, SWT.BORDER);
		final GridData gd_patternToDisableText = new GridData(275, SWT.DEFAULT);
		patternToDisableText.setLayoutData(gd_patternToDisableText);
		patternToDisableText.addModifyListener(adapter);
		// MUST do this via browse
		patternToDisableText.setEditable(false);
		
		browsePatternsButton = new Button(composite, SWT.NONE);
		browsePatternsButton.addSelectionListener(adapter);
		browsePatternsButton.setText("Browse");
		browsePatternsButton.setData("name", "Browse_Patterns");
		final GridData gd_browsePatternsButton = new GridData(GridData.FILL_HORIZONTAL);
		browsePatternsButton.setLayoutData(gd_browsePatternsButton);
				
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
		final GridData gd_chooseContributionButton = new GridData(GridData.FILL_HORIZONTAL);
		chooseContributionButton.setLayoutData(gd_chooseContributionButton);

		initContents();
		setControl(composite);
		updatePageComplete();
	}

	public void handleWidgetSelected(SelectionEvent e) {
		if (e.getSource() == chooseContributionButton) {
			chooseContributerButtonPressed();
		} else if (e.getSource() == browsePatternsButton){
			browsePatternsButtonPressed();
		}
		updatePageComplete();
	}
	
	public void handleModifyText(ModifyEvent e){
		// Should not really be called?
		//	updatePageComplete();
	}
	
	private void browsePatternsButtonPressed(){
		try {
			SelectContributionDialog dialog = new SelectPatternDialog(
					this.shell,
					provider);
			dialog.setTitle("Pattern Name Selection");
			dialog.setMessage("Enter a filter (* = any number of characters)"
					+ " or an empty string for no filtering: ");

			PatternFileContribution patternToDisable = (PatternFileContribution) dialog.browseAvailableContributions();
			if (patternToDisable != null){
				setSelection(patternToDisable);
				updatePageComplete();
			}

		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	
	protected void updatePageComplete(){
		
		// See if we have specified a valid one
		if (getSelection() == null){
			// Need to check the contents of the Text for a valid entry
			setErrorMessage("Pattern must be specified");	
		}
		
		if (getContributerSelection()== null){
			// Need to check the contents of the Text for a valid entry
			setErrorMessage("Contributer must be specified");	
		}
		
		setErrorMessage(null);	
		setMessage("Press 'Finish' to disable the pattern");
		setPageComplete(true);
		
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		setMessage("Select the pattern to be disabled");
		setPageComplete(false);
	}

	
	public String getPatternToDisable() {
		return patternToDisableText.getText().trim();
	}

	

	public String getContribution() {
		return contributerText.getText().trim();
	}


	public PatternFileContribution getSelection() {
		return selection;
	}


	public void setSelection(PatternFileContribution selection) {
		this.selection = selection;
		patternToDisableText.setText(provider.getPattern(selection.getContributor(), selection.getFileName()).getName());
	}


	
}
