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
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.sdk.internal.ISDKProvider;

public class AddArtifactMetadataWizardPage extends AbstractWizardPage implements IWizardPage{


	private Text nameText;
	private Text labelText;
	private Text iconFileText;
	private Button browseIconFilesButton;
	private Text iconNewFileText;
	private Button browseIconNewFilesButton;
	private Text iconGreyFileText;
	private Button browseIconGreyFilesButton;
	
	private Button fieldsButton;
	private Button literalsButton;
	private Button methodsButton;
	private Button chooseContributionButton; 
	
	
	

	protected AddArtifactMetadataWizardPage(String pageName, Shell shell, ISDKProvider provider) {
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
		
		final Label nameLabel = new Label(composite, SWT.NONE);
		nameLabel.setText("Artifact Type :");
		nameText = new Text(composite, SWT.BORDER);
		final GridData gd_patternToDisableText = new GridData(275, SWT.DEFAULT);
		nameText.setLayoutData(gd_patternToDisableText);
		nameText.addModifyListener(adapter);
		// MUST do this via browse
		nameText.setEditable(true);
		
		new Label(composite, SWT.NONE);
		final Label labelLabel = new Label(composite, SWT.NONE);
		labelLabel.setText("User Label :");
		labelText = new Text(composite, SWT.BORDER);
		labelText.setLayoutData(gd_patternToDisableText);
		labelText.addModifyListener(adapter);
		labelText.setEditable(true);
		
		
		
		
		
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		fieldsButton = new Button(composite, SWT.CHECK);
		fieldsButton.setLayoutData(new GridData());
		fieldsButton.setText("Fields");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		literalsButton = new Button(composite, SWT.CHECK);
		literalsButton.setLayoutData(new GridData());
		literalsButton.setText("Literals");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		methodsButton = new Button(composite, SWT.CHECK);
		methodsButton.setLayoutData(new GridData());
		methodsButton.setText("Methods");
		new Label(composite, SWT.NONE);

		
		final Label iconLabel = new Label(composite, SWT.NONE);
		iconLabel.setText("Icon:");
		iconFileText = new Text(composite, SWT.BORDER);
		iconFileText.setLayoutData(gd_patternToDisableText);
		iconFileText.addModifyListener(adapter);
		// MUST do this via browse
		iconFileText.setEditable(false);
		
		browseIconFilesButton = new Button(composite, SWT.NONE);
		browseIconFilesButton.addSelectionListener(adapter);
		browseIconFilesButton.setText("Browse");
		browseIconFilesButton.setData("name", "Browse_Icon_Files");
		final GridData gd_browsePatternsButton = new GridData(GridData.FILL_HORIZONTAL);
		browseIconFilesButton.setLayoutData(gd_browsePatternsButton);

		
		final Label iconNewLabel = new Label(composite, SWT.NONE);
		iconNewLabel.setText("Icon NEW:");
		iconNewFileText = new Text(composite, SWT.BORDER);
		iconNewFileText.setLayoutData(gd_patternToDisableText);
		iconNewFileText.addModifyListener(adapter);
		// MUST do this via browse
		iconNewFileText.setEditable(false);
		
		browseIconNewFilesButton = new Button(composite, SWT.NONE);
		browseIconNewFilesButton.addSelectionListener(adapter);
		browseIconNewFilesButton.setText("Browse");
		browseIconNewFilesButton.setData("name", "Browse_Icon_Files");
		browseIconNewFilesButton.setLayoutData(gd_browsePatternsButton);

		
		final Label iconGreyLabel = new Label(composite, SWT.NONE);
		iconGreyLabel.setText("Icon GREY:");
		iconGreyFileText = new Text(composite, SWT.BORDER);
		iconGreyFileText.setLayoutData(gd_patternToDisableText);
		iconGreyFileText.addModifyListener(adapter);
		// MUST do this via browse
		iconGreyFileText.setEditable(false);
		
		browseIconGreyFilesButton = new Button(composite, SWT.NONE);
		browseIconGreyFilesButton.addSelectionListener(adapter);
		browseIconGreyFilesButton.setText("Browse");
		browseIconGreyFilesButton.setData("name", "Browse_Icon_Files");
		browseIconGreyFilesButton.setLayoutData(gd_browsePatternsButton);

		
		
		
		setControl(composite);
	}

	public void handleWidgetSelected(SelectionEvent e) {
		if (e.getSource() == chooseContributionButton) {
			chooseContributerButtonPressed();
		} else if (e.getSource() == browseIconFilesButton){
			browseFilesButtonPressed(iconFileText,"Select the Icon File");
		} else if (e.getSource() == browseIconNewFilesButton){
			browseFilesButtonPressed(iconNewFileText,"Select the Icon NEW File");
		} else if (e.getSource() == browseIconGreyFilesButton){
			browseFilesButtonPressed(iconGreyFileText,"Select the Icon GREY File");
		}
		updatePageComplete();
	}
	
	public void handleModifyText(ModifyEvent e){

			updatePageComplete();
	}
	
	
	protected void updatePageComplete(){
		
		
		if (getContributerSelection()== null){
			// Need to check the contents of the Text for a valid entry
			setErrorMessage("Contributer must be specified");
			browseIconFilesButton.setEnabled(false);
			browseIconGreyFilesButton.setEnabled(false);
			browseIconNewFilesButton.setEnabled(false);
			return;
		}
		browseIconFilesButton.setEnabled(true);
		browseIconGreyFilesButton.setEnabled(true);
		browseIconNewFilesButton.setEnabled(true);
		
		// See if we have specified a valid name
		if (getName().equals("")){
			// Need to check the contents of the Text for a valid entry
			setErrorMessage("Name must be specified");
			return;
		}
		
		
		// See if we have specified a valid one
		if (getIconFile().equals("")){
			// Need to check the contents of the Text for a valid entry
			setMessage("Icon File not specified",3);

		}
		// And that it exists!
		IResource res = (IResource) getContributerSelection().getAdapter(IResource.class);
		IProject contProject = null;
		if (res != null){
			contProject = (IProject) res.getProject();
			IResource iconResource = contProject.findMember(getIconFile());
			if (!iconResource.exists()){
				setErrorMessage("Icon File does not exist");
				return;
			}
			
		}
		
		// See if we have specified a valid one
		if (getIconNewFile().equals("")){
			// Need to check the contents of the Text for a valid entry
			setMessage("Icon NEW File not specified", 3);
		}
		// And that it exists!

		if (res != null){
			contProject = (IProject) res.getProject();
			IResource iconResource = contProject.findMember(getIconNewFile());
			if (!iconResource.exists()){
				setErrorMessage("Icon NEW File does not exist");
				return;
			}
			
		}
		
		// See if we have specified a valid one
		if (getIconGreyFile().equals("")){
			// Need to check the contents of the Text for a valid entry
			setMessage("Icon GREY File not specified",3);

		}
		// And that it exists!

		if (res != null){
			contProject = (IProject) res.getProject();
			IResource iconResource = contProject.findMember(getIconGreyFile());
			if (!iconResource.exists()){
				setErrorMessage("Icon GREY File does not exist");
				return;
			}
			
		}
		
		
		
		
		setErrorMessage(null);
		setMessage("Press 'Finish' to add the icon contribution");
		setPageComplete(true);
		
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		setMessage("Specify details of the icon to be added");
		setPageComplete(false);
	}

	public String getName() {
		return nameText.getText().trim();
	}
	
	public String getUserLabel() {
		return labelText.getText().trim();
	}
	
	public String getIconFile() {
		return iconFileText.getText().trim();
	}

	public String getIconNewFile() {
		return iconNewFileText.getText().trim();
	}
	
	public String getIconGreyFile() {
		return iconGreyFileText.getText().trim();
	}

	public boolean getHasFields() {
		return fieldsButton.getSelection();
	}
	
	public boolean getHasLiterals() {
		return literalsButton.getSelection();
	}
	public boolean getHasMethods() {
		return methodsButton.getSelection();
	}
}
