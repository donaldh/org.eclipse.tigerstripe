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
package org.eclipse.tigerstripe.eclipse.wizards.imports.uml2;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogFieldGroup;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.api.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.eclipse.wizards.artifacts.TSRuntimeBasedWizardPage;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ImportUML2ProfileWizardPage extends TSRuntimeBasedWizardPage {

	public final static String PAGE_NAME = "ImportProfilesWizardPage";

	protected StringButtonDialogField profileFile;

	protected StringButtonDialogField fProfilesDir;

	protected SelectionButtonDialogFieldGroup optionButtonGroup;

	private IJavaElement initialElement;

	public ImportUML2ProfileWizardPage() {
		super(PAGE_NAME);

		setTitle("Import Profile...");
		setDescription("Import UML2 Profile Information into TS Profile.");
	}

	public void init(IStructuredSelection selection) {

		// Init this page's controls.
		ImportProfilesFieldsAdapter adapter = new ImportProfilesFieldsAdapter();

		profileFile = new StringButtonDialogField(adapter);
		profileFile.setLabelText("Target TS Profile");
		profileFile.setButtonLabel("Browse");
		profileFile.setDialogFieldListener(adapter);

		fProfilesDir = new StringButtonDialogField(adapter);
		fProfilesDir.setLabelText("Profiles Dir");
		fProfilesDir.setButtonLabel("Browse");
		fProfilesDir.setDialogFieldListener(adapter);

		String[] buttonName = new String[] { "Overwrite existing profile" };

		optionButtonGroup = new SelectionButtonDialogFieldGroup(SWT.CHECK,
				buttonName, 1);
		optionButtonGroup.setDialogFieldListener(adapter);
		optionButtonGroup.setSelection(0, true);
		// optionButtonGroup.setSelection(1, true);

		IJavaElement jelem = getInitialJavaElement(selection);
		setInitialElement(jelem);

		IPreferenceStore store = EclipsePlugin.getDefault()
				.getPreferenceStore();

		initTSRuntimeContext(jelem);
		initContainerPage(jelem);
		// initPage(jelem);
	}

	protected IJavaElement getInitialElement() {
		return initialElement;
	}

	protected void setInitialElement(IJavaElement initialElement) {
		this.initialElement = initialElement;
	}

	// -------- TypeFieldsAdapter --------

	private class ImportProfilesFieldsAdapter implements IStringButtonAdapter,
			IDialogFieldListener, IListAdapter {

		// -------- IStringButtonAdapter
		public void changeControlPressed(DialogField field) {
			importFromUML2ProfilePageChangeControlPressed(field);
		}

		// -------- IListAdapter
		public void customButtonPressed(ListDialogField field, int index) {
		}

		public void selectionChanged(ListDialogField field) {
		}

		// -------- IDialogFieldListener
		public void dialogFieldChanged(DialogField field) {
			importFromEMXPageDialogFieldChanged(field);
		}

		public void doubleClicked(ListDialogField field) {
		}
	}

	private void importFromUML2ProfilePageChangeControlPressed(DialogField field) {

		if (field == profileFile) {
			// final ResourceSelectionDialog dialog = new
			// ResourceSelectionDialog()
			final FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
			String fileName = profileFile.getText().trim();

			if (fileName.length() > 0) {
				final File path = new File(fileName);
				if (path.exists())
					dialog.setFilterPath(fileName);
			} else {
				IPackageFragmentRoot root = getPackageFragmentRoot();
				if (root != null) {
					IProject prj = root.getJavaProject().getProject();
					dialog.setFilterPath(prj.getLocation().toOSString());
				}
			}

			String selectedFile = dialog.open();
			if (selectedFile != null) {
				if (!selectedFile.endsWith(IWorkbenchProfile.FILE_EXTENSION)) {
					selectedFile += "." + IWorkbenchProfile.FILE_EXTENSION;
				}
				profileFile.setText(selectedFile);
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
				if (selDir.exists() && selDir.isDirectory()) {
					fProfilesDir.setText(selectedFile);
				}
			}
		}
		updatePageComplete();
	}

	/*
	 * A field on the type has changed. The fields' status and all dependent
	 * status are updated.
	 */
	private void importFromEMXPageDialogFieldChanged(DialogField field) {

	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);

		int nColumns = 4;

		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		// createContainerControls(composite, nColumns);
		createFileControls(composite, nColumns);

		setControl(composite);
		Dialog.applyDialogFont(composite);
		// WorkbenchHelp.setHelp(composite,
		// IJavaHelpContextIds.NEW_INTERFACE_WIZARD_PAGE);
		updatePageComplete();
	}

	protected void createProjectControls(Composite composite, int nColumns) {

	}

	/**
	 * Creates the controls for the package name field. Expects a
	 * <code>GridLayout</code> with at least 4 columns.
	 * 
	 * @param composite
	 *            the parent composite
	 * @param nColumns
	 *            number of columns to span
	 */
	protected void createFileControls(Composite composite, int nColumns) {

		profileFile.doFillIntoGrid(composite, nColumns);
		Text text = profileFile.getTextControl(null);
		LayoutUtil.setHorizontalGrabbing(text);

		fProfilesDir.doFillIntoGrid(composite, nColumns);
		text = fProfilesDir.getTextControl(null);
		LayoutUtil.setHorizontalGrabbing(text);

		LayoutUtil.setHorizontalSpan(optionButtonGroup
				.getLabelControl(composite), 1);

		Control control = optionButtonGroup.getSelectionButtonsGroup(composite);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan = nColumns - 2;
		control.setLayoutData(gd);

		DialogField.createEmptySpace(composite);

	}

	/**
	 * Perform any required update based on the runtime context
	 * 
	 */
	@Override
	protected void initFromContext() {
	}

	@Override
	protected void handleFieldChanged(String fieldName) {
		if (CONTAINER.equals(fieldName)) {
			updatePageComplete();
		}
	}

	public String getFilename() {
		return this.fProfilesDir.getText().trim();
	}

	public boolean getReplace() {
		return this.optionButtonGroup.isSelected(0);
	}

	// public boolean getMapExistence() {
	// return this.optionButtonGroup.isSelected(1);
	// }
	//
	public String getProfileFilename() {
		return this.profileFile.getText().trim();
	}

	private IJavaElement jElement;

	public IJavaElement getJElement() {
		return jElement;
	}

	public void setJElement(IJavaElement element) {
		jElement = element;
	}

	public void initPage(IJavaElement jElement) {
		setJElement(jElement);
		initFromContext();

		updatePageComplete();
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			updatePageComplete();
		}
	}

	protected void updatePageComplete() {

		if ("".equals(profileFile.getText().trim())) {
			setErrorMessage("Please select a valid Tigerstripe Profile Name");
			setPageComplete(false);
			return;
		}

		// File file = new File(profileFile.getText().trim());
		//
		// if (!file.exists()) {
		// setErrorMessage("Profile not found");
		// setPageComplete(false);
		// return;
		// }
		//
		if ("".equals(fProfilesDir.getText().trim())) {
			setErrorMessage("Please select a UML Profile location.");
			setPageComplete(false);
			return;
		}

		File dir = new File(fProfilesDir.getText().trim());

		if (!dir.exists() || !dir.isDirectory()) {
			setErrorMessage("The UML Profile location must be a valid directory.");
			setPageComplete(false);
			return;
		}

		setErrorMessage(null);

		setMessage("Import UML Profile into Tigerstripe Profile.");
		setPageComplete(true);
	}
}