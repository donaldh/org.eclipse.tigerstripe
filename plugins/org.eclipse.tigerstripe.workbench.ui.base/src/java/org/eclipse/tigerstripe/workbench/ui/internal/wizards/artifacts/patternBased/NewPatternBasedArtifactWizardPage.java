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
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.patternBased;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.dialogs.StatusUtil;
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
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonStatusDialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jdt.ui.wizards.NewContainerWizardPage;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.patterns.ArtifactPattern;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DependencyArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EnumArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EventArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.QueryArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.patterns.IArtifactPattern;
import org.eclipse.tigerstripe.workbench.patterns.IEnumPattern;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.patterns.IQueryPattern;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.BrowseForArtifactDialog;
import org.eclipse.tigerstripe.workbench.ui.internal.runtime.messages.NewWizardMessages;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.ArtifactNameValidator;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.TSRuntimeContext;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.WizardUtils;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.ArtifactSelectionDialog;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

/**
 * @author Eric Dillon
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class NewPatternBasedArtifactWizardPage extends
		NewContainerWizardPage {

	protected IPattern pattern;

	private final static String PAGE_NAME = "NewPatternBasedArtifactWizardPage"; //$NON-NLS-1$

	/** Field ID of the package input field. */
	protected final static String PACKAGE = PAGE_NAME + ".package"; //$NON-NLS-1$

	/** Field ID of the artifact name input field. */
	protected final static String ARTIFACTNAME = PAGE_NAME + ".artifactname"; //$NON-NLS-1$

	/** Field ID of the super type input field. */
	protected final static String SUPER = PAGE_NAME + ".superclass"; //$NON-NLS-1$

	/** Field ID of the super interfaces input field. */
	protected final static String INTERFACES = PAGE_NAME + ".interfaces"; //$NON-NLS-1$

	// The extended class
	private StringButtonDialogField extendedClassDialogField;

	protected IStatus extendedClassStatus;

	private JavaTypeCompletionProcessor extendedClassCompletionProcessor;

	/**
	 * The package of the generated artifact
	 */
	private IPackageFragment artifactPackage;

	protected IStatus artifactPackageStatus;

	protected IStatus artifactNameStatus;

	// The name of the artifact that will be generated
	private StringDialogField artifactNameDialogField;

	// The package of the Artifact
	private StringButtonStatusDialogField packageDialogField;

	// Help on the the package completions
	private JavaPackageCompletionProcessor packageCompletionProcessor;

	// Help on the Artifact name completion
	private JavaTypeCompletionProcessor artifactTypeCompletionProcessor;

	private IJavaElement initialElement;

	private TSRuntimeContext tsRuntimeContext;

	protected IStatus tsRuntimeContextStatus;

	// Only used for Enum
	private ComboDialogField baseEnumType;
	private final static String[] supportedEnumTypes = { "int", "String" };

	// Only used for Query
	private StringButtonDialogField returnedTypeClassDialogField;
	private IStatus returnedTypeClassStatus;
	private JavaTypeCompletionProcessor returnedTypeClassCompletionProcessor;

	public NewPatternBasedArtifactWizardPage(String pageName) {
		super(pageName);

		ArtifactFieldsAdapter adapter = new ArtifactFieldsAdapter();

		artifactNameDialogField = new StringDialogField();
		artifactNameDialogField.setDialogFieldListener(adapter);
		artifactNameDialogField.setLabelText("Name:"); //$NON-NLS-1$

	}

	private class ArtifactFieldsAdapter implements IStringButtonAdapter,
			IDialogFieldListener, IListAdapter {

		// -------- IStringButtonAdapter
		public void changeControlPressed(DialogField field) {
			artifactPageChangeControlPressed(field);
		}

		// -------- IListAdapter
		public void customButtonPressed(ListDialogField field, int index) {
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

		baseEnumType = new ComboDialogField(SWT.DROP_DOWN | SWT.READ_ONLY);
		baseEnumType.setDialogFieldListener(adapter);
		baseEnumType.setLabelText("Base Type:");
		baseEnumType.setItems(supportedEnumTypes);
		baseEnumType.selectItem(0);

		returnedTypeClassDialogField = new StringButtonDialogField(adapter);
		returnedTypeClassDialogField.setDialogFieldListener(adapter);
		returnedTypeClassDialogField.setLabelText("Returned Type"); //$NON-NLS-1$
		returnedTypeClassDialogField.setButtonLabel("Browse"); //$NON-NLS-1$

		returnedTypeClassStatus = new StatusInfo();
		returnedTypeClassCompletionProcessor = new JavaTypeCompletionProcessor(
				false, false);

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

		setArtifactPackageFragment(pack);

		setArtifactName(typeName, true);
	}

	/**
	 * Creates a separator line. Expects a <code>GridLayout</code> with at least
	 * 1 column.
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
		String targetType = ((ArtifactPattern) pattern).getTargetArtifactType();

		// TODO - Should these specifics actually be in the "nodePattern"
		boolean createExtends = true;
		boolean createBaseType = false;
		boolean createReturnType = false;
		// Don't support extends for Package
		if (targetType.equals(IPackageArtifact.class.getName())) {
			createExtends = false;
		}
		// Enums need base type, but should not be extendable!
		if (targetType.equals(IEnumArtifact.class.getName())) {
			createBaseType = true;
			createExtends = false;
		}
		// Queries need return type
		if (targetType.equals(IQueryArtifact.class.getName())) {
			returnedTypeClassDialogField.setText("");
			createReturnType = true;
		}

		createArtifactControls(composite, nColumns, createExtends,
				createBaseType, createReturnType);
		// createArtifactControls(composite, nColumns, createExtends, false,
		// false);
	}

	protected void createArtifactControls(Composite composite, int nColumns,
			boolean createExtendsControl, boolean createBaseTypeControl,
			boolean createReturnType) {
		createContainerControls(composite, nColumns);

		createPackageControls(composite, nColumns);
		createArtifactNameControls(composite, nColumns);

		if (createExtendsControl) {
			createSeparator(composite, nColumns);
			createExtendedArtifactControls(composite, nColumns);
		}

		if (createBaseTypeControl) {
			createSeparator(composite, nColumns);
			createBaseEnumTypeControls(composite, nColumns);
		}

		if (createReturnType) {
			createSeparator(composite, nColumns);
			createReturnedTypeControls(composite, nColumns);
		}

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

	/**
	 * Creates the controls for the enum base type. Expects a
	 * <code>GridLayout</code> with at least 3 columns.
	 * 
	 * @param composite
	 *            the parent composite
	 * @param nColumns
	 *            number of columns to span
	 */
	protected void createBaseEnumTypeControls(Composite composite, int nColumns) {
		baseEnumType.doFillIntoGrid(composite, nColumns);
		Combo combo = baseEnumType.getComboControl(null);
		LayoutUtil.setWidthHint(combo, getMaxFieldWidth());
		LayoutUtil.setHorizontalGrabbing(combo);
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

	/**
	 * Returns the package fragment corresponding to the current input.
	 * 
	 * @return a package fragment or <code>null</code> if the input could not be
	 *         resolved.
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
	public String getPackageName() {
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
	 * @see
	 * org.eclipse.jdt.ui.wizards.NewContainerWizardPage#handleFieldChanged(
	 * String)
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
		} else if (fieldName == EXTENDED_ARTIFACT) {
			extendedClassStatus = extendedClassChanged();

		}
		doStatusUpdate();
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

	public ITigerstripeModelProject getProject() throws TigerstripeException {
		if (getTSRuntimeContext() != null
				&& getTSRuntimeContext().isValidProject()) {
			return getTSRuntimeContext().getProjectHandle();

		}
		return null;

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
		} else if (field == extendedClassDialogField) {
			String type = chooseExtendedType();
			if (type != null) {
				// extendedClassDialogField.setText(JavaModelUtil
				// .getFullyQualifiedName(type));
				extendedClassDialogField.setText(type);
			}
		} else if (field == returnedTypeClassDialogField) {
			org.eclipse.jdt.core.IType type = chooseReturnedType();
			if (type != null) {
				returnedTypeClassDialogField.setText(type
						.getFullyQualifiedName());
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
        if (((IArtifactPattern) pattern).getTargetArtifactType().equals(IPackageArtifact.class.getName())) {
	       IStatus status = ArtifactNameValidator.validatePackageArtifactName(getArtifactName());
	       if (!status.isOK()) {
               return status;
           }
	    }
	    else
	    {
            IStatus status = ArtifactNameValidator.validateArtifactName(getArtifactName());
        	if (!status.isOK()) {
        		return status;
        	}
	    }

		return ArtifactNameValidator.validateArtifactDoesNotExist(getArtifactPackageFragment(), getArtifactName());

	}

	/*
	 * Updates the 'default' label next to the package field.
	 */
	private void updatePackageStatusLabel() {
		String packName = getPackageName();

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
	protected void artifactPageDialogFieldChanged(DialogField field) {
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
			fieldName = EXTENDED_ARTIFACT;
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

		String packName = getPackageName();
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

	/**
	 * Choose the returned type through a dialog. The dialog only shows Entity
	 * Artifact
	 * 
	 * @return
	 */
	private org.eclipse.jdt.core.IType chooseReturnedType() {

		ArtifactSelectionDialog dialog = new ArtifactSelectionDialog(
				getInitialElement(), ManagedEntityArtifact.MODEL);

		dialog.setTitle("Returned Entity Type");
		dialog
				.setMessage("Please selected the returned entity type for this query.");

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

	}

	private org.eclipse.jdt.core.IType resolveReturnedTypeName(
			IJavaProject jproject, String sclassName) throws JavaModelException {
		if (!jproject.exists())
			return null;
		org.eclipse.jdt.core.IType type = null;
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

	public final static String PACKAGE_NAME = "packageName";

	public final static String ARTIFACT_NAME = "artifactName";

	public final static String INTERFACE_PACKAGE = "interfacePackage";

	public final static String EXTENDED_ARTIFACT = "extendedArtifact";

	// TODO
	public IAbstractArtifact[] getArtifactModelForExtend() {
		String targetType = ((IArtifactPattern) pattern)
				.getTargetArtifactType();
		if (targetType.equals(IManagedEntityArtifact.class.getName())) {
			return new IAbstractArtifact[] { ManagedEntityArtifact.MODEL };
		} else if (targetType.equals(IDatatypeArtifact.class.getName())) {
			return new IAbstractArtifact[] { DatatypeArtifact.MODEL };
		} else if (targetType.equals(IEnumArtifact.class.getName())) {
			return new IAbstractArtifact[] { EnumArtifact.MODEL };
		} else if (targetType.equals(IExceptionArtifact.class.getName())) {
			return new IAbstractArtifact[] { ExceptionArtifact.MODEL };
		} else if (targetType.equals(IEventArtifact.class.getName())) {
			return new IAbstractArtifact[] { EventArtifact.MODEL };
		} else if (targetType.equals(IQueryArtifact.class.getName())) {
			return new IAbstractArtifact[] { QueryArtifact.MODEL };
		} else if (targetType.equals(IUpdateProcedureArtifact.class.getName())) {
			return new IAbstractArtifact[] { UpdateProcedureArtifact.MODEL };
		} else if (targetType.equals(ISessionArtifact.class.getName())) {
			return new IAbstractArtifact[] { SessionFacadeArtifact.MODEL };
		} else if (targetType.equals(IAssociationArtifact.class.getName())) {
			return new IAbstractArtifact[] { AssociationArtifact.MODEL };
		} else if (targetType.equals(IAssociationClassArtifact.class.getName())) {
			return new IAbstractArtifact[] { AssociationClassArtifact.MODEL };
		} else if (targetType.equals(IDependencyArtifact.class.getName())) {
			return new IAbstractArtifact[] { DependencyArtifact.MODEL };
		}

		// Default
		return new IAbstractArtifact[] { ManagedEntityArtifact.MODEL };
	}

	protected IJavaElement getInitialElement() {
		return initialElement;
	}

	protected void setInitialElement(IJavaElement initialElement) {
		this.initialElement = initialElement;
	}

	/**
	 * Perform any required update based on the runtime context
	 * 
	 */
	protected void initFromContext() {
		String extended = ((ArtifactPattern) this.pattern)
				.getExtendedArtifactName();
		if (!"".equals(extended)) {
			this.extendedClassDialogField.setText(extended);
		}

		if (this.pattern instanceof IEnumPattern) {
			String baseType = ((IEnumPattern) this.pattern).getBaseType();
			this.baseEnumType.selectItem(baseType);
		}

		if (this.pattern instanceof IQueryPattern) {
			String returnedType = ((IQueryPattern) this.pattern)
					.getReturnType();
			this.returnedTypeClassDialogField.setText(returnedType);
		}

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

		} catch (TigerstripeException e) {
			// The wizard is currently not pointing at a valid TS project
			// We ignore
			// TigerstripeRuntime.logErrorMessage("TigerstripeException
			// detected", e);
		}
	}

	public String getBaseType() {
		return baseEnumType.getText();
	}

	public String getReturnType() {
		return returnedTypeClassDialogField.getText();
	}

	/**
	 * Returns the content of the superclass input field.
	 * 
	 * @return the superclass name
	 */
	public String getExtendedArtifact() {
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

		String sclassName = getExtendedArtifact();
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
		String pack = getPackageName();
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

	protected void updateStatus(IStatus[] status) {

		IStatus[] additional = getAdditionalStatuses();
		if (additional != null && additional.length > 0) {
			List<IStatus> asList = new ArrayList<IStatus>();
			asList.addAll(Arrays.asList(status));
			asList.addAll(Arrays.asList(additional));
			status = asList.toArray(new IStatus[status.length
					+ additional.length]);
		}
		super.updateStatus(status);
	}

	protected abstract IStatus[] getAdditionalStatuses();

}