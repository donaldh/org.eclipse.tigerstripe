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
package org.eclipse.tigerstripe.eclipse.wizards.artifacts.updateProcedure;

import java.io.Writer;
import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.core.model.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.eclipse.wizards.artifacts.ArtifactDefinitionGenerator;
import org.eclipse.tigerstripe.eclipse.wizards.artifacts.NewArtifactWizardPage;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class NewUpdateProcedureWizardPage extends NewArtifactWizardPage {

	private final static String PAGE_NAME = "NewUpdateProcedureWizardPage";

	public NewUpdateProcedureWizardPage() {
		super(PAGE_NAME);

		setTitle("Update Procedure Artifact");
		setDescription("Create a new Update Procedure Artifact.");

	}

	@Override
	public IAbstractArtifact[] getArtifactModelForExtend() {
		return new IAbstractArtifact[] { UpdateProcedureArtifact.MODEL };
	}

	// -------- TypeFieldsAdapter --------

	private void entityPageChangeControlPressed(DialogField field) {
	}

	/*
	 * A field on the type has changed. The fields' status and all dependent
	 * status are updated.
	 */
	private void entityPageDialogFieldChanged(DialogField field) {
	}

	@Override
	public Properties getProperties() {
		Properties result = getPropertiesCommons();
		return result;
	}

	@Override
	protected ArtifactDefinitionGenerator getGenerator(
			Properties pageProperties, Writer writer) {
		return new UpdateProcedureDefinitionGenerator(pageProperties, writer);
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);

		int nColumns = 4;

		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		createArtifactControls(composite, nColumns, true, false);

		setControl(composite);
		Dialog.applyDialogFont(composite);
		// WorkbenchHelp.setHelp(composite,
		// IJavaHelpContextIds.NEW_INTERFACE_WIZARD_PAGE);
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

	@Override
	protected void handleFieldChanged(String fieldName) {
		super.handleFieldChanged(fieldName);

		doStatusUpdate();
	}
}