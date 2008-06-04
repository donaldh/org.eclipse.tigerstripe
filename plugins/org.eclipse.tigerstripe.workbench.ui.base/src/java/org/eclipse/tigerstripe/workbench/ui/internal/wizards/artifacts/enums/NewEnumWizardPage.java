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
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.enums;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.ControlContentAssistHelper;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.JavaPackageCompletionProcessor;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.JavaTypeCompletionProcessor;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ComboDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.Separator;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonStatusDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.metamodel.impl.IEnumArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.internal.runtime.messages.NewWizardMessages;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.TSRuntimeContext;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.ArtifactDefinitionGenerator;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.INewArtifactWizardPage;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.LabelsSelectionDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.TSRuntimeBasedWizardPage;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

// import
// org.eclipse.tigerstripe.eclipse.wizards.artifacts.NewArtifactWizardPage.ArtifactFieldsAdapter;
// import
// org.eclipse.tigerstripe.eclipse.wizards.artifacts.NewArtifactWizardPage.AttributeRef;
// import
// org.eclipse.tigerstripe.eclipse.wizards.artifacts.NewArtifactWizardPage.AttributesListLabelProvider;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class NewEnumWizardPage extends TSRuntimeBasedWizardPage implements
		INewArtifactWizardPage {

	private final static String PAGE_NAME = "NewEnumWizardPage";

	private ComboDialogField baseEnumType;

	private int fieldCounter = 0;

	private final static String[] supportedEnumTypes = { "int", "String" };

	public NewEnumWizardPage() {
		super(PAGE_NAME);

		setTitle(ArtifactMetadataFactory.INSTANCE.getMetadata(
				IEnumArtifactImpl.class.getName()).getLabel(null)
				+ " Artifact");
		setDescription("Create a new "
				+ ArtifactMetadataFactory.INSTANCE.getMetadata(
						IEnumArtifactImpl.class.getName()).getLabel(null)
				+ " Artifact.");

	}

	// -------- TypeFieldsAdapter --------

	private class ArtifactLabelsAdapter implements IStringButtonAdapter,
			IDialogFieldListener, IListAdapter {

		// -------- IStringButtonAdapter
		public void changeControlPressed(DialogField field) {
			artifactPageChangeControlPressed(field);
		}

		// -------- IListAdapter
		public void customButtonPressed(ListDialogField field, int index) {
			artifactPageCustomButtonPressed(field, index);
		}

		public void selectionChanged(ListDialogField field) {
		}

		// -------- IDialogFieldListener
		public void dialogFieldChanged(DialogField field) {
			artifactPageDialogFieldChanged(field);
		}

		public void doubleClicked(ListDialogField field) {
		}
	}

	public Properties getProperties() {
		Properties result = getPropertiesCommons();
		return result;
	}

	protected ArtifactDefinitionGenerator getGenerator(
			Properties pageProperties, Writer writer) {
		return new EnumDefinitionGenerator(pageProperties, writer);
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);

		int nColumns = 4;

		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		createArtifactControls(composite, nColumns);

		setControl(composite);
		Dialog.applyDialogFont(composite);
		// WorkbenchHelp.setHelp(composite,
		// IJavaHelpContextIds.NEW_INTERFACE_WIZARD_PAGE);
	}

	public void initPage(IJavaElement jElement) {
		initFromContext();

		setFocus();
		doStatusUpdate();
	}

	/**
	 * Perform any required update based on the runtime context
	 * 
	 */
	@Override
	protected void initFromContext() {
		try {
			TSRuntimeContext context = getTSRuntimeContext();
			ITigerstripeModelProject project = context.getProjectHandle();

			if ("".equals(this.packageDialogField.getText())) {
				this.packageDialogField
						.setText(project
								.getProjectDetails()
								.getProperty(
										IProjectDetails.DEFAULTARTIFACTPACKAGE_PROP,
										""));
			}

			IPluginConfig[] refs = project.getPluginConfigs();

			for (int i = 0; i < refs.length; i++) {
				// if jvtPlugin
				if ("ossj-jvt-spec".equals(refs[i].getPluginId())) {
					if (refs[i].getProperty("defaultInterfacePackage") == null)
						this.targetPackageDialogField.setText((String) refs[i]
								.getProperty("defaultInterfacePackage"));
					else
						this.targetPackageDialogField.setText("com.mycompany");

				}

				// if xmlPlugin
				if ("ossj-xml-spec".equals(refs[i].getPluginId())) {

				}
			}

			if (this.targetPackageDialogField.getText() == null
					|| this.targetPackageDialogField.getText().length() == 0) {
				targetPackageDialogField.setText("com.mycompany");
			}

		} catch (TigerstripeException e) {
			// The wizard is currently not pointing at a valid TS project
			// We ignore
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}
	}

	private void doStatusUpdate() {
		// status of all used components
		IStatus[] status = new IStatus[] { tsRuntimeContextStatus,
				fContainerStatus, artifactNameStatus, artifactPackageStatus };

		// the mode severe status will be displayed and the ok button
		// enabled/disabled.
		updateStatus(status);
	}

	@Override
	protected void handleFieldChanged(String fieldName) {
		super.handleFieldChanged(fieldName);
		if (fieldName == CONTAINER) {
			artifactPackageStatus = packageChanged();
			artifactNameStatus = artifactNameChanged();
			tsRuntimeContextStatus = tsRuntimeContextChanged();
		}

		doStatusUpdate();
	}

	/** Field ID of the package input field. */
	protected final static String PACKAGE = PAGE_NAME + ".package"; //$NON-NLS-1$

	/** Field ID of the artifact name input field. */
	protected final static String ARTIFACTNAME = PAGE_NAME + ".artifactname"; //$NON-NLS-1$

	/** Field ID of the super type input field. */
	protected final static String SUPER = PAGE_NAME + ".superclass"; //$NON-NLS-1$

	/** Field ID of the super interfaces input field. */
	protected final static String INTERFACES = PAGE_NAME + ".interfaces"; //$NON-NLS-1$

	/** Field ID of the modifier check boxes. */
	protected final static String MODIFIERS = PAGE_NAME + ".modifiers"; //$NON-NLS-1$

	/** Field ID of the method stubs check boxes. */
	protected final static String METHODS = PAGE_NAME + ".methods"; //$NON-NLS-1$

	/**
	 * The package of the generated artifact
	 */
	private IPackageFragment artifactPackage;

	protected IStatus artifactPackageStatus;

	protected IStatus artifactNameStatus;

	// the attribute list selection
	private ListDialogField attributesDialogField;

	// The name of the artifact that will be generated
	private StringDialogField artifactNameDialogField;

	// The package of the Artifact
	private StringButtonStatusDialogField packageDialogField;

	// The target package for the generated interface
	private StringButtonStatusDialogField targetPackageDialogField;

	// Help on the the package completions
	private JavaPackageCompletionProcessor packageCompletionProcessor;

	// Help on the Artifact name completion
	private JavaTypeCompletionProcessor artifactTypeCompletionProcessor;

	// the modifier buttons (generate, etc...)
	// private SelectionButtonDialogFieldGroup modifierButtons;

	private IJavaElement initialElement;

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

		setInitialElement(jelem);

		packageCompletionProcessor = new JavaPackageCompletionProcessor();
		artifactTypeCompletionProcessor = new JavaTypeCompletionProcessor(
				false, false);
		ArtifactLabelsAdapter adapter = new ArtifactLabelsAdapter();

		packageDialogField = new StringButtonStatusDialogField(adapter);
		packageDialogField.setDialogFieldListener(adapter);
		packageDialogField.setLabelText(NewWizardMessages
				.getString("NewArtifactWizardPage.package.label")); //$NON-NLS-1$
		packageDialogField.setButtonLabel(NewWizardMessages
				.getString("NewArtifactWizardPage.package.button")); //$NON-NLS-1$
		packageDialogField.setStatusWidthHint(NewWizardMessages
				.getString("NewArtifactWizardPage.default")); //$NON-NLS-1$

		targetPackageDialogField = new StringButtonStatusDialogField(adapter);
		targetPackageDialogField.setDialogFieldListener(adapter);
		targetPackageDialogField.setLabelText(NewWizardMessages
				.getString("NewArtifactWizardPage.interface.package.label")); //$NON-NLS-1$
		targetPackageDialogField.setButtonLabel(NewWizardMessages
				.getString("NewArtifactWizardPage.package.button")); //$NON-NLS-1$
		targetPackageDialogField.setStatusWidthHint(NewWizardMessages
				.getString("NewArtifactWizardPage.default")); //$NON-NLS-1$

		artifactNameDialogField = new StringDialogField();
		artifactNameDialogField.setDialogFieldListener(adapter);
		artifactNameDialogField.setLabelText("Name:"); //$NON-NLS-1$

		artifactPackageStatus = new StatusInfo();
		artifactNameStatus = new StatusInfo();
		tsRuntimeContextStatus = new StatusInfo();

		// The attribute list selection
		String[] addButtons = new String[] { /* 0 */"add", //$NON-NLS-1$
				/* 1 */"edit", /* 2 */"remove" //$NON-NLS-1$
		};

		attributesDialogField = new ListDialogField(adapter, addButtons,
				new LabelRefsListLabelProvider());
		attributesDialogField.setDialogFieldListener(adapter);
		String pluginsLabel = "Labels:";

		attributesDialogField.setLabelText(pluginsLabel);
		attributesDialogField.setRemoveButtonIndex(2);

		baseEnumType = new ComboDialogField(SWT.DROP_DOWN | SWT.READ_ONLY);
		baseEnumType.setDialogFieldListener(adapter);
		baseEnumType.setLabelText("Base Type:");
		baseEnumType.setItems(supportedEnumTypes);
		baseEnumType.selectItem(0);

		initTSRuntimeContext(jelem);
		initContainerPage(jelem);

		initArtifactPage(jelem);
		initPage(jelem);
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		setFocus();
		doStatusUpdate();
	}

	private void initArtifactPage(IJavaElement jElement) {
		IPackageFragment pack = null;

		if (jElement != null) {
			// evaluate the enclosing type
			pack = (IPackageFragment) jElement
					.getAncestor(IJavaElement.PACKAGE_FRAGMENT);
			IType typeInCU = (IType) jElement.getAncestor(IJavaElement.TYPE);
		}

		String typeName = ""; //$NON-NLS-1$

		ITextSelection selection = getCurrentTextSelection();
		if (selection != null) {
			String text = selection.getText();
			if (JavaConventions.validateJavaTypeName(text).isOK()) {
				typeName = text;
			}
		}

		ArrayList attributeList = new ArrayList(5);
		setAttributeRefs(attributeList, true);

		setArtifactPackageFragment(pack);

		setArtifactName(typeName, true);
	}

	/**
	 * Creates a separator line. Expects a <code>GridLayout</code> with at
	 * least 1 column.
	 * 
	 * @param composite
	 *            the parent composite
	 * @param nColumns
	 *            number of columns to span
	 */
	@Override
	protected void createSeparator(Composite composite, int nColumns) {
		(new Separator(SWT.SEPARATOR | SWT.HORIZONTAL)).doFillIntoGrid(
				composite, nColumns, convertHeightInCharsToPixels(1));
	}

	protected void createBaseEnumTypeControls(Composite composite, int nColumns) {
		baseEnumType.doFillIntoGrid(composite, nColumns);
		Combo combo = baseEnumType.getComboControl(null);
		LayoutUtil.setWidthHint(combo, getMaxFieldWidth());
		LayoutUtil.setHorizontalGrabbing(combo);
	}

	protected void createArtifactControls(Composite composite, int nColumns) {
		createArtifactControls(composite, nColumns, true);
	}

	protected void createArtifactControls(Composite composite, int nColumns,
			boolean createAttributeList) {
		createContainerControls(composite, nColumns);

		createPackageControls(composite, nColumns);
		createArtifactNameControls(composite, nColumns);

		createModifiersButtonsControls(composite, nColumns);
		if (createAttributeList) {
			createAttributeListControl(composite, nColumns);
		}

		createBaseEnumTypeControls(composite, nColumns);
		// createSeparator(composite, nColumns);
		// createTargetInterfacePackageControls(composite, nColumns);

		createEditMessage(composite, nColumns);
	}

	private void createEditMessage(Composite composite, int nColumns) {

		Label empty = new Label(composite, SWT.NULL);
		GridData gd = new GridData(GridData.FILL);
		gd.horizontalSpan = nColumns;
		empty.setLayoutData(gd);

		Label explanationMessage = new Label(composite, SWT.NULL);
		explanationMessage
				.setText("Please edit this artifact to specify additional details.");
		gd = new GridData(GridData.FILL);
		gd.horizontalSpan = nColumns;
		explanationMessage.setLayoutData(gd);
	}

	/**
	 * Creates the controls for the package name field. Expects a
	 * <code>GridLayout</code> with at least 4 columns.
	 * 
	 * @param composite
	 *            the parent composite
	 * @param nColumns
	 *            number of columns to span
	 */
	protected void createPackageControls(Composite composite, int nColumns) {
		packageDialogField.doFillIntoGrid(composite, nColumns);
		Text text = packageDialogField.getTextControl(null);
		LayoutUtil.setWidthHint(text, getMaxFieldWidth());
		LayoutUtil.setHorizontalGrabbing(text);
		ControlContentAssistHelper.createTextContentAssistant(text,
				packageCompletionProcessor);
	}

	/**
	 * Creates the controls for the package name field. Expects a
	 * <code>GridLayout</code> with at least 4 columns.
	 * 
	 * @param composite
	 *            the parent composite
	 * @param nColumns
	 *            number of columns to span
	 */
	protected void createTargetInterfacePackageControls(Composite composite,
			int nColumns) {
		targetPackageDialogField.doFillIntoGrid(composite, nColumns);
		Text text = targetPackageDialogField.getTextControl(null);
		LayoutUtil.setWidthHint(text, getMaxFieldWidth());
		LayoutUtil.setHorizontalGrabbing(text);
		ControlContentAssistHelper.createTextContentAssistant(text,
				packageCompletionProcessor);
	}

	protected void createModifiersButtonsControls(Composite composite,
			int nColumns) {
		// LayoutUtil.setHorizontalSpan(
		// modifierButtons.getLabelControl(composite), 1);
		//
		// Control control =
		// modifierButtons.getSelectionButtonsGroup(composite);
		// GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		// gd.horizontalSpan = nColumns - 2;
		// control.setLayoutData(gd);
		//
		// DialogField.createEmptySpace(composite);
	}

	/**
	 * Creates the controls for the type name field. Expects a
	 * <code>GridLayout</code> with at least 2 columns.
	 * 
	 * @param composite
	 *            the parent composite
	 * @param nColumns
	 *            number of columns to span
	 */
	protected void createArtifactNameControls(Composite composite, int nColumns) {
		artifactNameDialogField.doFillIntoGrid(composite, nColumns - 1);
		DialogField.createEmptySpace(composite);

		LayoutUtil.setWidthHint(artifactNameDialogField.getTextControl(null),
				getMaxFieldWidth());
	}

	protected void createAttributeListControl(Composite composite, int nColumns) {
		attributesDialogField.doFillIntoGrid(composite, nColumns);
		GridData gd = (GridData) attributesDialogField.getListControl(null)
				.getLayoutData();
		gd.heightHint = convertHeightInCharsToPixels(3);
		gd.grabExcessVerticalSpace = false;
		gd.widthHint = getMaxFieldWidth();

	}

	public List getAttributeRefs() {
		return attributesDialogField.getElements();
	}

	public void setAttributeRefs(List attributeRefs, boolean canBeModified) {
		attributesDialogField.setElements(attributeRefs);
		attributesDialogField.setEnabled(canBeModified);
	}

	/**
	 * Returns the package fragment corresponding to the current input.
	 * 
	 * @return a package fragment or <code>null</code> if the input could not
	 *         be resolved.
	 */
	public IPackageFragment getArtifactPackageFragment() {
		return artifactPackage;
	}

	/**
	 * Sets the package fragment to the given value. The method updates the
	 * model and the text of the control.
	 * 
	 * @param pack
	 *            the package fragment to be set
	 * @param canBeModified
	 *            if <code>true</code> the package fragment is editable;
	 *            otherwise it is read-only.
	 */
	public void setArtifactPackageFragment(IPackageFragment pack) {
		artifactPackage = pack;
		String str = (pack == null) ? "" : pack.getElementName(); //$NON-NLS-1$
		packageDialogField.setText(str);
	}

	/**
	 * Returns the text of the package input field.
	 * 
	 * @return the text of the package input field
	 */
	public String getPackageText() {
		return packageDialogField.getText();
	}

	/**
	 * Returns the type name entered into the type input field.
	 * 
	 * @return the type name
	 */
	public String getArtifactName() {
		return artifactNameDialogField.getText();
	}

	/**
	 * Sets the type name input field's text to the given value. Method doesn't
	 * update the model.
	 * 
	 * @param name
	 *            the new type name
	 * @param canBeModified
	 *            if <code>true</code> the type name field is editable;
	 *            otherwise it is read-only.
	 */
	public void setArtifactName(String name, boolean canBeModified) {
		artifactNameDialogField.setText(name);
		artifactNameDialogField.setEnabled(canBeModified);
	}

	/*
	 * @see org.eclipse.jdt.ui.wizards.NewContainerWizardPage#containerChanged()
	 */
	@Override
	protected IStatus containerChanged() {
		IStatus status = super.containerChanged();
		packageCompletionProcessor
				.setPackageFragmentRoot(getPackageFragmentRoot());
		return status;
	}

	/**
	 * Sets the focus on the type name input field.
	 */
	protected void setFocus() {
		artifactNameDialogField.setFocus();
	}

	private void artifactPageChangeControlPressed(DialogField field) {
		if (field == packageDialogField) {
			IPackageFragment pack = choosePackage();
			if (pack != null) {
				packageDialogField.setText(pack.getElementName());
			}
		} else if (field == targetPackageDialogField) {
			IPackageFragment pack = choosePackage();
			if (pack != null) {
				targetPackageDialogField.setText(pack.getElementName());
			}
		}
	}

	private int uniqueValue = 0;

	private String getUniqueLabelValue() {
		if ("int".equals(this.baseEnumType.getText().trim()))
			return String.valueOf(uniqueValue++);
		else
			return "\"" + String.valueOf(uniqueValue++) + "\"";
	}

	private void artifactPageCustomButtonPressed(DialogField field, int index) {
		if (field == attributesDialogField) {
			if (index == 0) {
				LabelRef newLabel = new LabelRef();
				newLabel.setLabelClass(this.baseEnumType.getText().trim());
				newLabel.setValue(getUniqueLabelValue());
				LabelsSelectionDialog dialog = new LabelsSelectionDialog(
						getShell(), newLabel, this, this.attributesDialogField
								.getElements(), false);

				if (dialog.open() == Window.OK) {
					LabelRef ref = dialog.getLabelRef();
					attributesDialogField.addElement(ref);
				}
			} else if (index == 1) {
				List selectedElem = attributesDialogField.getSelectedElements();
				if (selectedElem.size() == 0)
					return;
				LabelRef ref = (LabelRef) selectedElem.get(0);

				LabelsSelectionDialog dialog = new LabelsSelectionDialog(
						getShell(), (LabelRef) ref.clone(), this, this
								.getAttributeRefs(), true);

				if (dialog.open() == Window.OK) {
					LabelRef returnedRef = dialog.getLabelRef();
					ref.applyValues(returnedRef);
					attributesDialogField.refresh();
				}
			}
		}
	}

	private IPackageFragment choosePackage() {
		IPackageFragmentRoot froot = getPackageFragmentRoot();
		IJavaElement[] packages = null;
		try {
			if (froot != null && froot.exists()) {
				packages = froot.getChildren();
			}
		} catch (JavaModelException e) {
			JavaPlugin.log(e);
		}
		if (packages == null) {
			packages = new IJavaElement[0];
		}

		ElementListSelectionDialog dialog = new ElementListSelectionDialog(
				getShell(), new JavaElementLabelProvider(
						JavaElementLabelProvider.SHOW_DEFAULT));
		dialog.setIgnoreCase(false);
		dialog.setTitle(NewWizardMessages
				.getString("NewArtifactWizardPage.ChoosePackageDialog.title")); //$NON-NLS-1$
		dialog
				.setMessage(NewWizardMessages
						.getString("NewArtifactWizardPage.ChoosePackageDialog.description")); //$NON-NLS-1$
		dialog.setEmptyListMessage(NewWizardMessages
				.getString("NewArtifactWizardPage.ChoosePackageDialog.empty")); //$NON-NLS-1$
		dialog.setElements(packages);
		IPackageFragment pack = getArtifactPackageFragment();
		if (pack != null) {
			dialog.setInitialSelections(new Object[] { pack });
		}

		if (dialog.open() == Window.OK)
			return (IPackageFragment) dialog.getFirstResult();
		return null;
	}

	/**
	 * Hook method that gets called when the type name has changed. The method
	 * validates the type name and returns the status of the validation.
	 * <p>
	 * Subclasses may extend this method to perform their own validation.
	 * </p>
	 * 
	 * @return the status of the validation
	 */
	protected IStatus artifactNameChanged() {
		StatusInfo status = new StatusInfo();
		String typeName = getArtifactName();
		// must not be empty
		if (typeName.length() == 0) {
			status
					.setError(NewWizardMessages
							.getString("NewArtifactWizardPage.error.EnterArtifactName")); //$NON-NLS-1$
			return status;
		}
		if (typeName.indexOf('.') != -1) {
			status.setError(NewWizardMessages
					.getString("NewArtifactWizardPage.error.QualifiedName")); //$NON-NLS-1$
			return status;
		}
		IStatus val = JavaConventions.validateJavaTypeName(typeName);
		if (val.getSeverity() == IStatus.ERROR) {
			status
					.setError(NewWizardMessages
							.getFormattedString(
									"NewArtifactWizardPage.error.InvalidArtifactName", val.getMessage())); //$NON-NLS-1$
			return status;
		} else if (val.getSeverity() == IStatus.WARNING) {
			status
					.setWarning(NewWizardMessages
							.getFormattedString(
									"NewArtifactWizardPage.warning.TypeNameDiscouraged", val.getMessage())); //$NON-NLS-1$
			// continue checking
		}

		return status;
	}

	/*
	 * Updates the 'default' label next to the package field.
	 */
	private void updatePackageStatusLabel() {
		String packName = getPackageText();

		if (packName.length() == 0) {
			packageDialogField.setStatus(NewWizardMessages
					.getString("NewArtifactWizardPage.default")); //$NON-NLS-1$
		} else {
			packageDialogField.setStatus(""); //$NON-NLS-1$
		}
	}

	/*
	 * A field on the type has changed. The fields' status and all dependent
	 * status are updated.
	 */
	private void artifactPageDialogFieldChanged(DialogField field) {
		String fieldName = null;
		if (field == packageDialogField) {
			artifactPackageStatus = packageChanged();
			updatePackageStatusLabel();
			artifactNameStatus = artifactNameChanged();
			fieldName = PACKAGE;
		} else if (field == artifactNameDialogField) {
			artifactNameStatus = artifactNameChanged();
			fieldName = ARTIFACTNAME;
		} else if (field == baseEnumType) {
			updateLabelBaseEnumType();
			fieldName = BASE_TYPE;
		}

		// tell all others
		handleFieldChanged(fieldName);
	}

	/**
	 * As a result of a change of the BaseType this method goes through all
	 * defined labels and sets the type correctly.
	 * 
	 */
	private void updateLabelBaseEnumType() {
		List attrRefs = getAttributeRefs();
		for (Iterator iter = attrRefs.iterator(); iter.hasNext();) {
			LabelRef ref = (LabelRef) iter.next();

			ref.setLabelClass(baseEnumType.getText().trim());
		}
	}

	/**
	 * A hook method that gets called when the package field has changed. The
	 * method validates the package name and returns the status of the
	 * validation. The validation also updates the package fragment model.
	 * <p>
	 * Subclasses may extend this method to perform their own validation.
	 * </p>
	 * 
	 * @return the status of the validation
	 */
	protected IStatus packageChanged() {
		StatusInfo status = new StatusInfo();
		packageDialogField.enableButton(getPackageFragmentRoot() != null);

		String packName = getPackageText();
		if (packName.length() > 0) {
			IStatus val = JavaConventions.validatePackageName(packName);
			if (val.getSeverity() == IStatus.ERROR) {
				status
						.setError(NewWizardMessages
								.getFormattedString(
										"NewArtifactWizardPage.error.InvalidPackageName", val.getMessage())); //$NON-NLS-1$
				return status;
			} else if (val.getSeverity() == IStatus.WARNING) {
				status
						.setWarning(NewWizardMessages
								.getFormattedString(
										"NewArtifactWizardPage.warning.DiscouragedPackageName", val.getMessage())); //$NON-NLS-1$
				// continue
			}
		} else {
			status
					.setWarning(NewWizardMessages
							.getString("NewArtifactWizardPage.warning.DefaultPackageDiscouraged")); //$NON-NLS-1$
		}

		IPackageFragmentRoot root = getPackageFragmentRoot();
		if (root != null) {
			if (root.getJavaProject().exists() && packName.length() > 0) {
				try {
					IPath rootPath = root.getPath();
					IPath outputPath = root.getJavaProject()
							.getOutputLocation();
					if (rootPath.isPrefixOf(outputPath)
							&& !rootPath.equals(outputPath)) {
						// if the bin folder is inside of our root, don't allow
						// to name a package
						// like the bin folder
						IPath packagePath = rootPath.append(packName.replace(
								'.', '/'));
						if (outputPath.isPrefixOf(packagePath)) {
							status
									.setError(NewWizardMessages
											.getString("NewArtifactWizardPage.error.ClashOutputLocation")); //$NON-NLS-1$
							return status;
						}
					}
				} catch (JavaModelException e) {
					JavaPlugin.log(e);
					// let pass
				}
			}

			artifactPackage = root.getPackageFragment(packName);
		} else {
			status.setError(""); //$NON-NLS-1$
		}
		return status;
	}

	public final static String CONTAINER_NAME = "containerName";

	public final static String CONTAINER_PATH = "containerPath";

	public final static String SRCDIRECTORY_NAME = "srcDirectoryName";

	public final static String PACKAGE_NAME = "packageName";

	public final static String ARTIFACT_NAME = "artifactName";

	public final static String GENERATE = "generate";

	public final static String ATTRIBUTE_LIST = "constantList";

	public final static String INTERFACE_PACKAGE = "interfacePackage";

	public final static String BASE_TYPE = "baseType";

	/**
	 * Returns the common properties as gathered by this page.
	 * 
	 * @return Properties - the common properties gathered by this page
	 */
	public Properties getPropertiesCommons() {
		Properties result = new Properties();
		// TODO: this is way messy and needs to be re-done!!!

		result.put(CONTAINER_PATH, getPackageFragmentRoot().getJavaProject()
				.getElementName());
		result.put(SRCDIRECTORY_NAME, getPackageFragmentRoot().getPath()
				.removeFirstSegments(1).toOSString());
		result.put(CONTAINER_NAME, getWorkspaceRoot().getLocation()
				.toOSString());
		result.put(PACKAGE_NAME, packageDialogField.getText());
		result.put("isAbstract", "false");
		result.put(ARTIFACT_NAME, artifactNameDialogField.getText());
		result.put(INTERFACE_PACKAGE, targetPackageDialogField.getText());
		result.put(BASE_TYPE, baseEnumType.getText());
		// result.put(GENERATE, new Boolean( modifierButtons.isSelected(0)));
		result.put(GENERATE, new Boolean(true));
		result.put(ATTRIBUTE_LIST, getAttributeRefs());

		return result;
	}

	protected IJavaElement getInitialElement() {
		return initialElement;
	}

	protected void setInitialElement(IJavaElement initialElement) {
		this.initialElement = initialElement;
	}

	public int getFieldCounter() {
		return fieldCounter;
	}

	public void setFieldCounter(int fieldCounter) {
		this.fieldCounter = fieldCounter;
	}

}