/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ui.wizards;

import java.io.File;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.TSRuntimeBasedWizardPage;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

public class UML2ImportDetailsWizardPage extends TSRuntimeBasedWizardPage {

	private static final String PAGE_NAME = "UML2ImportDetailsWizardPage";
	
	private IDialogSettings wizardSettings;
	
	/** The location of the referenced profiles */
	protected StringButtonDialogField fProfilesDir;
	protected StringButtonDialogField modelFile;
	
	public Button tsButton;
	private Text tsProjectText;
	private ITigerstripeModelProject tsProject;
	
	/**
	 * Creates a new <code>NewPackageWizardPage</code>
	 */
	public UML2ImportDetailsWizardPage(IDialogSettings wizardSettings) {
		super(PAGE_NAME);

		setTitle("Import Details");
		setDescription("Please enter the details of the model to be imported.");
		this.wizardSettings = wizardSettings;
	}
	
	/**
	 * Perform any required update based on the runtime context
	 * 
	 */
	@Override
	protected void initFromContext() {
	}
	
	public void createControl(Composite parent) {
		
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);

		int nColumns = 4;

		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		createTSControls(composite, nColumns);
		createFileControls(composite, nColumns);
		
		setControl(composite);
		Dialog.applyDialogFont(composite);

		initContents();
		updatePageComplete();
	}
	
	protected void createTSControls(Composite composite, int nColumns) {


		Label label = new Label(composite, SWT.NULL);
		label.setText("&TS Project Name:");

		tsProjectText = new Text(composite, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		tsProjectText.setLayoutData(gd);
		tsProjectText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updatePageComplete();
			}
		});

		tsButton = new Button(composite, SWT.PUSH);
		tsButton.setText("Browse...");
		tsButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse(tsButton);
			}
		});

		DialogField.createEmptySpace(composite);
	}
	
	protected void createFileControls(Composite composite, int nColumns) {

		UML2ImportFieldsAdapter adapter = new UML2ImportFieldsAdapter();
		modelFile = new StringButtonDialogField(adapter);
		modelFile.setLabelText("Model for import.");
		modelFile.setButtonLabel("Browse");
		modelFile.setDialogFieldListener(adapter);
		
		fProfilesDir = new StringButtonDialogField(adapter);
		fProfilesDir.setLabelText("Profiles Dir");
		fProfilesDir.setButtonLabel("Browse");
		fProfilesDir.setDialogFieldListener(adapter);
		
		modelFile.doFillIntoGrid(composite, nColumns);
		Text text = modelFile.getTextControl(null);
		LayoutUtil.setHorizontalGrabbing(text);
		
		fProfilesDir.doFillIntoGrid(composite, nColumns);
		text = fProfilesDir.getTextControl(null);
		LayoutUtil.setHorizontalGrabbing(text);
			
	}
	
	public void initContents(){
		if (wizardSettings.get("TSProject") != null){
			tsProjectText.setText(wizardSettings.get("TSProject"));
		} 
		if (wizardSettings.get("ModelFile") != null){
			modelFile.setText(wizardSettings.get("ModelFile"));
		}
		if (wizardSettings.get("ProfileDir") != null){
			fProfilesDir.setText(wizardSettings.get("ProfileDir"));
		}
		
	}

	
	// -------- TypeFieldsAdapter --------

	private class UML2ImportFieldsAdapter implements IStringButtonAdapter,
			IDialogFieldListener, IListAdapter {

		// -------- IStringButtonAdapter
		public void changeControlPressed(DialogField field) {
			uml2ImportPageChangeControlPressed(field);
		}

		// -------- IListAdapter
		public void customButtonPressed(ListDialogField field, int index) {
		}

		public void selectionChanged(ListDialogField field) {
		}

		// -------- IDialogFieldListener
		public void dialogFieldChanged(DialogField field) {
			uml2ImportPageDialogFieldChanged(field);
		}

		public void doubleClicked(ListDialogField field) {
		}
	}

	private void uml2ImportPageChangeControlPressed(DialogField field) {

		if (field == modelFile) {
			final FileDialog dialog = new FileDialog(getShell());
			String fileName = modelFile.getText().trim();

			if (fileName.length() > 0) {
				final File path = new File(fileName);
				if (path.exists())
					dialog.setFilterPath(fileName);
			}

			final String selectedFile = dialog.open();
			if (selectedFile != null) {
				modelFile.setText(selectedFile);
			}
		} else if (field == fProfilesDir) {
			final DirectoryDialog dialog = new DirectoryDialog(getShell());
			String fileName = fProfilesDir.getText().trim();

			if (fileName.length() > 0) {
				final File path = new File(fileName);
				if (path.exists())
					dialog.setFilterPath(fileName);
			}

			final String selectedFile = dialog.open();
			if (selectedFile != null) {
				File selDir = new File(selectedFile);
				if (selDir.exists() && selDir.isDirectory()){
					fProfilesDir.setText(selectedFile);
				}
			}
		} 		updatePageComplete();
	}

	/*
	 * A field on the type has changed. The fields' status and all dependent
	 * status are updated.
	 */
	private void uml2ImportPageDialogFieldChanged(DialogField field) {

	}
	
	private void handleBrowse(Button buttonPressed) {

		if (buttonPressed.equals(tsButton)){
			ContainerSelectionDialog dialog = new ContainerSelectionDialog(
					getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
					"Select tigerstripe project");
			if (dialog.open() == ContainerSelectionDialog.OK) {
				Object[] result = dialog.getResult();
				if (result.length == 1) {
					tsProjectText.setText(((Path) result[0]).toString());
				}
			}
		}
	}
	
	protected void updatePageComplete() {

		
		
		if (getTigerstripeName().length() == 0) {
			setErrorMessage("Tigerstripe project must be specified");
			setPageComplete(false);
			return;
		}
		// TODO check thats this is really a Tigerstripe Project!
		// This might not be the best place to do this!
		IResource tsContainer = ResourcesPlugin.getWorkspace().getRoot()
	     	.findMember(new Path(getTigerstripeName()));
		if (tsContainer == null){
			setErrorMessage("A valid Tigerstripe project must be specified");
			setPageComplete(false);
			return;
		}
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		
		IPath rootpath = root.getLocation();
		IPath fullProjectPath = rootpath.append(tsContainer.getFullPath());
		
		try {		
			 IAbstractTigerstripeProject project = TigerstripeCore.findProject(fullProjectPath);
			 if (project instanceof ITigerstripeModelProject) {
				 tsProject = (ITigerstripeModelProject)  project;
			 } else {
				 throw new TigerstripeException("");
			 }
		} catch (TigerstripeException t){
			setErrorMessage("Tigerstripe project must exist");
			setPageComplete(false);
			return;
		}
		
		if (!tsContainer.isAccessible()) {
			setErrorMessage("TS Project must be writable");
			setPageComplete(false);
			return;
		}
		
		
		if ("".equals(modelFile.getText().trim())) {
			setErrorMessage("Model file name must be set.");
			setPageComplete(false);
			return;
		}

		File file = new File(modelFile.getText().trim());

		if (!file.exists()) {
			setErrorMessage("Model file is invalid.");
			setPageComplete(false);
			return;
		}
		
		
		if ("".equals(fProfilesDir.getText().trim())) {
			setErrorMessage("Profiles dir must be set.");
			setPageComplete(false);
			return;
		}

		File dir = new File(fProfilesDir.getText().trim());

		if (!dir.exists() || !dir.isDirectory()) {
			setErrorMessage("Profiles dir is invalid");
			setPageComplete(false);
			return;
		} 
		
		setErrorMessage(null);		
		setMessage(null);
		setPageComplete(true);
	}
	
	public String getProfilesFilename() {
		return this.fProfilesDir.getText().trim();
	}

	public String getModelFilename() {
		return this.modelFile.getText().trim();
	}
	
	public String getTigerstripeName() {
		return this.tsProjectText.getText().trim();
	}

	public ITigerstripeModelProject getTsProject() {
		return this.tsProject; 
	}
	
}
