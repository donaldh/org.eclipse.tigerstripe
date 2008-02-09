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
package org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable;

import java.util.ArrayList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.ArtifactBasedPPluginRule;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.CopyRule;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.rules.SimplePPluginRule;
import org.eclipse.tigerstripe.workbench.plugins.IPluginRuleExecutor;
import org.eclipse.tigerstripe.workbench.plugins.IRunRule;
import org.eclipse.tigerstripe.workbench.plugins.ITemplateRunRule;

/**
 * For each run of a PluggablePlugin, a PluginRuleExecutor is instantiated. It
 * takes every rule and runs them one by one.
 * 
 * Each rule will have a report, and can be enabled/disabled
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class PluginRuleExecutor implements IPluginRuleExecutor {

	private PluggablePlugin plugin;

	private PluggablePluginConfig pluginConfig;

	private ArrayList<RuleReport> reports;

	private RunConfig config;

	public PluginRuleExecutor(PluggablePlugin plugin,
			PluggablePluginConfig pluginConfig, RunConfig config) {
		this.plugin = plugin;
		this.pluginConfig = pluginConfig;
		this.config = config;
	}

	public PluggablePlugin getPlugin() {
		return this.plugin;
	}

	public RunConfig getConfig() {
		return this.config;
	}

	public void trigger() throws TigerstripeException {
		this.reports = new ArrayList<RuleReport>();

		TigerstripeRuntime.logTraceMessage(" Triggering "
				+ plugin.getPProject().getProjectDetails().getName());
		long startTime = System.currentTimeMillis();

		int counter = 0;
		// take care of the global rules first
		IProgressMonitor monitor = config.getMonitor();
		IRunRule[] globalRules = plugin.getPProject().getGlobalRules();

		monitor.beginTask("Running global rules", globalRules.length);
		for (IRunRule rule : globalRules) {
			monitor.subTask(rule.getName());
			if (rule.isEnabled()) {
				counter++;
				rule.trigger(pluginConfig, this);
				if (rule instanceof SimplePPluginRule) {
					SimplePPluginRule aRule = (SimplePPluginRule) rule;
					RuleReport subReport = aRule.getReport();
					this.reports.add(subReport);
				} else if (rule instanceof CopyRule) {
					CopyRule cRule = (CopyRule) rule;
					RuleReport subReport = cRule.getReport();
					this.reports.add(subReport);
				}
			}
			monitor.worked(1);
		}
		monitor.done();
		TigerstripeRuntime.logTraceMessage("     Ran global rules (" + counter
				+ "/" + globalRules.length + ") in "
				+ (System.currentTimeMillis() - startTime));

		long startTime2 = System.currentTimeMillis();
		counter = 0;
		// Then trigger all artifact-based rules
		ITemplateRunRule[] artifactRules = plugin.getPProject()
				.getArtifactRules();

		monitor.beginTask("Running artifact rules", artifactRules.length);
		for (ITemplateRunRule rule : artifactRules) {
			monitor.subTask(rule.getName());
			if (rule.isEnabled()) {
				counter++;
				rule.trigger(pluginConfig, this);
				if (rule instanceof ArtifactBasedPPluginRule) {
					ArtifactBasedPPluginRule aRule = (ArtifactBasedPPluginRule) rule;
					RuleReport subReport = aRule.getReport();
					this.reports.add(subReport);
				}
			}
			monitor.worked(1);
		}
		monitor.done();
		TigerstripeRuntime.logTraceMessage("     Ran artifact rules ("
				+ counter + "/" + artifactRules.length + ") in "
				+ (System.currentTimeMillis() - startTime2));
	}

	public ArrayList<RuleReport> getReports() {
		return this.reports;
	}
}
