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
package org.eclipse.tigerstripe.workbench.internal.api.impl.pluggable;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.WorkingCopyException;
import org.eclipse.tigerstripe.workbench.internal.WorkingCopyManager;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.api.impl.AbstractTigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.api.project.ITigerstripeVisitor;
import org.eclipse.tigerstripe.workbench.internal.core.project.ProjectDetails;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.PluggablePluginProject;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.BaseTemplatePPluginRule;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.runtime.PluginClasspathEntry;
import org.eclipse.tigerstripe.workbench.plugins.EPluggablePluginNature;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactBasedTemplateRunRule;
import org.eclipse.tigerstripe.workbench.plugins.IPluginClasspathEntry;
import org.eclipse.tigerstripe.workbench.plugins.IPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.IRunRule;
import org.eclipse.tigerstripe.workbench.plugins.ITemplateRunRule;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog.LogLevel;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripePluginProject;

/**
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class TigerstripePluginProjectHandle extends
		AbstractTigerstripeProjectHandle implements ITigerstripePluginProject {

	public final static String GLOBAL_PROPERTIES_F = "globalProperties";
	public final static String GLOBAL_RULE_F = "globalRule";
	public final static String ARTIFACT_RULE_F = "artifactRule";
	public final static String PLUGIN_NATURE_F = "pluginNature";
	public final static String CLASSPATH_ENTRY_F = "classpathEntry";
	public final static String ADDITIONAL_FILE_F = "additionalFile";
	public final static String DEFAULT_LOG_LEVEL_F = "logLevel";
	public final static String LOG_ENABLED_F = "logEnabled";
	public final static String LOG_PATH_F = "logPath";

	private PluggablePluginProject ppProject;

	@Override
	protected void addManagedFields() {
		super.addManagedFields();
		managedFields.add(GLOBAL_PROPERTIES_F);
		managedFields.add(GLOBAL_RULE_F);
		managedFields.add(ARTIFACT_RULE_F);
		managedFields.add(PLUGIN_NATURE_F);
		managedFields.add(CLASSPATH_ENTRY_F);
		managedFields.add(ADDITIONAL_FILE_F);
		managedFields.add(DEFAULT_LOG_LEVEL_F);
		managedFields.add(LOG_ENABLED_F);
		managedFields.add(LOG_PATH_F);
	}

	public TigerstripePluginProjectHandle(URI projectURI) {
		super(projectURI);
	}

	@Override
	public void markFieldDirty(String fieldID) throws TigerstripeException {
		super.markFieldDirty(fieldID);
	}

	public IPluginProperty[] getGlobalProperties() throws TigerstripeException {
		return getPPProject().getGlobalProperties();
	}

	public void setGlobalProperties(IPluginProperty[] properties)
			throws TigerstripeException {
		assertSet(GLOBAL_PROPERTIES_F);
		getPPProject().setGlobalProperties(properties);
		for (IPluginProperty property : properties) {
			property.setProject(this);
		}
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
	public void setProjectDetails(IProjectDetails projectDetails)
			throws WorkingCopyException, TigerstripeException {
		assertSet(PROJECT_DETAILS_F);
		PluggablePluginProject project = getPPProject();
		project.setProjectDetails((ProjectDetails) projectDetails);
	}

	@SuppressWarnings("unchecked")
	public Class[] getSupportedPluginProperties() {
		try {
			return getPPProject().getSupportedPluginProperties();
		} catch (TigerstripeException e) {
			return new Class[0];
		}
	}

	public String[] getSupportedPluginPropertyLabels() {
		try {
			return getPPProject().getSupportedPluginPropertyLabels();
		} catch (TigerstripeException e) {
			return new String[0];
		}
	}

	public <T extends IPluginProperty> IPluginProperty makeProperty(
			Class<T> propertyType) throws TigerstripeException {
		return getPPProject().makeProperty(propertyType);
	}

	public void addGlobalProperties(IPluginProperty[] properties)
			throws TigerstripeException {
		for (IPluginProperty property : properties) {
			addGlobalProperty(property);
		}
	}

	public void addGlobalProperty(IPluginProperty property)
			throws TigerstripeException {
		assertSet(GLOBAL_PROPERTIES_F);
		getPPProject().addGlobalProperty(property);
		property.setProject(this);
	}

	public void removeGlobalProperties(IPluginProperty[] properties)
			throws TigerstripeException {
		for (IPluginProperty property : properties) {
			removeGlobalProperty(property);
		}
	}

	public void removeGlobalProperty(IPluginProperty property)
			throws TigerstripeException {
		assertSet(GLOBAL_PROPERTIES_F);
		getPPProject().removeGlobalProperty(property);
		property.setProject(null);
	}

	// =========================================

	public void addGlobalRule(IRunRule rule) throws TigerstripeException {
		assertSet(GLOBAL_RULE_F);
		getPPProject().addGlobalRule(rule);
		((BaseTemplatePPluginRule)rule).setProject(this);
	}

	public void addGlobalRules(IRunRule[] rules) throws TigerstripeException {
		for (IRunRule rule : rules) {
			addGlobalRule(rule);
		}
	}

	public IRunRule[] getGlobalRules() throws TigerstripeException {
		return getPPProject().getGlobalRules();
	}

	public void removeGlobalRule(IRunRule rule) throws TigerstripeException {
		assertSet(GLOBAL_RULE_F);
		getPPProject().removeGlobalRule(rule);
		((BaseTemplatePPluginRule)rule).setProject(null);
	}

	public void removeGlobalRules(IRunRule[] rules) throws TigerstripeException {
		assertSet(GLOBAL_RULE_F);
		getPPProject().removeGlobalRules(rules);
	}

	public <T extends IRunRule> Class<T>[] getSupportedPluginRules() {
		try {
			return getPPProject().getSupportedPluginRules();
		} catch (TigerstripeException e) {
			return new Class[0];
		}
	}

	public String[] getSupportedPluginRuleLabels() {
		try {
			return getPPProject().getSupportedPluginRuleLabels();
		} catch (TigerstripeException e) {
			return new String[0];
		}
	}

	public <T extends IRunRule> IRunRule makeRule(Class<T> ruleType)
			throws TigerstripeException {
		return getPPProject().makeRule(ruleType);
	}

	// =========================================

	public void addArtifactRule(ITemplateRunRule rule)
			throws TigerstripeException {
		assertSet(ARTIFACT_RULE_F);
		getPPProject().addArtifactRule(rule);
		((BaseTemplatePPluginRule)rule).setProject(this);
	}

	public void addArtifactRules(ITemplateRunRule[] rules)
			throws TigerstripeException {
		for (ITemplateRunRule rule : rules) {
			addArtifactRule(rule);
		}
	}

	public ITemplateRunRule[] getArtifactRules() throws TigerstripeException {
		return getPPProject().getArtifactRules();
	}

	public void removeArtifactRule(ITemplateRunRule rule)
			throws TigerstripeException {
		assertSet(ARTIFACT_RULE_F);
		getPPProject().removeArtifactRule(rule);
		((BaseTemplatePPluginRule)rule).setProject(null);
	}

	public void removeArtifactRules(ITemplateRunRule[] rules)
			throws TigerstripeException {
		assertSet(ARTIFACT_RULE_F);
		getPPProject().removeArtifactRules(rules);
	}

	public <T extends IArtifactBasedTemplateRunRule> Class<T>[] getSupportedPluginArtifactRules() {
		try {
			return getPPProject().getSupportedPluginArtifactRules();
		} catch (TigerstripeException e) {
			return new Class[0];
		}
	}

	public String[] getSupportedPluginArtifactRuleLabels() {
		try {
			return getPPProject().getSupportedPluginArtifactRuleLabels();
		} catch (TigerstripeException e) {
			return new String[0];
		}
	}

	public IProjectDetails getIProjectDetails() throws TigerstripeException {

		return getProjectDetails();
	}

	public void addClasspathEntry(IPluginClasspathEntry entry)
			throws TigerstripeException {
		assertSet(CLASSPATH_ENTRY_F);
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
		assertSet(CLASSPATH_ENTRY_F);
		getPPProject().removeClasspathEntries(entries);
	}

	public void removeClasspathEntry(IPluginClasspathEntry entry)
			throws TigerstripeException {
		assertSet(CLASSPATH_ENTRY_F);
		getPPProject().removeClasspathEntry(entry);
	}

	public void doSave() throws TigerstripeException {
		PluggablePluginProject project = getPPProject();
		if (project != null) {
			project.doSave(null);
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
		assertSet(ADDITIONAL_FILE_F);
		PluggablePluginProject project = getPPProject();
		project.addAdditionalFile(relativePath, includeExclude);
	}

	public void removeAdditionalFile(String relativePath, int includeExclude)
			throws TigerstripeException {
		assertSet(ADDITIONAL_FILE_F);
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
		assertSet(DEFAULT_LOG_LEVEL_F);
		PluggablePluginProject project = getPPProject();
		project.setDefaultLogLevel(defaultLevel);
	}

	public void setLogEnabled(boolean isLogEnabled) throws TigerstripeException {
		assertSet(LOG_ENABLED_F);
		PluggablePluginProject project = getPPProject();
		project.setLogEnabled(isLogEnabled);
	}

	public void setLogPath(String logPath) throws TigerstripeException {
		assertSet(LOG_PATH_F);
		PluggablePluginProject project = getPPProject();
		project.setLogPath(logPath);
	}

	public void setPluginNature(EPluggablePluginNature nature)
			throws WorkingCopyException, TigerstripeException {
		assertSet(PLUGIN_NATURE_F);
		PluggablePluginProject project = getPPProject();
		project.setPluginNature(nature);
	}

	public EPluggablePluginNature getPluginNature() throws TigerstripeException {
		PluggablePluginProject project = getPPProject();
		return project.getPluginNature();
	}

	@Override
	protected void doCommit(IProgressMonitor monitor)
			throws TigerstripeException {
		doSave();
	}

	public final static String DESCRIPTOR_FILENAME = "ts-plugin.xml";

	public String getDescriptorFilename() {
		return DESCRIPTOR_FILENAME;
	}

	@Override
	public boolean exists() {
		boolean result = false;
		// check that a descriptor can be found and that it is valid
		if (findProjectDescriptor()) {
			try {
				getPPProject();
				result = true;
			} catch (TigerstripeException e) {
				result = false;
			}
		}
		return result;
	}

	@Override
	public void validate(ITigerstripeVisitor visitor)
			throws TigerstripeException {
		// TODO Auto-generated method stub

	}

	@Override
	protected WorkingCopyManager doCreateCopy(IProgressMonitor monitor)
			throws TigerstripeException {
		TigerstripePluginProjectHandle copy = new TigerstripePluginProjectHandle(
				getURI());
		return copy;
	}

}
