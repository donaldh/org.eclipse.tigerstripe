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
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
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
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogFieldGroup;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.eclipse.runtime.messages.NewWizardMessages;
import org.eclipse.tigerstripe.eclipse.wizards.artifacts.NewArtifactWizardPage.MethodDetailsModel;
import org.eclipse.tigerstripe.eclipse.wizards.artifacts.entity.NewEntityWizardPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.elements.MethodPropertiesDialog;
import org.eclipse.tigerstripe.workbench.ui.eclipse.elements.TSMessageDialog;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class MethodDetailsDialog extends TSMessageDialog {

	private final static String PAGE_NAME = "MethodDetailsDialog";

	private final static String METHOD_RETURNTYPE = PAGE_NAME + ".returnType";

	private SelectionButtonDialogField advancedButton;

	private SelectionButtonDialogFieldGroup fAccMdfButtons;

	// The "void" button
	private SelectionButtonDialogFieldGroup voidButton;

	private NewArtifactWizardPage.MethodDetailsModel initialModel;

	private StringButtonDialogField returnClassDialogField;

	private String returnClass;

	private IStatus returnClassStatus;

	private JavaTypeCompletionProcessor returnClassCompletionProcessor;

	private NewArtifactWizardPage parentPage;

	private StringDialogField methodNameDialogField;

	private StringDialogField returnDimensionDialogField;

	private int modifierSelection;

	// the attribute list selection
	private ListDialogField parametersDialogField;

	private StringDialogField signatureDialogField;

	// the attribute list selection
	private ListDialogField exceptionsDialogField;

	private class MethodFieldsAdapter implements IStringButtonAdapter,
			IDialogFieldListener, IListAdapter {

		// -------- IStringButtonAdapter
		public void changeControlPressed(DialogField field) {
			methodChangeControlPressed(field);
		}

		// -------- IListAdapter
		public void customButtonPressed(ListDialogField field, int index) {
			methodCustomButtonPressed(field, index);
		}

		public void selectionChanged(ListDialogField field) {
		}

		// -------- IDialogFieldListener
		public void dialogFieldChanged(DialogField field) {
			methodFieldChanged(field);
		}

		public void doubleClicked(ListDialogField field) {
		}
	}

	private class ExceptionsListLabelProvider extends LabelProvider {

		private Image exceptionImage;

		public ExceptionsListLabelProvider() {
			super();
			exceptionImage = JavaPluginImages
					.get(JavaPluginImages.IMG_FIELD_DEFAULT);
		}

		@Override
		public Image getImage(Object element) {
			return exceptionImage;
		}

		@Override
		public String getText(Object element) {
			if (element == null)
				return "unknown";

			ExceptionRef exceptionRef = (ExceptionRef) element;
			return exceptionRef.getExceptionClass();
		}
	}

	public class ExceptionRef {
		private String exceptionClass;

		@Override
		public Object clone() {
			ExceptionRef result = new ExceptionRef();
			result.applyValues(this);
			return result;
		}

		public void applyValues(ExceptionRef ref) {
			this.exceptionClass = ref.getExceptionClass();
		}

		public String getExceptionClass() {
			return exceptionClass;
		}

		public void setExceptionClass(String exceptionClass) {
			this.exceptionClass = exceptionClass;
		}
	}

	private class ParametersListLabelProvider extends LabelProvider {

		private Image parameterImage;

		public ParametersListLabelProvider() {
			super();
			parameterImage = JavaPluginImages
					.get(JavaPluginImages.IMG_FIELD_DEFAULT);
		}

		@Override
		public Image getImage(Object element) {
			return parameterImage;
		}

		@Override
		public String getText(Object element) {
			if (element == null)
				return "unknown";

			ParameterRef paramRef = (ParameterRef) element;
			return paramRef.getSignature();
		}
	}

	public class ParameterRef {

		private String name;

		private String parameterClass;

		private int dimensions;

		@Override
		public Object clone() {
			ParameterRef result = new ParameterRef();
			result.applyValues(this);
			return result;
		}

		public boolean matches(ParameterRef other) {
			boolean result = false;

			if (other.getParameterClass().equals(getParameterClass())
					&& other.getDimensions() == getDimensions()) {
				result = true;
			}
			return result;
		}

		public String getSignature() {
			String signature = getParameterClass();

			for (int i = 0; i < dimensions; i++) {
				signature = signature + "[]";
			}

			signature = signature + " " + getName();

			return signature;
		}

		public void applyValues(ParameterRef ref) {
			this.name = ref.getName();
			this.parameterClass = ref.getParameterClass();
			this.dimensions = ref.getDimensions();
		}

		public String getParameterClass() {
			return parameterClass;
		}

		public void setParameterClass(String attributeClass) {
			this.parameterClass = attributeClass;
		}

		public int getDimensions() {
			return dimensions;
		}

		public void setDimensions(int dimensions) {
			this.dimensions = dimensions;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	private List existingMethods;

	private boolean forEdit;

	private MethodPropertiesDialog methodPropertiesDialog;

	public MethodDetailsDialog(Shell parentShell,
			NewArtifactWizardPage.MethodDetailsModel methodModel,
			NewArtifactWizardPage parentPage, List existingMethods,
			boolean forEdit) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE);

		this.existingMethods = existingMethods;
		this.forEdit = forEdit;

		// this.setTitle("Method Definition Dialog");
		// this.setMessage("Please add methods to this artifact.");

		this.initialModel = methodModel;
		this.parentPage = parentPage;

		MethodFieldsAdapter adapter = new MethodFieldsAdapter();

		methodNameDialogField = new StringDialogField();
		methodNameDialogField.setDialogFieldListener(adapter);
		methodNameDialogField.setLabelText("Method Name:"); //$NON-NLS-1$

		signatureDialogField = new StringDialogField();
		signatureDialogField.setEnabled(false);
		signatureDialogField.setLabelText("Signature:"); //$NON-NLS-1$

		String[] voidButtonName = new String[] { /* 0 == VOID_INDEX */"Void" }; //$NON-NLS-1$

		voidButton = new SelectionButtonDialogFieldGroup(SWT.CHECK,
				voidButtonName, 4);
		voidButton.setDialogFieldListener(adapter);
		voidButton.setLabelText(NewWizardMessages
				.getString("NewArtifactWizardPage.modifier.generate.label")); //$NON-NLS-1$
		voidButton.setSelection(0, true);

		returnClassDialogField = new StringButtonDialogField(adapter);
		returnClassDialogField.setDialogFieldListener(adapter);
		returnClassDialogField.setLabelText("Return Type:"); //$NON-NLS-1$
		returnClassDialogField.setButtonLabel(NewWizardMessages
				.getString("NewArtifactWizardPage.package.button")); //$NON-NLS-1$

		returnClassStatus = new StatusInfo();
		returnClassCompletionProcessor = new JavaTypeCompletionProcessor(false,
				false);

		returnDimensionDialogField = new StringDialogField();
		returnDimensionDialogField.setDialogFieldListener(adapter);
		returnDimensionDialogField.setLabelText("Return Type Dimension:"); //$NON-NLS-1$

		// The attribute list selection
		String[] addButtons = new String[] { /* 0 */"add", //$NON-NLS-1$
				/* 1 */"edit", /* 2 */"remove" //$NON-NLS-1$
		};

		exceptionsDialogField = new ListDialogField(adapter, addButtons,
				new ExceptionsListLabelProvider());
		exceptionsDialogField.setDialogFieldListener(adapter);
		String pluginsLabel = "Exceptions:";

		exceptionsDialogField.setLabelText(pluginsLabel);
		exceptionsDialogField.setRemoveButtonIndex(2);

		parametersDialogField = new ListDialogField(adapter, addButtons,
				new ParametersListLabelProvider());
		parametersDialogField.setDialogFieldListener(adapter);
		String paramsLabel = "Parameters:";

		parametersDialogField.setLabelText(paramsLabel);
		parametersDialogField.setRemoveButtonIndex(2);

		advancedButton = new SelectionButtonDialogField(SWT.PUSH);
		advancedButton.setDialogFieldListener(adapter);
		advancedButton.setLabelText("Advanced");

		// Only an entity method can have this advanced button
		if (!(this.parentPage instanceof NewEntityWizardPage)) {
			advancedButton.setEnabled(false);
		} else {
			advancedButton.setEnabled(true);
		}

		String[] buttonNames1 = new String[] {
				/* 0 == PUBLIC_INDEX */NewWizardMessages
						.getString("NewArtifactWizardPage.attributes.modifiers.public"), //$NON-NLS-1$
				/* 1 == PRIVATE_INDEX */NewWizardMessages
						.getString("NewArtifactWizardPage.attributes.modifiers.private"), //$NON-NLS-1$
				/* 2 == PROTECTED_INDEX */NewWizardMessages
						.getString("NewArtifactWizardPage.attributes.modifiers.protected") //$NON-NLS-1$
		};
		fAccMdfButtons = new SelectionButtonDialogFieldGroup(SWT.RADIO,
				buttonNames1, 4);
		fAccMdfButtons.setDialogFieldListener(adapter);
		fAccMdfButtons
				.setLabelText(NewWizardMessages
						.getString("NewArtifactWizardPage.attributes.modifiers.acc.label")); //$NON-NLS-1$

		initMethodPropertiesDialog();
	}

	/**
	 * Initialize the dialog for the method ossj props and the ossjPros
	 * themselves.
	 * 
	 */
	private void initMethodPropertiesDialog() {
		// 
		String methodLabels[] = { this.methodNameDialogField.getText().trim() // index
		// = 0
		};

		Properties methodProperties[] = { this.getMethodDetailsModel()
				.getOssjProperties() };

		methodPropertiesDialog = new MethodPropertiesDialog(getShell(),
				methodLabels, methodProperties, "Method Signature",
				this.signatureDialogField.getText().trim());

		this.getMethodDetailsModel().setOssjProperties(
				methodPropertiesDialog.getReturnProperties(0));
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
		createMethodNameControl(composite, nColumns);
		createReturnTypeDetailsControl(composite, nColumns);
		createParametersListControl(composite, nColumns);
		createExceptionsListControl(composite, nColumns);
		createModifiersControl(composite, nColumns);
		createCommentControl(composite, nColumns);
		createSignatureControl(composite, nColumns);
		createAdvancedControls(composite, nColumns);

		initDialog();

		// WorkbenchHelp.setHelp(composite,
		// IJavaHelpContextIds.NEW_INTERFACE_WIZARD_PAGE);
		return area;
	}

	protected boolean validateMethod() {

		Button okButton = this.getButton(IDialogConstants.OK_ID);
		boolean okEnabled = true;

		// In case this is called before the button is created
		if (okButton != null) {
			okButton.setEnabled(okEnabled);
		}
		// Check for valid name
		String methodName = this.methodNameDialogField.getText().trim();
		if (methodName.charAt(0) >= '0' && methodName.charAt(0) <= '9') {
			okEnabled = false;
			setMessage("Invalid method name.");
		}

		char[] invalidChars = { ' ', ';', ',', '-', '{', '}', '[', ']', '(',
				')', '+', '=', '|', '\\', '/', '?', '<', '>', '.', '`', '~',
				'!', '@', '#', '$', '%', '^', '&', '*' };

		for (int i = 0; i < invalidChars.length; i++) {
			if (methodName.indexOf(invalidChars[i]) != -1) {
				okEnabled = false;
				setMessage("Invalid method name.");
			}
		}

		if (!forEdit) {
			if (isDuplicateMethod(this.existingMethods)) {
				okEnabled = false;
				setMessage("Duplicate method definition.");
			}
		}

		if (okEnabled) {
			setDefaultMessage();
		} else {
			if (okButton != null) {
				okButton.setEnabled(false);
			}
		}
		return okEnabled;
	}

	private boolean isDuplicateMethod(List existingMethods) {
		boolean result = false;
		for (Iterator iter = this.existingMethods.iterator(); iter.hasNext();) {
			MethodDetailsModel ref = (MethodDetailsModel) iter.next();

			// Compare name
			if (ref.getMethodName().equals(
					this.methodNameDialogField.getText().trim())) {

				// So let's compare the profile then
				List refParamList = ref.getParameterList();
				List paramList = this.parametersDialogField.getElements();

				if (refParamList.size() == paramList.size()) {
					boolean keepGoing = true;
					for (Iterator refIter = refParamList.iterator(); refIter
							.hasNext()
							&& keepGoing;) {
						ParameterRef refParam = (ParameterRef) refIter.next();
						for (Iterator paramIter = paramList.iterator(); paramIter
								.hasNext()
								&& keepGoing;) {
							ParameterRef param = (ParameterRef) paramIter
									.next();
							keepGoing = param.matches(refParam);
						}
					}
					if (keepGoing) {
						result = true;
						setMessage("Duplicate attribute name.");
					}
				}
			}
		}
		return result;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		validateMethod();
	}

	protected void initDialog() {

		if (initialModel.getMethodName() != null) {
			methodNameDialogField.setText(initialModel.getMethodName());
		} else {
			methodNameDialogField.setText("aMethod");
		}

		voidButton.setSelection(0, initialModel.isVoid());

		if (initialModel.getReturnClass() != null) {
			returnClassDialogField.setText(initialModel.getReturnClass());
		} else {
			returnClassDialogField.setText("String");
		}

		returnDimensionDialogField.setText(String.valueOf(initialModel
				.getReturnDimension()));
		fAccMdfButtons.setSelection(initialModel.getMethodModifier(), true);

		if (initialModel.getParameterList() != null) {
			parametersDialogField.setElements(initialModel.getParameterList());
		}
		// if ( initialAttributeRef.getName() != null ) {
		// attributeNameDialogField.setText( initialAttributeRef.getName() );
		// } else {
		// attributeNameDialogField.setText( "aString" );
		// }
		//		
		// if ( initialAttributeRef.getAttributeClass() != null ) {
		// attributeClassDialogField.setText(
		// initialAttributeRef.getAttributeClass() );
		// } else {
		// attributeClassDialogField.setText( "java.lang.String" );
		// }
		//
		// fAccMdfButtons.setSelection( initialAttributeRef.getModifier(),
		// true);
		// attributeDimensionDialogField.setText(
		// String.valueOf(initialAttributeRef.getDimensions()));

		setDefaultMessage();
		getShell().setText("Method Details");
		updatePage();
	}

	protected void setDefaultMessage() {
		setMessage("Specify method details...");
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

	private void createMethodNameControl(Composite composite, int nColumns) {
		methodNameDialogField.doFillIntoGrid(composite, nColumns - 1);
		DialogField.createEmptySpace(composite);

		LayoutUtil.setWidthHint(methodNameDialogField.getTextControl(null),
				getMaxFieldWidth());
	}

	private void createSignatureControl(Composite composite, int nColumns) {
		signatureDialogField.doFillIntoGrid(composite, nColumns - 1);
		DialogField.createEmptySpace(composite);

		LayoutUtil.setWidthHint(signatureDialogField.getTextControl(null),
				getMaxFieldWidth());
	}

	private void createReturnTypeDetailsControl(Composite composite,
			int nColumns) {

		LayoutUtil.setHorizontalSpan(voidButton.getLabelControl(composite), 1);

		Control control = voidButton.getSelectionButtonsGroup(composite);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan = nColumns - 2;
		control.setLayoutData(gd);

		DialogField.createEmptySpace(composite);

		returnClassDialogField.doFillIntoGrid(composite, nColumns);
		Text text = returnClassDialogField.getTextControl(null);
		LayoutUtil.setWidthHint(text, getMaxFieldWidth());
		ControlContentAssistHelper.createTextContentAssistant(text,
				returnClassCompletionProcessor);

		returnDimensionDialogField.doFillIntoGrid(composite, nColumns - 1);
		Text text2 = returnDimensionDialogField.getTextControl(null);
		text2.setTextLimit(2);
		DialogField.createEmptySpace(composite);
		LayoutUtil.setWidthHint(text2, convertWidthInCharsToPixels(2));
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

	private void createParametersListControl(Composite composite, int nColumns) {
		parametersDialogField.doFillIntoGrid(composite, nColumns);
		GridData gd = (GridData) parametersDialogField.getListControl(null)
				.getLayoutData();
		gd.heightHint = convertHeightInCharsToPixels(3);
		gd.grabExcessVerticalSpace = false;
		gd.widthHint = getMaxFieldWidth();
	}

	private void createExceptionsListControl(Composite composite, int nColumns) {
		exceptionsDialogField.doFillIntoGrid(composite, nColumns);
		GridData gd = (GridData) exceptionsDialogField.getListControl(null)
				.getLayoutData();
		gd.heightHint = convertHeightInCharsToPixels(3);
		gd.grabExcessVerticalSpace = false;
		gd.widthHint = getMaxFieldWidth();
		exceptionsDialogField.setEnabled(false);
	}

	private void createCommentControl(Composite composite, int nColumns) {

	}

	protected void methodChangeControlPressed(DialogField field) {
		if (field == returnClassDialogField) {
			returnClass = chooseAttributeType().getFullyQualifiedName();
			if (returnClass != null) {
				returnClassDialogField.setText(returnClass);
			}
		}
		validateMethod();
	}

	protected void methodCustomButtonPressed(DialogField field, int index) {
		if (field == parametersDialogField) {
			if (index == 0) {
				ParametersSelectionDialog dialog = new ParametersSelectionDialog(
						getShell(), new ParameterRef(), this.parentPage,
						this.parametersDialogField.getElements());

				if (dialog.open() == Window.OK) {
					ParameterRef ref = dialog.getParameterRef();
					parametersDialogField.addElement(ref);
				}
			} else if (index == 1) {
				List selectedElem = parametersDialogField.getSelectedElements();
				if (selectedElem.size() == 0)
					return;
				ParameterRef ref = (ParameterRef) selectedElem.get(0);

				ParametersSelectionDialog dialog = new ParametersSelectionDialog(
						getShell(), (ParameterRef) ref.clone(),
						this.parentPage, this.parametersDialogField
								.getElements());

				if (dialog.open() == Window.OK) {
					ParameterRef returnedRef = dialog.getParameterRef();
					ref.applyValues(returnedRef);
				}
			}
		}
		validateMethod();
	}

	protected void methodFieldChanged(DialogField field) {
		if (field == returnClassDialogField) {
			// TODO need to perform a validation here, but for some
			// reason, the scalar type won't work
			// attributeClassStatus = attributeClassChanged();
			returnClass = returnClassDialogField.getText();
		} else if (field == voidButton) {
			updatePage();
		} else if (field == returnDimensionDialogField) {
			// TODO check this is an int
		} else if (field == advancedButton) {
			String methodLabels[] = { this.methodNameDialogField.getText()
					.trim() // index
			// = 0
			};

			Properties methodProperties[] = { this.getMethodDetailsModel()
					.getOssjProperties() };

			methodPropertiesDialog = new MethodPropertiesDialog(getShell(),
					methodLabels, methodProperties, "Method Signature",
					this.signatureDialogField.getText().trim());

			if (methodPropertiesDialog.open() == Window.OK) {
				this.getMethodDetailsModel().setOssjProperties(
						methodPropertiesDialog.getReturnProperties(0));
			}
		}

		for (int i = 0; i < 3; i++) {
			if (fAccMdfButtons.isSelected(i)) {
				modifierSelection = i;
			}

		}
		validateMethod();
	}

	protected void createAdvancedControls(Composite composite, int nColumns) {
		DialogField.createEmptySpace(composite, 2);
		advancedButton.doFillIntoGrid(composite, 1);
	}

	protected void updatePage() {
		returnClassDialogField.setEnabled(!voidButton.isSelected(0));
		returnDimensionDialogField.setEnabled(!voidButton.isSelected(0));

		updateSignature();
	}

	protected void updateSignature() {
		signatureDialogField.setText(initialModel.getSignature());
	}

	@Override
	protected void okPressed() {
		super.okPressed();

		initialModel.setMethodName(methodNameDialogField.getText());
		initialModel.setVoid(voidButton.isSelected(0));
		initialModel.setMethodModifier(modifierSelection);
		initialModel.setReturnClass(returnClassDialogField.getText());
		initialModel.setReturnDimension(Integer
				.parseInt(returnDimensionDialogField.getText()));
		initialModel.setParameterList(parametersDialogField.getElements());
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
	protected IStatus methodClassChanged() {
		StatusInfo status = new StatusInfo();
		IPackageFragmentRoot root = parentPage.getPackageFragmentRoot();
		returnClassDialogField.enableButton(root != null);

		returnClass = null;

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
				returnClass = type.getFullyQualifiedName();
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
		return returnClassDialogField.getText();
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
		returnClassDialogField.setText(name);
		returnClassDialogField.setEnabled(canBeModified);
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

	public MethodDetailsModel getMethodDetailsModel() {
		return initialModel;
	}

	protected Image getImage() {
		// TODO Auto-generated method stub
		return null;
	}
}
