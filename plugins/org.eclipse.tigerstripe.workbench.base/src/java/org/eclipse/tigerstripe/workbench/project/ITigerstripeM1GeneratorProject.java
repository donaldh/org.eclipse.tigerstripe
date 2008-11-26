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
import org.eclipse.tigerstripe.workbench.plugins.IArtifactRule;
import org.eclipse.tigerstripe.workbench.plugins.IRule;

/**
 * A M1 (Class-based) Generator Project
 * 
 * @author Eric Dillon
 * @since 0.3
 */
public interface ITigerstripeM1GeneratorProject extends
		ITigerstripeGeneratorProject {

	// ==================================================
	// Rules-related definitions
	public IArtifactRule[] getArtifactRules() throws TigerstripeException;

	public void addArtifactRule(IArtifactRule rule)
			throws TigerstripeException;

	public void removeArtifactRule(IRule rule)
			throws TigerstripeException;

	public void removeArtifactRules(IRule[] rules)
			throws TigerstripeException;

	public void addArtifactRules(IArtifactRule[] rules)
			throws TigerstripeException;

	public <T extends IArtifactRule> Class<T>[] getSupportedPluginArtifactRules();

	public String[] getSupportedPluginArtifactRuleLabels();

}
