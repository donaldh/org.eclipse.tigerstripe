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
package org.eclipse.tigerstripe.workbench.base.test.adapt;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

import junit.framework.TestCase;

public class TestAdapters extends TestCase {

	private ITigerstripeModelProject project;
	private final static String PROJECTNAME = "TestAdapters";

	@Override
	protected void setUp() throws Exception {
		IProjectDetails projectDetails = TigerstripeCore.makeProjectDetails();
		projectDetails.setName(PROJECTNAME);
		project = (ITigerstripeModelProject) TigerstripeCore.createProject(
				projectDetails, null, ITigerstripeModelProject.class, null,
				null);
	}

	@Override
	protected void tearDown() throws Exception {
		if (project != null && project.exists())
			project.delete(true, null);
	}

	public void testProjectAdapters() throws Exception {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource res = root.findMember(PROJECTNAME);

		ITigerstripeModelProject mp = (ITigerstripeModelProject) res
				.getAdapter(ITigerstripeModelProject.class);
		assertNotNull(mp);

		IJavaProject jProject = JavaCore.create((IProject) res);
		mp = (ITigerstripeModelProject) jProject
				.getAdapter(ITigerstripeModelProject.class);
		assertNotNull(mp);
	}
}
