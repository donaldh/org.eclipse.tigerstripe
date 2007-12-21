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
package org.eclipse.tigerstripe.api.impl.pluggable;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URI;

import org.eclipse.tigerstripe.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.plugins.PluginLog.LogLevel;
import org.eclipse.tigerstripe.api.external.project.IextProjectDetails;
import org.eclipse.tigerstripe.api.impl.AbstractTigerstripeProjectHandle;
import org.eclipse.tigerstripe.api.plugins.pluggable.EPluggablePluginNature;
import org.eclipse.tigerstripe.api.plugins.pluggable.IPluggablePluginProject;
import org.eclipse.tigerstripe.api.plugins.pluggable.IPluggablePluginProperty;
import org.eclipse.tigerstripe.api.plugins.pluggable.IPluginClasspathEntry;
import org.eclipse.tigerstripe.api.plugins.pluggable.IRunRule;
import org.eclipse.tigerstripe.api.plugins.pluggable.ITemplateRunRule;
import org.eclipse.tigerstripe.api.project.IProjectDetails;
import org.eclipse.tigerstripe.api.project.ITigerstripeVisitor;
import org.eclipse.tigerstripe.core.project.pluggable.PluggablePluginProject;
import org.eclipse.tigerstripe.core.project.pluggable.runtime.PluginClasspathEntry;

/**
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public abstract class AbstractPluggablePluginProjectHandle extends
		AbstractTigerstripeProjectHandle implements IPluggablePluginProject {

	private PluggablePluginProject ppProject;

	public AbstractPluggablePluginProjectHandle(URI projectURI) {
		super(projectURI);
	}

	public IPluggablePluginProperty[] getGlobalProperties()
			throws TigerstripeException {
		return getPPProject().getGlobalProperties();
	}

	public void setGlobalProperties(IPluggablePluginProperty[] properties)
			throws TigerstripeException {
		getPPProject().setGlobalProperties(properties);
	}

	@Override
	protected boolean findProjectDescriptor() {
		projectContainer = new File(projectContainerURI);
		FilenameFilter filter = new FilenameFilter() {

			public boolean accept(File arg0, String arg1) {
				return ITigerstripeConstants.PLUGIN_DESCRIPTOR.equals(arg1);
			}

		};

		String[] desc = projectContainer.list(filter);

		return desc != null && desc.length == 1;
	}

	public PluggablePluginProject getPPProject() throws TigerstripeException {
		if (ppProject == null) {
			File baseDir = new File(this.projectContainerURI);
			if (baseDir.isDirectory()) {
				ppProject = new PluggablePluginProject(baseDir);
			} else
				throw new TigerstripeException("Invalid project "
						+ baseDir.toString());
		}

		ppProject.reload(false);
		return this.ppProject;
	}

	public IProjectDetails getProjectDetails() throws TigerstripeException {
		PluggablePluginProject project = getPPProject();
		return project.getProjectDetails();
	}

	@Override
	public String getProjectLabel() {
		try {
			return getPPProject().getProjectLabel();
		} catch (TigerstripeException e) {
			// Ignore for now
		}
		return "unknown";
	}

	public String[] getSupportedPluginProperties() {
		try {
			return getPPProject().getSupportedPluginProperties();
		} catch (TigerstripeException e) {
			return new String[0];
		}
	}

	public String[] getSupportedPluginPropertyLabels() {
		try {
			return getPPProject().getSupportedPluginPropertyLabels();
		} catch (TigerstripeException e) {
			return new String[0];
		}
	}

	public IPluggablePluginProperty makeProperty(String propertyType)
			throws TigerstripeException {
		return getPPProject().makeProperty(propertyType);
	}

	public void addGlobalProperties(IPluggablePluginProperty[] properties)
			throws TigerstripeException {
		getPPProject().addGlobalProperties(properties);
	}

	public void addGlobalProperty(IPluggablePluginProperty property)
			throws TigerstripeException {
		getPPProject().addGlobalProperty(property);
	}

	public void removeGlobalProperties(IPluggablePluginProperty[] properties)
			throws TigerstripeException {
		getPPProject().removeGlobalProperties(properties);
	}

	public void removeGlobalProperty(IPluggablePluginProperty property)
			throws TigerstripeException {
		getPPProject().removeGlobalProperty(property);
	}

	// =========================================

	public void addGlobalRule(IRunRule rule) throws TigerstripeException {
		getPPProject().addGlobalRule(rule);
	}

	public void addGlobalRules(IRunRule[] rules) throws TigerstripeException {
		getPPProject().addGlobalRules(rules);
	}

	public IRunRule[] getGlobalRules() throws TigerstripeException {
		return getPPProject().getGlobalRules();
	}

	public void removeGlobalRule(IRunRule rule) throws TigerstripeException {
		getPPProject().removeGlobalRule(rule);
	}

	public void removeGlobalRules(IRunRule[] rules) throws TigerstripeException {
		getPPProject().removeGlobalRules(rules);
	}

	public String[] getSupportedPluginRules() {
		try {
			return getPPProject().getSupportedPluginRules();
		} catch (TigerstripeException e) {
			return new String[0];
		}
	}

	public String[] getSupportedPluginRuleLabels() {
		try {
			return getPPProject().getSupportedPluginRuleLabels();
		} catch (TigerstripeException e) {
			return new String[0];
		}
	}

	public IRunRule makeRule(String ruleType) throws TigerstripeException {
		return getPPProject().makeRule(ruleType);
	}

	// =========================================

	public void addArtifactRule(ITemplateRunRule rule)
			throws TigerstripeException {
		getPPProject().addArtifactRule(rule);
	}

	public void addArtifactRules(ITemplateRunRule[] rules)
			throws TigerstripeException {
		getPPProject().addArtifactRules(rules);
	}

	public ITemplateRunRule[] getArtifactRules() throws TigerstripeException {
		return getPPProject().getArtifactRules();
	}

	public void removeArtifactRule(ITemplateRunRule rule)
			throws TigerstripeException {
		getPPProject().removeArtifactRule(rule);
	}

	public void removeArtifactRules(ITemplateRunRule[] rules)
			throws TigerstripeException {
		getPPProject().removeArtifactRules(rules);
	}

	public String[] getSupportedPluginArtifactRules() {
		try {
			return getPPProject().getSupportedPluginArtifactRules();
		} catch (TigerstripeException e) {
			return new String[0];
		}
	}

	public String[] getSupportedPluginArtifactRuleLabels() {
		try {
			return getPPProject().getSupportedPluginArtifactRuleLabels();
		} catch (TigerstripeException e) {
			return new String[0];
		}
	}

	public IextProjectDetails getIextProjectDetails()
			throws TigerstripeException {

		return getProjectDetails();
	}

	public void addClasspathEntry(IPluginClasspathEntry entry)
			throws TigerstripeException {
		getPPProject().addClasspathEntry(entry);
	}

	public IPluginClasspathEntry[] getClasspathEntries()
			throws TigerstripeException {
		return getPPProject().getClasspathEntries();
	}

	public IPluginClasspathEntry makeClasspathEntry() {
		PluginClasspathEntry result = new PluginClasspathEntry();
		result.setProject(this);
		return result;
	}

	public void removeClasspathEntries(IPluginClasspathEntry[] entries)
			throws TigerstripeException {
		getPPProject().removeClasspathEntries(entries);
	}

	public void removeClasspathEntry(IPluginClasspathEntry entry)
			throws TigerstripeException {
		getPPProject().removeClasspathEntry(entry);
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(ITigerstripeVisitor visitor)
			throws TigerstripeException {
		// TODO Auto-generated method stub

	}

	public void doSave() throws TigerstripeException {
		PluggablePluginProject project = getPPProject();
		if (project != null) {
			project.doSave();
			return;
		}

		throw new TigerstripeException("Invalid project, cannot save.");
	}

	public String[] getAdditionalFiles(int includeExclude)
			throws TigerstripeException {
		PluggablePluginProject project = getPPProject();
		return project.getAdditionalFiles(includeExclude).toArray(
				new String[project.getAdditionalFiles(includeExclude).size()]);
	}

	public void addAdditionalFile(String relativePath, int includeExclude)
			throws TigerstripeException {
		PluggablePluginProject project = getPPProject();
		project.addAdditionalFile(relativePath, includeExclude);
	}

	public void removeAdditionalFile(String relativePath, int includeExclude)
			throws TigerstripeException {
		PluggablePluginProject project = getPPProject();
		project.removeAdditionalFile(relativePath, includeExclude);
	}

	public LogLevel getDefaultLogLevel() throws TigerstripeException {
		PluggablePluginProject project = getPPProject();
		return project.getDefaultLogLevel();
	}

	public String getLogPath() throws TigerstripeException {
		PluggablePluginProject project = getPPProject();
		return project.getLogPath();
	}

	public boolean isLogEnabled() throws TigerstripeException {
		PluggablePluginProject project = getPPProject();
		return project.isLogEnabled();
	}

	public void setDefaultLogLevel(LogLevel defaultLevel)
			throws TigerstripeException {
		PluggablePluginProject project = getPPProject();
		project.setDefaultLogLevel(defaultLevel);
	}

	public void setLogEnabled(boolean isLogEnabled) throws TigerstripeException {
		PluggablePluginProject project = getPPProject();
		project.setLogEnabled(isLogEnabled);
	}

	public void setLogPath(String logPath) throws TigerstripeException {
		PluggablePluginProject project = getPPProject();
		project.setLogPath(logPath);
	}

	public void setPluginNature(EPluggablePluginNature nature)
			throws TigerstripeException {
		PluggablePluginProject project = getPPProject();
		project.setPluginNature(nature);
	}

	public EPluggablePluginNature getPluginNature() throws TigerstripeException {
		PluggablePluginProject project = getPPProject();
		return project.getPluginNature();
	}
}
