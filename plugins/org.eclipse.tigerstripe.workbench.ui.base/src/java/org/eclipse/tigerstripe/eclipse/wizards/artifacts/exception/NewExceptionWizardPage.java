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
package org.eclipse.tigerstripe.eclipse.wizards.artifacts.exception;

import java.io.Writer;
import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.eclipse.wizards.artifacts.ArtifactDefinitionGenerator;
import org.eclipse.tigerstripe.eclipse.wizards.artifacts.INewArtifactWizardPage;
import org.eclipse.tigerstripe.eclipse.wizards.artifacts.NewArtifactWizardPage;
import org.eclipse.tigerstripe.workbench.internal.core.model.ExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class NewExceptionWizardPage extends NewArtifactWizardPage implements
		INewArtifactWizardPage {

	private final static String PAGE_NAME = "NewExceptionWizardPage";

	public NewExceptionWizardPage() {
		super(PAGE_NAME);

		setTitle("Exception Artifact");
		setDescription("Create a new Exception Artifact.");

	}

	@Override
	public Properties getProperties() {
		Properties result = getPropertiesCommons();
		return result;
	}

	@Override
	protected ArtifactDefinitionGenerator getGenerator(
			Properties pageProperties, Writer writer) {
		return new ExceptionDefinitionGenerator(pageProperties, writer);
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
	public void initPage(IJavaElement jElement) {
		initFromContext();

		setFocus();
		doStatusUpdate();
	}

	@Override
	public IAbstractArtifact[] getArtifactModelForExtend() {
		return new IAbstractArtifact[] { ExceptionArtifact.MODEL };
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
		// if (fieldName == CONTAINER) {
		// artifactPackageStatus = packageChanged();
		// artifactNameStatus = artifactNameChanged();
		// tsRuntimeContextStatus = tsRuntimeContextChanged();
		// }

		doStatusUpdate();
	}

}