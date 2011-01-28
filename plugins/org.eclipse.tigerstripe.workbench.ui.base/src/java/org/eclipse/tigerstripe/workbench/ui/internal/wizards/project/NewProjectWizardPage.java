/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.project;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
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
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.preferences.GeneralPreferencePage;
import org.eclipse.tigerstripe.workbench.ui.internal.preferences.GenerationPreferencePage;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (mpe).
 */

public class NewProjectWizardPage extends WizardPage {

	protected Label defaultArtifactPackageLabel;

	protected Text defaultArtifactPackageText;

	protected Text projectNameText;

	protected Text projectDirectoryText;

	protected String projectDirectory;

	protected Button inWorkspaceButton;

	protected Button externalLocationButton;

	protected Button directoryBrowseButton;

	protected Label directoryLabel;

	protected String externalDirectoryPath = "";

	protected IPath defaultDirectoryPath;

	protected String getProjectName() {
		return projectNameText.getText().trim();
	}

	public NewProjectDetails getProjectNewProjectDetails() {

		Preferences store = EclipsePlugin.getDefault().getPluginPreferences();

		NewProjectDetails result = new NewProjectDetails();
		result.projectName = getProjectName();
		result.projectDirectory = this.projectDirectory;
		result.defaultArtifactPackage = getDefaultArtifactPackageText();
		// result.defaultInterfacePackage =
		// store.getString(GeneralPreferencePage.P_DEFAULTINTERFACEPACKAGE);
		result.outputDirectory = store
				.getString(GenerationPreferencePage.P_TARGETPATH);
		result.generateReport = store
				.getString(GenerationPreferencePage.P_GENERATEREPORT);
		result.logMessages = store
				.getString(GenerationPreferencePage.P_LOGMESSAGES);
		return result;
	}

	/**
	 * Constructor for NewProjectWizardPage.
	 * 
	 * @param pageName
	 */
	public NewProjectWizardPage(ISelection selection, ImageDescriptor image) {
		super("wizardPage");
		setTitle("New Tigerstripe Project");
		setImageDescriptor(image);

		setDescription("This wizard creates new Tigerstripe Project.");
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		projectNameText.setFocus();
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);

		Label label = new Label(container, SWT.NULL);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		label.setText("&Project Name:");

		projectNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		projectNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL));
		projectNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updatePage();
			}
		});

		Group projectLocation = new Group(container, SWT.NULL);
		projectLocation.setText("Location");
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		projectLocation.setLayout(gridLayout);
		final GridData locationGridData = new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL);
		locationGridData.horizontalSpan = 2;
		projectLocation.setLayoutData(locationGridData);

		inWorkspaceButton = new Button(projectLocation, SWT.RADIO);
		inWorkspaceButton.setText("Create project in workspace");
		inWorkspaceButton.setSelection(true);
		GridData inWorkspaceGridData = new GridData(
				GridData.HORIZONTAL_ALIGN_FILL);
		inWorkspaceGridData.horizontalSpan = 3;
		inWorkspaceButton.setLayoutData(inWorkspaceGridData);
		inWorkspaceButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updatePage();
			}
		});

		externalLocationButton = new Button(projectLocation, SWT.RADIO);
		externalLocationButton.setText("Create project at external location");
		GridData externalLocationGridData = new GridData(
				GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		externalLocationGridData.horizontalSpan = 3;
		externalLocationButton.setLayoutData(externalLocationGridData);
		externalLocationButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updatePage();
			}
		});

		directoryLabel = new Label(projectLocation, SWT.NONE);
		// GridData gridData_1 = new GridData(GridData.FILL |
		// GridData.HORIZONTAL_ALIGN_END);
		// directoryLabel.setLayoutData(gridData_1);
		directoryLabel.setText("Directory:");

		projectDirectoryText = new Text(projectLocation, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL);
		projectDirectoryText.setLayoutData(gd);
		projectDirectoryText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setExternalDirectoryPath(projectDirectoryText.getText());
				if (externalLocationButton.getSelection()) {
					projectDirectory = getExternalDirectoryPath()
							+ File.separator + getProjectName();
				}
			}
		});

		directoryBrowseButton = new Button(projectLocation, SWT.NONE);
		directoryBrowseButton.setText("Browse...");
		gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		directoryBrowseButton.setLayoutData(gd);
		// Fixed bug # 85 - Browse button was not doing anything
		directoryBrowseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});

		// Default Project Parameters
		createDefaultValuesGroup(container);

		Label desc = new Label(container, SWT.NULL);
		desc
				.setText("Please set the target profiles for this project in the tigerstripe.xml project descriptor.");
		GridData gd1 = new GridData(GridData.FILL);
		gd1.horizontalSpan = 2;
		desc.setLayoutData(gd1);

		initialize();

		projectDirectoryText.setText(defaultDirectoryPath.toOSString());

		updatePage();
		setControl(container);
	}

	protected String getDefaultArtifactPackageText() {
		return this.defaultArtifactPackageText.getText().trim();
	}

	public void createDefaultValuesGroup(Composite parent) {

		Preferences store = EclipsePlugin.getDefault().getPluginPreferences();

		Group projectNature = new Group(parent, SWT.NULL);
		projectNature.setText("Project Defaults");
		final GridLayout natureGridLayout = new GridLayout();
		natureGridLayout.numColumns = 2;
		projectNature.setLayout(natureGridLayout);
		final GridData natureGridData = new GridData(GridData.FILL_HORIZONTAL);
		natureGridData.horizontalSpan = 2;
		projectNature.setLayoutData(natureGridData);

		defaultArtifactPackageLabel = new Label(projectNature, SWT.NONE);
		defaultArtifactPackageLabel.setLayoutData(new GridData(
				GridData.BEGINNING));
		defaultArtifactPackageLabel.setText("Artifacts Package:");

		defaultArtifactPackageText = new Text(projectNature, SWT.BORDER);
		defaultArtifactPackageText.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));

		if ("".equals(store.getString(GeneralPreferencePage.P_DEFAULTPACKAGE))
				|| store.getString(GeneralPreferencePage.P_DEFAULTPACKAGE) == null) {
			defaultArtifactPackageText.setText("com.mycompany");
		} else {
			defaultArtifactPackageText.setText(store
					.getString(GeneralPreferencePage.P_DEFAULTPACKAGE));
		}
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	protected void initialize() {

		defaultDirectoryPath = Platform.getLocation();
		projectDirectory = defaultDirectoryPath.toOSString();

		// outputDirectory.setText( defaultDirectoryPath.toOSString() +
		// File.separator + "target" + File.separator + "ts");
		// defaultInterfacePackage.setText( "javax.oss" );
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */

	public void handleBrowse() {

		DirectoryDialog dd = new DirectoryDialog(getShell());
		dd.setMessage("Select New File Container");
		if (dd.open() != null) {
			projectDirectoryText.setText(dd.getFilterPath());
			projectDirectory = dd.getFilterPath() + File.separator
					+ getProjectName();
			setExternalDirectoryPath(dd.getFilterPath());
		}
	}

	/**
	 * 
	 * @return
	 */
	public IProject getProjectHandle() {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(
				getProjectName());
	}

	/**
	 * Returns the current project location path as entered by the user, or its
	 * anticipated initial value. Note that if the default has been returned the
	 * path in a project description used to create a project should not be set.
	 * 
	 * @return the project location path or its anticipated initial value.
	 */
	public IPath getLocationPath() {
		return new Path(projectDirectory.trim());
	}

	/**
	 * Ensures that both text fields are set.
	 */

	public void updatePage() {

		String message = null;

		boolean specifyDirectory = false;
		if (inWorkspaceButton.getSelection()) {
			projectDirectoryText.setText(defaultDirectoryPath.toOSString()
					+ File.separator + getProjectName());
			specifyDirectory = false;
		} else if (externalLocationButton.getSelection()) {
			specifyDirectory = true;
			projectDirectoryText.setText(getExternalDirectoryPath());
			// projectDirectory = projectDirectoryText.getText() +
			// File.separator + projectNameText.getText() ;
			// projectDirectory = projectDirectoryText.getText() ;
			if (getExternalDirectoryPath().indexOf(getProjectName()) > 0) {
				setExternalDirectoryPath("");
			}
		}
		projectDirectoryText.setEnabled(specifyDirectory);
		directoryBrowseButton.setEnabled(specifyDirectory);
		directoryLabel.setEnabled(specifyDirectory);
		
		String projectName = getProjectName();
		if ("".equals(projectName)) {
			setErrorMessage("Valid project name required");
			setPageComplete(false);
			return;
		}
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IStatus validNameStatus = workspace.validateName(projectName, IResource.PROJECT);
        if (!validNameStatus.isOK()) {
            setErrorMessage(validNameStatus.getMessage());
            setPageComplete(false);
            return;
        }
		// check for duplicate Project name
        IProject projectHandle = workspace.getRoot().getProject(getProjectName());
		if (projectHandle.exists()) {
		    setErrorMessage("Duplicate project name.");
			setPageComplete(false);
			return;
		}

		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getExternalDirectoryPath() {
		return externalDirectoryPath;
	}

	public void setExternalDirectoryPath(String externalDirectoryPath) {
		this.externalDirectoryPath = externalDirectoryPath;
	}

}