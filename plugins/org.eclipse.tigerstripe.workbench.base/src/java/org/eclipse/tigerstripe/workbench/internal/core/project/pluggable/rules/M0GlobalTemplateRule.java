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
package org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules;

import org.apache.velocity.VelocityContext;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.generation.M0RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.IPluginRuleExecutor;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePluginConfig;
import org.eclipse.tigerstripe.workbench.plugins.IGlobalTemplateRule;

/**
 * Implementation of a simple rule that is run once per generation cycle
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class M0GlobalTemplateRule extends GlobalTemplateRule implements
		IGlobalTemplateRule {

	public final static String LABEL = "Global M0 Template Rule";

	@Override
	public String getLabel() {
		return LABEL;
	}

	public String getType() {
		return IGlobalTemplateRule.class.getCanonicalName();
	}

	@Override
	protected VelocityContext getDefaultContext(
			PluggablePluginConfig pluginConfig, IPluginRuleExecutor exec)
			throws TigerstripeException {
		VelocityContext result = super.getDefaultContext(pluginConfig, exec);
		
		M0RunConfig config = (M0RunConfig) exec.getConfig();
		
		result.put("artifactInstances", config.getInstanceMap());
		return result;
	}

	
}
