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
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.dependency;

import java.io.Writer;
import java.util.Properties;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.tigerstripe.metamodel.impl.IDependencyArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.ArtifactDefinitionGenerator;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.NewArtifactWizard;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.NewArtifactWizardPage;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class NewDependencyWizard extends NewArtifactWizard {

	public NewDependencyWizard() {
		super();
		setDefaultPageImageDescriptor(Images.getDescriptor(Images.TS_LOGO));

		setDialogSettings(JavaPlugin.getDefault().getDialogSettings());
		setWindowTitle("New "
				+ ArtifactMetadataFactory.INSTANCE.getMetadata(
						IDependencyArtifactImpl.class.getName()).getLabel()
				+ " Artifact");
	}

	/*
	 * @see Wizard#addPages
	 */
	@Override
	public void addPages() {
		super.addPages();
		NewArtifactWizardPage page = new NewDependencyWizardPage();
		addPage(page);
		page.init(getSelection());
	}

	@Override
	protected ArtifactDefinitionGenerator getGenerator(
			Properties pageProperties, Writer writer) {
		// TODO Auto-generated method stub
		return new DependencyDefinitionGenerator(pageProperties, writer);
	}

}