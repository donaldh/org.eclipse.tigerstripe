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
package org.eclipse.tigerstripe.metamodel.test.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;

public class TestHelper {

	public static IJavaProject createJavaProject(String projectName)
			throws Exception {
		IProject project = org.eclipse.tigerstripe.test_common.TestHelper
				.createProject(projectName);

		IProjectDescription desc = project.getDescription();
		String[] natureIdOrig = desc.getNatureIds();
		List<String> natureIdNew = new ArrayList<String>();
		natureIdNew.addAll(Arrays.asList(natureIdOrig));
		natureIdNew.add(JavaCore.NATURE_ID);

		desc.setNatureIds(natureIdNew.toArray(new String[natureIdNew.size()]));
		project.setDescription(desc, null);

		IFolder srcFolder = project.getFolder("src");
		srcFolder.create(true, true, null);
		IFolder binFolder = project.getFolder("bin");
		binFolder.create(true, true, null);

		IJavaProject jProj = JavaCore.create(project);

		// set the build path
		IClasspathEntry[] buildPath = {
				JavaCore.newSourceEntry(project.getFolder("src").getFullPath()),
				JavaRuntime.getDefaultJREContainerEntry() };

		jProj.setRawClasspath(buildPath, project.getFullPath().append("bin"),
				null);

		return jProj;
	}

	public static void tearDown(IJavaProject jProject) throws Exception {
		if (jProject != null)
			jProject.getProject().delete(true, null);
	}
}
