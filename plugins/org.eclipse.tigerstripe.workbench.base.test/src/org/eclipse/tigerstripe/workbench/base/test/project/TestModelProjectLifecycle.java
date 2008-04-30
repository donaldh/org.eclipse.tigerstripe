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
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TestModelProjectLifecycle extends TestCase {

	public void testGetWorkingCopy() throws TigerstripeException {
		IProjectDetails projectDetails = TigerstripeCore.makeProjectDetails();
		projectDetails.setName("testGetWorkingCopy");

		ITigerstripeModelProject project = (ITigerstripeModelProject) TigerstripeCore
				.createProject(projectDetails, null, ITigerstripeModelProject.class,
						null, new NullProgressMonitor());
		assertNotNull(project);

		ITigerstripeModelProject workingCopy = (ITigerstripeModelProject) project
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

		ITigerstripeModelProject project = (ITigerstripeModelProject) TigerstripeCore
				.createProject(projectDetails, null, ITigerstripeModelProject.class,
						null, new NullProgressMonitor());
		assertNotNull(project);

		assertTrue(!project.isWorkingCopy());
		
		try {
			project.getProjectDetails().setName("changed");
			fail("Shouldn't allow to set project details on original.");
		} catch (WorkingCopyException e) {
			// let's try on a working copy now
			ITigerstripeModelProject workingCopy = (ITigerstripeModelProject) project
					.makeWorkingCopy(null);
			try {
				workingCopy.getProjectDetails().setName("NowChanged");
			} catch (WorkingCopyException ee) {
				fail("Set on working copy shouldn't fail.");
			}

			workingCopy.commit(null);

			IProjectDetails finalDetails = project.getProjectDetails();
			assertTrue(finalDetails.getName().equals("NowChanged"));
		}
	}

	public void testOriginalChangedCallback() throws TigerstripeException {

	}
}
