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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

import org.eclipse.core.resources.IFolder;
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
import org.eclipse.tigerstripe.workbench.internal.core.classpath.IReferencesConstants;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;

public class ModelProjectCreator extends BaseProjectCreator implements
		IProjectCreator {

	@SuppressWarnings("unchecked")
	public IWorkspaceRunnable getRunnable(final String projectName,
			final IProjectDetails projectDetails, final IPath location,
			final Map properties) throws TigerstripeException {

		IWorkspaceRunnable runnable = new IWorkspaceRunnable() {

			public void run(IProgressMonitor monitor) throws CoreException {

				// Creates a base Eclipse project
				createBaseProject(projectName, location, monitor);

				// Add the Appropriate Natures
				addTSandJavaNature(BuilderConstants.PROJECT_NATURE_ID);

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

	public void assertValidProperties(Map<String, Object> properties)
			throws TigerstripeException {
		super.assertValidProperties(properties);
	}

	public void createProjectContent(IProjectDetails projectDetails,
			IProgressMonitor monitor) throws TigerstripeException,
			CoreException, IOException {

		// first create the corresponding java project handle
		IJavaProject newJavaProject = JavaCore.create(projectHandle);

		// Create the src folder
		IFolder folder = projectHandle.getFolder("src");
		IFolder binFolder = projectHandle.getFolder("bin");

		folder.create(true, true, monitor);
		binFolder.create(true, true, null);

		// set the build path
		IClasspathEntry[] buildPath = {
				JavaCore.newSourceEntry(projectHandle.getFolder("src")
						.getFullPath()),
				JavaRuntime.getDefaultJREContainerEntry(),
				JavaCore.newVariableEntry(new Path(
						ITigerstripeConstants.PHANTOMLIB), null, null),
				JavaCore
						.newContainerEntry(IReferencesConstants.REFERENCES_CONTAINER_PATH) };

		newJavaProject.setRawClasspath(buildPath, projectHandle.getFullPath()
				.append("bin"), null);

		createDefaultDescriptor(projectHandle, projectDetails,
				TigerstripeProject.DEFAULT_FILENAME, monitor);
	}

	/**
	 * We will initialize file contents with a sample text.
	 * 
	 * @param pageProperties
	 *            - the properties gathered through the wizard
	 */
	protected InputStream openContentStream(IProjectDetails projectDetails)
			throws TigerstripeException {
		byte[] bytes = null;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				buffer));

		TigerstripeProject projectDescriptor = new TigerstripeProject(null);
		projectDescriptor.setProjectDetails((ProjectDetails) projectDetails);

		projectDescriptor.write(writer);

		bytes = buffer.toByteArray();
		return new ByteArrayInputStream(bytes);
	}

}
