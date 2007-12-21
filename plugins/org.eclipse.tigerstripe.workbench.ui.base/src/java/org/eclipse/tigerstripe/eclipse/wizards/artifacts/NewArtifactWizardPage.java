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

import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.dialogs.StatusUtil;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.ControlContentAssistHelper;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.JavaPackageCompletionProcessor;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.JavaTypeCompletionProcessor;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogFieldGroup;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.Separator;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonStatusDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jdt.ui.wizards.NewContainerWizardPage;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.api.IPluginReference;
import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.model.IextType;
import org.eclipse.tigerstripe.api.project.IProjectDetails;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.eclipse.runtime.messages.NewWizardMessages;
import org.eclipse.tigerstripe.eclipse.wizards.TSRuntimeContext;
import org.eclipse.tigerstripe.eclipse.wizards.WizardUtils;
import org.eclipse.tigerstripe.eclipse.wizards.artifacts.MethodDetailsDialog.ParameterRef;
import org.eclipse.tigerstripe.eclipse.wizards.model.ArtifactAttributeModel;
import org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs.BrowseForArtifactDialog;
import org.eclipse.tigerstripe.workbench.ui.eclipse.elements.LabelListDialog;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public abstract class NewArtifactWizardPage extends NewContainerWizardPage
		implements INewArtifactWizardPage {

	private final static String PAGE_NAME = "NewAssociationWizardPage"; //$NON-NLS-1$

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

	/** Field ID of the method stubs check boxes. */
	protected final static String LABELSLIST = PAGE_NAME + ".labelsList"; //$NON-NLS-1$

	// The extended class
	private StringButtonDialogField extendedClassDialogField;

	protected IStatus extendedClassStatus;

	private JavaTypeCompletionProcessor extendedClassCompletionProcessor;

	// The Labels button
	private SelectionButtonDialogField labelButton;

	private int fieldCounter = 0;

	/**
	 * The package of the generated artifact
	 */
	private IPackageFragment artifactPackage;

	protected IStatus artifactPackageStatus;

	protected IStatus artifactNameStatus;

	// the attribute list selection
	private ListDialogField attributesDialogField;

	// the method list selection
	private ListDialogField methodsDialogField;

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
	private SelectionButtonDialogFieldGroup modifierButtons;

	private IJavaElement initialElement;

	private TSRuntimeContext tsRuntimeContext;

	protected IStatus tsRuntimeContextStatus;

	private List labelRefsList;

	public NewArtifactWizardPage(String pageName) {
		super(pageName);

		ArtifactFieldsAdapter adapter = new ArtifactFieldsAdapter();

		artifactNameDialogField = new StringDialogField();
		artifactNameDialogField.setDialogFieldListener(adapter);
		artifactNameDialogField.setLabelText("Name:"); //$NON-NLS-1$

		labelButton = new SelectionButtonDialogField(SWT.PUSH);
		labelButton.setDialogFieldListener(adapter);
		labelButton.setLabelText("Constants");

		labelRefsList = new ArrayList();
	}

	private class ArtifactFieldsAdapter implements IStringButtonAdapter,
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
			artifactPageDoubleClicked(field);
		}
	}

	private class AttributesListLabelProvider extends LabelProvider {

		private Image attributeImage;

		public AttributesListLabelProvider() {
			super();
			attributeImage = JavaPluginImages
					.get(JavaPluginImages.IMG_FIELD_DEFAULT);
		}

		@Override
		public Image getImage(Object element) {
			return attributeImage;
		}

		@Override
		public String getText(Object element) {
			if (element == null)
				return "unknown";

			ArtifactAttributeModel attributeRef = (ArtifactAttributeModel) element;
			return attributeRef.getName()
					+ " : "
					+ attributeRef.getAttributeClass()
					+ (attributeRef.getDimensions() == IextType.MULTIPLICITY_MULTI ? "[]"
							: "");
		}
	}

	private class MethodsListLabelProvider extends LabelProvider {

		private Image methodImage;

		public MethodsListLabelProvider() {
			super();
			methodImage = JavaPluginImages
					.get(JavaPluginImages.IMG_FIELD_DEFAULT);
		}

		@Override
		public Image getImage(Object element) {
			return methodImage;
		}

		@Override
		public String getText(Object element) {
			if (element == null)
				return "unknown";

			MethodDetailsModel model = (MethodDetailsModel) element;
			return model.getSignature();
		}
	}

	/**
	 * Utility method to inspect a selection to find a Java element.
	 * 
	 * @param selection
	 *            the selection to be inspected
	 * @return a Java element to be used as the initial selection, or
	 *         <code>null</code>, if no Java element exists in the given
	 *         selection
	 */
	@Override
	protected IJavaElement getInitialJavaElement(IStructuredSelection selection) {
		return WizardUtils.getInitialJavaElement(selection);
	}

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
		ArtifactFieldsAdapter adapter = new ArtifactFieldsAdapter();

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

		String[] buttonNames1 = new String[] { /* 0 == GENERATE_INDEX */"Generate" }; //$NON-NLS-1$

		modifierButtons = new SelectionButtonDialogFieldGroup(SWT.CHECK,
				buttonNames1, 4);
		modifierButtons.setDialogFieldListener(adapter);
		modifierButtons.setLabelText(NewWizardMessages
				.getString("NewArtifactWizardPage.modifier.generate.label")); //$NON-NLS-1$
		modifierButtons.setSelection(0, true);

		extendedClassDialogField = new StringButtonDialogField(adapter);
		extendedClassDialogField.setDialogFieldListener(adapter);
		extendedClassDialogField.setLabelText("Super-artifact"); //$NON-NLS-1$
		extendedClassDialogField.setButtonLabel(NewWizardMessages
				.getString("NewEntityWizardPage.primaryKey.button")); //$NON-NLS-1$

		extendedClassStatus = new StatusInfo();
		extendedClassCompletionProcessor = new JavaTypeCompletionProcessor(
				false, false);

		artifactPackageStatus = packageChanged();
		artifactNameStatus = artifactNameChanged();
		tsRuntimeContextStatus = new StatusInfo();

		// The attribute list selection
		String[] addButtons = new String[] { /* 0 */"add", //$NON-NLS-1$
				/* 1 */"edit", /* 2 */"remove" //$NON-NLS-1$
		};

		attributesDialogField = new ListDialogField(adapter, addButtons,
				new AttributesListLabelProvider());
		attributesDialogField.setDialogFieldListener(adapter);
		String pluginsLabel = "Attributes:";

		attributesDialogField.setLabelText(pluginsLabel);
		attributesDialogField.setRemoveButtonIndex(2);

		methodsDialogField = new ListDialogField(adapter, addButtons,
				new MethodsListLabelProvider());
		methodsDialogField.setDialogFieldListener(adapter);
		String methodsLabel = "Methods:";

		methodsDialogField.setLabelText(methodsLabel);
		methodsDialogField.setRemoveButtonIndex(2);

		initTSRuntimeContext(jelem);

		initContainerPage(jelem);

		initArtifactPage(jelem);
		initPage(jelem);
	}

	/**
	 * This tries to initialize the TSRuntime context based on the given
	 * IJavaElement
	 * 
	 * @param jElement
	 */
	protected void initTSRuntimeContext(IJavaElement jElement) {
		this.tsRuntimeContext = new TSRuntimeContext();
		this.tsRuntimeContext.readTSDescriptor(jElement);
		tsRuntimeContextStatus = tsRuntimeContextChanged();
	}

	protected TSRuntimeContext getTSRuntimeContext() {
		return this.tsRuntimeContext;
	}

	/**
	 * To be implemented by the extending class
	 * 
	 * @param jElement
	 */
	protected abstract void initPage(IJavaElement jElement);

	@Override
	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		super.setVisible(visible);
		setFocus();
		doStatusUpdate();
	}

	@Override
	protected void updateStatus(IStatus status) {
		super.updateStatus(status);

		StatusUtil.applyToStatusLine(this, status);
	}

	protected abstract void doStatusUpdate();

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
	protected void createSeparator(Composite composite, int nColumns) {
		(new Separator(SWT.SEPARATOR | SWT.HORIZONTAL)).doFillIntoGrid(
				composite, nColumns, convertHeightInCharsToPixels(1));
	}

	protected void createArtifactControls(Composite composite, int nColumns) {
		createArtifactControls(composite, nColumns, true, true);
	}

	protected void createLabelControls(Composite composite, int nColumns) {
		DialogField.createEmptySpace(composite, nColumns - 1);
		labelButton.doFillIntoGrid(composite, 1);
	}

	protected void createArtifactControls(Composite composite, int nColumns,
			boolean createAttributeList, boolean createMethodList) {
		createContainerControls(composite, nColumns);

		createPackageControls(composite, nColumns);
		createArtifactNameControls(composite, nColumns);

		createSeparator(composite, nColumns);
		createExtendedArtifactControls(composite, nColumns);

		// createModifiersButtonsControls(composite, nColumns);
		if (createAttributeList) {
			createAttributeListControl(composite, nColumns);
		}
		// if (createMethodList) {
		// createMethodListControl(composite, nColumns);
		// }
		// createLabelControls(composite, nColumns);
		//
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
		LayoutUtil.setHorizontalSpan(
				modifierButtons.getLabelControl(composite), 1);

		Control control = modifierButtons.getSelectionButtonsGroup(composite);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan = nColumns - 2;
		control.setLayoutData(gd);

		DialogField.createEmptySpace(composite);
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

	protected void createMethodListControl(Composite composite, int nColumns) {
		methodsDialogField.doFillIntoGrid(composite, nColumns);
		GridData gd = (GridData) methodsDialogField.getListControl(null)
				.getLayoutData();
		gd.heightHint = convertHeightInCharsToPixels(3);
		gd.grabExcessVerticalSpace = false;
		gd.widthHint = getMaxFieldWidth();

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
	protected void createExtendedArtifactControls(Composite composite,
			int nColumns) {
		extendedClassDialogField.doFillIntoGrid(composite, nColumns);
		Text text = extendedClassDialogField.getTextControl(null);
		LayoutUtil.setWidthHint(text, getMaxFieldWidth());
		ControlContentAssistHelper.createTextContentAssistant(text,
				extendedClassCompletionProcessor);
	}

	public List getAttributeRefs() {
		return attributesDialogField.getElements();
	}

	public List getMethodDetailsModels() {
		return methodsDialogField.getElements();
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
		return packageDialogField.getText().trim();
	}

	/**
	 * Returns the type name entered into the type input field.
	 * 
	 * @return the type name
	 */
	public String getArtifactName() {
		return artifactNameDialogField.getText().trim();
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
	 * @see org.eclipse.jdt.ui.wizards.NewContainerWizardPage#handleFieldChanged(String)
	 */
	@Override
	protected void handleFieldChanged(String fieldName) {
		super.handleFieldChanged(fieldName);
		if (fieldName == CONTAINER) {
			tsRuntimeContextStatus = tsRuntimeContextChanged();
		} else if (fieldName == ARTIFACTNAME) {
			artifactNameStatus = artifactNameChanged();
		} else if (fieldName == PACKAGE) {
			artifactPackageStatus = packageChanged();
		}
	}

	protected IStatus tsRuntimeContextChanged() {

		StatusInfo status = new StatusInfo();

		this.tsRuntimeContext = new TSRuntimeContext();
		this.tsRuntimeContext.readTSDescriptor(getPackageFragmentRoot());
		initFromContext();

		if (getTSRuntimeContext() == null
				|| !getTSRuntimeContext().isValidProject()) {
			status.setError("Invalid Tigerstripe Project");
		}
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
		} else if (field == extendedClassDialogField) {
			String type = chooseExtendedType();
			if (type != null) {
				// extendedClassDialogField.setText(JavaModelUtil
				// .getFullyQualifiedName(type));
				extendedClassDialogField.setText(type);
			}
		}
	}

	private void artifactPageCustomButtonPressed(DialogField field, int index) {
		if (field == attributesDialogField) {
			if (index == 0) {
				AttributesSelectionDialog dialog = new AttributesSelectionDialog(
						getShell(), new ArtifactAttributeModel(), this,
						this.attributesDialogField.getElements(), false);

				if (dialog.open() == Window.OK) {
					ArtifactAttributeModel ref = dialog.getAttributeRef();
					attributesDialogField.addElement(ref);
				}
			} else if (index == 1) {
				List selectedElem = attributesDialogField.getSelectedElements();
				if (selectedElem.size() == 0)
					return;
				ArtifactAttributeModel ref = (ArtifactAttributeModel) selectedElem
						.get(0);

				AttributesSelectionDialog dialog = new AttributesSelectionDialog(
						getShell(), (ArtifactAttributeModel) ref.clone(), this,
						this.attributesDialogField.getElements(), true);

				if (dialog.open() == Window.OK) {
					ArtifactAttributeModel returnedRef = dialog
							.getAttributeRef();
					ref.applyValues(returnedRef);
					attributesDialogField.refresh();
				}
			}
		} else if (field == methodsDialogField) {
			if (index == 0) {
				MethodDetailsDialog dialog = new MethodDetailsDialog(
						getShell(), new MethodDetailsModel(), this,
						this.methodsDialogField.getElements(), false);
				if (dialog.open() == Window.OK) {
					MethodDetailsModel model = dialog.getMethodDetailsModel();
					methodsDialogField.addElement(model);
				}
			} else if (index == 1) {
				List selectedElem = methodsDialogField.getSelectedElements();
				if (selectedElem.size() == 0)
					return;
				MethodDetailsModel ref = (MethodDetailsModel) selectedElem
						.get(0);

				MethodDetailsDialog dialog = new MethodDetailsDialog(
						getShell(), (MethodDetailsModel) ref.clone(), this,
						this.methodsDialogField.getElements(), true);

				if (dialog.open() == Window.OK) {
					MethodDetailsModel returnedRef = dialog
							.getMethodDetailsModel();
					ref.applyValues(returnedRef);
					this.methodsDialogField.refresh();
				}
			}
		}

	}

	/**
	 * 
	 * @author Eric Dillon TODO: this should actually be extracted into its own
	 *         self contained class.
	 */
	public class MethodDetailsModel {

		private Properties ossjProperties;

		private String methodName;

		private List exceptionList;

		private List parameterList;

		private boolean isVoid;

		private String returnClass;

		private int returnDimension;

		private int methodModifier;

		public Properties getOssjProperties() {
			return this.ossjProperties;
		}

		public void setOssjProperties(Properties ossjProperties) {
			this.ossjProperties = ossjProperties;
		}

		public void applyValues(MethodDetailsModel model) {
			this.methodName = model.getMethodName();
			this.isVoid = model.isVoid();
			this.returnClass = model.getReturnClass();
			this.returnDimension = model.getReturnDimension();
			this.methodModifier = model.getMethodModifier();
			this.parameterList = model.getParameterList();
			this.ossjProperties = model.ossjProperties;
		}

		public String getSignature() {
			String signature = modifierLabels[getMethodModifier()];
			signature = signature + " ";

			if (isVoid()) {
				signature = signature + "void";
			} else {
				signature = signature + getReturnClass();
				for (int i = 0; i < getReturnDimension(); i++) {
					signature = signature + "[]";
				}
			}

			signature = signature + " " + getMethodName() + "(";

			if (getParameterList() != null) {
				Iterator iter = getParameterList().iterator();
				boolean firstPass = true;
				while (iter.hasNext()) {

					if (!firstPass) {
						signature = signature + ", ";
					}
					ParameterRef ref = (ParameterRef) iter.next();
					signature = signature + ref.getSignature();

					firstPass = false;
				}
			}
			signature = signature + ")";

			return signature;
		}

		@Override
		public Object clone() {
			MethodDetailsModel result = new MethodDetailsModel();
			result.applyValues(this);
			return result;
		}

		public final int PUBLIC = 0;

		public final int PROTECTED = 1;

		public final int PRIVATE = 2;

		private final String[] modifierLabels = { "public", "protected",
				"private" };

		public String getProperty(String key) {
			return this.getProperty(key);
		}

		public List getExceptionList() {
			return exceptionList;
		}

		public void setExceptionList(List exceptionList) {
			this.exceptionList = exceptionList;
		}

		public List getParameterList() {
			return parameterList;
		}

		public void setParameterList(List parameterList) {
			this.parameterList = parameterList;
		}

		public boolean isVoid() {
			return isVoid;
		}

		public void setVoid(boolean isVoid) {
			this.isVoid = isVoid;
		}

		public int getMethodModifier() {
			return methodModifier;
		}

		public void setMethodModifier(int methodModifier) {
			this.methodModifier = methodModifier;
		}

		public String getMethodName() {
			return methodName;
		}

		public void setMethodName(String methodName) {
			this.methodName = methodName;
		}

		public String getReturnClass() {
			return returnClass;
		}

		public void setReturnClass(String returnClass) {
			this.returnClass = returnClass;
		}

		public int getReturnDimension() {
			return returnDimension;
		}

		public void setReturnDimension(int returnDimension) {
			this.returnDimension = returnDimension;
		}

		public String getFinalReturnClass() {
			if (isVoid())
				return "void";
			else {
				String result = getReturnClass();
				for (int i = 0; i < getReturnDimension(); i++) {
					result = result + "[]";
				}
				return result;
			}
		}
	}

	private void artifactPageDoubleClicked(DialogField field) {
		if (field == attributesDialogField) {
			List selectedElem = attributesDialogField.getSelectedElements();
			if (selectedElem.size() == 0)
				return;
			ArtifactAttributeModel ref = (ArtifactAttributeModel) selectedElem
					.get(0);

			AttributesSelectionDialog dialog = new AttributesSelectionDialog(
					getShell(), (ArtifactAttributeModel) ref.clone(), this,
					this.attributesDialogField.getElements(), true);

			if (dialog.open() == Window.OK) {
				ArtifactAttributeModel returnedRef = dialog.getAttributeRef();
				ref.applyValues(returnedRef);
				attributesDialogField.refresh();
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
		if (typeName == null || typeName.length() == 0) {
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

		return checkArtifactDonotExist(typeName);
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
		} else if (field == extendedClassDialogField) {
			extendedClassStatus = extendedClassChanged();
			doStatusUpdate();
			fieldName = EXTENDED_ARTIFACT;
		}
		if (field == labelButton) {

			LabelListDialog dialog = new LabelListDialog(getShell(),
					this.labelRefsList, this);

			if (dialog.open() == Window.OK) {
				this.labelRefsList = dialog.getLabelRefsList();
			}
			fieldName = LABELSLIST;
		}

		// tell all others
		handleFieldChanged(fieldName);
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
		}
		return status;
	}

	public final static String CONTAINER_NAME = "containerName";

	public final static String CONTAINER_PATH = "containerPath";

	public final static String SRCDIRECTORY_NAME = "srcDirectoryName";

	public final static String PACKAGE_NAME = "packageName";

	public final static String ARTIFACT_NAME = "artifactName";

	public final static String ARTIFACT_DESCRIPTION = "artifactDescription";

	public final static String GENERATE = "generate";

	public final static String ATTRIBUTE_LIST = "attributes";

	public final static String METHOD_LIST = "methods";

	public final static String INTERFACE_PACKAGE = "interfacePackage";

	public final static String EXTENDED_ARTIFACT = "extendedArtifact";

	public final static String CONSTANT_LIST = "constantList";

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
		result.put(PACKAGE_NAME, packageDialogField.getText().trim());
		result.put(ARTIFACT_NAME, artifactNameDialogField.getText().trim());
		result.put("isAbstract", "false");
		result
				.put(INTERFACE_PACKAGE, targetPackageDialogField.getText()
						.trim());
		result.put(GENERATE, new Boolean(modifierButtons.isSelected(0)));
		result.put(ATTRIBUTE_LIST, getAttributeRefs());
		result.put(METHOD_LIST, getMethodDetailsModels());
		result.put(CONSTANT_LIST, getConstantList());
		result
				.put(EXTENDED_ARTIFACT, extendedClassDialogField.getText()
						.trim());
		return result;
	}

	public List getConstantList() {
		return labelRefsList;
	}

	public abstract IAbstractArtifact[] getArtifactModelForExtend();

	public abstract Properties getProperties();

	protected abstract ArtifactDefinitionGenerator getGenerator(
			Properties pageProperties, Writer writer);

	protected IJavaElement getInitialElement() {
		return initialElement;
	}

	protected void setInitialElement(IJavaElement initialElement) {
		this.initialElement = initialElement;
	}

	protected final static String OSSJ_JVT_SPEC = "ossj-jvt-spec";

	protected final static String OSSJ_XML_SPEC = "ossj-xml-spec";

	/**
	 * Perform any required update based on the runtime context
	 * 
	 */
	protected void initFromContext() {
		try {
			TSRuntimeContext context = getTSRuntimeContext();
			ITigerstripeProject project = context.getProjectHandle();

			if ("".equals(this.packageDialogField.getText())) {
				this.packageDialogField
						.setText(project
								.getProjectDetails()
								.getProperty(
										IProjectDetails.DEFAULTARTIFACTPACKAGE_PROP,
										""));
			}

			IPluginReference[] refs = project.getPluginReferences();

			for (int i = 0; i < refs.length; i++) {
				// if jvtPlugin
				if (OSSJ_JVT_SPEC.equals(refs[i].getPluginId())) {
					Properties refProps = refs[i].getProperties();
					this.targetPackageDialogField.setText(refProps.getProperty(
							"defaultInterfacePackage", "com.mycompany"));

				}

				// if xmlPlugin
				if (OSSJ_XML_SPEC.equals(refs[i].getPluginId())) {

				}
			}

			if (this.targetPackageDialogField.getText() == null
					|| this.targetPackageDialogField.getText().length() == 0) {
				targetPackageDialogField.setText("com.mycompany");
			}

		} catch (TigerstripeException e) {
			// The wizard is currently not pointing at a valid TS project
			// We ignore
			// TigerstripeRuntime.logErrorMessage("TigerstripeException
			// detected", e);
		}
	}

	/**
	 * Returns the content of the superclass input field.
	 * 
	 * @return the superclass name
	 */
	public String getExtendedClass() {
		return extendedClassDialogField.getText();
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
	public void setExtendedClass(String name, boolean canBeModified) {
		extendedClassDialogField.setText(name);
		extendedClassDialogField.setEnabled(canBeModified);
	}

	private String chooseExtendedType() {

		try {
			BrowseForArtifactDialog dialog = new BrowseForArtifactDialog(
					getTSRuntimeContext().getProjectHandle(),
					getArtifactModelForExtend());
			dialog.setTitle("Extended Artifact Type Selection");
			dialog.setMessage("Choose an Artifact to extend.");

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
	 * Hook method that gets called when the superclass name has changed. The
	 * method validates the superclass name and returns the status of the
	 * validation.
	 * <p>
	 * Subclasses may extend this method to perform their own validation.
	 * </p>
	 * 
	 * @return the status of the validation
	 */
	protected IStatus extendedClassChanged() {
		StatusInfo status = new StatusInfo();
		IPackageFragmentRoot root = getPackageFragmentRoot();
		extendedClassDialogField.enableButton(root != null);

		String sclassName = getExtendedClass();
		if (sclassName.length() == 0)
			// accept the empty field (stands for java.lang.Object)
			return status;
		IStatus val = JavaConventions.validateJavaTypeName(sclassName);
		if (val.getSeverity() == IStatus.ERROR) {
			status.setError("Invalid super artifact name."); //$NON-NLS-1$
			return status;
		}

		// Cannot self extend
		String fqn = getArtifactName();
		String pack = getPackageText();
		if (pack != null && pack.length() != 0) {
			fqn = pack + "." + fqn;
		}
		if (sclassName.equals(getArtifactName()) || sclassName.equals(fqn)) {
			status.setError("Artifact cannot extend itself.");//$NON-NLS-1$
			return status;
		}

		if (root != null) {
			// try {
			// IType type = resolveExtendedTypeName(root.getJavaProject(),
			// sclassName);
			// if (type == null) {
			// status
			// .setWarning(NewWizardMessages
			// .getString("NewTypeWizardPage.warning.SuperClassNotExists"));
			// //$NON-NLS-1$
			// return status;
			// }
			//
			// extendedClass = type;
			// } catch (JavaModelException e) {
			// status
			// .setError(NewWizardMessages
			// .getString("NewTypeWizardPage.error.InvalidSuperClassName"));
			// //$NON-NLS-1$
			// JavaPlugin.log(e);
			// }
		} else {
			status.setError(""); //$NON-NLS-1$
		}
		return status;

	}

	protected IStatus checkArtifactDonotExist(String typeName) {
		StatusInfo status = new StatusInfo();

		IPackageFragment pack = getArtifactPackageFragment();
		if (pack != null) {
			ICompilationUnit cu = pack.getCompilationUnit(typeName + ".java"); //$NON-NLS-1$
			IResource resource = cu.getResource();

			if (resource.exists()) {
				status
						.setError(org.eclipse.jdt.internal.ui.wizards.NewWizardMessages.NewTypeWizardPage_error_TypeNameExists);
				return status;
			}
			IPath location = resource.getLocation();
			if (location != null && location.toFile().exists()) {
				status
						.setError(org.eclipse.jdt.internal.ui.wizards.NewWizardMessages.NewTypeWizardPage_error_TypeNameExistsDifferentCase);
				return status;
			}
		}
		return status;
	}

	public int getFieldCounter() {
		return fieldCounter;
	}

	public void setFieldCounter(int fieldCounter) {
		this.fieldCounter = fieldCounter;
	}

}