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

	@SuppressWarnings("unchecked")
	public Object start(IApplicationContext context) throws Exception {

		printTigerstipeVersionInfo();
		setPluginParams(context);
		try {
			printProfile();
			initializeWorkspace();
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

	private void setPluginParams(IApplicationContext context) throws TigerstripeException {
		projects = new ArrayList<String>();
		String[] split = new String[2];
		String[] cmdLineArgs = (String[]) context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
		for (String arg : cmdLineArgs) {
			split = arg.split(DELIMITER);
			if(split[0].equals(IMPORT_PROJECT_ARG)) {
				projects.add(split[1]);
			}
			if(split[0].equals(GENERATION_PROJECT_ARG)) {
				System.out.println("Generating " + split[0]);
				generationProject = split[1];
			}
		}
		if (generationProject == null) {
			throw new TigerstripeException("Must have the generation project defined.");
		}
	}

	private void initializeWorkspace() throws TigerstripeException {
		for (String project : projects) {
			importProjectToWorkspace(project);
			System.out.println("Imported " + project + " into workspace.");
			System.out.println("Generation project: " + generationProject);
		}	
	}
	
	private void printProfile() {
		IWorkbenchProfileSession profileSession = TigerstripeCore.getWorkbenchProfileSession();
		System.out.println("Active Profile: "
				+ profileSession.getActiveProfile().getName() + " "
				+ profileSession.getActiveProfile().getVersion());
	}

	private boolean importProjectToWorkspace(final String project) {

		IWorkspaceRunnable op = new IWorkspaceRunnable() {

			public void run(IProgressMonitor monitor) throws CoreException {

				ProjectRecord projectRecord;
				projectRecord = new ProjectRecord(new File(project + File.separator + ".project"));

				String projectName = projectRecord.getProjectName();
				final IWorkspace workspace = ResourcesPlugin.getWorkspace();
				final IProject project = workspace.getRoot().getProject(projectName);
				if (!project.exists()) {
					try {
						project.create(projectRecord.description, null);
						project.open(IResource.BACKGROUND_REFRESH, null);
						project.refreshLocal(IResource.DEPTH_INFINITE, null);
					} catch (CoreException e) {
						throw e;
					}
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

	private void generateTigerstripeOutput() throws TigerstripeException {

		File projectFile = new File(generationProject);
		ITigerstripeModelProject project = (ITigerstripeModelProject) TigerstripeCore
				.findProject(projectFile.toURI());
		IM1RunConfig config = (IM1RunConfig) RunConfig.newGenerationConfig(project, RunConfig.M1);
		PluginRunStatus[] status = project.generate(config, null);

		if (status.length != 0) {
			for (PluginRunStatus pluginRunStatus : status) {
				System.out.println(pluginRunStatus.getMessage());
			}
		}
	}
	
	public void stop() {
		System.out.println("Stopping");
	}

}
