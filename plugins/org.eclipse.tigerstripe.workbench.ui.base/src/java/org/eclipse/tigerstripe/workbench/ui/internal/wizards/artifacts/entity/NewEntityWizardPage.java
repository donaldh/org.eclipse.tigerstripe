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
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.entity;

import java.io.Writer;
import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.dialogs.TypeSelectionDialog2;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.ControlContentAssistHelper;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.JavaTypeCompletionProcessor;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.metamodel.impl.IManagedEntityArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.OssjEntityMethodFlavor;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjFlavorDefaults;
import org.eclipse.tigerstripe.workbench.ui.internal.elements.MethodPropertiesDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.runtime.messages.NewWizardMessages;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.ArtifactDefinitionGenerator;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.NewArtifactWizardPage;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class NewEntityWizardPage extends NewArtifactWizardPage {

	@Override
	public IAbstractArtifact[] getArtifactModelForExtend() {
		return new IAbstractArtifact[] { ManagedEntityArtifact.MODEL };
	}

	// Initialize the MethodProperties Dialog and
	// use it to initialize the default values for CRUD ops
	String methodLabels[] = { "Create", // index = 0
			"Get", // index = 1
			"Set", // index = 2
			"Remove" // index =3
	};

	// Each CRUD operation as it stored on a class level tag
	private Properties createExposedOptions;

	private Properties getExposedOptions;

	private Properties setExposedOptions;

	private Properties removeExposedOptions;

	public Properties getCreateProperties() {
		return this.createExposedOptions;
	}

	public Properties getGetProperties() {
		return this.getExposedOptions;
	}

	public Properties getSetProperties() {
		return this.setExposedOptions;
	}

	public Properties getRemoveProperties() {
		return this.removeExposedOptions;
	}

	private final static String PAGE_NAME = "NewEntityWizardPage";

	private final static String PRIMARY_KEY_NAME = PAGE_NAME + ".primaryKey";

	private StringButtonDialogField primaryKeyClassDialogField;

	private IStatus primaryKeyClassStatus;

	private JavaTypeCompletionProcessor primaryKeyClassCompletionProcessor;

	private SelectionButtonDialogField advancedButton;

	private MethodPropertiesDialog methodPropertiesDialog;

	public NewEntityWizardPage() {
		super(PAGE_NAME);

		setTitle(ArtifactMetadataFactory.INSTANCE.getMetadata(
				IManagedEntityArtifactImpl.class.getName()).getLabel()
				+ " Artifact");
		setDescription("Create a new "
				+ ArtifactMetadataFactory.INSTANCE.getMetadata(
						IManagedEntityArtifactImpl.class.getName()).getLabel()
				+ " Artifact.");

		EntityFieldsAdapter adapter = new EntityFieldsAdapter();

		primaryKeyClassDialogField = new StringButtonDialogField(adapter);
		primaryKeyClassDialogField.setDialogFieldListener(adapter);
		primaryKeyClassDialogField.setLabelText("Primary Key"); //$NON-NLS-1$
		primaryKeyClassDialogField.setButtonLabel(NewWizardMessages
				.getString("NewEntityWizardPage.primaryKey.button")); //$NON-NLS-1$

		primaryKeyClassStatus = new StatusInfo();
		primaryKeyClassCompletionProcessor = new JavaTypeCompletionProcessor(
				false, false);

		advancedButton = new SelectionButtonDialogField(SWT.PUSH);
		advancedButton.setDialogFieldListener(adapter);
		advancedButton.setLabelText("Advanced");

		// Initialize the MethodProperties Dialog and
		// use it to initialize the default values for CRUD ops
		Properties methodProperties[] = { this.createExposedOptions,
				this.getExposedOptions, this.setExposedOptions,
				this.removeExposedOptions };

		methodPropertiesDialog = new MethodPropertiesDialog(getShell(),
				methodLabels, methodProperties, "Entity Name", this
						.getArtifactName());

		this.createExposedOptions = getDefaultProperties(
				IOssjFlavorDefaults.createMethodFlavors,
				IOssjFlavorDefaults.createMethodFlavorDefaults);
		this.getExposedOptions = getDefaultProperties(
				IOssjFlavorDefaults.getMethodFlavors,
				IOssjFlavorDefaults.getMethodFlavorDefaults);
		this.setExposedOptions = getDefaultProperties(
				IOssjFlavorDefaults.setMethodFlavors,
				IOssjFlavorDefaults.setMethodFlavorDefaults);
		this.removeExposedOptions = getDefaultProperties(
				IOssjFlavorDefaults.removeMethodFlavors,
				IOssjFlavorDefaults.removeMethodFlavorDefaults);
	}

	private Properties getDefaultProperties(
			OssjEntityMethodFlavor[] supportedFlavors, String[] defaultValues) {
		Properties result = new Properties();
		int index = 0;
		for (OssjEntityMethodFlavor flavor : supportedFlavors) {
			String fullDefault = defaultValues[index++];
			result.setProperty(flavor.getPojoLabel(), fullDefault.substring(0,
					fullDefault.indexOf(":")));
		}
		return result;
	}

	// ------ validation --------
	@Override
	protected void doStatusUpdate() {
		// status of all used components
		IStatus[] status = new IStatus[] { tsRuntimeContextStatus,
				fContainerStatus, artifactNameStatus, artifactPackageStatus,
				extendedClassStatus };

		// the mode severe status will be displayed and the ok button
		// enabled/disabled.
		updateStatus(status);
	}

	// -------- TypeFieldsAdapter --------

	private class EntityFieldsAdapter implements IStringButtonAdapter,
			IDialogFieldListener, IListAdapter {

		// -------- IStringButtonAdapter
		public void changeControlPressed(DialogField field) {
			entityPageChangeControlPressed(field);
		}

		// -------- IListAdapter
		public void customButtonPressed(ListDialogField field, int index) {
		}

		public void selectionChanged(ListDialogField field) {
		}

		// -------- IDialogFieldListener
		public void dialogFieldChanged(DialogField field) {
			entityPageDialogFieldChanged(field);
		}

		public void doubleClicked(ListDialogField field) {
		}
	}

	private void entityPageChangeControlPressed(DialogField field) {
		if (field == primaryKeyClassDialogField) {
			IType type = choosePrimaryKeyType();
			if (type != null) {
				primaryKeyClassDialogField.setText(JavaModelUtil
						.getFullyQualifiedName(type));
			}
		}
	}

	/*
	 * A field on the type has changed. The fields' status and all dependent
	 * status are updated.
	 */
	private void entityPageDialogFieldChanged(DialogField field) {
		String fieldName = null;
		if (field == primaryKeyClassDialogField) {
			primaryKeyClassStatus = primaryKeyClassChanged();
			fieldName = PRIMARY_KEY_NAME;
		} else if (field == advancedButton) {

			Properties methodProperties[] = { this.createExposedOptions,
					this.getExposedOptions, this.setExposedOptions,
					this.removeExposedOptions };

			methodPropertiesDialog = new MethodPropertiesDialog(getShell(),
					methodLabels, methodProperties, "Entity Name", this
							.getArtifactName());

			if (methodPropertiesDialog.open() == Window.OK) {
				this.createExposedOptions = methodPropertiesDialog
						.getReturnProperties(0);
				this.getExposedOptions = methodPropertiesDialog
						.getReturnProperties(1);
				this.setExposedOptions = methodPropertiesDialog
						.getReturnProperties(2);
				this.removeExposedOptions = methodPropertiesDialog
						.getReturnProperties(3);
			}
		}
		// tell all others
		handleFieldChanged(fieldName);
	}

	@Override
	protected void handleFieldChanged(String fieldName) {
		super.handleFieldChanged(fieldName);

		doStatusUpdate();
	}

	/**
	 * Hook method that gets called when the superclass name has changed. The
	 * method validates the superclass name and returns the status of the
	 * validation.
	 * <p>
	 * Subclasses may extend this method to perform their own validation.
	 * </p>
	 * 
	 * @return the status of the validation
	 */
	protected IStatus primaryKeyClassChanged() {
		StatusInfo status = new StatusInfo();
		IPackageFragmentRoot root = getPackageFragmentRoot();
		primaryKeyClassDialogField.enableButton(root != null);

		String sclassName = getPrimaryKeyClass();
		if (sclassName.length() == 0)
			// accept the empty field (stands for java.lang.Object)
			return status;
		IStatus val = JavaConventions.validateJavaTypeName(sclassName);
		if (val.getSeverity() == IStatus.ERROR) {
			status
					.setError(NewWizardMessages
							.getString("NewArtifactWizardPage.error.InvalidPrimaryKeyClassName")); //$NON-NLS-1$
			return status;
		}
		if (root != null) {
			try {
				IType type = resolvePrimaryKeyTypeName(root.getJavaProject(),
						sclassName);
				if (type == null) {
					status
							.setWarning(NewWizardMessages
									.getString("NewTypeWizardPage.warning.SuperClassNotExists")); //$NON-NLS-1$
					return status;
				}
			} catch (JavaModelException e) {
				status
						.setError(NewWizardMessages
								.getString("NewTypeWizardPage.error.InvalidSuperClassName")); //$NON-NLS-1$
				JavaPlugin.log(e);
			}
		} else {
			status.setError(""); //$NON-NLS-1$
		}
		return status;

	}

	private IType resolvePrimaryKeyTypeName(IJavaProject jproject,
			String sclassName) throws JavaModelException {
		if (!jproject.exists())
			return null;
		IType type = null;
		IPackageFragment currPack = getArtifactPackageFragment();
		if (type == null && currPack != null) {
			String packName = currPack.getElementName();
			// search in own package
			if (!currPack.isDefaultPackage()) {
				type = jproject.findType(packName, sclassName);
			}
			// search in java.lang
			if (type == null && !"java.lang".equals(packName)) { //$NON-NLS-1$
				type = jproject.findType("java.lang", sclassName); //$NON-NLS-1$
			}
		}
		// search fully qualified
		if (type == null) {
			type = jproject.findType(sclassName);
		}
		return type;
	}

	/**
	 * Returns the content of the superclass input field.
	 * 
	 * @return the superclass name
	 */
	public String getPrimaryKeyClass() {
		return primaryKeyClassDialogField.getText();
	}

	/**
	 * Sets the super class name.
	 * 
	 * @param name
	 *            the new superclass name
	 * @param canBeModified
	 *            if <code>true</code> the superclass name field is editable;
	 *            otherwise it is read-only.
	 */
	public void setPrimaryKeyClass(String name, boolean canBeModified) {
		primaryKeyClassDialogField.setText(name);
		primaryKeyClassDialogField.setEnabled(canBeModified);
	}

	private IType choosePrimaryKeyType() {
		IPackageFragmentRoot root = getPackageFragmentRoot();
		if (root == null)
			return null;

		IJavaElement[] elements = new IJavaElement[] { root.getJavaProject() };
		IJavaSearchScope scope = SearchEngine.createJavaSearchScope(elements);

		TypeSelectionDialog2 dialog = new TypeSelectionDialog2(getShell(),
				false, getWizard().getContainer(), scope,
				IJavaSearchConstants.TYPE);
		dialog.setTitle(NewWizardMessages
				.getString("NewTypeWizardPage.SuperClassDialog.title")); //$NON-NLS-1$
		dialog.setMessage(NewWizardMessages
				.getString("NewTypeWizardPage.SuperClassDialog.message")); //$NON-NLS-1$
		dialog.setFilter(getPrimaryKeyClass());

		if (dialog.open() == Window.OK)
			return (IType) dialog.getFirstResult();
		return null;
	}

	public final static String PRIMARY_KEY_TYPE = "primaryKeyType";

	@Override
	public Properties getProperties() {
		Properties result = getPropertiesCommons();
		result.put(PRIMARY_KEY_TYPE, primaryKeyClassDialogField.getText()
				.trim());

		result.put("ossj.entity.create", getCreateProperties());
		result.put("ossj.entity.get", getGetProperties());
		result.put("ossj.entity.set", getSetProperties());
		result.put("ossj.entity.remove", getRemoveProperties());
		return result;
	}

	@Override
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

		createArtifactControls(composite, nColumns);
		// createPrimaryKeyControls(composite, nColumns);
		// createAdvancedControls(composite, nColumns);

		setControl(composite);
		Dialog.applyDialogFont(composite);
		// WorkbenchHelp.setHelp(composite,
		// IJavaHelpContextIds.NEW_INTERFACE_WIZARD_PAGE);
	}

	/**
	 * Creates the controls for the superclass name field. Expects a
	 * <code>GridLayout</code> with at least 3 columns.
	 * 
	 * @param composite
	 *            the parent composite
	 * @param nColumns
	 *            number of columns to span
	 */
	protected void createPrimaryKeyControls(Composite composite, int nColumns) {
		primaryKeyClassDialogField.doFillIntoGrid(composite, nColumns);
		Text text = primaryKeyClassDialogField.getTextControl(null);
		LayoutUtil.setWidthHint(text, getMaxFieldWidth());
		ControlContentAssistHelper.createTextContentAssistant(text,
				primaryKeyClassCompletionProcessor);
	}

	protected void createAdvancedControls(Composite composite, int nColumns) {
		DialogField.createEmptySpace(composite, nColumns - 1);
		advancedButton.doFillIntoGrid(composite, 1);
	}

	@Override
	public void initPage(IJavaElement jElement) {
		initFromContext();

		primaryKeyClassDialogField.setText("String");
		setFocus();
		doStatusUpdate();
	}

	@Override
	protected void initFromContext() {
		super.initFromContext();

	}

}