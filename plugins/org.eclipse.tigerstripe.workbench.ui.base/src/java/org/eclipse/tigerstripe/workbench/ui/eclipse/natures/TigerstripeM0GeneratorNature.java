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
package org.eclipse.tigerstripe.workbench.ui.eclipse.natures;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;

public class TigerstripeM0GeneratorNature implements IProjectNature {

	private IProject project;

	public TigerstripeM0GeneratorNature() {
		super();
	}

	public void configure() throws CoreException {
		System.out.println("TigerstripeM0GeneratorNature.configure not implemented!");
//		PluggablePluginProjectAuditor.addBuilderToProject(project);
//		new Job("M0 Generator Project Audit") {
//			@Override
//			protected IStatus run(IProgressMonitor monitor) {
//				try {
//					project.build(IncrementalProjectBuilder.FULL_BUILD,
//							PluggablePluginProjectAuditor.BUILDER_ID, null,
//							monitor);
//				} catch (CoreException e) {
//					EclipsePlugin.log(e);
//				}
//				return org.eclipse.core.runtime.Status.OK_STATUS;
//			}
//		}.schedule();
	}

	public void deconfigure() throws CoreException {
		System.out.println("TigerstripeM0GeneratorNature.deconfigure not implemented!");
//		PluggablePluginProjectAuditor.removeBuilderFromProject(project);
	}

	public IProject getProject() {
		return this.project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

	/**
	 * Tests if a given project has the TigerstripeProjectNature
	 * 
	 * 
	 * @param project
	 * @return true if the given project has the TigerstripeProjectNature
	 * @throws CoreException
	 */
	public static boolean hasNature(IProject project) throws CoreException {

		if (!project.isAccessible()) // can't determine nature on a closed
			// project
			return true;

		return project
				.isNatureEnabled(TigerstripePluginConstants.M0Generator_NATURE_ID);

	}
}
