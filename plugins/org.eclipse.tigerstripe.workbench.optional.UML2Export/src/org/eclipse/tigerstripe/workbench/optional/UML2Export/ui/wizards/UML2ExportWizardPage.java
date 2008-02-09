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
package org.eclipse.tigerstripe.workbench.optional.UML2Export.ui.wizards;

import java.io.File;
import java.io.PrintWriter;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

/**
 * @author Richard Craddokc
 * 
 */
public class UML2ExportWizardPage extends WizardPage {

	public final static String PAGE_NAME = "UML2ExportWizardPage";

	protected StringButtonDialogField project;
	protected StringButtonDialogField exportDir;
	// protected StringButtonDialogField exportFile;

	/** The source of artifacts */
	private ITigerstripeModelProject sourceProject;

	protected PrintWriter out;
	protected MessageList messages;

	public UML2ExportWizardPage() {
		super(PAGE_NAME);

		setTitle("Export to UML2 ...");
		setDescription("Export Tigerstripe Project to UML2.");
		messages = new MessageList();
	}

	public void init(IStructuredSelection selection) {

		// Init this page's controls.
		BulkImportFieldsAdapter adapter = new BulkImportFieldsAdapter();

		project = new StringButtonDialogField(adapter);
		project.setLabelText("&TS Project Name:");
		project.setButtonLabel("Browse");
		project.setDialogFieldListener(adapter);

		exportDir = new StringButtonDialogField(adapter);
		exportDir.setLabelText("Directory for export:");
		exportDir.setButtonLabel("Browse");
		exportDir.setDialogFieldListener(adapter);

		/*
		 * exportFile = new StringButtonDialogField(adapter);
		 * exportFile.setLabelText("UML2 File for export:");
		 * exportFile.setButtonLabel("Browse");
		 * exportFile.setDialogFieldListener(adapter);
		 */

	}

	// -------- TypeFieldsAdapter --------

	private class BulkImportFieldsAdapter implements IStringButtonAdapter,
			IDialogFieldListener, IListAdapter {

		// -------- IStringButtonAdapter
		public void changeControlPressed(DialogField field) {
			bulkImportPageChangeControlPressed(field);
		}

		// -------- IListAdapter
		public void customButtonPressed(ListDialogField field, int index) {
		}

		public void selectionChanged(ListDialogField field) {
		}

		// -------- IDialogFieldListener
		public void dialogFieldChanged(DialogField field) {
			bulkImportPageDialogFieldChanged(field);
		}

		public void doubleClicked(ListDialogField field) {
		}
	}

	private void bulkImportPageChangeControlPressed(DialogField field) {

		if (field == exportDir) {

			DirectoryDialog dialog = new DirectoryDialog(getShell());

			final String selectedDir = dialog.open();
			if (selectedDir != null) {
				File f = new File(selectedDir);
				if (f.exists() && f.isDirectory()) {
					exportDir.setText(f.getAbsolutePath());
				}
			}

			/*
			 * }else if (field == exportFile) { final FileDialog dialog = new
			 * FileDialog(getShell()); String fileName =
			 * getExportDir()+File.separator+exportFile.getText().trim();
			 * 
			 * if (fileName.length() > 0) { final File path = new
			 * File(fileName); if (path.exists())
			 * dialog.setFilterPath(fileName); } String[] ext = new String[2];
			 * ext[0] = "*.xmi"; ext[1] = "*.*";
			 * dialog.setFilterExtensions(ext);
			 * 
			 * final String selectedFile = dialog.open(); if (selectedFile !=
			 * null) { File f = new File(selectedFile);
			 * exportFile.setText(f.getName()); }
			 */
		} else if (field == project) {
			ContainerSelectionDialog dialog = new ContainerSelectionDialog(
					getShell(), ResourcesPlugin.getWorkspace().getRoot(),
					false, "Select tigerstripe project");

			// This kind of thing would make sense
			// dialog.setValidator(new TSProjectSelectionValidator());

			if (dialog.open() == Window.OK) {
				Object[] result = dialog.getResult();
				if (result.length == 1) {
					// tsProjectText.setText(((Path) result[0]).toString());
					project.setText(((Path) result[0]).toString());
				}
			}
		}

		updatePageComplete();
	}

	/*
	 * A field on the type has changed. The fields' status and all dependent
	 * status are updated.
	 */
	private void bulkImportPageDialogFieldChanged(DialogField field) {

		updatePageComplete();
	}

	public ITigerstripeModelProject getTSProject() throws TigerstripeException {
		return sourceProject;
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);

		int nColumns = 4;

		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		createTSControls(composite, nColumns);

		setControl(composite);
		Dialog.applyDialogFont(composite);

		updatePageComplete();
	}

	protected void createTSControls(Composite composite, int nColumns) {

		project.doFillIntoGrid(composite, nColumns);
		Text projText = project.getTextControl(null);
		LayoutUtil.setHorizontalGrabbing(projText);

		exportDir.doFillIntoGrid(composite, nColumns);
		Text text = exportDir.getTextControl(null);
		LayoutUtil.setHorizontalGrabbing(text);

		/*
		 * exportFile.doFillIntoGrid(composite, nColumns); text =
		 * exportFile.getTextControl(null);
		 * LayoutUtil.setHorizontalGrabbing(text);
		 */

		DialogField.createEmptySpace(composite);
	}

	/**
	 * Perform any required update based on the runtime context
	 * 
	 */
	protected void initFromContext() {
	}

	protected void handleFieldChanged(String fieldName) {
		updatePageComplete();
	}

	public File getExportDir() {
		File dir = new File(this.exportDir.getText().trim());
		return dir;
	}

	/*
	 * public String getExportFilename() { return
	 * this.exportFile.getText().trim(); }
	 */

	public String getTigerstripeName() {
		// return this.tsProjectText.getText().trim();
		String name = this.project.getText().trim();
		if (name.startsWith("/")) {
			name = name.substring(1);
		}
		return name;
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			updatePageComplete();
		}
	}

	protected void updatePageComplete() {

		IResource tsContainer = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(new Path(getTigerstripeName()));

		if (getTigerstripeName().length() == 0) {
			setErrorMessage("Tigerstripe project must be specified");
			setPageComplete(false);
			return;
		}

		if (tsContainer == null
				|| (tsContainer.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
			setErrorMessage("Tigerstripe project must exist");
			setPageComplete(false);
			return;
		}

		if (!tsContainer.isAccessible()) {
			setErrorMessage("TS Project must be writable");
			setPageComplete(false);
			return;
		}

		/*
		 * if ("".equals(exportFile.getText().trim())) { setErrorMessage("Export
		 * file name must be set."); setPageComplete(false); return; }
		 * 
		 * File file = new File(getExportFilename());
		 */
		// TODO Change this round
		/*
		 * if (file.exists()) { setErrorMessage("Export file already exists.");
		 * out.println(file); setPageComplete(false); return; }
		 */

		setErrorMessage(null);

		setPageComplete(true);
	}

	public void closeLog() {
		this.out.close();
	}

}
