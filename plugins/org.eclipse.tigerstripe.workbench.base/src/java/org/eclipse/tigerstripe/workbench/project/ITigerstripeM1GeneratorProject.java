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
import org.eclipse.tigerstripe.workbench.plugins.IArtifactBasedTemplateRule;
import org.eclipse.tigerstripe.workbench.plugins.ITemplateBasedRule;

/**
 * A M1 (Class-based) Generator Project
 * 
 * @author Eric Dillon
 * @since 0.3
 */
public interface ITigerstripeM1GeneratorProject extends ITigerstripeGeneratorProject {

	// ==================================================
	// Rules-related definitions
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
