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
import java.net.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.WorkingCopyException;
import org.eclipse.tigerstripe.workbench.internal.WorkingCopyManager;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.core.project.ProjectDetails;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.GeneratorProjectDescriptor;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.PluggablePluginProject;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactBasedTemplateRule;
import org.eclipse.tigerstripe.workbench.plugins.IRule;
import org.eclipse.tigerstripe.workbench.plugins.ITemplateBasedRule;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripePluginProject;

/**
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class TigerstripePluginProjectHandle extends GeneratorProjectHandle
		implements ITigerstripePluginProject {

	private PluggablePluginProject descriptor;

	public TigerstripePluginProjectHandle(URI projectURI) {
		super(projectURI);
	}

	public GeneratorProjectDescriptor getDescriptor()
			throws TigerstripeException {
		if (descriptor == null) {
			File baseDir = new File(this.projectContainerURI);
			if (baseDir.isDirectory()) {
				descriptor = new PluggablePluginProject(baseDir);
			} else
				throw new TigerstripeException("Invalid project "
						+ baseDir.toString());
		}

		descriptor.reload(false);
		return this.descriptor;
	}

	public IProjectDetails getProjectDetails() throws TigerstripeException {
		return getDescriptor().getProjectDetails();
	}

	@Override
	public void setProjectDetails(IProjectDetails projectDetails)
			throws WorkingCopyException, TigerstripeException {
		assertSet();
		getDescriptor().setProjectDetails((ProjectDetails) projectDetails);
	}

	// =========================================

	public void addGlobalRule(IRule rule) throws TigerstripeException {
		assertSet();
		((PluggablePluginProject) getDescriptor()).addGlobalRule(rule);
	}

	public void addGlobalRules(IRule[] rules) throws TigerstripeException {
		for (IRule rule : rules) {
			addGlobalRule(rule);
		}
	}

	public IRule[] getGlobalRules() throws TigerstripeException {
		return ((PluggablePluginProject) getDescriptor()).getGlobalRules();
	}

	public void removeGlobalRule(IRule rule) throws TigerstripeException {
		assertSet();
		((PluggablePluginProject) getDescriptor()).removeGlobalRule(rule);
	}

	public void removeGlobalRules(IRule[] rules) throws TigerstripeException {
		assertSet();
		((PluggablePluginProject) getDescriptor()).removeGlobalRules(rules);
	}

	@SuppressWarnings("unchecked")
	public <T extends IRule> Class<T>[] getSupportedPluginRules() {
		try {
			return ((PluggablePluginProject) getDescriptor())
					.getSupportedPluginRules();
		} catch (TigerstripeException e) {
			return new Class[0];
		}
	}

	public String[] getSupportedPluginRuleLabels() {
		try {
			return ((PluggablePluginProject) getDescriptor())
					.getSupportedPluginRuleLabels();
		} catch (TigerstripeException e) {
			return new String[0];
		}
	}

	public <T extends IRule> IRule makeRule(Class<T> ruleType)
			throws TigerstripeException {
		return ((PluggablePluginProject) getDescriptor()).makeRule(ruleType);
	}

	// =========================================

	public void addArtifactRule(ITemplateBasedRule rule)
			throws TigerstripeException {
		assertSet();
		((PluggablePluginProject) getDescriptor()).addArtifactRule(rule);
	}

	public void addArtifactRules(ITemplateBasedRule[] rules)
			throws TigerstripeException {
		for (ITemplateBasedRule rule : rules) {
			addArtifactRule(rule);
		}
	}

	public ITemplateBasedRule[] getArtifactRules() throws TigerstripeException {
		return ((PluggablePluginProject) getDescriptor()).getArtifactRules();
	}

	public void removeArtifactRule(ITemplateBasedRule rule)
			throws TigerstripeException {
		assertSet();
		((PluggablePluginProject) getDescriptor()).removeArtifactRule(rule);
	}

	public void removeArtifactRules(ITemplateBasedRule[] rules)
			throws TigerstripeException {
		assertSet();
		((PluggablePluginProject) getDescriptor()).removeArtifactRules(rules);
	}

	@SuppressWarnings("unchecked")
	public <T extends IArtifactBasedTemplateRule> Class<T>[] getSupportedPluginArtifactRules() {
		try {
			return ((PluggablePluginProject) getDescriptor())
					.getSupportedPluginArtifactRules();
		} catch (TigerstripeException e) {
			return new Class[0];
		}
	}

	public String[] getSupportedPluginArtifactRuleLabels() {
		try {
			return ((PluggablePluginProject) getDescriptor())
					.getSupportedPluginArtifactRuleLabels();
		} catch (TigerstripeException e) {
			return new String[0];
		}
	}

	public IProjectDetails getIProjectDetails() throws TigerstripeException {

		return getProjectDetails();
	}

	public String getDescriptorFilename() {
		return ITigerstripeConstants.PLUGIN_DESCRIPTOR;
	}

	@Override
	protected WorkingCopyManager doCreateCopy(IProgressMonitor monitor)
			throws TigerstripeException {
		TigerstripePluginProjectHandle copy = new TigerstripePluginProjectHandle(
				getURI());
		return copy;
	}
}
