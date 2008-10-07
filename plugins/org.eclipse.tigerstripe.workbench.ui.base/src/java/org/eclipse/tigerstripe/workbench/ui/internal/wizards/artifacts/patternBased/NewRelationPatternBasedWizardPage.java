/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - rcraddoc
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.patternBased;

import java.util.Arrays;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SafeRunner;
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
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.patterns.ArtifactPattern;
import org.eclipse.tigerstripe.workbench.internal.api.patterns.RelationPattern;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.AssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EnumArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EventArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.PackageArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.QueryArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.patterns.IPatternBasedWizardValidator;
import org.eclipse.tigerstripe.workbench.patterns.IRelationPattern;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.dialogs.BrowseForArtifactDialog;

public class NewRelationPatternBasedWizardPage extends
		NewNodePatternBasedWizardPage {

	private final static String PAGE_NAME = "NewRelationPatternBasedWizardPage";
	
	private final static String AEND_TYPE = PAGE_NAME + ".aEndType";

	private final static String ZEND_TYPE = PAGE_NAME + ".zEndType";
	
	
	private StringButtonDialogField aEndTypeClassDialogField;

	private StringButtonDialogField zEndTypeClassDialogField;

	private IStatus aEndTypeClassStatus;

	private IStatus zEndTypeClassStatus;

	private JavaTypeCompletionProcessor aEndTypeClassCompletionProcessor;

	private JavaTypeCompletionProcessor zEndTypeClassCompletionProcessor;

	
	public NewRelationPatternBasedWizardPage(IPattern pattern) {
		super(pattern);
		
		aEndTypeClassDialogField = new StringButtonDialogField(adapter);
		aEndTypeClassDialogField.setDialogFieldListener(adapter);
		aEndTypeClassDialogField.setLabelText("aEnd Type"); //$NON-NLS-1$
		aEndTypeClassDialogField.setButtonLabel("Browse"); //$NON-NLS-1$

		aEndTypeClassStatus = aEndTypeClassChanged();
		aEndTypeClassCompletionProcessor = new JavaTypeCompletionProcessor(
				false, false);

		zEndTypeClassDialogField = new StringButtonDialogField(adapter);
		zEndTypeClassDialogField.setDialogFieldListener(adapter);
		zEndTypeClassDialogField.setLabelText("zEnd Type"); //$NON-NLS-1$
		zEndTypeClassDialogField.setButtonLabel("Browse"); //$NON-NLS-1$

		zEndTypeClassStatus = zEndTypeClassChanged();
		zEndTypeClassCompletionProcessor = new JavaTypeCompletionProcessor(
				false, false);

	}
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);

		int nColumns = 4;

		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		createArtifactControls(composite, nColumns, true, false, false);
		createSeparator(composite, nColumns);
		createEndTypeControls(composite, nColumns);

		setControl(composite);
		Dialog.applyDialogFont(composite);
	}
	
	protected void createEndTypeControls(Composite composite, int nColumns) {
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
	protected void doStatusUpdate() {
		// status of all used components
		IStatus[] status = new IStatus[] { tsRuntimeContextStatus,
				fContainerStatus, artifactNameStatus, artifactPackageStatus,
				extendedClassStatus, aEndTypeClassStatus, zEndTypeClassStatus };

		// the mode severe status will be displayed and the ok button
		// enabled/disabled.
		updateStatus(status);
	}

	
	
	@Override
	protected void initFromContext() {
		super.initFromContext();
		String aEnd = ((RelationPattern) this.pattern).getAEndType();
		if (! "".equals(aEnd)){
			this.aEndTypeClassDialogField.setText(aEnd);
		}
		String zEnd = ((RelationPattern) this.pattern).getZEndType();
		if (! "".equals(zEnd)){
			this.zEndTypeClassDialogField.setText(zEnd);
		}
		
	}
	protected void artifactPageChangeControlPressed(DialogField field) {
		super.artifactPageChangeControlPressed(field);
		if (field == aEndTypeClassDialogField) {
			IType type = chooseReturnedType();
			if (type != null) {
				aEndTypeClassDialogField.setText(type.getFullyQualifiedName());
			}
		} else if (field == zEndTypeClassDialogField) {
			IType type = chooseReturnedType();
			if (type != null) {
				zEndTypeClassDialogField.setText(type.getFullyQualifiedName());
			}
		}
	}
	
	/*
	 * A field on the type has changed. The fields' status and all dependent
	 * status are updated.
	 */
	protected void artifactPageDialogFieldChanged(DialogField field) {
		super.artifactPageDialogFieldChanged(field);
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
	
	
	/*
	 * @see org.eclipse.jdt.ui.wizards.NewContainerWizardPage#handleFieldChanged(String)
	 */
	@Override
	protected void handleFieldChanged(String fieldName) {
		super.handleFieldChanged(fieldName);
		if (fieldName == AEND_TYPE) {
			aEndTypeClassStatus = aEndTypeClassChanged();
		} else if (fieldName == ZEND_TYPE) {
			zEndTypeClassStatus = zEndTypeClassChanged();
		}
		doStatusUpdate();
	}
	
	public void setAendTypeClass(String name, boolean canBeModified) {
		aEndTypeClassDialogField.setText(name);
		aEndTypeClassDialogField.setEnabled(canBeModified);
	}

	public void setZendTypeClass(String name, boolean canBeModified) {
		zEndTypeClassDialogField.setText(name);
		zEndTypeClassDialogField.setEnabled(canBeModified);
	}
	
	protected IStatus aEndTypeClassChanged() {
		StatusInfo status = new StatusInfo();
		IPackageFragmentRoot root = getPackageFragmentRoot();
		// TODO - Why was this here?
//		aEndTypeClassDialogField.enableButton(root != null);

		String sclassName = getAendTypeClass();
		if (sclassName.length() == 0) {
			status.setError("Please select valid aEnd Class");
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
	public String getAendTypeClass() {
		return aEndTypeClassDialogField.getText();
	}


	public String getZendTypeClass() {
		return zEndTypeClassDialogField.getText();
	}
	
	protected IStatus zEndTypeClassChanged() {
		StatusInfo status = new StatusInfo();
		IPackageFragmentRoot root = getPackageFragmentRoot();
		// TODO - Why was this here?
//		zEndTypeClassDialogField.enableButton(root != null);

		String sclassName = getZendTypeClass();
		if (sclassName.length() == 0) {
			status.setError("Please select valid zEnd Class");
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
	
	private  IAbstractArtifact[]  suitableAssociationEndTypes = new IAbstractArtifact[] { 
			ManagedEntityArtifact.MODEL,
			DatatypeArtifact.MODEL, QueryArtifact.MODEL,
			UpdateProcedureArtifact.MODEL,
			SessionFacadeArtifact.MODEL,
			ExceptionArtifact.MODEL,
			EventArtifact.MODEL,
			EnumArtifact.MODEL, 
			AssociationClassArtifact.MODEL };
	
	private  IAbstractArtifact[]  suitableDependencyEndTypes = new IAbstractArtifact[] { 
			PackageArtifact.MODEL,
			ManagedEntityArtifact.MODEL,
			DatatypeArtifact.MODEL, QueryArtifact.MODEL,
			UpdateProcedureArtifact.MODEL,
			SessionFacadeArtifact.MODEL,
			ExceptionArtifact.MODEL,
			EventArtifact.MODEL,
			EnumArtifact.MODEL, 
			AssociationClassArtifact.MODEL };
	
	/**
	 * Choose the returned type through a dialog. 
	 * @return
	 */
	private IType chooseReturnedType() {
		try {
			IAbstractArtifact[] suitableTypes;
			if (((ArtifactPattern) pattern).getTargetArtifactType().equals(IAssociationArtifact.class.getName()) ||
					((ArtifactPattern) pattern).getTargetArtifactType().equals(IAssociationClassArtifact.class.getName()) ){
				suitableTypes = suitableAssociationEndTypes;
			} else {
				suitableTypes = suitableDependencyEndTypes;
			}
			
			BrowseForArtifactDialog dialog = new BrowseForArtifactDialog(
					getTSRuntimeContext().getProjectHandle(),
					suitableTypes);
			dialog.setTitle(pattern.getUILabel()
					+ " End Type");
			dialog.setMessage("Select the type of the "
					+ this.pattern.getUILabel() + " End.");

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
	
private IStatus[] statuses = new IStatus[0];
	
	@Override
	protected IStatus[] getAdditionalStatuses() {
		final IPatternBasedWizardValidator validator = pattern.getWizardValidator();
		final String name = this.getArtifactName();
		final String packageName = this.getPackageName();
		final String extendedFQN = this.getExtendedArtifact();
		final String aEndType = this.getAendTypeClass();
		final String zEndType = this.getZendTypeClass();
			
		if (validator != null){
			statuses = new IStatus[0];
			SafeRunner.run(new ISafeRunnable() {
				public void handleException(Throwable exception) {
					BasePlugin.log(exception);
				}

				public void run() throws Exception {
					statuses = validator.validate((IRelationPattern) pattern, name, packageName , extendedFQN,
							aEndType, zEndType);
				}

			});
			return statuses;
			
		}
		return null;
	}

}

