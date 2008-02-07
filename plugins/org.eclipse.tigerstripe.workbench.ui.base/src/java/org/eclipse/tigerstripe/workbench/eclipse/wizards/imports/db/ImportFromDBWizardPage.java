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
package org.eclipse.tigerstripe.workbench.eclipse.wizards.imports.db;

import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ComboDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.ArtifactDefinitionGenerator;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.NewArtifactWizardPage;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.TSRuntimeBasedWizardPage;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.entity.EntityDefinitionGenerator;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.entity.NewEntityWizardPage;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.imports.IImportFromWizardPage;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.imports.ImportWithCheckpointWizardPage;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.imports.xmi.XmiImportErrorMessages;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableElement;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableModel;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableModelFactory;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.ModelImportException;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.ModelImportResult;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.ModelImporterListener;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.DBImporter;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.DBModelImportConfiguration;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.database.AbstractDatabaseModel;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.db.database.DatabaseModelFactory;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.Message;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.elements.MessageListDialog;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ImportFromDBWizardPage extends TSRuntimeBasedWizardPage implements
		IImportFromWizardPage {

	public final static String PAGE_NAME = "ImportFromXMIWizardPage";

	private ITigerstripeProject targetProject;

	private ITigerstripeProject referenceProject;

	protected StringDialogField dbHostField;

	protected StringDialogField dbPortField;

	protected StringDialogField dbNameField;

	protected StringDialogField dbUserField;

	protected PasswordStringDialogField dbPasswordField;

	protected ComboDialogField dbTypeComboField;

	protected SelectionButtonDialogField loadButton;

	protected SelectionButtonDialogField logButton;

	private boolean modelLoaded;

	private MessageList messageList;

	private Text txtField;

	private ModelImportResult modelImportResult;

	private IJavaElement initialElement;

	public ImportFromDBWizardPage() {
		super(PAGE_NAME);

		setTitle("Import from database...");
		setDescription("Import Information Model from Database.");
	}

	public ModelImportResult getModelImportResult() {
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				try {
					if (referenceProject != null) {
						// need to compute deltas
						AnnotableModel model = modelImportResult.getModel();
						List<AnnotableElement> annotables = new ArrayList(model
								.getAnnotableElements());

						try {
							monitor.beginTask("Computing delta...", 100);
							monitor.worked(10);
							((TigerstripeProjectHandle) referenceProject)
									.getImportCheckpoint().computeDelta(
											annotables);
							monitor.done();
						} catch (TigerstripeException e) {
							EclipsePlugin.log(e);
						}
					}
				} finally {
					monitor.done();
				}
			}
		};

		try {
			getContainer().run(true, false, op);
		} catch (InvocationTargetException e) {
			// Shouldn't happen since the possible exception was captured in the
			// modelImportResult
			EclipsePlugin.log(e.getTargetException());
		} catch (InterruptedException e) {
			modelLoaded = false;
			modelImportResult = null;
			messageList = null;
			EclipsePlugin.log(e);
		}

		return this.modelImportResult;
	}

	protected void setProjects() {
		ImportWithCheckpointWizardPage initialPage = (ImportWithCheckpointWizardPage) getWizard()
				.getPreviousPage(this);
		try {
			this.targetProject = initialPage.getTSProject();
			this.referenceProject = initialPage.getReferenceTSProject();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

	}

	public void init(IStructuredSelection selection) {
		IJavaElement jelem = getInitialJavaElement(selection);
		setInitialElement(jelem);

		IPreferenceStore store = EclipsePlugin.getDefault()
				.getPreferenceStore();

		// Init this page's controls.
		ImportFromXMIFieldsAdapter adapter = new ImportFromXMIFieldsAdapter();

		String[] dbTypes = DatabaseModelFactory.getInstance()
				.getKnownDatabaseModels();

		dbTypeComboField = new ComboDialogField(SWT.READ_ONLY);
		dbTypeComboField.setLabelText("Database");
		dbTypeComboField.setItems(dbTypes);
		dbTypeComboField.setDialogFieldListener(adapter);

		dbHostField = new StringDialogField();
		dbHostField.setLabelText("DB Host:");
		dbHostField.setDialogFieldListener(adapter);

		dbPortField = new StringDialogField();
		dbPortField.setLabelText("DB Port:");
		dbPortField.setDialogFieldListener(adapter);

		dbNameField = new StringDialogField();
		dbNameField.setLabelText("DB Name:");
		dbNameField.setDialogFieldListener(adapter);

		dbUserField = new StringDialogField();
		dbUserField.setLabelText("Username:");
		dbUserField.setDialogFieldListener(adapter);

		dbPasswordField = new PasswordStringDialogField();
		dbPasswordField.setLabelText("Password:");
		dbPasswordField.setDialogFieldListener(adapter);

		loadButton = new SelectionButtonDialogField(SWT.PUSH);
		loadButton.setDialogFieldListener(adapter);
		loadButton.setLabelText("Extract DB Schema");
		loadButton.setEnabled(false);

		logButton = new SelectionButtonDialogField(SWT.PUSH);
		logButton.setDialogFieldListener(adapter);
		logButton.setLabelText("View Log");
		logButton.setEnabled(false);

		initTSRuntimeContext(jelem);
		initContainerPage(jelem);
		initPage(jelem);
	}

	protected IJavaElement getInitialElement() {
		return initialElement;
	}

	protected void setInitialElement(IJavaElement initialElement) {
		this.initialElement = initialElement;
	}

	// -------- TypeFieldsAdapter --------

	private class ImportFromXMIFieldsAdapter implements IStringButtonAdapter,
			IDialogFieldListener, IListAdapter {

		// -------- IStringButtonAdapter
		public void changeControlPressed(DialogField field) {
		}

		// -------- IListAdapter
		public void customButtonPressed(ListDialogField field, int index) {
		}

		public void selectionChanged(ListDialogField field) {
		}

		// -------- IDialogFieldListener
		public void dialogFieldChanged(DialogField field) {
			importFromDBPageDialogFieldChanged(field);
		}

		public void doubleClicked(ListDialogField field) {
		}
	}

	/*
	 * A field on the type has changed. The fields' status and all dependent
	 * status are updated.
	 */
	private void importFromDBPageDialogFieldChanged(DialogField field) {
		if (field == loadButton) {
			txtField.setText("");
			loadModel();
		} else if (field == logButton) {
			showLog();
		} else if (field == dbTypeComboField) {

		}
		updatePageComplete();
	}

	private void loadModel() {
		messageList = new MessageList();

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				try {
					loadModelAsync(monitor);
				} catch (ModelImportException e) {
					throw new InvocationTargetException(e);
				} catch (MalformedURLException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};

		try {
			getContainer().run(true, false, op);
		} catch (InvocationTargetException e) {
			// Shouldn't happen since the possible exception was captured in the
			// modelImportResult
			EclipsePlugin.log(e.getTargetException());
		} catch (InterruptedException e) {
			modelLoaded = false;
			modelImportResult = null;
			messageList = null;
			EclipsePlugin.log(e);
		}

		if (modelImportResult != null && modelImportResult.isSuccessful()) {
			MessageDialog.openInformation(getShell(), "DB Schema Extraction",
					"The database metadata was successfully extracted.");
		}
	}

	protected void handleModelImportException(Shell shell,
			ModelImportResult result) {
		String msg = XmiImportErrorMessages.getTextForException(result
				.getImportException());
		txtField.setText(msg);

	}

	private void setModelImportConfigurationFromDialog(
			DBModelImportConfiguration config) {
		try {
			AbstractDatabaseModel dbModel = DatabaseModelFactory.getInstance()
					.make(this.dbTypeComboField.getText());
			dbModel.setHostname(dbHostField.getText().trim());
			dbModel.setPort(dbPortField.getText().trim());
			dbModel.setDbName(dbNameField.getText().trim());
			dbModel.setUsername(dbUserField.getText().trim());
			dbModel.setPassword(dbPasswordField.getText().trim());
			config.setDatabaseModel(dbModel);
			// config.setReferenceProject(reuseTargetProjectToGuide
			// .isSelected());
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	/**
	 * 
	 * @param monitor
	 * @return XmiLoadResult
	 * @throws ModelImportException
	 * @throws MalformedURLException
	 */
	private void loadModelAsync(IProgressMonitor monitor)
			throws ModelImportException, MalformedURLException {

		final IProgressMonitor lMonitor = monitor;
		lMonitor.beginTask("Importing model", 10);

		AnnotableModelFactory factory = AnnotableModelFactory.getInstance();

		DBModelImportConfiguration config = (DBModelImportConfiguration) factory
				.makeConfiguration(DBImporter.class, referenceProject);
		setModelImportConfigurationFromDialog(config);

		ModelImporterListener listener = new ModelImporterListener() {
			public void importBeginTask(String task, int duration) {
				lMonitor.beginTask(task, duration);
			}

			public void importDone() {
				lMonitor.done();
			}

			public void worked(int duration) {
				lMonitor.worked(duration);
			}
		};

		try {
			ITigerstripeProject targetProject = getTSProject();
			modelImportResult = factory.importModelFromDb(messageList,
					listener, targetProject, config);
			modelLoaded = true;
		} catch (TigerstripeException e) {
			throw new ModelImportException("No target project", e);
		}
	}

	public ITigerstripeProject getTSProject() throws TigerstripeException {
		ImportWithCheckpointWizardPage initialPage = (ImportWithCheckpointWizardPage) getWizard()
				.getPreviousPage(this);
		return initialPage.getTSProject();
	}

	private void showLog() {
		MessageListDialog dialog = new MessageListDialog(getShell(),
				this.messageList);

		if (dialog.open() == Window.OK) {

		}
	}

	// protected void handleFinishException(Shell shell, Throwable e,
	// String title, String message) {
	//
	// XmiModelImportResult result = (XmiModelImportResult) modelImportResult;
	// TigerstripeRuntime.logInfoMessage("Actually loaded is="
	// + result.getActuallyLoadedURI().toASCIIString());
	//
	// StringWriter msg = new StringWriter();
	// if (message != null) {
	// msg.write(message);
	// msg.write("\n\n"); //$NON-NLS-1$
	// }
	//
	// String exceptionMessage = e.getMessage();
	// if (exceptionMessage == null || exceptionMessage.length() == 0)
	// msg.write(JavaUIMessages.ExceptionDialog_seeErrorLogMessage);
	// else
	// msg.write(exceptionMessage);
	// MessageDialog.openError(shell, title, msg.toString());
	// }
	//
	public Properties getProperties() {
		Properties result = new Properties();
		// TODO: this is way messy and needs to be re-done!!!

		result.put(NewArtifactWizardPage.CONTAINER_PATH,
				getPackageFragmentRoot().getJavaProject().getElementName());
		result.put(NewArtifactWizardPage.SRCDIRECTORY_NAME,
				getPackageFragmentRoot().getElementName());
		result.put(NewArtifactWizardPage.CONTAINER_NAME, getWorkspaceRoot()
				.getLocation().toOSString());
		result.put(NewArtifactWizardPage.GENERATE, new Boolean(true));
		result.put(NewEntityWizardPage.PRIMARY_KEY_TYPE, "String");

		// hack for now
		Properties prop = new Properties();
		prop.setProperty("simple", "true");
		result.put("ossj.entity.create", prop);
		result.put("ossj.entity.get", prop);
		result.put("ossj.entity.set", prop);
		result.put("ossj.entity.remove", prop);

		return result;
	}

	protected ArtifactDefinitionGenerator getGenerator(
			Properties pageProperties, Writer writer) {
		return new EntityDefinitionGenerator(pageProperties, writer);
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);

		int nColumns = 4;

		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		// createContainerControls(composite, nColumns);
		// createSeparator(composite, nColumns);

		createDBConnectionDetailsControl(composite, nColumns);

		// createSeparator(composite, nColumns);
		// createSavedMappingControls( composite, nColumns );

		createSeparator(composite, nColumns);
		createLoadAndLogControls(composite, nColumns);
		createTextMsgControls(composite, nColumns);

		setControl(composite);
		Dialog.applyDialogFont(composite);
		// WorkbenchHelp.setHelp(composite,
		// IJavaHelpContextIds.NEW_INTERFACE_WIZARD_PAGE);
		updatePageComplete();
	}

	private void createDBConnectionDetailsControl(Composite composite,
			int nColumns) {

		Label label = new Label(composite, SWT.BOLD);
		label.setText("Database details");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = nColumns;
		label.setLayoutData(gd);

		dbTypeComboField.doFillIntoGrid(composite, nColumns);

		dbHostField.doFillIntoGrid(composite, nColumns);
		dbPortField.doFillIntoGrid(composite, nColumns);
		dbNameField.doFillIntoGrid(composite, nColumns);
		dbUserField.doFillIntoGrid(composite, nColumns);
		dbPasswordField.doFillIntoGrid(composite, nColumns);
	}

	protected void createLoadAndLogControls(Composite composite, int nColumns) {

		loadButton.doFillIntoGrid(composite, nColumns);
		Control control = loadButton.getSelectionButton(composite);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan = 1;
		control.setLayoutData(gd);

		logButton.doFillIntoGrid(composite, nColumns);
		Control control2 = logButton.getSelectionButton(composite);
		GridData gd2 = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd2.horizontalSpan = 1;
		control2.setLayoutData(gd2);
	}

	protected void createTextMsgControls(Composite composite, int nColumns) {

		txtField = new Text(composite, SWT.WRAP | SWT.BORDER);
		txtField.setEditable(false);
		txtField.setToolTipText("Model load error message area");
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.FILL_BOTH);
		gd.horizontalSpan = 4;
		gd.verticalSpan = 5;
		gd.heightHint = 40;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		txtField.setLayoutData(gd);
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
			setProjects();
			updatePageComplete();
		}
	}

	// protected boolean checkValidTSProject() {
	// // we check that the container is pointing to a valid TS Project
	// // or else we bloc at this page
	// boolean result = false;
	// IPackageFragmentRoot fragmentRoot = getPackageFragmentRoot();
	// if (fragmentRoot != null) {
	// IJavaProject project = fragmentRoot.getJavaProject();
	// if (project != null) {
	// // TODO this is relying on tigerstripe.xml... which may change
	// // this should rely on the nature of the project or something...
	// IResource desc = project.getProject().findMember(
	// "tigerstripe.xml");
	// if (desc != null && desc.exists()) {
	// result = true;
	// setErrorMessage(null);
	// setPageComplete(true);
	// setMessage(null);
	// return true;
	// }
	// }
	// }
	// setErrorMessage("Please select a valid Tigerstripe Project.");
	// setPageComplete(false);
	// return false;
	// }

	protected void updatePageComplete() {

		// if (!checkValidTSProject()) {
		// return;
		// }
		//
		if ("".equals(dbHostField.getText().trim())) {
			setErrorMessage("Invalid hostname.");
			setPageComplete(false);
			return;
		}

		if ("".equals(dbPortField.getText().trim())) {
			setErrorMessage("Invalid port.");
			setPageComplete(false);
			return;
		}

		// if ("".equals(dbNameField.getText().trim())) {
		// setErrorMessage("Invalid database name.");
		// setPageComplete(false);
		// return;
		// }
		//
		loadButton.setEnabled(true);
		setErrorMessage(null);

		if (modelImportResult != null && !modelImportResult.isSuccessful()) {
			setPageComplete(false);
			setErrorMessage("Unable to extract database schema. Please see error.");
			if (modelImportResult.getImportException() != null) {
				this.txtField.setText(modelImportResult.getImportException()
						.getException().getMessage());
			}
			return;
		} else {
			txtField.setText("");
		}

		if (!modelLoaded) {
			setPageComplete(false);
			setMessage("Extract database schema.");
			return;
		}
		;

		if (messageList != null && !messageList.isEmpty()) {
			logButton.setEnabled(true);
		} else {
			logButton.setEnabled(false);
		}

		if (modelLoaded
				&& (messageList == null || !messageList.getMessagesBySeverity(
						Message.ERROR).isEmpty())) {
			setPageComplete(false);
			setMessage("Model contains errors. Please check log and fix.");
			return;
		}

		if (modelLoaded) {
			setMessage("Please continue with model import");
			setPageComplete(true);
			return;
		}

		setMessage("Import Information Model from database.");
		setPageComplete(true);
	}
}