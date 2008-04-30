/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial API and Implementation
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.builder.natures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.builder.BuilderConstants;
import org.eclipse.ui.PlatformUI;

/**
 * Upon open-sourcing of Tigerstripe the namespace of the code changed as well
 * as a number of the IDs of Tigerstripe extensions.
 * 
 * In particular the nature ID changed for Tigerstripe Projects. To ensure
 * smooth transition between commercial and open-source versions, the
 * nature/builders need to be updated when the old nature/builders are found on
 * a project.
 * 
 * @author erdillon
 * 
 */
public class ProjectMigrationUtils {

	public static void handlePluginProjectMigration(IProject project) {

		if (project == null || !project.exists() || !project.isOpen())
			return;

		// Bug 221695
		// Making sure the Equinox .jar is in the classpath for this project.
		final IJavaProject jProject = JavaCore.create(project);
		if (jProject != null) {
			boolean found = false;
			try {
				for (IClasspathEntry entry : jProject.getRawClasspath()) {
					if (entry.getPath().toString().equals(
							ITigerstripeConstants.EQUINOX_COMMON)) {
						found = true;
						break;
					}
				}

				if (!found) {
					Job projectMigrate = new Job(
							"Migrate Tigerstripe Generator Plugin Classpath: "
									+ project.getName()) {
						@Override
						protected IStatus run(IProgressMonitor monitor) {
							try {
								List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
								entries.addAll(Arrays.asList(jProject
										.getRawClasspath()));
								entries.add(JavaCore.newVariableEntry(new Path(
										ITigerstripeConstants.EQUINOX_COMMON),
										null, null));
								jProject.setRawClasspath(entries
										.toArray(new IClasspathEntry[entries
												.size()]), null);
							} catch (JavaModelException e) {
								IStatus s = new Status(IStatus.ERROR,
										BasePlugin.PLUGIN_ID, e.getMessage(), e);
								return s;
							}
							return Status.OK_STATUS;
						}
					};

					projectMigrate.setRule(project.getWorkspace().getRoot());
					projectMigrate.schedule();
				}
			} catch (JavaModelException e) {
				BasePlugin.log(e);
			}
		}
	}

	public static void handleProjectMigration(IProject project)
			throws CoreException {

		// Bug 221299
		if (project == null || !project.isOpen())
			return;

		// Get the description
		final IProjectDescription description = project.getDescription();

		// Look for Builder
		int index = -1;
		ICommand[] cmds = description.getBuildSpec();
		for (int j = 0; j < cmds.length; j++) {
			for (String OLD_ID : BuilderConstants.OLDPROJECT_BUILDER_IDs) {
				if (cmds[j].getBuilderName().equals(OLD_ID)) {
					index = j;
					break;
				}
			}
			for (String OLD_ID : BuilderConstants.OLDPLUGINPROJECT_BUILDER_IDs) {
				if (cmds[j].getBuilderName().equals(OLD_ID)) {
					index = j;
					break;
				}
			}
		}

		// then look for natures
		String[] natures = description.getNatureIds();
		boolean updateOldNature = false;
		for (int i = 0; i < natures.length; i++) {
			for (String OLD_NATURE_ID : BuilderConstants.OLDPROJECT_NATURE_IDs) {
				if (OLD_NATURE_ID.equals(natures[i])) {
					natures[i] = BuilderConstants.PROJECT_NATURE_ID;
					updateOldNature = true;
				}
			}
			for (String OLD_NATURE_ID : BuilderConstants.OLDPLUGINPROJECT_NATURE_IDs) {
				if (OLD_NATURE_ID.equals(natures[i])) {
					natures[i] = BuilderConstants.PLUGINPROJECT_NATURE_ID;
					updateOldNature = true;
				}
			}
		}

		if (updateOldNature)
			description.setNatureIds(natures);

		if (index == -1 && !updateOldNature)
			return;

		// Remove builder from project
		List<ICommand> newCmds = new ArrayList<ICommand>();
		newCmds.addAll(Arrays.asList(cmds));
		newCmds.remove(index);
		description.setBuildSpec((ICommand[]) newCmds
				.toArray(new ICommand[newCmds.size()]));

		final IProject fProject = project;

		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				try {
					fProject.setDescription(description,
							new NullProgressMonitor());
				} catch (CoreException e) {
					BasePlugin.log(e);
				}
			}
		});
	}
}
