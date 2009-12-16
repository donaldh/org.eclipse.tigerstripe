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
package org.eclipse.tigerstripe.workbench.internal.core.generation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.plugins.PluginLogger;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IGlobalSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginManager;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginReport;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.GlobalSettingsProperty;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog.LogLevel;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class M0Generator {

	private M0RunConfig config = null;

	public M0Generator(M0RunConfig config) {
		this.config = config;
		
		
	}

	protected ITigerstripeModelProject getProject() {
		return config.getTargetProject();
	}

	public PluginRunStatus[] run(IProgressMonitor monitor)
			throws TigerstripeException, GenerationException {
		List<PluginRunStatus> result = new ArrayList<PluginRunStatus>();
		monitor.beginTask("Generating.", config.getPluginConfigs().length);

		Collection<PluginReport> reports = new ArrayList<PluginReport>();

		IPluginConfig[] configs = config.getPluginConfigs();
		
		
		for (IPluginConfig pConfig : configs) {
			monitor.subTask(pConfig.getPluginId());
			if (pConfig.isEnabled()) {
				// this is the case where there is no Facet whatsoever
				internalPluginLoop(pConfig, result, reports, monitor);
			}
			monitor.worked(1);
		}
		monitor.done();
		return result.toArray(new PluginRunStatus[result.size()]);
	}

	private void internalPluginLoop(IPluginConfig ref,
			List<PluginRunStatus> result, Collection<PluginReport> reports,
			IProgressMonitor monitor) throws TigerstripeException {
		
		PluginConfig pRef = (PluginConfig) ref;
		ITigerstripeModelProject project = config.getTargetProject();
		PluginLogger.setUpForRun( pRef, config);

		PluginRunStatus pluginResult = new PluginRunStatus( pRef, project,
				config, project.getActiveFacet());
		try {
			monitor.worked(1);
			monitor.setTaskName("Running: " + pRef.getLabel());
			pRef.resolve(); // Bug #741. Need to resolve the ref in
			// case the underlying body changed.
			// TODO Capture the list of generated stuff
			config.setMonitor(monitor);
			pRef.trigger(config);
			if (!ref.validationFailed()) {
				result.add(pluginResult);
			}
			PluginReport rep = pRef.getReport();
			if (rep != null)
				reports.add(rep);

			monitor.worked(1);
		} catch (TigerstripeException e) {
			String failureMessage = "An error was detected while triggering '"
					+ pRef.getLabel() + "' plugin. Generation may be incomplete.";
			if (!"".equals(e.getMessage())) {
				failureMessage = e.getMessage()
						+ ". Generation may be incomplete.";
			}

			IStatus error = new Status(IStatus.ERROR, BasePlugin.getPluginId(),
					failureMessage, e);
			pluginResult.add(error);
			result.add(pluginResult);
			if (e.getException() != null) {
				PluginLogger.log(LogLevel.ERROR, failureMessage, e
						.getException());
			} else {
				PluginLogger.log(LogLevel.ERROR, failureMessage, e);
			}
		} catch (Exception e) {
			String failureMessage = "An error was detected while triggering '"
					+ pRef.getLabel() + "' plugin. Generation may be incomplete.";
			IStatus error = new Status(IStatus.ERROR, BasePlugin.getPluginId(),
					failureMessage, e);
			pluginResult.add(error);
			result.add(pluginResult);
			PluginLogger.log(LogLevel.ERROR, failureMessage, e);
		} finally {
			PluginLogger.tearDown();
		}

	}
}
