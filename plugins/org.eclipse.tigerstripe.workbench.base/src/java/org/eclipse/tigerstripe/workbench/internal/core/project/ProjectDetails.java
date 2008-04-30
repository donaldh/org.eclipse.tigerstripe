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

import org.eclipse.tigerstripe.workbench.internal.BaseContainerObject;
import org.eclipse.tigerstripe.workbench.internal.IContainedObject;
import org.eclipse.tigerstripe.workbench.internal.IContainerObject;
import org.eclipse.tigerstripe.workbench.internal.core.util.ContainedProperties;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;

/**
 * @author Eric Dillon
 * 
 * The project details for a Tigerstripe project
 * 
 */
public class ProjectDetails extends BaseContainerObject implements
		IContainedObject, IContainerObject, IProjectDetails {

	private String description = "";

	private String version = "";

	private String name = "";

	private String provider = "";

	private ContainedProperties properties = null;

	private String outputDirectory = "";

	private AbstractTigerstripeProject parentProject;

	// ===========================================================================
	// ===========================================================================
	// Containment Handling
	private boolean isLocalDirty = false;
	private IContainerObject container = null;

	public void setContainer(IContainerObject container) {
		isLocalDirty = false;
		this.container = container;
	}

	public void clearDirty() {
		isLocalDirty = false;
	}

	public boolean isDirty() {
		return isLocalDirty;
	}

	public IContainerObject getContainer() {
		return this.container;
	}

	/**
	 * Marks this object as dirty and notify the container if any
	 * 
	 */
	protected void markDirty() {
		isLocalDirty = true;
		if (container != null) {
			container.notifyDirty(this);
		}
	}

	// ===========================================================================
	// ===========================================================================

	public String getName() {
		return name;
	}

	public void setName(String name) {
		markDirty();
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		markDirty();
		this.description = description;
	}

	public void setVersion(String version) {
		markDirty();
		this.version = version;
	}

	public String getVersion() {
		return this.version;
	}

	public ProjectDetails(AbstractTigerstripeProject parentProject) {
		this.properties = new ContainedProperties();
		this.parentProject = parentProject;
		applyDefaults();

		this.properties.setContainer(this);
	}

	public void setParentProject(AbstractTigerstripeProject parentProject) {
		this.parentProject = parentProject;
	}

	/**
	 * Applies default values based on Preferences
	 * 
	 */
	protected void applyDefaults() {
		properties.put(IProjectDetails.DEFAULTARTIFACTPACKAGE_PROP,
				"com.mycompany");
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
			result = result + File.separator + parentProject.getProjectLabel();
			return result;
		}

		return result;
	}

	public String getProjectOutputDirectory() {
		return outputDirectory;
	}

	public void setProjectOutputDirectory(String outputDirectory) {
		markDirty();
		this.outputDirectory = outputDirectory;
	}

	public Properties getProperties() {
		return this.properties;
	}

	public void setProperties(Properties properties) {
		markDirty();
		this.properties = new ContainedProperties(properties);
		this.properties.setContainer(this);
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
		markDirty();
		this.provider = provider;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		ProjectDetails clone = new ProjectDetails(parentProject);
		clone.setDescription(getDescription());
		clone.setName(getName());
		clone.setProjectOutputDirectory(getOutputDirectory());
		clone.setProperties((Properties) getProperties().clone());
		clone.setProvider(getProvider());
		clone.setVersion(getVersion());
		return clone;
	}
}
