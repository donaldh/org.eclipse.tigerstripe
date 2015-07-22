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
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
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
 * nmehrega: Fixed Bug 331457 - [Headless] Project import does not work in
 * headless mode
 *
 */
public class Tigerstripe implements IApplication, IResourceChangeListener {

    private static final String DELIMITER = "=";

    private static final String GENERATION_PROJECT_ARG = "GENERATION_PROJECT";

    private static final String IMPORT_PROJECT_ARG = "PROJECT_IMPORT";

    private List<String> projects;

    private String generationProject;

    private static String postBuildLock = "LOCK";

    public Object start(IApplicationContext context) throws Exception {

        long start = System.currentTimeMillis();
        printTigerstipeVersionInfo();
        setPluginParams(context);
        try {

            PostInstallActions.init();
            printProfile();
            initializeWorkspace();

            File projectFile = new File(generationProject);
            ITigerstripeModelProject project = (ITigerstripeModelProject) TigerstripeCore
                    .findProject(projectFile.toURI());
            validateProject(project);
            generateTigerstripeOutput(project);
        } catch (Exception e) {
            System.err.println(e.getMessage());
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
        for (String project : projects) {
            System.out.println("Imported " + project + " into workspace.");
        }
        System.out.println("Generation project: " + generationProject);
    }

    private void validateProject(ITigerstripeModelProject project)
            throws TigerstripeException {
        try {
            IProject iProject = (IProject) project.getAdapter(IProject.class);
            if (iProject != null) {
                StringBuffer errorMsg = new StringBuffer();
                IMarker[] markers = iProject.findMarkers(IMarker.PROBLEM, true,
                        IResource.DEPTH_INFINITE);
                for (int i = 0; i < markers.length; i++) {
                    if (IMarker.SEVERITY_ERROR == markers[i].getAttribute(
                            IMarker.SEVERITY, IMarker.SEVERITY_INFO)) {
                        errorMsg.append("\n - ")
                                .append(markers[i].getResource()
                                        .getProjectRelativePath().toString())
                                .append(": ")
                                .append(markers[i].getAttribute(
                                        IMarker.MESSAGE, ""));
                    }
                }
                if (errorMsg.length() > 0) {
                    throw new TigerstripeException(
                            "Unable to perform generation. Project ["
                                    + iProject.getName()
                                    + "] contains errors: "
                                    + errorMsg.toString());
                }
            }
        } catch (CoreException e) {
            throw new TigerstripeException("Errors during project validation. "
                    + e.getMessage(), e);
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

        ResourcesPlugin.getWorkspace().addResourceChangeListener(this,
                IResourceChangeEvent.POST_BUILD);

        IWorkspaceRunnable op = new WorkspaceRunnable(projects);

        // run the new project creation operation
        try {
            ResourcesPlugin.getWorkspace().run(op, null);
        } catch (CoreException e) {
            throw new TigerstripeException(
                    "Importing projects to workspace has failed.  "
                            + e.getMessage(), e);
        }

        // NM: Wait until we get a build before proceeding. This also makes sure
        // all POST_CHANGE resource change listeners are processed
        synchronized (postBuildLock) {
            try {
                postBuildLock.wait(5000);
            } catch (InterruptedException e) {
                // Ignore
            }
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

    class WorkspaceRunnable implements IWorkspaceRunnable, IOverwriteQuery {

        private List<String> projects = null;

        public WorkspaceRunnable(List<String> projects) {
            this.projects = projects;
        }

        public String queryOverwrite(String pathString) {
            return IOverwriteQuery.YES;
        }

        public void run(IProgressMonitor monitor) throws CoreException {
            final IWorkspace workspace = ResourcesPlugin.getWorkspace();
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
                }
            }
            workspace.getRoot().refreshLocal(IResource.DEPTH_INFINITE, null);
        }

    }

    public void resourceChanged(IResourceChangeEvent event) {

        if (event.getType() == IResourceChangeEvent.POST_BUILD) {
            synchronized (postBuildLock) {

                try {
                    postBuildLock.notify();
                } catch (Exception e) {
                    // Ignore
                }
            }
        }

    }

}
