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
package org.eclipse.tigerstripe.workbench.base.test.project;

import java.util.Collection;

import junit.framework.TestCase;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TestProjectManagement extends TestCase {

	public void testFindProject() {
		fail("Not yet implemented");
	}

	public void testCreateModelProject() throws TigerstripeException,
			CoreException {
		IProjectDetails projectDetails = TigerstripeCore.makeProjectDetails();
		projectDetails.setName("testCreateModelProject");

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IAbstractTigerstripeProject project = TigerstripeCore.createProject(
				projectDetails, null, ITigerstripeModelProject.class, null,
				new NullProgressMonitor());
		assertNotNull(project);

		IResource proj = workspace.getRoot().findMember(
				"testCreateModelProject");
		assertNotNull(project);
		proj.delete(true, new NullProgressMonitor());
	}

	/**
	 * Tests that a removed project is actually completely removed. This
	 * exercise the cache of the TigerstripeProjectFactory
	 * 
	 * @throws TigerstripeException
	 * @throws CoreException
	 */
	public void testRemoveModelProject() throws TigerstripeException,
			CoreException {
		IProjectDetails projectDetails = TigerstripeCore.makeProjectDetails();
		projectDetails.setName("testRemoveModelProject");

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IAbstractTigerstripeProject project = TigerstripeCore.createProject(
				projectDetails, null, ITigerstripeModelProject.class, null,
				new NullProgressMonitor());
		assertNotNull(project);

		IResource proj = workspace.getRoot().findMember(
				"testRemoveModelProject");
		assertNotNull(project);
		proj.delete(true, new NullProgressMonitor());

		IAbstractTigerstripeProject tsProject = TigerstripeCore
				.findProject(proj.getFullPath());
		assertFalse(tsProject.exists());
	}

	public void testMakeProjectDetails() {
		IProjectDetails details = TigerstripeCore.makeProjectDetails();
		assertNotNull(details);
	}

	@SuppressWarnings("unchecked")
	public void testGetSupportedProjectTypes() {
		Collection<Class> supportedTypes = TigerstripeCore
				.getSupportedProjectTypes();
		assertTrue(supportedTypes.size() != 0);
	}

}
