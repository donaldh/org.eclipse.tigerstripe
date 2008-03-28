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
package org.eclipse.tigerstripe.workbench.ui.eclipse.natures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
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
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;
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
										EclipsePlugin.PLUGIN_ID,
										e.getMessage(), e);
								return s;
							}
							return Status.OK_STATUS;
						}
					};

					projectMigrate.setRule(project.getWorkspace().getRoot());
					projectMigrate.schedule();
				}
			} catch (JavaModelException e) {
				EclipsePlugin.log(e);
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
			if (cmds[j].getBuilderName().equals(
					TigerstripePluginConstants.OLDPROJECT_BUILDER_ID)) {
				index = j;
				break;
			} else if (cmds[j].getBuilderName().equals(
					TigerstripePluginConstants.OLDPLUGINPROJECT_BUILDER_ID)) {
				index = j;
				break;
			}
		}

		// then look for natures
		String[] natures = description.getNatureIds();
		boolean updateOldNature = false;
		for (int i = 0; i < natures.length; i++) {
			if (TigerstripePluginConstants.OLDPROJECT_NATURE_ID
					.equals(natures[i])) {
				natures[i] = TigerstripePluginConstants.PROJECT_NATURE_ID;
				updateOldNature = true;
			} else if (TigerstripePluginConstants.OLDPLUGINPROJECT_NATURE_ID
					.equals(natures[i])) {
				natures[i] = TigerstripePluginConstants.PLUGINPROJECT_NATURE_ID;
				updateOldNature = true;
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
					EclipsePlugin.log(e);
				}
			}
		});
	}

}
