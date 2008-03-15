/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.api.impl.pluggable;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.WorkingCopyException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.impl.AbstractTigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.project.ITigerstripeVisitor;
import org.eclipse.tigerstripe.workbench.internal.core.project.ProjectDetails;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.GeneratorProjectDescriptor;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.runtime.PluginClasspathEntry;
import org.eclipse.tigerstripe.workbench.plugins.EPluggablePluginNature;
import org.eclipse.tigerstripe.workbench.plugins.IPluginClasspathEntry;
import org.eclipse.tigerstripe.workbench.plugins.IPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog.LogLevel;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeGeneratorProject;

public abstract class GeneratorProjectHandle extends
		AbstractTigerstripeProjectHandle implements
		ITigerstripeGeneratorProject {

	public GeneratorProjectHandle(URI projectContainerURI) {
		super(projectContainerURI);
	}

	@Override
	public void validate(ITigerstripeVisitor visitor)
			throws TigerstripeException {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	public <T extends IPluginProperty> Class<T>[] getSupportedProperties() {
		try {
			return getDescriptor().getSupportedProperties();
		} catch (TigerstripeException e) {
			return new Class[0];
		}
	}

	public String[] getSupportedPluginPropertyLabels() {
		try {
			return getDescriptor().getSupportedPluginPropertyLabels();
		} catch (TigerstripeException e) {
			return new String[0];
		}
	}

	public <T extends IPluginProperty> IPluginProperty makeProperty(
			Class<T> propertyType) throws TigerstripeException {
		return getDescriptor().makeProperty(propertyType);
	}

	public void addGlobalProperties(IPluginProperty[] properties)
			throws TigerstripeException {
		for (IPluginProperty property : properties) {
			addGlobalProperty(property);
		}
	}

	public void addGlobalProperty(IPluginProperty property)
			throws TigerstripeException {
		assertSet();
		getDescriptor().addGlobalProperty(property);
	}

	public void removeGlobalProperties(IPluginProperty[] properties)
			throws TigerstripeException {
		for (IPluginProperty property : properties) {
			removeGlobalProperty(property);
		}
	}

	public void removeGlobalProperty(IPluginProperty property)
			throws TigerstripeException {
		assertSet();
		getDescriptor().removeGlobalProperty(property);
	}

	public void addClasspathEntry(IPluginClasspathEntry entry)
			throws TigerstripeException {
		assertSet();
		getDescriptor().addClasspathEntry(entry);
	}

	public IPluginClasspathEntry[] getClasspathEntries()
			throws TigerstripeException {
		return getDescriptor().getClasspathEntries();
	}

	public IPluginClasspathEntry makeClasspathEntry() {
		return new PluginClasspathEntry();
	}

	public void removeClasspathEntries(IPluginClasspathEntry[] entries)
			throws TigerstripeException {
		assertSet();
		getDescriptor().removeClasspathEntries(entries);
	}

	public void removeClasspathEntry(IPluginClasspathEntry entry)
			throws TigerstripeException {
		assertSet();
		getDescriptor().removeClasspathEntry(entry);
	}

	public IPluginProperty[] getGlobalProperties() throws TigerstripeException {
		return getDescriptor().getGlobalProperties();
	}

	public void setGlobalProperties(IPluginProperty[] properties)
			throws TigerstripeException {
		assertSet();
		getDescriptor().setGlobalProperties(properties);
	}

	protected abstract String getDescriptorFilename();

	public IProjectDetails getProjectDetails() throws TigerstripeException {
		GeneratorProjectDescriptor descriptor = getDescriptor();
		return descriptor.getProjectDetails();
	}

	@Override
	public void setProjectDetails(IProjectDetails projectDetails)
			throws WorkingCopyException, TigerstripeException {
		assertSet();
		getDescriptor().setProjectDetails((ProjectDetails) projectDetails);
	}

	@Override
	protected boolean findProjectDescriptor() {
		projectContainer = new File(projectContainerURI);
		FilenameFilter filter = new FilenameFilter() {

			public boolean accept(File arg0, String arg1) {
				return getDescriptorFilename().equals(arg1);
			}

		};

		String[] desc = projectContainer.list(filter);

		return desc != null && desc.length == 1;
	}

	@Override
	public boolean exists() {
		boolean result = false;
		// check that a descriptor can be found and that it is valid
		if (findProjectDescriptor()) {
			try {
				getDescriptor();
				result = true;
			} catch (TigerstripeException e) {
				result = false;
			}
		}
		return result;
	}

	public abstract GeneratorProjectDescriptor getDescriptor()
			throws TigerstripeException;

	@Override
	protected void doCommit(IProgressMonitor monitor)
			throws TigerstripeException {
		GeneratorProjectDescriptor descriptor = getDescriptor();
		if (descriptor != null) {
			descriptor.doSave(null);
			
			GeneratorProjectHandle original = (GeneratorProjectHandle) getOriginal();
			original.getDescriptor().reload(true); // this will force a reload.

			return;
		}

		throw new TigerstripeException("Invalid project, cannot save.");
	}

	public String[] getAdditionalFiles(int includeExclude)
			throws TigerstripeException {
		return getDescriptor().getAdditionalFiles(includeExclude).toArray(
				new String[getDescriptor().getAdditionalFiles(includeExclude)
						.size()]);
	}

	public void addAdditionalFile(String relativePath, int includeExclude)
			throws TigerstripeException {
		assertSet();
		getDescriptor().addAdditionalFile(relativePath, includeExclude);
	}

	public void removeAdditionalFile(String relativePath, int includeExclude)
			throws TigerstripeException {
		assertSet();
		getDescriptor().removeAdditionalFile(relativePath, includeExclude);
	}

	public LogLevel getDefaultLogLevel() throws TigerstripeException {
		return getDescriptor().getDefaultLogLevel();
	}

	public String getLogPath() throws TigerstripeException {
		return getDescriptor().getLogPath();
	}

	public boolean isLogEnabled() throws TigerstripeException {
		return getDescriptor().isLogEnabled();
	}

	public void setDefaultLogLevel(LogLevel defaultLevel)
			throws TigerstripeException {
		assertSet();
		getDescriptor().setDefaultLogLevel(defaultLevel);
	}

	public void setLogEnabled(boolean isLogEnabled) throws TigerstripeException {
		assertSet();
		getDescriptor().setLogEnabled(isLogEnabled);
	}

	public void setLogPath(String logPath) throws TigerstripeException {
		assertSet();
		getDescriptor().setLogPath(logPath);
	}

	public void setPluginNature(EPluggablePluginNature nature)
			throws WorkingCopyException, TigerstripeException {
		assertSet();
		getDescriptor().setPluginNature(nature);
	}

	public EPluggablePluginNature getPluginNature() throws TigerstripeException {
		return getDescriptor().getPluginNature();
	}

	@Override
	public boolean isDirty() {
		try {
			return getDescriptor().isDirty();
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}
		return false;
	}

}
