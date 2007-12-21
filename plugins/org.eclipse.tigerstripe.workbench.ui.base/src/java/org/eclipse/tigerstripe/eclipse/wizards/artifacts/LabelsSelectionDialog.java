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
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogFieldGroup;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jdt.ui.wizards.NewContainerWizardPage;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.eclipse.runtime.messages.NewWizardMessages;
import org.eclipse.tigerstripe.eclipse.wizards.artifacts.enums.LabelRef;
import org.eclipse.tigerstripe.eclipse.wizards.artifacts.enums.NewEnumWizardPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.elements.TSMessageDialog;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class LabelsSelectionDialog extends TSMessageDialog {

	private final static String PAGE_NAME = "LabelsSelectionDialog";

	private SelectionButtonDialogFieldGroup fAccMdfButtons;

	private int modifierSelection;

	private StringButtonDialogField labelClassDialogField;

	private String labelClass;

	private IStatus labelClassStatus;

	private LabelRef initialLabelRef;

	private NewContainerWizardPage parentPage;

	private StringDialogField labelNameDialogField;

	private StringDialogField labelValueDialogField;

	private JavaTypeCompletionProcessor labelClassCompletionProcessor;

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
			labelFieldChanged(field);
		}

		public void doubleClicked(ListDialogField field) {
		}
	}

	private List existingLabels;

	private boolean forEdit;

	public LabelsSelectionDialog(Shell parentShell, LabelRef labelRef,
			NewContainerWizardPage parentPage, List existingLabels,
			boolean forEdit) {
		super(parentShell);

		this.forEdit = forEdit;
		this.initialLabelRef = labelRef;
		this.parentPage = parentPage;
		this.existingLabels = existingLabels;

		AttributesFieldsAdapter adapter = new AttributesFieldsAdapter();

		labelNameDialogField = new StringDialogField();
		labelNameDialogField.setDialogFieldListener(adapter);
		labelNameDialogField.setLabelText("Label Name:"); //$NON-NLS-1$

		labelClassDialogField = new StringButtonDialogField(adapter);
		labelClassDialogField.setDialogFieldListener(adapter);
		labelClassDialogField.setLabelText("Label Type:"); //$NON-NLS-1$
		labelClassDialogField.setButtonLabel(NewWizardMessages
				.getString("NewArtifactWizardPage.package.button")); //$NON-NLS-1$
		setEnableCustomType(false);

		labelClassStatus = new StatusInfo();
		labelClassCompletionProcessor = new JavaTypeCompletionProcessor(false,
				false);

		labelValueDialogField = new StringDialogField();
		labelValueDialogField.setDialogFieldListener(adapter);
		labelValueDialogField.setLabelText("Label value:"); //$NON-NLS-1$

		String[] buttonNames1 = new String[] {
				/* 0 == PUBLIC_INDEX */NewWizardMessages
						.getString("NewArtifactWizardPage.attributes.modifiers.public"), //$NON-NLS-1$
				/* 1 == PRIVATE_INDEX */NewWizardMessages
						.getString("NewArtifactWizardPage.attributes.modifiers.private"), //$NON-NLS-1$
				/* 2 == PROTECTED_INDEX */NewWizardMessages
						.getString("NewArtifactWizardPage.attributes.modifiers.protected") //$NON-NLS-1$
		};
		fAccMdfButtons = new SelectionButtonDialogFieldGroup(SWT.RADIO,
				buttonNames1, 3);
		fAccMdfButtons.setDialogFieldListener(adapter);
		fAccMdfButtons
				.setLabelText(NewWizardMessages
						.getString("NewArtifactWizardPage.attributes.modifiers.acc.label")); //$NON-NLS-1$
	}

	public void setEnableCustomType(boolean enable) {
		labelClassDialogField.setEnabled(enable);
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
		createLabelNameControl(composite, nColumns);
		createTypeDetailsControl(composite, nColumns);
		createValueControl(composite, nColumns);
		createModifiersControl(composite, nColumns);
		createCommentControl(composite, nColumns);

		initDialog();

		// WorkbenchHelp.setHelp(composite,
		// IJavaHelpContextIds.NEW_INTERFACE_WIZARD_PAGE);
		return area;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		validateLabel();
	}

	protected boolean validateLabel() {
		Button okButton = this.getButton(IDialogConstants.OK_ID);
		boolean okEnabled = true;

		// Check for valid name
		String labelName = this.labelNameDialogField.getText().trim();
		String labelValue = this.labelValueDialogField.getText().trim();
		String baseType = this.getLabelClass();

		if (labelName == null || labelName.length() == 0) {
			okEnabled = false;
			setMessage("Invalid label name.");
			return okEnabled;
		}

		if (labelName.charAt(0) >= '0' && labelName.charAt(0) <= '9') {
			okEnabled = false;
			setMessage("Invalid label name.");
		}

		char[] invalidChars = { ' ', ';', ',', '-', '{', '}', '[', ']', '(',
				')', '+', '=', '|', '\\', '/', '?', '<', '>', '.', '`', '~',
				'!', '@', '#', '$', '%', '^', '&', '*' };

		for (int i = 0; i < invalidChars.length; i++) {
			if (labelName.indexOf(invalidChars[i]) != -1) {
				okEnabled = false;
				setMessage("Invalid label name.");
			}
		}

		// Check for duplicates
		if (!forEdit) {
			for (Iterator iter = this.existingLabels.iterator(); iter.hasNext();) {
				LabelRef ref = (LabelRef) iter.next();
				if (ref.getName().equals(labelName)) {
					okEnabled = false;
					setMessage("Duplicate label name.");
				}
			}
		}

		// Check for type match between the base type and the value
		if ("int".equals(baseType)) {
			try {
				int i = Integer.parseInt(labelValue);
			} catch (NumberFormatException e) {
				okEnabled = false;
				setMessage("The value is not a valid int.");
			}
		} else if ("java.lang.String".equals(baseType)
				|| "String".equals(baseType)) {
			if (labelValue.length() < 2 || labelValue.charAt(0) != '"'
					|| labelValue.charAt(labelValue.length() - 1) != '"') {
				okEnabled = false;
				setMessage("A String value must be surrounded by \'\"\'. For example, \"myvalue\".");
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

	protected void setDefaultMessage() {
		setMessage("Specify label details.");
	}

	private String findNewFieldName() {

		String result = "literal"
				+ ((NewEnumWizardPage) parentPage).getFieldCounter();
		((NewEnumWizardPage) parentPage)
				.setFieldCounter(((NewEnumWizardPage) parentPage)
						.getFieldCounter() + 1);
		return result;
	}

	protected void initDialog() {
		if (initialLabelRef.getName() != null) {
			labelNameDialogField.setText(initialLabelRef.getName());
		} else {
			// labelNameDialogField.setText("LABEL");
			labelNameDialogField.setText(findNewFieldName());
		}

		if (initialLabelRef.getLabelClass() != null) {
			labelClassDialogField.setText(initialLabelRef.getLabelClass());
		} else {
			labelClassDialogField.setText("String");
		}

		for (int i = 0; i < 3; i++) {
			fAccMdfButtons.setSelection(i, false);
		}
		fAccMdfButtons.setSelection(initialLabelRef.getModifier(), true);

		if (initialLabelRef.getValue() != null) {
			labelValueDialogField.setText(initialLabelRef.getValue());
		} else {
			if (initialLabelRef.getLabelClass().equalsIgnoreCase("int")) {
				labelValueDialogField.setText("0");
			} else {
				labelValueDialogField.setText("\"LABEL\"");
			}
		}
		getShell().setText("Label Details");
		validateLabel();
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

	private void createLabelNameControl(Composite composite, int nColumns) {
		labelNameDialogField.doFillIntoGrid(composite, nColumns - 1);
		DialogField.createEmptySpace(composite);

		LayoutUtil.setWidthHint(labelNameDialogField.getTextControl(null),
				getMaxFieldWidth());
	}

	private void createTypeDetailsControl(Composite composite, int nColumns) {
		labelClassDialogField.doFillIntoGrid(composite, nColumns);
		Text text = labelClassDialogField.getTextControl(null);
		LayoutUtil.setWidthHint(text, getMaxFieldWidth());
		ControlContentAssistHelper.createTextContentAssistant(text,
				labelClassCompletionProcessor);
	}

	private void createValueControl(Composite composite, int nColumns) {
		labelValueDialogField.doFillIntoGrid(composite, nColumns);
		Text text = labelValueDialogField.getTextControl(null);
		// LayoutUtil.setWidthHint(text, convertWidthInCharsToPixels(2));
	}

	private void createModifiersControl(Composite composite, int nColumns) {
		LayoutUtil.setHorizontalSpan(fAccMdfButtons.getLabelControl(composite),
				1);

		Control control = fAccMdfButtons.getSelectionButtonsGroup(composite);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan = nColumns - 2;
		control.setLayoutData(gd);

		DialogField.createEmptySpace(composite);
	}

	private void createCommentControl(Composite composite, int nColumns) {

	}

	protected void attributeChangeControlPressed(DialogField field) {
		if (field == labelClassDialogField) {
			labelClass = chooseAttributeType().getFullyQualifiedName();
			if (labelClass != null) {
				labelClassDialogField.setText(labelClass);
			}
		}
		validateLabel();
	}

	protected void labelFieldChanged(DialogField field) {
		if (field == labelClassDialogField) {
			// TODO need to perform a validation here, but for some
			// reason, the scalar type won't work
			// attributeClassStatus = attributeClassChanged();
			labelClass = labelClassDialogField.getText();
			return;
		} else if (field == labelValueDialogField) {
			// TODO check this is an int
		} else if (field == labelNameDialogField) {
			if ("java.lang.String".equals(labelClassDialogField.getText())
					|| "String".equals(labelClassDialogField.getText())) {
				labelValueDialogField.setText("\""
						+ labelNameDialogField.getText() + "\"");
			}
		}

		for (int i = 0; i < 3; i++) {
			if (fAccMdfButtons.isSelected(i)) {
				modifierSelection = i;
			}
		}
		validateLabel();
	}

	@Override
	protected void okPressed() {
		super.okPressed();

		// populate the attribute
		initialLabelRef.setName(labelNameDialogField.getText());
		initialLabelRef.setLabelClass(labelClass);
		String text = labelValueDialogField.getText();
		initialLabelRef.setValue(text);
		initialLabelRef.setModifier(modifierSelection);
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
	protected IStatus labelClassChanged() {
		StatusInfo status = new StatusInfo();
		IPackageFragmentRoot root = parentPage.getPackageFragmentRoot();
		labelClassDialogField.enableButton(root != null);

		labelClass = null;

		String sclassName = getLabelClass();
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
				labelClass = type.getFullyQualifiedName();
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

		IPackageFragment currPack = null;
		// TODO: This is a total hack! This dialog may be opened by
		// NewEnumWizardPage
		// but also by LabelListDialog. Since the ancestor class are different
		// we need
		// to cast appropriately :-(
		if (parentPage instanceof NewEnumWizardPage) {
			currPack = ((NewEnumWizardPage) parentPage)
					.getArtifactPackageFragment();
		} else {
			currPack = ((NewArtifactWizardPage) parentPage)
					.getArtifactPackageFragment();
		}
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
	public String getLabelClass() {
		return labelClassDialogField.getText();
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
	public void setLabelClass(String name, boolean canBeModified) {
		labelClassDialogField.setText(name);
		labelClassDialogField.setEnabled(canBeModified);
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
		dialog.setFilter(getLabelClass());

		if (dialog.open() == Window.OK)
			return (IType) dialog.getFirstResult();
		return null;
	}

	public LabelRef getLabelRef() {
		return initialLabelRef;
	}
}
