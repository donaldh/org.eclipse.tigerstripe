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
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.query;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.ControlContentAssistHelper;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.JavaTypeCompletionProcessor;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.metamodel.impl.IQueryArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.QueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.internal.runtime.messages.NewWizardMessages;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.ArtifactDefinitionGenerator;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.ArtifactSelectionDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.NewArtifactWizardPage;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class NewQueryWizardPage extends NewArtifactWizardPage {

	private final static String PAGE_NAME = "NewQueryWizardPage";

	private final static String RETURNED_TYPE = PAGE_NAME + ".returnedType";

	private StringButtonDialogField returnedTypeClassDialogField;

	private IStatus returnedTypeClassStatus;

	private JavaTypeCompletionProcessor returnedTypeClassCompletionProcessor;

	@Override
	public IAbstractArtifact[] getArtifactModelForExtend() {
		return new IAbstractArtifact[] { QueryArtifact.MODEL };
	}

	public NewQueryWizardPage() {
		super(PAGE_NAME);

		setTitle(ArtifactMetadataFactory.INSTANCE.getMetadata(
				IQueryArtifactImpl.class.getName()).getLabel(null)
				+ " Artifact");
		setDescription("Create a new "
				+ ArtifactMetadataFactory.INSTANCE.getMetadata(
						IQueryArtifactImpl.class.getName()).getLabel(null)
				+ " Artifact.");

		EntityFieldsAdapter adapter = new EntityFieldsAdapter();

		returnedTypeClassDialogField = new StringButtonDialogField(adapter);
		returnedTypeClassDialogField.setDialogFieldListener(adapter);
		returnedTypeClassDialogField.setLabelText("Returned Type"); //$NON-NLS-1$
		returnedTypeClassDialogField.setButtonLabel(NewWizardMessages
				.getString("NewQueryWizardPage.returnedEntity.button")); //$NON-NLS-1$

		returnedTypeClassStatus = new StatusInfo();
		returnedTypeClassCompletionProcessor = new JavaTypeCompletionProcessor(
				false, false);

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
		if (field == returnedTypeClassDialogField) {
			IType type = chooseReturnedType();
			if (type != null) {
				returnedTypeClassDialogField.setText(type.getFullyQualifiedName());
			}
		}
	}

	/*
	 * A field on the type has changed. The fields' status and all dependent
	 * status are updated.
	 */
	private void entityPageDialogFieldChanged(DialogField field) {
		String fieldName = null;
		if (field == returnedTypeClassDialogField) {
			returnedTypeClassStatus = returnedTypeClassChanged();
			fieldName = RETURNED_TYPE;
		}
		// tell all others
		handleFieldChanged(fieldName);
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
	protected IStatus returnedTypeClassChanged() {
		StatusInfo status = new StatusInfo();
		IPackageFragmentRoot root = getPackageFragmentRoot();
		returnedTypeClassDialogField.enableButton(root != null);

		String sclassName = getReturnedTypeClass();
		if (sclassName.length() == 0)
			// accept the empty field (stands for java.lang.Object)
			return status;
		IStatus val = JavaConventions.validateJavaTypeName(sclassName);
		if (val.getSeverity() == IStatus.ERROR) {
			status.setError("Invalid Returned Entity Class."); //$NON-NLS-1$
			return status;
		}
		if (root != null) {
			try {
				IType type = resolveReturnedTypeName(root.getJavaProject(),
						sclassName);
				if (type == null) {
					status
							.setWarning("Warning: the returned entity doesn't exist."); //$NON-NLS-1$
					return status;
				}
			} catch (JavaModelException e) {
				status.setError("Invalid returned entity class."); //$NON-NLS-1$
				JavaPlugin.log(e);
			}
		} else {
			status.setError(""); //$NON-NLS-1$
		}
		return status;

	}

	private IType resolveReturnedTypeName(IJavaProject jproject,
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
	public String getReturnedTypeClass() {
		return returnedTypeClassDialogField.getText();
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
	public void setReturnedTypeClass(String name, boolean canBeModified) {
		returnedTypeClassDialogField.setText(name);
		returnedTypeClassDialogField.setEnabled(canBeModified);
	}

	/**
	 * Choose the returned type through a dialog. The dialog only shows Entity
	 * Artifact
	 * 
	 * @return
	 */
	private IType chooseReturnedType() {

		ArtifactSelectionDialog dialog = new ArtifactSelectionDialog(
				getInitialElement(), ManagedEntityArtifact.MODEL);

		dialog.setTitle("Returned Entity Type");
		dialog
				.setMessage("Please selected the returned entity type for this query.");

		// TSRuntimeContext context = getTSRuntimeContext();
		// try {
		// if ( context.getProject() != null ) {
		// context.getProject().reload();
		// }
		// } catch ( TigerstripeException e ) {
		// TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
		// e);
		// }
		//		
		AbstractArtifact[] selectedArtifacts = dialog.browseAvailableArtifacts(
				getShell(), new ArrayList(), getTSRuntimeContext());

		if (selectedArtifacts.length == 0)
			return null;
		else {
			String classname = selectedArtifacts[0].getFullyQualifiedName();
			try {
				return resolveReturnedTypeName(getInitialElement()
						.getJavaProject(), classname);
			} catch (JavaModelException e) {
				TigerstripeRuntime.logErrorMessage(
						"JavaModelException detected", e);
			}
		}
		return null;

		// IPackageFragmentRoot root = getPackageFragmentRoot();
		// if (root == null) {
		// return null;
		// }
		//
		// IJavaElement[] elements = new IJavaElement[] { root.getJavaProject()
		// };
		// IJavaSearchScope scope =
		// SearchEngine.createJavaSearchScope(elements);
		//
		// TypeSelectionDialog dialog = new TypeSelectionDialog(getShell(),
		// getWizard().getContainer(), IJavaSearchConstants.CLASS, scope);
		// dialog.setTitle(NewWizardMessages
		// .getString("NewTypeWizardPage.SuperClassDialog.title"));
		// //$NON-NLS-1$
		// dialog.setMessage(NewWizardMessages
		// .getString("NewTypeWizardPage.SuperClassDialog.message"));
		// //$NON-NLS-1$
		// dialog.setFilter(getReturnedTypeClass());
		//
		//		
		// if (dialog.open() == Window.OK) {
		// return (IType) dialog.getFirstResult();
		// }
		// return null;
	}

	public final static String QUERIED_ENTITY = "queryReturn";

	@Override
	public Properties getProperties() {
		Properties result = getPropertiesCommons();
		result.put(QUERIED_ENTITY, returnedTypeClassDialogField.getText()
				.trim());
		return result;
	}

	@Override
	protected ArtifactDefinitionGenerator getGenerator(
			Properties pageProperties, Writer writer) {
		return new QueryDefinitionGenerator(pageProperties, writer);
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);

		int nColumns = 4;

		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		createArtifactControls(composite, nColumns, true, false,true);
		createSeparator(composite, nColumns);
		createReturnedTypeControls(composite, nColumns);

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
	protected void createReturnedTypeControls(Composite composite, int nColumns) {
		returnedTypeClassDialogField.doFillIntoGrid(composite, nColumns);
		Text text = returnedTypeClassDialogField.getTextControl(null);
		LayoutUtil.setWidthHint(text, getMaxFieldWidth());
		ControlContentAssistHelper.createTextContentAssistant(text,
				returnedTypeClassCompletionProcessor);
	}

	@Override
	public void initPage(IJavaElement jElement) {
		initFromContext();

		setFocus();
		doStatusUpdate();
	}

	@Override
	protected void initFromContext() {
		super.initFromContext();

	}

	@Override
	protected void doStatusUpdate() {
		// status of all used components
		IStatus[] status = new IStatus[] { tsRuntimeContextStatus,
				fContainerStatus, artifactNameStatus, artifactPackageStatus,
				extendedClassStatus, returnedTypeClassStatus };

		// the mode severe status will be displayed and the ok button
		// enabled/disabled.
		updateStatus(status);
	}

	@Override
	protected void handleFieldChanged(String fieldName) {
		super.handleFieldChanged(fieldName);

		doStatusUpdate();
	}
}