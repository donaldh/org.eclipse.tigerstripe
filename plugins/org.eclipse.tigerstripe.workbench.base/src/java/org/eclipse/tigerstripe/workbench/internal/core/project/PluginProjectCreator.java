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

import java.util.Map;

import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;

public class PluginProjectCreator extends BaseProjectCreator implements
		IProjectCreator {

	@Override
	@SuppressWarnings("unchecked")
	public IWorkspaceRunnable getRunnable(final IProjectDetails projectDetails,
			final IPath path, final Map properties)
			throws TigerstripeException {

		IWorkspaceRunnable runnable = new IWorkspaceRunnable() {

			@Override
			public void run(IProgressMonitor monitor) throws CoreException {

				// Creates a base Eclipse project
				createBaseProject(projectDetails, path, monitor);
			}

		};

		return runnable;
	}

	public void assertValidProperties(Map<String, Object> properties)
			throws TigerstripeException {
		super.assertValidProperties(properties);
	}
}
