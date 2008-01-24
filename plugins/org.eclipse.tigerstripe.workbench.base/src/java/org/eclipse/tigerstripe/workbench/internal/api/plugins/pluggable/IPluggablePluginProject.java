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
package org.eclipse.tigerstripe.workbench.internal.api.plugins.pluggable;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.PluggablePluginProject;
import org.eclipse.tigerstripe.workbench.plugins.IPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;

/**
 * A Pluggable plugin project
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface IPluggablePluginProject extends IAbstractTigerstripeProject {

	public PluggablePluginProject getPPProject() throws TigerstripeException;

	public IPluginProperty[] getGlobalProperties()
			throws TigerstripeException;

	public void setGlobalProperties(IPluginProperty[] properties)
			throws TigerstripeException;

	public void addGlobalProperty(IPluginProperty property)
			throws TigerstripeException;

	public void removeGlobalProperty(IPluginProperty property)
			throws TigerstripeException;

	public void removeGlobalProperties(IPluginProperty[] properties)
			throws TigerstripeException;

	public void addGlobalProperties(IPluginProperty[] properties)
			throws TigerstripeException;

	public String[] getSupportedPluginProperties();

	public String[] getSupportedPluginPropertyLabels();

	/**
	 * Factory method for Plugin properties
	 */
	public IPluginProperty makeProperty(String propertyType)
			throws TigerstripeException;

	// ==================================================
	// Rules-related definitions
	/**
	 * Global rules are run once per generation cycle
	 * 
	 */
	public IRunRule makeRule(String ruleType) throws TigerstripeException;

	public IRunRule[] getGlobalRules() throws TigerstripeException;

	public void addGlobalRule(IRunRule rule) throws TigerstripeException;

	public void removeGlobalRule(IRunRule rule) throws TigerstripeException;

	public void removeGlobalRules(IRunRule[] rules) throws TigerstripeException;

	public void addGlobalRules(IRunRule[] rules) throws TigerstripeException;

	public String[] getSupportedPluginRules();

	public String[] getSupportedPluginRuleLabels();

	public ITemplateRunRule[] getArtifactRules() throws TigerstripeException;

	public void addArtifactRule(ITemplateRunRule rule)
			throws TigerstripeException;

	public void removeArtifactRule(ITemplateRunRule rule)
			throws TigerstripeException;

	public void removeArtifactRules(ITemplateRunRule[] rules)
			throws TigerstripeException;

	public void addArtifactRules(ITemplateRunRule[] rules)
			throws TigerstripeException;

	public void addClasspathEntry(IPluginClasspathEntry entry)
			throws TigerstripeException;

	public void removeClasspathEntry(IPluginClasspathEntry entry)
			throws TigerstripeException;

	public void removeClasspathEntries(IPluginClasspathEntry[] entries)
			throws TigerstripeException;

	public IPluginClasspathEntry makeClasspathEntry();

	public IPluginClasspathEntry[] getClasspathEntries()
			throws TigerstripeException;

	public String[] getSupportedPluginArtifactRules();

	public String[] getSupportedPluginArtifactRuleLabels();

	public final static int ADDITIONAL_FILE_INCLUDE = 0;

	public final static int ADDITIONAL_FILE_EXCLUDE = 1;

	public void addAdditionalFile(String relativePath, int includeExclude)
			throws TigerstripeException;

	public void removeAdditionalFile(String relativePath, int includeExclude)
			throws TigerstripeException;

	public String[] getAdditionalFiles(int includeExclude)
			throws TigerstripeException;

	public void setLogEnabled(boolean isLogEnabled) throws TigerstripeException;

	public boolean isLogEnabled() throws TigerstripeException;

	public void setLogPath(String logPath) throws TigerstripeException;

	public String getLogPath() throws TigerstripeException;

	public PluginLog.LogLevel getDefaultLogLevel() throws TigerstripeException;

	public void setDefaultLogLevel(PluginLog.LogLevel defaultLevel)
			throws TigerstripeException;

	public EPluggablePluginNature getPluginNature() throws TigerstripeException;

	public void setPluginNature(EPluggablePluginNature nature)
			throws TigerstripeException;
}
