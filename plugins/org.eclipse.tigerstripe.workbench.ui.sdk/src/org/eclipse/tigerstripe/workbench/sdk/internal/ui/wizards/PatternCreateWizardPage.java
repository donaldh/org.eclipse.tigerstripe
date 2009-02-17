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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.ui.wizards.NewElementWizardPage;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DependencyArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EnumArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EventArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.PackageArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.QueryArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.BrowseForArtifactDialog;

public class PatternCreateWizardPage extends NewElementWizardPage implements IWizardPage{

	private static IAbstractArtifact[] suitableTypes;
	
	public static IAbstractArtifact[] getSuitableTypes(){
		if (suitableTypes == null)
			loadSuitableTypes();
		return suitableTypes;
	}
		
	private static void loadSuitableTypes(){
		List<IAbstractArtifact> suitableModelsList = new ArrayList<IAbstractArtifact>();
		suitableModelsList.add(DatatypeArtifact.MODEL);
		suitableModelsList.add(EnumArtifact.MODEL);
		suitableModelsList.add(ManagedEntityArtifact.MODEL);
		suitableModelsList.add(AssociationClassArtifact.MODEL);
		suitableModelsList.add(ExceptionArtifact.MODEL);
		suitableModelsList.add(AssociationArtifact.MODEL);
		suitableModelsList.add(DependencyArtifact.MODEL);
		suitableModelsList.add(QueryArtifact.MODEL);
		suitableModelsList.add(EventArtifact.MODEL);
		suitableModelsList.add(UpdateProcedureArtifact.MODEL);
		suitableModelsList.add(SessionFacadeArtifact.MODEL);
		suitableModelsList.add(PackageArtifact.MODEL);

		suitableTypes = suitableModelsList.toArray( new IAbstractArtifact[0] );
	}
	
	
	private ITigerstripeModelProject modelProject = null;
	private IAbstractArtifact art = null;
	private Text targetDirectoryText;
	private Text targetFileNameText;
	private Button browseDirectoryButton; 
	private Text sourceArtifactText;
	private Button chooseArtifactButton; 
	private Text patternNameText;
	private Text uILabelText;
	private Text iconPathText;
	private Text indexText;
	private Text descriptionText;
	private File targetFile = null;
	private Button includeEndNamesButton;
//	private Button iconPathBrowseButton;
	private IDialogSettings settings;
	
	
	protected PatternCreateWizardPage(String pageName, IDialogSettings settings) {
		super(pageName);
		this.settings = settings;
	}

	
	protected void init(IStructuredSelection selection){
		IProject selectedProject = EclipsePlugin.getProjectInFocus();
		modelProject  = (ITigerstripeModelProject) selectedProject.getAdapter(ITigerstripeModelProject.class);
	}
	
	protected void initContents(){
		if (settings.get("targetDirectory") != null){
			targetDirectoryText.setText(settings.get("targetDirectory"));
		} else {
			targetDirectoryText.setText("Target Directory");
		}
		targetFileNameText.setText("newPattern.xml");
		sourceArtifactText.setText("Source Artifact");
		patternNameText.setText("NewPatternName");
		uILabelText.setText("My New Pattern");
		iconPathText.setText("icons/entity_new.gif");
	}
	
	
	private class PatternCreatePageListener implements ModifyListener,
	 SelectionListener {

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}

		public void widgetSelected(SelectionEvent e) {
			handleWidgetSelected(e);
		}


		public void modifyText(ModifyEvent e) {
			handleModifyText(e);
		}

		public void keyReleased(KeyEvent e) {
		}

	}
	
	
	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		
		PatternCreatePageListener adapter = new PatternCreatePageListener();
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		composite.setLayout(gridLayout);

		final Label fileDirectoryLabel = new Label(composite, SWT.NONE);
		fileDirectoryLabel.setText("Target Directory");
		targetDirectoryText = new Text(composite, SWT.BORDER);
		final GridData gd_targetDirectoryText = new GridData(275, SWT.DEFAULT);
		targetDirectoryText.setLayoutData(gd_targetDirectoryText);
		targetDirectoryText.addModifyListener(adapter);
		
		
		browseDirectoryButton = new Button(composite, SWT.NONE);
		browseDirectoryButton.addSelectionListener(adapter);
		browseDirectoryButton.setText("Browse");
		browseDirectoryButton.setData("name", "Browse_Directory");
		final GridData gd_browseDirectoryButton = new GridData(SWT.FILL, SWT.FILL, true, true);
		browseDirectoryButton.setLayoutData(gd_browseDirectoryButton);
		
		final Label fileNameLabel = new Label(composite, SWT.NONE);
		fileNameLabel.setText("File Name");

		targetFileNameText = new Text(composite, SWT.BORDER);
		final GridData gd_targetFileNameText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		targetFileNameText.setLayoutData(gd_targetFileNameText);
		targetFileNameText.addModifyListener(adapter);
		
		new Label(composite, SWT.NONE);
		
		
		final Label sourceArtifactLabel = new Label(composite, SWT.NONE);
		sourceArtifactLabel.setText("Source Artifact");
		sourceArtifactText = new Text(composite, SWT.BORDER);
		final GridData gd_sourceArtifactText = new GridData(275, SWT.DEFAULT);
		sourceArtifactText.setLayoutData(gd_sourceArtifactText);
		sourceArtifactText.addModifyListener(adapter);
		
		chooseArtifactButton = new Button(composite, SWT.NONE);
		chooseArtifactButton.addSelectionListener(adapter);
		chooseArtifactButton.setText("Browse");
		chooseArtifactButton.setData("name", "Choose_Artifact");
		final GridData gd_chooseArtifactButton = new GridData(SWT.FILL, SWT.FILL, true, true);
		chooseArtifactButton.setLayoutData(gd_chooseArtifactButton);

		final Label patternNameLabel = new Label(composite, SWT.NONE);
		patternNameLabel.setText("Pattern Name");

		patternNameText = new Text(composite, SWT.BORDER);
		final GridData gd_patternNameText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		patternNameText.setLayoutData(gd_patternNameText);
		patternNameText.addModifyListener(adapter);
		
		new Label(composite, SWT.NONE);

		final Label label = new Label(composite, SWT.NONE);
		label.setText("UI Label");

		uILabelText = new Text(composite, SWT.BORDER);
		final GridData gd_uILabelText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		uILabelText.setLayoutData(gd_uILabelText);
		uILabelText.addModifyListener(adapter);
		
		new Label(composite, SWT.NONE);
		
		new Label(composite, SWT.NONE);
		includeEndNamesButton = new Button(composite, SWT.CHECK);
		includeEndNamesButton.setText("Include End Names");
		includeEndNamesButton.setSelection(true);
		includeEndNamesButton.setEnabled(false);
		new Label(composite, SWT.NONE);
		final Label iconPathLabel = new Label(composite, SWT.NONE);
		final GridData gd_iconPathLabel = new GridData(SWT.FILL, SWT.TOP, false, true);
		iconPathLabel.setLayoutData(gd_iconPathLabel);
		iconPathLabel.setText("Icon Path");

		iconPathText = new Text(composite, SWT.BORDER);
		final GridData gd_iconPathText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		iconPathText.setLayoutData(gd_iconPathText);
		iconPathText.addModifyListener(adapter);
		

		new Label(composite, SWT.NONE);
//		iconPathBrowseButton = new Button(composite, SWT.NONE);
//		final GridData gd_browseButton = new GridData(SWT.FILL, SWT.FILL, true, true);
//		iconPathBrowseButton.setLayoutData(gd_browseButton);
//		iconPathBrowseButton.setText("Browse");
//		chooseArtifactButton.setData("name", "Browse_IconPath");
//		iconPathBrowseButton.addSelectionListener(adapter);

		final Label indexLabel = new Label(composite, SWT.NONE);
		indexLabel.setText("Index");

		indexText = new Text(composite, SWT.BORDER);
		final GridData gd_indexText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		indexText.setLayoutData(gd_indexText);
		indexText.addModifyListener(adapter);
		new Label(composite, SWT.NONE);

		final Label descriptionLabel = new Label(composite, SWT.NONE);
		descriptionLabel.setText("Description");

		descriptionText = new Text(composite, SWT.BORDER);
		final GridData gd_descriptionText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_descriptionText.heightHint = 84;
		descriptionText.setLayoutData(gd_descriptionText);
		descriptionText.addModifyListener(adapter);
		new Label(composite, SWT.NONE);
		
		
		initContents();
		setControl(composite);
		updatePageComplete();
	}

	public void handleWidgetSelected(SelectionEvent e) {
		if (e.getSource() == chooseArtifactButton) {
			artifactBrowseButtonPressed();
		} else if (e.getSource() == browseDirectoryButton){
			directoryBrowseButtonPressed();
		}
		updatePageComplete();
	}
	
	public void handleModifyText(ModifyEvent e){
			updatePageComplete();
	}
	
	private void directoryBrowseButtonPressed(){
		final DirectoryDialog dialog = new DirectoryDialog(getShell());
		String fileName = targetDirectoryText.getText().trim();

		if (fileName.length() > 0) {
			final File path = new File(fileName);
			if (path.exists())
				dialog.setFilterPath(fileName);
		}

		final String selectedFile = dialog.open();
		if (selectedFile != null) {
			File selDir = new File(selectedFile);
			if (selDir.exists() && selDir.isDirectory()) {
				targetDirectoryText.setText(selectedFile);
			}
		}
	}
	
	private void artifactBrowseButtonPressed(){
		// Get the current Selection and find it's project
		if (modelProject != null){
			try{
				BrowseForArtifactDialog dialog = new BrowseForArtifactDialog(modelProject,
						getSuitableTypes());
				dialog.setTitle("SourceArtifact");
				dialog.setMessage("Select the artifact" + " to be used as base for the pattern file.");
				dialog.setIncludePrimitiveTypes(false);

				AbstractArtifact[] artifacts = dialog.browseAvailableArtifacts(
						getShell(), Arrays
						.asList(new Object[] { }));
				if ( artifacts.length > 0){
					AbstractArtifact artifactChosen = artifacts[0];
					sourceArtifactText.setText(artifactChosen.getFullyQualifiedName());
					
				}
			} catch (Exception t){
				t.printStackTrace();
			}
		}
	}

	
	private void updatePageComplete(){
		
		if (includeEndNamesButton != null){
			includeEndNamesButton.setEnabled(false);
		}
		// Check all the fields contain valid entries
		// The artifact must be valid in the context of the project!
		if (modelProject == null ){
			setErrorMessage("Tigerstripe project must be selected");
			setPageComplete(false);

			return;
		}
		IArtifactManagerSession session = null;
		try {
			session = modelProject.getArtifactManagerSession();
		} catch (TigerstripeException t){
			setErrorMessage("Cannot read Tigerstripe project");
			setPageComplete(false);
			return;
		}
		
		if (session == null){
			setErrorMessage("Cannot read Tigerstripe project");
			setPageComplete(false);
			return;
		}
		
		if (sourceArtifactText == null){
			// probably means during init!
			setErrorMessage("Source Artifact is invalid");
			setPageComplete(false);
			return;
		}
		
		art = session.getArtifactByFullyQualifiedName(getSourceArtifactText(), false);
		if (art == null){
			setErrorMessage("Source Artifact is invalid");
			setPageComplete(false);
			return;
		}
		
		if (art instanceof IRelationship){
			includeEndNamesButton.setEnabled(true);
		}
		
		// Make sure our destination is also valid.
		if ("".equals(getTargetDirectoryText())) {
			setErrorMessage("Target Directory is invalid.");
			setPageComplete(false);
			return;
		}

		File dir = new File(getTargetDirectoryText());

		if (!dir.exists() || !dir.isDirectory()) {
			setErrorMessage("Target Directory is invalid.");
			setPageComplete(false);
			return;
		}
		
		
		if (getPatternNameText().equals("")){
			setErrorMessage("Name must be specified");
			setPageComplete(false);
			return;
		}
		
		if (getUILabelText().equals("")){
			setErrorMessage("UI Label must be specified");
			setPageComplete(false);
			return;
		}

		// Index is optional
		if (! getIndexText().equals("")){
			try {
				Integer index = Integer.parseInt(getIndexText());
			} catch (NumberFormatException n){
				setErrorMessage("Index must be an integer");
				setPageComplete(false);
				return;
			}
		}
		
		
		String targetFileName = getTargetFileNameText();
		if (! targetFileName.endsWith(".xml")){
			targetFileName = targetFileName+".xml";
		}
		
		
		File target = new File(dir+File.separator+targetFileName);
		
		if (target.exists() && !target.canWrite()){
			setErrorMessage("Cannot write to target file");
			setPageComplete(false);
			return;
		}
		
		if (target.exists()){
			setErrorMessage(null);
			setMessage("Target fail already exists", WARNING);
		}
		setTargetFile(target);
		
		setErrorMessage(null);	
		setMessage("Press 'Finish' to create the pattern file");
		setPageComplete(true);
		
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		setMessage("Enter details for the pattern file");
		setPageComplete(false);
	}

	public ITigerstripeModelProject getModelProject() {
		return modelProject;
	}

	public String getTargetDirectoryText() {
		return targetDirectoryText.getText().trim();
	}

	public String getTargetFileNameText() {
		return targetFileNameText.getText().trim();
	}

	public String getSourceArtifactText() {
		return sourceArtifactText.getText().trim();
	}

	public String getPatternNameText() {
		return patternNameText.getText().trim();
	}

	public String getUILabelText() {
		return uILabelText.getText().trim();
	}

	public String getIconPathText() {
		return iconPathText.getText().trim();
	}

	public String getIndexText() {
		return indexText.getText().trim();
	}

	public String getDescriptionText() {
		return descriptionText.getText().trim();
	}

	public IAbstractArtifact getSourceArtifact() {
		return art;
	}

	public void setTargetFile(File targetFile) {
		this.targetFile = targetFile;
	}

	public File getTargetFile() {
		return targetFile;
	}
	
	public boolean getInlcudeEndNames(){
		// This will be irrelevant for a non-relation artifact
		return includeEndNamesButton.getSelection();
	}
	
}
