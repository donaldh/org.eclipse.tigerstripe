/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     J. Strawn (Cisco Systems, Inc.) - Initial implementation
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.optional.buckminster.ant;

import org.apache.tools.ant.BuildException;
import org.eclipse.ant.core.Task;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.project.GeneratorDeploymentHelper;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM1GeneratorProject;

public class InstallGenerator extends Task {

	private String projectname;

	public void setProjectname(String projectname) {
		
		this.projectname = projectname;
	}

	public void execute() throws BuildException {

		final ITigerstripeM1GeneratorProject project = getM1GeneratorProject();
		final GeneratorDeploymentHelper helper = new GeneratorDeploymentHelper();

		IWorkspaceRunnable op = null;
		op = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) {

				try {
					if (helper.deploy(project, new NullProgressMonitor()).toOSString() == null) {
						throw new BuildException(getExceptionInformation(project));
					}
					System.out.println(project.getName() + " has been successfully deployed");
				} catch (TigerstripeException e) {
					throw new BuildException(e);
				}

			}

			private String getExceptionInformation(final ITigerstripeM1GeneratorProject project) throws TigerstripeException {

				return "Plugin " + project.getName() + "(" + project.getProjectDetails().getVersion()
						+ ") could not be deployed. See Error log for more details";

			}
		};

		try {
			
			ResourcesPlugin.getWorkspace().run(op, new NullProgressMonitor());
		} catch (CoreException e) {
			new BuildException(e);
		}
	}

	private ITigerstripeM1GeneratorProject getM1GeneratorProject() {

		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectname);
		if (!project.exists()) {
			throw new BuildException("The " + projectname + " Tigerstripe generator project does not exist in the workspace.");
		}

		IAbstractTigerstripeProject tsProject = (IAbstractTigerstripeProject) project.getAdapter(IAbstractTigerstripeProject.class);
		if (!(tsProject instanceof ITigerstripeM1GeneratorProject)) {
			throw new BuildException("The " + projectname + " project is not a Tigerstripe generator project.");
		}
		return (ITigerstripeM1GeneratorProject) tsProject;
	}

}
