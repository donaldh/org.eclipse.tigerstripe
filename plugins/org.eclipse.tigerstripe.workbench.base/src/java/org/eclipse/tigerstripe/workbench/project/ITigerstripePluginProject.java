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
import org.eclipse.tigerstripe.workbench.plugins.IArtifactBasedTemplateRule;
import org.eclipse.tigerstripe.workbench.plugins.IPluginClasspathEntry;
import org.eclipse.tigerstripe.workbench.plugins.IPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.IRule;
import org.eclipse.tigerstripe.workbench.plugins.ITemplateBasedRule;
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
	public <T extends IRule> IRule makeRule(Class<T> ruleType)
			throws TigerstripeException;

	public IRule[] getGlobalRules() throws TigerstripeException;

	public void addGlobalRule(IRule rule) throws TigerstripeException;

	public void removeGlobalRule(IRule rule) throws TigerstripeException;

	public void removeGlobalRules(IRule[] rules) throws TigerstripeException;

	public void addGlobalRules(IRule[] rules) throws TigerstripeException;

	public <T extends IRule> Class<T>[] getSupportedPluginRules();

	public ITemplateBasedRule[] getArtifactRules() throws TigerstripeException;

	public void addArtifactRule(ITemplateBasedRule rule)
			throws TigerstripeException;

	public void removeArtifactRule(ITemplateBasedRule rule)
			throws TigerstripeException;

	public void removeArtifactRules(ITemplateBasedRule[] rules)
			throws TigerstripeException;

	public void addArtifactRules(ITemplateBasedRule[] rules)
			throws TigerstripeException;

	public <T extends IArtifactBasedTemplateRule> Class<T>[] getSupportedPluginArtifactRules();

	public String[] getSupportedPluginArtifactRuleLabels();

}
