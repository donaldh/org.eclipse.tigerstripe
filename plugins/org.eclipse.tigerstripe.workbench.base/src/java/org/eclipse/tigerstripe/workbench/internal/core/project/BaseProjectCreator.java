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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;

public abstract class BaseProjectCreator implements IProjectCreator {

	protected IProject projectHandle;

	protected void createBaseProject(IProjectDetails details, IPath location,
			IProgressMonitor monitor) throws OperationCanceledException,
			CoreException {

		String projectName = details.getName();

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IProjectDescription description = workspace
				.newProjectDescription(projectName);

		description.setLocation(location);

		projectHandle = ResourcesPlugin.getWorkspace().getRoot().getProject(
				projectName);
		projectHandle.create(description, monitor);

		if (monitor.isCanceled())
			throw new OperationCanceledException();

		projectHandle.open(monitor);
	}

	/**
	 * Adds the JavaNature and the given nature
	 * 
	 * @param targetTSNature
	 * @throws CoreException
	 */
	protected void addTSandJavaNature(String targetTSNature)
			throws CoreException {
		IProjectDescription description = projectHandle.getDescription();
		List<String> newIds = new ArrayList<String>();
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

		description.setNatureIds((String[]) newIds.toArray(new String[newIds
				.size()]));

		try {
		projectHandle.setDescription(description, null);
		} catch ( CoreException e ) {
			e.printStackTrace();
		}
	}

	/**
	 * Asserts that the minimum information is present in the properties so the
	 * base project creation can proceed.
	 * 
	 * @param properties
	 * @throws TigerstripeException
	 */
	public void assertValidProperties(Map<String, Object> properties)
			throws TigerstripeException {
	}
}
