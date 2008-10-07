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

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.patterns.INodePattern;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.patterns.IPatternBasedWizardValidator;

/**

 */
public class NewNodePatternBasedWizardPage extends NewPatternBasedArtifactWizardPage {


	
	private final static String PAGE_NAME = "NewNodePatternBasedWizardPage";
	protected EntityFieldsAdapter adapter;


	public NewNodePatternBasedWizardPage(IPattern pattern) {
		super(PAGE_NAME);
		this.pattern = pattern;
		setTitle(pattern.getUILabel());
		setDescription(pattern.getDescription());

		adapter = new EntityFieldsAdapter();

	}

	// ------ validation --------
	@Override
	protected void doStatusUpdate() {
		// status of all used components
		IStatus[] status = new IStatus[] { tsRuntimeContextStatus,
				fContainerStatus, artifactNameStatus, artifactPackageStatus,
				extendedClassStatus };

		// the mode severe status will be displayed and the ok button
		// enabled/disabled.
		updateStatus(status);
	}

	


	
	
	// -------- TypeFieldsAdapter --------

	private class EntityFieldsAdapter implements IStringButtonAdapter,
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

	protected void artifactPageChangeControlPressed(DialogField field) {
		
	}

	/*
	 * A field on the type has changed. The fields' status and all dependent
	 * status are updated.
	 */
	protected void artifactPageDialogFieldChanged(DialogField field) {
		super.artifactPageDialogFieldChanged(field);
		String fieldName = null;
		
		handleFieldChanged(fieldName);
	}

	@Override
	protected void handleFieldChanged(String fieldName) {
		super.handleFieldChanged(fieldName);
		doStatusUpdate();
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

	}


	@Override
	public void initPage(IJavaElement jElement) {
		initFromContext();

		setFocus();
		doStatusUpdate();
	}

	@Override
	protected void initFromContext() {
		super.initFromContext();

	}

	private IStatus[] statuses = new IStatus[0];
	
	@Override
	protected IStatus[] getAdditionalStatuses() {
		final IPatternBasedWizardValidator validator = pattern.getWizardValidator();
		final String name = this.getArtifactName();
		final String packageName = this.getPackageName();
		final String extendedFQN = this.getExtendedArtifact();
			
		if (validator != null){
			statuses = new IStatus[0];
			SafeRunner.run(new ISafeRunnable() {
				public void handleException(Throwable exception) {
					BasePlugin.log(exception);
				}

				public void run() throws Exception {
					statuses = validator.validate((INodePattern) pattern, name, packageName , extendedFQN);
				}

			});
			return statuses;
			
		}
		return null;
	}


}