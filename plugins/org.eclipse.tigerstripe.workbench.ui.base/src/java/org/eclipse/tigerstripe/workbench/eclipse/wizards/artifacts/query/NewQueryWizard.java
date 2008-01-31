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
package org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.query;

import java.io.Writer;
import java.util.Properties;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.tigerstripe.workbench.eclipse.runtime.images.TigerstripePluginImages;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.ArtifactDefinitionGenerator;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.NewArtifactWizard;
import org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.NewArtifactWizardPage;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class NewQueryWizard extends NewArtifactWizard {

	public NewQueryWizard() {
		super();
		setDefaultPageImageDescriptor(TigerstripePluginImages.DESC_TS_LOGO);

		setDialogSettings(JavaPlugin.getDefault().getDialogSettings());
		setWindowTitle("New Named Query Artifact");
	}

	/*
	 * @see Wizard#addPages
	 */
	@Override
	public void addPages() {
		super.addPages();
		NewArtifactWizardPage page = new NewQueryWizardPage();
		addPage(page);
		page.init(getSelection());
	}

	@Override
	protected ArtifactDefinitionGenerator getGenerator(
			Properties pageProperties, Writer writer) {
		// TODO Auto-generated method stub
		return new QueryDefinitionGenerator(pageProperties, writer);
	}

}