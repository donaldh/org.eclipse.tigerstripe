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
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.packageArtifact;

import java.io.File;
import java.io.Writer;
import java.util.Properties;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.tigerstripe.metamodel.IPackage;
import org.eclipse.tigerstripe.metamodel.impl.IManagedEntityArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IPackageImpl;
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
public class NewPackageWizard extends NewArtifactWizard {

	public NewPackageWizard() {
		super();
		setDefaultPageImageDescriptor(Images.getDescriptor(Images.TS_LOGO));

		setDialogSettings(JavaPlugin.getDefault().getDialogSettings());
		setWindowTitle("New "
				+ ArtifactMetadataFactory.INSTANCE.getMetadata(
						IPackageImpl.class.getName()).getLabel(null)
				+ " Artifact");
	}

	/*
	 * @see Wizard#addPages
	 */
	@Override
	public void addPages() {
		super.addPages();
		NewArtifactWizardPage page = new NewPackageWizardPage();
		addPage(page);
		page.init(getSelection());
	}

	@Override
	protected ArtifactDefinitionGenerator getGenerator(
			Properties pageProperties, Writer writer) {
		return new PackageDefinitionGenerator(pageProperties, writer);
	}

	// Override - we will stick this in the actual package, not the parent
	
	protected String getPackageForArtifact(String packageName, String artifactName){
		return packageName+"."+artifactName;
	}

	/**
	 * The new package Artifact name is in effect a new Package
	 */
	@Override
	protected String getFileName(String projectName, String srcDirectory,String packageName, String artifactName) {
		return projectName + File.separator + srcDirectory
			+ File.separator
			+packageName.replace('.', File.separatorChar)
			+ File.separator + artifactName+ File.separator + ".package";
	}

	
}