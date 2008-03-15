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
package org.eclipse.tigerstripe.workbench.eclipse.wizards.m0Generator;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
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

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (mpe).
 */

public class NewM0GeneratorProjectWizardPage extends WizardPage {

	public class NewProjectDetails {

		public String projectName;

		public String projectDirectory;

		public String getProjectName() {
			return this.projectName;
		}

		public String getProjectDirectory() {
			return this.projectDirectory;
		}
	}

	private Text projectNameText;

	private Text projectDirectoryText;

	private String projectDirectory;

	private Button inWorkspaceButton;

	private Button externalLocationButton;

	private Button directoryBrowseButton;

	private Label directoryLabel;

	private String externalDirectoryPath = "";

	private IPath defaultDirectoryPath;

	public NewProjectDetails getProjectNewProjectDetails() {
		NewProjectDetails result = new NewProjectDetails();
		result.projectName = projectNameText.getText().trim();
		result.projectDirectory = this.projectDirectory;
		return result;
	}

	/**
	 * Constructor for NewProjectWizardPage.
	 * 
	 * @param pageName
	 */
	public NewM0GeneratorProjectWizardPage(ISelection selection,
			ImageDescriptor image) {
		super("wizardPage");
		setTitle("New Tigerstripe M0-Level Generator Project");
		setImageDescriptor(image);

		setDescription("This wizard creates new Tigerstripe M0-Level Generator Project.");
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
							+ File.separator + projectNameText.getText();
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

		initialize();
		updatePage();
		setControl(container);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {

		defaultDirectoryPath = Platform.getLocation();
		projectDirectory = defaultDirectoryPath.toOSString();
		projectDirectoryText.setText(defaultDirectoryPath.toOSString());
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */

	private void handleBrowse() {

		DirectoryDialog dd = new DirectoryDialog(getShell());
		dd.setMessage("Select New File Container");
		if (dd.open() != null) {
			projectDirectoryText.setText(dd.getFilterPath());
			projectDirectory = dd.getFilterPath() + File.separator
					+ projectNameText.getText();
			setExternalDirectoryPath(dd.getFilterPath());
		}
	}

	/**
	 * 
	 * @return
	 */
	public IProject getProjectHandle() {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(
				projectNameText.getText().trim());
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

	private void updatePage() {

		String message = null;

		boolean specifyDirectory = false;
		if (inWorkspaceButton.getSelection()) {
			projectDirectoryText.setText(defaultDirectoryPath.toOSString()
					+ File.separator + projectNameText.getText());
			specifyDirectory = false;
		} else if (externalLocationButton.getSelection()) {
			specifyDirectory = true;
			projectDirectoryText.setText(getExternalDirectoryPath());
			// projectDirectory = projectDirectoryText.getText() +
			// File.separator + projectNameText.getText() ;
			// projectDirectory = projectDirectoryText.getText() ;
			if (getExternalDirectoryPath().indexOf(projectNameText.getText()) > 0) {
				setExternalDirectoryPath("");
			}
		}
		projectDirectoryText.setEnabled(specifyDirectory);
		directoryBrowseButton.setEnabled(specifyDirectory);
		directoryLabel.setEnabled(specifyDirectory);

		if ("".equals(projectNameText.getText().trim())) {
			setErrorMessage("Valid project name required");
			setPageComplete(false);
			return;
		}

		// check for duplicate Project name
		for (IProject proj : ResourcesPlugin.getWorkspace().getRoot()
				.getProjects()) {
			if (proj.getName().equals(projectNameText.getText().trim())) {
				setErrorMessage("Duplicate project name.");
				setPageComplete(false);
				return;
			}
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