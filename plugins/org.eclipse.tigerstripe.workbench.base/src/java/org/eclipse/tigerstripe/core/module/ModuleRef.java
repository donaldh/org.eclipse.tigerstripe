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
package org.eclipse.tigerstripe.core.module;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.modules.IModuleRef;
import org.eclipse.tigerstripe.api.utils.ITigerstripeProgressMonitor;
import org.eclipse.tigerstripe.core.model.ArtifactManager;
import org.eclipse.tigerstripe.core.project.ProjectDetails;
import org.eclipse.tigerstripe.core.project.TigerstripeProject;

/**
 * A Module reference as would appear from within a TS Project. This contains
 * the main reference to the actual .jar file
 * 
 * @author Eric Dillon
 * @since 0.4
 */
public class ModuleRef implements IModuleRef {

	// The URI to the jar file
	protected URI jarURI;

	protected ModuleDescriptorModel model;

	protected boolean isValid = false;

	/* package */ModuleRef(URI jarURI, ITigerstripeProgressMonitor monitor)
			throws InvalidModuleException {
		setJarURI(jarURI);
		parse(monitor);
	}

	// =================================================
	public boolean isValid() {
		return isValid;
	}

	public URI getJarURI() {
		return this.jarURI;
	}

	private void setJarURI(URI jarURI) {
		this.jarURI = jarURI;
	}

	public ModuleDescriptorModel getModel() {
		return this.model;
	}

	public ArtifactManager getArtifactManager() {
		return model.getArtifactManager();
	}

	public ProjectDetails getProjectDetails() {
		return model.getProjectDetails();
	}

	public ModuleHeader getModuleHeader() {
		return model.getModuleHeader();
	}

	public TigerstripeProject getEmbeddedProject() {
		return ((ModuleArtifactManager) getArtifactManager())
				.getEmbeddedProject();
	}

	private void parse(ITigerstripeProgressMonitor monitor)
			throws InvalidModuleException {
		try {
			TigerstripeProject embeddedProjet = parseEmbeddedProjectDescriptor();
			parseTSModuleDescriptor(embeddedProjet, monitor);
			isValid = true;
		} catch (IOException e) {
			isValid = false;
			throw new InvalidModuleException("Error reading "
					+ this.jarURI.getPath(), e);
		}
	}

	protected void parseTSModuleDescriptor(TigerstripeProject embeddedProject,
			ITigerstripeProgressMonitor monitor) throws InvalidModuleException,
			IOException {
		JarFile file = new JarFile(this.jarURI.getPath());
		JarEntry tsModuleEntry = file
				.getJarEntry(ModuleDescriptorModel.DESCRIPTOR);

		if (tsModuleEntry == null)
			throw new InvalidModuleException("can't find "
					+ ModuleDescriptorModel.DESCRIPTOR + " in "
					+ this.jarURI.getPath());

		InputStream stream = file.getInputStream(tsModuleEntry);
		Reader reader = new InputStreamReader(stream, "UTF-8");
		model = new ModuleDescriptorModel(embeddedProject, reader, true,
				monitor);
		file.close();
	}

	private TigerstripeProject parseEmbeddedProjectDescriptor()
			throws InvalidModuleException, IOException {
		JarFile file = new JarFile(this.jarURI.getPath());
		JarEntry tsDescriptorEntry = file
				.getJarEntry(TigerstripeProject.DEFAULT_FILENAME);

		if (tsDescriptorEntry == null)
			throw new InvalidModuleException("can't find "
					+ TigerstripeProject.DEFAULT_FILENAME + " in "
					+ this.jarURI.getPath());

		InputStream stream = file.getInputStream(tsDescriptorEntry);
		Reader reader = new InputStreamReader(stream);
		TigerstripeProject embeddedProject = new TigerstripeProject(null);
		try {
			embeddedProject.parse(reader);
			// ((ModuleArtifactManager) getArtifactManager())
			// .setEmbeddedProject(embeddedProject);
		} catch (TigerstripeException e) {
			throw new InvalidModuleException(
					"Can't read embedded Project Descriptor in "
							+ this.jarURI.getPath());
		}
		file.close();
		return embeddedProject;
	}
}
