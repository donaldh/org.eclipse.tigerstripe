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
package org.eclipse.tigerstripe.workbench.internal.core.project;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.builder.BuilderConstants;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.project.pluggable.PluggablePluginProject;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;

public class PluginProjectCreator extends BaseProjectCreator implements
		IProjectCreator {

	public static IClasspathEntry[] REQUIRED_ENTRIES = {
			JavaCore.newVariableEntry(new Path(
					ITigerstripeConstants.EQUINOX_COMMON), null, null),
			JavaCore.newVariableEntry(new Path(
					ITigerstripeConstants.EXTERNALAPI_LIB), null, null) };

	@SuppressWarnings("unchecked")
	public IWorkspaceRunnable getRunnable(final String projectName,
			final IProjectDetails projectDetails, final IPath path,
			final Map properties) throws TigerstripeException {

		IWorkspaceRunnable runnable = new IWorkspaceRunnable() {

			public void run(IProgressMonitor monitor) throws CoreException {

				// Creates a base Eclipse project
				createBaseProject(projectName, path, monitor);
				// Add the Appropriate Natures
				addTSandJavaNature(BuilderConstants.PLUGINPROJECT_NATURE_ID);

				// And create the content and descriptor
				try {
					createProjectContent(projectDetails, monitor);
				} catch (TigerstripeException e) {
					IStatus status = new Status(IStatus.ERROR,
							BasePlugin.PLUGIN_ID,
							"Error while creating project content:"
									+ e.getMessage(), e);
					throw new CoreException(status);
				} catch (IOException e) {
					IStatus status = new Status(IStatus.ERROR,
							BasePlugin.PLUGIN_ID,
							"Error while creating project content:"
									+ e.getMessage(), e);
					throw new CoreException(status);
				}
			}
		};
		return runnable;
	}

	public void createProjectContent(IProjectDetails projectDetails,
			IProgressMonitor monitor) throws TigerstripeException,
			CoreException, IOException {
		// first create the corresponding java project handle
		IJavaProject newJavaProject = JavaCore.create(projectHandle);

		// Create the src folder
		IFolder folder = projectHandle.getFolder("src");
		IFolder binFolder = projectHandle.getFolder("bin");
		IFolder templateFolder = projectHandle.getFolder("templates");
		try {
			folder.create(true, true, null);
			binFolder.create(true, true, null);
			templateFolder.create(true, true, null);

			// set the build path
			List<IClasspathEntry> buildPathList = new ArrayList<IClasspathEntry>();
			buildPathList.add(JavaCore.newSourceEntry(projectHandle.getFolder(
					"src").getFullPath()));
			buildPathList.add(JavaRuntime.getDefaultJREContainerEntry());
			for (IClasspathEntry entry : REQUIRED_ENTRIES) {
				buildPathList.add(entry);
			}
			IClasspathEntry[] buildPath = buildPathList
					.toArray(new IClasspathEntry[buildPathList.size()]);

			newJavaProject.setRawClasspath(buildPath, projectHandle
					.getFullPath().append("bin"), null);

			createDefaultDescriptor(projectHandle, projectDetails,
					PluggablePluginProject.DEFAULT_FILENAME, monitor);

			newJavaProject.getProject().refreshLocal(IResource.DEPTH_INFINITE,
					monitor);
		} catch (CoreException e) {
			TigerstripeRuntime.logErrorMessage("CoreException detected", e);
		}
	}

	/**
	 * We will initialize file contents with a sample text.
	 * 
	 * @param pageProperties
	 *            - the properties gathered through the wizard
	 */
	@Override
	protected InputStream openContentStream(IProjectDetails projectDetails)
			throws TigerstripeException {
		byte[] bytes = null;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				buffer));

        File f = new File(projectHandle.getLocationURI());
        PluggablePluginProject projectDescriptor = new PluggablePluginProject(f);
        try {
        	projectDescriptor.setProjectDetails((ProjectDetails) projectDetails);
        	projectDescriptor.write(writer);
        } finally {
        	projectDescriptor.dispose();
        }
		bytes = buffer.toByteArray();
		return new ByteArrayInputStream(bytes);
	}

	public void assertValidProperties(Map<String, Object> properties)
			throws TigerstripeException {
		super.assertValidProperties(properties);
	}
}
