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
package org.eclipse.tigerstripe.eclipse.wizards.artifacts.association;

import java.io.Writer;
import java.util.Arrays;
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
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.eclipse.wizards.artifacts.ArtifactDefinitionGenerator;
import org.eclipse.tigerstripe.eclipse.wizards.artifacts.NewArtifactWizardPage;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EnumArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EventArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.QueryArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs.BrowseForArtifactDialog;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class NewAssociationWizardPage extends NewArtifactWizardPage {

	private final static String PAGE_NAME = "NewAssociationWizardPage";

	private final static String AEND_TYPE = PAGE_NAME + ".aEndType";

	private final static String ZEND_TYPE = PAGE_NAME + ".zEndType";

	private StringButtonDialogField aEndTypeClassDialogField;

	private StringButtonDialogField zEndTypeClassDialogField;

	private IStatus aEndTypeClassStatus;

	private IStatus zEndTypeClassStatus;

	private JavaTypeCompletionProcessor aEndTypeClassCompletionProcessor;

	private JavaTypeCompletionProcessor zEndTypeClassCompletionProcessor;

	@Override
	public IAbstractArtifact[] getArtifactModelForExtend() {
		return new IAbstractArtifact[] { AssociationArtifact.MODEL };
	}

	public NewAssociationWizardPage() {
		super(PAGE_NAME);

		setTitle("Association Artifact");
		setDescription("Create a new Association Artifact.");

		EntityFieldsAdapter adapter = new EntityFieldsAdapter();

		aEndTypeClassDialogField = new StringButtonDialogField(adapter);
		aEndTypeClassDialogField.setDialogFieldListener(adapter);
		aEndTypeClassDialogField.setLabelText("aEnd Type"); //$NON-NLS-1$
		aEndTypeClassDialogField.setButtonLabel("Browse"); //$NON-NLS-1$

		aEndTypeClassStatus = new StatusInfo();
		aEndTypeClassCompletionProcessor = new JavaTypeCompletionProcessor(
				false, false);

		zEndTypeClassDialogField = new StringButtonDialogField(adapter);
		zEndTypeClassDialogField.setDialogFieldListener(adapter);
		zEndTypeClassDialogField.setLabelText("zEnd Type"); //$NON-NLS-1$
		zEndTypeClassDialogField.setButtonLabel("Browse"); //$NON-NLS-1$

		zEndTypeClassStatus = new StatusInfo();
		zEndTypeClassCompletionProcessor = new JavaTypeCompletionProcessor(
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
		if (field == aEndTypeClassDialogField) {
			IType type = chooseReturnedType();
			if (type != null) {
				aEndTypeClassDialogField.setText(JavaModelUtil
						.getFullyQualifiedName(type));
			}
		} else if (field == zEndTypeClassDialogField) {
			IType type = chooseReturnedType();
			if (type != null) {
				zEndTypeClassDialogField.setText(JavaModelUtil
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
		if (field == aEndTypeClassDialogField) {
			aEndTypeClassStatus = aEndTypeClassChanged();
			fieldName = AEND_TYPE;
		} else if (field == zEndTypeClassDialogField) {
			zEndTypeClassStatus = zEndTypeClassChanged();
			fieldName = ZEND_TYPE;
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
	protected IStatus aEndTypeClassChanged() {
		StatusInfo status = new StatusInfo();
		IPackageFragmentRoot root = getPackageFragmentRoot();
		aEndTypeClassDialogField.enableButton(root != null);

		String sclassName = getAendTypeClass();
		if (sclassName.length() == 0) {
			status.setError("Please select a valid aEnd Class."); //$NON-NLS-1$
			return status;
		}
		IStatus val = JavaConventions.validateJavaTypeName(sclassName);
		if (val.getSeverity() == IStatus.ERROR) {
			status.setError("Invalid aEnd Class."); //$NON-NLS-1$
			return status;
		}
		if (root != null) {
			try {
				IType type = resolveReturnedTypeName(root.getJavaProject(),
						sclassName);
				if (type == null) {
					status.setWarning("Warning: the aEnd doesn't exist."); //$NON-NLS-1$
					return status;
				}
			} catch (JavaModelException e) {
				status.setError("Invalid aEnd class."); //$NON-NLS-1$
				JavaPlugin.log(e);
			}
		} else {
			status.setError(""); //$NON-NLS-1$
		}
		return status;

	}

	protected IStatus zEndTypeClassChanged() {
		StatusInfo status = new StatusInfo();
		IPackageFragmentRoot root = getPackageFragmentRoot();
		zEndTypeClassDialogField.enableButton(root != null);

		String sclassName = getZendTypeClass();
		if (sclassName.length() == 0) {
			status.setError("Please select a valid zEnd Class."); //$NON-NLS-1$
			return status;
		}
		IStatus val = JavaConventions.validateJavaTypeName(sclassName);
		if (val.getSeverity() == IStatus.ERROR) {
			status.setError("Invalid zEnd Class."); //$NON-NLS-1$
			return status;
		}
		if (root != null) {
			try {
				IType type = resolveReturnedTypeName(root.getJavaProject(),
						sclassName);
				if (type == null) {
					status.setWarning("Warning: the zEnd doesn't exist."); //$NON-NLS-1$
					return status;
				}
			} catch (JavaModelException e) {
				status.setError("Invalid zEnd class."); //$NON-NLS-1$
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
	public String getAendTypeClass() {
		return aEndTypeClassDialogField.getText();
	}

	/**
	 * Returns the content of the superclass input field.
	 * 
	 * @return the superclass name
	 */
	public String getZendTypeClass() {
		return zEndTypeClassDialogField.getText();
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
	public void setAendTypeClass(String name, boolean canBeModified) {
		aEndTypeClassDialogField.setText(name);
		aEndTypeClassDialogField.setEnabled(canBeModified);
	}

	public void setZendTypeClass(String name, boolean canBeModified) {
		zEndTypeClassDialogField.setText(name);
		zEndTypeClassDialogField.setEnabled(canBeModified);
	}

	/**
	 * Choose the returned type through a dialog. The dialog only shows Entity
	 * Artifact
	 * 
	 * @return
	 */
	private IType chooseReturnedType() {
		try {
			BrowseForArtifactDialog dialog = new BrowseForArtifactDialog(
					getTSRuntimeContext().getProjectHandle(),
					new IAbstractArtifact[] { ManagedEntityArtifact.MODEL,
							DatatypeArtifact.MODEL, QueryArtifact.MODEL,
							UpdateProcedureArtifact.MODEL,
							SessionFacadeArtifact.MODEL,
							ExceptionArtifact.MODEL, EventArtifact.MODEL,
							EnumArtifact.MODEL, AssociationClassArtifact.MODEL });
			dialog.setTitle("Association End Type");
			dialog.setMessage("Select the type of the Association End.");

			IAbstractArtifact[] selectedArtifacts = dialog
					.browseAvailableArtifacts(getShell(), Arrays
							.asList(new Object[0]));
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
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		return null;
	}

	@Override
	public Properties getProperties() {
		Properties result = getPropertiesCommons();
		result.put("aEndType", aEndTypeClassDialogField.getText().trim());
		result.put("aEndName", "_aEnd");
		result.put("aEndIsNavigable", "false");
		result.put("aEndIsOrdered", "false");
		result.put("aEndAggregation", "none");
		result.put("aEndChangeable", "none");
		result.put("aEndMultiplicity", "0..1");

		result.put("zEndType", zEndTypeClassDialogField.getText().trim());
		result.put("zEndName", "_zEnd");
		result.put("zEndIsNavigable", "false");
		result.put("zEndIsOrdered", "false");
		result.put("zEndAggregation", "none");
		result.put("zEndChangeable", "none");
		result.put("zEndMultiplicity", "0..1");
		return result;
	}

	@Override
	protected ArtifactDefinitionGenerator getGenerator(
			Properties pageProperties, Writer writer) {
		return new AssociationDefinitionGenerator(pageProperties, writer);
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);

		int nColumns = 4;

		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		createArtifactControls(composite, nColumns, true, false);
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
		aEndTypeClassDialogField.doFillIntoGrid(composite, nColumns);
		Text text = aEndTypeClassDialogField.getTextControl(null);
		LayoutUtil.setWidthHint(text, getMaxFieldWidth());
		ControlContentAssistHelper.createTextContentAssistant(text,
				aEndTypeClassCompletionProcessor);

		zEndTypeClassDialogField.doFillIntoGrid(composite, nColumns);
		text = zEndTypeClassDialogField.getTextControl(null);
		LayoutUtil.setWidthHint(text, getMaxFieldWidth());
		ControlContentAssistHelper.createTextContentAssistant(text,
				zEndTypeClassCompletionProcessor);
	}

	@Override
	public void initPage(IJavaElement jElement) {
		initFromContext();

		aEndTypeClassStatus = aEndTypeClassChanged();
		zEndTypeClassStatus = zEndTypeClassChanged();

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
				fContainerStatus, artifactNameStatus, extendedClassStatus,
				artifactPackageStatus, aEndTypeClassStatus, zEndTypeClassStatus };

		// the mode severe status will be displayed and the ok button
		// enabled/disabled.
		updateStatus(status);
	}

	@Override
	protected void handleFieldChanged(String fieldName) {
		super.handleFieldChanged(fieldName);

		doStatusUpdate();
	}

	@Override
	protected void createAttributeListControl(Composite composite, int nColumns) {
		// by doing this, the attributes controls don't appear
	}

	@Override
	protected void createExtendedArtifactControls(Composite composite,
			int nColumns) {
		// by doing this, the extends control doesn't appear
	}
}