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
package org.eclipse.tigerstripe.eclipse.wizards.artifacts;

import java.util.Iterator;
import java.util.List;

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
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.eclipse.runtime.messages.NewWizardMessages;
import org.eclipse.tigerstripe.eclipse.wizards.artifacts.MethodDetailsDialog.ParameterRef;
import org.eclipse.tigerstripe.workbench.ui.eclipse.elements.TSMessageDialog;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ParametersSelectionDialog extends TSMessageDialog {

	private final static String PAGE_NAME = "ParametersSelectionDialog";

	private StringButtonDialogField attributeClassDialogField;
	private String attributeClass;
	private IStatus attributeClassStatus;

	private MethodDetailsDialog.ParameterRef initialParameterRef;
	private NewArtifactWizardPage parentPage;

	private StringDialogField attributeNameDialogField;
	private StringDialogField attributeDimensionDialogField;
	private JavaTypeCompletionProcessor attributeClassCompletionProcessor;

	private class AttributesFieldsAdapter implements IStringButtonAdapter,
			IDialogFieldListener, IListAdapter {

		// -------- IStringButtonAdapter
		public void changeControlPressed(DialogField field) {
			attributeChangeControlPressed(field);
		}

		// -------- IListAdapter
		public void customButtonPressed(ListDialogField field, int index) {
		}

		public void selectionChanged(ListDialogField field) {
		}

		// -------- IDialogFieldListener
		public void dialogFieldChanged(DialogField field) {
			attributeFieldChanged(field);
		}

		public void doubleClicked(ListDialogField field) {
		}
	}

	private List existingParameters;

	public ParametersSelectionDialog(Shell parentShell,
			MethodDetailsDialog.ParameterRef attributeRef,
			NewArtifactWizardPage parentPage, List existingParameters) {
		super(parentShell);

		this.initialParameterRef = attributeRef;
		this.parentPage = parentPage;
		this.existingParameters = existingParameters;

		AttributesFieldsAdapter adapter = new AttributesFieldsAdapter();

		attributeNameDialogField = new StringDialogField();
		attributeNameDialogField.setDialogFieldListener(adapter);
		attributeNameDialogField.setLabelText("Parameter Name:"); //$NON-NLS-1$

		attributeClassDialogField = new StringButtonDialogField(adapter);
		attributeClassDialogField.setDialogFieldListener(adapter);
		attributeClassDialogField.setLabelText("Attribute Type:"); //$NON-NLS-1$
		attributeClassDialogField.setButtonLabel(NewWizardMessages
				.getString("NewArtifactWizardPage.package.button")); //$NON-NLS-1$

		attributeClassStatus = new StatusInfo();
		attributeClassCompletionProcessor = new JavaTypeCompletionProcessor(
				false, false);

		attributeDimensionDialogField = new StringDialogField();
		attributeDimensionDialogField.setDialogFieldListener(adapter);
		attributeDimensionDialogField.setLabelText("Attribute Dimensions:"); //$NON-NLS-1$

	}

	protected void setDefaultMessage() {
		setMessage("Specify parameter details");
	}

	protected boolean validateParam() {
		Button okButton = this.getButton(IDialogConstants.OK_ID);
		boolean okEnabled = true;

		// Check for valid name
		String parameterName = this.attributeNameDialogField.getText().trim();
		if (parameterName.charAt(0) >= '0' && parameterName.charAt(0) <= '9') {
			okEnabled = false;
			setMessage("Invalid parameter name.");
		}

		char[] invalidChars = { ' ', ';', ',', '-', '{', '}', '[', ']', '(',
				')', '+', '=', '|', '\\', '/', '?', '<', '>', '.', '`', '~',
				'!', '@', '#', '$', '%', '^', '&', '*' };

		for (int i = 0; i < invalidChars.length; i++) {
			if (parameterName.indexOf(invalidChars[i]) != -1) {
				okEnabled = false;
				setMessage("Invalid parameter name.");
			}
		}

		// Check for duplicates
		for (Iterator iter = this.existingParameters.iterator(); iter.hasNext();) {
			ParameterRef ref = (ParameterRef) iter.next();
			if (ref.getName().equals(parameterName)) {
				okEnabled = false;
				setMessage("Duplicate parameter name.");
			}
		}

		// In case this is called before the button is created
		if (okButton != null) {
			okButton.setEnabled(okEnabled);
		}

		if (okEnabled) {
			setDefaultMessage();
		}
		return okEnabled;
	}

	@Override
	public Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new FormLayout());

		initializeDialogUnits(parent);
		Composite composite = new Composite(area, SWT.NONE);
		int nColumns = 3;
		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		createMessageArea(composite, nColumns);
		createAttributeNameControl(composite, nColumns);
		createTypeDetailsControl(composite, nColumns);
		createDimensionControl(composite, nColumns);
		createCommentControl(composite, nColumns);

		initDialog();

		// WorkbenchHelp.setHelp(composite,
		// IJavaHelpContextIds.NEW_INTERFACE_WIZARD_PAGE);
		return area;
	}

	protected void initDialog() {
		if (initialParameterRef.getName() != null) {
			attributeNameDialogField.setText(initialParameterRef.getName());
		} else {
			attributeNameDialogField.setText("aString");
		}

		if (initialParameterRef.getParameterClass() != null) {
			attributeClassDialogField.setText(initialParameterRef
					.getParameterClass());
		} else {
			attributeClassDialogField.setText("String");
		}

		attributeDimensionDialogField.setText(String
				.valueOf(initialParameterRef.getDimensions()));
		getShell().setText("Parameter Details");
		validateParam();
	}

	/**
	 * Returns the recommended maximum width for text fields (in pixels). This
	 * method requires that createContent has been called before this method is
	 * call. Subclasses may override to change the maximum width for text
	 * fields.
	 * 
	 * @return the recommended maximum width for text fields.
	 */
	protected int getMaxFieldWidth() {
		return convertWidthInCharsToPixels(40);
	}

	private void createAttributeNameControl(Composite composite, int nColumns) {
		attributeNameDialogField.doFillIntoGrid(composite, nColumns - 1);
		DialogField.createEmptySpace(composite);

		LayoutUtil.setWidthHint(attributeNameDialogField.getTextControl(null),
				getMaxFieldWidth());
	}

	private void createTypeDetailsControl(Composite composite, int nColumns) {
		attributeClassDialogField.doFillIntoGrid(composite, nColumns);
		Text text = attributeClassDialogField.getTextControl(null);
		LayoutUtil.setWidthHint(text, getMaxFieldWidth());
		ControlContentAssistHelper.createTextContentAssistant(text,
				attributeClassCompletionProcessor);
	}

	private void createDimensionControl(Composite composite, int nColumns) {
		attributeDimensionDialogField.doFillIntoGrid(composite, nColumns);
		Text text = attributeDimensionDialogField.getTextControl(null);
		text.setTextLimit(2);
		LayoutUtil.setWidthHint(text, convertWidthInCharsToPixels(2));
	}

	private void createCommentControl(Composite composite, int nColumns) {

	}

	protected void attributeChangeControlPressed(DialogField field) {
		if (field == attributeClassDialogField) {
			attributeClass = chooseAttributeType().getFullyQualifiedName();
			if (attributeClass != null) {
				attributeClassDialogField.setText(attributeClass);
			}
		}
		validateParam();
	}

	protected void attributeFieldChanged(DialogField field) {
		if (field == attributeClassDialogField) {
			// TODO need to perform a validation here, but for some
			// reason, the scalar type won't work
			// attributeClassStatus = attributeClassChanged();
			attributeClass = attributeClassDialogField.getText();
		} else if (field == attributeDimensionDialogField) {
			// TODO check this is an int
		}
		validateParam();
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		validateParam();
	}

	@Override
	protected void okPressed() {
		super.okPressed();

		// populate the attribute
		initialParameterRef.setName(attributeNameDialogField.getText());
		initialParameterRef.setParameterClass(attributeClass);
		String text = attributeDimensionDialogField.getText();

		initialParameterRef.setDimensions(Integer.parseInt(text));
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
	protected IStatus attributeClassChanged() {
		StatusInfo status = new StatusInfo();
		IPackageFragmentRoot root = parentPage.getPackageFragmentRoot();
		attributeClassDialogField.enableButton(root != null);

		attributeClass = null;

		String sclassName = getAttributeClass();
		if (sclassName.length() == 0)
			// accept the empty field (stands for java.lang.Object)
			return status;
		IStatus val = JavaConventions.validateJavaTypeName(sclassName);
		if (val.getSeverity() == IStatus.ERROR) {
			status
					.setError(NewWizardMessages
							.getString("NewArtifactWizardPage.attributes.error.InvalidClassName")); //$NON-NLS-1$
			return status;
		}
		if (root != null) {
			try {
				IType type = resolveAttributeTypeName(root.getJavaProject(),
						sclassName);
				if (type == null) {
					status
							.setWarning(NewWizardMessages
									.getString("NewArtifactWizardPage.attributes.warning.ClassNotExists")); //$NON-NLS-1$
					return status;
				}
				attributeClass = type.getFullyQualifiedName();
			} catch (JavaModelException e) {
				status
						.setError(NewWizardMessages
								.getString("NewArtifactWizardPage.attributes.error.InvalidClassName")); //$NON-NLS-1$
				JavaPlugin.log(e);
			}
		} else {
			status.setError(""); //$NON-NLS-1$
		}
		return status;

	}

	private IType resolveAttributeTypeName(IJavaProject jproject,
			String sclassName) throws JavaModelException {
		if (!jproject.exists())
			return null;
		IType type = null;
		IPackageFragment currPack = parentPage.getArtifactPackageFragment();
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
	public String getAttributeClass() {
		return attributeClassDialogField.getText();
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
	public void setAttributeClass(String name, boolean canBeModified) {
		attributeClassDialogField.setText(name);
		attributeClassDialogField.setEnabled(canBeModified);
	}

	private IType chooseAttributeType() {
		IPackageFragmentRoot root = parentPage.getPackageFragmentRoot();
		if (root == null)
			return null;
		IJavaElement[] elements = new IJavaElement[] { root.getJavaProject() };
		IJavaSearchScope scope = SearchEngine.createJavaSearchScope(elements);

		TypeSelectionDialog2 dialog = new TypeSelectionDialog2(getShell(),
				false, null, scope, IJavaSearchConstants.TYPE);
		dialog
				.setTitle(NewWizardMessages
						.getString("NewArtifactWizardPage.attributes.SuperClassDialog.title")); //$NON-NLS-1$
		dialog
				.setMessage(NewWizardMessages
						.getString("NewArtifactWizardPage.attributes.SuperClassDialog.message")); //$NON-NLS-1$
		dialog.setFilter(getAttributeClass());

		if (dialog.open() == Window.OK)
			return (IType) dialog.getFirstResult();
		return null;
	}

	public MethodDetailsDialog.ParameterRef getParameterRef() {
		return initialParameterRef;
	}
}
