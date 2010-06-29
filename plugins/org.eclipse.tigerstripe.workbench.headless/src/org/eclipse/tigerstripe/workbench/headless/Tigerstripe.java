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
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.generation.IM1RunConfig;
import org.eclipse.tigerstripe.workbench.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfileSession;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.osgi.framework.Constants;

public class Tigerstripe implements IApplication {

	private static final String DELIMITER = "=";

	private static final String GENERATION_PROJECT_ARG = "GENERATION_PROJECT";

	private static final String IMPORT_PROJECT_ARG = "PROJECT_IMPORT";

	private List<String> projects;

	private String generationProject;

	public Object start(IApplicationContext context) throws Exception {

		long start = System.currentTimeMillis();
		printTigerstipeVersionInfo();
		setPluginParams(context);
		try {
			printProfile();
			initializeWorkspace();
			generateTigerstripeOutput();
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
		long finish = System.currentTimeMillis();
		System.out.println("Generation complete. Took " + (finish - start)
				+ " milliseconds.");
		return EXIT_OK;
	}

	public static void printTigerstipeVersionInfo() {
		System.out.println(TigerstripeCore.getRuntimeDetails()
				.getBaseBundleValue(Constants.BUNDLE_NAME)
				+ " (v"
				+ TigerstripeCore.getRuntimeDetails().getBaseBundleValue(
						Constants.BUNDLE_VERSION) + ")");
	}

	private void setPluginParams(IApplicationContext context)
			throws TigerstripeException {
		projects = new ArrayList<String>();
		String[] split = new String[2];
		String[] cmdLineArgs = (String[]) context.getArguments().get(
				IApplicationContext.APPLICATION_ARGS);
		for (String arg : cmdLineArgs) {
			split = arg.split(DELIMITER);
			if (split[0].equals(IMPORT_PROJECT_ARG)) {
				projects.add(split[1]);
			}
			if (split[0].equals(GENERATION_PROJECT_ARG)) {
				generationProject = split[1];
			}
		}
		if (generationProject == null) {
			throw new TigerstripeException(
					"Must have the generation project defined.");
		}
	}

	private void initializeWorkspace() throws TigerstripeException {
		importProjectsToWorkspace(projects);
		for (String project : projects) {
			System.out.println("Imported " + project + " into workspace.");
		}
		System.out.println("Generation project: " + generationProject);
	}

	private void printProfile() {
		IWorkbenchProfileSession profileSession = TigerstripeCore
				.getWorkbenchProfileSession();
		System.out.println("Active Profile: "
				+ profileSession.getActiveProfile().getName() + " "
				+ profileSession.getActiveProfile().getVersion());
	}

	private void importProjectsToWorkspace(final List<String> projects)
			throws TigerstripeException {

		IWorkspaceRunnable op = new IWorkspaceRunnable() {

			public void run(IProgressMonitor monitor) throws CoreException {
				final IWorkspace workspace = ResourcesPlugin.getWorkspace();
				for (String projectName : projects) {
					ProjectRecord projectRecord = new ProjectRecord(new File(
							projectName + File.separator + ".project"));
					final IProject project = workspace.getRoot().getProject(
							projectName);
					if (!project.exists()) {
						try {
							project.create(projectRecord.description, null);
							project.open(IResource.BACKGROUND_REFRESH, null);
							// project.refreshLocal(IResource.DEPTH_INFINITE,
							// null);
						} catch (CoreException e) {
							throw e;
						}
					}
				}
				workspace.getRoot()
						.refreshLocal(IResource.DEPTH_INFINITE, null);
				// workspace.build(IncrementalProjectBuilder.FULL_BUILD,
				// monitor);
			}
		};

		// run the new project creation operation
		try {
			ResourcesPlugin.getWorkspace().run(op, null);
		} catch (CoreException e) {
			throw new TigerstripeException(
					"Importing projects to workspace is failed.", e);
		}
	}

	private void generateTigerstripeOutput() throws TigerstripeException {

		File projectFile = new File(generationProject);
		ITigerstripeModelProject project = (ITigerstripeModelProject) TigerstripeCore
				.findProject(projectFile.toURI());
		IM1RunConfig config = (IM1RunConfig) RunConfig.newGenerationConfig(
				project, RunConfig.M1);
		PluginRunStatus[] statuses = project.generate(config, null);
		boolean failed = false;
		if (statuses.length != 0) {
			for (PluginRunStatus pluginRunStatus : statuses) {
				System.out.println(pluginRunStatus);
				if (pluginRunStatus.getSeverity() == IStatus.ERROR) {
					failed = true;
				}
			}
		}
		if (failed)
			throw new TigerstripeException("Generation is failed.");
	}

	public void stop() {
		System.out.println("Stopping");
	}

}
