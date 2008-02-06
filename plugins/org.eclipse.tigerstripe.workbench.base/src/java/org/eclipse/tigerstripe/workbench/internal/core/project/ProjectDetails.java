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
package org.eclipse.tigerstripe.workbench.internal.core.project;

import java.io.File;
import java.util.Properties;

import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

/**
 * @author Eric Dillon
 * 
 * The project details for a Tigerstripe project
 * 
 */
public class ProjectDetails implements IProjectDetails {

	private String description = "";

	private String version = "";

	private String name = "";

	private String provider = "";

	private Properties properties = new Properties();

	private String outputDirectory = "";

	private String nature = "";

	private AbstractTigerstripeProject parentProject;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersion() {
		return this.version;
	}

	public ProjectDetails(AbstractTigerstripeProject parentProject) {
		this.properties = new Properties();
		setParentProject(parentProject);
	}

	public void setParentProject(AbstractTigerstripeProject parentProject) {
		this.parentProject = parentProject;
	}

	public String getNature() {
		return this.nature;
	}

	/**
	 * Sets the nature of the project
	 * 
	 * @see org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfig
	 *      for valid project natures
	 */
	public void setNature(String nature) {
		this.nature = nature;
	}

	/**
	 * Returns the actual output Directory that should be used taking into
	 * account facets and module eg.
	 */
	public String getOutputDirectory() {
		String result = getProjectOutputDirectory();

		if (parentProject == null)
			// parentProject==null when this class is used for
			// ModuleDescriptorModel as a hack.
			return result;

		if (parentProject.getBaseDir() == null) {
			// parent.getBaseDir()==null when this is a module.
			result = result + File.separator
					+ parentProject.getProjectDetails().getName();
			return result;
		}

		try {
			IAbstractTigerstripeProject aProject = TigerstripeCore
					.findProject(parentProject.getBaseDir().toURI());
			if (aProject instanceof ITigerstripeProject) {
				ITigerstripeProject project = (ITigerstripeProject) aProject;
				if (project.getActiveFacet() != null) {
					result = result + File.separator
							+ project.getActiveFacet().getGenerationDir();
				}
			}
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);

		}
		return result;
	}

	public String getProjectOutputDirectory() {
		return outputDirectory;
	}

	public void setProjectOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public Properties getProperties() {
		return this.properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getProperty(String name, String defaultValue) {
		return this.properties.getProperty(name, defaultValue);
	}

	public String getProperty(String name) {
		return this.properties.getProperty(name);
	}

	public String getProvider() {
		return this.provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}
}
