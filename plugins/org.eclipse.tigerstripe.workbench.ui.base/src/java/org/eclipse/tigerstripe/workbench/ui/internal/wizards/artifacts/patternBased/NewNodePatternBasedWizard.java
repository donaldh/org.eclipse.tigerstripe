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

import java.io.Writer;
import java.util.Properties;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.tigerstripe.metamodel.impl.IManagedEntityArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
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
public class NewNodePatternBasedWizard extends NewPatternBasedArtifactWizard {


	public NewNodePatternBasedWizard(IPattern pattern) {
		super(pattern);
	}

	/*
	 * @see Wizard#addPages
	 */
	@Override
	public void addPages() {
		super.addPages();
		NewNodePatternBasedWizardPage page = new NewNodePatternBasedWizardPage(this.pattern);
		addPage(page);
		page.init(getSelection());
	}

}