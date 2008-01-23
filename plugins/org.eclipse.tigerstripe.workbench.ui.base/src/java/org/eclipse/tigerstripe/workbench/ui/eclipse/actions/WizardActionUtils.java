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
package org.eclipse.tigerstripe.workbench.ui.eclipse.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.api.IPluginReference;
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.api.project.IAdvancedProperties;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.core.generation.RunConfig;
import org.eclipse.tigerstripe.core.model.ArtifactManager;
import org.eclipse.tigerstripe.core.plugin.PluginRef;
import org.eclipse.tigerstripe.core.plugin.PluginReport;
import org.eclipse.tigerstripe.core.plugin.base.ReportModel;
import org.eclipse.tigerstripe.core.plugin.base.ReportRunner;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;
import org.eclipse.tigerstripe.workbench.ui.eclipse.utils.TigerstripeProgressMonitor;

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
	public static IStatus[] triggerGenerate(ITigerstripeProject handle,
			IProgressMonitor monitor, RunConfig config)
			throws TigerstripeException, InterruptedException {
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		List statuses = new ArrayList();
		Collection<PluginReport> reports = new ArrayList<PluginReport>();

		try {

			if (handle == null)
				throw new TigerstripeException("Invalid Tigerstripe Project");

			IPluginReference[] plugins = handle.getPluginReferences();
			String projectName = handle.getProjectDetails().getName();

			handle.getArtifactManagerSession().refresh(
					new TigerstripeProgressMonitor(monitor));
			((ArtifactManagerSessionImpl) handle.getArtifactManagerSession())
					.setLockForGeneration(true);

			for (Iterator iter = Arrays.asList(plugins).iterator(); iter
					.hasNext();) {
				PluginRef ref = (PluginRef) iter.next();
				if (ref.getCategory() == IPluginReference.GENERATE_CATEGORY
						&& ref.isEnabled()) {
					try {
						monitor.worked(1);
						monitor.setTaskName("Running: " + ref.getLabel());

						ref.trigger();
						PluginReport rep = ref.getReport();
						reports.add(ref.getReport());

						monitor.worked(1);
					} catch (TigerstripeException e) {
						Status status = new Status(
								IStatus.ERROR,
								TigerstripePluginConstants.PLUGIN_ID,
								222,
								"An error was detected while triggering '"
										+ ref.getLabel()
										+ "' plugin. Generation maybe incomplete.",
								e);
						EclipsePlugin.logErrorStatus(
								"Tigerstripe Generation Error Detected.",
								status);
						statuses.add(status);
					} catch (Exception e) {
						Status status = new Status(
								IStatus.ERROR,
								TigerstripePluginConstants.PLUGIN_ID,
								222,
								"An unknown error was detected while triggering '"
										+ ref.getLabel()
										+ "' plugin. Generation maybe incomplete.",
								e);
						EclipsePlugin
								.logErrorStatus(
										"Unexpected Tigerstripe Generation Error Detected.",
										status);
						statuses.add(status);
					}
				}
			}

			((ArtifactManagerSessionImpl) handle.getArtifactManagerSession())
					.setLockForGeneration(false);

			try {
				TigerstripeProjectHandle tsHandle = (TigerstripeProjectHandle) handle;
				ReportModel model = new ReportModel(tsHandle.getTSProject());

				// handle.getAdvancedProperty(IAdvancedProperties.PROP_GENERATION_GenerateReport).toString();
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
					TigerstripePluginConstants.PLUGIN_ID,
					222,
					"An error was detected while generating a Tigerstripe project. Generation maybe incomplete.",
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
