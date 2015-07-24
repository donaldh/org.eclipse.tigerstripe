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
import org.eclipse.swt.widgets.Display;
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
 * nmehrega: Fixed Bug 331457 - [Headless] Project import does not work in
 * headless mode
 * 
 */
public class Tigerstripe implements IApplication {

    private static final String DELIMITER = "=";

    private static final String GENERATION_PROJECT_ARG = "GENERATION_PROJECT";

    private static final String IMPORT_PROJECT_ARG = "PROJECT_IMPORT";

    private List<String> projects;

    private String generationProject;

    public Object start(IApplicationContext context) throws Exception {

        long start = System.currentTimeMillis();

        System.out.println("Starting Tigerstripe...");
        printTigerstipeVersionInfo();
        setPluginParams(context);
        
        try {
            PostInstallActions.init();
            printProfile();

            System.out.println("Importing projects...");
            initializeWorkspace();
            
            File projectFile = new File(generationProject);
            ITigerstripeModelProject project = (ITigerstripeModelProject) TigerstripeCore
                    .findProject(projectFile.toURI());

            System.out.println("Validating project...");
            validateProject(project);
            
            System.out.println("Running generators...");
            generateTigerstripeOutput(project);
            
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

        if (projects == null || projects.isEmpty())
            return;

        importProjectsToWorkspace(projects);

        System.out.println("Generation project: " + generationProject);
    }

    private void validateProject(ITigerstripeModelProject project)
            throws TigerstripeException {

        final IProject iProject = (IProject) project.getAdapter(IProject.class);
        if (iProject == null) {
            throw new TigerstripeException(
                    "Failed to adapt project as org.eclipse.core.resources.IProject, not able to run validation checks.");
        }

        final StringBuffer errorMsg = new StringBuffer();
        IWorkspaceRunnable checkForErrorsRunnable = new IWorkspaceRunnable() {

            public void run(IProgressMonitor monitor) throws CoreException {

                IMarker[] markers = iProject.findMarkers(IMarker.PROBLEM, true,
                        IResource.DEPTH_INFINITE);
                for (int i = 0; i < markers.length; i++) {
                    if (IMarker.SEVERITY_ERROR == markers[i].getAttribute(
                            IMarker.SEVERITY, IMarker.SEVERITY_INFO)) {
                        String message = (String) markers[i]
                                .getAttribute(IMarker.MESSAGE);
                        if (message
                                .contains("Plugin execution not covered by lifecycle configuration")) {
                            continue;
                        }
                        errorMsg.append("\n - ")
                                .append(markers[i].getResource()
                                        .getProjectRelativePath().toString())
                                .append(": ").append(message);
                    }
                }
            }
        };

        try {
            ResourcesPlugin.getWorkspace().run(checkForErrorsRunnable,
                    new NullProgressMonitor());
        } catch (Exception e) {
            throw new TigerstripeException("Errors during project validation. "
                    + e.getMessage(), e);
        }

        if (errorMsg.length() > 0) {
            throw new TigerstripeException(
                    "Unable to perform generation. Project ["
                            + iProject.getName() + "] contains errors: "
                            + errorMsg.toString());
        }
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

        IWorkspaceRunnable op = new ImportProjectsRunnable(projects);
        try {
            ResourcesPlugin.getWorkspace().run(op, null);
        } catch (CoreException e) {
            throw new TigerstripeException(
                    "Importing projects to workspace has failed.  "
                            + e.getMessage(), e);
        }
    }

    private boolean isStringValid(String text) {
        return (text != null && text.trim().length() > 0);
    }

    private void generateTigerstripeOutput(ITigerstripeModelProject project)
            throws TigerstripeException {
        IM1RunConfig config = (IM1RunConfig) RunConfig.newGenerationConfig(
                project, RunConfig.M1);
        PluginRunStatus[] statuses = project.generate(config, null);
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
            throw new TigerstripeException(
                    "Generation failed. Check the following generators for errors: ["
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

            final List<IProject> importedProjects = new ArrayList<IProject>();
            for (String projectPath : projects) {
                if (isStringValid(projectPath)) {
                    if (projectPath.contains("\\"))
                        projectPath = projectPath.replaceAll("\\\\", "/");

                    if (projectPath.endsWith("/"))
                        projectPath = projectPath.substring(0,
                                projectPath.length() - 1);

                    File projectMetaFile = new File(projectPath + "/.project");
                    if (!projectMetaFile.exists()) {
                        throw new CoreException(new Status(IStatus.ERROR,
                                "headless_plugin", projectMetaFile.toString()
                                        + " does not exist!!"));
                    }

                    ProjectRecord projectRecord = new ProjectRecord(new File(
                            projectPath + "/.project"));
                    String projectName = projectPath.substring(projectPath
                            .lastIndexOf("/") + 1);

                    IProject project = workspace.getRoot().getProject(
                            projectName);
                    if (!project.exists()) {
                        try {
                            URI locationURI = projectRecord.description
                                    .getLocationURI();
                            File importSource = new File(locationURI);
                            List<?> filesToImport = FileSystemStructureProvider.INSTANCE
                                    .getChildren(importSource);
                            ImportOperation operation = new ImportOperation(
                                    project.getFullPath(), importSource,
                                    FileSystemStructureProvider.INSTANCE, this,
                                    filesToImport);
                            operation.setOverwriteResources(true);
                            operation.setCreateContainerStructure(false);
                            operation.run(monitor);

                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    importedProjects.add(project);
                }
            }

            workspace.getRoot().refreshLocal(IResource.DEPTH_INFINITE, monitor);

            for (final IProject project : importedProjects) {
                Display.getDefault().syncExec(new Runnable() {

                    public void run() {
                        try {
                            Object adapted = ((IAdaptable) project)
                                    .getAdapter(ITigerstripeModelProject.class);
                            if (adapted != null) {
                                ((ITigerstripeModelProject) adapted)
                                        .getArtifactManagerSession()
                                        .refreshAll(false, monitor);
                            } else {
                                System.out
                                        .println("Failed to process project as Tigerstripe Project, validation will not be run.");
                            }
                            project.build(IncrementalProjectBuilder.FULL_BUILD,
                                    monitor);
                        } catch (Exception e) {
                            System.out
                                    .println("An error was thrown while building the project:"
                                            + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });

            }
        }
    }

}
