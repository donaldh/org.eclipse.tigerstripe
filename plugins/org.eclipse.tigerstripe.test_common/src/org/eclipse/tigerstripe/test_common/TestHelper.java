/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.test_common;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;

public class TestHelper {

	public static IProject createProject(String projectName) throws Exception {
		// Create an empty project for this
		IProject project = null;
		IProjectDescription desc = ResourcesPlugin.getWorkspace()
				.newProjectDescription(projectName);
		project = ResourcesPlugin.getWorkspace().getRoot().getProject(
				projectName);
		project.create(desc, null);
		project.open(null);
		return project;
	}
	
	public static void tearDownProject(IProject project) throws Exception {
		if (project != null)
			project.delete(true, null);
	}
}
