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
import org.eclipse.tigerstripe.workbench.plugins.IArtifactRule;
import org.eclipse.tigerstripe.workbench.plugins.IModelRule;
import org.eclipse.tigerstripe.workbench.plugins.IRule;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM1GeneratorProject;

/**
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class TigerstripePluginProjectHandle extends GeneratorProjectHandle
		implements ITigerstripeM1GeneratorProject {

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

	public void addArtifactRule(IArtifactRule rule)
			throws TigerstripeException {
		assertSet();
		((PluggablePluginProject) getDescriptor()).addArtifactRule(rule);
	}

	public void addArtifactRules(IArtifactRule[] rules)
			throws TigerstripeException {
		for (IArtifactRule rule : rules) {
			addArtifactRule(rule);
		}
	}

	public IArtifactRule[] getArtifactRules() throws TigerstripeException {
		return ((PluggablePluginProject) getDescriptor()).getArtifactRules();
	}

	public void removeArtifactRule(IRule rule)
			throws TigerstripeException {
		assertSet();
		((PluggablePluginProject) getDescriptor()).removeArtifactRule(rule);
	}

	public void removeArtifactRules(IRule[] rules)
			throws TigerstripeException {
		assertSet();
		((PluggablePluginProject) getDescriptor()).removeArtifactRules(rules);
	}

	@SuppressWarnings("unchecked")
	public <T extends IArtifactRule> Class<T>[] getSupportedPluginArtifactRules() {
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

	// =========================================

	public void addModelRule(IModelRule rule)
			throws TigerstripeException {
		assertSet();
		((PluggablePluginProject) getDescriptor()).addModelRule(rule);
	}

	public void addModelRules(IModelRule[] rules)
			throws TigerstripeException {
		for (IModelRule rule : rules) {
			addModelRule(rule);
		}
	}

	public IModelRule[] getModelRules() throws TigerstripeException {
		return ((PluggablePluginProject) getDescriptor()).getModelRules();
	}

	public void removeModelRule(IRule rule)
			throws TigerstripeException {
		assertSet();
		((PluggablePluginProject) getDescriptor()).removeModelRule(rule);
	}

	public void removeModelRules(IRule[] rules)
			throws TigerstripeException {
		assertSet();
		((PluggablePluginProject) getDescriptor()).removeModelRules(rules);
	}

	@SuppressWarnings("unchecked")
	public <T extends IModelRule> Class<T>[] getSupportedPluginModelRules() {
		try {
			return ((PluggablePluginProject) getDescriptor())
					.getSupportedPluginModelRules();
		} catch (TigerstripeException e) {
			return new Class[0];
		}
	}

	public String[] getSupportedPluginModelRuleLabels() {
		try {
			return ((PluggablePluginProject) getDescriptor())
					.getSupportedPluginModelRuleLabels();
		} catch (TigerstripeException e) {
			return new String[0];
		}
	}
	
	//==========================
	
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
	
	@Override
	public void dispose() {
		super.dispose();
		if (descriptor != null) {
			descriptor.dispose();
		}
	}
	
}
