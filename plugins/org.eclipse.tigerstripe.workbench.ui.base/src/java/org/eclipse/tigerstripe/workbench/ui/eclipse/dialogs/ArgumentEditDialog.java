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
package org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
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
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.eclipse.runtime.messages.NewWizardMessages;
import org.eclipse.tigerstripe.eclipse.wizards.artifacts.AttributeRef;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IOssjLegacySettigsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.model.Type;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.OssjLegacySettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.util.Misc;
import org.eclipse.tigerstripe.workbench.internal.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.workbench.model.IField;
import org.eclipse.tigerstripe.workbench.model.ILabel;
import org.eclipse.tigerstripe.workbench.model.IAssociationEnd.EMultiplicity;
import org.eclipse.tigerstripe.workbench.model.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.StereotypeSectionManager;
import org.eclipse.tigerstripe.workbench.ui.eclipse.elements.TSMessageDialog;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ArgumentEditDialog extends TSMessageDialog {

	private final static String PAGE_NAME = "ParametersSelectionDialog";

	private StringButtonDialogField attributeClassDialogField;

	private String attributeClass;

	private String attributeDefaultValue;

	private IStatus attributeClassStatus;

	private IArgument initialArgument;

	private StringDialogField attributeNameDialogField;

	private ComboDialogField multiplicityCombo;

	// private StringDialogField attributeDimensionDialogField;

	private JavaTypeCompletionProcessor attributeClassCompletionProcessor;

	private SelectionButtonDialogFieldGroup fRefByButtons;

	private SelectionButtonDialogFieldGroup modifierButtons;

	private StringDialogField descriptionField;

	private ComboDialogField defaultValueField;

	private boolean defaultValueIsSet = false;

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

	private IJavaElement javaElement;

	private List existingParameters;

	private ITigerstripeProject tsProject;

	public ArgumentEditDialog(Shell parentShell, IArgument argument,
			List existingArguments, IJavaElement elem,
			ITigerstripeProject tsProject) {
		super(parentShell);

		this.tsProject = tsProject;
		this.initialArgument = argument;
		this.existingParameters = existingArguments;
		this.javaElement = elem;

		AttributesFieldsAdapter adapter = new AttributesFieldsAdapter();

		attributeNameDialogField = new StringDialogField();
		attributeNameDialogField.setDialogFieldListener(adapter);
		attributeNameDialogField.setLabelText("Argument Name:"); //$NON-NLS-1$

		attributeClassDialogField = new StringButtonDialogField(adapter);
		attributeClassDialogField.setDialogFieldListener(adapter);
		attributeClassDialogField.setLabelText("Argument Type:"); //$NON-NLS-1$
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
		multiplicityCombo.setItems(EMultiplicity.labels());
		multiplicityCombo.setLabelText("Multiplicity");
		multiplicityCombo.setDialogFieldListener(adapter);

		defaultValueField = new ComboDialogField(SWT.BORDER);
		defaultValueField.setDialogFieldListener(adapter);
		defaultValueField.setLabelText("Default Value:");

		descriptionField = new StringDialogField();
		descriptionField.setDialogFieldListener(adapter);
		descriptionField.setLabelText("Description");

		if (isRefByEnabled()) {
			String[] refByNames = new String[] {
					IField.refByLabels[IField.REFBY_VALUE],
					IField.refByLabels[IField.REFBY_KEY],
					IField.refByLabels[IField.REFBY_KEYRESULT] };

			fRefByButtons = new SelectionButtonDialogFieldGroup(SWT.RADIO,
					refByNames, 3);
			fRefByButtons.setDialogFieldListener(adapter);
			fRefByButtons.setLabelText("Reference by"); //$NON-NLS-1$
		}

		String[] modifierNames = new String[] { "Ordered", // INDEX=0
				"Unique", // INDEX = 1
		};
		modifierButtons = new SelectionButtonDialogFieldGroup(SWT.CHECK,
				modifierNames, 2);
		modifierButtons.setDialogFieldListener(adapter);
		modifierButtons.setLabelText("Modifiers"); //$NON-NLS-1$
	}

	private boolean isRefByEnabled() {
		OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) TigerstripeCore
				.getIWorkbenchProfileSession().getActiveProfile().getProperty(
						IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);
		return prop
				.getPropertyValue(IOssjLegacySettigsProperty.USEREFBY_MODIFIERS);
	}

	protected void setDefaultMessage() {
		setMessage("Specify Argument Details");
	}

	protected boolean validateParam() {
		Button okButton = this.getButton(IDialogConstants.OK_ID);
		boolean okEnabled = true;

		// Check for valid name
		String parameterName = this.attributeNameDialogField.getText().trim();
		if (parameterName.charAt(0) >= '0' && parameterName.charAt(0) <= '9') {
			okEnabled = false;
			setMessage("Invalid Argument Name.");
		}

		char[] invalidChars = { ' ', ';', ',', '-', '{', '}', '[', ']', '(',
				')', '+', '=', '|', '\\', '/', '?', '<', '>', '.', '`', '~',
				'!', '@', '#', '$', '%', '^', '&', '*' };

		for (int i = 0; i < invalidChars.length; i++) {
			if (parameterName.indexOf(invalidChars[i]) != -1) {
				okEnabled = false;
				setMessage("Invalid Argument Name.");
			}
		}

		// Check for duplicates
		for (Iterator iter = this.existingParameters.iterator(); iter.hasNext();) {
			IArgument ref = (IArgument) iter.next();
			if (ref.getName().equals(parameterName) && ref != initialArgument) {
				okEnabled = false;
				setMessage("Duplicate parameter name.");
			}
		}

		// check if attribute is reference to other Entity and adjust the
		// ref-by buttons
		if (isRefByEnabled())
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
		try {
			IAbstractArtifact artifact = tsProject.getArtifactManagerSession()
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

		createMessageArea(composite, nColumns);
		createAttributeNameControl(composite, nColumns);
		createTypeDetailsControl(composite, nColumns);
		createDimensionControl(composite, nColumns);
		createModifiersControl(composite, nColumns);
		createRefByControl(composite, nColumns);
		createDefaultValueControl(composite, nColumns);
		createCommentControl(composite, nColumns);
		createAnnotationsControls(composite, nColumns);

		initDialog();

		// WorkbenchHelp.setHelp(composite,
		// IJavaHelpContextIds.NEW_INTERFACE_WIZARD_PAGE);
		return area;
	}

	protected void initDialog() {
		if (initialArgument.getName() != null) {
			attributeNameDialogField.setText(initialArgument.getName());
		} else {
			attributeNameDialogField.setText("aString");
		}

		if (initialArgument.getComment() != null) {
			descriptionField.setText(initialArgument.getComment());
		} else {
			descriptionField.setText("");
		}

		if (initialArgument.getIType() != null) {
			attributeClassDialogField.setText(Misc
					.removeJavaLangString(initialArgument.getIType()
							.getFullyQualifiedName()));
		} else {
			attributeClassDialogField.setText("String");
		}

		updateDefaultValueCombo();

		if (initialArgument.getDefaultValue() != null) {
			defaultValueIsSet = true;
			if (defaultValueField.getItems().length != 0) {
				String[] items = defaultValueField.getItems();
				int i = 0, index = -1;
				for (String item : items) {
					if (item.equals(initialArgument.getDefaultValue())) {
						index = i;
					}
					i++;
				}
				if (index != -1) {
					defaultValueField.getComboControl(null).select(index);
				} else {
					defaultValueField.getComboControl(null).clearSelection();
				}
			} else {
				defaultValueField.setText(initialArgument.getDefaultValue());
			}
		} else {
			defaultValueIsSet = false;
			defaultValueField.getComboControl(null).clearSelection();
		}

		if (isRefByEnabled()) {
			if (initialArgument.getRefBy() == AttributeRef.NON_APPLICABLE) {
				fRefByButtons.setEnabled(false);
			} else {
				fRefByButtons.setEnabled(true);
				fRefByButtons
						.setSelection(AttributeRef.REFBY_VALUE,
								(AttributeRef.REFBY_VALUE == initialArgument
										.getRefBy()));
				fRefByButtons.setSelection(AttributeRef.REFBY_KEY,
						(AttributeRef.REFBY_KEY == initialArgument.getRefBy()));
				fRefByButtons.setSelection(AttributeRef.REFBY_KEYRESULT,
						(AttributeRef.REFBY_KEYRESULT == initialArgument
								.getRefBy()));
			}
		}

		modifierButtons.setSelection(0 // ORDERED
				, initialArgument.isOrdered());
		modifierButtons.setSelection(1 // UNIQUE
				, initialArgument.isUnique());

		multiplicityCombo.selectItem(EMultiplicity.indexOf(initialArgument
				.getIType().getTypeMultiplicity()));
		// attributeDimensionDialogField.setText(String.valueOf(initialArgument
		// .getIType().getMultiplicity()));
		getShell().setText("Argument Details");
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
		// attributeDimensionDialogField.doFillIntoGrid(composite, nColumns);
		// Text text = attributeDimensionDialogField.getTextControl(null);
		// text.setTextLimit(2);
		// LayoutUtil.setWidthHint(text, convertWidthInCharsToPixels(2));

		multiplicityCombo.doFillIntoGrid(composite, nColumns - 1);
		DialogField.createEmptySpace(composite);

		descriptionField.doFillIntoGrid(composite, nColumns - 1);
		DialogField.createEmptySpace(composite);
	}

	private void createModifiersControl(Composite composite, int nColumns) {
		LayoutUtil.setHorizontalSpan(
				modifierButtons.getLabelControl(composite), 1);

		Control control = modifierButtons.getSelectionButtonsGroup(composite);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan = nColumns - 2;
		control.setLayoutData(gd);

		DialogField.createEmptySpace(composite);
	}

	private void createRefByControl(Composite composite, int nColumns) {
		if (isRefByEnabled()) {
			LayoutUtil.setHorizontalSpan(fRefByButtons
					.getLabelControl(composite), 1);

			Control control = fRefByButtons.getSelectionButtonsGroup(composite);
			GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
			gd.horizontalSpan = nColumns - 2;
			control.setLayoutData(gd);

			DialogField.createEmptySpace(composite);
		}
	}

	private void createDefaultValueControl(Composite composite, int nColumns) {
		defaultValueField.doFillIntoGrid(composite, nColumns - 1);
		DialogField.createEmptySpace(composite);

		LayoutUtil.setWidthHint(defaultValueField.getComboControl(null),
				getMaxFieldWidth());
	}

	private void createCommentControl(Composite composite, int nColumns) {

	}

	private void createAnnotationsControls(Composite composite, int nColumns) {
		Group innerComposite = new Group(composite, SWT.BOLD);
		innerComposite.setText("Annotations");
		GridData gd = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL);
		gd.horizontalSpan = nColumns;
		innerComposite.setLayoutData(gd);
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		innerComposite.setLayout(layout);

		Table annTable = new Table(innerComposite, SWT.BORDER);
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.rowspan = 3;
		td.heightHint = 70;
		annTable.setLayoutData(td);

		Button addAnno = new Button(innerComposite, SWT.PUSH);
		addAnno.setText("Add");
		td = new TableWrapData(TableWrapData.FILL);
		addAnno.setLayoutData(td);

		Button editAnno = new Button(innerComposite, SWT.PUSH);
		editAnno.setText("Edit");
		td = new TableWrapData(TableWrapData.FILL);
		editAnno.setLayoutData(td);

		Button removeAnno = new Button(innerComposite, SWT.PUSH);
		removeAnno.setText("Remove");

		StereotypeSectionManager stereomgr = new StereotypeSectionManager(
				addAnno, editAnno, removeAnno, annTable, initialArgument,
				getShell(), null);
		stereomgr.delegate();

	}

	protected void attributeChangeControlPressed(DialogField field) {
		if (field == attributeClassDialogField) {
			// attributeClass = chooseAttributeType().getFullyQualifiedName();
			attributeClass = chooseAttributeType();
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
			updateDefaultValueCombo();
		} else if (field == defaultValueField) {
			attributeDefaultValue = defaultValueField.getText();
			defaultValueIsSet = true;
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
		initialArgument.setName(attributeNameDialogField.getText());
		org.eclipse.tigerstripe.workbench.model.IType type = initialArgument
				.getIType();
		type.setFullyQualifiedName(attributeClass);
		type.setTypeMultiplicity(EMultiplicity.at(multiplicityCombo
				.getSelectionIndex()));

		initialArgument.setIType(type);
		initialArgument.setComment(descriptionField.getText());

		if (defaultValueIsSet && attributeDefaultValue != null
				&& attributeDefaultValue.length() != 0) {
			initialArgument.setDefaultValue(attributeDefaultValue);
		} else
			initialArgument.setDefaultValue(null);

		if (isRefByEnabled() && fRefByButtons.isEnabled()) {
			if (fRefByButtons.isSelected(IField.REFBY_KEY)) {
				initialArgument.setRefBy(IField.REFBY_KEY);
			} else if (fRefByButtons.isSelected(IField.REFBY_KEYRESULT)) {
				initialArgument.setRefBy(IField.REFBY_KEYRESULT);
			} else {
				initialArgument.setRefBy(IField.REFBY_VALUE);
			}
		} else {
			initialArgument.setRefBy(IField.NON_APPLICABLE);
		}

		initialArgument.setOrdered(modifierButtons.isSelected(0));
		initialArgument.setUnique(modifierButtons.isSelected(1));
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
		try {
			IPackageFragmentRoot root = this.javaElement.getJavaProject()
					.findPackageFragmentRoot(this.javaElement.getPath());
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
								.getString("NewArtifactWizardPage.attributes.error.InvalidClassName"));
				//$NON-NLS-1$
				return status;
			}
			if (root != null) {
				try {
					IType type = resolveAttributeTypeName(
							root.getJavaProject(), sclassName);
					if (type == null) {
						status
								.setWarning(NewWizardMessages
										.getString("NewArtifactWizardPage.attributes.warning.ClassNotExists"));
						//$NON-NLS-1$
						return status;
					}
					attributeClass = type.getFullyQualifiedName();
				} catch (JavaModelException e) {
					status
							.setError(NewWizardMessages
									.getString("NewArtifactWizardPage.attributes.error.InvalidClassName"));
					//$NON-NLS-1$
					JavaPlugin.log(e);
				}
			} else {
				status.setError(""); //$NON-NLS-1$
			}
		} catch (JavaModelException e) {
			EclipsePlugin.log(e);
		}
		return status;
	}

	private IType resolveAttributeTypeName(IJavaProject jproject,
			String sclassName) throws JavaModelException {
		if (!jproject.exists())
			return null;
		IType type = null;
		// IPackageFragment currPack = parentPage.getArtifactPackageFragment();
		// if (type == null && currPack != null) {
		// String packName = currPack.getElementName();
		// // search in own package
		// if (!currPack.isDefaultPackage()) {
		// type = jproject.findType(packName, sclassName);
		// }
		// // search in java.lang
		// if (type == null && !"java.lang".equals(packName)) { //$NON-NLS-1$
		// type = jproject.findType("java.lang", sclassName); //$NON-NLS-1$
		// }
		// }
		// // search fully qualified
		// if (type == null) {
		// type = jproject.findType(sclassName);
		// }
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

	public String getAttributeDefaultValue() {
		return attributeDefaultValue;
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
					tsProject, new IAbstractArtifact[0]);
			dialog.setTitle("Artifact Type Selection");
			dialog.setMessage("Enter a filter (* = any number of characters)"
					+ " or an empty string for no filtering: ");

			IAbstractArtifact[] artifacts = dialog.browseAvailableArtifacts(
					getShell(), new ArrayList());
			if (artifacts.length != 0)
				return artifacts[0].getFullyQualifiedName();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		// try {
		// IPackageFragmentRoot[] roots = this.javaElement.getJavaProject()
		// .getAllPackageFragmentRoots();
		// IPackageFragmentRoot root = roots[0]; // FIXME this means there's
		// // only 1 root. May change
		// // in the future
		//
		// if (root == null) {
		// return null;
		// }
		// IJavaElement[] elements = new IJavaElement[] { root
		// .getJavaProject() };
		// IJavaSearchScope scope = SearchEngine
		// .createJavaSearchScope(elements);
		//
		// TypeSelectionDialog2 dialog = new TypeSelectionDialog2(getShell(),
		// false, null, scope, IJavaSearchConstants.TYPE);
		// dialog
		// .setTitle(NewWizardMessages
		// .getString("NewArtifactWizardPage.attributes.SuperClassDialog.title"));
		// //$NON-NLS-1$
		// dialog
		// .setMessage(NewWizardMessages
		// .getString("NewArtifactWizardPage.attributes.SuperClassDialog.message"));
		// //$NON-NLS-1$
		// dialog.setFilter(getAttributeClass());
		//
		// if (dialog.open() == Window.OK) {
		// return (IType) dialog.getFirstResult();
		// }
		// } catch (JavaModelException e) {
		// EclipsePlugin.log(e);
		// }
		return null;
	}

	public IArgument getIArgument() {
		return initialArgument;
	}

	private void updateDefaultValueCombo() {
		// Update the default value control based on the field type
		if (attributeClass != null) {
			Type type = (Type) getIArgument().getIType();
			IAbstractArtifact art = type.getArtifactManager()
					.getArtifactByFullyQualifiedName(attributeClass, true,
							new TigerstripeNullProgressMonitor());
			if (art instanceof IEnumArtifact) {
				IEnumArtifact enumArt = (IEnumArtifact) art;
				String[] items = new String[enumArt.getLabels().size()];
				int i = 0;
				for (ILabel label : enumArt.getLabels()) {
					items[i] = label.getName();
					i++;
				}
				defaultValueField.setItems(items);
			} else if (attributeClass.equals("boolean")) {
				defaultValueField.setItems(new String[] { "true", "false" });
				defaultValueField.getComboControl(getParentShell()).select(0);
			} else {
				defaultValueField.setItems(new String[0]);
			}
			defaultValueIsSet = false;
		}
	}
}
