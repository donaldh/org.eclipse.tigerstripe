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
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.generation.IM1RunConfig;
import org.eclipse.tigerstripe.workbench.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.internal.startup.PostInstallActions;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfileSession;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.osgi.framework.Constants;

/**
 * Main class responsible for headless Tigerstripe builds
 * 
 */
public class Tigerstripe implements IApplication {

	private static final String DELIMITER = "=";

	private static final String VALUE_SEPARATOR = ",";

	private static final String IMPORT_PROJECT_ARG = "PROJECT_IMPORT";

	private List<String> projects;

	private List<IProject> importedProjects;

	public Object start(IApplicationContext context) throws Exception {

		long start = System.currentTimeMillis();

		System.out.println("Starting Tigerstripe...");
		printTigerstipeVersionInfo();
		setPluginParams(context);

		try {
			PostInstallActions.init();
			printProfile();

			if (projects != null) {
				IWorkspaceRunnable op = new ImportProjectsRunnable(projects);
				ResourcesPlugin.getWorkspace().run(op, null);
			}

			for (IProject project : importedProjects) {
				System.out.println("Validating " + project.getName());
				validateProject(project);
				System.out.println("Running generation on " + project.getName());
				generateTigerstripeOutput(project);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}

		long finish = System.currentTimeMillis();
		System.out.println("Generation complete. Took " + (finish - start) + " milliseconds.");

		return EXIT_OK;
	}

	public static void printTigerstipeVersionInfo() {
		System.out.println(TigerstripeCore.getRuntimeDetails().getBaseBundleValue(Constants.BUNDLE_NAME) + " (v"
				+ TigerstripeCore.getRuntimeDetails().getBaseBundleValue(Constants.BUNDLE_VERSION) + ")");
	}

	private void setPluginParams(IApplicationContext context) throws TigerstripeException {
		projects = new ArrayList<String>();
		String[] split = new String[2];
		String[] cmdLineArgs = (String[]) context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
		for (String arg : cmdLineArgs) {
			split = arg.split(DELIMITER);
			String key = split[0];
			String[] values = split[1].split(VALUE_SEPARATOR);
			for (String value : values) {
				if (key.equals(IMPORT_PROJECT_ARG)) {
					projects.add(value);
				}
			}
		}
		if (projects.isEmpty()) {
			throw new TigerstripeException("Must have at least one generation project defined.");
		}
	}

	private void validateProject(final IProject project) throws Exception {

		final StringBuffer errorMsg = new StringBuffer();
		IWorkspaceRunnable checkForErrorsRunnable = new IWorkspaceRunnable() {

			public void run(IProgressMonitor monitor) throws CoreException {

				IMarker[] markers = project.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
				for (int i = 0; i < markers.length; i++) {
					if (IMarker.SEVERITY_ERROR == markers[i].getAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO)) {
						String message = (String) markers[i].getAttribute(IMarker.MESSAGE);
						if (message.contains("Plugin execution not covered by lifecycle configuration")) {
							continue;
						}
						errorMsg.append("\n - ").append(markers[i].getResource().getProjectRelativePath().toString())
								.append(": ").append(message);
					}
				}
			}
		};

		ResourcesPlugin.getWorkspace().run(checkForErrorsRunnable, new NullProgressMonitor());

		if (errorMsg.length() > 0) {
			throw new TigerstripeException("Unable to perform generation. Project [" + project.getName()
					+ "] contains errors: " + errorMsg.toString());
		}
	}

	private void printProfile() {
		IWorkbenchProfileSession profileSession = TigerstripeCore.getWorkbenchProfileSession();
		System.out.println("Active Profile: " + profileSession.getActiveProfile().getName() + " "
				+ profileSession.getActiveProfile().getVersion());
	}

	private boolean isStringValid(String text) {
		return (text != null && text.trim().length() > 0);
	}

	private void generateTigerstripeOutput(IProject project) throws TigerstripeException {

		ITigerstripeModelProject tsProject = (ITigerstripeModelProject) TigerstripeCore.findProject(project);
		IM1RunConfig config = (IM1RunConfig) RunConfig.newGenerationConfig(tsProject, RunConfig.M1);
		PluginRunStatus[] statuses = tsProject.generate(config, null);
		StringBuffer failedGenerators = new StringBuffer();
		if (statuses.length != 0) {
			for (PluginRunStatus pluginRunStatus : statuses) {
				System.out.println(pluginRunStatus);
				if (pluginRunStatus.getSeverity() == IStatus.ERROR) {
					if (failedGenerators.length() > 0) {
						failedGenerators.append(", ");
					}
					failedGenerators.append(pluginRunStatus.getPlugin());
				}
			}
		}
		if (failedGenerators.length() > 0) {
			throw new TigerstripeException("Generation failed. Check the following generators for errors: ["
					+ failedGenerators.toString() + "]");
		}
	}

	public void stop() {
		System.out.println("Stopping");
	}

	class ImportProjectsRunnable implements IWorkspaceRunnable, IOverwriteQuery {

		private List<String> projects = null;

		public ImportProjectsRunnable(List<String> projects) {
			this.projects = projects;
		}

		public String queryOverwrite(String pathString) {
			return IOverwriteQuery.YES;
		}

		public void run(final IProgressMonitor monitor) throws CoreException {

			final IWorkspace workspace = ResourcesPlugin.getWorkspace();

			importedProjects = new ArrayList<IProject>();
			for (String projectPath : projects) {
				if (isStringValid(projectPath)) {
					if (projectPath.contains("\\")) {
						projectPath = projectPath.replaceAll("\\\\", "/");
					}

					if (projectPath.endsWith("/")) {
						projectPath = projectPath.substring(0, projectPath.length() - 1);
					}
					System.out.println("Importing project: " + projectPath);

					String projectName = projectPath.substring(projectPath.lastIndexOf("/") + 1);

					File projectMetaFile = new File(projectPath + "/.project");
					if (!projectMetaFile.exists()) {
						try {
							System.out
									.println("Attempting to create default .project file for project: " + projectName);
							String content = FileUtils.readFileToString(new File("templates/project.xml"));
							content = content.replace("${project.name}", projectName);
							FileUtils.writeStringToFile(projectMetaFile, content);
						} catch (Exception e) {
							e.printStackTrace();
							throw new CoreException(new Status(IStatus.ERROR, "Tigerstripe", projectMetaFile.toString()
									+ " does not exist, and an error was thrown while trying to create a default. File is required and should be checked-in to your SCM."));
						}
					}

					ProjectRecord projectRecord = new ProjectRecord(new File(projectPath + "/.project"));

					IProject project = workspace.getRoot().getProject(projectName);
					if (!project.exists()) {
						try {
							URI locationURI = projectRecord.description.getLocationURI();
							File importSource = new File(locationURI);
							List<?> filesToImport = FileSystemStructureProvider.INSTANCE.getChildren(importSource);
							ImportOperation operation = new ImportOperation(project.getFullPath(), importSource,
									FileSystemStructureProvider.INSTANCE, this, filesToImport);
							operation.setOverwriteResources(true);
							operation.setCreateContainerStructure(false);
							operation.run(monitor);
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						IFile classpath = project.getFile(".classpath");
						if (!classpath.exists()) {
							try {
								FileUtils.copyFile(new File("templates/classpath.xml"),
										classpath.getLocation().toFile());
							} catch (Exception e) {
								System.err.println(
										"An error occurred trying to create default .classpath file for project: "
												+ project.getName() + ":");
								e.printStackTrace();
							}
						}
						
						// Remove maven build/nature before running headless,
						// wreaks
						// havoc.
						List<String> natures = new ArrayList<String>();
						for (String nature : project.getDescription().getNatureIds()) {
							if (!nature.equals("org.eclipse.m2e.core.maven2Nature")
									&& !nature.equals("org.maven.ide.eclipse.maven2Nature")) {
								natures.add(nature);
							}
						}
						String[] newNatures = new String[natures.size()];
						project.getDescription().setNatureIds(natures.toArray(newNatures));

						List<ICommand> builders = new ArrayList<ICommand>();
						for (ICommand build : project.getDescription().getBuildSpec()) {
							if (!build.getBuilderName().equals("org.eclipse.m2e.core.maven2Builder")
									&& !build.getBuilderName().equals("org.maven.ide.eclipse.maven2Builder")) {
								builders.add(build);
							}
						}
						ICommand[] build = new ICommand[builders.size()];
						project.getDescription().setBuildSpec(builders.toArray(build));
					}
					importedProjects.add(project);
				} else {
					System.err.print("Project path is not valid: " + projectPath);
				}
			}

			workspace.getRoot().refreshLocal(IResource.DEPTH_INFINITE, monitor);

			for (final IProject project : importedProjects) {
				Object adapted = null;
				try {
					adapted = ((IAdaptable) project).getAdapter(ITigerstripeModelProject.class);
					if (adapted != null) {
						((ITigerstripeModelProject) adapted).getArtifactManagerSession().refreshAll(false, monitor);
					} else {
						System.out.println(
								"Failed to process project as Tigerstripe Project, validation will not be run.");
					}
				} catch (Exception e) {
					throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
							"Failed to adapt imported project as a Tigerstripe Model project.", e));
				}

				project.build(IncrementalProjectBuilder.FULL_BUILD, monitor);
			}
		}
	}

}
