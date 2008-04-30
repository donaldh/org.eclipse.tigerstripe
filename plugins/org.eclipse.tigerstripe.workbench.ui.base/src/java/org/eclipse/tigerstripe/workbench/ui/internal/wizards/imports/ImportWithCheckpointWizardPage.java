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
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.imports;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.internal.ui.wizards.TypedElementSelectionValidator;
import org.eclipse.jdt.internal.ui.wizards.TypedViewerFilter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogFieldGroup;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jdt.ui.JavaElementSorter;
import org.eclipse.jdt.ui.StandardJavaElementContentProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.project.IImportCheckpoint;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.TSRuntimeBasedWizardPage;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ImportWithCheckpointWizardPage extends TSRuntimeBasedWizardPage {

	public final static String PAGE_NAME = "ImportWithCheckpointWizardPage";

	private SelectionButtonDialogFieldGroup useReferenceProject;

	private StringButtonDialogField useReferenceProjectDirText;

	private String defaultMessage;

	private IPackageFragmentRoot refWorkspaceRoot;

	private IJavaElement initialElement;

	private String importType;

	public ImportWithCheckpointWizardPage(String title, String description,
			String importType) {
		super(PAGE_NAME);

		this.importType = importType;
		setTitle(title);
		setDescription(description);
		this.defaultMessage = description;

		ImportWithCheckpointFieldsAdapter adapter = new ImportWithCheckpointFieldsAdapter();
		String[] useXSLTButtonName = new String[] { /* 0 == VOID_INDEX */"Use reference project (checkpoint)" }; //$NON-NLS-1$

		useReferenceProject = new SelectionButtonDialogFieldGroup(SWT.CHECK,
				useXSLTButtonName, 1);
		useReferenceProject.setDialogFieldListener(adapter);
		useReferenceProject.setSelection(0, false);

		useReferenceProjectDirText = new StringButtonDialogField(adapter);
		useReferenceProjectDirText.setLabelText("Reference project");
		useReferenceProjectDirText.setButtonLabel("Browse");
		useReferenceProjectDirText.setEnabled(false);

	}

	public void init(IStructuredSelection selection) {
		IJavaElement jelem = getInitialJavaElement(selection);
		setInitialElement(jelem);

		IPreferenceStore store = EclipsePlugin.getDefault()
				.getPreferenceStore();

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

	@Override
	public IWorkspaceRoot getWorkspaceRoot() {
		return super.getWorkspaceRoot();
	}

	@Override
	public int getMaxFieldWidth() {
		return super.getMaxFieldWidth();
	}

	// -------- TypeFieldsAdapter --------
	private class ImportWithCheckpointFieldsAdapter implements
			IStringButtonAdapter, IDialogFieldListener, IListAdapter {

		// -------- IStringButtonAdapter
		public void changeControlPressed(DialogField field) {
			importWithCheckpointPageChangeControlPressed(field);
		}

		// -------- IListAdapter
		public void customButtonPressed(ListDialogField field, int index) {
		}

		public void selectionChanged(ListDialogField field) {
		}

		// -------- IDialogFieldListener
		public void dialogFieldChanged(DialogField field) {
			importWithCheckpointPageDialogFieldChanged(field);
		}

		public void doubleClicked(ListDialogField field) {
		}
	}

	public void setReferencePackageFragmentRoot(IPackageFragmentRoot root,
			boolean canBeModified) {
		refWorkspaceRoot = root;
		String str = (root == null) ? "" : root.getPath().makeRelative().toString(); //$NON-NLS-1$
		useReferenceProjectDirText.setText(str);
		useReferenceProjectDirText.setEnabled(canBeModified);
	}

	private void importWithCheckpointPageChangeControlPressed(DialogField field) {

		if (field == useReferenceProjectDirText) {
			IPackageFragmentRoot root = getPackageFragmentRoot();
			root = chooseReferenceContainer(root);
			if (root != null) {
				setReferencePackageFragmentRoot(root, true);
			}
		}

		updatePageComplete();
	}

	/**
	 * Validate the referenced project to be a TS checkpoint
	 * 
	 * @param root
	 * @return true if the refernce project is a valid project with a valid
	 *         check point
	 */
	protected boolean validateReferenceProject() {
		try {
			ITigerstripeModelProject ref = getReferenceTSProject();
			if (ref != null) {
				IImportCheckpoint ckpt = ((TigerstripeProjectHandle) ref)
						.getImportCheckpoint();
				String ckptType = ckpt.getClass().getCanonicalName();

				String msg = "A '" + ckpt.getHumanReadableType()
						+ "' was unexpectedly found.";

				if (!ckptType.equals(importType)) {
					ErrorDialog
							.openError(
									getShell(),
									"Mismatched checkpoints",
									"The reference project '"
											+ ref.getProjectLabel()
											+ "' does not contain a valid checkpoint.\n Please make sure the reference project was an import of the same type.\n",
									new Status(IStatus.ERROR, EclipsePlugin
											.getPluginId(), 222, msg, null));
					return false;
				}
				return true;
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		return false;
	}

	/*
	 * A field on the type has changed. The fields' status and all dependent
	 * status are updated.
	 */
	private void importWithCheckpointPageDialogFieldChanged(DialogField field) {

		updatePageComplete();
	}

	/**
	 * Returns the target TS where all imported stuff shall be created
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	public ITigerstripeModelProject getTSProject() throws TigerstripeException {
		if (getPackageFragmentRoot() != null)
			return (ITigerstripeModelProject) getPackageFragmentRoot()
					.getJavaProject().getProject().getAdapter(
							ITigerstripeModelProject.class);
		else
			return null;
	}

	/**
	 * Returns the TS project to use as reference for the import
	 * 
	 * @return
	 * @throws TigerstripeException
	 */
	public ITigerstripeModelProject getReferenceTSProject()
			throws TigerstripeException {
		if (refWorkspaceRoot != null)
			return (ITigerstripeModelProject) refWorkspaceRoot.getJavaProject()
					.getProject().getAdapter(ITigerstripeModelProject.class);
		else
			return null;
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);

		int nColumns = 4;

		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		createContainerControls(composite, nColumns);
		createReferenceProjectControls(composite, nColumns);

		setControl(composite);
		Dialog.applyDialogFont(composite);
		// WorkbenchHelp.setHelp(composite,
		// IJavaHelpContextIds.NEW_INTERFACE_WIZARD_PAGE);
		updatePageComplete();
	}

	private void createReferenceProjectControls(Composite composite,
			int nColumns) {

		DialogField.createEmptySpace(composite, nColumns);

		LayoutUtil.setHorizontalSpan(useReferenceProject
				.getLabelControl(composite), 1);

		Control control = useReferenceProject
				.getSelectionButtonsGroup(composite);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan = nColumns - 1;
		control.setLayoutData(gd);

		useReferenceProjectDirText.doFillIntoGrid(composite, nColumns);
		Text text = useReferenceProjectDirText.getTextControl(null);
		LayoutUtil.setWidthHint(text, getMaxFieldWidth());
		LayoutUtil.setHorizontalGrabbing(text);

		// useTargetProjectToGuide.doFillIntoGrid(composite, nColumns);
		// Control control =
		// useTargetProjectToGuide.getSelectionButton(composite);
		// GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		// gd.horizontalSpan = nColumns;
		// control.setLayoutData(gd);
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
			updatePageComplete();
		}
	}

	protected boolean checkValidTSProject() {
		IPackageFragmentRoot fragmentRoot = getPackageFragmentRoot();
		if (fragmentRoot != null) {
			IJavaProject project = fragmentRoot.getJavaProject();
			if (project != null) {
				// TODO this is relying on tigerstripe.xml... which may change
				// this should rely on the nature of the project or something...
				IResource desc = project.getProject().findMember(
						ITigerstripeConstants.PROJECT_DESCRIPTOR);
				if (desc != null && desc.exists()) {
					setErrorMessage(null);
					setPageComplete(true);
					setMessage(null);
					return true;
				}
			}
		}
		setErrorMessage("Please select a valid Tigerstripe Project.");
		setPageComplete(false);
		return false;
	}

	protected void updatePageComplete() {

		if (!checkValidTSProject()) {
			setErrorMessage("Please select a valid Tigerstripe project to import into.");
			setPageComplete(false);
			return;
		}

		useReferenceProjectDirText
				.setEnabled(useReferenceProject.isSelected(0));

		if (useReferenceProject.isSelected(0) && !validateReferenceProject()) {
			setErrorMessage("Please select a valid reference project.");
			setPageComplete(false);
			return;
		}

		setMessage(this.defaultMessage);
		setPageComplete(true);
	}

	/**
	 * This allows the user to choose the project to use as the reference (with
	 * the checkpoint)
	 * 
	 * @param initElement
	 * @return
	 */
	private IPackageFragmentRoot chooseReferenceContainer(
			IJavaElement initElement) {
		Class[] acceptedClasses = new Class[] { IPackageFragmentRoot.class,
				IJavaProject.class };
		TypedElementSelectionValidator validator = new TypedElementSelectionValidator(
				acceptedClasses, false) {
			@Override
			public boolean isSelectedValid(Object element) {
				try {
					if (element instanceof IJavaProject) {
						IJavaProject jproject = (IJavaProject) element;
						IPath path = jproject.getProject().getFullPath();
						return (jproject.findPackageFragmentRoot(path) != null);
					} else if (element instanceof IPackageFragmentRoot)
						return (((IPackageFragmentRoot) element).getKind() == IPackageFragmentRoot.K_SOURCE);
					return true;
				} catch (JavaModelException e) {
					JavaPlugin.log(e.getStatus()); // just log, no UI in
					// validation
				}
				return false;
			}
		};

		acceptedClasses = new Class[] { IJavaModel.class,
				IPackageFragmentRoot.class, IJavaProject.class };
		ViewerFilter filter = new TypedViewerFilter(acceptedClasses) {
			@Override
			public boolean select(Viewer viewer, Object parent, Object element) {
				if (element instanceof IPackageFragmentRoot) {
					try {
						return (((IPackageFragmentRoot) element).getKind() == IPackageFragmentRoot.K_SOURCE);
					} catch (JavaModelException e) {
						JavaPlugin.log(e.getStatus()); // just log, no UI in
						// validation
						return false;
					}
				}
				return super.select(viewer, parent, element);
			}
		};

		StandardJavaElementContentProvider provider = new StandardJavaElementContentProvider();
		ILabelProvider labelProvider = new JavaElementLabelProvider(
				JavaElementLabelProvider.SHOW_DEFAULT);
		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(
				getShell(), labelProvider, provider);
		dialog.setValidator(validator);
		dialog.setSorter(new JavaElementSorter());
		dialog
				.setTitle(NewWizardMessages.NewContainerWizardPage_ChooseSourceContainerDialog_title);
		dialog
				.setMessage(NewWizardMessages.NewContainerWizardPage_ChooseSourceContainerDialog_description);
		dialog.addFilter(filter);
		dialog.setInput(JavaCore.create(ResourcesPlugin.getWorkspace()
				.getRoot()));
		dialog.setInitialSelection(initElement);

		if (dialog.open() == Window.OK) {
			Object element = dialog.getFirstResult();
			if (element instanceof IJavaProject) {
				IJavaProject jproject = (IJavaProject) element;
				return jproject.getPackageFragmentRoot(jproject.getProject());
			} else if (element instanceof IPackageFragmentRoot)
				return (IPackageFragmentRoot) element;
			return null;
		}
		return null;
	}

}