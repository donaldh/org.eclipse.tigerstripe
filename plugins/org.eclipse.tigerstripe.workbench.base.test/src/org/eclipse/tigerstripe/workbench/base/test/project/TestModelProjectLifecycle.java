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

import junit.framework.TestCase;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.WorkingCopyException;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;

public class TestModelProjectLifecycle extends TestCase {

	public void testGetWorkingCopy() throws TigerstripeException {
		IProjectDetails projectDetails = TigerstripeCore.makeProjectDetails();
		projectDetails.setName("testGetWorkingCopy");

		ITigerstripeProject project = (ITigerstripeProject) TigerstripeCore
				.createProject(projectDetails, null, ITigerstripeProject.class,
						null, new NullProgressMonitor());
		assertNotNull(project);

		ITigerstripeProject workingCopy = (ITigerstripeProject) project
				.makeWorkingCopy(null);

		assertNotNull(workingCopy);
		assertTrue(project != workingCopy);
		assertTrue(workingCopy.isWorkingCopy());

		IProjectDetails detailsCopy = workingCopy.getProjectDetails();
		assertTrue(detailsCopy != projectDetails);
		assertTrue(detailsCopy.getName().equals(projectDetails.getName()));

		project.delete(true, null);
	}

	public void testSetProjectDetails() throws TigerstripeException {
		IProjectDetails projectDetails = TigerstripeCore.makeProjectDetails();
		projectDetails.setName("testSetOnOriginal");

		ITigerstripeProject project = (ITigerstripeProject) TigerstripeCore
				.createProject(projectDetails, null, ITigerstripeProject.class,
						null, new NullProgressMonitor());
		assertNotNull(project);

		IProjectDetails details = project.getProjectDetails();
		details.setName("changed");

		// make sure we actually got a working object for that field
		assertTrue(project.getProjectDetails().getName().equals(
				"testSetOnOriginal"));

		try {
			project.setProjectDetails(details);
			fail("Shouldn't allow to set project details on original.");
		} catch (WorkingCopyException e) {
			// let's try on a working copy now
			ITigerstripeProject workingCopy = (ITigerstripeProject) project
					.makeWorkingCopy(null);
			try {
				workingCopy.setProjectDetails(details);
			} catch (WorkingCopyException ee) {
				fail("Set on working copy shouldn't fail.");
			}

			workingCopy.commit(null);

			IProjectDetails finalDetails = project.getProjectDetails();
			assertTrue(finalDetails.getName().equals("changed"));
		}
	}

	public void testOriginalChangedCallback() throws TigerstripeException {

	}
}
