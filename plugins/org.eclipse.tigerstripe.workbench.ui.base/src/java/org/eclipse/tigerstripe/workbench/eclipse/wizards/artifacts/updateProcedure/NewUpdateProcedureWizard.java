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
package org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.updateProcedure;

import java.io.Writer;
import java.util.Properties;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.tigerstripe.metamodel.impl.IUpdateProcedureArtifactImpl;
import org.eclipse.tigerstripe.metamodel.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.ArtifactDefinitionGenerator;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.NewArtifactWizard;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.NewArtifactWizardPage;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class NewUpdateProcedureWizard extends NewArtifactWizard {

	public NewUpdateProcedureWizard() {
		super();
		setDefaultPageImageDescriptor(Images.getDescriptor(Images.TS_LOGO));

		setDialogSettings(JavaPlugin.getDefault().getDialogSettings());
		setWindowTitle("New " +ArtifactMetadataFactory.INSTANCE.getMetadata(
				IUpdateProcedureArtifactImpl.class.getName()).getLabel() + " Artifact");
	}

	/*
	 * @see Wizard#addPages
	 */
	@Override
	public void addPages() {
		super.addPages();
		NewArtifactWizardPage page = new NewUpdateProcedureWizardPage();
		addPage(page);
		page.init(getSelection());
	}

	@Override
	protected ArtifactDefinitionGenerator getGenerator(
			Properties pageProperties, Writer writer) {
		return new UpdateProcedureDefinitionGenerator(pageProperties, writer);
	}

}