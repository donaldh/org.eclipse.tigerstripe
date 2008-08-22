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
package org.eclipse.tigerstripe.workbench.ui.internal.export;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.dialogs.StatusUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.undo.CheckButtonEditListener;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.TSRuntimeContext;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.TSRuntimeBasedWizardPage;

public class ModuleExportWizardPage extends TSRuntimeBasedWizardPage {

	private final static String PAGE_NAME = "org.eclipse.tigerstripe.workbench.ui.eclipse.wizards.export.page";

	// The moduleID for the module to be created
	private StringDialogField moduleIDDialogField;

	// The path for the jar file to be created.
	private StringButtonDialogField jarFileDialogField;
	
	private SelectionButtonDialogField includeDiagramsButton;
	
	private SelectionButtonDialogField includeAnnotationsButton;

	public ModuleExportWizardPage() {
		super(PAGE_NAME);

		setTitle("Export to Tigerstripe module");
		setDescription("This wizard creates module out of a Tigerstripe Model project.");

		moduleIDDialogField = new StringDialogField();
		moduleIDDialogField.setDialogFieldListener(new IDialogFieldListener() {

			public void dialogFieldChanged(DialogField field) {
				updateStatus(updatePage());
			}
		});
		moduleIDDialogField.setLabelText("Module ID:"); //$NON-NLS-1$

		jarFileDialogField = new StringButtonDialogField(
				new IStringButtonAdapter() {

					public void changeControlPressed(DialogField field) {
						openFileSelectionDialog();
					}

				});
		// jarFileDialogField.setDialogFieldListener(adapter);
		jarFileDialogField.setLabelText("Module Path:"); //$NON-NLS-1$
		jarFileDialogField.setDialogFieldListener(new IDialogFieldListener() {

			public void dialogFieldChanged(DialogField field) {
				updateStatus(updatePage());
			}
		});
		jarFileDialogField.setButtonLabel("Browse");
		
		includeDiagramsButton = new SelectionButtonDialogField(SWT.CHECK);
		includeDiagramsButton.setLabelText("Include Diagrams");
		includeDiagramsButton.setSelection(true);
		
		includeAnnotationsButton = new SelectionButtonDialogField(SWT.CHECK);
		includeAnnotationsButton.setLabelText("Include Annotations (.ann files)");
		includeAnnotationsButton.setSelection(true);
	}

	private void openFileSelectionDialog() {
		FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
		dialog.setFilterExtensions(new String[] { "*.zip", "*.jar" });
		dialog.setFilterPath(getIProject().getLocation().toOSString());
		dialog.setText("Select target module file (zip)");
		String result = dialog.open();

		if (result != null) {
			if (!result.endsWith(".zip") && !result.endsWith(".jar")) {
				result = result + ".zip";
			}
			jarFileDialogField.setText(result);
			updateStatus(updatePage());
		}
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);

		int nColumns = 4;

		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		createContainerControls(composite, nColumns);
		createModuleIDControl(composite, nColumns);
		createJarFileControl(composite, nColumns);
		
		createOptionalControls(composite, nColumns);

		setControl(composite);

		Dialog.applyDialogFont(composite);
		// WorkbenchHelp.setHelp(composite,
		// IJavaHelpContextIds.NEW_INTERFACE_WIZARD_PAGE);

	}

	protected void createOptionalControls( Composite composite, int nColumns ) {
//		includeDiagramsButton.doFillIntoGrid(composite, nColumns);
		includeAnnotationsButton.doFillIntoGrid(composite, nColumns);
	}
	
	private void createModuleIDControl(Composite composite, int nColumns) {
		moduleIDDialogField.doFillIntoGrid(composite, nColumns);
		Text text = moduleIDDialogField.getTextControl(null);
		LayoutUtil.setWidthHint(text, getMaxFieldWidth());
		LayoutUtil.setHorizontalGrabbing(text);
	}

	private void createJarFileControl(Composite composite, int nColumns) {
		jarFileDialogField.doFillIntoGrid(composite, nColumns);
		Text text = jarFileDialogField.getTextControl(null);
		// text.setTextLimit(20);
		LayoutUtil.setWidthHint(text, convertWidthInCharsToPixels(2));
		LayoutUtil.setHorizontalGrabbing(text);
	}

	@Override
	protected void initFromContext() {
		try {
			TSRuntimeContext context = getTSRuntimeContext();
			ITigerstripeModelProject project = context.getProjectHandle();

			// TODO

		} catch (TigerstripeException e) {
			// The wizard is currently not pointing at a valid TS project
			// We ignore
		}
	}

	/**
	 * The wizard owning this page is responsible for calling this method with
	 * the current selection. The selection is used to initialize the fields of
	 * the wizard page.
	 * 
	 * @param selection
	 *            used to initialize the fields
	 */
	public void init(IStructuredSelection selection) {
		IJavaElement jelem = getInitialJavaElement(selection);
		initContainerPage(jelem);
		initTSRuntimeContext(jelem);
		initFromContext();
	}

	@Override
	protected void updateStatus(IStatus status) {
		super.updateStatus(StatusUtil.getMostSevere(new IStatus[] { status,
				updatePage() }));
	}

	protected IStatus updatePage() {
		StatusInfo localStatus = new StatusInfo();

		if (tsRuntimeContextStatus.getSeverity() != IStatus.OK)
			return tsRuntimeContextStatus;

		if (moduleIDDialogField.getText().length() == 0) {
			localStatus = new StatusInfo();
			localStatus
					.setError("Please set a module ID for this exported module."); //$NON-NLS-1$
		}

		if (jarFileDialogField.getText().length() == 0) {
			localStatus = new StatusInfo();
			localStatus
					.setError("Please set a target .zip file for this exported module."); //$NON-NLS-1$
		}

		return localStatus;
	}

	public String getModuleID() {
		return moduleIDDialogField.getText().trim();
	}

	public String getJarFile() {
		return jarFileDialogField.getText().trim();
	}
	
	public boolean getIncludeDiagrams() {
		return includeDiagramsButton.isSelected();
	}

	public boolean getIncludeAnnotations() {
		return includeAnnotationsButton.isSelected();
	}

	public ITigerstripeModelProject getITigerstripeProject() {
		try {
			return getTSRuntimeContext().getProjectHandle();
		} catch (TigerstripeException e) {
			return null;
		}
	}
}
