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
package org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.ControlContentAssistHelper;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.JavaTypeCompletionProcessor;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ComboDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogFieldGroup;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jface.dialogs.IDialogConstants;
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
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.eclipse.runtime.messages.NewWizardMessages;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.TSRuntimeContext;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.model.ArtifactAttributeModel;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs.BrowseForArtifactDialog;
import org.eclipse.tigerstripe.workbench.ui.eclipse.elements.TSMessageDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class AttributesSelectionDialog extends TSMessageDialog {

	private final static String PAGE_NAME = "AttributesSelectionDialog";

	private SelectionButtonDialogFieldGroup fAccMdfButtons;

	private SelectionButtonDialogFieldGroup attrModifierButtons;

	private SelectionButtonDialogFieldGroup fRefByButtons;

	private int modifierSelection;

	private StringButtonDialogField attributeClassDialogField;

	private String attributeClass;

	private IStatus attributeClassStatus;

	private ComboDialogField multiplicityCombo;

	private ArtifactAttributeModel initialAttributeRef;

	private NewArtifactWizardPage parentPage;

	private StringDialogField attributeNameDialogField;

	private StringDialogField attributeDimensionDialogField;

	private JavaTypeCompletionProcessor attributeClassCompletionProcessor;

	private List existingAttributeRefs;

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

	private boolean forEdit;

	public AttributesSelectionDialog(Shell parentShell,
			ArtifactAttributeModel attributeRef,
			NewArtifactWizardPage parentPage, List existingAttributeRefs,
			boolean forEdit) {
		super(parentShell);

		this.forEdit = forEdit;
		this.initialAttributeRef = attributeRef;
		this.parentPage = parentPage;
		this.existingAttributeRefs = existingAttributeRefs;

		AttributesFieldsAdapter adapter = new AttributesFieldsAdapter();

		attributeNameDialogField = new StringDialogField();
		attributeNameDialogField.setDialogFieldListener(adapter);
		attributeNameDialogField.setLabelText("Attribute Name:"); //$NON-NLS-1$

		attributeClassDialogField = new StringButtonDialogField(adapter);
		attributeClassDialogField.setDialogFieldListener(adapter);
		attributeClassDialogField.setLabelText("Attribute Type:"); //$NON-NLS-1$
		attributeClassDialogField.setButtonLabel(NewWizardMessages
				.getString("NewArtifactWizardPage.package.button")); //$NON-NLS-1$

		attributeClassStatus = new StatusInfo();
		attributeClassCompletionProcessor = new JavaTypeCompletionProcessor(
				false, false);

		// attributeDimensionDialogField = new StringDialogField();
		// attributeDimensionDialogField.setDialogFieldListener(adapter);
		// attributeDimensionDialogField.setLabelText("Attribute Dimensions:");
		// //$NON-NLS-1$
		String[] multiItems = new String[2];
		multiItems[0] = "0..1";
		multiItems[1] = "*";
		multiplicityCombo = new ComboDialogField(0);
		multiplicityCombo.setItems(multiItems);
		multiplicityCombo.setLabelText("Multiplicity");
		multiplicityCombo.setDialogFieldListener(adapter);

		String[] buttonNames1 = new String[] {
				/* 0 == PUBLIC_INDEX */NewWizardMessages
						.getString("NewArtifactWizardPage.attributes.modifiers.public"), //$NON-NLS-1$
				/* 1 == PRIVATE_INDEX */NewWizardMessages
						.getString("NewArtifactWizardPage.attributes.modifiers.private"), //$NON-NLS-1$
				/* 2 == PROTECTED_INDEX */NewWizardMessages
						.getString("NewArtifactWizardPage.attributes.modifiers.protected"),
				/* 4 == PACKAGE_INDEX */"package" //$NON-NLS-1$
		};
		fAccMdfButtons = new SelectionButtonDialogFieldGroup(SWT.RADIO,
				buttonNames1, 5);
		fAccMdfButtons.setDialogFieldListener(adapter);
		fAccMdfButtons.setLabelText("Visibility"); //$NON-NLS-1$

		String[] refByNames = new String[] {
				AttributeRef.refByLabels[AttributeRef.REFBY_VALUE],
				AttributeRef.refByLabels[AttributeRef.REFBY_KEY],
				AttributeRef.refByLabels[AttributeRef.REFBY_KEYRESULT] };

		fRefByButtons = new SelectionButtonDialogFieldGroup(SWT.RADIO,
				refByNames, 2);
		fRefByButtons.setDialogFieldListener(adapter);
		fRefByButtons.setLabelText("Reference by"); //$NON-NLS-1$

		String[] attributeModifiers = { "Optional", "Read-only", "isUnique",
				"isOrdered" };

		attrModifierButtons = new SelectionButtonDialogFieldGroup(SWT.CHECK,
				attributeModifiers, 4);
		attrModifierButtons.setDialogFieldListener(adapter);
		attrModifierButtons.setLabelText("Modifiers"); //$NON-NLS-1$
	}

	/**
	 * Validate the attribute based on all other attribute defs (e.g. avoid
	 * duplicates)
	 * 
	 * @return
	 */
	protected boolean validateAttribute() {

		Button okButton = this.getButton(IDialogConstants.OK_ID);
		boolean okEnabled = true;

		// Check for valid name
		String attributeName = this.attributeNameDialogField.getText().trim();
		if (attributeName == null
				|| "".equals(attributeName)
				|| (attributeName.charAt(0) >= '0' && attributeName.charAt(0) <= '9')) {
			okEnabled = false;
			setMessage("Invalid attribute name.");
		}

		char[] invalidChars = { ' ', ';', ',', '-', '{', '}', '[', ']', '(',
				')', '+', '=', '|', '\\', '/', '?', '<', '>', '.', '`', '~',
				'!', '@', '#', '$', '%', '^', '&', '*' };

		for (int i = 0; i < invalidChars.length; i++) {
			if (attributeName.indexOf(invalidChars[i]) != -1) {
				okEnabled = false;
				setMessage("Invalid attribute name.");
			}
		}

		// Check for duplicates
		if (!forEdit) {
			for (Iterator iter = this.existingAttributeRefs.iterator(); iter
					.hasNext();) {
				ArtifactAttributeModel ref = (ArtifactAttributeModel) iter
						.next();
				if (ref.getName().equals(attributeName)) {
					okEnabled = false;
					setMessage("Duplicate attribute name.");
				}
			}
		}

		// check if attribute is reference to other Entity and adjust the
		// ref-by buttons
		fRefByButtons.setEnabled(isReferenceToEntity());

		// In case this is called before the button is created
		if (okButton != null) {
			okButton.setEnabled(okEnabled);
		}

		if (okEnabled) {
			setDefaultMessage();
		}
		return okEnabled;
	}

	/**
	 * Checks if the type of the current attribute is an EntityArtifact. If so,
	 * the ref-by buttons should be enabled
	 * 
	 * @return
	 */
	protected boolean isReferenceToEntity() {

		boolean result = false;
		TSRuntimeContext tsContext = this.parentPage.getTSRuntimeContext();

		try {
			ITigerstripeModelProject project = tsContext.getProjectHandle();
			IAbstractArtifact artifact = project.getArtifactManagerSession()
					.getArtifactByFullyQualifiedName(this.getAttributeClass());
			if (artifact != null && artifact instanceof IManagedEntityArtifact) {
				result = true;
			}
		} catch (TigerstripeException e) {
			result = false;
		}

		return result;
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

		getShell().setText("Attribute Details");

		createMessageArea(composite, nColumns);
		createAttributeNameControl(composite, nColumns);
		createTypeDetailsControl(composite, nColumns);
		createDimensionControl(composite, nColumns);
		createModifiersControl(composite, nColumns);
		createRefByControl(composite, nColumns);
		createCommentControl(composite, nColumns);

		initDialog();

		// WorkbenchHelp.setHelp(composite,
		// IJavaHelpContextIds.NEW_INTERFACE_WIZARD_PAGE);
		return area;
	}

	private String findNewFieldName() {
		String result = "attribute" + parentPage.getFieldCounter();
		parentPage.setFieldCounter(parentPage.getFieldCounter() + 1);
		return result;
	}

	protected void initDialog() {
		if (initialAttributeRef.getName() != null) {
			attributeNameDialogField.setText(initialAttributeRef.getName());
		} else {
			attributeNameDialogField.setText(findNewFieldName());
		}

		if (initialAttributeRef.getAttributeClass() != null) {
			attributeClassDialogField.setText(initialAttributeRef
					.getAttributeClass());
		} else {
			attributeClassDialogField.setText(getDefaultTypeName());
		}

		setDefaultMessage();
		for (int i = 0; i < 4; i++) {
			fAccMdfButtons.setSelection(i, i == initialAttributeRef
					.getModifier());
		}
		// attributeDimensionDialogField.setText(String
		// .valueOf(initialAttributeRef.getDimensions()));
		multiplicityCombo.selectItem(initialAttributeRef.getDimensions());

		if (initialAttributeRef.getRefBy() == AttributeRef.NON_APPLICABLE) {
			fRefByButtons.setEnabled(false);
		} else {
			fRefByButtons.setEnabled(true);
			fRefByButtons
					.setSelection(AttributeRef.REFBY_VALUE,
							(AttributeRef.REFBY_VALUE == initialAttributeRef
									.getRefBy()));
			fRefByButtons.setSelection(AttributeRef.REFBY_KEY,
					(AttributeRef.REFBY_KEY == initialAttributeRef.getRefBy()));
			fRefByButtons.setSelection(AttributeRef.REFBY_KEYRESULT,
					(AttributeRef.REFBY_KEYRESULT == initialAttributeRef
							.getRefBy()));
		}

		attrModifierButtons.setSelection(0, initialAttributeRef.isOptional());
		attrModifierButtons.setSelection(1, initialAttributeRef.isReadOnly());
		attrModifierButtons.setSelection(2, initialAttributeRef.isUnique());
		attrModifierButtons.setSelection(3, initialAttributeRef.isOrdered());
	}

	protected void setDefaultMessage() {
		setMessage("Specify attribute details...");
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		validateAttribute();
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

		multiplicityCombo.doFillIntoGrid(composite, nColumns);

		// attributeDimensionDialogField.doFillIntoGrid(composite, nColumns);
		// Text text = attributeDimensionDialogField.getTextControl(null);
		// text.setTextLimit(2);
		// LayoutUtil.setWidthHint(text, convertWidthInCharsToPixels(2));
	}

	private void createModifiersControl(Composite composite, int nColumns) {

		// The visibility modifiers first
		LayoutUtil.setHorizontalSpan(fAccMdfButtons.getLabelControl(composite),
				1);

		Control control = fAccMdfButtons.getSelectionButtonsGroup(composite);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan = nColumns - 2;
		control.setLayoutData(gd);

		DialogField.createEmptySpace(composite);

		// The attribute modifiers then
		LayoutUtil.setHorizontalSpan(attrModifierButtons
				.getLabelControl(composite), 1);

		Control control2 = attrModifierButtons
				.getSelectionButtonsGroup(composite);
		GridData gd2 = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd2.horizontalSpan = nColumns - 2;
		control2.setLayoutData(gd2);

		DialogField.createEmptySpace(composite);
	}

	private void createRefByControl(Composite composite, int nColumns) {
		LayoutUtil.setHorizontalSpan(fRefByButtons.getLabelControl(composite),
				1);

		Control control = fRefByButtons.getSelectionButtonsGroup(composite);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan = nColumns - 2;
		control.setLayoutData(gd);

		DialogField.createEmptySpace(composite);
	}

	private void createCommentControl(Composite composite, int nColumns) {

	}

	protected void attributeChangeControlPressed(DialogField field) {
		if (field == attributeClassDialogField) {
			attributeClass = chooseAttributeType();
			if (attributeClass != null) {
				attributeClassDialogField.setText(attributeClass);
			}
		}
		validateAttribute();
	}

	protected void attributeFieldChanged(DialogField field) {
		if (field == attributeClassDialogField) {
			// TODO need to perform a validation here, but for some
			// reason, the scalar type won't work
			// attributeClassStatus = attributeClassChanged();
			attributeClass = attributeClassDialogField.getText();
		} else if (field == attributeDimensionDialogField) {
			// TODO check this is an int
		} else if (field == attributeNameDialogField) {
			validateAttribute();
		}

		for (int i = 0; i < 4; i++) {
			if (fAccMdfButtons.isSelected(i)) {
				modifierSelection = i;
			}
		}
		validateAttribute();
	}

	@Override
	protected void okPressed() {
		super.okPressed();

		// populate the attribute
		initialAttributeRef.setName(attributeNameDialogField.getText());
		initialAttributeRef.setAttributeClass(attributeClass);
		// String text = attributeDimensionDialogField.getText();

		initialAttributeRef
				.setDimensions(multiplicityCombo.getSelectionIndex());
		// initialAttributeRef.setDimensions(Integer.parseInt(text));

		initialAttributeRef.setModifier(modifierSelection);

		if (fRefByButtons.isEnabled()) {
			if (fRefByButtons.isSelected(AttributeRef.REFBY_KEY)) {
				initialAttributeRef.setRefBy(AttributeRef.REFBY_KEY);
			} else if (fRefByButtons.isSelected(AttributeRef.REFBY_KEYRESULT)) {
				initialAttributeRef.setRefBy(AttributeRef.REFBY_KEYRESULT);
			} else {
				initialAttributeRef.setRefBy(AttributeRef.REFBY_VALUE);
			}
		} else {
			initialAttributeRef.setRefBy(AttributeRef.NON_APPLICABLE);
		}

		initialAttributeRef.setOptional(attrModifierButtons.isSelected(0));
		initialAttributeRef.setReadOnly(attrModifierButtons.isSelected(1));
		initialAttributeRef.setUnique(attrModifierButtons.isSelected(2));
		initialAttributeRef.setOrdered(attrModifierButtons.isSelected(3));
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

	private String chooseAttributeType() {

		try {
			BrowseForArtifactDialog dialog = new BrowseForArtifactDialog(
					parentPage.getTSRuntimeContext().getProjectHandle(),
					new IAbstractArtifact[0]);
			dialog.setTitle("Artifact Type Selection");
			dialog.setMessage("Choose a type.");

			IAbstractArtifact[] artifacts = dialog.browseAvailableArtifacts(
					getShell(), new ArrayList());
			if (artifacts.length != 0)
				return artifacts[0].getFullyQualifiedName();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		return null;
	}

	/**
	 * Gets the default attribute type from the active profile.
	 */
	private String getDefaultTypeName() {
		IWorkbenchProfile profile = TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile();
		return profile.getDefaultPrimitiveTypeString();
	}

	public ArtifactAttributeModel getAttributeRef() {
		return initialAttributeRef;
	}

	protected Image getImage() {
		return Images.get(Images.FIELD_ICON_NEW);
	}
}
