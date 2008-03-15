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
package org.eclipse.tigerstripe.workbench.project;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.WorkingCopyException;
import org.eclipse.tigerstripe.workbench.plugins.EPluggablePluginNature;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactBasedTemplateRunRule;
import org.eclipse.tigerstripe.workbench.plugins.IPluginClasspathEntry;
import org.eclipse.tigerstripe.workbench.plugins.IPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.IRunRule;
import org.eclipse.tigerstripe.workbench.plugins.ITemplateRunRule;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog;

/**
 * A Pluggable plugin project
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public interface ITigerstripePluginProject extends ITigerstripeGeneratorProject {

	// ==================================================
	// Rules-related definitions
	/**
	 * Global rules are run once per generation cycle
	 * 
	 */
	public <T extends IRunRule> IRunRule makeRule(Class<T> ruleType)
			throws TigerstripeException;

	public IRunRule[] getGlobalRules() throws TigerstripeException;

	public void addGlobalRule(IRunRule rule) throws TigerstripeException;

	public void removeGlobalRule(IRunRule rule) throws TigerstripeException;

	public void removeGlobalRules(IRunRule[] rules) throws TigerstripeException;

	public void addGlobalRules(IRunRule[] rules) throws TigerstripeException;

	public <T extends IRunRule> Class<T>[] getSupportedPluginRules();

	public ITemplateRunRule[] getArtifactRules() throws TigerstripeException;

	public void addArtifactRule(ITemplateRunRule rule)
			throws TigerstripeException;

	public void removeArtifactRule(ITemplateRunRule rule)
			throws TigerstripeException;

	public void removeArtifactRules(ITemplateRunRule[] rules)
			throws TigerstripeException;

	public void addArtifactRules(ITemplateRunRule[] rules)
			throws TigerstripeException;

	public <T extends IArtifactBasedTemplateRunRule> Class<T>[] getSupportedPluginArtifactRules();

	public String[] getSupportedPluginArtifactRuleLabels();

}
