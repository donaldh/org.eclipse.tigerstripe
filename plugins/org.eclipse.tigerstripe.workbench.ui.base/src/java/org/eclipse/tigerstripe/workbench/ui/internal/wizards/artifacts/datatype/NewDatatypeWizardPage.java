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
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.datatype;

import java.io.Writer;
import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.internal.core.model.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.ArtifactDefinitionGenerator;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.NewArtifactWizardPage;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.entity.EntityDefinitionGenerator;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class NewDatatypeWizardPage extends NewArtifactWizardPage {

	private final static String PAGE_NAME = "NewDatatypeWizardPage";

	@Override
	public IAbstractArtifact[] getArtifactModelForExtend() {
		return new IAbstractArtifact[] { DatatypeArtifact.MODEL };
	}

	public NewDatatypeWizardPage() {
		super(PAGE_NAME);

		setTitle(ArtifactMetadataFactory.INSTANCE
				.getMetadata(
						org.eclipse.tigerstripe.metamodel.impl.IDatatypeArtifactImpl.class
								.getName()).getLabel(null)
				+ " Artifact");
		setDescription("Create a new "
				+ ArtifactMetadataFactory.INSTANCE
						.getMetadata(
								org.eclipse.tigerstripe.metamodel.impl.IDatatypeArtifactImpl.class
										.getName()).getLabel(null) + " Artifact.");

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
		return new EntityDefinitionGenerator(pageProperties, writer);
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

	@Override
	protected void initFromContext() {
		super.initFromContext();

	}

	@Override
	public void initPage(IJavaElement jElement) {
		initFromContext();

		setFocus();
		doStatusUpdate();
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