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
package org.eclipse.tigerstripe.workbench.eclipse.wizards.imports.uml2;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
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
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.NewArtifactWizardPage;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.TSRuntimeBasedWizardPage;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.entity.NewEntityWizardPage;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.imports.IImportFromWizardPage;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.imports.ImportWithCheckpointWizardPage;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.imports.xmi.XmiImportErrorMessages;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableElement;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableModel;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableModelFactory;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.ModelImportException;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.ModelImportResult;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.ModelImporterListener;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2.UML2Importer;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2.UML2ModelImportConfiguration;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.Message;
import org.eclipse.tigerstripe.workbench.internal.core.util.messages.MessageList;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.elements.MessageListDialog;
import org.eclipse.uml2.uml.Model;

public class ImportFromUML2WizardPage extends TSRuntimeBasedWizardPage
		implements IImportFromWizardPage {

	protected SelectionButtonDialogField loadButton;

	protected SelectionButtonDialogField logButton;

	private boolean modelLoaded;

	private MessageList messageList;

	private Text txtField;

	private ModelImportResult modelImportResult;

	private class ImportUML2ModelFieldsAdapter implements IStringButtonAdapter,
			IDialogFieldListener, IListAdapter {

		// -------- IStringButtonAdapter
		public void changeControlPressed(DialogField field) {
			importFromUML2PageChangeControlPressed(field);
		}

		// -------- IListAdapter
		public void customButtonPressed(ListDialogField field, int index) {
		}

		public void selectionChanged(ListDialogField field) {
		}

		// -------- IDialogFieldListener
		public void dialogFieldChanged(DialogField field) {
			importFromUML2PageDialogFieldChanged(field);
		}

		public void doubleClicked(ListDialogField field) {
		}
	}

	public final static String PAGE_NAME = "ImportFromUML2WizardPage";

	private IJavaElement initialElement;

	protected StringButtonDialogField modelFile;

	/** The location of the referenced profiles */
	protected StringButtonDialogField fProfilesDir;

	private ITigerstripeModelProject targetProject;

	private ITigerstripeModelProject referenceProject;

	/**
	 * Perform any required update based on the runtime context
	 * 
	 */
	@Override
	protected void initFromContext() {
	}

	public ImportFromUML2WizardPage() {
		super(PAGE_NAME);

		setTitle("Import from UML2...");
		setDescription("Import Information Model from UML2.");
	}

	private void importFromUML2PageChangeControlPressed(DialogField field) {

		if (field == modelFile) {
			final FileDialog dialog = new FileDialog(getShell());
			String fileName = modelFile.getText().trim();

			if (fileName.length() > 0) {
				final File path = new File(fileName);
				if (path.exists())
					dialog.setFilterPath(fileName);
			} else {
				IProject proj = getIProject();
				if (proj != null) {
					dialog.setFilterPath(proj.getLocation().toOSString());
				}
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
				if (selDir.exists() && selDir.isDirectory()) {
					fProfilesDir.setText(selectedFile);
				}
			}
		}
		updatePageComplete();
	}

	private void importFromUML2PageDialogFieldChanged(DialogField field) {
		if (field == loadButton) {
			txtField.setText("");
			loadModel();
		} else if (field == logButton) {
			showLog();
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

		if (modelImportResult != null && modelImportResult.isSuccessful()
				&& messageList.hasNoError()) {
			MessageDialog.openInformation(getShell(), "UML2 Model Loading",
					"The UML2 model was successfully loaded.");
		}
	}

	protected void handleModelImportException(Shell shell,
			ModelImportResult result) {
		String msg = XmiImportErrorMessages.getTextForException(result
				.getImportException());
		txtField.setText(msg);

	}

	/**
	 * 
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

		UML2ModelImportConfiguration config = (UML2ModelImportConfiguration) factory
				.makeConfiguration(UML2Importer.class, referenceProject);

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
			ITigerstripeModelProject targetProject = getTSProject();

			// Set profile stuff
			Utilities.setupPaths();
			// Add specifics to point to MODEL_PROFILE.
			if (getProfilesFilename() != null
					&& getProfilesFilename().length() != 0)
				Utilities.addPaths(new File(getProfilesFilename()));
			// load Model
			try {
				File modelFile = new File(getModelFilename());

				monitor.beginTask("Opening model", 10);
				Model model = Utilities.openModelFile(modelFile);
				monitor.worked(3);
				if (model != null) {
					String modelLibrary = null;

					if (modelFile.exists()
							&& (getProfilesFilename() != null && getProfilesFilename()
									.length() != 0)) {
						modelLibrary = getLibraryName(new File(
								getProfilesFilename()), messageList);
						config.setModelLibrary(modelLibrary);
					}

					config.setModel(model);
					monitor.beginTask("Mapping model...", 15);
					modelImportResult = factory.importModelFromUML2(
							messageList, listener, targetProject, config);
					monitor.done();
					modelLoaded = true;
				}
			} catch (InvocationTargetException e) {
				throw new ModelImportException("Problem while loading model", e);
			}

		} catch (TigerstripeException e) {
			throw new ModelImportException("No target project", e);
		}
	}

	/** find the name of the library of primitive types */
	private String getLibraryName(File profilesDir, MessageList list) {
		try {

			File[] fList = profilesDir.listFiles();
			for (int i = 0; i < fList.length; i++) {
				File modelFile = fList[i];
				if (!modelFile.isFile())
					continue;
				if (!modelFile.getName().endsWith("uml2")
						&& !modelFile.getName().endsWith("uml"))
					continue;
				Model model;
				try {
					model = Utilities.openModelFile(modelFile);
					if (model != null)
						return model.getName();
				} catch (InvocationTargetException e) {
					TigerstripeRuntime.logErrorMessage(
							"InvocationTargetException detected", e);
					// this.out.close();
					return null;
				}
			}
		} catch (Exception e) {
			String msgText = "No UML Profile";
			Message newMsg = new Message();
			newMsg.setMessage(msgText);
			newMsg.setSeverity(Message.WARNING);
			list.addMessage(newMsg);
			// this.out.println("Error : "+msgText);
			TigerstripeRuntime.logErrorMessage("Exception detected", e);
			// this.out.close();
			return null;
		}
		return null;

	}

	private void showLog() {
		MessageListDialog dialog = new MessageListDialog(getShell(),
				this.messageList);

		if (dialog.open() == Window.OK) {

		}
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

	/**
	 * The properties from this page
	 * 
	 * @return
	 */
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

	public void init(IStructuredSelection selection) {
		IJavaElement jelem = getInitialJavaElement(selection);
		setInitialElement(jelem);

		IPreferenceStore store = EclipsePlugin.getDefault()
				.getPreferenceStore();

		ImportUML2ModelFieldsAdapter adapter = new ImportUML2ModelFieldsAdapter();

		modelFile = new StringButtonDialogField(adapter);
		modelFile.setLabelText("Model for import.");
		modelFile.setButtonLabel("Browse");
		modelFile.setDialogFieldListener(adapter);

		fProfilesDir = new StringButtonDialogField(adapter);
		fProfilesDir.setLabelText("Profiles Dir");
		fProfilesDir.setButtonLabel("Browse");
		fProfilesDir.setDialogFieldListener(adapter);

		loadButton = new SelectionButtonDialogField(SWT.PUSH);
		loadButton.setDialogFieldListener(adapter);
		loadButton.setLabelText("Load UML2 Model");
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

	public String getProfilesFilename() {
		return this.fProfilesDir.getText().trim();
	}

	public String getModelFilename() {
		return this.modelFile.getText().trim();
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

	private IJavaElement jElement;

	public IJavaElement getJElement() {
		return jElement;
	}

	public void setJElement(IJavaElement element) {
		jElement = element;
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			setProjects();
			updatePageComplete();
		}
	}

	public void initPage(IJavaElement jElement) {
		setJElement(jElement);
		initFromContext();

		updatePageComplete();
	}

	public ITigerstripeModelProject getTSProject() throws TigerstripeException {
		ImportWithCheckpointWizardPage initialPage = (ImportWithCheckpointWizardPage) getWizard()
				.getPreviousPage(this);
		return initialPage.getTSProject();
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);

		int nColumns = 4;

		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		createUML2Controls(composite, nColumns);

		createSeparator(composite, nColumns);
		createLoadAndLogControls(composite, nColumns);
		createTextMsgControls(composite, nColumns);

		setControl(composite);
		Dialog.applyDialogFont(composite);
		// WorkbenchHelp.setHelp(composite,
		// IJavaHelpContextIds.NEW_INTERFACE_WIZARD_PAGE);
		updatePageComplete();
	}

	private void createUML2Controls(Composite composite, int nColumns) {
		modelFile.doFillIntoGrid(composite, nColumns);
		Text text = modelFile.getTextControl(null);
		LayoutUtil.setHorizontalGrabbing(text);

		fProfilesDir.doFillIntoGrid(composite, nColumns);
		text = fProfilesDir.getTextControl(null);
		LayoutUtil.setHorizontalGrabbing(text);

		DialogField.createEmptySpace(composite);
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

	protected void updatePageComplete() {

		File file = new File(modelFile.getText().trim());

		if (!file.exists()) {
			setErrorMessage("Please select a valid UML2 model file.");
			setPageComplete(false);
			return;
		}

		File dir = new File(fProfilesDir.getText().trim());

		// if (!dir.exists() || !dir.isDirectory()) {
		// setErrorMessage("Invalid Profile directory.");
		// setPageComplete(false);
		// return;
		// }
		//
		loadButton.setEnabled(true);
		setErrorMessage(null);

		if (modelImportResult != null && !modelImportResult.isSuccessful()) {
			setPageComplete(false);
			setErrorMessage("Unable to load UML2 Model. Please see error.");
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
			setMessage("Please load UML2 Model.");
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

		setMessage("Import Information Model from UML2.");
		setPageComplete(true);
	}
}
