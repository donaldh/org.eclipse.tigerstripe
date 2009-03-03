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

public class AddAnnotationPackageLabelWizardPage extends AbstractWizardPage implements IWizardPage{


		
	private Text labelText;

	private Text ePackageURIText;
	//private Button browsePatternFilesButton;
	//private Text classText;
	//private Button browseClassesButton;
	private Button chooseContributionButton; 
	
	
	

	protected AddAnnotationPackageLabelWizardPage(String pageName, Shell shell, ISDKProvider provider) {
		super(pageName);
		this.shell = shell;
		this.provider = provider;
	}

	
	protected void init(IStructuredSelection selection){

	}
	
	
	
	@Override
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
		final GridData gd_chooseContributionButton = new GridData(GridData.FILL_HORIZONTAL);
		chooseContributionButton.setLayoutData(gd_chooseContributionButton);
		
		final Label patternLabel = new Label(composite, SWT.NONE);
		patternLabel.setText("Name:");
		labelText = new Text(composite, SWT.BORDER);
		final GridData gd_patternToDisableText = new GridData(275, SWT.DEFAULT);
		labelText.setLayoutData(gd_patternToDisableText);
		labelText.addModifyListener(adapter);
		// MUST do this via browse
		labelText.setEditable(true);
		
		new Label(composite, SWT.NONE);
				
		
		final Label ePackageURILabel = new Label(composite, SWT.NONE);
		ePackageURILabel.setText("ePackageURI:");
		ePackageURIText = new Text(composite, SWT.BORDER);
		//final GridData gd_patternToDisableText = new GridData(275, SWT.DEFAULT);
		ePackageURIText.setLayoutData(gd_patternToDisableText);
		ePackageURIText.addModifyListener(adapter);
		// MUST do this via browse
		ePackageURIText.setEditable(true);
		
		new Label(composite, SWT.NONE);
		
		setControl(composite);
	}

	public void handleWidgetSelected(SelectionEvent e) {
		if (e.getSource() == chooseContributionButton) {
			chooseContributerButtonPressed();
		}
		updatePageComplete();
	}
	
	public void handleModifyText(ModifyEvent e){
		// Should not really be called?
		//	updatePageComplete();
	}
	
	
	protected void updatePageComplete(){
		
		
		if (getContributerSelection()== null){
			// Need to check the contents of the Text for a valid entry
			setErrorMessage("Contributer must be specified");
			
			//browseClassesButton.setEnabled(false);
			return;
		}
		//browseClassesButton.setEnabled(true);
		
	
		
		setErrorMessage(null);	
		setMessage("Press 'Finish' to add the Annotation Package Label contribution");
		setPageComplete(true);
		
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		setMessage("Specify details of the Annotation Package Label to be added");
		setPageComplete(false);
	}

	
	public String getAnnotationPackageLabel() {
		return labelText.getText().trim();
	}


	public String getEPackageURI() {
		return ePackageURIText.getText().trim();
	}




	
}
