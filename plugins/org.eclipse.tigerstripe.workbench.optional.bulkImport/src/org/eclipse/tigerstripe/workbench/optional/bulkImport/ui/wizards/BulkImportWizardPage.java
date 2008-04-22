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
package org.eclipse.tigerstripe.workbench.optional.bulkImport.ui.wizards;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.Message;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.internal.tools.compare.Difference;
import org.eclipse.tigerstripe.workbench.optional.bulkImport.DiffFixer;
import org.eclipse.tigerstripe.workbench.optional.bulkImport.ImportBundle;
import org.eclipse.tigerstripe.workbench.optional.bulkImport.XML2TS;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.internal.builder.TigerstripeProjectAuditor;
import org.eclipse.tigerstripe.workbench.ui.internal.elements.MessageListDialog;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

/**
 * @author Richard Craddokc
 * 
 */
public class BulkImportWizardPage extends WizardPage {

	public final static String PAGE_NAME = "BulkImportWizardPage";

	/*
	 * private Button tsButton; private Text tsProjectText;
	 */

	protected StringButtonDialogField project;
	protected StringButtonDialogField importFile;

	protected SelectionButtonDialogField loadButton;
	protected SelectionButtonDialogField applyButton;

	/** The ultimate destination of the created artifacts */
	private ITigerstripeModelProject targetProject;

	ArrayList<Difference> allXMLDiffs;
	ImportBundle bundle;
	private boolean fileLoaded;

	protected PrintWriter out;
	protected MessageList messages;

	public BulkImportWizardPage() {
		super(PAGE_NAME);

		setTitle("Bulk Import from XML ...");
		setDescription("Import/Update TS Project using XML import.");
		messages = new MessageList();
	}

	public void init(IStructuredSelection selection) {

		// Init this page's controls.
		BulkImportFieldsAdapter adapter = new BulkImportFieldsAdapter();

		project = new StringButtonDialogField(adapter);
		project.setLabelText("&TS Project Name:");
		project.setButtonLabel("Browse");
		project.setDialogFieldListener(adapter);

		importFile = new StringButtonDialogField(adapter);
		importFile.setLabelText("XML File for import:");
		importFile.setButtonLabel("Browse");
		importFile.setDialogFieldListener(adapter);

		loadButton = new SelectionButtonDialogField(SWT.PUSH);
		loadButton.setDialogFieldListener(adapter);
		loadButton.setLabelText("Load XML file");
		loadButton.setEnabled(false);

		applyButton = new SelectionButtonDialogField(SWT.PUSH);
		applyButton.setDialogFieldListener(adapter);
		applyButton.setLabelText("Apply changes");
		applyButton.setEnabled(false);

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

		if (field == importFile) {
			final FileDialog dialog = new FileDialog(getShell());
			String fileName = importFile.getText().trim();

			if (fileName.length() > 0) {
				final File path = new File(fileName);
				if (path.exists())
					dialog.setFilterPath(fileName);
			}
			String[] ext = new String[2];
			ext[0] = "*.xml";
			ext[1] = "*.*";
			dialog.setFilterExtensions(ext);

			final String selectedFile = dialog.open();
			if (selectedFile != null) {
				importFile.setText(selectedFile);
			}
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
		if (field == loadButton) {
			loadXMLFile();
		} else if (field == applyButton) {
			applyFile();
		}
		updatePageComplete();
	}

	public ITigerstripeModelProject getTSProject() throws TigerstripeException {
		return targetProject;
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);

		int nColumns = 4;

		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		createTSControls(composite, nColumns);

		createLoadControls(composite, nColumns);

		setControl(composite);
		Dialog.applyDialogFont(composite);

		updatePageComplete();
	}

	protected void createTSControls(Composite composite, int nColumns) {

		project.doFillIntoGrid(composite, nColumns);
		Text projText = project.getTextControl(null);
		LayoutUtil.setHorizontalGrabbing(projText);

		importFile.doFillIntoGrid(composite, nColumns);
		Text text = importFile.getTextControl(null);
		LayoutUtil.setHorizontalGrabbing(text);

		DialogField.createEmptySpace(composite);
	}

	protected void createLoadControls(Composite composite, int nColumns) {
		loadButton.doFillIntoGrid(composite, nColumns);
		Control control = loadButton.getSelectionButton(composite);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan = 1;
		control.setLayoutData(gd);

		DialogField.createEmptySpace(composite);
		DialogField.createEmptySpace(composite);
		DialogField.createEmptySpace(composite);
		applyButton.doFillIntoGrid(composite, nColumns);
		control = applyButton.getSelectionButton(composite);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan = 1;
		control.setLayoutData(gd);
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

	public String getImportFilename() {
		return this.importFile.getText().trim();
	}

	public String getTigerstripeName() {
		// return this.tsProjectText.getText().trim();
		return this.project.getText().trim();
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

		if ("".equals(importFile.getText().trim())) {
			setErrorMessage("Import file name must be set.");
			setPageComplete(false);
			return;
		}

		File file = new File(importFile.getText().trim());

		if (!file.exists()) {
			setErrorMessage("Import file is invalid.");
			setPageComplete(false);
			return;
		}

		setErrorMessage(null);

		if (!fileLoaded) {
			setMessage("Ready to Import.");
			loadButton.setEnabled(true);
			return;
		}

		if (fileLoaded && allXMLDiffs.size() == 0) {
			setMessage("Zero Changes to apply.");
			applyButton.setEnabled(false);
		}

		if (fileLoaded && allXMLDiffs.size() > 0) {
			setMessage("Apply Changes to Project?");
			applyButton.setEnabled(true);
		}

		setPageComplete(true);
	}

	/**
	 * This method is called when 'Load' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	public boolean loadXMLFile() {

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				try {
					doLoad(monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(false, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException
					.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * This method is called when 'Load' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	public boolean applyFile() {

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				try {
					doApply(monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(false, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException
					.getMessage());
			return false;
		}
		return true;
	}

	public boolean doApply(IProgressMonitor monitor) throws CoreException {
		try {
			File importFile = new File(getImportFilename());
			File logFile = new File(importFile.getParent()
					+ "/XMLBulkImport.log");
			out = new PrintWriter(new FileOutputStream(logFile, true));
			TigerstripeProjectAuditor.setTurnedOffForImport(true);
			DiffFixer fixer = new DiffFixer();

			ArrayList<Difference> secondPassDiffs = fixer.fixAll(allXMLDiffs,
					bundle, out, messages);
			// Bit of a sledgehammer - we'll re-add the artifacts.
			fixer.fixAll(secondPassDiffs, bundle, out, messages);
			TigerstripeProjectAuditor.setTurnedOffForImport(false);

			out.close();
			monitor.done();
			if (!messages.isEmpty()) {
				MessageListDialog msgDialog = new MessageListDialog(this
						.getShell(), messages, "XML Import Log");
				msgDialog.open();
			}

			return true;

		} catch (Exception e) {
			String msgText = "Problem Applying changes File "
					+ getImportFilename();
			Message newMsg = new Message();
			newMsg.setMessage(msgText);
			newMsg.setSeverity(0);
			messages.addMessage(newMsg);

			out.println("Error : " + msgText);
			e.printStackTrace(out);
			out.close();
			monitor.done();
			return false;

		} finally {

			final String pName = getTigerstripeName();

			IWorkspaceRunnable op = new IWorkspaceRunnable() {

				public void run(IProgressMonitor monitor) throws CoreException {
					try {
						IResource resource = ResourcesPlugin.getWorkspace()
								.getRoot().findMember(new Path(pName));
						resource.refreshLocal(IResource.DEPTH_INFINITE, null);
					} finally {
						monitor.done();
					}
				}
			};

			try {
				ResourcesPlugin.getWorkspace().run(op, monitor);
			} catch (CoreException e) {
			}
		}
	}

	public boolean doLoad(IProgressMonitor monitor) throws CoreException {

		try {
			// Restart the list in case a new XML has been created.
			messages = new MessageList();
			File importFile = new File(getImportFilename());

			File logFile = new File(importFile.getParent()
					+ "/XMLBulkImport.log");

			out = new PrintWriter(new FileOutputStream(logFile));
			String importText = "Import " + getImportFilename() + " into "
					+ getTigerstripeName();

			out.println(importText);

			Message message = new Message();
			message.setMessage(importText);
			message.setSeverity(2);
			messages.addMessage(message);

			out.println("Loading file " + importFile.getName());
			// Parse into TS
			XML2TS xML2TS = new XML2TS();
			bundle = xML2TS.loadXMLtoTigerstripe(importFile,
					getTigerstripeName(), out, messages, monitor);

			allXMLDiffs = xML2TS.compareExtractAndProject(bundle, monitor, out,
					messages);
			out.close();
			monitor.done();
			if (!messages.isEmpty()) {
				MessageListDialog msgDialog = new MessageListDialog(this
						.getShell(), messages, "XML Import Log");
				msgDialog.open();
			}

			fileLoaded = true;
			return fileLoaded;

		} catch (Exception e) {
			String msgText = "Problem loading XML File " + getImportFilename();
			Message newMsg = new Message();
			newMsg.setMessage(msgText);
			newMsg.setSeverity(0);
			messages.addMessage(newMsg);

			out.println("Error : " + msgText);
			e.printStackTrace(out);
			out.close();
			monitor.done();
			fileLoaded = false;
			return fileLoaded;

		}
	}

	public void closeLog() {
		this.out.close();
	}

}
