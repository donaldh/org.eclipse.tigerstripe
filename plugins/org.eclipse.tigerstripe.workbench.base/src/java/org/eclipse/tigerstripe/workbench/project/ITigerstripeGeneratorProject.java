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
package org.eclipse.tigerstripe.workbench.project;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.WorkingCopyException;
import org.eclipse.tigerstripe.workbench.plugins.EPluggablePluginNature;
import org.eclipse.tigerstripe.workbench.plugins.IPluginClasspathEntry;
import org.eclipse.tigerstripe.workbench.plugins.IPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.IRule;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog;

public interface ITigerstripeGeneratorProject extends
		IAbstractTigerstripeProject {

	public IPluginProperty[] getGlobalProperties() throws TigerstripeException;

	public void setGlobalProperties(IPluginProperty[] properties)
			throws WorkingCopyException, TigerstripeException;

	public void addGlobalProperty(IPluginProperty property)
			throws WorkingCopyException, TigerstripeException;

	public void removeGlobalProperty(IPluginProperty property)
			throws WorkingCopyException, TigerstripeException;

	public void removeGlobalProperties(IPluginProperty[] properties)
			throws WorkingCopyException, TigerstripeException;

	public void addGlobalProperties(IPluginProperty[] properties)
			throws WorkingCopyException, TigerstripeException;

	public <T extends IPluginProperty> Class<T>[] getSupportedProperties();

	public IRule[] getGlobalRules() throws TigerstripeException;

	public void addGlobalRule(IRule rule) throws TigerstripeException;

	public void removeGlobalRule(IRule rule) throws TigerstripeException;

	public void removeGlobalRules(IRule[] rules) throws TigerstripeException;

	public void addGlobalRules(IRule[] rules) throws TigerstripeException;

	public <T extends IRule> Class<T>[] getSupportedGlobalRules();

	public String[] getSupportedGlobalRuleLabels();
	
	/**
	 * Global rules are run once per generation cycle
	 * 
	 */
	public <T extends IRule> IRule makeRule(Class<T> ruleType)
			throws TigerstripeException;

	/**
	 * Factory method for Plugin properties
	 */
	public <T extends IPluginProperty> IPluginProperty makeProperty(
			Class<T> propertyType) throws TigerstripeException;

	public void addClasspathEntry(IPluginClasspathEntry entry)
			throws TigerstripeException;

	public void removeClasspathEntry(IPluginClasspathEntry entry)
			throws TigerstripeException;

	public void removeClasspathEntries(IPluginClasspathEntry[] entries)
			throws TigerstripeException;

	public IPluginClasspathEntry makeClasspathEntry();

	public IPluginClasspathEntry[] getClasspathEntries()
			throws TigerstripeException;

	public final static int ADDITIONAL_FILE_INCLUDE = 0;

	public final static int ADDITIONAL_FILE_EXCLUDE = 1;

	public void addAdditionalFile(String relativePath, int includeExclude)
			throws WorkingCopyException, TigerstripeException;

	public void removeAdditionalFile(String relativePath, int includeExclude)
			throws WorkingCopyException, TigerstripeException;

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
			throws WorkingCopyException, TigerstripeException;
}
