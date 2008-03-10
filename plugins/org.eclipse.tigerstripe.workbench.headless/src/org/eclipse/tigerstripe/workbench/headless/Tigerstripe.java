/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.headless;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.generation.IRunConfig;
import org.eclipse.tigerstripe.workbench.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfileSession;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.osgi.framework.Constants;

public class Tigerstripe implements IApplication {

	private static final String DELIMITER = "=";

	private static final String ARG_PROFILE = "profile";

	private static final String ARG_PROJECT = "project";

	private Map<String, String> pluginArgs;

	@Override
	@SuppressWarnings("unchecked")
	public Object start(IApplicationContext context) throws Exception {

		printTigerstipeVersionInfo();
		setPluginParams(context);
		try {
			activateTigerstripeProfile();
			generateTigerstripeOutput();
		} catch (TigerstripeException e) {
			e.printStackTrace();
			return new Integer(1);
		}
		return EXIT_OK;
	}

	private void printTigerstipeVersionInfo() {

		System.out.println(TigerstripeCore.getRuntimeDetails()
				.getBaseBundleValue(Constants.BUNDLE_NAME)
				+ " (v"
				+ TigerstripeCore.getRuntimeDetails().getBaseBundleValue(
						Constants.BUNDLE_VERSION) + ")");
	}

	private void setPluginParams(IApplicationContext context) {

		String[] split = new String[2];
		pluginArgs = new HashMap<String, String>();
		String[] cmdLineArgs = (String[]) context.getArguments().get(
				IApplicationContext.APPLICATION_ARGS);
		for (String arg : cmdLineArgs) {
			split = arg.split(DELIMITER);
			pluginArgs.put(split[0], split[1]);
		}
	}

	private void activateTigerstripeProfile() throws TigerstripeException {

		IWorkbenchProfile activeProfile;
		IWorkbenchProfileSession profileSession = TigerstripeCore
				.getWorkbenchProfileSession();
		try {
			activeProfile = profileSession.getWorkbenchProfileFor(pluginArgs
					.get(ARG_PROFILE));
			profileSession.saveAsActiveProfile(activeProfile);
			profileSession.reloadActiveProfile();
		} catch (TigerstripeException e) {
			profileSession.rollbackActiveProfile();
			throw e;
		}
		System.out.println("Active Profile: "
				+ profileSession.getActiveProfile().getName());
	}

	private void generateTigerstripeOutput() throws TigerstripeException {

		importProjectToWorkspace();

		File projectFile = new File(pluginArgs.get(ARG_PROJECT));

		ITigerstripeModelProject project = (ITigerstripeModelProject) TigerstripeCore
				.findProject(projectFile.toURI());

		IRunConfig config = project.makeDefaultRunConfig();
		PluginRunStatus[] status = project.generate(config, null);

		// TODO - come back to this and handle status correctly throw TigerstripeException, etc
		if (status.length != 0) {
			for (PluginRunStatus pluginRunStatus : status) {
				pluginRunStatus.getMessage();
			}
		}
	}

	private boolean importProjectToWorkspace() {

		IWorkspaceRunnable op = new IWorkspaceRunnable() {

			@Override
			public void run(IProgressMonitor monitor) throws CoreException {

				ProjectRecord projectRecord = new ProjectRecord(new File(
						pluginArgs.get(ARG_PROJECT) + File.separator
								+ ".project"));

				String projectName = projectRecord.getProjectName();
				final IWorkspace workspace = ResourcesPlugin.getWorkspace();
				final IProject project = workspace.getRoot().getProject(
						projectName);

				try {
					project.create(projectRecord.description, null);
					project.open(IResource.BACKGROUND_REFRESH, null);
					project.refreshLocal(IResource.DEPTH_INFINITE, null);
				} catch (CoreException e) {
					throw e;
				}
			}

		};

		// run the new project creation operation
		try {
			ResourcesPlugin.getWorkspace().run(op, null);
		} catch (CoreException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void stop() {
		System.out.println("Stopping");
	}

}
