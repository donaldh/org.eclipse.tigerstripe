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
package org.eclipse.tigerstripe.workbench.ui.internal.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.generation.M1RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginReport;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.base.ReportModel;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.base.ReportRunner;
import org.eclipse.tigerstripe.workbench.plugins.IPluginReport;
import org.eclipse.tigerstripe.workbench.project.IAdvancedProperties;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;

;

public class WizardActionUtils {

	/**
	 * A forced Generate on the given project. Errors in the project audit are
	 * ignored.
	 * 
	 * @param handle
	 * @param monitor
	 * @throws TigerstripeException
	 */
	public static IStatus[] triggerGenerate(ITigerstripeModelProject handle,
			IProgressMonitor monitor, M1RunConfig config)
			throws TigerstripeException, InterruptedException {
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		List statuses = new ArrayList();
		Collection<PluginReport> reports = new ArrayList<PluginReport>();

		try {

			if (handle == null)
				throw new TigerstripeException("Invalid Tigerstripe Project");

			IPluginConfig[] plugins = handle.getPluginConfigs();
			String projectName = handle.getName();

			handle.getArtifactManagerSession().refresh(monitor);
			((ArtifactManagerSessionImpl) handle.getArtifactManagerSession())
					.setLockForGeneration(true);
			try {
				for (Iterator iter = Arrays.asList(plugins).iterator(); iter
						.hasNext();) {
					PluginConfig ref = (PluginConfig) iter.next();
					if (ref.getCategory() == IPluginConfig.GENERATE_CATEGORY
							&& ref.isEnabled()) {
						try {
							monitor.worked(1);
							monitor.setTaskName("Running: " + ref.getLabel());

							ref.trigger();
							IPluginReport rep = ref.getReport();
							reports.add(ref.getReport());

							monitor.worked(1);
						} catch (TigerstripeException e) {
							Status status = new Status(
									IStatus.ERROR,
									EclipsePlugin.getPluginId(),
									222,
									"An error was detected while triggering '"
											+ ref.getLabel()
											+ "' generator. Generation may be incomplete.",
									e);
							EclipsePlugin.logErrorStatus(
									"Tigerstripe Generation Error Detected.",
									status);
							statuses.add(status);
						} catch (Exception e) {
							Status status = new Status(
									IStatus.ERROR,
									EclipsePlugin.getPluginId(),
									222,
									"An unknown error was detected while triggering '"
											+ ref.getLabel()
											+ "' generator. Generation may be incomplete.",
									e);
							EclipsePlugin
									.logErrorStatus(
											"Unexpected Tigerstripe Generation Error Detected.",
											status);
							statuses.add(status);
						}
					}
				}

			} finally {
				((ArtifactManagerSessionImpl) handle
						.getArtifactManagerSession())
						.setLockForGeneration(false);
			}

			try {
				TigerstripeProjectHandle tsHandle = (TigerstripeProjectHandle) handle;
				ReportModel model = new ReportModel(tsHandle.getTSProject());

				// handle.getAdvancedProperty(IAdvancedProperties.
				// PROP_GENERATION_GenerateReport).toString();
				//
				if ("true"
						.equalsIgnoreCase(handle
								.getAdvancedProperty(IAdvancedProperties.PROP_GENERATION_GenerateReport))) {
					ArtifactManagerSessionImpl session = (ArtifactManagerSessionImpl) handle
							.getArtifactManagerSession();
					ArtifactManager artifactMgr = session.getArtifactManager();
					ReportRunner runner = new ReportRunner();
					runner.generateReport(model, artifactMgr, reports, config);
				}

			} catch (Exception e) {
				TigerstripeRuntime.logErrorMessage("Exception detected", e);
			}

		} catch (TigerstripeException e) {
			Status status = new Status(
					IStatus.ERROR,
					EclipsePlugin.getPluginId(),
					222,
					"An error was detected while generating a Tigerstripe project. Generation may be incomplete.",
					e);
			EclipsePlugin.logErrorStatus(
					"Tigerstripe Generation Error Detected.", status);
			statuses.add(status);

		}

		if (monitor.isCanceled())
			throw new InterruptedException();

		IStatus[] result = new IStatus[statuses.size()];
		if (result.length != 0)
			return (IStatus[]) statuses.toArray(result);
		return result;
	}
}
