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

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Properties;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.tigerstripe.metamodel.impl.ISessionArtifactImpl;
import org.eclipse.tigerstripe.metamodel.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.ArtifactDefinitionGenerator;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.NewArtifactWizard;

/**
 * @author Eric Dillon
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class NewSessionWizard extends NewArtifactWizard {

	private NewSessionWizardPage mainPage;
	private EntitySelectorWizardPage entitySelectorWizardPage;
	private QuerySelectorWizardPage querySelectorWizardPage;
	private UpdateProcedureSelectorWizardPage updateProcedureSelectorWizardPage;
	private EventSelectorWizardPage eventSelectorWizardPage;

	public NewSessionWizard() {
		super();
		setDefaultPageImageDescriptor(Images.getDescriptor(Images.TS_LOGO));

		setDialogSettings(JavaPlugin.getDefault().getDialogSettings());
		setWindowTitle("New "
				+ ArtifactMetadataFactory.INSTANCE.getMetadata(
						ISessionArtifactImpl.class.getName()).getLabel()
				+ " Artifact");
	}

	/*
	 * @see Wizard#addPages
	 */
	@Override
	public void addPages() {
		super.addPages();

		this.mainPage = new NewSessionWizardPage();
		this.entitySelectorWizardPage = new EntitySelectorWizardPage();
		this.querySelectorWizardPage = new QuerySelectorWizardPage();
		this.updateProcedureSelectorWizardPage = new UpdateProcedureSelectorWizardPage();
		this.eventSelectorWizardPage = new EventSelectorWizardPage();

		addPage(this.mainPage);
		addPage(this.entitySelectorWizardPage);
		addPage(this.querySelectorWizardPage);
		addPage(this.updateProcedureSelectorWizardPage);
		addPage(this.eventSelectorWizardPage);

		mainPage.init(getSelection());
	}

	@Override
	protected ArtifactDefinitionGenerator getGenerator(
			Properties pageProperties, Writer writer) {
		return new SessionDefinitionGenerator(pageProperties, writer);
	}

	/**
	 * We will initialize file contents with a sample text.
	 * 
	 * @param pageProperties -
	 *            the properties gathered through the wizard
	 */
	@Override
	protected InputStream openContentStream(Properties pageProperties) {

		byte[] bytes = null;
		buffer = new ByteArrayOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				buffer));

		SessionDefinitionGenerator generator = new SessionDefinitionGenerator(
				this.entitySelectorWizardPage.getEntities(),
				this.querySelectorWizardPage.getQueries(),
				this.updateProcedureSelectorWizardPage.getUpdateProcedures(),
				this.eventSelectorWizardPage.getEvents(), pageProperties,
				writer);

		generator.applyTemplate();
		bytes = buffer.toByteArray();
		return new ByteArrayInputStream(bytes);
	}
}