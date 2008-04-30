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
package org.eclipse.tigerstripe.workbench.ui.internal.wizards;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.core.project.TigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.project.NewProjectDetails;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.project.NewTigerstripeDescriptorGenerator;

/**
 * Helper class for basic Project Creation operations.
 * 
 * @author Eric Dillon
 * 
 */
public class NewProjectHelper {

	/***************************************************************************
	 * Creates a project resource given the project handle and description.
	 * 
	 * @param description
	 *            the project description to create a project resource for
	 * @param projectHandle
	 *            the project handle to create a project resource for
	 * @param monitor
	 *            the progress monitor to show visual progress with
	 * 
	 * @exception CoreException
	 *                if the operation fails
	 */
	public static void createProject(IProjectDescription description,
			IProject projectHandle, IProgressMonitor monitor)
			throws CoreException {
		try {
			monitor.beginTask("", 2000);

			projectHandle.create(description, new SubProgressMonitor(monitor,
					1000));

			if (monitor.isCanceled())
				throw new OperationCanceledException();

			projectHandle.open(new SubProgressMonitor(monitor, 1000));

		} finally {
			monitor.done();
		}
	}

	/**
	 * Adds a "targetTSNature" and "Java" nature to the given project handle to
	 * make it a Tigerstripe Project
	 * 
	 * 
	 * @param projectHandle
	 *            the projectHandle to act upon
	 * @param targetTSNature
	 *            the TS specific nature to add to the given project
	 */
	public static void addTSandJavaNature(IProject projectHandle,
			String targetTSNature) {
		IProjectDescription description;
		try {
			description = projectHandle.getDescription();
			List newIds = new ArrayList();
			newIds.addAll(Arrays.asList(description.getNatureIds()));

			// Add TS Nature
			int tsNatureIndex = newIds.indexOf(targetTSNature);
			if (tsNatureIndex == -1) {
				newIds.add(targetTSNature);
			}

			// Add Java Nature
			int index = newIds.indexOf(JavaCore.NATURE_ID);
			if (index == -1) {
				newIds.add(JavaCore.NATURE_ID);
			}

			description.setNatureIds((String[]) newIds
					.toArray(new String[newIds.size()]));

			projectHandle.setDescription(description, null);

		} catch (CoreException e) {
			EclipsePlugin.log(e);
		}
	}

	/**
	 * Creates the basic content for a TS model project
	 * 
	 * This includes the src&bin dirs, the necessary libraries and the
	 * descriptor
	 * 
	 * @param projectDetails
	 * @param projectHandle
	 * @param monitor
	 * @return
	 */
	public static IJavaProject createProjectContent(
			NewProjectDetails projectDetails, IProject projectHandle,
			IProgressMonitor monitor) throws CoreException {

		// first create the corresponding java project handle
		IJavaProject newJavaProject = JavaCore.create(projectHandle);

		// Create the src folder
		IFolder folder = projectHandle.getFolder("src");
		IFolder binFolder = projectHandle.getFolder("bin");

		folder.create(true, true, null);
		binFolder.create(true, true, null);

		// set the build path
		IClasspathEntry[] buildPath = {
				JavaCore.newSourceEntry(projectHandle.getFolder("src")
						.getFullPath()),
				JavaRuntime.getDefaultJREContainerEntry(),
				JavaCore.newVariableEntry(new Path(
						ITigerstripeConstants.PHANTOMLIB), null, null) };

		newJavaProject.setRawClasspath(buildPath, projectHandle.getFullPath()
				.append("bin"), null);

		createDefaultTigerstripeDescriptor(projectHandle, projectDetails,
				monitor);

		return newJavaProject;

	}

	private static void createDefaultTigerstripeDescriptor(
			IProject projectHandle, NewProjectDetails projectDetails,
			IProgressMonitor monitor) {

		final IFile file = projectHandle
				.getFile(TigerstripeProject.DEFAULT_FILENAME);
		try {
			InputStream stream = openContentStream(projectDetails);
			if (file.exists()) {
				file.setContents(stream, true, true, monitor);
			} else {
				file.create(stream, true, monitor);
			}
			stream.close();
		} catch (CoreException e) {
			EclipsePlugin.log(e);
		} catch (IOException e) {
			EclipsePlugin.log(e);
		}
	}

	/**
	 * We will initialize file contents with a sample text.
	 * 
	 * @param pageProperties -
	 *            the properties gathered through the wizard
	 */
	private static InputStream openContentStream(
			NewProjectDetails projectDetails) {

		// FIXME: should be using a ProjectDescriptor + save.
		byte[] bytes = null;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				buffer));

		NewTigerstripeDescriptorGenerator generator = new NewTigerstripeDescriptorGenerator(
				projectDetails, writer);
		generator.applyTemplate();

		bytes = buffer.toByteArray();
		return new ByteArrayInputStream(bytes);
	}

}
