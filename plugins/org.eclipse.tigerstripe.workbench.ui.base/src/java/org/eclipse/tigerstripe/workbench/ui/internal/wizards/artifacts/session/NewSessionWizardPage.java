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
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.session;

import java.io.Writer;
import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.metamodel.impl.ISessionArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.internal.core.model.SessionFacadeArtifact;
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
public class NewSessionWizardPage extends NewArtifactWizardPage {

	public final static String PAGE_NAME = "NewSessionWizardPage";

	public NewSessionWizardPage() {
		super(PAGE_NAME);

		setTitle(ArtifactMetadataFactory.INSTANCE.getMetadata(
				ISessionArtifactImpl.class.getName()).getLabel()
				+ " Artifact");
		setDescription("Create a new "
				+ ArtifactMetadataFactory.INSTANCE.getMetadata(
						ISessionArtifactImpl.class.getName()).getLabel()
				+ " Artifact.");
	}

	@Override
	public IAbstractArtifact[] getArtifactModelForExtend() {
		return new IAbstractArtifact[] { SessionFacadeArtifact.MODEL };
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

		createArtifactControls(composite, nColumns, false, true);

		setControl(composite);
		Dialog.applyDialogFont(composite);
		// WorkbenchHelp.setHelp(composite,
		// IJavaHelpContextIds.NEW_INTERFACE_WIZARD_PAGE);
		doStatusUpdate();
	}

	// protected void updatePageComplete() {
	// // we check that the container is pointing to a valid TS Project
	// // or else we bloc at this page
	// boolean result = false;
	// IPackageFragmentRoot fragmentRoot = getPackageFragmentRoot();
	// if ( fragmentRoot != null ) {
	// IJavaProject project = fragmentRoot.getJavaProject();
	// if ( project != null ) {
	// // TODO this is relying on tigerstripe.xml... which may change
	// // this should rely on the nature of the project or something...
	// IResource desc = project.getProject().findMember("tigerstripe.xml");
	// if ( desc != null && desc.exists() ) {
	// result = true;
	// setErrorMessage(null);
	// setPageComplete(true);
	// setMessage(null);
	// return;
	// }
	// }
	// }
	//		
	// setErrorMessage("Please select a valid Tigerstripe Project.");
	// setPageComplete( false );
	// }

	private IJavaElement jElement;

	public IJavaElement getJElement() {
		return jElement;
	}

	public void setJElement(IJavaElement element) {
		jElement = element;
	}

	@Override
	protected void initFromContext() {
		super.initFromContext();

	}

	@Override
	public void initPage(IJavaElement jElement) {
		setJElement(jElement);
		initFromContext();

		doStatusUpdate();
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			doStatusUpdate();
		}
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